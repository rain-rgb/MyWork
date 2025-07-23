package org.jeecg.modules.job.stprojectjob;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.bys.service.IBysRealService;
import com.trtm.iot.syj.entity.TSyjzb;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import com.trtm.iot.system.entity.ShebeiInfo;
import com.trtm.iot.system.service.IShebeiInfoService;
import com.trtm.iot.trzhanglarenwudan.entity.TrZhanglaRenwudan;
import com.trtm.iot.trzhanglarenwudan.service.ITrZhanglaRenwudanService;
import com.trtm.iot.zhanglam.entity.TrZhanglaM;
import com.trtm.iot.zhanglam.service.ITrZhanglaMService;
import com.trtm.iot.zhanglas.entity.TrZhanglaS;
import com.trtm.iot.zhanglas.service.ITrZhanglaSService;
import com.trtm.iot.zhanglaxxb.entity.TrZhanglaXxb;
import com.trtm.iot.zhanglaxxb.service.ITrZhanglaXxbService;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.A;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class STzhanglaJob implements Job {
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private IShebeiInfoService shebeiInfoService;
    @Autowired
    private ITrZhanglaXxbService zhanglaXxbService;
    @Autowired
    private ITrZhanglaMService zhanglaMService;
    @Autowired
    private ITrZhanglaSService zhanglaService;
    @Autowired
    private ITrZhanglaRenwudanService zhanglaRenwudanService;


    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.ST_ZHANGLA);
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info(String.format("未获取到苏台张拉定时任务的配置信息" + DateUtils.now()));
            return;
        }
        //当前数据检测到的目标id
        Integer curid = selectsysconfigone.getCurid();
        String extra = selectsysconfigone.getExtra();
        JSONObject jsonObject = JSONUtil.parseObj(extra);
        if (null == jsonObject || jsonObject.isEmpty()) {
            log.info(String.format("没有配置要传输苏台张拉的设备" + DateUtils.now()));
            return;
        }
        String shebeilist = jsonObject.getStr("shebeilist");
        List<TrZhanglaXxb> zhanglaXxbList = zhanglaXxbService.selectBYSBList(shebeilist,curid);
        if (null == zhanglaXxbList || zhanglaXxbList.size() == 0) {
            log.info(String.format("暂无苏台张拉的数据" + DateUtils.now()));
            return;
        }
//        JSONObject getJson = new JSONObject();
//        getJson.set("from", "testSoft");
        String get = HttpRequest.get("http://111.3.69.250:19030/doc-as/api/ext/token?from=testSoft")
                .header("Content-Type", "application/json")
//                .body(String.valueOf(getJson))
                .execute()
                .body();
        JSONObject jsonObject1 = new JSONObject(get);
        Integer code1 = (Integer) jsonObject1.get("code");
        if (code1 == 200) {
            JSONObject data = (JSONObject) jsonObject1.get("data");
            String token = data.get("access_token").toString();
            int id = 0;
            for (TrZhanglaXxb xxb : zhanglaXxbList) {
                id = xxb.getId();
                JSONObject saveDTOList = new JSONObject();
                QueryWrapper<TrZhanglaRenwudan> renwudanQueryWrapper = new QueryWrapper<>();
                renwudanQueryWrapper.eq("uuid", xxb.getUuid());
                TrZhanglaRenwudan one = zhanglaRenwudanService.getOne(renwudanQueryWrapper);
                if (one == null) {
                    log.info(String.format("id="+id+"张拉数据未绑定任务单!" + xxb.getUuid()));
                    sysConfigService.updateSysConfig(JobUtil.ST_ZHANGLA, id);//根据传过来的唯一值来修改当前判断到的数据id
                    continue;
                }
                Map wbsMap = zhanglaRenwudanService.selectwbs(one.getSgbwuuid());
                if (wbsMap == null) {
                    log.info(String.format("id="+id+"张拉数据未绑定wbs!" + xxb.getUuid()));
                    continue;
                }
                if(!wbsMap.get("id").toString().startsWith("TJ")){
                    sysConfigService.updateSysConfig(JobUtil.ST_ZHANGLA, id);//根据传过来的唯一值来修改当前判断到的数据id
                    continue;
                }
                saveDTOList.set("zhanglaKZYL", one.getSjzll());
                saveDTOList.set("projCode", "NXTXDQ");
                saveDTOList.set("htNo", one.getSysOrgCode());
                ShebeiInfo selectshebeione = shebeiInfoService.selectshebeione(xxb.getShebeibianhao());
                saveDTOList.set("companyCode", selectshebeione.getSysOrgCode());
                saveDTOList.set("wbsNo", wbsMap.get("id").toString().substring(4));
                saveDTOList.set("wbsFullName", wbsMap.get("description").toString());
                saveDTOList.set("position", xxb.getGjbh());
                saveDTOList.set("TaskUUId", xxb.getSyjid());
                saveDTOList.set("zhanglarq", xxb.getSgsj());
                saveDTOList.set("zhanglaqw", null);
                saveDTOList.set("ldqjdzllwcb", null);
//
//                List<Map> mapList = new ArrayList<>();
//                Map map1 = new HashMap();
//                map1.put("zlybh", xxb.getZly1());
//                map1.put("ybbh", xxb.getYbbh1());
//                map1.put("bdrq", xxb.getBdrq1());
//                Map map2 = new HashMap();
//                map2.put("zlybh", xxb.getZly2());
//                map2.put("ybbh", xxb.getYbbh2());
//                map2.put("bdrq", xxb.getBdrq2());
//                mapList.add(map1);
//                mapList.add(map2);
//                saveDTOList.set("zly1Datas", mapList);
                List<Map> gsDatas = new ArrayList<>();
                QueryWrapper<TrZhanglaM> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("syjid", xxb.getSyjid());
                List<TrZhanglaM> mList = zhanglaMService.list(queryWrapper);
                if (null != mList || mList.size() != 0) {
                    for (TrZhanglaM trZhanglaM : mList) {
                        QueryWrapper<TrZhanglaS> zhanglaSQueryWrapper = new QueryWrapper<>();
                        zhanglaSQueryWrapper.eq("fguid", trZhanglaM.getFguid());
                        List<TrZhanglaS> zhanglaSList = zhanglaService.list(zhanglaSQueryWrapper);
                        Map map = new HashMap();
                        map.put("gsbh", trZhanglaM.getGsbh());
                        map.put("zscl", trZhanglaM.getZscl());
                        map.put("llscl", trZhanglaM.getLlscl());
                        map.put("sclper", trZhanglaM.getSclper());
                        map.put("hsl", trZhanglaM.getHtl());
                        map.put("wt", trZhanglaM.getWt());
//                        List<Map> gsSubDatas = new ArrayList<>();

                        Map temp1 = new HashMap();
                        temp1.put("dh", "前端");
                        temp1.put("zlybh", xxb.getZly1());
                        temp1.put("ybbh", xxb.getYbbh1());
                        temp1.put("bdrq", xxb.getBdrq1());
                        temp1.putAll(map);
                        Map temp2 = new HashMap();
                        temp2.put("dh", "后端");
                        temp2.put("zlybh", xxb.getZly2());
                        temp2.put("ybbh", xxb.getYbbh2());
                        temp2.put("bdrq", xxb.getBdrq2());
                        temp2.putAll(map);
                        for (TrZhanglaS zhanglaS : zhanglaSList) {
                            String dh = zhanglaS.getDh();
                            String jdbfb = zhanglaS.getJdbfb();
                            if (dh.equals("1") && jdbfb.equals("10")) {
                                temp1.put("cylscl", zhanglaS.getJdscl());
                            }
                            if (dh.equals("1") && jdbfb.equals("20")) {
                                temp1.put("cylscl2", zhanglaS.getJdscl());
                            }
                            if (dh.equals("1") && jdbfb.equals("50")) {
                                temp1.put("dyd", zhanglaS.getJdscl());
                            }
                            if (dh.equals("1") && jdbfb.equals("50")) {
                                temp1.put("ded", zhanglaS.getJdscl());
                            }
                            if (dh.equals("1") && jdbfb.equals("100")) {
                                temp1.put("mgsscl", zhanglaS.getJdscl());
                            }
                            if (dh.equals("2") && jdbfb.equals("10")) {
                                temp2.put("cylscl", zhanglaS.getJdscl());
                            }
                            if (dh.equals("2") && jdbfb.equals("20")) {
                                temp2.put("cylscl2", zhanglaS.getJdscl());
                            }
                            if (dh.equals("2") && jdbfb.equals("50")) {
                                temp2.put("dyd", zhanglaS.getJdscl());
                            }
                            if (dh.equals("2") && jdbfb.equals("50")) {
                                temp2.put("ded", zhanglaS.getJdscl());
                            }
                            if (dh.equals("2") && jdbfb.equals("100")) {
                                temp2.put("mgsscl", zhanglaS.getJdscl());
                            }
                        }
//                        gsSubDatas.add(temp1);
//                        gsSubDatas.add(temp2);
//                        map.put("gsSubDatas", gsSubDatas);
                        gsDatas.add(temp1);
                        gsDatas.add(temp2);
                    }
                }
                saveDTOList.set("gsDatas", gsDatas);
                Map map = new HashMap();
                map.put("extSystemId","bf43b3ed-b108-4146-b813-00197dc84cd0");
                map.put("DocSystemCode","NXDQLJX");
//                map.put("projCode","NXDQLJX");
                map.put("formData",saveDTOList);
//                System.out.println(map);
                String post = HttpRequest.post("http://111.3.69.250:19030/doc-as/api/ext/upload-zhangla-datas")
                        .header("Content-Type", "multipart/form-data")
                        .header("token", token)
                        .form(map)
                        .execute()
                        .body();
                JSONObject postObject = new JSONObject(post);
                Integer code = (Integer) postObject.get("code");
                if (code == 200) {
                    log.info(String.format("苏台张拉数据推送成功!" + id));
                    sysConfigService.updateSysConfig(JobUtil.ST_ZHANGLA, id);//根据传过来的唯一值来修改当前判断到的数据id
                } else {
                    log.info(String.format("苏台张拉数据推送失败!" + id));
                }
            }
        }
    }
}
