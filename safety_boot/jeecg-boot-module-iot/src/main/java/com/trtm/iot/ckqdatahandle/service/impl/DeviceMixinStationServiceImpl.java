package com.trtm.iot.ckqdatahandle.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.trtm.iot.ckqdatahandle.entity.DeviceMixinStation;
import com.trtm.iot.ckqdatahandle.mapper.DeviceMixinStationMapper;
import com.trtm.iot.ckqdatahandle.service.IDeviceMixinStationService;
import com.trtm.iot.ckqdatahandle.vo.SelectDeviceMixinStationVo;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.exception.JeecgBootException;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class DeviceMixinStationServiceImpl extends ServiceImpl<DeviceMixinStationMapper, DeviceMixinStation> implements IDeviceMixinStationService {


    @Autowired
    private DeviceMixinStationServiceImpl deviceMixinStationService;

    @Autowired
    private DeviceMixinStationMapper deviceMixinStationMapper;
    @Autowired
    private RedisUtil redisUtil;
    /*
     * @Author zgq
     * @Description 搅拌站信息列表-分页查询
     * @Date 2021/6/13 14:21
     * @Param
     * @return
     **/
    @Override
    public IPage<DeviceMixinStation> selectMixinStationListPage(SelectDeviceMixinStationVo selectDeviceMixinStationVo) {
        IPage<DeviceMixinStation> page = new Page<>(selectDeviceMixinStationVo.getPageNo(), selectDeviceMixinStationVo.getPageSize());
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String  shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        //查询到他的设备编号
        String[] split = shebei.split(",");
        List<String> shebeilist = new ArrayList<>();
        Collections.addAll(shebeilist, split);
        QueryWrapper<DeviceMixinStation> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_delete", "0");
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
        IPage<DeviceMixinStation> result = this.page(page, queryWrapper);
        return result;
    }


}
