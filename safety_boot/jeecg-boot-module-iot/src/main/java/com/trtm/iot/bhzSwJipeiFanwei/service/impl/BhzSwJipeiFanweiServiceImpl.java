package com.trtm.iot.bhzSwJipeiFanwei.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.bhzSwJipeiFanwei.entity.BhzSwJipeiFanwei;
import com.trtm.iot.bhzSwJipeiFanwei.mapper.BhzSwJipeiFanweiMapper;
import com.trtm.iot.bhzSwJipeiFanwei.service.IBhzSwJipeiFanweiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 * @Description: bhz_sw_jipei_fanwei
 * @Author: jeecg-boot
 * @Date:   2023-06-14
 * @Version: V1.0
 */
@Service
public class BhzSwJipeiFanweiServiceImpl extends ServiceImpl<BhzSwJipeiFanweiMapper, BhzSwJipeiFanwei> implements IBhzSwJipeiFanweiService {
    @Autowired
    private BhzSwJipeiFanweiMapper bhzSwJipeiFanweiMapper;

    @Override
    public BhzSwJipeiFanwei selectone(String jipeibiaozhun, String formulaNo) {
        try{
            QueryWrapper<BhzSwJipeiFanwei> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("sbjno",jipeibiaozhun);
            queryWrapper.eq("poure_location",formulaNo);
            return this.getOne(queryWrapper);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<BhzSwJipeiFanwei> selectlist(String shebeilist, Integer curid) {
        return bhzSwJipeiFanweiMapper.selectlist(shebeilist,curid);
    }
}
