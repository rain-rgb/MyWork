package com.trtm.sy.sydpssysignature.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qiyuesuo.sdk.v2.bean.Contract;
import com.qiyuesuo.sdk.v2.response.SdkResponse;
import com.trtm.iot.sydpsqzlc.entity.DpsSyQianzhangliucheng;
import com.trtm.iot.sydpsqzlc.service.IDpsSyQianzhangliuchengService;
import com.trtm.sy.sign.mapper.BusSignMapper;
import com.trtm.sy.sign.model.entity.BusSign;
import com.trtm.sy.sign.model.enums.signEnums;
import com.trtm.sy.sign.service.BusSignService;
import com.trtm.sy.sydpssysample.entity.SyDpsSySample;
import com.trtm.sy.sydpssysample.service.ISyDpsSySampleService;
import com.trtm.sy.sydpssysignature.entity.SyDpsSySignature;
import com.trtm.sy.sydpssysignature.mapper.SyDpsSySignatureMapper;
import com.trtm.sy.sydpssysignature.service.ISyDpsSySignatureService;
import com.trtm.sy.sydpssysignature.service.SignPersonService;
import com.trtm.sy.wtgl.rwd.entity.SyDpsYyRenwudan;
import com.trtm.sy.wtgl.rwd.service.ISyDpsYyRenwudanService;
import com.xkcoding.http.util.StringUtil;
import net.sf.json.JSONObject;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.exception.JeecgBootException;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.oConvertUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Description: sy_dps_sy_signature
 * @Author: jeecg-boot
 * @Date: 2023-09-13
 * @Version: V1.0
 */
@Service
public class SyDpsSySignatureServiceImpl extends ServiceImpl<SyDpsSySignatureMapper, SyDpsSySignature> implements ISyDpsSySignatureService {
    SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    @Autowired
    private IDpsSyQianzhangliuchengService qianzhangliuchengService;
    @Autowired
    private ISyDpsSySampleService dpsSySampleService;
    @Autowired
    private BusSignService busSignService;
    @Resource
    private SignPersonService signPersonService;

    @Override
    @Transactional
    public void agree(HttpServletRequest request, MultipartFile file, HttpServletResponse response) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息

        String p1 = request.getParameter("p1");
        //ArrayList<Map<String,Object>> list = new ArrayList<>();
        JSONObject jsonObject = JSONObject.fromObject(p1);
//        String CAID = jsonObject.getString("CAID");
//        Map<String, Object> key = this.baseMapper.selectBYCAID(CAID);
//            if (key == null) {
//                model.setSuccess(EnumSuccess.error.getState());
//                model.setMsg("该印章未在系统中登记");
//            }
//        String path = request.getSession().getServletContext().getRealPath("/");
        //for (int i = 0; i < list.size(); i++) {

        String sql = "";
        //String id = list.get(i).get("id").toString();
        String id = jsonObject.getString("id");
        String date = sd.format(new Date());
        DpsSyQianzhangliucheng process = qianzhangliuchengService.getById(id);
        Integer type = process.getLiuchengleixing();
        SyDpsSySample sample = dpsSySampleService.getById(process.getSampleid());
//                SignatureLog log = new SignatureLog();


        // 通过
//                process.setCAID(key.get("id").toString());
        process.setQianzhangren(loginUser.getUsername());
        process.setQianzhangshijian(date);
        process.setQianzhangzhuangtai(1);
//                log.setSampleId(sample.getId());
//                log.setQianzhangliuchengId(id);
////                log.setCAID(key.get("id").toString());
//                log.setQianzhangren(user.getRealName());
//                log.setQianzhangshijian(date);
//                log.setQianzhangyijian("");
//                log.setQianzhangzhuangtai(0);
//                systemService.saveOrUpdate(log);
        qianzhangliuchengService.saveOrUpdate(process);
        String baogaoriqi = "";
        if ("BG_JCQZ".equals(process.getQianzhangguanjianzi()) || "JL_JCQZ".equals(process.getQianzhangguanjianzi())) {
            Map<String, Object> map = this.baseMapper.selectSignSql1(sample.getSampleno());
            Map<String, Object> map2 = this.baseMapper.selectSignSql2(sample.getSampleno(), map.get("tiNo").toString());
            this.baseMapper.updateSignSql1(map2.get("baogaoriqi").toString(), sample.getSampleno());
        } else if ("BG_ZSFZRQZ".equals(process.getQianzhangguanjianzi())) {
            Map<String, Object> map = this.baseMapper.selectSignSql1(sample.getSampleno());
            Map<String, Object> map2 = this.baseMapper.selectSignSql2(sample.getSampleno(), map.get("tiNo").toString());
            if (StringUtil.isNotEmpty(map2.get("baogaoriqi").toString()) && !"/".equals(map2.get("baogaoriqi").toString())) {
                baogaoriqi = map2.get("baogaoriqi").toString();
                baogaoriqi = baogaoriqi.substring(baogaoriqi.length() - 11, baogaoriqi.length());
            }
            this.baseMapper.updateSignSql1(map2.get("baogaoriqi").toString(), sample.getSampleno());
        } else if ("BG_FHQZ".equals(process.getQianzhangguanjianzi())) {
            Map<String, Object> map = this.baseMapper.selectSignSql1(sample.getSampleno());
            Map<String, Object> map2 = this.baseMapper.selectSignSql2(sample.getSampleno(), map.get("tiNo").toString());
            if (StringUtil.isNotEmpty(map2.get("baogaoriqi").toString()) && !"/".equals(map2.get("baogaoriqi").toString())) {
                baogaoriqi = map2.get("baogaoriqi").toString();
                baogaoriqi = baogaoriqi.substring(baogaoriqi.length() - 11, baogaoriqi.length());
            }
            this.baseMapper.updateSignSql1(map2.get("baogaoriqi").toString(), sample.getSampleno());
        }
        Long c = this.baseMapper.selectSignSql3(sample.getId(), type);
        String zt = "";
        switch (type) {
            case 0:
                zt = sample.getJilubiaoqianzhangzhuangtai();
                break;
            case 1:
                zt = sample.getBaogaoqianzhangzhuangtai();
                break;
            case 3:
                zt = sample.getBaoyandanqianzhangzhuangtai();
                break;
            case 4:
                zt = sample.getShenpibiaoqianzhangzhuangtai();
                break;
        }
        String[] zts = zt.split("/");
        zts[0] = String.valueOf(c);
        zt = zts[0] + "/" + zts[1];
        switch (type) {
            case 0:
                sample.setJilubiaoqianzhangzhuangtai(zt);
                break;
            case 1:
                sample.setBaogaoqianzhangzhuangtai(zt);
                break;
            case 3:
                sample.setBaoyandanqianzhangzhuangtai(zt);
                break;
            case 4:
                sample.setShenpibiaoqianzhangzhuangtai(zt);
                break;
        }
        sql = "select count(0) from dps_sy_qianzhangliucheng where sampleId = ? and qianzhangzhuangtai=0";
        c = this.baseMapper.selectSignSql4(sample.getId());
        if (c == 0) {
            sample.setQianzhangzhuangtai(1);
            //计量数据新增
            if ("BGLQ13001F".equals(sample.getTino()) || "JB010507".equals(sample.getTino()) ||
                    "JB010507a".equals(sample.getTino()) || "JB010512".equals(sample.getTino()) ||
                    "JB010507b".equals(sample.getTino()) || "BGLQ13002F".equals(sample.getTino()) ||
                    "JB010401".equals(sample.getTino())) {
                this.baseMapper.updateSignSql2(sample.getId());
            }
        }
        dpsSySampleService.saveOrUpdate(sample);
    }

    @Resource
    private ISyDpsYyRenwudanService syDpsYyRenwudanService;

    @Override
    public void createSign(String name, Map<String, String> personList, String pdfurl, Integer spbnum,Integer bgnum, Integer jlnum, String sampleno) {
        try {
            QueryWrapper<SyDpsYyRenwudan> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("sample_no", sampleno);
            SyDpsYyRenwudan renwudan = syDpsYyRenwudanService.getOne(queryWrapper);
            Map<String, String> phoneMap = new HashMap<>();
            if (oConvertUtils.isNotEmpty(renwudan.getJiancerenyuan())) {
                String phone = this.baseMapper.selectPhoneByName(renwudan.getJiancerenyuan());
                phoneMap.put("jcry", phone);
            }
            if (oConvertUtils.isNotEmpty(renwudan.getJilurenyuan())) {
                String phone = this.baseMapper.selectPhoneByName(renwudan.getJilurenyuan());
                phoneMap.put("jlry", phone);
            }
            if (oConvertUtils.isNotEmpty(renwudan.getFuherenyuan())) {
                String phone = this.baseMapper.selectPhoneByName(renwudan.getFuherenyuan());
                phoneMap.put("fhry", phone);
            }
            if (oConvertUtils.isNotEmpty(renwudan.getQianfarenyuan())) {
                String phone = this.baseMapper.selectPhoneByName(renwudan.getQianfarenyuan());
                phoneMap.put("qfry", phone);
            }
            //TODO
            SdkResponse<Contract> getdraft = busSignService.getdraft(name, phoneMap);
            Long contractId = getdraft.getResult().getId();
            Long documentId = busSignService.getaddbyfile(pdfurl, "试验报表", contractId);
            busSignService.getsend(getdraft, documentId, spbnum,bgnum, jlnum);
            BusSign busSign = new BusSign();
            BusSign sign = busSignService.getOne(Wrappers.lambdaQuery(new BusSign()).eq(BusSign::getSampleno, sampleno));
            if (oConvertUtils.isEmpty(sign)) {
                LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
                //保存相关信息
                busSign.setSampleno(sampleno);
                busSign.setContractid(contractId);
                busSign.setDocumentid(documentId);
                busSign.setStatus("10");
                busSign.setOrgCode(loginUser.getOrgCode());
                busSignService.save(busSign);
            } else {
                sign.setContractid(contractId);
                sign.setDocumentid(documentId);
                sign.setStatus("10");
                busSignService.updateById(sign);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new JeecgBootException("签章生成失败");
        }

    }

    @Resource
    private BusSignMapper busSignMapper;

    @Override
    public IPage gridNew(Page<Map> page, String orgCode, String titCode, String sampleNo, String sampleName, String sampleGcbw, String date, String reportNo, String tiNo, String username, String state, String self, String shenpizhuangtai) {
        IPage<Map> result = busSignMapper.selectListByCode(page, orgCode);
        List<Map> records = result.getRecords();
        if (records.size() != 0) {
            for (Map record : records) {
                if(oConvertUtils.isNotEmpty(record.get("personid"))){
                    String name = busSignMapper.selectNameById(record.get("personid").toString());
                    record.put("personName", name);
                }
                String orgcode = busSignMapper.selectNameByCode(record.get("org_code").toString());
                record.put("org_code", orgcode);
                if(oConvertUtils.isNotEmpty(record.get("contractid"))){
                    record.put("contractid", record.get("contractid").toString());
                }
                if(oConvertUtils.isNotEmpty(record.get("documentid"))){
                    record.put("documentid",  record.get("documentid").toString());
                }
                if(oConvertUtils.isNotEmpty(record.get("signid"))){
                    record.put("signid",  record.get("signid").toString());
                }
//                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                if(oConvertUtils.isNotEmpty(record.get("create_time"))){
////                    String time = sdf.format(new Date(Long.parseLong(record.get("create_time").toString())));
//                    record.put("create_time", record.get("create_time").toString());
//                }
            }
        }
        return result;
    }

    @Override
    public String sign(HashMap<String, Object> map) {
        String personid = map.get("personid").toString();
        String signid = map.get("signid").toString();
        BusSign busSign = new BusSign();
        busSign.setSignid(signid);
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        if (!loginUser.getId().equals(personid)) {
            throw new JeecgBootException("当前账号与待签署人不一致，无法签署");
        }
        String phone = this.baseMapper.selectPhoneByName(personid);
        busSignService.queryJL(phone);
//        Long documentid = (Long) map.get("documentid");
        Long contractid = Long.parseLong(map.get("contractid").toString()) ;
        String status = map.get("status").toString();
//        if (status.equals("13") || status.equals("23")) {
//            busSignService.getcompanysign(contractid, signEnums.sealId);
//        } else {
//            busSignService.getpersonalsign(contractid, phone);
//        }
        switch (status) {
            case "13":
                busSignService.getcompanysign(contractid, signEnums.sealId);
                status = "20";
                break;
            case "23":
                busSignService.getcompanysign(contractid, signEnums.sealId);
                status = "50";
                break;
            default:
                busSignService.getpersonalsign(contractid, phone);
                status = String.valueOf(Integer.parseInt(status) + 1);
        }
        busSign.setStatus(status);
        boolean update = busSignService.updateById(busSign);
        if (update){
            return "签署成功";
        }else{
            throw new JeecgBootException("签署失败");
        }

    }

}
