package com.trtm.iot.pmscomwbs.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import com.trtm.iot.pmscomwbs.entity.PmsComWbs;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Update;

/**
 * @Description: 中交象山疏港高速(原海德 wbs数据)
 * @Author: jeecg-boot
 * @Date:   2021-07-16
 * @Version: V1.0
 */
public interface PmsComWbsMapper extends BaseMapper<PmsComWbs> {

    @Update("update pms_com_wbs set status=#{status} where ids=#{id}")
    int updatealertsate(Integer id, Integer status);
}
