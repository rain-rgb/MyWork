package com.trtm.iot.company_contract_management.service;

import com.trtm.iot.company_contract_management.entity.CompanyContractManagement;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 合同管理
 * @Author: jeecg-boot
 * @Date:   2024-07-10
 * @Version: V1.0
 */
public interface ICompanyContractManagementService extends IService<CompanyContractManagement> {
    /**
     * 添加合同
     * @param companyContractManagement 合同信息
     */
    void add(CompanyContractManagement companyContractManagement);

    /**
     * 根据所属项目获取合同信息
     * @param ssxm 所属项目id
     * @return
     */
    CompanyContractManagement getBySsxm(String ssxm);

    /**
     * 根据合同号获取合同信息
     * @param hth 合同号
     * @return
     */
    CompanyContractManagement getByHth(String hth);
}
