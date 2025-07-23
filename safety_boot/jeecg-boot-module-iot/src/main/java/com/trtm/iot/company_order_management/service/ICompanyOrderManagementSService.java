package com.trtm.iot.company_order_management.service;

import com.trtm.iot.company_order_management.entity.CompanyOrderManagementS;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Description: 订单管理子表
 * @Author: jeecg-boot
 * @Date:   2024-07-01
 * @Version: V1.0
 */
public interface ICompanyOrderManagementSService extends IService<CompanyOrderManagementS> {

	public List<CompanyOrderManagementS> selectByMainId(String mainId);

    void updateByOrdernumber(String ordernumberOld, String s1);
}
