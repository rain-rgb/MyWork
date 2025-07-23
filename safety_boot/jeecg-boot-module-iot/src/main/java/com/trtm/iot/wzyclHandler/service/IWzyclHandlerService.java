package com.trtm.iot.wzyclHandler.service;

import com.trtm.iot.wzyclHandler.entity.WzyclHandler;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: wzycl_handler
 * @Author: jeecg-boot
 * @Date:   2022-11-21
 * @Version: V1.0
 */
public interface IWzyclHandlerService extends IService<WzyclHandler> {

    int handlerInfoAddOrUpdate(String baseid, String wtyy, String czfs, String czjg, String czperson, String czfile);

    int superviseInfoAddOrUpdate(String baseid, String spyj, String spjg, String spperson, String spfile);
}
