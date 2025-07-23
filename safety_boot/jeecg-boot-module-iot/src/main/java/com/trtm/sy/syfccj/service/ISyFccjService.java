package com.trtm.sy.syfccj.service;

import com.trtm.sy.syfccj.entity.SyFccj;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 发车抽检
 * @Author: jeecg-boot
 * @Date:   2022-09-20
 * @Version: V1.0
 */
public interface ISyFccjService extends IService<SyFccj> {

    public SyFccj findByFcid(String fcid,String post);
}
