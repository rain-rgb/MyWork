package com.trtm.iot.system.service;

import com.trtm.iot.system.entity.Shigongphb;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Description: 施工配合比
 * @Author: jeecg-boot
 * @Date:   2021-05-19
 * @Version: V1.0
 */
public interface IShigongphbService extends IService<Shigongphb> {

    List<Shigongphb> queryones(String code);

    List<Shigongphb> queryonelist(String code, String shebei, String workNo);

    Shigongphb queryoneCode(String code);

    Shigongphb queryoneRenwudan(String renwudan);

    List<Shigongphb> selectLists(String shebeilist, Integer curid);

    List<Shigongphb> selectListsYJQS(String shebeilist, Integer curid);


    List<Shigongphb> selectPhbList(Integer curid);

    List<Shigongphb> selectlistjz(Integer curid, int statistic);

    Shigongphb getByFormulaNo(String formulaNo,String device);

    List<Shigongphb> queryoneRenwudanbyStation(String code,String recipe, Integer station);

    List<Shigongphb> selectListytwnd(String shebeilist, Integer curid);
}
