package com.trtm.iot.hntbhz.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.hntbhz.entity.BhzCementDetail;
import com.trtm.iot.hntbhz.mapper.BhzCementDetailMapper;
import com.trtm.iot.hntbhz.service.IBhzCementDetailService;
import com.trtm.iot.hntbhz.vo.BhzCementDetailRC;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 拌合站子表材料信息
 * @Author: jeecg-boot
 * @Date: 2021-02-05
 * @Version: V1.0
 */
@Service
public class BhzCementDetailServiceImpl extends ServiceImpl<BhzCementDetailMapper, BhzCementDetail> implements IBhzCementDetailService {

    @Autowired
    private BhzCementDetailMapper bhzCementDetailMapper;

    @Override
    public List<BhzCementDetail> selectByMainId(String mainId) {
        return bhzCementDetailMapper.selectByMainId(mainId);
    }


    @Override
    public List querySysCementPageByUserId(Integer pageNo, Integer pageSize) {
        return bhzCementDetailMapper.querySysCementListByUserId(pageNo, pageSize);
//		bhzCementDetailMapper.selectPage(page,new QueryWrapper)
    }

    @Override
    public List<BhzCementDetail> selectcementlist(String batchNo) {
        return bhzCementDetailMapper.selectcementlist(batchNo);
    }


    @Override
    public int updateBy(BhzCementDetail bhzCementDetail) {
        return bhzCementDetailMapper.updateById(bhzCementDetail);

    }

    @Override
    public List<BhzCementDetailRC> selectcementlists(String batchNo) {
        return bhzCementDetailMapper.selectcementlists(batchNo);
    }

    @Override
    public List<BhzCementDetail> selectcementlist1(String batchNo) {
        return bhzCementDetailMapper.selectcementlist1(batchNo);
    }

    @Override
    public List<Map> selectByBatchList(List<String> list) {
        return baseMapper.selectByBatchList(list);
    }

    @Override
    public List<BhzCementDetail> selectByBatchNo(String batchNo) {
        QueryWrapper<BhzCementDetail> qw = new QueryWrapper<>();
        qw.eq("batch_no", batchNo);
        return baseMapper.selectList(qw);
    }
}
