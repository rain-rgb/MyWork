package com.trtm.iot.outsource.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.outsource.entity.FTrial;
import com.trtm.iot.outsource.mapper.FTrialMapper;
import com.trtm.iot.outsource.service.IFTrialService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 * @Description: 委外试验
 * @Author: lis1
 * @Date: 2022-12-09
 * @Version: V1.0
 */
@Service
public class FTrialServiceImpl extends ServiceImpl<FTrialMapper, FTrial> implements IFTrialService {

    @Override
    public List<FTrial> getDataToPush(Integer id) {
        try {
            QueryWrapper<FTrial> queryWrapper = new QueryWrapper<>();
            queryWrapper.gt("id", id);
            queryWrapper.last("limit 100");
            return this.list(queryWrapper);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
