package com.trtm.iot.bhzlqjipeifanwei.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import com.trtm.iot.bhzlqjipeifanwei.entity.BhzLqJipeiFanwei;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 沥青级配范围配置表
 * @Author: jeecg-boot
 * @Date:   2022-05-16
 * @Version: V1.0
 */
public interface BhzLqJipeiFanweiMapper extends BaseMapper<BhzLqJipeiFanwei> {

    List<BhzLqJipeiFanwei> selectlist(String shebeino, Integer id);

}
