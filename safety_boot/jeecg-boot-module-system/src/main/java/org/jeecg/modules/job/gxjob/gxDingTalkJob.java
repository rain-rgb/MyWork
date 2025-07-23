package org.jeecg.modules.job.gxjob;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dingtalk.api.response.OapiAttendanceListRecordResponse;
import com.dingtalk.api.response.OapiV2DepartmentListsubResponse;
import com.dingtalk.api.response.OapiV2UserListResponse;
import com.trtm.iot.entranceGuardRecord.entity.EntranceGuardRecord;
import com.trtm.iot.entranceGuardRecord.service.IEntranceGuardRecordService;
import com.trtm.iot.entranceguardrecordreal.entity.EntranceGuardRecordReal;
import com.trtm.iot.entranceguardrecordreal.service.IEntranceGuardRecordRealService;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.job.gxjob.statusEnum.recordStatus;
import org.jeecg.modules.job.jobutil.GXDingTalkUtil;
import org.quartz.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 高迅钉钉考勤数据获取
 */
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class gxDingTalkJob implements Job {

    @Autowired
    GXDingTalkUtil gxDingTalkUtil;
    @Autowired
    private IEntranceGuardRecordRealService entranceGuardRecordRealService;
    @Autowired
    private IEntranceGuardRecordService entranceGuardRecordService;

    private static Integer status;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        String strDateFormat = "yyyy-MM-dd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(strDateFormat);
        String currentTime = simpleDateFormat.format(new Date());
        String startTime = currentTime + " 00:00:00";
        String endTime = currentTime + " 23:59:59";

        //获取从钉钉接口查数据所需Token
        String token = gxDingTalkUtil.getAccessToken();
        //获取部门列表
        List<OapiV2DepartmentListsubResponse.DeptBaseResponse> deptList = gxDingTalkUtil.getDeptList(token);
        Iterator<OapiV2DepartmentListsubResponse.DeptBaseResponse> iterator = deptList.iterator();
        boolean b = iterator.hasNext();

        //遍历部门列表
        for (OapiV2DepartmentListsubResponse.DeptBaseResponse dept : deptList) {
            //跟据部门id获取员工信息
            Long cursor = 0L;
            Boolean hasMore = true;
            if (hasMore) {
                Map userMap = gxDingTalkUtil.getUserList(dept.getDeptId(), cursor, token);
                List<OapiV2UserListResponse.ListUserResponse> userList = (List<OapiV2UserListResponse.ListUserResponse>) userMap.get("userList");
                hasMore = (boolean) userMap.get("hasMore");
                if (hasMore) {
                    cursor = (long) userMap.get("next_cursor");
                }
                for (OapiV2UserListResponse.ListUserResponse userInfo : userList) {
                    List<String> userIdList = new ArrayList<>();
                    //添加userId到IdList
                    userIdList.add(userInfo.getUserid());
                    //获取员工姓名
                    String name = userInfo.getName();
                    //部门id
                    String departmentID = dept.getDeptId().toString();
                    //部门名称
                    String deptName = dept.getName();
                    //跟据员工idList（暂且为单个员工）
                    OapiAttendanceListRecordResponse.Recordresult recordResult = gxDingTalkUtil.attendanceRecord(userIdList, startTime, endTime, token);
                    if (recordResult != null) {
                        //打卡类型
                        String checkType = recordResult.getCheckType();
                        //打卡结果:状态
                        String timeResult = recordResult.getTimeResult();
                        //用户id
                        String userId = recordResult.getUserId();
                        //上班打卡时间
                        Date openTime = null;
                        //下班打卡时间
                        Date addDate = null;
                        //创建实时表实体类对象
                        EntranceGuardRecordReal entranceGuardRecordReal = new EntranceGuardRecordReal();
                        //实时表实体类条件构造器
                        QueryWrapper<EntranceGuardRecordReal> queryWrapper = new QueryWrapper<>();
                        queryWrapper.eq("openid", userId);
                        //跟据用户id查询实时表数据
                        List<EntranceGuardRecordReal> realTimeList = entranceGuardRecordRealService.list(queryWrapper);
                        //如果实时表不存在该用户打卡信息则添加一条
                        if (realTimeList.size() == 0) {
                            if (checkType != null && checkType.equals("OnDuty")) { //判断上班打卡
                                //使用枚举类 判断打卡状态
                                status = recordStatus.valueOf(timeResult).getStatus();
                                //判断该用户是否旷工
                                if (recordResult.getUserCheckTime() == null) {
                                    //如果旷工则获取缺勤时间
                                    openTime = recordResult.getBaseCheckTime();
                                } else {
                                    //没旷工则获取上班打卡时间
                                    openTime = recordResult.getUserCheckTime();
                                }
                            } else if (checkType != null && checkType.equals("OffDuty")) { //判断下班打卡
                                status = recordStatus.valueOf(timeResult).getStatus();
                                //判断该用户是否旷工
                                if (recordResult.getUserCheckTime() == null) {
                                    //如果旷工则获取缺勤时间
                                    addDate = recordResult.getBaseCheckTime();
                                } else {
                                    //没旷工则获取打卡时间
                                    addDate = recordResult.getUserCheckTime();
                                }
                            }
                            entranceGuardRecordReal.setDepartname(deptName);
                            entranceGuardRecordReal.setDepartmentid(departmentID);
                            entranceGuardRecordReal.setOpentime(openTime);
                            entranceGuardRecordReal.setAdddate(addDate);
                            entranceGuardRecordReal.setShebeino(recordResult.getDeviceId());
                            entranceGuardRecordReal.setOpenid(recordResult.getUserId());
                            entranceGuardRecordReal.setNames(name);
                            entranceGuardRecordReal.setTypes(status);
                            //添加一条数据到实时考勤记录表
                            boolean success = entranceGuardRecordRealService.save(entranceGuardRecordReal);
                            if (success) {
                                log.info(String.format(name + "钉钉考勤实时数据添加成功 " + DateUtils.now()));
                            } else {
                                log.info(String.format(name + "钉钉考勤实时数据添加失败 " + DateUtils.now()));
                            }
                        } else {
                            //如果实时表存在数据就把实时表数据保存在历史表中并且更新实时表数据
                            for (EntranceGuardRecordReal realTime : realTimeList) {
                                //创建历史表实体类对象
                                EntranceGuardRecord entranceGuardRecord = new EntranceGuardRecord();
                                BeanUtils.copyProperties(realTime, entranceGuardRecord);
                                //保存实时表数据到历史表中
                                boolean saveBefore = entranceGuardRecordService.save(entranceGuardRecord);
                                if (saveBefore) {
                                    log.info(String.format(name + "钉钉考勤历史数据添加成功 " + DateUtils.now()));
                                } else {
                                    log.info(String.format(name + "钉钉考勤历史数据添加失败 " + DateUtils.now()));
                                }
                                if (checkType != null && checkType.equals("OnDuty")) { //判断上班打卡
                                    //枚举对象判断打卡状态
                                    status = recordStatus.valueOf(timeResult).getStatus();
                                    //获取上班打卡时间
                                    if (recordResult.getUserCheckTime() == null) { //判断该用户是否旷工
                                        openTime = recordResult.getBaseCheckTime();
                                    } else { //没旷工则获取打卡时间
                                        openTime = recordResult.getUserCheckTime();
                                    }
                                } else if (checkType != null && checkType.equals("OffDuty")) { //判断下班打卡
                                    status = recordStatus.valueOf(timeResult).getStatus();
                                    //获取下班打卡时间
                                    if (recordResult.getUserCheckTime() == null) { //判断该用户是否旷工
                                        addDate = recordResult.getBaseCheckTime();
                                    } else { //没旷工则获取打卡时间
                                        addDate = recordResult.getUserCheckTime();
                                    }
                                }
                                entranceGuardRecordReal.setDepartname(deptName);
                                entranceGuardRecordReal.setDepartmentid(departmentID);
                                entranceGuardRecordReal.setOpentime(openTime);
                                entranceGuardRecordReal.setAdddate(addDate);
                                entranceGuardRecordReal.setShebeino(recordResult.getDeviceId());
                                entranceGuardRecordReal.setOpenid(recordResult.getUserId());
                                entranceGuardRecordReal.setNames(name);
                                entranceGuardRecordReal.setTypes(status);
                                QueryWrapper<EntranceGuardRecordReal> updateQw = new QueryWrapper<>();
                                updateQw.eq("OpenId", userId);
                                boolean updateResult = entranceGuardRecordRealService.update(entranceGuardRecordReal, updateQw);
                                if (updateResult) {
                                    log.info(String.format(name + "钉钉考勤实时数据修改成功 " + DateUtils.now()));
                                } else {
                                    log.info(String.format(name + "钉钉考勤实时数据修改失败 " + DateUtils.now()));
                                }
                            }
                        }
                    } else {
                        //如果查询打卡信息返回结果为空
                        log.info("获取不到 " + deptName + ":" + name + " 在 " + currentTime + " 的打卡数据");
                        continue;
                    }
                }
            } else {
                break;
            }
        }
    }
}

