package com.trtm.iot.gscsSectionDept.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.trtm.iot.gscsSectionDept.entity.GscsSectionDept;

import java.util.List;

/**
 * @Description: 班组安全管控系统标段与班组关联表
 * @Author: jeecg-boot
 * @Date:   2022-01-25
 * @Version: V1.0
 */
public interface IGscsSectionDeptService extends IService<GscsSectionDept> {
    List<GscsSectionDept> getDeptList(String section);
}
