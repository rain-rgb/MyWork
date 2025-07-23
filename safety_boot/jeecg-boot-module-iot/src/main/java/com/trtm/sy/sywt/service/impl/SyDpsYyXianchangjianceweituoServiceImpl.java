package com.trtm.sy.sywt.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.trtm.sy.sydpssysample.mapper.SyDpsSySampleMapper;
import com.trtm.sy.syjcxm.entity.SyDpsYyJcxm;
import com.trtm.sy.syjcxm.mapper.SyDpsYyJcxmMapper;
import com.trtm.sy.sylxdps.entity.SyDpsJcTestitemtype;
import com.trtm.sy.sylxdps.mapper.SyDpsJcTestitemtypeMapper;
import com.trtm.sy.syrules.entity.SyCodingRules;
import com.trtm.sy.syrules.mapper.SyCodingRulesMapper;
import com.trtm.sy.sywt.entity.SyDpsYyXianchangjianceweituo;
import com.trtm.sy.sywt.entity.XcWtRequest;
import com.trtm.sy.sywt.entity.XcWtResponse;
import com.trtm.sy.sywt.mapper.SyDpsYyXianchangjianceweituoMapper;
import com.trtm.sy.sywt.service.ISyDpsYyXianchangjianceweituoService;
import me.zhyd.oauth.utils.UuidUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.DateUtils;
import org.jeecg.common.util.ShtooneUtil;
import org.jeecg.common.util.oConvertUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.util.Map;

/**
 * @Description: sy_dps_yy_xianchangjianceweituo
 * @Author: jeecg-boot
 * @Date: 2023-06-28
 * @Version: V1.0
 */
@Service
public class SyDpsYyXianchangjianceweituoServiceImpl extends ServiceImpl<SyDpsYyXianchangjianceweituoMapper, SyDpsYyXianchangjianceweituo> implements ISyDpsYyXianchangjianceweituoService {

    @Autowired
    private SyDpsYyXianchangjianceweituoMapper weiTuoMapper;
    @Autowired
    private SyDpsJcTestitemtypeMapper jcTestitemtypeMapper;
    @Autowired
    private SyDpsYyJcxmMapper jcxmMapper;
    @Autowired
    private SyCodingRulesMapper rulesMapper;
    @Autowired
    private SyDpsSySampleMapper sampleMapper;


    @Override
    @Transactional
    public void saveWeiTuoDan(SyDpsYyXianchangjianceweituo xianchangjianceweituo) throws ParseException {
        LoginUser user = (LoginUser) SecurityUtils.getSubject().getPrincipal();
//        SysDepart sysDepart = weiTuoMapper.getDepartByUserOrgCode(user.getOrgCode());
        SyCodingRules rule = sampleMapper.selectDepartRuleByUserOrgCode(user.getOrgCode());

//        SyCodingRules rules = rulesMapper.selectOne(new QueryWrapper<SyCodingRules>().lambda().eq(SyCodingRules::getDepartId, sysDepart.getId()));

//        if (oConvertUtils.isNotEmpty(xianchangjianceweituo.getId())) {
//            xianchangjianceweituo = weiTuoMapper.selectById(xianchangjianceweituo.getId());
//        } else {

        if (oConvertUtils.isEmpty(xianchangjianceweituo.getId())) {
            String weituodanbianhao = getBH(rule.getWeituoTableCodingRules(), rule,
                    "sy_dps_yy_xianchangjianceweituo", "weituodanbianhao", xianchangjianceweituo.getTitCode());
            xianchangjianceweituo.setWeituodanbianhao(weituodanbianhao);
//            xianchangjianceweituo.setJianglidanwei(sysDepart.getParentLaboratoryName());
//            xianchangjianceweituo.setJianlidanweihetonghao(sysDepart.getContractNumber());
//            xianchangjianceweituo.setJianlidanweidepartid(sysDepart.getId());
//            xianchangjianceweituo.setJianlidanweiorgcode(sysDepart.getOrgCode());
//            xianchangjianceweituo.setWeituodanwei("温州筑诚交通工程监理有限公司");
        }
        this.saveOrUpdate(xianchangjianceweituo);

        if (oConvertUtils.isNotEmpty(xianchangjianceweituo.getJiancexiangmu())) {
            SyDpsYyJcxm jcxm = new SyDpsYyJcxm();
            jcxm.setJiancexiangmu(xianchangjianceweituo.getJiancexiangmu());
            jcxm.setDeparid(user.getOrgCode());
            jcxm.setUserid(user.getId());
            jcxm.setData(DateUtils.getDate("yyyy-MM-dd"));
            jcxmMapper.insert(jcxm);
        }
    }


    @Override
    public String getBH(String bhgz, SyCodingRules rules, String table, String liushuihao, String titCode) throws ParseException {
        SyDpsJcTestitemtype testItemType = jcTestitemtypeMapper.selectOne(new QueryWrapper<SyDpsJcTestitemtype>().lambda().eq(SyDpsJcTestitemtype::getTitcode, titCode));
        String TitSampleMark = "";
        if (oConvertUtils.isNotEmpty(testItemType)) {
            TitSampleMark = testItemType.getTitsamplemark();
        }
        // 获取编号规则
        String bh = ShtooneUtil.getCoding(bhgz, rules.getCodingDateFormat(), rules.getCodingFlowNumerDigit(),
                "NoFlowNumber",
                oConvertUtils.isNotEmpty(rules.getContractNumberCode()) ? rules.getContractNumberCode()
                        : rules.getContractNumber(),
                TitSampleMark, DateUtils.getDate("yyyy-MM-dd"),
                "");
        // 查看流水号
        Map<String, Object> current = weiTuoMapper.getCodingFlowNumber(bh);
        String currentCode = "";
        if (oConvertUtils.isNotEmpty(current)) {
            // 获取编号数量
            Long count = weiTuoMapper.getCountByTable(table, liushuihao, bh);
            Integer c = Integer.valueOf(current.get("currentCode").toString());
            if ("sy_dps_yy_xianchangjianceweituo".equals(table)) {
                Long count2 = weiTuoMapper.getCountForYcQyWt(liushuihao, bh);
                count = count + count2;
            }
            currentCode = ShtooneUtil.getFlowNumber(Integer.valueOf(rules.getCodingFlowNumerDigit()),
                    current.get("currentCode").toString());
            weiTuoMapper.updateCodeById(currentCode, current.get("id").toString());
            currentCode = current.get("currentCode").toString();
        } else {
            currentCode = ShtooneUtil.getFlowNumber(Integer.valueOf(rules.getCodingFlowNumerDigit()), "");
            weiTuoMapper.insertCodingFlowNumber(UuidUtils.getUUID(), currentCode, bh);
            currentCode = "";
        }
        // 生成编号
        bh = ShtooneUtil.getCoding(bhgz, rules.getCodingDateFormat(), rules.getCodingFlowNumerDigit(),
                currentCode,
                oConvertUtils.isNotEmpty(rules.getContractNumberCode()) ? rules.getContractNumberCode()
                        : rules.getContractNumber(), TitSampleMark, DateUtils.getDate("yyyy-MM-dd"), "");
        return bh;
    }

    @Override
    public IPage<XcWtResponse> getListXcWt(XcWtRequest request) {
        if (oConvertUtils.isEmpty(request.getSys_depart_orgcode())) {
            LoginUser user = (LoginUser) SecurityUtils.getSubject().getPrincipal();
            request.setSys_depart_orgcode(user.getOrgCode());
        }
        Page<XcWtResponse> page = new Page<>(request.getPageNo(), request.getPageSize());
        Page<XcWtResponse> result = this.baseMapper.getListXcWt(request, page);
        return result;
    }

    @Override
    public Map<String, Object> getXcWtById(Integer id) {
        return this.baseMapper.getXcWtById(id);
    }
}
