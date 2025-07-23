package com.trtm.iot.wzliaocang.service;

import com.trtm.iot.wzcailiaonamedict.entity.Wzcailiaonamedict;
import com.trtm.iot.wzliaocang.entity.Wzliaocang;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;


/**
 * @Description: 料仓配置表
 * @Author: jeecg-boot
 * @Date:   2021-05-07
 * @Version: V1.0
 */
public interface IWzliaocangService extends IService<Wzliaocang> {
    Map selectqueryone(String orgCode);

    List<Map> selectkucun(Integer pageNo, Integer pageSize);

    Wzliaocang queryselectone(String lcNo);
    List<Wzliaocang> lclistBylw(String lwNo);

    List<Wzliaocang> lclist(String departid);

    List<Wzliaocang> userliaocangList(String orgCode);

    void updateWeightBylcNo(String lcNo,String weight);

    List<Wzliaocang> selectwzlcList(Integer curid, List<String> strsToList1);

    Wzliaocang selectByGuid(String guid,String orgCode);

    List<Wzliaocang> selectByOrgCodeToPM(String code);

    List<Wzliaocang> getSandStorageDataToPM(String code);

    List getJYP(String guid);

    String getSnByGuid(String guid);


}
