package com.trtm.iot.company_order_management.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.trtm.iot.company_order_management.entity.CompanyOrderManagementS;
import com.trtm.iot.company_order_management.mapper.CompanyOrderManagementSMapper;
import com.trtm.iot.company_order_management.service.ICompanyOrderManagementSService;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 订单管理子表
 * @Author: jeecg-boot
 * @Date:   2024-07-01
 * @Version: V1.0
 */
@Service
public class CompanyOrderManagementSServiceImpl extends ServiceImpl<CompanyOrderManagementSMapper, CompanyOrderManagementS> implements ICompanyOrderManagementSService {
	
	@Override
	public List<CompanyOrderManagementS> selectByMainId(String mainId) {
		return this.selectByMainId(mainId);
	}

	@Override
	public void updateByOrdernumber(String ordernumberOld, String s1) {
		LambdaQueryWrapper<CompanyOrderManagementS> lambdaQueryWrapper = new LambdaQueryWrapper<>();
		lambdaQueryWrapper.eq(CompanyOrderManagementS::getOrdernumber, ordernumberOld);
		List<CompanyOrderManagementS> companyOrderManagementS = this.list(lambdaQueryWrapper);
		for (CompanyOrderManagementS companyOrderManagement : companyOrderManagementS) {
			companyOrderManagement.setOrdernumber(s1);
			this.updateById(companyOrderManagement);
		}
	}
}
