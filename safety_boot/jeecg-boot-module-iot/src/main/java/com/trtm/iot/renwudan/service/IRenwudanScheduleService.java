package com.trtm.iot.renwudan.service;

import com.trtm.iot.renwudan.entity.RenwudanSchedule;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Description: 拌合站任务单进度
 * @Author: jeecg-boot
 * @Date:   2021-06-16
 * @Version: V1.0
 */

public interface IRenwudanScheduleService extends IService<RenwudanSchedule> {

    /**
     *
     * @param code
     * @return
     */
    RenwudanSchedule queryone(String code);

    RenwudanSchedule queryones(String code,String shebei_no);

    int updateone(double metes, double bfb, Integer id, Date endtime, Integer dishCount);

    List<RenwudanSchedule> selectrwdschedule(String code, String shebeiNo);

    List<RenwudanSchedule> queryoness(String code, Integer station);

    Map<String,Object> selectmetes(String code, Integer station,String sys_depart_orgcode);

    List<RenwudanSchedule> selectmetesd(String code,String orgcode);

    int updateonezt(double metes, double bfb, Integer id, Date endtime, Integer dishCount, String conspos);
}
