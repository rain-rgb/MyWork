package com.trtm.iot.dianzhiweilan.service.impl;

import com.trtm.iot.dianzhiweilan.entity.Dianzhiweilan;
import com.trtm.iot.dianzhiweilan.mapper.DianzhiweilanMapper;
import com.trtm.iot.dianzhiweilan.service.IDianzhiweilanService;
import com.trtm.iot.entityprogresscheck.mapper.EntityCheckDetialMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;
import java.util.Map;

/**
 * @Description: 新电子围栏表
 * @Author: jeecg-boot
 * @Date:   2023-05-31
 * @Version: V1.0
 */
@Service
public class DianzhiweilanServiceImpl extends ServiceImpl<DianzhiweilanMapper, Dianzhiweilan> implements IDianzhiweilanService {
    @Autowired
    private DianzhiweilanMapper dianzhiweilanMapper;

    @Override
    public List<String> selectshebei() {
        return dianzhiweilanMapper.selectshebei();
    }

    @Override
    public List<Map<String, Object>> selectjinduweidu(String shebei,Integer i) {
        return dianzhiweilanMapper.selectjinduweidu(shebei,i);
    }
}
