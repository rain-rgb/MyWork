package com.trtm.iot.wzyclchuchanggbman.service.impl;

import com.trtm.iot.wzyclchuchanggbman.entity.WzyclchuchanggbMan;
import com.trtm.iot.wzyclchuchanggbman.mapper.WzyclchuchanggbManMapper;
import com.trtm.iot.wzyclchuchanggbman.service.IWzyclchuchanggbManService;
import com.trtm.iot.yclcl.entity.Wzyclchuchanggb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

import static org.jeecg.common.util.DateUtils.getTimestampten;

/**
 * @Description: wzyclchuchanggb_man
 * @Author: jeecg-boot
 * @Date:   2023-12-04
 * @Version: V1.0
 */
@Service
public class WzyclchuchanggbManServiceImpl extends ServiceImpl<WzyclchuchanggbManMapper, WzyclchuchanggbMan> implements IWzyclchuchanggbManService {

    @Autowired
    private WzyclchuchanggbManMapper wzyclchuchanggbManMapper;

    @Override
    @Transactional(rollbackOn = Exception.class)
    public Boolean saveListWzycljinchanggb(List<WzyclchuchanggbMan> list) {
        Integer ts = getTimestampten();//获取当前系统的时间戳（10位）
        for (WzyclchuchanggbMan i:list){
            String uuid = UUID.randomUUID().toString();//随机生成唯一码UUID
            i.setGuid(uuid);
            i.setTs(ts);
            i.setIsdel("0");
            save(i);
            //添加到出场纪录后,
            System.out.println("i.getJingzhong() = " + i.getJingzhong());
            Double num= Double.valueOf(i.getJingzhong());
            //更新仓库表的累计使用
            wzyclchuchanggbManMapper.updateWzliaocangljshiyongByGuid(num,i.getLcbianhao());
            //更新台账的使用量usenum

            wzyclchuchanggbManMapper.updateUseNumByPici(num,i.getPici());
        }
        return true;
    }
}
