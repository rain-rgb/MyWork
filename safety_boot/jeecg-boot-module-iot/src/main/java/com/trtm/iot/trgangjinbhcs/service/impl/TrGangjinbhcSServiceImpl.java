package com.trtm.iot.trgangjinbhcs.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.trgangjinbhcs.entity.TrGangjinbhcS;
import com.trtm.iot.trgangjinbhcs.mapper.TrGangjinbhcSMapper;
import com.trtm.iot.trgangjinbhcs.service.ITrGangjinbhcSService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 * @Description: 钢保检测子表
 * @Author: jeecg-boot
 * @Date:   2021-09-10
 * @Version: V1.0
 */
@Service
public class TrGangjinbhcSServiceImpl extends ServiceImpl<TrGangjinbhcSMapper, TrGangjinbhcS> implements ITrGangjinbhcSService {

    @Override
    public List<TrGangjinbhcS> selectGangjinSList(String testid) {
        try {
            QueryWrapper<TrGangjinbhcS> queryWrapper=new QueryWrapper<>();
            queryWrapper.eq("testId",testid);
            return this.list(queryWrapper);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
