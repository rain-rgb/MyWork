package com.trtm.iot.company_order_management.mapper;

import java.util.List;
import com.trtm.iot.company_order_management.entity.CompanyOrderManagementS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Description: 订单管理子表
 * @Author: jeecg-boot
 * @Date:   2024-07-01
 * @Version: V1.0
 */
public interface CompanyOrderManagementSMapper extends BaseMapper<CompanyOrderManagementS> {

	public boolean deleteByMainId(@Param("mainId") String mainId);
    
	public List<CompanyOrderManagementS> selectByMainId(@Param("mainId") String mainId);
}
