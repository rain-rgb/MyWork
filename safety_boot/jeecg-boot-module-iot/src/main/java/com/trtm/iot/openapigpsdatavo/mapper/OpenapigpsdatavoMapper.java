package com.trtm.iot.openapigpsdatavo.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import com.trtm.iot.openapigpsdatavo.entity.Openapigpsdatavo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 摊铺碾压数据
 * @Author: jeecg-boot
 * @Date:   2023-04-21
 * @Version: V1.0
 */
public interface OpenapigpsdatavoMapper extends BaseMapper<Openapigpsdatavo> {

    List<Openapigpsdatavo> getList(String shebeino, Integer id);

    List<Openapigpsdatavo> getBLList(String shebeino, Integer id);

    List<Openapigpsdatavo> getListjt(String shebeino, Integer id);
}
