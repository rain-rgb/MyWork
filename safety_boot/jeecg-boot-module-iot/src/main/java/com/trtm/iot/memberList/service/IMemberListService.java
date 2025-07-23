package com.trtm.iot.memberList.service;

import com.trtm.iot.memberList.entity.MemberList;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 在场成员名单
 * @Author: jeecg-boot
 * @Date:   2022-09-06
 * @Version: V1.0
 */
public interface IMemberListService extends IService<MemberList> {

    MemberList getperson(String personid);
}
