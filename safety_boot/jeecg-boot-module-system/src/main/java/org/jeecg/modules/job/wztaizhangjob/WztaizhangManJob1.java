package org.jeecg.modules.job.wztaizhangjob;

import cn.hutool.core.util.StrUtil;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import com.trtm.iot.system.entity.ShebeiInfo;
import com.trtm.iot.system.service.IShebeiInfoService;
import com.trtm.iot.wzcailiaonamedictman.entity.WzcailiaonamedictMan;
import com.trtm.iot.wzcailiaonamedictman.service.IWzcailiaonamedictManService;
import com.trtm.iot.wzgongyingshangman.entity.WzgongyingshangMan;
import com.trtm.iot.wzgongyingshangman.service.IWzgongyingshangManService;
import com.trtm.iot.wzliaocang.entity.Wzliaocang;
import com.trtm.iot.wzliaocang.service.IWzliaocangService;
import com.trtm.iot.wztaizhang.entity.Wztaizhang;
import com.trtm.iot.wztaizhang.service.IWztaizhangLcService;
import com.trtm.iot.wztaizhang.service.IWztaizhangService;
import com.trtm.iot.wzycljinchanggbman.entity.WzycljinchanggbMan;
import com.trtm.iot.wzycljinchanggbman.service.IWzycljinchanggbManService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.util.DateUtils;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.jeecg.common.util.DateUtils.differHours;

/**
 * \* Date: 2022/8/24
 * \* Time: 15:08
 * \* Description:根据原材料进场过磅的料仓名称、规格类型、材料名称、设备编号去计算毛重、皮重及净重以及给新的表添加数据
 * \
 */
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class WztaizhangManJob1 implements Job {
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private IWzycljinchanggbManService wzycljinchanggbManService;
    @Autowired
    private IWzliaocangService wzliaocangService;
    @Autowired
    private IWzcailiaonamedictManService wzcailiaonamedictManService;
    @Autowired
    private IWztaizhangService wztaizhangService;
    @Autowired
    private IWzgongyingshangManService wzgongyingshangManService;
    @Autowired
    private IShebeiInfoService shebeiInfoService;
    @Autowired
    private IWztaizhangLcService tzlcService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.WZTAIZHANG_MAN);//原材物资台账=17
        if (null == selectsysconfigone) {
            log.info("未获取到人工过磅原材料物资台账定时任务的配置信息" + DateUtils.now());
            return;
        }
        //当前数据检测到的目标id
        Integer curid = selectsysconfigone.getCurid();
        List<WzycljinchanggbMan> selectycljinchangones = wzycljinchanggbManService.selectycljinchangList(curid, 0);//没有进行过台账统计的数据
        if (null == selectycljinchangones || selectycljinchangones.size() == 0) {
            log.info("暂无需要统计原材料进场过磅的数据" + DateUtils.now());
            return;
        }
        int id = 0;
        DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (WzycljinchanggbMan selectycljinchangone : selectycljinchangones) {
            id = selectycljinchangone.getId();
            String shebeiNo = selectycljinchangone.getShebeibianhao();//设备编号
            String lcNo = selectycljinchangone.getLcbianhao();//料仓编号
            String jinchangshijian = selectycljinchangone.getJinchangshijian();//进场时间
            String cailiaoNo = selectycljinchangone.getCailiaono();//材料编号
            String maozhong1 = selectycljinchangone.getMaozhongt();
            String pizhong1 = selectycljinchangone.getPizhongt();
            String jingzhong1 = selectycljinchangone.getJingzhong();//数量
            String pici = selectycljinchangone.getPici();//批次
            String danwei = StringUtils.isNotBlank(selectycljinchangone.getGuobangleibie()) ? selectycljinchangone.getGuobangleibie() : "";//单位
            String gongyinsgangNo = selectycljinchangone.getGongyingshangdanweibianma();//供应商单位编码
            String zhibaodan =StringUtils.isNotBlank(selectycljinchangone.getSonghuodanpic())?("http://web.traiot.cn/docs/wz/" + selectycljinchangone.getSonghuodanpic()):"" ;// 质保单
            String address = selectycljinchangone.getLiaocangid() ; //+ (StringUtils.isNotBlank(selectycljinchangone.getYunshudanwei()) ? selectycljinchangone.getYunshudanwei() : "");//存放地点+料仓名称
            String usepart = selectycljinchangone.getHouchepai();// 使用部位
            String shengchanpihao = StringUtils.isNotBlank( selectycljinchangone.getYuancaimiaoshu()) ?  selectycljinchangone.getYuancaimiaoshu() : "/";
            ShebeiInfo shebeiInfo = shebeiInfoService.selectshebeione(shebeiNo);
            if (oConvertUtils.isEmpty(pici) || "/".equals(pici)) {
//                pici = "WZ-" + DateUtils.getDate("yyMMdd") + "-" + id;
//                log.info("未进行批次填写" + DateUtils.now());
                wzycljinchanggbManService.updateistongji(id, 5); // 未进行批次填写
                continue;

            }
            try {
                Wzliaocang queryselectone = wzliaocangService.queryselectone(lcNo);
                if (queryselectone == null) {
//                    log.info("暂无相匹配的料仓配置数据" + DateUtils.now());
                    wzycljinchanggbManService.updateistongji(id, 10);// 无相匹配的料仓配置数据
                    continue;
                }
                WzcailiaonamedictMan queryselectone1 = wzcailiaonamedictManService.queryselectone1(cailiaoNo);
                if (queryselectone1 == null) {
//                    log.info("暂无相匹配的材料字典数据" + DateUtils.now());
                    wzycljinchanggbManService.updateistongji(id, 20);// 无相匹配的材料字典数据
                    continue;
                }
                WzgongyingshangMan queryselectone2 = wzgongyingshangManService.selectnameone1(gongyinsgangNo);
                if (queryselectone2 == null) {
//                    log.info("暂无相匹配的供应商单位数据" + DateUtils.now());
                    wzycljinchanggbManService.updateistongji(id, 15); // 暂无相匹配的供应商单位数据
                    continue;
                }

                if (shebeiInfo == null) {
//                    log.info("该地磅没有在平台注册" + DateUtils.now());
                    wzycljinchanggbManService.updateistongji(id, 30);// 该地磅没有在平台注册
                    continue;
                }

//                Double maozhong = Double.valueOf(maozhong1);//毛重（吨）
//                Double pizhong = Double.valueOf(pizhong1);//皮重（吨）
//                Double jingzhong = Double.valueOf(jingzhong1);//净重（吨）
//                String maozhongT = String.format("%.2f", maozhong);
//                String pizhongT = String.format("%.2f", pizhong);
//                String jingzhongT = String.valueOf(jingzhong);
                String guigeleixing = queryselectone1.getGuigexinghao();
                String nodeType = queryselectone1.getNodetype();
                Wztaizhang queryone = null;
                Date date = new Date();

//                String jypici = "WZ-" + format.format(date) + "-" + id;
                selectycljinchangone.setQianchepai(pici);// 将自动生产的检验批号字段放到前车牌
//                pici ="WZ-" + format.format(date) + "-" + id ;
//                selectycljinchangone.setPici(pici);

                // 查询检验批表是否存在该登记进场信息
//                if(StringUtils.isNotBlank(String.valueOf(selectycljinchangone.getTaizhangid()))){
//                queryone = wztaizhangService.getById(selectycljinchangone.getTaizhangid());
//                }else{
                // queryone = wztaizhangService.queryoness(shebeiInfo.getSysOrgCode(), cailiaoNo, gongyinsgangNo, pici);
//                queryone = null;
//                }
                queryone = wztaizhangService.getByPici(pici);

                Integer ids = 0;
                if (queryone == null) {

                    Wztaizhang wztaizhang = new Wztaizhang();
                    wztaizhang.setCailiaono(cailiaoNo);
                    wztaizhang.setNodetype(nodeType);
                    wztaizhang.setShebeibianhao(shebeiNo);
                    wztaizhang.setJinchangshijian(jinchangshijian);
                    wztaizhang.setLcbianhao(lcNo);
                    // 质保单
                    wztaizhang.setZhibaodan(zhibaodan);
                    wztaizhang.setSysOrgCode(shebeiInfo.getSysOrgCode());
                    wztaizhang.setGuigexinghao(guigeleixing);
                    wztaizhang.setJingzhongt(jingzhong1);
                    wztaizhang.setDanwei(danwei);
                    wztaizhang.setCreateTime(date);
                    wztaizhang.setGongyingshangdanweibianma(gongyinsgangNo);
                    wztaizhang.setUsePart(usepart);// 使用部位
                    wztaizhang.setStoragePlace(address);// 存放地点
                    wztaizhang.setRuleway(1);
                    wztaizhang.setGblx(1);
                    wztaizhang.setIsfinish(1);
                    wztaizhang.setPici(pici);// 系统生成批次号
                    wztaizhang.setShengchanpihao(shengchanpihao); // 生产批号
//                    wztaizhang.setPizhongt(pici);// 人工手输入批次号填入该字段
                    boolean save = wztaizhangService.save(wztaizhang);
                    // 料仓和台账的中间表
                    boolean b = tzlcService.saveOrUpdateLC(wztaizhang.getPici(), lcNo,Double.valueOf(jingzhong1) ,jinchangshijian,cailiaoNo,gongyinsgangNo,shebeiNo,shebeiInfo.getSysOrgCode());

                    ids = wztaizhang.getId();
                    if (save) {
                        log.info("添加原材料物资台账成功" + DateUtils.now());
                    } else {
                        log.info("添加原材料物资台账失败" + DateUtils.now());
                    }
                } else {
////                    Wztaizhang wztaizhang1 = new Wztaizhang();
////                    wztaizhang1.setId(queryone.getId());
////                    wztaizhang1.setNodetype(nodeType);
////                    wztaizhang1.setJingzhongt(jingzhong1+danwei);
////                    wztaizhang1.setZhibaodan(zhibaodan);
////                    wztaizhang1.setJystatus(0);
////                    wztaizhang1.setPizhongt(pici);// 人工手输入批次号填入该字段
////                    wztaizhang1.setPici(pici);// 系统生成批次号
////                    wztaizhang1.setGongyingshangdanweibianma(gongyinsgangNo);
////                    wztaizhang1.setGuigexinghao(guigeleixing);
////                    wztaizhang1.setIsfinish(1);
                    String jz = new DecimalFormat("0.#####").format(Double.valueOf(jingzhong1) + Double.valueOf(queryone.getJingzhongt()));
                    queryone.setJingzhongt(jz);
                    // 累加后状态改为未推送，进行重新选择
                    queryone.setJystatus(0);
                    wztaizhangService.updateById(queryone);
                    ids = queryone.getId();
                    log.info("登记进场批次累加" + DateUtils.now());

                    // 料仓和台账的中间表
                    boolean b = tzlcService.saveOrUpdateLC(queryone.getPici(), lcNo,Double.valueOf(jingzhong1) ,jinchangshijian,cailiaoNo,gongyinsgangNo,shebeiNo,shebeiInfo.getSysOrgCode());
//                    wzycljinchanggbManService.updateistongji(id, 35);

                }

                WzycljinchanggbMan wzycljinchanggbMan = new WzycljinchanggbMan();
                wzycljinchanggbMan.setQianchepai(pici);
                wzycljinchanggbMan.setId(id);
                wzycljinchanggbMan.setTaizhangtj(1);
                wzycljinchanggbMan.setTaizhangid(ids);
                wzycljinchanggbManService.updateById(wzycljinchanggbMan);  //原材料进场过磅统计状态
            } catch (Exception e) {
                e.printStackTrace();
                wzycljinchanggbManService.updateistongji(id, 40);
            }
            log.info(String.format("原材料物资台账！   时间" + DateUtils.now(), "当前判断到" + id));
        }
        sysConfigService.updateSysConfig(JobUtil.WZTAIZHANG_MAN, id);
    }
}
