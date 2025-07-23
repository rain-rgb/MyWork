package com.trtm.iot.ckqdatahandle.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.trtm.iot.ckqdatahandle.entity.DeviceGroutingPumpDay;
import com.trtm.iot.ckqdatahandle.entity.DeviceMixinStation;
import com.trtm.iot.ckqdatahandle.entity.DeviceMixinStationDetails;
import com.trtm.iot.ckqdatahandle.mapper.DeviceMixinStationDetailsMapper;
import com.trtm.iot.ckqdatahandle.service.IDeviceMixinStationDetailsService;
import com.trtm.iot.ckqdatahandle.vo.DeviceMixinStationDetailsVo;
import com.trtm.iot.ckqdatahandle.vo.SelectDeviceMixinStationVo;
import lombok.Data;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class DeviceMixinStationDetailsServiceImpl extends ServiceImpl<DeviceMixinStationDetailsMapper, DeviceMixinStationDetails> implements IDeviceMixinStationDetailsService {

    @Autowired
    private IDeviceMixinStationDetailsService deviceMixinStationDetailsService;

    @Autowired
    private DeviceMixinStationDetailsMapper deviceMixinStationDetailsMapper;
    @Autowired
    private RedisUtil redisUtil;
    /*
     * @Author zgq
     * @Description 搅拌站详情信息列表-分页查询
     * @Date 2021/6/12 14:20
     * @Param
     * @return
     **/
    @Override
    public IPage<DeviceMixinStationDetails> selectMixinStationDetailsListPage(SelectDeviceMixinStationVo selectDeviceMixinStationVo) {
        IPage<DeviceMixinStationDetails> page = new Page<>(selectDeviceMixinStationVo.getPageNo(), selectDeviceMixinStationVo.getPageSize());
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String  shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        //查询到他的设备编号
        String[] split = shebei.split(",");
        List<String> shebeilist = new ArrayList<>();
        Collections.addAll(shebeilist, split);
        QueryWrapper<DeviceMixinStationDetails> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_delete", "1");
        queryWrapper.in("sid",shebeilist);//用户权限下设备编号
        //标段编号
        String bidCode = selectDeviceMixinStationVo.getBidCode();
        if (StringUtils.isNotEmpty(bidCode)) {
            queryWrapper.eq("bid_code", bidCode);
        }
        //搅拌站号
        String mixinStationCode = selectDeviceMixinStationVo.getMixinStationCode();
        if (StringUtils.isNotEmpty(mixinStationCode)) {
            queryWrapper.eq("mixin_station_code", mixinStationCode);
        }
        String startTime = selectDeviceMixinStationVo.getStartTime();
        String endTime = selectDeviceMixinStationVo.getEndTime();
        queryWrapper.apply(StringUtils.isNotBlank(startTime),
                "date_format (upload_time,'%Y-%m-%d %H:%i:%s') >= date_format('" + startTime + "','%Y-%m-%d %H:%i:%s')")
                .apply(StringUtils.isNotBlank(endTime),
                        "date_format (upload_time,'%Y-%m-%d %H:%i:%s') <= date_format('" + endTime + "','%Y-%m-%d %H:%i:%s')");
        queryWrapper.lambda().orderByDesc(DeviceMixinStationDetails::getUploadTime);
        IPage<DeviceMixinStationDetails> result = this.page(page, queryWrapper);
        if (result.getRecords().size() > 0) {
            for (DeviceMixinStationDetails dmsd : result.getRecords()) {
                dmsd.setTodayCementFlyAsh(dmsd.getTodayCement() + dmsd.getTodayFlyAsh());
                dmsd.setTotalCementFlyAsh(dmsd.getTotalCement() + dmsd.getTotalFlyAsh());
            }
        }
        return result;
    }

    /*
     * @Author zgq
     * @Description 详情
     * @Date 2021/6/16 14:20
     * @Param
     * @return
     **/
    @Override
    public Map<String, Object> selectMixinStationDetails(String sid, String uploadTime) {
        Map<String, Object> result = new HashMap<>();
        List<DeviceMixinStationDetailsVo> list = new LinkedList<>();
        QueryWrapper<DeviceMixinStationDetails> queryWrapper = new QueryWrapper();
        queryWrapper.eq("sid", sid);
        queryWrapper.eq("upload_time", uploadTime);
        queryWrapper.eq("is_delete", "0");
        List<DeviceMixinStationDetails> deviceMixinStationDetails = deviceMixinStationDetailsMapper.selectList(queryWrapper);
        if (deviceMixinStationDetails.size() > 0) {
            for (DeviceMixinStationDetails dmsd : deviceMixinStationDetails) {
                if (null != dmsd.getRealityCementOne() && null != dmsd.getTheoryCementOne()) {
                    DeviceMixinStationDetailsVo cement_1 = new DeviceMixinStationDetailsVo();
                    cement_1.setMaterialName("水泥-1");
                    cement_1.setTheoryCement(dmsd.getTheoryCementOne());
                    cement_1.setRealityCement(dmsd.getRealityCementOne());
                    cement_1.setErrorValues(dmsd.getTheoryCementOne() - dmsd.getRealityCementOne());
                    cement_1.setErrorRate(dmsd.getCementOneErrorRate());
                    list.add(cement_1);

                }
                if (null != dmsd.getRealityCementTwo() && null != dmsd.getTheoryCementTwo()) {
                    DeviceMixinStationDetailsVo cement_2 = new DeviceMixinStationDetailsVo();
                    cement_2.setMaterialName("水泥-2");
                    cement_2.setTheoryCement(dmsd.getTheoryCementTwo());
                    cement_2.setRealityCement(dmsd.getRealityCementTwo());
                    cement_2.setErrorValues(dmsd.getTheoryCementTwo() - dmsd.getRealityCementTwo());
                    cement_2.setErrorRate(dmsd.getCementTwoErrorRate());
                    list.add(cement_2);
                }
                if (null != dmsd.getTheoryFlyAshOne() && null != dmsd.getRealityFlyAshOne()) {
                    DeviceMixinStationDetailsVo flyAsh_1 = new DeviceMixinStationDetailsVo();
                    flyAsh_1.setMaterialName("粉煤灰-1");
                    flyAsh_1.setTheoryCement(dmsd.getTheoryFlyAshOne());
                    flyAsh_1.setRealityCement(dmsd.getRealityFlyAshOne());
                    flyAsh_1.setErrorValues(dmsd.getTheoryFlyAshOne() - dmsd.getRealityFlyAshOne());
                    flyAsh_1.setErrorRate(dmsd.getFlyAshOneErrorRate());
                    list.add(flyAsh_1);
                }
                if (null != dmsd.getTheoryFlyAshTwo() && null != dmsd.getRealityFlyAshTwo()) {
                    DeviceMixinStationDetailsVo flyAsh_2 = new DeviceMixinStationDetailsVo();
                    flyAsh_2.setMaterialName("粉煤灰-2");
                    flyAsh_2.setTheoryCement(dmsd.getTheoryFlyAshTwo());
                    flyAsh_2.setRealityCement(dmsd.getRealityFlyAshTwo());
                    flyAsh_2.setErrorValues(dmsd.getTheoryFlyAshTwo() - dmsd.getRealityFlyAshTwo());
                    flyAsh_2.setErrorRate(dmsd.getFlyAshTwoErrorRate());
                    list.add(flyAsh_2);
                }
                if (dmsd.getSid().equals("/usr/plcnet/BP1_2_STA/edge/u")) {
                    DeviceMixinStationDetailsVo cement_1 = new DeviceMixinStationDetailsVo();
                    cement_1.setMaterialName("水泥-1");
                    cement_1.setTheoryCement(dmsd.getTheoryCement());
                    cement_1.setRealityCement(dmsd.getRealityCement());
                    cement_1.setErrorValues(0.00);
                    cement_1.setErrorRate(0.00);
                    list.add(cement_1);

                    DeviceMixinStationDetailsVo flyAsh_1 = new DeviceMixinStationDetailsVo();
                    flyAsh_1.setMaterialName("粉煤灰-1");
                    flyAsh_1.setTheoryCement(dmsd.getTheoryFlyAsh());
                    flyAsh_1.setRealityCement(dmsd.getRealityFlyAsh());
                    flyAsh_1.setErrorValues(0.00);
                    flyAsh_1.setErrorRate(0.00);
                    list.add(flyAsh_1);


                }

            }
        }
        result.put("deviceMixinStationDetailsList", list);
        return result;
    }
}
