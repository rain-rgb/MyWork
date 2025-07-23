package com.trtm.iot.zhanglam.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.zhanglam.entity.ZhanglaYajiangOverHandler;
import com.trtm.iot.zhanglam.mapper.ZhanglaYajiangOverHandlerMapper;
import com.trtm.iot.zhanglam.service.IZhanglaYajiangOverHandlerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 * @Description: zhangla_yajiang_over_handler
 * @Author: jeecg-boot
 * @Date:   2021-12-27
 * @Version: V1.0
 */
@Service
public class ZhanglaYajiangOverHandlerServiceImpl extends ServiceImpl<ZhanglaYajiangOverHandlerMapper, ZhanglaYajiangOverHandler> implements IZhanglaYajiangOverHandlerService {

    @Autowired
    private ZhanglaYajiangOverHandlerMapper zhanglaYajiangOverHandlerMapper;
    @Override
    public ZhanglaYajiangOverHandler selectoneMessage(String baseid) {
        try {
            QueryWrapper<ZhanglaYajiangOverHandler> queryWrapper=new QueryWrapper<>();
            queryWrapper.eq("baseId",baseid);
            return this.getOne(queryWrapper);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<ZhanglaYajiangOverHandler> listToday(String shebeilist) {
        return zhanglaYajiangOverHandlerMapper.listToday(shebeilist);
    }

    @Override
    public Integer BhzCZAddOrUpDate(String wtyy, String clfs, String cljg, String baseid, String bizPath, String chuzhiren, int status, String holeid) {
        String i = zhanglaYajiangOverHandlerMapper.dataById2(baseid,holeid);
        int result = 0;
        if (i==null){
            result = zhanglaYajiangOverHandlerMapper.BhzchuZhiAddById(wtyy,clfs,cljg,baseid,bizPath,chuzhiren,status,holeid);
        }else {
            result = zhanglaYajiangOverHandlerMapper.BhzchuZhiUpdateById(wtyy,clfs,cljg,baseid,bizPath,chuzhiren,status,holeid);
        }
        return result;
    }

    @Override
    public Integer supervisorBohui(String jlbh, String baseid, String people, int status, String holeid) {
        String i = zhanglaYajiangOverHandlerMapper.dataById2(baseid,holeid);
        int result = 0;
        if (i==null){
            result = zhanglaYajiangOverHandlerMapper.BhzShBoHuiAddById(jlbh,baseid,people,status,holeid);
        }else {
            result = zhanglaYajiangOverHandlerMapper.BhzShBoHuiUpdateById(jlbh,  baseid, people,status,holeid);
        }
        return result;
    }

    @Override
    public Integer supervisorAddOrUpdate(String spyj, String spjg, String baseid, String bizPath, String shenpiren, int status, String holeid) {
        String i = zhanglaYajiangOverHandlerMapper.dataById2(baseid,holeid);
        int result = 0;
        if (i==null){
            result = zhanglaYajiangOverHandlerMapper.BhzSHAddById(spyj, spjg, baseid, bizPath,shenpiren,status,holeid);
        }else {
            result = zhanglaYajiangOverHandlerMapper.BhzSHUpdateById(spyj, spjg, baseid, bizPath,shenpiren,status,holeid);
        }
        return result;
    }

    @Override
    public Integer headquartersAddOrUpdate(String zhbyj, String zhbjg, String baseid, String bizPath, String shenpiren, int status, String holeid) {
        String i = zhanglaYajiangOverHandlerMapper.dataById2(baseid,holeid);
        int result = 0;
        if (i==null){
            result = zhanglaYajiangOverHandlerMapper.BhzZHBSHAddById(zhbyj, zhbjg, baseid, bizPath,shenpiren,status,holeid);
        }else {
            result = zhanglaYajiangOverHandlerMapper.BhzZHBSHUpdateById(zhbyj, zhbjg,  baseid, bizPath,shenpiren,status,holeid);
        }
        return result;
    }

    @Override
    public Integer headquartersBohui(String zhbbh, String baseid, String shenpiren, int status, String holeid) {
        String i = zhanglaYajiangOverHandlerMapper.dataById2(baseid,holeid);
        int result = 0;
        if (i==null){
            result = zhanglaYajiangOverHandlerMapper.BhzZHBBoHuiAddById(zhbbh, baseid,  shenpiren,status,holeid);
        }else {
            result = zhanglaYajiangOverHandlerMapper.BhzZHBBoHuiUpdateById(zhbbh,  baseid,  shenpiren,status,holeid);
        }
        return result;
    }
}
