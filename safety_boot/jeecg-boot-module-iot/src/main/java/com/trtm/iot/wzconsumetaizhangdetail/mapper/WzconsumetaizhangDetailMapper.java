package com.trtm.iot.wzconsumetaizhangdetail.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import com.trtm.iot.wzconsumetaizhangdetail.entity.WzconsumetaizhangDetail;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Update;

/**
 * @Description: 物资原材料消耗台账材料子表
 * @Author: jeecg-boot
 * @Date:   2021-09-07
 * @Version: V1.0
 */
public interface WzconsumetaizhangDetailMapper extends BaseMapper<WzconsumetaizhangDetail> {

    @Update("update wzconsumetaizhang_detail set reality_number =#{realityNumber} ,cailiaoshengyut =#{cailiaoshengyut} where xh_id=#{ids} and lcbianhao =#{lcNo}")
    int updatedetail(Integer ids, Double realityNumber, Double cailiaoshengyut, String lcNo);
}
