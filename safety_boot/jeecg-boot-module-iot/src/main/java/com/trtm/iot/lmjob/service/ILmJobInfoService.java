package com.trtm.iot.lmjob.service;

import com.trtm.iot.lmjob.entity.LmJobInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.trtm.iot.lmjob.entity.LmUploadFiles;

import java.util.List;
import java.util.Map;

/**
 * @Description: lm_job_info
 * @Author: jeecg-boot
 * @Date:   2023-08-31
 * @Version: V1.0
 */
public interface ILmJobInfoService extends IService<LmJobInfo> {

    List<LmUploadFiles> sselectUploadFileList();

    List<Map<String, String>> selectLmFile(String sysOrgCode);
}
