package org.jeecg.modules.job.ydJob;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dingtalk.api.response.OapiAttendanceListRecordResponse;
import com.dingtalk.api.response.OapiV2UserListResponse;
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
import java.util.*;

/**
 * \* @author: Xx
 * \* Date: 2021/10/19
 * \* Time: 14:44
 * \* Description:  义东高速(东阳段：预制场钉钉考勤数据接入) 获取用户打卡信息
 * \
 */
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class ydDingTalkJob implements Job {
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private DingTalkUtil dingTalkUtil;
    @Autowired
    private IEntranceGuardRecordRealService entranceGuardRecordRealService;
    @Autowired
    private IEntranceGuardRecordService entranceGuardRecordService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.YDDING_TALK);
        //如果他为空,日志，并直接return
        if (null == selectsysconfigone) {
            log.info(String.format("未获取到钉钉考勤定时任务配置信息" + DateUtils.now()));
            return;
        }
        String token = dingTalkUtil.getToken();
        long offset = 0L;
        Boolean hasMore = true;
        while (hasMore) {
            if (hasMore) {
                Map map = dingTalkUtil.GetUserMessageDingTalk(offset, token);
                List<OapiV2UserListResponse.ListUserResponse> userlist = (List<OapiV2UserListResponse.ListUserResponse>) map.get("userlist");
                hasMore = (Boolean) map.get("hasMore");
                if (hasMore) {
                    offset = (long) map.get("next_cursor");
                }
                if (userlist.size() > 0) {
                    for (OapiV2UserListResponse.ListUserResponse userlist1 : userlist) {
                        List userIdList = new ArrayList();
                        EntranceGuardRecordReal entranceGuardRecordReal = new EntranceGuardRecordReal();
                        String name = userlist1.getName();
                        String userid = userlist1.getUserid();
                        userIdList.add(userid);
                        QueryWrapper<EntranceGuardRecordReal> queryWrapper = new QueryWrapper<>();
                        queryWrapper.eq("openid", userid);
                        List<EntranceGuardRecordReal> list = entranceGuardRecordRealService.list(queryWrapper);
                        if (list.size() == 0) { //不存在就新增
                            String strDateFormat = "yyyy-MM-dd";
                            SimpleDateFormat sdf = new SimpleDateFormat(strDateFormat);
                            String format = sdf.format(new Date());
//                            LocalDateTime today = LocalDateTime.now();
//                            LocalDateTime yesterday = today.minusDays(1);
//                            Date yesterdayDate = Date.from( yesterday.atZone( ZoneId.systemDefault()).toInstant());
//                            String yesterdayStr = sdf.format(yesterdayDate);
                            String startTime = format + " 00:00:00";
                            String endTime = format + " 23:59:59";
                            Integer status = 0;
                            String DepartName = null;
                            Date openTime = null;
                            Date addDate = null;
                            List<OapiAttendanceListRecordResponse.Recordresult> recordresult = dingTalkUtil.PostListRecord(userIdList, startTime, endTime, token);
                            if (recordresult != null) {
                                if (recordresult.size() > 0) {
                                    for (OapiAttendanceListRecordResponse.Recordresult item : recordresult) {
                                        if (item.getCheckType() == null) {
                                            continue;
                                        }
                                        if (item.getCheckType().equals("OnDuty")) {//上班打开
                                            status = recordStatus.valueOf(item.getTimeResult()).getStatus();
                                            DepartName = item.getUserAddress();
                                            if (item.getUserCheckTime() == null) {
                                                openTime = item.getBaseCheckTime();
                                            } else {
                                                openTime = item.getUserCheckTime();
                                            }
                                            entranceGuardRecordReal.setOpentime(openTime);
                                        } else if (item.getCheckType().equals("OffDuty")) {
                                            status = recordStatus.valueOf(item.getTimeResult()).getStatus();
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
                                    entranceGuardRecordReal.setNames(name);
                                    entranceGuardRecordReal.setShebeino("A05A01A03A01A01A01_MJKQ_0262");
                                    entranceGuardRecordReal.setOpenid(userid);
                                    entranceGuardRecordReal.setDepartname(DepartName);
                                    boolean save = entranceGuardRecordRealService.save(entranceGuardRecordReal);
                                    if (save) {
                                        log.info(String.format("钉钉考勤实时数据添加成功" + DateUtils.now()));
                                        EntranceGuardRecord entranceGuardRecord = new EntranceGuardRecord();
                                        BeanUtils.copyProperties(entranceGuardRecordReal, entranceGuardRecord);
                                        boolean save1 = entranceGuardRecordService.save(entranceGuardRecord);
                                        if (save1) {
                                            log.info(String.format("钉钉考勤历史数据添加成功" + DateUtils.now()));
                                        } else {
                                            log.info(String.format("钉钉考勤历史数据添加失败" + DateUtils.now()));
                                        }
                                    } else {
                                        log.info(String.format("钉钉考勤实时数据添加失败" + DateUtils.now()));
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
                                updateDetail(userIdList, startTime, endTime, token, guardRecordReal);
                            }
                        }
                    }
                }
            } else {
                break;
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
                        one.setStatus(6);//打卡状态获取异常 存6
                        System.out.println("打卡状态为: "+item.getTimeResult());
                    }
                    if (!"Normal".equals(item.getTimeResult()) && !"NotSigned".equals(item.getCheckType())) {
                        one.setAdddate(item.getBaseCheckTime()); // 打卡状态不正常 并且不是 未打卡状态
                    } else if ("NotSigned".equals(item.getCheckType())) {
                        one.setAdddate(null);  //未打卡状态
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
                    EntranceGuardRecord saveEntity = new EntranceGuardRecord();
                    BeanUtils.copyProperties(one, saveEntity);
                    saveEntity.setId(null);
                    boolean save = entranceGuardRecordService.save(saveEntity);
                    if (save) {
                        log.info(String.format("钉钉考勤历史数据保存成功 " + DateUtils.now()));
                    } else {
                        log.info(String.format("钉钉考勤历史数据保存失败 " + DateUtils.now()));
                    }
                    one.setDepartname(item.getUserAddress());
                    if (!"Normal".equals(item.getTimeResult()) && !"NotSigned".equals(item.getCheckType())) {
                        one.setOpentime(item.getBaseCheckTime());// 打卡状态不正常 并且不是 未打卡状态
                    } else if ("NotSigned".equals(item.getCheckType())) {
                        one.setOpentime(null);  //未打卡状态
                    } else {
                        one.setOpentime(item.getUserCheckTime()); //正常打卡状态
                    }
                    one.setAdddate(null);
                    try {
                        one.setStatus(recordStatus.valueOf(item.getTimeResult()).getStatus());
                    } catch (Exception e) {
                        one.setStatus(6);//打卡状态获取异常 存6
                        System.out.println("打卡状态为: "+item.getTimeResult());
                    }
                    one.setTypes(5);
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
}
