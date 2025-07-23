package org.jeecg.modules.job.wztaizhangjob;

import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.trtm.iot.bhzcfg.service.IBhzCallCfgService;
import com.trtm.iot.bhzcfg.service.IBhzChaobiaoCfgService;
import com.trtm.iot.guizhouttuisong.entity.GuizhoutTuisong;
import com.trtm.iot.guizhouttuisong.service.IGuizhoutTuisongService;
import com.trtm.iot.hntbhz.entity.BhzCementBase;
import com.trtm.iot.hntbhz.entity.BhzCementDetail;
import com.trtm.iot.hntbhz.service.IBhzCementBaseService;
import com.trtm.iot.hntbhz.service.IBhzCementDetailService;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import com.trtm.iot.system.service.IShebeiInfoService;
import com.trtm.iot.wzgongyingshang.entity.Wzgongyingshang;
import com.trtm.iot.wzgongyingshang.service.IWzgongyingshangService;
import com.trtm.iot.wzliaocang.entity.Wzliaocang;
import com.trtm.iot.wzliaocang.service.IWzliaocangService;
import com.trtm.iot.wztaizhang.entity.Wztaizhang;
import com.trtm.iot.wztaizhang.service.IWztaizhangService;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.DateUtils;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.jeecg.modules.job.jobutil.ShandongUtil;
import org.jeecg.modules.message.service.ISysMessageService;
import org.jeecg.modules.system.service.ISysDepartService;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

/**
 * \* @author:
 * \* Date: 2021/10/19
 * \* Time: 15:00
 * \* Description:义东原材料检验批数据推送(义东高速1标梁场智能地磅)
 * \
 */
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class JianyanpiYDJob implements Job {
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private IWztaizhangService wztaizhangService;
    @Autowired
    private IWzliaocangService wzliaocangService;
    @Autowired
    private IWzgongyingshangService wzgongyingshangService;
    @Autowired
    private JobUtil jobUtil;
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.WZJYP_YDTS);//物资原材料检验批数据推送义东高速
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info(String.format("未获取到物资原材料检验批数据推送义东高速配置信息" + DateUtils.now()));
            return;
        }
        //当前数据检测到的目标id
        Integer curid = selectsysconfigone.getCurid();
        String extra = selectsysconfigone.getExtra();
        JSONObject jsonObject = JSONUtil.parseObj(extra);
        if (null == jsonObject || jsonObject.isEmpty()) {
            log.info(String.format("没有配置要传输地磅的设备" + DateUtils.now()));
            return;
        }
        String shebeilist = jsonObject.getStr("shebeilist");
        String[] split = shebeilist.split(",");
        List<String> strsToList1= Arrays.asList(split);
        List<Wztaizhang> selectwzjypones = wztaizhangService.selectwzjypList(curid,strsToList1);//所有未推送的数据
        if (null == selectwzjypones || selectwzjypones.size() == 0) {
            log.info(String.format("暂无未推送的物资检验批数据" + DateUtils.now()));
            return;
        }
        int id=0;
        for (Wztaizhang wztaizhang : selectwzjypones) {
            id = wztaizhang.getId();
            JSONObject publicPitchList1 = new JSONObject();
            String shebeiNo = wztaizhang.getShebeibianhao();
            LambdaQueryWrapper<Wzliaocang> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Wzliaocang::getGuid,wztaizhang.getLcbianhao());
            Wzliaocang wzliaocang = wzliaocangService.getOne(queryWrapper);
            if (wzliaocang == null){
                Wztaizhang wztaizhang1 = new Wztaizhang();
                wztaizhang1.setId(id);
                wztaizhang1.setYidongstatus(10);
                wztaizhangService.updateById(wztaizhang1);
                log.info("暂无相匹配的料仓配置数据" + DateUtils.now());
                continue;
            }
            LambdaQueryWrapper<Wzgongyingshang> queryWrapper1 = new LambdaQueryWrapper<>();
            queryWrapper1.eq(Wzgongyingshang::getGuid,wztaizhang.getGongyingshangdanweibianma());
            Wzgongyingshang wzgongyingshang = wzgongyingshangService.getOne(queryWrapper1);
            if (wzgongyingshang == null){
                Wztaizhang wztaizhang1 = new Wztaizhang();
                wztaizhang1.setId(id);
                wztaizhang1.setYidongstatus(20);
                wztaizhangService.updateById(wztaizhang1);
                log.info("暂无相匹配的供应商单位配置数据" + DateUtils.now());
                continue;
            }
            publicPitchList1.putOpt("shebeibianhao",wztaizhang.getShebeibianhao());//设备编号
            publicPitchList1.set("taizhangid",id);//原始数据主键
            publicPitchList1.set("jinchangshijian",wztaizhang.getJinchangshijian());//进场时间
            publicPitchList1.set("lcbianhao",wztaizhang.getLcbianhao());//料仓编号
            publicPitchList1.set("lcname",wzliaocang.getName());//料仓名称
            publicPitchList1.set("cailiaono",wztaizhang.getCailiaono());//材料编号
            publicPitchList1.set("maozhongt",wztaizhang.getMaozhongt());//毛重（吨）
            publicPitchList1.set("pizhongt",wztaizhang.getPizhongt());//皮重（吨）
            publicPitchList1.set("jingzhongt",wztaizhang.getJingzhongt());//净重（吨）
            publicPitchList1.set("pici",wztaizhang.getPici());//检验批
            publicPitchList1.set("guigexinghao",wztaizhang.getGuigexinghao());//规格型号
            publicPitchList1.set("createTime",wztaizhang.getCreateTime());//创建时间
            publicPitchList1.set("jianyanstate",wztaizhang.getJianyanstate());//检验状态 0:未检验 1:合格 2:不合格
            publicPitchList1.set("gongyingshangdanweibianma",wzgongyingshang.getGongyingshangname());//供应商单位
            publicPitchList1.set("isfinish",wztaizhang.getIsfinish());//形成检验批状态
            publicPitchList1.set("isquyang",wztaizhang.getIsquyang());//是否已取样
            publicPitchList1.set("baogaofile",wztaizhang.getBaogaofile());//报告附件
            Integer integer = jobUtil.GETYidongWzjyp(String.valueOf(publicPitchList1));
            Wztaizhang wztaizhang1 = new Wztaizhang();
            wztaizhang1.setId(id);
            if(integer==200){
                wztaizhang1.setYidongstatus(1);
                log.info(String.format("物资检验批数据推义东高速送成功!"));
            }else{
                wztaizhang1.setYidongstatus(2);
                log.info(String.format("物资检验批数据推义东高速送失败!"));
            }
            wztaizhangService.updateById(wztaizhang1);
        }
        sysConfigService.updateSysConfig(JobUtil.WZJYP_YDTS, id);//根据传过来的唯一值来修改当前判断到的数据id
    }
}
