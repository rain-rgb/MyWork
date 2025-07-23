package com.trtm.sy.sysbjdjz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.trtm.sy.sysbjdjz.entity.SbJianDingJiaoZhunResponse;
import com.trtm.sy.sysbjdjz.entity.SyDpsJcShebeiJiandingjiaozhun;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Description: sy_dps_jc_shebei_jiandingjiaozhun
 * @Author: jeecg-boot
 * @Date: 2023-10-17
 * @Version: V1.0
 */
public interface SyDpsJcShebeiJiandingjiaozhunMapper extends BaseMapper<SyDpsJcShebeiJiandingjiaozhun> {

    IPage<SbJianDingJiaoZhunResponse> getJdJz(@Param("list") List<String> list, @Param("startTime") String startTime, @Param("endTime") String endTime, @Param("type") Integer type, Page page);
}
