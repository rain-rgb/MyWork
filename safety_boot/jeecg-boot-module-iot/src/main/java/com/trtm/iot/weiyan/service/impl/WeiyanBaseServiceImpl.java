package com.trtm.iot.weiyan.service.impl;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.weiyan.entity.WeiyanBase;
import com.trtm.iot.weiyan.mapper.WeiyanBaseMapper;
import com.trtm.iot.weiyan.service.IWeiyanBaseService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.Date;
import java.util.List;

/**
 * @Description: 围岩量测主表
 * @Author: jeecg-boot
 * @Date:   2021-04-08
 * @Version: V1.0
 */
@Service
public class WeiyanBaseServiceImpl extends ServiceImpl<WeiyanBaseMapper, WeiyanBase> implements IWeiyanBaseService {
    @Autowired
    private IWeiyanBaseService weiyanBaseService;
    /**
     * 围岩终端上传
     * @param json
     * @return
     */
    public String weiyanSave(String json){
        JSONObject result = new JSONObject(true);
        try {
            //将json转换为JOSNObject数组
            JSONObject jsonObject = JSONUtil.parseObj(json);
            //将json转化为实体类对象
            WeiyanBase weiyanBase = jsonObject.toBean(WeiyanBase.class);
            if (StringUtils.isBlank(weiyanBase.getBatchNo())) {
                result.put("code", 202);
                result.put("msg", "batchNo不能为空！！");
            } else {
                //生成uuid
                Snowflake snowflake = IdUtil.createSnowflake(1, 1);
                long id = snowflake.nextId();
                weiyanBase.setId((int) id);
                WeiyanBase i = new WeiyanBase();
                i.setBatchNo(weiyanBase.getBatchNo());
                QueryWrapper<WeiyanBase> queryWrapper = new QueryWrapper<>(i);
                List<WeiyanBase> weiyanBases = weiyanBaseService.list(queryWrapper);
                if (weiyanBases.size() == 0) {
                    weiyanBase.setUploadTime(new Date());
                    String jobLocation = weiyanBase.getJobLocation();
                    //String s = DataUtils.wallRockReturnWbsId(jobLocation);
                    weiyanBase.setJobLocation(jobLocation);
                    weiyanBaseService.save(weiyanBase);
                    result.put("code", 200);
                    result.put("msg", "请求成功");
                    result.put("batchNo",weiyanBase.getBatchNo());
                } else {
                    result.put("code", 201);
                    result.put("msg", "数据重复");
                    result.put("batchNo",weiyanBase.getBatchNo());
                }
            }
        } catch (Exception ex) {
            result.put("code", 200);
            result.put("msg", "请求失败");
            log.error("终端数据"+ json+"异常信息"+ ex);
            throw ex;
        }
        return result.toString();
    }
    
}
