package com.trtm.iot.wzyclpidaichen.service;

import com.trtm.iot.wzyclpidaichen.entity.Wzyclpidaichen;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Description: wzyclpidaichen
 * @Author: jeecg-boot
 * @Date:   2024-04-18
 * @Version: V1.0
 */
public interface IWzyclpidaichenService extends IService<Wzyclpidaichen> {

    List<Wzyclpidaichen> slistrqid(String shebeilist, Integer curid);
}
