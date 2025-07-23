package com.trtm.iot.ydwbs.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.ydwbs.entity.Ydwbs;
import com.trtm.iot.ydwbs.mapper.YdwbsMapper;
import com.trtm.iot.ydwbs.service.IYdwbsService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

import static com.baomidou.mybatisplus.core.enums.SqlKeyword.ASC;

/**
 * @Description: ydwbs
 * @Author: jeecg-boot
 * @Date:   2021-09-14
 * @Version: V1.0
 */
@Service
public class YdwbsServiceImpl extends ServiceImpl<YdwbsMapper, Ydwbs> implements IYdwbsService {

    @Override
    public List<Ydwbs> getList(Integer status) {
        QueryWrapper<Ydwbs> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("status",0);
        queryWrapper.orderBy(true,true,"sort");
        return this.list(queryWrapper);
    }
}
