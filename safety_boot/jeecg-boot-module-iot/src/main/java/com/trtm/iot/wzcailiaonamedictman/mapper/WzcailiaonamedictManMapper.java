package com.trtm.iot.wzcailiaonamedictman.mapper;

import java.util.List;
import java.util.Map;

import com.trtm.iot.wzcailiaonamedict.entity.Wzcailiaonamedict;
import com.trtm.iot.wzcailiaonamedictman.entity.WzcailiaonamedictManVo;
import com.trtm.iot.yclud.entity.GuiGeXingHao;
import org.apache.ibatis.annotations.Param;
import com.trtm.iot.wzcailiaonamedictman.entity.WzcailiaonamedictMan;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

/**
 * @Description: wzcailiaonamedict_man
 * @Author: jeecg-boot
 * @Date:   2022-08-08
 * @Version: V1.0
 */
public interface WzcailiaonamedictManMapper extends BaseMapper<WzcailiaonamedictMan> {

    @Select("select id from wzcailiaonamedict_man  order by id desc  limit 1")
    List<WzcailiaonamedictMan> arrayOnecailiaos();

    @Select("select id from sys_depart where org_code=#{orgCode}")
    Map selectqueryone(String orgCode);
    String selectBylmcailiaolx(int i);
    List<Map> selectByYCCP(Integer cprule, Integer ycrule, String code);

    List<String> selectCailiaoNoByName(String cailiaoName);

    List<String> selectListByYCCP(Integer cprule, Integer ycrule, String code);

    List<String> getcailiaoNo(List types);

    List<GuiGeXingHao> getByGgXh(String nodeType);

    List<WzcailiaonamedictMan> getNodeType();
    List<WzcailiaonamedictMan> queryMaterialByComponentId(@Param("id") String id);

    List<WzcailiaonamedictMan> queryMaterialByNodeType(@Param("nodeType")String nodeType);

    List<WzcailiaonamedictMan> queryMaterialList(@Param("sys_depart_orgcode") String sys_depart_orgcode,
                                                 @Param("materialName") String materialName,
                                                 @Param("startDate") String startDate,
                                                 @Param("endDate") String endDate);

    /**根据设备编号查询材料信息
     * @param cailiaoNo
     * @return
     */
    WzcailiaonamedictMan getWzcailiaonamedictManById(@Param("id") String id);

    /**根据单位编号，查找对应材料单位
     * @param type
     * @return
     */
    String DanweiByDanweiType(@Param("type") String type);

    List<WzcailiaonamedictManVo> getWzcailiaonamedictManByCaiLiaoName(@Param("subItemName") String subItemName);
}
