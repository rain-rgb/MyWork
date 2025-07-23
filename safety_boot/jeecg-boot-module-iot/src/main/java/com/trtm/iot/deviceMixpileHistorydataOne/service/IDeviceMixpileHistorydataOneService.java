package com.trtm.iot.deviceMixpileHistorydataOne.service;

import com.trtm.iot.deviceMixpileHistorydataOne.entity.DeviceMixpileHistorydataOne;
import com.baomidou.mybatisplus.extension.service.IService;
import com.trtm.iot.deviceMixpileHistorydataOne.vo.DeviceMixpileHistorydataOnePage;
import com.trtm.iot.deviceMixpileHistorydataOne.vo.DeviceMixpileOneHandler;
import com.trtm.sy.sydpssysample.entity.SysDepart;

import java.util.List;
import java.util.Map;

/**
 * @Description: device_mixpile_historydata_one
 * @Author: jeecg-boot
 * @Date:   2021-10-24
 * @Version: V1.0
 */
public interface IDeviceMixpileHistorydataOneService extends IService<DeviceMixpileHistorydataOne> {

    /**
     * 根据条件查出相对应的数据
     * @param id
     * @param alertstate
     * @return
     */
    List<DeviceMixpileHistorydataOne> selectjbzzone(Integer id, Integer alertstate);

    List<DeviceMixpileHistorydataOne> selectjbzzonelisj(Integer id, String alertstate);

    List<Map<String,Object>> countList();

    List<DeviceMixpileOneHandler> selecthandlerecodes( DeviceMixpileOneHandler handlerrecodes);

    List<DeviceMixpileHistorydataOne> selectjbzzones(Integer curid, int i);

    List<DeviceMixpileHistorydataOne> selectLists(String shebeilist, Integer curid);
    DeviceMixpileHistorydataOne selectone(String shebeino, String pileno, String piletime);

    Integer findXiangMuZSsb(List<String> querySheBeiList, String time);

    Integer findYuJingSsb(List<String> querySheBeiList, String time);

    Integer findBiHeSsb(List<String> querySheBeiList, String time);

    Integer findXiangMuZSsbs(List<String> querySheBeiList, String time);

    Integer findYuJingSsbs(List<String> querySheBeiList, String time);

    Integer findBiHeSsbs(List<String> querySheBeiList, String time);

    SysDepart getSysDepart(String sysOrgCode);

    DeviceMixpileHistorydataOne getFJ(DeviceMixpileHistorydataOne one, int i);

    List<DeviceMixpileHistorydataOnePage> selectJbList(String shebeino,String pileMileage, String pileNo);

    List<DeviceMixpileHistorydataOne> selebyshebeicuid(List<String> shebeiInfolist, Integer curid);

    List<DeviceMixpileHistorydataOne> selectListzlpz(String shebeiList, Integer curid);

    DeviceMixpileHistorydataOne selebysailno(String sbjno, String pileno);

    boolean saveBatchs(List<DeviceMixpileHistorydataOne> list);

    List<DeviceMixpileHistorydataOne> getCBFJ(DeviceMixpileHistorydataOne oneRecord, int i);

    List<DeviceMixpileHistorydataOne> selectListss(String shebeilist, Integer curid);
}
