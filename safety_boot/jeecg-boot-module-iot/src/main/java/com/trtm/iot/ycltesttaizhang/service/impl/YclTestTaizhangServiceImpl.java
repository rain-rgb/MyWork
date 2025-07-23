package com.trtm.iot.ycltesttaizhang.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.wzcailiaonamedict.entity.Wzcailiaonamedict;
import com.trtm.iot.wzcailiaonamedict.service.IWzcailiaonamedictService;
import com.trtm.iot.wzcailiaonamedictman.entity.WzcailiaonamedictMan;
import com.trtm.iot.wzcailiaonamedictman.service.IWzcailiaonamedictManService;
import com.trtm.iot.wzgongyingshang.entity.Wzgongyingshang;
import com.trtm.iot.wzgongyingshang.service.IWzgongyingshangService;
import com.trtm.iot.wzgongyingshangman.entity.WzgongyingshangMan;
import com.trtm.iot.wzgongyingshangman.service.impl.WzgongyingshangManServiceImpl;
import com.trtm.iot.ycltesttaizhang.entity.YclTestTaizhang;
import com.trtm.iot.ycltesttaizhang.mapper.YclTestTaizhangMapper;
import com.trtm.iot.ycltesttaizhang.service.IYclTestTaizhangService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 * @Description: ycl_test_taizhang
 * @Author: jeecg-boot
 * @Date: 2023-05-15
 * @Version: V1.0
 */
@Service
public class YclTestTaizhangServiceImpl extends ServiceImpl<YclTestTaizhangMapper, YclTestTaizhang> implements IYclTestTaizhangService {

    @Autowired
    private YclTestTaizhangMapper yclTestTaiZhangMapper;
    @Autowired
    private IWzcailiaonamedictService wzcailiaonamedictService;
    @Autowired
    private IWzcailiaonamedictManService wzcailiaonamedictManService;
    @Autowired
    private IWzgongyingshangService wzgongyingshangService;
    @Autowired
    private WzgongyingshangManServiceImpl wzgongyingshangManService;

    @Override
    public List<YclTestTaizhang> getList(String time, String orgCode) {
        return yclTestTaiZhangMapper.getList(time, orgCode);
    }

    @Override
    public List<YclTestTaizhang> getHgList(String time, String orgCode) {
        return yclTestTaiZhangMapper.getHgList(time, orgCode);
    }

    @Override
    public String getCailiaoName(String cailiaono) {
        String cailiaoName = "";
        QueryWrapper<Wzcailiaonamedict> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("cailiaono", cailiaono);
        Wzcailiaonamedict wzcailiaonamedict = wzcailiaonamedictService.getOne(queryWrapper);
        if (ObjectUtil.isNotEmpty(wzcailiaonamedict)) {
            cailiaoName = wzcailiaonamedict.getCailiaoname();
        } else {
            QueryWrapper<WzcailiaonamedictMan> queryWrapper1 = new QueryWrapper<>();
            queryWrapper1.eq("cailiaono", cailiaono);
            WzcailiaonamedictMan wzcailiaonamedictMan = wzcailiaonamedictManService.getOne(queryWrapper1);
            cailiaoName = wzcailiaonamedictMan.getCailiaoname();
        }
        return cailiaoName;
    }

    @Override
    public String getGongYSName(String gongyingshangdanweibianma) {
        String gongyingshangName = "";
        QueryWrapper<Wzgongyingshang> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("guid", gongyingshangdanweibianma);
        Wzgongyingshang wzgongyingshang = wzgongyingshangService.getOne(queryWrapper);
        if (ObjectUtil.isNotEmpty(wzgongyingshang)) {
            gongyingshangName = wzgongyingshang.getGongyingshangname();
        } else {
            QueryWrapper<WzgongyingshangMan> queryWrapper1 = new QueryWrapper<>();
            queryWrapper1.eq("guid", gongyingshangdanweibianma);
            WzgongyingshangMan wzgongyingshangMan = wzgongyingshangManService.getOne(queryWrapper1);
            gongyingshangName = wzgongyingshangMan.getGongyingshangname();
        }
        return gongyingshangName;
    }

    @Override
    public void updateByPici(YclTestTaizhang yclTestTaizhang) {
        yclTestTaiZhangMapper.updateByPici(yclTestTaizhang.getCailiaoname(),yclTestTaizhang.getGuige(),yclTestTaizhang.getNodetype(),yclTestTaizhang.getGongyingshang(),
                yclTestTaizhang.getJinchangtime(),yclTestTaizhang.getCunfangplace(),yclTestTaizhang.getShuliang(),yclTestTaizhang.getUsepart(),yclTestTaizhang.getSysOrgCode(),yclTestTaizhang.getCreateTime(),
                yclTestTaizhang.getReslut(),yclTestTaizhang.getZjpdf(),yclTestTaizhang.getCjreslut(),yclTestTaizhang.getCjpdf(),yclTestTaizhang.getPici());
    }

    @Override
    public void updateZgztByPici(String zgzt,String pici) {
        yclTestTaiZhangMapper.updateZgztByPici(zgzt,pici);
    }

    @Override
    public int getBhCount(String s, String orgCode) {
        return yclTestTaiZhangMapper.getBhCount(s, orgCode);
    }
}
