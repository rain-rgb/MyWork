package com.trtm.iot.company_invoice.service;

import com.trtm.iot.company_invoice.entity.CompanyInvoice;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 开票管理
 * @Author: jeecg-boot
 * @Date:   2025-04-02
 * @Version: V1.0
 */
public interface ICompanyInvoiceService extends IService<CompanyInvoice> {

    /**
     * 保存开票管理并复制到回款管理
     * @param companyInvoice 开票信息
     */
    void saveAndCopyToPayment(CompanyInvoice companyInvoice);

    /**
     * 修改开票信息并更改回款信息
     * @param companyInvoice 开票信息
     */
    void updateMain(CompanyInvoice companyInvoice);
}
