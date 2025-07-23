package com.trtm.iot.clgl.service;

import cn.hutool.json.JSONObject;
import com.trtm.iot.clgl.entity.FrontDeviceHistorydata;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * @Description: 车辆信息历史数据表
 * @Author: jeecg-boot
 * @Date:   2021-05-13
 * @Version: V1.0
 */
public interface IFrontDeviceHistorydataService extends IService<FrontDeviceHistorydata> {
    void insertGpsdata(JSONObject json);

    Map selectToken(String shebeiNo,String datatime_end,String datatime_begin);

    FrontDeviceHistorydata selectnewData(String shebeino, double lng1, double lat1);

    List<Map> selectgetHistoryMByMUtc(String shebeiNo, String projectId, String uploadtime);
}
