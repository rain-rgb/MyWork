package com.trtm.iot.wzgongyingshang.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import com.trtm.iot.wzgongyingshang.entity.Wzgongyingshang;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

/**
 * @Description: 物资供应商主表
 * @Author: jeecg-boot
 * @Date:   2021-05-07
 * @Version: V1.0
 */
public interface WzgongyingshangMapper extends BaseMapper<Wzgongyingshang> {

    @Select("select id from sys_depart where org_code=#{orgCode}")
    Map selectqueryone(String orgCode);

    List<Map> queryList(String sysOrgCode);

}
