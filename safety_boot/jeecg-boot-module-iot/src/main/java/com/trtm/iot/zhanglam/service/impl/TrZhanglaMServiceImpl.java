package com.trtm.iot.zhanglam.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.devicepipepilehistorydataone.entity.DevicePipepileHistorydataOne;
import com.trtm.iot.devicepipepilehistorydataone.mapper.DevicePipepileHistorydataOneMapper;
import com.trtm.iot.system.entity.ShebeiInfo;
import com.trtm.iot.system.mapper.ShebeiInfoMapper;
import com.trtm.iot.yajiangs.entity.TrYajiangS;
import com.trtm.iot.yajiangs.mapper.TrYajiangSMapper;
import com.trtm.iot.zhanglam.entity.TrZhanglaM;
import com.trtm.iot.zhanglam.mapper.TrZhanglaMMapper;
import com.trtm.iot.zhanglam.service.ITrZhanglaMService;
import com.trtm.iot.zhanglaxxb.entity.TrZhanglaXxb;
import com.trtm.iot.zhanglaxxb.entity.TrZhanglaXxbMStatistics;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.system.vo.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import javax.annotation.Resource;
import java.text.DecimalFormat;
import java.util.*;

/**
 * @Description: 张拉信息主表
 * @Author: jeecg-boot
 * @Date: 2021-08-31
 * @Version: V1.0
 */
@Service
public class TrZhanglaMServiceImpl extends ServiceImpl<TrZhanglaMMapper, TrZhanglaM> implements ITrZhanglaMService {

    @Resource
    TrZhanglaMMapper trZhanglaMMapper;
    @Autowired
    private ShebeiInfoMapper shebeiInfoMapper;
    @Autowired
    private DevicePipepileHistorydataOneMapper devicePipepileHistorydataOneMapper;
    @Autowired
    private TrYajiangSMapper trYajiangSMapper;

    @Override
    public List<TrZhanglaM> selectmList(String syjid) {
//        try{
//            QueryWrapper<TrZhanglaM> queryWrapper = new QueryWrapper<>();
//            queryWrapper.eq("syjid",syjid);
//            return  this.list(queryWrapper);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        return trZhanglaMMapper.selectmList(syjid);
    }

    @Override
    public List<Map<String, Object>> countList() {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        return trZhanglaMMapper.countList(loginUser.getOrgCode());
    }

    @Override
    public int updateoverproofStatus(Integer overproofStatus, String syjid) {
        return trZhanglaMMapper.updateoverproofStatus(overproofStatus, syjid);
    }

    @Override
    public List<TrZhanglaXxbMStatistics> findBysyjid(String syjid) {
        return trZhanglaMMapper.findBysyjid(syjid);
    }

    @Override
    public List<TrZhanglaM> selectmnotList(String syjid) {
        return trZhanglaMMapper.selectmnotList(syjid);
    }

    @Override
    public List<TrZhanglaM> selectListbl(String shebeibianhao) {
        return trZhanglaMMapper.selectListbl(shebeibianhao);
    }

    @Override
    public void updateSbjById(String sbjno, Integer id) {
        trZhanglaMMapper.updateSbjById(sbjno, id);
    }

}
