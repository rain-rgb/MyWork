package com.trtm.iot.company_order_management.service;

import com.trtm.iot.company_order_management.entity.CompanyOrderManagementS;
import com.trtm.iot.company_order_management.entity.CompanyOrderManagement;
import com.baomidou.mybatisplus.extension.service.IService;
import com.trtm.iot.company_order_management.vo.CompanyOrderManagementPage;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @Description: 订单管理
 * @Author: jeecg-boot
 * @Date:   2024-07-01
 * @Version: V1.0
 */
public interface ICompanyOrderManagementService extends IService<CompanyOrderManagement> {

	/**
	 * 添加一对多
	 *
	 */
	public void saveMain(CompanyOrderManagement companyOrderManagement,List<CompanyOrderManagementS> companyOrderManagementSList) ;

	/**
	 * 修改一对多
	 *
	 */
	public void updateMain(CompanyOrderManagement companyOrderManagement,List<CompanyOrderManagementS> companyOrderManagementSList);

	/**
	 * 删除一对多
	 */
	public void delMain (String id);

	/**
	 * 批量删除一对多
	 */
	public void delBatchMain (Collection<? extends Serializable> idList);

	/**
	 * 根据合同编号查询订单
	 * @param contractnumber 合同编号
	 * @return
	 */
	List<CompanyOrderManagementPage> getByContractnumber(String contractnumber);

	/**
	 * 施工记录
	 * @param companyOrderManagementPage 订单信息包含施工记录
	 */
	void editsg(CompanyOrderManagementPage companyOrderManagementPage);
}
