package com.trtm.iot.wzgongyingshang.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.wzgongyingshang.entity.Wzgongyingshang;
import com.trtm.iot.wzgongyingshang.mapper.WzgongyingshangMapper;
import com.trtm.iot.wzgongyingshang.service.IWzgongyingshangService;
import com.trtm.iot.wzliaocang.entity.Wzliaocang;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;
import java.util.Map;

/**
 * @Description: 物资供应商主表
 * @Author: jeecg-boot
 * @Date:   2021-05-07
 * @Version: V1.0
 */
@Service
public class WzgongyingshangServiceImpl extends ServiceImpl<WzgongyingshangMapper, Wzgongyingshang> implements IWzgongyingshangService {

    @Autowired WzgongyingshangMapper wzgongyingshangMapper;

    @Override
    public Map selectqueryone (String orgCode){return wzgongyingshangMapper.selectqueryone(orgCode);}

    @Override
    public Wzgongyingshang selectnameone(String guid) {
        try {
            QueryWrapper<Wzgongyingshang> queryWrapper=new QueryWrapper<>();
            queryWrapper.eq("guid",guid);
            return this.getOne(queryWrapper);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    @Override
    public List<Wzgongyingshang> gyslist(String departid) {
        try {
            QueryWrapper<Wzgongyingshang> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("departId",departid);
            return this.list(queryWrapper);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Map> queryList(String sysOrgCode) {
        if (null == sysOrgCode || "".equals(sysOrgCode)) {
            sysOrgCode = "A";
        }
        return wzgongyingshangMapper.queryList(sysOrgCode);
    }

}
