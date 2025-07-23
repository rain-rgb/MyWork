package com.trtm.iot.frontDeviceWeilan.service;

import com.trtm.iot.frontDeviceWeilan.entity.FrontDeviceWeilan;
import com.baomidou.mybatisplus.extension.service.IService;
import org.w3c.dom.ls.LSInput;

import java.util.List;

/**
 * @Description: 运输车电子围栏数据
 * @Author: jeecg-boot
 * @Date:   2021-06-28
 * @Version: V1.0
 */
public interface IFrontDeviceWeilanService extends IService<FrontDeviceWeilan> {

    List<FrontDeviceWeilan> querylist(Integer status,Integer isdel,Integer delstatus);
}
