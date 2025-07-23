package com.trtm.iot.wzyclHandler.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import com.trtm.iot.wzyclHandler.entity.WzyclHandler;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

/**
 * @Description: wzycl_handler
 * @Author: jeecg-boot
 * @Date: 2022-11-21
 * @Version: V1.0
 */
public interface WzyclHandlerMapper extends BaseMapper<WzyclHandler> {

    String selectByBaseId(String baseid);

    int addCZInfoById(String baseid, String wtyy, String czfs, String czjg, String czperson, String czfile);

    int updateCZInfoById(String wtyy, String czfs, String czjg, String czperson, String czfile, String baseid);

    int addSPInfoById(String baseid, String spyj, String spjg, String spperson, String spfile);

    int updateSPInfoById( String spyj, String spjg, String spperson, String spfile, String baseid);
}
