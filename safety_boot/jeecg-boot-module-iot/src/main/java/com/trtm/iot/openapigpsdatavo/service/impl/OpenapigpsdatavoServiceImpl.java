package com.trtm.iot.openapigpsdatavo.service.impl;

import com.trtm.iot.openapigpsdatavo.entity.Openapigpsdatavo;
import com.trtm.iot.openapigpsdatavo.mapper.OpenapigpsdatavoMapper;
import com.trtm.iot.openapigpsdatavo.service.IOpenapigpsdatavoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 * @Description: 摊铺碾压数据
 * @Author: jeecg-boot
 * @Date:   2023-04-21
 * @Version: V1.0
 */
@Service
public class OpenapigpsdatavoServiceImpl extends ServiceImpl<OpenapigpsdatavoMapper, Openapigpsdatavo> implements IOpenapigpsdatavoService {
    @Autowired
    private OpenapigpsdatavoMapper openapigpsdatavoMapper;

    @Override
    public List<Openapigpsdatavo> getList(String shebeino, Integer id) {
        return openapigpsdatavoMapper.getList(shebeino,id);
    }

    @Override
    public List<Openapigpsdatavo> getBLList(String shebeino, Integer id) {
        return openapigpsdatavoMapper.getBLList(shebeino,id);
    }

    @Override
    public List<Openapigpsdatavo> getListjt(String shebeino, Integer id) {
        return openapigpsdatavoMapper.getListjt(shebeino,id);
    }
}
