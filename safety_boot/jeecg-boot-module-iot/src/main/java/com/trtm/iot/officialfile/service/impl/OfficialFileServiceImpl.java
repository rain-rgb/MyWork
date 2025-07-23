package com.trtm.iot.officialfile.service.impl;

import com.trtm.iot.officialfile.entity.OfficialFile;
import com.trtm.iot.officialfile.mapper.OfficialFileMapper;
import com.trtm.iot.officialfile.service.IOfficialFileService;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.system.vo.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;
import java.util.Map;

/**
 * @Description: 公文信息表
 * @Author: jeecg-boot
 * @Date:   2021-11-29
 * @Version: V1.0
 */
@Service
public class OfficialFileServiceImpl extends ServiceImpl<OfficialFileMapper, OfficialFile> implements IOfficialFileService {
    @Autowired
    OfficialFileMapper officialFileMapper;
    @Override
    public List<Map> events() {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        return officialFileMapper.events(loginUser.getOrgCode());
    }

    @Override
    public List<Map> archive() {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        return officialFileMapper.archive(loginUser.getOrgCode());
    }
}
