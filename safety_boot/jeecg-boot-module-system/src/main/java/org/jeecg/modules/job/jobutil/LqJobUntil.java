package org.jeecg.modules.job.jobutil;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.bhzMaterialCfg.entity.BhzMaterialCfg;
import com.trtm.iot.bhzMaterialCfg.service.IBhzMaterialCfgService;
import com.trtm.iot.bhzStatistics.service.IBhzCementStatisticsService;
import com.trtm.iot.bhzcfg.entity.BhzChaobiaoCfg;
import com.trtm.iot.bhzlqjipeifanwei.entity.BhzLqJipeiFanwei;
import com.trtm.iot.bhzlqjipeifanwei.service.IBhzLqJipeiFanweiService;
import com.trtm.iot.bhzlqjipeistatistics.entity.BhzLqJipeiStatistics;
import com.trtm.iot.bhzlqjipeistatistics.service.IBhzLqJipeiStatisticsService;
import com.trtm.iot.bhzlqjipeistatistics.vo.BhzLqJipei;
import com.trtm.iot.bhzlqshaifenshiyan.entity.BhzLqShaifenShiyan;
import com.trtm.iot.bhzlqshaifenshiyan.service.IBhzLqShaifenShiyanService;
import com.trtm.iot.lqbhz.entity.BhzLqBases;
import com.trtm.iot.lqbhz.entity.BhzLqCailiao;
import com.trtm.iot.lqbhz.service.IBhzLqBasesService;
import com.trtm.iot.lqbhz.service.IBhzLqCailiaoService;
import com.trtm.iot.lqbhzStatistics.entity.BhzLqStatistics;
import com.trtm.iot.lqbhzStatistics.service.IBhzLqStatisticsService;
import com.trtm.iot.lqbhzcailiaoStatistics.entity.BhzLqCailiaoStatistics;
import com.trtm.iot.lqbhzcailiaoStatistics.service.IBhzLqCailiaoStatisticsService;
import com.trtm.iot.lqbhzrecipe.entity.BhzLqPhbZibiao;
import com.trtm.iot.lqbhzrecipe.entity.BhzLqRecipe;
import com.trtm.iot.lqbhzrecipe.service.IBhzLqPhbZibiaoService;
import com.trtm.iot.lqbhzrecipe.service.IBhzLqRecipeService;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import com.trtm.iot.system.entity.ShebeiInfo;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sun.rmi.runtime.Log;


import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;

/**
 * 沥青工具类
 */

@Component
@Slf4j
public class LqJobUntil {

    @Autowired
    private ISysConfigService sysConfigService;//定时任务事务层
    @Autowired
    private IBhzCementStatisticsService bhzCementStatisticsService;
    @Autowired
    private IBhzLqBasesService bhzLqBasesService;
    @Autowired
    private IBhzLqCailiaoService bhzLqCailiaoService;
    @Autowired
    private IBhzLqRecipeService bhzLqRecipeService;
    @Autowired
    private IBhzLqPhbZibiaoService bhzLqPhbZibiaoService;
    @Autowired
    private IBhzLqStatisticsService bhzLqStatisticsService;
    @Autowired
    private IBhzLqCailiaoStatisticsService bhzLqCailiaoStatisticsService;
    @Autowired
    private IBhzMaterialCfgService bhzMaterialCfgService;
    @Autowired
    private IBhzLqJipeiFanweiService bhzLqJipeiFanweiService;
    @Autowired
    private IBhzLqJipeiStatisticsService bhzLqJipeiStatisticsService;
    @Autowired
    private IBhzLqShaifenShiyanService bhzLqShaifenShiyanService;

    /**
     * 材料超标判定并改变材料表bhz_lq_cailiao的超标数据
     * 判断材料超标------------------------沥青材料表------------材料超标预警阈值------是否报警---设备编号----lqbhzguid--总产量----------沥青设备表信息-----lq拌合生成数据----出料时间
     *
     * @return Map(level ( 所有材料超标的最高级别), final_content（材料超标的内容）)
     */
    public Map lqCailiaoOver(List<BhzLqCailiao> selectcailiaolist, List<BhzChaobiaoCfg> selectchaobiaolist, int is_call, String shebeibianhao, String guid, double zongchanliang,
                             ShebeiInfo selectshebeione, BhzLqBases selectlqbhzone, String chuliaoshijian) {

        Map result = new HashMap<>();
        DecimalFormat df = new DecimalFormat("######0.00");
        int final_over_level = 0;
        StringBuilder final_content = new StringBuilder();
        String hhllx = null;
        float primary = 0;
        float middle = 0;
        float advanced = 0;

        //外掺法理论油石比之和
        double wcfllysbSum = 0;
        //沥青用量
        double liqingnum = 0;
        //油石比
        double youshibi = 0;
        //理论油石比
        double llysb = 0;
        if (StrUtil.isNotBlank(selectlqbhzone.getYoushibi())) {
            youshibi = Double.parseDouble(selectlqbhzone.getYoushibi());
        }
        if (StrUtil.isNotBlank(selectlqbhzone.getLlysb())) {
            llysb = Double.parseDouble(selectlqbhzone.getLlysb());
        }

        //理论总重量
        double lilunzongliang = 0;
        //沥青理论用量
        double lqluzyl = 0;
        //需要计算实际总用量
        boolean jssjzyl = false;
        // 总产量为0需要计算
        if (zongchanliang == 0) {
            jssjzyl = true;
        }

        //重新计算总重量
        for (BhzLqCailiao bhzLqCailiao : selectcailiaolist) {
            if (jssjzyl) {
                zongchanliang += bhzLqCailiao.getShijiyongliang();
            }
            lilunzongliang += Convert.toFloat(bhzLqCailiao.getTheoryNumber(), 0f);
            if (liqingnum == 0) {
                if (null != bhzLqCailiao.getCailiaoming() && (bhzLqCailiao.getCailiaoming().contains("沥青")) ||
                        Convert.toInt(bhzLqCailiao.getCailiaoleixing(), 0) == 6) {
                    liqingnum = bhzLqCailiao.getShijiyongliang();
                    // 沥青理论用量
                    lqluzyl = Convert.toFloat(bhzLqCailiao.getTheoryNumber(), 0f);
                }
            }
        }

        if (jssjzyl) {
            bhzLqBasesService.updateZclByGuid(guid, zongchanliang);
        }
        if (selectshebeione.getInterfacetype() == 0) {
            //如果未采集到油石比,计算出油石比并更新数据库
            if (youshibi == 0 && liqingnum > 0 && zongchanliang > 0) {
                youshibi = liqingnum * 100 / (zongchanliang - liqingnum);
                bhzLqBasesService.updateYsbByGuid(guid, String.format("%.2f", youshibi));
            }
            //如果未采集到理论油石比，计算出理论油石比并更新数据库
            if (llysb == 0 && lqluzyl > 0 && lilunzongliang > 0) {
                llysb = lqluzyl * 100 / (lilunzongliang - lqluzyl);
                bhzLqBasesService.updateLlysbByGuid(String.format("%.2f", llysb), guid);
            }
        } else if (selectshebeione.getInterfacetype() == 1) {
            if (StringUtils.isNotBlank(selectlqbhzone.getFormulaNo())) {
                wcfllysbSum = bhzLqPhbZibiaoService.selectSum(selectlqbhzone.getFormulaNo(), selectlqbhzone.getShebeibianhao());
                if (wcfllysbSum > 0) {
                    //如果未采集到油石比,计算出油石比并更新数据库
                    if (youshibi == 0 && liqingnum > 0 && zongchanliang > 0) {
                        youshibi = liqingnum / zongchanliang * wcfllysbSum;
                        bhzLqBasesService.updateYsbByGuid(guid, String.format("%.2f", youshibi));
                    }
                    //如果未采集到理论油石比，计算出理论油石比并更新数据库
                    if (llysb == 0 && lqluzyl > 0 && lilunzongliang > 0) {
                        llysb = lqluzyl / lilunzongliang * wcfllysbSum;
                        bhzLqBasesService.updateLlysbByGuid(String.format("%.2f", llysb), guid);
                    }
                } else {
                    return null;
                }
            }
        }

//        //如果计算方式是外掺法，需要先将总数shijipb量减去沥青总重量
//        if (selectshebeione.getInterfacetype() == 1) {
//            zongchanliang = zongchanliang - liqingnum;
//            lilunzongliang = lilunzongliang - lqluzyl;
//        }
        double wulqzongchanliang = zongchanliang - liqingnum;
        StringBuffer a = new StringBuffer();
        for (BhzLqCailiao bhzLqCailiao : selectcailiaolist) {
            //实际用量
            double realnum = 0;
            //理论用量
            double theorynum = 0;
            //材料类型
            int cailiaoleixing = 0;

            //实际
            if (null != bhzLqCailiao.getShijiyongliang()) {
                realnum = bhzLqCailiao.getShijiyongliang();
            }
            //理论
            if (null != bhzLqCailiao.getTheoryNumber()) {
                theorynum = bhzLqCailiao.getTheoryNumber();
            }
            //材料
            if (null != bhzLqCailiao.getCailiaoleixing()) {
                cailiaoleixing = bhzLqCailiao.getCailiaoleixing();
            }

            String cailiaoming = "";
            if (null != bhzLqCailiao.getCailiaoming()) {
                cailiaoming = bhzLqCailiao.getCailiaoming();
            }

            //没采集到类型，重新判断
            boolean updateleixin = false;
            if (cailiaoleixing == 0) {
                cailiaoleixing = lqCailiaotype(cailiaoming);
                updateleixin = true;
            }

            double lilunpb = 0;
            double errornum = 0;
            double overnum = 0;
            if (null != bhzLqCailiao.getLilunpb()) {
                lilunpb = Convert.toFloat(bhzLqCailiao.getLilunpb(), 0f);
//                lilunpb = Double.parseDouble(bhzLqCailiao.getLilunpb());
            }
            double shijipd = 0;
            if (selectshebeione.getInterfacetype() == 0) {
                shijipd = realnum / zongchanliang * 100;
                //如果采集到的是理论用量，则计算理论配比
                if (lilunpb == 0 && theorynum > 0) {
                    lilunpb = theorynum / lilunzongliang * 100;
                    bhzLqCailiaoService.updateLilunpb(Double.valueOf(df.format(lilunpb)), bhzLqCailiao.getId());
                }
            } else if (selectshebeione.getInterfacetype() == 1) {
                shijipd = realnum / zongchanliang * wcfllysbSum;
            }
            double wlqshijipd = realnum / wulqzongchanliang * 100;

            //如果理论配比为0，需要去匹配输入的理论配合比
            boolean autolilun = false;
            if (lilunpb == 0) {
                BhzLqRecipe lilun = null;
                BhzLqPhbZibiao lilun2 = null;
                if (cailiaoming.contains("沥青")) {
                    if (llysb > 0) {
                        lilunpb = llysb;
                        autolilun = true;
                    } else if (youshibi > 0) {
                        BhzLqRecipe bhzLqRecipe = null;
                        //如果设置为根据配合比编号理论配比
                        if (selectshebeione.getDtubaud() != null && selectshebeione.getDtubaud() == 1) { //按照配方号去计算
                            QueryWrapper<BhzLqRecipe> queryWrapper = new QueryWrapper<>();
                            queryWrapper.eq("shebeibianhao", shebeibianhao);
                            queryWrapper.eq("phbid", selectlqbhzone.getFormulaNo());
                            bhzLqRecipe = bhzLqRecipeService.getOne(queryWrapper);
                        } else {//按照油石比范围去计算
                            QueryWrapper<BhzLqRecipe> queryWrapper = new QueryWrapper<>();
                            queryWrapper.eq("shebeibianhao", shebeibianhao);
                            queryWrapper.gt("llysb", youshibi * 0.9);
                            queryWrapper.lt("llysb", youshibi * 1.1);
                            queryWrapper.eq("llmoren", 1);
                            queryWrapper.last("limit 1");
                            bhzLqRecipe = bhzLqRecipeService.getOne(queryWrapper);
                        }
                        if (StringUtils.isNotBlank(bhzLqRecipe.getGcmc())) {
                            bhzLqBasesService.updateGcmcByGuid(bhzLqRecipe.getGcmc(), guid);
                        }

                        if (null != bhzLqRecipe) {
                            hhllx = bhzLqRecipe.getHhllx();
                            // 理论配比 赋值 理论配合比的油石比
                            lilunpb = Double.parseDouble(bhzLqRecipe.getLlysb());
                            autolilun = true;
                            //如果未采集到理论油石比，则将自动匹配到的理论油石比写入数据库
                            if (lilunpb > 0) {
                                bhzLqBasesService.updateLlysbByGuid(String.valueOf(df.format(lilunpb)), guid);
                                if (null != bhzLqRecipe.getHhllx()) {
                                    bhzLqBasesService.updatehunheliaoidByGuid(bhzLqRecipe.getHhllx(), guid);
                                }
                                if (selectshebeione.getDtubaud() != null && selectshebeione.getDtubaud() == 1) {
                                    lilunpb = Double.parseDouble(bhzLqPhbZibiaoService.selectTianjiaji(shebeibianhao, selectlqbhzone.getFormulaNo(), cailiaoming).getTianjiaji());
                                }
                            }
                        }
                    }
                } else if (llysb > 0) {
                    if (selectshebeione.getDtubaud() != null && selectshebeione.getDtubaud() == 1) { //按照配方号去计算
                        lilun2 = bhzLqPhbZibiaoService.selectTianjiaji(shebeibianhao, selectlqbhzone.getFormulaNo(), cailiaoming);
                    } else {
                        lilun2 = bhzLqPhbZibiaoService.selectTianjiajis(shebeibianhao, llysb, cailiaoming);
                    }
                    if (null != lilun2) {
                        lilunpb = Double.parseDouble(lilun2.getTianjiaji());
                        autolilun = true;
                    }
                } else if (youshibi > 0) {
                    if (selectshebeione.getDtubaud() != null && selectshebeione.getDtubaud() == 1) { //按照配方号去计算
                        lilun2 = bhzLqPhbZibiaoService.selectTianjiaji(shebeibianhao, selectlqbhzone.getFormulaNo(), cailiaoming);
                    } else {
                        lilun2 = bhzLqPhbZibiaoService.selectTianjiajiByYsb(shebeibianhao, Double.toString(youshibi * 0.9), Double.toString(youshibi * 1.1), cailiaoming);
                    }
                    if (null != lilun2) {
                        lilunpb = Double.parseDouble(lilun2.getTianjiaji());
                        autolilun = true;
                    }
                }
            }
//            else{
//                BhzLqRecipe  bhzLqRecipe=null;
//                if(selectshebeione.getDtubaud()!=null&&selectshebeione.getDtubaud()==1){ //按照配方号去计算
//                    QueryWrapper<BhzLqRecipe> queryWrapper = new QueryWrapper<>();
//                    queryWrapper.eq("shebeibianhao",shebeibianhao);
//                    queryWrapper.eq("phbid",selectlqbhzone.getFormulaNo());
//                      bhzLqRecipe = bhzLqRecipeService.getOne(queryWrapper);
//                }else{
//                    QueryWrapper<BhzLqRecipe> queryWrapper = new QueryWrapper<>();
//                    queryWrapper.eq("shebeibianhao",shebeibianhao);
//                    queryWrapper.gt("llysb",youshibi*0.9);
//                    queryWrapper.lt("llysb",youshibi*1.1);
//                    queryWrapper.eq("llmoren",1);
//                    queryWrapper.last("limit 1");
//                    bhzLqRecipe = bhzLqRecipeService.getOne(queryWrapper);
//                }
//                if (null != bhzLqRecipe){
//                        BigDecimal b = new BigDecimal(bhzLqRecipe.getLlysb());
//                        double lilunpbs = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
//                        if(llysb==0){
//                            bhzLqBasesService.updateLlysbByGuid(Double.toString(lilunpbs),guid);
//                        }
//                        if(null!=bhzLqRecipe.getHhllx()){
//                            bhzLqBasesService.updatehunheliaoidByGuid(bhzLqRecipe.getHhllx(),guid);
//                        }
//                }
//            }
            if (lilunpb > 0) {
                errornum = shijipd - lilunpb;
                overnum = shijipd - lilunpb;
            }

            if (Double.isNaN(overnum)) {
                overnum = 0;
            }
            primary = 1;
            middle = 3;
            advanced = 5;
            BhzLqCailiao bhzLqCailiaoCun = new BhzLqCailiao();
            BhzChaobiaoCfg cfg = null;


            bhzLqCailiaoCun.setWucha(errornum);
            bhzLqCailiaoCun.setChaobiao(overnum);
            bhzLqCailiaoCun.setShijipb(String.format("%.2f", shijipd));
            if (!cailiaoming.contains("沥青")) {
                String formulaNo = selectlqbhzone.getFormulaNo();
                List<BhzLqJipei> list = getshaifenshiyan(shebeibianhao, bhzLqCailiao.getCailiaoleixing(), formulaNo);
                if (list.size() > 0) {
                    double hechengjipei = 0.0;
                    for (BhzLqJipei bhzLqJipei : list) {
                        BhzLqJipeiStatistics bhzLqJipeiStatistics = new BhzLqJipeiStatistics();
                        bhzLqJipeiStatistics.setZhongzhi(bhzLqJipei.getZhongzhi());
                        bhzLqJipeiStatistics.setXiaxian(bhzLqJipei.getXiaxian());
                        bhzLqJipeiStatistics.setShangxian(bhzLqJipei.getShangxian());
                        bhzLqJipeiStatistics.setShaikong(bhzLqJipei.getShaikong());
                        bhzLqJipeiStatistics.setBaseid(bhzLqCailiao.getBaseGuid());
                        bhzLqJipeiStatistics.setSbjno(shebeibianhao);
                        bhzLqJipeiStatistics.setChuliaoshijian(selectlqbhzone.getChuliaoshijian());
                        BhzLqJipeiStatistics bhzLqJipeiStatistics1 = bhzLqJipeiStatisticsService.selectone(bhzLqCailiao.getBaseGuid(), shebeibianhao, bhzLqJipei.getShaikong());
                        if (bhzLqJipeiStatistics1 != null) {
                            hechengjipei = bhzLqJipeiStatistics1.getHechengjipei() + bhzLqJipei.getHechengjipei() * wlqshijipd / 100;
                            if (hechengjipei > 100) {
                                hechengjipei = 100.00;
                            }
                            bhzLqJipeiStatistics.setHechengjipei(hechengjipei);
                            bhzLqJipeiStatistics.setId(bhzLqJipeiStatistics1.getId());
                            bhzLqJipeiStatistics.setUpdatetime(new Date());
                            bhzLqJipeiStatisticsService.updateById(bhzLqJipeiStatistics);
                        } else {
                            hechengjipei = bhzLqJipei.getHechengjipei() * wlqshijipd / 100;
                            bhzLqJipeiStatistics.setHechengjipei(hechengjipei);
                            bhzLqJipeiStatistics.setCreatetime(new Date());
                            bhzLqJipeiStatisticsService.save(bhzLqJipeiStatistics);
                        }
                    }
                }
            }
            if (updateleixin) {
                bhzLqCailiaoCun.setCailiaoleixing(cailiaoleixing);
            }
            if (autolilun) {
                bhzLqCailiaoCun.setLilunpb(String.format("%.2f", lilunpb));
            }
            if (selectshebeione.getInterfacetype() == 0) {
                if (theorynum == 0) {
                    theorynum = zongchanliang * lilunpb / 100;
                    bhzLqCailiaoCun.setTheoryNumber(theorynum);
                } else {
                    bhzLqCailiaoCun.setTheoryNumber(theorynum);
                }
            } else if (selectshebeione.getInterfacetype() == 1) {
                if (theorynum == 0) {
                    theorynum = zongchanliang / wcfllysbSum *lilunpb;
                    bhzLqCailiaoCun.setTheoryNumber(theorynum);
                } else {
                    bhzLqCailiaoCun.setTheoryNumber(theorynum);
                }
            }
            bhzLqCailiaoCun.setTheoryNumber(theorynum);
            if (null != selectchaobiaolist) {
                cfg = findOneByField(selectchaobiaolist, "material_type", cailiaoleixing);
            }

            if (null != cfg) {
                Object primary1 = cfg.getRimary();
                Object middle1 = cfg.getMiddle();
                Object advanced1 = cfg.getAdvanced();
                if (null != primary1) {
                    primary = cfg.getRimary();
                }
                if (null != middle1) {
                    middle = cfg.getMiddle();
                }
                if (null != advanced1) {
                    advanced = cfg.getAdvanced();
                }
            }
            int chaobiaodengji = 0;
            if (overnum < 0) {  //如果小于0就把值变为正数
                overnum = -overnum;
            }
            if (overnum > 0 && overnum <= primary) {
                chaobiaodengji = 0;
            } else if (overnum > primary && overnum <= middle) {
                chaobiaodengji = 1;
            } else if (overnum > middle && overnum < advanced) {
                chaobiaodengji = 2;
            } else if (overnum > advanced) {
                chaobiaodengji = 3;
            }
            if (chaobiaodengji > final_over_level) {
                final_over_level = chaobiaodengji;
            }
            bhzLqCailiaoCun.setChaobiaodengji(chaobiaodengji);
            bhzLqCailiaoCun.setChujichaobiao((double) primary);
            bhzLqCailiaoCun.setZhongjichaobiao((double) middle);
            bhzLqCailiaoCun.setGaojichaobiao((double) advanced);
            bhzLqCailiaoCun.setId(bhzLqCailiao.getId());
            bhzLqCailiaoService.updateById(bhzLqCailiaoCun);
            if (chaobiaodengji == 0) {
                continue;
            }
            final_content.append(String.format("%1$s超%2$.2f%%", cailiaoming, overnum));
        }
        result.put("level", final_over_level);
        result.put("final_content", final_content.toString());
        result.put("hhllx", hhllx);
        return result;

    }

    /**
     * 材料超标判定并改变材料表bhz_lq_cailiao的超标数据
     * 判断材料超标------------------------沥青材料表------------材料超标预警阈值------是否报警---设备编号----lqbhzguid--总产量----------沥青设备表信息-----lq拌合生成数据----出料时间
     *
     * @return Map(level ( 所有材料超标的最高级别), final_content（材料超标的内容）)
     */
    public Map lqCailiaoOver47(List<BhzLqCailiao> selectcailiaolist, List<BhzChaobiaoCfg> selectchaobiaolist, int is_call, String shebeibianhao, String guid, double zongchanliang,
                               ShebeiInfo selectshebeione, BhzLqBases selectlqbhzone, String chuliaoshijian) {


        Map result = new HashMap<>();
        DecimalFormat df = new DecimalFormat("######0.00");
        int final_over_level = 0;
        StringBuilder final_content = new StringBuilder();
        String hhllx = null;
        float primary = 0;
        float middle = 0;
        float advanced = 0;

        //沥青用量
        double liqingnum = 0;
        //油石比
        double youshibi = 0;
        //理论油石比
        double llysb = 0;
        if (StrUtil.isNotBlank(selectlqbhzone.getYoushibi())) {
            youshibi = Double.parseDouble(selectlqbhzone.getYoushibi());
        }
        if (StrUtil.isNotBlank(selectlqbhzone.getLlysb())) {
            llysb = Double.parseDouble(selectlqbhzone.getLlysb());
        }

        //理论总重量
        double lilunzongliang = 0;
        //沥青理论用量
        double lqluzyl = 0;
        //需要计算实际总用量
        boolean jssjzyl = false;
        // 总产量为0需要计算
        if (zongchanliang == 0) {
            jssjzyl = true;
        }

        //重新计算总重量
        for (BhzLqCailiao bhzLqCailiao : selectcailiaolist) {
            if (jssjzyl) {
                zongchanliang += bhzLqCailiao.getShijiyongliang();
            }
            lilunzongliang += Convert.toFloat(bhzLqCailiao.getTheoryNumber(), 0f);
            if (liqingnum == 0) {
                if (null != bhzLqCailiao.getCailiaoming() && (bhzLqCailiao.getCailiaoming().contains("沥青")) ||
                        Convert.toInt(bhzLqCailiao.getCailiaoleixing(), 0) == 6) {
                    liqingnum = bhzLqCailiao.getShijiyongliang();
                    // 沥青理论用量
                    lqluzyl = Convert.toFloat(bhzLqCailiao.getTheoryNumber(), 0f);
                }
            }
        }

        if (jssjzyl) {
            bhzLqBasesService.updateZclByGuid(guid, zongchanliang);
        }
        //如果未采集到油石比,计算出油石比并更新数据库
        if (youshibi == 0 && liqingnum > 0 && zongchanliang > 0) {
            youshibi = liqingnum * 100 / (zongchanliang);
            bhzLqBasesService.updateYsbByGuid(guid, String.format("%.2f", youshibi));
        }
        //如果未采集到理论油石比，计算出理论油石比并更新数据库
        if (llysb == 0 && lqluzyl > 0 && lilunzongliang > 0) {
            llysb = lqluzyl * 100 / (lilunzongliang - lqluzyl);
            bhzLqBasesService.updateLlysbByGuid(String.format("%.2f", llysb), guid);
        }

        //如果计算方式是外掺法，需要先将总数shijipb量减去沥青总重量
        if (selectshebeione.getInterfacetype() == 1) {
            zongchanliang = zongchanliang - liqingnum;
            lilunzongliang = lilunzongliang - lqluzyl;
        }
        double wulqzongchanliang = zongchanliang - liqingnum;
        StringBuffer a = new StringBuffer();
        for (BhzLqCailiao bhzLqCailiao : selectcailiaolist) {
            //实际用量
            double realnum = 0;
            //理论用量
            double theorynum = 0;
            //材料类型
            int cailiaoleixing = 0;

            //实际
            if (null != bhzLqCailiao.getShijiyongliang()) {
                realnum = bhzLqCailiao.getShijiyongliang();
            }
            //理论
            if (null != bhzLqCailiao.getTheoryNumber()) {
                theorynum = bhzLqCailiao.getTheoryNumber();
            }
            //材料
            if (null != bhzLqCailiao.getCailiaoleixing()) {
                cailiaoleixing = bhzLqCailiao.getCailiaoleixing();
            }

            String cailiaoming = "";
            if (null != bhzLqCailiao.getCailiaoming()) {
                cailiaoming = bhzLqCailiao.getCailiaoming();
            }

            //没采集到类型，重新判断
            boolean updateleixin = false;
            if (cailiaoleixing == 0) {
                cailiaoleixing = lqCailiaotype(cailiaoming);
                updateleixin = true;
            }

            double lilunpb = 0;
            double errornum = 0;
            double overnum = 0;
            if (null != bhzLqCailiao.getLilunpb()) {
                lilunpb = Convert.toFloat(bhzLqCailiao.getLilunpb(), 0f);
//                lilunpb = Double.parseDouble(bhzLqCailiao.getLilunpb());
            }
            double shijipd = realnum / zongchanliang * 100;
            double wlqshijipd = realnum / wulqzongchanliang * 100;
            //如果采集到的是理论用量，则计算理论配比
            if (lilunpb == 0 && theorynum > 0) {
                lilunpb = theorynum / lilunzongliang * 100;
                bhzLqCailiaoService.updateLilunpb(Double.valueOf(df.format(lilunpb)), bhzLqCailiao.getId());
            }
            //如果理论配比为0，需要去匹配输入的理论配合比
            boolean autolilun = false;
            if (lilunpb == 0) {
                BhzLqRecipe lilun = null;
                BhzLqPhbZibiao lilun2 = null;
                if (cailiaoming.contains("沥青")) {
                    if (llysb > 0) {
                        lilunpb = llysb;
                        autolilun = true;
                    } else if (youshibi > 0) {
                        BhzLqRecipe bhzLqRecipe = null;
                        //如果设置为根据配合比编号理论配比
                        if (selectshebeione.getDtubaud() != null && selectshebeione.getDtubaud() == 1) { //按照配方号去计算
                            QueryWrapper<BhzLqRecipe> queryWrapper = new QueryWrapper<>();
                            queryWrapper.eq("shebeibianhao", shebeibianhao);
                            queryWrapper.eq("phbid", selectlqbhzone.getFormulaNo());
                            bhzLqRecipe = bhzLqRecipeService.getOne(queryWrapper);
                        } else {//按照油石比范围去计算
                            QueryWrapper<BhzLqRecipe> queryWrapper = new QueryWrapper<>();
                            queryWrapper.eq("shebeibianhao", shebeibianhao);
                            queryWrapper.gt("llysb", youshibi * 0.9);
                            queryWrapper.lt("llysb", youshibi * 1.1);
                            queryWrapper.eq("llmoren", 1);
                            queryWrapper.last("limit 1");
                            bhzLqRecipe = bhzLqRecipeService.getOne(queryWrapper);
                        }
                        if (StringUtils.isNotBlank(bhzLqRecipe.getGcmc())) {
                            bhzLqBasesService.updateGcmcByGuid(bhzLqRecipe.getGcmc(), guid);
                        }

                        if (null != bhzLqRecipe) {
                            hhllx = bhzLqRecipe.getHhllx();
                            // 理论配比 赋值 理论配合比的油石比
                            lilunpb = Double.parseDouble(bhzLqRecipe.getLlysb());
                            autolilun = true;
                            //如果未采集到理论油石比，则将自动匹配到的理论油石比写入数据库
                            if (lilunpb > 0) {
                                bhzLqBasesService.updateLlysbByGuid(String.valueOf(df.format(lilunpb)), guid);
                                if (null != bhzLqRecipe.getHhllx()) {
                                    bhzLqBasesService.updatehunheliaoidByGuid(bhzLqRecipe.getHhllx(), guid);
                                }
                                if (selectshebeione.getDtubaud() != null && selectshebeione.getDtubaud() == 1) {
                                    lilunpb = Double.parseDouble(bhzLqPhbZibiaoService.selectTianjiaji(shebeibianhao, selectlqbhzone.getFormulaNo(), cailiaoming).getTianjiaji());
                                }
                            }
                        }
                    }
                } else if (llysb > 0) {
                    if (selectshebeione.getDtubaud() != null && selectshebeione.getDtubaud() == 1) { //按照配方号去计算
                        lilun2 = bhzLqPhbZibiaoService.selectTianjiaji(shebeibianhao, selectlqbhzone.getFormulaNo(), cailiaoming);
                    } else {
                        lilun2 = bhzLqPhbZibiaoService.selectTianjiajis(shebeibianhao, llysb, cailiaoming);
                    }
                    if (null != lilun2) {
                        lilunpb = Double.parseDouble(lilun2.getTianjiaji());
                        autolilun = true;
                    }
                } else if (youshibi > 0) {
                    if (selectshebeione.getDtubaud() != null && selectshebeione.getDtubaud() == 1) { //按照配方号去计算
                        lilun2 = bhzLqPhbZibiaoService.selectTianjiaji(shebeibianhao, selectlqbhzone.getFormulaNo(), cailiaoming);
                    } else {
                        lilun2 = bhzLqPhbZibiaoService.selectTianjiajiByYsb(shebeibianhao, Double.toString(youshibi * 0.9), Double.toString(youshibi * 1.1), cailiaoming);
                    }
                    if (null != lilun2) {
                        lilunpb = Double.parseDouble(lilun2.getTianjiaji());
                        autolilun = true;
                    }
                }
            }
//            else{
//                BhzLqRecipe  bhzLqRecipe=null;
//                if(selectshebeione.getDtubaud()!=null&&selectshebeione.getDtubaud()==1){ //按照配方号去计算
//                    QueryWrapper<BhzLqRecipe> queryWrapper = new QueryWrapper<>();
//                    queryWrapper.eq("shebeibianhao",shebeibianhao);
//                    queryWrapper.eq("phbid",selectlqbhzone.getFormulaNo());
//                      bhzLqRecipe = bhzLqRecipeService.getOne(queryWrapper);
//                }else{
//                    QueryWrapper<BhzLqRecipe> queryWrapper = new QueryWrapper<>();
//                    queryWrapper.eq("shebeibianhao",shebeibianhao);
//                    queryWrapper.gt("llysb",youshibi*0.9);
//                    queryWrapper.lt("llysb",youshibi*1.1);
//                    queryWrapper.eq("llmoren",1);
//                    queryWrapper.last("limit 1");
//                    bhzLqRecipe = bhzLqRecipeService.getOne(queryWrapper);
//                }
//                if (null != bhzLqRecipe){
//                        BigDecimal b = new BigDecimal(bhzLqRecipe.getLlysb());
//                        double lilunpbs = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
//                        if(llysb==0){
//                            bhzLqBasesService.updateLlysbByGuid(Double.toString(lilunpbs),guid);
//                        }
//                        if(null!=bhzLqRecipe.getHhllx()){
//                            bhzLqBasesService.updatehunheliaoidByGuid(bhzLqRecipe.getHhllx(),guid);
//                        }
//                }
//            }
            if (lilunpb > 0) {
                errornum = shijipd - lilunpb;
                overnum = shijipd - lilunpb;
            }

            if (Double.isNaN(overnum)) {
                overnum = 0;
            }
            primary = 1;
            middle = 3;
            advanced = 5;
            BhzLqCailiao bhzLqCailiaoCun = new BhzLqCailiao();
            BhzChaobiaoCfg cfg = null;


            bhzLqCailiaoCun.setWucha(errornum);
            bhzLqCailiaoCun.setChaobiao(overnum);
            bhzLqCailiaoCun.setShijipb(String.format("%.2f", shijipd));
            if (!cailiaoming.contains("沥青")) {
                String formulaNo = selectlqbhzone.getFormulaNo();
                List<BhzLqJipei> list = getshaifenshiyan(shebeibianhao, bhzLqCailiao.getCailiaoleixing(), formulaNo);
                if (list.size() > 0) {
                    double hechengjipei = 0.0;
                    for (BhzLqJipei bhzLqJipei : list) {
                        BhzLqJipeiStatistics bhzLqJipeiStatistics = new BhzLqJipeiStatistics();
                        bhzLqJipeiStatistics.setZhongzhi(bhzLqJipei.getZhongzhi());
                        bhzLqJipeiStatistics.setXiaxian(bhzLqJipei.getXiaxian());
                        bhzLqJipeiStatistics.setShangxian(bhzLqJipei.getShangxian());
                        bhzLqJipeiStatistics.setShaikong(bhzLqJipei.getShaikong());
                        bhzLqJipeiStatistics.setBaseid(bhzLqCailiao.getBaseGuid());
                        bhzLqJipeiStatistics.setSbjno(shebeibianhao);
                        bhzLqJipeiStatistics.setChuliaoshijian(selectlqbhzone.getChuliaoshijian());
                        BhzLqJipeiStatistics bhzLqJipeiStatistics1 = bhzLqJipeiStatisticsService.selectone(bhzLqCailiao.getBaseGuid(), shebeibianhao, bhzLqJipei.getShaikong());
                        if (bhzLqJipeiStatistics1 != null) {
                            hechengjipei = bhzLqJipeiStatistics1.getHechengjipei() + bhzLqJipei.getHechengjipei() * wlqshijipd / 100;
                            if (hechengjipei > 100) {
                                hechengjipei = 100.00;
                            }
                            bhzLqJipeiStatistics.setHechengjipei(hechengjipei);
                            bhzLqJipeiStatistics.setId(bhzLqJipeiStatistics1.getId());
                            bhzLqJipeiStatistics.setUpdatetime(new Date());
                            bhzLqJipeiStatisticsService.updateById(bhzLqJipeiStatistics);
                        } else {
                            hechengjipei = bhzLqJipei.getHechengjipei() * wlqshijipd / 100;
                            bhzLqJipeiStatistics.setHechengjipei(hechengjipei);
                            bhzLqJipeiStatistics.setCreatetime(new Date());
                            bhzLqJipeiStatisticsService.save(bhzLqJipeiStatistics);
                        }
                    }
                }
            }
            if (updateleixin) {
                bhzLqCailiaoCun.setCailiaoleixing(cailiaoleixing);
            }
            if (autolilun) {
                bhzLqCailiaoCun.setLilunpb(String.format("%.2f", lilunpb));
            }
            if (theorynum == 0) {
                theorynum = zongchanliang * lilunpb / 100;
                bhzLqCailiaoCun.setTheoryNumber(theorynum);
            } else {
                bhzLqCailiaoCun.setTheoryNumber(theorynum);
            }
            bhzLqCailiaoCun.setTheoryNumber(theorynum);
            if (null != selectchaobiaolist) {
                cfg = findOneByField(selectchaobiaolist, "material_type", cailiaoleixing);
            }

            if (null != cfg) {
                Object primary1 = cfg.getRimary();
                Object middle1 = cfg.getMiddle();
                Object advanced1 = cfg.getAdvanced();
                if (null != primary1) {
                    primary = cfg.getRimary();
                }
                if (null != middle1) {
                    middle = cfg.getMiddle();
                }
                if (null != advanced1) {
                    advanced = cfg.getAdvanced();
                }
            }
            int chaobiaodengji = 0;
            if (overnum < 0) {  //如果小于0就把值变为正数
                overnum = -overnum;
            }
            if (overnum > 0 && overnum <= primary) {
                chaobiaodengji = 0;
            } else if (overnum > primary && overnum <= middle) {
                chaobiaodengji = 1;
            } else if (overnum > middle && overnum < advanced) {
                chaobiaodengji = 2;
            } else if (overnum > advanced) {
                chaobiaodengji = 3;
            }
            if (chaobiaodengji > final_over_level) {
                final_over_level = chaobiaodengji;
            }
            bhzLqCailiaoCun.setChaobiaodengji(chaobiaodengji);
            bhzLqCailiaoCun.setChujichaobiao((double) primary);
            bhzLqCailiaoCun.setZhongjichaobiao((double) middle);
            bhzLqCailiaoCun.setGaojichaobiao((double) advanced);
            bhzLqCailiaoCun.setId(bhzLqCailiao.getId());
            bhzLqCailiaoService.updateById(bhzLqCailiaoCun);
            if (chaobiaodengji == 0) {
                continue;
            }
            final_content.append(String.format("%1$s超%2$.2f%%", cailiaoming, overnum));
        }
        result.put("level", final_over_level);
        result.put("final_content", final_content.toString());
        result.put("hhllx", hhllx);
        return result;

    }

    private List<BhzLqJipei> getshaifenshiyan(String shebeibianhao, Integer cailiaoid, String formulaNo) {
        List<BhzLqJipei> list = new ArrayList<>();
        BhzLqJipeiFanwei bhzLqJipeiFanwei = bhzLqJipeiFanweiService.selectone(shebeibianhao, formulaNo);
        if (bhzLqJipeiFanwei != null) {
            BhzLqShaifenShiyan bhzLqShaifenShiyan = bhzLqShaifenShiyanService.selectone(shebeibianhao, cailiaoid, bhzLqJipeiFanwei.getUuid());
            if (bhzLqShaifenShiyan != null) {
                BhzLqJipei bhzLqJipei1 = new BhzLqJipei();
                bhzLqJipei1.setShaikong("0.075");
                bhzLqJipei1.setHechengjipei(Double.parseDouble(bhzLqShaifenShiyan.getShaikong0075()));
                bhzLqJipei1.setShangxian(bhzLqJipeiFanwei.getSk0075shangxian());
                bhzLqJipei1.setXiaxian(bhzLqJipeiFanwei.getSk0075xiaxian());
                bhzLqJipei1.setZhongzhi(bhzLqJipeiFanwei.getSk0075zhongxian());
                list.add(bhzLqJipei1);
                BhzLqJipei bhzLqJipei2 = new BhzLqJipei();
                bhzLqJipei2.setShaikong("0.15");
                bhzLqJipei2.setHechengjipei(Double.parseDouble(bhzLqShaifenShiyan.getShaikong015()));
                bhzLqJipei2.setShangxian(bhzLqJipeiFanwei.getSk015shangxian());
                bhzLqJipei2.setXiaxian(bhzLqJipeiFanwei.getSk015xiaxian());
                bhzLqJipei2.setZhongzhi(bhzLqJipeiFanwei.getSk015zhongxian());
                list.add(bhzLqJipei2);
                BhzLqJipei bhzLqJipei3 = new BhzLqJipei();
                bhzLqJipei3.setShaikong("0.3");
                bhzLqJipei3.setHechengjipei(Double.parseDouble(bhzLqShaifenShiyan.getShaikong03()));
                bhzLqJipei3.setShangxian(bhzLqJipeiFanwei.getSk015shangxian());
                bhzLqJipei3.setXiaxian(bhzLqJipeiFanwei.getSk015xiaxian());
                bhzLqJipei3.setZhongzhi(bhzLqJipeiFanwei.getSk015zhongxian());
                list.add(bhzLqJipei3);
                BhzLqJipei bhzLqJipei4 = new BhzLqJipei();
                bhzLqJipei4.setShaikong("0.6");
                bhzLqJipei4.setHechengjipei(Double.parseDouble(bhzLqShaifenShiyan.getShaikong06()));
                bhzLqJipei4.setShangxian(bhzLqJipeiFanwei.getSk06shangxian());
                bhzLqJipei4.setXiaxian(bhzLqJipeiFanwei.getSk06xiaxian());
                bhzLqJipei4.setZhongzhi(bhzLqJipeiFanwei.getSk06zhongxian());
                list.add(bhzLqJipei4);
                BhzLqJipei bhzLqJipei5 = new BhzLqJipei();
                bhzLqJipei5.setShaikong("1.18");
                bhzLqJipei5.setHechengjipei(Double.parseDouble(bhzLqShaifenShiyan.getShaikong118()));
                bhzLqJipei5.setShangxian(bhzLqJipeiFanwei.getSk118shangxian());
                bhzLqJipei5.setXiaxian(bhzLqJipeiFanwei.getSk118xiaxian());
                bhzLqJipei5.setZhongzhi(bhzLqJipeiFanwei.getSk118zhongxian());
                list.add(bhzLqJipei5);
                BhzLqJipei bhzLqJipei6 = new BhzLqJipei();
                bhzLqJipei6.setShaikong("2.36");
                bhzLqJipei6.setHechengjipei(Double.parseDouble(bhzLqShaifenShiyan.getShaikong236()));
                bhzLqJipei6.setShangxian(bhzLqJipeiFanwei.getSk236shangxian());
                bhzLqJipei6.setXiaxian(bhzLqJipeiFanwei.getSk236xiaxian());
                bhzLqJipei6.setZhongzhi(bhzLqJipeiFanwei.getSk236zhongxian());
                list.add(bhzLqJipei6);
                BhzLqJipei bhzLqJipei7 = new BhzLqJipei();
                bhzLqJipei7.setShaikong("4.75");
                bhzLqJipei7.setHechengjipei(Double.parseDouble(bhzLqShaifenShiyan.getShaikong475()));
                bhzLqJipei7.setShangxian(bhzLqJipeiFanwei.getSk475shangxian());
                bhzLqJipei7.setXiaxian(bhzLqJipeiFanwei.getSk475xiaxian());
                bhzLqJipei7.setZhongzhi(bhzLqJipeiFanwei.getSk475zhongxian());
                list.add(bhzLqJipei7);
                BhzLqJipei bhzLqJipei8 = new BhzLqJipei();
                bhzLqJipei8.setShaikong("9.5");
                bhzLqJipei8.setHechengjipei(Double.parseDouble(bhzLqShaifenShiyan.getShaikong95()));
                bhzLqJipei8.setShangxian(bhzLqJipeiFanwei.getSk95shangxian());
                bhzLqJipei8.setXiaxian(bhzLqJipeiFanwei.getSk95xiaxian());
                bhzLqJipei8.setZhongzhi(bhzLqJipeiFanwei.getSk95zhongxian());
                list.add(bhzLqJipei8);
                BhzLqJipei bhzLqJipei9 = new BhzLqJipei();
                bhzLqJipei9.setShaikong("13.2");
                bhzLqJipei9.setHechengjipei(Double.parseDouble(bhzLqShaifenShiyan.getShaikong132()));
                bhzLqJipei9.setShangxian(bhzLqJipeiFanwei.getSk132shangxian());
                bhzLqJipei9.setXiaxian(bhzLqJipeiFanwei.getSk132xiaxian());
                bhzLqJipei9.setZhongzhi(bhzLqJipeiFanwei.getSk132zhongxian());
                list.add(bhzLqJipei9);
                BhzLqJipei bhzLqJipei10 = new BhzLqJipei();
                bhzLqJipei10.setShaikong("16");
                bhzLqJipei10.setHechengjipei(Double.parseDouble(bhzLqShaifenShiyan.getShaikong16()));
                bhzLqJipei10.setShangxian(bhzLqJipeiFanwei.getSk16shangxian());
                bhzLqJipei10.setXiaxian(bhzLqJipeiFanwei.getSk16xiaxian());
                bhzLqJipei10.setZhongzhi(bhzLqJipeiFanwei.getSk16zhongxian());
                list.add(bhzLqJipei10);
                BhzLqJipei bhzLqJipei11 = new BhzLqJipei();
                bhzLqJipei11.setShaikong("19");
                bhzLqJipei11.setHechengjipei(Double.parseDouble(bhzLqShaifenShiyan.getShaikong19()));
                bhzLqJipei11.setShangxian(bhzLqJipeiFanwei.getSk19shangxian());
                bhzLqJipei11.setXiaxian(bhzLqJipeiFanwei.getSk19xiaxian());
                bhzLqJipei11.setZhongzhi(bhzLqJipeiFanwei.getSk19zhongxian());
                list.add(bhzLqJipei11);
                BhzLqJipei bhzLqJipei12 = new BhzLqJipei();
                bhzLqJipei12.setShaikong("26.5");
                bhzLqJipei12.setHechengjipei(Double.parseDouble(bhzLqShaifenShiyan.getShaikong265()));
                bhzLqJipei12.setShangxian(bhzLqJipeiFanwei.getSk265shangxian());
                bhzLqJipei12.setXiaxian(bhzLqJipeiFanwei.getSk265xiaxian());
                bhzLqJipei12.setZhongzhi(bhzLqJipeiFanwei.getSk265zhongxian());
                list.add(bhzLqJipei12);
                BhzLqJipei bhzLqJipei13 = new BhzLqJipei();
                bhzLqJipei13.setShaikong("37.5");
                bhzLqJipei13.setHechengjipei(Double.parseDouble(bhzLqShaifenShiyan.getShaikong375()));
                bhzLqJipei13.setShangxian(bhzLqJipeiFanwei.getSk375shangxian());
                bhzLqJipei13.setXiaxian(bhzLqJipeiFanwei.getSk375xiaxian());
                bhzLqJipei13.setZhongzhi(bhzLqJipeiFanwei.getSk375zhongxian());
                list.add(bhzLqJipei13);
                BhzLqJipei bhzLqJipei14 = new BhzLqJipei();
                bhzLqJipei14.setShaikong("53");
                bhzLqJipei14.setHechengjipei(Double.parseDouble(bhzLqShaifenShiyan.getShaikong53()));
                bhzLqJipei14.setShangxian(bhzLqJipeiFanwei.getSk53shangxian());
                bhzLqJipei14.setXiaxian(bhzLqJipeiFanwei.getSk53xiaxian());
                bhzLqJipei14.setZhongzhi(bhzLqJipeiFanwei.getSk53zhongxian());
                list.add(bhzLqJipei14);
            }
        }
        return list;
    }

    /**
     * 根据材料名字判断材料类型
     */
    public static int lqCailiaotype(String materialname) {
        int materialType = MaterialTypeEnum.Other.getMaterialType();
        if (StrUtil.isNotBlank(materialname)) {
            if (isMatch(materialname, cementstrs)) {
                materialType = MaterialTypeEnum.Cement.getMaterialType();
            } else if (isMatch(materialname, fastrs)) {
                materialType = MaterialTypeEnum.FlyAsh.getMaterialType();
            } else if (isMatch(materialname, mpstrs)) {
                materialType = MaterialTypeEnum.MineralPowder.getMaterialType();
            } else if (isMatch(materialname, adtstrs)) {
                materialType = MaterialTypeEnum.Additive.getMaterialType();
            } else if (isMatch(materialname, finestrs)) {
                materialType = MaterialTypeEnum.Fine.getMaterialType();
            } else if (isMatch(materialname, waterstrs)) {
                materialType = MaterialTypeEnum.Water.getMaterialType();
            } else if (isMatch(materialname, stonestrs)) {
                if (isMatch(materialname, bigstonestrs)) {
                    materialType = MaterialTypeEnum.BigStone.getMaterialType();
                } else if (isMatch(materialname, sstonestrs)) {
                    materialType = MaterialTypeEnum.SmallStone.getMaterialType();
                } else if (isMatch(materialname, mstonestrs)) {
                    materialType = MaterialTypeEnum.MediumStone.getMaterialType();
                } else {
                    materialType = MaterialTypeEnum.MediumStone.getMaterialType();
                }
            } else {
                materialType = MaterialTypeEnum.Other.getMaterialType();
            }
        }
        return materialType;
    }

    public static boolean isMatch(String materialname, String[] strs) {
        boolean match = false;
        for (int i = 0; i < strs.length; i++) {
            if (materialname.contains(strs[i])) {
                match = true;
                break;
            }
        }
        return match;
    }

    //水泥或沥青
    private static String[] cementstrs = {"水泥", "沥青", "SN", "华新"};
    //粉煤灰
    private static String[] fastrs = {"煤"};
    //矿粉
    private static String[] mpstrs = {"矿", "粉", "KF"};
    //外加剂
    private static String[] adtstrs = {"液", "外", "低标", "高标", "剂", "W"};
    //细骨料
    private static String[] finestrs = {"砂", "沙", "细"};
    //水
    private static String[] waterstrs = {"水"};
    //粗骨料
    private static String[] stonestrs = {"粗", "骨料", "料", "石", "mm", "-", "GL", "分子"};
    private static String[] bigstonestrs = {"大", "26.5", "31.5", "17"};
    private static String[] mstonestrs = {"中", "16", "20", "11", "9.5", "13"};
    private static String[] sstonestrs = {"小", "5", "4.75", "3"};

    /**
     * 沥青拌合站搅拌时间超标查询
     */
    public void lqTimeOver(int banheshijian, int stir_datetime_design, String guid) {
        int time;
        int over;
        time = stir_datetime_design - banheshijian;
        if (time > 0) {
            over = 1;
        } else {
            over = 0;
        }
        bhzLqBasesService.updateLqOverTime(over, guid);
    }


    public int lqtemperatureOver(String clwd, String sjclwd) {
        int wenduchaobiao = 0;
        int sjz = Integer.parseInt(sjclwd);
        String[] split = clwd.split(",");
        if (split.length == 2) {
            int min = Integer.parseInt(split[0]);
            int max = Integer.parseInt(split[1]);
            wenduchaobiao = (sjz >= min && sjz <= max) ? 0 : 1;
        }
        if (split.length > 2) {
            int min = Integer.parseInt(split[0]);
            int max = Integer.parseInt(split[1]);
            int overmmax = Integer.parseInt(split[2]);
            wenduchaobiao = (sjz >= overmmax) ? 2 : ((sjz >= min && sjz <= max) ? 0 : 1);

        }

        return wenduchaobiao;
    }


    /**
     * @param p
     * @param column 集合元素对象的字段名
     * @param value  集合元素对象的字段值
     * @return 返回满足条件的Record
     */
    public static BhzChaobiaoCfg findOneByField(List<BhzChaobiaoCfg> p, String column, Object value) {
        BhzChaobiaoCfg result = null;

        for (BhzChaobiaoCfg one : p) {
            Object object = one.getMaterialType();
            if (null == object || object != value) {
                continue;

            } else if (object == value) {

                result = one;
            }


        }
        return result;

    }

    /**
     * 拌合机生产统计
     *
     * @param baseinfo 一条拌合机bhz_lq_bases表的数据
     * @param level    超标级别
     */
    public synchronized void Sattistics(BhzLqBases baseinfo, List<BhzLqCailiao> bhzLqCailiaoList,
                                        int level, String sheibeibianhao, String hhllx) {
         /* 进行超标检查时，直接获取本条base数据   并且对统计表进行判定，
		  如果没有这一天的设备编号，工程名称，施工地点浇筑部位与配方号，强度等级
		  就创建一行这样的数据，如果有就直接取出
		   */
        Double zongchanliang = baseinfo.getZongchanliang();
        String shebeibianhao = baseinfo.getShebeibianhao();
        String strengthRank = null;
        if ("null".equals(hhllx)) {
            hhllx = baseinfo.getHunheliaoid();
        }
        if ("null".equals(hhllx)) {
            hhllx = "空";
        }
        String projectName = baseinfo.getProjectName();
        String jobLocation = baseinfo.getJobLocation();
        String poureLocation = baseinfo.getPoureLocation();
        String formulaNo = baseinfo.getFormulaNo();
        BhzLqStatistics statistics = null;
        int statisticsId = 0;
        DateTime dateTime = DateUtil.parseDate(baseinfo.getChuliaoshijian());
        String datanyr = org.jeecg.modules.job.jobutil.NumberUtil.Stringnyr(dateTime);//格式化后的时间
        Date datanyr1 = org.jeecg.modules.job.jobutil.NumberUtil.datanyr(datanyr);
        BhzLqStatistics selectlimit = bhzLqStatisticsService.selectlimitone(shebeibianhao, formulaNo, datanyr1, hhllx, projectName, jobLocation, poureLocation);
        if (selectlimit != null) {
            count(selectlimit, level);
            statisticsId = selectlimit.getId();
            Double estimateNumber = selectlimit.getEstimateNumber();
            updateestimatenumber(statisticsId, zongchanliang, estimateNumber);
        } else {
            //添加一条信息
            InsertSattistics(baseinfo, hhllx);
            BhzLqStatistics selectlimit1 = bhzLqStatisticsService.selectlimitone(shebeibianhao, formulaNo, datanyr1, hhllx, projectName, jobLocation, poureLocation);
            count(selectlimit1, level);
            statisticsId = selectlimit1.getId();
            Double estimateNumber = selectlimit1.getEstimateNumber();
            updateestimatenumber(statisticsId, zongchanliang, estimateNumber);
        }
        cailiaoStatisticsBase(bhzLqCailiaoList, sheibeibianhao, statisticsId);

    }

    /**
     * 把总盘数加1  如果超标级别为1 在初级超标盘数中加1 如果超标级别为2 在中 级超标盘数中加1 如果超标级别为3 在高 级超标盘数中加1
     *
     * @param Sattistics
     * @param level
     */
    public synchronized void count(BhzLqStatistics Sattistics, int level) {
        BhzLqStatistics statistics1 = new BhzLqStatistics();
        int id = Sattistics.getId();
        Integer allDish = Sattistics.getAllDish();
        Integer allsum = allDish + 1;
        statistics1.setAllDish(allsum);
        statistics1.setId(id);
        bhzLqStatisticsService.updateById(statistics1);//总盘数加1
        if (level == 1) {
            updateprimaryDish(Sattistics);//初级超标盘数加1
            updatebhzCementStatistics(Sattistics);//总超标盘数加1
        } else if (level == 2) {
            updateMiddleDish(Sattistics);//中级超标盘数加1
            updatebhzCementStatistics(Sattistics);//总超标盘数加1
        } else if (level == 3) {
            updateAdvancedDish(Sattistics);//高级超标盘数加1
            updatebhzCementStatistics(Sattistics);//总超标盘数加1
        }
    }

    /**
     * 初级超标盘数加1
     *
     * @param Sattistics
     */
    public synchronized void updateprimaryDish(BhzLqStatistics Sattistics) {
        BhzLqStatistics statistics = new BhzLqStatistics();
        int id = Sattistics.getId();
        Integer primaryDish = Sattistics.getPrimaryDish();
        Integer primaryDish1 = primaryDish + 1;
        statistics.setPrimaryDish(primaryDish1);
        statistics.setId(id);
        bhzLqStatisticsService.updateById(statistics);//初级超标盘数加1
    }

    /**
     * 总超标盘数加1
     *
     * @param Sattistics
     */
    public synchronized void updatebhzCementStatistics(BhzLqStatistics Sattistics) {
        BhzLqStatistics statistics = new BhzLqStatistics();
        int id = Sattistics.getId();
        Integer allOverproofDish = Sattistics.getAllOverproofDish();
        Integer allOverproofDish1 = allOverproofDish + 1;
        statistics.setAllOverproofDish(allOverproofDish1);
        statistics.setId(id);
        bhzLqStatisticsService.updateById(statistics);//总超标盘数加1
    }

    /**
     * 中级超标盘数加1
     *
     * @param Sattistics
     */
    public synchronized void updateMiddleDish(BhzLqStatistics Sattistics) {
        BhzLqStatistics statistics = new BhzLqStatistics();
        int id = Sattistics.getId();
        Integer middleDish = Sattistics.getMiddleDish();
        Integer middleDish1 = middleDish + 1;
        statistics.setMiddleDish(middleDish1);
        statistics.setId(id);
        bhzLqStatisticsService.updateById(statistics);//中级超标盘数加1
    }

    /**
     * 高级超标盘数加1
     *
     * @param Sattistics
     */
    public synchronized void updateAdvancedDish(BhzLqStatistics Sattistics) {
        BhzLqStatistics statistics = new BhzLqStatistics();
        int id = Sattistics.getId();
        Integer advancedDish = Sattistics.getAdvancedDish();
        Integer advancedDish1 = advancedDish + 1;
        statistics.setAdvancedDish(advancedDish1);
        statistics.setId(id);
        bhzLqStatisticsService.updateById(statistics);//高级超标盘数加1
    }

    /**
     * 方量累加
     *
     * @param id
     * @param estimate_number
     * @param estimateNumber
     */
    public synchronized void updateestimatenumber(Integer id, Double estimate_number, Double estimateNumber) {
        BhzLqStatistics statistics = new BhzLqStatistics();
        double es = estimate_number + estimateNumber;
        statistics.setEstimateNumber(es);
        statistics.setId(id);
        bhzLqStatisticsService.updateById(statistics);//高级超标盘数加1
    }

    /**
     * 拌合机生产统计表中添加一条数据
     *
     * @param baseinfo
     * @param hhllx
     */
    public synchronized void InsertSattistics(BhzLqBases baseinfo, String hhllx) {
        BhzLqStatistics a = new BhzLqStatistics();
        String shebei_no = baseinfo.getShebeibianhao();
        String project_name = baseinfo.getProjectName();
        String job_location = baseinfo.getJobLocation();
        String poure_location = baseinfo.getPoureLocation();
        String formula_no = baseinfo.getFormulaNo();
        Date date = cn.hutool.core.date.DateUtil.parseDateTime(baseinfo.getChuliaoshijian());
        String stringnyr = org.jeecg.modules.job.jobutil.NumberUtil.Stringnyr(date);
        Date datanyr = org.jeecg.modules.job.jobutil.NumberUtil.datanyr(stringnyr);
        BhzLqStatistics bhzLqStatistics = new BhzLqStatistics();
        bhzLqStatistics.setShebeibianhao(shebei_no);
        bhzLqStatistics.setPoureLocation(poure_location);
        bhzLqStatistics.setJobLocation(job_location);
        bhzLqStatistics.setFormulaNo(formula_no);
        bhzLqStatistics.setStrengthRank(hhllx);
        bhzLqStatistics.setStatisticsTime(datanyr);
        bhzLqStatistics.setProjectName(project_name);
        bhzLqStatisticsService.save(bhzLqStatistics);
    }

    /**
     * 拌合站（沥青）材料生产统计
     */
    public synchronized void cailiaoStatisticsBase(List<BhzLqCailiao> bhzLqCailiaoList, String sbno, int cs_id) {

        double realnum = 0;
        double theorynum = 0;
        String cailiaoId = "";  //材料id
        String materiale_name = "";
        for (BhzLqCailiao bhzLqCailiao : bhzLqCailiaoList) {
            //实际用量
            realnum = Convert.toFloat(bhzLqCailiao.getShijiyongliang());
            //理论用量
            theorynum = Convert.toFloat(bhzLqCailiao.getTheoryNumber(), 0f);
            //材料名字
            materiale_name = bhzLqCailiao.getCailiaoming();
            //材料ID
            cailiaoId = bhzLqCailiao.getCailiaoid();
            //如果材料id为空，则生成并写入材料配置表
            if (StrUtil.isBlank(cailiaoId) && StrUtil.isNotBlank(materiale_name)) {
                BhzMaterialCfg selectone = bhzMaterialCfgService.selectone(sbno, materiale_name);
                if (null == selectone) {
                    cailiaoId = IdUtil.fastSimpleUUID();
                    BhzMaterialCfg bhzMaterialCfg = new BhzMaterialCfg();
                    bhzMaterialCfg.setBhjno(sbno);
                    bhzMaterialCfg.setName(materiale_name);
                    bhzMaterialCfg.setMaterialid(cailiaoId);
                    bhzMaterialCfg.setSpecs(materiale_name);
                    bhzMaterialCfgService.save(bhzMaterialCfg);
                } else {
                    cailiaoId = selectone.getMaterialid();
                }
                bhzLqCailiao.setCailiaoid(cailiaoId);
                bhzLqCailiaoService.updateById(bhzLqCailiao);
            }
            Integer materiale_type = Convert.toInt(bhzLqCailiao.getCailiaoleixing(), 0);
            // 查询材料统计表中是否有这样的数据，取出id，如果有就在这个数据的基础上把真实用量
            // ，理论用量累加到数据库中，如果没有就创建一条数据，取出id ，再把数据累加进去，
            int DetailStatisticsId = selectDetailStatisticId(cs_id, cailiaoId);
            if (DetailStatisticsId == 0) {
                InsertDetailStatistics(cs_id, materiale_type, cailiaoId, materiale_name, realnum, theorynum);
                DetailStatisticsId = selectDetailStatisticId(cs_id, cailiaoId);
            } else {
                AddReality_numberOne(DetailStatisticsId, realnum);
                AddTheory_numberOne(DetailStatisticsId, theorynum);
            }
            int chaobiaodengji = bhzLqCailiao.getChaobiaodengji();
            if (chaobiaodengji == 1) {  //初级超标
                bhzLqCailiaoStatisticsService.AddPrimary_numberOne(DetailStatisticsId);
            } else if (chaobiaodengji == 2) { //中级超标
                bhzLqCailiaoStatisticsService.AddMiddle_numberOne(DetailStatisticsId);
            } else if (chaobiaodengji == 3) { //高级超标
                bhzLqCailiaoStatisticsService.AddAdvanced_numberOne(DetailStatisticsId);
            }
        }

    }

    /**
     * 向材料统计表中以id累加理论用量
     *
     * @param
     * @param
     */
    private synchronized void AddTheory_numberOne(int detailStatisticsId, double theorynum) {
        BhzLqCailiaoStatistics bhzLqCailiaoStatistics = new BhzLqCailiaoStatistics();
        bhzLqCailiaoStatistics.setId(detailStatisticsId);
        bhzLqCailiaoStatistics.setRealityNumber(theorynum);
        bhzLqCailiaoStatisticsService.updateById(bhzLqCailiaoStatistics);
    }

    /**
     * 根据拌合机生产统计表的主键id与材料类别查询材料统计表中是否有相同的数据 如果有取出数据的主键id如果没有就返回0
     *
     * @param cs_id
     * @return
     */
    private synchronized int selectDetailStatisticId(int cs_id, String cailiaoid) {
        int result = 0;
        BhzLqCailiaoStatistics bhzLqCailiaoStatistics = bhzLqCailiaoStatisticsService.selectId(cs_id, cailiaoid);
        if (null != bhzLqCailiaoStatistics) {
            result = bhzLqCailiaoStatistics.getId();
        }
        return result;
    }

    /**
     * 添加一条材料统计
     *
     * @param cs_id
     * @param materiale_type
     * @param materiale_name
     * @param reality_number
     * @param theory_number
     * @return
     */
    public synchronized boolean InsertDetailStatistics(int cs_id, int materiale_type, String cailiaoId, String
            materiale_name, double reality_number, double theory_number) {
        BhzLqCailiaoStatistics bhzLqCailiaoStatistics = new BhzLqCailiaoStatistics();
        bhzLqCailiaoStatistics.setCsId(cs_id);
        bhzLqCailiaoStatistics.setCailiaoid(cailiaoId);
        bhzLqCailiaoStatistics.setMaterialeType(materiale_type);
        bhzLqCailiaoStatistics.setMaterialeName(materiale_name);
        bhzLqCailiaoStatistics.setRealityNumber(reality_number);
        bhzLqCailiaoStatistics.setTheoryNumber(theory_number);
        return bhzLqCailiaoStatisticsService.save(bhzLqCailiaoStatistics);
    }

    /**
     * 向材料统计表中以id累加真实用量
     *
     * @param id
     * @param reality_number
     */
    public synchronized void AddReality_numberOne(int id, double reality_number) {
        BhzLqCailiaoStatistics bhzLqCailiaoStatistics = new BhzLqCailiaoStatistics();
        bhzLqCailiaoStatistics.setId(id);
        bhzLqCailiaoStatistics.setRealityNumber(reality_number);
        bhzLqCailiaoStatisticsService.updateById(bhzLqCailiaoStatistics);
    }


}
