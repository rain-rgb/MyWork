package com.trtm.iot.gscsInform.service;

import com.trtm.iot.gscsInform.entity.GscsInform;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Description: 班组安全管控系统通知详情表
 * @Author: jeecg-boot
 * @Date:   2022-01-25
 * @Version: V1.0
 */
public interface IGscsInformService extends IService<GscsInform> {
    //获取考试次数
    Integer getTestCount();
    //
    String getIdByTitle(String title);
    //
    Integer selectCount(String title);
}
