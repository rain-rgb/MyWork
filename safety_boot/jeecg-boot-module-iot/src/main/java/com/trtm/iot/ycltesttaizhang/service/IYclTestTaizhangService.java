package com.trtm.iot.ycltesttaizhang.service;

import com.trtm.iot.wzcailiaonamedict.entity.Wzcailiaonamedict;
import com.trtm.iot.ycltesttaizhang.entity.YclTestTaizhang;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Description: ycl_test_taizhang
 * @Author: jeecg-boot
 * @Date:   2023-05-15
 * @Version: V1.0
 */
public interface IYclTestTaizhangService extends IService<YclTestTaizhang> {

    List<YclTestTaizhang> getList(String time,String orgCode);

    List<YclTestTaizhang> getHgList(String time,String orgCode);

    String getCailiaoName(String cailiaono);

    String getGongYSName(String gongyingshangdanweibianma);

    void updateByPici(YclTestTaizhang yclTestTaizhang);

    void updateZgztByPici(String zgzt,String pici);

    int getBhCount(String s, String orgCode);
}
