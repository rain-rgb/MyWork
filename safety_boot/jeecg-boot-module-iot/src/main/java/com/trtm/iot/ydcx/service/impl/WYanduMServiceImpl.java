package com.trtm.iot.ydcx.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.trtm.iot.ydcx.entity.WYanduM;
import com.trtm.iot.ydcx.mapper.WYanduMMapper;
import com.trtm.iot.ydcx.service.IWYanduMService;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @Description: w_yandu_m
 * @Author: jeecg-boot
 * @Date:   2021-04-26
 * @Version: V1.0
 */
@Service
public class WYanduMServiceImpl extends ServiceImpl<WYanduMMapper, WYanduM> implements IWYanduMService {
    @Autowired
    private WYanduMMapper wYanduMMapper;

    @Override
    public Map selectSYSwHeGeLv(String tableName, List<String> shebeiList) {

        return wYanduMMapper.selectSYSwHeGeLv(tableName,shebeiList);
    }

    @Override
    public List<WYanduM> getListjt(String shebeilist, Integer curid) {
        return wYanduMMapper.getListjt(shebeilist,curid);
    }
}
