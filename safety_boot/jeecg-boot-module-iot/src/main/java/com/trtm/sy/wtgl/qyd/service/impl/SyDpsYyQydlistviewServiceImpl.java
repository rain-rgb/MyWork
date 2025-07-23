package com.trtm.sy.wtgl.qyd.service.impl;


import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.trtm.sy.wtgl.qyd.entity.SyDpsYyQydlistview;
import com.trtm.sy.wtgl.qyd.entity.response.YpdEntity;
import com.trtm.sy.wtgl.qyd.mapper.SyDpsYyQydlistviewMapper;
import com.trtm.sy.wtgl.qyd.service.ISyDpsYyQydlistviewService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;
import java.util.Map;

/**
 * @Description: sy_dps_yy_qydlistview
 * @Author: jeecg-boot
 * @Date:   2023-08-25
 * @Version: V1.0
 */
@Service
public class SyDpsYyQydlistviewServiceImpl extends ServiceImpl<SyDpsYyQydlistviewMapper, SyDpsYyQydlistview> implements ISyDpsYyQydlistviewService {

    @Override
    public YpdEntity queryById(String id) {
        Integer wztzid = Integer.valueOf(id);
        YpdEntity qyd = this.baseMapper.getQyXxById(wztzid);
        Integer quyangdanid = qyd.getQuyangdanid();
        List<Map<String, Object>> qyPic = this.baseMapper.queryQyPic(quyangdanid);
        List<Map<String, Object>> syPic = this.baseMapper.querySyPic(quyangdanid);
        qyd.setQyPic(qyPic);
        qyd.setSyPic(syPic);
        return qyd;
    }
}
