package com.trtm.iot.bhzSwPhbZibiao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import com.trtm.iot.bhzSwPhbZibiao.entity.BhzSwPhbZibiao;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

/**
 * @Description: 水稳理论配合比子表
 * @Author: jeecg-boot
 * @Date:   2021-11-23
 * @Version: V1.0
 */
public interface BhzSwPhbZibiaoMapper extends BaseMapper<BhzSwPhbZibiao> {

    @Select("select a.tianjiaji from  bhz_sw_phb_zibiao a left join bhz_sw_recipe b " +
            " on a.zhuziid=b.zhuziid where  b.shebeibianhao=#{shebeibianhao} and b.phbid=#{formulaNo} and a.cailiaono=#{cailiaoming}  limit 1")
    BhzSwPhbZibiao selectTianjiaji(String shebeibianhao, String formulaNo, String cailiaoming);

    @Select("select a.tianjiaji from  bhz_sw_phb_zibiao a left join bhz_sw_recipe b " +
            "on a.zhuziid=b.zhuziid where  b.shebeibianhao=#{shebeibianhao} and a.cailiaono=#{cailiaoming} ORDER BY b.llshijian DESC limit 1")
    BhzSwPhbZibiao selectTianjiajis(String shebeibianhao, String cailiaoming);
}
