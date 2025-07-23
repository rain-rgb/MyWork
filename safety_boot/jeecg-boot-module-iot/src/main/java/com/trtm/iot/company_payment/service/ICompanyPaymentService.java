package com.trtm.iot.company_payment.service;

import com.trtm.iot.company_payment.entity.CompanyPayment;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 回款管理
 * @Author: jeecg-boot
 * @Date:   2025-04-02
 * @Version: V1.0
 */
public interface ICompanyPaymentService extends IService<CompanyPayment> {

    void saveMian(CompanyPayment companyPayment);
}
