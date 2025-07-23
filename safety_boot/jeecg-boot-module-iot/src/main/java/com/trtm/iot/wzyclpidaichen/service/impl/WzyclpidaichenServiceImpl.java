package com.trtm.iot.wzyclpidaichen.service.impl;

import com.trtm.iot.wzyclpidaichen.entity.Wzyclpidaichen;
import com.trtm.iot.wzyclpidaichen.mapper.WzyclpidaichenMapper;
import com.trtm.iot.wzyclpidaichen.service.IWzyclpidaichenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 * @Description: wzyclpidaichen
 * @Author: jeecg-boot
 * @Date:   2024-04-18
 * @Version: V1.0
 */
@Service
public class WzyclpidaichenServiceImpl extends ServiceImpl<WzyclpidaichenMapper, Wzyclpidaichen> implements IWzyclpidaichenService {

    @Autowired
    WzyclpidaichenMapper wzyclpidaichenMapper;

    @Override
    public List<Wzyclpidaichen> slistrqid(String shebeilist, Integer curid) {
        return wzyclpidaichenMapper.slistrqid(shebeilist,curid);
    }
}
