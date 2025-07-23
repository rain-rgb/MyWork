package com.trtm.iot.bhzyuanliaoStatistic.service;

import com.trtm.iot.bhzyuanliaoStatistic.entity.BhzYuanliaoStatistic;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 拌合站原材料统计
 * @Author: jeecg-boot
 * @Date:   2022-10-10
 * @Version: V1.0
 */
public interface IBhzYuanliaoStatisticService extends IService<BhzYuanliaoStatistic> {

    BhzYuanliaoStatistic selectByShebei(String shebeiNo, String materialeName);

}
