package org.jeecg.modules.job.weiyanJob;

import cn.hutool.core.date.DateBetween;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.trtm.iot.sysconfig.entity.SysConfig;
import com.trtm.iot.sysconfig.service.ISysConfigService;
import com.trtm.iot.weiyan.entity.WeiyanBase;
import com.trtm.iot.weiyan.mapper.WeiyanBaseMapper;
import com.trtm.iot.weiyan.service.IWeiyanBaseService;
import com.trtm.iot.weiyan.vo.IotWallRock;
import com.trtm.iot.weiyan.vo.IotWallRockTemporary;
import com.trtm.iot.weiyan.vo.IotWeiYanNormCfg;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.job.jobutil.JobUtil;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.NumberFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class weiyanJob implements Job {
    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private IWeiyanBaseService weiyanBaseService;
    @Autowired
    private WeiyanBaseMapper weiyanBaseMapper;
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        SysConfig selectsysconfigone = sysConfigService.selectsysconfigone(JobUtil.YIDONG_SUIDAOWEIYAN);//义东隧道围岩数据
        //如果他等于空
        if (null == selectsysconfigone) {
            log.info(String.format("未获取到创建围岩量测定时任务的配置信息" + DateUtils.now()));
            return;
        }

        IotWallRock base = new IotWallRock();
        log.warn("隧道围岩超标统计启动");
        base.setAlertstate(0);
        base.setOrder(0);
       // PageHelper.startPage( 1,1);
        List<IotWallRock> iotWallRocks1 = weiyanBaseMapper.selectWallRockOne(base);
        if (iotWallRocks1.size()==0){  log.warn("隧道围岩超超标统计任务1：暂无数据需要处理");return;}
        for(IotWallRock iotWallRocks:   iotWallRocks1 ){
            if (iotWallRocks == null){  log.warn("隧道围岩超超标统计任务2：暂无数据需要处理");return;}
            String measuringStatus = iotWallRocks.getMeasuringStatus();

//如果是重新布设就把变形量与累计变形量都为0，
            if(iotWallRocks.getMeasuringStatus().equals("1")){
                isMeasuring(iotWallRocks);

            }else{

                countOnceDeformation(iotWallRocks);
                Sta(iotWallRocks);

            }


           /*   String s = PointType(iotWallRocks.getMeasuringPoint());
               iotWallRocks.setMeasuringPoint(s);*/

            iotWallRocks.setAlertstate(1);
            weiyanBaseMapper.updateById(iotWallRocks);

            try {   } catch (Exception e) {
                System.out.println("隧道围岩超标判定出现异常，异常详情："+e.getMessage());
                iotWallRocks.setAlertstate(40);
                weiyanBaseMapper.updateById(iotWallRocks);

            }

        }
        }
    //如果是重新布设就把变形量与累计变形量为上一条数据的累计变形量
    public void  isMeasuring(IotWallRock iotWallRock){

        Double  AccumulatedDeformation=0.0;//累计变形量
        IotWallRock base  = new IotWallRock();
        base.setLastOrNextData(0);//  0：为临近时间的一条数据 1：为最早的一条数据
        base.setSectionName(iotWallRock.getSectionName());//
        base.setJobLocation(iotWallRock.getJobLocation());//
        base.setMeasuringPoint(iotWallRock.getMeasuringPoint());
        base.setBirthTime(iotWallRock.getBirthTime());
        base.setOrder(1); // 降序
        //PageHelper.startPage( 1,1);
        List<IotWallRock> iotWallRocks1 = weiyanBaseMapper.selectWallRockOne(base);
        IotWallRock iotWallRocks = iotWallRocks1.size()>0?iotWallRocks1.get(0):null;//判定数据的前一条数据
        if (iotWallRocks != null) {
            AccumulatedDeformation=iotWallRocks.getAccumulatedDeformation();
        }
        //计算间隔天数，并计算单次速率iotWallRock.getBirthTime(),
        Double between = 0.00;
        if(iotWallRocks!=null) {
            if (iotWallRock != null && iotWallRock.getBirthTime() != null && iotWallRocks.getBirthTime() != null) {
                between = (double)(DateBetween.create(DateUtil.parseDateTime(DateUtil.formatDateTime(iotWallRock.getBirthTime()))
                        , DateUtil.parseDateTime(DateUtil.formatDateTime(iotWallRocks.getBirthTime())),false).between(DateUnit.HOUR))/24;
            }
            iotWallRock.setIntervalTime((double)Math.round(between*100)/100);// 间隔时间
        }
        iotWallRock.setOnceDeformation(0.0);// 单次变形量（单位mm）
        iotWallRock.setDeformationRate(0.0); // 变形速率（单次变形量除以时间（天））
        iotWallRock.setAccumulatedDeformation(AccumulatedDeformation); //累计变形量
        iotWallRock.setOnceStatus(0);// 单次是否超标
        iotWallRock.setAccumulatedStatus(0); // 累计是否超标
        iotWallRock.setCallLevel(0);//  报警级别(0,代表合格，1代表黄色报警，21代表红色报警)
        iotWallRock.setSuperviseType(0); // 检测类型 速率
        iotWallRock.setCallSum(0); //
    }

    /*
   计算出当前数据的单次变形与累计变形量
    */
    private void countOnceDeformation(IotWallRock iotWallRocks) {
        Double oldMeasuringNumber = iotWallRocks.getMeasuringNumber();

        Double  AccumulatedDeformation=0.0;//累计变形量
        Map<String, Object> a = new HashMap<String, Object>();
        IotWallRock base  = new IotWallRock();
        base.setLastOrNextData(0);
        base.setSectionName(iotWallRocks.getSectionName());//
        base.setJobLocation(iotWallRocks.getJobLocation());//
        base.setMeasuringPoint(iotWallRocks.getMeasuringPoint());
        base.setBirthTime(iotWallRocks.getBirthTime());
        base.setOrder(1);
        //PageHelper.startPage( 1,1);
        List<IotWallRock> iotWallRocks1 = weiyanBaseMapper.selectWallRockOne(base);
        IotWallRock iotWallRock = iotWallRocks1.size()>0?iotWallRocks1.get(0):null;//判定数据的前一条数据
        if (iotWallRock != null) {
            oldMeasuringNumber = iotWallRock.getMeasuringNumber();
            AccumulatedDeformation=iotWallRock.getAccumulatedDeformation();
            if (AccumulatedDeformation==null){
                AccumulatedDeformation=0.0;
            }
        }
        //单次变形量
        double v = (oldMeasuringNumber - iotWallRocks.getMeasuringNumber()) * 1000;
        String format = weiyanJob.format(v, 2).replace(",", "");
        Double OnceDeformation = Double.valueOf(format);

        //计算间隔天数，并计算单次速率iotWallRock.getBirthTime(),
        Double between = 0.00;
        if (iotWallRock != null&&iotWallRock.getBirthTime() != null && iotWallRocks.getBirthTime() != null) {
            between = (double)(DateBetween.create(DateUtil.parseDateTime(DateUtil.formatDateTime(iotWallRock.getBirthTime()))
                    , DateUtil.parseDateTime(DateUtil.formatDateTime(iotWallRocks.getBirthTime())),false).between(DateUnit.HOUR))/24;
        }
        Double DeformationRate = between == 0.00 ? OnceDeformation : OnceDeformation / (double)Math.round(between*100)/100;
        //计算累计变形量
        AccumulatedDeformation= AccumulatedDeformation+OnceDeformation;//当前累计变形量=上次累计变形量+本次变形量
        iotWallRocks.setOnceDeformation(OnceDeformation);
        iotWallRocks.setIntervalTime((double)Math.round(between*100)/100);
        iotWallRocks.setDeformationRate(Double.valueOf(weiyanJob.format(DeformationRate, 2).replace(",", "")));
        iotWallRocks.setAccumulatedDeformation(AccumulatedDeformation);
    }
    /**
     *
     * @Description
     *  四舍五入数值
     * @param d：要转化的double
     * @param places：位数
     * @return：转换后的字符串
     */
    public static String format(double d, int places) {
        NumberFormat nf = NumberFormat.getInstance();
        nf.setMinimumFractionDigits(places);
        nf.setMaximumFractionDigits(places);
        return nf.format(d);
    }
    /*
*A 开头  拱顶
S[123]周边收敛  特殊情况 XS[123] 斜测线
DB[0123456789]  地表
*
* A 拱顶沉降
S1 周边收敛
S2 地表沉降
*/
    private  String  PointType(String Type){
        String result="";

        boolean a = Type.contains("A");
        if(a){

            result="A";
            return result;
        }

        boolean s = Type.contains("S");
        if(s){

            result="S";
            return result;
        }
        boolean db = Type.contains("DB");
        if(db){

            result="DB";
            return result;
        }
        return result;
    }
    private  Integer WallRockLevel(String Level){
        int result=0;
        switch (Level){
            case    "I":   result=1;break;
            case    "II":   result=2;break;
            case    "III":   result=3;break;
            case    "IV":   result=4;break;
            case    "V":   result=5;break;
            case    "VI":   result=6;break;
        }
        return result;

    }
    //此方法负责新增修改功能，如果单次超标了，就累加原有天数，如果不超标就重置为0,
    public double WallRockTemporary(IotWallRock iotWallRock, int OneCallLevel) {
        Double resultDays = 0.00;
        IotWallRockTemporary wallRockTemporary = new IotWallRockTemporary();
        wallRockTemporary.setShebeiNo(iotWallRock.getShebeiNo());
        wallRockTemporary.setJobLocation(iotWallRock.getJobLocation());
        wallRockTemporary.setSectionName(iotWallRock.getSectionName());
        wallRockTemporary.setSectionLength(iotWallRock.getSectionLength());
        wallRockTemporary.setSectionWidth(iotWallRock.getSectionWidth());
        wallRockTemporary.setExcavationType(iotWallRock.getExcavationType());
        wallRockTemporary.setWallRockLevel(iotWallRock.getWallRockLevel());
        wallRockTemporary.setMeasuringPoint(iotWallRock.getMeasuringPoint());
        List<IotWallRockTemporary> select = weiyanBaseMapper.select2(wallRockTemporary);
        if (select.size() == 0) {
            resultDays= iotWallRock.getIntervalTime();
            if(OneCallLevel==0)resultDays=0.00 ;
            wallRockTemporary.setSuccessionDay(resultDays.intValue());
            weiyanBaseMapper.insert1(wallRockTemporary);
        }else{
            IotWallRockTemporary iotWallRockTemporary = select.get(0);
            resultDays= iotWallRock.getIntervalTime()+iotWallRockTemporary.getSuccessionDay();
            if(OneCallLevel==0)resultDays=0.00 ;
            wallRockTemporary.setSuccessionDay(resultDays.intValue());
            weiyanBaseMapper.update(wallRockTemporary);
        }
        return resultDays;
    }
    //判定报警级别，速率超标，累计超标
    private void Sta(IotWallRock iotWallRock) {
        boolean aa = false;
        int CALL_LEVEL = 0;  //报警级别，不论是速率还是累计取最大值
        //检测类型（0:速率，1累计）(这条数据是什么超标
        int ONCE_STATUS = 0;         // 单次是否超标 (0:未超标，1:超标)
        int ACCUMULATED_STATUS = 0;//累计是否超标  (0:未超标，1:超标)
        int OneCallLevel = 0; //单次超标等级
        int AccumulatedCallLevel = 0;//累计超标等级
        String PointType = PointType(iotWallRock.getMeasuringPoint());//格式化测点类型，
        String  MassageContent ="["+iotWallRock.getSectionNum()+"]隧道围岩预警：\n"+iotWallRock.getSectionNum()+"标段，数据测量时间："+DateUtil.formatDateTime(iotWallRock.getBirthTime())+",切面名称：" +iotWallRock.getSectionName()+","+iotWallRock.getWallRockLevel()+",测点/线："+iotWallRock.getMeasuringPoint()+",";

        String OneMassageContent = "";
        String OneMassageType = "";
        String AccumulatedMassageContent= "";
        String problemRecord="";
        IotWeiYanNormCfg param = new IotWeiYanNormCfg();
        String substring = iotWallRock.getWallRockLevel().substring(2, iotWallRock.getWallRockLevel().length() - 1).toUpperCase();
        Integer integer = WallRockLevel(substring);
        param.setWallRockLevel( integer);
        param.setSuperviseType(PointType);
        param.setDetectionType(1);//速率
        List<IotWeiYanNormCfg> select = weiyanBaseMapper.select(param);
        //速率超标判定,取出围岩配置，根据围岩配置上下值判断是否超标，并比较当前超标级别是否大于原有的超标级别
        Double deformationRate = iotWallRock.getDeformationRate();//变形速率
        for (IotWeiYanNormCfg a : select) {

            if (deformationRate >a.getUpNorm() ) {
                ONCE_STATUS = 1;
                if (OneCallLevel < a.getCallLevel()) {
                    OneCallLevel = a.getCallLevel();
                }

                CALL_LEVEL = CALL_LEVEL > a.getCallLevel() ? CALL_LEVEL : a.getCallLevel();
            }





        }
        if (OneCallLevel == 1){
            for (IotWeiYanNormCfg a : select) {
                if(a.getCallLevel()==1){
                    OneMassageType="变形速率超标，预警级别:黄色预警。";
                    OneMassageContent =OneMassageContent+ "变形速率为" + deformationRate + "mm/天，超标标准为:>" + a.getUpNorm() + "mm/天；";
                }
            } }
        if (OneCallLevel == 21){

            for (IotWeiYanNormCfg a : select) {
                if(a.getCallLevel()==21){
                    OneMassageType="变形速率超标，预警级别:红色预警。";
                    OneMassageContent = OneMassageContent+"变形速率为" + deformationRate + "mm/天，超标标准为" + a.getUpNorm() + "mm/天;";

                }
            }
        }



        //如果单次超标了，就累加原有天数，如果不超标就重置为0
        Double i = WallRockTemporary(iotWallRock, OneCallLevel);
        //连续两天超过黄色预警也进行高级预警判定
        iotWallRock.setCallSum(i.intValue());

        param.setCallLevel(OneCallLevel);
        List<IotWeiYanNormCfg> daySta = weiyanBaseMapper.select(param);
        if(daySta.size()>0){
            IotWeiYanNormCfg iotWeiYanNormCfg = daySta.get(0);
            if(OneCallLevel!=0&&i> iotWeiYanNormCfg.getSuccessionDay()){
                CALL_LEVEL = CALL_LEVEL >21 ? CALL_LEVEL : 21;
                OneMassageType="变形速率超标，预警级别:红色预警。";
                OneMassageContent=  OneMassageContent+"连续超标"+i+" （天），配置连续超标天数为 "+iotWeiYanNormCfg.getSuccessionDay()+"（天）";
            }
        }
        param.setCallLevel(null); param.setDetectionType(2);//累计
        List<IotWeiYanNormCfg> select2 = weiyanBaseMapper.select(param);

        Double accumulatedDeformation = iotWallRock.getAccumulatedDeformation();
        for (IotWeiYanNormCfg a : select2) {
            if (accumulatedDeformation >a.getUpNorm()) {
                ACCUMULATED_STATUS = 1;
                if (AccumulatedCallLevel < a.getCallLevel())  AccumulatedCallLevel = a.getCallLevel();
                CALL_LEVEL = CALL_LEVEL > a.getCallLevel() ? CALL_LEVEL : a.getCallLevel();
            }
        }
        if (AccumulatedCallLevel == 1)
        {  for (IotWeiYanNormCfg a : select2) {
            if(a.getCallLevel()== 1){
                AccumulatedMassageContent =AccumulatedMassageContent+ "累计变形量超标，预警级别:黄色预警。累计变形量为" + accumulatedDeformation + "mm，超标标准为" + a.getUpNorm() + "mm~" + a.getDownNorm() + "mm";
            } }   }
        if (AccumulatedCallLevel == 21)
        {  for (IotWeiYanNormCfg a : select2) {
            if(a.getCallLevel()==21){
                AccumulatedMassageContent =AccumulatedMassageContent+ "累计变形量超标，预警级别:红色预警。累计变形量为" + accumulatedDeformation + "mm，超标标准为" + a.getUpNorm() + "mm~" + a.getDownNorm() + "mm";
            } }   }

        problemRecord=  OneMassageType+OneMassageContent+AccumulatedMassageContent;
        iotWallRock.setProblemRecord(problemRecord);
        //if (CALL_LEVEL != 0) Message(iotWallRock, CALL_LEVEL, MassageContent+ OneMassageType+OneMassageContent+AccumulatedMassageContent);
        iotWallRock.setOnceStatus(ONCE_STATUS);
        iotWallRock.setAccumulatedStatus(ACCUMULATED_STATUS);
        iotWallRock.setCallLevel(CALL_LEVEL);
        //判断此条数据是什么超标，是单次超标还是累计超标，又或是两者都超标
        int a = 0;
        a = iotWallRock.getOnceStatus() == 1 ? 1 : a;
        a = iotWallRock.getAccumulatedStatus() == 1 ? 2 : a;
        a = iotWallRock.getOnceStatus() == 1 && iotWallRock.getAccumulatedStatus() == 1 ? 3 : a;
        iotWallRock.setSuperviseType(a);


    }
//    public void Message(IotWallRock iotWallRock, int CallLevels, String MassageContent) {
//       /* String PhonesUUid = "";
//        IotEquimentNormCfg cfg = new IotEquimentNormCfg();
//        cfg.setShebeiNo(iotWallRock.getShebeiNo());
//        if (CallLevels == 1) cfg.setNormType(3);
//        if (CallLevels == 21) cfg.setNormType(4);
//        List<IotEquimentNormCfg> select1 = iotEquimentNormCfgMapper.select(cfg);
//        IotEquimentNormCfg select = select1.size() > 0 ? select1.get(0) : null;
//        if (select != null) {
//        PhonesUUid = select.getPhoneUuid();
//        }
//        iotMessageRecordService.save(iotWallRock.getBatchNo(), PhonesUUid, iotWallRock.getShebeiNo(), iotWallRock.getBirthTime(), MassageContent);
//*/
//        Date birthTime = iotWallRock.getBirthTime();
//
//
//
//
//        String Phones="";
//        if(StrUtil.isNotEmpty(iotWallRock.getSectionId()) ){
//            iotPhone iotPhone = iotPhoneService.selectBytypeAndDepartId("8", iotWallRock.getSectionId());
//            Phones =Phones+iotPhone!=null&&iotPhone.getPhones()!=null?iotPhone.getPhones():"";
//        }
//        Map Messageinfo=new HashMap();
//        Messageinfo.put("batch_no",iotWallRock.getBatchNo());
//        Messageinfo.put("content",  "["+iotWallRock.getSectionNum()+"]"+MassageContent);
//        Messageinfo.put("shebei_no", iotWallRock.getSectionName());
//        Messageinfo.put("Phones", Phones);
//        iotMessageRecordService.saveForJob( Messageinfo,0,birthTime);
//    }
}
