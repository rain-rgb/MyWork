package com.trtm.iot.salarylist.service.impl;

import com.trtm.iot.salarylist.entity.SalaryList;
import com.trtm.iot.salarylist.mapper.SalaryListMapper;
import com.trtm.iot.salarylist.service.ISalaryListService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 工资清单
 * @Author: jeecg-boot
 * @Date:   2022-07-13
 * @Version: V1.0
 */
@Service
public class SalaryListServiceImpl extends ServiceImpl<SalaryListMapper, SalaryList> implements ISalaryListService {

}
