package org.jeecg.modules.job.wztaizhangjob.ztyjjob;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import com.trtm.iot.yclcl.entity.Wzyclchuchanggb;
import com.trtm.iot.yclcl.service.IWzyclchuchanggbService;
import com.trtm.iot.yclsl.entity.Wzycljinchanggb;
import com.trtm.iot.ztwzchucslsjb.entity.Ztwzchucslsjb;
import com.trtm.iot.ztwzchucslsjb.service.IZtwzchucslsjbService;
import com.trtm.iot.ztwzchumaterial.entity.Ztwzchumaterial;
import com.trtm.iot.ztwzchumaterial.service.IZtwzchumaterialService;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
public class ztyjclccJob implements Job {
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private IZtwzchucslsjbService ztwzchucslsjbService;
    @Autowired
    private IZtwzchumaterialService ztwzchumaterialService;

    @Autowired
    private IZtwzphotoService ztwzphotoService;
    @Autowired
    private IWzyclchuchanggbService wzyclchuchanggbService;

    private static final String accessId = "1v89j0f+TbRqCsjlAwjU";
    private static final String secretKey = "OxB7HrrZfaSdxFytrQ0CocLovjHQqE6QPnQSMMdo";

    private static final String baseUrl = "https://api.mctech.vip";

    private static final String pcrApi = "/mquantity/get-delivery-weight-order-list?offset=%d&startId=%d&version=%d&limit=%d&orgId=%d";
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
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.ZTYJ_CLCC);//中铁一局，增量获取收料数据
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
        List<Ztwzchucslsjb> list = ztwzchucslsjbService.list();
        int offset = list.size();
        QueryWrapper<Ztwzchucslsjb> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("max(id) id,version");
        Ztwzchucslsjb one = ztwzchucslsjbService.getOne(queryWrapper);
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
            Wzyclchuchanggb wzyclchuchanggb = new Wzyclchuchanggb();
            wzyclchuchanggb.setShebeibianhao("20240408");
            //更新主表数据
            JSONObject object = new JSONObject(a);
            System.out.println(object);
            setztwzjincslsjb(object,wzyclchuchanggb);
            //更新从表数据
            cn.hutool.json.JSONArray material = (cn.hutool.json.JSONArray)object.get("material");
            JSONObject jsonObject = new JSONObject(material.get(0));
            System.out.println(jsonObject);
            setztwzjinmaterial(jsonObject,wzyclchuchanggb);
            //更新图片数据
            cn.hutool.json.JSONArray material1 = (cn.hutool.json.JSONArray)object.get("photo");
            System.out.println(material1);
            for (Object b :material1){
                JSONObject object1 = new JSONObject(b);
                wzyclchuchanggb = setztwzjinmaterialphoto(object1, wzyclchuchanggb);
            }

            QueryWrapper<Wzyclchuchanggb> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("JCGKPic",wzyclchuchanggb.getJcgkpic());
            Wzyclchuchanggb one = wzyclchuchanggbService.getOne(queryWrapper);
            if (one == null){
                wzyclchuchanggbService.save(wzyclchuchanggb);
            }

        }
    }
    //更新图片数据
    private Wzyclchuchanggb setztwzjinmaterialphoto(JSONObject object,Wzyclchuchanggb wzycljinchanggb){
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
    //更新主表数据
    private Wzyclchuchanggb setztwzjincslsjb(JSONObject object,Wzyclchuchanggb wzycljinchanggb){
        Ztwzchucslsjb ztwzchucslsjb = new Ztwzchucslsjb();
        Object id = object.get("id");
        ztwzchucslsjb.setFid(String.valueOf(id));
        Object maker = object.get("maker");
        ztwzchucslsjb.setMaker(String.valueOf(maker));
        Object remark = object.get("remark");
        ztwzchucslsjb.setRemark(String.valueOf(remark));
        Object auditor = object.get("auditor");
        ztwzchucslsjb.setAuditor(String.valueOf(auditor));
        Object version = object.get("version");
        ztwzchucslsjb.setVersion(String.valueOf(version));
        Object oriOrgName = object.get("oriOrgName");
        ztwzchucslsjb.setOriorgname(String.valueOf(oriOrgName));
        Object orgId = object.get("orgId");
        ztwzchucslsjb.setOrgid(String.valueOf(orgId));
        Object orderDate = object.get("orderDate");
        ztwzchucslsjb.setOrderdate(String.valueOf(orderDate));
        Object recordedDate = object.get("recordedDate");
        ztwzchucslsjb.setRecordeddate(String.valueOf(recordedDate));
        Object orderCode = object.get("orderCode");
        ztwzchucslsjb.setOrdercode(String.valueOf(orderCode));
        Object orderOrigin = object.get("orderOrigin");
        ztwzchucslsjb.setOrderorigin(Integer.valueOf(String.valueOf(orderOrigin)));
        Object serviceType = object.get("serviceType");
        ztwzchucslsjb.setServicetype(Integer.valueOf(String.valueOf(serviceType)));
        Object orderType = object.get("orderType");
        ztwzchucslsjb.setOrdertype(Integer.valueOf(String.valueOf(orderType)));
        Object makerDate = object.get("makerDate");
        ztwzchucslsjb.setMakerdate(String.valueOf(makerDate));
        Object printTimes = object.get("printTimes");
        ztwzchucslsjb.setPrinttimes(Integer.valueOf(String.valueOf(printTimes)));
        Object plateNumber = object.get("plateNumber");
        ztwzchucslsjb.setPlatenumber(String.valueOf(plateNumber));
        Object labourId = object.get("labourId");
        ztwzchucslsjb.setLabourid(String.valueOf(labourId));
        Object labourName = object.get("labourName");
        ztwzchucslsjb.setLabourname(String.valueOf(labourName));
        Object ghId = object.get("ghId");
        ztwzchucslsjb.setGhid(String.valueOf(ghId));
        Object ghFullId = object.get("ghFullId");
        ztwzchucslsjb.setGhfullid(String.valueOf(ghFullId));
        Object ghFullName = object.get("ghFullName");
        ztwzchucslsjb.setGhfullname(String.valueOf(ghFullName));
        Object ghName = object.get("ghName");
        ztwzchucslsjb.setGhname(String.valueOf(ghName));
        Object isRed = object.get("isRed");
        ztwzchucslsjb.setIsred(String.valueOf(isRed));
        Object isAudit = object.get("isAudit");
        ztwzchucslsjb.setIsaudit(String.valueOf(isAudit));
        Object auditDate = object.get("auditDate");
        ztwzchucslsjb.setAuditdate(String.valueOf(auditDate));
        Object oriOrgId = object.get("oriOrgId");
        ztwzchucslsjb.setOriorgid(String.valueOf(oriOrgId));
        Object oriOrderId = object.get("oriOrderId");
        ztwzchucslsjb.setOriorderid(String.valueOf(oriOrderId));
        Object creatorId = object.get("creatorId");
        ztwzchucslsjb.setCreatorid(String.valueOf(creatorId));
        Object creatorName = object.get("creatorName");
        ztwzchucslsjb.setCreatorname(String.valueOf(creatorName));
        Object createdAt = object.get("createdAt");
        ztwzchucslsjb.setCreatedat(String.valueOf(createdAt));
        Object modifierId = object.get("modifierId");
        ztwzchucslsjb.setModifierid(String.valueOf(modifierId));
        Object modifierName = object.get("modifierName");
        ztwzchucslsjb.setModifiername(String.valueOf(modifierName));
        Object updatedAt = object.get("updatedAt");
        ztwzchucslsjb.setUpdatedat(String.valueOf(updatedAt));
        Object isRemoved = object.get("isRemoved");
        ztwzchucslsjb.setIsremoved(String.valueOf(isRemoved));
        Object orderDataId = object.get("orderDataId");
        ztwzchucslsjb.setOrderdataid(String.valueOf(orderDataId));
        Object discernPlateNumber = object.get("discernPlateNumber");
        ztwzchucslsjb.setDiscernplatenumber(String.valueOf(discernPlateNumber));
        Object stockbinFullName = object.get("stockbinFullName");
        ztwzchucslsjb.setStockbinfullname(String.valueOf(stockbinFullName));
        Object stockbinName = object.get("stockbinName");
        ztwzchucslsjb.setStockbinname(String.valueOf(stockbinName));
        Object stockbinId = object.get("stockbinId");
        ztwzchucslsjb.setStockbinid(String.valueOf(stockbinId));
        Object oriStockbinId = object.get("oriStockbinId");
        ztwzchucslsjb.setOristockbinid(String.valueOf(oriStockbinId));
        Object enterTime = object.get("enterTime");
        ztwzchucslsjb.setEntertime(String.valueOf(enterTime));
        Object exitTime = object.get("exitTime");
        ztwzchucslsjb.setExittime(String.valueOf(exitTime));
        Object sortTime = object.get("sortTime");
        ztwzchucslsjb.setSorttime(String.valueOf(sortTime));
        Object isReturn = object.get("isReturn");
        ztwzchucslsjb.setIsreturn(String.valueOf(isReturn));
        Object isExit = object.get("isExit");
        ztwzchucslsjb.setIsexit(String.valueOf(isExit));
        Object isTare = object.get("isTare");
        ztwzchucslsjb.setIstare(String.valueOf(isTare));
        Object isMultiplication = object.get("isMultiplication");
        ztwzchucslsjb.setIsmultiplication(String.valueOf(isMultiplication));
        Object roughQuantity = object.get("roughQuantity");
        ztwzchucslsjb.setRoughquantity(String.valueOf(roughQuantity));
        Object tareQuantity = object.get("tareQuantity");
        ztwzchucslsjb.setTarequantity(String.valueOf(tareQuantity));
        Object deductRate = object.get("deductRate");
        ztwzchucslsjb.setDeductrate(String.valueOf(deductRate));
        Object deductQuantity = object.get("deductQuantity");
        ztwzchucslsjb.setDeductquantity(String.valueOf(deductQuantity));
        Object auxiliaryNetQuantity = object.get("auxiliaryNetQuantity");
        ztwzchucslsjb.setAuxiliarynetquantity(String.valueOf(auxiliaryNetQuantity));
        Object orderBarCode = object.get("orderBarCode");
        ztwzchucslsjb.setOrderbarcode(String.valueOf(orderBarCode));
        Object weightType = object.get("weightType");
        ztwzchucslsjb.setWeighttype(String.valueOf(weightType));
        Object isAffirm = object.get("isAffirm");
        ztwzchucslsjb.setIsaffirm(String.valueOf(isAffirm));
        Object isConcrete = object.get("isConcrete");
        ztwzchucslsjb.setIsconcrete(String.valueOf(isConcrete));
        Object oriLabourId = object.get("oriLabourId");
        ztwzchucslsjb.setOrilabourid(String.valueOf(oriLabourId));
        Object oriGhId = object.get("oriGhId");
        ztwzchucslsjb.setOrighid(String.valueOf(oriGhId));
        Object orgName = object.get("orgName");
        ztwzchucslsjb.setOrgname(String.valueOf(orgName));
        Object realOrgId = object.get("realOrgId");
        ztwzchucslsjb.setRealorgid(String.valueOf(realOrgId));

        QueryWrapper<Ztwzchucslsjb> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("oriorderid",ztwzchucslsjb.getOriorderid());
        Ztwzchucslsjb one = ztwzchucslsjbService.getOne(queryWrapper);
        if (one != null){
            ztwzchucslsjb.setId(one.getId());
            ztwzchucslsjbService.updateById(ztwzchucslsjb);
        }else {
            ztwzchucslsjbService.save(ztwzchucslsjb);
        }
        wzycljinchanggb.setQianchepai(ztwzchucslsjb.getPlatenumber());
        wzycljinchanggb.setHouchepai(ztwzchucslsjb.getPlatenumber());
        wzycljinchanggb.setMaozhongt(ztwzchucslsjb.getRoughquantity());
        wzycljinchanggb.setPizhongt(ztwzchucslsjb.getTarequantity());
        wzycljinchanggb.setJingzhongt(ztwzchucslsjb.getAuxiliarynetquantity());
        wzycljinchanggb.setLcbianhao(ztwzchucslsjb.getStockbinfullname());
        return wzycljinchanggb;
    }
    //更新从表数据
    private Wzyclchuchanggb setztwzjinmaterial(JSONObject object,Wzyclchuchanggb wzycljinchanggb){
        Ztwzchumaterial ztwzchumaterial = new Ztwzchumaterial();
        Object orgId = object.get("orgId");
        ztwzchumaterial.setOrgid(String.valueOf(orgId));
        Object orderId = object.get("orderId");
        ztwzchumaterial.setOrderid(String.valueOf(orderId));
        Object remark = object.get("remark");
        ztwzchumaterial.setRemark(String.valueOf(remark));
        Object version = object.get("version");
        ztwzchumaterial.setVersion(String.valueOf(version));
        Object materialId = object.get("materialId");
        ztwzchumaterial.setMaterialid(String.valueOf(materialId));
        Object materialCode = object.get("materialCode");
        ztwzchumaterial.setMaterialcode(String.valueOf(materialCode));
        Object materialName = object.get("materialName");
        ztwzchumaterial.setMaterialname(String.valueOf(materialName));
        Object materialModel = object.get("materialModel");
        ztwzchumaterial.setMaterialmodel(String.valueOf(materialModel));
        Object materialUnit = object.get("materialUnit");
        ztwzchumaterial.setMaterialunit(String.valueOf(materialUnit));
        Object auxiliaryUnit = object.get("auxiliaryUnit");
        ztwzchumaterial.setAuxiliaryunit(String.valueOf(auxiliaryUnit));
        Object classId = object.get("classId");
        ztwzchumaterial.setClassid(String.valueOf(classId));
        Object classFullId = object.get("classFullId");
        ztwzchumaterial.setClassfullid(String.valueOf(classFullId));
        Object netQuantity = object.get("netQuantity");
        ztwzchumaterial.setNetquantity(String.valueOf(netQuantity));
        Object conversionRate = object.get("conversionRate");
        ztwzchumaterial.setConversionrate(String.valueOf(conversionRate));
        Object deductRate = object.get("deductRate");
        ztwzchumaterial.setDeductrate(String.valueOf(deductRate));
        Object deductQuantity = object.get("deductQuantity");
        ztwzchumaterial.setDeductquantity(String.valueOf(deductQuantity));
        Object auxiliaryNetQuantity = object.get("auxiliaryNetQuantity");
        ztwzchumaterial.setAuxiliarynetquantity(String.valueOf(auxiliaryNetQuantity));
        Object mainNetQuantity = object.get("mainNetQuantity");
        ztwzchumaterial.setMainnetquantity(String.valueOf(mainNetQuantity));
        Object oriItemId = object.get("oriItemId");
        ztwzchumaterial.setOriitemid(String.valueOf(oriItemId));
        Object oriOrderId = object.get("oriOrderId");
        ztwzchumaterial.setOriorderid(String.valueOf(oriOrderId));
        Object createdAt = object.get("createdAt");
        ztwzchumaterial.setCreatedat(String.valueOf(createdAt));
        Object isRemoved = object.get("isRemoved");
        ztwzchumaterial.setIsremoved(String.valueOf(isRemoved));
        Object creatorId = object.get("creatorId");
        ztwzchumaterial.setCreatorid(String.valueOf(creatorId));
        Object creatorName = object.get("creatorName");
        ztwzchumaterial.setCreatorname(String.valueOf(creatorName));
        Object updatedAt = object.get("updatedAt");
        ztwzchumaterial.setUpdatedat(String.valueOf(updatedAt));
        Object modifierId = object.get("modifierId");
        ztwzchumaterial.setModifierid(String.valueOf(modifierId));
        Object modifierName = object.get("modifierName");
        ztwzchumaterial.setModifiername(String.valueOf(modifierName));
        Object itemBarCode = object.get("itemBarCode");
        ztwzchumaterial.setItembarcode(String.valueOf(itemBarCode));
        Object oriOrgId = object.get("oriOrgId");
        ztwzchumaterial.setOriorgid(String.valueOf(oriOrgId));
        Object serviceType = object.get("serviceType");
        ztwzchumaterial.setServicetype(String.valueOf(serviceType));
        Object orderType = object.get("orderType");
        ztwzchumaterial.setOrdertype(String.valueOf(orderType));
        Object externalUploadState = object.get("externalUploadState");
        ztwzchumaterial.setExternaluploadstate(String.valueOf(externalUploadState));
        Object oriItemRedId = object.get("oriItemRedId");
        ztwzchumaterial.setOriitemredid(String.valueOf(oriItemRedId));
        Object className = object.get("className");
        ztwzchumaterial.setClassname(String.valueOf(className));
        Object classCode = object.get("classCode");
        ztwzchumaterial.setClasscode(String.valueOf(classCode));
        Object classFullName = object.get("classFullName");
        ztwzchumaterial.setClassfullname(String.valueOf(classFullName));
        Object oriMaterialId = object.get("oriMaterialId");
        ztwzchumaterial.setOrimaterialid(String.valueOf(oriMaterialId));
        Object oriClassId = object.get("oriClassId");
        ztwzchumaterial.setOriclassid(String.valueOf(oriClassId));

        QueryWrapper<Ztwzchumaterial> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("oriorderid",ztwzchumaterial.getOriorderid());
        Ztwzchumaterial one = ztwzchumaterialService.getOne(queryWrapper);
        if (one != null){
            ztwzchumaterial.setId(one.getId());
            ztwzchumaterialService.updateById(ztwzchumaterial);
        }else {
            ztwzchumaterialService.save(ztwzchumaterial);
        }
        wzycljinchanggb.setCailiaono(ztwzchumaterial.getMaterialname());
        wzycljinchanggb.setBeizhu(ztwzchumaterial.getMaterialmodel());
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
