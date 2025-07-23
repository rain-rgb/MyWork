package com.trtm.iot.wbshebeidetail.service;

import com.trtm.iot.wbshebeidetail.entity.WbshebeiDetail;
import com.baomidou.mybatisplus.extension.service.IService;


/**
 * @Description: 电子锁详情数据表
 * @Author: jeecg-boot
 * @Date:   2022-02-22
 * @Version: V1.0
 */
public interface IWbshebeiDetailService extends IService<WbshebeiDetail> {

    String getDepartName(String userdepartid);
}
