package com.trtm.iot.spreadandcrush.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import com.trtm.iot.spreadandcrush.entity.Spreadandcrush;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 摊铺碾压
 * @Author: jeecg-boot
 * @Date:   2023-04-20
 * @Version: V1.0
 */
public interface SpreadandcrushMapper extends BaseMapper<Spreadandcrush> {


    List<Spreadandcrush> getList(String Shebeino, Integer id);
}
