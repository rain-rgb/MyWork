package com.trtm.iot.lmjob.mapper;

import java.util.List;
import java.util.Map;

import com.trtm.iot.lmjob.entity.LmUploadFiles;
import org.apache.ibatis.annotations.Param;
import com.trtm.iot.lmjob.entity.LmJobInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

/**
 * @Description: lm_job_info
 * @Author: jeecg-boot
 * @Date: 2023-08-31
 * @Version: V1.0
 */
public interface LmJobInfoMapper extends BaseMapper<LmJobInfo> {

    @Select("select * from upload_file")
    List<LmUploadFiles> sselectUploadFileList();

    @Select("select sgjd,SUM(filecount) as fileCount from lm_lq_files where sys_org_code like concat(#{sysOrgCode},'%') group by sgjd")
    List<Map<String, String>> selectLmlqFile(String sysOrgCode);

    @Select("select sgjd,SUM(filecount) as fileCount from lm_sw_files where sys_org_code like concat(#{sysOrgCode},'%') group by sgjd")
    List<Map<String, String>> selectLmswFile(String sysOrgCode);
}
