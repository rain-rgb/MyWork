package org.jeecg.modules.job.stprojectjob;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import com.trtm.iot.system.entity.ShebeiInfo;
import com.trtm.iot.system.service.IShebeiInfoService;
import com.trtm.iot.trzhanglarenwudan.entity.TrZhanglaRenwudan;
import com.trtm.iot.trzhanglarenwudan.service.ITrZhanglaRenwudanService;
import com.trtm.iot.yajiangm.entity.TrYajiangM;
import com.trtm.iot.yajiangm.service.ITrYajiangMService;
import com.trtm.iot.yajiangrenwudan.entity.TrYajiangRenwudan;
import com.trtm.iot.yajiangrenwudan.service.ITrYajiangRenwudanService;
import com.trtm.iot.yajiangs.entity.TrYajiangS;
import com.trtm.iot.yajiangs.service.ITrYajiangSService;
import lombok.extern.slf4j.Slf4j;
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
public class STyajiangJob implements Job {
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private IShebeiInfoService shebeiInfoService;
    @Autowired
    private ITrYajiangMService yajiangMService;
    @Autowired
    private ITrYajiangSService yajiangSService;
    @Autowired
    private ITrYajiangRenwudanService yajiangRenwudanService;
    @Autowired
    private ITrZhanglaRenwudanService zhanglaRenwudanService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.ST_YAJIANG);
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info(String.format("未获取到苏台压浆定时任务的配置信息" + DateUtils.now()));
            return;
        }
        //当前数据检测到的目标id
        Integer curid = selectsysconfigone.getCurid();
        String extra = selectsysconfigone.getExtra();
        JSONObject jsonObject = JSONUtil.parseObj(extra);
        if (null == jsonObject || jsonObject.isEmpty()) {
            log.info(String.format("没有配置要传输苏台压浆的设备" + DateUtils.now()));
            return;
        }
        String shebeilist = jsonObject.getStr("shebeilist");
        List<TrYajiangM> yajiangMList = yajiangMService.selectBySBList(shebeilist,curid);
        if (null == yajiangMList || yajiangMList.size() == 0) {
            log.info(String.format("暂无苏台压浆的数据" + DateUtils.now()));
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
            for (TrYajiangM m : yajiangMList) {
                id = m.getId();
                JSONObject saveDTOList = new JSONObject();
                QueryWrapper<TrYajiangRenwudan> renwudanQueryWrapper = new QueryWrapper<>();
                renwudanQueryWrapper.eq("uuid", m.getUuid());
                TrYajiangRenwudan one = yajiangRenwudanService.getOne(renwudanQueryWrapper);
                if (one == null) {
                    log.info(String.format("id="+id+"压浆数据未绑定任务单!" + m.getUuid()));
                    sysConfigService.updateSysConfig(JobUtil.ST_YAJIANG, id);//根据传过来的唯一值来修改当前判断到的数据id
                    continue;
                }
                Map wbsMap = zhanglaRenwudanService.selectwbs(one.getSgbwuuid());
                if (wbsMap == null) {
                    log.info(String.format("id="+id+"压浆数据未绑定wbs!" + m.getUuid()));
                    continue;
                }
                if(!wbsMap.get("id").toString().startsWith("TJ")){
                    sysConfigService.updateSysConfig(JobUtil.ST_YAJIANG, id);//根据传过来的唯一值来修改当前判断到的数据id
                    continue;
                }
                saveDTOList.set("projCode", "NXTXDQ");
                saveDTOList.set("htNo", one.getSysOrgCode());
                ShebeiInfo selectshebeione = shebeiInfoService.selectshebeione(m.getYjsbbh());
                saveDTOList.set("companyCode", selectshebeione.getSysOrgCode());
                saveDTOList.set("wbsNo", wbsMap.get("id").toString().substring(4));
                saveDTOList.set("wbsFullName", wbsMap.get("description").toString());
                saveDTOList.set("position", m.getGjbh());
                saveDTOList.set("TaskUUId", m.getSyjid());
                saveDTOList.set("wjjyl", null);
                saveDTOList.set("wjjmc", null);
                saveDTOList.set("shuijiaobi", m.getShuijiaobi());
                saveDTOList.set("yjff", null);
                saveDTOList.set("yjsj", m.getYjsj());
                saveDTOList.set("zlsj", m.getZlsj());
                saveDTOList.set("jiaobanj", null);//搅拌机
                saveDTOList.set("yajiangj", m.getYjsbbh());//压浆机
                saveDTOList.set("shjiangguangcd", null);//输浆管长度
                saveDTOList.set("qdsjz", null);//强度设计值
                saveDTOList.set("qdjcz", null);//强度检查值
                saveDTOList.set("cdsjz", null);//稠度设计值
                saveDTOList.set("cdjcz", null);//稠度检查值
                saveDTOList.set("mslsjz_zd", m.getMsl());//泌水率最大设计值
                saveDTOList.set("msljcz_zd", m.getMsl());//泌水率最大检查值
                saveDTOList.set("mslbz_zd", null);//泌水率最大备注
//                List<Map> mslDatas = new ArrayList<>();
//                Map map = new HashMap();
//                map.put("itemname","最大");
//                map.put("sj",null);
//                map.put("jcjg",null);
//                map.put("remark",null);
//                mslDatas.add(map);
//                saveDTOList.set("mslDatas", mslDatas);
                QueryWrapper<TrYajiangS> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("syjid",m.getSyjid());
                List<TrYajiangS> yajiangSList = yajiangSService.list(queryWrapper);
                List<Map> kongdaoDatas = new ArrayList<>();
                for (TrYajiangS trYajiangS : yajiangSList) {
                    Map temp = new HashMap();
                    temp.put("kongdaobh",trYajiangS.getKongdao());
                    temp.put("kongdaocd",null);//孔道长度
                    temp.put("yjsx",null);//压浆顺序
                    temp.put("yjkwz",null);//压浆孔位置
                    temp.put("pqkwz",null);//排气（水）孔位置
                    temp.put("yjzdyl",null);//压浆最大压力
                    temp.put("ycqk",trYajiangS.getMjqk());
                    kongdaoDatas.add(temp);
                }
                saveDTOList.set("kongdaoDatas", kongdaoDatas);
//                System.out.println(saveDTOList);
                Map map = new HashMap();
                map.put("extSystemId","bf43b3ed-b108-4146-b813-00197dc84cd0");
                map.put("DocSystemCode","NXDQLJX");
//                map.put("projCode","NXDQLJX");
                map.put("formData",saveDTOList);
                String post = HttpRequest.post("http://111.3.69.250:19030/doc-as//api/ext/upload-zhangla-datas")
                        .header("Content-Type", "multipart/form-data")
                        .header("token", token)
                        .form(map)
                        .execute()
                        .body();
                JSONObject postObject = new JSONObject(post);
                Integer code = (Integer) postObject.get("code");
                if (code == 200) {
                    log.info(String.format("苏台压浆数据推送成功!" + id));
                    sysConfigService.updateSysConfig(JobUtil.ST_YAJIANG, id);//根据传过来的唯一值来修改当前判断到的数据id
                } else {
                    log.info(String.format("苏台压浆数据推送失败!" + id));
                }
            }
        }
    }
}
