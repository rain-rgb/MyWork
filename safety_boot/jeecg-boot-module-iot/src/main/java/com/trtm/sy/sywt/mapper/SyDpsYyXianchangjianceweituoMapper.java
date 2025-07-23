package com.trtm.sy.sywt.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.trtm.sy.sydpssysample.entity.SysDepart;
import com.trtm.sy.sywt.entity.SyDpsYyXianchangjianceweituo;
import com.trtm.sy.sywt.entity.XcWtRequest;
import com.trtm.sy.sywt.entity.XcWtResponse;
import org.apache.ibatis.annotations.Select;

import java.util.Map;

/**
 * @Description: sy_dps_yy_xianchangjianceweituo
 * @Author: jeecg-boot
 * @Date: 2023-06-28
 * @Version: V1.0
 */
public interface SyDpsYyXianchangjianceweituoMapper extends BaseMapper<SyDpsYyXianchangjianceweituo> {

    @Select("select * from sys_depart where org_code=#{orgCode}")
    SysDepart getDepartByUserOrgCode(String orgCode);

    @Select("select id,currentCode from sy_dps_sy_codingFlowNumber where NoFlowNumber=#{bh}")
    Map<String, Object> getCodingFlowNumber(String bh);

    Long getCountByTable(String table, String liushuihao, String bh);

    Long getCountForYcQyWt(String liushuihao, String bh);

    void updateCodeById(String currentCode, String id);

    void insertCodingFlowNumber(String uuid, String currentCode, String bh);

    Page<XcWtResponse> getListXcWt(XcWtRequest request, Page<XcWtResponse> page);

    Map<String, Object> getXcWtById(Integer id);
}
