package org.jeecg.modules.job.kezhujob;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.pushandreturn.service.IPushandreturnService;
import com.trtm.iot.swbhz.entity.BhzSwBases;
import com.trtm.iot.swbhz.entity.BhzSwCailiao;
import com.trtm.iot.swbhz.service.IBhzSwBasesService;
import com.trtm.iot.swbhz.service.IBhzSwCailiaoService;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import com.trtm.iot.system.entity.ShebeiInfo;
import com.trtm.iot.system.service.IShebeiInfoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.jeecg.modules.system.entity.SysDict;
import org.jeecg.modules.system.entity.SysDictItem;
import org.jeecg.modules.system.service.ISysDictItemService;
import org.jeecg.modules.system.service.ISysDictService;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class swbhzJob  implements Job {
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private IPushandreturnService pushandreturnService;
    @Autowired
    private IBhzSwBasesService bhzSwBasesService;
    @Autowired
    private IBhzSwCailiaoService swCailiaoService;
    @Autowired
    private IShebeiInfoService shebeiInfoService;
    @Autowired
    private ISysDictService sysDictService;
    @Autowired
    private ISysDictItemService sysDictItemService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.KZ_SWBHZ);//瑞苍拌合站
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info(String.format("未获取到瑞苍砼拌合站定时任务的配置信息" + DateUtils.now()));
            return;
        }
        //当前数据检测到的目标id
        Integer curid = selectsysconfigone.getCurid();
        String extra = selectsysconfigone.getExtra();
        JSONObject jsonObject = JSONUtil.parseObj(extra);
        if (null == jsonObject || jsonObject.isEmpty()) {
            log.info(String.format("没有配置要传输瑞苍砼拌合站的设备" + DateUtils.now()));
            return;
        }
        String shebeilist = jsonObject.getStr("shebeilist");
        List<BhzSwBases> bhzSwBasesList = bhzSwBasesService.selectkzlist(shebeilist,curid);
        if (null == bhzSwBasesList || bhzSwBasesList.isEmpty()) {
            log.info(String.format("瑞苍砼拌合站没有数据" + DateUtils.now()));
            return;
        }
        int id = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        for (BhzSwBases bhzSwBases : bhzSwBasesList) {
            id = bhzSwBases.getId();
            JSONObject sendObject = new JSONObject();
            String guid = bhzSwBases.getGuid();

            String wsKid = "i9434AMzTfi4nh99qUvJGw";
            if (bhzSwBases.getShebeibianhao().equals("kz1bsw")) {
                wsKid = "na_qqMfpQ-yAdda_ZQE2xA";
            }
            //ProduceDate	DATETIME	生产时间
            sendObject.put("dbName", "zj_kzPallasIOT");
            sendObject.put("kid", bhzSwBases.getId());
            sendObject.put("topProjId", "81500");
            sendObject.put("projId", "81501");
            sendObject.put("wsKid", wsKid);
            sendObject.put("ToneName", bhzSwBases.getFormulaNo()); //ToneName	VARCHAR(100)	配比名称
            sendObject.put("ProduceDate", bhzSwBases.getChuliaoshijian());
            //sendObject.put("bTime", bhzSwBases.getbTime());//bTime	DATETIME	开始时间
            //sendObject.put("eTime", bhzSwBases.geteTime());//eTime	DATETIME	结束时间
            //sendObject.put("design", );//design	DECIMAL(18,4)	配方总量
            sendObject.put("actual", bhzSwBases.getZongchanliang());//actual	DECIMAL(18,4)	生产总量
            sendObject.put("queueKid", "");//queueKid	Varchar(22)	派车单Kid（没有就传空值）

            String body = HttpRequest.post("http://www.weepal.cn/cpmshelper/117/water/uploadWaterProduced")
                    .form(sendObject)
                    .execute()
                    .body();

            pushandreturnService.saveData(id, sendObject.toString(), selectsysconfigone.getRemark(), body);
            bhzSwBases.setIszlpz(1);
            bhzSwBasesService.updateById(bhzSwBases);

            LambdaQueryWrapper<BhzSwCailiao> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(BhzSwCailiao::getBaseGuid, guid);
            List<BhzSwCailiao> swCailiaoList = swCailiaoService.list(queryWrapper);
            if (null != swCailiaoList && !swCailiaoList.isEmpty()) {
                for (BhzSwCailiao swCailiao : swCailiaoList) {
                    //替换
                    String llphb = swCailiao.getLilunpb().replace("外掺", "");

                    Double lilunpb = Double.valueOf(llphb);
                    JSONObject sendObject1 = new JSONObject();
                    sendObject1.put("dbName", "zj_kzPallasIOT");
                    sendObject1.put("prodKid", bhzSwBases.getId());
                    sendObject1.put("Material", swCailiao.getCailiaoming());

                    String getcailiaoming = getcailiaoming(swCailiao.getCailiaoleixing());
                    sendObject1.put("wsMaterial", getcailiaoming);//Material	String	物料名称
                    //lilunpb * 0.01 * zongzhongliang  保留四位小数
                    Double aDouble = null;
                    try {
                        aDouble = Double.valueOf(new DecimalFormat("0.0000").format(lilunpb * 0.01 * bhzSwBases.getZongchanliang()));
                    } catch (NumberFormatException e) {
                        bhzSwBases.setIszlpz(3);
                        bhzSwBasesService.updateById(bhzSwBases);
                        continue;
                    }
                    sendObject1.put("design", aDouble);
                    sendObject1.put("actual", swCailiao.getShijiyongliang());

                    String bodyxx = HttpRequest.post("http://www.weepal.cn/cpmshelper/117/water/uploadWaterProdDetail")
                            .form(sendObject1)
                            .execute()
                            .body();
                    bhzSwBases.setIszlpz(2);
                    bhzSwBasesService.updateById(bhzSwBases);
                    pushandreturnService.saveData(id, sendObject1.toString(), selectsysconfigone.getRemark(), bodyxx);
                }
            }
        }
    }

    private String getcailiaoming(int cailiaoleixing) {

        QueryWrapper<SysDict> sysDictQueryWrapper = new QueryWrapper<>();
        sysDictQueryWrapper.eq("dict_code", "nodeType");
        SysDict one = sysDictService.getOne(sysDictQueryWrapper);
        String id1 = one.getId();

        QueryWrapper<SysDictItem> sysDictItemQueryWrapper = new QueryWrapper<>();
        sysDictItemQueryWrapper.eq("dict_id", id1);
        sysDictItemQueryWrapper.eq("item_value", cailiaoleixing);
        SysDictItem one1 = sysDictItemService.getOne(sysDictItemQueryWrapper);
        return one1.getItemText();
    }
}
