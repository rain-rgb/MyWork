package org.jeecg.modules.job.RCJob;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import com.trtm.iot.system.entity.Bhzrenwudan;
import com.trtm.iot.system.entity.Shigongphb;
import com.trtm.iot.system.service.IBhzrenwudanService;
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
 * @ClassName BhzZJzlJob：数据推送甬台温的是32702
 * @Description TODO
 * @Author 55314
 * @Date 2022/7/20 10:44
 * @Version 1.0
 **/
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class YTWBhzZJzlJob implements Job {
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private IShigongphbService shigongphbService;
    @Autowired
    private IBhzrenwudanService bhzrenwudanService;
    @Autowired
    private ISysDepartprojectService sysDepartprojectService;

    private final static Integer index_id = 1238;
    private final static Integer old_id = 112;
    private final static String treeid = "1186|1187|1193|1198|1201|1211|1238|";
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.YTW_ZJBHZTS);//甬台温拌合站质检资料推送
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
        for (Shigongphb shigongphb : shigongphbs) {
            JSONObject sendObject = JSONUtil.createObj();
            id = shigongphb.getId();
//            sendObject.set("ID", shigongphb.getId());
            QueryWrapper<Bhzrenwudan> bhzrenwudanQueryWrapper = new QueryWrapper<>();
            bhzrenwudanQueryWrapper.eq("Code",shigongphb.getRenwudan());
            List<Bhzrenwudan> list = bhzrenwudanService.list(bhzrenwudanQueryWrapper);
            if (list.size()>0){
                sendObject.set("designyl", list.get(0).getMete());
                sendObject.set("sgrq", format.format(list.get(0).getBegtim()));
                sendObject.set("jcjlrq", list.get(0).getDattim());
                sendObject.set("yjjzsj", list.get(0).getBegtim());

                String projgrade = list.get(0).getProjgrade();
                if (projgrade!=null) {
                    QueryWrapper<SysDepartproject> sysDepartprojectQueryWrapper = new QueryWrapper<>();
                    sysDepartprojectQueryWrapper.eq("org_code", list.get(0).getProjgrade());
                    List<SysDepartproject> list1 = sysDepartprojectService.list(sysDepartprojectQueryWrapper);
                    if (list1.size() > 0) {
                        sendObject.set("treeid", list1.get(0).getTreeid());
                        if (list1.get(0).getOrgType().equals("4")) {
                            sendObject.set("index_id", list1.get(0).getId().substring(2));
                            sendObject.set("old_id", list1.get(0).getOldid());

                            QueryWrapper<SysDepartproject> fbfxq = new QueryWrapper<>();
                            fbfxq.eq("id", list1.get(0).getParentId());
                            List<SysDepartproject> fbfx = sysDepartprojectService.list(fbfxq);
                            sendObject.set("fxgc", fbfx.get(0).getDepartName() + "、" + list1.get(0).getDepartName());

                            QueryWrapper<SysDepartproject> fbfxq1 = new QueryWrapper<>();
                            fbfxq1.eq("id", fbfx.get(0).getParentId());
                            List<SysDepartproject> fbfx1 = sysDepartprojectService.list(fbfxq1);
                            sendObject.set("fbgc", fbfx1.get(0).getDepartName());

                            QueryWrapper<SysDepartproject> fbfxq2 = new QueryWrapper<>();
                            fbfxq2.eq("id", fbfx1.get(0).getParentId());
                            List<SysDepartproject> fbfx2 = sysDepartprojectService.list(fbfxq2);
                            sendObject.set("dwgc", fbfx2.get(0).getDepartName());
                        }
                        if (list1.get(0).getOrgType().equals("6")) {
                            sendObject.set("index_id", list1.get(0).getId().substring(2));
                            sendObject.set("old_id", list1.get(0).getOldid());

                            QueryWrapper<SysDepartproject> fbfxq = new QueryWrapper<>();
                            fbfxq.eq("id", list1.get(0).getParentId());
                            List<SysDepartproject> fbfx = sysDepartprojectService.list(fbfxq);
                            sendObject.set("fxgc", fbfx.get(0).getDepartName() + "、" + list1.get(0).getDepartName());

                            QueryWrapper<SysDepartproject> fbfxq1 = new QueryWrapper<>();
                            fbfxq1.eq("id", fbfx.get(0).getParentId());
                            List<SysDepartproject> fbfx1 = sysDepartprojectService.list(fbfxq1);


                            QueryWrapper<SysDepartproject> fbfxq2 = new QueryWrapper<>();
                            fbfxq2.eq("id", fbfx1.get(0).getParentId());
                            List<SysDepartproject> fbfx2 = sysDepartprojectService.list(fbfxq2);
                            sendObject.set("fbgc", fbfx1.get(0).getDepartName() + "、" + fbfx2.get(0).getDepartName());

                            QueryWrapper<SysDepartproject> fbfxq3 = new QueryWrapper<>();
                            fbfxq3.eq("id", fbfx2.get(0).getParentId());
                            List<SysDepartproject> fbfx3 = sysDepartprojectService.list(fbfxq3);
                            sendObject.set("dwgc", fbfx3.get(0).getDepartName());
                        }
                    }
                }
            }
            sendObject.set("index_id",index_id);
            sendObject.set("treeid",treeid);
            sendObject.set("old_id",old_id);
            sendObject.set("createdate", shigongphb.getDattim());
            sendObject.set("updatedate", shigongphb.getDattim());
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
//            sendObject.set("snsccs", shigongphb.get);
//            sendObject.set("hssccs", shigongphb.get);
//            sendObject.set("sls1sccs", shigongphb.get);
//            sendObject.set("sls2sccs", shigongphb.get);
//            sendObject.set("sls3sccs", shigongphb.get);
//            sendObject.set("cjjsccs", shigongphb.get);
//            sendObject.set("chjsccs", shigongphb.get);
//            sendObject.set("sncfd", shigongphb.get);
//            sendObject.set("hscfd", shigongphb.get);
//            sendObject.set("sls1cfd", shigongphb.get);
//            sendObject.set("sls2cfd", shigongphb.get);
//            sendObject.set("sls3cfd", shigongphb.get);
//            sendObject.set("cjjcfd", shigongphb.get);
//            sendObject.set("chjcfd", shigongphb.get);
//            sendObject.set("snpzwh", shigongphb.get);
//            sendObject.set("hspzwh", shigongphb.get);
//            sendObject.set("sls1pzwh", shigongphb.get);
//            sendObject.set("sls2pzwh", shigongphb.get);
//            sendObject.set("sls3pzwh", shigongphb.get);
//            sendObject.set("cjjpzwh", shigongphb.get);
//            sendObject.set("chjpzwh", shigongphb.get);
//            sendObject.set("snzbsl", shigongphb.get);
//            sendObject.set("hszbsl", shigongphb.get);
//            sendObject.set("sls1zbsl", shigongphb.get);
//            sendObject.set("sls2zbsl", shigongphb.get);
//            sendObject.set("sls3zbsl", shigongphb.get);
//            sendObject.set("cjjzbsl", shigongphb.get);
//            sendObject.set("chjzbsl", shigongphb.get);
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
//            sendObject.set("dwtjl", shigongphb.get);
            sendObject.set("kylq", "28");
            if (shigongphb.getBetlev() != null && shigongphb.getBetlev().length() >= 3) {
                sendObject.set("kyqd", shigongphb.getBetlev().substring(1,3));
            }
//            sendObject.set("snhsl", shigongphb.get);
            sendObject.set("hshsl", shigongphb.getMc4());
            sendObject.set("sls1hslv", shigongphb.getMc5());
            sendObject.set("sls2hslv", shigongphb.getMc6());
            sendObject.set("sls3hslv", shigongphb.getMc7());
            sendObject.set("cjjhslv", shigongphb.getMc8());
//            sendObject.set("chj1hslv", shigongphb.get);
//            sendObject.set("chj2hslv", shigongphb.get);
//            sendObject.set("snhsli", shigongphb.get);
            sendObject.set("hshsli", shigongphb.getMcl4());
            sendObject.set("sls1hsli", shigongphb.getMcl5());
            sendObject.set("sls2hsli", shigongphb.getMcl6());
            sendObject.set("sls3hsli", shigongphb.getMcl7());
            sendObject.set("cjjhsli", shigongphb.getMcl8());
//            sendObject.set("chj1hsli", shigongphb.get);
//            sendObject.set("chj2hsli", shigongphb.get);
            sendObject.set("sgsnyl", shigongphb.getRu1());
            sendObject.set("sghsyl", shigongphb.getRu4());
            sendObject.set("sgsls1yl", shigongphb.getRu5());
            sendObject.set("sgsls2yl", shigongphb.getRu6());
            sendObject.set("sgsls3yl", shigongphb.getRu7());
            sendObject.set("sgsyl", shigongphb.getRu11());
            sendObject.set("sgcjjyl", shigongphb.getRu8());
            sendObject.set("sgchj1yl", shigongphb.getRu2());
            sendObject.set("sgchj2yl", shigongphb.getRu9());
            sendObject.set("actualyl", list.get(0).getMete());

//            sendObject.set("sgtld1", shigongphb.get);
//            sendObject.set("sgtld2", shigongphb.get);
//            sendObject.set("sgtld3", shigongphb.get);

            List lists = new ArrayList();
            lists.add(sendObject);
            SecureRandom random = new SecureRandom();
            String s = String.valueOf(random.nextInt());
            String time = String.valueOf(System.currentTimeMillis());
            String signature = SignUtil.signature("e6540129236a44639778678879ce47bb", s, time);
            String encode = "";
            try {
                encode = URLEncoder.encode(lists.toString(),"UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            System.out.println(sendObject);
            String url = "http://fjhmtd.com:32702/glaf/website/ws/execute/api/crud?useId=1a2af5a06c0049bd8b3bc262c0cc113d&appId=b598a75acb65427c979d0bdd317f9846&ty=cu&params="+encode;
            String back = HttpRequest.post(url)
                    .header("x-rio-seq", s)
                    .header("signature", signature)
                    .header("timestamp", time)
                    .timeout(20000)
                    .execute().body();
            JSONObject jsonObject1 = new JSONObject(back);
            String codes = jsonObject1.get("status").toString();
            if (codes.equals("200")) {
                log.info(String.format("瑞仓配合比推送成功!" + id + "Json数据" + back));
            } else {
                log.info(String.format("瑞仓配合比推送失败!" + id + "Json数据" + back));
            }
            sysConfigService.updateSysConfig(JobUtil.RC_ZJBHZTS, id);//根据传过来的唯一值来修改当前判断到的数据id
        }
    }
}

