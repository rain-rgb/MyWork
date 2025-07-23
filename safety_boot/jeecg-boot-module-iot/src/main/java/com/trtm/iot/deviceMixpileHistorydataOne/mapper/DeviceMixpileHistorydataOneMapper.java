package com.trtm.iot.deviceMixpileHistorydataOne.mapper;

import java.util.List;
import java.util.Map;

import com.trtm.iot.deviceMixpileHistorydataOne.vo.DeviceMixpileHistorydataOnePage;
import com.trtm.iot.deviceMixpileHistorydataOne.vo.DeviceMixpileOneHandler;
import com.trtm.sy.sydpssysample.entity.SysDepart;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import com.trtm.iot.deviceMixpileHistorydataOne.entity.DeviceMixpileHistorydataOne;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * @Description: device_mixpile_historydata_one
 * @Author: jeecg-boot
 * @Date:   2021-10-24
 * @Version: V1.0
 */
@Mapper
@Repository
public interface DeviceMixpileHistorydataOneMapper extends BaseMapper<DeviceMixpileHistorydataOne> {
    List<Map<String,Object>> getrichaxun(@Param("shebeis") List<String> shebeis);
    List<Map<String,Object>> getzhouchaxun(@Param("shebeis") List<String> shebeis);
    List<Map<String,Object>> getyuechaxun(@Param("shebeis") List<String> shebeis);
    List<Map<String,Object>> countList(@Param("orgCode") String orgCode);
     List<DeviceMixpileOneHandler> selecthandlerecodes(com.trtm.iot.deviceMixpileHistorydataOne.vo.DeviceMixpileOneHandler handlerrecodes);

    List<DeviceMixpileHistorydataOne> selectjbzzones(Integer curid, int i);

    List<DeviceMixpileHistorydataOne> selectLists(String shebeilist, Integer curid);

    DeviceMixpileHistorydataOne selectOnes(String shebeino, String pileno, String piletime);

    Integer findYuJingS(List<String> querySheBeiList);

    Integer findBiHeS(List<String> querySheBeiList);

    Integer findYuJingSs(List<String> querySheBeiList);

    Integer findBiHeSs(List<String> querySheBeiList);

    Integer findXiangMuZS(List<String> querySheBeiList);

    Integer findXiangMuZSs(List<String> querySheBeiList);

    Integer findXiangMuZStime(List<String> querySheBeiList, String dateNowStr);

    Integer findYuJingStime(List<String> querySheBeiList, String dateNowStr);

    Integer findBiHeStime(List<String> querySheBeiList, String dateNowStr);

    Integer findXiangMuZSstime(List<String> querySheBeiList, String dateNowStr);

    Integer findYuJingSstime(List<String> querySheBeiList, String dateNowStr);

    Integer findBiHeSstime(List<String> querySheBeiList, String dateNowStr);

    Integer findXiangMuZStimes(List<String> querySheBeiList, String beginTime, String endTime);

    Integer findYuJingStimes(List<String> querySheBeiList, String beginTime, String endTime);

    Integer findBiHeStimes(List<String> querySheBeiList, String beginTime, String endTime);

    Integer findXiangMuZSstimes(List<String> querySheBeiList, String beginTime, String endTime);

    Integer findYuJingSstimes(List<String> querySheBeiList, String beginTime, String endTime);

    Integer findBiHeSstimes(List<String> querySheBeiList, String beginTime, String endTime);

    Integer findXiangMuZSsb(List<String> querySheBeiList, String time);

    Integer findYuJingSsb(List<String> querySheBeiList, String time);

    Integer findBiHeSsb(List<String> querySheBeiList, String time);

    Integer findXiangMuZSsbs(List<String> querySheBeiList, String time);

    Integer findYuJingSsbs(List<String> querySheBeiList, String time);

    Integer findBiHeSsbs(List<String> querySheBeiList, String time);

    @Select("select * from sys_depart where org_code = #{sysOrgCode}")
    SysDepart getSysDepart(String sysOrgCode);

    @Select("select * from device_mixpile_historydata_one where shebeino = #{shebeino} and pile_mileage = #{pileMileage} and pile_no = #{pileNo} and gzcount = #{i}")
    DeviceMixpileHistorydataOne getFJ(String shebeino, String pileMileage, String pileNo, int i);

    @Select("select * from device_mixpile_historydata_one where shebeino = #{shebeino} and pile_mileage = #{pileMileage} and pile_no = #{pileNo}")
    List<DeviceMixpileHistorydataOnePage> selectJbList(String shebeino,String  pileMileage, String pileNo);

    @Select("select * from device_mixpile_historydata_one where shebeino = #{shebeino} and pile_mileage = #{pileMileage} and pile_no = #{pileNo} and gzcount = #{i} and chaobiaodengji = 1")
    List<DeviceMixpileHistorydataOne> getCBFJ(String shebeino, String pileMileage, String pileNo, int i);

    List<DeviceMixpileHistorydataOne> selectListzlpz(String shebeiList, Integer curid);

    List<DeviceMixpileHistorydataOne> selectListss(String shebeilist, Integer curid);
}
