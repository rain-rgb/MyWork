package org.jeecg.modules.job.ckqdatahandlejob;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.ckqdatahandle.entity.DeviceMixinStation;
import com.trtm.iot.ckqdatahandle.entity.DevicePulpingHistoryData;
import com.trtm.iot.ckqdatahandle.mapper.*;
import com.trtm.iot.ckqdatahandle.service.impl.DeviceMixinStationServiceImpl;
import com.trtm.iot.ckqdatahandle.vo.CementFlyAsh;
import com.trtm.iot.ckqhistorydata.entity.DeviceTrafficHistoryData;
import com.trtm.iot.ckqhistorydata.mapper.DeviceTrafficHistoryDataMapper;
import com.trtm.iot.ckqhistorydata.service.impl.DeviceTrafficHistoryDataServiceImpl;
import com.trtm.iot.ckqhistorydata.vo.LookDeviceTrafficHistoryDataVo;
import com.trtm.iot.ckqrealdata.entity.DeviceTrafficRealdata;
import com.trtm.iot.ckqrealdata.mapper.DeviceTrafficRealdataMapper;
import com.trtm.iot.ckqrealdata.service.impl.DeviceTrafficRealdataServiceImpl;
import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.jeecg.common.util.DateUtils;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

//采空区（搅拌站）定时任务
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class CkQDeviceMixinStationJob implements Job {

    @Autowired
    private DeviceMixinStationServiceImpl deviceMixinStationService;

    @Autowired
    private DeviceMixinStationMapper deviceMixinStationMapper;

    @Autowired
    private DeviceTrafficHistoryDataServiceImpl deviceTrafficHistoryDataService;

    @Autowired
    private DeviceTrafficHistoryDataMapper deviceTrafficHistoryDataMapper;

    @Autowired
    private DeviceTrafficRealdataServiceImpl deviceTrafficRealDataService;

    @Autowired
    private DeviceTrafficRealdataMapper deviceTrafficRealdataMapper;

    @Autowired
    private DeviceGroutingPumpMapper deviceGroutingPumpMapper;

    @Autowired
    private DeviceMixinStationDetailsMapper deviceMixinStationDetailsMapper;

    @Autowired
    private DeviceGroutingPumpDayMapper deviceGroutingPumpDayMapper;

    @Autowired
    private DeviceMixinStationUpdateLogMapper deviceMixinStationUpdateLogMapper;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        synchronized (Object.class) {
            //处理垃圾数据
            List<DeviceTrafficHistoryData> deviceTrafficHistoryDataGarbage = deviceTrafficHistoryDataMapper.deleteTrafficHistoryDataGarbage();
            if (deviceTrafficHistoryDataGarbage.size() > 0) {
                for (DeviceTrafficHistoryData dthdg : deviceTrafficHistoryDataGarbage) {
                    dthdg.setHandleStatus("1");
                    deviceTrafficHistoryDataMapper.update(dthdg, new QueryWrapper<DeviceTrafficHistoryData>().eq("id", dthdg.getId()));
                }
            }
            //处理实际值过大
            List<DeviceTrafficHistoryData> list = deviceTrafficHistoryDataMapper.selectGarbageDateBigMixinStation();
            if (list.size() > 0) {
                for (DeviceTrafficHistoryData dthdg : list) {
                    dthdg.setHandleStatus("1");
                    deviceTrafficHistoryDataMapper.update(dthdg, new QueryWrapper<DeviceTrafficHistoryData>().eq("id", dthdg.getId()));
                }
            }
            //配料生产总量
            List<DeviceMixinStation> deviceMixinStationList = deviceMixinStationService.list();
            if (null != deviceMixinStationList && deviceMixinStationList.size() > 0) {
                for (DeviceMixinStation dmskl : deviceMixinStationList) {
                    QueryWrapper<DeviceTrafficHistoryData> queryWrapper = new QueryWrapper<>();
                    queryWrapper.select("id", "pid", "v", "handle_status", "topic");
                    queryWrapper.eq("handle_status", "0");
                    queryWrapper.eq("topic", dmskl.getSid());
                    List<DeviceTrafficHistoryData> deviceTrafficHistoryData = deviceTrafficHistoryDataMapper.selectList(queryWrapper);
                    if (null != deviceTrafficHistoryData && deviceTrafficHistoryData.size() > 0) {
                        for (DeviceTrafficHistoryData dthd : deviceTrafficHistoryData) {
                            //通过“W”判断是否是配料
                            if (dthd.getPid().contains("W")) {
                                //设置数据处理状态
                                dthd.setHandleStatus("1");
                                deviceTrafficHistoryDataMapper.update(dthd, new QueryWrapper<DeviceTrafficHistoryData>().eq("id", dthd.getId()));
                                //计算配料生产总量
                                if (Double.parseDouble(dthd.getV()) > 0 && dthd.getV().length() < 4) {
                                    if (null != dmskl.getBatchingProductionTotal()) {
                                        dmskl.setBatchingProductionTotal(dmskl.getBatchingProductionTotal() + Double.parseDouble(dthd.getV()));
                                    } else {
                                        dmskl.setBatchingProductionTotal(Double.parseDouble(dthd.getV()));
                                    }
                                }
                                deviceMixinStationMapper.update(dmskl, new QueryWrapper<DeviceMixinStation>().eq("id", dmskl.getId()));
                            }
                        }
                    }
                    //一标二号配料信息单独计算
                    if (dmskl.getSid().equals("/usr/plcnet/BP1_2_STA/edge/u")) {
                        oneBidBatchingInformationCalculation(dmskl);
                    }
                }
                //已注浆井数量
                for (DeviceMixinStation dms : deviceMixinStationList) {

                    String bidCode = dms.getBidCode();
                    if ("6-1".equals(bidCode)){
                        bidCode = "2";
                    }
                    String mixinStationCode = dms.getMixinStationCode();
                    Integer number = deviceGroutingPumpMapper.selectGroutingWellsNumber(bidCode, mixinStationCode);
                    dms.setGroutingWellsNumber(number);
                    deviceMixinStationMapper.update(dms, new QueryWrapper<DeviceMixinStation>().eq("id", dms.getId()));
                }
                //注浆总量(每个标下各个注浆泵最新流量计的总和)
                for (DeviceMixinStation dms : deviceMixinStationList) {
                    List<LookDeviceTrafficHistoryDataVo> lookDeviceTrafficHistoryDataVo;
                    if ("/usr/plcnet/BP6_1_STA/edge/u".equals(dms.getSid())) {
                        lookDeviceTrafficHistoryDataVo = deviceTrafficHistoryDataMapper.selectGroutingPumpRealTotal("6_1");
                    } else {
                        lookDeviceTrafficHistoryDataVo = deviceTrafficHistoryDataMapper.selectGroutingPumpRealTotal(dms.getSid().replace("STA", "PUMP"));
                    }
                    Double totalV = 0.00;
                    if (lookDeviceTrafficHistoryDataVo.size() > 0) {
                        for (LookDeviceTrafficHistoryDataVo v : lookDeviceTrafficHistoryDataVo) {
                            totalV += v.getV();
                        }
                    }
                    Double aDouble = deviceMixinStationUpdateLogMapper.countGroutingTotalUpdate(dms.getSid());//注浆总量修正
                    if (null == aDouble) {
                        aDouble = 0.00;
                    }
                    dms.setGroutingTotal(totalV + aDouble);
                    deviceMixinStationMapper.update(dms, new QueryWrapper<DeviceMixinStation>().eq("id", dms.getId()));
                }
            }
            //6标配料信息
            sixBidBatchingInformationCalculation();
            log.info(String.format(" 采空区（搅拌站）定时任务!  时间:" + DateUtils.getTimestamp()));

        }
    }

    //六标配料信息单独计算
    public void sixBidBatchingInformationCalculation() {
        List<DevicePulpingHistoryData> devicePulpingHistoryData = deviceMixinStationDetailsMapper.selectSixMixinStation();
        if (devicePulpingHistoryData.size() > 0) {
            Double s = deviceMixinStationDetailsMapper.countSixRealityCementTotle();
            Double s1 = deviceMixinStationDetailsMapper.countSixRealityFlyAshTotle();
            QueryWrapper<DeviceMixinStation> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("bid_code", "6");
            queryWrapper.eq("mixin_station_code", "1");
            DeviceMixinStation deviceMixinStation = deviceMixinStationMapper.selectOne(queryWrapper);
            if (null == deviceMixinStation) {
                DeviceMixinStation dms = new DeviceMixinStation();
                dms.setBidCode("6");
                dms.setMixinStationCode("1");
                dms.setBatchingProductionTotal(s + s1);
                dms.setSid("/usr/plcnet/BP6_1_STA/edge/u");
                deviceMixinStationService.save(dms);
            } else {
                double v = s + s1;
                if (null != deviceMixinStation.getBatchingProductionTotal()) {
                    deviceMixinStation.setBatchingProductionTotal(v + deviceMixinStation.getBatchingProductionTotal());
                }
                deviceMixinStationMapper.update(deviceMixinStation, new QueryWrapper<DeviceMixinStation>().eq("id", deviceMixinStation.getId()));
            }
            setHandleSixStatus(devicePulpingHistoryData);
        }
    }

    //一标二号站配料信息单独计算
    public void oneBidBatchingInformationCalculation(DeviceMixinStation dmskl) {
        //水泥
        List<DeviceTrafficHistoryData> listCT1S = deviceMixinStationMapper.selectDTHOneBidTwoMixinStationCT1STotal();
        if (listCT1S.size() > 1) {
            DeviceTrafficHistoryData firstData = listCT1S.get(0);
            DeviceTrafficHistoryData lastData = listCT1S.get(listCT1S.size() - 1);
            if (StringUtils.isNotEmpty(firstData.getV()) && StringUtils.isNotEmpty(lastData.getV())) {
                double v = Double.parseDouble(firstData.getV()) - Double.parseDouble(lastData.getV());
                if (v > 0) {
                    setHandleOneBidStatus(listCT1S);
                    dmskl.setBatchingProductionTotal(dmskl.getBatchingProductionTotal() + v * 18);
                    deviceMixinStationMapper.update(dmskl, new QueryWrapper<DeviceMixinStation>().eq("id", dmskl.getId()));
                }
            }
        }
        //粉煤灰
        List<DeviceTrafficHistoryData> listFLY1S = deviceMixinStationMapper.selectDTHOneBidTwoMixinStationFLY1STotal();
        if (listFLY1S.size() > 1) {
            DeviceTrafficHistoryData firstData = listFLY1S.get(0);
            DeviceTrafficHistoryData lastData = listFLY1S.get(listFLY1S.size() - 1);
            if (StringUtils.isNotEmpty(firstData.getV()) && StringUtils.isNotEmpty(lastData.getV())) {
                double v = Double.parseDouble(firstData.getV()) - Double.parseDouble(lastData.getV());
                if (v > 0) {
                    setHandleOneBidStatus(listFLY1S);
                    dmskl.setBatchingProductionTotal(dmskl.getBatchingProductionTotal() + v * 14);
                    deviceMixinStationMapper.update(dmskl, new QueryWrapper<DeviceMixinStation>().eq("id", dmskl.getId()));
                }
            }
        }


    }

    //设置六标数据处理状态
    public void setHandleSixStatus(List<DevicePulpingHistoryData> devicePulpingHistoryData) {
        for (DevicePulpingHistoryData dphd : devicePulpingHistoryData) {
            deviceMixinStationDetailsMapper.updateDevicePulpingHistoryDataTwo(dphd.getId());
        }
    }

    //设置一标二号站数据处理状态
    public void setHandleOneBidStatus(List<DeviceTrafficHistoryData> deviceTrafficHistoryData) {
        DeviceTrafficHistoryData deviceTrafficHistoryDataFirst = deviceTrafficHistoryData.get(0);
        for (DeviceTrafficHistoryData dthd : deviceTrafficHistoryData) {
            if (!dthd.getId().equals(deviceTrafficHistoryDataFirst.getId())) {
                dthd.setHandleStatus("1");
                deviceTrafficHistoryDataMapper.update(dthd, new QueryWrapper<DeviceTrafficHistoryData>().eq("id", dthd.getId()));
            }
        }
    }

    //设置数据处理状态
    public void setHandleStatusPump(List<DeviceTrafficHistoryData> deviceTrafficHistoryDataList) {
        for (DeviceTrafficHistoryData dthd : deviceTrafficHistoryDataList) {
            dthd.setHandleStatus("1");
            deviceTrafficHistoryDataMapper.update(dthd, new QueryWrapper<DeviceTrafficHistoryData>().eq("id", dthd.getId()));
        }
    }
}
