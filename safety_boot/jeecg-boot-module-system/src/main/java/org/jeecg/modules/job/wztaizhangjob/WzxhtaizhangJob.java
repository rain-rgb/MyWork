package org.jeecg.modules.job.wztaizhangjob;

import com.trtm.iot.TbhzcailiaoStatistics.entity.BhzCementDetailStatistics;
import com.trtm.iot.TbhzcailiaoStatistics.service.IBhzCementDetailStatisticsService;
import com.trtm.iot.bhzStatistics.entity.BhzCementStatistics;
import com.trtm.iot.bhzStatistics.service.IBhzCementStatisticsService;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import com.trtm.iot.system.entity.ShebeiInfo;
import com.trtm.iot.system.entity.Shigongphb;
import com.trtm.iot.system.service.IShebeiInfoService;
import com.trtm.iot.system.service.IShigongphbService;
import com.trtm.iot.wzconsumetaizhang.entity.Wzconsumetaizhang;
import com.trtm.iot.wzconsumetaizhang.service.IWzconsumetaizhangService;
import com.trtm.iot.wzconsumetaizhangdetail.entity.WzconsumetaizhangDetail;
import com.trtm.iot.wzconsumetaizhangdetail.service.IWzconsumetaizhangDetailService;
import com.trtm.iot.wzliaocang.entity.Wzliaocang;
import com.trtm.iot.wzliaocang.service.IWzliaocangService;
import com.trtm.iot.wztaizhang.entity.Wztaizhang;
import com.trtm.iot.wztaizhang.service.IWztaizhangService;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.jeecg.modules.job.jobutil.NumberUtil;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;


import java.util.Date;
import java.util.List;

/**
 * \* Date: 2021/9/07
 * \* Time: 15:08
 * \* Description:根据拌合站统计表数据的配方号（配料单编号）获取料仓，根据料仓获取批次库存，拌合站材料统计表获取材料消耗，然后计算材料剩余以及给新的表添加数据
 * \
 */
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class WzxhtaizhangJob implements Job {
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private IBhzCementStatisticsService bhzCementStatisticsService;
    @Autowired
    private IShigongphbService shigongphbService;
    @Autowired
    private IBhzCementDetailStatisticsService bhzCementDetailStatisticsService;
    @Autowired
    private IShebeiInfoService shebeiInfoService;
    @Autowired
    private IWztaizhangService wztaizhangService;
    @Autowired
    private IWzconsumetaizhangService wzconsumetaizhangService;
    @Autowired
    private IWzconsumetaizhangDetailService wzconsumetaizhangDetailService;
    @Autowired
    private IWzliaocangService wzliaocangService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.WZXHTAIZHANG);//原材料物资消耗台账=26
        if (null == selectsysconfigone) {
            log.info(String.format("未获取到原材料物资消耗台账定时任务的配置信息" + DateUtils.now()));
            return;
        }
        //当前数据检测到的目标id
        Integer curid = selectsysconfigone.getCurid();
        Date date = new Date();
        String datanyr = NumberUtil.Stringnyr(date);//格式化后的时间
        Date datanyr1 = NumberUtil.datanyr(datanyr);
        List<BhzCementStatistics> selectbhzstaones = bhzCementStatisticsService.selectbhzstaList(curid,0,datanyr1);//没有进行过台账统计的数据
        if (null == selectbhzstaones || selectbhzstaones.size() == 0) {
            log.info(String.format("暂无需要计算原材消耗的拌合站统计数据" + DateUtils.now()));
            return;
        }
        int id = 0;

        for (BhzCementStatistics bhzCementStatistics : selectbhzstaones) {
            id = bhzCementStatistics.getId();
            String shebeiNo = bhzCementStatistics.getShebeiNo();//设备编号
            String formulaNo = bhzCementStatistics.getFormulaNo();//配方号
            List<BhzCementDetailStatistics> bhzCementDetailStatistics = bhzCementDetailStatisticsService.selectdetaillist(id);
            try {
                ShebeiInfo selectshebeione = shebeiInfoService.selectshebeione(shebeiNo);
                String sysOrgCode = selectshebeione.getSysOrgCode();
                //判断是否有设备
                if (null == selectshebeione) {
                    log.info(String.format("暂无该拌合站设备" + DateUtils.now()));
                    continue;
                }
                Shigongphb queryoneCode = shigongphbService.queryoneCode(formulaNo);
                if (queryoneCode == null) {
                    bhzCementStatisticsService.updateistongji(id, 20);
                    log.info(String.format("暂无相匹配的配料单数据" + DateUtils.now()));
                    continue;
                }else {
                    String lcNo1 = queryoneCode.getLc1();//水泥料仓编号
                    String lcNo2 = queryoneCode.getLc2();//粉煤灰料仓编号
                    String lcNo3 = queryoneCode.getLc3();//矿粉料仓编号
                    String lcNo4 = queryoneCode.getLc4();//骨料1料仓编号
                    String lcNo5 = queryoneCode.getLc5();//骨料2料仓编号
                    String lcNo6 = queryoneCode.getLc6();//骨料3料仓编号
                    String lcNo7 = queryoneCode.getLc7();//骨料4料仓编号
                    String lcNo8 = queryoneCode.getLc8();//外加剂1料仓编号
                    String lcNo9 = queryoneCode.getLc9();//外加剂2料仓编号
                    String lcNo10 = queryoneCode.getLc10();//外加剂3料仓编号
                    String lcNo11 = queryoneCode.getLc11();//水料仓编号
                    String lcNo12 = queryoneCode.getLc12();//骨料5料仓编号
                    String lcNo13 = queryoneCode.getLc13();//水泥2料仓编号
                    String m1 = queryoneCode.getM1();//水泥
                    String m2 = queryoneCode.getM2();//粉煤灰
                    String m3 = queryoneCode.getM3();//矿粉
                    String m4 = queryoneCode.getM4();//骨料1
                    String m5 = queryoneCode.getM5();//骨料2
                    String m6 = queryoneCode.getM6();//骨料3
                    String m7 = queryoneCode.getM7();//骨料4
                    String m8 = queryoneCode.getM8();//外加剂1
                    String m9 = queryoneCode.getM9();//外加剂2
                    String m10 = queryoneCode.getM10();//外加剂3
                    String m11 = queryoneCode.getM11();//水
                    String m12 = queryoneCode.getM12();//骨料5
                    String m13 = queryoneCode.getM13();//水泥2
                    if (bhzCementDetailStatistics.size()>0){
                        for (BhzCementDetailStatistics bhzCementDetailStatis : bhzCementDetailStatistics){
                            Double realityNumber = bhzCementDetailStatis.getRealityNumber();
//                            Integer materialeType = bhzCementDetailStatis.getMaterialeType();
                            String materialeName = bhzCementDetailStatis.getMaterialeName();
                            if (materialeName.equals(m1) && lcNo1 != null){
                                List<Wztaizhang> selectlcones = wztaizhangService.selectlc(lcNo1);
                                if (selectlcones != null && selectlcones.size()>0){
                                    select(bhzCementStatistics,sysOrgCode,bhzCementDetailStatis,selectlcones);
                                } else {
                                    bhzCementStatisticsService.updateistongji(id, 10);
                                    log.info(String.format("暂无相匹配的料仓" + DateUtils.now()));
                                }
                            }else if (materialeName.equals(m2) && lcNo2 != null){
                                List<Wztaizhang> selectlcones = wztaizhangService.selectlc(lcNo2);
                                if (selectlcones != null && selectlcones.size()>0){
                                    select(bhzCementStatistics,sysOrgCode,bhzCementDetailStatis,selectlcones);
                                } else {
                                    bhzCementStatisticsService.updateistongji(id, 10);
                                    log.info(String.format("暂无相匹配的料仓" + DateUtils.now()));
                                }
                            }else if (materialeName.equals(m3) && lcNo3 != null){
                                List<Wztaizhang> selectlcones = wztaizhangService.selectlc(lcNo3);
                                if (selectlcones != null && selectlcones.size()>0){
                                    select(bhzCementStatistics,sysOrgCode,bhzCementDetailStatis,selectlcones);
                                } else {
                                    bhzCementStatisticsService.updateistongji(id, 10);
                                    log.info(String.format("暂无相匹配的料仓" + DateUtils.now()));
                                }
                            } else if (materialeName.equals(m4) && lcNo4 != null){
                                List<Wztaizhang> selectlcones = wztaizhangService.selectlc(lcNo4);
                                if (selectlcones != null && selectlcones.size()>0){
                                    select(bhzCementStatistics,sysOrgCode,bhzCementDetailStatis,selectlcones);
                                } else {
                                    bhzCementStatisticsService.updateistongji(id, 10);
                                    log.info(String.format("暂无相匹配的料仓" + DateUtils.now()));
                                }
                            } else if (materialeName.equals(m5) && lcNo5 != null){
                                List<Wztaizhang> selectlcones = wztaizhangService.selectlc(lcNo5);
                                if (selectlcones != null && selectlcones.size()>0){
                                    select(bhzCementStatistics,sysOrgCode,bhzCementDetailStatis,selectlcones);
                                } else {
                                    bhzCementStatisticsService.updateistongji(id, 10);
                                    log.info(String.format("暂无相匹配的料仓" + DateUtils.now()));
                                }
                            } else if (materialeName.equals(m6) && lcNo6 != null){
                                List<Wztaizhang> selectlcones = wztaizhangService.selectlc(lcNo6);
                                if (selectlcones != null && selectlcones.size()>0){
                                    select(bhzCementStatistics,sysOrgCode,bhzCementDetailStatis,selectlcones);
                                } else {
                                    bhzCementStatisticsService.updateistongji(id, 10);
                                    log.info(String.format("暂无相匹配的料仓" + DateUtils.now()));
                                }
                            } else if (materialeName.equals(m7) && lcNo7 != null){
                                List<Wztaizhang> selectlcones = wztaizhangService.selectlc(lcNo7);
                                if (selectlcones != null && selectlcones.size()>0){
                                    select(bhzCementStatistics,sysOrgCode,bhzCementDetailStatis,selectlcones);
                                } else {
                                    bhzCementStatisticsService.updateistongji(id, 10);
                                    log.info(String.format("暂无相匹配的料仓" + DateUtils.now()));
                                }
                            } else if (materialeName.equals(m8) && lcNo8 != null){
                                List<Wztaizhang> selectlcones = wztaizhangService.selectlc(lcNo8);
                                if (selectlcones != null && selectlcones.size()>0){
                                    select(bhzCementStatistics,sysOrgCode,bhzCementDetailStatis,selectlcones);
                                } else {
                                    bhzCementStatisticsService.updateistongji(id, 10);
                                    log.info(String.format("暂无相匹配的料仓" + DateUtils.now()));
                                }
                            } else if (materialeName.equals(m9) && lcNo9 != null){
                                List<Wztaizhang> selectlcones = wztaizhangService.selectlc(lcNo9);
                                if (selectlcones != null && selectlcones.size()>0){
                                    select(bhzCementStatistics,sysOrgCode,bhzCementDetailStatis,selectlcones);
                                } else {
                                    bhzCementStatisticsService.updateistongji(id, 10);
                                    log.info(String.format("暂无相匹配的料仓" + DateUtils.now()));
                                }
                            } else if (materialeName.equals(m10) && lcNo10 != null){
                                List<Wztaizhang> selectlcones = wztaizhangService.selectlc(lcNo10);
                                if (selectlcones != null && selectlcones.size()>0){
                                    select(bhzCementStatistics,sysOrgCode,bhzCementDetailStatis,selectlcones);
                                } else {
                                    bhzCementStatisticsService.updateistongji(id, 10);
                                    log.info(String.format("暂无相匹配的料仓" + DateUtils.now()));
                                }
                            } else if (materialeName.equals(m11) && lcNo11 != null){
                                List<Wztaizhang> selectlcones = wztaizhangService.selectlc(lcNo11);
                                if (selectlcones != null && selectlcones.size()>0){
                                    select(bhzCementStatistics,sysOrgCode,bhzCementDetailStatis,selectlcones);
                                } else {
                                    bhzCementStatisticsService.updateistongji(id, 10);
                                    log.info(String.format("暂无相匹配的料仓" + DateUtils.now()));
                                }
                            } else if (materialeName.equals(m12) && lcNo12 != null){
                                List<Wztaizhang> selectlcones = wztaizhangService.selectlc(lcNo12);
                                if (selectlcones != null && selectlcones.size()>0){
                                    select(bhzCementStatistics,sysOrgCode,bhzCementDetailStatis,selectlcones);
                                } else {
                                    bhzCementStatisticsService.updateistongji(id, 10);
                                    log.info(String.format("暂无相匹配的料仓" + DateUtils.now()));
                                }
                            } else if (materialeName.equals(m13) && lcNo13 != null){
                                List<Wztaizhang> selectlcones = wztaizhangService.selectlc(lcNo13);
                                if (selectlcones != null && selectlcones.size()>0){
                                    select(bhzCementStatistics,sysOrgCode,bhzCementDetailStatis,selectlcones);
                                } else {
                                    bhzCementStatisticsService.updateistongji(id, 10);
                                    log.info(String.format("暂无相匹配的料仓" + DateUtils.now()));
                                }
                            }else {
                                bhzCementStatisticsService.updateistongji(id, 10);
                                log.info(String.format("暂无相匹配的料仓" + DateUtils.now()));
                            }
                        }

                    }else {
                        bhzCementStatisticsService.updateistongji(id, 30);
                        log.info(String.format("没有材料统计信息" + DateUtils.now()));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                bhzCementStatisticsService.updateistongji(id, 40);
            }log.info(String.format("原材料物资消耗台账！   时间" +DateUtils.now(),"当前判断到"+id));
        }
        sysConfigService.updateSysConfig(JobUtil.WZXHTAIZHANG,id);
    }

    private void select(BhzCementStatistics bhzCementStatistics, String sysOrgCode, BhzCementDetailStatistics bhzCementDetailStatis, List<Wztaizhang> selectlcones) {
        int ids =0;
        String projectName = bhzCementStatistics.getProjectName();//工程名称
        String poureLocation = bhzCementStatistics.getPoureLocation();//浇筑部位
        String strengthRank = bhzCementStatistics.getStrengthRank();//强度等级
        Double estimateNumber = bhzCementStatistics.getEstimateNumber();//方量
        Date statisticsTime = bhzCementStatistics.getStatisticsTime();//统计时间
        Double realityNumber = bhzCementDetailStatis.getRealityNumber();
        String materialeName = bhzCementDetailStatis.getMaterialeName();
        Wzconsumetaizhang selectwzxiaohao = wzconsumetaizhangService.selectwzxiaohao(sysOrgCode,projectName,poureLocation,strengthRank);
        if (selectwzxiaohao == null){
            Wzconsumetaizhang wzconsumetaizhang = new Wzconsumetaizhang();
            wzconsumetaizhang.setProjectName(projectName);
            wzconsumetaizhang.setPoureLocation(poureLocation);
            wzconsumetaizhang.setEstimateNumber(estimateNumber);
            wzconsumetaizhang.setStarttim(statisticsTime);
            wzconsumetaizhang.setEndtim(statisticsTime);
            wzconsumetaizhang.setStrengthRank(strengthRank);
            wzconsumetaizhang.setSysOrgCode(sysOrgCode);
            wzconsumetaizhangService.save(wzconsumetaizhang);
            ids = wzconsumetaizhang.getId();
        }else {
            Double estimateNumbersum = selectwzxiaohao.getEstimateNumber() + estimateNumber;
            Wzconsumetaizhang wzconsumetaizhang = new Wzconsumetaizhang();
            wzconsumetaizhang.setId(selectwzxiaohao.getId());
            wzconsumetaizhang.setEstimateNumber(estimateNumbersum);
            wzconsumetaizhang.setEndtim(statisticsTime);
            wzconsumetaizhangService.updateById(wzconsumetaizhang);
            ids = wzconsumetaizhang.getId();
        }
        WzconsumetaizhangDetail wzconsumetaizhangDetail = wzconsumetaizhangDetailService.selectwzxiaohaodetail(materialeName,ids);
        if (wzconsumetaizhangDetail != null){
            count(wzconsumetaizhangDetail,ids,realityNumber);
        }else {
            addwzxiaohao(ids,selectlcones,materialeName);
            WzconsumetaizhangDetail wzconsumetaizhangDetail1 = wzconsumetaizhangDetailService.selectwzxiaohaodetail(materialeName,ids);
            count(wzconsumetaizhangDetail1,ids,realityNumber);
        }
        BhzCementStatistics bhzCementStatistics1 = new BhzCementStatistics();
        bhzCementStatistics1.setId(bhzCementStatistics.getId());
        bhzCementStatistics1.setIstongji(1);
        bhzCementStatisticsService.updateById(bhzCementStatistics1);
    }

    private void addwzxiaohao(int ids, List<Wztaizhang> selectlcones, String materialeName) {
        double jingzsum = 0.0;
        String pici = "";
        String lcNo = "";
        String lcname = "";
        for (Wztaizhang wztaizhang : selectlcones){
            String jingzhong = wztaizhang.getJingzhongt();
            String jingzhongt = String .format("%.2f",Double.valueOf(jingzhong));
            jingzsum = jingzsum + Double.parseDouble(jingzhongt);
            if (pici.equals("")){
                pici =  ""+wztaizhang.getPici()+"";
            }else {
                pici = pici + "," + wztaizhang.getPici()+"";
            }
            lcNo = wztaizhang.getLcbianhao();
            Wzliaocang wzliaocang = wzliaocangService.queryselectone(lcNo);
            lcname = wzliaocang.getName();
        }
        WzconsumetaizhangDetail wzconsumetaizhangDetail1 = new WzconsumetaizhangDetail();
        wzconsumetaizhangDetail1.setXhId(ids);
        wzconsumetaizhangDetail1.setLcbianhao(lcNo);
        wzconsumetaizhangDetail1.setName(lcname);
        wzconsumetaizhangDetail1.setMaterialeName(materialeName);
        wzconsumetaizhangDetail1.setCailiaoshengyut(jingzsum);
        wzconsumetaizhangDetail1.setPicizhong(jingzsum);
        wzconsumetaizhangDetail1.setRealityNumber(0.0);
        wzconsumetaizhangDetail1.setPici(pici);
        wzconsumetaizhangDetailService.save(wzconsumetaizhangDetail1);
    }

    private void count(WzconsumetaizhangDetail wzconsumetaizhangDetail,Integer ids,Double realityNumber) {
        String lcNo = wzconsumetaizhangDetail.getLcbianhao();
        Double realityNumber1 = wzconsumetaizhangDetail.getRealityNumber() + realityNumber/1000;
        Double cailiaosysum = wzconsumetaizhangDetail.getCailiaoshengyut() - realityNumber/1000;
        wzconsumetaizhangDetailService.updatedetail(ids,realityNumber1,cailiaosysum,lcNo);
    }
}
