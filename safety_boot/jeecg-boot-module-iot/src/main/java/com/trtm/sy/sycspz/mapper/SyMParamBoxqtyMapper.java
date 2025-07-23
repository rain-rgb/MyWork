package com.trtm.sy.sycspz.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.trtm.sy.sycspz.entity.SyMParamBoxqty;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * @Description: sy_m_param_boxqty
 * @Author: jeecg-boot
 * @Date:   2023-12-07
 * @Version: V1.0
 */
public interface SyMParamBoxqtyMapper extends BaseMapper<SyMParamBoxqty> {

    IPage<Map<String, Object>> mparamraboxqtyList(Page page, String boxno, String orgCode);

    @Select("select a.Boxno from SY_M_PARAM_BOXQTY a where a.departId = #{id} ")
    List<Map<String, Object>> importExcel(String id);
}
