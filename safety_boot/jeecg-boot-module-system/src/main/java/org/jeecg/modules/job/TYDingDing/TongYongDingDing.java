package org.jeecg.modules.job.TYDingDing;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dingtalk.api.response.OapiAttendanceListRecordResponse;
import com.dingtalk.api.response.OapiV2UserListResponse;
import com.trtm.iot.dingcfg.entity.DingCfg;
import com.trtm.iot.dingcfg.service.IDingCfgService;
import com.trtm.iot.entranceGuardRecord.entity.EntranceGuardRecord;
import com.trtm.iot.entranceGuardRecord.service.IEntranceGuardRecordService;
import com.trtm.iot.entranceguardrecordreal.entity.EntranceGuardRecordReal;
import com.trtm.iot.entranceguardrecordreal.service.IEntranceGuardRecordRealService;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.job.gxjob.statusEnum.recordStatus;
import org.jeecg.modules.job.jobutil.DingTalkUtil;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.quartz.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * \* @author: Xx
 * \* Date: 2022/3/30
 * \* Time: 16:21
 * \* Description:钉钉考勤数据接入
 * \
 */
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class TongYongDingDing implements Job {
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private DingTalkUtil dingTalkUtil;
    @Autowired
    private IEntranceGuardRecordRealService entranceGuardRecordRealService;
    @Autowired
    private IEntranceGuardRecordService entranceGuardRecordService;
    @Autowired
    private IDingCfgService iDingCfgService;

    private String devices;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        SysConfig sysConfig = sysConfigService.selectsysconfigone(JobUtil.ALL_DINGDING);
        //如果他为空,日志，并直接return
        if (null == sysConfig) {
            log.info(String.format("未获取到钉钉考勤定时任务配置信息" + DateUtils.now()));
            return;
        }
        List<DingCfg> configList = iDingCfgService.selectlist();
        for (DingCfg dingCfg : configList) {
            String tokens = dingTalkUtil.getTokens(dingCfg.getAppkey(), dingCfg.getAppsecret());
            devices = dingCfg.getDevicesn();
            long offset = 0L;
            Boolean hasMore = true;
            while (hasMore) {
                if (hasMore) {
                    Map map = dingTalkUtil.GetUserMessageDingTalks(Long.parseLong(dingCfg.getDeptid()), offset, tokens);
                    List<OapiV2UserListResponse.ListUserResponse> userList = (List<OapiV2UserListResponse.ListUserResponse>) map.get("userlist");
                    hasMore = (Boolean) map.get("hasMore");
                    if (hasMore) {
                        offset = (long) map.get("next_cursor");
                    }
                    if (userList.size() > 0) {
                        for (OapiV2UserListResponse.ListUserResponse user : userList) {
                            List userIdList = new ArrayList();
                            EntranceGuardRecordReal entranceGuardRecordReal = new EntranceGuardRecordReal();
                            String name = user.getName();
                            String userid = user.getUserid();
                            userIdList.add(userid);
                            List<EntranceGuardRecordReal> list = getListByOpenid(userid);
                            if (list.size() == 0) { //不存在就新增
                                String strDateFormat = "yyyy-MM-dd";
                                SimpleDateFormat sdf = new SimpleDateFormat(strDateFormat);
                                String format = sdf.format(new Date());
                                String startTime = format + " 00:00:00";
                                String endTime = format + " 23:59:59";
                                Integer status = 0;
                                String DepartName = null;
                                Date openTime = null;
                                Date addDate = null;
                                List<OapiAttendanceListRecordResponse.Recordresult> recordResultList = dingTalkUtil.PostListRecord(userIdList, startTime, endTime, tokens);
                                if (recordResultList != null) {
                                    if (recordResultList.size() > 0) {
                                        for (OapiAttendanceListRecordResponse.Recordresult item : recordResultList) {
                                            if (item.getCheckType() == null) {
                                                continue;
                                            }
                                            if (item.getCheckType().equals("OnDuty")) {//上班打卡
                                                try {
                                                    status = recordStatus.valueOf(item.getTimeResult()).getStatus();
                                                } catch (Exception e) {
                                                    status = 2;//打卡状态获取异常 存6
                                                }
                                                DepartName = item.getUserAddress();
                                                if (item.getUserCheckTime() == null) {
                                                    openTime = item.getBaseCheckTime();
                                                } else {
                                                    openTime = item.getUserCheckTime();
                                                }
                                                entranceGuardRecordReal.setOpentime(openTime);
                                            } else if (item.getCheckType().equals("OffDuty")) {
                                                try {
                                                    status = recordStatus.valueOf(item.getTimeResult()).getStatus();
                                                } catch (Exception e) {
                                                    status = 2;//打卡状态获取异常 存2
                                                }
                                                DepartName = item.getUserAddress();
                                                if (item.getUserCheckTime() == null) {
                                                    addDate = item.getBaseCheckTime();
                                                } else {
                                                    addDate = item.getUserCheckTime();
                                                }
                                                entranceGuardRecordReal.setAdddate(addDate);
                                            }
                                        }
                                        entranceGuardRecordReal.setTypes(5);
                                        entranceGuardRecordReal.setStatus(status);
                                        entranceGuardRecordReal.setNames(name);
                                        entranceGuardRecordReal.setShebeino(dingCfg.getDevicesn());
                                        entranceGuardRecordReal.setOpenid(userid);
                                        entranceGuardRecordReal.setCid(userid);
                                        entranceGuardRecordReal.setDepartname(DepartName);
                                        boolean realSave = entranceGuardRecordRealService.save(entranceGuardRecordReal);
                                        if (realSave) {
                                            log.info(String.format("钉钉考勤实时数据添加成功 " + DateUtils.now()));
                                        } else {
                                            log.info(String.format("钉钉考勤实时数据添加失败 " + DateUtils.now()));
                                        }
                                    }
                                } else {
                                    continue;
                                }
                            } else {//如果实时表存在当前用户数据 则判断打卡类型，下班打卡更新实时考勤表，上班打卡则先保存到历史表再更新实时表
                                for (EntranceGuardRecordReal guardRecordReal : list) {
                                    String strDateFormat = "yyyy-MM-dd";
                                    SimpleDateFormat sdf = new SimpleDateFormat(strDateFormat);
                                    String format = sdf.format(new Date());
                                    String startTime = format + " 00:00:00";
                                    String endTime = format + " 23:59:59";
                                    updateDetail(userIdList, startTime, endTime, tokens, guardRecordReal);
                                }
                            }
                        }
                    }
                } else {
                    break;
                }
            }
        }
    }

    /**
     * 考勤实时表已存在该用户时更新流程 ps：如果最新的打卡类型是下班打卡则不会将实时表数据保存到历史表
     *
     * @param list
     * @param startTime
     * @param endTime
     * @param token
     * @param one
     */
    public void updateDetail(List list, String startTime, String endTime, String token, EntranceGuardRecordReal one) {
        List<OapiAttendanceListRecordResponse.Recordresult> recordResultList = dingTalkUtil.PostListRecord(list, startTime, endTime, token);
        if (recordResultList != null && recordResultList.size() > 0) {
            for (OapiAttendanceListRecordResponse.Recordresult item : recordResultList) {
                if ("OffDuty".equals(item.getCheckType())) { //下班打卡处理
                    try {
                        one.setStatus(recordStatus.valueOf(item.getTimeResult()).getStatus());
                    } catch (Exception e) {
                        one.setStatus(2);//打卡状态获取为空 异常 存2 迟到打卡
                        System.out.println("打卡状态为: " + item.getTimeResult());
                    }
                    if (!"Normal".equals(item.getTimeResult()) && !"NotSigned".equals(item.getTimeResult())) {
                        one.setAdddate(item.getBaseCheckTime()); // 打卡状态不正常 并且不是 未打卡状态
                    } else if ("NotSigned".equals(item.getCheckType())) {
                        one.setAdddate(null);  //未打卡状态
                        continue;
                    } else {
                        one.setAdddate(item.getUserCheckTime()); //正常打卡状态
                    }
                    one.setTypes(5);
                    boolean updateStatus = entranceGuardRecordRealService.updateById(one);
                    if (updateStatus) {
                        log.info(String.format("钉钉考勤实时数据更新成功 " + DateUtils.now()));
                    } else {
                        log.info(String.format("钉钉考勤实时数据更新失败 " + DateUtils.now()));
                    }
                } else { //上班打卡处理
                    Date time = null;
                    EntranceGuardRecord saveEntity = new EntranceGuardRecord();
                    BeanUtils.copyProperties(one, saveEntity);
                    one.setDepartname(item.getUserAddress());
                    if (!"Normal".equals(item.getTimeResult()) && !"NotSigned".equals(item.getTimeResult())) { // 打卡状态不正常 并且不是 未打卡状态
                        System.out.println("用户" + one.getNames() + "打卡状态不正常");
                        if (item.getBaseCheckTime() == null) {
                            time = item.getUserCheckTime();
                            one.setOpentime(item.getUserCheckTime());
                            System.out.println("baseCheckTime为空");
                        }else {
                            one.setOpentime(item.getBaseCheckTime());
                            time = item.getBaseCheckTime();
                        }
                        System.out.println("不正常打卡状态:" + item.getTimeResult());
                        System.out.println("不正常打卡时间:" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(item.getUserCheckTime()));
                    } else if ("NotSigned".equals(item.getCheckType())) {
                        one.setOpentime(null);  //未打卡状态
                        System.out.println("用户" + one.getNames() + "未打卡");
                    } else {
                        one.setOpentime(item.getUserCheckTime()); //正常打卡状态
                        time = item.getUserCheckTime();
                    }
                    QueryWrapper<EntranceGuardRecordReal> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("OpenID", one.getOpenid()).eq("OpenTime", time);
                    EntranceGuardRecordReal isExist = entranceGuardRecordRealService.getOne(queryWrapper);
                    if (isExist == null) { //如果同一用户 在实时表中有此次打卡记录 则不将此记录保存到历史表中
                        saveEntity.setId(null);
                        boolean save = entranceGuardRecordService.save(saveEntity);
                        if (save) {
                            log.info(String.format("钉钉考勤历史数据保存成功 " + DateUtils.now()));
                        } else {
                            log.info(String.format("钉钉考勤历史数据保存失败 " + DateUtils.now()));
                        }
                    } else {
                        log.info(String.format("用户：" + one.getNames() + "的当日考勤历史数据已存在,历史表记录不予新增 " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(one.getOpentime())));
                    }
                    one.setAdddate(null);
                    try {
                        one.setStatus(recordStatus.valueOf(item.getTimeResult()).getStatus());
                    } catch (Exception e) {
                        one.setStatus(2);//打卡状态获取异常 存2
                        System.out.println("打卡状态为: " + item.getTimeResult());
                    }
                    one.setTypes(5);
                    one.setShebeino(devices);
                    boolean updateStatus = entranceGuardRecordRealService.updateById(one);
                    if (updateStatus) {
                        log.info(String.format("钉钉考勤实时数据更新成功 " + DateUtils.now()));
                    } else {
                        log.info(String.format("钉钉考勤实时数据更新失败 " + DateUtils.now()));
                    }
                }
            }
        }
    }

    private List<EntranceGuardRecordReal> getListByOpenid(String id) {
        QueryWrapper<EntranceGuardRecordReal> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("openid", id);
        return entranceGuardRecordRealService.list(queryWrapper);
    }
}
