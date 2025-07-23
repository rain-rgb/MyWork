package com.trtm.iot.staffbase.service;

import com.trtm.iot.staffbase.entity.StaffBaseInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Description: staff_base_info
 * @Author: jeecg-boot
 * @Date:   2024-10-17
 * @Version: V1.0
 */
public interface IStaffBaseInfoService extends IService<StaffBaseInfo> {

    List<StaffBaseInfo> getPushList( String renyuantype,String orgcode);
    List<String> userIdList(String orgcode);
}
