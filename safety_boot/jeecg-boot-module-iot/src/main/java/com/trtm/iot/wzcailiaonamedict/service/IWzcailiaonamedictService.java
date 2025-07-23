package com.trtm.iot.wzcailiaonamedict.service;

import com.trtm.iot.rebarComponentMaterial.entity.RebarComponentMaterial;
import com.trtm.iot.wzcailiaonamedict.entity.Wzcailiaonamedict;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * @Description: 材料配置主表
 * @Author: jeecg-boot
 * @Date:   2021-05-07
 * @Version: V1.0
 */
public interface IWzcailiaonamedictService extends IService<Wzcailiaonamedict> {

    List<Wzcailiaonamedict> arrayOnecailiaos();

    Map selectqueryone(String sysOrgCode);

    Wzcailiaonamedict queryselectone1(String cailiaoNo);

    List<Wzcailiaonamedict> cailiaolist(String departid);

    List<Wzcailiaonamedict> usercailiaoList(String orgCode);

    List<Wzcailiaonamedict> cailiaolists(String departid, List<Integer> lmcailiaolx);

    List<String> queryNodetypeList1(String orgCode);

    List<String> queryNodetypeList2();

    String selectBylmcailiaolx(int i);

    List<Map> selectByYCCP(Integer cprule, Integer ycrule, String code);

    List<String> selectCailiaoNoByName(String cailiaoName);

    List<String> selectListByYCCP(Integer cprule, Integer ycrule, String code);



    List<RebarComponentMaterial> addMaterialToComponentId(String materialIds, String componentId);
}
