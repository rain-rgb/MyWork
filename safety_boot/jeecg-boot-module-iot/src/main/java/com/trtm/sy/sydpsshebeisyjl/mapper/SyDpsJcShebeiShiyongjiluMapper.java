package com.trtm.sy.sydpsshebeisyjl.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.trtm.sy.sydpsshebeisyjl.entity.SheBeiSyJlResponse;
import com.trtm.sy.sydpsshebeisyjl.entity.SyDpsJcShebeiShiyongjilu;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Description: dps_jc_shebei_shiyongjilu
 * @Author: jeecg-boot
 * @Date: 2023-02-27
 * @Version: V1.0
 */
public interface SyDpsJcShebeiShiyongjiluMapper extends BaseMapper<SyDpsJcShebeiShiyongjilu> {

    IPage<SheBeiSyJlResponse> getSbSyJl(@Param("sheBeiIds") List<String> sheBeiIds, @Param("startTime") String startTime, @Param("endTime") String endTime, Page page);
}
