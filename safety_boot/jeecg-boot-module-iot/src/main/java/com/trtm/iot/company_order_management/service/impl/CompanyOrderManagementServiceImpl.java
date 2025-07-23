package com.trtm.iot.company_order_management.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.trtm.iot.car.service.ISchedulingCarService;
import com.trtm.iot.company_contract_management.entity.CompanyContractManagement;
import com.trtm.iot.company_contract_management.service.ICompanyContractManagementService;
import com.trtm.iot.company_customer.entity.CompanyCustomer;
import com.trtm.iot.company_customer.service.ICompanyCustomerService;
import com.trtm.iot.company_order_management.entity.CompanyOrderManagement;
import com.trtm.iot.company_order_management.entity.CompanyOrderManagementS;
import com.trtm.iot.company_order_management.mapper.CompanyOrderManagementSMapper;
import com.trtm.iot.company_order_management.mapper.CompanyOrderManagementMapper;
import com.trtm.iot.company_order_management.service.ICompanyOrderManagementSService;
import com.trtm.iot.company_order_management.service.ICompanyOrderManagementService;
import com.trtm.iot.company_order_management.vo.CompanyOrderManagementPage;
import com.trtm.iot.company_project.entity.CompanyProject;
import com.trtm.iot.company_project.service.ICompanyProjectService;
import com.trtm.sy.sydpssysample.entity.SysDepart;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.system.vo.LoginUser;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Collection;
import java.util.UUID;


/**
 * @Description: 订单管理
 * @Author: jeecg-boot
 * @Date: 2024-07-01
 * @Version: V1.0
 */
@Service
public class CompanyOrderManagementServiceImpl extends ServiceImpl<CompanyOrderManagementMapper, CompanyOrderManagement> implements ICompanyOrderManagementService {

    @Autowired
    private CompanyOrderManagementMapper companyOrderManagementMapper;
    @Autowired
    private CompanyOrderManagementSMapper companyOrderManagementSMapper;
    @Autowired
    private ICompanyOrderManagementService companyOrderManagementService;
    @Autowired
    private ICompanyOrderManagementSService companyOrderManagementSService;
    @Autowired
    private ICompanyProjectService companyProjectService;
    @Autowired
    private ICompanyContractManagementService companyContractManagementService;

    @Override
    @Transactional
    public void saveMain(CompanyOrderManagement companyOrderManagement, List<CompanyOrderManagementS> companyOrderManagementSList) {
        //获取随机guid
        String s = UUID.randomUUID().toString();
        companyOrderManagement.setGuid(s);
        // 获取当前日期
        LocalDate date = LocalDate.now();

        // 创建一个 DateTimeFormatter 对象用于格式化日期
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        // 将日期格式化为字符串
        String formattedDate = formatter.format(date);

        //生成订单编号
        String s1 = companyOrderManagement.getOrdernumber() + "-" + formattedDate;
        //查询时候有包含s1的订单号 s1 = "DD-" + date.getTime() + "数量 +1";
        LambdaQueryWrapper<CompanyOrderManagement> queryWrapper = new LambdaQueryWrapper<CompanyOrderManagement>();
        queryWrapper.like(CompanyOrderManagement::getOrdernumber, s1);
        List<CompanyOrderManagement> list = companyOrderManagementService.list(queryWrapper);
        int size = list.size();
        int sizePlusOne = size + 1;
        String result = String.format("%03d", sizePlusOne);

        s1 = companyOrderManagement.getOrdernumber() + "-" + formattedDate + "-" + result;
        companyOrderManagement.setOrdernumber(s1);
        //获取当前日期
        LocalDateTime localDateTime = date.atTime(LocalDateTime.now().toLocalTime());
        Date Creattime = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
        companyOrderManagement.setCreattime(Creattime);

        //调用项目接口，增加订单数，累加订单金额
        companyProjectService.updateByOrders(companyOrderManagement, 1);

        //获取当前用户的信息
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        String username = loginUser.getUsername();
        companyOrderManagement.setCreatperson(username);

        companyOrderManagementMapper.insert(companyOrderManagement);

        if (companyOrderManagementSList != null && companyOrderManagementSList.size() > 0) {
            for (CompanyOrderManagementS entity : companyOrderManagementSList) {
                //外键设置
                entity.setOrdernumber(companyOrderManagement.getOrdernumber());
                entity.setCreattime(Creattime);
                entity.setOrdernumber(s1);
                companyOrderManagementSMapper.insert(entity);
            }
        }
    }

    @Override
    @Transactional
    public void updateMain(CompanyOrderManagement companyOrderManagement, List<CompanyOrderManagementS> companyOrderManagementSList) {
        //获取旧订单信息
        CompanyOrderManagement companyOrderManagementOld = companyOrderManagementService.getById(companyOrderManagement.getId());
        String ordernumberNew = companyOrderManagement.getOrdernumber();
        String ordernumberOld = companyOrderManagementOld.getOrdernumber();
        if (!ordernumberOld.contains(ordernumberNew)){
            // 获取当前日期
            LocalDate date = LocalDate.now();

            // 创建一个 DateTimeFormatter 对象用于格式化日期
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
            // 将日期格式化为字符串
            String formattedDate = formatter.format(date);

            //生成订单编号
            String s1 = companyOrderManagement.getOrdernumber() + "-" + formattedDate;
            //查询时候有包含s1的订单号 s1 = "DD-" + date.getTime() + "数量 +1";
            LambdaQueryWrapper<CompanyOrderManagement> queryWrapper = new LambdaQueryWrapper<CompanyOrderManagement>();
            queryWrapper.like(CompanyOrderManagement::getOrdernumber, s1);
            List<CompanyOrderManagement> list = companyOrderManagementService.list(queryWrapper);
            int size = list.size();
            int sizePlusOne = size + 1;
            String result = String.format("%03d", sizePlusOne);

            s1 = companyOrderManagement.getOrdernumber() + "-" + formattedDate + "-" + result;
            companyOrderManagement.setOrdernumber(s1);

            companyOrderManagementSService.updateByOrdernumber(ordernumberOld, s1);
        }
        //修改订单信息
        companyOrderManagementMapper.updateById(companyOrderManagement);

//        //1.先删除子表数据
//        companyOrderManagementSMapper.deleteByMainId(companyOrderManagement.getOrdernumber());

        //2.子表数据重新插入
        if (companyOrderManagementSList != null && companyOrderManagementSList.size() > 0) {
            for (CompanyOrderManagementS entity : companyOrderManagementSList) {
                //外键设置
                entity.setOrdernumber(companyOrderManagement.getOrdernumber());
                companyOrderManagementSService.saveOrUpdate(entity);
            }
        }
        //订单金额
        BigDecimal totalamountOld = companyOrderManagementOld.getTotalamount();
        BigDecimal totalamountNew = companyOrderManagement.getTotalamount();
        BigDecimal amount1 = totalamountOld != null ? totalamountOld : BigDecimal.ZERO;
        BigDecimal amount2 = totalamountNew != null ? totalamountNew : BigDecimal.ZERO;
        //判断如果不相同，用totalamountNew-totalamountOld，修改项目信息
        if (amount1.compareTo(amount2) != 0) {
            BigDecimal totalamount = amount2.subtract(amount1);
            companyOrderManagement.setTotalamount(totalamount);
            companyProjectService.updateByOrders(companyOrderManagement, 2);
        }

    }

    @Override
    public void editsg(CompanyOrderManagementPage companyOrderManagementPage) {
        BigDecimal totalamount = BigDecimal.ZERO;
        List<CompanyOrderManagementS> companyOrderManagementSList = companyOrderManagementPage.getCompanyOrderManagementSList();
        for (CompanyOrderManagementS companyOrderManagementS : companyOrderManagementSList) {
            //完成状态
            Integer status = companyOrderManagementS.getStatus();
            if (status == 1) {
                companyOrderManagementSService.saveOrUpdate(companyOrderManagementS);
                BigDecimal subtotal = companyOrderManagementS.getSubtotal();
                totalamount = totalamount.add(subtotal);
            }
        }
        String hth = companyOrderManagementPage.getContractnumber();
        String ssxm = companyOrderManagementPage.getSsxm();
        CompanyContractManagement byHth = companyContractManagementService.getByHth(hth);
        if (byHth != null) {
            BigDecimal totalamountHt = byHth.getTotalamount();
            //totalamountHt  totalamount都不为空 算完成率
            if (totalamountHt != null && totalamount != null) {
                BigDecimal divide = totalamount.divide(totalamountHt, 2, RoundingMode.HALF_UP);
                BigDecimal multiply = divide.multiply(new BigDecimal(100));
                byHth.setSchedule(multiply);
                companyContractManagementService.updateById(byHth);
            }
        }
        companyProjectService.updateJindu(ssxm);
    }

    @Override
    @Transactional
    public void delMain(String id) {
        LambdaQueryWrapper<CompanyOrderManagement> companyOrderManagementLambdaQueryWrapper = new LambdaQueryWrapper<>();
        companyOrderManagementLambdaQueryWrapper.eq(CompanyOrderManagement::getId, id);
        companyOrderManagementSMapper.deleteByMainId(id);
        companyOrderManagementMapper.deleteById(id);

    }

    @Override
    @Transactional
    public void delBatchMain(Collection<? extends Serializable> idList) {
        for (Serializable id : idList) {
            companyOrderManagementSMapper.deleteByMainId(id.toString());
            companyOrderManagementMapper.deleteById(id);
        }
    }

    @Override
    public List<CompanyOrderManagementPage> getByContractnumber(String contractnumber) {
        return companyOrderManagementMapper.getByContractnumber(contractnumber);
    }
}
