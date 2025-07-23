package com.trtm.iot.ydcx.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.trtm.iot.ydcx.entity.WYanduM;

import java.util.List;
import java.util.Map;

/**
 * @Description: w_yandu_m
 * @Author: jeecg-boot
 * @Date:   2021-04-26
 * @Version: V1.0
 */
public interface IWYanduMService extends IService<WYanduM> {

    Map selectSYSwHeGeLv(String tableName, List<String> shebeiList);

    List<WYanduM> getListjt(String shebeilist, Integer curid);
}
