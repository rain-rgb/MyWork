package com.trtm.iot.company_order_management.mapper;

import java.util.List;

import com.trtm.iot.company_order_management.vo.CompanyOrderManagementPage;
import org.apache.ibatis.annotations.Param;
import com.trtm.iot.company_order_management.entity.CompanyOrderManagement;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 订单管理
 * @Author: jeecg-boot
 * @Date:   2024-07-01
 * @Version: V1.0
 */
public interface CompanyOrderManagementMapper extends BaseMapper<CompanyOrderManagement> {

    List<CompanyOrderManagementPage> getByContractnumber(String contractnumber);
}
