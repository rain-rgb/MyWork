package com.trtm.iot.wzcailiaonamedict.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.rebarComponentMaterial.entity.RebarComponentMaterial;
import com.trtm.iot.wzcailiaonamedict.entity.Wzcailiaonamedict;
import com.trtm.iot.wzcailiaonamedict.mapper.WzcailiaonamedictMapper;
import com.trtm.iot.wzcailiaonamedict.service.IWzcailiaonamedictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @Description: 材料配置主表
 * @Author: jeecg-boot
 * @Date:   2021-05-07
 * @Version: V1.0
 */
@Service
public class WzcailiaonamedictServiceImpl extends ServiceImpl<WzcailiaonamedictMapper, Wzcailiaonamedict> implements IWzcailiaonamedictService {

    @Autowired
    private WzcailiaonamedictMapper WzcailiaonamedictMapper;

    @Override
    public List<Wzcailiaonamedict> arrayOnecailiaos() {
        return WzcailiaonamedictMapper.arrayOnecailiaos();
    }

    @Override
    public Map selectqueryone(String orgCode) {
        return WzcailiaonamedictMapper.selectqueryone(orgCode);
    }
    @Override
    public Wzcailiaonamedict queryselectone1(String cailiaoNo) {
        try {
            QueryWrapper<Wzcailiaonamedict> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("cailiaoNo", cailiaoNo);
            return this.getOne(queryWrapper);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    @Override
    public List<Wzcailiaonamedict> cailiaolist(String departid) {
        try {
            QueryWrapper<Wzcailiaonamedict> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("WZCaiLiaoDanWeiHuanSuan",departid);
            return this.list(queryWrapper);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    @Override
    public List<Wzcailiaonamedict> usercailiaoList(String sys_org_code) {
        return WzcailiaonamedictMapper.selectList(new QueryWrapper<Wzcailiaonamedict>().like("sys_org_code",sys_org_code));
    }

    @Override
    public List<Wzcailiaonamedict> cailiaolists(String departid, List<Integer> lmcailiaolx) {
        try {
            QueryWrapper<Wzcailiaonamedict> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("WZCaiLiaoDanWeiHuanSuan",departid);
            queryWrapper.in("lmcailiaolx",lmcailiaolx);
            return this.list(queryWrapper);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<String> queryNodetypeList1(String orgCode) {
        return WzcailiaonamedictMapper.queryNodetypeList1(orgCode);
    }

    @Override
    public List<String> queryNodetypeList2() {
        return  WzcailiaonamedictMapper.queryNodetypeList2();
    }

    @Override
    public String selectBylmcailiaolx(int i) {
        return WzcailiaonamedictMapper.selectBylmcailiaolx(i);
    }

    @Override
    public List<Map> selectByYCCP(Integer cprule, Integer ycrule, String code) {
        return WzcailiaonamedictMapper.selectByYCCP(cprule,ycrule,code);
    }

    @Override
    public List<String> selectCailiaoNoByName(String cailiaoName) {
        return WzcailiaonamedictMapper.selectCailiaoNoByName(cailiaoName);
    }

    @Override
    public List<String> selectListByYCCP(Integer cprule, Integer ycrule, String code) {
        return WzcailiaonamedictMapper.selectListByYCCP(cprule,ycrule,code);
    }



    @Override
    public List<RebarComponentMaterial> addMaterialToComponentId(String materialIds, String componentId) {
        List<String> strings = Arrays.asList(materialIds.split(","));
//        WzcailiaonamedictMapper.sa

        return null;
    }
}
