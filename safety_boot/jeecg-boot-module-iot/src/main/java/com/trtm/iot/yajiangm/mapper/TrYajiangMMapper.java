package com.trtm.iot.yajiangm.mapper;

import java.util.List;
import java.util.Map;

import com.trtm.iot.yajiangm.entity.TrYajiangSM;
import com.trtm.iot.yajiangrenwudan.entity.TrYajiangRenwudan;
import org.apache.ibatis.annotations.Param;
import com.trtm.iot.yajiangm.entity.TrYajiangM;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * @Description: 压浆主表信息
 * @Author: jeecg-boot
 * @Date:   2021-09-06
 * @Version: V1.0
 */
public interface TrYajiangMMapper extends BaseMapper<TrYajiangM> {
        List<Map<String, Object>> countList(@Param("orgCode") String orgCode);

        List<TrYajiangSM> selectListstatistics(Integer id, Integer statistics);

        @Update("update tr_yajiang_m set statistics=#{statistics} where yjsbbh=#{sbjno}")
        void updateStatistics(String sbjno, Integer statistics);

    TrYajiangM queryone(String shebeiNo);

    List<TrYajiangM> selectLists(String uuid);

    List<TrYajiangM> selectListzlpz(String shebeibianhao);

    List<TrYajiangM> selectListdata(String shebeibianhao);

    List<TrYajiangM> selectyjList(String shebeiNo, Integer id);

    List<TrYajiangM> selectrcyjList(String shebeiNo, Integer id);

    List<TrYajiangM> selectyjblList(String shebeiNo, Integer id);

    List<TrYajiangM> selectydyjList(String shebeiNo, Integer id);

    List<TrYajiangM> selectyj47toyd(String shebeiNo, Integer id);

    List<TrYajiangM> selectListToSHYJ(String shebeiNo, Integer id);

    @Select("select * from tr_yajiang_m where id > #{curid} and is_over_level = 0 limit 100")
    List<TrYajiangM> selectbigid(Integer curid);

    @Select("SELECT * FROM tr_yajiang_m where syjid = #{syjid}")
    TrYajiangM selectceslll(String syjid);

    Integer findRenwudanzs(List<String> yajiangRenwudans);

    Integer findRenwudanyjs(List<String> yajiangRenwudans);

    Integer findRenwudanbhs(List<String> yajiangRenwudans);

    @Select("select syjid from tr_yajiang_m where yjsbbh = #{shebei}")
    List<String> selectListsb(String shebei);

    List<TrYajiangM> selectUnifiedProcess(Integer curid, Integer alertstate, List<String> shebeiList, Integer overLevel);

    List<TrYajiangM> selectBySBList(String shebeilist, Integer curid);

    List<TrYajiangM> selectListst(String shebeilist, Integer curid);

    List<TrYajiangM> selectlist();

    List<TrYajiangM> selectListoJG(String shebeilist, Integer curid);
}
