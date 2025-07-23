package com.trtm.iot.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.system.entity.Shigongphb;
import com.trtm.iot.system.mapper.ShigongphbMapper;
import com.trtm.iot.system.service.IShigongphbService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 * @Description: 施工配合比
 * @Author: jeecg-boot
 * @Date:   2021-05-19
 * @Version: V1.0
 */
@Service
public class ShigongphbServiceImpl extends ServiceImpl<ShigongphbMapper, Shigongphb> implements IShigongphbService {

    @Autowired
    private ShigongphbMapper shigongphbMapper;

    @Override
    public List<Shigongphb> queryones(String code) {
        try{
            QueryWrapper<Shigongphb> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("Code",code);
            queryWrapper.eq("isdel",0);
            return this.list(queryWrapper);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Shigongphb> queryonelist(String code, String shebei, String workNo) {
        try{
            QueryWrapper<Shigongphb> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("Code",code);
            queryWrapper.eq("shebeibianhao",shebei);
            queryWrapper.eq("renwudan",workNo);
            queryWrapper.eq("isdel",0);
            return this.list(queryWrapper);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Shigongphb queryoneCode(String code) {
        try{
            QueryWrapper<Shigongphb> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("Code",code);
            return this.getOne(queryWrapper);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Shigongphb queryoneRenwudan(String renwudan) {
        try{
            QueryWrapper<Shigongphb> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("renwudan",renwudan);
            return this.getOne(queryWrapper);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Shigongphb> queryoneRenwudanbyStation(String code,String recipe, Integer station) {
        try{
            QueryWrapper<Shigongphb> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("renwudan",code);
            queryWrapper.isNull("Flag");
            queryWrapper.eq("Code",recipe);
            return this.list(queryWrapper);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    @Override
    public List<Shigongphb> selectLists(String shebeilist, Integer curid) {

        return shigongphbMapper.selectLists(shebeilist,curid);
    }
    @Override
    public List<Shigongphb> selectListsYJQS(String shebeilist, Integer curid) {
        return shigongphbMapper.selectListsYJQS(shebeilist,curid);
    }
    @Override
    public List<Shigongphb> selectListytwnd(String shebeilist, Integer curid) {
        return shigongphbMapper.selectListytwnd(shebeilist,curid);
    }

    @Override
    public List<Shigongphb> selectPhbList(Integer curid) {
        return shigongphbMapper.selectPhbList(curid);
    }

    @Override
    public List<Shigongphb> selectlistjz(Integer curid, int statistic) {
        return shigongphbMapper.selectlistjz(curid,statistic);
    }

    @Override
    public Shigongphb getByFormulaNo(String formulaNo,String device) {
        QueryWrapper<Shigongphb> qw = new QueryWrapper<>();
        qw.eq("Code",formulaNo);
        qw.eq("shebeibianhao",device);
        return baseMapper.selectOne(qw);
    }
}
