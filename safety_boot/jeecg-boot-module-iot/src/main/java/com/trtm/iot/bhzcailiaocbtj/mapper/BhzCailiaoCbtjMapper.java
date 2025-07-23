package com.trtm.iot.bhzcailiaocbtj.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import com.trtm.iot.bhzcailiaocbtj.entity.BhzCailiaoCbtj;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

/**
 * @Description: bhz_cailiao_cbtj
 * @Author: jeecg-boot
 * @Date:   2022-12-06
 * @Version: V1.0
 */
public interface BhzCailiaoCbtjMapper extends BaseMapper<BhzCailiaoCbtj> {

    @Select("select * from bhz_cailiao_cbtj where shebei_no=#{shebeiNo} and materiale_type=#{materialeType} and materiale_name=#{materialeName}")
    BhzCailiaoCbtj selectclOne(String shebeiNo, Integer materialeType, String materialeName);
}
