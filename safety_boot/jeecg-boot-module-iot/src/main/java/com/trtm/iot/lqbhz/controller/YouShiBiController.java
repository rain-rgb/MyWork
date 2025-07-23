package com.trtm.iot.lqbhz.controller;


import com.trtm.iot.lqbhz.entity.YouShiBi;
import com.trtm.iot.lqbhz.mapper.YouShiBiMapper;


import org.jeecg.common.api.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * @Description: TODO
 * @author: scott
 * @date: 2021年11月11日 15:35
 */
@RestController
@RequestMapping("/lqbhz/YouShiBi")
public class YouShiBiController {
    @Autowired
    YouShiBiMapper youShiBiMapper;
    // 根据设备编号查询油石比
    @GetMapping("/getshiyoubi")
    public Result<?> getShiyoubi(String sheBeiNo){
        List<YouShiBi> list10 = youShiBiMapper.getShiyoubi(sheBeiNo);
        System.out.println(list10);
        List list =new ArrayList();
        for (YouShiBi statistics : list10) {
            Map map=new HashMap();
            String youshibi = statistics.getYoushibi();
            if(youshibi==null){
                youshibi ="0";
            }
            String time = statistics.getChuliaoshijian();
            map.put("youshibi",youshibi);
            map.put("time",time);
            list.add(map);
        }
        return  Result.OK(list);
    }
}
