package com.trtm.iot.tryajiangconfigure.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import com.trtm.iot.tryajiangconfigure.entity.TrYajiangConfigure;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

/**
 * @Description: 压浆配置表
 * @Author: jeecg-boot
 * @Date:   2023-03-28
 * @Version: V1.0
 */
public interface TrYajiangConfigureMapper extends BaseMapper<TrYajiangConfigure> {

    @Select("select * from tr_yajiang_configure where shebeino = #{shebeibianhao}")
    TrYajiangConfigure selectbyshebei(String shebeibianhao);
}
