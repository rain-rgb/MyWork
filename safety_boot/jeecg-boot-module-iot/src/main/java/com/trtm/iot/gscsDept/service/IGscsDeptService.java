package com.trtm.iot.gscsDept.service;

import com.trtm.iot.gscsDept.entity.GscsDept;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 班组安全管控系统班组表
 * @Author: jeecg-boot
 * @Date: 2022-01-25
 * @Version: V1.0
 */
public interface IGscsDeptService extends IService<GscsDept> {
    String selectDeptName(String deptId);
}
