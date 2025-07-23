package com.trtm.sy.tableform.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.trtm.sy.sylxdps.entity.SyDpsJcTestitemtype;
import com.trtm.sy.tableform.model.TableForm;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface TableFormMapper extends BaseMapper<TableForm> {

    List<SyDpsJcTestitemtype> selectParentItemType(List<String> titCodes);

    List<SyDpsJcTestitemtype> selectSonsItemType(String titcode);

    @Select("select ti_nos from sy_dps_yy_userastestitemtype where role_id = #{roleId} and titCode = #{titcode}")
    String getExperimentChridList(String roleId, String titcode);

    @Select("select * from sy_dps_jc_testitem where tiNo = #{tino} ")
    TableForm selectTableForm(String tino);
}
