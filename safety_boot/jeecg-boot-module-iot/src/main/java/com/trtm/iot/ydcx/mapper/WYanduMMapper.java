package com.trtm.iot.ydcx.mapper;


import com.baomidou.mybatisplus.annotation.SqlParser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.trtm.iot.ydcx.entity.WYanduM;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @Description: w_yandu_m
 * @Author: jeecg-boot
 * @Date:   2021-04-26
 * @Version: V1.0
 */
public interface WYanduMMapper extends BaseMapper<WYanduM> {


    Map selectSYSwHeGeLv(@Param("tableName") String tableName,@Param("shebeiList") List<String> shebeiList);

    List<WYanduM> getListjt(String shebeilist, Integer curid);
}
