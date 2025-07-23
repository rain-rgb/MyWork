package com.trtm.iot.trhnthtm.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import com.trtm.iot.trhnthtm.entity.TrHnthtM;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 混凝土回弹主表
 * @Author: jeecg-boot
 * @Date:   2021-09-13
 * @Version: V1.0
 */
public interface TrHnthtMMapper extends BaseMapper<TrHnthtM> {

    List<TrHnthtM> selecthntList(Integer curid, String shebeilist);

    List<TrHnthtM> selectHntHtList(String shebeilist, Integer curid);

    List<TrHnthtM> selectHntHtsyList(String shebeilist, Integer curid);
}
