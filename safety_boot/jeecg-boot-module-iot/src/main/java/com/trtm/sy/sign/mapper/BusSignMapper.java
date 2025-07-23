package com.trtm.sy.sign.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.trtm.sy.sign.model.entity.BusSign;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 *
 * @author wcx
 */
@Mapper
public interface BusSignMapper extends BaseMapper<BusSign> {

    void updateStatus(String signid, String status);

    void removeByBginfoid(String bginfoid);

    Integer selectSignatureStatus(String tenantName);

    IPage<Map> selectListByCode(Page<Map> page, String orgCode);

    String selectNameById(String personid);

    String selectNameByCode(String orgcode);
}
