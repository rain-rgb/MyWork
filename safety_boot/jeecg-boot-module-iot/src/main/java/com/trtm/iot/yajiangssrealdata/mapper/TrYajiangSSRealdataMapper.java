package com.trtm.iot.yajiangssrealdata.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import com.trtm.iot.yajiangssrealdata.entity.TrYajiangSSRealdata;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

/**
 * @Description: tr_yajiang_s_s_realdata
 * @Author: jeecg-boot
 * @Date:   2023-05-11
 * @Version: V1.0
 */
public interface TrYajiangSSRealdataMapper extends BaseMapper<TrYajiangSSRealdata> {

    @Select("select DISTINCT shebeibianh from tr_yajiang_s_s_realdata")
    List<String> getSbList();
}
