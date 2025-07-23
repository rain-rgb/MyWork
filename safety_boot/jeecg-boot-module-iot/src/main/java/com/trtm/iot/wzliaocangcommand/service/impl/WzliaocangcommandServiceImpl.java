package com.trtm.iot.wzliaocangcommand.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.wzliaocangcommand.entity.Wzliaocangcommand;
import com.trtm.iot.wzliaocangcommand.mapper.WzliaocangcommandMapper;
import com.trtm.iot.wzliaocangcommand.service.IWzliaocangcommandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 料仓门禁表信息
 * @Author: jeecg-boot
 * @Date:   2021-07-01
 * @Version: V1.0
 */
@Service
public class WzliaocangcommandServiceImpl extends ServiceImpl<WzliaocangcommandMapper, Wzliaocangcommand> implements IWzliaocangcommandService {
    @Autowired
    WzliaocangcommandMapper wzliaocangcommandMapper;

    @Override
    public Wzliaocangcommand selectone(Integer index, String code,String sysOrgCode){
        try {
            QueryWrapper<Wzliaocangcommand> queryWrapper=new QueryWrapper<>();
            //queryWrapper.eq("Indexs",index);
            queryWrapper.like("code",code);
            queryWrapper.likeRight("sys_org_code",sysOrgCode);
            return this.getOne(queryWrapper);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  null;
    }
    @Override
    public  int updateone(Integer id,Integer index,String code){
        return wzliaocangcommandMapper.updateone(id,index,code);
    }
    @Override
    public  int updateone1(Integer id,Integer index,String code){
        return wzliaocangcommandMapper.updateone1(id,index,code);
    }
}
