package com.trtm.iot.bys.service;

import com.trtm.iot.bys.entity.BysReal;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * @Description: bys_real
 * @Author: jeecg-boot
 * @Date:   2021-03-22
 * @Version: V1.0
 */
public interface IBysRealService extends IService<BysReal> {

    List<BysReal> selectbysone(Integer curid, Integer alertstate);

    List<BysReal> selectbysbaselist(String shebeino);

    List<BysReal> selectbysbaselists(List<String> shebeino,Integer id);

    List<BysReal> selectListBySbno(String shebeino);

    int updatebysone(Integer id, Integer alertstate);

    int updateStatus(Integer id, Integer final_over_level);

    Map stsPageLists3(String shebeis);

    BysReal queryone(String shebeiNo);

    List<BysReal> selectBysList(Integer curid, String shebeilist);

    List<BysReal> selectBysListkz(Integer curid, String shebeilist);


    BysReal selectBysbno(String shebeino, Integer curid);

    int upadteIstuisong(Integer id, int i);

    List<BysReal> queryListBySheBeis(String shebei, Integer curid);

    List<BysReal> queryListbim(String shebei, Integer curid);

    BysReal selectBysbnotwo(String shebeino, Integer tuisongid);
}
