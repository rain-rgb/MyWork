package org.jeecg.modules.job.renwudanJob;

import com.trtm.iot.hntbhz.entity.BhzCementBase;
import com.trtm.iot.hntbhz.service.IBhzCementBaseService;
import com.trtm.iot.renwudan.entity.RenwudanSchedule;
import com.trtm.iot.renwudan.service.IRenwudanScheduleService;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import com.trtm.iot.system.entity.Bhzrenwudan;
import com.trtm.iot.system.entity.ShebeiInfo;
import com.trtm.iot.system.entity.Shigongphb;
import com.trtm.iot.system.service.IBhzrenwudanService;
import com.trtm.iot.system.service.IShebeiInfoService;
import com.trtm.iot.system.service.IShigongphbService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;

/**
 * \* @author: Xx
 * \* Date: 2021/6/16
 * \* Time: 17:17
 * \* Description:根据任务单的任务编号去查询拌合站的数据去计算进度以及给新的表添加数据
 * \
 */
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class RenwudanJob implements Job {
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private IBhzCementBaseService bhzCementBaseService;
    @Autowired
    private IBhzrenwudanService bhzrenwudanService;
    @Autowired
    private IRenwudanScheduleService renwudanScheduleService;
    @Autowired
    private IShebeiInfoService shebeiInfoService;
    @Autowired
    private IShigongphbService shigongphbService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.HNTBHZ_RENWUDAN);//砼拌合站进度（根据任务单以及拌合站数据）=16
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info(String.format("未获取到砼拌合站进度定时任务的配置信息" + DateUtils.now()));
            return;
        }
        //当前数据检测到的目标id
        Integer curid = selectsysconfigone.getCurid();
        List<BhzCementBase> selecthntbhzones = bhzCementBaseService.selecthntbhzList1(curid, 0);//没有进行过进度统计的数据
        if (null == selecthntbhzones || selecthntbhzones.size() == 0) {
            log.info(String.format("暂无砼拌合站未进度统计的数据" + DateUtils.now()));
            return;
        }
        int id = 0;
        for (BhzCementBase selecthntbhzone : selecthntbhzones) {
            id = selecthntbhzone.getId();
            String batchNo = selecthntbhzone.getBatchNo();//唯一编码
            String shebeiNo = selecthntbhzone.getShebeiNo();//设备编号
            String formulaNo = selecthntbhzone.getFormulaNo();//配方号
            String workNo = selecthntbhzone.getWorkNo();//任务单号
            Date productDatetime = selecthntbhzone.getProductDatetime();//出料时间
            Double estimateNumber = selecthntbhzone.getEstimateNumber();//生产方量
            DecimalFormat df = new DecimalFormat("######0.00");
            String format = df.format(estimateNumber);
            if (StringUtils.isEmpty(formulaNo)) {
                log.info(String.format("暂无砼拌合站配单号数据" + DateUtils.now()));
                bhzCementBaseService.updatehntbhzrenwustatus(batchNo, 10);
                continue;
            }
            ShebeiInfo selectshebeione = shebeiInfoService.selectshebeione(shebeiNo);
            if (null == selectshebeione) {
                log.info(String.format("暂无砼拌合站的设备" + DateUtils.now()));
                bhzCementBaseService.updatehntbhzrenwustatus(batchNo, 20);
                continue;
            }
            List<Bhzrenwudan> queryselectone = null;
            List<Shigongphb> shigongphb = shigongphbService.queryonelist(formulaNo, shebeiNo, workNo);
            if (shigongphb.size() > 0 && StringUtils.isNotBlank(formulaNo)) {
                for (Shigongphb shigongphb1 : shigongphb) {
                    String renwudan = shigongphb1.getRenwudan();
                    if (renwudan != null) {
                        queryselectone = bhzrenwudanService.queryselectlist1(renwudan, shigongphb1.getStation(), shigongphb1.getSysOrgCode());
                        if (queryselectone.size() == 0) {
                            queryselectone = bhzrenwudanService.queryselectlist(renwudan, 3, shigongphb1.getSysOrgCode());
                            if (queryselectone.size() == 0) {
                                log.info(String.format("暂无相匹配的任务单数据" + DateUtils.now()));
                                bhzCementBaseService.updatehntbhzrenwustatus(batchNo, 30);
                                continue;
                            }
                        }
                    } else {
//                        queryselectone = bhzrenwudanService.queryselectlist(shigongphb1.getCode(),shigongphb1.getStation());
//                        if(queryselectone.size()==0){
//                            queryselectone = bhzrenwudanService.queryselectlist(shigongphb1.getCode(),3);
//                            if(queryselectone.size()==0){
//                                log.info(String.format("暂无相匹配的施工配合比数据" + DateUtils.now()));
//                                bhzCementBaseService.updatehntbhzrenwustatus(batchNo,30);
//                                continue;
//                            }
//                        }
                        log.info(String.format("暂无砼拌合站配单号数据" + DateUtils.now()));
                        bhzCementBaseService.updatehntbhzrenwustatus(batchNo, 10);
                        continue;
                    }
                    try {
                        for (Bhzrenwudan bhzrenwudan : queryselectone) {
                            boolean ztyj = true;
                            if (bhzrenwudan.getSysOrgCode().contains("A05A01A06A16")){
                                bhzrenwudan.setConspos(bhzrenwudan.getProjadr());
                                ztyj = false;
                            }
                            String projname = bhzrenwudan.getProjname();//项目名称
                            String code = bhzrenwudan.getCode();//任务单编号
                            Integer station = bhzrenwudan.getStation();//生产线
                            String conspos = bhzrenwudan.getConspos();//浇筑部位
                            String pour = bhzrenwudan.getPour();//浇筑方式
                            String betlev = bhzrenwudan.getBetlev();//强度等级
                            String lands = bhzrenwudan.getLands();//塌落度
                            Double mete = bhzrenwudan.getMete();//设计方量
                            String projgrade = bhzrenwudan.getProjgrade();//分部分项节点
                            String sysOrgCode = bhzrenwudan.getSysOrgCode();//权限
                            List<RenwudanSchedule> list = renwudanScheduleService.selectrwdschedule(code, shebeiNo);
                            if (list.size() > 1) {
                                bhzCementBaseService.updatehntbhzrenwustatus(batchNo, 5);//任务单进度数据重复
                                log.info(String.format("数据重复暂时跳过" + DateUtils.now()));
                                continue;
                            }
                            RenwudanSchedule queryone = renwudanScheduleService.queryones(code, shebeiNo);
                            if (queryone == null) {
                                RenwudanSchedule renwudanSchedule = new RenwudanSchedule();
                                renwudanSchedule.setCode(code);
                                renwudanSchedule.setShebeiNo(shebeiNo);
                                renwudanSchedule.setStation(station);
                                renwudanSchedule.setProjectname(projname);
                                renwudanSchedule.setConspos(conspos);
                                renwudanSchedule.setPour(pour);
                                renwudanSchedule.setStarttim(productDatetime);
                                renwudanSchedule.setBetlev(betlev);
                                renwudanSchedule.setLands(lands);
                                renwudanSchedule.setMete(mete);
                                renwudanSchedule.setMetes(Double.valueOf(format));
                                renwudanSchedule.setSysOrgCode(sysOrgCode);
                                renwudanSchedule.setSysDepartProject(projgrade);
                                renwudanSchedule.setDishCount(1);
                                if(mete == null){
                                    mete = 1.0;
                                }
                                double bfb = Double.valueOf(format) / mete * 100;
                                String format1 = df.format(bfb);
                                renwudanSchedule.setBfb(Double.valueOf(format1));
                                boolean save = renwudanScheduleService.save(renwudanSchedule);
                                if (save) {
                                    log.info(String.format("添加任务单进度统计成功" + DateUtils.now()));
                                    Bhzrenwudan bhzrenwudans = new Bhzrenwudan();
                                    bhzrenwudans.setId(bhzrenwudan.getId());//修改任务单完成状态
                                    bhzrenwudans.setStatus(4);
                                    boolean b = bhzrenwudanService.updateById(bhzrenwudans);
                                    if (b) {
                                        log.info(String.format("任务单状态修改成功" + DateUtils.now()));
                                    }
                                } else {
                                    log.info(String.format("添加任务单进度统计失败" + DateUtils.now()));
                                }
                            } else {
                                Integer id1 = queryone.getId();
                                Integer dishCount = queryone.getDishCount() + 1;
                                Double metes = queryone.getMetes();
                                Double mete1 = queryone.getMete();
                                Double aDouble = Double.valueOf(format);
                                double metes1 = metes + aDouble;
                                String format2 = df.format(metes1);
                                double bfb = metes1 / mete1 * 100;
                                String format1 = df.format(bfb);
                                double jindu = Double.parseDouble(format1);
                                if (jindu >= 100.0) {
                                    jindu = 100.0;
                                    Bhzrenwudan bhzrenwudans = new Bhzrenwudan();
                                    bhzrenwudans.setId(bhzrenwudan.getId());//修改任务单完成状态
                                    bhzrenwudans.setIsfinish(1);
                                    bhzrenwudans.setStatus(5);
                                    bhzrenwudanService.updateById(bhzrenwudans);
                                }

                                int updateone = 0;
                                if (ztyj){
                                    updateone = renwudanScheduleService.updateone(Double.parseDouble(format2), jindu, id1, productDatetime, dishCount);
                                }else {
                                    updateone = renwudanScheduleService.updateonezt(Double.parseDouble(format2), jindu, id1, productDatetime, dishCount,bhzrenwudan.getConspos());
                                }
                                if (updateone == 1) {
                                    log.info(String.format("添加任务单进度统计成功" + DateUtils.now()));
                                } else {
                                    log.info(String.format("添加任务单进度统计失败" + DateUtils.now()));
                                }
                            }
                            BhzCementBase bhzCementBase = new BhzCementBase();
                            bhzCementBase.setId(id);
                            bhzCementBase.setRenwudanstatus(1);
                            bhzCementBaseService.updateById(bhzCementBase);//拌合站修改任务单统计进度的状态
                        }
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                        bhzCementBaseService.updatehntbhzrenwustatus(batchNo, 40);
                    }
                    log.info(String.format("任务单统计进度!   时间:" + DateUtils.now()));
                }
            } else {
                bhzCementBaseService.updatehntbhzrenwustatus(batchNo, 30);
                log.info(String.format("暂无相匹配的施工配合比数据" + DateUtils.now()));
                continue;
            }

        }
        sysConfigService.updateSysConfig(JobUtil.HNTBHZ_RENWUDAN, id);//根据传过来的唯一值来修改当前判断到的数据id
    }
}
