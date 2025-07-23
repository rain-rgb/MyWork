package com.trtm.iot.hc_transportrecords.mapper;

import java.util.List;

import com.trtm.iot.hc_transportrecords.entity.HcTransportrecords;
import com.trtm.iot.hc_transportrecords.entity.HcTransportrecordsPave;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Description: 运输数据子表
 * @Author: jeecg-boot
 * @Date:   2023-05-09
 * @Version: V1.0
 */
public interface HcTransportrecordsPaveMapper extends BaseMapper<HcTransportrecordsPave> {

	public boolean deleteByMainId(@Param("mainId") String mainId);

	public List<HcTransportrecordsPave> selectByMainId(@Param("mainId") String mainId);

    List<HcTransportrecords> getList(String shebeilist, Integer curid);

    List<HcTransportrecords> getListjt(String shebeilist, Integer curid);
}
