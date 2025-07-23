package com.trtm.iot.hnthtconsign.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.hnthtconsign.entity.HnthtConsign;
import com.trtm.iot.hnthtconsign.mapper.HnthtConsignMapper;
import com.trtm.iot.hnthtconsign.service.IHnthtConsignService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 * @Description: 检测试验任务单下发信息表
 * @Author: jeecg-boot
 * @Date:   2021-07-09
 * @Version: V1.0
 */
@Service
public class HnthtConsignServiceImpl extends ServiceImpl<HnthtConsignMapper, HnthtConsign> implements IHnthtConsignService {

    @Override
    public List<HnthtConsign> queryones(String shebeichangjia, String shebeibianhao, String status) {
        try {
            QueryWrapper<HnthtConsign> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("shebeichangjia",shebeichangjia);
            queryWrapper.eq("shebeibianhao",shebeibianhao);
            queryWrapper.eq("status",status);
            return this.list(queryWrapper);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public HnthtConsign queryone(String code) {
        try {
            QueryWrapper<HnthtConsign> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("uuid",code);
            queryWrapper.orderByDesc("id");
            queryWrapper.last("limit 1");
            return this.getOne(queryWrapper);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<HnthtConsign> selectlist(List<String> shebeiNo, Integer id) {
        try {
            QueryWrapper<HnthtConsign> queryWrapper = new QueryWrapper<>();
            queryWrapper.gt("id", id);
            queryWrapper.in("shebeibianhao", shebeiNo);
            return this.list(queryWrapper);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<HnthtConsign> selectPushList(String shebeiNo, Integer pushStatus) {

        return this.baseMapper.selectPushList(shebeiNo,pushStatus);

    }

    @Override
    public void updatePushStatus(HnthtConsign hnthtConsign) {
        hnthtConsign.setPushStatus(1);
        updateById(hnthtConsign);
    }

    @Override
    public void updateStatus(String uuid) {
        LambdaQueryWrapper<HnthtConsign> hnthtConsignLambdaQueryWrapper = new LambdaQueryWrapper<>();
        hnthtConsignLambdaQueryWrapper.eq(HnthtConsign::getUuid,uuid);
        HnthtConsign one = this.getOne(hnthtConsignLambdaQueryWrapper);

        if (one != null) {
            one.setStatus("1");
            updateById(one);
        }
    }
}
