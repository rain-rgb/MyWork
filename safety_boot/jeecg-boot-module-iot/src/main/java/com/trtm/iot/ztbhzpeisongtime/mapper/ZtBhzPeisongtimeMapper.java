package com.trtm.iot.ztbhzpeisongtime.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import com.trtm.iot.ztbhzpeisongtime.entity.ZtBhzPeisongtime;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

/**
 * @Description: 自建拌和站配送考核标准时间表
 * @Author: jeecg-boot
 * @Date:   2023-11-01
 * @Version: V1.0
 */
public interface ZtBhzPeisongtimeMapper extends BaseMapper<ZtBhzPeisongtime> {

    @Select("select * from zt_bhz_peisongtime where engineering = #{projname}")
    ZtBhzPeisongtime selecdw(String projname);

    @Select("select * from zt_bhz_peisongtime where engineering = #{projname}")
    ZtBhzPeisongtime ztyjsb(String projname);
}
