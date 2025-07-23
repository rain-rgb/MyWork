package com.trtm.iot.wzgongyingshangman.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.wzgongyingshang.entity.Wzgongyingshang;
import com.trtm.iot.wzgongyingshangman.entity.WzgongyingshangMan;
import com.trtm.iot.wzgongyingshangman.mapper.WzgongyingshangManMapper;
import com.trtm.iot.wzgongyingshangman.service.IWzgongyingshangManService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.Map;

/**
 * @Description: wzgongyingshang_man
 * @Author: jeecg-boot
 * @Date:   2022-08-08
 * @Version: V1.0
 */
@Service
public class WzgongyingshangManServiceImpl extends ServiceImpl<WzgongyingshangManMapper, WzgongyingshangMan> implements IWzgongyingshangManService {

    @Autowired
    WzgongyingshangManMapper wzgongyingshangManMapper;

    @Override
    public Map selectqueryone(String sysOrgCode) {
        return wzgongyingshangManMapper.selectqueryone(sysOrgCode);
    }

    @Override
    public WzgongyingshangMan selectnameone(String guid) {
        try {
            QueryWrapper<WzgongyingshangMan> queryWrapper=new QueryWrapper<>();
            queryWrapper.eq("guid",guid);
            return this.getOne(queryWrapper);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public WzgongyingshangMan selectnameone1(String gongyinsgangNo) {
        try {
            QueryWrapper<WzgongyingshangMan> queryWrapper=new QueryWrapper<>();
            queryWrapper.eq("guid",gongyinsgangNo);
            return this.getOne(queryWrapper);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
