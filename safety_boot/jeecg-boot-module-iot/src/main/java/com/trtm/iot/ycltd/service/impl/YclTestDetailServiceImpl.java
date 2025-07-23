package com.trtm.iot.ycltd.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.ycltd.entity.YclTestDetail;
import com.trtm.iot.ycltd.mapper.YclTestDetailMapper;
import com.trtm.iot.ycltd.service.IYclTestDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.Date;
import java.util.List;

/**
 * @Description: 原材料试验详情实现类
 * @Author: lis1
 * @Date: 2022-11-18
 * @Version: V1.0
 */
@Service
public class YclTestDetailServiceImpl extends ServiceImpl<YclTestDetailMapper, YclTestDetail> implements IYclTestDetailService {

    @Autowired
    private YclTestDetailMapper yclTestDetailMapper;

    /**
     * @param iTN 检验批编号
     * @return
     */
    @Override
    public YclTestDetail getByILN(String iTN) {
        QueryWrapper<YclTestDetail> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("inspection_lot_number", iTN);
        List<YclTestDetail> list = this.list(queryWrapper);
        if (list.size() > 0){
            return list.get(0);
        }else {
            return null;
        }
    }

    @Override
    public YclTestDetail getByILNBHG(String iTN) {
        QueryWrapper<YclTestDetail> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("inspection_lot_number", iTN);
        queryWrapper.eq("test_status", 2);// 检验状态 0:未检验 1:合格 2:不合格 3:检验中
        queryWrapper.last(" limit 1");
        return this.getOne(queryWrapper);
    }

    @Override
    public boolean updateJYStateByPici(Integer jianyanstate, String pici) {
        return yclTestDetailMapper.updateJYStateByPici(jianyanstate, pici);
    }

    @Override
    public boolean updateCJStateByPici(Integer choujianstate, String pici) {
        return yclTestDetailMapper.updateCJStateByPici(choujianstate, pici);
    }

    @Override
    public YclTestDetail selectBySampleNumber(String inspectionLotNumber, String sapmleNumber) {
        return yclTestDetailMapper.selectBySampleNumber(inspectionLotNumber, sapmleNumber);
    }

    @Override
    public boolean updateBySampleNumber(String storageId, String inspectionLotNumber, String testName, Date samplingTime,
                                        Date testTime, Integer jianyanType, String conclusion, Integer testStatus, String tester,
                                        String report, String createBy, Date createTime, String updateTime, String sampleNumber) {
        return yclTestDetailMapper.updateBySampleNumber(storageId, inspectionLotNumber, testName, samplingTime, testTime, jianyanType, conclusion, testStatus, tester, report, createBy, createTime, updateTime, sampleNumber);
    }

}
