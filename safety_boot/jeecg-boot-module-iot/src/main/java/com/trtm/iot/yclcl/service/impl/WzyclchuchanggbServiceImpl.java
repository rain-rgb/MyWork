package com.trtm.iot.yclcl.service.impl;

import com.trtm.iot.wztaizhang.mapper.WztaizhangMapper;
import com.trtm.iot.wzycljinchanggbman.entity.WzycljinchanggbMan;
import com.trtm.iot.yclcl.entity.Wzyclchuchanggb;
import com.trtm.iot.yclcl.mapper.WzyclchuchanggbMapper;
import com.trtm.iot.yclcl.service.IWzyclchuchanggbService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

import static org.jeecg.common.util.DateUtils.getTimestampten;

/**
 * @Description: wzyclchuchanggb
 * @Author: jeecg-boot
 * @Date:   2021-05-26
 * @Version: V1.0
 */
@Service
public class WzyclchuchanggbServiceImpl extends ServiceImpl<WzyclchuchanggbMapper, Wzyclchuchanggb> implements IWzyclchuchanggbService {
    @Autowired
    WzyclchuchanggbMapper wzyclchuchanggbMapper;

    @Autowired
    private WztaizhangMapper wztaizhangMapper;

    @Override
    public List<Wzyclchuchanggb> selecones(int istongji, Integer curid) {
        return wzyclchuchanggbMapper.selecones(istongji,curid);
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public Boolean saveListWzycljinchanggb(List<Wzyclchuchanggb> list) {
        Integer ts = getTimestampten();//获取当前系统的时间戳（10位）
        for (Wzyclchuchanggb i:list){
            String uuid = UUID.randomUUID().toString();//随机生成唯一码UUID
            i.setGuid(uuid);
            i.setTs(ts);
            i.setIsdel("0");
            save(i);
            //添加到出场纪录后,
            System.out.println("i.getJingzhong() = " + i.getJingzhong());
            Double num= Double.valueOf(i.getJingzhong());
            //更新仓库表的累计使用
            wzyclchuchanggbMapper.updateWzliaocangljshiyongByGuid(num,i.getLcbianhao());
            //更新台账的使用量usenum

            wztaizhangMapper.updateUseNumByPici(num,i.getPici());
        }
        return true;
    }

    @Override
    public List<Wzyclchuchanggb> slistrqid(String shebeilist, Integer curid) {
        return  wzyclchuchanggbMapper.slistrqid(shebeilist,curid);
    }
}
