package com.trtm.iot.wzcailiaonamedictman.service;

import com.trtm.iot.rebarComponent.entity.ComponentVo;
import com.trtm.iot.rebarComponent.vo.MaterialVo;
import com.trtm.iot.wzcailiaonamedict.entity.Wzcailiaonamedict;
import com.trtm.iot.wzcailiaonamedictman.entity.WzcailiaonamedictMan;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * @Description: wzcailiaonamedict_man
 * @Author: jeecg-boot
 * @Date:   2022-08-08
 * @Version: V1.0
 */
public interface IWzcailiaonamedictManService extends IService<WzcailiaonamedictMan> {

    List<WzcailiaonamedictMan> arrayOnecailiaos();

    Map selectqueryone(String sysOrgCode);

    WzcailiaonamedictMan queryselectone1(String cailiaono);
    String selectBylmcailiaolx(int i);

    List<Map> selectByYCCP(Integer cprule, Integer ycrule, String code);

    List<String> selectCailiaoNoByName(String cailiaoName);

    List<String> selectListByYCCP(Integer cprule, Integer ycrule, String code);
    List<MaterialVo> queryMaterialByComponentId(String id);

    List<WzcailiaonamedictMan> queryMaterialByNodeType(String nodeType);

    List<WzcailiaonamedictMan> queryMaterialList(String sys_depart_orgcode, String materialName, String startDate, String endDate);

    List<MaterialVo> getMaterialList(MaterialVo materialVo, Integer pageNo, Integer pageSize, String sys_depart_orgcode);

    WzcailiaonamedictMan getWzcailiaonamedictManById(String id);
}
