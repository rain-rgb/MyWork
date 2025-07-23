package com.trtm.iot.hc_datalinkage.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import com.trtm.iot.hc_datalinkage.entity.HcDatalinkage;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 数据联动（运输信息）
 * @Author: jeecg-boot
 * @Date:   2023-06-13
 * @Version: V1.0
 */
public interface HcDatalinkageMapper extends BaseMapper<HcDatalinkage> {
    List<HcDatalinkage> getList(String shebeilist, Integer curid);
    List<HcDatalinkage> getListjt(String shebeilist, Integer curid);
}
