package com.trtm.iot.yajiangs.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.trtm.iot.devicepipepilehistorydataone.mapper.DevicePipepileHistorydataOneMapper;
import com.trtm.iot.syj.entity.TSyjzb;
import com.trtm.iot.system.entity.ShebeiInfo;
import com.trtm.iot.system.mapper.ShebeiInfoMapper;
import com.trtm.iot.yajiangm.entity.TrYajiangSM;
import com.trtm.iot.yajiangs.entity.TrYajiangS;
import com.trtm.iot.yajiangs.mapper.TrYajiangSMapper;
import com.trtm.iot.yajiangs.service.ITrYajiangSService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.text.DecimalFormat;
import java.util.*;

/**
 * @Description: 压浆实时数据表
 * @Author: jeecg-boot
 * @Date: 2021-09-06
 * @Version: V1.0
 */
@Service
public class TrYajiangSServiceImpl extends ServiceImpl<TrYajiangSMapper, TrYajiangS> implements ITrYajiangSService {

    @Autowired
    private TrYajiangSMapper trYajiangSMapper;
    @Autowired
    private ShebeiInfoMapper shebeiInfoMapper;
    @Autowired
    private DevicePipepileHistorydataOneMapper devicePipepileHistorydataOneMapper;

    @Override
    public List<TrYajiangS> selectListTrYajiangS(String syjid) {
        try {
            QueryWrapper<TrYajiangS> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("syjid", syjid);
            return this.list(queryWrapper);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public IPage<TrYajiangS> selectListPage(Integer pageNo, Integer pageSize, String shebeis, String shebeibianhao) {
        Page<TrYajiangS> page = new Page<>(pageNo, pageSize);
        return trYajiangSMapper.selectListPage(page, shebeis, shebeibianhao);
    }

    @Override
    public IPage<TrYajiangS> selectChaobiaoListPage(Integer pageNo, Integer pageSize, String shebeis, String shebeibianhao) {
        Page<TrYajiangS> page = new Page<>(pageNo, pageSize);
        return trYajiangSMapper.selectChaobiaoListPage(page, shebeis, shebeibianhao);
    }
    @Override
    public IPage<TrYajiangS> selectChaobiaoListPage1(Integer pageNo, Integer pageSize, String shebeis, String shebeibianhao, Integer overproofStatus) {
        Page<TrYajiangS> page = new Page<>(pageNo, pageSize);
        return trYajiangSMapper.selectChaobiaoListPage1(page, shebeis, shebeibianhao, overproofStatus);
    }

    @Override
    public int updateoverproofStatus(Integer OverproofStatus, String syjid) {
        return trYajiangSMapper.updateoverproofStatus(OverproofStatus, syjid);
    }

    @Override
    public List<TrYajiangSM> findBysyjid(String syjid) {
        return trYajiangSMapper.findBysyjid(syjid);
    }

    @Override
    public Integer findXiangMuZSss(List<String> querySheBeiList, String time) {
        return trYajiangSMapper.findXiangMuZSss(querySheBeiList, time);
    }

    @Override
    public Integer findYuJingSs(List<String> querySheBeiList, String time) {
        return trYajiangSMapper.findYuJingSs(querySheBeiList, time);
    }

    @Override
    public Integer findBiHeSs(List<String> querySheBeiList, String time) {
        return trYajiangSMapper.findBiHeSs(querySheBeiList, time);
    }

    @Override
    public List<TrYajiangS> selectmList(String syjid) {
        return trYajiangSMapper.selectmList(syjid);
    }

    @Override
    public List<TrYajiangS> selectmnotList(String syjid) {
        return trYajiangSMapper.selectmnotList(syjid);
    }

    @Override
    public List<TrYajiangS> selectListbltozl(String shebeino) {
        return trYajiangSMapper.selectListbltozl(shebeino);
    }

}
