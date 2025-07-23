package com.trtm.iot.lqbhz.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.hntbhz.entity.BhzCementDetail;
import com.trtm.iot.lqbhz.entity.BhzLqCailiao;
import com.trtm.iot.lqbhz.mapper.BhzLqCailiaoMapper;
import com.trtm.iot.lqbhz.service.IBhzLqCailiaoService;
import org.springframework.stereotype.Service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 沥青子表信息
 * @Author: jeecg-boot
 * @Date: 2021-02-22
 * @Version: V1.0
 */
@Service
public class BhzLqCailiaoServiceImpl extends ServiceImpl<BhzLqCailiaoMapper, BhzLqCailiao> implements IBhzLqCailiaoService {

    @Autowired
    private BhzLqCailiaoMapper bhzLqCailiaoMapper;

    @Override
    public List<BhzLqCailiao> selectByMainId(String mainId) {
        return bhzLqCailiaoMapper.selectByMainId(mainId);
    }

    /**
     * 根据唯一id查询沥青材料表数据
     *
     * @param guid
     * @return
     */
    @Override
    public List<BhzLqCailiao> selectcailiaolist(String guid) {
        try {
            QueryWrapper<BhzLqCailiao> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("base_guid", guid);
            return this.list(queryWrapper);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 根据id修改理论配比
     */
    @Override
    public void updateLilunpb(Double liunpb, int id) {
        bhzLqCailiaoMapper.updateLilunpb(liunpb, id);
    }

    /**
     * 修改拌合机base表中的每盘数据的超标等级
     */
    @Override
    public void updateOver_level(Integer id, int chaobiaodengji) {
        bhzLqCailiaoMapper.updateOver_level(id, chaobiaodengji);
    }

    @Override
    public List<BhzCementDetail> selectdetail(String id) {
        return bhzLqCailiaoMapper.selectdetail(id);
    }

    @Override
    public List<BhzLqCailiao> selectByGuid(String guid) {
        QueryWrapper<BhzLqCailiao> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("base_guid", guid);
        queryWrapper.groupBy("cailiaono");
        return bhzLqCailiaoMapper.selectList(queryWrapper);
    }

    @Override
    public List<BhzLqCailiao> getcailiaoList(String chuliaoshijian_begin, String chuliaoshijian_end, String hunheliaoid, String projectName, String shebeibianhao) {
        return bhzLqCailiaoMapper.getcailiaoList(chuliaoshijian_begin,chuliaoshijian_end,hunheliaoid,projectName,shebeibianhao);
    }
}
