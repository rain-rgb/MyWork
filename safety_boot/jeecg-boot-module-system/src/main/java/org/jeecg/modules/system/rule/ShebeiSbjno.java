package org.jeecg.modules.system.rule;

import com.alibaba.fastjson.JSONObject;
import org.jeecg.common.handler.IFillRuleHandler;
import org.jeecg.common.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author xgd
 * @date 2021/3/11
 * 设备编号生成规则
 */
public class ShebeiSbjno implements IFillRuleHandler {
    @Autowired
    private RedisUtil redisUtil;
    @Override
    public Object execute(JSONObject params, JSONObject formData) {
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
        String MR="MR";//不知道具体设备类型
        Integer bianma=0001;
        String shebeibianma="";
        redisUtil.set("bianma",bianma);
        Integer bianma1 = (Integer) redisUtil.get("bianma");
        bianma=bianma1+1;
        if(params!=null){
            Integer type = (Integer) params.get("type");
            String sysOrgCode = String.valueOf(params.get("sysOrgCode"));
            if(type==0){
                shebeibianma=sysOrgCode+"_"+BHZ+"_"+bianma;
            }else if(type==1){
                shebeibianma=sysOrgCode+"_"+LQZ+"_"+bianma;
            }else if(type==2){
                shebeibianma=sysOrgCode+"_"+SWZ+"_"+bianma;
            }else if(type==3){
                shebeibianma=sysOrgCode+"_"+WN+"_"+bianma;
            }else if(type==4){
                shebeibianma=sysOrgCode+"_"+YL+"_"+bianma;
            }else if(type==5){
                shebeibianma=sysOrgCode+"_"+RHD+"_"+bianma;
            }else if(type==6){
                shebeibianma=sysOrgCode+"_"+ZRD+"_"+bianma;
            }else if(type==7){
                shebeibianma=sysOrgCode+"_"+YD+"_"+bianma;
            }else if(type==8){
                shebeibianma=sysOrgCode+"_"+MXE+"_"+bianma;
            }else if(type==9){
                shebeibianma=sysOrgCode+"_"+ZL+"_"+bianma;
            }else if(type==10){
                shebeibianma=sysOrgCode+"_"+YL+"_"+bianma;
            }else{
                shebeibianma=sysOrgCode+"_"+MR+"_"+bianma;
            }
        }
        return shebeibianma;
    }

    @Override
    public Object executes(JSONObject params, JSONObject formData) {
        return null;
    }


}
