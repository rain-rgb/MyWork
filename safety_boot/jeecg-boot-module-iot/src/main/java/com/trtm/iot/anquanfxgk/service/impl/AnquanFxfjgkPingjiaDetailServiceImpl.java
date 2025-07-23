package com.trtm.iot.anquanfxgk.service.impl;

import com.trtm.iot.anquanfxgk.entity.AnquanFxfjgkPingjiaDetail;
import com.trtm.iot.anquanfxgk.mapper.AnquanFxfjgkPingjiaDetailMapper;
import com.trtm.iot.anquanfxgk.service.IAnquanFxfjgkPingjiaDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 * @Description: anquan_fxfjgk_pingjia_detail
 * @Author: jeecg-boot
 * @Date:   2024-09-04
 * @Version: V1.0
 */
@Service
public class AnquanFxfjgkPingjiaDetailServiceImpl extends ServiceImpl<AnquanFxfjgkPingjiaDetailMapper, AnquanFxfjgkPingjiaDetail> implements IAnquanFxfjgkPingjiaDetailService {

    @Autowired
    private AnquanFxfjgkPingjiaDetailMapper mapper;

    @Override
    public Integer deleteAnquanFxfjgkPingjiaDetailByParent(String parentId) {
        return mapper.deleteAnquanFxfjgkPingjiaDetailByParent(parentId);
    }

    @Override
    public List<AnquanFxfjgkPingjiaDetail> getAnquanFxfjgkPingjiaDetail(String parentId) {
        return mapper.getAnquanFxfjgkPingjiaDetailByParent(parentId);
    }
}
