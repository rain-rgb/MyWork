package com.trtm.iot.devicemixpilerealdata.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import com.trtm.iot.devicemixpilerealdata.entity.DeviceMixpileRealdata;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

/**
 * @Description: device_mixpile_realdata
 * @Author: jeecg-boot
 * @Date:   2021-10-20
 * @Version: V1.0
 */
public interface DeviceMixpileRealdataMapper extends BaseMapper<DeviceMixpileRealdata> {
   // @Select(" SELECT   count( 1 ) onlinecount    FROM   device_mixpile_realdata r  INNER JOIN shebei_info s ON r.shebeino = s.sbjno   WHERE   s.sys_org_code LIKE #{sysOrgCode}    AND r.datatime BETWEEN date_add( now(), INTERVAL - 5 MINUTE )   AND now()")
    List<Map<String, Object>> countOnline(String sysOrgCode);

    DeviceMixpileRealdata queryone(String shebeiNo);

    List<DeviceMixpileRealdata> listByshebei(String shebeilist);

    List<DeviceMixpileRealdata> queryones(String shebeiNo);
}
