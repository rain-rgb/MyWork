package com.trtm.iot.zhiliangtaizuocfg.service;

import com.trtm.iot.zhiliangtaizuocfg.entity.ZhiliangTaizuoCfg;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Description: 制梁台座配置表信息
 * @Author: jeecg-boot
 * @Date:   2021-09-13
 * @Version: V1.0
 */
public interface IZhiliangTaizuoCfgService extends IService<ZhiliangTaizuoCfg> {

    ZhiliangTaizuoCfg selectzltaizuone();

    ZhiliangTaizuoCfg selectzltaizuo(String taizuono);

    ZhiliangTaizuoCfg selectzltaizuos(String taizuono);
}
