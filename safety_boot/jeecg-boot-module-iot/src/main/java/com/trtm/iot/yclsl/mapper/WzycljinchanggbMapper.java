package com.trtm.iot.yclsl.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import com.trtm.iot.yclsl.entity.Wzycljinchanggb;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * @Description: 原材料收料主表
 * @Author: jeecg-boot
 * @Date:   2021-04-21
 * @Version: V1.0
 */
public interface WzycljinchanggbMapper extends BaseMapper<Wzycljinchanggb> {
    @Update("update wzycljinchanggb set taizhangtj = #{taizhangtj} where id = #{id}")
    void updateistongji(Integer id, Integer taizhangtj);

    List<Wzycljinchanggb> selectLists( List<String> strsToList1,  Integer curid);

    List<Wzycljinchanggb> selectLists1( String strsToList1, Integer curid);

    List<Wzycljinchanggb> selectListsyjqs( String strsToList1, Integer curid);

    Wzycljinchanggb selectone(String jinchuliaodanno);

    List<Wzycljinchanggb> selectycljinchangList(Integer id, Integer taizhangtj);

    List<Wzycljinchanggb> selecones(int istongji,Integer curid);

    List<Wzycljinchanggb> selectyclList(Integer curid, String shebeilist);

    List<Wzycljinchanggb> selectydyclList(Integer curid, String shebeilist);

    @Select("select * from wzycljinchanggb where id > #{curid} and jingzhongTtj = #{jingzhongTtj} and LCbianhao is not null limit 100")
    List<Wzycljinchanggb> selectlistjz(Integer curid, int jingzhongTtj);


    List<Wzycljinchanggb> slistrqid(String shebeilist, Integer curid);
}
