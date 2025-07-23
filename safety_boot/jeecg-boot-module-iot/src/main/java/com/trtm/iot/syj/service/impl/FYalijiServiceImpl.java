package com.trtm.iot.syj.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.trtm.iot.syj.entity.FYaliji;
import com.trtm.iot.syj.entity.FsYaliji;
import com.trtm.iot.syj.entity.TSyjzb;
import com.trtm.iot.syj.mapper.FYalijiMapper;
import com.trtm.iot.syj.service.IFYalijiService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Description: f_yaliji
 * @Author: jeecg-boot
 * @Date: 2021-03-12
 * @Version: V1.0
 */
@Service
public class FYalijiServiceImpl extends ServiceImpl<FYalijiMapper, FsYaliji> implements IFYalijiService {
    @Resource
    private FYalijiMapper fYalijiMapper;

    @Override
    public IPage<TSyjzb> selFyljMapper(Integer pageNo, Integer pageSize) {
        Page<TSyjzb> page = new Page<>(pageNo, pageSize);
        return  fYalijiMapper.selFyljMapper(page);

    }

    @Override
    public List<FsYaliji> selectFsYalijiList(String syjid) {
        try {
            QueryWrapper<FsYaliji> queryWrapper=new QueryWrapper<>();
            queryWrapper.eq("SYJID",syjid);
            return this.list(queryWrapper);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<FYaliji> selectFsYalijiData(String syjid) {
        return fYalijiMapper.selectFsYalijiData(syjid);
    }

    @Override
    public List<FYaliji> selectFYalijiList(String syjid) {
        return fYalijiMapper.selectFYalijiList(syjid);
    }

    @Override
    public String selectMaxSysj(String syjid) {
        return fYalijiMapper.selectMaxSysj(syjid);
    }

    @Override
    public void updateSbbh(String shebeilist) {
        fYalijiMapper.updateSbbh(shebeilist);
    }

    @Override
    public List<FsYaliji> selectSyjylList(String shebeilist, Integer curid) {
        return fYalijiMapper.selectSyjylList(shebeilist, curid);
    }
}
