package com.trtm.iot.liangGongxuPeople.service;

import com.trtm.iot.liangGongxuPeople.entity.LiangGongxuPeople;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Description: 自动工序负责人配置
 * @Author: jeecg-boot
 * @Date:   2022-11-16
 * @Version: V1.0
 */
public interface ILiangGongxuPeopleService extends IService<LiangGongxuPeople> {

    LiangGongxuPeople selectResponsible(String sysOrgCode, Integer xuhao);
}
