package org.jeecg.common.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.handler.IFillRuleHandler;


/**
 * 规则值自动生成工具类
 *
 * @author qinfeng
 * @举例： 自动生成订单号；自动生成当前日期
 */
@Slf4j
public class FillRuleUtil {

    /**
     * @param ruleCode ruleCode
     * @return
     */
    @SuppressWarnings("unchecked")
    public static Object executeRule(String ruleCode, JSONObject formData) {
        if (!StringUtils.isEmpty(ruleCode)) {
            try {
                // 获取 Service
                ServiceImpl impl = (ServiceImpl) SpringContextUtils.getBean("sysFillRuleServiceImpl");
                // 根据 ruleCode 查询出实体
                QueryWrapper queryWrapper = new QueryWrapper();
                queryWrapper.eq("rule_code", ruleCode);
                JSONObject entity = JSON.parseObject(JSON.toJSONString(impl.getOne(queryWrapper)));
                if (entity == null) {
                    log.warn("填值规则：" + ruleCode + " 不存在");
                    return null;
                }
                // 获取必要的参数
                String ruleClass = entity.getString("ruleClass");
                JSONObject params = entity.getJSONObject("ruleParams");
                if (params == null) {
                    params = new JSONObject();
                }
                if (formData == null) {
                    formData = new JSONObject();
                }
                // 通过反射执行配置的类里的方法
                IFillRuleHandler ruleHandler = (IFillRuleHandler) Class.forName(ruleClass).newInstance();
                return ruleHandler.execute(params, formData);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }
    public static Object shebeisbjno(String sysOrgCode,Integer sbtype) {
        String LQZ="LQZ";//沥青设备
        String BHZ="BHZ";//拌合站
        String SWZ="SWZ";//水稳站
        String WN="WN";//万能
        String YL="YL";//压力
        String RHD="RHD";//软化度
        String ZRD="ZRD";//针入度
        String YD="YD";//延度
        String MXE="MXE";//马歇尔
        String ZL="ZL";//张拉
        String YJ="YJ";//压浆
        String BYSWSD="BYSWSD";//标养室温湿度
        String KYKZ="KYKZ";//抗压抗折
        String WYLC="WYLC";//围岩量测
        String ZJJC="ZJJC";//桩基检测
        String HJJC="HJJC";//环境检测
        String SNJBZ="SNJBZ";//水泥搅拌桩
        String ZHYD="ZHYD";//智慧用电
        String MJKQ="MJKQ";//门禁考勤
        String GYXPZ="GYXPZ";//高压旋喷桩
        String SDHJ="SDHJ";//隧道环境
        String LMD="LMD";//龙门吊
        String GL="GL";//挂篮
        String JQJ="JQJ";//架桥机
        String TPJ="TPJ";//摊铺机
        String YLJ="YLJ";//压路机
        String RLSB="RLSB";//人脸识别
        String DB="DB";//地磅
        String HYLYTJ="HYLYTJ";//恒应力一体机
        String ZRDSY="ZRDSY";//针入度试验
        String RHDSY="RHDSY";//软化点试验
        String DYSY="DYSY";//延度试验
        String MXRWDSY="MXRWDSY";//马歇尔稳定试验
        String MR="MR";//不知道具体设备类型

        String shebeibianma="";


        if(sbtype!=null){
            if(sbtype==0){
                shebeibianma=sysOrgCode+"_"+BHZ;
            }else if(sbtype==1){
                shebeibianma=sysOrgCode+"_"+LQZ;
            }else if(sbtype==2){
                shebeibianma=sysOrgCode+"_"+SWZ;
            }else if(sbtype==3){
                shebeibianma=sysOrgCode+"_"+WN;
            }else if(sbtype==4){
                shebeibianma=sysOrgCode+"_"+YL;
            }else if(sbtype==5){
                shebeibianma=sysOrgCode+"_"+RHD;
            }else if(sbtype==6){
                shebeibianma=sysOrgCode+"_"+ZRD;
            }else if(sbtype==7){
                shebeibianma=sysOrgCode+"_"+YD;
            }else if(sbtype==8){
                shebeibianma=sysOrgCode+"_"+MXE;
            }else if(sbtype==9){
                shebeibianma=sysOrgCode+"_"+ZL;
            }else if(sbtype==10){
                shebeibianma=sysOrgCode+"_"+YJ;
            }else if(sbtype==11){
                shebeibianma=sysOrgCode+"_"+BYSWSD;
            }else if(sbtype==12){
                shebeibianma=sysOrgCode+"_"+KYKZ;
            }else if(sbtype==13){
                shebeibianma=sysOrgCode+"_"+WYLC;
            }else if(sbtype==14){
                shebeibianma=sysOrgCode+"_"+ZJJC;
            }else if(sbtype==15){
                shebeibianma=sysOrgCode+"_"+HJJC;
            }else if(sbtype==16){
                shebeibianma=sysOrgCode+"_"+SNJBZ;
            }else if(sbtype==17){
                shebeibianma=sysOrgCode+"_"+ZHYD;
            }else if(sbtype==18){
                shebeibianma=sysOrgCode+"_"+MJKQ;
            }else if(sbtype==19){
                shebeibianma=sysOrgCode+"_"+GYXPZ;
            }else if(sbtype==20){
                shebeibianma=sysOrgCode+"_"+SDHJ;
            }else if(sbtype==21){
                shebeibianma=sysOrgCode+"_"+LMD;
            }else if(sbtype==22){
                shebeibianma=sysOrgCode+"_"+GL;
            }else if(sbtype==23){
                shebeibianma=sysOrgCode+"_"+JQJ;
            }else if(sbtype==24){
                shebeibianma=sysOrgCode+"_"+TPJ;
            }else if(sbtype==25){
                shebeibianma=sysOrgCode+"_"+YLJ;
            }else if(sbtype==26){
                shebeibianma=sysOrgCode+"_"+RLSB;
            }else if(sbtype==27){
                shebeibianma=sysOrgCode+"_"+DB;
            }else if(sbtype==28){
                shebeibianma=sysOrgCode+"_"+HYLYTJ;
            }else if(sbtype==29){
                shebeibianma=sysOrgCode+"_"+ZRDSY;
            }else if(sbtype==30){
                shebeibianma=sysOrgCode+"_"+RHDSY;
            }else if(sbtype==31){
                shebeibianma=sysOrgCode+"_"+DYSY;
            }else if(sbtype==32){
                shebeibianma=sysOrgCode+"_"+MXRWDSY;
            }
            else{
                shebeibianma=sysOrgCode+"_"+MR;
            }
        }
        return shebeibianma;
    }

    /**
     * @param ruleCode ruleCode
     * @return
     */
    @SuppressWarnings("uncheckeds")
    public static Object executeRules(String ruleCode, JSONObject formData) {
        if (!StringUtils.isEmpty(ruleCode)) {
            try {
                // 获取 Service
                ServiceImpl impl = (ServiceImpl) SpringContextUtils.getBean("sysFillRuleServiceImpl");
                // 根据 ruleCode 查询出实体
                QueryWrapper queryWrapper = new QueryWrapper();
                queryWrapper.eq("rule_code", ruleCode);
                JSONObject entity = JSON.parseObject(JSON.toJSONString(impl.getOne(queryWrapper)));
                if (entity == null) {
                    log.warn("填值规则：" + ruleCode + " 不存在");
                    return null;
                }
                // 获取必要的参数
                String ruleClass = entity.getString("ruleClass");
                JSONObject params = entity.getJSONObject("ruleParams");
                if (params == null) {
                    params = new JSONObject();
                }
                if (formData == null) {
                    formData = new JSONObject();
                }
                // 通过反射执行配置的类里的方法
                IFillRuleHandler ruleHandler = (IFillRuleHandler) Class.forName(ruleClass).newInstance();
                return ruleHandler.executes(params, formData);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
