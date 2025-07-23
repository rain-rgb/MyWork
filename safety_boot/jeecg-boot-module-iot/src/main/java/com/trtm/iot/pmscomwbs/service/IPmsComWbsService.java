package com.trtm.iot.pmscomwbs.service;

import com.trtm.iot.pmscomwbs.entity.PmsComWbs;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Description: 中交象山疏港高速(原海德 wbs数据)
 * @Author: jeecg-boot
 * @Date:   2021-07-16
 * @Version: V1.0
 */
public interface IPmsComWbsService extends IService<PmsComWbs> {
    /**
     * 根据条件插叙
     * @param id
     * @param status
     * @return
     */
    List<PmsComWbs> queryselectlist(Integer id,Integer status);

    /**
     * 根据id修改状态
     * @param id
     * @param status
     * @return
     */
    int updateids(Integer id,Integer status);


}
