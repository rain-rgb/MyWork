package com.trtm.iot.materialTesting.service.impl;

import com.trtm.iot.materialTesting.entity.MaterialTesting;
import com.trtm.iot.materialTesting.mapper.MaterialTestingMapper;
import com.trtm.iot.materialTesting.service.IMaterialTestingService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 原材料本月检测工作量汇总表
 * @Author: jeecg-boot
 * @Date:   2022-07-15
 * @Version: V1.0
 */
@Service
public class MaterialTestingServiceImpl extends ServiceImpl<MaterialTestingMapper, MaterialTesting> implements IMaterialTestingService {

}
