package com.trtm.iot.swbhzcailiaoStatistics.mapper;

import java.util.List;

import io.swagger.models.auth.In;
import org.apache.ibatis.annotations.Param;
import com.trtm.iot.swbhzcailiaoStatistics.entity.BhzSwCailiaoStatistics;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Update;

/**
 * @Description: bhz_sw_cailiao_statistics
 * @Author: jeecg-boot
 * @Date:   2021-03-30
 * @Version: V1.0
 */
public interface BhzSwCailiaoStatisticsMapper extends BaseMapper<BhzSwCailiaoStatistics> {

    @Update("update bhz_sw_cailiao_statistics set reality_number=reality_number+#{reality_number} where id=#{id}")
    int AddReality_numberOne(Integer id, Float reality_number);

    @Update("update bhz_sw_cailiao_statistics set theory_number=theory_number+#{theory_number} where id=#{id}")
    int AddTheory_numberOne(Integer id,Float theory_number);

    @Update("update bhz_sw_cailiao_statistics set primary_number=primary_number+1 where id=#{id}")
    int AddPrimary_numberOne(Integer id);

    @Update("update bhz_sw_cailiao_statistics set middle_number=middle_number+1 where id=#{id}")
    int AddMiddle_numberOne(Integer id);

    @Update("update bhz_sw_cailiao_statistics set advanced_number=advanced_number+1 where id=#{id}")
    int AddAdvanced_numberOne(Integer id);
}
