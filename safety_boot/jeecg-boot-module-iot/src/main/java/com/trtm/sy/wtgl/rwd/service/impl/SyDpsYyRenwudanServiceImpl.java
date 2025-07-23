package com.trtm.sy.wtgl.rwd.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.trtm.sy.sywt.entity.SyDpsYyXianchangjianceweituo;
import com.trtm.sy.sywt.service.ISyDpsYyXianchangjianceweituoService;
import com.trtm.sy.wtgl.qywtd.entity.SyDpsYyYuancaiquyangweituo;
import com.trtm.sy.wtgl.qywtd.service.ISyDpsYyYuancaiquyangweituoService;
import com.trtm.sy.wtgl.rwd.entity.SyDpsYyRenwudan;
import com.trtm.sy.wtgl.rwd.mapper.SyDpsYyRenwudanMapper;
import com.trtm.sy.wtgl.rwd.service.ISyDpsYyRenwudanService;
import org.jeecg.common.util.oConvertUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.UUID;

/**
 * @Description: sy_dps_yy_renwudan
 * @Author: jeecg-boot
 * @Date: 2023-08-31
 * @Version: V1.0
 */
@Service
public class SyDpsYyRenwudanServiceImpl extends ServiceImpl<SyDpsYyRenwudanMapper, SyDpsYyRenwudan> implements ISyDpsYyRenwudanService {

    @Autowired
    private ISyDpsYyXianchangjianceweituoService xianchangjianceweituoService;
    @Autowired
    private ISyDpsYyYuancaiquyangweituoService yuancaiquyangweituoService;

    @Override
    @Transactional
    public void add(SyDpsYyRenwudan syDpsYyRenwudan) {
        if ("1".equals(syDpsYyRenwudan.getWtType())) {
            SyDpsYyXianchangjianceweituo xianchangjianceweituo = xianchangjianceweituoService.getOne(new QueryWrapper<SyDpsYyXianchangjianceweituo>()
                    .lambda().eq(SyDpsYyXianchangjianceweituo::getId, syDpsYyRenwudan.getXcwtdid()));
            syDpsYyRenwudan.setRenwudanleixing(1);
            String renwudanbh = xianchangjianceweituo.getSampleno().replace("YP-", "RW-");
            String weituodanbianhao = xianchangjianceweituo.getSampleno().replace("YP-", "WT-");
            String quyangdanliushuihao = xianchangjianceweituo.getSampleno().replace("YP-", "QY-");
            syDpsYyRenwudan.setRenwudanliushuihao(renwudanbh);
            this.saveOrUpdate(syDpsYyRenwudan);
            this.baseMapper.updateSampleTable(syDpsYyRenwudan.getUserid(), renwudanbh, xianchangjianceweituo.getSampleno());
            this.baseMapper.updateReportMTable(syDpsYyRenwudan.getUserid(), xianchangjianceweituo.getSampleno());
            this.baseMapper.updateTableHeader(renwudanbh, xianchangjianceweituo.getSampleno());
        } else {
            String sampleNo = "";
            if (oConvertUtils.isEmpty(syDpsYyRenwudan.getSampleNo())) {
                SyDpsYyYuancaiquyangweituo yuancaiquyangweituo = yuancaiquyangweituoService.getOne(new QueryWrapper<SyDpsYyYuancaiquyangweituo>()
                        .lambda().eq(SyDpsYyYuancaiquyangweituo::getYuancaijinchangdengjiid, syDpsYyRenwudan.getYuancaijinchangdengjiid()));
                sampleNo = yuancaiquyangweituo.getSampleno();
                syDpsYyRenwudan.setRenwudanleixing(1);
                String renwudanbh = sampleNo.replace("YP-", "RW-");
                String weituodanbianhao = sampleNo.replace("YP-", "WT-");
                String quyangdanliushuihao = sampleNo.replace("YP-", "QY-");
                syDpsYyRenwudan.setRenwudanliushuihao(renwudanbh);
                this.saveOrUpdate(syDpsYyRenwudan);
                yuancaiquyangweituo.setZhixingzhuangtai(1);
                yuancaiquyangweituo.setWeituozhuangtai(5);
                yuancaiquyangweituo.setWeituodanbianhao(weituodanbianhao);
                yuancaiquyangweituoService.updateById(yuancaiquyangweituo);

                this.baseMapper.updateSampleTable(syDpsYyRenwudan.getJiancerenyuan(), renwudanbh, sampleNo);
                this.baseMapper.updateReportMTable(syDpsYyRenwudan.getJiancerenyuan(), sampleNo);
                this.baseMapper.updateTableHeader(renwudanbh, sampleNo);
                this.baseMapper.updateQuYangJlTable(quyangdanliushuihao, yuancaiquyangweituo.getYuancaijinchangdengjiid());
            } else {
                sampleNo = syDpsYyRenwudan.getSampleNo();
                syDpsYyRenwudan.setRenwudanleixing(1);
                String renwudanbh = sampleNo.replace("YP-", "RW-");
                syDpsYyRenwudan.setRenwudanliushuihao(renwudanbh);
                this.save(syDpsYyRenwudan);
            }
        }
    }

    @Override
    public Map<String, Object> getRwdSaveData(String yuancaijinchangdengjiId) {
        return this.baseMapper.getRwdSaveData(yuancaijinchangdengjiId);
    }

    @Override
    public Map<String, Object> getXcwtRelation(Integer xcwtdId) {
        return this.baseMapper.getXcwtRelation(xcwtdId);
    }
}
