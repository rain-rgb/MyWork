package com.trtm.iot.syj.service.impl;

import com.trtm.iot.syj.entity.FWangnj;
import com.trtm.iot.syj.mapper.FWangnjMapper;
import com.trtm.iot.syj.service.IFWangnjService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 * @Description: f_wangnj
 * @Author: jeecg-boot
 * @Date:   2021-03-15
 * @Version: V1.0
 */
@Service
public class FWangnjServiceImpl extends ServiceImpl<FWangnjMapper, FWangnj> implements IFWangnjService {

    @Autowired
    FWangnjMapper fWangnjMapper;

    @Override
    public List<FWangnj> selectFswannjData(String syjid) {
        return fWangnjMapper.selectFswannjData(syjid);
    }

    @Override
    public String selectMaxSysj(String syjid) {
        return fWangnjMapper.selectMaxSysj(syjid);
    }

    @Override
    public void updateSbbh(String shebeilist) {
        fWangnjMapper.updateSbbh(shebeilist);
    }

    @Override
    public List<FWangnj> selectSyjwnList(String shebeilist, Integer curid) {
        return fWangnjMapper.selectSyjwnList(shebeilist, curid);
    }
}
