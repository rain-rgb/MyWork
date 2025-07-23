package com.trtm.iot.liangGongxuPeople.service.impl;

import com.trtm.iot.liangGongxuPeople.entity.LiangGongxuPeople;
import com.trtm.iot.liangGongxuPeople.mapper.LiangGongxuPeopleMapper;
import com.trtm.iot.liangGongxuPeople.service.ILiangGongxuPeopleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 * @Description: 自动工序负责人配置
 * @Author: jeecg-boot
 * @Date:   2022-11-16
 * @Version: V1.0
 */
@Service
public class LiangGongxuPeopleServiceImpl extends ServiceImpl<LiangGongxuPeopleMapper, LiangGongxuPeople> implements ILiangGongxuPeopleService {

    @Autowired
    LiangGongxuPeopleMapper liangGongxuPeopleMapper;
    @Override
    public LiangGongxuPeople selectResponsible(String sysOrgCode, Integer xuhao) {
        return liangGongxuPeopleMapper.selectResponsible(sysOrgCode, xuhao);
    }
}
