package com.trtm.sy.sysbjdjz.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.trtm.sy.sysbjdjz.entity.SbJianDingJiaoZhunResponse;
import com.trtm.sy.sysbjdjz.entity.SyDpsJcShebeiJiandingjiaozhun;

import java.text.ParseException;
import java.util.List;

/**
 * @Description: sy_dps_jc_shebei_jiandingjiaozhun
 * @Author: jeecg-boot
 * @Date: 2023-10-17
 * @Version: V1.0
 */
public interface ISyDpsJcShebeiJiandingjiaozhunService extends IService<SyDpsJcShebeiJiandingjiaozhun> {

    IPage<SbJianDingJiaoZhunResponse> getJdJz(String sheBeiIds, String startTime, String endTime, Integer type, Integer pageNo, Integer pageSize);

    void addOrUpdate(SyDpsJcShebeiJiandingjiaozhun syDpsJcShebeiJiandingjiaozhun) throws ParseException;
}
