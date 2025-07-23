package com.trtm.iot.devicemixpileoneoverhandler.service.impl;

import com.trtm.iot.devicemixpileoneoverhandler.entity.MixpileOneOverHandler;
import com.trtm.iot.devicemixpileoneoverhandler.mapper.MixpileOneOverHandlerMapper;
import com.trtm.iot.devicemixpileoneoverhandler.service.IMixpileOneOverHandlerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 软基超标处理
 * @Author: jeecg-boot
 * @Date:   2021-12-31
 * @Version: V1.0
 */
@Service
public class MixpileOneOverHandlerServiceImpl extends ServiceImpl<MixpileOneOverHandlerMapper, MixpileOneOverHandler> implements IMixpileOneOverHandlerService {
    @Autowired
    private MixpileOneOverHandlerMapper mixpileOneOverHandlerMapper;

    @Override
    public Integer GZCZAddOrUpDate(String wtyy, String clfs, String cljg, String hntbatch, String bizPath, String chuzhiren, int status) {
        String i = mixpileOneOverHandlerMapper.dataById(hntbatch);
        int result = 0;
        if (i==null){
            result = mixpileOneOverHandlerMapper.GZchuZhiAddById(wtyy,clfs,cljg,hntbatch,bizPath,chuzhiren,status);
        }else {
            result = mixpileOneOverHandlerMapper.GZchuZhiUpdateById(wtyy,clfs,cljg,hntbatch,bizPath,chuzhiren,status);
        }
        return result;
    }

    @Override
    public Integer supervisorAddOrUpdate(String spyj, String spjg, String hntbatch, String bizPath, String shenpiren, int status) {
        String i = mixpileOneOverHandlerMapper.dataById(hntbatch);
        int result = 0;
        if (i==null){
            result = mixpileOneOverHandlerMapper.GZSHAddById(spyj, spjg, hntbatch, bizPath,shenpiren,status);
        }else {
            result = mixpileOneOverHandlerMapper.GZSHUpdateById(spyj, spjg, hntbatch, bizPath,shenpiren,status);
        }
        return result;
    }

    @Override
    public Integer headquartersAddOrUpdate(String zhbyj, String zhbjg, String hntbatch, String bizPath, String shenpiren, int status) {
        String i = mixpileOneOverHandlerMapper.dataById(hntbatch);
        int result = 0;
        if (i==null){
            result = mixpileOneOverHandlerMapper.GZZHBSHAddById(zhbyj, zhbjg, hntbatch, bizPath,shenpiren,status);
        }else {
            result = mixpileOneOverHandlerMapper.GZZHBSHUpdateById(zhbyj, zhbjg,  hntbatch, bizPath,shenpiren,status);
        }
        return result;
    }

    @Override
    public Integer supervisorBohui(String jlbh, String hntbatch, String people, int status) {
        String i = mixpileOneOverHandlerMapper.dataById(hntbatch);
        int result = 0;
        if (i==null){
            result = mixpileOneOverHandlerMapper.GZShBoHuiAddById(jlbh,hntbatch,people,status);
        }else {
            result = mixpileOneOverHandlerMapper.GZShBoHuiUpdateById(jlbh,  hntbatch, people,status);
        }
        return result;

    }

    @Override
    public Integer headquartersBohui(String zhbbh, String hntbatch,  String shenpiren, int status) {
        String i = mixpileOneOverHandlerMapper.dataById(hntbatch);
        int result = 0;
        if (i==null){
            result = mixpileOneOverHandlerMapper.GZZHBBoHuiAddById(zhbbh, hntbatch,  shenpiren,status);
        }else {
            result = mixpileOneOverHandlerMapper.GZZHBBoHuiUpdateById(zhbbh,  hntbatch,  shenpiren,status);
        }
        return result;
    }
}
