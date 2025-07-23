package com.trtm.iot.qrcode.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.qrcode.entity.Qrcode;
import com.trtm.iot.qrcode.mapper.QrcodeMapper;
import com.trtm.iot.qrcode.service.IQrcodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 * @Description: 二维码数据表信息
 * @Author: jeecg-boot
 * @Date:   2021-08-10
 * @Version: V1.0
 */
@Service
public class QrcodeServiceImpl extends ServiceImpl<QrcodeMapper, Qrcode> implements IQrcodeService {
    @Autowired
    QrcodeMapper qrcodeMapper;
    @Override
    public Qrcode queryGetOne(String uuid) {
        try {
            QueryWrapper<Qrcode> qrcodeQueryWrapper=new QueryWrapper<>();
            qrcodeQueryWrapper.like("uuid",uuid);
            return this.getOne(qrcodeQueryWrapper);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Qrcode getQRlistByidN(Integer id, Integer maxn, Integer minin) {
        return qrcodeMapper.getQRlistByidN(id,maxn,minin);
    }

}
