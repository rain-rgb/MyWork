package com.trtm.iot.openapigpsdatavo.service;

import com.trtm.iot.openapigpsdatavo.entity.Openapigpsdatavo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Description: 摊铺碾压数据
 * @Author: jeecg-boot
 * @Date:   2023-04-21
 * @Version: V1.0
 */
public interface IOpenapigpsdatavoService extends IService<Openapigpsdatavo> {

    List<Openapigpsdatavo> getList(String shebeilist, Integer curid);

    List<Openapigpsdatavo> getBLList(String shebeilist, Integer curid);

    List<Openapigpsdatavo> getListjt(String shebeilist, Integer curid);
}
