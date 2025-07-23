package com.trtm.iot.wbsquality.service.impl;

import com.trtm.iot.wbsquality.entity.WbsQuality;
import com.trtm.iot.wbsquality.entity.WbsQualityDetail;
import com.trtm.iot.wbsquality.mapper.WbsQualityDetailMapper;
import com.trtm.iot.wbsquality.service.IWbsQualityDetailService;
import com.trtm.iot.wbsquality.service.IWbsQualityService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description: wbs_quality_detail
 * @Author: jeecg-boot
 * @Date:   2024-11-27
 * @Version: V1.0
 */
@Service
public class WbsQualityDetailServiceImpl extends ServiceImpl<WbsQualityDetailMapper, WbsQualityDetail> implements IWbsQualityDetailService {

    @Autowired
    private IWbsQualityService wbsQualityService;


    @Override
    public String saveBatchBywbsid(String wbsid, String treeid, String temid) {
        List<WbsQuality> list = wbsQualityService.list();
        List<WbsQualityDetail> listdetail = new ArrayList<>();
        String msg ="";
        if( list == null){
            // 暂未关联部位质检表数据
            msg = "暂未关联改部位的质检表数据";
        }else{
            for( WbsQuality one : list ){
                WbsQualityDetail detail = new WbsQualityDetail();
                BeanUtils.copyProperties(one,detail);
                detail.setId(null);
                detail.setWbsid(wbsid);
                detail.setTreeid(treeid);
                listdetail.add(detail);
            }
            boolean b = this.saveOrUpdateBatch(listdetail);
            if(b){
                msg = "保存表数据成功";
            }else{
                msg = "保存表数据失败";
            }

        }
        return msg;
    }
}
