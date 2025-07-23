package com.trtm.iot.bhzLiaodou.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.bhzLiaodou.entity.BhzLiaodou;
import com.trtm.iot.bhzLiaodou.mapper.BhzLiaodouMapper;
import com.trtm.iot.bhzLiaodou.service.IBhzLiaodouService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: bhz_liaodou
 * @Author: jeecg-boot
 * @Date:   2023-01-29
 * @Version: V1.0
 */
@Service
public class BhzLiaodouServiceImpl extends ServiceImpl<BhzLiaodouMapper, BhzLiaodou> implements IBhzLiaodouService {
    @Autowired
    BhzLiaodouMapper bhzLiaodouMapper;
    @Override
    public BhzLiaodou selectByMaterialNo(String materialNo, String sysOrgCode) {
        try {
            QueryWrapper<BhzLiaodou> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("material_no",materialNo);
            queryWrapper.eq("sys_org_code",sysOrgCode);
            return this.getOne(queryWrapper);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }
}
