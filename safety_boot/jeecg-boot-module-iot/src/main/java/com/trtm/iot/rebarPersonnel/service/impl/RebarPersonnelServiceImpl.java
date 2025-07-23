package com.trtm.iot.rebarPersonnel.service.impl;

import com.trtm.iot.rebarComponent.vo.MaterialVo;
import com.trtm.iot.rebarPersonnel.entity.RebarPersonnel;
import com.trtm.iot.rebarPersonnel.mapper.RebarPersonnelMapper;
import com.trtm.iot.rebarPersonnel.service.IRebarPersonnelService;
import com.trtm.iot.wzcailiaonamedictman.mapper.WzcailiaonamedictManMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description: rebar_personnel
 * @Author: jeecg-boot
 * @Date:   2024-11-14
 * @Version: V1.0
 */
@Service
public class RebarPersonnelServiceImpl extends ServiceImpl<RebarPersonnelMapper, RebarPersonnel> implements IRebarPersonnelService {
    @Autowired
    RebarPersonnelMapper rebarPersonnelMapper;

    @Override
    public List<RebarPersonnel> getLeadTeamByFactory(RebarPersonnel rebarPersonnel, String factoryId, Integer pageNo, Integer pageSize, HttpServletRequest req) {

        List<RebarPersonnel> rebarPersonnelList = rebarPersonnelMapper.getLeadTeamByFactory(factoryId);
        for (RebarPersonnel personnel : rebarPersonnelList) {

        }

        List<RebarPersonnel> collect = rebarPersonnelList.stream().skip((pageNo - 1) * pageSize).limit(pageSize).collect(Collectors.toList());
        return collect;
    }
}
