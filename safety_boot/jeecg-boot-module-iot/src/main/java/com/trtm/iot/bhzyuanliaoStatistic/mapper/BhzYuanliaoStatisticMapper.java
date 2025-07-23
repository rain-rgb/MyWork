package com.trtm.iot.bhzyuanliaoStatistic.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import com.trtm.iot.bhzyuanliaoStatistic.entity.BhzYuanliaoStatistic;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

/**
 * @Description: 拌合站原材料统计
 * @Author: jeecg-boot
 * @Date:   2022-10-10
 * @Version: V1.0
 */
public interface BhzYuanliaoStatisticMapper extends BaseMapper<BhzYuanliaoStatistic> {

    @Select("select * from bhz_yuanliao_statistic where shebei_no = #{shebeiNo} and materiale_name = #{materialeName}")
    BhzYuanliaoStatistic selectByShebei(String shebeiNo, String materialeName);
}
