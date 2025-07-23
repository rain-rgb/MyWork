package com.trtm.iot.staffbase.mapper;

import java.util.List;

import com.trtm.iot.staffbase.entity.StaffBaseInfo;
import org.apache.ibatis.annotations.Param;
import com.trtm.iot.staffbase.entity.StaffWorkDetail;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

/**
 * @Description: staff_work_detail
 * @Author: jeecg-boot
 * @Date:   2024-10-17
 * @Version: V1.0
 */
public interface StaffWorkDetailMapper extends BaseMapper<StaffWorkDetail> {

    @Select("SELECT * FROM staff_work_detail where  pushstatus = 0 and sys_org_code = #{orgcode}  limit 100")
    List<StaffWorkDetail> getPushList( @Param("orgcode") String orgcode);

}
