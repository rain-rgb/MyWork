package com.trtm.iot.byssta.mapper;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import com.trtm.iot.byssta.entity.BysSta;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 标养室统计信息表
 * @Author: jeecg-boot
 * @Date:   2022-05-11
 * @Version: V1.0
 */
public interface BysStaMapper extends BaseMapper<BysSta> {

    BysSta selectlimit(Date datanyr1, String shebeino);
}
