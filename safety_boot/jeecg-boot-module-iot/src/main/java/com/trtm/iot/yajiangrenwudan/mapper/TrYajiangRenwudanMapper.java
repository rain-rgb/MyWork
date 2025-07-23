package com.trtm.iot.yajiangrenwudan.mapper;

import java.util.List;

import com.trtm.iot.trzhanglarenwudan.entity.TrZhanglaRenwudan;
import org.apache.ibatis.annotations.Param;
import com.trtm.iot.yajiangrenwudan.entity.TrYajiangRenwudan;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 压浆任务单
 * @Author: jeecg-boot
 * @Date:   2021-09-08
 * @Version: V1.0
 */
public interface TrYajiangRenwudanMapper extends BaseMapper<TrYajiangRenwudan> {

    List<TrYajiangRenwudan> selectLists(String strsToList1, Integer curid);

    List<String> saveShebei(List<String> querySheBeiList);

    List<TrYajiangRenwudan> saveShebeis(List<String> querySheBeiList);
}
