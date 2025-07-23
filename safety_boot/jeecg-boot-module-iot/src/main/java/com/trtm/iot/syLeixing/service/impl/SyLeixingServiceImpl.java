package com.trtm.iot.syLeixing.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.syLeixing.entity.SyLeixing;

import com.trtm.iot.syLeixing.mapper.SyLeixingMapper;
import com.trtm.iot.syLeixing.service.ISyLeixingService;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @Description: 试验类型
 * @Author: jeecg-boot
 * @Date: 2022-03-09
 * @Version: V1.0
 */
@Service
public class SyLeixingServiceImpl extends ServiceImpl<SyLeixingMapper, SyLeixing> implements ISyLeixingService {

    /**
     * 修改需要存到数据库的对象，新增时需从数据库查出当前节点下编码的最大值并加一
     * （ps：由于数据库中数据类型是varchar 所以做以下处理）
     */
    @Override
    public SyLeixing modifyStorageObject(SyLeixing syLeixing) {
        Integer nodeType = syLeixing.getNodetype();
        ArrayList<Integer> expNoList = new ArrayList<>();
        //查出全部材料或试验编码
        List<String> list = baseMapper.getAllByCailiaono();
        //如果没有父节点则走这条分支
        if (nodeType == 0) {
            //遍历所有编码后 切割字符串只要前三位(父节点编码)
            for (int i = 0; i < list.size(); i++) {
                Integer expNo = Integer.valueOf(list.get(i).substring(0, 3));
                expNoList.add(expNo);
            }
            return materialEncodingAddOne(syLeixing, expNoList);
        } else {
            //有父节点走这条
            String parentNo = syLeixing.getParentno();
            for (int i = 0; i < list.size(); i++) {
                String s = list.get(i);
                //遍历所有编码后取出前三位(父节点编码)对比前端所传父节点编码,只保留父节点和父节点下的编码(用于取最大值) 否则会出现数据异常！！！！
                if (s.substring(0, 3).equals(parentNo)) {
                    expNoList.add(Integer.valueOf(s));
                }
            }
            SyLeixing syType = materialEncodingAddOne(syLeixing, expNoList);
            return syType;
        }
    }

    /**
     * 获取材料编码最大值并加一
     */
    public SyLeixing materialEncodingAddOne(SyLeixing syLeixing, List<Integer> list) {
        Integer max = Collections.max(list);
        String materialEncoding = convert(max, integer -> String.valueOf(integer + 1));
        //为符合编码格式 字符串转String后在前 + "00"
        String x = "00" + materialEncoding;
        syLeixing.setCailiaono(x);
        return syLeixing;
    }


    /**
     * 使用函数式接口完成字符串转换
     *
     * @param i
     * @param function
     */
    private static String convert(Integer i, Function<Integer, String> function) {
        return function.apply(i);
    }
}
