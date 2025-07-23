package com.trtm.iot.jikeng.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.jikeng.entity.JikengQingjiao;
import com.trtm.iot.jikeng.entity.JikengWeiyCfg;
import com.trtm.iot.jikeng.mapper.JikengWeiyCfgMapper;
import com.trtm.iot.jikeng.service.IJikengWeiyCfgService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.Date;
import java.util.List;

/**
 * @Description: jikeng_weiy_cfg
 * @Author: jeecg-boot
 * @Date:   2025-01-15
 * @Version: V1.0
 */
@Service
public class JikengWeiyCfgServiceImpl extends ServiceImpl<JikengWeiyCfgMapper, JikengWeiyCfg> implements IJikengWeiyCfgService {

    @Override
    public void updateByShbeino(String shebeino, String jsondata) {
        QueryWrapper<JikengWeiyCfg> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("shebeino",shebeino);
        List<JikengWeiyCfg> list = this.list(queryWrapper);
        if(list.size()>0){
            for(JikengWeiyCfg one:list){
                one.setJsondata(jsondata);
                one.setDatatime(new Date());
            }
            this.updateBatchById(list);
        }

    }
}
