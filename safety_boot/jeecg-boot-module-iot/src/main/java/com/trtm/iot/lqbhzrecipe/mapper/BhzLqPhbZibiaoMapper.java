package com.trtm.iot.lqbhzrecipe.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import com.trtm.iot.lqbhzrecipe.entity.BhzLqPhbZibiao;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

/**
 * @Description: bhz_lq_phb_zibiao
 * @Author: jeecg-boot
 * @Date:   2021-03-30
 * @Version: V1.0
 */
public interface BhzLqPhbZibiaoMapper extends BaseMapper<BhzLqPhbZibiao> {
    /**
     * 查询添加剂
     */
    @Select("select a.tianjiaji from  bhz_lq_phb_zibiao a left join bhz_lq_recipe b " +
            " on a.zhuziid=b.zhuziid where  b.shebeibianhao=#{shebeibianhao} and b.phbid=#{FormulaNo} and a.cailiaono=#{cailiaoming}  limit 1")
    BhzLqPhbZibiao selectTianjiaji(String shebeibianhao, String FormulaNo , String cailiaoming);


    @Select("select a.tianjiaji from  bhz_lq_phb_zibiao a left join bhz_lq_recipe b " +
            " on a.zhuziid=b.zhuziid where b.llmoren=1 and  b.shebeibianhao=#{shebeibianhao} and b.llysb=#{llysb} and a.cailiaono=#{cailiaoming}  limit 1")
    BhzLqPhbZibiao selectTianjiajis(String shebeibianhao, Double llysb , String cailiaoming);

    /**
     *查询添加剂
     */
    @Select("select a.tianjiaji from  bhz_lq_phb_zibiao a left join bhz_lq_recipe b " +
            " on a.zhuziid=b.zhuziid where b.llmoren=1 and b.shebeibianhao=#{sheibeibianhao}  and b.llysb>#{youshibi1} and b.llysb<#{youshibi2}" +
            " and a.cailiaono=#{cailiaoming}  limit 1")
    BhzLqPhbZibiao selectTianjiajiByYsb(String sheibeibianhao , String youshibi1 ,String youshibi2 ,String cailiaoming);

    @Select("SELECT IFNULL(sum( z.tianjiaji ) ,0.0)  FROM bhz_lq_recipe r  left join bhz_lq_phb_zibiao z on r.zhuziid = z.zhuziid where r.shebeibianhao = #{shebeibianhao} and r.phbid = #{formulaNo}")
    double selectSum(String formulaNo, String shebeibianhao);
}
