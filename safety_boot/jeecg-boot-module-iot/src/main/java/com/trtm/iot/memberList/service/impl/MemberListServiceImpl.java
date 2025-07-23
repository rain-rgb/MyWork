package com.trtm.iot.memberList.service.impl;

import com.trtm.iot.memberList.entity.MemberList;
import com.trtm.iot.memberList.mapper.MemberListMapper;
import com.trtm.iot.memberList.service.IMemberListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 在场成员名单
 * @Author: jeecg-boot
 * @Date:   2022-09-06
 * @Version: V1.0
 */
@Service
public class MemberListServiceImpl extends ServiceImpl<MemberListMapper, MemberList> implements IMemberListService {

    @Autowired MemberListMapper memberListMapper;
    @Override
    public MemberList getperson(String personid) {
        return memberListMapper.getperson(personid);
    }
}
