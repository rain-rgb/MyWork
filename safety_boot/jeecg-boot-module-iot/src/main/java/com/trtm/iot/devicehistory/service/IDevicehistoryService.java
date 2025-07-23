package com.trtm.iot.devicehistory.service;

import com.trtm.iot.devicehistory.entity.Devicehistory;
import com.baomidou.mybatisplus.extension.service.IService;

import java.io.IOException;
import java.util.List;

/**
 * @Description: 环境监测历史记录
 * @Author: jeecg-boot
 * @Date:   2021-04-15
 * @Version: V1.0
 */
public interface IDevicehistoryService extends IService<Devicehistory> {


    List<Devicehistory> selectListes(String DevAddr,Integer id);

    List<Devicehistory> selectdeviceone(Integer curid, int alertstate);

    Devicehistory queryone(String shebeiNo);

    List<Devicehistory> selectlistdata(String strsToList1, Integer curid);

    List<Devicehistory> selectlistToSHYJ(String shebeiNo, Integer id);

    List<Devicehistory> selectlistYJQS(String shebeiNo, Integer id);
    List<Devicehistory> selectlistYLQ(String shebeiNo, Integer id);

    List<Devicehistory> selectlistdatatop1(String strsToList1, Integer curid);

    List<Devicehistory> selectListByDev(String DevAddr);

    Devicehistory selectNewByDev(String shebeino);

    int upadteIstuisong(Integer id, int i);

    List<Devicehistory> selectyjList(String shebeilist, Integer curid);
    void push(Devicehistory devicehistory) throws IOException;
}
