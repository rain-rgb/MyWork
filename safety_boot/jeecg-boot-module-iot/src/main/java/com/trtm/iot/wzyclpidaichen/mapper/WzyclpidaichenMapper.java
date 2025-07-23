package com.trtm.iot.wzyclpidaichen.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import com.trtm.iot.wzyclpidaichen.entity.Wzyclpidaichen;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: wzyclpidaichen
 * @Author: jeecg-boot
 * @Date:   2024-04-18
 * @Version: V1.0
 */
public interface WzyclpidaichenMapper extends BaseMapper<Wzyclpidaichen> {

    List<Wzyclpidaichen> slistrqid(String shebeilist, Integer curid);
}
