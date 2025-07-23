package com.trtm.iot.pmscomwbs.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.pmscomwbs.entity.PmsComWbs;
import com.trtm.iot.pmscomwbs.mapper.PmsComWbsMapper;
import com.trtm.iot.pmscomwbs.service.IPmsComWbsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 * @Description: 中交象山疏港高速(原海德 wbs数据)
 * @Author: jeecg-boot
 * @Date:   2021-07-16
 * @Version: V1.0
 */
@Service
public class PmsComWbsServiceImpl extends ServiceImpl<PmsComWbsMapper, PmsComWbs> implements IPmsComWbsService {
    @Autowired
    private PmsComWbsMapper pmsComWbsMapper;
    @Override
    public List<PmsComWbs> queryselectlist(Integer id, Integer status) {
        try {
            QueryWrapper<PmsComWbs> queryWrapper=new QueryWrapper<>();
            queryWrapper.gt("ids",id);
            queryWrapper.eq("status",status);
            queryWrapper.last(" order by ids  asc");
            return this.list(queryWrapper);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int updateids(Integer id, Integer status) {
        return pmsComWbsMapper.updatealertsate(id,status);
    }


}
