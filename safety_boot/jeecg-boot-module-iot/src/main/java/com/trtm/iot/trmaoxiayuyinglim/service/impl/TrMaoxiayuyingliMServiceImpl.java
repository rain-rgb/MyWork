package com.trtm.iot.trmaoxiayuyinglim.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.trtm.iot.bhzcfg.entity.BhzPhone;
import com.trtm.iot.bhzcfg.service.IBhzPhoneService;
import com.trtm.iot.system.entity.ShebeiInfo;
import com.trtm.iot.system.service.IShebeiInfoService;
import com.trtm.iot.trmaoxiayuyinglim.entity.TrMaoxiayuyingliM;
import com.trtm.iot.trmaoxiayuyinglim.entity.TrMaoxiayuyingliS;
import com.trtm.iot.trmaoxiayuyinglim.mapper.TrMaoxiayuyingliSMapper;
import com.trtm.iot.trmaoxiayuyinglim.mapper.TrMaoxiayuyingliMMapper;
import com.trtm.iot.trmaoxiayuyinglim.service.ITrMaoxiayuyingliMService;
import org.jeecg.common.system.message.entity.SysMessageCore;
import org.jeecg.common.system.message.service.ISysMessageCoreService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Collection;

/**
 * @Description: 锚下预应力张拉主表
 * @Author: jeecg-boot
 * @Date: 2024-03-12
 * @Version: V1.0
 */
@Service
public class TrMaoxiayuyingliMServiceImpl extends ServiceImpl<TrMaoxiayuyingliMMapper, TrMaoxiayuyingliM> implements ITrMaoxiayuyingliMService {

    @Autowired
    private TrMaoxiayuyingliMMapper trMaoxiayuyingliMMapper;
    @Autowired
    private TrMaoxiayuyingliSMapper trMaoxiayuyingliSMapper;
    @Autowired
    private IBhzPhoneService bhzPhoneService;
    @Autowired
    private ISysMessageCoreService sysMessageCoreService;
    @Autowired
    private IShebeiInfoService shebeiInfoService;

    @Override
    @Transactional
    public void saveMain(TrMaoxiayuyingliM trMaoxiayuyingliM, List<TrMaoxiayuyingliS> trMaoxiayuyingliSList) {
        int overLevelM = 0;
        if (trMaoxiayuyingliSList != null && trMaoxiayuyingliSList.size() > 0) {
            for (TrMaoxiayuyingliS entity : trMaoxiayuyingliSList) {
                String piancha = entity.getPiancha();
                //判断piancha的绝对值是否大于五
                if (Math.abs(Double.parseDouble(piancha)) > 5) {
                    entity.setOverLevel(1);
                    overLevelM = 1;
                } else {
                    entity.setOverLevel(0);
                }
                //外键设置
                entity.setUuid(String.valueOf(trMaoxiayuyingliM.getUuid()));
                trMaoxiayuyingliSMapper.insert(entity);
            }
        }
        if (overLevelM > 0) {
            try {
                smssend(trMaoxiayuyingliM.getSbbh());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        trMaoxiayuyingliM.setOverLevel(overLevelM);
        trMaoxiayuyingliMMapper.insert(trMaoxiayuyingliM);
    }

    private void smssend(String sbbh) {
        LambdaQueryWrapper<ShebeiInfo> shebeiInfoLambdaQueryWrapper = new LambdaQueryWrapper<>();
        shebeiInfoLambdaQueryWrapper.eq(ShebeiInfo::getSbjno, sbbh);
        ShebeiInfo shebeiInfo = shebeiInfoService.getOne(shebeiInfoLambdaQueryWrapper);

        BhzPhone bhzPhone = new BhzPhone();
        LambdaQueryWrapper<BhzPhone> bhzPhoneLambdaQueryWrapper = new LambdaQueryWrapper<>();
        bhzPhoneLambdaQueryWrapper.eq(BhzPhone::getSysOrgCode, shebeiInfo.getSysOrgCode())
                .eq(BhzPhone::getPhonesname, 35);

        //可能会有多条结果，使用list()方法
        List<BhzPhone> bhzPhoneList = bhzPhoneService.list(bhzPhoneLambdaQueryWrapper);
        if (!bhzPhoneList.isEmpty()) {
            bhzPhone = bhzPhoneList.get(0); // 获取第一条结果
        }
        SysMessageCore sysSms = new SysMessageCore();
        sysSms.setEsTitle("锚下预应力张拉预警");
        sysSms.setEsType("1");
        sysSms.setEsReceiver(bhzPhone.getPhones());
        com.alibaba.fastjson.JSONObject obj = new JSONObject();
        obj.put("sbname", shebeiInfo.getSbname());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date now = new Date();
        obj.put("time", sdf.format(now));
        obj.put("content", "张拉有效预应力超阈值范围");
        sysSms.setEsContent(obj.toString());
        sysSms.setEsSendStatus("0");
        sysSms.setEsSendNum(0);
        sysSms.setRemark(shebeiInfo.getSbjno());
        sysMessageCoreService.save(sysSms);
    }

    @Override
    @Transactional
    public void updateMain(TrMaoxiayuyingliM trMaoxiayuyingliM, List<TrMaoxiayuyingliS> trMaoxiayuyingliSList) {
        trMaoxiayuyingliMMapper.updateById(trMaoxiayuyingliM);

        //1.先删除子表数据
        trMaoxiayuyingliSMapper.deleteByMainId(String.valueOf(trMaoxiayuyingliM.getId()));

        //2.子表数据重新插入
        if (trMaoxiayuyingliSList != null && trMaoxiayuyingliSList.size() > 0) {
            for (TrMaoxiayuyingliS entity : trMaoxiayuyingliSList) {
                //外键设置
                entity.setUuid(String.valueOf(trMaoxiayuyingliM.getUuid()));
                trMaoxiayuyingliSMapper.insert(entity);
            }
        }
    }

    @Override
    @Transactional
    public void delMain(String id) {
        trMaoxiayuyingliSMapper.deleteByMainId(id);
        trMaoxiayuyingliMMapper.deleteById(id);
    }

    @Override
    @Transactional
    public void delBatchMain(Collection<? extends Serializable> idList) {
        for (Serializable id : idList) {
            trMaoxiayuyingliSMapper.deleteByMainId(id.toString());
            trMaoxiayuyingliMMapper.deleteById(id);
        }
    }

}
