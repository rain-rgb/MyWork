package com.trtm.iot.byssta.service;

import com.trtm.iot.byssta.entity.BysSta;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Date;

/**
 * @Description: 标养室统计信息表
 * @Author: jeecg-boot
 * @Date:   2022-05-11
 * @Version: V1.0
 */
public interface IBysStaService extends IService<BysSta> {

    BysSta selectlimit(Date datanyr1, String shebeino);
}
