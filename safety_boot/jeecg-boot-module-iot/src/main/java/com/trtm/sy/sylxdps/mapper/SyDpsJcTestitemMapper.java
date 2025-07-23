package com.trtm.sy.sylxdps.mapper;


import com.trtm.sy.sylxdps.entity.SyDpsJcTestitem;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @Description: sy_dps_jc_testitem
 * @Author: jeecg-boot
 * @Date:   2023-01-10
 * @Version: V1.0
 */
public interface SyDpsJcTestitemMapper extends BaseMapper<SyDpsJcTestitem> {

    List<Map> getPList(@Param("titCode") String titCode);

    List<Map> getList(@Param("titCode") String titCode, @Param("tiNoList") List<String> tiNoList);

    Map<String, Object> findById(String id);



    Map<String, Object> findSampById(String id);

    List<Map> findReportSBySampleNo(String sampleNo);

    List<Map> findQRCodeBySampleNo(String sampleNo);

    List<Map> findPicBySampleNo(String sampleNo);

    List<Map> findYjBySampleNo(String sampleNo);

    List<String> findColumnName(String tiNo);

    Map<String, Object> findByTiNos(List<String> columnName, String tiNo, String sampleNo, String tiNoTemp);



    List<Map<String, Object>> findSql(@Param("id") String id, @Param("type") String type);

    List<Map<String, Object>> findSql1(String type);

    List<Map<String, Object>> findSql2(String value);

    String selectTiNoByUserId(@Param("roleIds") List<String> roleIds, @Param("titCode") String titCode);
}
