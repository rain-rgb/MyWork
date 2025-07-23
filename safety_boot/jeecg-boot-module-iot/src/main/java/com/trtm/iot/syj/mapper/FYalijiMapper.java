package com.trtm.iot.syj.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.trtm.iot.syj.entity.FYaliji;
import com.trtm.iot.syj.entity.TSyjzb;
import org.apache.ibatis.annotations.Mapper;
import com.trtm.iot.syj.entity.FsYaliji;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * @Description: f_yaliji
 * @Author: jeecg-boot
 * @Date:   2021-03-12
 * @Version: V1.0
 */
@Mapper
public interface FYalijiMapper extends BaseMapper<FsYaliji> {

    IPage<TSyjzb> selFyljMapper(Page<TSyjzb> page);

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
