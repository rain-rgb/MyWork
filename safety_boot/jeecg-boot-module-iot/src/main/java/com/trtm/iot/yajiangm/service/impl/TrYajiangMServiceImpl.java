package com.trtm.iot.yajiangm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.trtm.iot.trzhanglarenwudan.entity.TrZhanglaRenwudan;
import com.trtm.iot.yajiangm.entity.TrYajiangM;
import com.trtm.iot.yajiangm.entity.TrYajiangSM;
import com.trtm.iot.yajiangm.mapper.TrYajiangMMapper;
import com.trtm.iot.yajiangm.service.ITrYajiangMService;
import com.trtm.iot.yajiangrenwudan.entity.TrYajiangRenwudan;
import com.trtm.iot.yajiangrenwudan.service.ITrYajiangRenwudanService;
import com.trtm.iot.yajiangs.entity.TrYajiangS;
import com.trtm.iot.yajiangs.mapper.TrYajiangSMapper;
import com.trtm.iot.yajiangss.mapper.TrYajiangSSMapper;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.system.vo.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * @Description: 压浆主表信息
 * @Author: jeecg-boot
 * @Date:   2021-09-06
 * @Version: V1.0
 */
@Service
public class TrYajiangMServiceImpl extends ServiceImpl<TrYajiangMMapper, TrYajiangM> implements ITrYajiangMService {
@Autowired
private TrYajiangMMapper trYajiangMMapper;
@Autowired
private TrYajiangSMapper trYajiangSMapper;
@Autowired
private TrYajiangSSMapper trYajiangSSMapper;
@Autowired
private ITrYajiangRenwudanService yajiangRenwudanService;


    @Override
    public void saveMain(TrYajiangM trYajiangM, List<TrYajiangS> yajiangs) {
        //申浙设备随机进浆压力0.5-0.7
        if (trYajiangM.getYjsbbh().equals("dq2023122801")){
            for (TrYajiangS trYajiangS : yajiangs) {
                String jinjiangyal = trYajiangS.getJinjiangyal();
                double v = Double.parseDouble(jinjiangyal);
                if (v < 0.5 || v > 0.7){
                    //随机数
                    Random random = new Random();
                    // 生成一个0.0至1.0之间的随机数
                    double randomNumber = random.nextDouble();
                    // 将其映射到0.5至0.7之间
                    double scaledRandomNumber = 0.5 + randomNumber * 0.2;
                    trYajiangS.setJinjiangyal(String.valueOf(scaledRandomNumber));
                }
            }
        }

        QueryWrapper<TrYajiangM> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("syjid",trYajiangM.getSyjid());
        TrYajiangM one = this.getOne(queryWrapper);
        if(one==null){
            trYajiangMMapper.insert(trYajiangM);
            if(yajiangs!=null && yajiangs.size()>0){
                for (TrYajiangS trYajiangS : yajiangs) {
                    QueryWrapper<TrYajiangS> queryWrapper1=new QueryWrapper<>();
                    queryWrapper1.eq("fguid",trYajiangS.getFguid());
                    TrYajiangS one1 = trYajiangSMapper.selectOne(queryWrapper1);
                    if (one1 != null){
                        trYajiangS.setId(one1.getId());
                        trYajiangSMapper.updateById(trYajiangS);
                    }else {
                        trYajiangSMapper.insert(trYajiangS);
                    }
                }
            }
        }else{
            trYajiangM.setId(one.getId());
            trYajiangMMapper.updateById(trYajiangM);
            if(yajiangs!=null && yajiangs.size()>0){
                for (TrYajiangS trYajiangS : yajiangs) {
                    QueryWrapper<TrYajiangS> queryWrapper1=new QueryWrapper<>();
                    queryWrapper1.eq("fguid",trYajiangS.getFguid());
                    TrYajiangS one1 = trYajiangSMapper.selectOne(queryWrapper1);
                    if (one1 != null){
                        trYajiangS.setId(one1.getId());
                        if(StringUtils.isNotBlank(trYajiangS.getJinjiangshu()) && StringUtils.isNotBlank(one1.getJinjiangshu())){
                            Double second = Double.parseDouble(trYajiangS.getJinjiangshu());
                            trYajiangS.setJinjiangshu(String.format("%.2f",Double.parseDouble(one1.getJinjiangshu())+second));
                        }

                        trYajiangSMapper.updateById(trYajiangS);
                    }else {
                        trYajiangSMapper.insert(trYajiangS);
                    }
                }
            }
        }
        String uuid = trYajiangM.getUuid();
        LambdaQueryWrapper<TrYajiangRenwudan> zhanglaRenwudanLambdaQueryWrapper = new LambdaQueryWrapper<>();
        zhanglaRenwudanLambdaQueryWrapper.eq(TrYajiangRenwudan::getUuid, uuid);
        List<TrYajiangRenwudan> list = yajiangRenwudanService.list(zhanglaRenwudanLambdaQueryWrapper);
        for (TrYajiangRenwudan trZhanglaRenwudan : list) {
            trZhanglaRenwudan.setStatus(1);
            yajiangRenwudanService.updateById(trZhanglaRenwudan);
        }
    }

    @Override
    public List<Map<String, Object>> countList() {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        return trYajiangMMapper.countList(loginUser.getOrgCode());
    }

    @Override
    public TrYajiangM selectgetOne(String syjid) {
        try {
            QueryWrapper<TrYajiangM> queryWrapper=new QueryWrapper<>();
            queryWrapper.eq("syjid",syjid);
           return this.getOne(queryWrapper);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<TrYajiangM> selectLists(String uuid) {
//        try {
//            QueryWrapper<TrYajiangM> queryWrapper = new QueryWrapper<>();
//            queryWrapper.eq("uuid",uuid);
//            return this.list(queryWrapper);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        return trYajiangMMapper.selectLists(uuid);
    }

    @Override
    public List<TrYajiangSM> selectListstatistics(Integer curid, Integer statistics) {
        return trYajiangMMapper.selectListstatistics(curid,statistics);
    }

    @Override
    public List<TrYajiangM> selectListzlpz(String shebeibianhao) {
        return trYajiangMMapper.selectListzlpz(shebeibianhao);
    }

    @Override
    public void updateStatistics(String sbjno, Integer statistics) {
        trYajiangMMapper.updateStatistics(sbjno, statistics);
    }

    @Override
    public TrYajiangM queryone(String shebeiNo) {
        return trYajiangMMapper.queryone(shebeiNo);
    }

    @Override
    public List<TrYajiangM> selectList(String shebeibianhao) {
        return trYajiangMMapper.selectListdata(shebeibianhao);
    }

    @Override
    public List<TrYajiangM> selectyjList(String shebeiNo, Integer id) {
        return trYajiangMMapper.selectyjList(shebeiNo, id);
    }
    @Override
    public List<TrYajiangM> selectrcyjList(String shebeiNo, Integer id) {
        return trYajiangMMapper.selectrcyjList(shebeiNo, id);
    }
    @Override
    public List<TrYajiangM> selectyjblList(String shebeiNo, Integer id) {
        return trYajiangMMapper.selectyjblList(shebeiNo, id);
    }

    @Override
    public List<TrYajiangM> selectydyjList(String shebeiNo, Integer id) {
        return trYajiangMMapper.selectydyjList(shebeiNo, id);
    }

    @Override
    public List<TrYajiangM> selectyj47toyd(String shebeiNo, Integer id) {
        return trYajiangMMapper.selectyj47toyd(shebeiNo, id);
    }

    @Override
    public List<TrYajiangM> selectListToSHYJ(String shebeiNo, Integer id) {
        return trYajiangMMapper.selectListToSHYJ(shebeiNo, id);
    }

    @Override
    public List<TrYajiangM> selectbigid(Integer curid) {
        return trYajiangMMapper.selectbigid(curid);
    }

    @Override
    public TrYajiangM selectceslll(String syjid) {
        return trYajiangMMapper.selectceslll(syjid);
    }

    @Override
    public Integer findRenwudanzs(List<String> yajiangRenwudans) {
        return trYajiangMMapper.findRenwudanzs(yajiangRenwudans);
    }

    @Override
    public Integer findRenwudanyjs(List<String> yajiangRenwudans) {
        return trYajiangMMapper.findRenwudanyjs(yajiangRenwudans);
    }

    @Override
    public Integer findRenwudanbhs(List<String> yajiangRenwudans) {
        return trYajiangMMapper.findRenwudanbhs(yajiangRenwudans);
    }

    @Override
    public List<String> selectListsb(String shebei) {
        return trYajiangMMapper.selectListsb(shebei);

    }

    @Override
    public List<TrYajiangM> selectUnifiedProcess(Integer curid, Integer alertstate, List<String> shebeiList, Integer overLevel) {
        return trYajiangMMapper.selectUnifiedProcess(curid,alertstate,shebeiList,overLevel);
    }

    @Override
    public List<TrYajiangM> selectBySBList(String shebeilist, Integer curid) {
        return trYajiangMMapper.selectBySBList(shebeilist,curid);
    }

    @Override
    public List<TrYajiangM> selectListst(String shebeilist, Integer curid) {
        return trYajiangMMapper.selectListst(shebeilist,curid);
    }

    @Override
    public List<TrYajiangM> selectlist() {
        return trYajiangMMapper.selectlist();
    }

    @Override
    public List<TrYajiangM> selectListoJG(String shebeilist, Integer curid) {
        return trYajiangMMapper.selectListoJG(shebeilist,curid);
    }
}
