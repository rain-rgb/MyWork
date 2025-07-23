package com.trtm.iot.wzshouhuodanwei.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.wzshouhuodanwei.entity.Wzshouhuodanwei;
import com.trtm.iot.wzshouhuodanwei.mapper.WzshouhuodanweiMapper;
import com.trtm.iot.wzshouhuodanwei.service.IWzshouhuodanweiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 * @Description: 物资收获单位信息表
 * @Author: jeecg-boot
 * @Date:   2022-09-14
 * @Version: V1.0
 */
@Service
public class WzshouhuodanweiServiceImpl extends ServiceImpl<WzshouhuodanweiMapper, Wzshouhuodanwei> implements IWzshouhuodanweiService {

    @Autowired WzshouhuodanweiMapper wzshouhuodanweiMapper;
    @Override
    public List<Wzshouhuodanwei> getdatalist(String departid) {
        try {
            QueryWrapper<Wzshouhuodanwei> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("departId",departid);
            return this.list(queryWrapper);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
