package com.trtm.iot.bangza.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.bangza.entity.WiretieProcedure;
import com.trtm.iot.bangza.mapper.WiretieProcedureMapper;
import com.trtm.iot.bangza.service.IWiretieProcedureService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 钢筋绑扎工序表信息
 * @Author: jeecg-boot
 * @Date:   2021-08-18
 * @Version: V1.0
 */
@Service
public class WiretieProcedureServiceImpl extends ServiceImpl<WiretieProcedureMapper, WiretieProcedure> implements IWiretieProcedureService {

    @Override
    public WiretieProcedure selectones(String uuid) {
        try {
            QueryWrapper<WiretieProcedure> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("uuid",uuid);
            return this.getOne(queryWrapper);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
