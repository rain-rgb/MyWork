package com.trtm.iot.lmjob.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.trtm.iot.lmjob.entity.LmJobInfo;
import com.trtm.iot.lmjob.entity.LmUploadFiles;
import com.trtm.iot.lmjob.mapper.LmJobInfoMapper;
import com.trtm.iot.lmjob.service.ILmJobInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.security.Key;
import java.util.*;

/**
 * @Description: lm_job_info
 * @Author: jeecg-boot
 * @Date: 2023-08-31
 * @Version: V1.0
 */
@Service
public class LmJobInfoServiceImpl extends ServiceImpl<LmJobInfoMapper, LmJobInfo> implements ILmJobInfoService {

    @Autowired
    private LmJobInfoMapper lmJobInfoMapper;

    @Override
    public List<LmUploadFiles> sselectUploadFileList() {
        return lmJobInfoMapper.sselectUploadFileList();
    }

    @Override
    public List<Map<String, String>> selectLmFile(String sysOrgCode) {
        List<Map<String, String>> listMap1 = lmJobInfoMapper.selectLmlqFile(sysOrgCode);
        List<Map<String, String>> listMap2 = lmJobInfoMapper.selectLmswFile(sysOrgCode);
        List<Map<String, String>> maps = new ArrayList<>();
        for (Map<String, String> map1 : listMap1) {
            Map<String, String> map = new LinkedHashMap<>();
            BeanUtil.copyProperties(map1,map);
            maps.add(map);
        }
        for (Map<String, String> map2 : listMap2) {
            Map<String, String> map = new LinkedHashMap<>();
            BeanUtil.copyProperties(map2,map);
            maps.add(map);
        }
        return maps;
    }
}
