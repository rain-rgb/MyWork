package com.trtm.iot.bys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.bys.entity.BysReal;
import com.trtm.iot.bys.mapper.BysRealMapper;
import com.trtm.iot.bys.service.IBysRealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;
import java.util.Map;

/**
 * @Description: bys_real
 * @Author: jeecg-boot
 * @Date:   2021-03-22
 * @Version: V1.0
 */
@Service
public class BysRealServiceImpl extends ServiceImpl<BysRealMapper, BysReal> implements IBysRealService {
    @Autowired
    BysRealMapper bysRealMapper;

    @Override
    public List<BysReal> selectbysone(Integer curid, Integer alertstate) {
//        try {
//            QueryWrapper<BysReal> queryWrapper = new QueryWrapper<>();
//            queryWrapper.ge("id",curid);
//            queryWrapper.eq("alertstate",alertstate);
//            queryWrapper.last("limit 100");
//            return this.list(queryWrapper);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
        return bysRealMapper.selectbysone(curid, alertstate);
    }

    @Override
    public List<BysReal> selectbysbaselist(String shebeino) {
        try {
            QueryWrapper<BysReal> queryWrapper = new QueryWrapper<>();
            queryWrapper.ge("shebeiNo", shebeino);
            return this.list(queryWrapper);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<BysReal> selectbysbaselists(List<String> shebeino, Integer id) {
        try {
            QueryWrapper<BysReal> queryWrapper = new QueryWrapper<>();
            queryWrapper.gt("id", id);
            queryWrapper.eq("alertstate", 1);
            queryWrapper.in("shebeiNo", shebeino);
            queryWrapper.last("limit 100");
            return this.list(queryWrapper);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<BysReal> selectListBySbno(String shebeino) {
        return bysRealMapper.selectListBySbno(shebeino);
    }

    @Override
    public int updatebysone(Integer id, Integer alertstate) {
        return bysRealMapper.updatealertstate(id, alertstate);
    }

    @Override
    public int updateStatus(Integer id, Integer status) {
        return bysRealMapper.updateStatus(id, status);
    }

    @Override
    public Map stsPageLists3(String shebeis) {
        return bysRealMapper.stsPageLists3(shebeis);
    }

    @Override
    public BysReal queryone(String shebeiNo) {
        return bysRealMapper.queryone(shebeiNo);
    }

    @Override
    public List<BysReal> selectBysList(Integer curid, String shebeilist) {
        return bysRealMapper.selectBysList(curid, shebeilist);
    }

    @Override
    public List<BysReal> selectBysListkz(Integer curid, String shebeilist) {
        return bysRealMapper.selectBysListkz(curid, shebeilist);
    }

    @Override
    public BysReal selectBysbno(String shebeino, Integer curid) {
        return bysRealMapper.selectBysbno(shebeino,curid);
    }

    @Override
    public int upadteIstuisong(Integer id, int i) {

        BysReal bysReal = new BysReal();
        bysReal.setId(id);
        bysReal.setIstuisong(1);
        return bysRealMapper.updateById(bysReal);

    }

    @Override
    public List<BysReal> queryListBySheBeis(String shebei, Integer curid) {
        return bysRealMapper.queryListBySheBeis(shebei,curid);
    }

    @Override
    public List<BysReal> queryListbim(String shebei, Integer curid) {
        return bysRealMapper.queryListbim(shebei,curid);
    }

    @Override
    public BysReal selectBysbnotwo(String shebeino, Integer curid) {
        return bysRealMapper.selectBysbnotwo(shebeino,curid);

    }
}
