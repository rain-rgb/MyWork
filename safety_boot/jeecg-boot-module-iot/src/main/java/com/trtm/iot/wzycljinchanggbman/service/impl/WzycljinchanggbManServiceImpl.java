package com.trtm.iot.wzycljinchanggbman.service.impl;

import com.trtm.iot.wzliaocang.mapper.WzliaocangMapper;
import com.trtm.iot.wzycljinchanggbman.entity.WzycljinchanggbMan;
import com.trtm.iot.wzycljinchanggbman.mapper.WzycljinchanggbManMapper;
import com.trtm.iot.wzycljinchanggbman.service.IWzycljinchanggbManService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

import static org.jeecg.common.util.DateUtils.getTimestampten;

/**
 * @Description: wzycljinchanggb_man
 * @Author: jeecg-boot
 * @Date:   2022-08-08
 * @Version: V1.0
 */
@Service
public class WzycljinchanggbManServiceImpl extends ServiceImpl<WzycljinchanggbManMapper, WzycljinchanggbMan> implements IWzycljinchanggbManService {
    @Autowired
    WzycljinchanggbManMapper wzycljinchanggbManMapper;

    @Autowired
    WzliaocangMapper wzliaocangMapper;

    @Override
    public List<WzycljinchanggbMan> selectycljinchangList(Integer curid, int taizhangtj) {
        return wzycljinchanggbManMapper.selectycljinchangList(curid,taizhangtj);
    }

    @Override
    public void updateistongji(Integer id, Integer taizhangtj) {
        wzycljinchanggbManMapper.updateistongji(id, taizhangtj);
    }

    @Override
    public Integer getJYStatus(Integer taizhangid) {
        return wzycljinchanggbManMapper.getJYStatus(taizhangid);
    }

    @Override
    public List<String> getCailiaoByNodetype(String nodetype) {
        return wzycljinchanggbManMapper.getCailiaoByNodetype(nodetype);
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public Boolean saveListWzycljinchanggb(List<WzycljinchanggbMan> list) {
        Integer ts = getTimestampten();//获取当前系统的时间戳（10位）
        for (WzycljinchanggbMan i:list){
            String uuid = UUID.randomUUID().toString();//随机生成唯一码UUID
            i.setGuid(uuid);
            i.setTs(ts);
            i.setIsdel("0");
            save(i);
            System.out.println("i.getJingzhong() = " + i.getJingzhong());
            Double num= Double.valueOf(i.getJingzhong());
            wzliaocangMapper.updateWzliaocangljshiyongByGuid(num,i.getLcbianhao());
        }
        return true;
    }
}
