package com.trtm.iot.ckqdatahandle.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.trtm.iot.ckqdatahandle.entity.DeviceGroutingPump;
import com.trtm.iot.ckqdatahandle.entity.DeviceGroutingPumpDay;
import com.trtm.iot.ckqdatahandle.entity.DeviceMixinStationDetails;
import com.trtm.iot.ckqdatahandle.mapper.DeviceGroutingPumpMapper;
import com.trtm.iot.ckqdatahandle.service.IDeviceGroutingPumpService;
import com.trtm.iot.ckqdatahandle.vo.DeviceGroutingPumpVo;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

@Service
public class DeviceGroutingPumpServiceImpl extends ServiceImpl<DeviceGroutingPumpMapper, DeviceGroutingPump> implements IDeviceGroutingPumpService {

    /*
     * @Author zgq
     * @Description 注浆泵信息列表-分页查询
     * @Date 2021/6/14 14:22
     * @Param
     * @return
     **/
    @Override
    public IPage<DeviceGroutingPump> selectDeviceGroutingPumpListPage(DeviceGroutingPumpVo deviceGroutingPumpVo) {
        IPage<DeviceGroutingPump> page = new Page<>(deviceGroutingPumpVo.getPageNo(), deviceGroutingPumpVo.getPageSize());
        QueryWrapper<DeviceGroutingPump> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_delete", "0");
        //标段编号
        String bidCode = deviceGroutingPumpVo.getBidCode();
        if (StringUtils.isNotEmpty(bidCode)) {
            queryWrapper.eq("bid_code", bidCode);
        }
        String groutingPumpName = deviceGroutingPumpVo.getGroutingPumpName();
        if (StringUtils.isNotEmpty(groutingPumpName)) {
            queryWrapper.eq("grouting_pump_name", groutingPumpName);
        }
        //注浆泵编号
        String groutingPumpCode = deviceGroutingPumpVo.getGroutingPumpCode();
        if (StringUtils.isNotEmpty(groutingPumpCode)) {
            queryWrapper.eq("grouting_pump_code", groutingPumpCode);
        }
        //施工井号
        String constructionWellCode = deviceGroutingPumpVo.getConstructionWellCode();
        if (StringUtils.isNotEmpty(constructionWellCode)) {
            queryWrapper.eq("construction_well_code", constructionWellCode);
        }

        String startTime = deviceGroutingPumpVo.getStartTime();
        String endTime = deviceGroutingPumpVo.getEndTime();
        queryWrapper.apply(StringUtils.isNotBlank(startTime),
                "date_format (grouting_start_time,'%Y-%m-%d %H:%i:%s') >= date_format('" + startTime + "','%Y-%m-%d %H:%i:%s')")
                .apply(StringUtils.isNotBlank(endTime),
                        "date_format (grouting_end_time,'%Y-%m-%d %H:%i:%s') <= date_format('" + endTime + "','%Y-%m-%d %H:%i:%s')");


        queryWrapper.ne("grouting_total","");
        queryWrapper.lambda().orderByDesc(DeviceGroutingPump::getGroutingStartTime);
        IPage<DeviceGroutingPump> result = this.page(page, queryWrapper);
        return result;
    }
}
