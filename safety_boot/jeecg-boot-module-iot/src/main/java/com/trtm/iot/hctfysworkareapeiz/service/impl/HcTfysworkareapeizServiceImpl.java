package com.trtm.iot.hctfysworkareapeiz.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.trtm.iot.hctfysworkareapeiz.entity.HcTfysworkareapeiz;
import com.trtm.iot.hctfysworkareapeiz.mapper.HcTfysworkareapeizMapper;
import com.trtm.iot.hctfysworkareapeiz.service.IHcTfysworkareapeizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description: 土方压实配置表
 * @Author: jeecg-boot
 * @Date:   2024-04-09
 * @Version: V1.0
 */
@Service
public class HcTfysworkareapeizServiceImpl extends ServiceImpl<HcTfysworkareapeizMapper, HcTfysworkareapeiz> implements IHcTfysworkareapeizService {
    @Autowired
    HcTfysworkareapeizMapper hcTfysworkareapeizMapper;

    @Override
    public List<HcTfysworkareapeiz> tfys(String shebeilist, Integer curid) {
        return hcTfysworkareapeizMapper.tfys(shebeilist,curid);
    }
}
