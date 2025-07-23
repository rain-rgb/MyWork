package com.trtm.iot.car.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.trtm.iot.bhzrenwudancar.entity.BhzrenwudanCar;
import com.trtm.iot.bhzrenwudancar.entity.BhzrenwudanCarVO;
import com.trtm.iot.car.entity.GetCarDetailVo;
import com.trtm.iot.car.entity.SchedulingCar;
import com.trtm.iot.car.entity.SchedulingCardcVo;
import com.trtm.iot.car.mapper.SchedulingCarMapper;
import com.trtm.iot.car.service.ISchedulingCarService;
import com.trtm.iot.hntbhz.entity.BhzCementBase;
import com.trtm.iot.hntbhz.mapper.BhzCementBaseMapper;
import com.trtm.iot.ztbhzgonxutime.entity.ZtBhzGonxutime;
import com.trtm.iot.ztbhzjckh.entity.ZtBhzjckh;
import com.trtm.iot.ztbhzkhdf.entity.ZtBhzkhdf;
import com.trtm.iot.ztbhzpeisongtime.entity.ZtBhzPeisongtime;
import com.trtm.sy.sydpssysample.entity.SysDepart;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Description: 调度车辆
 * @Author: jeecg-boot
 * @Date: 2021-05-20
 * @Version: V1.0
 */
@Service
public class SchedulingCarServiceImpl extends ServiceImpl<SchedulingCarMapper, SchedulingCar> implements ISchedulingCarService {

    @Resource
    private SchedulingCarMapper schedulingCarMapper;

    @Resource
    private BhzCementBaseMapper bhzCementBaseMapper;

    @Override
    public List<SchedulingCar> getSchedulingCarByDanHao(String danhao) {
        if (StringUtils.isNotBlank(danhao)) {
            QueryWrapper<SchedulingCar> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("danhao", danhao);
            return this.list(queryWrapper);
        } else {
            return null;
        }

    }

    @Override
    public List<SchedulingCardcVo> selectcarpile(String orgcode) {
        return schedulingCarMapper.selectcarpile(orgcode);
    }

    @Override
    public SysDepart getSysDepart(String sysOrgCode) {
        return schedulingCarMapper.getSysDepart(sysOrgCode);
    }

    @Override
    public String getFcdName(SysDepart sysDepart) {
        SysDepart sysDepart5 = schedulingCarMapper.getParentDepart(sysDepart.getParentId());
        SysDepart sysDepart4 = schedulingCarMapper.getParentDepart(sysDepart5.getParentId());
        SysDepart sysDepart3 = schedulingCarMapper.getParentDepart(sysDepart4.getParentId());
        SysDepart sysDepart2 = schedulingCarMapper.getParentDepart(sysDepart3.getParentId());
        return sysDepart2.getDepartName()+sysDepart3.getDepartName()+sysDepart4.getDepartName()+sysDepart5.getDepartName()+sysDepart.getDepartName();
    }

    @Override
    public List<SchedulingCardcVo> selectallziduan(String sys_depart_orgcode, String dattim_begin, String dattim_end) {
        return schedulingCarMapper.selectallziduan(sys_depart_orgcode,dattim_begin,dattim_end);
    }

    @Override
    public Page<BhzrenwudanCar> selecerenwudan(Page<BhzrenwudanCar> page,String sys_depart_orgcode, String dattim_begin, String dattim_end, Integer pageSize, Integer pageno) {
        return schedulingCarMapper.selecerenwudan(page,sys_depart_orgcode,dattim_begin,dattim_end,pageSize,pageno);
    }
    @Override
    public List<BhzrenwudanCar> selecerenwudanps(String sys_depart_orgcode, String dattim_begin, String dattim_end, String taiz, String projname) {
        return schedulingCarMapper.selecerenwudanps(sys_depart_orgcode,dattim_begin,dattim_end,taiz,projname);
    }

    @Override
    public List<BhzrenwudanCar> selecerenwudangx(String sys_depart_orgcode, String dattim_begin, String dattim_end, String taiz, String conspos) {
        return schedulingCarMapper.selecerenwudangx(sys_depart_orgcode,dattim_begin,dattim_end,taiz,conspos);
    }
    @Override
    public List<ZtBhzkhdf> selecerenwudanpsfz(String sys_depart_orgcode, String dattim_begin, String dattim_end) {
        return schedulingCarMapper.selecerenwudanpsfz(sys_depart_orgcode,dattim_begin,dattim_end);
    }

    @Override
    public List<ZtBhzjckh> selecerenwudanpsgx(String sys_depart_orgcode, String dattim_begin, String dattim_end) {
        return schedulingCarMapper.selecerenwudanpsgx(sys_depart_orgcode,dattim_begin,dattim_end);
    }

    @Override
    public ZtBhzGonxutime ztgxsb(String conspos) {
        return schedulingCarMapper.ztgxsb(conspos);
    }

    @Override
    public List<BhzrenwudanCarVO> selecerenwudandc(String sys_depart_orgcode, String dattim_begin, String dattim_end) {
        return schedulingCarMapper.selecerenwudandc(sys_depart_orgcode,dattim_begin,dattim_end);
    }

    @Override
    public List<ZtBhzjckh> selecerenwudanpsgxdc(String sys_depart_orgcode, String dattim_begin, String dattim_end) {
        return schedulingCarMapper.selecerenwudanpsgxdc(sys_depart_orgcode,dattim_begin,dattim_end);
    }

    @Override
    public List<ZtBhzkhdf> selecerenwudanpsfzdc(String sys_depart_orgcode, String dattim_begin, String dattim_end) {
        return schedulingCarMapper.selecerenwudanpsfzdc(sys_depart_orgcode,dattim_begin,dattim_end);
    }

    @Override
    public List<BhzrenwudanCar> selecerenwudanpsdc(String sys_depart_orgcode, String dattim_begin, String dattim_end, String taiz, String projname, String conspo) {
        return schedulingCarMapper.selecerenwudanpsdc(sys_depart_orgcode,dattim_begin,dattim_end,taiz,projname,conspo);
    }

    @Override
    public void saveOverMsg(String esTitle, String esType, String esReceiver, String esContent, String esSendStatus, int esSendNum, String remark, Date createTime) {
        schedulingCarMapper.saveOverMsg( esTitle,  esType,  esReceiver,  esContent,  esSendStatus,  esSendNum,  remark,  createTime);
    }


    @Override
    public ZtBhzPeisongtime ztyjsb(String projname) {
        return schedulingCarMapper.ztyjsb(projname);
    }

    @Override
    public List<BhzrenwudanCar> selecerenwudans(String sys_depart_orgcode, String dattim_begin, String dattim_end) {
        return schedulingCarMapper.selecerenwudans(sys_depart_orgcode,dattim_begin,dattim_end);
    }


    @Override
    public List<SchedulingCar> querylist(Integer status, Integer id) {
        try {
            QueryWrapper<SchedulingCar> queryWrapper = new QueryWrapper<>();
            queryWrapper.gt("id", id);
            queryWrapper.eq("status1", status);
            queryWrapper.last("limit 100");
            return this.list(queryWrapper);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public GetCarDetailVo getCarAndMixingStationData(String Code) {
        GetCarDetailVo getCarDetailVo = new GetCarDetailVo();
        List<SchedulingCar> vos;
        if (StringUtils.isNotBlank(Code)) {
            QueryWrapper<SchedulingCar> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("Code", Code).orderByAsc("DatTim");
            List<SchedulingCar> schedulingCarList = schedulingCarMapper.selectList(queryWrapper);
            int carQuantity = schedulingCarList.size();
            int overProof = 0;
            int close = 0;
            int transportTimeout = 0;
            String castingTime = "";
            vos = schedulingCarList;
            if (vos != null && vos.size() > 0) {
                Date firstTime = vos.get(0).getDattim();
                SchedulingCar lastCar = vos.get(vos.size() - 1);
                if (lastCar.getStatus() == 1) {
                    Date lastCarSignInTime = lastCar.getQianshoutime();
                    if (lastCar.getQianshoutime() != null) {
                        castingTime = getDeltaT(firstTime, lastCarSignInTime);
                    }
                } else {
                    castingTime = getDeltaT(firstTime, new Date());
                }
                for (SchedulingCar scr : vos) {
                    // 计算运输时长和运输超时数量
                    Date practicalFinalTime = scr.getQianshoutime(); //实际送达时间
                    Date finalTime = scr.getEtatime();   //预计送达时间
                    Integer status = scr.getStatus();
                    String shippingLength = "";
                    if (status == 1) {
                        if (scr.getQianshoutime() != null) {
                            shippingLength = getDeltaT(scr.getDattim(), scr.getQianshoutime());
                        }
                        if (finalTime != null) {
                            if (practicalFinalTime.after(finalTime)) {
                                transportTimeout++;
                            }
                        }
                    } else {
                        if (finalTime != null) {
                            if (finalTime.before(new Date())) {
                                transportTimeout++;
                            }
                        }
                        shippingLength = getDeltaT(scr.getDattim(), new Date());
                    }
                    scr.setShippingLength(shippingLength);
                    //计算运输方量、闭合数、超标数
                    String orderNo = scr.getDanhao();
                    QueryWrapper<BhzCementBase> cementQueryWrapper = new QueryWrapper<>();
                    cementQueryWrapper.select("product_datetime", "strength_rank", "stir_datetime", "overproof_status", "estimate_number", "over_level");
                    cementQueryWrapper.eq("client_no", orderNo);
                    List<BhzCementBase> cementBaseList = bhzCementBaseMapper.selectList(cementQueryWrapper);
                    Double totalVolume = 0.00;
                    if (cementBaseList != null && cementBaseList.size() > 0) {
                        for (BhzCementBase item : cementBaseList) {
                            totalVolume += item.getEstimateNumber();
                            if (item.getOverLevel() != 0) {
                                overProof++;
                                if (item.getOverproofStatus() == 20) {
                                    close++;
                                }
                            }
                        }
                    }
                    scr.setTransportVolume(totalVolume);
                    scr.setBhzCementBasePOList(cementBaseList);
                }
            }
            getCarDetailVo.setCarAndMixingStationList(vos);
            getCarDetailVo.setClose(close);
            getCarDetailVo.setCarQuantity(carQuantity);
            getCarDetailVo.setOverProof(overProof);
            getCarDetailVo.setTransportTimeout(transportTimeout);
            getCarDetailVo.setCastingTime(castingTime);
        }
        return getCarDetailVo;
    }

    @Override
    public String getMeteByCode(String code) {

        return  schedulingCarMapper.getMeteByCode( code);
//        try {
//            QueryWrapper<SchedulingCar> queryWrapper = new QueryWrapper<>();
//            queryWrapper.select(" IFNULL(FORMAT(sum(ProdMete),2),0.00) yunshu ");
//            queryWrapper.eq("`code`", code);
//            Map<String, Object> map = this.getMap(queryWrapper);
//            return String.valueOf(map.get("yunshu"));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    @Override
    public List<SchedulingCar> selectCaiList(Integer curid, String orgCode) {
        return schedulingCarMapper.selectCaiList(curid, orgCode);
    }

    @Override
    public List<BhzCementBase> getBhzByDanHao(String danhao) {
        return schedulingCarMapper.getBhzByDanHao(danhao);
    }

    @Override
    public List<BhzCementBase> getBhzByPHB(String phb) {
        return schedulingCarMapper.getBhzByPHB(phb);
    }

    /**
     * 时间差计算
     *
     * @param start 开始时间
     * @param end   结束时间
     * @return 结果字符串
     */
    private String getDeltaT(Date start, Date end) {
        // Date 转 LocalDateTime
        LocalDateTime startTime = LocalDateTime.ofInstant(start.toInstant(), ZoneOffset.of("+8"));
        LocalDateTime endTime = LocalDateTime.ofInstant(end.toInstant(), ZoneOffset.of("+8"));
        //时间格式转换对象
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        String startStr = startTime.format(dtf);
        System.out.println("开始时间 = " + startStr);

        String endStr = endTime.format(dtf);
        System.out.println("结束时间 = " + endStr);

        //获取开始秒数
        long startSecond = startTime.toEpochSecond(ZoneOffset.ofHours(0));
        System.out.println("开始的秒数 = " + startSecond);

        //获取结束秒数
        long endSecond = endTime.toEpochSecond(ZoneOffset.ofHours(0));
        System.out.println("结束的秒数 = " + endSecond);

        //Math.abs()方法,两个long类型的数相减，得到的永远是正数
        long abs = Math.abs(endSecond - startSecond);

        //计算两个时间差的秒数，Java8的API暂时不支持，到Java9开始引入 duration.toSeconds() 支持
        //获取秒数
        long second = abs % 60;
        //获取分钟数
        long minute = abs / 60 % 60;
        //获取小时数
        long hour = abs / 60 / 60 % 24;
        //获取天数
        long day = abs / 60 / 60 / 24;
        String result = day + "天" + hour + "时" + minute + "分" + second + "秒";
        System.out.println(day + "天" + hour + "时" + minute + "分" + second + "秒");
        return result;
    }
}
