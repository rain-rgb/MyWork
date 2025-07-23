package com.trtm.iot.wztaizhang.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.trtm.iot.system.entity.Bhzrenwudan;
import com.trtm.iot.wzcailiaonamedict.mapper.WzcailiaonamedictMapper;
import com.trtm.iot.wztaizhang.entity.JypWztz;
import com.trtm.iot.wztaizhang.entity.Wztaizhang;
import com.trtm.iot.wztaizhang.mapper.WztaizhangMapper;
import com.trtm.iot.wztaizhang.service.IWztaizhangService;
import com.trtm.iot.wztaizhang.vo.WztaizhangKBVO;
import com.trtm.iot.yclud.entity.YclUsageDetail;
import com.trtm.sy.wtgl.qywtd.entity.QueryVo;
import com.trtm.sy.wtgl.qywtd.mapper.SyDpsYyYuancaiquyangweituoMapper;
import com.trtm.sy.wtgl.qywtd.service.ISyDpsYyYuancaiquyangweituoService;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.oConvertUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.text.DecimalFormat;
import java.util.*;

/**
 * @Description: wztaizhang
 * @Author: jeecg-boot
 * @Date: 2021-06-18
 * @Version: V1.0
 */
@Service
public class WztaizhangServiceImpl extends ServiceImpl<WztaizhangMapper, Wztaizhang> implements IWztaizhangService {
    @Autowired
    private WztaizhangMapper wztaizhangMapper;
    @Autowired
    private WzcailiaonamedictMapper wzcailiaonamedictMapper;
    @Autowired
    private ISyDpsYyYuancaiquyangweituoService weiTuoService;
    @Autowired
    private SyDpsYyYuancaiquyangweituoMapper weiTuoMapper;

    @Override
    public List<Map> selectPiciBylc(String lcguid) {
        return wztaizhangMapper.selectPiciBylc(lcguid);
    }

    @Override
    public List<Map> selectPiciBylc2(String lcguid) {
        return wztaizhangMapper.selectPiciBylc2(lcguid);
    }

    @Override
    public Wztaizhang getWztaizhang(String sysOrgCode, String cailiaoNo, String gongyinsgangNo, String pici) {

        QueryWrapper<Wztaizhang> queryWrapper = new QueryWrapper<>();
        //地磅人工登记的进行生成批次
//        queryWrapper.eq("ruleway",3);
        queryWrapper.eq("sys_org_code", sysOrgCode);
        queryWrapper.eq("cailiaoNo", cailiaoNo);
        queryWrapper.eq("gongyingshangdanweibianma", gongyinsgangNo);
        queryWrapper.eq(!(StrUtil.isBlank(pici) || "/".equals(pici)), "pici", pici);
        queryWrapper.orderByDesc("id");
        queryWrapper.last(" limit 1");
        Wztaizhang pageList = this.getOne(queryWrapper);
        return pageList;
    }


    public int updateone(String maozhongT2, String pizhongT2, Integer ids, String jingzhongT2) {
        return wztaizhangMapper.updatealertsate(maozhongT2, pizhongT2, ids, jingzhongT2);
    }

    @Override
    public Wztaizhang queryone(String shebeiNo, String lcNo, String cailiaoNo, String gongyinsgangNo) {
        try {
            QueryWrapper<Wztaizhang> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("shebeibianhao", shebeiNo);
            queryWrapper.eq("LCbianhao", lcNo);
            queryWrapper.eq("cailiaoNo", cailiaoNo);
            queryWrapper.eq("gongyingshangdanweibianma", gongyinsgangNo);
            queryWrapper.orderByDesc("id");
            queryWrapper.last("limit 1");
            return this.getOne(queryWrapper);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Wztaizhang selectwztaizhangone(Integer ids) {
        try {
            QueryWrapper<Wztaizhang> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("id", ids);
            return this.getOne(queryWrapper);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Wztaizhang> selectlc(String lcNo) {
        try {
            QueryWrapper<Wztaizhang> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("LCbianhao", lcNo);
            queryWrapper.eq("jianyanstate", 1);
            return this.list(queryWrapper);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Wztaizhang> selectwzjypList(Integer curid, List<String> strsToList1) {
        try {
            QueryWrapper<Wztaizhang> queryWrapper = new QueryWrapper<>();
            queryWrapper.gt("id", curid);
            queryWrapper.in("shebeibianhao", strsToList1);
            queryWrapper.last("limit 100");
            return this.list(queryWrapper);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Map> zhongdianCailiao() {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        return wztaizhangMapper.zhongdianCailiao(loginUser.getOrgCode());
    }

    @Override
    public Wztaizhang getselectlcone(String lc) {
        try {
            QueryWrapper<Wztaizhang> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("LCbianhao", lc);
            queryWrapper.eq("isfinish", 1);
            queryWrapper.last("limit 1");
            return this.getOne(queryWrapper);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Wztaizhang> selectLists(String strsToList1, Integer curid) {
        return wztaizhangMapper.selectLists(strsToList1, curid);
    }

    @Override
    public Wztaizhang queryones(String sysOrgCode, String cailiaoNo, String gongyinsgangNo) {
        return wztaizhangMapper.queryones(sysOrgCode, cailiaoNo, gongyinsgangNo);
    }

    @Override
    public Wztaizhang queryoness(String sysOrgCode, String cailiaoNo, String gongyinsgangNo, String pici) {
        return wztaizhangMapper.queryoness(sysOrgCode, cailiaoNo, gongyinsgangNo, pici);
    }

    @Override
    public List<Wztaizhang> selectolddata(Integer ids) {
        return wztaizhangMapper.selectolddata(ids);
    }

    @Override
    public List<String> getOrgCodeList(String orgCategory) {
        return wztaizhangMapper.getOrgCodeList(orgCategory);
    }

    @Override
    public String getDepartName(String orgCode) {
        return wztaizhangMapper.getDepartName(orgCode);
    }

    @Override
    public String selectPici(String orgcode, String nodetype) {
        return wztaizhangMapper.selectPici(orgcode, nodetype);
    }

    @Override
    public String selectBhgPici(String orgcode, String nodetype) {
        return wztaizhangMapper.selectBhgPici(orgcode, nodetype);
    }

    /**
     * 计算百分比字符串，传入参数 diff=1,sum=2,返回50%
     *
     * @param diff
     * @param sum
     * @return
     */
    @Override
    public String getPercentStr(Integer diff, Integer sum) {
        DecimalFormat df = new DecimalFormat("0.00");
        if (sum == 0) {
            return "0.00%";
        }
        float num = (float) diff / sum * 100;
        String str = df.format(num);
        return str + "%";
    }

    @Override
    public List<String> selectOrgcode(String orgCategory, String orgCode) {
        return wztaizhangMapper.selectOrgcode(orgCategory, orgCode);
    }

    @Override
    public List<Wztaizhang> selectBhgPiciByBd(String nodetype, String orgcode) {
        return wztaizhangMapper.selectBhgPiciByBd(nodetype, orgcode);
    }

    @Override
    public String selectGsName(String orgcode) {
        return wztaizhangMapper.selectGsName(orgcode);
    }

    @Override
    public String selectCailiaoName(String cailiaono) {
        return wztaizhangMapper.selectCailiaoName(cailiaono);
    }

    @Override
    public List<JypWztz> queryJypList() {
        return wztaizhangMapper.queryJypList();
    }

    @Override
    public String selectNodetypeByCailiaono(String cailiaono) {
        return wztaizhangMapper.selectNodetypeByCailiaono(cailiaono);
    }

    @Override
    public List<Map> selectmapList(String jinchangshijian_begin, String jinchangshijian_end, Integer cprule, Integer ycrule) {
        return wztaizhangMapper.selectmapList(jinchangshijian_begin, jinchangshijian_end, cprule, ycrule);
    }

    @Override
    public List<Map> selectByYearList(Integer ycrule, Integer cprule) {
        return wztaizhangMapper.selectByYearList(ycrule, cprule);
    }

    @Override
    public List<Map> selectByProject(String code) {
        return wztaizhangMapper.selectByProject(code);
    }

    @Override
    public WztaizhangKBVO selectNumByCode(String code, String beginTime, String endTime, String cailiaoNo) {
        return wztaizhangMapper.selectNumByCode(code, beginTime, endTime, cailiaoNo);
    }

    @Override
    public List<Map> selectBiaoduanBycode(String code) {
        return wztaizhangMapper.selectBiaoduanBycode(code);
    }

    @Override
    public Double selectCountByPro(String sys_depart_orgcode, String beginTime, String endTime, Integer cprule, Integer ycrule, List<String> stringList) {
        return wztaizhangMapper.selectCountByPro(sys_depart_orgcode, beginTime, endTime, cprule, ycrule, stringList);
    }

    @Override
    public List<Map> selectMonthByCode(String code, String beginTime, String endTime, Integer cprule, Integer ycrule, List<String> stringList) {
        return wztaizhangMapper.selectMonthByCode(code, beginTime, endTime, cprule, ycrule, stringList);
    }

    @Override
    public List<Map> selectMLvByCode(String code, String beginTime, String endTime, Integer cprule, Integer ycrule, List<String> stringList) {
        return wztaizhangMapper.selectMLvByCode(code, beginTime, endTime, cprule, ycrule, stringList);
    }

    @Override
    public Double selectCountByCl(List<String> cailiaoNo, String code, String beginTime, String endTime) {
        return wztaizhangMapper.selectCountByCl(cailiaoNo, code, beginTime, endTime);
    }

    @Override
    public List<JypWztz> queryJypList2(String curdate) {
        return wztaizhangMapper.queryJypList2(curdate);
    }

    @Override
    public IPage<Map> getWzTz(Integer pageNo, Integer pageSize, QueryVo queryVo) {
        if (oConvertUtils.isNotEmpty(queryVo.getZhipaizhuangtai()))
            return weiTuoService.getSyList(queryVo, pageNo, pageSize);

        Page<Map> page = new Page<>(pageNo, pageSize);
        if (oConvertUtils.isNotEmpty(queryVo.getNodeType())) {
            String nodeType = queryVo.getNodeType();
            List<String> caiLiaoNos = weiTuoMapper.getCaiLiaoNo(nodeType);
            queryVo.setCaiLiaoNos(caiLiaoNos);
        }
        IPage<Map> iPage = wztaizhangMapper.getWzTz(page, queryVo);
        return iPage;
    }

    @Override
    public List<String> getNodetypeByOrgCode(String orgCode) {
        return wztaizhangMapper.getNodetypeByOrgCode(orgCode);
    }

    @Override
    public List<String> getNodetypeByWbsid(String wbsId) {
        String orgCode = wztaizhangMapper.getOrgCode(wbsId);
        return wztaizhangMapper.getNodetypeByOrgCode(orgCode);
    }

    @Override
    public String getNodetypeName(String nodetype) {
        String id = wztaizhangMapper.getDictId();
        return wztaizhangMapper.getNodetypeName(id, nodetype);
    }

    @Override
    public double getUses(String pici) {
        List<YclUsageDetail> usesList = wztaizhangMapper.getUses(pici);
        Double sum = 0.0;
        for (YclUsageDetail one : usesList) {
            double uses = Double.parseDouble(one.getUses());
            sum += uses;
        }
        return sum;
    }

    @Override
    public String getWbsOrgCode(String wbsId) {
        return wztaizhangMapper.getWbsOrgCode(wbsId);
    }

    @Override
    public Wztaizhang getState(String inspectionLotNumber) {
        return wztaizhangMapper.getState(inspectionLotNumber);
    }

    @Override
    public List<String> getNode() {
        return wztaizhangMapper.getNode();
    }

    @Override
    public List<YclUsageDetail> getUseageList(String nodeType, String wbsid) {
        return wztaizhangMapper.getUseageList(nodeType, wbsid);
    }

    @Override
    public String selectBydict(String nodetype) {
        return wztaizhangMapper.selectBydict(nodetype);
    }

    @Override
    public List<String> selectByDictValue(String cailiaoName) {
        return wztaizhangMapper.selectByDictValue(cailiaoName);
    }

    @Override
    public List<Map> selectDictList(Integer cprule, Integer ycrule) {
        return wztaizhangMapper.selectDictList(cprule, ycrule);
    }

    @Override
    public List<String> selectByGongyingshang(String gongyingshangdanweibianma) {
        return wztaizhangMapper.selectByGongyingshang(gongyingshangdanweibianma);
    }

    @Override
    public Wztaizhang getByPici(String pici) {
        return wztaizhangMapper.getByPici(pici);
    }

    @Override
    public String getBgfile(String pici) {
        return wztaizhangMapper.getBgfile(pici);
    }

    @Override
    public List<Wztaizhang> SXListYCSD(String ycStr, String sysOrgCode) {
        return wztaizhangMapper.SXListYCSD(ycStr, sysOrgCode);
    }

    @Override
    public List<Wztaizhang> SXBhgListYCSD(String ycStr, String sysOrgCode) {
        return wztaizhangMapper.SXBhgListYCSD(ycStr, sysOrgCode);
    }

    @Override
    public List<Wztaizhang> SXListCPSD(String cpStr, String sysOrgCode) {
        return wztaizhangMapper.SXListCPSD(cpStr, sysOrgCode);
    }

    @Override
    public List<Wztaizhang> SXBhgListCPSD(String cpStr, String sysOrgCode) {
        return wztaizhangMapper.SXBhgListCPSD(cpStr, sysOrgCode);
    }

    @Override
    public List<Wztaizhang> SXListYCLC(String ycStr, String sysOrgCode) {
        return wztaizhangMapper.SXListYCLC(ycStr, sysOrgCode);
    }

    @Override
    public List<Wztaizhang> SXBhgListYCLC(String ycStr, String sysOrgCode) {
        return wztaizhangMapper.SXBhgListYCLC(ycStr, sysOrgCode);
    }

    @Override
    public List<Wztaizhang> SXListCPLC(String cpStr, String sysOrgCode) {
        return wztaizhangMapper.SXListCPLC(cpStr, sysOrgCode);
    }

    @Override
    public List<Wztaizhang> SXBhgListCPLC(String cpStr, String sysOrgCode) {
        return wztaizhangMapper.SXBhgListCPLC(cpStr, sysOrgCode);
    }

    @Override
    public String selectJYL(String sysOrgCode, String nodetype, int i) {
        return wztaizhangMapper.selectJYL(sysOrgCode, nodetype, i);
    }

    @Override
    public String selectJCL(String sysOrgCode, String nodetype) {
        return wztaizhangMapper.selectJCL(sysOrgCode, nodetype);
    }

    @Override
    public String selectUSES(String sysOrgCode, String nodetype) {
        return wztaizhangMapper.selectUSES(sysOrgCode, nodetype);
    }

    @Override
    public List<Wztaizhang> getPiciByStateCailiaoLiaocangGongyingshangNo(Integer jianyanstate, String cailiaono, String lcbianhao, String gongyingshangdanweibianma, String orgCode) {

        List<Wztaizhang> wztaizhangList = wztaizhangMapper.getPiciByStateCailiaoLiaocangGongyingshangNo(jianyanstate, cailiaono, lcbianhao, gongyingshangdanweibianma, orgCode);
        for (Wztaizhang wztaizhang : wztaizhangList) {
            //净重
            String jingzhongt = wztaizhang.getJingzhongt();
            //使用量
            Double usenum = wztaizhang.getUsenum();

        }
        return wztaizhangList;
    }

    @Override
    public List<Wztaizhang> getListzlpz(String shebeilist) {
        return wztaizhangMapper.getListzlpz(shebeilist);
    }

    @Override
    public List<Wztaizhang> selectRWDList(String sysOrgCode, Integer curid) {
        try {
            QueryWrapper<Wztaizhang> queryWrapper = new QueryWrapper<>();
            queryWrapper.likeRight("sys_org_code", sysOrgCode);
            queryWrapper.gt("id", curid);
            queryWrapper.ne("jingzhongt",0);
            queryWrapper.last("limit 200");
            return this.list(queryWrapper);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

















