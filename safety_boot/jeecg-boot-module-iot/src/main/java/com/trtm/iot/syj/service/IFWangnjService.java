package com.trtm.iot.syj.service;

import com.trtm.iot.syj.entity.FWangnj;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Description: f_wangnj
 * @Author: jeecg-boot
 * @Date:   2021-03-15
 * @Version: V1.0
 */
public interface IFWangnjService extends IService<FWangnj> {


    List<FWangnj> selectFswannjData(String syjid);

    String selectMaxSysj(String syjid);
    void updateSbbh(String shebeilist);

    List<FWangnj> selectSyjwnList(String shebeilist, Integer curid);
}
