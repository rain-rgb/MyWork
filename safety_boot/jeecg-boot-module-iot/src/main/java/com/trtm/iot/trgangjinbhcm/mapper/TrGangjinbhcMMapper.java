package com.trtm.iot.trgangjinbhcm.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import com.trtm.iot.trgangjinbhcm.entity.TrGangjinbhcM;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 钢保数据检测主表
 * @Author: jeecg-boot
 * @Date:   2021-09-10
 * @Version: V1.0
 */
public interface TrGangjinbhcMMapper extends BaseMapper<TrGangjinbhcM> {

    public List<Map> getgbhgl(String projectid,String targetType);

    List<TrGangjinbhcM> selectGbList(Integer curid, String shebeilist);

    List<TrGangjinbhcM> selectGangJinList(String shebeilist, Integer curid);

    Integer selecbyzhid(List<String> sheBeiNoList,String zhifu);

    Integer selecbyzhidbhg(List<String> sheBeiNoList, String zhifu);
}
