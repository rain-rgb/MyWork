package com.trtm.iot.bhzlqjipeifanwei.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.bhzlqjipeifanwei.entity.BhzLqJipeiFanwei;
import com.trtm.iot.bhzlqjipeifanwei.mapper.BhzLqJipeiFanweiMapper;
import com.trtm.iot.bhzlqjipeifanwei.service.IBhzLqJipeiFanweiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 * @Description: 沥青级配范围配置表
 * @Author: jeecg-boot
 * @Date:   2022-05-16
 * @Version: V1.0
 */
@Service
public class BhzLqJipeiFanweiServiceImpl extends ServiceImpl<BhzLqJipeiFanweiMapper, BhzLqJipeiFanwei> implements IBhzLqJipeiFanweiService {
    @Autowired
    private BhzLqJipeiFanweiMapper bhzLqJipeiFanweiMapper;

    @Override
    public BhzLqJipeiFanwei selectone(String jipeibiaozhun, String formulaNo) {
        try{
            QueryWrapper<BhzLqJipeiFanwei> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("sbjno",jipeibiaozhun);
            queryWrapper.eq("poure_location",formulaNo);
            return this.getOne(queryWrapper);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<BhzLqJipeiFanwei> selectlist(String shebeino, Integer id) {
        return bhzLqJipeiFanweiMapper.selectlist(shebeino, id);
    }
}
