package com.trtm.iot.yajiangrenwudan.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.trzhanglarenwudan.entity.TrZhanglaRenwudan;
import com.trtm.iot.yajiangrenwudan.entity.TrYajiangRenwudan;
import com.trtm.iot.yajiangrenwudan.mapper.TrYajiangRenwudanMapper;
import com.trtm.iot.yajiangrenwudan.service.ITrYajiangRenwudanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 * @Description: 压浆任务单
 * @Author: jeecg-boot
 * @Date:   2021-09-08
 * @Version: V1.0
 */
@Service
public class TrYajiangRenwudanServiceImpl extends ServiceImpl<TrYajiangRenwudanMapper, TrYajiangRenwudan> implements ITrYajiangRenwudanService {

    @Autowired TrYajiangRenwudanMapper trYajiangRenwudanMapper;

    @Override
    public TrYajiangRenwudan selectone(String uuid) {
        try {
            QueryWrapper<TrYajiangRenwudan> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("uuid", uuid)
                    .last("limit 1");
            return this.getOne(queryWrapper);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<TrYajiangRenwudan> selectLists(String strsToList1, Integer curid) {
//        try{
//            QueryWrapper<TrYajiangRenwudan> queryWrapper =new QueryWrapper<>();
//            queryWrapper.ge("id",curid);
//            queryWrapper.in("shebeibianhao",strsToList1);
//            queryWrapper.eq("status",1);
//            queryWrapper.last("limit 1");
//            return this.list(queryWrapper);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        return trYajiangRenwudanMapper.selectLists(strsToList1,curid);
    }

    @Override
    public List<String> saveShebei(List<String> sbs) {
        return trYajiangRenwudanMapper.saveShebei(sbs);
    }

    @Override
    public List<TrYajiangRenwudan> saveShebeis(List<String> sbs) {
        return trYajiangRenwudanMapper.saveShebeis(sbs);
    }
}
