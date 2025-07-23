package com.trtm.iot.ydwbs.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.trtm.iot.ydwbs.entity.Ydwbs;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Description: ydwbs
 * @Author: jeecg-boot
 * @Date:   2021-09-14
 * @Version: V1.0
 */
public interface IYdwbsService extends IService<Ydwbs> {

    List<Ydwbs> getList(Integer status);

}
