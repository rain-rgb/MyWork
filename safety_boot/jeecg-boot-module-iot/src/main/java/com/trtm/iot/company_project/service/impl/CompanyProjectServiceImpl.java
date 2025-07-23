package com.trtm.iot.company_project.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.company_contract_management.entity.CompanyContractManagement;
import com.trtm.iot.company_contract_management.service.ICompanyContractManagementService;
import com.trtm.iot.company_invoice.entity.CompanyInvoice;
import com.trtm.iot.company_order_management.entity.CompanyOrderManagement;
import com.trtm.iot.company_order_management.service.ICompanyOrderManagementService;
import com.trtm.iot.company_payment.entity.CompanyPayment;
import com.trtm.iot.company_payment.service.ICompanyPaymentService;
import com.trtm.iot.company_project.entity.CompanyProject;
import com.trtm.iot.company_project.mapper.CompanyProjectMapper;
import com.trtm.iot.company_project.service.ICompanyProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Description: 项目管理
 * @Author: jeecg-boot
 * @Date: 2025-03-10
 * @Version: V1.0
 */
@Service
public class CompanyProjectServiceImpl extends ServiceImpl<CompanyProjectMapper, CompanyProject> implements ICompanyProjectService {

    @Autowired
    private ICompanyOrderManagementService companyOrderManagementService;
    @Autowired
    private ICompanyProjectService companyProjectService;
    @Autowired
    private ICompanyContractManagementService companyContractManagementService;
    @Autowired
    private ICompanyPaymentService companyPaymentService;

    @Override
    public void updateByContract(CompanyContractManagement companyContractManagement, Integer type) {
        //获取项目数据
        String ssxm = companyContractManagement.getSsxm();
        CompanyProject one = getBySsxm(ssxm);
        //1新增，2修改，3删除
        if (type == 1) {
            //项目表合同数加一
            int totalnumber = one.getTotalnumber() == null ? 0 : one.getTotalnumber();
            one.setTotalnumber(totalnumber + 1);
            //项目表合同金额累加
            BigDecimal amount1 = one.getTotalamount() != null ? one.getTotalamount() : BigDecimal.ZERO;
            BigDecimal amount2 = companyContractManagement.getTotalamount() != null ? companyContractManagement.getTotalamount() : BigDecimal.ZERO;
            BigDecimal totalamount = amount1.add(amount2);
            one.setTotalamount(totalamount);
        }
        if (type == 2) {
            //修改项目信息
            BigDecimal totalamount = companyContractManagement.getTotalamount();
            BigDecimal amount1_ = one.getTotalamount() != null ? one.getTotalamount() : BigDecimal.ZERO;
            BigDecimal totalamount_ = amount1_.add(totalamount);
            one.setTotalamount(totalamount_);
        }
        if (type == 3) {
            //修改项目信息
            int totalnumber = one.getTotalnumber() == null ? 0 : one.getTotalnumber();
            one.setTotalnumber(totalnumber - 1);
            BigDecimal amount1 = one.getTotalamount() != null ? one.getTotalamount() : BigDecimal.ZERO;
            BigDecimal amount2 = companyContractManagement.getTotalamount() != null ? companyContractManagement.getTotalamount() : BigDecimal.ZERO;
            BigDecimal totalamount = amount1.subtract(amount2);
            one.setTotalamount(totalamount);
        }
        companyProjectService.updateById(one);
    }

    @Override
    public void updateByOrders(CompanyOrderManagement companyOrderManagement, Integer type) {
        //获取项目
        String ssxm = companyOrderManagement.getSsxm();
        CompanyProject one = getBySsxm(ssxm);
        //1新增，2修改，3删除
        if (type == 1) {
            companyOrderManagement.setProjectname(one.getProjectname());
            //修改项目信息
            int ordernumber = one.getOrdernumber() == null ? 0 : one.getOrdernumber();
            one.setOrdernumber(ordernumber + 1);

            BigDecimal amount1 = one.getOrderamount() != null ? one.getOrderamount() : BigDecimal.ZERO;
            BigDecimal amount2 = companyOrderManagement.getTotalamount() != null ? companyOrderManagement.getTotalamount() : BigDecimal.ZERO;
            BigDecimal orderamount = amount1.add(amount2);
            one.setOrderamount(orderamount);
        }
        if (type == 2) {
            //修改项目信息
            BigDecimal totalamount = companyOrderManagement.getTotalamount();
            BigDecimal amount1_ = one.getTotalamount() != null ? one.getTotalamount() : BigDecimal.ZERO;
            BigDecimal totalamount_ = amount1_.add(totalamount);
            one.setTotalamount(totalamount_);
        }
        if (type == 3) {
            int ordernumber = one.getOrdernumber() == null ? 0 : one.getOrdernumber();
            one.setOrdernumber(ordernumber - 1);
            BigDecimal amount1 = one.getOrderamount() != null ? one.getOrderamount() : BigDecimal.ZERO;
            BigDecimal amount2 = companyOrderManagement.getTotalamount() != null ? companyOrderManagement.getTotalamount() : BigDecimal.ZERO;
            BigDecimal orderamount = amount1.subtract(amount2);
            one.setOrderamount(orderamount);
        }
        companyProjectService.updateById(one);
    }

    @Override
    public void updateByInvoice(CompanyInvoice companyInvoice, Integer type) {
        //获取项目数据
        String ssxm = companyInvoice.getSsxm();
        CompanyProject one = getBySsxm(ssxm);
        BigDecimal amount1 = one.getInvoicamount() != null ? one.getInvoicamount() : BigDecimal.ZERO;
        BigDecimal amount2 = companyInvoice.getAmount() != null ? companyInvoice.getAmount() : BigDecimal.ZERO;
        //1新增，2修改，3删除
        if (type == 1) {
            //项目表开票金额累加,与合同总额计算开票进度
            BigDecimal invoicamount = amount1.add(amount2);
            one.setInvoicamount(invoicamount);
            if (one.getTotalamount() != null) {
                BigDecimal invoicpercentage = invoicamount.divide(one.getTotalamount(), 2, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100));
                one.setInvoicpercentage(invoicpercentage);
            }
        }
        if (type == 2) {
            BigDecimal invoicamount = amount1.add(amount2);
            one.setInvoicamount(invoicamount);
            if (one.getTotalamount() != null) {
                BigDecimal invoicpercentage = invoicamount.divide(one.getTotalamount(), 2, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100));
                one.setInvoicpercentage(invoicpercentage);
            }
        }
        if (type == 3) {
            BigDecimal invoicamount = amount1.subtract(amount2);
            one.setInvoicamount(invoicamount);
            if (one.getTotalamount() != null) {
                BigDecimal invoicpercentage = invoicamount.divide(one.getTotalamount(), 2, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100));
                one.setInvoicpercentage(invoicpercentage);
            }

        }
        companyProjectService.updateById(one);
    }

    @Override
    public void updateByPayment(CompanyPayment companyPayment, Integer type) {
        //获取项目数据
        String ssxm = companyPayment.getSsxm();
        CompanyProject one = getBySsxm(ssxm);
        BigDecimal amount1 = one.getPaymentamount() != null ? one.getPaymentamount() : BigDecimal.ZERO;
        BigDecimal amount2 = companyPayment.getPaymentAmount() != null ? companyPayment.getPaymentAmount() : BigDecimal.ZERO;
        //1新增，2修改，3删除
        if (type == 1) {
            //项目表回款金额累加,与合同总额计算回款进度
            BigDecimal paymentamount = amount1.add(amount2);
            one.setPaymentamount(paymentamount);
            if (one.getTotalamount() != null) {
                BigDecimal paymentpercentage = paymentamount.divide(one.getTotalamount(), 2, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100));
                one.setPaymentpercentage(paymentpercentage);
            }
        }
        if (type == 2) {
            BigDecimal paymentamount = amount1.add(amount2);
            one.setPaymentamount(paymentamount);
            if (one.getTotalamount() != null) {
                BigDecimal paymentpercentage = paymentamount.divide(one.getTotalamount(), 2, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100));
                one.setPaymentpercentage(paymentpercentage);
            }
        }
        if (type == 3) {
            BigDecimal paymentamount = amount1.subtract(amount2);
            one.setPaymentamount(paymentamount);
            if (one.getTotalamount() != null) {
                BigDecimal paymentpercentage = paymentamount.divide(one.getTotalamount(), 2, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100));
                one.setPaymentpercentage(paymentpercentage);
            }
        }
        companyProjectService.updateById(one);
        if (companyPayment.getStatus() > 0){
            updatePaymentPercentage(one);
        }

    }

    private void updatePaymentPercentage(CompanyProject companyProject) {

        BigDecimal paymentpercentage = companyProject.getPaymentpercentage();
        //计算进度状态
        //要求日期
        Date deliverytime = companyProject.getDeliverytime();
        //签订日期
        Date signedtime = companyProject.getSignedtime();
        // 当前日期
        Date currentDate = new Date();
        // 计算总工期（毫秒）
        long totalDuration = deliverytime.getTime() - signedtime.getTime();
        // 计算已用工期（毫秒）
        long elapsedDuration = currentDate.getTime() - signedtime.getTime();

        // 计算预期进度
        BigDecimal expectedProgress = new BigDecimal(elapsedDuration)
                .divide(new BigDecimal(totalDuration), 2, BigDecimal.ROUND_HALF_UP)
                .multiply(new BigDecimal(100));

        // 比较实际进度与预期进度
        int comparison = paymentpercentage.compareTo(expectedProgress);
        Integer paymentstate = 0;
        if (comparison > 0) {
            paymentstate = 1;
        } else if (comparison < 0) {
            paymentstate = 3;
        } else {
            paymentstate = 2;
        }
        companyProject.setPaymentstate(paymentstate);
        companyProjectService.updateById(companyProject);
    }

    @Override
    public CompanyProject getBySsxm(String ssxm) {
        LambdaQueryWrapper<CompanyProject> companyProjectLambdaQueryWrapper = new LambdaQueryWrapper<>();
        companyProjectLambdaQueryWrapper.eq(CompanyProject::getId, ssxm);
        return companyProjectService.getOne(companyProjectLambdaQueryWrapper);
    }

    @Override
    public void updateJindu(String ssxm) {
        //获取项目下合同的实施进度
        QueryWrapper<CompanyContractManagement> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("avg(schedule) as schedule")
                .eq("ssxm", ssxm);
        CompanyContractManagement companyContractManagement = companyContractManagementService.getOne(queryWrapper);
        //执行进度
        BigDecimal schedule = companyContractManagement.getSchedule();

        //不为空则修改项目进度
        if (schedule != null) {
            CompanyProject one = companyProjectService.getBySsxm(ssxm);
            one.setProgresspercentage(schedule);
            //计算进度状态
            //要求日期
            Date deliverytime = one.getDeliverytime();
            //签订日期
            Date signedtime = one.getSignedtime();
            // 当前日期
            Date currentDate = new Date();
            // 计算总工期（毫秒）
            long totalDuration = deliverytime.getTime() - signedtime.getTime();
            // 计算已用工期（毫秒）
            long elapsedDuration = currentDate.getTime() - signedtime.getTime();

            // 计算预期进度
            BigDecimal expectedProgress = new BigDecimal(elapsedDuration)
                    .divide(new BigDecimal(totalDuration), 2, BigDecimal.ROUND_HALF_UP)
                    .multiply(new BigDecimal(100));

            // 比较实际进度与预期进度
            int comparison = schedule.compareTo(expectedProgress);
            Integer progressstate = 0;
            if (comparison > 0) {
                progressstate = 1;
            } else if (comparison < 0) {
                progressstate = 3;
            } else {
                progressstate = 2;
            }
            one.setProgressstate(progressstate);
            companyProjectService.updateById(one);
        }
    }
}
