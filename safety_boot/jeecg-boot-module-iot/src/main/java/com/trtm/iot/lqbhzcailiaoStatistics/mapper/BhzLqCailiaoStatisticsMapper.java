package com.trtm.iot.lqbhzcailiaoStatistics.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import com.trtm.iot.lqbhzcailiaoStatistics.entity.BhzLqCailiaoStatistics;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * @Description: bhz_lq_cailiao_statistics
 * @Author: jeecg-boot
 * @Date:   2021-03-30
 * @Version: V1.0
 */
public interface BhzLqCailiaoStatisticsMapper extends BaseMapper<BhzLqCailiaoStatistics> {
    @Select("select id from bhz_lq_cailiao_statistics where  cs_id=#{cs_id} and cailiaoid=#{cailiaoid}  LIMIT 1")
    BhzLqCailiaoStatistics selectId(int cs_id , String cailiaoid);
//    @Update("update bhz_lq_cailiao_statistics set reality_number=reality_number+#{reality_number} where id=#{id}")
//    void updateRealityById(float reality_number , int id);
    @Update("update bhz_lq_cailiao_statistics set primary_number=primary_number+1 where id=#{id}")
    void AddPrimary_numberOne(int id);
    @Update("update bhz_lq_cailiao_statistics set middle_number=middle_number+1 where id=#{id}")
    void AddMiddle_numberOne(int id);
    @Update( "update bhz_lq_cailiao_statistics set advanced_number=advanced_number+1 where id=#{id}")
    void AddAdvanced_numberOne(int id);
}
