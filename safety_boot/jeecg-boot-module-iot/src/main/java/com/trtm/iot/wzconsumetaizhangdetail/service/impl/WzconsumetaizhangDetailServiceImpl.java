package com.trtm.iot.wzconsumetaizhangdetail.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.wzconsumetaizhangdetail.entity.WzconsumetaizhangDetail;
import com.trtm.iot.wzconsumetaizhangdetail.mapper.WzconsumetaizhangDetailMapper;
import com.trtm.iot.wzconsumetaizhangdetail.service.IWzconsumetaizhangDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 * @Description: 物资原材料消耗台账材料子表
 * @Author: jeecg-boot
 * @Date:   2021-09-07
 * @Version: V1.0
 */
@Service
public class WzconsumetaizhangDetailServiceImpl extends ServiceImpl<WzconsumetaizhangDetailMapper, WzconsumetaizhangDetail> implements IWzconsumetaizhangDetailService {

    @Autowired
    WzconsumetaizhangDetailMapper wzconsumetaizhangDetailMapper;
    @Override
    public WzconsumetaizhangDetail selectwzxiaohaodetail(String materialeName, int ids) {
        try {
            QueryWrapper<WzconsumetaizhangDetail> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("materiale_name",materialeName);
            queryWrapper.eq("xh_id",ids);
            return this.getOne(queryWrapper);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int updatedetail(Integer ids, Double realityNumber, Double cailiaoshengyut, String lcNo) {
        return wzconsumetaizhangDetailMapper.updatedetail(ids,realityNumber,cailiaoshengyut,lcNo);
    }

    @Override
    public List<WzconsumetaizhangDetail> selectdetaillist(Integer id) {
        try{
            QueryWrapper<WzconsumetaizhangDetail> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("xh_id",id);
            return this.list(queryWrapper);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
