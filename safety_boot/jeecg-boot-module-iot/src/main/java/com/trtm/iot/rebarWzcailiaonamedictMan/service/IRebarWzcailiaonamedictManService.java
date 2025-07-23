package com.trtm.iot.rebarWzcailiaonamedictMan.service;

import cn.hutool.json.JSONObject;
import com.trtm.iot.rebarComponent.entity.ComponentVo;
import com.trtm.iot.rebarWzcailiaonamedictMan.entity.Number0CaiLiao;
import com.trtm.iot.rebarWzcailiaonamedictMan.entity.Number0CaiLiaoVoS;
import com.trtm.iot.rebarWzcailiaonamedictMan.entity.RebarWzcailiaonamedictMan;
import com.baomidou.mybatisplus.extension.service.IService;
import com.trtm.iot.rebarWzcailiaonamedictMan.entity.RebarWzcailiaonamedictManVo;

import java.util.List;

/**
 * @Description: rebar_wzcailiaonamedict_man
 * @Author: jeecg-boot
 * @Date:   2024-12-10
 * @Version: V1.0
 */
public interface IRebarWzcailiaonamedictManService extends IService<RebarWzcailiaonamedictMan> {

    List<RebarWzcailiaonamedictManVo> getRebarWzcailiaoList(RebarWzcailiaonamedictManVo rebarWzcailiaonamedictManVo, Integer pageNo, Integer pageSize, String sys_depart_orgcode, String sys_depart_orgcodes);


    List<Number0CaiLiao> getWzCailiao(String wbsId,String sysOrgCode, String sysOrgCodes,String token);

    boolean addWzCaiLiao(Number0CaiLiaoVoS number0CaiLiaoVoS);

    String getToken();

    boolean addWzCaiLiaoComponentTask(Number0CaiLiaoVoS number0CaiLiaoVoS);
}
