package org.jeecg.modules.job.bhzworkstatusjob;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.bhzcementworkstatus.entity.BhzCementWorkstatus;
import com.trtm.iot.bhzcementworkstatus.service.IBhzCementWorkstatusService;
import com.trtm.iot.bhzcfg.entity.BhzPhone;
import com.trtm.iot.bhzcfg.service.IBhzPhoneService;
import com.trtm.iot.bys.entity.BysReal;
import com.trtm.iot.bys.service.IBysRealService;
import com.trtm.iot.devicecranerealdata.entity.DeviceCraneRealdata;
import com.trtm.iot.devicecranerealdata.service.IDeviceCraneRealdataService;
import com.trtm.iot.devicehistory.entity.Devicehistory;
import com.trtm.iot.devicehistory.service.IDevicehistoryService;
import com.trtm.iot.devicemixpilerealdata.entity.DeviceMixpileRealdata;
import com.trtm.iot.devicemixpilerealdata.service.IDeviceMixpileRealdataService;
import com.trtm.iot.hntbhz.entity.BhzCementBase;
import com.trtm.iot.hntbhz.service.IBhzCementBaseService;
import com.trtm.iot.lqbhz.entity.BhzLqBases;
import com.trtm.iot.lqbhz.service.IBhzLqBasesService;
import com.trtm.iot.shebeiWarncfg.entity.ShebeiWarncfg;
import com.trtm.iot.shebeiWarncfg.service.IShebeiWarncfgService;
import com.trtm.iot.syj.entity.TSyjzb;
import com.trtm.iot.syj.service.ITSyjzbService;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import com.trtm.iot.system.entity.ShebeiInfo;
import com.trtm.iot.system.service.IShebeiInfoService;
import com.trtm.iot.yajiangm.entity.TrYajiangM;
import com.trtm.iot.yajiangm.service.ITrYajiangMService;
import com.trtm.iot.zhanglaxxb.entity.TrZhanglaXxb;
import com.trtm.iot.zhanglaxxb.service.ITrZhanglaXxbService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.jeecg.modules.message.entity.SysMessage;
import org.jeecg.modules.message.handle.ISendMsgHandle;
import org.jeecg.modules.message.handle.enums.SendMsgStatusEnum;
import org.jeecg.modules.message.handle.enums.SendMsgTypeEnum;
import org.jeecg.modules.message.service.ISysMessageService;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.jeecg.common.util.DateUtils.date2Str;
import static org.jeecg.common.util.DateUtils.differHours;

/**
 * \* Date: 2021/8/20
 * \* Time: 16:18
 * \* Description:根据保存时间与当前时间对比判断砼拌合站你的工作状态以及给新的表添加数据（当前时间-保存时间的差值在2小时内，砼拌合站工作状态为正常工作；
 * 当前时间-保存时间差值大于等于2小时24小时内，砼拌合站工作状态为延时工作；
 * 当前时间-保存时间差值在24小时(包含24小时)外，砼拌合站工作状态为未工作
 * 只有设备信息没有砼拌合站信息，未安装）
 * \
 */
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class BhzworkstatusJob implements Job {
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private IBhzCementBaseService bhzCementBaseService;
    @Autowired
    private IShebeiInfoService shebeiInfoService;
    @Autowired
    private IBhzCementWorkstatusService bhzCementWorkstatusService;
    @Autowired
    private ITrZhanglaXxbService trZhanglaXxbService;
    @Autowired
    private ITSyjzbService tSyjzbService;
    @Autowired
    private ITrYajiangMService trYajiangMService;
    @Autowired
    private IBysRealService bysRealService;
    @Autowired
    private IDevicehistoryService devicehistoryService;
    @Autowired
    private IDeviceMixpileRealdataService deviceMixpileRealdataService;
    @Autowired
    private IDeviceCraneRealdataService deviceCraneRealdataService;
    @Autowired
    private JobUtil jobUtil;
    @Autowired
    private IShebeiWarncfgService shebeiWarncfgService;
    @Autowired
    private IBhzPhoneService bhzPhoneService;
    @Autowired
    private ISysMessageService sysMessageService;//消息
    @Autowired
    private IBhzLqBasesService bhzLqBasesService;

    @SneakyThrows
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.BHZ_WORKSTATUA);//设备工作状态=24
        if (null == selectsysconfigone) {
            log.info("未获取到设备工作状态定时任务的配置信息" + DateUtils.now());
            return;
        }
        List<ShebeiInfo> selectbhzones = shebeiInfoService.selectbhzonelist();//查询全部设备信息
        if (null == selectbhzones || selectbhzones.size() == 0) {
            log.info("暂无需要判断工作状态的设备数据" + DateUtils.now());
            return;
        }
        for (ShebeiInfo shebeiInfo : selectbhzones) {
            String shebeiNo = shebeiInfo.getSbjno();//设备编号
            int status = 0;//工作状态 0：未安装 1：未工作 2：延时 3：正常工作
            String id = shebeiInfo.getId();
            int ids = 0;
            ShebeiInfo shebeiInfo1 = new ShebeiInfo();
            shebeiInfo1.setId(id);
            Date time = null;
            try {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                int sbtype = shebeiInfo.getSbtype();
                if (sbtype == 0) {//混凝土拌合站
                    BhzCementBase queryone1 = bhzCementBaseService.queryones(shebeiNo);//查询该拌合站设备最新保存时间的数据
                    if (queryone1 != null) {
                        time = queryone1.getSaveDatetime();//保存时间
                        status = jobUtil.getUpdateStatus(time, id);
                    }
                } else if (sbtype == 1) {//沥青拌合站
                    BhzLqBases bhzLqBases = bhzLqBasesService.queryone(shebeiNo);
                    if (null!=bhzLqBases){
                        time = DateUtils.str2Date(bhzLqBases.getChuliaoshijian(),format);//出料时间
                        status = jobUtil.getUpdateStatus(time, id);
                    }
                } else if (sbtype == 2) {//水稳拌合站
                    continue;
                } else if (sbtype == 3 || sbtype == 4 || sbtype == 12) {//万能机、压力机、抗压抗折机
                    TSyjzb tSyjzb = tSyjzbService.queryone(shebeiNo);
                    if (tSyjzb != null) {
                        time = tSyjzb.getSjscsj();//数据上传时间
                        status = jobUtil.getUpdateStatus(time, id);
                    } else {
                        status = 0;
                    }
                } else if (sbtype == 9) {//张拉
                    TrZhanglaXxb trZhanglaXxb = trZhanglaXxbService.queryone(shebeiNo);
                    if (trZhanglaXxb != null) {
                        time = DateUtils.str2Date(trZhanglaXxb.getSgsj(), format);//施工时间
                        status = jobUtil.getUpdateStatus(time, id);
                    }
                } else if (sbtype == 10) {//压浆
                    TrYajiangM trYajiangM = trYajiangMService.queryone(shebeiNo);
                    if (trYajiangM != null) {
                        time = DateUtils.str2Date(trYajiangM.getYjsj(), format);
                        status = jobUtil.getUpdateStatus(time, id);
                    }
                } else if (sbtype == 11) {//标养室
                    BysReal bysReal = bysRealService.queryone(shebeiNo);
                    if (bysReal != null) {
                        time = bysReal.getDatatime();
                        status = jobUtil.getUpdateStatus(time, id);
                    }
                } else if (sbtype == 15) {//环境监测
                    Devicehistory devicehistory = devicehistoryService.queryone(shebeiNo);
                    if (devicehistory != null) {
                        time = devicehistory.getTimevalue();
                        status = jobUtil.getUpdateStatus(time, id);
                    }
                } else if (sbtype == 16 || sbtype == 19 || sbtype == 53 || sbtype == 54) {//搅拌桩
                    DeviceMixpileRealdata deviceMixpileRealdata = deviceMixpileRealdataService.queryone(shebeiNo);
                    if (deviceMixpileRealdata != null) {
                        time = deviceMixpileRealdata.getDatatime();
                        status = jobUtil.getUpdateStatus(time, id);
                    }
                } else if (sbtype == 21 || sbtype == 23 || sbtype == 50 || sbtype == 51) {//大型设备
                    DeviceCraneRealdata deviceCraneRealdata = deviceCraneRealdataService.queryone(shebeiNo);
                    if (deviceCraneRealdata != null) {
                        time = deviceCraneRealdata.getCranedate();
                        status = jobUtil.getUpdateStatus(time, id);
                    }
                } else {
                    continue;
                }
            } catch (Exception e) {
                e.printStackTrace();
                status = 40;
            }
            shebeiInfo1.setStatus(status);
            shebeiInfo1.setUploadDatetime(time);
            shebeiInfoService.updateById(shebeiInfo1);
            if (status == 1 || status == 2) {
                ShebeiWarncfg shebeiWarncfg = shebeiWarncfgService.getcfgdata(shebeiInfo.getSbjno());
                if (null == shebeiWarncfg) {
                    continue;
                } else {
                    QueryWrapper<BhzPhone> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("uid", shebeiWarncfg.getPhone());
                    BhzPhone one = bhzPhoneService.getOne(queryWrapper);
                    SysMessage sysMessage = new SysMessage();
                    if (one != null) {
                        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
                        String format2 = ft.format(new Date());
                        SysMessage sysMessage1 = sysMessageService.selectones(shebeiNo, format2 + "%");
                        if (null == sysMessage1) {
                            JSONObject obj = new JSONObject();
                            obj.put("sbname", shebeiInfo.getSbname());
                            obj.put("time", DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));//数据上传时间;
                            if (status == 1) {
                                obj.put("content", "该设备未工作,请注意查看");
                            }
                            if (status == 2) {
                                obj.put("content", "该设备延时工作,请注意查看");
                            }
                            ISendMsgHandle sendMsgHandle = null;
                            sysMessage.setEsTitle("设备延时/未工作通知");
                            sysMessage.setEsReceiver(one.getPhones());
                            sysMessage.setEsContent(obj.toString());//短信内容
                            sysMessage.setEsType("1");
                            sysMessage.setCreateTime(new Date());
                            sysMessage.setRemark(shebeiNo);
                            if (shebeiWarncfg.getIsCall() == 0) {
                                int sendNum = 0;
                                sendMsgHandle = (ISendMsgHandle) Class.forName(SendMsgTypeEnum.SMS.getImplClass()).newInstance();
                                try {
                                    sendMsgHandle.SendMsg(sysMessage.getEsReceiver(), sysMessage.getEsTitle(),
                                            sysMessage.getEsContent());
                                    // 发送消息成功
                                    sysMessage.setEsSendStatus(SendMsgStatusEnum.SUCCESS.getCode());
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    // 发送消息出现异常
                                    sysMessage.setEsSendStatus(SendMsgStatusEnum.FAIL.getCode());
                                }
                                sysMessage.setEsSendNum(++sendNum);
                            } else {
                                sysMessage.setEsSendStatus("-1");//推送状态-1 不需要推送
                                sysMessage.setEsSendNum(0);//推送总次数
                                sysMessage.setEsResult("不需要推送");
                            }
                            sysMessageService.save(sysMessage);
                        } else {
                            continue;
                        }
                    }
                }
            }

            log.info("设备工作状态！   时间" + DateUtils.now());
        }
    }
}
