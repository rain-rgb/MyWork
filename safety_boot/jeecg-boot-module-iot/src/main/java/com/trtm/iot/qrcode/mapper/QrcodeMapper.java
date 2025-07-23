package com.trtm.iot.qrcode.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import com.trtm.iot.qrcode.entity.Qrcode;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

/**
 * @Description: 二维码数据表信息
 * @Author: jeecg-boot
 * @Date:   2021-08-10
 * @Version: V1.0
 */
public interface QrcodeMapper extends BaseMapper<Qrcode> {

//    @Select("SELECT " +
//            " n as number, id, SUBSTRING_INDEX( SUBSTRING_INDEX( uuid, ',', n ), ',', - 1 ) AS uuid  " +
//            "FROM " +
//            " `qrcode`, " +
//            " ( SELECT @rownum := @rownum + 1 AS n FROM ( SELECT @rownum := 0 ) r, `qrcode` ) x  " +
//            "WHERE id = #{id}  AND n > #{minin}  AND n <= #{maxn}  AND n <= 200")

    @Select("SELECT #{maxn} as number , id , SUBSTRING_INDEX( SUBSTRING_INDEX( uuid, ',', #{maxn} ), ',', - 1 ) AS uuid FROM `qrcode` WHERE id = #{id}  ")
    Qrcode getQRlistByidN(Integer id, Integer maxn, Integer minin);
}
