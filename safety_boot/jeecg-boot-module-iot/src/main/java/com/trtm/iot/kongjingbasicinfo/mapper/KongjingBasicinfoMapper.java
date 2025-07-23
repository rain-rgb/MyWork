package com.trtm.iot.kongjingbasicinfo.mapper;

import java.util.List;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import org.apache.ibatis.annotations.Param;
import com.trtm.iot.kongjingbasicinfo.entity.KongjingBasicinfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 孔径基本信息数据
 * @Author: jeecg-boot
 * @Date:   2022-03-01
 * @Version: V1.0
 */
@InterceptorIgnore(tenantLine = "1")
public interface KongjingBasicinfoMapper extends BaseMapper<KongjingBasicinfo> {

}

