package com.trtm.iot.wzcailiaonamedictall.mapper;

import java.util.List;

import com.trtm.iot.wzcailiaonamedictall.entity.WzcailiaonamedictAll;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.data.repository.query.Param;

/**
 * @Description: wzcailiaonamedict_all
 * @Author: jeecg-boot
 * @Date:   2023-09-06
 * @Version: V1.0
 */
public interface WzcailiaonamedictAllMapper extends BaseMapper<WzcailiaonamedictAll> {

    @Select("select * from ycl_cailiaodict where cailiaoNo=#{cailiaonno}")
    WzcailiaonamedictAll getcailiaoInfo(String cailiaonno);


}
