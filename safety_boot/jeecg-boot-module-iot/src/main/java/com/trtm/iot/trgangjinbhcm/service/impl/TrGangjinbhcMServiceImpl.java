package com.trtm.iot.trgangjinbhcm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.trtm.iot.hnthtconsign.entity.HnthtConsign;
import com.trtm.iot.hnthtconsign.service.IHnthtConsignService;
import com.trtm.iot.trgangjinbhcm.entity.TrGangjinbhcM;
import com.trtm.iot.trgangjinbhcm.mapper.TrGangjinbhcMMapper;
import com.trtm.iot.trgangjinbhcm.service.ITrGangjinbhcMService;
import com.trtm.iot.trgangjinbhcs.entity.TrGangjinbhcS;
import com.trtm.iot.trgangjinbhcs.mapper.TrGangjinbhcSMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @Description: 钢保数据检测主表
 * @Author: jeecg-boot
 * @Date:   2021-09-10
 * @Version: V1.0
 */
@Service
public class TrGangjinbhcMServiceImpl extends ServiceImpl<TrGangjinbhcMMapper, TrGangjinbhcM> implements ITrGangjinbhcMService {

    @Autowired
    private TrGangjinbhcMMapper trGangjinbhcMMapper;
    @Autowired
    private TrGangjinbhcSMapper trGangjinbhcSMapper;
    @Autowired
    private IHnthtConsignService hnthtConsignService;



    @Override
    public int saveMain(TrGangjinbhcM trGangjinbhcM, List<TrGangjinbhcS> trGangjinbhcS) {
        Integer code=0;
        LambdaQueryWrapper<TrGangjinbhcM> lambdaQueryWrapper=new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(TrGangjinbhcM::getTestid,trGangjinbhcM.getTestid())
                .or().eq(TrGangjinbhcM::getCode,trGangjinbhcM.getCode());
        List<TrGangjinbhcM> list = this.list(lambdaQueryWrapper);
        try {
            if(list.size()>0){
                for (TrGangjinbhcM gangjinbhcM : list) {
                    String testid = gangjinbhcM.getTestid();
                    //删除等于testid子表数据与主表数据
                    trGangjinbhcSMapper.delete(new QueryWrapper<TrGangjinbhcS>().eq("testid", testid));
                    trGangjinbhcMMapper.delete(new QueryWrapper<TrGangjinbhcM>().eq("testid", testid));
                }
            }
            //判断是否传shebeiNo
            String shebeiNo = trGangjinbhcM.getShebeiNo();
            //shebeiNo为空取任务单中的shebeiNo
            if (shebeiNo == null){
                LambdaQueryWrapper<HnthtConsign> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.eq(HnthtConsign::getUuid, trGangjinbhcM.getCode());
                List<HnthtConsign> list1 = hnthtConsignService.list(queryWrapper);
                if (list1.size() > 0) {
                    shebeiNo = list1.get(0).getShebeibianhao();
                    trGangjinbhcM.setShebeiNo(shebeiNo);
                }
            }
            trGangjinbhcMMapper.insert(trGangjinbhcM);
            if(trGangjinbhcS!=null && trGangjinbhcS.size()>0) {
                for(TrGangjinbhcS entity:trGangjinbhcS) {
                    //外键设置
                    entity.setTestid(trGangjinbhcM.getTestid());
                    trGangjinbhcSMapper.insert(entity);
                }
            }
            code=200;
        } catch (Exception e) {
            code=400;
        }
            return code;
    }

    @Override
    public List<TrGangjinbhcM> selectGangJinList(String shebeiNo, Integer id) {
        return trGangjinbhcMMapper.selectGangJinList(shebeiNo, id);
    }

    @Override
    public List<TrGangjinbhcM> selectGangJinLists(List<String> shebeiNo, Integer id) {
        try {
            QueryWrapper<TrGangjinbhcM> queryWrapper = new QueryWrapper<>();
            queryWrapper.gt("id", id);
            queryWrapper.in("shebei_no", shebeiNo);
            return this.list(queryWrapper);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Map> getgbhgl(String projectid, String targetType) {
        return trGangjinbhcMMapper.getgbhgl(projectid, targetType);
    }

    @Override
    public List<TrGangjinbhcM> selectGbList(Integer curid, String shebeilist) {
        return trGangjinbhcMMapper.selectGbList(curid,shebeilist);
    }
}
