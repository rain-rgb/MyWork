package com.trtm.iot.bhzcementworkstatus.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import com.trtm.iot.bhzcementworkstatus.entity.BhzCementWorkstatus;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Update;

/**
 * @Description: 砼拌合站工作状态表信息
 * @Author: jeecg-boot
 * @Date:   2021-08-20
 * @Version: V1.0
 */
public interface BhzCementWorkstatusMapper extends BaseMapper<BhzCementWorkstatus> {

    @Update("update bhz_cement_workstatus set status =#{status} where id =#{id}")
    int updatestatus(int id, int status);

    BhzCementWorkstatus queryone(String shebeiNo, String sysOrgCode);
}
