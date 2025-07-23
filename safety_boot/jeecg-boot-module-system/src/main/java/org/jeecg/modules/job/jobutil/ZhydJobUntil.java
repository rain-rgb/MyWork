package org.jeecg.modules.job.jobutil;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.bhzStatistics.entity.BhzCementStatistics;
import com.trtm.iot.bhzcfg.entity.BhzChaobiaoCfg;
import com.trtm.iot.hntbhz.entity.BhzCementBase;
import com.trtm.iot.hntbhz.entity.BhzCementDetail;
import com.trtm.iot.system.entity.ShebeiInfo;
import com.trtm.iot.zhydcfg.entity.DeviceElectricCfg;
import com.trtm.iot.zhydcfg.service.IDeviceElectricCfgService;
import com.trtm.iot.zhydhistory.entity.DeviceElectricHistorydata;
import com.trtm.iot.zhydhistory.service.IDeviceElectricHistorydataService;
import com.trtm.iot.zhydreal.entity.DeviceElectricRealdata;
import com.trtm.iot.zhydreal.service.IDeviceElectricRealdataService;
import com.trtm.iot.zhydstatistics.entity.DeviceElectrcStatistics;
import com.trtm.iot.zhydstatistics.service.IDeviceElectrcStatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 智慧用电工具类
 */
@Component
public class ZhydJobUntil {

    @Autowired
    private IDeviceElectricCfgService deviceElectricCfgService;

    @Autowired
    private IDeviceElectricRealdataService deviceElectricRealdataService;

    @Autowired
    private IDeviceElectricHistorydataService deviceElectricHistorydataService;

    @Autowired
    private IDeviceElectrcStatisticsService deviceElectrcStatisticsService;

    private static ZhydJobUntil zhydJobUntil;

    @PostConstruct
    public void init() {
        zhydJobUntil = this;
    }


    /**
     * 判断智慧用电是否超标
     *
     * @param selectzhydone
     * @param selectshebeione
     * @param is_call
     * @return
     */

    public Map zhydOver(DeviceElectricRealdata selectzhydone,ShebeiInfo selectshebeione, int is_call) {

        Map map = new HashMap<>();

        int final_over_level = 0;
        StringBuilder final_content = new StringBuilder();
        double voltagebza = 0.0;//电压A
        double voltagebzb = 0.0;//电压B
        double voltagebzc = 0.0;//电压C
        double frequencybza = 0.0;//频率A
        double frequencybzb = 0.0;//频率B
        double frequencybzc = 0.0;//频率C
        double electricitybza = 0.0;//电流A
        double electricitybzb = 0.0;//电流B
        double electricitybzc = 0.0;//电流C
        double seepelectricitybz = 0.0;//漏电流
        double tempbza = 0.0;//温度A
        double tempbzb = 0.0;//温度B
        double tempbzc = 0.0;//温度C
        double tempbzn = 0.0;//温度N
//        double energybza = 0.0;//电能A
//        double energybzb = 0.0;//电能B
//        double energybzc = 0.0;//电能C
        String imei = selectshebeione.getSbjno();

        Double va = selectzhydone.getVoltagea();
        Double vb = selectzhydone.getVoltageb();
        Double vc = selectzhydone.getVoltagec();
        Double fa = selectzhydone.getFrequencya();
        Double fb = selectzhydone.getFrequencyb();
        Double fc = selectzhydone.getFrequencyc();
        Double ea = selectzhydone.getElectricitya();
        Double eb = selectzhydone.getElectricityb();
        Double ec = selectzhydone.getElectricityc();
        Double se = selectzhydone.getSeepelectricity();
        Double ta = selectzhydone.getTempa();
        Double tb = selectzhydone.getTempb();
        Double tc = selectzhydone.getTempc();
        Double tn = selectzhydone.getTempn();
//            Double ena = realdata.getEnergya();
//            Double enb = realdata.getEnergyb();
//            Double enc = realdata.getEnergyc();
        if (null != selectzhydone.getVoltagea()) {
            va = selectzhydone.getVoltagea();
        }
        if (null != selectzhydone.getVoltageb()) {
            vb = selectzhydone.getVoltageb();
        }
        if (null != selectzhydone.getVoltagec()) {
            vc = selectzhydone.getVoltagec();
        }
        if (null != selectzhydone.getFrequencya()) {
            fa = selectzhydone.getFrequencya();
        }
        if (null != selectzhydone.getFrequencyb()) {
            fb = selectzhydone.getFrequencyb();
        }
        if (null != selectzhydone.getFrequencyc()) {
            fc = selectzhydone.getFrequencyc();
        }
        if (null != selectzhydone.getElectricitya()) {
            ea = selectzhydone.getElectricitya();
        }
        if (null != selectzhydone.getElectricityb()) {
            eb = selectzhydone.getElectricityb();
        }
        if (null != selectzhydone.getElectricityc()) {
            ec = selectzhydone.getElectricityc();
        }
        if (null != selectzhydone.getSeepelectricity()) {
            se = selectzhydone.getSeepelectricity();
        }
        if (null != selectzhydone.getTempa()) {
            ta = selectzhydone.getTempa();
        }
        if (null != selectzhydone.getTempb()) {
            tb = selectzhydone.getTempb();
        }
        if (null != selectzhydone.getTempc()) {
            tc = selectzhydone.getTempc();
        }
        if (null != selectzhydone.getTempn()) {
            tn = selectzhydone.getTempn();
        }
//            if(null!=realdata.getEnergya()){
//                ena = realdata.getEnergya();
//            }
//            if(null!=realdata.getEnergyb()){
//                enb = realdata.getEnergyb();
//            }
//            if(null!=realdata.getEnergyc()){
//                enc = realdata.getEnergyc();
//            }
        QueryWrapper<DeviceElectricCfg> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("imei", imei);
        DeviceElectricCfg deviceElectricCfg = deviceElectricCfgService.getOne(queryWrapper);
        if (null != deviceElectricCfg) {
            voltagebza = deviceElectricCfg.getVoltagebza();
            voltagebzb = deviceElectricCfg.getVoltagebzb();
            voltagebzc = deviceElectricCfg.getVoltagebzc();
            frequencybza = deviceElectricCfg.getFrequencybza();
            frequencybzb = deviceElectricCfg.getFrequencybzb();//频率B
            frequencybzc = deviceElectricCfg.getFrequencybzc();//频率C
            electricitybza = deviceElectricCfg.getElectricitybza();//电流A
            electricitybzb = deviceElectricCfg.getElectricitybzb();//电流B
            electricitybzc = deviceElectricCfg.getElectricitybzc();//电流C
            seepelectricitybz = deviceElectricCfg.getSeepelectricitybz();//漏电流
            tempbza = deviceElectricCfg.getTempbza();//温度A
            tempbzb = deviceElectricCfg.getTempbzb();//温度B
            tempbzc = deviceElectricCfg.getTempbzc();//温度C
            tempbzn = deviceElectricCfg.getTempbzn();//温度N
        } else {
            voltagebza = 253.0;
            voltagebzb = 253.0;
            voltagebzc = 253.0;
            frequencybza = 50.0;
            frequencybzb = 50.0;//频率B
            frequencybzc = 50.0;//频率C
            electricitybza = 123.0;//电流A
            electricitybzb = 123.0;//电流B
            electricitybzc = 123.0;//电流C
            seepelectricitybz = 2000.0;//漏电流
            tempbza = 70.0;//温度A
            tempbzb = 70.0;//温度B
            tempbzc = 70.0;//温度C
            tempbzn = 70.0;//温度N
//                energybza = 0.0;//电能A
//                energybzb = 0.0;//电能B
//                energybzc = 0.0;//电能C
        }
        int status = 0;
        int vastatus = 0;
        int vbstatus = 0;
        int vcstatus = 0;
        int fastatus = 0;
        int fbstatus = 0;
        int fcstatus = 0;
        int eastatus = 0;
        int ebstatus = 0;
        int ecstatus = 0;
        int sestatus = 0;
        int tastatus = 0;
        int tbstatus = 0;
        int tcstatus = 0;
        String massage = "";
        if (va > voltagebza) {
            status = 2;
            vastatus = 2;
            massage = "电压A超标";
        } else {
            vastatus = 1;
        }
        if (vb > voltagebzb) {
            status = 2;
            vbstatus = 2;
            massage = massage+","+"电压B超标";
        } else {
            vbstatus = 1;
        }
        if (vc > voltagebzc) {
            status = 2;
            vcstatus = 2;
            massage = massage+","+"电压C超标";
        } else {
            vcstatus = 1;
        }
        if (fa < frequencybza - 1 || fa > frequencybza + 1) {
            status = 2;
            fastatus = 2;
            massage = massage+","+"频率A超标";
        } else {
            fastatus = 1;
        }
        if (fb < frequencybzb - 1 || fb > frequencybzb + 1) {
            status = 2;
            fbstatus = 2;
            massage = massage+","+"频率B超标";
        } else {
            fbstatus = 1;
        }
        if (fc > frequencybzc + 1 || fc < frequencybzc - 1) {
            status = 2;
            fcstatus = 2;
            massage = massage+","+"频率B超标";
        } else {
            fcstatus = 1;
        }
        if (ea > electricitybza) {
            status = 2;
            eastatus = 2;
            massage = massage+","+"电流A超标";
        } else {
            eastatus = 1;
        }
        if (eb > electricitybzb) {
            status = 2;
            ebstatus = 2;
            massage = massage+","+"电流B超标";
        } else {
            ebstatus = 1;
        }
        if (ec > electricitybzc) {
            status = 2;
            ecstatus = 2;
            massage = massage+","+"电流C超标";
        } else {
            ecstatus = 1;
        }
        if (se > seepelectricitybz) {
            status = 2;
            sestatus = 2;
            massage = massage+","+"漏电流超标";
        } else {
            sestatus = 1;
        }
        if (ta > tempbza) {
            status = 2;
            tastatus = 2;
            massage = massage+","+"温度A超标";
        } else {
            tastatus = 1;
        }
        if (tb > tempbzb) {
            status = 2;
            tbstatus = 2;
            massage = massage+","+"温度B超标";
        } else {
            tbstatus = 1;
        }
        if (tc > tempbzc) {
            status = 2;
            tcstatus = 2;
            massage = massage+","+"温度C超标";
        } else {
            tcstatus = 1;
        }
        if (tn > tempbzn) {
            status = 2;
            massage = massage+","+"温度N超标";
        }
//            if(ena > energybza){
//                status = 2;
//                massage = massage+"，"+"电能A超出预警值";
//            }
//            if(enb > energybzb){
//                status = 2;
//                massage = massage+"，"+"电能B超出预警值";
//            }
//            if(enc > energybzc){
//                status = 2;
//                massage = massage+"，"+"电能C超出预警值";
//            }
        final_content.append(String.format("%1$s", massage));
        final_over_level = status;
        DeviceElectricRealdata RealdataUpdate = new DeviceElectricRealdata();
        RealdataUpdate.setStatus(status);
        RealdataUpdate.setVaStatus(vastatus);
        RealdataUpdate.setVbStatus(vbstatus);
        RealdataUpdate.setVcStatus(vcstatus);
        RealdataUpdate.setFaStatus(fastatus);
        RealdataUpdate.setFbStatus(fbstatus);
        RealdataUpdate.setFcStatus(fcstatus);
        RealdataUpdate.setEaStatus(eastatus);
        RealdataUpdate.setEbStatus(ebstatus);
        RealdataUpdate.setEcStatus(ecstatus);
        RealdataUpdate.setSeStatus(sestatus);
        RealdataUpdate.setTaStatus(tastatus);
        RealdataUpdate.setTbStatus(tbstatus);
        RealdataUpdate.setTcStatus(tcstatus);
        RealdataUpdate.setId(selectzhydone.getId());
        deviceElectricRealdataService.updateById(RealdataUpdate);
        map.put("status", final_over_level);
        map.put("final_content", final_content.toString());
        return map;
    }


    /**
     * 判断智慧用电历史数据是否超标
     *
     * @param selectzhydhistoryones
     * @param
     * @param is_call
     * @return
     */

    public Map zhydHistoryOver(DeviceElectricHistorydata selectzhydhistoryones, ShebeiInfo selectshebeione, int is_call) {

        Map map = new HashMap<>();

        int final_over_level1 = 0;
        StringBuilder final_content = new StringBuilder();
        double voltagebzah = 0.0;//电压A
        double voltagebzbh = 0.0;//电压B
        double voltagebzch = 0.0;//电压C
        double frequencybzah = 0.0;//频率A
        double frequencybzbh = 0.0;//频率B
        double frequencybzch = 0.0;//频率C
        double electricitybzah = 0.0;//电流A
        double electricitybzbh = 0.0;//电流B
        double electricitybzch = 0.0;//电流C
        double seepelectricitybzh = 0.0;//漏电流
        double tempbzah = 0.0;//温度A
        double tempbzbh = 0.0;//温度B
        double tempbzch = 0.0;//温度C
        double tempbznh = 0.0;//温度N

        int status = 0;
        int vastatus = 0;
        int vbstatus = 0;
        int vcstatus = 0;
        int fastatus = 0;
        int fbstatus = 0;
        int fcstatus = 0;
        int eastatus = 0;
        int ebstatus = 0;
        int ecstatus = 0;
        int sestatus = 0;
        int tastatus = 0;
        int tbstatus = 0;
        int tcstatus = 0;

        String imei = selectshebeione.getSbjno();
        Double vah = selectzhydhistoryones.getVoltagea();
        Double vbh = selectzhydhistoryones.getVoltageb();
        Double vch = selectzhydhistoryones.getVoltagec();
        Double fah = selectzhydhistoryones.getFrequencya();
        Double fbh = selectzhydhistoryones.getFrequencyb();
        Double fch = selectzhydhistoryones.getFrequencyc();
        Double eah = selectzhydhistoryones.getElectricitya();
        Double ebh = selectzhydhistoryones.getElectricityb();
        Double ech = selectzhydhistoryones.getElectricityc();
        Double seh = selectzhydhistoryones.getSeepelectricity();
        Double tah = selectzhydhistoryones.getTempa();
        Double tbh = selectzhydhistoryones.getTempb();
        Double tch = selectzhydhistoryones.getTempc();
        Double tnh = selectzhydhistoryones.getTempn();
        DeviceElectricCfg deviceElectricCfg = new DeviceElectricCfg();
        DeviceElectricCfg selectzhydlist = deviceElectricCfgService.selectzhydcallone(imei);
        if (null != selectzhydlist) {
            deviceElectricCfg = selectzhydlist;
            voltagebzah = deviceElectricCfg.getVoltagebza();
            voltagebzbh = deviceElectricCfg.getVoltagebzb();
            voltagebzch = deviceElectricCfg.getVoltagebzc();
            frequencybzah = deviceElectricCfg.getFrequencybza();
            frequencybzbh = deviceElectricCfg.getFrequencybzb();
            frequencybzch = deviceElectricCfg.getFrequencybzc();
            electricitybzah = deviceElectricCfg.getElectricitybza();
            electricitybzbh = deviceElectricCfg.getElectricitybzb();
            electricitybzch = deviceElectricCfg.getElectricitybzc();
            seepelectricitybzh = deviceElectricCfg.getSeepelectricitybz();
            tempbzah = deviceElectricCfg.getTempbza();
            tempbzbh = deviceElectricCfg.getTempbzb();
            tempbzch = deviceElectricCfg.getTempbzc();
            tempbznh = deviceElectricCfg.getTempbzn();
        } else {
            voltagebzah = 253.0;
            voltagebzbh = 253.0;
            voltagebzch = 253.0;
            frequencybzah = 50.0;
            frequencybzbh = 50.0;//频率B
            frequencybzch = 50.0;//频率C
            electricitybzah = 123.0;//电流A
            electricitybzbh = 123.0;//电流B
            electricitybzch = 123.0;//电流C
            seepelectricitybzh = 2000.0;//漏电流
            tempbzah = 70.0;//温度A
            tempbzbh = 70.0;//温度B
            tempbzch = 70.0;//温度C
            tempbznh = 70.0;//温度N
        }

        String massage = "";
        if (vah > voltagebzah) {
            status = 2;
            vastatus = 2;
            massage = "电压A超标";
        } else {
            vastatus = 1;
        }
        if (vbh > voltagebzbh) {
            status = 2;
            vbstatus = 2;
            massage = massage+","+"电压B超标";
        } else {
            vbstatus = 1;
        }
        if (vch > voltagebzch) {
            status = 2;
            vcstatus = 2;
            massage = massage+","+"电压C超标";
        } else {
            vcstatus = 1;
        }
        if (fah < frequencybzah - 1 || fah > frequencybzah + 1) {
            status = 2;
            fastatus = 2;
            massage = massage+","+"频率A超标";
        } else {
            fastatus = 1;
        }
        if (fbh < frequencybzbh - 1 || fbh > frequencybzbh + 1) {
            status = 2;
            fbstatus = 2;
            massage = massage+","+"频率B超标";
        } else {
            fbstatus = 1;
        }
        if (fch > frequencybzch + 1 || fch < frequencybzch - 1) {
            status = 2;
            fcstatus = 2;
            massage = massage+","+"频率B超标";
        } else {
            fcstatus = 1;
        }
        if (eah > electricitybzah) {
            status = 2;
            eastatus = 2;
            massage = massage+","+"电流A超标";
        } else {
            eastatus = 1;
        }
        if (ebh > electricitybzbh) {
            status = 2;
            ebstatus = 2;
            massage = massage+","+"电流B超标";
        } else {
            ebstatus = 1;
        }
        if (ech > electricitybzch) {
            status = 2;
            ecstatus = 2;
            massage = massage+","+"电流C超标";
        } else {
            ecstatus = 1;
        }
        if (seh > seepelectricitybzh) {
            status = 2;
            sestatus = 2;
            massage = massage+","+"漏电流超标";
        } else {
            sestatus = 1;
        }
        if (tah > tempbzah) {
            status = 2;
            tastatus = 2;
            massage = massage+","+"温度A超标";
        } else {
            tastatus = 1;
        }
        if (tbh > tempbzbh) {
            status = 2;
            tbstatus = 2;
            massage = massage+","+"温度B超标";
        } else {
            tbstatus = 1;
        }
        if (tch > tempbzch) {
            status = 2;
            tcstatus = 2;
            massage = massage+","+"温度C超标";
        } else {
            tcstatus = 1;
        }
        if (tnh > tempbznh) {
            status = 2;
            massage = massage+","+"温度N超标";
        }
        final_content.append(String.format("%1$s", massage));
        final_over_level1 = status;
        DeviceElectricHistorydata HistorydataUpdate = new DeviceElectricHistorydata();
        HistorydataUpdate.setVaStatus(vastatus);
        HistorydataUpdate.setVbStatus(vbstatus);
        HistorydataUpdate.setVcStatus(vcstatus);
        HistorydataUpdate.setFaStatus(fastatus);
        HistorydataUpdate.setFbStatus(fbstatus);
        HistorydataUpdate.setFcStatus(fcstatus);
        HistorydataUpdate.setEaStatus(eastatus);
        HistorydataUpdate.setEbStatus(ebstatus);
        HistorydataUpdate.setEcStatus(ecstatus);
        HistorydataUpdate.setSeStatus(sestatus);
        HistorydataUpdate.setTaStatus(tastatus);
        HistorydataUpdate.setTbStatus(tbstatus);
        HistorydataUpdate.setTcStatus(tcstatus);
        HistorydataUpdate.setId(selectzhydhistoryones.getId());
        deviceElectricHistorydataService.updateById(HistorydataUpdate);
        if(status>1){
            Date elecdate = selectzhydhistoryones.getElecdate();
            String datanyr = NumberUtil.Stringnyr(elecdate);//格式化后的时间
            Date datanyr1 = NumberUtil.datanyr(datanyr);
            DeviceElectrcStatistics selectlimit = deviceElectrcStatisticsService.selectlimit(datanyr1, imei);
            if (selectlimit != null) {
                count(selectlimit, HistorydataUpdate);
            } else {
                DeviceElectrcStatistics deviceElectrcStatistics = new DeviceElectrcStatistics();
                deviceElectrcStatistics.setImei(imei);
                deviceElectrcStatistics.setStatisticsTime(datanyr1);
                deviceElectrcStatistics.setVoltage(0);
                deviceElectrcStatistics.setFrequency(0);
                deviceElectrcStatistics.setSeepelectricity(0);
                deviceElectrcStatistics.setTemp(0);
                deviceElectrcStatistics.setElectricity(0);
                deviceElectrcStatistics.setVoltagea(0);
                deviceElectrcStatistics.setVoltageb(0);
                deviceElectrcStatistics.setVoltagec(0);
                deviceElectrcStatistics.setFrequencya(0);
                deviceElectrcStatistics.setFrequencyb(0);
                deviceElectrcStatistics.setFrequencyc(0);
                deviceElectrcStatistics.setTempa(0);
                deviceElectrcStatistics.setTempb(0);
                deviceElectrcStatistics.setTempc(0);
                deviceElectrcStatistics.setElectricitya(0);
                deviceElectrcStatistics.setElectricityb(0);
                deviceElectrcStatistics.setElectricityc(0);
                deviceElectrcStatisticsService.save(deviceElectrcStatistics);
                DeviceElectrcStatistics selectlimit1 = deviceElectrcStatisticsService.selectlimit(datanyr1, imei);
                count(selectlimit1, HistorydataUpdate);
            }
        }
        map.put("status", final_over_level1);
        map.put("final_content", final_content.toString());
        return map;
    }

    /**
     * 根据电压、频率、电流、漏电流、温度超标状态统计其异常数量
     *
     * @param Sattistics
     * @param
     */
    public void count(DeviceElectrcStatistics Sattistics, DeviceElectricHistorydata HistorydataUpdate) {
        Integer vaStatus = HistorydataUpdate.getVaStatus();
        Integer vbStatus = HistorydataUpdate.getVbStatus();
        Integer vcStatus = HistorydataUpdate.getVcStatus();
        Integer faStatus = HistorydataUpdate.getFaStatus();
        Integer fbStatus = HistorydataUpdate.getFbStatus();
        Integer fcStatus = HistorydataUpdate.getFcStatus();
        Integer eaStatus = HistorydataUpdate.getEaStatus();
        Integer ebStatus = HistorydataUpdate.getEbStatus();
        Integer ecStatus = HistorydataUpdate.getEcStatus();
        Integer seStatus = HistorydataUpdate.getSeStatus();
        Integer taStatus = HistorydataUpdate.getTaStatus();
        Integer tbStatus = HistorydataUpdate.getTbStatus();
        Integer tcStatus = HistorydataUpdate.getTcStatus();
        DeviceElectrcStatistics deviceElectrcStatistics = new DeviceElectrcStatistics();
        int id = Sattistics.getId();
        Integer voltage = Sattistics.getVoltage();
        Integer frequency = Sattistics.getFrequency();
        Integer electricity = Sattistics.getElectricity();
        Integer seepelectricity = Sattistics.getSeepelectricity();
        Integer temp = Sattistics.getTemp();
        Integer voltagea = Sattistics.getVoltagea();
        Integer voltageb = Sattistics.getVoltageb();
        Integer voltagec = Sattistics.getVoltagec();
        Integer frequencya = Sattistics.getFrequencya();
        Integer frequencyb = Sattistics.getFrequencyb();
        Integer frequencyc = Sattistics.getFrequencyc();
        Integer electricitya = Sattistics.getElectricitya();
        Integer electricityb = Sattistics.getElectricityb();
        Integer electricityc = Sattistics.getElectricityc();
        Integer tempa = Sattistics.getTempa();
        Integer tempb = Sattistics.getTempb();
        Integer tempc = Sattistics.getTempc();
        if (vaStatus == 2) {
            voltagea += 1;
            voltage +=1;
        }
        if (vbStatus == 2) {
            voltageb += 1;
            voltage += 1;
        }
        if (vcStatus == 2) {
            voltagec += 1;
            voltage += 1;
        }
        if (faStatus == 2) {
            frequencya += 1;
            frequency += 1;
        }
        if (fbStatus == 2) {
            frequencyb += 1;
            frequency += 1;
        }
        if (fcStatus == 2) {
            frequencyc += 1;
            frequency += 1;
        }
        if (eaStatus == 2) {
            electricitya += 1;
            electricity += 1;
        }
        if (ebStatus == 2) {
            electricitya += 1;
            electricity += 1;
        }
        if (ecStatus == 2) {
            electricitya += 1;
            electricity += 1;
        }
        if (seStatus == 2) {
            seepelectricity += 1;
        }
        if (taStatus == 2) {
            tempa += 1;
            temp += 1;
        }
        if (tbStatus == 2) {
            tempb += 1;
            temp += 1;
        }
        if (tcStatus == 2) {
            tempc += 1;
            temp += 1;
        }
        deviceElectrcStatistics.setId(id);
        deviceElectrcStatistics.setVoltage(voltage);
        deviceElectrcStatistics.setFrequency(frequency);
        deviceElectrcStatistics.setElectricity(electricity);
        deviceElectrcStatistics.setSeepelectricity(seepelectricity);
        deviceElectrcStatistics.setTemp(temp);
        deviceElectrcStatistics.setVoltagea(voltagea);
        deviceElectrcStatistics.setVoltageb(voltageb);
        deviceElectrcStatistics.setVoltagec(voltagec);
        deviceElectrcStatistics.setFrequencya(frequencya);
        deviceElectrcStatistics.setFrequencyb(frequencyb);
        deviceElectrcStatistics.setFrequencyc(frequencyc);
        deviceElectrcStatistics.setElectricitya(electricitya);
        deviceElectrcStatistics.setElectricityb(electricityb);
        deviceElectrcStatistics.setElectricityc(electricityc);
        deviceElectrcStatistics.setTempa(tempa);
        deviceElectrcStatistics.setTempb(tempb);
        deviceElectrcStatistics.setTempc(tempc);
        deviceElectrcStatisticsService.updateById(deviceElectrcStatistics);
    }
}

