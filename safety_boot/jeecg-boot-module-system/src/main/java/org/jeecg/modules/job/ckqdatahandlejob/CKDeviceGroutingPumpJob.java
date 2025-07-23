package org.jeecg.modules.job.ckqdatahandlejob;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.ckqdatahandle.entity.DeviceGroutingPump;
import com.trtm.iot.ckqdatahandle.mapper.DeviceGroutingPumpMapper;
import com.trtm.iot.ckqdatahandle.service.impl.DeviceGroutingPumpServiceImpl;
import com.trtm.iot.ckqhistorydata.entity.DeviceTrafficHistoryData;
import com.trtm.iot.ckqhistorydata.mapper.DeviceTrafficHistoryDataMapper;
import com.trtm.iot.ckqhistorydata.service.impl.DeviceTrafficHistoryDataServiceImpl;
import com.trtm.iot.ckqrealdata.entity.DeviceTrafficRealdata;
import com.trtm.iot.ckqrealdata.mapper.DeviceTrafficRealdataMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.jeecg.common.util.DateUtils;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

//采空区（注浆泵）定时任务
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class CKDeviceGroutingPumpJob implements Job {


    @Autowired
    private DeviceGroutingPumpMapper deviceGroutingPumpMapper;

    @Autowired
    private DeviceGroutingPumpServiceImpl deviceGroutingPumpService;

    @Autowired
    private DeviceTrafficHistoryDataMapper deviceTrafficHistoryDataMapper;

    @Autowired
    private DeviceTrafficHistoryDataServiceImpl deviceTrafficHistoryDataService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        synchronized (Object.class) {
            // 处理垃圾数据
            List<DeviceTrafficHistoryData> fmList = deviceGroutingPumpMapper.handleTrafficHistoryDataGarbageFM();
            if (fmList.size() > 0) {
                setHandleStatusPump(fmList);
            }
            List<DeviceTrafficHistoryData> pgList = deviceGroutingPumpMapper.handleTrafficHistoryDataGarbagePG();
            if (pgList.size() > 0) {
                setHandleStatusPump(pgList);
            }
            //六标垃圾数据处理
            List<DeviceTrafficHistoryData> sixFmList = deviceGroutingPumpMapper.handleSixTrafficHistoryDataGarbageFM();
            if (sixFmList.size() > 0) {
                setHandleStatusPump(sixFmList);
            }

            //流量计
            List<DeviceTrafficHistoryData> DeviceTrafficHistoryDatas = deviceGroutingPumpMapper.selectHistoryDataByPid("FM");
            if (null != DeviceTrafficHistoryDatas && DeviceTrafficHistoryDatas.size() > 0) {
                for (DeviceTrafficHistoryData dthd : DeviceTrafficHistoryDatas) {
                    String sid = dthd.getTopic();
                    String fmValues = dthd.getPid();
                    QueryWrapper<DeviceTrafficHistoryData> fm = new QueryWrapper<>();
                    fm.eq("topic", sid);
                    fm.eq("pid", fmValues);
                    fm.eq("handle_status_two", "0");
                    fm.lambda().orderByAsc(DeviceTrafficHistoryData::getDatatime);
                    List<DeviceTrafficHistoryData> deviceTrafficHistoryData = deviceTrafficHistoryDataMapper.selectList(fm);
                    QueryWrapper<DeviceGroutingPump> groutingPumpQueryWrapper = new QueryWrapper<>();
                    groutingPumpQueryWrapper.eq("sid", sid);
                    groutingPumpQueryWrapper.eq("grouting_pump_code", getLastString(fmValues));
                    groutingPumpQueryWrapper.eq("is_delete", "0");
                    groutingPumpQueryWrapper.eq("complete_status", "0");
                    DeviceGroutingPump deviceGroutingPump = deviceGroutingPumpMapper.selectOne(groutingPumpQueryWrapper);
                    if (null != deviceGroutingPump) {
                        if (deviceTrafficHistoryData.size() > 1) {
                            //流量计计算
                            if (null == deviceGroutingPump.getGroutingTotal()) {
                                deviceGroutingPump.setGroutingTotal(countPump(deviceTrafficHistoryData));
                            } else {
                                deviceGroutingPump.setGroutingTotal(deviceGroutingPump.getGroutingTotal() + countPump(deviceTrafficHistoryData));
                            }
                            deviceGroutingPumpMapper.update(deviceGroutingPump, new QueryWrapper<DeviceGroutingPump>().eq("id", deviceGroutingPump.getId()));
                        }
                    } else {
                        if (deviceTrafficHistoryData.size() > 1) {
                            Double aDouble = countPump(deviceTrafficHistoryData);
                            if (aDouble != 0.00 && null != aDouble) {//流量计为0时，不保存
                                DeviceGroutingPump dgp = new DeviceGroutingPump();
                                String substring = sid.substring(12, 22);
                                String[] split = substring.split("_");
                                dgp.setSid(sid);
                                dgp.setBidCode(getLastString(split[0]));
                                dgp.setGroutingPumpName(split[1]);
                                dgp.setGroutingPumpCode(getLastString(fmValues));
                                dgp.setGpNameCode(split[1] + "号站" + "-" + getLastString(fmValues) + "号注浆泵");
                                dgp.setGroutingTotal(countPump(deviceTrafficHistoryData));//流量计计算
                                dgp.setGroutingStartTime(deviceTrafficHistoryData.get(0).getDatatime());
                                //判断数据是否已经存在
                                QueryWrapper<DeviceGroutingPump> pumpQueryWrapper = new QueryWrapper<>();
                                pumpQueryWrapper.eq("sid", sid);
                                pumpQueryWrapper.eq("grouting_pump_code", getLastString(fmValues));
                                pumpQueryWrapper.eq("complete_status", "0");
                                List<DeviceGroutingPump> deviceGroutingPumList = deviceGroutingPumpMapper.selectList(pumpQueryWrapper);
                                if (0 == deviceGroutingPumList.size()) {
                                    deviceGroutingPumpService.save(dgp);
                                }
                            }
                        }
                    }
                }
            }
            //六标流量计单独处理(topic=‘6-1’,sid代表井号)
            sixFmHandle();

            //压力和井号
            QueryWrapper<DeviceGroutingPump> qwpfg = new QueryWrapper<>();
            qwpfg.eq("is_delete", "0");
            qwpfg.eq("complete_status", "0");
            List<DeviceGroutingPump> deviceGroutingFG = deviceGroutingPumpMapper.selectList(qwpfg);
            if (deviceGroutingFG.size() > 0) {
                for (DeviceGroutingPump dgp : deviceGroutingFG) {
                    String sid = dgp.getSid();
                    String groutingPumpCode = dgp.getGroutingPumpCode();
                    //压力
                    QueryWrapper<DeviceTrafficHistoryData> queryWrapperPG = new QueryWrapper<>();
                    queryWrapperPG.eq("topic", sid);
                    queryWrapperPG.eq("pid", "PG" + groutingPumpCode);
                    queryWrapperPG.eq("handle_status_two", "0");
                    queryWrapperPG.ne("v", "0");
                    queryWrapperPG.ne("v", "");
                    queryWrapperPG.notLike("v", "-");
                    queryWrapperPG.lambda().orderByDesc(DeviceTrafficHistoryData::getDatatime);//按传输时间的倒序排序
                    List<DeviceTrafficHistoryData> deviceTrafficHistoryData = deviceTrafficHistoryDataMapper.selectList(queryWrapperPG);
                    if (deviceTrafficHistoryData.size() > 0) {
                        seHandleStatus(deviceTrafficHistoryData);
                        if (StringUtils.isNotEmpty(deviceTrafficHistoryData.get(0).getV())) {
                            dgp.setGroutingPressure(new BigDecimal(deviceTrafficHistoryData.get(0).getV()));
                        }
                        deviceGroutingPumpMapper.update(dgp, new QueryWrapper<DeviceGroutingPump>().eq("id", dgp.getId()));
                    }
                    //井号
                    String constructionWellCode = dgp.getConstructionWellCode();
                    if (StringUtils.isEmpty(constructionWellCode)) {
                        QueryWrapper<DeviceTrafficHistoryData> queryWrapperPUMP = new QueryWrapper<>();
                        queryWrapperPUMP.eq("topic", sid);
                        queryWrapperPUMP.eq("pid", "PUMP" + groutingPumpCode);
                        queryWrapperPUMP.eq("handle_status_two", "0");
                        queryWrapperPUMP.ne("v", "0");
                        queryWrapperPUMP.ne("v", "");
                        queryWrapperPUMP.notLike("v", "-");
                        queryWrapperPUMP.lambda().orderByDesc(DeviceTrafficHistoryData::getDatatime);//按传输时间的倒序排序
                        List<DeviceTrafficHistoryData> deviceTrafficHistoryDataPUMP = deviceTrafficHistoryDataMapper.selectList(queryWrapperPUMP);
                        if (deviceTrafficHistoryDataPUMP.size() > 0) {
                            setHandleStatusPump(deviceTrafficHistoryDataPUMP);
                            if (StringUtils.isNotEmpty(deviceTrafficHistoryDataPUMP.get(0).getV())) {
                                dgp.setConstructionWellCode(deviceTrafficHistoryDataPUMP.get(0).getV());
                            }
                            deviceGroutingPumpMapper.update(dgp, new QueryWrapper<DeviceGroutingPump>().eq("id", dgp.getId()));
                        }
                    } else {
                        QueryWrapper<DeviceTrafficHistoryData> queryWrapperPUMP = new QueryWrapper<>();
                        queryWrapperPUMP.eq("topic", sid);
                        queryWrapperPUMP.eq("pid", "PUMP" + groutingPumpCode);
                        queryWrapperPUMP.eq("handle_status_two", "0");
                        queryWrapperPUMP.ne("v", "0");
                        queryWrapperPUMP.ne("v", "");
                        queryWrapperPUMP.notLike("v", "-");
                        queryWrapperPUMP.lambda().orderByDesc(DeviceTrafficHistoryData::getDatatime);//按传输时间的倒序排序
                        List<DeviceTrafficHistoryData> deviceTrafficHistoryDataPUMP = deviceTrafficHistoryDataMapper.selectList(queryWrapperPUMP);
                        if (deviceTrafficHistoryDataPUMP.size() > 0) {
                            if (deviceTrafficHistoryDataPUMP.get(0).getV().equals(dgp.getConstructionWellCode())) {
                                setHandleStatusPump(deviceTrafficHistoryDataPUMP);
                            } else {
                                dgp.setCompleteStatus("1");
                                dgp.setGroutingEndTime(deviceTrafficHistoryDataPUMP.get(0).getDatatime());
                                deviceGroutingPumpMapper.update(dgp, new QueryWrapper<DeviceGroutingPump>().eq("id", dgp.getId()));
                            }
                        }
                    }
                }
            }
            log.info(String.format(" 采空区（注浆泵）定时任务!  时间:" + DateUtils.getTimestamp()));
        }
    }

    //设置数据处理状态
    public void setHandleStatusPump(List<DeviceTrafficHistoryData> deviceTrafficHistoryDataList) {
        for (DeviceTrafficHistoryData dthd : deviceTrafficHistoryDataList) {
            dthd.setHandleStatusTwo("1");
            deviceTrafficHistoryDataMapper.update(dthd, new QueryWrapper<DeviceTrafficHistoryData>().eq("id", dthd.getId()));
        }
    }

    //设置数据处理状态
    public void seHandleStatus(List<DeviceTrafficHistoryData> deviceTrafficHistoryDataList) {
        Integer id = deviceTrafficHistoryDataList.get(0).getId();
        for (DeviceTrafficHistoryData dthd : deviceTrafficHistoryDataList) {
            if (!dthd.getId().equals(id)) {
                dthd.setHandleStatusTwo("1");
                deviceTrafficHistoryDataMapper.update(dthd, new QueryWrapper<DeviceTrafficHistoryData>().eq("id", dthd.getId()));
            }
        }
    }

    /*
     * 流量计如果被断电了或者连接不上流量计就会是0，或者清零了也会是0。
     * 是0的时候就不要进行计算了。如果是0后续直接显示的数值有大于之前非0数据的就进行当天累加计算。
     * 如果是值大于0小于之前的值的就说明流量计被清零，这个时候前面的累加计算截止，
     * 计算从新采集值和0之间的数据进行累加
     * */
    public Double countPump(List<DeviceTrafficHistoryData> deviceTrafficHistoryData) {
        if (deviceTrafficHistoryData.size() > 1) {
            //两条两条数据计算
            double one = Double.parseDouble(deviceTrafficHistoryData.get(0).getV());//第一条数据
            double two = Double.parseDouble(deviceTrafficHistoryData.get(1).getV());//第二条数据
            //清理异常数据
            double v = Math.abs(one - two);
            if (v > 1000) {
                List<DeviceTrafficHistoryData> list = new ArrayList<>();
                list.add(deviceTrafficHistoryData.get(0));
                list.add(deviceTrafficHistoryData.get(1));
                seHandleStatusAll(list);
                return 0.00;
            } else {
                if (0 == one && 0 == two) {
                    List<DeviceTrafficHistoryData> list = new ArrayList<>();
                    list.add(deviceTrafficHistoryData.get(0));
                    list.add(deviceTrafficHistoryData.get(1));
                    seHandleStatusAll(list);
                    return two;
                }
                if (0 != one && 0 != two) {
                    if (Double.doubleToLongBits(two) > Double.doubleToLongBits(one)) {
                        seHandleStatusSingle(deviceTrafficHistoryData.get(0));
                        return two - one;
                    } else {
                        seHandleStatusSingle(deviceTrafficHistoryData.get(1));
                        return 0.00;
                    }
                }
                if (0 == one && 0 != two) {
                    seHandleStatusSingle(deviceTrafficHistoryData.get(0));
                    return two;
                }
                if (0 != one && 0 == two) {
                    if (deviceTrafficHistoryData.size() > 2) {
                        List<DeviceTrafficHistoryData> list = new ArrayList<>();
                        list.add(deviceTrafficHistoryData.get(0));
                        list.add(deviceTrafficHistoryData.get(1));
                        seHandleStatusAll(list);
                        double three = Double.parseDouble(deviceTrafficHistoryData.get(2).getV());//第二条数据
                        if (Double.doubleToLongBits(three) >= Double.doubleToLongBits(one)) {
                            return three - one;
                        } else {
                            return three;
                        }
                    } else {
                        return 0.00;
                    }
                }
            }
        }
        return 0.00;
    }

    public void seHandleStatusAll(List<DeviceTrafficHistoryData> deviceTrafficHistoryDataList) {
        for (DeviceTrafficHistoryData dthd : deviceTrafficHistoryDataList) {
            dthd.setHandleStatusTwo("1");
            deviceTrafficHistoryDataMapper.update(dthd, new QueryWrapper<DeviceTrafficHistoryData>().eq("id", dthd.getId()));
        }
    }

    //设置数据处理状态(单个)
    public void seHandleStatusSingle(DeviceTrafficHistoryData deviceTrafficHistoryData) {
        deviceTrafficHistoryData.setHandleStatusTwo("1");
        deviceTrafficHistoryDataMapper.update(deviceTrafficHistoryData, new QueryWrapper<DeviceTrafficHistoryData>().eq("id", deviceTrafficHistoryData.getId()));
    }

    //获取字符串最后一个字符
    public static String getLastString(String s) {
        return s.substring(s.length() - 1, s.length());
    }

    //注浆泵集合
    public static List<String> getFMList() {
        List<String> list = new ArrayList<>();
        list.add("FM1");
        list.add("FM2");
        list.add("FM3");
        list.add("FM4");
        list.add("FM5");
        list.add("FM6");
        list.add("FM7");
        list.add("FM8");
        return list;
    }

    //六标流量计数据单独处理
    public void sixFmHandle() {
        List<String> fms = getFMList();
        for (String fm : fms) {
            List<DeviceTrafficHistoryData> list = deviceTrafficHistoryDataMapper.selectSixFM(fm,"");
            if (list.size() > 1) {
                List<DeviceTrafficHistoryData> constructionWellCodes = deviceTrafficHistoryDataMapper.selectSixFMWhetherComplete(fm);
                for (DeviceTrafficHistoryData well : constructionWellCodes) {
                    QueryWrapper<DeviceGroutingPump> pumpQueryWrapper = new QueryWrapper<>();
                    pumpQueryWrapper.eq("sid", "/usr/plcnet/BP6_1_PUMP/edge/u");
                    pumpQueryWrapper.eq("grouting_pump_code", getLastString(fm));
                    pumpQueryWrapper.eq("construction_well_code", well.getSid());
                    pumpQueryWrapper.eq("complete_status", "0");
                    DeviceGroutingPump deviceGroutingPump = deviceGroutingPumpMapper.selectOne(pumpQueryWrapper);
                    List<DeviceTrafficHistoryData> listByWell = deviceTrafficHistoryDataMapper.selectSixFM(fm, well.getSid());
                    if (null == deviceGroutingPump) {
                        DeviceGroutingPump dgp = new DeviceGroutingPump();
                        dgp.setBidCode("6");
                        dgp.setGroutingPumpName("1");
                        dgp.setGroutingPumpCode(getLastString(fm));
                        dgp.setGpNameCode(dgp.getGroutingPumpName() + "号站" + "-" + dgp.getGroutingPumpCode() + "号注浆泵");
                        dgp.setGroutingTotal(countPump(listByWell));
                        dgp.setConstructionWellCode(well.getSid());
                        dgp.setSid("/usr/plcnet/BP6_1_PUMP/edge/u");
                        if (dgp.getGroutingTotal() != 0) {
                            dgp.setGroutingStartTime(listByWell.get(listByWell.size() - 1).getDatatime());
                        }
                        deviceGroutingPumpService.save(dgp);
                    } else {
                        if (null == deviceGroutingPump.getGroutingTotal()) {
                            deviceGroutingPump.setGroutingTotal(countPump(listByWell));
                        } else {
                            deviceGroutingPump.setGroutingTotal(deviceGroutingPump.getGroutingTotal() + countPump(listByWell));
                        }
                        if (null == deviceGroutingPump.getGroutingStartTime()) {
                            deviceGroutingPump.setGroutingStartTime(listByWell.get(listByWell.size() - 1).getDatatime());
                        }
                        deviceGroutingPumpMapper.update(deviceGroutingPump, new QueryWrapper<DeviceGroutingPump>().eq("id", deviceGroutingPump.getId()));
                    }
                }
                //判断是否住满及更换井号
                List<DeviceTrafficHistoryData> whetherFM = deviceTrafficHistoryDataMapper.selectSixFMWhetherComplete(fm);
                if (whetherFM.size() > 1) {
                    QueryWrapper<DeviceGroutingPump> queryWrapperPump = new QueryWrapper<>();
                    queryWrapperPump.eq("sid", "/usr/plcnet/BP6_1_PUMP/edge/u");
                    queryWrapperPump.eq("grouting_pump_code", getLastString(fm));
                    queryWrapperPump.eq("complete_status", "0");
                    queryWrapperPump.eq("construction_well_code", whetherFM.get(0).getSid());
                    DeviceGroutingPump dgp = deviceGroutingPumpMapper.selectOne(queryWrapperPump);
                    if (null != dgp) {
                        //住满设置住满信息
                        List<DeviceTrafficHistoryData> dthdList =deviceTrafficHistoryDataMapper.selectSixFM(fm,whetherFM.get(0).getSid());
                        dgp.setGroutingEndTime(dthdList.get(0).getDatatime());
                        if (dthdList.size() == 1) {
                            seHandleStatusSingle(dthdList.get(0));
                            dgp.setCompleteStatus("1");
                        }
                        if (dthdList.size() > 1) {
                            dgp.setGroutingTotal(dgp.getGroutingTotal() + countPump(dthdList));
                        }
                        deviceGroutingPumpMapper.update(dgp, new QueryWrapper<DeviceGroutingPump>().eq("id", dgp.getId()));
                    }

                }
            }
        }
    }
}
