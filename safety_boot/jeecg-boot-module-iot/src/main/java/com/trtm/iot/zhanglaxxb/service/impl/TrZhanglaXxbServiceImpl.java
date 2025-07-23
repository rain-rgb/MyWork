package com.trtm.iot.zhanglaxxb.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.trzhanglarenwudan.entity.TrZhanglaRenwudan;
import com.trtm.iot.trzhanglarenwudan.service.ITrZhanglaRenwudanService;
import com.trtm.iot.zhanglam.entity.TrZhanglaM;
import com.trtm.iot.zhanglam.mapper.TrZhanglaMMapper;
import com.trtm.iot.zhanglam.service.ITrZhanglaMService;
import com.trtm.iot.zhanglas.mapper.TrZhanglaSMapper;
import com.trtm.iot.zhanglass.mapper.TrZhanglaSSMapper;
import com.trtm.iot.zhanglaxxb.entity.TrZhanglaXxb;
import com.trtm.iot.zhanglaxxb.entity.TrZhanglaXxbMStatistics;
import com.trtm.iot.zhanglaxxb.mapper.TrZhanglaXxbMapper;
import com.trtm.iot.zhanglaxxb.service.ITrZhanglaXxbService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;
import java.util.Objects;

/**
 * @Description: 张拉信息表
 * @Author: jeecg-boot
 * @Date:   2021-09-01
 * @Version: V1.0
 */
@Service
public class TrZhanglaXxbServiceImpl extends ServiceImpl<TrZhanglaXxbMapper, TrZhanglaXxb> implements ITrZhanglaXxbService {

    @Autowired
    private TrZhanglaXxbMapper trZhanglaXxbMapper;
    @Autowired
    private TrZhanglaMMapper trZhanglaMMapper;
    @Autowired
    private TrZhanglaSMapper trZhanglaSMapper;
    @Autowired
    private TrZhanglaSSMapper trZhanglaSSMapper;
    @Autowired
    private ITrZhanglaXxbService trZhanglaXxbService;
    @Autowired
    private ITrZhanglaMService trZhanglaMService;
    @Autowired
    private ITrZhanglaRenwudanService zhanglaRenwudanService;

    @Override
    public List<TrZhanglaXxb> selectxxbList(String syjid) {
        try {
            QueryWrapper<TrZhanglaXxb> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("syjid",syjid);
            return this.list(queryWrapper);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void saveMain(TrZhanglaXxb trZhanglaXxb, List<TrZhanglaM> zhanglam) {
        QueryWrapper<TrZhanglaXxb> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("syjid",trZhanglaXxb.getSyjid());
        TrZhanglaXxb one = trZhanglaXxbService.getOne(queryWrapper);
        if(one==null){
            Integer hegenum = 1;
            if(zhanglam!=null && zhanglam.size()>0){
                for (TrZhanglaM trZhanglaM : zhanglam) {
                    String hege = trZhanglaM.getHege();
                    if ("0".equals(hege) || "不合格".equals(hege)) {
                        trZhanglaM.setOverLevel(1);
                        hegenum = 0;
                    }
                    QueryWrapper<TrZhanglaM> queryWrapper1 =new QueryWrapper<>();
                    queryWrapper1.eq("fguid",trZhanglaM.getFguid());
                    TrZhanglaM one1 = trZhanglaMService.getOne(queryWrapper1);
                    if (one1 != null){
                        trZhanglaM.setId(one1.getId());
                        trZhanglaMMapper.updateById(trZhanglaM);
                    }else {
                        trZhanglaMMapper.insert(trZhanglaM);
                    }
                }
            }
            trZhanglaXxb.setHege(String.valueOf(hegenum));
            trZhanglaXxbMapper.insert(trZhanglaXxb);
        }else{
            trZhanglaXxb.setId(one.getId());
            Integer hegenum = 1;
            if(zhanglam!=null && zhanglam.size()>0){
                for (TrZhanglaM trZhanglaM : zhanglam) {
                    String hege = trZhanglaM.getHege();
                    if ("0".equals(hege) || "不合格".equals(hege)) {
                        trZhanglaM.setOverLevel(1);
                        hegenum = 0;
                    }
                    QueryWrapper<TrZhanglaM> queryWrapper1 =new QueryWrapper<>();
                    queryWrapper1.eq("fguid",trZhanglaM.getFguid())
                            .or(obj -> obj.eq("SYJID", trZhanglaM.getSyjid()).eq("gsbh", trZhanglaM.getGsbh()))
                            .last("LIMIT 1");
                    TrZhanglaM one1 = trZhanglaMService.getOne(queryWrapper1);
                    if (one1 != null){
                        trZhanglaM.setId(one1.getId());
                        trZhanglaMMapper.updateById(trZhanglaM);
                    }else {
                        trZhanglaMMapper.insert(trZhanglaM);
                    }
                }
            }
            trZhanglaXxb.setHege(String.valueOf(hegenum));
            trZhanglaXxbMapper.updateById(trZhanglaXxb);
        }
        String uuid = trZhanglaXxb.getUuid();
        LambdaQueryWrapper<TrZhanglaRenwudan> zhanglaRenwudanLambdaQueryWrapper = new LambdaQueryWrapper<>();
        zhanglaRenwudanLambdaQueryWrapper.eq(TrZhanglaRenwudan::getUuid, uuid);
        List<TrZhanglaRenwudan> list = zhanglaRenwudanService.list(zhanglaRenwudanLambdaQueryWrapper);
        for (TrZhanglaRenwudan trZhanglaRenwudan : list) {
            trZhanglaRenwudan.setStatus("1");
            zhanglaRenwudanService.updateById(trZhanglaRenwudan);
        }
    }

    @Override
    public List<TrZhanglaXxb> selectLists(String uuid) {
//        try{
//            QueryWrapper<TrZhanglaXxb> queryWrapper = new QueryWrapper<>();
//            queryWrapper.eq("uuid",uuid);
//            return this.list(queryWrapper);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        return trZhanglaXxbMapper.selectLists(uuid);
    }

    @Override
    public List<TrZhanglaXxb> selectListsss(String shebeibianhao,Integer id) {
        return trZhanglaXxbMapper.selectListsss(shebeibianhao,id);
    }

    @Override
    public List<TrZhanglaM> selectListm(String shebeibianhao,Integer id) {
        return trZhanglaXxbMapper.selectListm(shebeibianhao,id);
    }

    @Override
    public List<TrZhanglaXxb> selectListshyj(String shebeiNo,Integer id) {
        return trZhanglaXxbMapper.selectListshyj(shebeiNo,id);
    }

    @Override
    public List<TrZhanglaXxb> selectListzjzl(String shebeiNo,Integer id) {
        return trZhanglaXxbMapper.selectListzjzl(shebeiNo,id);
    }

    @Override
    public List<TrZhanglaXxb> selectListbl(String shebeiNo,Integer id) {
        return trZhanglaXxbMapper.selectListbl(shebeiNo,id);
    }

    @Override
    public List<TrZhanglaXxb> selectListzlpz(String shebeibianhao) {
        return trZhanglaXxbMapper.selectListzlpz(shebeibianhao);
    }

    @Override
    public List<TrZhanglaXxb> selectzlList(String shebeiNo,Integer id) {
        return trZhanglaXxbMapper.selectzlList(shebeiNo,id);
    }

    @Override
    public List<TrZhanglaXxb> selectrczlList(String shebeiNo,Integer id) {
        return trZhanglaXxbMapper.selectrczlList(shebeiNo,id);
    }    @Override
    public List<TrZhanglaXxb> selectytwzlList(String shebeiNo,Integer id) {
        return trZhanglaXxbMapper.selectytwzlList(shebeiNo,id);
    }

    @Override
    public List<TrZhanglaXxb> selectydzlList(String shebeiNo,Integer id) {
        return trZhanglaXxbMapper.selectydzlList(shebeiNo,id);
    }

    @Override
    public List<TrZhanglaXxb> selectzl47toyd(String shebeiNo,Integer id) {
        return trZhanglaXxbMapper.selectzl47toyd(shebeiNo,id);
    }

    @Override
    public List<TrZhanglaXxbMStatistics> selectxxbListstatistics(Integer id, Integer statistics) {
        return trZhanglaXxbMapper.selectxxbListstatistics(id,statistics);
    }

    @Override
    public int updateStatistics(String shebeibianhao, Integer statistics) {
        return trZhanglaXxbMapper.updateStatistics(shebeibianhao,statistics);
    }

    @Override
    public TrZhanglaXxb queryone(String shebeiNo) {
        return trZhanglaXxbMapper.queryone(shebeiNo);
    }

    @Override
    public List<TrZhanglaXxb> selectList(String shebeibianh) {
        return trZhanglaXxbMapper.selectListdata(shebeibianh);
    }

    @Override
    public List<TrZhanglaXxb> selectListst(String shebeibianh) {
        return trZhanglaXxbMapper.selectListst(shebeibianh);
    }

    @Override
    public List<TrZhanglaXxb> selectbigid(Integer curid) {
        return trZhanglaXxbMapper.selectbigid(curid);
    }

    @Override
    public List<TrZhanglaXxb> selectbigids(Integer curid) {
        return trZhanglaXxbMapper.selectbigids(curid);
    }

    @Override
    public Integer findXiangMuZSs(List<String> querySheBeiList, String time) {
        return trZhanglaXxbMapper.findXiangMuZSs(querySheBeiList,time);
    }

    @Override
    public Integer findYuJingSs(List<String> querySheBeiList, String time) {
        return trZhanglaXxbMapper.findYuJingSs(querySheBeiList,time);
    }

    @Override
    public Integer findBiHeSs(List<String> querySheBeiList, String time) {
        return trZhanglaXxbMapper.findBiHeSs(querySheBeiList,time);
    }

    @Override
    public Integer findRenwudanzs(List<String> zhiliangrenwudanList) {
        return trZhanglaXxbMapper.findRenwudanzs(zhiliangrenwudanList);
    }

    @Override
    public Integer findRenwudanyjs(List<String> zhiliangrenwudanList) {
        return trZhanglaXxbMapper.findRenwudanyjs(zhiliangrenwudanList);
    }

    @Override
    public Integer findRenwudanbhs(List<String> zhiliangrenwudanList) {
        return trZhanglaXxbMapper.findRenwudanbhs(zhiliangrenwudanList);
    }

    @Override
    public List<TrZhanglaXxb> selectUnifiedProcess(Integer curid, Integer alertstate, List<String> shebeiList, Integer overLevel) {
        return trZhanglaXxbMapper.selectUnifiedProcess(curid,alertstate,shebeiList,overLevel);
    }

    @Override
    public List<TrZhanglaXxb> selectListss(List<String> list1, String date) {
        return trZhanglaXxbMapper.selectListss(list1,date);
    }

    @Override
    public List<TrZhanglaXxb> selectBYSBList(String shebeilist, Integer curid) {
        return trZhanglaXxbMapper.selectBYSBList(shebeilist,curid);
    }

    @Override
    public List<TrZhanglaXxb> getrcwqxzhanglaList() {
        return trZhanglaXxbMapper.getrcwqxzhanglaList();
    }

    @Override
    public List<TrZhanglaXxb> selectlist() {
        return trZhanglaXxbMapper.selectlist();
    }

    @Override
    public List<TrZhanglaXxb> selectListoJG(String shebeilist) {
        return trZhanglaXxbMapper.selectListoJG(shebeilist);
    }
}
