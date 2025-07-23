package com.trtm.sy.wtgl.qywtd.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.trtm.sy.wtgl.qywtd.entity.QueryVo;
import com.trtm.sy.wtgl.qywtd.entity.SyDpsYyYuancaiquyangweituo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.trtm.sy.wtgl.qywtd.entity.request.QyRequest;
import com.trtm.sy.wtgl.qywtd.entity.request.ZpQyRequest;
import com.trtm.sy.wtgl.qywtd.entity.response.ClResponse;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: sy_dps_yy_yuancaiquyangweituo
 * @Author: jeecg-boot
 * @Date:   2023-02-23
 * @Version: V1.0
 */
public interface ISyDpsYyYuancaiquyangweituoService extends IService<SyDpsYyYuancaiquyangweituo> {

    void insertDelegate(Integer id, SyDpsYyYuancaiquyangweituo weiTuo) throws ParseException;

    void deleteDelegate(Integer id);

    IPage selectByVo(QueryVo queryVo, Integer pageNo, Integer pageSize);

    IPage selectQuYangList(Integer pageNo, Integer pageSize);

    Boolean zhiPaiQuYang(ZpQyRequest zpQyRequest) throws ParseException;

    void shouYang(QyRequest qyRequest);

    IPage<Map> getSyList(QueryVo queryVo, Integer pageNo, Integer pageSize);

    IPage<Map> getYgList(QueryVo queryVo, Integer pageNo, Integer pageSize);

    IPage<SyDpsYyYuancaiquyangweituo> selectList(Integer pageNo, Integer pageSize, QueryVo queryVo);

    void quYang(QyRequest qyRequest);

    void saveQuYangData(QyRequest qyRequest);

    Map jiAnZhEng(String erweimabianhao);

    List<ClResponse> getQueryConditionCl(String nodeType, String sysOrgCode);

    void updateQyZt(QyRequest qyRequest);

    Map getDepartData(String orgCode);

}
