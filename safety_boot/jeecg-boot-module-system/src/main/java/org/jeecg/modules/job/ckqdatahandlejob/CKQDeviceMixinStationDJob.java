package org.jeecg.modules.job.ckqdatahandlejob;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.ckqdatahandle.entity.DeviceGroutingPumpDay;
import com.trtm.iot.ckqdatahandle.entity.DeviceMixinStation;
import com.trtm.iot.ckqdatahandle.entity.DeviceMixinStationDetails;
import com.trtm.iot.ckqdatahandle.entity.DevicePulpingHistoryData;
import com.trtm.iot.ckqdatahandle.mapper.DeviceMixinStationDetailsMapper;
import com.trtm.iot.ckqdatahandle.mapper.DeviceMixinStationMapper;
import com.trtm.iot.ckqdatahandle.service.impl.DeviceMixinStationDetailsServiceImpl;
import com.trtm.iot.ckqhistorydata.entity.DeviceTrafficHistoryData;
import com.trtm.iot.ckqhistorydata.mapper.DeviceTrafficHistoryDataMapper;
import com.trtm.iot.ckqrealdata.mapper.DeviceTrafficRealdataMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.formula.functions.T;
import org.jeecg.common.util.DateUtils;
import org.quartz.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Calendar;

//采空区（搅拌站详情）定时任务
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class CKQDeviceMixinStationDJob implements Job {

    private String parameter;

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }

    @Autowired
    private DeviceTrafficRealdataMapper deviceTrafficRealdataMapper;

    @Autowired
    private DeviceMixinStationDetailsMapper deviceMixinStationDetailsMapper;

    @Autowired
    private DeviceMixinStationDetailsServiceImpl deviceMixinStationDetailsServiceImpl;

    @Autowired
    private DeviceTrafficHistoryDataMapper deviceTrafficHistoryDataMapper;

    @SneakyThrows
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        synchronized (Object.class) {
            //处理垃圾数据
            List<DeviceTrafficHistoryData> deviceTrafficHistoryDataGarbage = deviceMixinStationDetailsMapper.selectGarbageDateMixinStation();
            if (deviceTrafficHistoryDataGarbage.size() > 0) {
                setHandleStatus(deviceTrafficHistoryDataGarbage);
            }
            //处理实际值过大
            List<DeviceTrafficHistoryData> bigMixinStationD = deviceMixinStationDetailsMapper.selectGarbageDateBigMixinStationD();
            if (bigMixinStationD.size() > 0) {
                setHandleStatus(bigMixinStationD);
            }
            QueryWrapper<DeviceMixinStationDetails> queryWrapper = new QueryWrapper<>();
            queryWrapper.like("upload_time", getStringTime());
            queryWrapper.eq("is_delete", "1");
            List<DeviceMixinStationDetails> deviceMixinStationDetailList = deviceMixinStationDetailsMapper.selectList(queryWrapper);
            if (deviceMixinStationDetailList.size() == 0) {
                getDeviceMixinStationDayBase();
            }
            if (null != deviceMixinStationDetailList && deviceMixinStationDetailList.size() > 0) {
                for (DeviceMixinStationDetails dmsd : deviceMixinStationDetailList) {
                    String bidCode = dmsd.getBidCode();
                    String mixinStationCode = dmsd.getMixinStationCode();
                    //1标段1号搅拌站
                    if (bidCode.equals("1") && mixinStationCode.equals("1")) {
                        proportioningCalculationMethod(dmsd);
                    }
                    if (bidCode.equals("1") && mixinStationCode.equals("2")) {
                        //一标二号站单独处理
                        OneBidTwoMixinStation(dmsd);
                    }
                    //2标段1号搅拌站
                    if (bidCode.equals("2") && mixinStationCode.equals("1")) {
                        proportioningCalculationMethod(dmsd);
                    }
                    //3标段1号搅拌站
                    if (bidCode.equals("3") && mixinStationCode.equals("1")) {
                        proportioningCalculationMethod(dmsd);
                    }
                    //4标段1号搅拌站(没有实际值)
                    if (bidCode.equals("4") && mixinStationCode.equals("1")) {
                        proportioningFourCalculationMethod(dmsd);
                    }
                    //5标段1号搅拌站
                    if (bidCode.equals("5") && mixinStationCode.equals("1")) {
                        proportioningCalculationMethod(dmsd);
                    }
                    //6标段1号搅拌站
                    if (bidCode.equals("6") && mixinStationCode.equals("1")) {
                        sixBidData(dmsd);
                    }
                }
            }
            log.info(String.format(" 采空区（搅拌站-详情）定时任务!  时间:" + DateUtils.getTimestamp()));
        }
    }


    //配料计算方法
    public void proportioningCalculationMethod(DeviceMixinStationDetails dmsd) throws ParseException {
        //水泥1
        List<DeviceTrafficHistoryData> listCt1 = deviceTrafficHistoryDataMapper.selectDTHBid(dmsd.getSid(), getStringTime(), "CT1_W");
        if (listCt1.size() > 0) {
            Double countCT1 = deviceTrafficHistoryDataMapper.selectDTHBidCount(dmsd.getSid(), getStringTime(), "CT1_W");
            dmsd.setTodayCement(dmsd.getTodayCement() + countCT1);
            deviceMixinStationDetailsMapper.update(dmsd, new QueryWrapper<DeviceMixinStationDetails>().eq("id", dmsd.getId()));
            setHandleStatus(listCt1);
        }
        //水泥2
        List<DeviceTrafficHistoryData> listCt2 = deviceTrafficHistoryDataMapper.selectDTHBid(dmsd.getSid(), getStringTime(), "CT2_W");
        if (listCt2.size() > 0) {
            Double countCT2 = deviceTrafficHistoryDataMapper.selectDTHBidCount(dmsd.getSid(), getStringTime(), "CT2_W");
            dmsd.setTodayCement(dmsd.getTodayCement() + countCT2);
            deviceMixinStationDetailsMapper.update(dmsd, new QueryWrapper<DeviceMixinStationDetails>().eq("id", dmsd.getId()));
            setHandleStatus(listCt2);
        }
        //粉煤灰1
        List<DeviceTrafficHistoryData> listFly1 = deviceTrafficHistoryDataMapper.selectDTHBid(dmsd.getSid(), getStringTime(), "FLY1_W");
        if (listFly1.size() > 0) {
            Double countFLY1 = deviceTrafficHistoryDataMapper.selectDTHBidCount(dmsd.getSid(), getStringTime(), "FLY1_W");
            dmsd.setTodayFlyAsh(dmsd.getTodayFlyAsh() + countFLY1);
            deviceMixinStationDetailsMapper.update(dmsd, new QueryWrapper<DeviceMixinStationDetails>().eq("id", dmsd.getId()));
            setHandleStatus(listFly1);
        }
        //粉煤灰2
        List<DeviceTrafficHistoryData> listFly2 = deviceTrafficHistoryDataMapper.selectDTHBid(dmsd.getSid(), getStringTime(), "FLY2_W");
        if (listFly2.size() > 0) {
            Double countFLY2 = deviceTrafficHistoryDataMapper.selectDTHBidCount(dmsd.getSid(), getStringTime(), "FLY2_W");
            dmsd.setTodayFlyAsh(dmsd.getTodayFlyAsh() + countFLY2);
            deviceMixinStationDetailsMapper.update(dmsd, new QueryWrapper<DeviceMixinStationDetails>().eq("id", dmsd.getId()));
            setHandleStatus(listFly2);
        }
        Double totalCement = deviceMixinStationDetailsMapper.selectMSDTotalCement(dmsd.getSid());
        Double totalFlyAsh = deviceMixinStationDetailsMapper.selectMSDTotalFlyAsh(dmsd.getSid());
        dmsd.setTotalCement(totalCement);
        dmsd.setTotalFlyAsh(totalFlyAsh);
        deviceMixinStationDetailsMapper.update(dmsd, new QueryWrapper<DeviceMixinStationDetails>().eq("id", dmsd.getId()));

    }

    //每天都生成基础数据
    public void getDeviceMixinStationDayBase() throws ParseException {
        List<DeviceMixinStationDetails> list = new ArrayList<>();
        QueryWrapper<DeviceMixinStationDetails> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("upload_time", getStringTime());
        queryWrapper.eq("is_delete", "1");
        List<DeviceMixinStationDetails> deviceMixinStationDetails = deviceMixinStationDetailsMapper.selectList(queryWrapper);
        if (deviceMixinStationDetails.size() == 0) {
            list.add(new DeviceMixinStationDetails("1", "1", getDateTime()));
            list.add(new DeviceMixinStationDetails("1", "2", getDateTime()));
            list.add(new DeviceMixinStationDetails("2", "1", getDateTime()));
            list.add(new DeviceMixinStationDetails("3", "1", getDateTime()));
            list.add(new DeviceMixinStationDetails("4", "1", getDateTime()));
            list.add(new DeviceMixinStationDetails("5", "1", getDateTime()));
            list.add(new DeviceMixinStationDetails("6", "1", getDateTime()));
            deviceMixinStationDetailsServiceImpl.saveBatch(list);
        }
    }

    //6标数据单独计算
    public void sixBidData(DeviceMixinStationDetails dmsd)  {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
        String format = df.format(new Date());
        List<DevicePulpingHistoryData> devicePulpingHistoryData = deviceMixinStationDetailsMapper.selectSixMixinStationDetails(format);
        if (devicePulpingHistoryData.size() > 0) {
            //计算配料信息
            Double cement = deviceMixinStationDetailsMapper.countSixRealityCement(format);
            Double flyAsh = deviceMixinStationDetailsMapper.countSixRealityFlyAsh(format);
            dmsd.setTodayCement(dmsd.getTodayCement() + cement);
            dmsd.setTodayFlyAsh(dmsd.getTodayFlyAsh() + flyAsh);
            deviceMixinStationDetailsMapper.update(dmsd, new QueryWrapper<DeviceMixinStationDetails>().eq("id", dmsd.getId()));
            //设置6标数据处理状态
            setHandleSixStatus(devicePulpingHistoryData);
        }
        Double totalCement = deviceMixinStationDetailsMapper.selectMSDTotalCement(dmsd.getSid());
        Double totalFlyAsh = deviceMixinStationDetailsMapper.selectMSDTotalFlyAsh(dmsd.getSid());
        dmsd.setTotalCement(totalCement);
        dmsd.setTotalFlyAsh(totalFlyAsh);
        deviceMixinStationDetailsMapper.update(dmsd, new QueryWrapper<DeviceMixinStationDetails>().eq("id", dmsd.getId()));

    }

    //一标二号站数据单独计算
    public void OneBidTwoMixinStation(DeviceMixinStationDetails dmsd) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
        String format = df.format(new Date());
        List<DeviceTrafficHistoryData> listCT1 = deviceTrafficHistoryDataMapper.selectDTHOneBidTwoMixinStationCT1S(format);
        List<DeviceTrafficHistoryData> listFLY1S = deviceTrafficHistoryDataMapper.selectDTHOneBidTwoMixinStationFLY1S(format);
        if (listCT1.size() > 1) {
            //最新的一条数据
            String newData = listCT1.get(0).getV();
            //最后一条数据
            String oldData = listCT1.get(listCT1.size() - 1).getV();
            if (Double.doubleToLongBits(Double.parseDouble(newData)) > Double.doubleToLongBits(Double.parseDouble(oldData))) {
                Double ct1 = Double.parseDouble(newData) - Double.parseDouble(oldData);
                dmsd.setTodayCement(dmsd.getTodayCement() + ct1 * 18);
                deviceMixinStationDetailsMapper.update(dmsd, new QueryWrapper<DeviceMixinStationDetails>().eq("id", dmsd.getId()));
            }
            setHandleOneBidTwoMixinStation(listCT1);
        }
        if (listFLY1S.size() > 1) {
            //最新的一条数据
            String newData = listFLY1S.get(0).getV();
            //最后一条数据
            String oldData = listFLY1S.get(listFLY1S.size() - 1).getV();
            if (Double.doubleToLongBits(Double.parseDouble(newData)) > Double.doubleToLongBits(Double.parseDouble(oldData))) {
                Double fly1s = Double.parseDouble(newData) - Double.parseDouble(oldData);
                dmsd.setTodayFlyAsh(dmsd.getTodayFlyAsh() + fly1s * 14);
                deviceMixinStationDetailsMapper.update(dmsd, new QueryWrapper<DeviceMixinStationDetails>().eq("id", dmsd.getId()));
            }
            setHandleOneBidTwoMixinStation(listFLY1S);
        }
        Double totalCement = deviceMixinStationDetailsMapper.selectMSDTotalCement(dmsd.getSid());
        Double totalFlyAsh = deviceMixinStationDetailsMapper.selectMSDTotalFlyAsh(dmsd.getSid());
        dmsd.setTotalCement(totalCement);
        dmsd.setTotalFlyAsh(totalFlyAsh);
        deviceMixinStationDetailsMapper.update(dmsd, new QueryWrapper<DeviceMixinStationDetails>().eq("id", dmsd.getId()));
    }

    //四标一号站单独处理
    public void proportioningFourCalculationMethod(DeviceMixinStationDetails dmsd) throws ParseException {
        //水泥1
        List<DeviceTrafficHistoryData> listCt1 = deviceTrafficHistoryDataMapper.selectDTHBid(dmsd.getSid(), getStringTime(), "CT1");
        if (listCt1.size() > 0) {
            Double countCT1 = deviceTrafficHistoryDataMapper.selectDTHBidCount(dmsd.getSid(), getStringTime(), "CT1");
            dmsd.setTodayCement(dmsd.getTodayCement() + countCT1);
            deviceMixinStationDetailsMapper.update(dmsd, new QueryWrapper<DeviceMixinStationDetails>().eq("id", dmsd.getId()));
            setHandleStatus(listCt1);
        }
        //水泥2
        List<DeviceTrafficHistoryData> listCt2 = deviceTrafficHistoryDataMapper.selectDTHBid(dmsd.getSid(), getStringTime(), "CT2");
        if (listCt2.size() > 0) {
            Double countCT2 = deviceTrafficHistoryDataMapper.selectDTHBidCount(dmsd.getSid(), getStringTime(), "CT2");
            dmsd.setTodayCement(dmsd.getTodayCement() + countCT2);
            deviceMixinStationDetailsMapper.update(dmsd, new QueryWrapper<DeviceMixinStationDetails>().eq("id", dmsd.getId()));
            setHandleStatus(listCt2);
        }
        //粉煤灰1
        List<DeviceTrafficHistoryData> listFly1 = deviceTrafficHistoryDataMapper.selectDTHBid(dmsd.getSid(), getStringTime(), "FLY1");
        if (listFly1.size() > 0) {
            Double countFLY1 = deviceTrafficHistoryDataMapper.selectDTHBidCount(dmsd.getSid(), getStringTime(), "FLY1");
            dmsd.setTodayFlyAsh(dmsd.getTodayFlyAsh() + countFLY1);
            deviceMixinStationDetailsMapper.update(dmsd, new QueryWrapper<DeviceMixinStationDetails>().eq("id", dmsd.getId()));
            setHandleStatus(listFly1);
        }
        //粉煤灰2
        List<DeviceTrafficHistoryData> listFly2 = deviceTrafficHistoryDataMapper.selectDTHBid(dmsd.getSid(), getStringTime(), "FLY2");
        if (listFly2.size() > 0) {
            Double countFLY2 = deviceTrafficHistoryDataMapper.selectDTHBidCount(dmsd.getSid(), getStringTime(), "FLY2");
            dmsd.setTodayFlyAsh(dmsd.getTodayFlyAsh() + countFLY2);
            deviceMixinStationDetailsMapper.update(dmsd, new QueryWrapper<DeviceMixinStationDetails>().eq("id", dmsd.getId()));
            setHandleStatus(listFly2);
        }
        Double totalCement = deviceMixinStationDetailsMapper.selectMSDTotalCement(dmsd.getSid());
        Double totalFlyAsh = deviceMixinStationDetailsMapper.selectMSDTotalFlyAsh(dmsd.getSid());
        dmsd.setTotalCement(totalCement);
        dmsd.setTotalFlyAsh(totalFlyAsh);
        deviceMixinStationDetailsMapper.update(dmsd, new QueryWrapper<DeviceMixinStationDetails>().eq("id", dmsd.getId()));
    }

    //计算误差率方法
    public static Double calculation(Double theory, Double reality) {
        if (null != theory && null != reality) {
            double v = theory - reality;
            DecimalFormat df = new DecimalFormat("0.00");//设置保留位数
            String format = df.format(v / theory * 100);
            double abs = Math.abs(Double.parseDouble(format));//绝对值
            return abs;
        }
        return null;
    }

    //计算总量
    public Double add(DeviceMixinStationDetails deviceMixinStationDetails) {
        Double realityCement = deviceMixinStationDetails.getRealityCement();
        Double realityFlyAsh = deviceMixinStationDetails.getRealityFlyAsh();
        Double rco = 0.00;
        Double rfao = 0.00;
        if (null != realityCement) {
            rco = realityCement;
        }
        if (null != realityFlyAsh) {
            rfao = realityFlyAsh;
        }
        return rco + rfao;
    }

    //设置数据处理状态
    public void setHandleStatus(List<DeviceTrafficHistoryData> deviceTrafficHistoryDataList) {
        for (DeviceTrafficHistoryData dthd : deviceTrafficHistoryDataList) {
            dthd.setHandleStatusTwo("1");
            deviceTrafficHistoryDataMapper.update(dthd, new QueryWrapper<DeviceTrafficHistoryData>().eq("id", dthd.getId()));
        }
    }

    //6标数据处理状态
    public void setHandleSixStatus(List<DevicePulpingHistoryData> devicePulpingHistoryData) {
        for (DevicePulpingHistoryData dphd : devicePulpingHistoryData) {
            deviceMixinStationDetailsMapper.updateDevicePulpingHistoryData(dphd.getId());
        }
    }

    //一标二号站设置数据处理状态
    public void setHandleOneBidTwoMixinStation(List<DeviceTrafficHistoryData> deviceTrafficHistoryDataList) {
        Integer id = deviceTrafficHistoryDataList.get(0).getId();
        for (DeviceTrafficHistoryData dthd : deviceTrafficHistoryDataList) {
            if (!dthd.getId().equals(id)) {
                dthd.setHandleStatusTwo("1");
                deviceTrafficHistoryDataMapper.update(dthd, new QueryWrapper<DeviceTrafficHistoryData>().eq("id", dthd.getId()));
            }
        }
    }

    public  String getStringTime() throws ParseException {
        Calendar ca = Calendar.getInstance();//得到一个Calendar的实例
        ca.setTime(new Date()); //设置时间为当前时间
        ca.add(Calendar.DATE, -Integer.parseInt(parameter));//前parameter天
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
        String format = df.format(ca.getTime());
        return format;
    }

    public  Date getDateTime() throws ParseException {
        Calendar ca = Calendar.getInstance();//得到一个Calendar的实例
        ca.setTime(new Date()); //设置时间为当前时间
        ca.add(Calendar.DATE, -Integer.parseInt(parameter));//前parameter天
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
        String format = df.format(ca.getTime());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");//格式时间对象
        Date date = sdf.parse(format);
        return date;
    }


}
