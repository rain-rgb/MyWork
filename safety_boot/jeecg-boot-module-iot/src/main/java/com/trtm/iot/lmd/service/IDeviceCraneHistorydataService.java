package com.trtm.iot.lmd.service;

import com.trtm.iot.lmd.entity.DeviceCraneHistorydata;
import com.baomidou.mybatisplus.extension.service.IService;
import com.trtm.iot.system.entity.ShebeiInfo;

import java.util.List;
import java.util.Map;

/**
 * @Description: 龙门吊表信息
 * @Author: jeecg-boot
 * @Date: 2021-07-28
 * @Version: V1.0
 */
public interface IDeviceCraneHistorydataService extends IService<DeviceCraneHistorydata> {

    List<DeviceCraneHistorydata> selectLists(List<String> shebeiNo, Integer id);


    List<DeviceCraneHistorydata> selectListsHistoryList(List<String> shebeiNo, Integer id, int alertstate);

    List<DeviceCraneHistorydata> selectcranehistoryone(Integer curid, int alertstate);

    int updateStatus(int id, int status);

    int updatebysone(int id, Integer alertstate);

    List<DeviceCraneHistorydata> selectListsHistoryList2(String split, Integer curid, int i);

    List<DeviceCraneHistorydata> selectListToSHYJ(String shebeiNo, Integer id);

    List<DeviceCraneHistorydata> selectListbim(String shebeiNo, Integer id);

    List<ShebeiInfo> getShebei(int sbtype, String orgCode);

    List<DeviceCraneHistorydata> selectLmdList(String shebeilist, Integer curid);

    List<Map<String, Object>> getYjList();

    List<DeviceCraneHistorydata> selectListtoJG(String shebeilist, Integer curid);
}
