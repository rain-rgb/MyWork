package com.trtm.iot.lqbhzrecipe.service.impl;

import com.trtm.iot.lqbhzrecipe.entity.BhzLqPhbZibiao;
import com.trtm.iot.lqbhzrecipe.mapper.BhzLqPhbZibiaoMapper;
import com.trtm.iot.lqbhzrecipe.service.IBhzLqPhbZibiaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: bhz_lq_phb_zibiao
 * @Author: jeecg-boot
 * @Date:   2021-03-30
 * @Version: V1.0
 */
@Service
public class BhzLqPhbZibiaoServiceImpl extends ServiceImpl<BhzLqPhbZibiaoMapper, BhzLqPhbZibiao> implements IBhzLqPhbZibiaoService {
    @Autowired
    private BhzLqPhbZibiaoMapper bhzLqPhbZibiaoMapper;
    /**
     * 查询添加剂
     * @param shebeibianhao
     * @param
     * @param
     * @return
     */
    @Override
    public BhzLqPhbZibiao selectTianjiaji(String shebeibianhao, String FormulaNo , String cailiaoming) {
        return bhzLqPhbZibiaoMapper.selectTianjiaji(shebeibianhao,FormulaNo,cailiaoming);
    }

    @Override
    public BhzLqPhbZibiao selectTianjiajis(String shebeibianhao, Double llysb , String cailiaoming) {
        return bhzLqPhbZibiaoMapper.selectTianjiajis(shebeibianhao,llysb,cailiaoming);
    }

    @Override
    public BhzLqPhbZibiao selectTianjiajiByYsb(String sheibeibianhao, String youshibi1, String youshibi2, String cailiaoming) {
        return bhzLqPhbZibiaoMapper.selectTianjiajiByYsb(sheibeibianhao,youshibi1,youshibi2,cailiaoming);
    }

    @Override
    public double selectSum(String formulaNo, String shebeibianhao) {
        return bhzLqPhbZibiaoMapper.selectSum(formulaNo,shebeibianhao);
    }
}
