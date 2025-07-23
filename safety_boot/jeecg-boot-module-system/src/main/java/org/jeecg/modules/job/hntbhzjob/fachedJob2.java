package org.jeecg.modules.job.hntbhzjob;

import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.exceptions.ClientException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.trtm.api.utils.StringUtils;
import com.trtm.iot.bhzcfg.entity.BhzPhone;
import com.trtm.iot.bhzcfg.service.IBhzPhoneService;
import com.trtm.iot.car.entity.SchedulingCar;
import com.trtm.iot.car.service.ISchedulingCarService;
import com.trtm.iot.devicemixpileoneoverhandler.entity.MixpileOneOverHandler;
import com.trtm.iot.suchingcarpeiz.entity.SuchingCarpeiz;
import com.trtm.iot.suchingcarpeiz.service.ISuchingCarpeizService;
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
import org.jeecg.modules.system.entity.SysDepart;
import org.jeecg.modules.system.service.ISysDepartService;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 审核超两小时预警
 */
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class fachedJob2 implements Job {

    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private ISchedulingCarService schedulingCarService;
    @Autowired
    private ISysMessageService sysMessageService;//消息
    @Autowired
    private ISysDepartService sysDepartService;
    @Autowired
    private ISuchingCarpeizService suchingCarpeizService;
    @Autowired
    private IShebeiInfoService shebeiInfoService;
    @Autowired
    private IBhzPhoneService bhzPhoneService;
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.FCD_CSYJ);//发车单超时预警=304
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info("未获取到发车单运输超时定时任务的配置信息" + DateUtils.now());
            return;
        }
        //当前数据检测到的目标id
        Integer curid = selectsysconfigone.getCurid();
        int id = 0;
        QueryWrapper<SchedulingCar> queryWrapper = new QueryWrapper<>();
        // 状态为未判断的
        queryWrapper.eq("status1",0);
       // queryWrapper.ne("status1",3);
       // queryWrapper.gt("id",curid);
       // queryWrapper.last(" dattim < NOW()-INTERVAL 3 HOUR ");

//       queryWrapper.apply(true, "TO_DAYS(NOW())-TO_DAYS(create_at) = 0");//查询当天的数据
        List<SchedulingCar> list = schedulingCarService.list(queryWrapper);
        if (list.size() > 0){
            for (SchedulingCar l :list){

               //  if (l.getDattim() != null && l.getQianshoutime() != null){
                    QueryWrapper<ShebeiInfo> queryWrapper1 = new QueryWrapper<>();
                     queryWrapper1.likeRight("sys_org_code",l.getSysOrgCode());
                    queryWrapper1.eq("sbname",l.getVehicle());
                    ShebeiInfo one = shebeiInfoService.getOne(queryWrapper1)  ;
                    String section = "";
                    if(StringUtils.isNotBlank(one.getSysOrgCode())){
                        SysDepart queryone = sysDepartService.queryone(one.getSysOrgCode());
                         section = queryone != null ?queryone.getDepartName():"";
                    }

                    if (one != null){
                        QueryWrapper<SuchingCarpeiz> queryWrapper2 = new QueryWrapper<>();
                        queryWrapper2.eq("imei",one.getSbjno());
                        SuchingCarpeiz one1 = suchingCarpeizService.getOne(queryWrapper2);
                        if(one1 != null){
                            long ms = 1;
                            String msg = "";
                            int ast = 1;// 运输中
                            // 预警时间间隔计算
                            if(one1.getUnit() == 1){ // 小时
                                ms = (long)(1000*60*60* Double.parseDouble(one1.getRemark())) ;
                            }else if(one1.getUnit() == 0) { // 分钟
                                ms =(long)(1000*60* Double.parseDouble(one1.getRemark())) ;
                            }else{// 天
                                ms = (long)(1000*60*60* 24*Double.parseDouble(one1.getRemark())) ;
                            }
                            // 判断是否签收
                            if(l.getStatus() > 0 && l.getQianshoutime() != null  ){
                                if((l.getQianshoutime().getTime()-l.getDattim().getTime()) > ms){
                                    msg ="驾驶员：" +l.getDriver()+" 运输超规定时间" +(l.getQianshoutime().getTime()-l.getDattim().getTime())/(1000*60)+"分";
                                    ast = 3; // 已超时
                                }

                            }else{
                                if((new Date().getTime()-l.getDattim().getTime()) > ms){
                                    msg ="驾驶员：" +l.getDriver()+" 运输超规定时间" +(new Date().getTime()-l.getDattim().getTime())/(1000*60)+"分";
                                    ast = 3; // 已超时
                                }
                            }

                           if(ast == 3){
                               SchedulingCar car = new SchedulingCar();
                               car.setId(l.getId());
                               // 设置超期状态
                               car.setStatus1(ast);
                               car.setFlag(msg);
                               schedulingCarService.updateById(car);
                               if (one1.getIsCall() == 0){
                                   try {
                                       SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                       String format2 = dateFormat.format(l.getDattim());
                                       fsyjxx(one1,format2,section+one.getSbname()+"车",msg);
                                   } catch (ClientException e) {
                                       e.printStackTrace();
                                   }
                               }
                           }
                        }else {
                            SchedulingCar car = new SchedulingCar();
                            car.setId(l.getId());
                            // 未设置 超时预警
                            car.setStatus1(5);
                            car.setFlag("该车牌没有配置预警信息！！！");
                            schedulingCarService.updateById(car);
                        }
                    } else {
                        SchedulingCar car = new SchedulingCar();
                        car.setId(l.getId());
                        // 设置已经判断状态,未匹配GPS
                        car.setStatus1(4);
                        car.setFlag("车牌没有对应的GPS");
                        schedulingCarService.updateById(car);

                }
                    id = l.getId();
            }
        }
        sysConfigService.updateSysConfigs(JobUtil.FCD_CSYJ, id,0);//根据传过来的唯一值来修改当前判断到的数据id
    }

    // 处置人推送
    public void fsyjxx(SuchingCarpeiz one1,String product_datetime,String getSbjno,String overReason) throws ClientException {
        BhzPhone bhzPhone = bhzPhoneService.selectBhzPhone(one1.getZhydPhones());
        JSONObject obj = new JSONObject();
        obj.put("sbname", getSbjno);
        obj.put("time", product_datetime);
        obj.put("content", overReason);
        SysMessage sysMessage3 = new SysMessage();
        sysMessage3.setEsTitle("混凝土运输车签收超时预警");
        sysMessage3.setEsType("1");//短信类型  1短信
        sysMessage3.setEsReceiver(bhzPhone.getPhones());//手机号
        sysMessage3.setEsContent(obj.toString());//短信内容
        sysMessage3.setEsSendStatus("0");//推送状态0未推送
        sysMessage3.setEsSendNum(0);//推送总次数
        sysMessage3.setRemark(getSbjno);
        sysMessage3.setCreateTime(new Date());

//        boolean b = DySmsHelper.sendSms(one1.getZhydPhones(), obj, DySmsEnum.SMS_CB_CODE);
//        if (b){
//            sysMessage3.setEsSendNum(1);
//            log.info("拌合站超时预警短信发送成功"+obj);
//        }else {
//            sysMessage3.setEsSendNum(2);
//            log.info("拌合站超时预警短信发送失败"+obj);
//        }
        sysMessageService.save(sysMessage3);
    }
}
