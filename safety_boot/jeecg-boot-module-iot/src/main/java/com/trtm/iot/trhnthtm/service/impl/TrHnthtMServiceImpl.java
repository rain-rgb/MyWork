package com.trtm.iot.trhnthtm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.hnthtconsign.entity.HnthtConsign;
import com.trtm.iot.hnthtconsign.service.IHnthtConsignService;
import com.trtm.iot.trhnthtm.entity.TrHnthtM;
import com.trtm.iot.trhnthtm.mapper.TrHnthtMMapper;
import com.trtm.iot.trhnthtm.service.ITrHnthtMService;
import com.trtm.iot.trhnthts.entity.TrHnthtS;
import com.trtm.iot.trhnthts.mapper.TrHnthtSMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 * @Description: 混凝土回弹主表
 * @Author: jeecg-boot
 * @Date: 2021-09-13
 * @Version: V1.0
 */
@Service
public class TrHnthtMServiceImpl extends ServiceImpl<TrHnthtMMapper, TrHnthtM> implements ITrHnthtMService {

    @Autowired
    private TrHnthtMMapper trHnthtMMapper;
    @Autowired
    private TrHnthtSMapper trHnthtSMapper;
    @Autowired
    private IHnthtConsignService hnthtConsignService;

    @Override
    public int saveMain(TrHnthtM trHnthtM, List<TrHnthtS> trHnthtS) {
        Integer code = 0;
        LambdaQueryWrapper<TrHnthtM> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(TrHnthtM::getTestid, trHnthtM.getTestid())
                .or().
                eq(TrHnthtM::getCode, trHnthtM.getCode());
        TrHnthtM one = this.getOne(lambdaQueryWrapper);

        //判断是否传shebeiNo
        String shebeiNo = trHnthtM.getShebeiNo();
        //shebeiNo为空取任务单中的shebeiNo
        if (shebeiNo == null){
            LambdaQueryWrapper<HnthtConsign> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(HnthtConsign::getUuid, trHnthtM.getCode());
            List<HnthtConsign> list1 = hnthtConsignService.list(queryWrapper);
            if (list1.size() > 0) {
                shebeiNo = list1.get(0).getShebeibianhao();
                trHnthtM.setShebeiNo(shebeiNo);
            }
        }

        try {
            if (one == null) {
                trHnthtMMapper.insert(trHnthtM);
                code = 200;
            } else {
                trHnthtM.setIstuisong(0);
                trHnthtMMapper.update(trHnthtM, lambdaQueryWrapper);
                LambdaQueryWrapper<TrHnthtS> deleteWrapper = new LambdaQueryWrapper<>();
                deleteWrapper.eq(TrHnthtS::getTestid, one.getTestid());
                trHnthtSMapper.delete(deleteWrapper);
                code = 300;
            }
            if (trHnthtS != null && trHnthtS.size() > 0) {
                for (TrHnthtS entity : trHnthtS) {
                    //外键设置
                    entity.setTestid(trHnthtM.getTestid());
                    trHnthtSMapper.insert(entity);
                }
            }
        } catch (Exception e) {
            code = 400;
        }
        return code;
    }

    @Override
    public List<TrHnthtM> selectHntHtList(String shebeiNo, Integer id) {
        return trHnthtMMapper.selectHntHtList(shebeiNo, id);
    }

    @Override
    public List<TrHnthtM> selectHntHtsyList(String shebeiNo, Integer id) {
        return trHnthtMMapper.selectHntHtsyList(shebeiNo, id);
    }

    @Override
    public List<TrHnthtM> selectHntHtLists(List<String> shebeiNo, Integer id) {
        try {
            QueryWrapper<TrHnthtM> queryWrapper = new QueryWrapper<>();
            queryWrapper.gt("id", id);
            queryWrapper.in("shebei_no", shebeiNo);
            return this.list(queryWrapper);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<TrHnthtM> selectList(Integer curid, String shebeilist) {
        return trHnthtMMapper.selecthntList(curid, shebeilist);
    }
}
