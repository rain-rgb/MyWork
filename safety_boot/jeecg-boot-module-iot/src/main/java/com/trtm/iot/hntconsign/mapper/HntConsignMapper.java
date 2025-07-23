package com.trtm.iot.hntconsign.mapper;

import java.util.List;

import com.trtm.iot.syj.entity.TSyjzb;
import com.trtm.sy.sydpssysample.entity.SysDepart;
import org.apache.ibatis.annotations.Param;
import com.trtm.iot.hntconsign.entity.HntConsign;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 混凝土见证取样表信息
 * @Author: jeecg-boot
 * @Date:   2021-07-02
 * @Version: V1.0
 */
public interface HntConsignMapper extends BaseMapper<HntConsign> {

    List<SysDepart> getbiaoduan();

    List<TSyjzb> getsyj(String arrayshebei);
}
