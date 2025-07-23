package com.trtm.iot.wzshouhuodanwei.service;

import com.trtm.iot.wzshouhuodanwei.entity.Wzshouhuodanwei;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Description: 物资收获单位信息表
 * @Author: jeecg-boot
 * @Date:   2022-09-14
 * @Version: V1.0
 */
public interface IWzshouhuodanweiService extends IService<Wzshouhuodanwei> {

    List<Wzshouhuodanwei> getdatalist(String departid);
}
