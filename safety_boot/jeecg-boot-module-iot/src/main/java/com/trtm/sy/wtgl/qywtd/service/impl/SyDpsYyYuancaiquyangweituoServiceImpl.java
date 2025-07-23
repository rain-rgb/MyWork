package com.trtm.sy.wtgl.qywtd.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.trtm.iot.syycjcdj.entity.SyDpsYyYuancaijinchangdengji;
import com.trtm.iot.syycjcdj.mapper.SyDpsYyYuancaijinchangdengjiMapper;
import com.trtm.iot.wztaizhang.entity.Wztaizhang;
import com.trtm.iot.wztaizhang.mapper.WztaizhangMapper;
import com.trtm.sy.sydpssysample.entity.SyDpsYySypic;
import com.trtm.sy.sydpssysample.entity.SysDepart;
import com.trtm.sy.sydpssysample.mapper.SyDpsSySampleMapper;
import com.trtm.sy.sydpssysample.mapper.SyDpsYySypicMapper;
import com.trtm.sy.syjcxm.entity.SyDpsYyJcxm;
import com.trtm.sy.syjcxm.mapper.SyDpsYyJcxmMapper;
import com.trtm.sy.syrules.entity.SyCodingRules;
import com.trtm.sy.syrules.mapper.SyCodingRulesMapper;
import com.trtm.sy.sywt.mapper.SyDpsYyXianchangjianceweituoMapper;
import com.trtm.sy.sywt.service.ISyDpsYyXianchangjianceweituoService;
import com.trtm.sy.wtgl.qywtd.entity.QuYangVo;
import com.trtm.sy.wtgl.qywtd.entity.QueryVo;
import com.trtm.sy.wtgl.qywtd.entity.SyDpsYyQuyangjiludan;
import com.trtm.sy.wtgl.qywtd.entity.SyDpsYyYuancaiquyangweituo;
import com.trtm.sy.wtgl.qywtd.entity.request.QyRequest;
import com.trtm.sy.wtgl.qywtd.entity.request.ZpQyRequest;
import com.trtm.sy.wtgl.qywtd.entity.response.ClResponse;
import com.trtm.sy.wtgl.qywtd.mapper.SyDpsYyQuyangjiludanMapper;
import com.trtm.sy.wtgl.qywtd.mapper.SyDpsYyYuancaiquyangweituoMapper;
import com.trtm.sy.wtgl.qywtd.service.ISyDpsYyYuancaiquyangweituoService;
import com.xkcoding.http.util.StringUtil;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.exception.JeecgBootException;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.DateUtils;
import org.jeecg.common.util.oConvertUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Description: sy_dps_yy_yuancaiquyangweituo
 * @Author: jeecg-boot
 * @Date: 2023-02-23
 * @Version: V1.0
 */
@Service
public class SyDpsYyYuancaiquyangweituoServiceImpl extends ServiceImpl<SyDpsYyYuancaiquyangweituoMapper, SyDpsYyYuancaiquyangweituo> implements ISyDpsYyYuancaiquyangweituoService {
    @Autowired
    private SyDpsYyYuancaiquyangweituoMapper weiTuoMapper;
    @Autowired
    private WztaizhangMapper wztaizhangMapper;
    @Autowired
    private SyDpsYyQuyangjiludanMapper quyangjiludanMapper;
    @Autowired
    private SyDpsYySypicMapper sypicMapper;
    @Autowired
    private SyDpsYyYuancaijinchangdengjiMapper yuancaijinchangdengjiMapper;
    @Autowired
    private SyDpsYyXianchangjianceweituoMapper xcWeiTuo;
    @Autowired
    private ISyDpsYyXianchangjianceweituoService xcWeiTuoService;
    @Autowired
    private SyDpsYyJcxmMapper jcxmMapper;
    @Autowired
    private SyCodingRulesMapper rulesMapper;
    @Autowired
    private SyDpsSySampleMapper sampleMapper;


    private SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void insertDelegate(Integer id, SyDpsYyYuancaiquyangweituo weiTuo) throws ParseException {
        LoginUser user = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        SyCodingRules rule = sampleMapper.selectDepartRuleByUserOrgCode(user.getOrgCode());
        SysDepart depart = new SysDepart();
//        if (user.getOrgCode().length() > 9 && "A01A01A01".equals(user.getOrgCode().substring(0, 9))) {
//            depart = systemService.get(TSDepart.class, "f9a823d87c7c8d33017c81b17c7a0077");
//        } else {
            depart = xcWeiTuo.getDepartByUserOrgCode(user.getOrgCode());
//        }
        SyCodingRules rules = rulesMapper.selectOne(new QueryWrapper<SyCodingRules>().lambda()
                .eq(SyCodingRules::getDepartId, depart.getId()));
        weiTuo.setYuancaijinchangdengjiid(id);
        Wztaizhang wztaizhang = wztaizhangMapper.selectById(id);
        SysDepart tsDepart = xcWeiTuo.getDepartByUserOrgCode(wztaizhang.getSysOrgCode());
        if (oConvertUtils.isEmpty(weiTuo.getId())) {
            String wTbh = xcWeiTuoService.getBH(rules.getWeituoTableCodingRules(), rule,
                    "sy_dps_yy_yuancaiquyangweituo", "weituodanbianhao", "");
            weiTuo.setWeituodanbianhao(wTbh);
        }
        weiTuo.setQuyangzhuangtai(0);
        weiTuo.setShouyangzhuangtai(0);
        weiTuo.setJianzhengzhuangtai(0);
        if (oConvertUtils.isEmpty(weiTuo.getQuyangdidian())) {
            weiTuo.setQuyangdidian(wztaizhang.getLcbianhao());
        }
        weiTuo.setWeituoriqi(oConvertUtils.isEmpty(weiTuo.getWeituoriqi()) ? DateUtils.getDate("yyyy-MM-dd") : weiTuo.getWeituoriqi());
        weiTuo.setWeituoren(oConvertUtils.isEmpty(weiTuo.getWeituoren()) ? user.getUsername() : weiTuo.getWeituoren());
//        weiTuo.setJianglidanwei(depart.getJianlidanwei());
        String contractNumber = weiTuoMapper.getContractNumber(wztaizhang.getSysOrgCode());
        if (oConvertUtils.isNotEmpty(contractNumber)) {
            weiTuo.setShigongdanweihetonghao(contractNumber);
        }
//        weiTuo.setJianlidanweihetonghao(depart.getContractNumber());
        weiTuo.setJianlidanweidepartid(depart.getId());
        weiTuo.setJianlidanweiorgcode(depart.getOrgCode());
        weiTuo.setZhipaizhuangtai(0);
        weiTuo.setZhixingzhuangtai(0);
//        weiTuo.setWeituozhuangtai(1);

//        if ("0".equals(weiTuo.getWeituozhuangtai())) {
//            wztaizhang.setDelegateState(0);
//        } else if ("1".equals(weiTuo.getWeituozhuangtai())) {
        wztaizhang.setDelegateState(1);
//        }
        this.saveOrUpdate(weiTuo);
        wztaizhangMapper.updateById(wztaizhang);
        if (oConvertUtils.isNotEmpty(weiTuo.getJiancexiangmu())) {
            SyDpsYyJcxm jcxmEntity = new SyDpsYyJcxm();
            jcxmEntity.setJiancexiangmu(weiTuo.getJiancexiangmu());
            jcxmEntity.setDeparid(user.getOrgCode());
            jcxmEntity.setUserid(user.getId());
            jcxmEntity.setData(DateUtils.getDate("yyyy-MM-dd"));
            jcxmMapper.insert(jcxmEntity);
        }

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteDelegate(Integer id) {
        SyDpsYyYuancaiquyangweituo syDpsYyYuancaiquyangweituo = weiTuoMapper.selectOne(new QueryWrapper<SyDpsYyYuancaiquyangweituo>()
                .lambda().eq(SyDpsYyYuancaiquyangweituo::getYuancaijinchangdengjiid, id));
        if (syDpsYyYuancaiquyangweituo.getZhipaizhuangtai() == 1) {
            throw new JeecgBootException("已指派无法撤回");
        }
        wztaizhangMapper.updateDelegateState(id, 0); //修改wztaizhang表中委托的字段
        weiTuoMapper.delete(new QueryWrapper<SyDpsYyYuancaiquyangweituo>().lambda()
                .eq(SyDpsYyYuancaiquyangweituo::getYuancaijinchangdengjiid, id));
    }

    @Override
    public IPage selectByVo(QueryVo queryVo, Integer pageNo, Integer pageSize) {
        LoginUser user = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        if (oConvertUtils.isEmpty(queryVo.getSys_depart_orgcode())) {
            queryVo.setOrgCode(user.getOrgCode());
        }
        Page<Wztaizhang> page = new Page<>(pageNo, pageSize);
        if (isNotEmpty(queryVo.getNodeType())) {
            String nodeType = queryVo.getNodeType();
            List<String> caiLiaoNos = weiTuoMapper.getCaiLiaoNo(nodeType);
            queryVo.setCaiLiaoNos(caiLiaoNos);
        }
        return wztaizhangMapper.selectByV(queryVo, page);
    }

    @Override
    public IPage selectQuYangList(Integer pageNo, Integer pageSize) {
        Page<QuYangVo> page = new Page<>(pageNo, pageSize);
        return weiTuoMapper.selectQuYangList(page);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean zhiPaiQuYang(ZpQyRequest zpQyRequest) throws ParseException {
        boolean flag = true;
        Integer wztzid = zpQyRequest.getWztzid();
        Wztaizhang wztaizhang = wztaizhangMapper.selectById(wztzid);
        SyDpsYyYuancaiquyangweituo yuancaiquyangweituo = null;
        if (wztaizhang.getDelegateState() == 0) { //委托状态为0说明没有委托，先委托
            yuancaiquyangweituo = new SyDpsYyYuancaiquyangweituo();
            yuancaiquyangweituo.setYuancaijinchangdengjiid(wztzid);
            yuancaiquyangweituo.setWeituozhuangtai(1);
            this.insertDelegate(wztzid, yuancaiquyangweituo);
        }
        if (oConvertUtils.isEmpty(yuancaiquyangweituo)) {
            yuancaiquyangweituo = weiTuoMapper.selectOne(new QueryWrapper<SyDpsYyYuancaiquyangweituo>().lambda()
                    .eq(SyDpsYyYuancaiquyangweituo::getYuancaijinchangdengjiid, wztzid));
        }
//        Integer wtid = yuancaiquyangweituo.getId();
        wztaizhang.setTitCode(zpQyRequest.getTitCode());
        wztaizhangMapper.updateById(wztaizhang);
        long count = weiTuoMapper.countQuYangJiLu(wztzid);
        if (count > 0l) {
            flag = false;
            return flag;
        } else {
            LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
//            SyDpsYyYuancaiquyangweituo syDpsYyYuancaiquyangweituo = weiTuoMapper.selectById(wtid);
            yuancaiquyangweituo.setZhipaiquyangren(zpQyRequest.getZhipaiquyangren());
            yuancaiquyangweituo.setZhipaiquyangrenid(zpQyRequest.getZhipaiquyangrenid());
            yuancaiquyangweituo.setZhipairen(loginUser.getUsername());
            yuancaiquyangweituo.setZhipaishijian(sd.format(new Date()));
            yuancaiquyangweituo.setZhipaizhuangtai(1);
            weiTuoMapper.updateById(yuancaiquyangweituo);


//            SysDepart tsDepart = xcWeiTuo.getDepartByUserOrgCode(wztaizhang.getSysOrgCode());
            SysDepart depart = xcWeiTuo.getDepartByUserOrgCode(loginUser.getOrgCode());
            SyCodingRules rules = rulesMapper.selectOne(new QueryWrapper<SyCodingRules>().lambda()
                    .eq(SyCodingRules::getDepartId, depart.getId()));
            String qudbh = xcWeiTuoService.getBH(rules.getQuyangTableCodingRules(), rules,
                    "sy_dps_yy_quyangjiludan", "quyangdanliushuihao", zpQyRequest.getTitCode());
            SyDpsYyQuyangjiludan quYangJiLuDanEntity = new SyDpsYyQuyangjiludan();
            quYangJiLuDanEntity.setQuyangdanliushuihao(qudbh);
            quYangJiLuDanEntity.setQuyangdanleixing(1);
            quYangJiLuDanEntity.setYuancaijinchangdengjiid(yuancaiquyangweituo.getYuancaijinchangdengjiid());
            quYangJiLuDanEntity.setChouyangrenyuan(yuancaiquyangweituo.getZhipaiquyangren());
            quYangJiLuDanEntity.setQuyangdidian(yuancaiquyangweituo.getQuyangdidian());
            quyangjiludanMapper.insert(quYangJiLuDanEntity);
            return flag;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void shouYang(QyRequest qyRequest) {
        LoginUser user = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String ewmuuid = "";
        if (!StringUtil.isNotEmpty(qyRequest.getErweimaweiyima())) {
            ewmuuid = VerifyQRCodes(qyRequest.getErweimabianhao());
        } else {
            ewmuuid = qyRequest.getErweimaweiyima();
        }
        if (isNotEmpty(ewmuuid)) {
            SyDpsYySypic syPicEntity = new SyDpsYySypic();
//            SyDpsYyYuancaiquyangweituo yangWeiTuoEntity = weiTuoMapper.findByErWeiMa(ewmuuid);
            SyDpsYyYuancaiquyangweituo ycwt = weiTuoMapper.findJinChangDJByEWM(ewmuuid);
            SyDpsYyQuyangjiludan qyjLuDanEntity =
                    quyangjiludanMapper.selectOne(new QueryWrapper<SyDpsYyQuyangjiludan>().lambda()
                            .eq(SyDpsYyQuyangjiludan::getYuancaijinchangdengjiid, ycwt.getYuancaijinchangdengjiid()));
            if (1 == ycwt.getJianzhengzhuangtai()) {
                ycwt.setShouyangren(user.getUsername());
                ycwt.setShouyangshijian(sd.format(new Date()));
                ycwt.setShouyangzhuangtai(1);
                weiTuoMapper.updateById(ycwt);
//                ycwt.setShouyangren(user.getUsername());
//                ycwt.setShouyangshijian(sd.format(new Date()));
//                ycwt.setShouyangzhuangtai(1);
//                ycwt.setWeituozhuangtai(3);
//                yuancaijinchangdengjiMapper.updateById(yCaiJinChangDengJiEntity);
                if (isNotEmpty(qyRequest.getImgs())) {
                    String[] imgString = qyRequest.getImgs().split(",");
                    for (int i = 0; i < imgString.length; i++) {
                        syPicEntity.setUrl(imgString[i]);
                        syPicEntity.setQydid(qyjLuDanEntity.getId());
                        sypicMapper.insert(syPicEntity);
                    }
                }
            } else {
                throw new JeecgBootException("数据没有见证无法收样!");
            }
        } else {
            throw new JeecgBootException("二维码没有对应样品数据!");
        }
    }

    @Override
    public IPage<Map> getSyList(QueryVo queryVo, Integer pageNo, Integer pageSize) {
        Page<Map> page = new Page<>(pageNo, pageSize);

        if (isNotEmpty(queryVo.getNodeType())) {
            String nodeType = queryVo.getNodeType();
            List<String> caiLiaoNos = weiTuoMapper.getCaiLiaoNo(nodeType);
            queryVo.setCaiLiaoNos(caiLiaoNos);
        }
//        return weiTuoMapper.getSyLis(page, queryVo);
        IPage<Map> syList = wztaizhangMapper.getSyList(page, queryVo);
        for (Map record : syList.getRecords()) {
            if (oConvertUtils.isNotEmpty(record.get("quyangdanid"))) {
                List<String> urls = this.baseMapper.getPhotoUrl(Integer.valueOf(record.get("quyangdanid").toString()));
                if (urls.size() > 0) {
                    StringBuilder urlBuilds = new StringBuilder();
                    for (String url : urls) {
                        urlBuilds.append(url).append(",");
                    }
                    record.put("imgs", urlBuilds.substring(0, urlBuilds.length() - 1));
                }
            }
        }
        return syList;
    }

    @Override
    public IPage<Map> getYgList(QueryVo queryVo, Integer pageNo, Integer pageSize) {
        Page<Map> page = new Page<>(pageNo, pageSize);

        if (isNotEmpty(queryVo.getNodeType())) {
            String nodeType = queryVo.getNodeType();
            List<String> caiLiaoNos = weiTuoMapper.getCaiLiaoNo(nodeType);
            queryVo.setCaiLiaoNos(caiLiaoNos);
        }

        return weiTuoMapper.getYgList(page, queryVo);
    }

    @Override
    public IPage<SyDpsYyYuancaiquyangweituo> selectList(Integer pageNo, Integer pageSize, QueryVo queryVo) {
        Page<SyDpsYyYuancaiquyangweituo> page = new Page<>(pageNo, pageSize);

        if (isNotEmpty(queryVo.getNodeType())) {
            String nodeType = queryVo.getNodeType();
            List<String> caiLiaoNos = weiTuoMapper.getCaiLiaoNo(nodeType);
            queryVo.setCaiLiaoNos(caiLiaoNos);
        }

        return weiTuoMapper.selectListB(page, queryVo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void quYang(QyRequest qyRequest) {
        LoginUser user = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        SyDpsYyQuyangjiludan quyangjiludan = quyangjiludanMapper.selectById(qyRequest.getQuyangdanid());
        quyangjiludan.setChengxingriqi(qyRequest.getChengxingriqi());
        quyangjiludan.setYangpingshuliang(qyRequest.getYangpingshuliang());
        quyangjiludan.setYangpinmiaoshu(qyRequest.getYangpinmiaoshu());
        quyangjiludanMapper.updateById(quyangjiludan);
        SyDpsYySypic qyPicEntity = new SyDpsYySypic();
        if (isNotEmpty(qyRequest.getImgs())) {
            String[] imgString = qyRequest.getImgs().split(",");
            for (int i = 0; i < imgString.length; i++) {
                qyPicEntity.setUrl(imgString[i]);
                qyPicEntity.setQydid(qyRequest.getQuyangdanid());
                sypicMapper.insert(qyPicEntity);
            }
        }
        String ewmuuid = "";
        if (!isNotEmpty(qyRequest.getErweimaweiyima())) {
            ewmuuid = VerifyQRCodes(qyRequest.getErweimabianhao());
        } else {
            ewmuuid = qyRequest.getErweimaweiyima();
        }
        SyDpsYyYuancaiquyangweituo yuancaiquyangweituo = weiTuoMapper.selectById(qyRequest.getWtid());
//        SyDpsYyYuancaijinchangdengji yCaiJinChangDengJiEntity = weiTuoMapper.findJinChangDJByYCID(quyangjiludan.getYuancaijinchangdengjiid());
        if (StringUtil.isNotEmpty(ewmuuid) /*|| yCaiJinChangDengJiEntity.getWaiweizhuangtai().equals("1")*/) {
            yuancaiquyangweituo.setQuyangren(user.getUsername());
            yuancaiquyangweituo.setQuyangshijian(sd.format(new Date()));
            yuancaiquyangweituo.setQuyangzhuangtai(1);
            yuancaiquyangweituo.setErweimabianhao(qyRequest.getErweimabianhao());
            yuancaiquyangweituo.setErweimaweiyima(ewmuuid);
            weiTuoMapper.updateById(yuancaiquyangweituo);
          /*  yCaiJinChangDengJiEntity.setQuyangren(user.getUsername());
            yCaiJinChangDengJiEntity.setQuyangshijian(sd.format(new Date()));
            yCaiJinChangDengJiEntity.setQuyangzhuangtai(1);
            yCaiJinChangDengJiEntity.setErweimabianhao(map.get("erweimabianhao").toString());
            yCaiJinChangDengJiEntity.setErweimaweiyima(ewmuuid);
            yCaiJinChangDengJiEntity.setWeituozhuangtai(2);
            yuancaijinchangdengjiMapper.updateById(yCaiJinChangDengJiEntity);*/
        } else {
            throw new JeecgBootException("二维码已被使用，请更换!");
        }

    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveQuYangData(QyRequest qyRequest) {
        LoginUser user = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        if (oConvertUtils.isEmpty(qyRequest.getQuyangdanid())) {
            throw new JeecgBootException("请先指派人员取样");
        }
        SyDpsYyQuyangjiludan quyangjiludan = quyangjiludanMapper.selectById(qyRequest.getQuyangdanid());
        quyangjiludan.setChengxingriqi(qyRequest.getChengxingriqi());
        quyangjiludan.setYangpingshuliang(qyRequest.getYangpingshuliang());
        quyangjiludan.setYangpinmiaoshu(qyRequest.getYangpinmiaoshu());
        quyangjiludanMapper.updateById(quyangjiludan);
        SyDpsYySypic qyPicEntity = new SyDpsYySypic();
        if (isNotEmpty(qyRequest.getImgs())) {
            String[] imgString = qyRequest.getImgs().split(",");
            for (int i = 0; i < imgString.length; i++) {
                qyPicEntity.setUrl(imgString[i]);
                qyPicEntity.setQydid(qyRequest.getQuyangdanid());
                sypicMapper.insert(qyPicEntity);
            }
        }
//        String ewmuuid = "";
//        if (!isNotEmpty(qyRequest.getErweimaweiyima())) {
//            ewmuuid = VerifyQRCodes(qyRequest.getErweimabianhao());
//        } else {
//            ewmuuid = qyRequest.getErweimaweiyima();
//        }
        SyDpsYyYuancaiquyangweituo yuancaiquyangweituo = weiTuoMapper.selectById(qyRequest.getWtid());
//        if (StringUtil.isNotEmpty(ewmuuid)) {
            yuancaiquyangweituo.setQuyangren(user.getUsername());
            yuancaiquyangweituo.setQuyangshijian(qyRequest.getQuyangshijian());
//            yuancaiquyangweituo.setQuyangzhuangtai(1);
//            yuancaiquyangweituo.setErweimabianhao(qyRequest.getErweimabianhao());
//            yuancaiquyangweituo.setErweimaweiyima(ewmuuid);
            weiTuoMapper.updateById(yuancaiquyangweituo);
//        } else {
//            throw new JeecgBootException("二维码已被使用，请更换!");
//        }

    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateQyZt(QyRequest qyRequest) {
        LoginUser user = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String ewmuuid = "";
        if (!isNotEmpty(qyRequest.getErweimaweiyima())) {
            ewmuuid = VerifyQRCodes(qyRequest.getErweimabianhao());
        } else {
            ewmuuid = qyRequest.getErweimaweiyima();
        }
        SyDpsYyYuancaiquyangweituo yuancaiquyangweituo = weiTuoMapper.selectById(qyRequest.getWtid());
        if (StringUtil.isNotEmpty(ewmuuid)) {
            yuancaiquyangweituo.setQuyangren(user.getUsername());
            yuancaiquyangweituo.setQuyangshijian(sd.format(new Date()));

            yuancaiquyangweituo.setJianzhengzhuangtai(1);
            yuancaiquyangweituo.setJianzhengren(user.getRealname());
            yuancaiquyangweituo.setJianzhengshijian(sd.format(new Date()));

            yuancaiquyangweituo.setQuyangzhuangtai(1);
            yuancaiquyangweituo.setErweimabianhao(qyRequest.getErweimabianhao());
            yuancaiquyangweituo.setErweimaweiyima(ewmuuid);
            weiTuoMapper.updateById(yuancaiquyangweituo);
            saveQuYangData(qyRequest);
        } else {
            throw new JeecgBootException("二维码已被使用，请更换!");
        }

    }

    @Override
    public Map getDepartData(String orgCode) {
//        LoginUser user = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        return this.baseMapper.getDepartData(orgCode);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map jiAnZhEng(String erweimabianhao) {
        LoginUser user = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String ewmuuid = ewm(erweimabianhao);
        if (StringUtil.isNotEmpty(ewmuuid)) {
            SyDpsYyYuancaiquyangweituo yangWeiTuoEntity = weiTuoMapper
                    .selectOne(new QueryWrapper<SyDpsYyYuancaiquyangweituo>().lambda().eq(SyDpsYyYuancaiquyangweituo::getErweimaweiyima, ewmuuid));
            /*SyDpsYyYuancaijinchangdengji yCaiJinChangDengJiEntity = yuancaijinchangdengjiMapper
                    .selectOne(new QueryWrapper<SyDpsYyYuancaijinchangdengji>().lambda().eq(SyDpsYyYuancaijinchangdengji::getErweimaweiyima, ewmuuid));*/
            if (isNotEmpty(yangWeiTuoEntity)) {
                String jianzhengren = yangWeiTuoEntity.getJianzhengren();
                if ("".equals(jianzhengren) || StringUtils.isEmpty(jianzhengren)) {
                    yangWeiTuoEntity.setJianzhengren(user.getUsername());
                } else {
                    yangWeiTuoEntity.setJianzhengren(jianzhengren + "," + user.getUsername());
                }

                yangWeiTuoEntity.setJianzhengshijian(sd.format(new Date()));
                yangWeiTuoEntity.setJianzhengzhuangtai(1);
                weiTuoMapper.updateById(yangWeiTuoEntity);

                /*if ("".equals(jianzhengren) || StringUtils.isEmpty(jianzhengren)) {
                    yCaiJinChangDengJiEntity.setJianzhengren(user.getUsername());
                }else {
                    yCaiJinChangDengJiEntity.setJianzhengren(jianzhengren+","+user.getUsername());
                }
                yCaiJinChangDengJiEntity.setJianzhengshijian(sd.format(new Date()));
                yCaiJinChangDengJiEntity.setJianzhengzhuangtai(1);
                yCaiJinChangDengJiEntity.setWeituozhuangtai(3);
                yuancaijinchangdengjiMapper.updateById(yCaiJinChangDengJiEntity);*/
                Wztaizhang wztaizhang = wztaizhangMapper.selectOne(new QueryWrapper<Wztaizhang>().lambda()
                        .eq(Wztaizhang::getId, yangWeiTuoEntity.getYuancaijinchangdengjiid()));
                Map<String, Object> map = new HashMap<>();
                map.put("erweimabianhao", erweimabianhao);
                map.put("cailiaoNo", wztaizhang.getCailiaono());
//                map.put("Cailiaomingcheng", yCaiJinChangDengJiEntity.getCailiaomingcheng());
                return map;
            } else {
                throw new JeecgBootException("二维码没有对应样品数据!");
            }
        } else {
            throw new JeecgBootException("二维码未在系统录入!");
        }
    }

    @Override
    public List<ClResponse> getQueryConditionCl(String nodeType, String sysOrgCode) {
        if (oConvertUtils.isEmpty(sysOrgCode)) {
            sysOrgCode = "A";
        }
        return this.baseMapper.getQueryConditionCl(nodeType, sysOrgCode);
    }


    private String ewm(String CODENO) {
        String[] qrcode = CODENO.split("-");
//        Map<String, Object> map = systemService.findOneForJdbc("SELECT uuid FROM QRCODE WHERE id='" + qrcode[0] + "'");
        Map<String, Object> map = weiTuoMapper.findUUIDByQRCODE(qrcode[0]);
        if (isNotEmpty(map)) {
            String[] qrcodes = map.get("uuid").toString().split(",");
            String codeString = qrcodes[Integer.valueOf(qrcode[1])];
            return codeString;
        } else {
            return "";
        }
    }


    private String VerifyQRCodes(String CODENO) {
        String msgString = "";
        String[] qrcode = CODENO.split("-");
        String code = qrcode[0];
//        Map<String, Object> map =	systemService.findOneForJdbc("SELECT uuid FROM QRCODE WHERE id='"+qrcode[0]+"'");
        Map<String, Object> map = weiTuoMapper.queryUUIDByQrCode(code);
        if (isNotEmpty(map)) {
            String[] qrcodes = map.get("uuid").toString().split(",");
            String codeString = qrcodes[Integer.valueOf(qrcode[1])];
            Map<String, Object> sampleQRCode = weiTuoMapper.findByQrCode(codeString);
            Map<String, Object> tcCodeEntity = weiTuoMapper.findByCodeNo(codeString);
            SyDpsYyYuancaiquyangweituo yangWeiTuoEntity = weiTuoMapper.findByErWeiMa(codeString);
            msgString = codeString;
            if (isNotEmpty(sampleQRCode)) {
                return "";
            } else if (isNotEmpty(tcCodeEntity)) {
                return "";
            } else if (isNotEmpty(yangWeiTuoEntity)) {
                return "";
            } else {
                return msgString;
            }
        } else {
            return "";
        }
    }

    /**
     * 判断对象是否为空
     *
     * @param str
     * @return
     */
    private boolean isNotEmpty(Object str) {
        boolean flag = true;
        if (str != null && !str.equals("")) {
            if (str.toString().length() > 0) {
                flag = true;
            }
        } else {
            flag = false;
        }
        return flag;
    }

    private List asList(String[] split) {
        List list = new ArrayList<>();
        for (String s : split) {
            list.add(s);
        }
        return list;
    }
}
