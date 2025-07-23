package com.trtm.iot.yajiangm.service;

import com.trtm.iot.yajiangm.entity.TrYajiangM;
import com.baomidou.mybatisplus.extension.service.IService;
import com.trtm.iot.yajiangm.entity.TrYajiangSM;
import com.trtm.iot.yajiangs.entity.TrYajiangS;

import java.util.List;
import java.util.Map;

/**
 * @Description: 压浆主表信息
 * @Author: jeecg-boot
 * @Date:   2021-09-06
 * @Version: V1.0
 */
public interface ITrYajiangMService extends IService<TrYajiangM> {


    /**
     * 添加一对多
     *
     */
    public void saveMain(TrYajiangM trYajiangM, List<TrYajiangS> yajiangs);

    List<Map<String, Object>> countList();

    public TrYajiangM selectgetOne(String syjid);

    List<TrYajiangM> selectLists(String uuid);

    List<TrYajiangM> selectListzlpz(String shebeibianhao);

    List<TrYajiangSM> selectListstatistics(Integer curid, Integer statistics);

    void updateStatistics(String sbjno, Integer statistics);

    TrYajiangM queryone(String shebeiNo);

    List<TrYajiangM> selectList(String shebeibianhao);

    List<TrYajiangM> selectyjList(String shebeiNo, Integer id);

    List<TrYajiangM> selectrcyjList(String shebeiNo, Integer id);

    List<TrYajiangM> selectyjblList(String shebeiNo, Integer id);

    List<TrYajiangM> selectydyjList(String shebeiNo, Integer id);

    List<TrYajiangM> selectyj47toyd(String shebeiNo, Integer id);

    List<TrYajiangM> selectListToSHYJ(String shebeiNo, Integer id);

    List<TrYajiangM> selectbigid(Integer curid);

    TrYajiangM selectceslll(String syjid);

    Integer findRenwudanzs(List<String> yajiangRenwudans);

    Integer findRenwudanyjs(List<String> yajiangRenwudans);

    Integer findRenwudanbhs(List<String> yajiangRenwudans);

    List<String> selectListsb(String shebei);

    List<TrYajiangM> selectUnifiedProcess(Integer curid, Integer alertstate, List<String> shebeiList, Integer overLevel);

    List<TrYajiangM> selectBySBList(String shebeilist, Integer curid);

    List<TrYajiangM> selectListst(String shebeilist, Integer curid);

    List<TrYajiangM> selectlist();

    List<TrYajiangM> selectListoJG(String shebeilist, Integer curid);
}
