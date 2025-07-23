package com.trtm.iot.bhzrwdxx.mapper;

import java.util.List;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.annotation.SqlParser;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;
import com.trtm.iot.bhzrwdxx.entity.Bhzrwdxx;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: bhzrwdxx
 * @Author: jeecg-boot
 * @Date:   2022-03-02
 * @Version: V1.0
 */

@InterceptorIgnore(tenantLine = "true")
public interface BhzrwdxxMapper extends BaseMapper<Bhzrwdxx> {

}
