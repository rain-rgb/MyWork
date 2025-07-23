package com.trtm.iot.byscfg.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.byscfg.entity.BysCfg;
import com.trtm.iot.byscfg.mapper.BysCfgMapper;
import com.trtm.iot.byscfg.service.IBysCfgService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 * @Description: 标养室温湿度配置表
 * @Author: jeecg-boot
 * @Date:   2021-07-13
 * @Version: V1.0
 */
@Service
public class BysCfgServiceImpl extends ServiceImpl<BysCfgMapper, BysCfg> implements IBysCfgService {

    @Override
    public BysCfg selectbyscallone(String shebeino) {
        try{
            QueryWrapper<BysCfg> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("shebeiNo",shebeino);
            return this.getOne(queryWrapper);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<BysCfg> selectbyslist(String shebeino) {
        try{
            QueryWrapper<BysCfg> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("shebeiNo",shebeino);
            return this.list(queryWrapper);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
