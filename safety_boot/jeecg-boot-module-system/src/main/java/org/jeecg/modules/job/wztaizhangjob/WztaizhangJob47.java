package org.jeecg.modules.job.wztaizhangjob;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import com.trtm.iot.system.entity.ShebeiInfo;
import com.trtm.iot.system.service.IShebeiInfoService;
import com.trtm.iot.wzcailiaonamedict.entity.Wzcailiaonamedict;
import com.trtm.iot.wzcailiaonamedict.service.IWzcailiaonamedictService;
import com.trtm.iot.wzgongyingshang.entity.Wzgongyingshang;
import com.trtm.iot.wzgongyingshang.service.IWzgongyingshangService;
import com.trtm.iot.wzliaocang.entity.Wzliaocang;
import com.trtm.iot.wzliaocang.service.IWzliaocangService;
import com.trtm.iot.wztaizhang.entity.Wztaizhang;
import com.trtm.iot.wztaizhang.entity.WztaizhangLc;
import com.trtm.iot.wztaizhang.entity.WzyclConfig;
import com.trtm.iot.wztaizhang.service.IWztaizhangLcService;
import com.trtm.iot.wztaizhang.service.IWztaizhangService;
import com.trtm.iot.wztaizhang.service.IWzyclConfigService;
import com.trtm.iot.yclsl.entity.Wzycljinchanggb;
import com.trtm.iot.yclsl.service.IWzycljinchanggbService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.jeecg.common.util.DateUtils.differHours;

/**
 * \* Date: 2021/6/18
 * \* Time: 15:08
 * \* Description:根据原材料进场过磅的料仓名称、规格类型、材料名称、设备编号去计算毛重、皮重及净重以及给新的表添加数据
 * \
 */
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class WztaizhangJob47 implements Job {
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private IWzycljinchanggbService wzycljinchanggbService;
    @Autowired
    private IWzliaocangService wzliaocangService;
    @Autowired
    private IWzcailiaonamedictService wzcailiaonamedictService;
    @Autowired
    private IWztaizhangService wztaizhangService;
    @Autowired
    private IWzgongyingshangService wzgongyingshangService;
    @Autowired
    private IShebeiInfoService shebeiInfoService;
    @Autowired
    private IWztaizhangLcService tzlcService;

    @Autowired
    private IWzyclConfigService wzyclConfigService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.WZTAIZHANG);//原材物资台账=17
        if (null == selectsysconfigone) {
            log.info("未获取到原材料物资台账定时任务的配置信息" + DateUtils.now());
            return;
        }
        //当前数据检测到的目标id
        Integer curid = selectsysconfigone.getCurid();

        List<Wzycljinchanggb> selectycljinchangones = wzycljinchanggbService.selectycljinchangListRc("其他项目", 0,"");//没有进行过台账统计的数据
        if (null == selectycljinchangones || selectycljinchangones.size() == 0) {
            log.info("暂无需要统计原材料进场过磅的数据" + DateUtils.now());
            return;
        }
        int id = 0;
        for (Wzycljinchanggb selectycljinchangone : selectycljinchangones) {
            id = selectycljinchangone.getId();
            String shebeiNo = selectycljinchangone.getShebeibianhao();//设备编号
            String lcNo = selectycljinchangone.getLcbianhao();//料仓编号
            String jinchangshijian = selectycljinchangone.getJinchangshijian();//进场时间
            String cailiaoNo = selectycljinchangone.getCailiaono();//材料编号
            String maozhong1 = selectycljinchangone.getMaozhongt();
            String pizhong1 = selectycljinchangone.getPizhongt();
            String jingzhong1 = selectycljinchangone.getJingzhongt();
            String pici = selectycljinchangone.getPici();//批次
            String gongyinsgangNo = selectycljinchangone.getGongyingshangdanweibianma();//供应商单位编码
            String zhibaodan = StringUtils.isNotBlank(selectycljinchangone.getSonghuodanpic())?("http://web.traiot.cn/docs/wz/" + selectycljinchangone.getSonghuodanpic()):"" ;// 质保单
            String cunfnag = StringUtils.isNotBlank(selectycljinchangone.getLiaocangid()) ? selectycljinchangone.getLiaocangid() : "";
            ShebeiInfo shebeiInfo = shebeiInfoService.selectshebeione(shebeiNo);
            String shengchanpihao = StringUtils.isNotBlank( selectycljinchangone.getYuancaimiaoshu()) ?  selectycljinchangone.getYuancaimiaoshu() : "/";
            String chuangchangshijian = selectycljinchangone.getChuchangshijian();
            try {
                Wzliaocang queryselectone = wzliaocangService.queryselectone(lcNo);
                if (queryselectone == null) {
                    log.info("暂无相匹配的料仓配置数据" + DateUtils.now());
                    wzycljinchanggbService.updateistongji(id, 10);
//                    continue;
                }else{
                  //  cunfnag = cunfnag+queryselectone.getName();
                }
                Wzcailiaonamedict queryselectone1 = wzcailiaonamedictService.queryselectone1(cailiaoNo);
                if (queryselectone1 == null) {
                    log.info("暂无相匹配的材料字典数据" + DateUtils.now());
                    wzycljinchanggbService.updateistongji(id, 20);
                    continue;
                }
                Wzgongyingshang queryselectone2 = wzgongyingshangService.selectnameone(gongyinsgangNo);
                if (queryselectone2 == null) {
                    log.info("暂无相匹配的供应商单位数据" + DateUtils.now());
                    wzycljinchanggbService.updateistongji(id, 15);
                    continue;
                }
                if (jingzhong1 == null || "0".equals(jingzhong1) || "".equals(jingzhong1)) {
                    log.info("原材料进场过磅暂无净重" + DateUtils.now());
                    wzycljinchanggbService.updateistongji(id, 25);
                    continue;
                }
                Integer hour = 120 ;
                Integer cujiliao = 600;
                Integer xijiliao = 600 ;
                Integer shuini = 500 ;
                Integer kuangfeng = 200 ;
                Integer fenmeihui = 500 ;
                Integer jianshuiji = 50;
                Integer other = 600 ;

                if (shebeiInfo==null){
                    wzycljinchanggbService.updateistongji(id, 30);
                    log.info("该地磅没有在平台注册" + DateUtils.now());
                    continue;
                }else{
                    QueryWrapper<WzyclConfig> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("sbjno",shebeiInfo.getSbjno());
                    WzyclConfig one = wzyclConfigService.getOne(queryWrapper);
                    if( one != null ){
                        hour = one.getHour() ;
                        cujiliao = one.getCujiliao();
                        xijiliao = one.getXijiliao() ;
                        shuini = one.getShuini() ;
                        kuangfeng = one.getKuangfeng() ;
                        fenmeihui = one.getFenmeihui() ;
                        jianshuiji = one.getJianshuiji();

                    }

                }
//                if (maozhong1 == null || "0".equals(maozhong1) || maozhong1 == ""){
//                    log.info(String.format("原材料进场过磅暂无毛重" + DateUtils.now()));
//                    continue;
//                }
//                if (pizhong1 == null || "0".equals(pizhong1) || pizhong1 == ""){
//                    log.info(String.format("原材料进场过磅暂无皮重" + DateUtils.now()));
//                    continue;
//                }
                Double maozhong = Double.valueOf(maozhong1);//毛重（吨）
                Double pizhong = Double.valueOf(pizhong1);//皮重（吨）
                Double jingzhong = Double.valueOf(jingzhong1);//净重（吨）
                String maozhongT = String.format("%.2f", maozhong);
                String pizhongT = String.format("%.2f", pizhong);
                String jingzhongT = String.format("%.2f", jingzhong);
                String guigeleixing = queryselectone1.getGuigexinghao();
                String nodeType = queryselectone1.getNodetype();
                Wztaizhang queryone = null;

                // 批次是空的人工录入
                if(nodeType.length()>1 && !selectycljinchangone.getBeizhu().equals("人工录入") ){
                    wzycljinchanggbService.updateistongji(id, 60);
                    continue;
                }
                queryone = wztaizhangService.getWztaizhang(shebeiInfo.getSysOrgCode(), cailiaoNo, gongyinsgangNo,pici);
//                if (StrUtil.isBlank(pici) || "/".equals(pici)) {
//                    queryone = wztaizhangService.queryones(shebeiInfo.getSysOrgCode(), cailiaoNo, gongyinsgangNo);
//
//                    // 批次不是空的人工录入
//                } else {
//                    queryone = wztaizhangService.queryoness(shebeiInfo.getSysOrgCode(), cailiaoNo, gongyinsgangNo, pici);
//                }
                Integer ids = 0;
                Date date = new Date();
                DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                if (queryone == null) {
                    Wztaizhang wztaizhang = new Wztaizhang();
                    wztaizhang.setCailiaono(cailiaoNo);
                    wztaizhang.setShebeibianhao(shebeiNo);
                    wztaizhang.setJinchangshijian(jinchangshijian);
                    wztaizhang.setLcbianhao(lcNo);
                    if(StringUtils.isNotBlank(zhibaodan)){
                        wztaizhang.setZhibaodan(zhibaodan);
                    }
                    wztaizhang.setStoragePlace(cunfnag);
                    wztaizhang.setSysOrgCode(shebeiInfo.getSysOrgCode());
                    wztaizhang.setGuigexinghao(guigeleixing);
                    wztaizhang.setMaozhongt(maozhongT);
                    wztaizhang.setPizhongt(pizhongT);
                    wztaizhang.setJingzhongt(jingzhongT);
                    wztaizhang.setCreateTime(date);
                    wztaizhang.setGongyingshangdanweibianma(gongyinsgangNo);
                    wztaizhang.setRuleway(1);
                    wztaizhang.setShengchanpihao(shengchanpihao); // 生产批号

                    if(chuangchangshijian != null){
                        wztaizhang.setChuchangshijian(format.parse(chuangchangshijian));
                    }

                    if (StrUtil.isBlank(pici) || "/".equals(pici)) {
                        wztaizhang.setPici("JYP-" + format.format(date) + "-" + id);
                    } else {
                        wztaizhang.setPici(pici);
                    }
                    wztaizhang.setNodetype(nodeType);
                    boolean save = wztaizhangService.save(wztaizhang);
                    if(save){
                        // 料仓和台账的中间表
                        boolean b = tzlcService.saveOrUpdateLC(wztaizhang.getPici(),lcNo, jingzhong,jinchangshijian,cailiaoNo,gongyinsgangNo,shebeiNo,shebeiInfo.getSysOrgCode() );
                    }
                    ids = wztaizhang.getId();
                    if (save) {
                        log.info("添加原材料物资台账成功" + DateUtils.now());
                    } else {
                        log.info("添加原材料物资台账失败" + DateUtils.now());
                    }
                } else {
                    ids = queryone.getId();
                    Double maozsum = Double.valueOf(StringUtils.isNotBlank(queryone.getMaozhongt())?queryone.getMaozhongt().replace("t",""):"0") + maozhong;
                    Double pizsum = Double.valueOf(StringUtils.isNotBlank(queryone.getPizhongt())?queryone.getPizhongt().replace("t",""):"0") + pizhong;
                    double jingzsum = Double.valueOf(StringUtils.isNotBlank(queryone.getJingzhongt())?queryone.getJingzhongt().replace("t",""):"0") + jingzhong;
                    String maozhongT2 = String.format("%.2f", maozsum);
                    String pizhongT2 = String.format("%.2f", pizsum);
                    String jingzhongT2 = String.format("%.2f", jingzsum);
                    String jinchangshijiantz = queryone.getJinchangshijian();

                    // 料仓和台账的中间表
                    boolean b = tzlcService.saveOrUpdateLC(queryone.getPici(), lcNo, jingzhong,jinchangshijian,cailiaoNo,gongyinsgangNo,shebeiNo,shebeiInfo.getSysOrgCode());

                    boolean boo = false;
                    boolean boo1 = true;
                    boolean boo2 = true;
                    if (StrUtil.isBlank(pici) || "/".equals(pici)) {
                        int df = differHours(jinchangshijiantz, jinchangshijian);
                        if (df > hour) {
                            boo1 = false;
                        }
                        if ("2".equals(nodeType) || "3".equals(nodeType) || "4".equals(nodeType)) {//粗骨料
                            if (jingzsum > cujiliao) {
                                boo2 = false;
                            }
                            if (boo1 && boo2) {
                                boo = true;
                            }
                        } else if ("1".equals(nodeType)) {//细骨料
                            if (jingzsum > xijiliao) {
                                boo2 = false;
                            }
                            if (boo1 && boo2) {
                                boo = true;
                            }
                        } else if ("6".equals(nodeType)) {//水泥
                            if (jingzsum > shuini) {
                                boo2 = false;
                            }
//                        if (df > 72){
//                            boo2 = false;
//                        }
                            if (boo1 && boo2) {
                                boo = true;
                            }
                        } else if ("7".equals(nodeType)) {//矿粉
                            if (jingzsum > kuangfeng) {
                                boo2 = false;
                            }
                            if (boo1 && boo2) {
                                boo = true;
                            }
                        } else if ("8".equals(nodeType)) {//粉煤灰
                            if (jingzsum > fenmeihui) {
                                boo2 = false;
                            }
                            if (boo1 && boo2) {
                                boo = true;
                            }
                        } else if ("9".equals(nodeType)) {//减水剂
                            if (jingzsum > jianshuiji) {
                                boo2 = false;
                            }
                            if (boo1 && boo2) {
                                boo = true;
                            }
                        } else {//其他
                            // 不需要生成检验批
                            boo = true;
                            wzycljinchanggbService.updateistongji(id, 60);
                            continue;
                        }
                    }else {//地磅进场数据有批次 统计统一批次的数据
                        if (pici.equals(queryone.getPici())) {
                            boo = true;
                        }
                    }
                    if (boo) {
                        int updateone = wztaizhangService.updateone(maozhongT2, pizhongT2, ids, jingzhongT2);
                        if (updateone == 1) {
                            log.info("添加原材料物资台账成功" + DateUtils.now());
                        } else {
                            log.info("添加原材料物资台账失败" + DateUtils.now());
                        }
                    } else {
                        Wztaizhang selectwztaizhangone = wztaizhangService.selectwztaizhangone(ids);
                        if (selectwztaizhangone != null) {
                            Wztaizhang wztaizhang = new Wztaizhang();
                            wztaizhang.setIsfinish(1);
                            wztaizhang.setId(ids);

                            wztaizhangService.updateById(wztaizhang);
                            List<Wztaizhang> wztaizhangList = wztaizhangService.selectolddata(ids);
                            if (wztaizhangList.size()>0){
                                for (Wztaizhang wztaizhang1:wztaizhangList){
                                    int df = differHours(wztaizhang1.getJinchangshijian(),selectwztaizhangone.getJinchangshijian());
                                    if (df>hour){
                                        wztaizhang1.setIsfinish(1);
                                        wztaizhangService.updateById(wztaizhang1);
                                    }
                                }
                            }
                        }

                        Wztaizhang wztaizhang = new Wztaizhang();
                        // 生产批号不为空则更新
                        if( StringUtils.isNotBlank(shengchanpihao) && shengchanpihao != "/"){
                            wztaizhang.setShengchanpihao(shengchanpihao);
                        }
                        wztaizhang.setCailiaono(cailiaoNo);
                        wztaizhang.setSysOrgCode(shebeiInfo.getSysOrgCode());
                        wztaizhang.setShebeibianhao(shebeiNo);
                        wztaizhang.setJinchangshijian(jinchangshijian);
                        wztaizhang.setLcbianhao(lcNo);
                        if(StringUtils.isNotBlank(zhibaodan)){
                            wztaizhang.setZhibaodan(zhibaodan);
                        }
                        wztaizhang.setStoragePlace(cunfnag);
                        wztaizhang.setGuigexinghao(guigeleixing);
                        wztaizhang.setMaozhongt(maozhongT);
                        wztaizhang.setPizhongt(pizhongT);
                        wztaizhang.setJingzhongt(jingzhongT);
                        wztaizhang.setCreateTime(date);
                        if(chuangchangshijian != null){
                            wztaizhang.setChuchangshijian(format.parse(chuangchangshijian));
                        }
                        wztaizhang.setRuleway(1);
                        wztaizhang.setGongyingshangdanweibianma(gongyinsgangNo);
                        if (StrUtil.isBlank(pici) || "/".equals(pici)) {
                            wztaizhang.setPici("JYP-" + format.format(date) + "-" + id);
                        } else {
                            wztaizhang.setPici(pici);
                        }
                        wztaizhang.setNodetype(nodeType);
                        boolean save = wztaizhangService.save(wztaizhang);
                        ids = wztaizhang.getId();
                        if (save) {
                            log.info("添加原材料物资台账成功" + DateUtils.now());
                        } else {
                            log.info("添加原材料物资台账失败" + DateUtils.now());
                        }
                    }
                }
                Wzycljinchanggb wzycljinchanggb = new Wzycljinchanggb();
                wzycljinchanggb.setId(id);
                wzycljinchanggb.setTaizhangtj(1);
                wzycljinchanggb.setTaizhangid(ids);
                wzycljinchanggbService.updateById(wzycljinchanggb);  //原材料进场过磅统计状态
            } catch (Exception e) {
                e.printStackTrace();
                wzycljinchanggbService.updateistongji(id, 40);
            }
            log.info(String.format("原材料物资台账！   时间" + DateUtils.now(), "当前判断到" + id));
        }
        sysConfigService.updateSysConfig(JobUtil.WZTAIZHANG, id);
    }



    public Wztaizhang getWztaizhang(String sysOrgCode, String cailiaoNo,String  gongyinsgangNo,String pici) {

        QueryWrapper<Wztaizhang> queryWrapper = new QueryWrapper<>();
        //地磅人工登记的进行生成批次
//        queryWrapper.eq("ruleway",3);
        queryWrapper.eq("sys_org_code",sysOrgCode);
        queryWrapper.eq("cailiaoNo",cailiaoNo);
        queryWrapper.eq("gongyingshangdanweibianma",gongyinsgangNo);
        queryWrapper.eq(!(StrUtil.isBlank(pici) || "/".equals(pici)),"pici",pici);
        queryWrapper.orderByDesc("id");
        queryWrapper.last(" limit 1");
        Wztaizhang pageList = wztaizhangService.getOne(queryWrapper);
        return pageList;
    }


}
