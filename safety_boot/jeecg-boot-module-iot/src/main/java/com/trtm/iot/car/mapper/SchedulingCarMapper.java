package com.trtm.iot.car.mapper;

import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.trtm.iot.bhzrenwudancar.entity.BhzrenwudanCar;
import com.trtm.iot.bhzrenwudancar.entity.BhzrenwudanCarVO;
import com.trtm.iot.car.entity.SchedulingCar;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.trtm.iot.car.entity.SchedulingCardcVo;
import com.trtm.iot.hntbhz.entity.BhzCementBase;
import com.trtm.iot.ztbhzgonxutime.entity.ZtBhzGonxutime;
import com.trtm.iot.ztbhzjckh.entity.ZtBhzjckh;
import com.trtm.iot.ztbhzkhdf.entity.ZtBhzkhdf;
import com.trtm.iot.ztbhzpeisongtime.entity.ZtBhzPeisongtime;
import com.trtm.sy.sydpssysample.entity.SysDepart;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

/**
 * @Description: 调度车辆
 * @Author: jeecg-boot
 * @Date:   2021-05-20
 * @Version: V1.0
 */
public interface SchedulingCarMapper extends BaseMapper<SchedulingCar> {

    List<SchedulingCar> selectCaiList(Integer curid, String orgCode);

    @Select("select * from bhz_cement_base where client_no = #{danhao}")
    List<BhzCementBase> getBhzByDanHao(String danhao);
    @Select("select * from bhz_cement_base where formula_no = #{phb}")
    List<BhzCementBase> getBhzByPHB(String phb);

    @Select("select * from scheduling_car a,renwudan_schedule b where a.sys_org_code like #{orgcode} and a.Code = b.Code and a.Station = b.station")
    List<SchedulingCardcVo> selectcarpile(String orgcode);

    @Select("select * from sys_depart where org_code = #{sysOrgCode}")
    SysDepart getSysDepart(String sysOrgCode);

    @Select("select * from sys_depart where id = #{parentId}")
    SysDepart getParentDepart(String parentId);

    List<SchedulingCardcVo> selectallziduan(String sys_depart_orgcode, String dattim_begin, String dattim_end);

    Page<BhzrenwudanCar> selecerenwudan(Page<BhzrenwudanCar> page,String sys_depart_orgcode, String dattim_begin, String dattim_end, Integer pageSize, Integer pageno);

    List<BhzrenwudanCar> selecerenwudans(String sys_depart_orgcode, String dattim_begin, String dattim_end);

    List<BhzrenwudanCar> selecerenwudanps(String sys_depart_orgcode, String dattim_begin, String dattim_end, String taiz, String projname);

    List<ZtBhzkhdf> selecerenwudanpsfz(String sys_depart_orgcode, String dattim_begin, String dattim_end);

    @Select("select * from zt_bhz_peisongtime where engineering = #{projname}")
    ZtBhzPeisongtime ztyjsb(String projname);

    @Select("select * from zt_bhz_gonxutime where conspos = #{conspos}")
    ZtBhzGonxutime ztgxsb(String conspos);

    List<ZtBhzjckh> selecerenwudanpsgx(String sys_depart_orgcode, String dattim_begin, String dattim_end);

    List<BhzrenwudanCar> selecerenwudangx(String sys_depart_orgcode, String dattim_begin, String dattim_end, String taiz, String conspos);

    List<BhzrenwudanCarVO> selecerenwudandc(String sys_depart_orgcode, String dattim_begin, String dattim_end);

    List<ZtBhzjckh> selecerenwudanpsgxdc(String sys_depart_orgcode, String dattim_begin, String dattim_end);

    List<ZtBhzkhdf> selecerenwudanpsfzdc(String sys_depart_orgcode, String dattim_begin, String dattim_end);

    List<BhzrenwudanCar> selecerenwudanpsdc(String sys_depart_orgcode, String dattim_begin, String dattim_end, String taiz, String projname, String conspo);

    @Insert("INSERT INTO sys_sms (id,es_title,es_type,es_receiver,es_content,es_send_status,es_send_num,remark,create_time)  VALUES  ( FLOOR(RAND() * POW(10, 19)) , #{esTitle},#{esType},#{esReceiver},#{esContent},#{esSendStatus},#{esSendNum},#{remark},#{createTime})")
    void saveOverMsg(String esTitle, String esType, String esReceiver, String esContent, String esSendStatus, int esSendNum, String remark, Date createTime);

    @Select("SELECT Round(sum(ProdMete),2) as yunshu FROM `scheduling_car` where `code` = #{code}")
    String getMeteByCode(String code);
}
