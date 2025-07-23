package com.trtm.iot.lqbhzrecipe.service;

import com.trtm.iot.lqbhzrecipe.entity.BhzLqPhbZibiao;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: bhz_lq_phb_zibiao
 * @Author: jeecg-boot
 * @Date:   2021-03-30
 * @Version: V1.0
 */
public interface IBhzLqPhbZibiaoService extends IService<BhzLqPhbZibiao> {
    /**
     * 查询添加剂
     * @param shebeibianhao
     * @param
     * @param
     * @return
     */
    BhzLqPhbZibiao selectTianjiaji(String shebeibianhao, String FormulaNo , String cailiaoming);

    BhzLqPhbZibiao selectTianjiajis(String shebeibianhao, Double llysb , String cailiaoming);

    BhzLqPhbZibiao selectTianjiajiByYsb(String sheibeibianhao , String youshibi1 ,String youshibi2 ,String cailiaoming);

    double selectSum(String formulaNo, String shebeibianhao);

}
