package com.trtm.iot.spreadandcrush.service.impl;

import com.trtm.iot.spreadandcrush.entity.Spreadandcrush;
import com.trtm.iot.spreadandcrush.mapper.SpreadandcrushMapper;
import com.trtm.iot.spreadandcrush.service.ISpreadandcrushService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 * @Description: 摊铺碾压
 * @Author: jeecg-boot
 * @Date: 2023-04-20
 * @Version: V1.0
 */
@Service
public class SpreadandcrushServiceImpl extends ServiceImpl<SpreadandcrushMapper, Spreadandcrush> implements ISpreadandcrushService {

    @Autowired
    private SpreadandcrushMapper spreadandcrushMapper;

    @Override
    public List<Spreadandcrush> getList(String Shebeino, Integer id) {
        return spreadandcrushMapper.getList(Shebeino, id);
    }
}
