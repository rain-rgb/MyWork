package com.trtm.iot.chaoshengbo.service;

import com.trtm.iot.chaoshengbo.entity.*;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * @Description: chaoshengbo_syjbsj
 * @Author: jeecg-boot
 * @Date:   2021-04-08
 * @Version: V1.0
 */
public interface IChaoshengboSyjbsjService extends IService<ChaoshengboSyjbsj> {


    public int saveMain(ChaoshengboSyjbsjs chaoshengboSyjbsjs, List<ChaoshengboSybltsj> chaoshengboSybltsjList, List<ChaoshengboSybsj> chaoshengboSybsjList, List<ChaoshengboSydsj> chaoshengboSydsjList, List<ChaoshengboSyjsb> chaoshengboSyjsbList, List<ChaoshengboSyljzs> chaoshengboSyljzsList, List<ChaoshengboSyqxsj> chaoshengboSyqxsjList);

    List<ChaoshengboSyjbsj> selectList(String liushuihao);

    List<ChaoshengboSyjbsj> selectLists(String shebeino,Integer id);

    void updateByIdToYb(Integer level, String file, String fileInfo, Integer id);

    Map statisticsLevelZB(List<String> shebeilist, Integer date);

    List tongjiZJLevel(List<String> shebeilist, Integer date);
}
