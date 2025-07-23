package com.trtm.iot.entranceguardrecordreal.service;

import com.trtm.iot.entranceguardrecordreal.entity.EntranceGuardRecordReal;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Date;
import java.util.List;


/**
 * @Description: 门禁考勤实时表
 * @Author: jeecg-boot
 * @Date:   2021-09-27
 * @Version: V1.0
 */
public interface IEntranceGuardRecordRealService extends IService<EntranceGuardRecordReal> {

    EntranceGuardRecordReal listMaxOpentime( String shebeis);

    List<EntranceGuardRecordReal> list1(String doorid1);

    List<EntranceGuardRecordReal> getBanZu(String shebeino, Date parse);

    List<EntranceGuardRecordReal> getEntranceGuardRecordRealList(String shebeino, Date parse);

    List<EntranceGuardRecordReal> queryJiXinList(String shebeino);

    List<EntranceGuardRecordReal> queryJiXinBanZuList(String shebeino);

    List<EntranceGuardRecordReal> querylistAttendance(String shebeino);

    EntranceGuardRecordReal getByshebi(String shebei);
}
