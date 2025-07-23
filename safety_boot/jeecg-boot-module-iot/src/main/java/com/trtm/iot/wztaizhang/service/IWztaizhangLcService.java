package com.trtm.iot.wztaizhang.service;

import com.trtm.iot.wztaizhang.entity.WztaizhangLc;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Date;

/**
 * @Description: wztaizhang_lc
 * @Author: jeecg-boot
 * @Date:   2023-12-04
 * @Version: V1.0
 */
public interface IWztaizhangLcService extends IService<WztaizhangLc> {


    boolean saveOrUpdateLC(String pici, String lcbh, Double incount, String jctime,String cailiaono,String gongyingshang,String sbbh,String sysorgcode );
}
