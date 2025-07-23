package com.trtm.iot.hc_tfysworkarea.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;
import com.trtm.iot.hc_tfysworkarea.entity.HcTfysworkarea;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

/**
 * @Description: 土方工作区施工成果
 * @Author: jeecg-boot
 * @Date:   2023-10-10
 * @Version: V1.0
 */
public interface HcTfysworkareaMapper extends BaseMapper<HcTfysworkarea> {

    @Select("select sectionName from hc_section where sectionId = #{sectionid}")
    String getSectionName(String sectionid);

    List<HcTfysworkarea> listbytj();

    @Select("select sum( workmile )workmile,sum( workarea ) workarea,sum( timesavg ) timesavg,sum( thickavg ) thickavg,startstation,endstation,projectid,sectionid from hc_tfysworkarea where starttime > #{s1} and starttime < #{s16} group by projectid,sectionid")
    List<HcTfysworkarea> listbytjs(String s1, String s16);
}
