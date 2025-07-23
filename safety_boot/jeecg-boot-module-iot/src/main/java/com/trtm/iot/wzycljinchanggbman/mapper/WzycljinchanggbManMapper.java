package com.trtm.iot.wzycljinchanggbman.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import com.trtm.iot.wzycljinchanggbman.entity.WzycljinchanggbMan;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * @Description: wzycljinchanggb_man
 * @Author: jeecg-boot
 * @Date:   2022-08-08
 * @Version: V1.0
 */
public interface WzycljinchanggbManMapper extends BaseMapper<WzycljinchanggbMan> {

    List<WzycljinchanggbMan> selectycljinchangList(Integer id, int taizhangtj);

    @Update("update wzycljinchanggb_man set taizhangtj = #{taizhangtj} where id = #{id}")
    void updateistongji(Integer id, Integer taizhangtj);

    @Select("select DISTINCT  a.jystatus from wztaizhang a left join wzycljinchanggb_man b on a.id = b.taizhangid where b.taizhangid = #{taizhangid}")
    Integer getJYStatus(Integer taizhangid);

    @Select("select cailiaoNo from wzcailiaonamedict_man where nodeType = #{nodetype}")
    List<String> getCailiaoByNodetype(String nodetype);
}
