package com.trtm.iot.TbhzcailiaoStatistics.mapper;

import java.util.List;

import com.trtm.iot.TbhzcailiaoStatistics.entity.BhzCementDetailStatistics;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Update;

/**
 * @Description: bhz_cement_detail_statistics
 * @Author: jeecg-boot
 * @Date:   2021-03-30
 * @Version: V1.0
 */
public interface BhzCementDetailStatisticsMapper extends BaseMapper<BhzCementDetailStatistics> {

    @Update("update bhz_cement_detail_statistics set reality_number=reality_number+#{reality_number} where id=#{id}")
    int AddReality_numberOne(Integer id, Float reality_number);

    @Update("update bhz_cement_detail_statistics set theory_number=theory_number+#{theory_number} where id=#{id}")
    int AddTheory_numberOne(Integer id,Float theory_number);

    @Update("update bhz_cement_detail_statistics set primary_number=primary_number+1 where id=#{id}")
    int AddPrimary_numberOne(Integer id);

    @Update("update bhz_cement_detail_statistics set middle_number=middle_number+1 where id=#{id}")
    int AddMiddle_numberOne(Integer id);

    @Update("update bhz_cement_detail_statistics set advanced_number=advanced_number+1 where id=#{id}")
    int AddAdvanced_numberOne(Integer id);


    List<BhzCementDetailStatistics> selectShebeiDetail(String begin, String end, List<String> shebeiNo);
}
