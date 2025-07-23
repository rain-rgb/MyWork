package org.jeecg.modules.job.lijiyashijob;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.ljyadata.entity.Ljyadata;
import com.trtm.iot.ljyadata.service.ILjyadataService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

/**
 * @ClassName LuJiYaShiJob：
 * @Description TODO
 * @Author 55314
 * @Date 2022/12/12 14:00
 * @Version 1.0
 **/
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class LuJiYaShiJob implements Job {
    @Autowired
    private ILjyadataService ljyadataService;

    private String projectId = "admin@20220728224041";

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        QueryWrapper<Ljyadata> ljyadataQueryWrapper = new QueryWrapper<>();
        ljyadataQueryWrapper.select("max(gpsTime) as gpsTime, sectionId");
        ljyadataQueryWrapper.groupBy("sectionId");
        List<Ljyadata> list = ljyadataService.list(ljyadataQueryWrapper);

        for (Ljyadata ljyadataq : list) {
            Object sectionId = ljyadataq.getSectionid();
            Date gpstime = ljyadataq.getGpstime();
            String url = "http://47.111.106.172:6021/frontManage/getDataAttrNew?sectionId=" + sectionId + "&date=" + gpstime + "&projectId="+projectId+"";
            String back = HttpRequest.get(url)
                    .header("Content-Type", "application/json")
                    .execute()
                    .body();
            if ("".equals(back)){
                continue;
            }
            JSONArray array = JSON.parseArray(back);
            for (int i = 0; i < array.size(); i++) {
                Ljyadata ljyadata = new Ljyadata();

                JSONObject jsonObject = array.getJSONObject(i);
                String rollerId = jsonObject.getString("rollerId");
                String temperature = jsonObject.getString("temperature");
                String lon = jsonObject.getString("lon");
                Date gpsTime = jsonObject.getDate("gpsTime");
                String velocity = jsonObject.getString("velocity");
                String lat = jsonObject.getString("lat");

                ljyadata.setGpstime(gpsTime);
                ljyadata.setLat(lat);
                ljyadata.setLon(lon);
                ljyadata.setRollerid(rollerId);
                ljyadata.setVelocity(velocity);
                ljyadata.setSectionid(String.valueOf(sectionId));
                boolean save = ljyadataService.save(ljyadata);
                if (save){
                    System.out.println("保存成功！！！");
                }else {
                    System.out.println("保存失败！！！");
                }
            }

        }
    }

    public List<Map<String,Object>> getSectionId() {
        List list = new ArrayList();
        Map oneMap = new HashMap();
        oneMap.put("projectId", "admin@20221011231138");
        Map twoMap = new HashMap();
        twoMap.put("projectId", "admin@20220728224205");
        Map threeMap = new HashMap();
        threeMap.put("projectId", "admin@20220922155751");
        Map fourMap = new HashMap();
        fourMap.put("projectId", "admin@20221026180158");
        list.add(oneMap);
        list.add(twoMap);
        list.add(threeMap);
        list.add(fourMap);
        return list;
    }
}
