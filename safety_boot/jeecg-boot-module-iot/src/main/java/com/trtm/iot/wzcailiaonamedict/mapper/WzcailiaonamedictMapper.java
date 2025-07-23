package com.trtm.iot.wzcailiaonamedict.mapper;

import java.util.List;
import java.util.Map;

import com.trtm.iot.wzcailiaonamedict.entity.Wzcailiaonamedict;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * @Description: 材料配置主表
 * @Author: jeecg-boot
 * @Date:   2021-05-07
 * @Version: V1.0
 */
public interface WzcailiaonamedictMapper extends BaseMapper<Wzcailiaonamedict> {

    @Select("select testid from wzcailiaonamedict  order by testid desc  limit 1")
    public  List<Wzcailiaonamedict> arrayOnecailiaos();
    @Select("select id from sys_depart where org_code=#{orgCode}")
    Map selectqueryone(String orgCode);

    @Select("select distinct nodetype from wzcailiaonamedict where sys_org_code like concat(#{orgCode},'%') order by -nodetype desc")
    List<String> queryNodetypeList1(String orgCode);

    @Select("select distinct nodetype from wzcailiaonamedict order by -nodetype desc")
    List<String> queryNodetypeList2();

    @Select("select distinct nodetype from wzcailiaonamedict group by nodetype")
    List<String> selectNodetype();

    String selectBylmcailiaolx(int i);

    List<Map> selectByYCCP(Integer cprule, Integer ycrule, String code);

    List<String> selectCailiaoNoByName(String cailiaoName);

    List<String> selectListByYCCP(Integer cprule, Integer ycrule, String code);


    List<String> getcailiaoNoList(String s);

    List<String> getGgXh(String nodeType, String orgCode);



}
