package com.trtm.iot.zhanglassrealdata.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import com.trtm.iot.zhanglassrealdata.entity.TrZhanglaSSRealdata;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

/**
 * @Description: tr_zhangla_s_s_realdata
 * @Author: jeecg-boot
 * @Date:   2023-05-12
 * @Version: V1.0
 */
public interface TrZhanglaSSRealdataMapper extends BaseMapper<TrZhanglaSSRealdata> {

    @Select("select DISTINCT shebeibianhao from tr_zhangla_s_s_realdata")
    List<String> getSbList();
}
