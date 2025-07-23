package com.trtm.iot.wzconsumetaizhangdetail.service;

import com.trtm.iot.wzconsumetaizhangdetail.entity.WzconsumetaizhangDetail;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Description: 物资原材料消耗台账材料子表
 * @Author: jeecg-boot
 * @Date:   2021-09-07
 * @Version: V1.0
 */
public interface IWzconsumetaizhangDetailService extends IService<WzconsumetaizhangDetail> {

    WzconsumetaizhangDetail selectwzxiaohaodetail(String materialeName, int ids);

    int updatedetail(Integer ids, Double realityNumber, Double cailiaoshengyut, String lcNo);

    List<WzconsumetaizhangDetail> selectdetaillist(Integer id);
}
