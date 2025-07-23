package com.trtm.iot.dianzhiweilan.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import com.trtm.iot.dianzhiweilan.entity.Dianzhiweilan;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

/**
 * @Description: 新电子围栏表
 * @Author: jeecg-boot
 * @Date:   2023-05-31
 * @Version: V1.0
 */
public interface DianzhiweilanMapper extends BaseMapper<Dianzhiweilan> {

    @Select("select shebeibianhao from dianzhiweilan group by shebeibianhao")
    List<String> selectshebei();

    @Select("select jindu,weidu from dianzhiweilan where shebeibianhao = #{shebei} and stutis = #{i}")
    List<Map<String, Object>> selectjinduweidu(String shebei,Integer i);
}
