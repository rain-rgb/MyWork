/**
 * @ClassName DeviceGroutingPumpDayServiceImpl.java
 * @author zgq
 * @version 1.0.0
 * @Description TODO
 * @createTime 2021年07月09日 09:42:00
 */
package com.trtm.iot.ckqdatahandle.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.trtm.iot.ckqdatahandle.entity.DeviceGroutingPumpDay;
import com.trtm.iot.ckqdatahandle.entity.DeviceMixinStationDetails;
import com.trtm.iot.ckqdatahandle.mapper.DeviceGroutingPumpDayMapper;
import com.trtm.iot.ckqdatahandle.service.IDeviceGroutingPumpDayService;
import com.trtm.iot.ckqdatahandle.vo.DeviceGroutingPumpDayDetailsVo;
import com.trtm.iot.ckqdatahandle.vo.DeviceGroutingPumpDayVo;
import com.trtm.iot.ckqdatahandle.vo.DeviceGroutingPumpVo;
import com.trtm.iot.ckqhistorydata.entity.DeviceTrafficHistoryData;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class DeviceGroutingPumpDayServiceImpl extends ServiceImpl<DeviceGroutingPumpDayMapper, DeviceGroutingPumpDay> implements IDeviceGroutingPumpDayService {

    @Autowired
    private DeviceGroutingPumpDayMapper deviceGroutingPumpDayMapper;
    @Autowired
    private RedisUtil redisUtil;

    @Override
    public IPage<DeviceGroutingPumpDay> selectDeviceGroutingPumpDayListPage(DeviceGroutingPumpVo deviceGroutingPumpVo) {
        IPage<DeviceGroutingPumpDay> page = new Page<>(deviceGroutingPumpVo.getPageNo(), deviceGroutingPumpVo.getPageSize());
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String  shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        //查询到他的设备编号
        String[] split = shebei.split(",");
        List<String> shebeilist = new ArrayList<>();
        Collections.addAll(shebeilist, split);
        QueryWrapper<DeviceGroutingPumpDay> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_delete", "0");
        queryWrapper.in("sid",shebeilist);//用户权限下设备编号
        //标段编号
        String bidCode = deviceGroutingPumpVo.getBidCode();
        if (StringUtils.isNotEmpty(bidCode)) {
            queryWrapper.eq("bid_code", bidCode);
        }
        //注浆泵名称
        String groutingPumpName = deviceGroutingPumpVo.getGroutingPumpName();
        if (StringUtils.isNotEmpty(groutingPumpName)) {
            queryWrapper.eq("grouting_pump_name", groutingPumpName);
        }
        //注浆泵编号
        String groutingPumpCode = deviceGroutingPumpVo.getGroutingPumpCode();
        if (StringUtils.isNotEmpty(groutingPumpCode)) {
            queryWrapper.eq("grouting_pump_code", groutingPumpCode);
        }
        //时间
        String startTime = deviceGroutingPumpVo.getStartTime();
        if (StringUtils.isNotEmpty(startTime)) {
            queryWrapper.eq("time", startTime);
        }
        /*String endTime = deviceGroutingPumpVo.getEndTime();
        queryWrapper.apply(StringUtils.isNotBlank(startTime),
                "date_format (time,'%Y-%m-%d %H:%i:%s') >= date_format('" + startTime + "','%Y-%m-%d %H:%i:%s')")
                .apply(StringUtils.isNotBlank(endTime),
                        "date_format (time,'%Y-%m-%d %H:%i:%s') <= date_format('" + endTime + "','%Y-%m-%d %H:%i:%s')");*/
        queryWrapper.lambda().orderByDesc(DeviceGroutingPumpDay::getTime);
        queryWrapper.lambda().orderByAsc(DeviceGroutingPumpDay::getBidCode);
        queryWrapper.lambda().orderByAsc(DeviceGroutingPumpDay::getGroutingPumpName);
        queryWrapper.lambda().orderByAsc(DeviceGroutingPumpDay::getGroutingPumpCode);
        IPage<DeviceGroutingPumpDay> result = this.page(page, queryWrapper);
        Double total = 0.00;
        if (result.getRecords().size() > 0) {
            for (DeviceGroutingPumpDay dgpd : result.getRecords()) {
                total += dgpd.getGroutingTotal();
            }
            result.getRecords().get(0).setTodayGroutingTotal(total);
        }
        return result;
    }

    /*
     * @Author zgq
     * @Description
     * @Date 2021/8/3 18:03
     * @Param
     * @return
     **/
    @Override
    public List<DeviceGroutingPumpDayVo> selectDeviceGroutingPumpDayVoList(String time) {
        Calendar ca = Calendar.getInstance();//得到一个Calendar的实例
        ca.setTime(new Date()); //设置时间为当前时间
        ca.add(Calendar.DATE, -1);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
        String format = df.format(ca.getTime());
        String selectTime = null;
        if (StringUtils.isEmpty(time)) {
            selectTime = format;
        }else {
            selectTime =time;
        }
        List<DeviceGroutingPumpDayVo> deviceGroutingPumpDayVoList = new ArrayList<>();
        for (String bid : getBid()) {
            for (String groutingPumpName : getGroutingPumpName()) {
                List<DeviceGroutingPumpDay> deviceGroutingPumpDayList = deviceGroutingPumpDayMapper.selectDeviceGroutingPumpDays(bid, groutingPumpName, selectTime);
                if (deviceGroutingPumpDayList.size() > 0) {
                    DeviceGroutingPumpDayVo deviceGroutingPumpDayVo = new DeviceGroutingPumpDayVo();
                    if ("2".equals(bid)){
                        deviceGroutingPumpDayVo.setBidCode("6-1");
                    }else {
                        deviceGroutingPumpDayVo.setBidCode(bid);
                    }
                    deviceGroutingPumpDayVo.setGroutingPumpCode(groutingPumpName);
                    deviceGroutingPumpDayVo.setTime(selectTime);
                    deviceGroutingPumpDayVo.setTodayGroutingTotal(deviceGroutingPumpDayMapper.selectDGPDTodayTotal(bid,groutingPumpName, selectTime));
                    deviceGroutingPumpDayVo.setAllDayGroutingTotal(deviceGroutingPumpDayMapper.selectDGPDAllDayTotal(bid, groutingPumpName,selectTime));
                    List<DeviceGroutingPumpDayDetailsVo> deviceGroutingPumpDayDetailsVoList = new ArrayList<>();
                    for (DeviceGroutingPumpDay dgp : deviceGroutingPumpDayList) {
                        DeviceGroutingPumpDayDetailsVo deviceGroutingPumpDayDetailsVo = new DeviceGroutingPumpDayDetailsVo();
                        deviceGroutingPumpDayDetailsVo.setGroutingPumpCode(dgp.getGroutingPumpCode());
                        deviceGroutingPumpDayDetailsVo.setGroutingTotal(dgp.getGroutingTotal());
                        deviceGroutingPumpDayDetailsVoList.add(deviceGroutingPumpDayDetailsVo);
                    }
                    deviceGroutingPumpDayVo.setGroutingPumpList(deviceGroutingPumpDayDetailsVoList);
                    deviceGroutingPumpDayVoList.add(deviceGroutingPumpDayVo);
                }
            }
        }
        return deviceGroutingPumpDayVoList;
    }


    public static List<String> getBid() {
        List<String> list = new ArrayList<>();
        list.add("1");
        list.add("3");
        list.add("4");
        list.add("5");
        list.add("6");
        list.add("2");
        return list;
    }

    public static List<String> getGroutingPumpName() {
        List<String> list = new ArrayList<>();
        list.add("1");
        list.add("2");
        return list;
    }
}


