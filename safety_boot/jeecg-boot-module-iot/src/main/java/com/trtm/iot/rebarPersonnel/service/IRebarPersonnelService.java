package com.trtm.iot.rebarPersonnel.service;

import com.trtm.iot.rebarPersonnel.entity.RebarPersonnel;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @Description: rebar_personnel
 * @Author: jeecg-boot
 * @Date:   2024-11-14
 * @Version: V1.0
 */
public interface IRebarPersonnelService extends IService<RebarPersonnel> {

    List<RebarPersonnel> getLeadTeamByFactory(RebarPersonnel rebarPersonnel, String factoryId, Integer pageNo, Integer pageSize, HttpServletRequest req);
}
