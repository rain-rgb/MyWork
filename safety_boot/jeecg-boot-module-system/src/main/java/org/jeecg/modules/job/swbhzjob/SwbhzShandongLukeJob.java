package org.jeecg.modules.job.swbhzjob;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.trtm.iot.bhzcfg.service.IBhzCallCfgService;
import com.trtm.iot.bhzcfg.service.IBhzChaobiaoCfgService;
import com.trtm.iot.hntbhz.entity.BhzCementBase;
import com.trtm.iot.hntbhz.entity.BhzCementDetail;
import com.trtm.iot.hntbhz.service.IBhzCementBaseService;
import com.trtm.iot.hntbhz.service.IBhzCementDetailService;
import com.trtm.iot.swbhz.entity.BhzSwBases;
import com.trtm.iot.swbhz.entity.BhzSwCailiao;
import com.trtm.iot.swbhz.service.IBhzSwBasesService;
import com.trtm.iot.swbhz.service.IBhzSwCailiaoService;
import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import com.trtm.iot.system.entity.ShebeiInfo;
import com.trtm.iot.system.service.IShebeiInfoService;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.DateUtils;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.jeecg.modules.job.jobutil.ShandongUtil;
import org.jeecg.modules.message.service.ISysMessageService;
import org.jeecg.modules.system.entity.SysDepart;
import org.jeecg.modules.system.service.ISysDepartService;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Timestamp;
import java.util.List;

/**
 * \* @author: Xx
 * \* Date: 2021/6/4
 * \* Time: 18:05
 * \* Description:山东陆科推送水稳拌合站实时数据(实时数据包含所有的合格不合格数据 注：推送的数据已经判断过超不超标)
 * \
 */
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class SwbhzShandongLukeJob implements Job {
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private IBhzCementBaseService bhzCementBaseService;
    @Autowired
    private ISysMessageService sysMessageService;//消息
    @Autowired
    private IBhzSwBasesService bhzSwBasesService;
    @Autowired
    private IBhzCallCfgService bhzCallCfgService;
    @Autowired
    private IBhzChaobiaoCfgService bhzChaobiaoCfgService;
    @Autowired
    private IShebeiInfoService shebeiInfoService;
    @Autowired
    private IBhzSwCailiaoService bhzSwCailiaoService;
    @Autowired
    private IBhzCementDetailService bhzCementDetailService;
    @Autowired
    private JobUtil jobUtil;
    @Autowired
    private ShandongUtil shandongUtil;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private ISysDepartService sysDepartService;
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

        //根据定时任务配置表获取一条配置信息
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.SWBHZ_TUISONG_SHANDONGLUKE);//水稳拌合站数据推送山东陆科
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info(String.format("未获取到水稳拌合站推送山东陆科定时任务的配置信息" + DateUtils.now()));
            return;
        }
        //当前数据检测到的目标id
        Integer curid = selectsysconfigone.getCurid();
        String extra = selectsysconfigone.getExtra();
        JSONObject jsonObject = JSONUtil.parseObj(extra);
        if (null == jsonObject || jsonObject.isEmpty()) {
            log.info(String.format("没有配置要传输水稳拌合站的设备" + DateUtils.now()));
            return;
        }
        String shebeilist = jsonObject.getStr("shebeilist");
        String[] split = shebeilist.split(",");
        String sbjnolist=null;
        for (int i=0;i<split.length;i++) {
            String shebeiNo = split[i];
            ShebeiInfo selectshebeione = shebeiInfoService.selectshebeione(shebeiNo);//设备信息
            Integer status1 = selectshebeione.getStatus1();
            if(status1==3){//判断当前设备是否发送了项目信息 拌合站信息 以及设备信息
                if(sbjnolist==null){
                    sbjnolist=selectshebeione.getSbjno();
                }else{
                    sbjnolist=sbjnolist+","+selectshebeione.getSbjno();
                }
            }
        }
        if (StrUtil.isBlank(sbjnolist)) {
            log.info(String.format("没有配置要传输水稳拌合站的设备" + DateUtils.now()));
            return;
        }
        List<BhzSwBases> selecthntbhzones = bhzSwBasesService.selectswbhzlist(curid, 1, sbjnolist);
        if (null == selecthntbhzones || selecthntbhzones.size() == 0) {
            log.info(String.format("暂无水稳拌合站未判断的数据" + DateUtils.now()));
            return;
        }
        int id=0;
        String s = shandongUtil.GetTokenpost(ShandongUtil.GetToken, ShandongUtil.MORENToken);
        String token=null;
        if(s.equals("200")){
            //重新刷新获取的token
            token = (String) redisUtil.get("ShanDongLuKeToken");
        }else{
            //未刷新之前的token
            token=ShandongUtil.MORENToken;
        }
        JSONArray publicWaterList=new JSONArray();//传的参数的格式为[{},{}]
        for (BhzSwBases selecthntbhzone : selecthntbhzones) {
            id=selecthntbhzone.getId();
            String shebeiNo = selecthntbhzone.getShebeibianhao();
            ShebeiInfo selectshebeione = shebeiInfoService.selectshebeione(shebeiNo);
            SysDepart queryone = sysDepartService.queryone(selectshebeione.getSysOrgCode());//水稳拌合站信息
            String procode = selectshebeione.getProcode();// 项目编码
            JSONObject publicMixingStationList1 =new JSONObject();
            int chuliaotimes = (int) ((Timestamp.valueOf(selecthntbhzone.getChuliaoshijian()).getTime())/1000);
            int savetimes = (int) ((Timestamp.valueOf(selecthntbhzone.getBaocunshijian()).getTime())/1000);
            int caijitimes = (int) ((selecthntbhzone.getCaijishijian().getTime())/1000);
            publicMixingStationList1.putOpt("id",selecthntbhzone.getId());
            publicMixingStationList1.putOpt("deviceNumber",shebeiNo);//设备编号
            publicMixingStationList1.putOpt("proCode",procode);//    项目编码
            publicMixingStationList1.putOpt("realityCount",selecthntbhzone.getZongchanliang());//	实际方量
            publicMixingStationList1.putOpt("jobNumber",selecthntbhzone.getClientNo());//	工单号
            publicMixingStationList1.putOpt("handlers",selecthntbhzone.getYonghu());//	操作者
            publicMixingStationList1.putOpt("strengthGrade",selecthntbhzone.getStrengthRank());//	强度等级
            publicMixingStationList1.putOpt("outgoingDate",chuliaotimes);//    出料时间
            publicMixingStationList1.putOpt("acquisitionDate",savetimes);//    保存时间
            publicMixingStationList1.putOpt("mixingName",selectshebeione.getSbname());//    拌合机名称
            publicMixingStationList1.putOpt("mixingStationName",queryone.getDepartName());//    拌合站名称
            publicMixingStationList1.putOpt("mixingStationId",queryone.getId());//    拌合站ID
            publicMixingStationList1.putOpt("type",2);//    拌合站类型
            publicMixingStationList1.putOpt("collectDate",caijitimes);//    采集时间
            if(selecthntbhzone.getChaobiaodengji()>0){
                publicMixingStationList1.putOpt("isStandard",1);//    是否超标
            }else{
                publicMixingStationList1.putOpt("isStandard",0);//    是否超标
            }
            publicMixingStationList1.putOpt("standardType",selecthntbhzone.getChaobiaodengji());//    超标级别
            String batchNo = selecthntbhzone.getGuid();
            List<BhzSwCailiao> selectswbhzcailiao = bhzSwCailiaoService.selectswbhzcailiao(batchNo);
            if(selectswbhzcailiao.size()>0){
                int [] datelist={0,0,0,0,0,0,0,0,0,0};
                for (BhzSwCailiao bhzCementDetail : selectswbhzcailiao) {

                    if(bhzCementDetail.getCailiaoleixing()==1||bhzCementDetail.getCailiaoleixing()==2||
                            bhzCementDetail.getCailiaoleixing()==3||bhzCementDetail.getCailiaoleixing()==4){//实际骨料1-实际骨料5 理论骨料1-理论骨料5
                        if(datelist[2]==0){
                            publicMixingStationList1.putOpt("factAggregateOne",bhzCementDetail.getShijiyongliang());//实际骨料1
                            publicMixingStationList1.putOpt("theoryAggregateOne",bhzCementDetail.getTheoryNumber());//理论骨料1
                            datelist[2]=1;
                        }else if(datelist[2]==1){
                            publicMixingStationList1.putOpt("factAggregateTwo",bhzCementDetail.getShijiyongliang());//    实际骨料2
                            publicMixingStationList1.putOpt("theoryAggregateTwo",bhzCementDetail.getTheoryNumber());//理论骨料2
                            datelist[2]=2;
                        }else if(datelist[2]==2){
                            publicMixingStationList1.putOpt("factAggregateThree",bhzCementDetail.getShijiyongliang());//    实际骨料3
                            publicMixingStationList1.putOpt("theoryAggregateThree",bhzCementDetail.getTheoryNumber());//理论骨料3
                            datelist[2]=3;
                        }else if(datelist[2]==3){
                            publicMixingStationList1.putOpt("factAggregateFour",bhzCementDetail.getShijiyongliang());//    实际骨料4
                            publicMixingStationList1.putOpt("theoryAggregateFour",bhzCementDetail.getTheoryNumber());//理论骨料4
                            datelist[2]=4;
                        }else if(datelist[2]==4){
                            publicMixingStationList1.putOpt("factAggregateFive",bhzCementDetail.getShijiyongliang());//    实际骨料5
                            publicMixingStationList1.putOpt("theoryAggregateFive",bhzCementDetail.getTheoryNumber());//理论骨料5
                            datelist[2]=5;
                        }
                    }
                    if(bhzCementDetail.getCailiaoleixing()==5){//实际水/理论水
                        if(datelist[4]==0){
                            publicMixingStationList1.putOpt("factWater",bhzCementDetail.getShijiyongliang());//实际水
                            publicMixingStationList1.putOpt("theoryWater",bhzCementDetail.getTheoryNumber());//理论水
                            datelist[4]=1;
                        }
                    }
                    if(bhzCementDetail.getCailiaoleixing()==8){//实际粉料1/理论粉料1/实际粉料2/理论粉料2
                        if(datelist[7]==0){
                            publicMixingStationList1.putOpt("factPowderOne",bhzCementDetail.getShijiyongliang());//实际粉料1
                            publicMixingStationList1.putOpt("theoryPowderOne",bhzCementDetail.getTheoryNumber());//理论粉料1
                            datelist[7]=1;
                        }else if(datelist[7]==1){
                            publicMixingStationList1.putOpt("factPowderTwo",bhzCementDetail.getShijiyongliang());//实际粉料2
                            publicMixingStationList1.putOpt("theoryPowderTwo",bhzCementDetail.getTheoryNumber());//理论粉料2
                            datelist[7]=2;
                        }
                    }
                }
            }
            publicWaterList.add(publicMixingStationList1);
        }
        if(publicWaterList.size()>0){
            String s1 = shandongUtil.TuisongList(ShandongUtil.TuiSong, token, ShandongUtil.publicWaterList, publicWaterList.toString());
            if(s1.equals("200")){
                sysConfigService.updateSysConfig(JobUtil.SWBHZ_TUISONG_SHANDONGLUKE, id);//根据传过来的唯一值来修改当前判断到的数据id
                //此处修改sys_config 中记录推送到那个id
                log.info(String.format("推送水稳拌合站数据成功" + id+DateUtils.now()));
            }else{
                log.info(String.format("推送水稳拌合站数据失败" + id+DateUtils.now()));
            }
        }

    }
}
