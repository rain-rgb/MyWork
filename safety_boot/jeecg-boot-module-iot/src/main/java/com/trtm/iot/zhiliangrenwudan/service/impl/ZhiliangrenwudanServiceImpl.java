package com.trtm.iot.zhiliangrenwudan.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.bhzrecipe.entity.BhzRecipe;
import com.trtm.iot.bhzrecipe.entity.BhzRecipedetail;
import com.trtm.iot.bhzrecipe.mapper.BhzRecipeMapper;
import com.trtm.iot.bhzrecipe.mapper.BhzRecipedetailMapper;
import com.trtm.iot.zhilianggongxu.entity.ZhiliangGongxu;
import com.trtm.iot.zhilianggongxu.mapper.ZhiliangGongxuMapper;
import com.trtm.iot.zhiliangrenwudan.entity.Zhiliangrenwudan;
import com.trtm.iot.zhiliangrenwudan.mapper.ZhiliangrenwudanMapper;
import com.trtm.iot.zhiliangrenwudan.service.IZhiliangrenwudanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * @Description: 任务单（制梁）表信息
 * @Author: jeecg-boot
 * @Date:   2021-08-13
 * @Version: V1.0
 */
@Service
public class ZhiliangrenwudanServiceImpl extends ServiceImpl<ZhiliangrenwudanMapper, Zhiliangrenwudan> implements IZhiliangrenwudanService {

    @Autowired
    private ZhiliangrenwudanMapper zhiliangrenwudanMapper;
    @Autowired
    private ZhiliangGongxuMapper zhiliangGongxuMapper;
    @Override
    public List<Zhiliangrenwudan> selectone(String taizuono) {
        try {
            QueryWrapper<Zhiliangrenwudan> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("taizuono",taizuono);
            queryWrapper.eq("isdel",0);
            return this.list(queryWrapper);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 根据制梁台座编号查询
     *
     * @param taizuono
     */
    @Override
    public List<Zhiliangrenwudan> selectone1(String taizuono) {
        try {
            QueryWrapper<Zhiliangrenwudan> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("taizuono",taizuono);
            queryWrapper.eq("cunliangstatus",1);
            queryWrapper.eq("isdel",0);
            return this.list(queryWrapper);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    @Transactional
    public void saveMain(Zhiliangrenwudan zhiliangrenwudan, List<ZhiliangGongxu> zhiliangGongxuList) {
        zhiliangrenwudanMapper.insert(zhiliangrenwudan);
        if(zhiliangGongxuList!=null && zhiliangGongxuList.size()>0) {
            for(ZhiliangGongxu entity:zhiliangGongxuList) {
                //外键设置
                entity.setUuid(zhiliangrenwudan.getUuid());
                entity.setId(null);
                zhiliangGongxuMapper.insert(entity);
            }
        }
    }

    @Override
    @Transactional
    public void updateMain(Zhiliangrenwudan zhiliangrenwudan,List<ZhiliangGongxu> zhiliangGongxuList) {
        zhiliangrenwudanMapper.updateById(zhiliangrenwudan);

        //1.先删除子表数据
        zhiliangGongxuMapper.deleteByMainId(zhiliangrenwudan.getUuid());

        //2.子表数据重新插入
        if(zhiliangGongxuList!=null && zhiliangGongxuList.size()>0) {
            for(ZhiliangGongxu entity:zhiliangGongxuList) {
                //外键设置
                entity.setUuid(zhiliangrenwudan.getUuid());
                zhiliangGongxuMapper.insert(entity);
            }
        }
    }

    @Override
    public List<Zhiliangrenwudan> selectones(Integer itemValue,String sysOrgCode) {
        try {
            QueryWrapper<Zhiliangrenwudan> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("station",itemValue);
            queryWrapper.likeRight("sys_org_code",sysOrgCode);
            queryWrapper.eq("isdel",0);
            queryWrapper.eq("status",1);
            return this.list(queryWrapper);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int selectBetlevSum(String betlev) {
        return zhiliangrenwudanMapper.selectBetlevSum(betlev);
    }

    @Override
    public Zhiliangrenwudan selectuuid(String uuid) {
        try{
            QueryWrapper<Zhiliangrenwudan> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("uuid",uuid);
            return this.getOne(queryWrapper);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Zhiliangrenwudan> getzhilianglist() {
        return zhiliangrenwudanMapper.getzhilianglist();
    }

    @Override
    public Zhiliangrenwudan selectbyorgCode(String sys_depart_orgcode) {
        return zhiliangrenwudanMapper.selectbyorgCode(sys_depart_orgcode);
    }
}
