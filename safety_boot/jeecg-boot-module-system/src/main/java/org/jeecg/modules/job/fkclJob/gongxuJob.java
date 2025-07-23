package org.jeecg.modules.job.fkclJob;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.carmiles.entity.Carmiles;
import com.trtm.iot.carmiles.service.ICarmilesService;
import com.trtm.iot.deviceMixpileHistorydataOne.service.IDeviceMixpileHistorydataOneService;
import com.trtm.iot.devicepipepilehistorydataone.service.IDevicePipepileHistorydataOneService;
import com.trtm.iot.gongyistatistic.entity.GongyiStatistic;
import com.trtm.iot.gongyistatistic.service.IGongyiStatisticService;
import com.trtm.iot.hntbhz.service.IBhzCementBaseService;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import com.trtm.iot.system.service.IShebeiInfoService;
import com.trtm.iot.tpny.entity.FrontDeviceRealdata;
import com.trtm.iot.tpny.service.IFrontDeviceRealdataService;
import com.trtm.iot.yajiangs.service.ITrYajiangSService;
import com.trtm.iot.zhanglaxxb.service.ITrZhanglaXxbService;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.jeecg.modules.system.entity.SysDepart;
import org.jeecg.modules.system.entity.SysDictItem;
import org.jeecg.modules.system.service.ISysDepartService;
import org.jeecg.modules.system.service.ISysDictService;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class gongxuJob implements Job {
    @Autowired
    private ISysConfigService sysConfigService;

    @Autowired
    private IBhzCementBaseService bhzCementBaseService;

    @Autowired
    private ITrZhanglaXxbService zhanglaXxbService;
    @Autowired
    private ITrYajiangSService yajiangSService;
    @Autowired
    private IDevicePipepileHistorydataOneService pipepileHistorydataOneService;
    @Autowired
    private IDeviceMixpileHistorydataOneService mixpileHistorydataOneService;

    @Autowired
    private ISysDepartService departService;

    @Autowired
    private ISysDictService iSysDictService;
    @Autowired
    private IGongyiStatisticService gongyiStatisticService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.ZJWZ_GONGYI);//高信工艺新增接口
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info("未获取到高信工艺新增接口定时任务的配置信息" + DateUtils.now());
            return;
        }
        Date date=new Date();//此时date为当前的时间
        SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy");//设置当前时间的格式，为年
        String time = null;
        // 12个月的数据
        for (int i = 1; i < 13; i++) {
            if (i < 10){
                time = dateFormat.format(date) + "-0"+i;
            }else {
                time = dateFormat.format(date) + "-"+i;
            }
            // 查询项目
            List<SysDepart> sysDepartList = departService.selectBySBNo();
            for (SysDepart l :sysDepartList){
                // 获取组织机构code 和 项目名称
                String orgCode = l.getOrgCode();
                String departName = l.getDepartName();
                // 查询项目下设备列表
                List<String> querySheBeiList = departService.fingAllSheBeiNo(orgCode);
                Integer zongShu = 0;
                Integer yuJingS = 0;
                Integer biHeS = 0;
                if (querySheBeiList != null && querySheBeiList.size() > 0) {
                    for (int j = 1; j < 7; j++) {
                        SysDictItem s = iSysDictService.selectdictbyid("prontzhi",j);
                        if (j == 1){
                            // 混凝土拌合站
                            zongShu = bhzCementBaseService.findXiangMuZSs(querySheBeiList,time);//查询所有设备在表中存在的数量
                            yuJingS = bhzCementBaseService.findYuJingSs(querySheBeiList,time);//根据所有的设备查询不合格的数量
                            biHeS = bhzCementBaseService.findBiHeSs(querySheBeiList,time);//根据所有的设备查询状态值20 的数量
                            // 查询是否统计过
                            QueryWrapper<GongyiStatistic> queryWrapper = new QueryWrapper<>();
                            queryWrapper.eq("sys_org_code",orgCode);
                            queryWrapper.eq("prontzhi",j);
                            queryWrapper.eq("pront_time",time);
                            GongyiStatistic one = gongyiStatisticService.getOne(queryWrapper);
                            if (one != null){
                                one.setZongshu(String.valueOf(zongShu));
                                one.setYujings(String.valueOf(yuJingS));
                                one.setBihes(String.valueOf(biHeS));
                                gongyiStatisticService.updateById(one);
                            }else {
                                GongyiStatistic gongyiStatistic = new GongyiStatistic();
                                gongyiStatistic.setSysOrgCode(orgCode);
                                gongyiStatistic.setOrgcodeName(departName);
                                gongyiStatistic.setProntzhi(j);
                                gongyiStatistic.setProntTime(time);
                                gongyiStatistic.setShebeiNo(s.getItemText());
                                gongyiStatistic.setZongshu(String.valueOf(zongShu));
                                gongyiStatistic.setYujings(String.valueOf(yuJingS));
                                gongyiStatistic.setBihes(String.valueOf(biHeS));
                                gongyiStatisticService.save(gongyiStatistic);
                            }
                        }else if (j == 2){
                            // 张拉
                            zongShu = zhanglaXxbService.findXiangMuZSs(querySheBeiList,time);//查询所有设备在表中存在的数量
                            yuJingS = zhanglaXxbService.findYuJingSs(querySheBeiList,time);//根据所有的设备查询不合格的数量
                            biHeS = zhanglaXxbService.findBiHeSs(querySheBeiList,time);//根据所有的设备查询状态值20 的数量
                            // 查询是否统计过
                            QueryWrapper<GongyiStatistic> queryWrapper = new QueryWrapper<>();
                            queryWrapper.eq("sys_org_code",orgCode);
                            queryWrapper.eq("prontzhi",j);
                            queryWrapper.eq("pront_time",time);
                            GongyiStatistic one = gongyiStatisticService.getOne(queryWrapper);
                            if (one != null){
                                one.setZongshu(String.valueOf(zongShu));
                                one.setYujings(String.valueOf(yuJingS));
                                one.setBihes(String.valueOf(biHeS));
                                gongyiStatisticService.updateById(one);
                            }else {
                                GongyiStatistic gongyiStatistic = new GongyiStatistic();
                                gongyiStatistic.setSysOrgCode(orgCode);
                                gongyiStatistic.setOrgcodeName(departName);
                                gongyiStatistic.setProntzhi(j);
                                gongyiStatistic.setProntTime(time);
                                gongyiStatistic.setShebeiNo(s.getItemText());
                                gongyiStatistic.setZongshu(String.valueOf(zongShu));
                                gongyiStatistic.setYujings(String.valueOf(yuJingS));
                                gongyiStatistic.setBihes(String.valueOf(biHeS));
                                gongyiStatisticService.save(gongyiStatistic);
                            }
                        }else if (j == 3){
                            // 压浆
                            zongShu = yajiangSService.findXiangMuZSss(querySheBeiList,time);//查询所有设备在表中存在的数量
                            yuJingS = yajiangSService.findYuJingSs(querySheBeiList,time);//根据所有的设备查询不合格的数量
                            biHeS = yajiangSService.findBiHeSs(querySheBeiList,time);//根据所有的设备查询状态值20 的数量
                            // 查询是否统计过
                            QueryWrapper<GongyiStatistic> queryWrapper = new QueryWrapper<>();
                            queryWrapper.eq("sys_org_code",orgCode);
                            queryWrapper.eq("prontzhi",j);
                            queryWrapper.eq("pront_time",time);
                            GongyiStatistic one = gongyiStatisticService.getOne(queryWrapper);
                            if (one != null){
                                one.setZongshu(String.valueOf(zongShu));
                                one.setYujings(String.valueOf(yuJingS));
                                one.setBihes(String.valueOf(biHeS));
                                gongyiStatisticService.updateById(one);
                            }else {
                                GongyiStatistic gongyiStatistic = new GongyiStatistic();
                                gongyiStatistic.setSysOrgCode(orgCode);
                                gongyiStatistic.setOrgcodeName(departName);
                                gongyiStatistic.setProntzhi(j);
                                gongyiStatistic.setProntTime(time);
                                gongyiStatistic.setShebeiNo(s.getItemText());
                                gongyiStatistic.setZongshu(String.valueOf(zongShu));
                                gongyiStatistic.setYujings(String.valueOf(yuJingS));
                                gongyiStatistic.setBihes(String.valueOf(biHeS));
                                gongyiStatisticService.save(gongyiStatistic);
                            }
                        }else if (j == 4){
                            // 管桩
                            zongShu = pipepileHistorydataOneService.findXiangMuZSsb(querySheBeiList,time);//查询所有设备在表中存在的数量
                            yuJingS = pipepileHistorydataOneService.findYuJingSsb(querySheBeiList,time);//根据所有的设备查询不合格的数量
                            biHeS = pipepileHistorydataOneService.findBiHeSsb(querySheBeiList,time);//根据所有的设备查询状态值20 的数量
                            // 查询是否统计过
                            QueryWrapper<GongyiStatistic> queryWrapper = new QueryWrapper<>();
                            queryWrapper.eq("sys_org_code",orgCode);
                            queryWrapper.eq("prontzhi",j);
                            queryWrapper.eq("pront_time",time);
                            GongyiStatistic one = gongyiStatisticService.getOne(queryWrapper);
                            if (one != null){
                                one.setZongshu(String.valueOf(zongShu));
                                one.setYujings(String.valueOf(yuJingS));
                                one.setBihes(String.valueOf(biHeS));
                                gongyiStatisticService.updateById(one);
                            }else {
                                GongyiStatistic gongyiStatistic = new GongyiStatistic();
                                gongyiStatistic.setSysOrgCode(orgCode);
                                gongyiStatistic.setOrgcodeName(departName);
                                gongyiStatistic.setProntzhi(j);
                                gongyiStatistic.setProntTime(time);
                                gongyiStatistic.setShebeiNo(s.getItemText());
                                gongyiStatistic.setZongshu(String.valueOf(zongShu));
                                gongyiStatistic.setYujings(String.valueOf(yuJingS));
                                gongyiStatistic.setBihes(String.valueOf(biHeS));
                                gongyiStatisticService.save(gongyiStatistic);
                            }
                        }else if (j == 5){
                            // 搅拌桩
                            zongShu = mixpileHistorydataOneService.findXiangMuZSsb(querySheBeiList,time);//查询所有设备在表中存在的数量
                            yuJingS = mixpileHistorydataOneService.findYuJingSsb(querySheBeiList,time);//根据所有的设备查询不合格的数量
                            biHeS = mixpileHistorydataOneService.findBiHeSsb(querySheBeiList,time);//根据所有的设备查询状态值20 的数量
                            // 查询是否统计过
                            QueryWrapper<GongyiStatistic> queryWrapper = new QueryWrapper<>();
                            queryWrapper.eq("sys_org_code",orgCode);
                            queryWrapper.eq("prontzhi",j);
                            queryWrapper.eq("pront_time",time);
                            GongyiStatistic one = gongyiStatisticService.getOne(queryWrapper);
                            if (one != null){
                                one.setZongshu(String.valueOf(zongShu));
                                one.setYujings(String.valueOf(yuJingS));
                                one.setBihes(String.valueOf(biHeS));
                                gongyiStatisticService.updateById(one);
                            }else {
                                GongyiStatistic gongyiStatistic = new GongyiStatistic();
                                gongyiStatistic.setSysOrgCode(orgCode);
                                gongyiStatistic.setOrgcodeName(departName);
                                gongyiStatistic.setProntzhi(j);
                                gongyiStatistic.setProntTime(time);
                                gongyiStatistic.setShebeiNo(s.getItemText());
                                gongyiStatistic.setZongshu(String.valueOf(zongShu));
                                gongyiStatistic.setYujings(String.valueOf(yuJingS));
                                gongyiStatistic.setBihes(String.valueOf(biHeS));
                                gongyiStatisticService.save(gongyiStatistic);
                            }
                        }else if(j == 6) {
                            // 旋喷桩
                            zongShu = mixpileHistorydataOneService.findXiangMuZSsbs(querySheBeiList,time);//查询所有设备在表中存在的数量
                            yuJingS = mixpileHistorydataOneService.findYuJingSsbs(querySheBeiList,time);//根据所有的设备查询不合格的数量
                            biHeS = mixpileHistorydataOneService.findBiHeSsbs(querySheBeiList,time);//根据所有的设备查询状态值20 的数量
                            // 查询是否统计过
                            QueryWrapper<GongyiStatistic> queryWrapper = new QueryWrapper<>();
                            queryWrapper.eq("sys_org_code",orgCode);
                            queryWrapper.eq("prontzhi",j);
                            queryWrapper.eq("pront_time",time);
                            GongyiStatistic one = gongyiStatisticService.getOne(queryWrapper);
                            if (one != null){
                                one.setZongshu(String.valueOf(zongShu));
                                one.setYujings(String.valueOf(yuJingS));
                                one.setBihes(String.valueOf(biHeS));
                                gongyiStatisticService.updateById(one);
                            }else {
                                GongyiStatistic gongyiStatistic = new GongyiStatistic();
                                gongyiStatistic.setSysOrgCode(orgCode);
                                gongyiStatistic.setOrgcodeName(departName);
                                gongyiStatistic.setProntzhi(j);
                                gongyiStatistic.setProntTime(time);
                                gongyiStatistic.setShebeiNo(s.getItemText());
                                gongyiStatistic.setZongshu(String.valueOf(zongShu));
                                gongyiStatistic.setYujings(String.valueOf(yuJingS));
                                gongyiStatistic.setBihes(String.valueOf(biHeS));
                                gongyiStatisticService.save(gongyiStatistic);
                            }
                        }
                    }
                }else {
                    for (int j = 1; j < 7; j++) {
                        SysDictItem s = iSysDictService.selectdictbyid("prontzhi",j);
                        if (j == 1){
                            zongShu = 0;//查询所有设备在表中存在的数量
                            yuJingS = 0;//根据所有的设备查询不合格的数量
                            biHeS = 0;//根据所有的设备查询状态值20 的数量
                            // 查询是否统计过
                            QueryWrapper<GongyiStatistic> queryWrapper = new QueryWrapper<>();
                            queryWrapper.eq("sys_org_code",orgCode);
                            queryWrapper.eq("prontzhi",j);
                            queryWrapper.eq("pront_time",time);
                            GongyiStatistic one = gongyiStatisticService.getOne(queryWrapper);
                            if (one != null){
                                one.setZongshu(String.valueOf(zongShu));
                                one.setYujings(String.valueOf(yuJingS));
                                one.setBihes(String.valueOf(biHeS));
                                gongyiStatisticService.updateById(one);
                            }else {
                                GongyiStatistic gongyiStatistic = new GongyiStatistic();
                                gongyiStatistic.setSysOrgCode(orgCode);
                                gongyiStatistic.setOrgcodeName(departName);
                                gongyiStatistic.setProntzhi(j);
                                gongyiStatistic.setProntTime(time);
                                gongyiStatistic.setShebeiNo(s.getItemText());
                                gongyiStatistic.setZongshu(String.valueOf(zongShu));
                                gongyiStatistic.setYujings(String.valueOf(yuJingS));
                                gongyiStatistic.setBihes(String.valueOf(biHeS));
                                gongyiStatisticService.save(gongyiStatistic);
                            }
                        }else if (j == 2){
                            zongShu = 0;//查询所有设备在表中存在的数量
                            yuJingS = 0;//根据所有的设备查询不合格的数量
                            biHeS = 0;//根据所有的设备查询状态值20 的数量
                            // 查询是否统计过
                            QueryWrapper<GongyiStatistic> queryWrapper = new QueryWrapper<>();
                            queryWrapper.eq("sys_org_code",orgCode);
                            queryWrapper.eq("prontzhi",j);
                            queryWrapper.eq("pront_time",time);
                            GongyiStatistic one = gongyiStatisticService.getOne(queryWrapper);
                            if (one != null){
                                one.setZongshu(String.valueOf(zongShu));
                                one.setYujings(String.valueOf(yuJingS));
                                one.setBihes(String.valueOf(biHeS));
                                gongyiStatisticService.updateById(one);
                            }else {
                                GongyiStatistic gongyiStatistic = new GongyiStatistic();
                                gongyiStatistic.setSysOrgCode(orgCode);
                                gongyiStatistic.setOrgcodeName(departName);
                                gongyiStatistic.setProntzhi(j);
                                gongyiStatistic.setProntTime(time);
                                gongyiStatistic.setShebeiNo(s.getItemText());
                                gongyiStatistic.setZongshu(String.valueOf(zongShu));
                                gongyiStatistic.setYujings(String.valueOf(yuJingS));
                                gongyiStatistic.setBihes(String.valueOf(biHeS));
                                gongyiStatisticService.save(gongyiStatistic);
                            }
                        }else if (j == 3){
                            zongShu = 0;//查询所有设备在表中存在的数量
                            yuJingS = 0;//根据所有的设备查询不合格的数量
                            biHeS = 0;//根据所有的设备查询状态值20 的数量
                            // 查询是否统计过
                            QueryWrapper<GongyiStatistic> queryWrapper = new QueryWrapper<>();
                            queryWrapper.eq("sys_org_code",orgCode);
                            queryWrapper.eq("prontzhi",j);
                            queryWrapper.eq("pront_time",time);
                            GongyiStatistic one = gongyiStatisticService.getOne(queryWrapper);
                            if (one != null){
                                one.setZongshu(String.valueOf(zongShu));
                                one.setYujings(String.valueOf(yuJingS));
                                one.setBihes(String.valueOf(biHeS));
                                gongyiStatisticService.updateById(one);
                            }else {
                                GongyiStatistic gongyiStatistic = new GongyiStatistic();
                                gongyiStatistic.setSysOrgCode(orgCode);
                                gongyiStatistic.setOrgcodeName(departName);
                                gongyiStatistic.setProntzhi(j);
                                gongyiStatistic.setProntTime(time);
                                gongyiStatistic.setShebeiNo(s.getItemText());
                                gongyiStatistic.setZongshu(String.valueOf(zongShu));
                                gongyiStatistic.setYujings(String.valueOf(yuJingS));
                                gongyiStatistic.setBihes(String.valueOf(biHeS));
                                gongyiStatisticService.save(gongyiStatistic);
                            }
                        }else if (j == 4){
                            zongShu = 0;//查询所有设备在表中存在的数量
                            yuJingS = 0;//根据所有的设备查询不合格的数量
                            biHeS = 0;//根据所有的设备查询状态值20 的数量
                            // 查询是否统计过
                            QueryWrapper<GongyiStatistic> queryWrapper = new QueryWrapper<>();
                            queryWrapper.eq("sys_org_code",orgCode);
                            queryWrapper.eq("prontzhi",j);
                            queryWrapper.eq("pront_time",time);
                            GongyiStatistic one = gongyiStatisticService.getOne(queryWrapper);
                            if (one != null){
                                one.setZongshu(String.valueOf(zongShu));
                                one.setYujings(String.valueOf(yuJingS));
                                one.setBihes(String.valueOf(biHeS));
                                gongyiStatisticService.updateById(one);
                            }else {
                                GongyiStatistic gongyiStatistic = new GongyiStatistic();
                                gongyiStatistic.setSysOrgCode(orgCode);
                                gongyiStatistic.setOrgcodeName(departName);
                                gongyiStatistic.setProntzhi(j);
                                gongyiStatistic.setProntTime(time);
                                gongyiStatistic.setShebeiNo(s.getItemText());
                                gongyiStatistic.setZongshu(String.valueOf(zongShu));
                                gongyiStatistic.setYujings(String.valueOf(yuJingS));
                                gongyiStatistic.setBihes(String.valueOf(biHeS));
                                gongyiStatisticService.save(gongyiStatistic);
                            }
                        }else if (j == 5){
                            zongShu = 0;//查询所有设备在表中存在的数量
                            yuJingS = 0;//根据所有的设备查询不合格的数量
                            biHeS = 0;//根据所有的设备查询状态值20 的数量
                            // 查询是否统计过
                            QueryWrapper<GongyiStatistic> queryWrapper = new QueryWrapper<>();
                            queryWrapper.eq("sys_org_code",orgCode);
                            queryWrapper.eq("prontzhi",j);
                            queryWrapper.eq("pront_time",time);
                            GongyiStatistic one = gongyiStatisticService.getOne(queryWrapper);
                            if (one != null){
                                one.setZongshu(String.valueOf(zongShu));
                                one.setYujings(String.valueOf(yuJingS));
                                one.setBihes(String.valueOf(biHeS));
                                gongyiStatisticService.updateById(one);
                            }else {
                                GongyiStatistic gongyiStatistic = new GongyiStatistic();
                                gongyiStatistic.setSysOrgCode(orgCode);
                                gongyiStatistic.setOrgcodeName(departName);
                                gongyiStatistic.setProntzhi(j);
                                gongyiStatistic.setProntTime(time);
                                gongyiStatistic.setShebeiNo(s.getItemText());
                                gongyiStatistic.setZongshu(String.valueOf(zongShu));
                                gongyiStatistic.setYujings(String.valueOf(yuJingS));
                                gongyiStatistic.setBihes(String.valueOf(biHeS));
                                gongyiStatisticService.save(gongyiStatistic);
                            }
                        }else {
                            zongShu = 0;//查询所有设备在表中存在的数量
                            yuJingS = 0;//根据所有的设备查询不合格的数量
                            biHeS = 0;//根据所有的设备查询状态值20 的数量
                            // 查询是否统计过
                            QueryWrapper<GongyiStatistic> queryWrapper = new QueryWrapper<>();
                            queryWrapper.eq("sys_org_code",orgCode);
                            queryWrapper.eq("prontzhi",j);
                            queryWrapper.eq("pront_time",time);
                            GongyiStatistic one = gongyiStatisticService.getOne(queryWrapper);
                            if (one != null){
                                one.setZongshu(String.valueOf(zongShu));
                                one.setYujings(String.valueOf(yuJingS));
                                one.setBihes(String.valueOf(biHeS));
                                gongyiStatisticService.updateById(one);
                            }else {
                                GongyiStatistic gongyiStatistic = new GongyiStatistic();
                                gongyiStatistic.setSysOrgCode(orgCode);
                                gongyiStatistic.setOrgcodeName(departName);
                                gongyiStatistic.setProntzhi(j);
                                gongyiStatistic.setProntTime(time);
                                gongyiStatistic.setShebeiNo(s.getItemText());
                                gongyiStatistic.setZongshu(String.valueOf(zongShu));
                                gongyiStatistic.setYujings(String.valueOf(yuJingS));
                                gongyiStatistic.setBihes(String.valueOf(biHeS));
                                gongyiStatisticService.save(gongyiStatistic);
                            }
                        }
                    }
                }
            }
        }
    }
}
