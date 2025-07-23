package com.trtm.iot.projectinfo.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import com.trtm.iot.projectinfo.entity.ProjectInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

/**
 * @Description: project_info
 * @Author: jeecg-boot
 * @Date:   2022-12-16
 * @Version: V1.0
 */
public interface ProjectInfoMapper extends BaseMapper<ProjectInfo> {

    @Select("select distinct org_category from project_info")
    List<String> selectOrgCategory();

}
