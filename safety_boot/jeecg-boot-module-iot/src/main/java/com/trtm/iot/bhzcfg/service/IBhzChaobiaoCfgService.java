package com.trtm.iot.bhzcfg.service;

import com.trtm.iot.bhzcfg.entity.BhzChaobiaoCfg;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Description: 拌合站超标配置子表
 * @Author: jeecg-boot
 * @Date:   2021-03-17
 * @Version: V1.0
 */
public interface IBhzChaobiaoCfgService extends IService<BhzChaobiaoCfg> {

	public List<BhzChaobiaoCfg> selectByMainIds(String mainId);

    /**
     * 根据拌合机设备去查询超标标准
     * @param bhjno
     * @return
     */
    List<BhzChaobiaoCfg> selectchaobiaolist(String bhjno,String uid);
}
