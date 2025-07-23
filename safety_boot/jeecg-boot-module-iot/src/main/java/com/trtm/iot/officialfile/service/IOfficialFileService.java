package com.trtm.iot.officialfile.service;

import com.trtm.iot.officialfile.entity.OfficialFile;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * @Description: 公文信息表
 * @Author: jeecg-boot
 * @Date:   2021-11-29
 * @Version: V1.0
 */
public interface IOfficialFileService extends IService<OfficialFile> {
    List<Map> events();
    List<Map> archive();
}
