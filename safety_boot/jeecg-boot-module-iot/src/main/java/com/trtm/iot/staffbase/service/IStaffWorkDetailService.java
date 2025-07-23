package com.trtm.iot.staffbase.service;

import com.trtm.iot.staffbase.entity.StaffWorkDetail;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Description: staff_work_detail
 * @Author: jeecg-boot
 * @Date:   2024-10-17
 * @Version: V1.0
 */
public interface IStaffWorkDetailService extends IService<StaffWorkDetail> {
    List<StaffWorkDetail> getPushList( String orgcode);

}
