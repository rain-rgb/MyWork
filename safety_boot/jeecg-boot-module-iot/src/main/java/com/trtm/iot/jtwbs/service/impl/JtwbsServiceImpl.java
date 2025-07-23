package com.trtm.iot.jtwbs.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.jtwbs.entity.Jtwbs;
import com.trtm.iot.jtwbs.mapper.JtwbsMapper;
import com.trtm.iot.jtwbs.service.IJtwbsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 * @Description: jtwbs
 * @Author: jeecg-boot
 * @Date:   2023-04-23
 * @Version: V1.0
 */
@Service
public class JtwbsServiceImpl extends ServiceImpl<JtwbsMapper, Jtwbs> implements IJtwbsService {
    @Autowired
    private JtwbsMapper jtwbsMapper;

    @Override
    public Jtwbs selectbycogcode(String orgCode) {
        return jtwbsMapper.selectbycogcode(orgCode);
    }

    @Override
    public List<Jtwbs> selectbycogcodelist(String orgCode) {
        QueryWrapper<Jtwbs> queryWrapper = new QueryWrapper<>();
        queryWrapper.likeRight("sys_org_code",orgCode);
        List<Jtwbs> jtwbs = jtwbsMapper.selectList(queryWrapper);
        return jtwbs;
    }
}
