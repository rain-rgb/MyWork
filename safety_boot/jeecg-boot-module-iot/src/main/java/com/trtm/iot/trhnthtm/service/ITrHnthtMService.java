package com.trtm.iot.trhnthtm.service;

import com.trtm.iot.trgangjinbhcm.entity.TrGangjinbhcM;
import com.trtm.iot.trgangjinbhcs.entity.TrGangjinbhcS;
import com.trtm.iot.trhnthtm.entity.TrHnthtM;
import com.baomidou.mybatisplus.extension.service.IService;
import com.trtm.iot.trhnthts.entity.TrHnthtS;

import java.util.List;

/**
 * @Description: 混凝土回弹主表
 * @Author: jeecg-boot
 * @Date:   2021-09-13
 * @Version: V1.0
 */
public interface ITrHnthtMService extends IService<TrHnthtM> {

    /**
     * 添加一对多
     *
     */
    public int saveMain(TrHnthtM trHnthtM, List<TrHnthtS> trHnthtS) ;

    public List<TrHnthtM> selectHntHtList(String shebeiNo,Integer id);

    public List<TrHnthtM> selectHntHtsyList(String shebeiNo,Integer id);

    public List<TrHnthtM> selectHntHtLists(List<String> shebeiNo,Integer id);

    List<TrHnthtM> selectList(Integer curid, String shebeilist);
}
