package com.trtm.iot.bhzStatistics.mapper;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.trtm.iot.bhzStatistics.controller.BhzCementStatisticsController;
import com.trtm.sy.sydpssysample.entity.SysDepart;
import org.apache.ibatis.annotations.Param;
import com.trtm.iot.bhzStatistics.entity.BhzCementStatistics;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * @Description: 拌合站统计表
 * @Author: jeecg-boot
 * @Date:   2021-03-22
 * @Version: V1.0
 */
public interface BhzCementStatisticsMapper extends BaseMapper<BhzCementStatistics> {

    @Update("update bhz_cement_statistics set istongji =#{istongji} where id =#{id}")
    int updateistongji(int id, Integer istongji);

    List<Map<String, Object>> countList(@Param("orgCode") String orgCode);
    @Select("SELECT b.org_code,b.depart_name  FROM `sys_depart` a join sys_depart b on a.id = b.parent_id where a.org_code=#{orgCode}")
    List<Map<Object,String>> selectOrgCode(String orgCode);

    List<Map<String, Object>> countLists(String orgCategorys, String orgCode, Integer date);

    BhzCementStatistics selectlimit( Date datanyr1,String  projectName,String poureLocation, String jobLocation,String formulaNo,String strengthRank,String shebeiNo);

    @Update("update bhz_cement_statistics set all_dish=#{allsum},estimate_number=#{sumfl}, primary_dish =#{cjsum},all_overproof_dish=#{allsums},middle_dish=#{zjsum},advanced_dish=#{gjsum} where id =#{id}")
    boolean updatestatisticsone(Integer id,Integer allsum ,Double sumfl,Integer cjsum,Integer allsums,Integer zjsum,Integer gjsum);

    @Select("select id,shebei_no from bhz_cement_statistics where shebei_no =#{shebeino} and id > #{curid}")
    List<BhzCementStatistics> selectjbzzones(String shebeino,Integer curid);

    List<BhzCementStatistics> selectbydays(String shebeiNo);

    Integer selectCountNumber(List<String> querySheBeiList,String dateNowStr);

    BhzCementStatistics selectsum(List<String> querySheBeiList, String dateNowStr);

    BhzCementStatistics findRenwudanzs(List<String> querySheBeiList);

    List<String> getQuarters();

    @Select("select * from sys_depart where org_code like concat(#{sysDepartOrgcode},'%') and org_category = 7")
    List<SysDepart> getDepartList(String sysDepartOrgcode);

    @Select("select sbjno from shebei_info where sys_org_code like concat(#{orgCode},'%')")
    List<String> getSbjList(String orgCode);
}
