package com.trtm.iot.byssta.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.byssta.entity.BysSta;
import com.trtm.iot.byssta.mapper.BysStaMapper;
import com.trtm.iot.byssta.service.IBysStaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @Description: 标养室统计信息表
 * @Author: jeecg-boot
 * @Date:   2022-05-11
 * @Version: V1.0
 */
@Service
public class BysStaServiceImpl extends ServiceImpl<BysStaMapper, BysSta> implements IBysStaService {

    @Resource
    BysStaMapper bysStaMapper;
    @Override
    public BysSta selectlimit(Date datanyr1, String shebeino) {
//        try{
//            QueryWrapper<BysSta> queryWrapper = new QueryWrapper<>();
//            queryWrapper.eq("statistics_time",datanyr1);
//            queryWrapper.eq("shebeiNo",shebeino);
//            return this.getOne(queryWrapper);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
        return bysStaMapper.selectlimit(datanyr1,shebeino);
    }
}
