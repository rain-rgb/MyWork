package com.trtm.sy.registertable.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.trtm.sy.registertable.model.SyFormRecord;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;


import java.util.List;
import java.util.Map;

public interface SyFormRecordMapper extends BaseMapper<SyFormRecord> {

    /**
     * 根据样品ID 获取样品名称 以及选择了那些检测参数
     *
     * @param sampleId
     * @return
     */
    @Select("SELECT\n" +
            "DISTINCT\n" +
            "\typjccs.jlbid,\n" +
            "\typjccs.jlbmc,\n" +
            "\typjccs.jlbbh " +
            " FROM\n" +
            "\t(\n" +
            "\tSELECT DISTINCT\n" +
            "\t\t(\n" +
            "\t\tsubstring_index( substring_index( a.col, ',', b.help_topic_id + 1 ), ',',- 1 )) AS jccs,\n" +
            "\t\ta.yp_name,\n" +
            "\t\typid,\n" +
            "\t\typmc_id \n" +
            "\tFROM\n" +
            "\t\t(\n" +
            "\t\tSELECT\n" +
            "\t\t\typid,\n" +
            "\t\t\typ_name,\n" +
            "\t\t\tjccs AS col,\n" +
            "\t\t\typmc_id \n" +
            "\t\tFROM\n" +
            "\t\t\tsy_yp AS yp\n" +
            "\t\t\tLEFT JOIN sy_ypmc AS ypmc ON yp.ypmc = ypmc.yp_name_code \n" +
            "\t\tWHERE\n" +
            "\t\t\typ.ypid = #{sampleId} \n" +
            "\t\t) AS a\n" +
            "\t\tJOIN mysql.help_topic AS b ON b.help_topic_id <(\n" +
            "\t\t\tchar_length( a.col ) - char_length(\n" +
            "\t\t\tREPLACE ( a.col, ',', '' ))+ 1 \n" +
            "\t\t) \n" +
            "\t) AS yp\n" +
            "\tLEFT JOIN sy_ypjccs AS ypjccs ON yp.jccs = ypjccs.ypjccs_id")
    List<Map<String, Object>> getSampleTable(@Param("sampleId") String sampleId);


    /**
     * 根据样品ID 获取样品名称 以及选择了那些检测参数
     *
     * @param sampleId
     * @return
     */
    @Select("SELECT ypid,ypjccs_id,yp_name,yp_jccs,ypmc_id,jcsbhbh FROM (\n" +
            "SELECT DISTINCT ( substring_index( substring_index( a.col, ',', b.help_topic_id + 1 ), ',',- 1 )) AS jccs ,a.yp_name ,ypid,ypmc_id\n" +
            "FROM(SELECT ypid, yp_name, jccs AS col ,ypmc_id\n" +
            "FROM sy_yp AS yp LEFT JOIN sy_ypmc AS ypmc ON yp.ypmc = ypmc.yp_name_code \n" +
            "WHERE yp.ypid = #{sampleId} \n" +
            ") AS a JOIN mysql.help_topic AS b ON b.help_topic_id <(char_length( a.col ) - char_length(REPLACE ( a.col, ',', '' ))+ 1 )\n" +
            ") AS yp\n" +
            "LEFT JOIN sy_ypjccs AS ypjccs ON yp.jccs = ypjccs.ypjccs_id")
    List<Map<String, Object>> getSampleParams(@Param("sampleId") String sampleId);

    /**
     * 获取样品表单参数公共规则
     *
     * @return
     */
    @Select("SELECT bgbhids FROM sy_rwd  WHERE ypid = #{sampleId}")
    String getRwdBgbhids(@Param("sampleId") String sampleId);

    @Select("SELECT * FROM sy_record_sb WHERE ypid = #{sampleId}")
    List<Map<String, Object>> getRecordSb(@Param("sampleId") String sampleId);

    @Insert("INSERT INTO sy_record_sb VALUES(#{a},#{b},#{c},#{d},#{e},#{f},#{r},#{o},#{bz}); ")
    void addRecordSb(@Param("a") String a,
                     @Param("b") String b,
                     @Param("c") String c,
                     @Param("d") String d,
                     @Param("e") String e,
                     @Param("f") String f,
                     @Param("r") String r,
                     @Param("o") String o,
                     @Param("bz") String bz
    );

    void deleteRecordSb(@Param("ids") List<String> ids);

    List<SyFormRecord> delByTableNum(String tableNum);
}
