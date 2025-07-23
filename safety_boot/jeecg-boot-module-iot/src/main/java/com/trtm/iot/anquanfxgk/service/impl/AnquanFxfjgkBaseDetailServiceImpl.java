package com.trtm.iot.anquanfxgk.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.aiwarnmsgs.entity.AiWarnMsgs;
import com.trtm.iot.anquanfxgk.entity.AnquanFxfjgkBaseDetail;
import com.trtm.iot.anquanfxgk.mapper.AnquanFxfjgkBaseDetailMapper;
import com.trtm.iot.anquanfxgk.service.IAnquanFxfjgkBaseDetailService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 * @Description: anquan_fxfjgk_base_detail
 * @Author: jeecg-boot
 * @Date:   2024-06-11
 * @Version: V1.0
 */
@Service
public class AnquanFxfjgkBaseDetailServiceImpl extends ServiceImpl<AnquanFxfjgkBaseDetailMapper, AnquanFxfjgkBaseDetail> implements IAnquanFxfjgkBaseDetailService {

    @Override
    public List<AnquanFxfjgkBaseDetail> getDetailList(String guid) {
        try {
            QueryWrapper<AnquanFxfjgkBaseDetail> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("base_guid", guid);
            return this.list(queryWrapper);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
