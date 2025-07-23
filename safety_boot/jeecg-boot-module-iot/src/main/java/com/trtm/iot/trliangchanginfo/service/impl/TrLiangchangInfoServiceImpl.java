package com.trtm.iot.trliangchanginfo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.trtm.iot.bhzStatistics.entity.BhzCementStatistics;
import com.trtm.iot.bhzStatistics.service.IBhzCementStatisticsService;
import com.trtm.iot.czsh.entity.BhzCementOverHandler;
import com.trtm.iot.czsh.service.IBhzCementOverHandlerService;
import com.trtm.iot.devicepipepilehistorydataone.mapper.DevicePipepileHistorydataOneMapper;
import com.trtm.iot.hntbhz.entity.BhzCementBase;
import com.trtm.iot.hntbhz.entity.BhzCementDetail;
import com.trtm.iot.hntbhz.service.IBhzCementBaseService;
import com.trtm.iot.hntbhz.service.IBhzCementDetailService;
import com.trtm.iot.hntbhz.vo.BhzCementBasePage;
import com.trtm.iot.kanbaninfo.entity.Kanbaninfo;
import com.trtm.iot.kanbaninfo.service.IKanbaninfoService;
import com.trtm.iot.system.entity.Bhzrenwudan;
import com.trtm.iot.system.entity.ShebeiInfo;
import com.trtm.iot.system.mapper.ShebeiInfoMapper;
import com.trtm.iot.system.service.IBhzrenwudanService;
import com.trtm.iot.trliangchanginfo.entity.TrLiangchangInfo;
import com.trtm.iot.trliangchanginfo.mapper.TrLiangchangInfoMapper;
import com.trtm.iot.trliangchanginfo.service.ITrLiangchangInfoService;
import com.trtm.iot.trzhanglarenwudan.entity.TrZhanglaRenwudan;
import com.trtm.iot.trzhanglarenwudan.service.ITrZhanglaRenwudanService;
import com.trtm.iot.yajiangm.entity.TrYajiangM;
import com.trtm.iot.yajiangm.service.ITrYajiangMService;
import com.trtm.iot.yajiangrenwudan.entity.TrYajiangRenwudan;
import com.trtm.iot.yajiangrenwudan.service.ITrYajiangRenwudanService;
import com.trtm.iot.yajiangs.entity.TrYajiangS;
import com.trtm.iot.yajiangs.service.ITrYajiangSService;
import com.trtm.iot.zhanglam.entity.TrZhanglaM;
import com.trtm.iot.zhanglam.service.ITrZhanglaMService;
import com.trtm.iot.zhanglaxxb.entity.TrZhanglaXxb;
import com.trtm.iot.zhanglaxxb.service.ITrZhanglaXxbService;
import com.trtm.iot.zhiliangrenwudan.entity.Zhiliangrenwudan;
import com.trtm.sy.sydpssysample.entity.SysDepart;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: tr_liangchang_info
 * @Author: jeecg-boot
 * @Date:   2023-04-25
 * @Version: V1.0
 */
@Service
public class TrLiangchangInfoServiceImpl extends ServiceImpl<TrLiangchangInfoMapper, TrLiangchangInfo> implements ITrLiangchangInfoService {
    @Autowired
    IBhzrenwudanService bhzrenwudanService;
    @Autowired
    ITrZhanglaRenwudanService zhanglaRenwudanService;
    @Autowired
    ITrYajiangRenwudanService yajiangRenwudanService;
    @Autowired
    ITrZhanglaXxbService zhanglaXxbService;
    @Autowired
    ITrZhanglaMService zhanglaMService;
    @Autowired
    ITrYajiangMService yajiangMService;
    @Autowired
    ITrYajiangSService yajiangSService;
    @Autowired
    TrLiangchangInfoMapper liangchangInfoMapper;
    @Autowired
    ShebeiInfoMapper shebeiInfoMapper;
    @Autowired
    DevicePipepileHistorydataOneMapper devicePipepileHistorydataOneMapper;
    @Autowired
    IBhzCementStatisticsService bhzCementStatisticsService;
    @Autowired
    IBhzCementBaseService bhzCementBaseService;
    @Autowired
    IBhzCementDetailService bhzCementDetailService;
    @Autowired
    IBhzCementOverHandlerService bhzCementOverHandlerService;
    @Autowired
    IKanbaninfoService kanbaninfoService;

    @Override
    public List<Map<String, Object>> selectbyorgcode(String dateNowStr,String orgCode) {
        List<Map<String, Object>> sj = new ArrayList<>();
        List<String> sbs = devicePipepileHistorydataOneMapper.fingAllSheBeiNo(orgCode);
        for (int i = 1; i < 4; i++) {
            Map<String, Object> map1 = new HashMap<>();
            Integer zongShu = 0;
            Integer yuJingS = 0;
            Integer biHeS = 0;
            if (i == 1){
                QueryWrapper<Bhzrenwudan> queryWrapper = new QueryWrapper<>();
                queryWrapper.likeRight("sys_org_code",orgCode);
                queryWrapper.likeRight("wbs_structure_type","后张法预制梁（板）");
                List<Bhzrenwudan> list = bhzrenwudanService.list(queryWrapper);

                QueryWrapper<Kanbaninfo> queryWrapper1 = new QueryWrapper<>();
                queryWrapper1.eq("type",8);
                queryWrapper1.likeRight("sys_org_code",orgCode);
                List<Kanbaninfo> list1 = kanbaninfoService.list(queryWrapper1);
                Kanbaninfo kanbaninfo = new Kanbaninfo();
                int num = 0;
                if (list1.size() > 0){
                    kanbaninfo = list1.get(0);
                    num = list.size() + Integer.parseInt(kanbaninfo.getAllcount());
                }else {
                    num = list.size();
                }

//                BhzCementStatistics bhzCementStatistics = bhzCementStatisticsService.findRenwudanzs(sbs);
                if (sbs != null){
//                    zongShu = bhzCementStatistics.getAllDish();
//                    yuJingS = bhzCementStatistics.getAllOverproofDish();
//                    biHeS = bhzCementStatistics.getAllHandleDish();
                    zongShu = bhzCementBaseService.findXiangMuZSs(sbs, dateNowStr);//查询所有设备在表中存在的数量
                    yuJingS = bhzCementBaseService.findYuJingSs(sbs, dateNowStr);//根据所有的设备查询不合格的数量
                    biHeS = bhzCementBaseService.findBiHeSs(sbs, dateNowStr);//根据所有的设备查询状态值20 的数量
                }
                String yujinglv = "";
                if (zongShu == 0){
                    yujinglv = "0";
                } else {
                    yujinglv = getPercentStrs(yuJingS, zongShu);//预警率
                    if (Double.parseDouble(yujinglv) > 100){
                        yujinglv = "100";
                    }
                    if (yujinglv.equals("0.00")){
                        yujinglv = "0";
                    }
                }

                String bihelv = "";
                if (zongShu == 0){
                    bihelv = "-";
                } else {
                    bihelv = getPercentStrs(biHeS, yuJingS);//预警率
                    if (Double.parseDouble(bihelv) > 100){
                        bihelv = "100";
                    }
                    if (bihelv.equals("0.00")){
                        bihelv = "-";
                    }
                }

                map1.put("name","混凝土拌合");
                map1.put("sczl",num);
                map1.put("zs",zongShu);
                map1.put("yjl",yujinglv+"%");
                map1.put("bhl",bihelv+"%");
                map1.put("type",i);
            }else if (i == 2){
                List<String> zhiliangrenwudanList = zhanglaRenwudanService.saveShebei(sbs);
                if (zhiliangrenwudanList.size() > 0){
                    zongShu = zhanglaXxbService.findRenwudanzs(zhiliangrenwudanList);
                    yuJingS = zhanglaXxbService.findRenwudanyjs(zhiliangrenwudanList);
                    biHeS = zhanglaXxbService.findRenwudanbhs(zhiliangrenwudanList);
                }
                String yujinglv = "";
                if (zongShu == 0){
                    yujinglv = "0";
                } else {
                    yujinglv = getPercentStrs(yuJingS, zongShu);//预警率
                    if (Double.parseDouble(yujinglv) > 100){
                        yujinglv = "100";
                    }
                    if (yujinglv.equals("0.00")){
                        yujinglv = "0";
                    }
                }
                String bihelv = "";
                if (zongShu == 0){
                    bihelv = "-";
                } else {
                    bihelv = getPercentStrs(biHeS, yuJingS);//预警率
                    if (Double.parseDouble(bihelv) > 100){
                        bihelv = "100";
                    }
                    if (bihelv.equals("0.00")){
                        bihelv = "-";
                    }
                }
                map1.put("name","智能张拉");
                map1.put("sczl",zhiliangrenwudanList.size());
                map1.put("zs",zongShu);
                map1.put("yjl",yujinglv+"%");
                map1.put("bhl",bihelv+"%");
                map1.put("type",i);
            }else {
                List<String> yajiangRenwudans = yajiangRenwudanService.saveShebei(sbs);
                if (yajiangRenwudans.size() > 0){
                    zongShu = yajiangMService.findRenwudanzs(yajiangRenwudans);
                    yuJingS = yajiangMService.findRenwudanyjs(yajiangRenwudans);
                    biHeS = yajiangMService.findRenwudanbhs(yajiangRenwudans);
                }
                String yujinglv = "";
                if (zongShu == 0){
                    yujinglv = "0";
                } else {
                    yujinglv = getPercentStrs(yuJingS, zongShu);//预警率
                    if (Double.parseDouble(yujinglv) > 100){
                        yujinglv = "100";
                    }
                    if (yujinglv.equals("0.00")){
                        yujinglv = "0";
                    }
                }

                String bihelv = "";
                if (zongShu == 0){
                    bihelv = "-";
                } else {
                    bihelv = getPercentStrs(biHeS, yuJingS);//预警率
                    if (Double.parseDouble(bihelv) > 100){
                        bihelv = "100";
                    }
                    if (bihelv.equals("0.00")){
                        bihelv = "-";
                    }
                }

                map1.put("name","智能压浆");
                map1.put("sczl",yajiangRenwudans.size());
                map1.put("zs",zongShu);
                map1.put("yjl",yujinglv+"%");
                map1.put("bhl",bihelv+"%");
                map1.put("type",i);
            }
            sj.add(map1);
        }
        return sj;
    }

    @Override
    public List<Map<String, Object>> seyorgcodexq(String orgCode, Integer type) {
        List<Map<String, Object>> sj = new ArrayList<>();
        SysDepart selectdepart = liangchangInfoMapper.selectdepart(orgCode);

        SysDepart fuid = liangchangInfoMapper.selectbyfuid(selectdepart.getParentId());
        SysDepart sysDepart = liangchangInfoMapper.selectbyfuid1(fuid.getParentId());
        List<String> sbs = devicePipepileHistorydataOneMapper.fingAllSheBeiNo(orgCode);
        if (type == 1){
            BhzCementBase bhzCementBase = new BhzCementBase();
            bhzCementBase.setIsdel(0);
            QueryWrapper<BhzCementBase> queryWrapper1 = new QueryWrapper<>();
            queryWrapper1.eq("isdel",0);
            queryWrapper1.gt("over_level", 0);
            queryWrapper1.gt("product_datetime","2023-05");
            queryWrapper1.in("shebei_no",sbs);
            queryWrapper1.orderByDesc("product_datetime");
            List<BhzCementBase> list1 = bhzCementBaseService.list(queryWrapper1);
            double max = 0.0;
            double enm = 0.0;
            for (BhzCementBase record : list1) {
                Map<String, Object> map1 = new HashMap<>();
                BhzCementBasePage bhzCementBasePage = new BhzCementBasePage();
                BeanUtils.copyProperties(record, bhzCementBasePage);
                String batchNo = record.getBatchNo();
                List<BhzCementDetail> details = bhzCementDetailService.selectcementlist(batchNo);
                if (details.size() > 0) {
                    bhzCementBasePage.setBhzCementDetailList(details);
                    max = 0.0;
                    enm = 0.0;
                    for (BhzCementDetail bhzCementDetail : details) {
                        if (bhzCementDetail.getMaterialeOverLevel() > 0) {
                            max = max > bhzCementDetail.getOverValue() ? max : bhzCementDetail.getOverValue();
                            if (max > enm){
                                if (bhzCementDetail.getMaterialeName().equals("污水")){
                                    map1.put("cbcs","水");
                                }else {
                                    map1.put("cbcs",bhzCementDetail.getMaterialeName());
                                }
                            }
                            enm = max;
                        }
                    }
                    bhzCementBasePage.setBhzCementDetailList(details);
                }
                bhzCementBasePage.setAdditiveVariety(max + "%");
                ShebeiInfo device = shebeiInfoMapper.selectshebeione(record.getShebeiNo());
                map1.put("shebei","");
                if (device != null) {
                    bhzCementBasePage.setShebeiNo(device.getSbname());
                    map1.put("shebei",device.getSbname());
                }
                BhzCementOverHandler list = bhzCementOverHandlerService.selectlist(batchNo);
                if (list == null) {
                    BhzCementOverHandler bhzCementOverHandler = new BhzCementOverHandler();
                    bhzCementOverHandler.setOverproofStatus(0);
                    bhzCementBasePage.setBhzCementOverHandler(bhzCementOverHandler);
                    map1.put("bhsj","");
                } else {
                    bhzCementBasePage.setBhzCementOverHandler(list);
                    map1.put("bhsj",list.getHandleTime());
                }
                map1.put("bd",selectdepart.getDepartName());
                map1.put("yzc",sysDepart.getDepartName());
                map1.put("jhscsj",record.getCollectDatetime());
                map1.put("jzbw",record.getPoureLocation());
                map1.put("qddj",record.getStrengthRank());
                map1.put("cbdj",record.getOverLevel());
                map1.put("cbbl",max);
                map1.put("bhqk",record.getOverproofStatus());
                map1.put("fid",record.getBatchNo());
                sj.add(map1);
            }
        }else if (type == 2){
            List<TrZhanglaRenwudan> list = zhanglaRenwudanService.saveShebeis(sbs);

            if (list.size() > 0){
                for (TrZhanglaRenwudan l :list){
                    QueryWrapper<TrZhanglaXxb> queryWrapper1 = new QueryWrapper<>();
                    queryWrapper1.eq("uuid",l.getUuid());
                    TrZhanglaXxb one = null;
                    try {
                        one = zhanglaXxbService.getOne(queryWrapper1);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    if (one != null){
                        List<TrZhanglaM> trZhanglaMS = zhanglaMService.selectmnotList(one.getSyjid());//不合格的
                        if (trZhanglaMS.size() > 0){
                            for (TrZhanglaM zhanglaM :trZhanglaMS){
                                ShebeiInfo selectshebeione = shebeiInfoMapper.selectshebeione(zhanglaM.getShebeibianhao());
                                Map<String, Object> map1 = new HashMap<>();
                                map1.put("bd",selectdepart.getDepartName());
                                map1.put("lc",fuid.getDepartName());
                                map1.put("yzc",sysDepart.getDepartName());
                                map1.put("shebei",selectshebeione.getSbname());
                                map1.put("shebeino",selectshebeione.getSbjno());
                                map1.put("gcbw",one.getGjbh());
                                map1.put("kd",zhanglaM.getGsbh());
                                map1.put("zlsj",zhanglaM.getZlsj());
                                map1.put("sjscl",zhanglaM.getLlscl());
                                double v = Double.parseDouble(zhanglaM.getLlscl()) - Double.parseDouble(zhanglaM.getZscl());
                                map1.put("pc",v);
                                map1.put("gfyq",zhanglaM.getYxpc());
                                map1.put("cbdj",zhanglaM.getOverLevel());
                                map1.put("bhqk",zhanglaM.getOverproofStatus());
                                map1.put("fid",one.getSyjid());
                                sj.add(map1);
                            }
                        }
                    }
                }
            }
        }else {
            List<TrYajiangRenwudan> list = yajiangRenwudanService.saveShebeis(sbs);
            if (list.size() > 0){
                for (TrYajiangRenwudan l :list){
                    QueryWrapper<TrYajiangM> queryWrapper1 = new QueryWrapper<>();
                    queryWrapper1.eq("uuid",l.getUuid());
                    TrYajiangM one = null;
                    try {
                        one = yajiangMService.getOne(queryWrapper1);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    if (one != null){
                        List<TrYajiangS> yajiangS = yajiangSService.selectmnotList(one.getSyjid());//不合格的
                        if (yajiangS.size() > 0){
                            for (TrYajiangS trZhanglaM :yajiangS){
                                ShebeiInfo selectshebeione = shebeiInfoMapper.selectshebeione(one.getYjsbbh());
                                Map<String, Object> map1 = new HashMap<>();
                                map1.put("bd",selectdepart.getDepartName());
                                map1.put("lc",fuid.getDepartName());
                                map1.put("yzc",sysDepart.getDepartName());
                                map1.put("shebei",selectshebeione.getSbname());
                                map1.put("gcbw",one.getGjbh());
                                map1.put("kd",trZhanglaM.getKongdao());
                                map1.put("yjsj",trZhanglaM.getStarttime());
                                map1.put("shyjl",trZhanglaM.getJinjiangshu());
                                map1.put("sjyl",trZhanglaM.getJinjiangyal());
                                map1.put("cbdj",trZhanglaM.getIsOverLevel());
                                map1.put("bhqk",trZhanglaM.getOverproofStatus());
                                map1.put("sjyjl",l.getLljl());
                                map1.put("lilunyal",l.getSjyl());
                                map1.put("fid",one.getSyjid());
                                sj.add(map1);
                            }
                        }
                    }
                }
            }
        }
        return sj;
    }

    /**
     * 得到百分比的字符串，比如传参 1,3，返回33.33
     */
    private String getPercentStrs(Integer diff, Integer sum) {
        DecimalFormat df = new DecimalFormat("0.00");//格式化小数
        if (sum == 0) {
            return "0.00";
        }
        float num = (float) (diff * 100) / sum;
        String str = df.format(num);
        return str;
    }
}
