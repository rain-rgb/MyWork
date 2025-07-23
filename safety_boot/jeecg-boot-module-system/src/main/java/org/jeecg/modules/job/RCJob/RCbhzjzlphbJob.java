package org.jeecg.modules.job.RCJob;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import com.trtm.iot.system.entity.Shigongphb;
import com.trtm.iot.system.service.IShigongphbService;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class RCbhzjzlphbJob implements Job {
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private IShigongphbService shigongphbService;
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.RCBHZ_JZL_PHB);//瑞仓拌合站浇筑令配合比数据推送
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info(String.format("未获取到瑞仓拌合站浇筑令配合比数据推送定时任务的配置信息" + DateUtils.now()));
            return;
        }
        //当前数据检测到的目标id
        Integer curid = selectsysconfigone.getCurid();
        String token = selectsysconfigone.getToken();
        List<Shigongphb> list = shigongphbService.selectPhbList(curid);
        if (null == list || list.size() == 0) {
            log.info(String.format("暂无瑞仓拌合站浇筑令配合比未推送数据" + DateUtils.now()));
            return;
        }
        int id = 0;
        for (Shigongphb shigongphb : list){
            id = shigongphb.getId();
            JSONObject jsonObject2 = new JSONObject();
            jsonObject2.set("Station", shigongphb.getStation());
            jsonObject2.set("Code", shigongphb.getCode());
            jsonObject2.set("Tag", shigongphb.getTag());
            jsonObject2.set("BetLev", shigongphb.getBetlev());
            jsonObject2.set("Filter", shigongphb.getFilter());
            jsonObject2.set("Freeze", shigongphb.getFreeze());
            jsonObject2.set("Season", shigongphb.getSeason());
            jsonObject2.set("Lands", shigongphb.getLands());
            jsonObject2.set("MixLast", shigongphb.getMixlast());
            jsonObject2.set("Scale", shigongphb.getScale());
            jsonObject2.set("OtherReq", shigongphb.getOtherreq());
            jsonObject2.set("Exper", shigongphb.getExper());
            jsonObject2.set("Assoss", shigongphb.getAssoss());
            jsonObject2.set("DatTim", shigongphb.getDattim());
            jsonObject2.set("Flag", shigongphb.getFlag());
            jsonObject2.set("Note", shigongphb.getNote());
            jsonObject2.set("M1", shigongphb.getM1());
            jsonObject2.set("U1", shigongphb.getU1());
            jsonObject2.set("M2", shigongphb.getM2());
            jsonObject2.set("U2", shigongphb.getU2());
            jsonObject2.set("M3", shigongphb.getM3());
            jsonObject2.set("U3", shigongphb.getU3());
            jsonObject2.set("M4", shigongphb.getM4());
            jsonObject2.set("U4", shigongphb.getU4());
            jsonObject2.set("M5", shigongphb.getM5());
            jsonObject2.set("U5", shigongphb.getU5());
            jsonObject2.set("M6", shigongphb.getM6());
            jsonObject2.set("U6", shigongphb.getU6());
            jsonObject2.set("M7", shigongphb.getM7());
            jsonObject2.set("U7", shigongphb.getU7());
            jsonObject2.set("M8", shigongphb.getM8());
            jsonObject2.set("U8", shigongphb.getU8());
            jsonObject2.set("M9", shigongphb.getM9());
            jsonObject2.set("U9", shigongphb.getU9());
            jsonObject2.set("M10", shigongphb.getM10());
            jsonObject2.set("U10", shigongphb.getU10());
            jsonObject2.set("M11", shigongphb.getM11());
            jsonObject2.set("U11", shigongphb.getU11());
            jsonObject2.set("M12", shigongphb.getM12());
            jsonObject2.set("U12", shigongphb.getU12());
            jsonObject2.set("M13", shigongphb.getM13());
            jsonObject2.set("U13", shigongphb.getU13());
            jsonObject2.set("M14", shigongphb.getM14());
            jsonObject2.set("U14", shigongphb.getU14());
            jsonObject2.set("M15", shigongphb.getM15());
            jsonObject2.set("U15", shigongphb.getU15());
            jsonObject2.set("M16", shigongphb.getM16());
            jsonObject2.set("U16", shigongphb.getU16());
            jsonObject2.set("M17", shigongphb.getM17());
            jsonObject2.set("U17", shigongphb.getU17());
            jsonObject2.set("M18", shigongphb.getM18());
            jsonObject2.set("U18", shigongphb.getU18());
            jsonObject2.set("M19", shigongphb.getM19());
            jsonObject2.set("U19", shigongphb.getU19());
            jsonObject2.set("M20", shigongphb.getM20());
            jsonObject2.set("U20", shigongphb.getU20());
            jsonObject2.set("M21", shigongphb.getM21());
            jsonObject2.set("U21", shigongphb.getM21());
            jsonObject2.set("M22", shigongphb.getM22());
            jsonObject2.set("U22", shigongphb.getU22());
            jsonObject2.set("M23", shigongphb.getM23());
            jsonObject2.set("U23", shigongphb.getU23());
            jsonObject2.set("M24", shigongphb.getM24());
            jsonObject2.set("U24", shigongphb.getU24());
            jsonObject2.set("M25", shigongphb.getM25());
            jsonObject2.set("U25", shigongphb.getU25());
            jsonObject2.set("M26", shigongphb.getM26());
            jsonObject2.set("U26", shigongphb.getU26());
            jsonObject2.set("M27", shigongphb.getM27());
            jsonObject2.set("U27", shigongphb.getU27());
            jsonObject2.set("M28", shigongphb.getM28());
            jsonObject2.set("U28", shigongphb.getU28());
            jsonObject2.set("M29", shigongphb.getM29());
            jsonObject2.set("U29", shigongphb.getU29());
            jsonObject2.set("M30", shigongphb.getM30());
            jsonObject2.set("U30", shigongphb.getU30());
            jsonObject2.set("M31", shigongphb.getM31());
            jsonObject2.set("U31", shigongphb.getU31());
            jsonObject2.set("M32", shigongphb.getM32());
            jsonObject2.set("U32", shigongphb.getU32());
            jsonObject2.set("shebeibianhao", shigongphb.getShebeibianhao());
            jsonObject2.set("sys_org_code", shigongphb.getSysOrgCode());
            jsonObject2.set("create_by", shigongphb.getCreateBy());
            jsonObject2.set("code1", shigongphb.getCode1());
            jsonObject2.set("ProjName", shigongphb.getProjname());
            jsonObject2.set("ConsPos", shigongphb.getConspos());
            jsonObject2.set("Pour", shigongphb.getPour());
            jsonObject2.set("mc4", shigongphb.getMc4());
            jsonObject2.set("mc5", shigongphb.getMc5());
            jsonObject2.set("mc6", shigongphb.getMc6());
            jsonObject2.set("mc7", shigongphb.getMc7());
            jsonObject2.set("mc8", shigongphb.getMc8());
            jsonObject2.set("mc9", shigongphb.getMc9());
            jsonObject2.set("mc10", shigongphb.getMc10());
            jsonObject2.set("mc12", shigongphb.getMc12());
            jsonObject2.set("ru1", shigongphb.getRu1());
            jsonObject2.set("ru2", shigongphb.getRu2());
            jsonObject2.set("ru3", shigongphb.getRu3());
            jsonObject2.set("ru4", shigongphb.getRu4());
            jsonObject2.set("ru5", shigongphb.getRu5());
            jsonObject2.set("ru6", shigongphb.getRu6());
            jsonObject2.set("ru7", shigongphb.getRu7());
            jsonObject2.set("ru8", shigongphb.getRu8());
            jsonObject2.set("ru9", shigongphb.getRu9());
            jsonObject2.set("ru10", shigongphb.getRu10());
            jsonObject2.set("ru11", shigongphb.getRu11());
            jsonObject2.set("ru12", shigongphb.getRu12());
            jsonObject2.set("ru13", shigongphb.getRu13());
            jsonObject2.set("renwudan", shigongphb.getRenwudan());
            jsonObject2.set("mcl4", shigongphb.getMcl4());
            jsonObject2.set("mcl5", shigongphb.getMcl5());
            jsonObject2.set("mcl6", shigongphb.getMcl6());
            jsonObject2.set("mcl7", shigongphb.getMcl7());
            jsonObject2.set("mcl8", shigongphb.getMcl8());
            jsonObject2.set("mcl9", shigongphb.getMcl9());
            jsonObject2.set("mcl10", shigongphb.getMcl10());
            jsonObject2.set("mcl12", shigongphb.getMcl12());
            jsonObject2.set("Mete", shigongphb.getMete());
            jsonObject2.set("waterbindratio", shigongphb.getWaterbindratio());
            jsonObject2.set("lc1", shigongphb.getLc1());
            jsonObject2.set("lc2", shigongphb.getLc2());
            jsonObject2.set("lc3", shigongphb.getLc3());
            jsonObject2.set("lc4", shigongphb.getLc4());
            jsonObject2.set("lc5", shigongphb.getLc5());
            jsonObject2.set("lc6", shigongphb.getLc6());
            jsonObject2.set("lc7", shigongphb.getLc7());
            jsonObject2.set("lc8", shigongphb.getLc8());
            jsonObject2.set("lc9", shigongphb.getLc9());
            jsonObject2.set("lc10", shigongphb.getLc10());
            jsonObject2.set("lc11", shigongphb.getLc11());
            jsonObject2.set("lc12", shigongphb.getLc12());
            jsonObject2.set("lc13", shigongphb.getLc13());
            jsonObject2.set("isdel", shigongphb.getIsdel());
            jsonObject2.set("pici1", shigongphb.getPici1());
            jsonObject2.set("pici2", shigongphb.getPici2());
            jsonObject2.set("pici3", shigongphb.getPici3());
            jsonObject2.set("pici4", shigongphb.getPici4());
            jsonObject2.set("pici5", shigongphb.getPici5());
            jsonObject2.set("pici6", shigongphb.getPici6());
            jsonObject2.set("pici7", shigongphb.getPici7());
            jsonObject2.set("pici8", shigongphb.getPici8());
            jsonObject2.set("pici9", shigongphb.getPici9());
            jsonObject2.set("pici10", shigongphb.getPici10());
            jsonObject2.set("pici11", shigongphb.getPici11());
            jsonObject2.set("zhiliangcode", shigongphb.getZhiliangcode());
            jsonObject2.set("customer", shigongphb.getCustomer());
            jsonObject2.set("projadr", shigongphb.getProjadr());
            System.out.println(jsonObject2);
            String back = HttpRequest.post("http://47.96.161.157:1080/api/iot/hnt/tphb/IshigongphbUpload")
                    .header("Authorization", String.format("%s %s", "Bearer", token))
                    .body(String.valueOf(jsonObject2))
                    .execute()
                    .body();
            JSONObject jsonObject3 = new JSONObject(back);
            Integer code = (Integer) jsonObject3.get("code");
            if (code == 200) {
                log.info(String.format("瑞仓拌合站浇筑令配合比数据推送成功!" + id));
                sysConfigService.updateSysConfig(JobUtil.RCBHZ_JZL_PHB, id);//根据传过来的唯一值来修改当前判断到的数据id
            }else if(code == 201){
                log.info(String.format("瑞仓拌合站浇筑令配合比数据推送失败，数据重复，更新配置信息!" + id));
                sysConfigService.updateSysConfig(JobUtil.RCBHZ_JZL_PHB, id);//根据传过来的唯一值来修改当前判断到的数据id
            } else {
                log.info(String.format("瑞仓拌合站浇筑令配合比数据推送失败!" + id));
            }
        }
    }
}
