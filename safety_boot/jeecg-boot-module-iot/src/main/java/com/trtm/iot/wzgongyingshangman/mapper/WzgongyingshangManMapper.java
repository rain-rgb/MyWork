package com.trtm.iot.wzgongyingshangman.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import com.trtm.iot.wzgongyingshangman.entity.WzgongyingshangMan;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

/**
 * @Description: wzgongyingshang_man
 * @Author: jeecg-boot
 * @Date:   2022-08-08
 * @Version: V1.0
 */
public interface WzgongyingshangManMapper extends BaseMapper<WzgongyingshangMan> {

    @Select("select id from sys_depart where org_code=#{orgCode}")
    Map selectqueryone(String sysOrgCode);
}
