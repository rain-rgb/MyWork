package com.trtm.iot.entranceguardrecordreal.mapper;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import com.trtm.iot.entranceguardrecordreal.entity.EntranceGuardRecordReal;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

/**
 * @Description: 门禁考勤实时表
 * @Author: jeecg-boot
 * @Date:   2021-09-27
 * @Version: V1.0
 */
public interface EntranceGuardRecordRealMapper extends BaseMapper<EntranceGuardRecordReal> {

    @Select("SELECT * FROM `entrance_guard_record_real` WHERE OpenTime = (select max(OpenTime) from entrance_guard_record_real where shebeino in (${shebeis}))")
    EntranceGuardRecordReal listMaxOpentime(String shebeis);
    @Select("SELECT shebeino FROM `entrance_guard_record_real` where OpenID")
    List<EntranceGuardRecordReal> list1(String doorid1);

    List<EntranceGuardRecordReal> getBanZu(@Param("shebeino") String shebeino,@Param("parse") Date parse);

    List<EntranceGuardRecordReal> getEntranceGuardRecordRealList(@Param("shebeino") String shebeino,@Param("parse") Date parse);

    List<EntranceGuardRecordReal> queryJiXinList(@Param("shebeino") String shebeino);

    List<EntranceGuardRecordReal> queryJiXinBanZuList(@Param("shebeino") String shebeino);

    List<EntranceGuardRecordReal> querylistAttendance(@Param("shebeino") String shebeino);

    @Select("SELECT * FROM `entrance_guard_record_real` WHERE shebeino = #{shebei} limit 1 ")
    EntranceGuardRecordReal getByshebi(String shebei);

}
