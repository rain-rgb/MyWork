package com.trtm.iot.hntconsign.service;

import com.trtm.iot.hntconsign.entity.HntConsign;
import com.baomidou.mybatisplus.extension.service.IService;
import com.trtm.iot.syj.entity.TSyjzb;
import com.trtm.sy.sydpssysample.entity.SysDepart;

import java.util.List;

/**
 * @Description: 混凝土见证取样表信息
 * @Author: jeecg-boot
 * @Date:   2021-07-02
 * @Version: V1.0
 */
public interface IHntConsignService extends IService<HntConsign> {

    /**
     * 验证二维码是否已经添加过
     * @param code
     * @return
     */
    HntConsign queryGetone(String code);


    /**
     *
     */
    HntConsign queryGetoneNo(String code);

    List<SysDepart> getbiaoduan();

    List<TSyjzb> getsyj(String arrayshebei);
}
