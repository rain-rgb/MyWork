package org.jeecg.modules.job.Licangjob;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import com.trtm.iot.system.entity.Bhzrenwudan;
import com.trtm.iot.system.entity.Shigongphb;
import com.trtm.iot.system.mapper.BhzrenwudanMapper;
import com.trtm.iot.system.mapper.ShigongphbMapper;
import com.trtm.iot.wzliaocang.entity.Wzliaocang;
import com.trtm.iot.wzliaocang.mapper.WzliaocangMapper;
import com.trtm.iot.wztaizhang.entity.Wztaizhang;
import com.trtm.iot.wztaizhang.mapper.WztaizhangMapper;
import com.trtm.iot.yclud.entity.YclUsageDetail;
import com.trtm.iot.yclud.service.IYclUsageDetailService;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.DateUtils;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 统计料仓累计使用
 */
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class LiCangSyJob implements Job {
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private ShigongphbMapper shigongphbMapper;
    @Autowired
    private WzliaocangMapper wzliaocangMapper;
    @Autowired
    private IYclUsageDetailService detailService;
    @Autowired
    private BhzrenwudanMapper bhzrenwudanMapper;
    @Autowired
    private WztaizhangMapper wztaizhangMapper;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.TJ_LIAOCANG_SY);//查询料仓使用的设备
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info(String.format("未获取到料仓累计使用统计信息" + DateUtils.now()));
            return;
        }
        String extra = selectsysconfigone.getExtra();
        JSONObject jsonObject = JSONUtil.parseObj(extra);
        if (null == jsonObject || jsonObject.isEmpty()) {
            log.info(String.format("没有配置要统计使用的设备" + DateUtils.now()));
            return;
        }
        String shebeilist = jsonObject.getStr("shebeilist");
        List<String> sbs = Arrays.asList(shebeilist.split(","));

        List<Shigongphb> shigongphbList = shigongphbMapper.selectListSs(sbs);
        if (shigongphbList.size() == 0) {
            log.info(String.format("设备：(" + shebeilist + ")      设备没有未统计的数据"));
            return;
        }
        for (Shigongphb shigongphb : shigongphbList) {
            if (oConvertUtils.isEmpty(shigongphb.getMete()) || shigongphb.getMete() == 0) {
                shigongphbMapper.updateTjById(shigongphb.getId(), 2);
                log.error("shigongphb中id 为： {} 的数据设计方量字段为空问题" + shigongphb.getId());
                continue;
            }
            try {
                updateSy(shigongphb);
            } catch (Exception e) {
                e.printStackTrace();
                shigongphbMapper.updateTjById(shigongphb.getId(), 2);
                log.error("shigongphb中id 为： {} 数据 统计失败原因是 ： {}" + shigongphb.getId() , e.getMessage());
            }
        }
    }

    @Transactional
    public void updateSy(Shigongphb shigongphb) {

        List<YclUsageDetail> details = new ArrayList<>();

        for (int i = 1; i < 18; i++) {
            Map map = getLc(shigongphb, i);
            if (oConvertUtils.isEmpty(map.get("lc")) || oConvertUtils.isEmpty(map.get("ru")) || 0 == (Double) map.get("ru")) {
                continue;
            }
            String lc = map.get("lc").toString();
            Double ru = Double.valueOf(map.get("ru").toString());
            String sysOrgCode = shigongphb.getSysOrgCode();

            YclUsageDetail detail = new YclUsageDetail();//材料使用明细表添加
            detail.setCreateBy(shigongphb.getCreateBy());
            detail.setCreateTime(shigongphb.getCreateTime());
            detail.setDosingOrderNumber(shigongphb.getCode());
            detail.setDosingTime(shigongphb.getCreateTime());
            detail.setProjectPart(shigongphb.getConspos());
            detail.setSysOrgCode(shigongphb.getSysOrgCode());
            detail.setStorageId(lc);
            detail.setInspectionLotNumber(oConvertUtils.isEmpty(map.get("pici")) ? "" : map.get("pici").toString());
            detail.setUses(String.valueOf(ru / 1000));
            detail.setRenwudan(shigongphb.getRenwudan());
            detail.setStorageName(oConvertUtils.isEmpty(map.get("m")) ? "" : map.get("m").toString());

            Wzliaocang wzliaocang = null;
            if (generateJudgment(lc)) {
                QueryWrapper<Wzliaocang> wrapper = new QueryWrapper<>();
                wrapper.select("id, guid, ljshiyong, ljxiuzheng, ljjinchang").eq("sys_org_code", sysOrgCode).eq("cailiaoname", lc).last("FOR UPDATE");
                wzliaocang = wzliaocangMapper.selectOne(wrapper);
            } else {
                wzliaocang = wzliaocangMapper.selectOne(new QueryWrapper<Wzliaocang>().select("id, guid, ljshiyong, ljxiuzheng, ljjinchang").eq("guid", lc).last("FOR UPDATE"));
            }

            if (oConvertUtils.isNotEmpty(wzliaocang)) {
                wzliaocang.setLjshiyong(ru / 1000 + wzliaocang.getLjshiyong());
                wzliaocangMapper.updateById(wzliaocang);
                detail.setStorageId(wzliaocang.getGuid());
                if (oConvertUtils.isNotEmpty(map.get("pici"))) {
                    QueryWrapper<Wztaizhang> zwrapper = new QueryWrapper<>();
//                    zwrapper.select("cailiaoNo, guigexinghao").eq("LCbianhao", wzliaocang.getGuid()).eq("pici", map.get("pici").toString());
                    zwrapper.select("cailiaoNo, guigexinghao").eq("pici", map.get("pici").toString());
                    List<Wztaizhang>  wztaizhangs = wztaizhangMapper.selectList(zwrapper);
                    if(wztaizhangs.size()>0){
                        detail.setGuigexinghao(wztaizhangs.get(0).getGuigexinghao());
                        detail.setCailiaono(wztaizhangs.get(0).getCailiaono());
                    }

                }
            }

            QueryWrapper<Bhzrenwudan> wrapperDan = new QueryWrapper<>();
//            wrapperDan.eq("sys_org_code", sysOrgCode).eq("Code", shigongphb.getRenwudan());
            wrapperDan.eq("Code", shigongphb.getRenwudan());
            Bhzrenwudan bhzrenwudan = bhzrenwudanMapper.selectOne(wrapperDan);
            if (oConvertUtils.isNotEmpty(bhzrenwudan)) {
                detail.setTreeid(bhzrenwudan.getTreeid());
                detail.setCode(bhzrenwudan.getProjgrade());
                if (oConvertUtils.isEmpty(detail.getProjectPart())) {
                    detail.setProjectPart(bhzrenwudan.getConspos());
                }
            }

            details.add(detail);
        }
        if (details.size() > 0) {
            QueryWrapper<YclUsageDetail> queryWrapper =  new QueryWrapper<>();
            queryWrapper.eq("dosing_order_number",shigongphb.getCode());
            List<YclUsageDetail> list = detailService.list(queryWrapper);
            if(list.size()>0){
                detailService.remove(queryWrapper);
            }
                detailService.saveBatch(details, details.size());


        }

        shigongphbMapper.updateTjById(shigongphb.getId(), 1);
    }


    /**
     * 根据每一条数据查询对应的料斗号和对应的使用量
     * 比如，lc1 对应使用量 ru1
     *
     * @param shigongphb
     * @param i
     * @return lc： 料斗号，    ru： 使用量
     */
    private Map getLc(Shigongphb shigongphb, int i) {
        Map map = new HashMap<>();
        Double mete = shigongphb.getMete();//任务方量
        String lc = "";
        Double ru = 0.000;
        String pici = "";
        String m = "";
        switch (i) {
            case 1:
                lc = shigongphb.getLc1();
                ru = shigongphb.getRu1();
                pici = shigongphb.getPici1();
                m = shigongphb.getM1();
                break;
            case 2:
                lc = shigongphb.getLc2();
                ru = shigongphb.getRu2();
                pici = shigongphb.getPici2();
                m = shigongphb.getM2();
                break;
            case 3:
                lc = shigongphb.getLc3();
                ru = shigongphb.getRu3();
                pici = shigongphb.getPici3();
                m = shigongphb.getM3();
                break;
            case 4:
                lc = shigongphb.getLc4();
                ru = shigongphb.getRu4();
                pici = shigongphb.getPici4();
                m = shigongphb.getM4();
                break;
            case 5:
                lc = shigongphb.getLc5();
                ru = shigongphb.getRu5();
                pici = shigongphb.getPici5();
                m = shigongphb.getM5();
                break;
            case 6:
                lc = shigongphb.getLc6();
                ru = shigongphb.getRu6();
                pici = shigongphb.getPici6();
                m = shigongphb.getM6();
                break;
            case 7:
                lc = shigongphb.getLc7();
                ru = shigongphb.getRu7();
                pici = shigongphb.getPici7();
                m = shigongphb.getM7();
                break;
            case 8:
                lc = shigongphb.getLc8();
                ru = shigongphb.getRu8();
                pici = shigongphb.getPici8();
                m = shigongphb.getM8();
                break;
            case 9:
                lc = shigongphb.getLc9();
                ru = shigongphb.getRu9();
                pici = shigongphb.getPici9();
                m = shigongphb.getM9();
                break;
            case 10:
                lc = shigongphb.getLc10();
                ru = shigongphb.getRu10();
                pici = shigongphb.getPici10();
                m = shigongphb.getM10();
                break;
            case 11:
                lc = shigongphb.getLc11();
                ru = shigongphb.getRu11();
                pici = shigongphb.getPici11();
                m = shigongphb.getM11();
                break;
            case 12:
                lc = shigongphb.getLc12();
                ru = shigongphb.getRu12();
                pici = shigongphb.getPici12();
                m = shigongphb.getM12();
                break;
            case 13:
                lc = shigongphb.getLc13();
                ru = shigongphb.getRu13();
                pici = shigongphb.getPici13();
                m = shigongphb.getM13();
                break;
            case 14:
                lc = shigongphb.getLc14();
                ru = shigongphb.getRu14();
                pici = shigongphb.getPici14();
                m = shigongphb.getM14();
                break;
            case 15:
                lc = shigongphb.getLc15();
                ru = shigongphb.getRu15();
                pici = shigongphb.getPici15();
                m = shigongphb.getM15();
                break;
            case 16:
                lc = shigongphb.getLc16();
                if (oConvertUtils.isNotEmpty(shigongphb.getRu16())) {
                    ru = Double.valueOf(shigongphb.getRu16());
                }
                pici = shigongphb.getPici16();
                m = shigongphb.getM16();
                break;
            case 17:
                lc = shigongphb.getLc17();
                if (oConvertUtils.isNotEmpty(shigongphb.getRu17())) {
                    ru = Double.valueOf(shigongphb.getRu17());
                }
                pici = shigongphb.getPici17();
                m = shigongphb.getM17();
                break;
        }
        if (oConvertUtils.isNotEmpty(ru)) {
            ru = ru * mete;
        }
        map.put("lc", lc);
        map.put("ru", ru);
        map.put("pici", pici);
        map.put("m", m);
        return map;
    }


    private boolean generateJudgment(String countname) {
        Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher isNum = p.matcher(countname);
        if (isNum.find()) {
            return true;
        }
        return false;
    }
}
