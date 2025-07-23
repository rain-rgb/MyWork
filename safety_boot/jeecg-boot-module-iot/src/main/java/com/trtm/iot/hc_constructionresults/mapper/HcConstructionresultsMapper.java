package com.trtm.iot.hc_constructionresults.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import com.trtm.iot.hc_constructionresults.entity.HcConstructionresults;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * @Description: 施工成果
 * @Author: jeecg-boot
 * @Date:   2023-04-25
 * @Version: V1.0
 */
public interface HcConstructionresultsMapper extends BaseMapper<HcConstructionresults> {

    List<String> selectsum(List<String> querySheBeiList, String dateNowStr);

    @Select("select sum(workMileage) workMileage from hc_constructionresults where projectId = #{pjid} and sectionId = #{machineid}")
    String selectbypjidmid(String pjid, String machineid);

    @Select("select * from hc_constructionresults where projectId = #{pjid} and sectionId = #{sectionid} limit 3")
    List<HcConstructionresults> selectpjidmid(String pjid, String sectionid);

    List<String> selecid(String date_begin, String date_end, List<String> querySheBeiList);

    @Update("update hc_constructionresults set ismodify = 1 , projectId = #{projectid} , sectionId = #{sectionid} where id in (${ids}) " )
    void updateSection(@Param("ids")String ids,@Param("projectid") String projectid, @Param("sectionid")String sectionid);
}
