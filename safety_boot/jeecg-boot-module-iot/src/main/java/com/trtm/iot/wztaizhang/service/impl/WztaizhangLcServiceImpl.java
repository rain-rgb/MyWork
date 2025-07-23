package com.trtm.iot.wztaizhang.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.wztaizhang.entity.WztaizhangLc;
import com.trtm.iot.wztaizhang.mapper.WztaizhangLcMapper;
import com.trtm.iot.wztaizhang.service.IWztaizhangLcService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.Date;

/**
 * @Description: wztaizhang_lc
 * @Author: jeecg-boot
 * @Date:   2023-12-04
 * @Version: V1.0
 */
@Service
public class WztaizhangLcServiceImpl extends ServiceImpl<WztaizhangLcMapper, WztaizhangLc> implements IWztaizhangLcService {

    @Override
    public boolean saveOrUpdateLC(String pici, String lcbh, Double incount , String jctime,String cailiaono,String gongyingshang,String sbbh,String sysorgcode ){
        QueryWrapper<WztaizhangLc> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("pici",pici);
        queryWrapper.eq("liaocangno",lcbh);
        WztaizhangLc one = this.getOne(queryWrapper);

        if( null == one){
            one = new WztaizhangLc();
            one.setIncount( String.format("%.2f", incount));
            one.setPici(pici);
            one.setLiaocangno(lcbh);
            one.setJctime(jctime);
            one.setCailiaono(cailiaono);
            one.setGongyingshang(gongyingshang);
            one.setShebeino(sbbh);
            one.setSysOrgCode(sysorgcode);
        }else{
            incount = incount+ Double.parseDouble(one.getIncount());
            one.setIncount( String.format("%.2f", incount));

        }

        return this.saveOrUpdate(one);
    }

}
