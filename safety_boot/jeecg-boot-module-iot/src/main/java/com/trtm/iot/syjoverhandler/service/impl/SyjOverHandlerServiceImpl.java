package com.trtm.iot.syjoverhandler.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.syjoverhandler.entity.SyjOverHandler;
import com.trtm.iot.syjoverhandler.mapper.SyjOverHandlerMapper;
import com.trtm.iot.syjoverhandler.service.ISyjOverHandlerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 试验机处置审核表
 * @Author: jeecg-boot
 * @Date:   2021-12-30
 * @Version: V1.0
 */
@Service
public class SyjOverHandlerServiceImpl extends ServiceImpl<SyjOverHandlerMapper, SyjOverHandler> implements ISyjOverHandlerService {

    @Autowired
    private SyjOverHandlerMapper syjOverHandlerMapper;

    @Override
    public SyjOverHandler selectOneSyj(String syjid) {
        try {
            QueryWrapper<SyjOverHandler> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("baseId", syjid);
            return this.getOne(queryWrapper);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    @Override
    public SyjOverHandler selectone(String baseId) {
        try {
            QueryWrapper<SyjOverHandler> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("baseId",baseId);
            return this.getOne(queryWrapper);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Integer syjCZAddOrUpDate(String wtyy, String clfs, String cljg, String syjbatch, String bizPath, String chuzhiren, int status) {
        String i = syjOverHandlerMapper.dataById(syjbatch);
        int result = 0;
        if (i==null){
            result = syjOverHandlerMapper.syjchuZhiAddById(wtyy,clfs,cljg,syjbatch,bizPath,chuzhiren,status);
        }else {
            result = syjOverHandlerMapper.syjchuZhiUpdateById(wtyy,clfs,cljg,syjbatch,bizPath,chuzhiren,status);
        }
        return result;
    }

    @Override
    public Integer supervisorBohui(String jlbh, String syjbatch, String people, int status) {
        String i = syjOverHandlerMapper.dataById(syjbatch);
        int result = 0;
        if (i==null){
            result = syjOverHandlerMapper.BhzShBoHuiAddById(jlbh,syjbatch,people,status);
        }else {
            result = syjOverHandlerMapper.BhzShBoHuiUpdateById(jlbh,  syjbatch, people,status);
        }
        return result;
    }

    @Override
    public Integer supervisorAddOrUpdate(String spyj, String spjg, String syjbatch, String bizPath, String shenpiren, int status) {
        String i = syjOverHandlerMapper.dataById(syjbatch);
        int result = 0;
        if (i==null){
            result = syjOverHandlerMapper.BhzSHAddById(spyj, spjg, syjbatch, bizPath,shenpiren,status);
        }else {
            result = syjOverHandlerMapper.BhzSHUpdateById(spyj, spjg, syjbatch, bizPath,shenpiren,status);
        }
        return result;
    }

    @Override
    public Integer headquartersAddOrUpdate(String zhbyj, String zhbjg, String syjbatch, String bizPath, String shenpiren, int status) {
        String i = syjOverHandlerMapper.dataById(syjbatch);
        int result = 0;
        if (i==null){
            result = syjOverHandlerMapper.BhzZHBSHAddById(zhbyj, zhbjg, syjbatch, bizPath,shenpiren,status);
        }else {
            result = syjOverHandlerMapper.BhzZHBSHUpdateById(zhbyj, zhbjg,  syjbatch, bizPath,shenpiren,status);
        }
        return result;
    }

    @Override
    public Integer headquartersBohui(String zhbbh, String syjbatch, String shenpiren, int status) {
        String i = syjOverHandlerMapper.dataById(syjbatch);
        int result = 0;
        if (i==null){
            result = syjOverHandlerMapper.BhzZHBBoHuiAddById(zhbbh, syjbatch,  shenpiren,status);
        }else {
            result = syjOverHandlerMapper.BhzZHBBoHuiUpdateById(zhbbh,  syjbatch,  shenpiren,status);
        }
        return result;
    }

    @Override
    public Boolean updateTableField(String tableName, String fieldName, int status, String syjbatch) {
        return syjOverHandlerMapper.updateTableField(tableName,fieldName,status,syjbatch);
    }


}
