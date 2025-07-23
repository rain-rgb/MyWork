package org.jeecg.modules.job.hntbhzjob;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.exceptions.ClientException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.bhzcfg.entity.BhzCallCfg;
import com.trtm.iot.bhzcfg.entity.BhzChaobiaoCfg;
import com.trtm.iot.bhzcfg.entity.BhzPhone;
import com.trtm.iot.bhzcfg.service.IBhzCallCfgService;
import com.trtm.iot.bhzcfg.service.IBhzChaobiaoCfgService;
import com.trtm.iot.bhzcfg.service.IBhzPhoneService;
import com.trtm.iot.hntbhz.entity.BhzCementBase;
import com.trtm.iot.hntbhz.entity.BhzCementDetail;
import com.trtm.iot.hntbhz.service.IBhzCementBaseService;
import com.trtm.iot.hntbhz.service.IBhzCementDetailService;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import com.trtm.iot.system.entity.ShebeiInfo;
import com.trtm.iot.system.service.IShebeiInfoService;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.DateUtils;
import org.jeecg.common.util.DySmsEnum;
import org.jeecg.common.util.DySmsHelper;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.jeecg.modules.message.entity.SysMessage;
import org.jeecg.modules.message.service.ISysMessageService;
import org.jeecg.modules.system.entity.SysUser;
import org.jeecg.modules.system.service.ISysUserService;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 审核超两小时预警
 */
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class bhzChaoShiJob implements Job {

    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private IBhzCementBaseService bhzCementBaseService;
    @Autowired
    private ISysMessageService sysMessageService;//消息
    @Autowired
    private IBhzCallCfgService bhzCallCfgService;
    @Autowired
    private ISysUserService userService;
    @Autowired
    private IShebeiInfoService shebeiInfoService;
    @Autowired
    private IBhzCementDetailService bhzCementDetailService;
    @Autowired
    private IBhzPhoneService bhzPhoneService;
    @Autowired
    private JobUtil jobUtil;
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.BHZ_CSYJ);//拌合站超时预警=204
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info("未获取到砼拌合站定时任务的配置信息" + DateUtils.now());
            return;
        }
        Date date = new Date();
        SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat dateFormat1= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formats = dateFormat.format(date);
        String formats1 = dateFormat1.format(date);
        Date parse = null;
        try {
            parse = dateFormat1.parse(formats1);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String format = formats + "%";
        int id = 0;
        List<BhzCementBase> selecthntbhzones = bhzCementBaseService.selectBhzChao(id,20,format);
        if (selecthntbhzones.size() > 0){
            for (BhzCementBase selecthntbhzone :selecthntbhzones){
                id = selecthntbhzone.getId();
                Date collectDatetime = selecthntbhzone.getCollectDatetime();
                Integer overLevel = selecthntbhzone.getOverLevel();// 中级2，高级3
                Integer overproofStatus = selecthntbhzone.getOverproofStatus();//
                String product_datetime = DateUtil.format(selecthntbhzone.getProductDatetime(), "yyyy-MM-dd HH:mm:ss");//出料时间
                String overReason = selecthntbhzone.getOverReason();// 预警原因
                assert parse != null;
                long l = parse.getTime() - collectDatetime.getTime();
                String shebeiNo = selecthntbhzone.getShebeiNo();
                BhzCallCfg selectbhzcallone = bhzCallCfgService.selectbhzcallone(shebeiNo);// 查询配置表
                if (selectbhzcallone != null){
                    if (selectbhzcallone.getCsyj() != null && selectbhzcallone.getCsyj() == 0){
                        if (selectbhzcallone.getCssj() != null && selectbhzcallone.getCssjdw() != null){
                            Integer cssj = selectbhzcallone.getCssj();//超时时长
                            Integer cssjdw = selectbhzcallone.getCssjdw();//超时单位0分钟1小时
                            long i = 0;
                            if (cssjdw == 0){
                                i = cssj * 60 * 1000;
                            }else {
                                i = cssjdw * 60 * 60 * 1000;
                            }
                            if (l > i){
                                try {
                                    cssysmessage(overLevel,overproofStatus,selectbhzcallone,product_datetime,overReason);
                                } catch (ClientException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                }else {
                    log.info("该设备未进行拌合站配置!" + DateUtils.now());
                }
            }
        }else {
            log.info("未获取到砼拌合站超时为审核数据!" + DateUtils.now());
        }
        sysConfigService.updateSysConfigs(JobUtil.HNTBHZ_CFG_NEW, id,0);//根据传过来的唯一值来修改当前判断到的数据id
    }

    public void cssysmessage(int overLevel,int overproofStatus,BhzCallCfg selectbhzcallone,String product_datetime,String overReason) throws ClientException {
        if (selectbhzcallone.getCzperson() != null && selectbhzcallone.getCzperson() != null && selectbhzcallone.getCzperson() != null){
            String czperson = selectbhzcallone.getCzperson();// 处置负责人
            String shperson = selectbhzcallone.getShperson();// 审核负责人
            String spperson = selectbhzcallone.getSpperson();// 审批负责人

            if (overLevel == 1){
                // 发送预警信息
                fsyjxx(czperson,selectbhzcallone,product_datetime,overReason,"初级");
            }else if (overLevel == 2){
                if (overproofStatus == 0 || overproofStatus == 30){
                    fsyjxx(czperson,selectbhzcallone,product_datetime,overReason,"中级");
                }else if (overproofStatus == 10){
                    fsyjxx(shperson,selectbhzcallone,product_datetime,overReason,"中级");
                }
            }else {
                if (overproofStatus == 0 || overproofStatus == 30){
                    fsyjxx(czperson,selectbhzcallone,product_datetime,overReason,"高级");
                }else if (overproofStatus == 10 || overproofStatus == 60){
                    fsyjxx(shperson,selectbhzcallone,product_datetime,overReason,"高级");
                }else if (overproofStatus == 40){
                    fsyjxx(spperson,selectbhzcallone,product_datetime,overReason,"高级");
                }
            }
        }

    }

    public String userphone(String people){
        QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username",people);
        SysUser one = userService.getOne(queryWrapper);
        if(one != null){
            if(one.getPhone() != null){
                return one.getPhone();
            }else {
                log.info("未配置手机号");
                return null;
            }
        }else {
            log.info("查询不到用户名");
            return null;
        }
    }
    // 处置人推送
    public void fsyjxx(String people,BhzCallCfg selectbhzcallone,String product_datetime,String overReason,String l) throws ClientException {
        ShebeiInfo selectshebeione = shebeiInfoService.selectshebeione(selectbhzcallone.getBhjno());
        if (selectbhzcallone.getCzperson() != null){
            JSONObject obj = new JSONObject();
            if(selectbhzcallone.getBhjno()==null){
                obj.put("sbname", "平台未注册设备");
            }else{
                obj.put("sbname", selectshebeione.getSbname());
            }
            obj.put("time", product_datetime);
            obj.put("content", overReason+"超时未闭合，请尽快处理");
            SysMessage sysMessage3 = new SysMessage();
            if (selectbhzcallone.getCsyj() == 0) {
                sysMessage3.setEsTitle("砼拌合站"+l+"预警");
                sysMessage3.setEsType("1");//短信类型  1短信
                sysMessage3.setEsReceiver(userphone(people));//手机号
                sysMessage3.setEsContent(obj.toString());//短信内容
                sysMessage3.setEsSendStatus("0");//推送状态0未推送
                sysMessage3.setEsSendNum(0);//推送总次数
                sysMessage3.setRemark(selectbhzcallone.getBhjno());
                sysMessage3.setCreateTime(new Date());
            } else {
                sysMessage3.setEsTitle("砼拌合站"+l+"预警");
                sysMessage3.setEsType("1");
                sysMessage3.setEsReceiver(userphone(people));
                sysMessage3.setEsContent(obj.toString());
                sysMessage3.setEsSendStatus("-1");//推送状态-1 不需要推送
                sysMessage3.setEsSendNum(0);//推送总次数
                sysMessage3.setRemark(selectbhzcallone.getBhjno());
                sysMessage3.setEsResult("不需要推送");
                sysMessage3.setCreateTime(new Date());
            }

            boolean b = DySmsHelper.sendSms(userphone(people), obj, DySmsEnum.SMS_CB_CODE);
            if (b){
                sysMessage3.setEsSendNum(1);
                log.info("拌合站超时预警短信发送成功"+obj);
            }else {
                sysMessage3.setEsSendNum(2);
                log.info("拌合站超时预警短信发送失败"+obj);
            }
            sysMessageService.save(sysMessage3);
        }
    }
}
