package com.trtm.iot.rebarWzcailiaonamedictMan.service.impl;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.trtm.api.utils.RSAUtil;
import com.trtm.iot.rebarComponent.entity.ComponentVo;
import com.trtm.iot.rebarComponent.entity.RebarComponent;
import com.trtm.iot.rebarComponent.mapper.RebarComponentMapper;
import com.trtm.iot.rebarComponent.vo.MaterialVo;
import com.trtm.iot.rebarComponentMaterial.entity.RebarComponentMaterial;
import com.trtm.iot.rebarComponentMaterial.mapper.RebarComponentMaterialMapper;
import com.trtm.iot.rebarComponentTask.entity.RebarComponentTask;
import com.trtm.iot.rebarComponentTask.mapper.RebarComponentTaskMapper;
import com.trtm.iot.rebarTaskChecklist.entity.RebarTaskChecklist;
import com.trtm.iot.rebarTaskChecklist.mapper.RebarTaskChecklistMapper;
import com.trtm.iot.rebarWzcailiaonamedictMan.entity.Number0CaiLiao;
import com.trtm.iot.rebarWzcailiaonamedictMan.entity.Number0CaiLiaoVoS;
import com.trtm.iot.rebarWzcailiaonamedictMan.entity.RebarWzcailiaonamedictMan;
import com.trtm.iot.rebarWzcailiaonamedictMan.entity.RebarWzcailiaonamedictManVo;
import com.trtm.iot.rebarWzcailiaonamedictMan.mapper.RebarWzcailiaonamedictManMapper;
import com.trtm.iot.rebarWzcailiaonamedictMan.service.IRebarWzcailiaonamedictManService;
import com.trtm.iot.wzcailiaonamedictman.entity.WzcailiaonamedictMan;
import com.trtm.iot.wzcailiaonamedictman.entity.WzcailiaonamedictManVo;
import com.trtm.iot.wzcailiaonamedictman.mapper.WzcailiaonamedictManMapper;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.system.vo.LoginUser;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import javax.crypto.Cipher;
import javax.transaction.Transactional;
import java.beans.Transient;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * @Description: rebar_wzcailiaonamedict_man
 * @Author: jeecg-boot
 * @Date: 2024-12-10
 * @Version: V1.0
 */
@Service
public class RebarWzcailiaonamedictManServiceImpl extends ServiceImpl<RebarWzcailiaonamedictManMapper, RebarWzcailiaonamedictMan> implements IRebarWzcailiaonamedictManService {

    @Autowired
    RebarWzcailiaonamedictManMapper rebarWzcailiaonamedictManMapper;
    @Autowired
    private RebarTaskChecklistMapper rebarTaskChecklistMapper;
    @Autowired
    private RebarComponentMapper rebarComponentMapper;
    @Autowired
    private RebarComponentTaskMapper rebarComponentTaskMapper;
    @Autowired
    private RebarComponentMaterialMapper rebarComponentMaterialMapper;
    @Autowired
    WzcailiaonamedictManMapper wzcailiaonamedictManMapper;

    @Override
    public List<RebarWzcailiaonamedictManVo> getRebarWzcailiaoList(RebarWzcailiaonamedictManVo rebarWzcailiaonamedictManVo, Integer pageNo, Integer pageSize, String sys_depart_orgcode, String sys_depart_orgcodes) {
        List<RebarWzcailiaonamedictManVo> rebarWzcailiaonamedictManVoList = rebarWzcailiaonamedictManMapper.getRebarWzcailiaoList(rebarWzcailiaonamedictManVo.getStartDate(), rebarWzcailiaonamedictManVo.getEndDate());
        if (rebarWzcailiaonamedictManVoList == null) {
            return null;
        }
        List<RebarWzcailiaonamedictManVo> rebarWzcailiaonamedictManVos = new ArrayList<>();
        for (RebarWzcailiaonamedictManVo vo : rebarWzcailiaonamedictManVoList) {
            ComponentVo componentVo1 = new ComponentVo();

//            vo.setSysOrgCodes(vo.getSysOrgCode());
            //重量
            Double weight = vo.getWeight();
            if (weight != null) {
                //总重量 = 总数*重量
                Integer allNum = vo.getAllNum();

                vo.setAllWeight(allNum * weight);
            }
        }

        List<RebarWzcailiaonamedictManVo> e = rebarWzcailiaonamedictManVoList.stream().skip((pageNo - 1) * pageSize).limit(pageSize).collect(Collectors.toList());
        return e;
    }

    @Override
    public List<Number0CaiLiao> getWzCailiao(String wbsId, String sysOrgCode, String sysOrgCodes, String token) {

//        String token = null;
        String clientSecret = RSAUtil.main();
        if (token==null){
            String clientSecret1 = "188dffc35f3e04b1d926a5321af5b3cc22e58010903c4a152a7edd9e8e9da368b2ba9a361ad4f3dfb277e18e58436877e49e6447b31ed955ac96d6a1fa344d186578420702ffb9f1d66e7d503256301721e7dfac6f4ac9ceb773f43e6761ac108f59999f2e0755ce893201e4b442ca856908aae930d8d2dc6508588caba9e110";
            String back1 = HttpRequest.post("https://yggc.cncico.com:1080/api/login/appToken")
                    .header("clientid", "app-ext-iot")
                    .header("clientSecret", clientSecret1)
                    .execute()
                    .body();
            JSONObject jsonObject1 = new JSONObject(back1);
            String code1 = (String) jsonObject1.get("code");
            if (code1.equals("00000")) {
                JSONObject model = (JSONObject) jsonObject1.get("model");
                token = (String) model.get("access_token");
            }
        }
        String back = HttpRequest.get("https://yggc.cncico.com:1080/api/measure/meterage/zeroAccounts/action/list?wbsId=" + wbsId)
                .header("clientid", "app-ext-iot")
                .header("clientSecret", clientSecret)
                .header("Authorization", String.format("%s %s", "Bearer", token))
                .execute()
                .body();
        JSONObject jsonObject = new JSONObject(back);
//        Map<String, Object> model1 = jsonObject.get("model");
        String code = (String) jsonObject.get("code");
        List<Number0CaiLiao> caiLiaoList = new ArrayList<>();
        if (code.equals("00000")) {
            JSONArray model = (JSONArray) jsonObject.get("model");

            for (Object o : model) {
//            Number0CaiLiao number0CaiLiao = new ObjectMapper().convertValue(o, Number0CaiLiao.class);

                Number0CaiLiao number0CaiLiao = JSON.parseObject(JSON.toJSONString(o), Number0CaiLiao.class);

                //添加上已使用数量
                String caiLiaoNo = number0CaiLiao.getSubItemNumber();//材料编号
                //分部分项 sysOrgCodes
                String usedNumber = rebarWzcailiaonamedictManMapper.getUsedNumber(caiLiaoNo, sysOrgCode, sysOrgCodes);
                if (usedNumber != null) {
                    number0CaiLiao.setUsedNumber(usedNumber);
                } else {
                    number0CaiLiao.setUsedNumber("0");
                }
                //根据材料名称去查对应的规格型号
                String subItemName = number0CaiLiao.getSubItemName();
                //判断是否有固定的名字
                String hnt = "混凝土";
                String xg = "型钢";
                boolean hntIs = subItemName.contains(hnt);
                boolean xgIs = subItemName.contains(xg);
                if (hntIs){
                    subItemName = hnt;
                }
                if (xgIs){
                    subItemName = xg;
                }

                List<WzcailiaonamedictManVo> wzcailiaonamedictManList = wzcailiaonamedictManMapper.getWzcailiaonamedictManByCaiLiaoName(subItemName);
                if (wzcailiaonamedictManList!=null){
//                    for (WzcailiaonamedictManVo wzcailiaonamedictMan : wzcailiaonamedictManList) {
//                        String guigexinghao = wzcailiaonamedictMan.getGuigexinghao();//规格型号
//                        String cailiaoNo = wzcailiaonamedictMan.getCailiaoNo();//材料编号
//                        String cailiaoName = wzcailiaonamedictMan.getCailiaoName();//材料名称
//                        String guid = wzcailiaonamedictMan.getGuid();//材料唯一id
//                    }
                }
                number0CaiLiao.setWzcailiaonamedictManVoList(wzcailiaonamedictManList);

                caiLiaoList.add(number0CaiLiao);
                System.out.println(o);

            }
        }
//        String total = model.get("total");

        return caiLiaoList;
    }

    @Override
    public boolean addWzCaiLiao(Number0CaiLiaoVoS number0CaiLiaoVoS) {
        //只添加total不是0的************
        String sysOrgCode = number0CaiLiaoVoS.getSysOrgCode();
        String sysOrgCodes = number0CaiLiaoVoS.getSysOrgCodes();
        List<Number0CaiLiao> number0CaiLiaoList = number0CaiLiaoVoS.getNumber0CaiLiaoList();
        for (Number0CaiLiao number0CaiLiao : number0CaiLiaoList) {
            //材料名,材料id,材料型号,数量
            if (new Double(number0CaiLiao.getTotal())>0) {
                RebarWzcailiaonamedictMan rebarWzcailiaonamedictMan = new RebarWzcailiaonamedictMan();
                rebarWzcailiaonamedictMan.setCailiaono(number0CaiLiao.getSubItemNumber());//材料编号
                rebarWzcailiaonamedictMan.setCailiaoname(number0CaiLiao.getSubItemName());//材料名
                rebarWzcailiaonamedictMan.setGuigexinghao(number0CaiLiao.getFigureNumber());//规格型号
//            rebarWzcailiaonamedictMan.setNumber(Integer.valueOf(number0CaiLiao.getErratumAfterQuantity()).intValue());//复核数量
                rebarWzcailiaonamedictMan.setNumber(new Double(number0CaiLiao.getTotal()));//本次使用数量
                Double v = new Double(number0CaiLiao.getTotal());
                System.out.println("v = " + v);

                rebarWzcailiaonamedictMan.setWzcailiaojiliangdanwei(number0CaiLiao.getUnitName());//单位
                rebarWzcailiaonamedictMan.setImage(number0CaiLiao.getUpload());//上传图片
                rebarWzcailiaonamedictMan.setSysOrgCode(sysOrgCode);//所属部门
                rebarWzcailiaonamedictMan.setSysOrgCodes(sysOrgCodes);//分部分项
                rebarWzcailiaonamedictMan.setCreateTime(new Date());//创建时间
                rebarWzcailiaonamedictMan.setIsdel(0);//逻辑删除标志
                rebarWzcailiaonamedictMan.setNodetype("12");//钢筋字典对应的值
                rebarWzcailiaonamedictMan.setWeight(1.0);//材料重量默认成1

                int insert = rebarWzcailiaonamedictManMapper.insert(rebarWzcailiaonamedictMan);
                System.out.println("insert = " + insert);
            }

        }
        return true;
    }
    @Override
    @Transactional
    public boolean addWzCaiLiaoComponentTask(Number0CaiLiaoVoS number0CaiLiaoVoS) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户信息
        String realname = loginUser.getRealname();

        String sysOrgCode = number0CaiLiaoVoS.getSysOrgCode();
        String sysOrgCodes = number0CaiLiaoVoS.getSysOrgCodes();
        List<Number0CaiLiao> number0CaiLiaoList = number0CaiLiaoVoS.getNumber0CaiLiaoList();
        //任务编号//构件编号
        String RwNo = addRWNo().getTaskId();
        String GjNo = addRWNo().getComponentId();

        //添加任务表
        RebarTaskChecklist rebarTaskChecklist = new RebarTaskChecklist();
        rebarTaskChecklist.setTaskId(RwNo);//任务编号
        rebarTaskChecklist.setOrgCode(sysOrgCode);//组织机构编码
        rebarTaskChecklist.setOrgCodes(sysOrgCodes);//分部分项编码
        rebarTaskChecklist.setStatus("0");//状态:0 未加工
        rebarTaskChecklist.setIsDeleted(0);//逻辑删除标记
        rebarTaskChecklist.setCreateTime(new Date());//创建时间
        rebarTaskChecklist.setCreatePerson(realname);//创建人

        int rwInsert = rebarTaskChecklistMapper.insert(rebarTaskChecklist);
        if (rwInsert<0){
            return false;
        }

        //添加构件表
        RebarComponent rebarComponent = new RebarComponent();
        rebarComponent.setComponentId(GjNo);//构件编号
        rebarComponent.setTaskId(RwNo);//任务编号
        rebarComponent.setOrgCode(sysOrgCode);//组织机构编码
        rebarComponent.setOrgCodes(sysOrgCodes);//分部分项编码
        rebarComponent.setCreateTime(new Date());//创建时间
        rebarComponent.setCreatePerson(realname);//创建人
        rebarComponent.setUnit("个");//单位
        rebarComponent.setWeight(1.0);//半成品加工的构件重量,默认1
        rebarComponent.setStatus(1);//状态
        rebarComponent.setComponentType("3");//构件类型
        rebarComponent.setComponentNumber(1);//数量
        rebarComponent.setComponentName("半成品构件");//构件名
        rebarComponent.setIsDeleted(0);//逻辑删除标记

        int gjInsert = rebarComponentMapper.insert(rebarComponent);
        if (gjInsert<0){
            return false;
        }

        //添加任务构件表
        RebarComponentTask rebarComponentTask = new RebarComponentTask();
        rebarComponentTask.setComponentId(GjNo);//构件编号
        rebarComponentTask.setTaskId(RwNo);//任务编号
        rebarComponentTask.setOrgCode(sysOrgCode);//组织机构编码
        rebarComponentTask.setOrgCodes(sysOrgCodes);//分部分项编码
        rebarComponentTask.setComponentNumber("1");//构件数量,默认1

        int rwgjInsert = rebarComponentTaskMapper.insert(rebarComponentTask);
        if (gjInsert<0){
            return false;
        }

        //添加构件材料表
        for (Number0CaiLiao number0CaiLiao : number0CaiLiaoList) {
            if (new Double(number0CaiLiao.getTotal())>0) {
                RebarComponentMaterial rebarComponentMaterial = new RebarComponentMaterial();
                rebarComponentMaterial.setComponentId(GjNo);//构件编号
//                String subItemNumber = number0CaiLiao.getSubItemNumber();//材料编号
                String cailiaoNo = number0CaiLiao.getCailiaoNo();//材料编号
                rebarComponentMaterial.setMaterialId(cailiaoNo);
                String total = number0CaiLiao.getTotal();//本次使用数量
                rebarComponentMaterial.setMaterialNumber(total);
                String subItemName = number0CaiLiao.getSubItemName();//材料名称
                rebarComponentMaterial.setMaterialName(subItemName);
//                String guigexinghao = "";//规格型号(目前没有)
                String guigexinghao = number0CaiLiao.getGuigexinghao();//规格型号(目前没有)
                rebarComponentMaterial.setMaterialType(guigexinghao);
                String guid = number0CaiLiao.getGuid();//材料唯一编号
                rebarComponentMaterial.setGuid(guid);


                int gjclinsert = rebarComponentMaterialMapper.insert(rebarComponentMaterial);
                if (gjclinsert<0){
                    return false;
                }
            }
        }



        //添加材料表
        for (Number0CaiLiao number0CaiLiao : number0CaiLiaoList) {
//            if (new Double(number0CaiLiao.getTotal())>0) {
//                RebarWzcailiaonamedictMan rebarWzcailiaonamedictMan = new RebarWzcailiaonamedictMan();
//                rebarWzcailiaonamedictMan.setCailiaono(number0CaiLiao.getSubItemNumber());//材料编号
//                rebarWzcailiaonamedictMan.setCailiaoname(number0CaiLiao.getSubItemName());//材料名
//                rebarWzcailiaonamedictMan.setGuigexinghao(number0CaiLiao.getFigureNumber());//规格型号
////            rebarWzcailiaonamedictMan.setNumber(Integer.valueOf(number0CaiLiao.getErratumAfterQuantity()).intValue());//复核数量
//                rebarWzcailiaonamedictMan.setNumber(new Double(number0CaiLiao.getTotal()));//本次使用数量
//                rebarWzcailiaonamedictMan.setWzcailiaojiliangdanwei(number0CaiLiao.getUnitName());//单位
//                rebarWzcailiaonamedictMan.setImage(number0CaiLiao.getUpload());//上传图片
//                rebarWzcailiaonamedictMan.setSysOrgCode(sysOrgCode);//所属部门
//                rebarWzcailiaonamedictMan.setSysOrgCodes(sysOrgCodes);//分部分项
//                rebarWzcailiaonamedictMan.setCreateTime(new Date());//创建时间
//                rebarWzcailiaonamedictMan.setIsdel(0);//逻辑删除标志
//                rebarWzcailiaonamedictMan.setNodetype("12");//钢筋字典对应的值
//                rebarWzcailiaonamedictMan.setWeight(1.0);//材料重量默认成1
//
//                int clInsert = rebarWzcailiaonamedictManMapper.insert(rebarWzcailiaonamedictMan);
//                if (clInsert<0){
//                    return false;
//                }
//            }
        }
        return true;
    }

    private static RebarComponentTask addRWNo() {
        //定义任务编号和构件编号
        RebarComponentTask rebarComponentTask = new RebarComponentTask();
        DateTime date = DateUtil.date();
        String s1 = date.toString();
//        s = taskCheckVo.getCreateTime().toString();
        s1.replaceAll("-","");
        rebarComponentTask.setTaskId("RW-" + s1);
        rebarComponentTask.setComponentId("GJ-" + s1);

        return rebarComponentTask;
    }

    @Override
    public String getToken() {
        String token = null;
        String clientSecret1 = "188dffc35f3e04b1d926a5321af5b3cc22e58010903c4a152a7edd9e8e9da368b2ba9a361ad4f3dfb277e18e58436877e49e6447b31ed955ac96d6a1fa344d186578420702ffb9f1d66e7d503256301721e7dfac6f4ac9ceb773f43e6761ac108f59999f2e0755ce893201e4b442ca856908aae930d8d2dc6508588caba9e110";
        String back1 = HttpRequest.post("https://yggc.cncico.com:1080/api/login/appToken")
                .header("clientid", "app-ext-iot")
                .header("clientSecret", clientSecret1)
                .execute()
                .body();
        JSONObject jsonObject1 = new JSONObject(back1);
        String code1 = (String) jsonObject1.get("code");
        if (code1.equals("00000")) {
            JSONObject model = (JSONObject) jsonObject1.get("model");
            token = (String) model.get("access_token");
        }
        return token;
    }



}
