package com.trtm.iot.syj.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.trtm.iot.syj.entity.FWangnj;
import com.trtm.iot.syj.entity.fswangnj;

import java.util.List;

/**
 * @Description: f_wangnj
 * @Author: jeecg-boot
 * @Date:   2021-03-15
 * @Version: V1.0
 */
public interface FWangnjMapper extends BaseMapper<FWangnj> {

    void insert(fswangnj fsWangnj);

    List<FWangnj> selectFswannjData(String syjid);

    String selectMaxSysj(String syjid);

    void updateSbbh(String shebeilist);

    List<FWangnj> selectSyjwnList(String shebeilist, Integer curid);
}
