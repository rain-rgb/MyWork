package com.trtm.iot.sysconfig.mapper;

import java.util.List;

import com.trtm.iot.hntbhz.entity.BhzCementBase;
import com.trtm.iot.hntbhz.entity.BhzCementDetail;
import org.apache.ibatis.annotations.Param;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

/**
 * @Description: 定时任务配置表
 * @Author: jeecg-boot
 * @Date:   2021-03-17
 * @Version: V1.0
 */
public interface SysConfigMapper extends BaseMapper<SysConfig> {

    @Select("select * from bhz_cement_base  where  bhz_cement_base.id = #{id}")
    public List<BhzCementBase> selectListbai(@Param("id") Integer id);

    @Select("select * from bhz_cement_detail  where  bhz_cement_detail.batch_no = #{batchNo}")
    public List<BhzCementDetail> selectListbhzdetail(@Param("batchNo") String  batchNo);

    SysConfig selectsysconfigone(Integer cfgtype);
}
