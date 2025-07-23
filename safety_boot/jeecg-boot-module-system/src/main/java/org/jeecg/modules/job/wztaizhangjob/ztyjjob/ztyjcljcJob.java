package org.jeecg.modules.job.wztaizhangjob.ztyjjob;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import com.trtm.iot.yclsl.entity.Wzycljinchanggb;
import com.trtm.iot.yclsl.service.IWzycljinchanggbService;
import com.trtm.iot.ztwzjincslsjb.entity.Ztwzjincslsjb;
import com.trtm.iot.ztwzjincslsjb.service.IZtwzjincslsjbService;
import com.trtm.iot.ztwzjinmaterial.entity.Ztwzjinmaterial;
import com.trtm.iot.ztwzjinmaterial.service.IZtwzjinmaterialService;
import com.trtm.iot.ztwzphoto.entity.Ztwzphoto;
import com.trtm.iot.ztwzphoto.service.IZtwzphotoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.*;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * \* @author: zml
 * \* Date: 2022-08-17
 * \* Time: 13:08
 * \* Description: 滨淮压力机数据推送
 * \
 */
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class ztyjcljcJob implements Job {
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private IZtwzjincslsjbService ztwzjincslsjbService;
    @Autowired
    private IZtwzjinmaterialService ztwzjinmaterialService;

    @Autowired
    private IZtwzphotoService ztwzphotoService;
    @Autowired
    private IWzycljinchanggbService wzycljinchanggbService;

    private static final String accessId = "1v89j0f+TbRqCsjlAwjU";
    private static final String secretKey = "OxB7HrrZfaSdxFytrQ0CocLovjHQqE6QPnQSMMdo";

    private static final String baseUrl = "https://api.mctech.vip";

    private static final String pcrApi = "/mquantity/get-receive-weight-order-list?offset=%d&startId=%d&version=%d&limit=%d&orgId=%d";
    private static final String photoApi = "/mquantity/fs/accesses";

    private static Logger logger = LogManager.getLogger("logger");
    private static String outputFile = "project-construction-record.txt";


    private static Object lockObj = new Object();
    // 20线程
    private static int threadCount = 20;
    private static ExecutorService service = Executors.newFixedThreadPool(threadCount);

    private static OpenApiClient client = new OpenApiClient(baseUrl, accessId, secretKey);

    private static AtomicInteger totalCount = new AtomicInteger(0);

    private static final Semaphore sem = new Semaphore(threadCount);

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.ZTYJ_CLJC);//中铁一局，增量获取收料数据
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info("未获取到中铁一局，增量获取收料数据推送定时任务的配置信息" + DateUtils.now());
            return;
        }

        File f = new File(outputFile);
        if (f.exists()) {
            f.delete();
        }

//        list.add(1633827216528896L);//对接组织名称：杭州机场高铁站前3标1#钢筋加工场
//        list.add(1633827609351680L);//对接组织名称：杭州机场高铁站前3标拌和站
        List<Long> list = Arrays.asList(1633827609351680L);
        for (long id : list) {
            try {
                sem.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                logger.info("start project: " + id);
                getPcr(id);
            } finally {
                sem.release();
            }
        }
    }

    private void getPcr(long orgId) {
        int offset = ztwzjincslsjbService.count();
        QueryWrapper<Ztwzjincslsjb> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("max(id) id,version");
        Ztwzjincslsjb one = ztwzjincslsjbService.getOne(queryWrapper);
        long startId = 0;
        long version = 0;
        if (one != null){
            version = Long.parseLong(one.getVersion());
        }
        int pageSize = 20;
        String apiUrl = String.format(pcrApi, offset, startId, version, pageSize, orgId);
        try (RequestResult result = client.get(apiUrl)) {
            // 调用result.GetJsonObject方法可以使用强类型的实体，也可以使用Newtonsoft.json.dll中的JArray类型
            JSONArray arr = (JSONArray) result.getJsonObject();
            setrukedan(arr);
            System.out.println(arr);
            int count = arr.size();
            totalCount.addAndGet(count);
            WritePcrFile(arr);
            logger.info(String.format("get %d records on project %d", totalCount.get(), orgId));

            startId = arr.getJSONObject(arr.size() - 1).getLongValue("id");
            version = arr.getJSONObject(arr.size() - 1).getLongValue("version");
        } catch (Exception ex) {
            logger.info(String.format("call api error:  %s", ex.getMessage()), ex);
        }
    }

    //入库单
    private  void setrukedan(JSONArray arr) {
        for (Object a : arr) {
            Wzycljinchanggb wzycljinchanggb = new Wzycljinchanggb();
            wzycljinchanggb.setShebeibianhao("20240408");
            //更新主表数据
            JSONObject object = new JSONObject(a);
            System.out.println(object);
            setztwzjincslsjb(object,wzycljinchanggb);
            //更新从表数据
            cn.hutool.json.JSONArray material = (cn.hutool.json.JSONArray)object.get("material");
            JSONObject jsonObject = new JSONObject(material.get(0));
            System.out.println(jsonObject);
            setztwzjinmaterial(jsonObject,wzycljinchanggb);
            //更新图片数据
            cn.hutool.json.JSONArray material1 = (cn.hutool.json.JSONArray)object.get("photo");
            System.out.println(material1);
            for (Object b :material1){
                JSONObject object1 = new JSONObject(b);
                wzycljinchanggb = setztwzjinmaterialphoto(object1, wzycljinchanggb);
            }

            QueryWrapper<Wzycljinchanggb> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("JCGKPic",wzycljinchanggb.getJcgkpic());
            Wzycljinchanggb one = wzycljinchanggbService.getOne(queryWrapper);
            if (one == null){
                wzycljinchanggbService.save(wzycljinchanggb);
            }
        }
    }
    //更新主表数据
    private Wzycljinchanggb setztwzjincslsjb(JSONObject object,Wzycljinchanggb wzycljinchanggb){
        Ztwzjincslsjb ztwzjincslsjb = new Ztwzjincslsjb();
        Object id = object.get("id");
        ztwzjincslsjb.setFid(String.valueOf(id));
        Object maker = object.get("maker");
        ztwzjincslsjb.setMaker(String.valueOf(maker));
        Object remark = object.get("remark");
        ztwzjincslsjb.setRemark(String.valueOf(remark));
        Object auditor = object.get("auditor");
        ztwzjincslsjb.setAuditor(String.valueOf(auditor));
        Object version = object.get("version");
        ztwzjincslsjb.setVersion(String.valueOf(version));
        Object oriOrgName = object.get("oriOrgName");
        ztwzjincslsjb.setOriorgname(String.valueOf(oriOrgName));
        Object orgId = object.get("orgId");
        ztwzjincslsjb.setOrgid(String.valueOf(orgId));
        Object orderDate = object.get("orderDate");
        ztwzjincslsjb.setOrderdate(String.valueOf(orderDate));
        Object recordedDate = object.get("recordedDate");
        ztwzjincslsjb.setRecordeddate(String.valueOf(recordedDate));
        Object orderCode = object.get("orderCode");
        ztwzjincslsjb.setOrdercode(String.valueOf(orderCode));
        Object orderOrigin = object.get("orderOrigin");
        ztwzjincslsjb.setOrderorigin(Integer.valueOf(String.valueOf(orderOrigin)));
        Object serviceType = object.get("serviceType");
        ztwzjincslsjb.setServicetype(Integer.valueOf(String.valueOf(serviceType)));
        Object orderType = object.get("orderType");
        ztwzjincslsjb.setOrdertype(Integer.valueOf(String.valueOf(orderType)));
        Object makerDate = object.get("makerDate");
        ztwzjincslsjb.setMakerdate(String.valueOf(makerDate));
        Object printTimes = object.get("printTimes");
        ztwzjincslsjb.setPrinttimes(Integer.valueOf(String.valueOf(printTimes)));
        Object plateNumber = object.get("plateNumber");
        ztwzjincslsjb.setPlatenumber(String.valueOf(plateNumber));
        Object supplierId = object.get("supplierId");
        ztwzjincslsjb.setSupplierid((Long) supplierId);
        Object supplierName = object.get("supplierName");
        ztwzjincslsjb.setSuppliername(String.valueOf(supplierName));
        Object isRed = object.get("isRed");
        ztwzjincslsjb.setIsred(String.valueOf(isRed));
        Object isAudit = object.get("isAudit");
        ztwzjincslsjb.setIsaudit(String.valueOf(isAudit));
        Object auditDate = object.get("auditDate");
        ztwzjincslsjb.setAuditdate(String.valueOf(auditDate));
        Object oriOrgId = object.get("oriOrgId");
        ztwzjincslsjb.setOriorgid(String.valueOf(oriOrgId));
        Object oriOrderId = object.get("oriOrderId");
        ztwzjincslsjb.setOriorderid(String.valueOf(oriOrderId));
        Object creatorId = object.get("creatorId");
        ztwzjincslsjb.setCreatorid(String.valueOf(creatorId));
        Object creatorName = object.get("creatorName");
        ztwzjincslsjb.setCreatorname(String.valueOf(creatorName));
        Object createdAt = object.get("createdAt");
        ztwzjincslsjb.setCreatedat(String.valueOf(createdAt));
        Object modifierId = object.get("modifierId");
        ztwzjincslsjb.setModifierid(String.valueOf(modifierId));
        Object modifierName = object.get("modifierName");
        ztwzjincslsjb.setModifiername(String.valueOf(modifierName));
        Object updatedAt = object.get("updatedAt");
        ztwzjincslsjb.setUpdatedat(String.valueOf(updatedAt));
        Object isRemoved = object.get("isRemoved");
        ztwzjincslsjb.setIsremoved(String.valueOf(isRemoved));
        Object orderDataId = object.get("orderDataId");
        ztwzjincslsjb.setOrderdataid(String.valueOf(orderDataId));
        Object discernPlateNumber = object.get("discernPlateNumber");
        ztwzjincslsjb.setDiscernplatenumber(String.valueOf(discernPlateNumber));
        Object stockbinFullName = object.get("stockbinFullName");
        ztwzjincslsjb.setStockbinfullname(String.valueOf(stockbinFullName));
        Object stockbinName = object.get("stockbinName");
        ztwzjincslsjb.setStockbinname(String.valueOf(stockbinName));
        Object stockbinId = object.get("stockbinId");
        ztwzjincslsjb.setStockbinid(String.valueOf(stockbinId));
        Object oriStockbinId = object.get("oriStockbinId");
        ztwzjincslsjb.setOristockbinid(String.valueOf(oriStockbinId));
        Object enterTime = object.get("enterTime");
        ztwzjincslsjb.setEntertime(String.valueOf(enterTime));
        Object exitTime = object.get("exitTime");
        ztwzjincslsjb.setExittime(String.valueOf(exitTime));
        Object sortTime = object.get("sortTime");
        ztwzjincslsjb.setSorttime(String.valueOf(sortTime));
        Object isReturn = object.get("isReturn");
        ztwzjincslsjb.setIsreturn(String.valueOf(isReturn));
        Object isExit = object.get("isExit");
        ztwzjincslsjb.setIsexit(String.valueOf(isExit));
        Object isTare = object.get("isTare");
        ztwzjincslsjb.setIstare(String.valueOf(isTare));
        Object isMultiplication = object.get("isMultiplication");
        ztwzjincslsjb.setIsmultiplication(String.valueOf(isMultiplication));
        Object isUseOriNetQuantity = object.get("isUseOriNetQuantity");
        ztwzjincslsjb.setIsuseorinetquantity(String.valueOf(isUseOriNetQuantity));
        Object deductionRate = object.get("deductionRate");
        ztwzjincslsjb.setDeductionrate(String.valueOf(deductionRate));
        Object roughQuantity = object.get("roughQuantity");
        ztwzjincslsjb.setRoughquantity(String.valueOf(roughQuantity));
        Object tareQuantity = object.get("tareQuantity");
        ztwzjincslsjb.setTarequantity(String.valueOf(tareQuantity));
        Object deductRate = object.get("deductRate");
        ztwzjincslsjb.setDeductrate(String.valueOf(deductRate));
        Object deductQuantity = object.get("deductQuantity");
        ztwzjincslsjb.setDeductquantity(String.valueOf(deductQuantity));
        Object auxiliaryNetQuantity = object.get("auxiliaryNetQuantity");
        ztwzjincslsjb.setAuxiliarynetquantity(String.valueOf(auxiliaryNetQuantity));
        Object waybillWeight = object.get("waybillWeight");
        ztwzjincslsjb.setWaybillweight(String.valueOf(waybillWeight));
        Object orderBarCode = object.get("orderBarCode");
        ztwzjincslsjb.setOrderbarcode(String.valueOf(orderBarCode));
        Object weightType = object.get("weightType");
        ztwzjincslsjb.setWeighttype(String.valueOf(weightType));
        Object isAffirm = object.get("isAffirm");
        ztwzjincslsjb.setIsaffirm(String.valueOf(isAffirm));
        Object oriSupplierId = object.get("oriSupplierId");
        ztwzjincslsjb.setOrisupplierid(String.valueOf(oriSupplierId));
        Object orgName = object.get("orgName");
        ztwzjincslsjb.setOrgname(String.valueOf(orgName));
        Object realOrgId = object.get("realOrgId");
        ztwzjincslsjb.setRealorgid(String.valueOf(realOrgId));

        QueryWrapper<Ztwzjincslsjb> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("oriorderid",ztwzjincslsjb.getOriorderid());
        Ztwzjincslsjb one = ztwzjincslsjbService.getOne(queryWrapper);
        if (one != null){
            ztwzjincslsjb.setId(one.getId());
            ztwzjincslsjbService.updateById(ztwzjincslsjb);
        }else {
            ztwzjincslsjbService.save(ztwzjincslsjb);
        }
        wzycljinchanggb.setQianchepai(ztwzjincslsjb.getPlatenumber());
        wzycljinchanggb.setHouchepai(ztwzjincslsjb.getPlatenumber());
        wzycljinchanggb.setMaozhongt(ztwzjincslsjb.getRoughquantity());
        wzycljinchanggb.setPizhongt(ztwzjincslsjb.getTarequantity());
        wzycljinchanggb.setJingzhongt(ztwzjincslsjb.getAuxiliarynetquantity());
        wzycljinchanggb.setLcbianhao(ztwzjincslsjb.getStockbinfullname());
        return wzycljinchanggb;
    }
    //更新从表数据
    private Wzycljinchanggb setztwzjinmaterial(JSONObject object,Wzycljinchanggb wzycljinchanggb){
        Ztwzjinmaterial ztwzjinmaterial = new Ztwzjinmaterial();
        Object orgId = object.get("orgId");
        ztwzjinmaterial.setOrgid(String.valueOf(orgId));
        Object orderId = object.get("orderId");
        ztwzjinmaterial.setOrderid(String.valueOf(orderId));
        Object manufacturer = object.get("manufacturer");
        ztwzjinmaterial.setManufacturer(String.valueOf(manufacturer));
        Object remark = object.get("remark");
        ztwzjinmaterial.setRemark(String.valueOf(remark));
        Object version = object.get("version");
        ztwzjinmaterial.setVersion(String.valueOf(version));
        Object materialId = object.get("materialId");
        ztwzjinmaterial.setMaterialid(String.valueOf(materialId));
        Object materialCode = object.get("materialCode");
        ztwzjinmaterial.setMaterialcode(String.valueOf(materialCode));
        Object materialName = object.get("materialName");
        ztwzjinmaterial.setMaterialname(String.valueOf(materialName));
        Object materialModel = object.get("materialModel");
        ztwzjinmaterial.setMaterialmodel(String.valueOf(materialModel));
        Object materialUnit = object.get("materialUnit");
        ztwzjinmaterial.setMaterialunit(String.valueOf(materialUnit));
        Object auxiliaryUnit = object.get("auxiliaryUnit");
        ztwzjinmaterial.setAuxiliaryunit(String.valueOf(auxiliaryUnit));
        Object classId = object.get("classId");
        ztwzjinmaterial.setClassid(String.valueOf(classId));
        Object classFullId = object.get("classFullId");
        ztwzjinmaterial.setClassfullid(String.valueOf(classFullId));
        Object batchNo = object.get("batchNo");
        ztwzjinmaterial.setBatchno(String.valueOf(batchNo));
        Object testReportNo = object.get("testReportNo");
        ztwzjinmaterial.setTestreportno(String.valueOf(testReportNo));
        Object netQuantity = object.get("netQuantity");
        ztwzjinmaterial.setNetquantity(String.valueOf(netQuantity));
        Object roughQuantity = object.get("roughQuantity");
        ztwzjinmaterial.setRoughquantity(String.valueOf(roughQuantity));
        Object conversionRate = object.get("conversionRate");
        ztwzjinmaterial.setConversionrate(String.valueOf(conversionRate));
        Object deductRate = object.get("deductRate");
        ztwzjinmaterial.setDeductrate(String.valueOf(deductRate));
        Object deductQuantity = object.get("deductQuantity");
        ztwzjinmaterial.setDeductquantity(String.valueOf(deductQuantity));
        Object oriNetQuantity = object.get("oriNetQuantity");
        ztwzjinmaterial.setOrinetquantity(String.valueOf(oriNetQuantity));
        Object auxiliaryNetQuantity = object.get("auxiliaryNetQuantity");
        ztwzjinmaterial.setAuxiliarynetquantity(String.valueOf(auxiliaryNetQuantity));
        Object mainNetQuantity = object.get("mainNetQuantity");
        ztwzjinmaterial.setMainnetquantity(String.valueOf(mainNetQuantity));
        Object oriItemId = object.get("oriItemId");
        ztwzjinmaterial.setOriitemid(String.valueOf(oriItemId));
        Object oriOrderId = object.get("oriOrderId");
        ztwzjinmaterial.setOriorderid(String.valueOf(oriOrderId));
        Object createdAt = object.get("createdAt");
        ztwzjinmaterial.setCreatedat(String.valueOf(createdAt));
        Object isRemoved = object.get("isRemoved");
        ztwzjinmaterial.setIsremoved(String.valueOf(isRemoved));
        Object creatorId = object.get("creatorId");
        ztwzjinmaterial.setCreatorid(String.valueOf(creatorId));
        Object creatorName = object.get("creatorName");
        ztwzjinmaterial.setCreatorname(String.valueOf(creatorName));
        Object updatedAt = object.get("updatedAt");
        ztwzjinmaterial.setUpdatedat(String.valueOf(updatedAt));
        Object modifierId = object.get("modifierId");
        ztwzjinmaterial.setModifierid(String.valueOf(modifierId));
        Object modifierName = object.get("modifierName");
        ztwzjinmaterial.setModifiername(String.valueOf(modifierName));
        Object itemBarCode = object.get("itemBarCode");
        ztwzjinmaterial.setItembarcode(String.valueOf(itemBarCode));
        Object oriOrgId = object.get("oriOrgId");
        ztwzjinmaterial.setOriorgid(String.valueOf(oriOrgId));
        Object storagePlace = object.get("storagePlace");
        ztwzjinmaterial.setStorageplace(String.valueOf(storagePlace));
        Object skillCardNo = object.get("skillCardNo");
        ztwzjinmaterial.setSkillcardno(String.valueOf(skillCardNo));
        Object receivePrice = object.get("receivePrice");
        ztwzjinmaterial.setReceiveprice(String.valueOf(receivePrice));
        Object waybillWeight = object.get("waybillWeight");
        ztwzjinmaterial.setWaybillweight(String.valueOf(waybillWeight));
        Object serviceType = object.get("serviceType");
        ztwzjinmaterial.setServicetype(String.valueOf(serviceType));
        Object orderType = object.get("orderType");
        ztwzjinmaterial.setOrdertype(String.valueOf(orderType));
        Object externalUploadState = object.get("externalUploadState");
        ztwzjinmaterial.setExternaluploadstate(String.valueOf(externalUploadState));
        Object oriItemRedId = object.get("oriItemRedId");
        ztwzjinmaterial.setOriitemredid(String.valueOf(oriItemRedId));
        Object className = object.get("className");
        ztwzjinmaterial.setClassname(String.valueOf(className));
        Object classCode = object.get("classCode");
        ztwzjinmaterial.setClasscode(String.valueOf(classCode));
        Object classFullName = object.get("classFullName");
        ztwzjinmaterial.setClassfullname(String.valueOf(classFullName));
        Object oriMaterialId = object.get("oriMaterialId");
        ztwzjinmaterial.setOrimaterialid(String.valueOf(oriMaterialId));
        Object oriClassId = object.get("oriClassId");
        ztwzjinmaterial.setOriclassid(String.valueOf(oriClassId));

        QueryWrapper<Ztwzjinmaterial> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("oriorderid",ztwzjinmaterial.getOriorderid());
        Ztwzjinmaterial one = ztwzjinmaterialService.getOne(queryWrapper);
        if (one != null){
            ztwzjinmaterial.setId(one.getId());
            ztwzjinmaterialService.updateById(ztwzjinmaterial);
        }else {
            ztwzjinmaterialService.save(ztwzjinmaterial);
        }
        wzycljinchanggb.setCailiaono(ztwzjinmaterial.getMaterialname());
        wzycljinchanggb.setBeizhu(ztwzjinmaterial.getMaterialmodel());
        return wzycljinchanggb;
    }

    //更新图片数据
    private Wzycljinchanggb setztwzjinmaterialphoto(JSONObject object,Wzycljinchanggb wzycljinchanggb){
        Ztwzphoto ztwzphoto = new Ztwzphoto();
        Object orgId = object.get("orgId");
        ztwzphoto.setOrgid(String.valueOf(orgId));
        Object orderId = object.get("orderId");
        ztwzphoto.setOrderid(String.valueOf(orderId));
        Object cameraPosition = object.get("cameraPosition");
        ztwzphoto.setCameraposition(String.valueOf(cameraPosition));
        Object cameraCode = object.get("cameraCode");
        ztwzphoto.setCameracode(String.valueOf(cameraCode));
        Object photoType = object.get("photoType");
        ztwzphoto.setPhototype(String.valueOf(photoType));
        Object photoUrl = object.get("photoUrl");
        ztwzphoto.setPhotourl(String.valueOf(photoUrl));
        Object oriItemId = object.get("oriItemId");
        ztwzphoto.setOriitemid(String.valueOf(oriItemId));
        Object oriOrderId = object.get("oriOrderId");
        ztwzphoto.setOriorderid(String.valueOf(oriOrderId));
        Object isRemoved = object.get("isRemoved");
        ztwzphoto.setIsremoved(String.valueOf(isRemoved));
        Object externalUploadState = object.get("externalUploadState");
        ztwzphoto.setExternaluploadstate(String.valueOf(externalUploadState));

        QueryWrapper<Ztwzphoto> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("oriitemid",ztwzphoto.getOriitemid());
        Ztwzphoto one = ztwzphotoService.getOne(queryWrapper);
        if (one != null){
            ztwzphoto.setId(one.getId());
            ztwzphotoService.updateById(ztwzphoto);
        }else {
            ztwzphotoService.save(ztwzphoto);
        }

        //获取图片地址
        JSONObject sendObject = JSONUtil.createObj();
        ArrayList<String> list = new ArrayList<>();
        list.add(String.valueOf(photoUrl));
        sendObject.set("product","mquantity");
        sendObject.set("keys",list);
        sendObject.set("expires",15);
        sendObject.set("type","img");
        sendObject.set("imgParameters","image/resize,h_400,w_800");
        System.out.println(String.valueOf(sendObject));
        try (RequestResult result = client.post(photoApi, String.valueOf(sendObject))){
            // 调用result.GetJsonObject方法可以使用强类型的实体，也可以使用Newtonsoft.json.dll中的JArray类型
            System.out.println(result);
            JSON arr = result.getJsonObject();
            JSONObject jsonObject = new JSONObject(arr);
            System.out.println(jsonObject);
            Object o = jsonObject.get(String.valueOf(photoUrl));

            if (String.valueOf(photoType).equals("入场")){
                if (String.valueOf(cameraPosition).equals("车前")){
                    wzycljinchanggb.setJcgkpic(String.valueOf(o));
                }else if (String.valueOf(cameraPosition).equals("车尾")){
                    wzycljinchanggb.setJccppic(String.valueOf(o));
                }else if (String.valueOf(cameraPosition).equals("车顶")){
                    wzycljinchanggb.setJchcppic(String.valueOf(o));
                }else {
                    wzycljinchanggb.setJcbfpic(String.valueOf(o));
                }
            }else {
                if (String.valueOf(cameraPosition).equals("车前")){
                    wzycljinchanggb.setCcgkpic(String.valueOf(o));
                }else if (String.valueOf(cameraPosition).equals("车尾")){
                    wzycljinchanggb.setCccppic(String.valueOf(o));
                }else if (String.valueOf(cameraPosition).equals("车顶")){
                    wzycljinchanggb.setCchcppic(String.valueOf(o));
                }else {
                    wzycljinchanggb.setCcbfpic(String.valueOf(o));
                }
            }
            logger.info(String.format("get %d records on project %d", totalCount.get(), orgId));
        } catch (Exception ex) {
            logger.info(String.format("call api error:  %s", ex.getMessage()), ex);
        }
        return wzycljinchanggb;
    }

    private static void WritePcrFile(JSONArray array)
            throws IOException {
        synchronized (lockObj) {
            try (FileOutputStream stream = new FileOutputStream(outputFile, true)) {
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(stream, "utf-8"));
                writer.write(array.toString());
                writer.flush();
            }
        }
    }

}
