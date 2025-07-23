package com.trtm.iot.company_contract_management.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.trtm.iot.company_contract_management.entity.CompanyContractManagement;
import com.trtm.iot.company_contract_management.mapper.CompanyContractManagementMapper;
import com.trtm.iot.company_contract_management.service.ICompanyContractManagementService;
import com.trtm.iot.company_project.entity.CompanyProject;
import com.trtm.iot.company_project.service.ICompanyProjectService;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.oConvertUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @Description: 合同管理
 * @Author: jeecg-boot
 * @Date:   2024-07-10
 * @Version: V1.0
 */
@Service
public class CompanyContractManagementServiceImpl extends ServiceImpl<CompanyContractManagementMapper, CompanyContractManagement> implements ICompanyContractManagementService {
    @Autowired
    private ICompanyContractManagementService companyContractManagementService;
    @Autowired
    private ICompanyProjectService companyProjectService;
    @Override
    public void add(CompanyContractManagement companyContractManagement) {
        //获取随机guid
        String s = UUID.randomUUID().toString();
        companyContractManagement.setGuid(s);

        // 获取当前日期
        LocalDate date = LocalDate.now();

        // 创建一个 DateTimeFormatter 对象用于格式化日期
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        // 将日期格式化为字符串
        String formattedDate = formatter.format(date);
        String contractnumber = companyContractManagement.getContractnumber();
        if (oConvertUtils.isEmpty(contractnumber)) {
            //生成订单编号
            String s1 = "HT-" + formattedDate;
            //查询时候有包含s1的订单号 s1 = "DD-" + date.getTime() + "数量 +1";
            LambdaQueryWrapper<CompanyContractManagement> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.like(CompanyContractManagement::getContractnumber, s1);
            List<CompanyContractManagement> list = companyContractManagementService.list(queryWrapper);
            int size = list.size();
            int sizePlusOne = size + 1;
            String result = String.format("%03d", sizePlusOne);

            s1 = "HT-" + formattedDate + "-" + result;
            companyContractManagement.setContractnumber(s1);
        }
        //获取当前时间
        LocalDateTime localDateTime = date.atTime(LocalDateTime.now().toLocalTime());
        Date Creattime = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
        companyContractManagement.setCreattime(Creattime);

        //获取项目数据,修改合同表组织机构
        CompanyProject bySsxm = companyProjectService.getBySsxm(companyContractManagement.getSsxm());
        companyContractManagement.setSysOrgCode(bySsxm.getOrgcode());

        //获取当前用户的信息
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        String username = loginUser.getUsername();
        companyContractManagement.setCreatperson(username);

        companyContractManagementService.save(companyContractManagement);

        //调用项目新增时的接口，增加合同数，累加合同金额
        companyProjectService.updateByContract(companyContractManagement, 1);
    }

    @Override
    public CompanyContractManagement getBySsxm(String ssxm) {
        LambdaQueryWrapper<CompanyContractManagement> companyContractManagementLambdaQueryWrapper = new LambdaQueryWrapper<>();
        companyContractManagementLambdaQueryWrapper.eq(CompanyContractManagement::getSsxm, ssxm);
        return companyContractManagementService.getOne(companyContractManagementLambdaQueryWrapper);
    }

    @Override
    public CompanyContractManagement getByHth(String hth) {
        LambdaQueryWrapper<CompanyContractManagement> companyContractManagementLambdaQueryWrapper = new LambdaQueryWrapper<>();
        companyContractManagementLambdaQueryWrapper.eq(CompanyContractManagement::getContractnumber, hth);
        return companyContractManagementService.getOne(companyContractManagementLambdaQueryWrapper);
    }
}
