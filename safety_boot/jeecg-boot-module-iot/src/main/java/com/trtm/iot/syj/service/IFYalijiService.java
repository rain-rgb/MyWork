package com.trtm.iot.syj.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.trtm.iot.syj.entity.FYaliji;
import com.trtm.iot.syj.entity.FsYaliji;
import com.baomidou.mybatisplus.extension.service.IService;
import com.trtm.iot.syj.entity.TSyjzb;

import java.util.List;

/**
 * @Description: f_yaliji
 * @Author: jeecg-boot
 * @Date:   2021-03-12
 * @Version: V1.0
 */

public interface IFYalijiService extends IService<FsYaliji> {


    IPage<TSyjzb> selFyljMapper(Integer pageNo, Integer pageSize);

    List<FsYaliji> selectFsYalijiList(String syjid);

    List<FYaliji> selectFsYalijiData(String syjid);

    /**
     * 通过主表唯一id查询子表数据
     * @param syjid
     * @return
     */
    List<FYaliji> selectFYalijiList(String syjid);

    String selectMaxSysj(String syjid);

    void updateSbbh(String shebeilist);

    List<FsYaliji> selectSyjylList(String shebeilist, Integer curid);
}
