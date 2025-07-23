package org.jeecg.modules.job.ydJob.dongjiao;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.trtm.iot.lqbhz.entity.BhzLqBases;
import com.trtm.iot.lqbhz.entity.BhzLqCailiao;
import com.trtm.iot.lqbhz.service.IBhzLqBasesService;
import com.trtm.iot.lqbhz.service.IBhzLqCailiaoService;
import com.trtm.iot.pushandreturn.service.IPushandreturnService;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import freemarker.core.ParseException;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.jeecg.modules.system.entity.SysDict;
import org.jeecg.modules.system.entity.SysDictItem;
import org.jeecg.modules.system.service.ISysDictItemService;
import org.jeecg.modules.system.service.ISysDictService;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @ClassName LqBhzJob：
 * @Description 浙高建东交
 * @Author 55314
 * @Date 2023/4/13 9:09
 * @Version 1.0
 **/
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class LqBhzJob implements Job {
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private IBhzLqBasesService lqBasesService;
    @Autowired
    private IBhzLqCailiaoService lqCailiaoService;
    @Autowired
    private IPushandreturnService pushandreturnService;
    @Autowired
    private ISysDictService sysDictService;
    @Autowired
    private ISysDictItemService sysDictItemService;

    @SneakyThrows
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.DJ_LQ);//沥青拌合站
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info(String.format("未获取到义东沥青拌合站定时任务的配置信息" + DateUtils.now()));
            return;
        }
        //当前数据检测到的目标id
        Integer curid = selectsysconfigone.getCurid();
        String extra = selectsysconfigone.getExtra();
        JSONObject jsonObject = JSONUtil.parseObj(extra);
        if (null == jsonObject || jsonObject.isEmpty()) {
            log.info(String.format("没有配置要传输义东沥青拌合站的设备" + DateUtils.now()));
            return;
        }
        int id = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // 时间格式化器
        String shebeilist = jsonObject.getStr("shebeilist");
        List<BhzLqBases> bhzLqBases = lqBasesService.selectListtoDJ(shebeilist, curid);
        if (null == bhzLqBases || bhzLqBases.size() == 0) {
            log.info(String.format("暂无义东沥青拌合站未推送数据" + DateUtils.now()));
            return;
        }
        for (BhzLqBases bhzLqBase : bhzLqBases) {

            id = bhzLqBase.getId();
            JSONObject sendDate = new JSONObject();
            sendDate.set("id", bhzLqBase.getId());//主键(不能重复)
            sendDate.set("mp_num", bhzLqBase.getShebeibianhao());//拌合站编号
            sendDate.set("col_time", sdf.format(bhzLqBase.getCaijishijian()));//采集时间

            String chuliaoshijian = bhzLqBase.getChuliaoshijian(); // 获取时间字符串
            Date date = sdf.parse(chuliaoshijian);
            long timestamp = date.getTime() / 1000; // 转换成秒
            sendDate.set("remark_1", timestamp);//出料时间（时间戳）
            sendDate.set("is_p", 1);//理论配合比传送方式
            String formulaNo = bhzLqBase.getFormulaNo();
            QueryWrapper<SysDict> sysDictQueryWrapper = new QueryWrapper<>();
            sysDictQueryWrapper.eq("dict_code", "hhllx");
            SysDict one = sysDictService.getOne(sysDictQueryWrapper);
            String SysDictId = one.getId();
            QueryWrapper<SysDictItem> sysDictItemQueryWrapper = new QueryWrapper<>();
            sysDictItemQueryWrapper.eq("dict_id",SysDictId);
            sysDictItemQueryWrapper.eq("item_value",formulaNo);
            SysDictItem one1 = sysDictItemService.getOne(sysDictItemQueryWrapper);
            String itemText = one1.getItemText();

            sendDate.set("formula", itemText);//配方号
            sendDate.set("discharge_time", chuliaoshijian);//出料时间
            sendDate.set("stir_time", bhzLqBase.getBanheshijian());//拌合时间分钟数
            sendDate.set("lq_tem", bhzLqBase.getLiqingwd());//沥青温度
            sendDate.set("stone_tem", bhzLqBase.getGuliaowd());//石料温度
            int chuliaowd = Integer.parseInt(bhzLqBase.getChuliaowd());
            sendDate.set("discharge_tem", String.valueOf(chuliaowd));//出料温度
            sendDate.set("production", bhzLqBase.getZongchanliang());//总重量
            sendDate.set("ar", bhzLqBase.getYoushibi());//实际油石比

            //材料表
            QueryWrapper<BhzLqCailiao> lqCailiaoQueryWrapper = new QueryWrapper<>();
            lqCailiaoQueryWrapper.eq("base_guid", bhzLqBase.getGuid());
            List<BhzLqCailiao> lqCailiaoList = lqCailiaoService.list(lqCailiaoQueryWrapper);
            for (BhzLqCailiao bhzLqCailiao : lqCailiaoList) {
                String cailiaoming = bhzLqCailiao.getCailiaoming();
                if ("石料1".equals(cailiaoming)) {
                    sendDate.set("stone_1", bhzLqCailiao.getShijiyongliang());//实际石料1质量
                    sendDate.set("th_stone_1", bhzLqCailiao.getTheoryNumber());//理论石料1
                }
                if ("石料2".equals(cailiaoming)) {
                    sendDate.set("stone_2", bhzLqCailiao.getShijiyongliang());//实际石料2质量
                    sendDate.set("th_stone_2", bhzLqCailiao.getTheoryNumber());//理论石料2
                }
                if ("石料3".equals(cailiaoming)) {
                    sendDate.set("stone_3", bhzLqCailiao.getShijiyongliang());//实际石料3质量
                    sendDate.set("th_stone_3", bhzLqCailiao.getTheoryNumber());//理论石料3
                }
                if ("石料4".equals(cailiaoming)) {
                    sendDate.set("stone_4", bhzLqCailiao.getShijiyongliang());//实际石料4质量
                    sendDate.set("th_stone_4", bhzLqCailiao.getTheoryNumber());//理论石料4
                }
                if ("石料5".equals(cailiaoming)) {
                    sendDate.set("stone_5", bhzLqCailiao.getShijiyongliang());//实际石料5质量
                    sendDate.set("th_stone_5", bhzLqCailiao.getTheoryNumber());//理论石料5
                }
                if ("石料6".equals(cailiaoming)) {
                    sendDate.set("stone_6", bhzLqCailiao.getShijiyongliang());//实际石料6质量
                    sendDate.set("th_stone_6", bhzLqCailiao.getTheoryNumber());//理论石料6
                }
                if ("石料7".equals(cailiaoming)) {
                    sendDate.set("stone_7", bhzLqCailiao.getShijiyongliang());//实际石料7质量
                    sendDate.set("th_stone_7", bhzLqCailiao.getTheoryNumber());//理论石料7
                }
                if ("粉料1".equals(cailiaoming)) {
                    sendDate.set("ore_1", bhzLqCailiao.getShijiyongliang());//实际粉料1质量
                    sendDate.set("th_ore_1", bhzLqCailiao.getTheoryNumber());//理论粉料1
                }
                if ("粉料2".equals(cailiaoming)) {
                    sendDate.set("ore_2", bhzLqCailiao.getShijiyongliang());//实际粉料2质量
                    sendDate.set("th_ore_2", bhzLqCailiao.getTheoryNumber());//理论粉料2
                }
                if ("添加剂".equals(cailiaoming)) {
                    sendDate.set("additive", bhzLqCailiao.getShijiyongliang());//实际添加剂质量
                    sendDate.set("th_additive", bhzLqCailiao.getTheoryNumber());//理论添加剂
                }
                if ("沥青".equals(cailiaoming)) {
                    sendDate.set("lq", bhzLqCailiao.getShijiyongliang());//实际沥青质量
                    sendDate.set("th_lq", bhzLqCailiao.getTheoryNumber());//理论沥青
                }
            }
            sendDate.set("th_ar", bhzLqBase.getLlysb());//理论油石比
            sendDate.set("client_name", bhzLqBase.getYonghu());//客户名称
            sendDate.set("project_name", bhzLqBase.getProjectName());//工程名称
            sendDate.set("chlient_part", bhzLqBase.getPoureLocation());//施工部位
            sendDate.set("baocunshijian", bhzLqBase.getBaocunshijian());//保存时间
            sendDate.set("surfaceCourse", itemText);//面层

            String body = HttpRequest.post("http://123.60.37.16:443/receive/hcdata/dtu")
                    .form("data", sendDate)
                    .execute()
                    .body();

            if (body.contains("success")) {
                log.info(String.format("义东拌合站推送成功!" + id + body));
            } else {
                log.info(String.format("义东拌合站推送失败!" + id + "Json数据" + sendDate));
            }
            sysConfigService.updateSysConfig(JobUtil.DJ_LQ, id);//根据传过来的唯一值来修改当前判断到的数据id
            pushandreturnService.saveData(id, String.valueOf(sendDate), selectsysconfigone.getRemark(), body);
        }
    }
}
