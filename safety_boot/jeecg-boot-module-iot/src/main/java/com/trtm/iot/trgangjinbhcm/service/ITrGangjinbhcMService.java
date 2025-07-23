package com.trtm.iot.trgangjinbhcm.service;

import com.trtm.iot.hntbhz.entity.BhzCementBase;
import com.trtm.iot.hntbhz.entity.BhzCementDetail;
import com.trtm.iot.trgangjinbhcm.entity.TrGangjinbhcM;
import com.baomidou.mybatisplus.extension.service.IService;
import com.trtm.iot.trgangjinbhcs.entity.TrGangjinbhcS;
import com.trtm.iot.trhnthtm.entity.TrHnthtM;

import java.util.List;
import java.util.Map;

/**
 * @Description: 钢保数据检测主表
 * @Author: jeecg-boot
 * @Date:   2021-09-10
 * @Version: V1.0
 */
public interface ITrGangjinbhcMService extends IService<TrGangjinbhcM> {

    /**
     * 添加一对多
     *
     */
    public int saveMain(TrGangjinbhcM trGangjinbhcM, List<TrGangjinbhcS> trGangjinbhcS) ;


    public List<TrGangjinbhcM> selectGangJinList(String shebeiNo, Integer id);

    public List<TrGangjinbhcM> selectGangJinLists(List<String> shebeiNo, Integer id);

    public List<Map> getgbhgl(String projectid,String targetType);

    List<TrGangjinbhcM> selectGbList(Integer curid, String shebeilist);
}
