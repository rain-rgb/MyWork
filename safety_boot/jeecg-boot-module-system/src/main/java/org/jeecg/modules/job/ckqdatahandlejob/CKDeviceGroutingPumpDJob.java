/**
 * @ClassName CKDeviceGroutingPumpDJob.java
 * @author zgq
 * @version 1.0.0
 * @Description TODO
 * @createTime 2021年07月09日 09:45:00
 */
package org.jeecg.modules.job.ckqdatahandlejob;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.ckqdatahandle.entity.DeviceGroutingPumpDay;
import com.trtm.iot.ckqdatahandle.mapper.DeviceGroutingPumpDayMapper;
import com.trtm.iot.ckqdatahandle.mapper.DeviceGroutingPumpMapper;
import com.trtm.iot.ckqdatahandle.service.impl.DeviceGroutingPumpDayServiceImpl;
import com.trtm.iot.ckqhistorydata.entity.DeviceTrafficHistoryData;
import com.trtm.iot.ckqhistorydata.mapper.DeviceTrafficHistoryDataMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.DateUtils;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

//采空区注浆泵日流量计
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class CKDeviceGroutingPumpDJob implements Job {

    @Autowired
    private DeviceGroutingPumpDayMapper deviceGroutingPumpDayMapper;

    @Autowired
    private DeviceTrafficHistoryDataMapper deviceTrafficHistoryDataMapper;

    @Autowired
    private DeviceGroutingPumpMapper deviceGroutingPumpMapper;

    @Autowired
    private DeviceGroutingPumpDayServiceImpl deviceGroutingPumpDayService;

    @SneakyThrows
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        synchronized (Object.class) {
            Calendar ca = Calendar.getInstance();//得到一个Calendar的实例
            ca.setTime(new Date()); //设置时间为当前时间
            ca.add(Calendar.DATE, -1);
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
            String format = df.format(ca.getTime());
            QueryWrapper<DeviceGroutingPumpDay> queryWrapper = new QueryWrapper<>();
            queryWrapper.like("time", format);
            List<DeviceGroutingPumpDay> DeviceGroutingPumpDays = deviceGroutingPumpDayMapper.selectList(queryWrapper);
            if (DeviceGroutingPumpDays.size() == 0) {
                getDeviceGroutingPumpDayBase();
            }
            if (DeviceGroutingPumpDays.size() > 0) {
                for (DeviceGroutingPumpDay dgpd : DeviceGroutingPumpDays) {
                    DeviceTrafficHistoryData deviceTrafficHistoryData;
                    QueryWrapper<DeviceTrafficHistoryData> historyDataQueryWrapper = new QueryWrapper<>();
                    historyDataQueryWrapper.eq("handle_status_three", "0");
                    historyDataQueryWrapper.like("dataTime", format);
                    //六标流量计单独计算
                    if ("/usr/plcnet/BP6_1_PUMP/edge/u".equals(dgpd.getSid())) {
                        historyDataQueryWrapper.eq("topic", "6_1");
                        deviceTrafficHistoryData = deviceGroutingPumpDayMapper.selectDTHYesterdayOneData("6_1", "FM" + dgpd.getGroutingPumpCode(), getStringDate());
                    } else {
                        historyDataQueryWrapper.eq("topic", dgpd.getSid());
                        deviceTrafficHistoryData = deviceGroutingPumpDayMapper.selectDTHYesterdayOneData(dgpd.getSid(), "FM" + dgpd.getGroutingPumpCode(), getStringDate());
                    }
                    historyDataQueryWrapper.eq("pid", "FM" + dgpd.getGroutingPumpCode());
                    historyDataQueryWrapper.ne("v", "");
                    historyDataQueryWrapper.ne("v", 0);
                    historyDataQueryWrapper.lambda().orderByAsc(DeviceTrafficHistoryData::getDatatime);//按传输时间的倒序排序
                    List<DeviceTrafficHistoryData> deviceTrafficHistoryDatas = deviceTrafficHistoryDataMapper.selectList(historyDataQueryWrapper);
                    if (deviceTrafficHistoryDatas.size() > 1) {
                        if (null != deviceTrafficHistoryData) {
                            deviceTrafficHistoryDatas.add(0,deviceTrafficHistoryData);
                        }
                        Double groutingTotal = dgpd.getGroutingTotal();
                        if (null != groutingTotal) {
                            dgpd.setGroutingTotal(groutingTotal + countPumpDay(deviceTrafficHistoryDatas));
                        } else {
                            dgpd.setGroutingTotal(countPumpDay(deviceTrafficHistoryDatas));
                        }
                        deviceGroutingPumpDayMapper.update(dgpd, new QueryWrapper<DeviceGroutingPumpDay>().eq("id", dgpd.getId()));
                    }
                    //计算当日某个标段下的所有注浆泵累积量
                    Double todayTotal = deviceGroutingPumpDayMapper.selectDGPDTodayTotalByBidCode(dgpd.getSid(), format);
                    dgpd.setTodayGroutingTotal(todayTotal);
                    //计算至今某个标段下的所有注浆泵累积量
                    Double allDayTotal = deviceGroutingPumpDayMapper.selectDGPDAllDayTotalByBidCode(dgpd.getSid(), format);
                    dgpd.setAllDayGroutingTotal(allDayTotal);
                    deviceGroutingPumpDayMapper.update(dgpd, new QueryWrapper<DeviceGroutingPumpDay>().eq("id", dgpd.getId()));
                }
            }
            log.info(String.format(" 采空区（注浆泵）日定时任务!  时间:" + DateUtils.getTimestamp()));
        }
    }

    /*
     * 每天只计算一次
     * */
    public Double countPumpDay(List<DeviceTrafficHistoryData> deviceTrafficHistoryData) {
        double one = Double.parseDouble(deviceTrafficHistoryData.get(0).getV());//第一条数据
        double two = Double.parseDouble(deviceTrafficHistoryData.get(deviceTrafficHistoryData.size() - 1).getV());//最后一条数据
        if (Double.doubleToLongBits(two) > Double.doubleToLongBits(one)) {
            seHandleStatusAll(deviceTrafficHistoryData);
            return two - one;
        }
        return 0.00;
    }

    public void seHandleStatusAll(List<DeviceTrafficHistoryData> deviceTrafficHistoryDataList) {
        Integer id = deviceTrafficHistoryDataList.get(deviceTrafficHistoryDataList.size() - 1).getId();
        for (DeviceTrafficHistoryData dthd : deviceTrafficHistoryDataList) {
            if (!dthd.getId().equals(id)) {
                dthd.setHandleStatusThree("1");
                deviceTrafficHistoryDataMapper.update(dthd, new QueryWrapper<DeviceTrafficHistoryData>().eq("id", dthd.getId()));
            }

        }
    }

    //设置数据处理状态(单个)
    public void seHandleStatusSingle(DeviceTrafficHistoryData deviceTrafficHistoryData) {
        deviceTrafficHistoryData.setHandleStatusThree("1");
        deviceTrafficHistoryDataMapper.update(deviceTrafficHistoryData, new QueryWrapper<DeviceTrafficHistoryData>().eq("id", deviceTrafficHistoryData.getId()));
    }

    //每个每天注浆泵都生成一条数据，不管有没有流量计的变化
    public void getDeviceGroutingPumpDayBase() throws ParseException {
        Calendar ca = Calendar.getInstance();//得到一个Calendar的实例
        ca.setTime(new Date()); //设置时间为当前时间
        ca.add(Calendar.DATE, -1);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
        String format = df.format(ca.getTime());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");//格式时间对象
        Date date = sdf.parse(format);
        List<DeviceGroutingPumpDay> list = new ArrayList<>();
        QueryWrapper<DeviceGroutingPumpDay> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("time", format);
        List<DeviceGroutingPumpDay> deviceGroutingPumpDays = deviceGroutingPumpDayMapper.selectList(queryWrapper);
        if (deviceGroutingPumpDays.size() == 0) {
            for (int i = 1; i <= 8; i++) {
                list.add(new DeviceGroutingPumpDay("1", "1", i + "", date));
                list.add(new DeviceGroutingPumpDay("1", "2", i + "", date));
                list.add(new DeviceGroutingPumpDay("2", "1", i + "", date));
                list.add(new DeviceGroutingPumpDay("3", "1", i + "", date));
                list.add(new DeviceGroutingPumpDay("4", "1", i + "", date));
                list.add(new DeviceGroutingPumpDay("5", "1", i + "", date));
                list.add(new DeviceGroutingPumpDay("6", "1", i + "", date));
            }
            list.add(new DeviceGroutingPumpDay("1", "2", 10 + "", date));
            list.add(new DeviceGroutingPumpDay("1", "2", 11 + "", date));
            list.add(new DeviceGroutingPumpDay("2", "1", 9 + "", date));
            list.add(new DeviceGroutingPumpDay("2", "1", 10 + "", date));
            list.add(new DeviceGroutingPumpDay("2", "1", 11 + "", date));
            deviceGroutingPumpDayService.saveBatch(list);
        }
    }
    //前两天
    public String getStringDate() {
        Calendar ca = Calendar.getInstance();//得到一个Calendar的实例
        ca.setTime(new Date()); //设置时间为当前时间
        ca.add(Calendar.DATE, -2);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
        String format = df.format(ca.getTime());
        return format;
    }
}


