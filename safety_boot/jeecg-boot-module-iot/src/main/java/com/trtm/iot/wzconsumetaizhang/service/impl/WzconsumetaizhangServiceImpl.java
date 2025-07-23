package com.trtm.iot.wzconsumetaizhang.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.wzconsumetaizhang.entity.Wzconsumetaizhang;
import com.trtm.iot.wzconsumetaizhang.mapper.WzconsumetaizhangMapper;
import com.trtm.iot.wzconsumetaizhang.service.IWzconsumetaizhangService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 物资原材料消耗台账主表信息
 * @Author: jeecg-boot
 * @Date:   2021-09-07
 * @Version: V1.0
 */
@Service
public class WzconsumetaizhangServiceImpl extends ServiceImpl<WzconsumetaizhangMapper, Wzconsumetaizhang> implements IWzconsumetaizhangService {

    @Override
    public Wzconsumetaizhang selectwzxiaohao(String sysOrgCode, String projectName, String poureLocation, String strengthRank) {
        try {
            QueryWrapper<Wzconsumetaizhang> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("sys_org_code",sysOrgCode);
            queryWrapper.eq("project_name",projectName);
            queryWrapper.eq("poure_location",poureLocation);
            queryWrapper.eq("strength_rank",strengthRank);
            return this.getOne(queryWrapper);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
