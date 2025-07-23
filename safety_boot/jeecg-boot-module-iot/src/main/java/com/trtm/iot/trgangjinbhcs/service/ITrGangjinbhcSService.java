package com.trtm.iot.trgangjinbhcs.service;

import com.trtm.iot.trgangjinbhcs.entity.TrGangjinbhcS;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Description: 钢保检测子表
 * @Author: jeecg-boot
 * @Date:   2021-09-10
 * @Version: V1.0
 */
public interface ITrGangjinbhcSService extends IService<TrGangjinbhcS> {

    List<TrGangjinbhcS> selectGangjinSList(String testid);

}
