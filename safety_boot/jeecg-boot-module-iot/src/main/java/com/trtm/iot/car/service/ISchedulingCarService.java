package com.trtm.iot.car.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.trtm.iot.bhzrenwudancar.entity.BhzrenwudanCar;
import com.trtm.iot.bhzrenwudancar.entity.BhzrenwudanCarVO;
import com.trtm.iot.car.entity.GetCarDetailVo;
import com.trtm.iot.car.entity.SchedulingCar;
import com.baomidou.mybatisplus.extension.service.IService;
import com.trtm.iot.car.entity.SchedulingCardcVo;
import com.trtm.iot.hntbhz.entity.BhzCementBase;
import com.trtm.iot.ztbhzgonxutime.entity.ZtBhzGonxutime;
import com.trtm.iot.ztbhzjckh.entity.ZtBhzjckh;
import com.trtm.iot.ztbhzkhdf.entity.ZtBhzkhdf;
import com.trtm.iot.ztbhzpeisongtime.entity.ZtBhzPeisongtime;
import com.trtm.sy.sydpssysample.entity.SysDepart;

import java.util.Date;
import java.util.List;

/**
 * @Description: 调度车辆
 * @Author: jeecg-boot
 * @Date: 2021-05-20
 * @Version: V1.0
 */
public interface ISchedulingCarService extends IService<SchedulingCar> {

    List<SchedulingCar> querylist(Integer status, Integer id);

    /**
     * 运输车详情查询
     *
     * @param Code
     * @return SchedulingCarRespVO
     */
    GetCarDetailVo getCarAndMixingStationData(String Code);

    String getMeteByCode(String code);
    List<SchedulingCar> selectCaiList(Integer curid, String orgCode);

    List<BhzCementBase> getBhzByDanHao(String danhao);
    List<BhzCementBase> getBhzByPHB(String phb);

    List<SchedulingCar> getSchedulingCarByDanHao(String danhao);

    List<SchedulingCardcVo> selectcarpile(String orgcode);

    SysDepart getSysDepart(String sysOrgCode);

    String getFcdName(SysDepart sysDepart);

    List<SchedulingCardcVo> selectallziduan(String sys_depart_orgcode, String dattim_begin, String dattim_end);

    Page<BhzrenwudanCar> selecerenwudan(Page<BhzrenwudanCar> page,String sys_depart_orgcode, String dattim_begin, String dattim_end, Integer pageSize, Integer pageno);

    List<BhzrenwudanCar> selecerenwudans(String sys_depart_orgcode, String dattim_begin, String dattim_end);

    List<BhzrenwudanCar> selecerenwudanps(String sys_depart_orgcode, String dattim_begin, String dattim_end, String taiz, String projname);

    List<BhzrenwudanCar> selecerenwudangx(String sys_depart_orgcode, String dattim_begin, String dattim_end, String taiz, String conspos);

    List<ZtBhzkhdf> selecerenwudanpsfz(String sys_depart_orgcode, String dattim_begin, String dattim_end);

    ZtBhzPeisongtime ztyjsb(String projname);

    List<ZtBhzjckh> selecerenwudanpsgx(String sys_depart_orgcode, String dattim_begin, String dattim_end);

    ZtBhzGonxutime ztgxsb(String conspos);

    List<BhzrenwudanCarVO> selecerenwudandc(String sys_depart_orgcode, String dattim_begin, String dattim_end);

    List<ZtBhzjckh> selecerenwudanpsgxdc(String sys_depart_orgcode, String dattim_begin, String dattim_end);

    List<ZtBhzkhdf> selecerenwudanpsfzdc(String sys_depart_orgcode, String dattim_begin, String dattim_end);

    List<BhzrenwudanCar> selecerenwudanpsdc(String sys_depart_orgcode, String dattim_begin, String dattim_end, String taiz, String projname, String conspos);

    void saveOverMsg(String esTitle, String esType, String esReceiver, String esContent, String esSendStatus, int esSendNum, String remark, Date createTime);
}
