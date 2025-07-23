package com.trtm.iot.zhanglas.service;

import com.trtm.iot.hntbhz.entity.BhzCementBase;
import com.trtm.iot.hntbhz.entity.BhzCementDetail;
import com.trtm.iot.zhanglas.entity.TrZhanglaS;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Description: 张拉信息子表
 * @Author: jeecg-boot
 * @Date:   2021-08-31
 * @Version: V1.0
 */
public interface ITrZhanglaSService extends IService<TrZhanglaS> {

    /**
     * 添加多条数据
     * @param trZhanglaS
     */
    public void saveMain(List<TrZhanglaS> trZhanglaS);

    List<TrZhanglaS> selelctList(String syjid, String fguid);

    List<TrZhanglaS> selelctLists(String syjid, String fguid, int i);

    List<TrZhanglaS> selelctListbybd(String syjid, String fguid, int i);
}
