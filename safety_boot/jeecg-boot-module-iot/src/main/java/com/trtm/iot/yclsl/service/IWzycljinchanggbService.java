package com.trtm.iot.yclsl.service;

import com.trtm.iot.wzcailiaonamedict.entity.Wzcailiaonamedict;
import com.trtm.iot.wzgongyingshang.entity.Wzgongyingshang;
import com.trtm.iot.wzliaocang.entity.Wzliaocang;
import com.trtm.iot.yclsl.entity.Wzycljinchanggb;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Description: 原材料收料主表
 * @Author: jeecg-boot
 * @Date:   2021-04-21
 * @Version: V1.0
 */
public interface IWzycljinchanggbService extends IService<Wzycljinchanggb> {

    List<Wzycljinchanggb> selectycljinchangList(Integer id,Integer taizhangtj);

    List<Wzycljinchanggb> selectycljinchangListRc(String sbbh, Integer taizhangtj ,String beizhu);

    void updateistongji(Integer id, Integer taizhangtj);

    Wzycljinchanggb selectByjinchuliaodanno(String jinchuliaodanno);

    List<Wzycljinchanggb> selectLists(List<String> strsToList1, Integer curid);

    List<Wzycljinchanggb> selectLists1(String strsToList1, Integer curid);

    List<Wzycljinchanggb> selectListsyjqs(String strsToList1, Integer curid);

    int saveMain(Wzycljinchanggb wzycljinchanggb, Wzcailiaonamedict wzcailiaonamedict, Wzgongyingshang wzgongyingshang, Wzliaocang wzliaocang);

    Wzycljinchanggb selectone(String jinchuliaodanno);

    List<Wzycljinchanggb> selecones(int istongji, Integer curid);

    List<Wzycljinchanggb> selectyclList(Integer curid, String shebeilist);

    List<Wzycljinchanggb> selectydyclList(Integer curid, String shebeilist);

    List<Wzycljinchanggb> selectlistjz(Integer curid, int jingzhongTtj);

    List<Wzycljinchanggb> slistrqid(String shebeilist, Integer curid);
}
