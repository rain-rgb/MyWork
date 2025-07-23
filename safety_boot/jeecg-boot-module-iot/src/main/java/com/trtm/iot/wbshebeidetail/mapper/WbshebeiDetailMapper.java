package com.trtm.iot.wbshebeidetail.mapper;

import com.trtm.iot.wbshebeidetail.entity.WbshebeiDetail;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

/**
 * @Description: 电子锁详情数据表
 * @Author: jeecg-boot
 * @Date:   2022-02-22
 * @Version: V1.0
 */
public interface WbshebeiDetailMapper extends BaseMapper<WbshebeiDetail> {

    @Select("select depart_name from sys_depart where org_code = #{userdepartid}")
    String getDepartName(String userdepartid);
}
