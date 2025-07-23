package com.trtm.iot.anquanfxgk.service.impl;

import com.trtm.iot.anquanfxgk.entity.AnquanFxfjgkPingjia;
import com.trtm.iot.anquanfxgk.entity.AnquanFxfjgkPingjiaDetail;
import com.trtm.iot.anquanfxgk.mapper.AnquanFxfjgkPingjiaMapper;
import com.trtm.iot.anquanfxgk.service.IAnquanFxfjgkPingjiaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * @Description: anquan_fxfjgk_pingjia
 * @Author: jeecg-boot
 * @Date:   2024-09-04
 * @Version: V1.0
 */
@Service
public class AnquanFxfjgkPingjiaServiceImpl extends ServiceImpl<AnquanFxfjgkPingjiaMapper, AnquanFxfjgkPingjia> implements IAnquanFxfjgkPingjiaService {

    @Autowired
    private AnquanFxfjgkPingjiaDetailServiceImpl detailService;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Integer saveAnquanFxfjgkPingjia(AnquanFxfjgkPingjia anquanFxfjgkPingjia) {
        String uuid = UUID.randomUUID().toString();
        anquanFxfjgkPingjia.setBaseGuid(uuid);
        save(anquanFxfjgkPingjia);//添加主表
        if (anquanFxfjgkPingjia.getAnquanFxfjgkPingjiaDetails()!=null&&anquanFxfjgkPingjia.getAnquanFxfjgkPingjiaDetails().size()>0){
            anquanFxfjgkPingjia.getAnquanFxfjgkPingjiaDetails().forEach(item->item.setParentId(anquanFxfjgkPingjia.getBaseGuid()));
            detailService.saveBatch(anquanFxfjgkPingjia.getAnquanFxfjgkPingjiaDetails());
        }
        return null;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Integer updateAnquanFxfjgkPingjia(AnquanFxfjgkPingjia anquanFxfjgkPingjia) {

        updateById(anquanFxfjgkPingjia);
        if (anquanFxfjgkPingjia.getAnquanFxfjgkPingjiaDetails()!=null&&anquanFxfjgkPingjia.getAnquanFxfjgkPingjiaDetails().size()>0){
            detailService.deleteAnquanFxfjgkPingjiaDetailByParent(anquanFxfjgkPingjia.getBaseGuid());
            anquanFxfjgkPingjia.getAnquanFxfjgkPingjiaDetails().forEach(item->item.setParentId(anquanFxfjgkPingjia.getBaseGuid()));
            detailService.saveBatch(anquanFxfjgkPingjia.getAnquanFxfjgkPingjiaDetails());
        }
        return null;
    }


}
