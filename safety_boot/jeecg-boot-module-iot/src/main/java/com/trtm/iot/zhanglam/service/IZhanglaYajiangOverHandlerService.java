package com.trtm.iot.zhanglam.service;

import com.trtm.iot.zhanglam.entity.ZhanglaYajiangOverHandler;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Description: zhangla_yajiang_over_handler
 * @Author: jeecg-boot
 * @Date:   2021-12-27
 * @Version: V1.0
 */
public interface IZhanglaYajiangOverHandlerService extends IService<ZhanglaYajiangOverHandler> {

    ZhanglaYajiangOverHandler selectoneMessage(String baseid);

    List<ZhanglaYajiangOverHandler> listToday(String shebeilist);

    Integer BhzCZAddOrUpDate(String wtyy, String clfs, String cljg, String baseid, String bizPath, String people, int i, String holeid);

    Integer supervisorBohui(String jlbh, String baseid, String people, int i, String holeid);

    Integer supervisorAddOrUpdate(String spyj, String spjg, String baseid, String bizPath, String people, int i, String holeid);

    Integer headquartersAddOrUpdate(String zhbyj, String zhbjg, String baseid, String bizPath, String people, int i, String holeid);

    Integer headquartersBohui(String zhbbh, String baseid, String people, int i, String holeid);
}
