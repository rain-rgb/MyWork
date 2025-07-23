package com.trtm.iot.trzhanglarenwudan.mapper;

import java.util.List;
import java.util.Map;

import com.trtm.iot.zhiliangrenwudan.entity.Zhiliangrenwudan;
import org.apache.ibatis.annotations.Param;
import com.trtm.iot.trzhanglarenwudan.entity.TrZhanglaRenwudan;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 张拉任务单
 * @Author: jeecg-boot
 * @Date:   2021-09-07
 * @Version: V1.0
 */
public interface TrZhanglaRenwudanMapper extends BaseMapper<TrZhanglaRenwudan> {

    List<TrZhanglaRenwudan> selectLists(String strsToList1, Integer curid);

    List<String> saveShebei(List<String> querySheBeiList);

    List<TrZhanglaRenwudan> saveShebeis(List<String> querySheBeiList);

    Map selectwbs(String sgbwuuid);
}
