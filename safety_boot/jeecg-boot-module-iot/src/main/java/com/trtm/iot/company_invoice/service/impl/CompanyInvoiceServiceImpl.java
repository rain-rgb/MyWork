package com.trtm.iot.company_invoice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.trtm.iot.company_invoice.entity.CompanyInvoice;
import com.trtm.iot.company_invoice.mapper.CompanyInvoiceMapper;
import com.trtm.iot.company_invoice.service.ICompanyInvoiceService;
import com.trtm.iot.company_payment.entity.CompanyPayment;
import com.trtm.iot.company_payment.mapper.CompanyPaymentMapper;
import com.trtm.iot.company_project.service.ICompanyProjectService;
import org.jeecg.common.util.DateUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.system.vo.LoginUser;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.Date;

/**
 * @Description: 开票管理
 * @Author: jeecg-boot
 * @Date:   2025-04-02
 * @Version: V1.0
 */
@Service
public class CompanyInvoiceServiceImpl extends ServiceImpl<CompanyInvoiceMapper, CompanyInvoice> implements ICompanyInvoiceService {
    @Autowired
    private CompanyInvoiceMapper companyInvoiceMapper;
    @Autowired
    private CompanyPaymentMapper companyPaymentMapper;
    @Autowired
    private ICompanyProjectService companyProjectService;
    @Override
    public void saveAndCopyToPayment(CompanyInvoice companyInvoice) {

        //获取当前用户的信息
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        companyInvoice.setCreatedBy(loginUser.getRealname());
        companyInvoice.setCreatedAt(new Date());
        companyInvoice.setInvoiceNumber(DateUtils.codeFormat("INV-"));
        companyInvoiceMapper.insert(companyInvoice);

        CompanyPayment companyPayment = new CompanyPayment();
        BeanUtils.copyProperties(companyInvoice, companyPayment);
        companyPayment.setStatus(0);
        companyPayment.setPaymentNumber(DateUtils.codeFormat("PAY-"));
        companyPaymentMapper.insert(companyPayment);
        if (companyInvoice.getStatus() == 1) {
            companyProjectService.updateByInvoice(companyInvoice, 1);
        }
    }

    @Override
    public void updateMain(CompanyInvoice companyInvoice) {
        companyInvoiceMapper.updateById(companyInvoice);
        String invoiceNumber = companyInvoice.getInvoiceNumber();
        LambdaQueryWrapper<CompanyPayment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CompanyPayment::getInvoiceNumber, invoiceNumber);
        CompanyPayment one = companyPaymentMapper.selectOne(queryWrapper);
        if (one != null) {
            CompanyPayment companyPayment = new CompanyPayment();
            BeanUtils.copyProperties(companyInvoice, companyPayment);
            companyPayment.setId(one.getId());
            companyPaymentMapper.updateById(companyPayment);
        }
    }
}
