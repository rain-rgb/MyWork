package com.trtm.iot.lmd.mapper;

import java.util.List;
import java.util.Map;

import com.trtm.iot.system.entity.ShebeiInfo;
import org.apache.ibatis.annotations.Param;
import com.trtm.iot.lmd.entity.DeviceCraneHistorydata;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * @Description: 龙门吊表信息
 * @Author: jeecg-boot
 * @Date:   2021-07-28
 * @Version: V1.0
 */
public interface DeviceCraneHistorydataMapper extends BaseMapper<DeviceCraneHistorydata> {

    @Update("update device_crane_historydata set status =#{status} where id =#{id}")
    int updateStatus(int id, int status);

    @Update("update device_crane_historydata set alertstate =#{alertstate} where id =#{id}")
    int updatebysone(int id, Integer alertstate);

    List<DeviceCraneHistorydata> selectListsHistoryList2(String split, Integer curid, int i);

    List<DeviceCraneHistorydata> selectListToSHYJ(String shebeiNo, Integer id);

    List<DeviceCraneHistorydata> selectListbim(String shebeiNo, Integer id);

    @Select("select * from shebei_info where sbtype = #{sbtype} and sys_org_code like concat(#{orgCode},'%')")
    List<ShebeiInfo> getShebei(int sbtype, String orgCode);

    List<DeviceCraneHistorydata> selectLmdList(String shebeilist, Integer curid);

    List<Map<String, Object>> getYjList();

    List<DeviceCraneHistorydata> selectListtoJG(String shebeilist, Integer curid);
}
