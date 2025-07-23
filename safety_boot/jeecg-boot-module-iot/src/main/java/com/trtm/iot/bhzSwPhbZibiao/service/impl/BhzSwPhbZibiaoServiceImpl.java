package com.trtm.iot.bhzSwPhbZibiao.service.impl;

import com.trtm.iot.bhzSwPhbZibiao.entity.BhzSwPhbZibiao;
import com.trtm.iot.bhzSwPhbZibiao.mapper.BhzSwPhbZibiaoMapper;
import com.trtm.iot.bhzSwPhbZibiao.service.IBhzSwPhbZibiaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 水稳理论配合比子表
 * @Author: jeecg-boot
 * @Date:   2021-11-23
 * @Version: V1.0
 */
@Service
public class BhzSwPhbZibiaoServiceImpl extends ServiceImpl<BhzSwPhbZibiaoMapper, BhzSwPhbZibiao> implements IBhzSwPhbZibiaoService {

    @Autowired
    private BhzSwPhbZibiaoMapper bhzSwPhbZibiaoMapper;
    @Override
    public BhzSwPhbZibiao selectTianjiaji(String shebeibianhao, String formulaNo, String cailiaoming) {
        return bhzSwPhbZibiaoMapper.selectTianjiaji(shebeibianhao,formulaNo,cailiaoming);

    }

    @Override
    public BhzSwPhbZibiao selectTianjiajis(String shebeibianhao, String cailiaoming) {
        return bhzSwPhbZibiaoMapper.selectTianjiajis(shebeibianhao,cailiaoming);
    }
}
