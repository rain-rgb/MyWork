package com.trtm.iot.staffbase.service;

import com.trtm.iot.staffbase.entity.StaffBaseSalary;
import com.trtm.iot.staffbase.entity.StaffBaseWork;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @Description: staff_base_work
 * @Author: jeecg-boot
 * @Date:   2024-10-17
 * @Version: V1.0
 */
public interface IStaffBaseWorkService extends IService<StaffBaseWork> {

    List<Map> getStatistics( String orgcode,String start);
    List<StaffBaseSalary> getMothonHours(String orgcode,String start,Integer pageNo,Integer pageSize);

}
