package com.trtm.sy.sydpsshebeisyjl.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.trtm.sy.sydpsshebeisyjl.entity.SheBeiSyJlResponse;
import com.trtm.sy.sydpsshebeisyjl.entity.SyDpsJcShebeiShiyongjilu;
import com.trtm.sy.sydpsshebeisyjl.mapper.SyDpsJcShebeiShiyongjiluMapper;
import com.trtm.sy.sydpsshebeisyjl.service.ISyDpsJcShebeiShiyongjiluService;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * @Description: dps_jc_shebei_shiyongjilu
 * @Author: jeecg-boot
 * @Date: 2023-02-27
 * @Version: V1.0
 */
@Service
public class SyDpsJcShebeiShiyongjiluServiceImpl extends ServiceImpl<SyDpsJcShebeiShiyongjiluMapper, SyDpsJcShebeiShiyongjilu> implements ISyDpsJcShebeiShiyongjiluService {

    @Override
    public IPage<SheBeiSyJlResponse> getSbSyJl(String sheBeiIds, String startTime, String endTime, Integer pageNo, Integer pageSize) {
        Page<SheBeiSyJlResponse> page = new Page<>(pageNo, pageSize);
        String[] strings = sheBeiIds.split(",");
        List<String> list = Arrays.asList(strings);
        return this.baseMapper.getSbSyJl(list, startTime, endTime, page);
    }
}
