package com.trtm.iot.zhiliangqrcode.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.zhiliangqrcode.entity.Zhiliangqrcode;
import com.trtm.iot.zhiliangqrcode.mapper.ZhiliangqrcodeMapper;
import com.trtm.iot.zhiliangqrcode.service.IZhiliangqrcodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: zhiliangqrcode
 * @Author: jeecg-boot
 * @Date:   2022-08-10
 * @Version: V1.0
 */
@Service
public class ZhiliangqrcodeServiceImpl extends ServiceImpl<ZhiliangqrcodeMapper, Zhiliangqrcode> implements IZhiliangqrcodeService {

    @Autowired ZhiliangqrcodeMapper zhiliangqrcodeMapper;
    @Override
    public Zhiliangqrcode selectOne(String uuid) {
        try {
            QueryWrapper<Zhiliangqrcode> queryWrapper = new QueryWrapper<>();
            queryWrapper.like("uuid",uuid);
            return this.getOne(queryWrapper);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
