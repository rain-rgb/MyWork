package com.trtm.iot.memberList.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import com.trtm.iot.memberList.entity.MemberList;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 在场成员名单
 * @Author: jeecg-boot
 * @Date:   2022-09-06
 * @Version: V1.0
 */
public interface MemberListMapper extends BaseMapper<MemberList> {

    MemberList getperson(String personid);
}
