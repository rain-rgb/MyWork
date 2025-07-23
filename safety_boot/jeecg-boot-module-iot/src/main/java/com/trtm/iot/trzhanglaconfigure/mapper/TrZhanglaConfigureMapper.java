package com.trtm.iot.trzhanglaconfigure.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import com.trtm.iot.trzhanglaconfigure.entity.TrZhanglaConfigure;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

/**
 * @Description: 张拉预警配置表
 * @Author: jeecg-boot
 * @Date:   2023-02-14
 * @Version: V1.0
 */
public interface TrZhanglaConfigureMapper extends BaseMapper<TrZhanglaConfigure> {

    @Select("select * from tr_zhangla_configure where shebei_no = #{shebeibianhao}")
    TrZhanglaConfigure selectbyshebei(String shebeibianhao);
}
