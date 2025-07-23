package org.jeecg.modules.job.RCJob;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.pushandreturn.service.IPushandreturnService;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import com.trtm.iot.system.entity.Bhzrenwudan;
import com.trtm.iot.system.entity.ShebeiInfo;
import com.trtm.iot.system.entity.Shigongphb;
import com.trtm.iot.system.service.IBhzrenwudanService;
import com.trtm.iot.system.service.IShebeiInfoService;
import com.trtm.iot.system.service.IShigongphbService;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.jeecg.modules.job.util.SignUtil;
import org.jeecg.modules.system.entity.SysDepartproject;
import org.jeecg.modules.system.service.ISysDepartprojectService;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @ClassName BhzZJzlJob：瑞苍数据推送32221
 * @Description TODO
 * @Author 55314
 * @Date 2022/7/20 10:44
 * @Version 1.0
 **/
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class BhzZJzlJob implements Job {
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private IShigongphbService shigongphbService;
    @Autowired
    private IBhzrenwudanService bhzrenwudanService;
    @Autowired
    private ISysDepartprojectService sysDepartprojectService;
    @Autowired
    private IPushandreturnService pushandreturnService;
    @Autowired
    private IShebeiInfoService shebeiInfoService;
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.RC_ZJBHZTS);//瑞苍拌合站质检资料推送
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info(String.format("未获取到瑞仓配合比定时任务的配置信息" + DateUtils.now()));
            return;
        }
        //当前数据检测到的目标id
        Integer curid = selectsysconfigone.getCurid();
        String extra = selectsysconfigone.getExtra();
        JSONObject jsonObject = JSONUtil.parseObj(extra);
        if (null == jsonObject || jsonObject.isEmpty()) {
            log.info(String.format("没有配置要传输瑞仓配合比的设备" + DateUtils.now()));
            return;
        }
        String shebeilist = jsonObject.getStr("shebeilist");
        List<Shigongphb> shigongphbs = shigongphbService.selectLists(shebeilist, curid);
        if (null == shigongphbs || shigongphbs.size() == 0) {
            log.info(String.format("暂无瑞仓配合比未推送数据" + DateUtils.now()));
            return;
        }
        int id = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (Shigongphb shigongphb : shigongphbs) {
            JSONObject sendObject = JSONUtil.createObj();
            id = shigongphb.getId();

            // 更新数据用实体类
            Bhzrenwudan rwd = new Bhzrenwudan();
            QueryWrapper<Bhzrenwudan> bhzrenwudanQueryWrapper = new QueryWrapper<>();
            bhzrenwudanQueryWrapper.eq("Code", shigongphb.getRenwudan());
            Bhzrenwudan one = bhzrenwudanService.getOne(bhzrenwudanQueryWrapper);
            if (null != one) {
                sendObject.set("designyl", one.getMete());
                Date begtim = one.getBegtim();
                if (begtim!= null) {
                    sendObject.set("sgrq", sdf.format(shigongphb.getJztime()));
                    sendObject.set("yjjzsj", sdf.format(shigongphb.getJztime()));
                }
                sendObject.set("jcjlrq", sdf.format(one.getDattim()));

                String projgrade = one.getProjgrade();
                if (projgrade != null) {
                    QueryWrapper<SysDepartproject> sysDepartprojectQueryWrapper = new QueryWrapper<>();
                    sysDepartprojectQueryWrapper.eq("org_code", one.getProjgrade());
                    SysDepartproject oneProject = sysDepartprojectService.getOne(sysDepartprojectQueryWrapper);
                    if (null != oneProject) {
                        String orgType = oneProject.getOrgType();
                        if (!orgType.equals("13")) {
                            String id1 = oneProject.getId();
                            QueryWrapper<SysDepartproject> sysDepartprojectQueryWrapper1 = new QueryWrapper<>();
                            sysDepartprojectQueryWrapper1.eq("parent_id", id1)
                                    .and(wrapper -> wrapper.like("depart_name", "混凝土浇筑").or().eq("depart_name", "浇筑").or().like("depart_name", "喷射混凝土"))
                                    .eq("org_type","13")
                                    .last("limit 1");
                            oneProject = sysDepartprojectService.getOne(sysDepartprojectQueryWrapper1)!=null?sysDepartprojectService.getOne(sysDepartprojectQueryWrapper1):oneProject;
                        }
                        sendObject.set("actualyl", one.getMete());
                        sendObject.set("treeid", oneProject.getTreeid());

                        sendObject.set("index_id", oneProject.getId());
                        //
                        sendObject.set("old_id", oneProject.getOldid());

                        String description = oneProject.getDescription();
                        if (description != null) {
                            String[] split = description.split("/");

                            if (split.length > 1) {
                                sendObject.set("fbgc", split[0]);
                                sendObject.set("fxgc", split[1]);
                            }else {
                                sendObject.set("fbgc",description );
                            }

                        }

                        sendObject.set("dwgc", oneProject.getDepartName());
                    }
                }
                rwd.setId(one.getId());
                rwd.setIszjzl(1);

            } else {
                shigongphb.setIszjzl(3);// 未关联浇筑令
            }
            Date dattim = shigongphb.getDattim();
            if (dattim!= null) {
                sendObject.set("createdate", sdf.format(shigongphb.getDattim()));
                sendObject.set("updatedate", sdf.format(shigongphb.getDattim()));
            }
            sendObject.set("zhbw", shigongphb.getConspos());
            sendObject.set("tqd", shigongphb.getBetlev());
            sendObject.set("yjgcl", shigongphb.getMete());
            sendObject.set("sngg", shigongphb.getM1());
            sendObject.set("hsgg", shigongphb.getM4());
            sendObject.set("sls1gg", shigongphb.getM5());
            sendObject.set("sls2gg", shigongphb.getM6());
            sendObject.set("sls3gg", shigongphb.getM7());
            sendObject.set("chjgg", shigongphb.getM8());
            sendObject.set("cjjgg", shigongphb.getM2());
            sendObject.set("pebbgbh", shigongphb.getCode());
            sendObject.set("sjqd", shigongphb.getBetlev());
            sendObject.set("sjb", shigongphb.getWaterbindratio());
            sendObject.set("sjsnyl", shigongphb.getU1());
            sendObject.set("sjhsyl", shigongphb.getU4());
            sendObject.set("sjsls1yl", shigongphb.getU5());
            sendObject.set("sjsls2yl", shigongphb.getU6());
            sendObject.set("sjsls3yl", shigongphb.getU7());
            sendObject.set("sjsyl", shigongphb.getU11());
            sendObject.set("sjscjjyl", shigongphb.getU8());
            sendObject.set("sjschj1yl", shigongphb.getU2());
            sendObject.set("sjschj2yl", shigongphb.getU9());
            sendObject.set("tld", shigongphb.getLands());
            sendObject.set("kylq", "28");
            String betlev = shigongphb.getBetlev();
            if ( betlev != null && betlev.length() >= 3) {
                if (betlev.contains("·")){
                    betlev.replace("·",".");
                }else {
                    sendObject.set("kyqd", betlev.substring(1, 3));
                }
            }else {
                sendObject.set("kyqd", betlev);
            }
            sendObject.set("hshsl", shigongphb.getMc4());
            sendObject.set("sls1hslv", shigongphb.getMc5());
            sendObject.set("sls2hslv", shigongphb.getMc6());
            sendObject.set("sls3hslv", shigongphb.getMc7());
            sendObject.set("cjjhslv", shigongphb.getMc8());
            sendObject.set("hshsli", shigongphb.getMcl4());
            sendObject.set("sls1hsli", shigongphb.getMcl5());
            sendObject.set("sls2hsli", shigongphb.getMcl6());
            sendObject.set("sls3hsli", shigongphb.getMcl7());
            sendObject.set("cjjhsli", shigongphb.getMcl8());
            sendObject.set("sgsnyl", shigongphb.getRu1());
            sendObject.set("sghsyl", shigongphb.getRu4());
            sendObject.set("sgsls1yl", shigongphb.getRu5());
            sendObject.set("sgsls2yl", shigongphb.getRu6());
            sendObject.set("sgsls3yl", shigongphb.getRu7());
            sendObject.set("sgsyl", shigongphb.getRu11());
            sendObject.set("sgcjjyl", shigongphb.getRu8());
            sendObject.set("sgchj1yl", shigongphb.getRu2());
            sendObject.set("sgchj2yl", shigongphb.getRu9());

            List lists = new ArrayList();
            lists.add(sendObject);
            SecureRandom random = new SecureRandom();
            String s = String.valueOf(random.nextInt());
            String time = String.valueOf(System.currentTimeMillis());
            String signature = SignUtil.signature("e6540129236a44639778678879ce47bb", s, time);
            String encode = "";
            try {
                encode = URLEncoder.encode(lists.toString(), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            String shebeibianhao = shigongphb.getShebeibianhao();
            ShebeiInfo sbjwd = shebeiInfoService.SBJWD(shebeibianhao);
            String zhijianid = sbjwd.getZhijianid();
            String http = "http://fjhmtd.com:"+zhijianid;
            if (zhijianid.equals("32907")||zhijianid.equals("32621")){
                http = "https://fjhmtd.com:"+zhijianid;
            }
            String url = http+"/glaf/website/ws/execute/api/crud?useId=1a2af5a06c0049bd8b3bc262c0cc113d&appId=b598a75acb65427c979d0bdd317f9846&ty=cu&params=" + encode;
            String back = HttpRequest.post(url)
                    .header("x-rio-seq", s)
                    .header("signature", signature)
                    .header("timestamp", time)
                    .timeout(20000)
                    .execute().body();

            pushandreturnService.saveData(id, String.valueOf(lists), selectsysconfigone.getRemark(), back);

            cn.hutool.json.JSONObject jsonObject2 = JSONUtil.parseObj(back);
            Object data = jsonObject2.get("data");
            List<Object> listarr = JSON.parseArray(String.valueOf(data));
            String dataItem = String.valueOf(listarr.get(0));
            cn.hutool.json.JSONObject jsonObject3 = JSONUtil.parseObj(dataItem);
            String sid = String.valueOf(jsonObject3.get("id"));

            if (back.contains("操作成功")) {
                shigongphb.setMtid(sid);
                log.info(String.format("瑞仓配合比推送成功!" + id + "Json数据" + back));
                shigongphb.setIszjzl(1);// 成功推送
                if (rwd.getId() != null) {
                    bhzrenwudanService.updateById(rwd);
                }

            } else {
                log.info(String.format("瑞仓配合比推送失败!" + id + "Json数据" + back));
                shigongphb.setIszjzl(2);// 未成功推送
            }
            shigongphbService.updateById(shigongphb);
            sysConfigService.updateSysConfig(JobUtil.RC_ZJBHZTS, id);//根据传过来的唯一值来修改当前判断到的数据id
        }
    }
}


