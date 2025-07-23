package com.trtm.iot.trzhanglarenwudan.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.trzhanglarenwudan.entity.TrZhanglaRenwudan;
import com.trtm.iot.trzhanglarenwudan.mapper.TrZhanglaRenwudanMapper;
import com.trtm.iot.trzhanglarenwudan.service.ITrZhanglaRenwudanService;
import com.trtm.iot.zhiliangrenwudan.entity.Zhiliangrenwudan;
import org.jeecg.common.system.query.QueryGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;
import java.util.Map;

/**
 * @Description: 张拉任务单
 * @Author: jeecg-boot
 * @Date:   2021-09-07
 * @Version: V1.0
 */
@Service
public class TrZhanglaRenwudanServiceImpl extends ServiceImpl<TrZhanglaRenwudanMapper, TrZhanglaRenwudan> implements ITrZhanglaRenwudanService {

    @Autowired TrZhanglaRenwudanMapper trZhanglaRenwudanMapper;

    @Override
    public TrZhanglaRenwudan selectone(String uuid) {
        try {
            QueryWrapper<TrZhanglaRenwudan> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("uuid",uuid);
            return this.getOne(queryWrapper);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<TrZhanglaRenwudan> selectLists(String strsToList1, Integer curid) {
//        try{
//            QueryWrapper<TrZhanglaRenwudan> queryWrapper = new QueryWrapper<>();
//            queryWrapper.ge("id",curid);
//            queryWrapper.in("shebeibianh",strsToList1);
//            queryWrapper.eq("status",1);
//            queryWrapper.or();
//            queryWrapper.eq("status2",1);
//            queryWrapper.last("limit 100");
//            return this.list(queryWrapper);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        return trZhanglaRenwudanMapper.selectLists(strsToList1,curid);
    }

    @Override
    public List<String> saveShebei(List<String> sbs) {
        return trZhanglaRenwudanMapper.saveShebei(sbs);
    }

    @Override
    public List<TrZhanglaRenwudan> saveShebeis(List<String> sbs) {
        return trZhanglaRenwudanMapper.saveShebeis(sbs);
    }

    @Override
    public Map selectwbs(String sgbwuuid) {
        return trZhanglaRenwudanMapper.selectwbs(sgbwuuid);
    }
}
