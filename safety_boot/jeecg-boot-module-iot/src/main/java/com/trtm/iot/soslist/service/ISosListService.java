package com.trtm.iot.soslist.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.trtm.iot.soslist.entity.SosList;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: sos_list
 * @Author: jeecg-boot
 * @Date:   2024-12-17
 * @Version: V1.0
 */
public interface ISosListService extends IService<SosList> {

    void saveOrUpdateBySid(JsonNode dataObj);
}
