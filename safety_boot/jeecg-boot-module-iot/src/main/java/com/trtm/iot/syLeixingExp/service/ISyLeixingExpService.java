package com.trtm.iot.syLeixingExp.service;

import com.trtm.iot.syLeixingExp.entity.SyLeixingExp;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Description: 试验类型和试验关联表
 * @Author: jeecg-boot
 * @Date: 2022-03-22
 * @Version: V1.0
 */
public interface ISyLeixingExpService extends IService<SyLeixingExp> {
    List getExpList(String typeId);
}
