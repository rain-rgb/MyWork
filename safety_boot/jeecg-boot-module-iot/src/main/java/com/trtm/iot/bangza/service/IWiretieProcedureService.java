package com.trtm.iot.bangza.service;

import com.trtm.iot.bangza.entity.WiretieProcedure;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 钢筋绑扎工序表信息
 * @Author: jeecg-boot
 * @Date:   2021-08-18
 * @Version: V1.0
 */
public interface IWiretieProcedureService extends IService<WiretieProcedure> {

    WiretieProcedure selectones(String uuid);
}
