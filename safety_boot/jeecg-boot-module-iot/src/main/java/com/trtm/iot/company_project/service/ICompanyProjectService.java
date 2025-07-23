package com.trtm.iot.company_project.service;

import com.trtm.iot.company_contract_management.entity.CompanyContractManagement;
import com.trtm.iot.company_invoice.entity.CompanyInvoice;
import com.trtm.iot.company_order_management.entity.CompanyOrderManagement;
import com.trtm.iot.company_payment.entity.CompanyPayment;
import com.trtm.iot.company_project.entity.CompanyProject;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 项目管理
 * @Author: jeecg-boot
 * @Date:   2025-03-10
 * @Version: V1.0
 */
public interface ICompanyProjectService extends IService<CompanyProject> {

    /**
     * 操作合同时更新项目表信息
     * @param companyContractManagement 合同信息
     * @param type 1：添加 2：修改 3：删除
     */
    void updateByContract(CompanyContractManagement companyContractManagement,Integer type);

    /**
     * 操作订单时更新项目表信息
     * @param companyOrderManagement 订单信息
     * @param type 1：添加 2：修改 3：删除
     */
    void updateByOrders(CompanyOrderManagement companyOrderManagement, Integer type);

    /**
     * 根据所属项目查询项目信息
     * @param ssxm
     * @return
     */
    CompanyProject getBySsxm(String ssxm);

    /**
     * 修改项目进度
     * @param ssxm
     */
    void updateJindu(String ssxm);

    /**
     *  操作发票时更新项目表信息
     * @param companyInvoice 发票信息
     * @param type 1：添加 2：修改 3：删除
     */
    void updateByInvoice(CompanyInvoice companyInvoice, Integer type);

    /**
     * 操作付款时更新项目表信息
     * @param companyPayment 付款信息
     * @param type 1：添加 2：修改 3：删除
     */
    void updateByPayment(CompanyPayment companyPayment, Integer type);
}
