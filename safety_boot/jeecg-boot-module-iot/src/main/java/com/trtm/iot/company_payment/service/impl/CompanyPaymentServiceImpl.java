package com.trtm.iot.company_payment.service.impl;

import com.trtm.iot.company_payment.entity.CompanyPayment;
import com.trtm.iot.company_payment.mapper.CompanyPaymentMapper;
import com.trtm.iot.company_payment.service.ICompanyPaymentService;
import com.trtm.iot.company_project.entity.CompanyProject;
import com.trtm.iot.company_project.service.ICompanyProjectService;
import org.jeecg.common.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 回款管理
 * @Author: jeecg-boot
 * @Date:   2025-04-02
 * @Version: V1.0
 */
@Service
public class CompanyPaymentServiceImpl extends ServiceImpl<CompanyPaymentMapper, CompanyPayment> implements ICompanyPaymentService {
    @Autowired
    private ICompanyProjectService companyProjectService;
    @Override
    public void saveMian(CompanyPayment companyPayment) {
        companyPayment.setPaymentNumber(DateUtils.codeFormat("PAY-"));
        this.save(companyPayment);
        if (companyPayment.getStatus() > 0 && companyPayment.getStatus() != 4){
            companyProjectService.updateByPayment(companyPayment,1);
        }
    }
}
