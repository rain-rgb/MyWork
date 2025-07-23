package com.trtm.iot.wzliaocang.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import com.trtm.iot.wzliaocang.entity.Wzliaocang;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * @Description: 料仓配置表
 * @Author: jeecg-boot
 * @Date:   2021-05-07
 * @Version: V1.0
 */
public interface WzliaocangMapper extends BaseMapper<Wzliaocang> {

    @Select("select id from sys_depart where org_code=#{orgCode}")
    Map selectqueryone(String orgCode);

    @Select("select wzycljinchanggb.`cailiaono`,wzliaocang.`name`,SUM(wzycljinchanggb.jingzhongT) kucun from  wzliaocang left join wzycljinchanggb on wzliaocang.guid = wzycljinchanggb.LCbianhao  where wzliaocang.`name` LIKE '%号仓%' and wzliaocang.sys_org_code like 'A05A03A01A01%' " +
            "GROUP BY wzliaocang.`name` " +
            "order by field(name,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16) asc  ")
    List<Map> selectkucun(Integer pageNo, Integer pageSize);

    @Select("select lc_sn from wzlc_sn where wzlc_id = #{guid}")
    String getSnByGuid(String guid);

    /**材料管理，扫码入库，更新仓库累计进场量
     * @param num 数量
     * @param guid 仓储唯一值
     * @return
     */
    @Update("update wzliaocang set ljjinchang=ljjinchang+#{num} where guid=#{guid}")
    Integer updateWzliaocangljshiyongByGuid(@Param("num") Double num,@Param("guid") String guid);

}
