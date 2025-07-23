package com.trtm.iot.wzliaocang.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.system.entity.Bhzrenwudan;
import com.trtm.iot.system.entity.ShebeiInfo;
import com.trtm.iot.wzliaocang.entity.Wzliaocang;
import com.trtm.iot.wzliaocang.mapper.WzliaocangMapper;
import com.trtm.iot.wzliaocang.service.IWzliaocangService;

import com.trtm.iot.wztaizhang.entity.Wztaizhang;
import com.trtm.iot.wztaizhang.mapper.WztaizhangMapper;
import com.trtm.iot.yclsl.entity.Wzycljinchanggb;
import org.jeecg.common.exception.JeecgBootException;
import org.jeecg.common.util.oConvertUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * @Description: 料仓配置表
 * @Author: jeecg-boot
 * @Date: 2021-05-07
 * @Version: V1.0
 */
@Service
public class WzliaocangServiceImpl extends ServiceImpl<WzliaocangMapper, Wzliaocang> implements IWzliaocangService {
    @Autowired
    private WzliaocangMapper wzliaocangMapper;
    @Autowired
    private WztaizhangMapper wztaizhangMapper;

    @Override
    public Map selectqueryone(String orgCode) {
        return wzliaocangMapper.selectqueryone(orgCode);
    }

    @Override
    public List<Map> selectkucun(Integer pageNo, Integer pageSize) {
        return wzliaocangMapper.selectkucun(pageNo, pageSize);
    }

    @Override
    public Wzliaocang queryselectone(String lcNo) {
        try {
            QueryWrapper<Wzliaocang> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("guid", lcNo);
            return this.getOne(queryWrapper);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Wzliaocang> lclistBylw(String lw) {
        try {
            QueryWrapper<Wzliaocang> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("liaoweino", lw);
            return this.list(queryWrapper);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    @Override
    public List<Wzliaocang> lclist(String departid) {
        try {
            QueryWrapper<Wzliaocang> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("departid", departid);
            return this.list(queryWrapper);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Wzliaocang> userliaocangList(String sys_org_code) {
        return wzliaocangMapper.selectList(new QueryWrapper<Wzliaocang>().like("sys_org_code", sys_org_code));
    }

    @Override
    public void updateWeightBylcNo(String lcNo, String weight) {
        Wzliaocang wzliaocang = queryselectone(lcNo);
        if (null != wzliaocang) {
            wzliaocang.setWeight(weight);
            this.updateById(wzliaocang);
        }
    }

    @Override
    public List<Wzliaocang> selectwzlcList(Integer curid, List<String> strsToList1) {
        try {
            QueryWrapper<Wzliaocang> queryWrapper = new QueryWrapper<>();
            queryWrapper.ne("id", curid);
            queryWrapper.in("sys_org_code", strsToList1);
            queryWrapper.last("limit 100");
            return this.list(queryWrapper);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Wzliaocang selectByGuid(String guid, String orgCode) {
        QueryWrapper<Wzliaocang> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name", guid);
        queryWrapper.eq("sys_org_code", orgCode);
        return baseMapper.selectOne(queryWrapper);
    }

    /**
     * 跟据组织机构id查询料仓数据 （品茗）
     * @param code 组织机构id
     * @return 料仓列表
     */
    @Override
    public List<Wzliaocang> selectByOrgCodeToPM(String code) {
        List<Integer> list = new ArrayList<>();
        list.add(6);
        list.add(7);
        list.add(8);
        list.add(9);
        QueryWrapper<Wzliaocang> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("sys_org_code", code);
        queryWrapper.in("cailiaono",list);
        return baseMapper.selectList(queryWrapper);
    }

    /**
     * 跟据组织机构id查询料仓数据 （品茗）
     * @param code 组织机构id
     * @return 料仓列表
     */
    @Override
    public List<Wzliaocang> getSandStorageDataToPM(String code) {
        List<Integer> list = new ArrayList<>();
        list.add(2);
        list.add(3);
        list.add(4);
        QueryWrapper<Wzliaocang> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("sys_org_code", code);
        queryWrapper.in("cailiaono",list);
        return baseMapper.selectList(queryWrapper);
    }

    @Override
    public List getJYP(String guid) {
        if (oConvertUtils.isEmpty(guid)) throw new JeecgBootException("没有对应检验批");
        List<Wztaizhang> list = wztaizhangMapper.selectList(new QueryWrapper<Wztaizhang>()
                .select("pici").eq("LCbianhao", guid).orderByDesc("jinchangshijian").last("limit 3"));
        return list;
    }

    @Override
    public String getSnByGuid(String guid) {
        return wzliaocangMapper.getSnByGuid(guid);
    }

}
