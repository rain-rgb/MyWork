package com.trtm.sy.sydpssysample.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.http.HttpRequest;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.utils.PdfMerger;
import com.trtm.iot.hntconsign.entity.HntConsign;
import com.trtm.iot.sydpsqzlc.entity.DpsSyQianzhangliucheng;
import com.trtm.iot.sydpsqzlc.service.IDpsSyQianzhangliuchengService;
import com.trtm.iot.sydpsshenpirizhi.entity.DpsSyShenpirizhi;
import com.trtm.iot.sydpsshenpirizhi.service.IDpsSyShenpirizhiService;
import com.trtm.iot.syj.entity.TSyjzb;
import com.trtm.iot.syj.service.ITSyjzbService;
import com.trtm.iot.syycjcdj.entity.SyDpsYyYuancaijinchangdengji;
import com.trtm.iot.syycjcdj.service.ISyDpsYyYuancaijinchangdengjiService;
import com.trtm.iot.wztaizhang.entity.Wztaizhang;
import com.trtm.iot.wztaizhang.service.IWztaizhangService;
import com.trtm.sy.sydpsshebeisyjl.entity.SyDpsJcShebeiShiyongjilu;
import com.trtm.sy.sydpsshebeisyjl.service.ISyDpsJcShebeiShiyongjiluService;
import com.trtm.sy.sydpssysample.entity.*;
import com.trtm.sy.sydpssysample.mapper.SyDpsSySampleMapper;
import com.trtm.sy.sydpssysample.service.ISyDpsSySampleService;
import com.trtm.sy.sydpssysample.service.ISyDpsSySampleWtService;
import com.trtm.sy.sydpssysample.service.ISyDpsSySamplepicService;
import com.trtm.sy.sydpssysample.service.ISyDpsSySampleqrcodeService;
import com.trtm.sy.sydpssysignature.service.ISyDpsSySignatureService;
import com.trtm.sy.syjlbentity.*;
import com.trtm.sy.sylxdps.entity.*;
import com.trtm.sy.sylxdps.service.ISyDpsSyReportMService;
import com.trtm.sy.sylxdps.service.ISyDpsSyReportSService;
import com.trtm.sy.sylxdps.service.ISyDpsSyTableheaderService;
import com.trtm.sy.syrules.entity.SyCodingRules;
import com.trtm.sy.sywt.entity.SyDpsYyXianchangjianceweituo;
import com.trtm.sy.sywt.service.ISyDpsYyXianchangjianceweituoService;
import com.trtm.sy.wtgl.qywtd.entity.SyDpsYyYuancaiquyangweituo;
import com.trtm.sy.wtgl.qywtd.service.ISyDpsYyYuancaiquyangweituoService;
import com.trtm.sy.wtgl.rwd.entity.SyDpsYyRenwudan;
import com.trtm.sy.wtgl.rwd.service.ISyDpsYyRenwudanService;
import com.xkcoding.http.util.StringUtil;
import io.swagger.models.auth.In;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.exception.JeecgBootException;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.DateUtils;
import org.jeecg.common.util.ShtooneUtil;
import org.jeecg.common.util.oConvertUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Description: sy_dps_sy_sample
 * @Author: jeecg-boot
 * @Date: 2023-01-12
 * @Version: V1.0
 */
@Service
public class SyDpsSySampleServiceImpl extends ServiceImpl<SyDpsSySampleMapper, SyDpsSySample> implements ISyDpsSySampleService {

    @Autowired
    private SyDpsSySampleMapper sampleMapper;
    @Autowired
    private ISyDpsSySampleqrcodeService dpsSySampleqrcodeService;
    @Autowired
    private ISyDpsSySamplepicService samplepicService;
    @Autowired
    private ISyDpsSyReportMService reportMService;
    @Autowired
    private ISyDpsSyReportSService reportSService;
    @Autowired
    private ISyDpsSyTableheaderService tableheaderService;
    @Autowired
    private ISyDpsSySampleWtService sampleWtService;
    @Autowired
    private ISyDpsYyYuancaijinchangdengjiService syDpsYyYuancaijinchangdengjiService;
    @Autowired
    private ISyDpsYyXianchangjianceweituoService xianchangjianceweituoService;
    @Autowired
    private ISyDpsYyYuancaiquyangweituoService yuancaiquyangweituoService;
    @Autowired
    private ISyDpsYyRenwudanService renwudanService;
    @Autowired
    private ISyDpsJcShebeiShiyongjiluService shebeiShiyongjiluService;
    @Autowired
    private IDpsSyShenpirizhiService shenpirizhiService;
    @Autowired
    private IDpsSyQianzhangliuchengService qianzhangliuchengService;
    @Autowired
    private ISyDpsSySignatureService syDpsSySignatureService;
    @Autowired
    private ITSyjzbService itSyjzbService;

    @Autowired
    private IWztaizhangService wztaizhangService;
    @Value("${sys.datasource}")
    private String dataSource;
    @Value("${sys.uploadFile.pdfPath}")
    private String pdfUrl;
    @Value("${sys.wkhtmltopdf}")
    private String cmd;
    @Value("${sys.uploadFile.htmlPath}")
    private String htmlPath;


    @Override
    public String selectProjNames(String orgCode) {
        String projNames = sampleMapper.selectProjNames(orgCode);
        return projNames;
    }

    @Override
    public SyDpsSySample getSampleByWtbh(String wtbh) {
        QueryWrapper<SyDpsSySample> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("wtbh",wtbh);
        return this.getOne(queryWrapper);
    }

    SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    @Transactional
    public void insert(HashMap map) {
        map.remove("sampleNo");
        map.remove("reportNo");
        map.remove("tableNumber");
        map.remove("tiNo");
        Map<String, Object> s = sampleMapper.selectReportSByUuid(map.get("id").toString());
        String sampleNo = s.get("sampleNo").toString();
        SyDpsSySample sample = sampleMapper.selectSampleByNo(sampleNo);
        Map<String, Object> titType = sampleMapper.selectItemTypeByTitCode(sample.getTitcode());
        Map<String, Object> bgno = null;
        if (s.get("tiType").toString().equals("0")) {
            sampleMapper.updateSampleStatusByNo(sampleNo);
            bgno = sampleMapper.selectReportSByNo(sampleNo);
            Map bg = (Map) map.get("bg");
            List<String> bglist = new ArrayList<>();
            for (Object obj : bg.values()) {
                bglist.add(obj.toString());
            }
            String tiNo = bgno.get("tiNo").toString();
            SyDpsJcTestitem testItem = sampleMapper.selectTestItemByTino(s.get("tiNo").toString());
            if (bglist.size() > 0) {
                if (testItem.getPlusmark() == 1) {
                    List<String> list = sampleMapper.findSySColumns(dataSource, bgno.get("tiNo").toString());
                    StringBuilder stringOne = new StringBuilder();
                    for (int i = 0; i < list.size(); i++) {
                        String key = list.get(i);
//                        String key = columns.get("name").toString();
                        if (key.equals("id")) {
                            continue;
                        }
                        if (bglist.indexOf(key) >= 0) {
                            Set<String> set = bg.keySet();
                            for (String bgkey : set) {
                                if (bg.get(bgkey).toString().equals(key) && map.containsKey(bgkey)) {
                                    String value;
                                    if (oConvertUtils.isEmpty(map.get(bgkey))) {
                                        value = "/";
                                    } else {
                                        value = map.get(bgkey).toString();
                                        if (StringUtil.isEmpty(value)) {
                                            value = "/";
                                        } else if ("NaN".equals(value)) {
                                            value = "/";
                                        }
                                    }
                                    stringOne.append(key + "=" + "'" + value + "'" + ",");
//                                    sampleMapper.updateTiNo(tiNo, key, value, s.get("sampleNo").toString(), s.get("tiNoTemp").toString());
                                }
                            }
                        }
                    }
                    if (stringOne.length() > 0) {
                        sampleMapper.updateByTiNo(tiNo, stringOne.substring(0, stringOne.length() - 1), s.get("sampleNo").toString(), s.get("tiNoTemp").toString());
                    }
                    sampleMapper.findSySColumns(dataSource, "sy_dps_sy_tableHeader");
                    StringBuilder stringTwo = new StringBuilder();
                    for (int i = 0; i < list.size(); i++) {
                        String key = list.get(i);
//                        String key = columns.get("name").toString();
                        if (key.equals("id")) {
                            continue;
                        }
                        if (bglist.indexOf(key) >= 0) {
                            Set<String> set = bg.keySet();
                            for (String bgkey : set) {
                                if (bg.get(bgkey).toString().equals(key) && map.containsKey(bgkey)) {
                                    String value;
                                    if (oConvertUtils.isEmpty(map.get(bgkey))) {
                                        value = "/";
                                    } else {
                                        value = map.get(bgkey).toString();
                                        if (StringUtil.isEmpty(value)) {
                                            value = "/";
                                        } else if ("NaN".equals(value)) {
                                            value = "/";
                                        }
                                    }
                                    stringTwo.append(key + "=" + "'" + value + "'" + ",");
//                                    sampleMapper.updateTiNoBy(key, value, s.get("sampleNo").toString(), tiNo);
                                }
                            }
                        }
                    }
                    if (stringTwo.length() > 0) {
                        sampleMapper.updateTiNoBySql(stringTwo.substring(0, stringTwo.length() - 1), s.get("sampleNo").toString(), tiNo);
                    }
                } else {
                    List<String> list = sampleMapper.findSySColumns(dataSource, tiNo);
                    StringBuilder stringThree = new StringBuilder();
                    for (int i = 0; i < list.size(); i++) {
                        String key = list.get(i);
//                        String key = columns.get("name").toString();
                        if (key.equals("id")) {
                            continue;
                        }
                        if (bglist.indexOf(key) >= 0) {
                            Set<String> set = bg.keySet();
                            for (String bgkey : set) {
                                if (bg.get(bgkey).toString().equals(key) && map.containsKey(bgkey)) {
                                    String value;
                                    if (oConvertUtils.isEmpty(map.get(bgkey))) {
                                        value = "/";
                                    } else {
                                        value = map.get(bgkey).toString();
                                        if (StringUtil.isEmpty(value)) {
                                            value = "/";
                                        } else if ("NaN".equals(value)) {
                                            value = "/";
                                        }
                                    }
                                    stringThree.append(key + "=" + "'" + value + "'" + ",");
//                                    sampleMapper.updateTiNo(tiNo, key, value, s.get("sampleNo").toString(), "1");
                                }
                            }
                        }
                    }
                    if (stringThree.length() > 0) {
                        sampleMapper.updateTiNoSql(tiNo, stringThree.substring(0, stringThree.length() - 1), s.get("sampleNo").toString(), "1");
                    }
                    list = sampleMapper.findSySColumns(dataSource, "sy_dps_sy_tableHeader");
                    StringBuilder stringFour = new StringBuilder();
                    for (int i = 0; i < list.size(); i++) {
                        String key = list.get(i);
//                        String key = columns.get("name").toString();
                        if (key.equals("id")) {
                            continue;
                        }
                        if (bglist.indexOf(key) >= 0) {
                            Set<String> set = bg.keySet();
                            for (String bgkey : set) {
                                if (bg.get(bgkey).toString().equals(key) && map.containsKey(bgkey)) {
                                    String value;
                                    if (oConvertUtils.isEmpty(map.get(bgkey))) {
                                        value = "/";
                                    } else {
                                        value = map.get(bgkey).toString();
                                        if (StringUtil.isEmpty(value)) {
                                            value = "/";
                                        } else if ("NaN".equals(value)) {
                                            value = "/";
                                        }
                                    }
                                    stringFour.append(key + "=" + "'" + value + "'" + ",");
//                                    sampleMapper.updateByTiNoTemp(key, value, s.get("sampleNo").toString(), tiNo);
                                }
                            }
                        }
                    }
                    if (stringFour.length() > 0) {
                        sampleMapper.updateByTiNoTempSql(stringFour.substring(0, stringFour.length() - 1), s.get("sampleNo").toString(), tiNo);
                    }
                }
            }
            Map hz = (Map) map.get("hz");
            List<String> hzlist = new ArrayList<String>();
            for (Object obj : hz.values()) {
//                String[] objs = obj.toString().split(",");
//                for (String string : objs) {
                hzlist.add(obj.toString());
//                }
            }
            if (hzlist.size() > 0) {
                // 级配碎石的cbr
                if (hz.get("JPSS") != null) {
                    tiNo = "JJ0105d1";
                    List<Map<String, Object>> listid = sampleMapper.findIdBySampleNo(s.get("sampleNo").toString());
                    List<String> list = sampleMapper.findSySColumns(dataSource, "JJ0105d1");
                    if (listid.size() > 0) {
                        for (int i = 0; i < list.size(); i++) {
                            String key = list.get(i);
//                            String key = columns.get("name").toString();
                            String keyk = key.substring(0, key.length() - 1);
                            if (hzlist.indexOf(keyk) >= 0) {
                                Set<String> set = hz.keySet();
                                for (String hzkey : set) {
                                    if (hzkey.equalsIgnoreCase(keyk)) {
                                        String hzname = "";
                                        if (hzkey.equals("sampleNo") || hzkey.equals("tableNumber")
                                                || hzkey.equals("reportNo") || hzkey.equals("departId")
                                                || hzkey.equals("tiNo")) {
                                            hzname = hz.get(hzkey).toString();
                                        } else {
                                            if (s.get("tiNo").toString().equalsIgnoreCase("JJ0105a1")) {
                                                hzname = hz.get(hzkey) + "1";
                                            } else if (s.get("tiNo").toString().equalsIgnoreCase("JJ0105b1")) {
                                                hzname = hz.get(hzkey) + "2";
                                            } else if (s.get("tiNo").toString().equalsIgnoreCase("JJ0105c1")) {
                                                hzname = hz.get(hzkey) + "3";
                                            } else if (s.get("tiNo").toString().equalsIgnoreCase("JJ01031")) {
                                                hzname = hz.get(hzkey) + "1";
                                            }
                                        }
                                        if (hzname.equals(key) && map.containsKey(hzkey)) {
                                            if (hzkey.equals("tiNoTemp")) {
                                                sampleMapper.updateTiNoTempBySample(tiNo, "1", key, "0", s.get("sampleNo").toString());
                                            } else {
                                                String value;
                                                if (oConvertUtils.isEmpty(map.get(hzkey))) {
                                                    value = "/";
                                                } else {
                                                    value = map.get(hzkey).toString();
                                                    if (StringUtil.isEmpty(value)) {
                                                        value = "/";
                                                    } else if ("NaN".equals(value)) {
                                                        value = "/";
                                                    }
                                                }
                                                sampleMapper.updateTiNoTempBySample(tiNo, "1", key, value, s.get("sampleNo").toString());
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        String ziduanmings = "";
                        List<String> values = new ArrayList<>();
                        for (int i = 0; i < list.size(); i++) {
                            String key = list.get(i);
//                            String key = columns.get("name").toString();
                            if (key.equals("id")) {
                                ziduanmings += " id,sampleNo,tiNoTemp,";
                                values.add(UUID.randomUUID().toString());
//                                String sampleNo = s.get("sampleNo").toString();
                                values.add(sampleNo);
                                values.add("1");
                            }
                            String keyk = key.substring(0, key.length() - 1);
                            if (hzlist.indexOf(keyk) >= 0) {
                                Set<String> set = hz.keySet();
                                for (String hzkey : set) {
                                    String hzname = "";
                                    if (hzkey.equals("sampleNo") || hzkey.equals("tableNumber")
                                            || hzkey.equals("reportNo") || hzkey.equals("departId")
                                            || hzkey.equals("tiNo") || hzkey.equals("tiNoTemp")) {
                                        hzname = hz.get(hzkey).toString();
                                    } else {
                                        if (s.get("tiNo").toString().equalsIgnoreCase("JJ0105a1")) {
                                            hzname = hz.get(hzkey) + "1";
                                        } else if (s.get("tiNo").toString().equalsIgnoreCase("JJ0105b1")) {
                                            hzname = hz.get(hzkey) + "2";
                                        } else if (s.get("tiNo").toString().equalsIgnoreCase("JJ0105c1")) {
                                            hzname = hz.get(hzkey) + "3";
                                        } else if (s.get("tiNo").toString().equalsIgnoreCase("JJ01031")) {
                                            hzname = hz.get(hzkey) + "1";
                                        }
                                    }
                                    if (hzname.equals(key) && map.containsKey(hzkey)) {
                                        ziduanmings += key + ",";
                                        String value;
                                        if (oConvertUtils.isEmpty(map.get(hzkey))) {
                                            value = "/";
                                        } else {
                                            value = map.get(hzkey).toString();
                                            if (StringUtil.isEmpty(value)) {
                                                value = "/";
                                            } else if ("NaN".equals(value)) {
                                                value = "/";
                                            }
                                        }
                                        values.add(value);
                                    }
                                }
                            }
                        }
                        if (ziduanmings.length() > 0) {
                            ziduanmings = ziduanmings.substring(0, ziduanmings.length() - 1);
                        }

                        sampleMapper.insertSql(tiNo, ziduanmings, values);
                    }
                } else {
                    tiNo = "JJ0105d";
                    List<Map<String, Object>> listid = sampleMapper.queryBySampleNo(s.get("sampleNo").toString());
                    List<String> list = sampleMapper.findSySColumns(dataSource, "JJ0105d");
                    if (listid.size() > 0) {
                        for (int i = 0; i < list.size(); i++) {
                            String key = list.get(i);
//                            String key = columns.get("name").toString();
                            String keyk = key.substring(0, key.length() - 1);
                            if (hzlist.indexOf(keyk) >= 0) {
                                Set<String> set = hz.keySet();
                                for (String hzkey : set) {
                                    if (hzkey.equalsIgnoreCase(keyk)) {
                                        String hzname = "";
                                        if (hzkey.equals("sampleNo") || hzkey.equals("tableNumber")
                                                || hzkey.equals("reportNo") || hzkey.equals("departId")
                                                || hzkey.equals("tiNo")) {
                                            hzname = hz.get(hzkey).toString();
                                        } else {
                                            if (s.get("tiNo").toString().equalsIgnoreCase("JJ0105a")) {
                                                hzname = hz.get(hzkey) + "1";
                                            } else if (s.get("tiNo").toString().equalsIgnoreCase("JJ0105b")) {
                                                hzname = hz.get(hzkey) + "2";
                                            } else if (s.get("tiNo").toString().equalsIgnoreCase("JJ0105c")) {
                                                hzname = hz.get(hzkey) + "3";
                                            } else if (s.get("tiNo").toString().equalsIgnoreCase("JJ0103")) {
                                                hzname = hz.get(hzkey) + "1";
                                            }
                                        }
                                        // if (bg.get(bgkey).toString().equals(key) && map.containsKey(bgkey)) {
                                        if (hzname.equals(key) && map.containsKey(hzkey)) {
                                            if (hzkey.equals("tiNoTemp")) {
                                                sampleMapper.updateTiNoTempBySample(tiNo, "1", key, "0", s.get("sampleNo").toString());
                                            } else {
                                                String value;
                                                if (oConvertUtils.isEmpty(map.get(hzkey))) {
                                                    value = "/";
                                                } else {
                                                    value = map.get(hzkey).toString();
                                                    if (StringUtil.isEmpty(value)) {
                                                        value = "/";
                                                    } else if ("NaN".equals(value)) {
                                                        value = "/";
                                                    }
                                                }
                                                sampleMapper.updateTiNoTempBySample(tiNo, "1", key, value, s.get("sampleNo").toString());
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        String ziduanmings = "";
                        List<String> values = new ArrayList<>();
                        for (int i = 0; i < list.size(); i++) {
                            String key = list.get(i);
//                            String key = columns.get("name").toString();
                            if (key.equals("id")) {
                                ziduanmings += " id,sampleNo,tiNoTemp,";
                                values.add(UUID.randomUUID().toString());
//                                String sampleNo = s.get("sampleNo").toString();
                                values.add(sampleNo);
                                values.add("1");
                            }
                            String keyk = key.substring(0, key.length() - 1);
                            if (hzlist.indexOf(keyk) >= 0) {
                                Set<String> set = hz.keySet();
                                for (String hzkey : set) {
                                    String hzname = "";
                                    if (hzkey.equals("sampleNo") || hzkey.equals("tableNumber")
                                            || hzkey.equals("reportNo") || hzkey.equals("departId")
                                            || hzkey.equals("tiNo") || hzkey.equals("tiNoTemp")) {
                                        hzname = hz.get(hzkey).toString();
                                    } else {
                                        if (s.get("tiNo").toString().equalsIgnoreCase("JJ0105a")) {
                                            hzname = hz.get(hzkey) + "1";
                                        } else if (s.get("tiNo").toString().equalsIgnoreCase("JJ0105b")) {
                                            hzname = hz.get(hzkey) + "2";
                                        } else if (s.get("tiNo").toString().equalsIgnoreCase("JJ0105c")) {
                                            hzname = hz.get(hzkey) + "3";
                                        } else if (s.get("tiNo").toString().equalsIgnoreCase("JJ0103")) {
                                            hzname = hz.get(hzkey) + "1";
                                        }
                                    }
                                    if (hzname.equals(key) && map.containsKey(hzkey)) {
                                        ziduanmings += key + ",";
                                        String value;
                                        if (oConvertUtils.isEmpty(map.get(hzkey))) {
                                            value = "/";
                                        } else {
                                            value = map.get(hzkey).toString();
                                            if (StringUtil.isEmpty(value)) {
                                                value = "/";
                                            } else if ("NaN".equals(value)) {
                                                value = "/";
                                            }
                                        }
                                        values.add(value);
                                    }
                                }
                            }
                        }
                        if (ziduanmings.length() > 0) {
                            ziduanmings = ziduanmings.substring(0, ziduanmings.length() - 1);
                        }

                        sampleMapper.insertSql(tiNo, ziduanmings, values);
                    }
                }
            }
            if ("JJ0103".equals(s.get("tiNo"))) {
                String zuijhansl1 = map.get("zuijhansl1").toString();
                String zuidganmid1 = map.get("zuidganmid1").toString();
                String xiuzzuijhansl1 = map.get("xiuzzuijhansl1").toString();
                String xiuzzuidganmid1 = map.get("xiuzzuidganmid1").toString();
                if (xiuzzuijhansl1.equals("") && xiuzzuidganmid1.equals("")) {
                    this.dynamicRevise("JJ0105a", "zuijiahansl", zuijhansl1, "zuidaganmd", zuidganmid1, "sampleNo", s.get("sampleNo").toString());
                } else {
                    this.dynamicRevise("JJ0105a", "zuijiahansl", xiuzzuijhansl1, "zuidaganmd", xiuzzuidganmid1, "sampleNo", s.get("sampleNo").toString());
                }
                if (xiuzzuijhansl1.equals("") && xiuzzuidganmid1.equals("")) {
                    this.dynamicRevise("JJ0105b", "zuijiahansl", zuijhansl1, "zuidaganmd", zuidganmid1, "sampleNo", s.get("sampleNo").toString());
                } else {
                    this.dynamicRevise("JJ0105b", "zuijiahansl", xiuzzuijhansl1, "zuidaganmd", xiuzzuidganmid1, "sampleNo", s.get("sampleNo").toString());
                }
                if (xiuzzuijhansl1.equals("") && xiuzzuidganmid1.equals("")) {
                    this.dynamicRevise("JJ0105c", "zuijiahansl", zuijhansl1, "zuidaganmd", zuidganmid1, "sampleNo", s.get("sampleNo").toString());
                } else {
                    this.dynamicRevise("JJ0105c", "zuijiahansl", xiuzzuijhansl1, "zuidaganmd", xiuzzuidganmid1, "sampleNo", s.get("sampleNo").toString());
                }
            } else if ("JJ01031".equals(s.get("tiNo"))) {// 级配碎石
                String zuijhansl1 = map.get("zuijhansl1").toString();
                String zuidganmid1 = map.get("zuidganmid1").toString();
                String xiuzzuijhansl1 = map.get("xiuzzuijhansl1").toString();
                String xiuzzuidganmid1 = map.get("xiuzzuidganmid1").toString();
                if (xiuzzuijhansl1.equals("") && xiuzzuidganmid1.equals("")) {
                    this.dynamicRevise("JJ0105a1", "zuijiahansl", zuijhansl1, "zuidaganmd", zuidganmid1, "sampleNo", s.get("sampleNo").toString());
                } else {
                    this.dynamicRevise("JJ0105a1", "zuijiahansl", xiuzzuijhansl1, "zuidaganmd", xiuzzuidganmid1, "sampleNo", s.get("sampleNo").toString());
                }
                if (xiuzzuijhansl1.equals("") && xiuzzuidganmid1.equals("")) {
                    this.dynamicRevise("JJ0105b1", "zuijiahansl", zuijhansl1, "zuidaganmd", zuidganmid1, "sampleNo", s.get("sampleNo").toString());
                } else {
                    this.dynamicRevise("JJ0105b1", "zuijiahansl", xiuzzuijhansl1, "zuidaganmd", xiuzzuidganmid1, "sampleNo", s.get("sampleNo").toString());
                }
                if (xiuzzuijhansl1.equals("") && xiuzzuidganmid1.equals("")) {
                    this.dynamicRevise("JJ0105c1", "zuijiahansl", zuijhansl1, "zuidaganmd", zuidganmid1, "sampleNo", s.get("sampleNo").toString());
                } else {
                    this.dynamicRevise("JJ0105c1", "zuijiahansl", xiuzzuijhansl1, "zuidaganmd", xiuzzuidganmid1, "sampleNo", s.get("sampleNo").toString());
                }
            }
            if ("JJ1401".equals(s.get("tiNo"))) {
                double total = 0;
                int cedianshu = 0;// 测点数
                double pingjunzhi = 0;// 平均值
                double pingjunzhizh = 0;// 平均值总和
                double biaozhuncha = 0;// 标准差
                double bianyixishu = 0;// 变异系数
                double shicedaibiaozhi = 0;// 实测代表值
                List<String> list1 = new ArrayList<String>();
                List<Map<String, Object>> datas = sampleMapper.querySql1(s.get("sampleNo").toString());
                for (int i = 0; i < datas.size(); i++) {
                    Map<String, Object> map2 = datas.get(i);
                    for (int j = 1; j <= 16; j++) {
                        String k = j + "";
                        if (j < 10) {
                            k = "0" + j;
                        }
                        if (map2.get("sid").equals(map.get("id"))) {
                            if (StringUtil.isNotEmpty(map.get("zhuangh" + k).toString())
                                    && !(map.get("zhuangh" + k).equals("/"))) {
//									cedianshu++;
                            }
                            if (StringUtil.isNotEmpty(map.get("pingjunz" + k).toString())
                                    && !(map.get("pingjunz" + k).equals("/"))) {
                                cedianshu++;
                                pingjunzhizh += Double.valueOf(map.get("pingjunz" + k).toString());
                                list1.add(map.get("pingjunz" + k).toString());
                            }
                        } else {
                            if (StringUtil.isNotEmpty(map2.get("zhuangh" + k).toString())
                                    && !(map2.get("zhuangh" + k).equals("/"))) {
//									cedianshu++;
                            }
                            if (StringUtil.isNotEmpty(map2.get("pingjunz" + k).toString())
                                    && !(map2.get("pingjunz" + k).equals("/"))) {
                                cedianshu++;
                                pingjunzhizh += Double.valueOf(map2.get("pingjunz" + k).toString());
                                list1.add(map2.get("pingjunz" + k).toString());
                            }
                        }
                    }
                }
                if (datas.size() > 0) {
                    if (cedianshu > 0) {
                        pingjunzhi = pingjunzhizh / cedianshu;// 平均值
                        String pingjunzhixy = sciCal(pingjunzhi, 1);
                        pingjunzhi = new Double(pingjunzhixy);
                    }
                    for (int i = 0; i < list1.size(); i++) {
                        String sum = list1.get(i);
                        double num = Double.valueOf(sum);
                        total += (num - pingjunzhi) * (num - pingjunzhi);
                    }
                    if (list1.size() > 0) {
                        biaozhuncha = Math.sqrt(total / (list1.size() - 1));// 标准差
                    }
                }
                if (cedianshu > 0) {
                    double bianyixs = Double.parseDouble(String.format("%.3f", biaozhuncha / pingjunzhi));
                    Map<String, Object> m = sampleMapper.querySql2(cedianshu);
                    double tgn = 0;
//                    System.out.println(map.get("baozl").toString());
                    if (map.get("baozl") != null && !map.get("baozl").equals("")) {
                        if (m != null) {
                            tgn = Double.parseDouble(m.get(map.get("baozl").toString()).toString());
                        }
                    }
                    double daibz = Double.parseDouble(String.format("%.3f", pingjunzhi - (biaozhuncha * tgn)));
                    // 保存
                    sampleMapper.updateSql7(cedianshu, FourSSixRFiveCS(pingjunzhi, 1),
                            FourSSixRFiveCS(biaozhuncha, 1),
                            FourSSixRFiveCS(bianyixs, 1),
                            FourSSixRFiveCS(daibz, 1), s.get("sampleNo").toString());
                }
            }
            if ("JJ0105a".equals(s.get("tiNo")) || "JJ0105b".equals(s.get("tiNo")) || "JJ0105c".equals(s.get("tiNo"))) {
                sampleMapper.updateSql8(oConvertUtils.isEmpty(map.get("yiqishebei")) ? "" : map.get("yiqishebei").toString(),
                        s.get("sampleNo").toString(),
                        "'JJ0105a','JJ0105b','JJ0105c'");
            } // 级配碎石
            else if ("JJ0105a1".equals(s.get("tiNo")) || "JJ0105b1".equals(s.get("tiNo")) || "JJ0105c1".equals(s.get("tiNo"))) {
                sampleMapper.updateSql8(oConvertUtils.isEmpty(map.get("yiqishebei")) ? "" : map.get("yiqishebei").toString(),
                        s.get("sampleNo").toString(),
                        "'JJ0105a1','JJ0105b1','JJ0105c1'");
            }
            if ("JJ0105a".equals(s.get("tiNo"))) {
                this.dynamicRevise("JJ0105d", "CBR25avg1", oConvertUtils.isEmpty(map.get("CBR25avg1")) ? "" : map.get("CBR25avg1").toString(),
                        "CBR50avg1", oConvertUtils.isEmpty(map.get("CBR50avg1")) ? "" : map.get("CBR50avg1").toString(),
                        "sampleNo", s.get("sampleNo").toString());
            } // 级配碎石
            else if ("JJ0105a".equals(s.get("tiNo"))) {
                this.dynamicRevise("JJ0105d1", "CBR25avg1", oConvertUtils.isEmpty(map.get("CBR25avg1")) ? "" : map.get("CBR25avg1").toString(),
                        "CBR50avg1", oConvertUtils.isEmpty(map.get("CBR50avg1")) ? "" : map.get("CBR50avg1").toString(),
                        "sampleNo", s.get("sampleNo").toString());
            }
            if ("JJ0105b".equals(s.get("tiNo"))) {
                this.dynamicRevise("JJ0105d", "CBR25avg2", oConvertUtils.isEmpty(map.get("CBR25avg2")) ? "" : map.get("CBR25avg2").toString(),
                        "CBR50avg2", oConvertUtils.isEmpty(map.get("CBR50avg2")) ? "" : map.get("CBR50avg2").toString(),
                        "sampleNo", s.get("sampleNo").toString());
            } // 级配碎石
            else if ("JJ0105b1".equals(s.get("tiNo"))) {
                this.dynamicRevise("JJ0105d1", "CBR25avg2", oConvertUtils.isEmpty(map.get("CBR25avg2")) ? "" : map.get("CBR25avg2").toString(),
                        "CBR50avg2", oConvertUtils.isEmpty(map.get("CBR50avg2")) ? "" : map.get("CBR50avg2").toString(),
                        "sampleNo", s.get("sampleNo").toString());
            }
            if ("JJ0105c".equals(s.get("tiNo"))) {
                this.dynamicRevise("JJ0105d", "CBR25avg3", oConvertUtils.isEmpty(map.get("CBR25avg3")) ? "" : map.get("CBR25avg3").toString(),
                        "CBR50avg3", oConvertUtils.isEmpty(map.get("CBR50avg3")) ? "" : map.get("CBR50avg3").toString(),
                        "sampleNo", s.get("sampleNo").toString());
            } // 级配碎石
            else if ("JJ0105c1".equals(s.get("tiNo"))) {
                this.dynamicRevise("JJ0105d1", "CBR25avg3", oConvertUtils.isEmpty(map.get("CBR25avg3")) ? "" : map.get("CBR25avg3").toString(),
                        "CBR50avg3", oConvertUtils.isEmpty(map.get("CBR50avg3")) ? "" : map.get("CBR50avg3").toString(),
                        "sampleNo", s.get("sampleNo").toString());
            }
        } else if (s.get("tiType").toString().equals("1")) {
            sampleMapper.updateSql15(oConvertUtils.isNotEmpty(map.get("baogaoriqi")) ? map.get("baogaoriqi").toString() : "",
                    oConvertUtils.isNotEmpty(map.get("SYJG")) ? Integer.valueOf(map.get("SYJG").toString()) : null,
                    oConvertUtils.isNotEmpty(map.get("buhegeyuanyin")) ? map.get("buhegeyuanyin").toString() : "",
                    s.get("sampleNo").toString());
            sampleMapper.updateSql16(oConvertUtils.isNotEmpty(map.get("baogaoriqi")) ? map.get("baogaoriqi").toString() : "", s.get("sampleNo").toString(), s.get("tiNo").toString());
        } else {
            sampleMapper.updateSql17(oConvertUtils.isNotEmpty(map.get("shiyanriqi")) ? map.get("shiyanriqi").toString() : "", s.get("sampleNo").toString());
            sampleMapper.updateSql16(oConvertUtils.isNotEmpty(map.get("shiyanriqi")) ? map.get("shiyanriqi").toString() : "", s.get("sampleNo").toString(), s.get("tiNo").toString());
        }
        String id = map.get("id").toString();
        String tiNo = s.get("tiNo").toString();
//        String sampleNo = s.get("sampleNo").toString();
        String tiNoTemp = s.get("tiNoTemp").toString();
        sampleMapper.updateSql18(id);
        List<String> list = sampleMapper.findSySColumns(dataSource, tiNo);
        StringBuilder stringFive = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            String key = list.get(i);
//            String key = columns.get("name").toString();
            if (key.equals("id")) {
                continue;
            }
            if (map.containsKey(key)) {
                String value;
                if (oConvertUtils.isEmpty(map.get(key))) {
                    value = "/";
                } else {
                    value = map.get(key).toString();
                    if (StringUtil.isEmpty(value)) {
                        value = "/";
                    } else if ("NaN".equals(value)) {
                        value = "/";
                    }
                }
                stringFive.append(key + "=" + "'" + value + "'" + ",");
//                sampleMapper.updateTiNo(tiNo, key, value, sampleNo, tiNoTemp);
            }
        }
        if (stringFive.length() > 0) {
            sampleMapper.updateTiNoSql(tiNo, stringFive.substring(0, stringFive.length() - 1), sampleNo, tiNoTemp);
        }
        list = sampleMapper.findSySColumns(dataSource, "sy_dps_sy_tableHeader");
        StringBuilder stringSix = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            String key = list.get(i);
//            String key = columns.get("name").toString().trim();
            if (key.equals("id")) {
                continue;
            }
            if (map.containsKey(key)) {
                String value;
                if (oConvertUtils.isEmpty(map.get(key))) {
                    value = "/";
                } else {
                    value = map.get(key).toString();
                    if (!key.equals("jlyj") && !key.equals("remark")) {
                        if (StringUtil.isEmpty(value)) {
                            value = "/";
                        } else if ("NaN".equals(value)) {
                            value = "/";
                        }
                    }
                }
                stringSix.append(key + "=" + "'" + value + "'" + ",");
//                sampleMapper.updateByTiNoTempTwo(key, value, sampleNo, tiNoTemp, tiNo);
            }
        }
        if (stringSix.length() > 0) {
            sampleMapper.updateByTiNoTempTwoSql(stringSix.substring(0, stringSix.length() - 1), sampleNo, tiNoTemp, tiNo);
        }
        if (s.get("tiType").toString().equals("0")) {
            if (!("".equals(s.get("yiqishebei")) || "/".equals(s.get("yiqishebei")))) {
                LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
                String orgCode = loginUser.getOrgCode();
                String username = loginUser.getUsername();
                Integer count = sampleMapper.findByOrgCodeAndUserName(orgCode, s.get("tiNo").toString(), username, sample.getTitcode());
                if (count > 0) {
                    sampleMapper.updateYiQiSheBei(map.get("yiqishebei"), orgCode, s.get("tiNo").toString(), username, sample.getTitcode());
                } else {
                    String uuid = UUID.randomUUID().toString();
                    sampleMapper.insertYiQiSheBei(uuid, orgCode, s.get("tiNo").toString(), username, map.get("yiqishebei").toString(), sample.getTitcode());
                }
            }
            // 仪器设备
            List<Map<String, Object>> jllist = sampleMapper.querySql3(s.get("sampleNo").toString(), s.get("sampleNo").toString());
            List<String> slist = new ArrayList<>();
            for (int i = 0; i < jllist.size(); i++) {
                String yiqishebei = jllist.get(i).get("yiqishebei").toString();
                String[] yiqishebeis = yiqishebei.split("、|,|，|;");
                for (String string : yiqishebeis) {
                    if (StringUtil.isNotEmpty(string) && !slist.contains(string) && !string.equals("/")) {
                        slist.add(string);
                    }
                }
            }
            String y = StringUtils.join(slist.toArray(), "、");
            sampleMapper.updateSql19(y, s.get("sampleNo").toString(), bgno.get("tiNo").toString());
            // 试验依据
            List<Map<String, Object>> jllists = sampleMapper.querySql4(s.get("sampleNo").toString(), s.get("sampleNo").toString());
            slist = new ArrayList<>();
            for (int i = 0; i < jllist.size(); i++) {
                String yiqishebei = jllists.get(i).get("shiyanyiju").toString();
                String[] yiqishebeis = yiqishebei.split("、|,|，|;");
                for (String string : yiqishebeis) {
                    if (StringUtil.isNotEmpty(string) && !slist.contains(string) && !string.equals("/")) {
                        slist.add(string);
                    }
                }
            }
            y = StringUtils.join(slist.toArray(), ";");
            sampleMapper.updateSql20(y, s.get("sampleNo").toString(), bgno.get("tiNo").toString());
            if (null != map.get("baogaoriqi") && oConvertUtils.isNotEmpty(map.get("baogaoriqi"))) {
                sampleMapper.updateSql21(map.get("baogaoriqi").toString(), s.get("sampleNo").toString());
            }
            if ("1".equals(titType.get("titType").toString())) {
                List<Map<String, Object>> pdyj = sampleMapper.querySql5(s.get("sampleNo").toString(), s.get("sampleNo").toString());
                slist = new ArrayList<>();
                for (int i = 0; i < pdyj.size(); i++) {
                    String pandingyiju = pdyj.get(i).get("pandingyiju").toString();
                    String[] pandingyijus = pandingyiju.split("、|,|，|;");
                    for (String string : pandingyijus) {
                        if (StringUtil.isNotEmpty(string) && !slist.contains(string) && !string.equals("/")) {
                            slist.add(string);
                        }
                    }
                }
                y = StringUtils.join(slist.toArray(), ";");
                sampleMapper.updateSql22(y, s.get("sampleNo").toString(), bgno.get("tiNo").toString());
            }
        }
        if (map.containsKey("wzid")) {
            sampleMapper.updateSql23(map.get("wzid").toString());
        }
    }
//    {1502, 1503, 1505, 15, 1501, 1507, 1508, 1506, 1509, 1510, 1511, 1512, 1513, 1514, 1515, 0501,
//            0503, 0505, 0504, 0506, 0508, 0511, 05, 0507, 0510, 0513, 0515}


    @Override
    @Transactional
    public void add(HashMap map) {
        if (oConvertUtils.isEmpty(map.get("bj"))) { //编辑保存的接口可以避开进场批次的校验，编辑的接口返回bj字段
            boolean flag = true;
            String titType = this.baseMapper.selectTypeByTitCode(map.get("titCode").toString());
            if (oConvertUtils.isNotEmpty(titType) && ("1".equals(titType) || "2".equals(titType) || "3".equals(titType))) {
                flag = false;
            }
            if (oConvertUtils.isEmpty(map.get("update")) || oConvertUtils.isEmpty(map.get("insert"))) {
//                if (flag && oConvertUtils.isEmpty(map.get("wztzid"))) {
//                    throw new JeecgBootException("请选择对应的进场批次!");
//                }
                if (oConvertUtils.isNotEmpty(map.get("wztzid"))) {
                    Wztaizhang wztaizhang = wztaizhangService.getById(map.get("wztzid").toString());
                    if (oConvertUtils.isNotEmpty(wztaizhang.getSampleNo())) {
                        throw new JeecgBootException("该检验批已关联!");
                    }
                }
            }
        }

        String tiNames = "";
        SyDpsSySample sample = new SyDpsSySample();
        SyDpsSyReportM reportM = null;
        List<SyDpsSyReportS> reportSlist = new ArrayList<>();
        List<SyDpsSyTableheader> tableHeaderlist = new ArrayList<>();
//        List<String> sqllist = new ArrayList<>();
        Map<String, Object> itemTypemapMap = sampleMapper.getOneSql1(map.get("titCode").toString());//dps_jc_testItemType
        SyDpsJcTestitemtype itemType = sampleMapper.getOneSql2(itemTypemapMap.get("uuid").toString());
        SysDepart depart = sampleMapper.getByOrgCode(map.get("orgCode").toString());
//        TSDepart depart1 = sampleMapper.getDepartById(map.get("departid").toString());
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String departOrgCode = loginUser.getOrgCode();
        SyCodingRules rules = this.baseMapper.selectDepartRuleByUserOrgCode(departOrgCode);
        /*String departIds = loginUser.getDepartIds();
        String[] strings = departIds.split(",");
        List listDepart = new ArrayList<>();
        for (int k = 0; k < strings.length; k++) {
            TSDepart depart2 = sampleMapper.getDepartById(strings[k]);
            if (depart2 != null) {
                listDepart.add(depart2);
            }
        }
        for (int k = 0; k < listDepart.size(); k++) {*/
//            TSDepart depart2 = (TSDepart) listDepart.get(k);
        Map<String, Object> data = sampleMapper.getOneSql3(depart.getId(), map.get("titCode").toString());

        String sampleCodingRules = rules.getSampleCodingRules();
        String reportCodingRules = rules.getReportCodingRules();
        String recordCodingRules = rules.getRecordCodingRules();
        String codingDateFormat = rules.getCodingDateFormat();
        Integer codingFlowNumerDigit = rules.getCodingFlowNumerDigit();
        String reportingSheetCodingRules = rules.getReportingSheetCodingRules();
        String approvalTableCodingRules = rules.getApprovalTableCodingRules();
        boolean addstate = false;
        if (oConvertUtils.isNotEmpty(map.get("addstate"))) {
            addstate = Boolean.valueOf(map.get("addstate").toString());
        }
        if (oConvertUtils.isNotEmpty(data)) {
            SyDpsJcTestitemtypeCodingrules typeCoding = sampleMapper.getOneSql4(data.get("id").toString());
            sampleCodingRules = typeCoding.getSamplecodingrules();
            reportCodingRules = typeCoding.getReportcodingrules();
            recordCodingRules = typeCoding.getRecordcodingrules();
            codingDateFormat = typeCoding.getCodingdateformat();
            codingFlowNumerDigit = typeCoding.getCodingflownumerdigit();
            reportingSheetCodingRules = typeCoding.getReportingsheetcodingrules();
            approvalTableCodingRules = typeCoding.getApprovaltablecodingrules();
        }
        if (oConvertUtils.isNotEmpty(map.get("id"))) {
            sample = sampleMapper.selectById(map.get("id").toString());
            sample.setSamplename(oConvertUtils.isEmpty(map.get("sampleName")) ? "" : map.get("sampleName").toString());
            sample.setSampledescribe(map.get("sampleDescribe").toString());
            sample.setSampledate(map.get("sampleDate").toString());
            sample.setSamplechandi(map.get("sampleChanDi").toString());
            sample.setSampledaibiaoshuliang(map.get("sampleDaiBiaoShuLiang").toString());
            sample.setSampleshiyanzushu(map.get("sampleShiYanZuShu").toString());
            sample.setSampleguigexinghao(map.get("sampleGuiGeXingHao").toString());
            sample.setSamplequyangdidian(map.get("sampleQuYangDiDian").toString());
            sample.setSampleshengchanchangjia(map.get("sampleShengChanChangJia").toString());
            sample.setSamplechuchangbianhao(map.get("sampleChuChangBianHao").toString());
            sample.setSampleqiangdudengji(map.get("sampleQiangDuDengJi").toString());
            sample.setSamplejiaobanfangshi(map.get("sampleJiaoBanFangShi").toString());
            sample.setSamplehunningtuzhonglei(map.get("sampleHunNingTuZhongLei").toString());
            sample.setSampleyanghutiaojian(map.get("sampleYangHuTiaoJian").toString());
            sample.setSampleshajiangzhonglei(map.get("sampleShaJiangZhongLei").toString());
            sample.setSampleshengchanriqi(map.get("sampleShengChanRIQi").toString());
            sample.setSampleshengchanpihao(map.get("sampleShengChanPiHao").toString());
            sample.setSamplepinzhongdengji(map.get("samplePinZhongDengJi").toString());
            sample.setSampleshejijiliang(map.get("sampleSheJiJiLiang").toString());
            sample.setSamplejieheliaozhonglei(map.get("sampleJieHeLiaoZhongLei").toString());
            sample.setSampleliqingbiaohao(map.get("sampleLiQingBiaoHao").toString());
            sample.setSampleliqingzhonglei(map.get("sampleLiQingZhongLei").toString());
            sample.setSamplejipeileixing(map.get("sampleJiPeiLeiXing").toString());
            sample.setSampleshiyancengwei(map.get("sampleShiYanCengWei").toString());
            sample.setSampleliqinghunheliaoleixing(map.get("sampleLiQingHunHeLiaoLeiXing").toString());
            sample.setSamplegangjinzhijing(map.get("sampleGangJinZhiJing").toString());
            sample.setSamplegangjinzhonglei(map.get("sampleGangJinZhongLei").toString());
            sample.setSamplelingqi(map.get("sampleLingQi").toString());

            //计算最大龄期
            if (oConvertUtils.isNotEmpty(map.get("sampleLingQi"))) {
                String LingQi = map.get("sampleLingQi").toString().replaceAll("\\|", "");
                String[] SampleLingQi1 = LingQi.split(",");
                if (SampleLingQi1.length == 0) {
                    sample.setSamplelingqimax("");
                } else {
                    String sampleLingQiMax = SampleLingQi1[0];
                    for (int j = 0; j < SampleLingQi1.length; j++) {
                        if (Integer.parseInt(sampleLingQiMax) <= Integer.parseInt(SampleLingQi1[j])) {
                            sampleLingQiMax = SampleLingQi1[j];
                        }
                    }
                    sample.setSamplelingqimax(sampleLingQiMax);
                }
            }
            sample.setSamplewaijiajichanliang(map.get("sampleWaiJiaJiChanLiang").toString());
            sample.setSamplequyangren(map.get("sampleQuYangRen").toString());
            sample.setSampleremark(map.get("sampleRemark").toString());
            //sample.setReportDate("");
            sample.setSamplechandi2(map.get("sampleChanDi2").toString());
            sample.setSamplechandi3(map.get("sampleChanDi3").toString());
            sample.setSamplequyangdidian2(map.get("sampleQuYangDiDian2").toString());
            sample.setSamplequyangdidian3(map.get("sampleQuYangDiDian3").toString());
            sample.setSampledate2(map.get("sampleDate2").toString());
            sample.setSampledate3(map.get("sampleDate3").toString());
            sample.setSampledaibiaoshuliang2(map.get("sampleDaiBiaoShuLiang2").toString());
            sample.setSampledaibiaoshuliang3(map.get("sampleDaiBiaoShuLiang3").toString());
            sample.setSampleyangpinshuliang4(map.get("sampleDaiBiaoShuLiang4") == null ? "" : map.get("sampleDaiBiaoShuLiang4").toString());
            sample.setSampleyangpinshuliang5(map.get("sampleDaiBiaoShuLiang5") == null ? "" : map.get("sampleDaiBiaoShuLiang5").toString());
            sample.setSampleguigexinghao2(map.get("sampleGuiGeXingHao2").toString());
            sample.setSampleguigexinghao3(map.get("sampleGuiGeXingHao3").toString());
            sample.setSampleyangpinshuliang4(map.get("sampleGuiGeXingHao4") == null ? "" : map.get("sampleGuiGeXingHao4").toString());
            sample.setSampleyangpinshuliang5(map.get("sampleGuiGeXingHao5") == null ? "" : map.get("sampleGuiGeXingHao5").toString());
            sample.setSamplegongchengbihou(map.get("sampleGongChengBiHou").toString());
            if (oConvertUtils.isNotEmpty(map.get("wztzid"))) {
                sample.setYcjcwtdbh(map.get("wztzid").toString());
            }
            sample.setSampleyangpinshuliang(map.get("sampleYangPinShuLiang").toString());
            sample.setSampleyangpinshuliang2(map.get("sampleYangPinShuLiang2").toString());
            sample.setSampleyangpinshuliang3(map.get("sampleYangPinShuLiang3").toString());
            sample.setSampleyangpinshuliang4(map.get("sampleYangPinShuLiang4") == null ? "" : map.get("sampleYangPinShuLiang4").toString());
            sample.setSampleyangpinshuliang5(map.get("sampleYangPinShuLiang5") == null ? "" : map.get("sampleYangPinShuLiang5").toString());

            sample.setSamplechangdu(map.get("sampleChangDu").toString());
            sample.setShikuaichicun(map.get("shikuaichicun") == null ? "" : map.get("shikuaichicun").toString());
            sample.setSampleliuyangshuliang(map.get("sampleLiuYangShuLiang") == null ? "" : map.get("sampleLiuYangShuLiang").toString());
            sample.setSamplezhijianriqi(map.get("sampleZhiJianRiQi") == null ? "" : map.get("sampleZhiJianRiQi").toString());
            if (oConvertUtils.isNotEmpty(map.get("syzt"))) {
                sample.setSyzt(Integer.valueOf(map.get("syzt").toString()));
            } else {
                sample.setSyzt(2);
            }
            if (oConvertUtils.isNotEmpty(map.get("shifouliuyang"))) {
                sample.setShifouliuyang(Integer.valueOf(map.get("shifouliuyang").toString()));
            }
            sample.setLiuyangriqi(map.get("liuyangriqi").toString());
            if (oConvertUtils.isNotEmpty(map.get("liuyangqixian"))) {
                sample.setLiuyangqixian(Integer.valueOf(map.get("liuyangqixian").toString()));
            }
            sample.setLiuyangchuli(map.get("liuyangchuli").toString());
            sample.setProjectid(map.get("projectId").toString());
//				sample.setSampleNoNew(StringUtil.isEmpty(sampleNoNew) ? sample.getSampleNoNew() : sampleNoNew);
//				sample.setTableNumberNew(StringUtil.isEmpty(tableNumberNew) ? sample.getTableNumberNew() : tableNumberNew);
//				sample.setReportNoNew(StringUtil.isEmpty(reportNoNew) ? sample.getReportNoNew() : reportNoNew);
            if (oConvertUtils.isNotEmpty(map.get("sampleGcbw"))) {
                sample.setSamplegcbw(map.get("sampleGcbw").toString());
            } else {
                sample.setSamplegcbw(map.get("projectName").toString());
            }
            reportM = sampleMapper.getOneSql5(sample.getSampleno());
            if (oConvertUtils.isNotEmpty(map.get("project"))) {
                reportM.setProjectcode(map.get("project").toString());
            } else {
                reportM.setProjectcode(map.get("projectId").toString());
            }
            reportM.setProjectname(map.get("projectName").toString());
            List<String> tiNos1 = asList(map.get("tiNos").toString().split(","));
            List<Map<String, Object>> list = sampleMapper.getOneSql6(sample.getSampleno());

            List<String> tiNos2 = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                tiNos2.add(list.get(i).get("tiNo").toString());
                if (!tiNos1.contains(tiNos2.get(i))) {
                    sampleMapper.deleteSql1(tiNos2.get(i), sample.getSampleno());
                    sampleMapper.deleteSql2(tiNos2.get(i), sample.getSampleno());
                    Map<String, Object> table = sampleMapper.getOneSql7(tiNos2.get(i));
                    sampleMapper.deleteSql3(table.get("tiTableNum").toString(), tiNos2.get(i), sample.getSampleno());
                }
            }
            List<String> deivces = new ArrayList<>();
            Map<String, Object> pt = sampleMapper.getOneSql8(sample.getSampleno(), map.get("ptiNo").toString());
            if (null != pt) {
                deivces = asList(pt.get("yiqishebei").toString().split(","));
            }
            for (int i = 0; i < tiNos1.size(); i++) {
                if (!tiNos2.contains(tiNos1.get(i))) {
                    SyDpsSyReportS reportS = new SyDpsSyReportS();
                    Long dpsR = sampleMapper.getCount1(tiNos1.get(i), sample.getSampleno());
                    Map<String, Object> table = sampleMapper.getOneSql9(tiNos1.get(i));
                    if (dpsR == 0) {
                        reportS.setTino(tiNos1.get(i));
                        reportS.setReportno(reportM.getReportno());
                        reportS.setSampleno(sample.getSampleno());
                        reportS.setTitype(Integer.valueOf(table.get("tiType").toString()));
                        reportS.setEditstate(0);
                        reportS.setTinotemp(1);
                        reportSlist.add(reportS);
                    }
                    SyDpsSyTableheader tableHeader = new SyDpsSyTableheader();
                    SyDpsJcTestitem items = sampleMapper.selectTestItemByTino(tiNos1.get(i));

                    if (oConvertUtils.isNotEmpty(map.get("ShiYanYiJu"))) {
                        tableHeader.setShiyanyiju(map.get("ShiYanYiJu").toString());
                    } else {
                        if (StringUtil.isNotEmpty(items.getTishiyanyiju())) {
                            tableHeader.setShiyanyiju(items.getTishiyanyiju());
                        } else {
                            tableHeader.setShiyanyiju(itemType.getTishiyanyiju());
                        }
                    }
                    if (oConvertUtils.isNotEmpty(map.get("PanDingYiJu"))) {
                        tableHeader.setPandingyiju(map.get("PanDingYiJu").toString());
                    } else {
                        if (StringUtil.isNotEmpty(items.getTipandingyiju())) {
                            tableHeader.setPandingyiju(items.getTipandingyiju());
                        } else {
                            tableHeader.setPandingyiju(itemType.getTipandingyiju());
                        }

                    }

//                        tableHeader.setDepartid(depart2.getId());
                    tableHeader.setDepartid(depart.getId());

                    tableHeader.setGongchengmingcheng(depart.getDepartName());
//                        tableHeader.setGongchengmingcheng(depart2.getDepartfullname());
                    tableHeader.setDepartfullname(depart.getDepartName());
//                        tableHeader.setDepartfullname(depart2.getDepartfullname());
                    tableHeader.setGongzhangmingcheng(depart.getDepartName());
//                        tableHeader.setGongzhangmingcheng(depart2.getGongzhangmingcheng());

//                        tableHeader.setShigongdanwei(depart1.getParentlaboratoryname());
                    tableHeader.setShigongdanwei(depart.getDepartName());

//                        tableHeader.setHetonghao(depart2.getContractnumber());
//                        tableHeader.setJianlidanwei(depart1.getJianlidanwei());
                    tableHeader.setJianlidanwei(depart.getDepartName());
//                        tableHeader.setZonbaodanwei(depart1.getZonbaodanwei());
                    tableHeader.setZonbaodanwei(depart.getDepartName());

                    tableHeader.setSamplegcbw(sample.getSamplegcbw());

                    //tableHeader.setSampleNo(sample.getSampleNoNew());
                    //tableHeader.setTableNumber(sample.getTableNumberNew());
                    //tableHeader.setReportNo(sample.getReportNoNew());
                    tableHeader.setSampleno(sample.getSampleno());
                    tableHeader.setTablenumber(sample.getTablenumbernew());
                    tableHeader.setReportno(sample.getReportnonew());
                    tableHeader.setReportingsheetno(sample.getReportingsheetnonew());
                    tableHeader.setApprovaltableno(sample.getApprovaltablenonew());

                    tableHeader.setTino(tiNos1.get(i));
                    tableHeader.setTinotemp(1);

                    tableHeader.setSamplename(sample.getSamplename());
                    tableHeader.setSampledescribe(sample.getSampledescribe());
                    tableHeader.setSamplequyangdidian(sample.getSamplequyangdidian());
                    tableHeader.setSampledaibiaoshuliang(sample.getSampledaibiaoshuliang());
                    tableHeader.setSampleshengchanchangjia(sample.getSampleshengchanchangjia());
                    tableHeader.setSamplepihao(sample.getSampleshengchanpihao());
                    tableHeader.setSampleyanghutiaojian(sample.getSampleyanghutiaojian());
                    tableHeader.setSampledate(sample.getSampledate());
                    tableHeader.setSamplechandi(sample.getSamplechandi());
                    tableHeader.setSampleshengchanpihao(sample.getSampleshengchanpihao());
                    tableHeader.setSamplepinzhongdengji(sample.getSamplepinzhongdengji());
                    tableHeader.setSampleshejijiliang(sample.getSampleshejijiliang());
                    tableHeader.setSamplejieheliaozhonglei(sample.getSamplejieheliaozhonglei());
                    tableHeader.setSampleliqingbiaohao(sample.getSampleliqingbiaohao());
                    tableHeader.setSampleliqingzhonglei(sample.getSampleliqingzhonglei());
                    tableHeader.setSamplejipeileixing(sample.getSamplejipeileixing());
                    tableHeader.setSampleshiyancengwei(sample.getSampleshiyancengwei());
                    tableHeader.setSampleliqinghunheliaoleixing(sample.getSampleliqinghunheliaoleixing());
                    tableHeader.setSamplegangjinzhijing(sample.getSamplegangjinzhijing());
                    tableHeader.setSamplegangjinzhonglei(sample.getSamplegangjinzhonglei());
                    tableHeader.setSamplelingqi(sample.getSamplelingqi());
                    tableHeader.setSamplewaijiajichanliang(sample.getSamplewaijiajichanliang());
                    tableHeader.setSampleshengchanriqi(sample.getSampleshengchanriqi());
                    tableHeader.setSampleguigexinghao(sample.getSampleguigexinghao());
                    tableHeader.setSamplequyangren(sample.getSamplequyangren());
                    tableHeader.setSamplechuchangbianhao(sample.getSamplechuchangbianhao());
                    tableHeader.setSampleqiangdudengji(sample.getSampleqiangdudengji());
                    tableHeader.setSamplejiaobanfangshi(sample.getSamplejiaobanfangshi());
                    tableHeader.setSamplehunningtuzhonglei(sample.getSamplehunningtuzhonglei());
                    tableHeader.setSampleshajiangzhonglei(sample.getSampleshajiangzhonglei());
                    tableHeader.setSampleshiyanzushu(sample.getSampleshiyanzushu());
                    tableHeader.setSamplechandi2(sample.getSamplechandi2());
                    tableHeader.setSamplechandi3(sample.getSamplechandi3());
                    tableHeader.setSamplequyangdidian2(sample.getSamplequyangdidian2());
                    tableHeader.setSamplequyangdidian3(sample.getSamplequyangdidian3());
                    tableHeader.setSampledate2(sample.getSampledate2());
                    tableHeader.setSampledate3(sample.getSampledate3());
                    tableHeader.setSampledaibiaoshuliang2(sample.getSampledaibiaoshuliang2());
                    tableHeader.setSampledaibiaoshuliang3(sample.getSampledaibiaoshuliang3());
                    tableHeader.setSampledaibiaoshuliang4(sample.getSampledaibiaoshuliang4());
                    tableHeader.setSampledaibiaoshuliang5(sample.getSampledaibiaoshuliang5());
                    tableHeader.setSampleguigexinghao2(sample.getSampleguigexinghao2());
                    tableHeader.setSampleguigexinghao3(sample.getSampleguigexinghao3());
                    tableHeader.setSampleguigexinghao4(sample.getSampleguigexinghao4());
                    tableHeader.setSampleguigexinghao5(sample.getSampleguigexinghao5());
                    tableHeader.setSamplegongchengbihou(sample.getSamplegongchengbihou());
                    tableHeader.setYcjcwtdbh(sample.getYcjcwtdbh());
                    tableHeader.setSampleyangpinshuliang(sample.getSampleyangpinshuliang());
                    tableHeader.setSampleyangpinshuliang2(sample.getSampleyangpinshuliang2());
                    tableHeader.setSampleyangpinshuliang3(sample.getSampleyangpinshuliang3());
                    tableHeader.setSamplechangdu(sample.getSamplechangdu());
                    tableHeader.setSamplezhijianriqi(sample.getSamplezhijianriqi());

                    tableHeader.setSamplenonew(sample.getSamplenonew());
                    tableHeader.setTablenumbernew(sample.getTablenumbernew());
                    tableHeader.setReportnonew(sample.getReportnonew());
                    tableHeader.setReportingsheetnonew(sample.getReportingsheetnonew());
                    tableHeader.setApprovaltablenonew(sample.getApprovaltablenonew());

                    if ("".equals(depart.getOrgType())) {
                        tableHeader.setWtrwno("委托编号");
                        tableHeader.setWtsgdw("委托单位");
                    } else {
                        tableHeader.setWtrwno("任务编号");
                        tableHeader.setWtsgdw("施工单位");
                    }
                    tableHeader.setYpxx(getYpxx(itemType.getTiyangpinxinxijl(), sample));
                    Map<String, Object> deivcelist = sampleMapper.getOneSql10(depart.getId(), tiNos1.get(i), loginUser.getUsername(), itemType.getTitcode());

                    List<String> deivce = new ArrayList<>();
                    if (deivcelist != null) {
                        if (deivcelist.get("shebeiName") != null) {
                            String[] str = deivcelist.get("shebeiName").toString().split(",");
                            for (int j = 0; j < str.length; j++) {
                                if (!"".equals(str[j])) {
                                    if (deivce.indexOf(str[j]) == -1) {
                                        deivce.add(str[j]);
                                    }
                                    if (deivces.indexOf(str[j]) == -1) {
                                        deivces.add(str[j]);
                                    }
                                }
                            }
                        }
                    }
                    String tiTableNum = table.get("tiTableNum").toString();
                    tableHeader.setYiqishebei(joinString(deivce, ","));
                    tableHeaderlist.add(tableHeader);
                    Long count = sampleMapper.getCount2(table.get("tiTableNum").toString(), sample.getSampleno());
                    if (Integer.valueOf(table.get("tiType").toString()) == 3
                            || Integer.valueOf(table.get("tiType").toString()) == 4) {
                        Map<String, Object> byd = sampleMapper.getOneSql11(tiTableNum, depart.getId());
                        if (byd != null) {
                            if (count == 0) {
                                sampleMapper.insertSql1(tiTableNum,
                                        UUID.randomUUID().toString(), tiNos1.get(i).toString(),
                                        1, sample.getSampleno(), reportM.getTablenumber(),
                                        reportM.getReportno(), sample.getDepartid());
                            } else {
                                sampleMapper.updateSql24(tiTableNum, tiNos1.get(i).toString(), 1, sample.getSampleno(),
                                        reportM.getTablenumber(), reportM.getReportno(), sample.getDepartid(),
                                        byd.get("cbryj").toString(), sample.getSampleno());
                            }
                        } else {
                            if (count == 0) {
                                String uuid = UUID.randomUUID().toString();
                                sampleMapper.insertSql2(tiTableNum, uuid, tiNos1.get(i), 1, sample.getSampleno(),
                                        reportM.getTablenumber(), reportM.getReportno(), sample.getDepartid());
                            } else {
                                sampleMapper.updateSql25(tiTableNum, tiNos1.get(i).toString(), 1, sample.getSampleno(), reportM.getTablenumber(),
                                        reportM.getReportno(), sample.getDepartid(), sample.getSampleno());
                            }
                        }
                    } else {
                        if (count == 0) {
                            String uuid = UUID.randomUUID().toString();
                            sampleMapper.insertSql2(tiTableNum, uuid, tiNos1.get(i), 1, sample.getSampleno(),
                                    reportM.getTablenumber(), reportM.getReportno(), sample.getDepartid());
                        } else {
                            sampleMapper.updateSql25(tiTableNum, tiNos1.get(i), 1, sample.getSampleno(), reportM.getTablenumber(),
                                    reportM.getReportno(), sample.getDepartid(), sample.getSampleno());
                        }
                    }
                }
                SyDpsJcTestitem items2 = sampleMapper.selectTestItemByTino(tiNos1.get(i));
                tiNames += items2.getTiname();
            }
            sample.setTinames(tiNames);
            sampleMapper.updateSql26(joinString(deivces, ","), sample.getSampleno(), map.get("ptiNo").toString());
            sampleMapper.updateSql27(reportM.getProjectname(), reportM.getSampleno());
            if (null != map.get("update") && "1".equals(map.get("update").toString())) {
                sampleMapper.updateTableHeader(reportM.getProjectname(), sample.getSamplename(),
                        sample.getSampledescribe(), sample.getSamplequyangdidian(),
                        sample.getSampledaibiaoshuliang(), sample.getSampleshengchanchangjia(),
                        sample.getSampleshengchanpihao(), sample.getSampleyanghutiaojian(), sample.getSampledate(),
                        sample.getSamplechandi(), sample.getSamplepinzhongdengji(), sample.getSampleshejijiliang(),
                        sample.getSamplejieheliaozhonglei(), sample.getSampleliqingbiaohao(),
                        sample.getSampleliqingzhonglei(), sample.getSamplejipeileixing(),
                        sample.getSampleshiyancengwei(), sample.getSampleliqinghunheliaoleixing(),
                        sample.getSamplegangjinzhijing(), sample.getSamplegangjinzhonglei(),
                        sample.getSamplelingqi(), sample.getSamplewaijiajichanliang(),
                        sample.getSampleshengchanriqi(), sample.getSampleguigexinghao(),
                        sample.getSamplequyangren(), sample.getSamplechuchangbianhao(),
                        sample.getSampleqiangdudengji(), sample.getSamplejiaobanfangshi(),
                        sample.getSamplehunningtuzhonglei(), sample.getSampleshajiangzhonglei(), sample.getSampleshiyanzushu(),
                        sample.getSamplechandi2(), sample.getSamplechandi3(), sample.getSamplequyangdidian2(), sample.getSamplequyangdidian3(),
                        sample.getSampledate2(), sample.getSampledate3(), sample.getSampledaibiaoshuliang2(), sample.getSampledaibiaoshuliang3(),
                        sample.getSampledaibiaoshuliang4(), sample.getSampledaibiaoshuliang5(), sample.getSampleguigexinghao2(), sample.getSampleguigexinghao3(),
                        sample.getSampleguigexinghao4(), sample.getSampleguigexinghao5(), sample.getSamplegongchengbihou(), sample.getYcjcwtdbh(),
                        sample.getSampleyangpinshuliang(), sample.getSampleyangpinshuliang2(), sample.getSampleyangpinshuliang3(),
                        sample.getSampleyangpinshuliang4(), sample.getSampleyangpinshuliang5(), map.get("ShiYanYiJu").toString(),
                        map.get("PanDingYiJu").toString(), sample.getSamplechangdu(), sample.getSamplezhijianriqi(),
                        reportM.getSampleno());
                String bgypxx = getYpxx(itemType.getTiyangpinxinxibg(), sample);
                String jlypxx = getYpxx(itemType.getTiyangpinxinxijl(), sample);
                String jlypxx1 = new String();
                String jlypxx2 = new String();
                String jlypxx3 = new String();
                if ("0201".equals(itemType.getTitcode())) {
                    bgypxx = bgypxx.substring(0, bgypxx.length() - 1) + "/" + sample.getSampleyangpinshuliang2() + "/" + sample.getSampleyangpinshuliang3();
                    jlypxx = jlypxx.substring(0, jlypxx.length() - 1) + "/" + sample.getSampleyangpinshuliang2() + "/" + sample.getSampleyangpinshuliang3();
                }
                sampleMapper.updateTableHeaderYPXX1(bgypxx, sample.getSampleno());
                sampleMapper.updateTableHeaderYPXX2(jlypxx, sample.getSampleno());
                if ("0201".equals(itemType.getTitcode())) {
                    jlypxx1 = "取样日期:" + sample.getSampledate() + ";样品名称:" + sample.getSamplename() + ";样品编号:" + sample.getSampleno() + "(01);样品描述:"
                            + sample.getSampledescribe() + ";样品数量:" + sample.getSampleyangpinshuliang() + "/" + sample.getSampleyangpinshuliang2() + "/" + sample.getSampleyangpinshuliang3();
                    sampleMapper.updateTableHeaderByTiNo(jlypxx1, sample.getSampleno(), "JJ0201a");

                    jlypxx2 = "取样日期:" + sample.getSampledate() + ";样品名称:" + sample.getSamplename() + ";样品编号:" + sample.getSampleno() + "(02);样品描述:"
                            + sample.getSampledescribe() + ";样品数量:" + sample.getSampleyangpinshuliang() + "/" + sample.getSampleyangpinshuliang2() + "/" + sample.getSampleyangpinshuliang3();
                    sampleMapper.updateTableHeaderByTiNo(jlypxx2, sample.getSampleno(), "JJ0201a2");

                    jlypxx3 = "取样日期:" + sample.getSampledate() + ";样品名称:" + sample.getSamplename() + ";样品编号:" + sample.getSampleno() + "(03);样品描述:"
                            + sample.getSampledescribe() + ";样品数量:" + sample.getSampleyangpinshuliang() + "/" + sample.getSampleyangpinshuliang2() + "/" + sample.getSampleyangpinshuliang3();
                    sampleMapper.updateTableHeaderByTiNo(jlypxx3, sample.getSampleno(), "JJ0201a3");
                }
            }
        } else {
            sample.setSamplename(oConvertUtils.isEmpty(map.get("sampleName")) ? "" : map.get("sampleName").toString());
            sample.setSampledescribe(oConvertUtils.isEmpty(map.get("sampleDescribe")) ? "" : map.get("sampleDescribe").toString());
            sample.setSampledate(oConvertUtils.isEmpty(map.get("sampleDate")) ? "" : map.get("sampleDate").toString());
            sample.setSamplechandi(oConvertUtils.isEmpty(map.get("sampleChanDi")) ? "" : map.get("sampleChanDi").toString());
            sample.setSampledaibiaoshuliang(oConvertUtils.isEmpty(map.get("sampleDaiBiaoShuLiang")) ? "" : map.get("sampleDaiBiaoShuLiang").toString());
            sample.setSampleshiyanzushu(oConvertUtils.isEmpty(map.get("sampleShiYanZuShu")) ? "" : map.get("sampleShiYanZuShu").toString());
            sample.setSampleguigexinghao(oConvertUtils.isEmpty(map.get("sampleGuiGeXingHao")) ? "" : map.get("sampleGuiGeXingHao").toString());
            sample.setSamplequyangdidian(oConvertUtils.isEmpty(map.get("sampleQuYangDiDian")) ? "" : map.get("sampleQuYangDiDian").toString());
            sample.setSampleshengchanchangjia(oConvertUtils.isEmpty(map.get("sampleShengChanChangJia")) ? "" : map.get("sampleShengChanChangJia").toString());
            sample.setSamplechuchangbianhao(oConvertUtils.isEmpty(map.get("sampleChuChangBianHao")) ? "" : map.get("sampleChuChangBianHao").toString());
            sample.setSampleqiangdudengji(oConvertUtils.isEmpty(map.get("sampleQiangDuDengJi")) ? "" : map.get("sampleQiangDuDengJi").toString());
            sample.setSamplejiaobanfangshi(oConvertUtils.isEmpty(map.get("sampleJiaoBanFangShi")) ? "" : map.get("sampleJiaoBanFangShi").toString());
            sample.setSamplehunningtuzhonglei(oConvertUtils.isEmpty(map.get("sampleHunNingTuZhongLei")) ? "" : map.get("sampleHunNingTuZhongLei").toString());
            sample.setSampleyanghutiaojian(oConvertUtils.isEmpty(map.get("sampleYangHuTiaoJian")) ? "" : map.get("sampleYangHuTiaoJian").toString());
            sample.setSampleshajiangzhonglei(oConvertUtils.isEmpty(map.get("sampleShaJiangZhongLei")) ? "" : map.get("sampleShaJiangZhongLei").toString());
            sample.setSampleshengchanriqi(oConvertUtils.isEmpty(map.get("sampleShengChanRIQi")) ? "" : map.get("sampleShengChanRIQi").toString());
            sample.setSampleshengchanpihao(oConvertUtils.isEmpty(map.get("sampleShengChanPiHao")) ? "" : map.get("sampleShengChanPiHao").toString());
            sample.setSamplepinzhongdengji(oConvertUtils.isEmpty(map.get("samplePinZhongDengJi")) ? "" : map.get("samplePinZhongDengJi").toString());
            sample.setSampleshejijiliang(oConvertUtils.isEmpty(map.get("sampleSheJiJiLiang")) ? "" : map.get("sampleSheJiJiLiang").toString());
            sample.setSamplejieheliaozhonglei(oConvertUtils.isEmpty(map.get("sampleJieHeLiaoZhongLei")) ? "" : map.get("sampleJieHeLiaoZhongLei").toString());
            sample.setSampleliqingbiaohao(oConvertUtils.isEmpty(map.get("sampleLiQingBiaoHao")) ? "" : map.get("sampleLiQingBiaoHao").toString());
            sample.setSampleliqingzhonglei(oConvertUtils.isEmpty(map.get("sampleLiQingZhongLei")) ? "" : map.get("sampleLiQingZhongLei").toString());
            sample.setSamplejipeileixing(oConvertUtils.isEmpty(map.get("sampleJiPeiLeiXing")) ? "" : map.get("sampleJiPeiLeiXing").toString());
            sample.setSampleshiyancengwei(oConvertUtils.isEmpty(map.get("sampleShiYanCengWei")) ? "" : map.get("sampleShiYanCengWei").toString());
            sample.setSampleliqinghunheliaoleixing(oConvertUtils.isEmpty(map.get("sampleLiQingHunHeLiaoLeiXing")) ? "" : map.get("sampleLiQingHunHeLiaoLeiXing").toString());
            sample.setSamplegangjinzhijing(oConvertUtils.isEmpty(map.get("sampleGangJinZhiJing")) ? "" : map.get("sampleGangJinZhiJing").toString());
            sample.setSamplegangjinzhonglei(oConvertUtils.isEmpty(map.get("sampleGangJinZhongLei")) ? "" : map.get("sampleGangJinZhongLei").toString());
            sample.setSamplelingqi(oConvertUtils.isEmpty(map.get("sampleLingQi")) ? "" : map.get("sampleLingQi").toString());

            //计算最大龄期
            if (oConvertUtils.isNotEmpty(map.get("sampleLingQi"))) {
                String LingQi = map.get("sampleLingQi").toString().replaceAll("\\|", "");
                String[] SampleLingQi1 = LingQi.split(",");
                if (SampleLingQi1.length == 0) {
                    sample.setSamplelingqimax("");
                } else {
                    String sampleLingQiMax = SampleLingQi1[0];
                    for (int j = 0; j < SampleLingQi1.length; j++) {
                        if (Integer.parseInt(sampleLingQiMax) <= Integer.parseInt(SampleLingQi1[j])) {
                            sampleLingQiMax = SampleLingQi1[j];
                        }
                    }
                    sample.setSamplelingqimax(sampleLingQiMax);
                }
            }
            sample.setSamplewaijiajichanliang(oConvertUtils.isEmpty(map.get("sampleWaiJiaJiChanLiang")) ? "" : map.get("sampleWaiJiaJiChanLiang").toString());
            sample.setSamplequyangren(oConvertUtils.isEmpty(map.get("sampleQuYangRen")) ? "" : map.get("sampleQuYangRen").toString());
            sample.setSampleremark(oConvertUtils.isEmpty(map.get("sampleRemark")) ? "" : map.get("sampleRemark").toString());
            sample.setSamplechandi2(map.get("sampleChanDi2") == null ? "" : map.get("sampleChanDi2").toString());
            sample.setSamplechandi3(map.get("sampleChanDi3") == null ? "" : map.get("sampleChanDi3").toString());
            sample.setSamplequyangdidian2(map.get("sampleQuYangDiDian2") == null ? "" : map.get("sampleQuYangDiDian2").toString());
            sample.setSamplequyangdidian3(map.get("sampleQuYangDiDian3") == null ? "" : map.get("sampleQuYangDiDian3").toString());
            sample.setSampledate2(map.get("sampleDate2") == null ? "" : map.get("sampleDate2").toString());
            sample.setSampledate3(map.get("sampleDate3") == null ? "" : map.get("sampleDate3").toString());
            sample.setSampledaibiaoshuliang2(map.get("sampleDaiBiaoShuLiang2") == null ? "" : map.get("sampleDaiBiaoShuLiang2").toString());
            sample.setSampledaibiaoshuliang3(map.get("sampleDaiBiaoShuLiang3") == null ? "" : map.get("sampleDaiBiaoShuLiang3").toString());
            sample.setSampledaibiaoshuliang4(map.get("sampleDaiBiaoShuLiang4") == null ? "" : map.get("sampleDaiBiaoShuLiang4").toString());
            sample.setSampledaibiaoshuliang5(map.get("sampleDaiBiaoShuLiang5") == null ? "" : map.get("sampleDaiBiaoShuLiang5").toString());
            sample.setSampleguigexinghao2(map.get("sampleGuiGeXingHao2") == null ? "" : map.get("sampleGuiGeXingHao2").toString());
            sample.setSampleguigexinghao3(map.get("sampleGuiGeXingHao3") == null ? "" : map.get("sampleGuiGeXingHao3").toString());
            sample.setSampleguigexinghao4(map.get("sampleGuiGeXingHao4") == null ? "" : map.get("sampleGuiGeXingHao4").toString());
            sample.setSampleguigexinghao5(map.get("sampleGuiGeXingHao5") == null ? "" : map.get("sampleGuiGeXingHao5").toString());
            sample.setSamplegongchengbihou(map.get("sampleGongChengBiHou") == null ? "" : map.get("sampleGongChengBiHou").toString());
            sample.setYcjcwtdbh(map.get("wztzid") == null ? "" : map.get("wztzid").toString());
            sample.setSampleyangpinshuliang(map.get("sampleYangPinShuLiang") == null ? "" : map.get("sampleYangPinShuLiang").toString());
            sample.setSampleyangpinshuliang2(map.get("sampleYangPinShuLiang2") == null ? "" : map.get("sampleYangPinShuLiang2").toString());
            sample.setSampleyangpinshuliang3(map.get("sampleYangPinShuLiang3") == null ? "" : map.get("sampleYangPinShuLiang3").toString());
            sample.setSampleyangpinshuliang4(map.get("sampleYangPinShuLiang4") == null ? "" : map.get("sampleYangPinShuLiang4").toString());
            sample.setSampleyangpinshuliang5(map.get("sampleYangPinShuLiang5") == null ? "" : map.get("sampleYangPinShuLiang5").toString());
            sample.setSamplechangdu(map.get("sampleChangDu") == null ? "" : map.get("sampleChangDu").toString());
            sample.setShikuaichicun(map.get("shikuaichicun") == null ? "" : map.get("shikuaichicun").toString());
            sample.setSampleliuyangshuliang(map.get("sampleLiuYangShuLiang") == null ? "" : map.get("sampleLiuYangShuLiang").toString());
            sample.setSamplezhijianriqi(map.get("sampleZhiJianRiQi") == null ? "" : map.get("sampleZhiJianRiQi").toString());
            sample.setRigidrreboundshebeino(map.get("RigidrReboundshebeiNo") == null ? "" : map.get("RigidrReboundshebeiNo").toString());
            sample.setRigidrreboundshebeicj(map.get("RigidrReboundshebeiCJ") == null ? "" : map.get("RigidrReboundshebeiCJ").toString());
            if (addstate) {
                sample.setSyzt(1);
            } else {
                sample.setSyzt(2);
            }
            sample.setSampleisdel(0);
            sample.setSamplestate(0);
            sample.setDepartid(depart.getId());
            sample.setOrgcode(depart.getOrgCode());
//                sample.setSampledepartid(depart1.getId());
            sample.setSampledepartid(depart.getId());
            sample.setSamplecreatedate(sd.format(new Date()));
            sample.setSamplecreateperson(loginUser.getUsername());
            if (oConvertUtils.isNotEmpty(map.get("shifouliuyang"))) {
                sample.setShifouliuyang(Integer.valueOf(map.get("shifouliuyang").toString()));
            }
            sample.setLiuyangriqi(oConvertUtils.isNotEmpty(map.get("liuyangriqi")) ? map.get("liuyangriqi").toString() : "");
            if (oConvertUtils.isNotEmpty(map.get("liuyangqixian"))) {
                sample.setLiuyangqixian(Integer.valueOf(map.get("liuyangqixian").toString()));
            }
            sample.setLiuyangchuli(oConvertUtils.isNotEmpty(map.get("liuyangchuli")) ? map.get("liuyangriqi").toString() : "");
            sample.setJilubiaoqianzhangzhuangtai("-1");
            sample.setBaogaoqianzhangzhuangtai("-1");
            sample.setBaoyandanqianzhangzhuangtai("-1");
            sample.setShenpibiaoqianzhangzhuangtai("-1");
            sample.setShenpizhuangtai(0);
            sample.setQianzhangzhuangtai(0);
            sample.setZhuanfazhuangtai(0);
            sample.setProjectid(oConvertUtils.isEmpty(map.get("projectId")) ? "" : map.get("projectId").toString());
            if (oConvertUtils.isNotEmpty(map.get("sampleGcbw"))) {
                sample.setSamplegcbw(map.get("sampleGcbw").toString());
            }
            String yp = "";
            String jl = "";
            String bg = "";
            String by = "";
            String sp = "";
            if (oConvertUtils.isNotEmpty(map.get("insert"))) {
                String insert = map.get("insert").toString();
                sample.setInsertno(insert);
                Map<String, Object> current = sampleMapper.getOneSql12(insert);
                String currentCode = "";
                if (current != null) {
                    Long count = sampleMapper.getCount3(insert, insert + "%");
                    Integer c = Integer.valueOf(current.get("currentCode").toString());
                    if (count < c) {
                        for (int i = 1; i <= c; i++) {
                            String n = autoGenericCode(String.valueOf(i), 2);
                            Map<String, Object> m = sampleMapper.getOneSql13(insert + "-" + n);
                            if (m == null) {
                                currentCode = n;
                                break;
                            }
                        }
                    } else {
                        currentCode = getFlowNumber(2, current.get("currentCode").toString());
                        sampleMapper.updateSql28(currentCode, current.get("id").toString());
                    }
                } else {
                    currentCode = getFlowNumber(2, "");
                    String uuid = UUID.randomUUID().toString();
                    sampleMapper.insertSql3(uuid, currentCode, insert);
                }
                yp = insert + "-" + currentCode;
                jl = yp.replace("YP-", "JL-");
                bg = yp.replace("YP-", "BG-");
                by = yp.replace("YP-", "BY-");
                sp = yp.replace("YP-", "SP-");
            } else {
                yp = getCoding(sampleCodingRules, codingDateFormat, codingFlowNumerDigit,
                        "NoFlowNumber",
                        StringUtil.isNotEmpty(rules.getContractNumberCode()) ? rules.getContractNumberCode()
                                : depart.getOrgCategory(),
                        itemType.getTitsamplemark(), sample.getSampledate(), map.get("ptiNo").toString());
                // 获取当前编号
                Map<String, Object> current = sampleMapper.getSql1(yp);
                // 更新编号
                String currentCode = "";
                if (current != null) {
                    Long count = sampleMapper.getCount4(yp + "%");
                    Integer c = Integer.valueOf(current.get("currentCode").toString());
                    if (count < c) {
                        // 获取所有同类型编号
                        List<Map<String, Object>> ypbhList = sampleMapper.getSql2(yp + "%");
                        Map<String, Object> mypbhMap = new HashMap<>();
                        for (int j = 0; j < ypbhList.size(); j++) {
                            mypbhMap.put(ypbhList.get(j).get("sampleNo").toString(), "sampleNo");
                        }
                        for (int i = 0; i < c; i++) {
                            String n = "";
                            if (i != 0) {
                                n = autoGenericCode(String.valueOf(i),
                                        Integer.valueOf(depart.getOrgType()));
                            }
                            String ypbh = getCoding(sampleCodingRules, codingDateFormat,
                                    codingFlowNumerDigit, n,
                                    StringUtil.isNotEmpty(rules.getContractNumberCode())
                                            ? rules.getContractNumberCode()
                                            : depart.getOrgCategory(),
                                    itemType.getTitsamplemark(), sample.getSampledate(),
                                    map.get("ptiNo").toString());
                            if (mypbhMap.get(ypbh) == null) {
                                currentCode = n;
                                break;
                            }
                        }
                    } else {
                        currentCode = getFlowNumber(Integer.valueOf(codingFlowNumerDigit),
                                current.get("currentCode").toString());
                        sampleMapper.updateSql29(currentCode, current.get("id").toString());
                        currentCode = current.get("currentCode").toString();
                    }
                } else {
                    currentCode = getFlowNumber(Integer.valueOf(codingFlowNumerDigit), "");
                    String uuid = UUID.randomUUID().toString();
                    sampleMapper.insertSql3(uuid, currentCode, yp);
                    currentCode = "";
                }
                // 监理和试验室用选择部门的合同号
                yp = getCoding(sampleCodingRules, codingDateFormat, codingFlowNumerDigit, currentCode,
                        StringUtil.isNotEmpty(rules.getContractNumberCode()) ? rules.getContractNumberCode()
                                : depart.getOrgCategory(),
                        itemType.getTitsamplemark(), sample.getSampledate(), map.get("ptiNo").toString());
                jl = getCoding(recordCodingRules, codingDateFormat, codingFlowNumerDigit, currentCode,
                        StringUtil.isNotEmpty(rules.getContractNumberCode()) ? rules.getContractNumberCode()
                                : depart.getOrgCategory(),
                        itemType.getTitsamplemark(), sample.getSampledate(), map.get("ptiNo").toString());
                bg = getCoding(reportCodingRules, codingDateFormat, codingFlowNumerDigit, currentCode,
                        StringUtil.isNotEmpty(rules.getContractNumberCode()) ? rules.getContractNumberCode()
                                : depart.getOrgCategory(),
                        itemType.getTitsamplemark(), sample.getSampledate(), map.get("ptiNo").toString());
                by = getCoding(reportingSheetCodingRules, codingDateFormat, codingFlowNumerDigit,
                        currentCode,
                        StringUtil.isNotEmpty(rules.getContractNumberCode()) ? rules.getContractNumberCode()
                                : depart.getOrgCategory(),
                        itemType.getTitsamplemark(), sample.getSampledate(), map.get("ptiNo").toString());
                sp = getCoding(approvalTableCodingRules, codingDateFormat, codingFlowNumerDigit,
                        currentCode,
                        StringUtil.isNotEmpty(rules.getContractNumberCode()) ? rules.getContractNumberCode()
                                : depart.getOrgCategory(),
                        itemType.getTitsamplemark(), sample.getSampledate(), map.get("ptiNo").toString());
            }
            sample.setTitcode(itemType.getTitcode());

            sample.setSampleno(yp);
//				sample.setSampleNoNew(StringUtil.isEmpty(sampleNoNew) ? yp : sampleNoNew);
//				sample.setTableNumberNew(StringUtil.isEmpty(tableNumberNew) ? jl : tableNumberNew);
//				sample.setReportNoNew(StringUtil.isEmpty(reportNoNew) ? bg : reportNoNew);
            sample.setSamplenonew(yp);
            sample.setTablenumbernew(jl);
            sample.setReportnonew(bg);
            sample.setReportingsheetnonew(by);
            sample.setApprovaltablenonew(sp);

            String[] tiNos = map.get("tiNos").toString().split(",");
            reportM = new SyDpsSyReportM();
            reportM.setSampleno(yp);
            reportM.setReportno(bg);
            reportM.setTablenumber(jl);
            reportM.setDepartid(depart.getId());
            reportM.setOrgcode(depart.getOrgCode());
            reportM.setProjectcode(oConvertUtils.isEmpty(map.get("project")) ? "" : map.get("project").toString());
            reportM.setProjectname(oConvertUtils.isEmpty(map.get("projectName")) ? "" : map.get("projectName").toString());
            reportM.setReportcreatedate(sd.format(new Date()));
            reportM.setReporteditperson(loginUser.getUsername());

            SyDpsSyReportS reportS = new SyDpsSyReportS();
            reportS.setTino(map.get("ptiNo").toString());
            reportS.setReportno(bg);
            reportS.setSampleno(sample.getSampleno());
            reportS.setTitype(1);
            reportS.setEditstate(0);
            reportS.setTinotemp(1);
            reportSlist.add(reportS);

            SyDpsSyTableheader tableHeader = new SyDpsSyTableheader();
            SyDpsJcTestitem item = sampleMapper.selectTestItemByTino(map.get("ptiNo").toString());

            if (item!=null) {
                if (oConvertUtils.isNotEmpty(map.get("ShiYanYiJu"))) {
                    tableHeader.setShiyanyiju(map.get("ShiYanYiJu").toString());
                } else {
                    if (StringUtil.isNotEmpty(item.getTishiyanyiju())) {
                        tableHeader.setShiyanyiju(item.getTishiyanyiju());
                    } else {
                        tableHeader.setShiyanyiju(itemType.getTishiyanyiju());
                    }
                }
                if (oConvertUtils.isNotEmpty(map.get("PanDingYiJu"))) {
                    tableHeader.setPandingyiju(map.get("PanDingYiJu").toString());
                } else {
                    if (StringUtil.isNotEmpty(item.getTipandingyiju())) {
                        tableHeader.setPandingyiju(item.getTipandingyiju());
                    } else {
                        tableHeader.setPandingyiju(itemType.getTipandingyiju());
                    }
                }
            }
            tableHeader.setDepartid(depart.getId());

//                tableHeader.setGongchengmingcheng(depart2.getDepartfullname());
            tableHeader.setGongchengmingcheng(depart.getDepartName());
//                tableHeader.setDepartfullname(depart2.getDepartfullname());
            tableHeader.setDepartfullname(depart.getDepartName());
//                tableHeader.setGongzhangmingcheng(depart2.getGongzhangmingcheng());
            tableHeader.setGongzhangmingcheng(depart.getDepartName());

//                tableHeader.setShigongdanwei(depart1.getParentlaboratoryname());
            tableHeader.setShigongdanwei(depart.getDepartName());
//                tableHeader.setHetonghao(depart2.getContractnumber());
//                tableHeader.setJianlidanwei(depart1.getJianlidanwei());
            tableHeader.setJianlidanwei(depart.getDepartName());
//                tableHeader.setZonbaodanwei(depart1.getZonbaodanwei());
            tableHeader.setZonbaodanwei(depart.getDepartName());

            tableHeader.setSamplegcbw(sample.getSamplegcbw());
            tableHeader.setTablenumber(jl);
            tableHeader.setSampleno(yp);
            tableHeader.setReportno(bg);
            tableHeader.setReportingsheetno(by);
            tableHeader.setApprovaltableno(sp);
            tableHeader.setTino(map.get("ptiNo").toString());
            tableHeader.setTinotemp(1);

            tableHeader.setSamplename(sample.getSamplename());
            tableHeader.setSampledescribe(sample.getSampledescribe());
            tableHeader.setSamplequyangdidian(sample.getSamplequyangdidian());
            tableHeader.setSampledaibiaoshuliang(sample.getSampledaibiaoshuliang());
            tableHeader.setSampleshengchanchangjia(sample.getSampleshengchanchangjia());
            tableHeader.setSamplepihao(sample.getSampleshengchanpihao());
            tableHeader.setSampleyanghutiaojian(sample.getSampleyanghutiaojian());
            tableHeader.setSampledate(sample.getSampledate());
            tableHeader.setSamplechandi(sample.getSamplechandi());
            tableHeader.setSampleshengchanpihao(sample.getSampleshengchanpihao());
            tableHeader.setSamplepinzhongdengji(sample.getSamplepinzhongdengji());
            tableHeader.setSampleshejijiliang(sample.getSampleshejijiliang());
            tableHeader.setSamplejieheliaozhonglei(sample.getSamplejieheliaozhonglei());
            tableHeader.setSampleliqingbiaohao(sample.getSampleliqingbiaohao());
            tableHeader.setSampleliqingzhonglei(sample.getSampleliqingzhonglei());
            tableHeader.setSamplejipeileixing(sample.getSamplejipeileixing());
            tableHeader.setSampleshiyancengwei(sample.getSampleshiyancengwei());
            tableHeader.setSampleliqinghunheliaoleixing(sample.getSampleliqinghunheliaoleixing());
            tableHeader.setSamplegangjinzhijing(sample.getSamplegangjinzhijing());
            tableHeader.setSamplegangjinzhonglei(sample.getSamplegangjinzhonglei());
            tableHeader.setSamplelingqi(sample.getSamplelingqi());
            tableHeader.setSamplewaijiajichanliang(sample.getSamplewaijiajichanliang());
            tableHeader.setSampleshengchanriqi(sample.getSampleshengchanriqi());
            tableHeader.setSampleguigexinghao(sample.getSampleguigexinghao());
            tableHeader.setSamplequyangren(sample.getSamplequyangren());
            tableHeader.setSamplechuchangbianhao(sample.getSamplechuchangbianhao());
            tableHeader.setSampleqiangdudengji(sample.getSampleqiangdudengji());
            tableHeader.setSamplejiaobanfangshi(sample.getSamplejiaobanfangshi());
            tableHeader.setSamplehunningtuzhonglei(sample.getSamplehunningtuzhonglei());
            tableHeader.setSampleshajiangzhonglei(sample.getSampleshajiangzhonglei());
            tableHeader.setSampleshiyanzushu(sample.getSampleshiyanzushu());
            tableHeader.setSamplechandi2(sample.getSamplechandi2());
            tableHeader.setSamplechandi3(sample.getSamplechandi3());
            tableHeader.setSamplequyangdidian2(sample.getSamplequyangdidian2());
            tableHeader.setSamplequyangdidian3(sample.getSamplequyangdidian3());
            tableHeader.setSampledate2(sample.getSampledate2());
            tableHeader.setSampledate3(sample.getSampledate3());
            tableHeader.setSampledaibiaoshuliang2(sample.getSampledaibiaoshuliang2());
            tableHeader.setSampledaibiaoshuliang3(sample.getSampledaibiaoshuliang3());
            tableHeader.setSampledaibiaoshuliang4(sample.getSampledaibiaoshuliang4());
            tableHeader.setSampledaibiaoshuliang5(sample.getSampledaibiaoshuliang5());
            tableHeader.setSampleguigexinghao2(sample.getSampleguigexinghao2());
            tableHeader.setSampleguigexinghao3(sample.getSampleguigexinghao3());
            tableHeader.setSampleguigexinghao4(sample.getSampleguigexinghao4());
            tableHeader.setSampleguigexinghao5(sample.getSampleguigexinghao5());
            tableHeader.setSamplegongchengbihou(sample.getSamplegongchengbihou());
            tableHeader.setYcjcwtdbh(sample.getYcjcwtdbh());
            tableHeader.setSampleyangpinshuliang(sample.getSampleyangpinshuliang());
            tableHeader.setSampleyangpinshuliang2(sample.getSampleyangpinshuliang2());
            tableHeader.setSampleyangpinshuliang3(sample.getSampleyangpinshuliang3());
            tableHeader.setSampleyangpinshuliang4(sample.getSampleyangpinshuliang4());
            tableHeader.setSampleyangpinshuliang5(sample.getSampleyangpinshuliang5());
            tableHeader.setSamplechangdu(sample.getSamplechangdu());
            tableHeader.setSamplezhijianriqi(sample.getSamplezhijianriqi());

            tableHeader.setSamplenonew(sample.getSamplenonew());
            tableHeader.setTablenumbernew(sample.getTablenumbernew());
            tableHeader.setReportnonew(sample.getReportnonew());
            tableHeader.setReportingsheetnonew(sample.getReportingsheetnonew());
            tableHeader.setApprovaltablenonew(sample.getApprovaltablenonew());

            if ("".equals(depart.getOrgType())) {
                tableHeader.setWtrwno("委托编号");
                tableHeader.setWtsgdw("委托单位");
            } else {
                tableHeader.setWtrwno("任务编号");
                tableHeader.setWtsgdw("施工单位");
            }
            tableHeader.setYpxx(getYpxx(itemType.getTiyangpinxinxibg(), sample));
            String ypxxString = tableHeader.getYpxx();
            if ("0201".equals(itemType.getTitcode())) {
                ypxxString = ypxxString.substring(0, ypxxString.length() - 1) + "/" + sample.getSampleyangpinshuliang2() + "/" + sample.getSampleyangpinshuliang3();
                tableHeader.setYpxx(ypxxString);
            }
            tableHeaderlist.add(tableHeader);
            Map<String, Object> table = sampleMapper.getSql3(map.get("ptiNo").toString());
            if (table!=null) {
                sampleMapper.insertSql2(table.get("tiTableNum").toString(), UUID.randomUUID().toString(), map.get("ptiNo").toString(), 1, yp, jl, bg, depart.getId());
            }
            List<String> deivces = new ArrayList<>();
            for (int i = 0; i < tiNos.length; i++) {
                table = sampleMapper.getSql4(tiNos[i]);
                if (table!=null) {
                    tiNames += table.get("tiName") + ",";
                }
                reportS = new SyDpsSyReportS();
                reportS.setTino(tiNos[i]);
                reportS.setReportno(bg);
                reportS.setSampleno(sample.getSampleno());
                if (table!=null) {
                    reportS.setTitype(Integer.valueOf(table.get("tiType").toString()));
                }
                reportS.setEditstate(0);
                reportS.setTinotemp(1);
                reportSlist.add(reportS);

                tableHeader = new SyDpsSyTableheader();
                SyDpsJcTestitem items = sampleMapper.findByTiNo(tiNos[i]);
                if (items!=null) {
                    if (oConvertUtils.isNotEmpty(map.get("ShiYanYiJu"))) {
                        tableHeader.setShiyanyiju(map.get("ShiYanYiJu").toString());
                    } else {
                        if (StringUtil.isNotEmpty(items.getTishiyanyiju())) {
                            tableHeader.setShiyanyiju(items.getTishiyanyiju());
                        } else {
                            tableHeader.setShiyanyiju(itemType.getTishiyanyiju());
                        }
                    }
                    if (oConvertUtils.isNotEmpty(map.get("PanDingYiJu"))) {
                        tableHeader.setPandingyiju(map.get("PanDingYiJu").toString());
                    } else {
                        if (StringUtil.isNotEmpty(items.getTipandingyiju())) {
                            tableHeader.setPandingyiju(items.getTipandingyiju());
                        } else {
                            tableHeader.setPandingyiju(itemType.getTipandingyiju());
                        }
                    }
                }
                tableHeader.setDepartid(depart.getId());

//                    tableHeader.setGongchengmingcheng(depart2.getDepartfullname());
                tableHeader.setGongchengmingcheng(depart.getDepartName());
//                    tableHeader.setDepartfullname(depart2.getDepartfullname());
                tableHeader.setDepartfullname(depart.getDepartName());
//                    tableHeader.setGongzhangmingcheng(depart2.getGongzhangmingcheng());
                tableHeader.setGongzhangmingcheng(depart.getDepartName());

                tableHeader.setShigongdanwei(depart.getDepartName());
//                    tableHeader.setShigongdanwei(depart1.getParentlaboratoryname());
//                    tableHeader.setHetonghao(depart2.getContractnumber());
                // 取登錄用戶的合同號
                tableHeader.setJianlidanwei(depart.getDepartName());
//                    tableHeader.setJianlidanwei(depart1.getJianlidanwei());
                tableHeader.setZonbaodanwei(depart.getDepartName());
//                    tableHeader.setZonbaodanwei(depart1.getZonbaodanwei());

                tableHeader.setSamplegcbw(sample.getSamplegcbw());
                tableHeader.setSampleno(yp);
                tableHeader.setReportno(bg);
                tableHeader.setTablenumber(jl);
                tableHeader.setReportingsheetno(by);
                tableHeader.setApprovaltableno(sp);
                tableHeader.setTinotemp(1);
                tableHeader.setTino(tiNos[i]);

                tableHeader.setSamplename(sample.getSamplename());
                tableHeader.setSampledescribe(sample.getSampledescribe());
                tableHeader.setSamplequyangdidian(sample.getSamplequyangdidian());
                tableHeader.setSampledaibiaoshuliang(sample.getSampledaibiaoshuliang());
                tableHeader.setSampleshengchanchangjia(sample.getSampleshengchanchangjia());
                tableHeader.setSamplepihao(sample.getSampleshengchanpihao());
                tableHeader.setSampleyanghutiaojian(sample.getSampleyanghutiaojian());
                tableHeader.setSampledate(sample.getSampledate());
                tableHeader.setSamplechandi(sample.getSamplechandi());
                tableHeader.setSampleshengchanpihao(sample.getSampleshengchanpihao());
                tableHeader.setSamplepinzhongdengji(sample.getSamplepinzhongdengji());
                tableHeader.setSampleshejijiliang(sample.getSampleshejijiliang());
                tableHeader.setSamplejieheliaozhonglei(sample.getSamplejieheliaozhonglei());
                tableHeader.setSampleliqingbiaohao(sample.getSampleliqingbiaohao());
                tableHeader.setSampleliqingzhonglei(sample.getSampleliqingzhonglei());
                tableHeader.setSamplejipeileixing(sample.getSamplejipeileixing());
                tableHeader.setSampleshiyancengwei(sample.getSampleshiyancengwei());
                tableHeader.setSampleliqinghunheliaoleixing(sample.getSampleliqinghunheliaoleixing());
                tableHeader.setSamplegangjinzhijing(sample.getSamplegangjinzhijing());
                tableHeader.setSamplegangjinzhonglei(sample.getSamplegangjinzhonglei());
                tableHeader.setSamplelingqi(sample.getSamplelingqi());
                tableHeader.setSamplewaijiajichanliang(sample.getSamplewaijiajichanliang());
                tableHeader.setSampleshengchanriqi(sample.getSampleshengchanriqi());
                tableHeader.setSampleguigexinghao(sample.getSampleguigexinghao());
                tableHeader.setSamplequyangren(sample.getSamplequyangren());
                tableHeader.setSamplechuchangbianhao(sample.getSamplechuchangbianhao());
                tableHeader.setSampleqiangdudengji(sample.getSampleqiangdudengji());
                tableHeader.setSamplejiaobanfangshi(sample.getSamplejiaobanfangshi());
                tableHeader.setSamplehunningtuzhonglei(sample.getSamplehunningtuzhonglei());
                tableHeader.setSampleshajiangzhonglei(sample.getSampleshajiangzhonglei());
                tableHeader.setSampleshiyanzushu(sample.getSampleshiyanzushu());
                tableHeader.setSamplechandi2(sample.getSamplechandi2());
                tableHeader.setSamplechandi3(sample.getSamplechandi3());
                tableHeader.setSamplequyangdidian2(sample.getSamplequyangdidian2());
                tableHeader.setSamplequyangdidian3(sample.getSamplequyangdidian3());
                tableHeader.setSampledate2(sample.getSampledate2());
                tableHeader.setSampledate3(sample.getSampledate3());
                tableHeader.setSampledaibiaoshuliang2(sample.getSampledaibiaoshuliang2());
                tableHeader.setSampledaibiaoshuliang3(sample.getSampledaibiaoshuliang3());
                tableHeader.setSampledaibiaoshuliang4(sample.getSampledaibiaoshuliang4());
                tableHeader.setSampledaibiaoshuliang5(sample.getSampledaibiaoshuliang5());
                tableHeader.setSampleguigexinghao2(sample.getSampleguigexinghao2());
                tableHeader.setSampleguigexinghao3(sample.getSampleguigexinghao3());
                tableHeader.setSampleguigexinghao4(sample.getSampleguigexinghao4());
                tableHeader.setSampleguigexinghao5(sample.getSampleguigexinghao5());
                tableHeader.setSamplegongchengbihou(sample.getSamplegongchengbihou());
                tableHeader.setYcjcwtdbh(sample.getYcjcwtdbh());
                tableHeader.setSampleyangpinshuliang(sample.getSampleyangpinshuliang());
                tableHeader.setSampleyangpinshuliang2(sample.getSampleyangpinshuliang2());
                tableHeader.setSampleyangpinshuliang3(sample.getSampleyangpinshuliang3());
                tableHeader.setSampleyangpinshuliang4(sample.getSampleyangpinshuliang4());
                tableHeader.setSampleyangpinshuliang5(sample.getSampleyangpinshuliang5());
                tableHeader.setSamplechangdu(sample.getSamplechangdu());
                tableHeader.setSamplezhijianriqi(sample.getSamplezhijianriqi());

                tableHeader.setSamplenonew(sample.getSamplenonew());
                tableHeader.setTablenumbernew(sample.getTablenumbernew());
                tableHeader.setReportnonew(sample.getReportnonew());
                tableHeader.setReportingsheetnonew(sample.getReportingsheetnonew());
                tableHeader.setApprovaltablenonew(sample.getApprovaltablenonew());
                if ("".equals(depart.getOrgType())) {
                    tableHeader.setWtrwno("委托编号");
                    tableHeader.setWtsgdw("委托单位");
                } else {
                    tableHeader.setWtrwno("任务编号");
                    tableHeader.setWtsgdw("施工单位");
                }
                tableHeader.setYpxx(getYpxx(itemType.getTiyangpinxinxijl(), sample));
                String ypxxString2 = tableHeader.getYpxx();
                if ("0201".equals(itemType.getTitcode())) {
                    ypxxString2 = ypxxString2.substring(0, ypxxString2.length() - 1) + "/" + sample.getSampleyangpinshuliang2() + "/" + sample.getSampleyangpinshuliang3();
                    tableHeader.setYpxx(ypxxString2);
                    if ("JJ0201a".equals(tableHeader.getTino())) {
                        ypxxString2 = "取样日期:" + tableHeader.getSampledate() + ";样品名称:" + tableHeader.getSamplename() + ";样品编号:" + tableHeader.getSampleno() + "(01);样品描述:"
                                + tableHeader.getSampledescribe() + ";样品数量:" + tableHeader.getSampleyangpinshuliang() + "/" + tableHeader.getSampleyangpinshuliang2() + "/" + tableHeader.getSampleyangpinshuliang3();
                        tableHeader.setYpxx(ypxxString2);
                    } else if ("JJ0201a2".equals(tableHeader.getTino())) {
                        ypxxString2 = "取样日期:" + tableHeader.getSampledate() + ";样品名称:" + tableHeader.getSamplename() + ";样品编号:" + tableHeader.getSampleno() + "(02);样品描述:"
                                + tableHeader.getSampledescribe() + ";样品数量:" + tableHeader.getSampleyangpinshuliang() + "/" + tableHeader.getSampleyangpinshuliang2() + "/" + tableHeader.getSampleyangpinshuliang3();
                        tableHeader.setYpxx(ypxxString2);
                    } else if ("JJ0201a3".equals(tableHeader.getTino())) {
                        ypxxString2 = "取样日期:" + tableHeader.getSampledate() + ";样品名称:" + tableHeader.getSamplename() + ";样品编号:" + tableHeader.getSampleno() + "(03);样品描述:"
                                + tableHeader.getSampledescribe() + ";样品数量:" + tableHeader.getSampleyangpinshuliang() + "/" + tableHeader.getSampleyangpinshuliang2() + "/" + tableHeader.getSampleyangpinshuliang3();
                        tableHeader.setYpxx(ypxxString2);
                    }
                }
                Map<String, Object> deivcelist = sampleMapper.getSheBeiName(depart.getId(), tiNos[i], loginUser.getUsername(), itemType.getTitcode());
                List<String> deivce = new ArrayList<>();
                if (deivcelist != null && deivcelist.get("shebeiName") != null) {
                    String[] str = deivcelist.get("shebeiName").toString().split(",");
                    for (int j = 0; j < str.length; j++) {
                        if (!"".equals(str[j])) {
                            if (deivce.indexOf(str[j]) == -1) {
                                deivce.add(str[j]);
                            }
                            if (deivces.indexOf(str[j]) == -1) {
                                deivces.add(str[j]);
                            }
                        }
                    }
                }
                tableHeader.setYiqishebei(joinString(deivce, ","));
                tableHeaderlist.add(tableHeader);
                if (table!=null) {
                    if (Integer.valueOf(table.get("tiType").toString()) == 3
                            || Integer.valueOf(table.get("tiType").toString()) == 4) {
                        Map<String, Object> byd = sampleMapper.getSql5(table.get("tiTableNum").toString(), depart.getId());
                        if (byd != null) {
                            sampleMapper.insertSql1(table.get("tiTableNum").toString(), UUID.randomUUID().toString(), tiNos[i], 1, yp, jl, bg, depart.getId());
                        } else {
                            sampleMapper.insertSql1(table.get("tiTableNum").toString(), UUID.randomUUID().toString(), tiNos[i], 1, yp, jl, bg, depart.getId());
                        }
                    } else {
                        sampleMapper.insertSql1(table.get("tiTableNum").toString(), UUID.randomUUID().toString(), tiNos[i], 1, yp, jl, bg, depart.getId());
                    }
                }
            }
            tableHeader = tableHeaderlist.get(0);
            tableHeader.setYiqishebei(joinString(deivces, ","));
            sample.setTinames(tiNames);
            if (map.get("QRCode") != null) {
                String[] QRCode = map.get("QRCode").toString().split(",");
                for (int i = 0; i < QRCode.length; i++) {
                    if (!QRCode[i].equals("")) {
                        SyDpsSySampleqrcode code = new SyDpsSySampleqrcode();
                        code.setQrcode(QRCode[i]);
                        code.setSampleno(yp);
                        dpsSySampleqrcodeService.save(code);
                    }
                }
            }
            if (map.get("Img") != null) {
                String[] Img = map.get("Img").toString().split(",");
                SyDpsSySamplepic pic = new SyDpsSySamplepic();
                if (Img.length >= 1) {
                    pic.setPic1(Img[0]);
                }
                if (Img.length >= 2) {
                    pic.setPic2(Img[1]);
                }
                if (Img.length >= 3) {
                    pic.setPic3(Img[2]);
                }
                if (Img.length >= 4) {
                    pic.setPic4(Img[3]);
                }
                pic.setSampleno(yp);
                samplepicService.save(pic);
            }
            if (map.get("plusMark") != null) {
                sample.setPlusmark(1);
            } else {
                sample.setPlusmark(0);
            }
        }
        if (!StringUtil.isNotEmpty(sample.getSamplegcbw())) {
            sample.setSamplegcbw(reportM.getProjectname());
        }
        sample.setTittype(sampleMapper.selectTitTypeByTitCode(map.get("titCode").toString()));
        sample.setTino(oConvertUtils.isNotEmpty(map.get("tiNos")) ? map.get("tiNos").toString() : "");
        this.saveOrUpdate(sample);
        if (reportM != null) {
            if (null == reportM.getUuid()) {
                reportM.setUuid(UUID.randomUUID().toString());
            }
            reportMService.saveOrUpdate(reportM);
        }
        for (int i = 0; i < reportSlist.size(); i++) {
            reportSlist.get(i).setUuid(UUID.randomUUID().toString());
            reportSService.save(reportSlist.get(i));
        }
        for (int i = 0; i < tableHeaderlist.size(); i++) {
            tableheaderService.save(tableHeaderlist.get(i));
        }

        if (oConvertUtils.isNotEmpty(map.get("wztz"))) {
            SyDpsSySampleWt wt = sampleWtService.getById(map.get("wztz").toString());
            if (wt == null) {
                wt = new SyDpsSySampleWt();
                wt.setId(map.get("wztz").toString());
            }
            wt.setSampleid(sample.getId());
            wt.setSampleno(sample.getSampleno());
            wt.setQualifiedstate(0);
            sampleWtService.saveOrUpdate(wt);
        }

        if (addstate) {
            sampleMapper.updateSql30(sample.getSampleno(), map.get("jianceyiju").toString(), map.get("pandingyiju").toString(), map.get("shiyanwanchengqixian").toString(), Integer.valueOf(map.get("yuancaijinchangdengjiId").toString()));
            sampleMapper.updateSql31(sample.getSampleno(), Integer.valueOf(map.get("yuancaijinchangdengjiId").toString()));
        }
        LambdaUpdateWrapper<Wztaizhang> wrapper = new LambdaUpdateWrapper<>();
        if (oConvertUtils.isNotEmpty(map.get("wztzid"))) {
            UpdateWrapper<SyDpsYyYuancaiquyangweituo> updateWrapper = new UpdateWrapper<>();
            updateWrapper.lambda().eq(SyDpsYyYuancaiquyangweituo::getYuancaijinchangdengjiid, map.get("wztzid").toString())
                    .set(SyDpsYyYuancaiquyangweituo::getSampleno, sample.getSampleno());
            yuancaiquyangweituoService.update(updateWrapper);

            wrapper.eq(Wztaizhang::getId, map.get("wztzid").toString());
            wrapper.set(Wztaizhang::getSampleNo, sample.getSampleno());
        }
        if (oConvertUtils.isNotEmpty(map.get("gdsy"))) {
            wrapper.set(Wztaizhang::getBdzj, 1);
            SyDpsYyRenwudan renwudan = renwudanService.getOne(new QueryWrapper<SyDpsYyRenwudan>().lambda()
                    .eq(SyDpsYyRenwudan::getSampleNo, sample.getSampleno()));
            if (oConvertUtils.isEmpty(renwudan)) {
                renwudan = new SyDpsYyRenwudan();
            }
            if (oConvertUtils.isNotEmpty(map.get("wztzid"))) {
                renwudan.setYuancaijinchangdengjiid(Integer.valueOf(map.get("wztzid").toString()));
            }
            renwudan.setJiancerenyuan(map.get("jiancerenyuan").toString());
            renwudan.setJilurenyuan(map.get("jilurenyuan").toString());
            renwudan.setFuherenyuan(map.get("fuherenyuan").toString());
            renwudan.setQianfarenyuan(map.get("qianfarenyuan").toString());
            renwudan.setSampleNo(sample.getSampleno());
//            renwudan.setJiancerenyuandate(map.get("jiancerenyuandate").toString());
//            renwudan.setFuherenyuandate(map.get("fuherenyuandate").toString());
            renwudan.setJianceyiju(oConvertUtils.isEmpty(map.get("ShiYanYiJu")) ? "" : map.get("ShiYanYiJu").toString());
            renwudan.setPandingyiju(oConvertUtils.isEmpty(map.get("PanDingYiJu")) ? "" : map.get("PanDingYiJu").toString());
            String renwudanbh = UUID.randomUUID().toString();
            renwudan.setRenwudanliushuihao(renwudanbh);
            renwudanService.saveOrUpdate(renwudan);
        }
        if (oConvertUtils.isNotEmpty(map.get("wztzid"))) {
            wztaizhangService.update(wrapper);
        }
        if (oConvertUtils.isNotEmpty(map.get("xcwtdId"))) {
            SyDpsYyXianchangjianceweituo xcwt = xianchangjianceweituoService.getById(Integer.valueOf(map.get("xcwtdId").toString()));
            xcwt.setSampleno(sample.getSampleno());
            xcwt.setShiyanwanchengqixian(map.get("shiyanwanchengqixian").toString());
            xcwt.setJianceyiju(map.get("jianceyiju").toString());
            xcwt.setPandingyiju(map.get("pandingyiju").toString());
            xcwt.setSampleDate(sample.getSampledate());
            xianchangjianceweituoService.updateById(xcwt);
        }
    }


    private static final String TINOS = "JJ0406，JJ0406a，CS312，JJ0501a，JJ0501b1，JJ0501a1，JJ0501c，JJ0501d，JJ1001b_GJ，JJ1412，JJ1001a，JJ1001b，JJ1001c，JGLQ15002，JJ0520b";

    @Override
    public Map getSList(String id) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String orgCode = loginUser.getOrgCode();
        if (id.indexOf(",") == -1) {
            SyDpsSySample sample = sampleMapper.selectById(id);
            List<Map<String, Object>> list = sampleMapper.selectBySam(sample.getSampleno());
            list.forEach(k -> {
                if (TINOS.contains(k.get("tiNo").toString())) {
                    k.put("tq", 1);
                } else {
                    k.put("tq", 0);
                }
            });
            List<Map<String, Object>> device = sampleMapper.getDevice(orgCode, sample.getTitcode(), df.format(new Date()));
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("sample", sample);
            map.put("list", list);
            map.put("device", device);
            return map;
        } else {
            String[] ids = id.split(",");
            List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
            for (int i = 0; i < ids.length; i++) {
                SyDpsSySample sample = sampleMapper.selectById(ids[i]);
                list.add(sampleMapper.selectBySam2(sample.getSampleno()));
            }
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("list", list);
            return map;
        }
    }

    @Override
    public Map getBgSList(String id) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String orgCode = loginUser.getOrgCode();
        if (id.indexOf(",") == -1) {
            SyDpsSySample sample = sampleMapper.selectById(id);
            List<Map<String, Object>> list = sampleMapper.selectBgBySam(sample.getSampleno());
            List<Map<String, Object>> device = sampleMapper.getDevice(orgCode, sample.getTitcode(), df.format(new Date()));
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("sample", sample);
            map.put("list", list);
            map.put("device", device);
            return map;
        } else {
            String[] ids = id.split(",");
            List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
            for (int i = 0; i < ids.length; i++) {
                SyDpsSySample sample = sampleMapper.selectById(ids[i]);
                list.add(sampleMapper.selectBgBySam2(sample.getSampleno()));
            }
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("list", list);
            return map;
        }
    }

    public static String sciCal(double value, int digit) {
        String result = "-999";
        try {
            double ratio = Math.pow(10, digit);
            double _num = value * ratio;
            double mod = _num % 1;
            double integer = Math.floor(_num);
            double returnNum;
            if (mod > 0.5) {
                returnNum = (integer + 1) / ratio;
            } else if (mod < 0.5) {
                returnNum = integer / ratio;
            } else {
                returnNum = (integer % 2 == 0 ? integer : integer + 1) / ratio;
            }
            BigDecimal bg = new BigDecimal(returnNum);
            result = bg.setScale((int) digit, BigDecimal.ROUND_HALF_UP).toString();
        } catch (RuntimeException e) {
            throw e;
        }
        return result;
    }

    /**
     * 四舍六入五成双
     *
     * @param str 要修约的数字
     * @param s   保留小数位
     * @return
     */
    public static String FourSSixRFiveCS(double str, int s) {
        NumberFormat nfFormat = NumberFormat.getInstance();
        nfFormat.setMaximumFractionDigits(s);
        return nfFormat.format(str);
    }

    public List<String> asList(String[] strings) {
        List<String> list = new ArrayList<String>();
        for (int i = 0; i < strings.length; i++) {
            list.add(strings[i]);
        }
        return list;
    }

    public String getYpxx(String ypxx, SyDpsSySample sample) {
        String xx = "";
        try {
            if (StringUtil.isNotEmpty(ypxx)) {
                String[] a = ypxx.split("\\|");
                for (int i = 0; i < a.length; i++) {
                    String[] b = a[i].split(",");
                    Method method = sample.getClass()
                            .getMethod("get" + b[1].substring(0, 1).toUpperCase() + b[1].substring(1).toLowerCase());
                    Object field = method.invoke(sample);
                    if (StringUtil.isNotEmpty(field.toString())) {
                        xx += b[0] + ":" + field + ";";
                    } else {
                        xx += b[0] + ":/;";
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new JeecgBootException("样品信息获取失败!");
        }
        return xx;
    }

    /**
     * 把string array or list用给定的符号symbol连接成一个字符串
     *
     * @param array
     * @param symbol
     * @return
     */
    public static String joinString(List array, String symbol) {
        String result = "";
        if (array != null) {
            for (int i = 0; i < array.size(); i++) {
                String temp = array.get(i).toString();
                if (temp != null && temp.trim().length() > 0)
                    result += (temp + symbol);
            }
            if (result.length() > 1)
                result = result.substring(0, result.length() - 1);
        }
        return result;
    }

    /**
     * 不够位数的在前面补0，保留num的长度位数字
     *
     * @param code
     * @return
     */
    public static String autoGenericCode(String code, int num) {
        String result = "";
        // 保留num的位数
        // 0 代表前面补充0
        // num 代表长度为4
        // d 代表参数为正数型
        result = String.format("%0" + num + "d", Integer.parseInt(code));

        return result;
    }

    /**
     * 生成流水号
     *
     * @param codingFlowNumerDigit 流水号位数
     * @param FlowNumber           当前流水号
     * @return
     */
    public static String getFlowNumber(int codingFlowNumerDigit, String FlowNumber) {
        try {
            // 如果当前流水号为空则从1开始
            if (null == FlowNumber || "".equals(FlowNumber)) {
                return autoGenericCode("1", codingFlowNumerDigit);
            } else {
                return autoGenericCode(FlowNumber);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("生成流水号异常");
            return "";
        }
    }

    /**
     * 不够位数的在前面补0，保留code的长度位数字
     *
     * @param code
     * @return
     */
    public static String autoGenericCode(String code) {
        String result = "";
        // 保留code的位数
        result = String.format("%0" + code.length() + "d", Integer.parseInt(code) + 1);

        return result;
    }

    /**
     * 生成样品/记录/报告编码
     *
     * @param codingRules          编码规则 来源T_S_Depart 组织机构表 codingRules
     * @param codingDateFormat     编码时间格式 来源T_S_Depart 组织机构表 codingDateFormat
     * @param codingFlowNumerDigit 流水位数 来源T_S_Depart 组织机构表 codingFlowNumerDigit
     * @param FlowNumber           当前流水号 来源T_S_Depart 组织机构表 FlowNumber
     * @param BDH                  标段号 来源T_S_Depart 组织机构表 contractnumber 合同号
     * @param SYLXBM               试验类型编码 来源：dps_jc_testItemType#试验项目类型表 titSampleMark字段
     * @return
     * @throws ParseException
     */
    public static String getCoding(String codingRules, String codingDateFormat, int codingFlowNumerDigit,
                                   String FlowNumber, String BDH, String SYLXBM, String date, String type) {
        SimpleDateFormat sdf = new SimpleDateFormat(codingDateFormat);
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
        try {
            if ("NoFlowNumber".equals(FlowNumber)) {// 根据编码规则生成不需要带流水号的值
                /*
                 * 编码规则说明 编码规则例子：YP-LSSJ-BDH-SYLXBM-LSH 编码规则中可出现如下关键字，关键字符为动态替换键，关键字可自由调换位置
                 * LSSJ:流水时间 BDH:标段号 SYLXBM:试验类型编码 LSH:流水号 注意：编码规则制定完成后并已开始做资料，请勿随意更改
                 */
                codingRules = codingRules.replaceAll("-LSH", "");
                if (SYLXBM.equals("GJJ")) {
                    if (type.equals("JB011002")) {
                        SYLXBM = "GJH";
                    }
                    if (type.equals("JB011003")) {
                        SYLXBM = "GJL";
                    }
                }
                return codingRules.replace("LSSJ", sdf.format(sd.parse(date))).replace("BDH", BDH).replace("SYLXBM",
                        SYLXBM);
            } else {
                /*
                 * 编码规则说明 编码规则例子：YP-LSSJ-BDH-SYLXBM-LSH 编码规则中可出现如下关键字，关键字符为动态替换键，关键字可自由调换位置
                 * LSSJ:流水时间 BDH:标段号 SYLXBM:试验类型编码 LSH:流水号 注意：编码规则制定完成后并已开始做资料，请勿随意更改
                 */
                if (SYLXBM.equals("GJJ")) {
                    if (type.equals("JB011002")) {
                        SYLXBM = "GJH";
                    }
                    if (type.equals("JB011003")) {
                        SYLXBM = "GJL";
                    }
                }
                return codingRules.replace("LSSJ", sdf.format(sd.parse(date))).replace("BDH", BDH).replace("SYLXBM", SYLXBM)
                        .replace("LSH", getFlowNumber(codingFlowNumerDigit, FlowNumber));
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new JeecgBootException("编号生成失败");
        }
    }

    @Override
    public IPage getGrid(Integer pageNo, Integer pageSize, String orgCode, String titCode,
                         String sampleState, String sampleNo, String sampleName, String sampleGcbw, String titType, String sampleDate,
                         String reportNo, String tiNo, String userName, Boolean lookself, String signature, String shenpizhuangtai, String qianzhangzhuangtai, String lq) {
        Page<Map> page = new Page<>(pageNo, pageSize);
        IPage list = null;
        String view = "";
        if (titType != null) {
            if (titType.equals("1")) {
                view = "dps_sy_SampleList_XCJC_View";
                list = sampleMapper.selectInfo1(view, orgCode, signature, titCode, sampleState, sampleNo, reportNo, sampleName, sampleGcbw, titType, tiNo, lq, sampleDate, lookself, userName, shenpizhuangtai, qianzhangzhuangtai, userName, page);
            } else {
                view = "dps_sy_SampleList_View";
                list = sampleMapper.selectInfo1(view, orgCode, signature, titCode, sampleState, sampleNo, reportNo, sampleName, sampleGcbw, titType, tiNo, lq, sampleDate, lookself, userName, shenpizhuangtai, qianzhangzhuangtai, userName, page);
            }
        } else {
            view = "dps_sy_Sample_View";
            list = sampleMapper.selectInfo1(view, orgCode, signature, titCode, sampleState, sampleNo, reportNo, sampleName, sampleGcbw, titType, tiNo, lq, sampleDate, lookself, userName, shenpizhuangtai, qianzhangzhuangtai, userName, page);
        }

        return list;
    }

    @Override
    public void deleteBySampleNo(String sampleNo) {
        sampleMapper.deleteBySampleNo(sampleNo);
    }

    @Override
    public List<Map<String, Object>> selectTinoBySampleNo(String sampleNo) {
        List<Map<String, Object>> list = sampleMapper.selectTinoBySampleNo(sampleNo);
        return list;
    }

    @Override
    public List<Map<String, Object>> selectDate(String sampleNo, String tino) {
        List<Map<String, Object>> list = sampleMapper.selectDate(sampleNo, tino);
        return list;
    }

    @Override
    public SysDepart queryDepartByDepartid(String id) {
        SysDepart sysDepart = sampleMapper.queryDepartByDepartid(id);
        return sysDepart;
    }

    @Override
    public List<Map<String, Object>> selectByLeixing(int qianzhangleixing, String titcode, int liuchengleixing) {
        List<Map<String, Object>> list = sampleMapper.selectByLeixing(qianzhangleixing, titcode, liuchengleixing);
        return list;
    }

    @Override
    public List<Map<String, Object>> selectByIdAndType(String id, Integer type) {
        List<Map<String, Object>> list = sampleMapper.selectByIdAndType(id, type);
        return list;
    }

    @Override
    public String selectOneTinoBySampleno(String sampleno) {
        String s = sampleMapper.selectOneTinoBySampleno(sampleno);
        return s;
    }

    @Override
    public String selectBgdateBySampleno(String sampleno, String tino) {
        String s = sampleMapper.selectBgdateBySampleno(sampleno, tino);
        return s;
    }

    @Override
    public Map<String, Object> selectByRSId(String id) {
        Map<String, Object> map = sampleMapper.selectByRSId(id);
        return map;
    }

    @Override
    public Map<String, Object> selectBysampleNoAndtiNoTemp(String tiNo, String sampleno, String tinotemp) {
        Map<String, Object> map = sampleMapper.selectBysampleNoAndtiNoTemp(tiNo, sampleno, tinotemp);
        return map;
    }

    @Override
    public SysDepart getDepartById(String id) {
        return sampleMapper.getDepartById(id);
    }

    @Override
    public Map<String, Object> selectId(String departId, String titCode) {
        return sampleMapper.selectId(departId, titCode);
    }

    @Override
    public SyDpsJcTestitemtypeCodingrules selectTCById(String id) {
        return sampleMapper.selectTCById(id);
    }

    @Override
    public SysDepart selectByDepartId(String departId) {
        return sampleMapper.selectByDepartId(departId);
    }

    @Override
    public SyDpsJcTestitemtype selectTtByTitcode(String titCode) {
        return sampleMapper.selectTtByTitcode(titCode);
    }

    @Override
    public Map<String, Object> selectTinoBySampleno(String sampleNo) {
        return sampleMapper.selectTinoBySampleno(sampleNo);
    }

    @Override
    public Map<String, Object> selectByNoFlowNumber(String NoFlowNumber) {
        return sampleMapper.selectByNoFlowNumber(NoFlowNumber);
    }

    @Override
    public Long selectCount(String str) {
        return sampleMapper.selectCount(str);
    }

    @Override
    public List<Map<String, Object>> selectSampleNoLike(String str) {
        return sampleMapper.selectSampleNoLike(str);
    }

    @Override
    public void updateCurrentCodeById(String currentCode, String id) {
        sampleMapper.updateCurrentCodeById(currentCode, id);
    }

    @Override
    public void addCodingFlowNumber(String currentCode, String str) {
        sampleMapper.addCodingFlowNumber(currentCode, str);
    }

    @Override
    public SyDpsSyReportM selectRMBySampleNo(String sampleNo) {
        return sampleMapper.selectRMBySampleNo(sampleNo);
    }

    @Override
    public List<SyDpsSyReportS> selectRSBySampleNo(String sampleNo) {
        return sampleMapper.selectRSBySampleNo(sampleNo);
    }

    @Override
    public List<SyDpsSyTableheader> selectTHBySampleNo(String sampleNo) {
        return sampleMapper.selectTHBySampleNo(sampleNo);
    }

    @Override
    public SyDpsJcTestitem selectTIByTino(String tiNo) {
        return sampleMapper.selectTIByTino(tiNo);
    }

    @Override
    public Map<String, Object> selectBySamplenoFromTino(String tableName, String sampleNo, Integer tiNoTemp) {
        return sampleMapper.selectBySamplenoFromTino(tableName, sampleNo, tiNoTemp);
    }

    //header.getTinotemp(), yp, jl, bg, depart.getId()
    @Override
    public void insert1(String tino, String str, Integer tiNoTemp, String yp, String jl, String bg, String departId) {
        sampleMapper.insert1(tino, str, tiNoTemp, yp, jl, bg, departId);
    }

    @Override
    public void update1(String tino, String str, String key, String value) {
        sampleMapper.update1(tino, str, key, value);
    }

    @Override
    public Long selectCountBySampleNo(String sampleNo) {
        return sampleMapper.selectCountBySampleNo(sampleNo);
    }

    @Override
    public void updateCFN(String sampleNo2, String sampleNo1, String sampleNoNoSuffix) {
        sampleMapper.updateCFN(sampleNo2, sampleNo1, sampleNoNoSuffix);
    }

    @Override
    public void updateRM(String sampleNo, String reportNo, String tableNumber, String sampleNoOld) {
        sampleMapper.updateRM(sampleNo, reportNo, tableNumber, sampleNoOld);
    }

    @Override
    public void updateRS(String sampleNo, String reportNo, String sampleNoOld) {
        sampleMapper.updateRS(sampleNo, reportNo, sampleNoOld);
    }

    @Override
    public List<String> selectTiNoList(String sampleNo) {
        return sampleMapper.selectTiNoList(sampleNo);
    }

    @Override
    public void updateTable(String table, String sampleNo, String reportNo, String tableNumber, String sampleNoOld) {
        sampleMapper.updateTable(table, sampleNo, reportNo, tableNumber, sampleNoOld);
    }

    @Override
    public void updateNo1(String sampleNox, String reportNo, String tableNumber, String reportingSheetNo, String approvalTableNo, String sampleNoNew, String reportNoNew, String tableNumberNew, String reportingSheetNoNew, String approvalTableNoNew, String sampleNo) {
        sampleMapper.updateNo1(sampleNox, reportNo, tableNumber, reportingSheetNo, approvalTableNo, sampleNoNew, reportNoNew, tableNumberNew, reportingSheetNoNew, approvalTableNoNew, sampleNo);
    }

    @Override
    public void updateNo2(String table, String sampleNo1, String sampleNo2) {
        sampleMapper.updateNo2(table, sampleNo1, sampleNo2);
    }

    @Override
    public void deleteNo3(String table, String sampleId) {
        sampleMapper.deleteNo3(table, sampleId);
    }

    @Override
    @Transactional
    public void delSample(String id) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        SyDpsSySample sample = sampleMapper.selectSam(id);
        String sampleNo = sample.getSampleno();
        sampleMapper.delRMBySampleNo(sampleNo);
        sampleMapper.delRSBySampleNo(sampleNo);

        List<Map<String, Object>> list = sampleMapper.selectTH(sampleNo);
        sampleMapper.delTHBySampleNo(sampleNo);

        for (int i = 0; i < list.size(); i++) {
            try {
                sampleMapper.delTableBySampleNo(String.valueOf(list.get(i).get("tiNo")), sampleNo);
            } catch (Exception e) {
            }
        }
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sampleMapper.insertDT(loginUser.getUsername(), sd.format(new Date()), sample.getSamplenonew());
        List<Map<String, Object>> qrcode = sampleMapper.selectGrcode(sampleNo);

        for (int i = 0; i < qrcode.size(); i++) {
            sampleMapper.del1(String.valueOf(qrcode.get(i).get("qrcode")));
        }
        sampleMapper.del2(sampleNo);
        sampleMapper.del3(sampleNo);
//        sampleMapper.del4(sampleNo);
        sampleMapper.delGBHT(sampleNo);
        sampleMapper.delMeasurement(sampleNo);

        //sy_dps_yy_yuancaijinchangdengji
        SyDpsYyYuancaijinchangdengji ycjcdj = sampleMapper.selectYCJCDJ(sampleNo);
        if (ycjcdj != null) {
            sampleMapper.del5(ycjcdj.getId());
            sampleMapper.del6(ycjcdj.getId());
            sampleMapper.del7(ycjcdj.getId());
            ycjcdj.setErweimabianhao(null);
            ycjcdj.setErweimaweiyima(null);
            ycjcdj.setJianlichoujianzhuangtai(0);
            ycjcdj.setQuyangzhuangtai(0);
            ycjcdj.setQuyangren(null);
            ycjcdj.setQuyangshijian(null);
            ycjcdj.setShouyangzhuangtai(0);
            ycjcdj.setShouyangren(null);
            ycjcdj.setShouyangshijian(null);
            ycjcdj.setWaiweizhuangtai("0");
            ycjcdj.setWeituodanbianhao(null);
            ycjcdj.setJianzhengzhuangtai(0);
            ycjcdj.setJianzhengren(null);
            ycjcdj.setJianzhengshijian(null);
            ycjcdj.setSampleno(null);
            ycjcdj.setWeituozhuangtai(0);

            syDpsYyYuancaijinchangdengjiService.saveOrUpdate(ycjcdj);
        }
        sampleMapper.del4(sampleNo);
        //删除之后重新添加最新日期的同类型数据
    }

    @Override
    public void getSampleId(String id) {
        try {
            Map<String, Object> data = sampleMapper.getSampleId(id);
            List<Map<String, Object>> tiNos = sampleMapper.getRSTino(data.get("sampleNo").toString());
            data.put("tiNos", tiNos);

            data.put("QRCode", sampleMapper.getQRCode(String.valueOf(data.get("sampleNo"))));
            data.put("Pic", sampleMapper.getPici(String.valueOf(data.get("sampleNo"))));
            data.put("yj", sampleMapper.getYJ(String.valueOf(data.get("sampleNo"))));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    @Transactional
    public void returnSample(String sampleNo, String xcjc) {
        LoginUser user = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        if ("true".equals(xcjc)) {
            SyDpsYyXianchangjianceweituo xcjcwt = xianchangjianceweituoService.getOne(
                    Wrappers.lambdaQuery(new SyDpsYyXianchangjianceweituo()).eq(SyDpsYyXianchangjianceweituo::getSampleno, sampleNo));
            SyDpsYyRenwudan rwd = renwudanService.getOne(
                    Wrappers.lambdaQuery(new SyDpsYyRenwudan()).eq(SyDpsYyRenwudan::getYuancaijinchangdengjiid, xcjcwt.getId()));
            if (oConvertUtils.isNotEmpty(rwd.getJiancerenyuan())) {
                throw new JeecgBootException("以指派无法退回");
            }
            xcjcwt.setSampleno("");
            xianchangjianceweituoService.saveOrUpdate(xcjcwt);
//                SyDpsSySample sample = sampleMapper.selectById(Wrappers.lambdaQuery(new SyDpsSySample()).eq(SyDpsSySample::getSampleno, sampleNo));
            this.baseMapper.deleteReportM(sampleNo);
            this.baseMapper.deleteReportS(sampleNo);
            List<SyDpsSyTableheader> list = tableheaderService.list(
                    Wrappers.lambdaQuery(new SyDpsSyTableheader()).select(SyDpsSyTableheader::getTino).eq(SyDpsSyTableheader::getSampleno, sampleNo));
            tableheaderService.remove(Wrappers.lambdaQuery(new SyDpsSyTableheader()).eq(SyDpsSyTableheader::getSampleno, sampleNo));
            for (SyDpsSyTableheader tableheader : list) {
                this.baseMapper.deleteByTiNo(tableheader.getTino(), sampleNo);
            }
            this.baseMapper.delete(Wrappers.lambdaQuery(new SyDpsSySample()).eq(SyDpsSySample::getSampleno, sampleNo));
            this.baseMapper.insertDataToDeleteYp(user.getId(), DateUtils.getDate("yyyy-MM-dd HH:mm:ss"), sampleNo);
        } else {
            SyDpsYyYuancaiquyangweituo yuancaiquyangweituo = yuancaiquyangweituoService.getOne(
                    Wrappers.lambdaQuery(new SyDpsYyYuancaiquyangweituo()).eq(SyDpsYyYuancaiquyangweituo::getSampleno, sampleNo));
            SyDpsYyRenwudan renwudan = renwudanService.getOne(
                    Wrappers.lambdaQuery(new SyDpsYyRenwudan()).eq(SyDpsYyRenwudan::getYuancaijinchangdengjiid, yuancaiquyangweituo.getYuancaijinchangdengjiid()));
            if (oConvertUtils.isNotEmpty(renwudan.getJiancerenyuan())) {
                throw new JeecgBootException("以指派无法退回");
            }
            yuancaiquyangweituo.setSampleno("");
            yuancaiquyangweituoService.updateById(yuancaiquyangweituo);
            Wztaizhang wztaizhang = wztaizhangService.getById(yuancaiquyangweituo.getYuancaijinchangdengjiid());
            if ("1".equals(wztaizhang.getWaiweizhuangtai())) {
                this.baseMapper.deleteFromWaiBuWT(sampleNo);
            } else {
//                SyDpsSySample sample = sampleMapper.selectById(Wrappers.lambdaQuery(new SyDpsSySample()).eq(SyDpsSySample::getSampleno, sampleNo));
                this.baseMapper.deleteReportM(sampleNo);
                this.baseMapper.deleteReportS(sampleNo);
                List<SyDpsSyTableheader> list = tableheaderService.list(
                        Wrappers.lambdaQuery(new SyDpsSyTableheader()).select(SyDpsSyTableheader::getTino).eq(SyDpsSyTableheader::getSampleno, sampleNo));
                tableheaderService.remove(Wrappers.lambdaQuery(new SyDpsSyTableheader()).eq(SyDpsSyTableheader::getSampleno, sampleNo));
                for (SyDpsSyTableheader tableheader : list) {
                    this.baseMapper.deleteByTiNo(tableheader.getTino(), sampleNo);
                }
                this.baseMapper.delete(Wrappers.lambdaQuery(new SyDpsSySample()).eq(SyDpsSySample::getSampleno, sampleNo));
                this.baseMapper.insertDataToDeleteYp(user.getId(), DateUtils.getDate("yyyy-MM-dd HH:mm:ss"), sampleNo);
                wztaizhang.setSampleNo("");
                wztaizhangService.updateById(wztaizhang);
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String approval(HttpServletRequest request, HttpServletResponse response, String id, String type) throws Exception {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String msg = "";
        String[] ids = id.split(",");
        for (int i = 0; i < ids.length; i++) {
            id = ids[i];
            if (ids.length == 0) {
                break;
            }
            QueryWrapper<SyDpsSySample> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("id", id);
            SyDpsSySample sample = this.getOne(queryWrapper);
            if (sample.getSamplestate() != 1) {
//                throw new JeecgBootException("只能审批在检数据!");
                msg = "只能审批在检数据!";
                continue;
            }
            sample.setSamplestate(2);
            sample.setShenpizhuangtai(1);
            this.updateById(sample);
            this.deleteBySampleNo(sample.getSampleno());
            List<Map<String, Object>> list = this.selectTinoBySampleNo(sample.getSampleno());
            for (int j = 0; j < list.size(); j++) {
                String tino = list.get(j).get("tiNo").toString();
                String shiyanriqi = "";
                List<Map<String, Object>> data = this.selectDate(sample.getSampleno(), tino);
                Set<String> yiqishebei = new HashSet<>();
                for (int k = 0; k < data.size(); k++) {
                    String[] yiqishebeis = data.get(k).get("yiqishebei").toString().split(",|，|、");
                    if (StringUtil.isNotEmpty(shiyanriqi) && StringUtil.isNotEmpty(data.get(k).get("shiyanriqi").toString()) && !"/".equals(data.get(j).get("shiyanriqi").toString())) {
                        shiyanriqi = data.get(k).get("shiyanriqi").toString();
                    }
                    for (int l = 0; l < yiqishebeis.length; l++) {
                        if (StringUtil.isNotEmpty(yiqishebeis[l]) && !"/".equals(yiqishebeis[l])) {
                            yiqishebei.add(yiqishebeis[l]);
                        }
                    }
                }
                for (String yqsb : yiqishebei) {
                    SyDpsJcShebeiShiyongjilu shebeiShiyongjilu = new SyDpsJcShebeiShiyongjilu();
                    shebeiShiyongjilu.setShiyanriqi(shiyanriqi);
                    shebeiShiyongjilu.setShebeiid(yqsb);
                    shebeiShiyongjilu.setShiyongren(loginUser.getUsername());
                    shebeiShiyongjilu.setSampleno(sample.getSampleno());
                    shebeiShiyongjilu.setTino(tino);
                    shebeiShiyongjiluService.saveOrUpdate(shebeiShiyongjilu);
                }
            }
            DpsSyShenpirizhi shenpiLog = new DpsSyShenpirizhi();
            shenpiLog.setSampleid(sample.getId());
            shenpiLog.setShenpiren(loginUser.getRealname());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            shenpiLog.setShenpishijian(sdf.format(new Date()));
            shenpiLog.setShenpizhuangtai(1);
            shenpirizhiService.save(shenpiLog);

            //审核
            approval2(request, response, id, type);
        }
        return msg;
    }

    @Override
    public List<Map<String, Object>> get2(String biaoming, JSONArray json, String orderby) {
        String sql = "";
        for (int i = 0; i < json.size(); i++) {
            JSONObject jsono = json.getJSONObject(i);
            String key = jsono.getString("key");
            String value = jsono.getString("value");
            if (!key.equals("_t")) {
                sql += " and " + key + " = '" + value + "'";
            }

        }
        List<Map<String, Object>> data = sampleMapper.get2(sql, biaoming, orderby);
        return data;
    }

    @Override
    public List<Map<String, Object>> get1(String biaoming, JSONArray json, String orderby, String groupby) {
        String sql = "";
        for (int i = 0; i < json.size(); i++) {
            JSONObject jsono = json.getJSONObject(i);
            String key = jsono.getString("key");
            String value = jsono.getString("value");
            if (!key.equals("_t")) {
                sql += " and " + key + " = '" + value + "'";
            }

        }
        return sampleMapper.get1(sql, biaoming, orderby, groupby);
    }

    @Override
    public List<Map<String, Object>> getTable(String table, HttpServletRequest request) {
        Map<String, String[]> m = request.getParameterMap();
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String sql = "";
        if (!m.containsKey("noDepart")) {
            String orgCode = loginUser.getOrgCode();
            SysDepart sysDepart = sampleMapper.getByOrgCode(orgCode);
            sql += " and departId =  '" + sysDepart.getId() + "'";
        }

        for (String key : m.keySet()) {
            if (!key.equals("noDepart") && !key.equals("_t")) {
                sql += " and " + key + " = '" + m.get(key)[0] + "'";
            }
        }
        return sampleMapper.getTable(table, sql);
    }

    @Override
    public Map<String, Object> searchOneReturn(String biaoming, JSONArray json) {
        List<String> list = sampleMapper.findSySColumns(dataSource, biaoming);
        Map<String, Object> data = new HashMap<>();
        String sql = "";
        boolean pd = false;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).equals("tiNo")) {
                pd = true;
            }
        }
        for (int i = 0; i < json.size(); i++) {
            JSONObject jsono = json.getJSONObject(i);
            String key = jsono.getString("key");
            String value = jsono.getString("value");
            String string = "j." + key;
            if (!key.equals("_t")) {
                sql += " and " + string + " = '" + value + "'";
            }

        }
        if (pd) {
            data = sampleMapper.selectByBiaoMing1(biaoming, sql);
        } else {
            data = sampleMapper.selectByBiaoMing2(biaoming, sql);

        }
        return data;
    }

    @Override
    public List<Map<String, Object>> dushuget(HttpServletRequest request) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String orgCode = loginUser.getOrgCode();
        SysDepart sysDepart = sampleMapper.getByOrgCode(orgCode);
        return sampleMapper.selectCBRDUSHU(sysDepart.getId());
    }

    @Override
    public Object getSyjData(HashMap<String, Object> map) {
        Object obj = null;
        String tiNo = (String) map.get("tiNo");
        String wtid = (String) map.get("sampleNo");
        String tiNoTemp = map.get("tiNoTemp").toString();//记录表对应页码
        if (StringUtil.isNotEmpty(wtid)) {
            QueryWrapper<TSyjzb> wrapper = new QueryWrapper<>();
            wrapper.lambda().eq(TSyjzb::getWtbh, wtid).orderByAsc(TSyjzb::getSjbh);
            List<TSyjzb> listAll = itSyjzbService.list(wrapper);
            if (listAll.size() > 0) {
                TSyjzb entity = listAll.get(0);
//                String WTBH = entity.getWtbh();
                // 水泥强度试验试验
                if (tiNo.equals("JJ0406")) {
                    List<Map> list = this.baseMapper.getByJlb("JJ0406", wtid);
                    if (list.size() > 0) {
                        Map data = list.get(0);
                        Jj0406 jj0406 = BeanUtil.toBean(data, Jj0406.class);
                        // JJ0406 jj0406_tq = new JJ0406();
                        // MyBeanUtils.copyBeanNotNull2Bean(jj0406,jj0406_tq);
                        jj0406.setIstq("1");
                        // systemService.saveOrUpdate(jj0406);
                        jj0406.setCxrq(entity.getZzrq());
                        jj0406 = jj0406(jj0406, wtid, "3", "100136");
                        jj0406 = jj0406(jj0406, wtid, "7", "100136");
                        jj0406 = jj0406(jj0406, wtid, "28", "100136");
                        jj0406 = jj0406(jj0406, wtid, "3", "100135");
                        jj0406 = jj0406(jj0406, wtid, "7", "100135");
                        jj0406 = jj0406(jj0406, wtid, "28", "100135");
                        obj = jj0406;
                    }
                } else if (tiNo.equals("JJ0406a")) {
                    List<Map> list = this.baseMapper.getByJlb("JJ0406a", wtid);
                    if (list.size() > 0) {
                        Map data = list.get(0);
                        Jj0406a jj0406a = BeanUtil.toBean(data, Jj0406a.class);
                        // JJ0406 jj0406_tq = new JJ0406();
                        // MyBeanUtils.copyBeanNotNull2Bean(jj0406,jj0406_tq);
                        jj0406a.setIstq("1");
                        // systemService.saveOrUpdate(jj0406);
                        jj0406a.setCxrq(entity.getZzrq());
                        jj0406a = jj0406a(jj0406a, wtid, "28", "100136");
                        jj0406a = jj0406a(jj0406a, wtid, "28", "100135");
                        obj = jj0406a;
                    }
                } else if (tiNo.equals("CS312")) {
                    List<Map> list = this.baseMapper.getByJlb("CS312", wtid);
                    if (list.size() > 0) {
                        Map data = list.get(0);
                        Cs312 cs312 = BeanUtil.toBean(data, Cs312.class);
                        cs312.setIstq("1");
                        cs312.setCxrq(entity.getZzrq());
                        cs312 = cs312(cs312, wtid, "3", "100136");
                        cs312 = cs312(cs312, wtid, "28", "100136");
                        cs312 = cs312(cs312, wtid, "3", "100135");
                        cs312 = cs312(cs312, wtid, "28", "100135");
                        obj = cs312;
                    }
                } else if (tiNo.equals("JJ0501a")) {
//						List<JJ0501a> Listjj0501a = systemService.findByProperty(JJ0501a.class, "sampleno", wtid);
                    List<Map> list = this.baseMapper.getByJlb2("JJ0501a", wtid, tiNoTemp);
                    if (list.size() > 0) {
                        Map data = list.get(0);
                        Jj0501a jj0501a = BeanUtil.toBean(data, Jj0501a.class);
                        // jj0501a.setShejqddj(entity.getSjqd());
                        // jj0501a.setChichsxs(entity.getZsxs());
                        jj0501a.setYhtj(entity.getFbl());
                        Integer page = Integer.valueOf(tiNoTemp);
                        jj0501a = jj0501a(jj0501a, listAll, page);
                        // jj0501a.setIstq("1");
                        obj = jj0501a;
                    }
                } else if (tiNo.equals("JJ0501b1")) {
                    List<Map> list = this.baseMapper.getByJlb("JJ0501b1", wtid);
                    if (list.size() > 0) {
                        Map data = list.get(0);
                        Jj0501b1 jj0501b1 = BeanUtil.toBean(data, Jj0501b1.class);
                        // jj0501a.setShejqddj(entity.getSjqd());
                        // jj0501a.setChichsxs(entity.getZsxs());
                        jj0501b1.setYhtj(entity.getFbl());
                        jj0501b1 = jj0501b1(jj0501b1, listAll);
                        // jj0501a.setIstq("1");
                        obj = jj0501b1;
                    }
                } else if (tiNo.equals("JJ0501a1")) {
                    List<Map> list = this.baseMapper.getByJlb("JJ0501a1", wtid);
                    if (list.size() > 0) {
                        Map data = list.get(0);
                        Jj0501a1 jj0501a1 = BeanUtil.toBean(data, Jj0501a1.class);
                        // jj0501a.setShejqddj(entity.getSjqd());
                        // jj0501a.setChichsxs(entity.getZsxs());
                        jj0501a1.setYhtj(entity.getFbl());
                        jj0501a1 = jj0501a1(jj0501a1, listAll);
                        // jj0501a.setIstq("1");
                        obj = jj0501a1;
                    }
                } else if (tiNo.equals("JJ0501c")) {
                    List<Map> list = this.baseMapper.getByJlb("JJ0501c", wtid);
                    if (list.size() > 0) {
                        Map data = list.get(0);
                        Jj0501c jj0501c = BeanUtil.toBean(data, Jj0501c.class);
                        // jj0501c.setShajshejqddj(entity.getSjqd());
                        jj0501c.setYhtj(entity.getFbl());
                        jj0501c = jj0501c(jj0501c, listAll);
                        // jj0501c.setIstq("1");
                        obj = jj0501c;
                    }
                } else if (tiNo.equals("JJ0501d")) {
                    List<Map> list = this.baseMapper.getByJlb("JJ0501d", wtid);
                    if (list.size() > 0) {
                        Map data = list.get(0);
                        Jj0501d jj0501d = BeanUtil.toBean(data, Jj0501d.class);
                        // jj0501d.setShajshejqddj(entity.getSjqd());
                        jj0501d.setYhtj(entity.getFbl());
                        jj0501d = jj0501d(jj0501d, listAll);
                        // jj0501d.setIstq("1");
                        obj = jj0501d;
                    }
                } else if (tiNo.equals("JJ1001b_GJ")) {
                    List<Map> list = this.baseMapper.getByJlb("JJ1001b_GJ", wtid);
                    if (list.size() > 0) {
                        Map data = list.get(0);
                        Jj1001bGj jj1001bGJ = BeanUtil.toBean(data, Jj1001bGj.class);
                        // jj0501d.setShajshejqddj(entity.getSjqd());
                        jj1001bGJ = jj0501dGJ(jj1001bGJ, listAll);
                        // jj0501d.setIstq("1");
                        obj = jj1001bGJ;
                    }
                } else if (tiNo.equals("JJ1412")) {
                    List<Map> list = this.baseMapper.getByJlb("JJ1412", wtid);
                    if (list.size() > 0) {
                        Map data = list.get(0);
                        Jj1412 jj1412 = BeanUtil.toBean(data, Jj1412.class);
                        // jj0501d.setShajshejqddj(entity.getSjqd());
                        jj1412 = jj1412(jj1412, listAll);
                        // jj0501d.setIstq("1");
                        obj = jj1412;
                    }
                } else if (tiNo.equals("JJ1001a")) {
                    List<Map> list = this.baseMapper.getByJlb("JJ1001a", wtid);
                    if (list.size() > 0) {
                        Map data = list.get(0);
                        Jj1001a jj1001a = BeanUtil.toBean(data, Jj1001a.class);
                        jj1001a.setShengccj(entity.getFbl());
                        List<Map<String, Object>> SyjzbEntity1001a = this.baseMapper.getPzBmBySyJzb(wtid);
                        String gangjpaih1 = "", gangjpaih2 = "";
                        if (SyjzbEntity1001a.size() > 0) {
                            if (SyjzbEntity1001a.size() < 2) {
                                Map<String, Object> map1 = SyjzbEntity1001a.get(0);
                                gangjpaih1 = (String) map1.get("pzbm");
                            } else {
                                Map<String, Object> map1 = SyjzbEntity1001a.get(0);
                                gangjpaih1 = (String) map1.get("pzbm");
                                Map<String, Object> map2 = SyjzbEntity1001a.get(1);
                                gangjpaih2 = (String) map2.get("pzbm");
                            }
                        }

                        if (StringUtil.isNotEmpty(gangjpaih1)) {
                            jj1001a.setGangjpaih(gangjpaih1);
                            jj1001a = jj1001a(jj1001a, wtid, gangjpaih1, "0");
                        }
                        if (StringUtil.isNotEmpty(gangjpaih2)) {
                            jj1001a.setGangjpaih2(gangjpaih2);
                            jj1001a = jj1001a(jj1001a, wtid, gangjpaih2, "1");
                        }
                        // jj1001a.setIstq("1");
                        obj = jj1001a;
                    }
                } else if (tiNo.equals("JJ1001b")) {
                    List<Map> list = this.baseMapper.getByJlb("JJ1001b", wtid);
                    if (list.size() > 0) {
                        Map data = list.get(0);
                        Jj1001b jj1001b = BeanUtil.toBean(data, Jj1001b.class);
                        jj1001b.setChangj(entity.getCjmc());
                        List<Map<String, Object>> SyjzbEntity1001b = this.baseMapper.getPzBmBySyJzb(wtid);
                        String gangjpaih1 = "", gangjpaih2 = "";
                        if (SyjzbEntity1001b.size() > 0) {
                            if (SyjzbEntity1001b.size() < 2) {
                                Map<String, Object> map1 = SyjzbEntity1001b.get(0);
                                gangjpaih1 = (String) map1.get("pzbm");
                            } else {
                                Map<String, Object> map1 = SyjzbEntity1001b.get(0);
                                gangjpaih1 = (String) map1.get("pzbm");
                                Map<String, Object> map2 = SyjzbEntity1001b.get(1);
                                gangjpaih2 = (String) map2.get("pzbm");
                            }
                        }
                        if (StringUtil.isNotEmpty(gangjpaih1)) {
                            jj1001b.setGangjzl1(gangjpaih1);
                            jj1001b = jj1001b(jj1001b, wtid, gangjpaih1, "0");
                        }
                        if (StringUtil.isNotEmpty(gangjpaih2)) {
                            jj1001b.setGangjzl2(gangjpaih2);
                            jj1001b = jj1001b(jj1001b, wtid, gangjpaih2, "1");
                        }
                        jj1001b.setIstq("1");
                        obj = jj1001b;
                    }
                } else if (tiNo.equals("JJ1001c")) {
                    List<Map> list = this.baseMapper.getByJlb("JJ1001c", wtid);
                    if (list.size() > 0) {
                        Map data = list.get(0);
                        Jj1001c jj1001c = BeanUtil.toBean(data, Jj1001c.class);
                        List<Map<String, Object>> zb = this.baseMapper.getSyJzb(wtid);
                        if (zb.size() > 0) {
                            List<Map<String, Object>> wnj = this.baseMapper.selectWnj(zb.get(0).get("SYJID").toString());
                            if (wnj.size() > 0) {
                                jj1001c.setShijbh1(wnj.get(0).get("SJXH").toString());
                                // jj1001c.setMuczhij1(zb.get(0).get("GCZJ").toString());
                                jj1001c.setHjmj1(zb.get(0).get("SJMJ").toString());
                                jj1001c.setJxhz1(wnj.get(0).get("LZ").toString());
                                jj1001c.setKlqd1(wnj.get(0).get("LZQD").toString());
                                //jj1001c.setPhxs1(wnj.get(0).get("LDCMS").toString());
                                //jj1001c.setDlwz1(wnj.get(0).get("DKWZ").toString());
                            }
                            if (wnj.size() > 1) {
                                jj1001c.setShijbh2(wnj.get(1).get("SJXH").toString());
                                // jj1001c.setMuczhij1(zb.get(0).get("GCZJ").toString());
                                jj1001c.setHjmj2(zb.get(0).get("SJMJ").toString());
                                jj1001c.setJxhz2(wnj.get(1).get("LZ").toString());
                                jj1001c.setKlqd2(wnj.get(1).get("LZQD").toString());
                                //jj1001c.setPhxs2(wnj.get(1).get("LDCMS").toString());
                                //jj1001c.setDlwz2(wnj.get(1).get("DKWZ").toString());
                            }
                            if (wnj.size() > 2) {
                                jj1001c.setShijbh3(wnj.get(2).get("SJXH").toString());
                                // jj1001c.setMuczhij1(zb.get(0).get("GCZJ").toString());
                                jj1001c.setHjmj3(zb.get(0).get("SJMJ").toString());
                                jj1001c.setJxhz3(wnj.get(2).get("LZ").toString());
                                jj1001c.setKlqd3(wnj.get(2).get("LZQD").toString());
                                //jj1001c.setPhxs3(wnj.get(2).get("LDCMS").toString());
                                //jj1001c.setDlwz3(wnj.get(2).get("DKWZ").toString());
                            }
                        }
                        // jj1001c.setIstq("1");
                        obj = jj1001c;
                    }
                } else if (tiNo.equals("JGLQ15002")) {
                    List<Map> list = this.baseMapper.getByJlb("JGLQ15002", wtid);
                    if (list.size() > 0) {
                        Map data = list.get(0);
                        Jglq15002 jglq15002 = BeanUtil.toBean(data, Jglq15002.class);
                        List<Map<String, Object>> SyjzbEntity1001a = this.baseMapper.getPzBmBySyJzb(wtid);
                        String gangjpaih1 = "", gangjpaih2 = "";
                        if (SyjzbEntity1001a.size() > 0) {
                            if (SyjzbEntity1001a.size() < 2) {
                                Map<String, Object> map1 = SyjzbEntity1001a.get(0);
                                gangjpaih1 = (String) map1.get("pzbm");
                            } else {
                                Map<String, Object> map1 = SyjzbEntity1001a.get(0);
                                gangjpaih1 = (String) map1.get("pzbm");
                                Map<String, Object> map2 = SyjzbEntity1001a.get(1);
                                gangjpaih2 = (String) map2.get("pzbm");
                            }
                        }

                        if (StringUtil.isNotEmpty(gangjpaih1)) {
                            //jglq15002.setGangjpaih(gangjpaih1);
                            jglq15002 = jglq15002(jglq15002, wtid, gangjpaih1, "0");
                        }
                        if (StringUtil.isNotEmpty(gangjpaih2)) {
                            //jj1001a.setGangjpaih2(gangjpaih2);
                            jglq15002 = jglq15002(jglq15002, wtid, gangjpaih2, "1");
                        }
                        // jj1001a.setIstq("1");
                        obj = jglq15002;
                    }
                } else if (tiNo.equals("JJ0520b")) {
                    List<Map> list = this.baseMapper.getByJlb("JJ0520b", wtid);
                    if (list.size() > 0) {
                        Map data = list.get(0);
                        Jj0520b Jj0520b = BeanUtil.toBean(data, Jj0520b.class);
                        Jj0520b = jj0520b(Jj0520b, listAll);
                        obj = Jj0520b;
                    }
                }

            }
        }
        return obj;
    }

    @Override
    public IPage<SyResponse> getGrids(SyRequest syRequest) {
        LoginUser user = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        syRequest.setUsername(user.getUsername());
        if (oConvertUtils.isEmpty(syRequest.getSys_depart_orgcode())) {
            syRequest.setOrgCode(user.getOrgCode());
        }
        Page<SyResponse> page = new Page<>(syRequest.getPageNo(), syRequest.getPageSize());
        IPage<SyResponse> list = null;
        String view = "";
        if (oConvertUtils.isNotEmpty(syRequest.getTitType())) {
//            if (syRequest.getTitType().equals("1")) {
//                view = "dps_sy_SampleList_XCJC_View";
//                list = sampleMapper.selectInfo1(view, orgCode, signature,
//                        syRequest.getTitCode, sampleState, sampleNo,
//                        reportNo, sampleName, sampleGcbw,
//                        titType, tiNo, lq, sampleDate,
//                        lookself, userName, shenpizhuangtai,
//                        qianzhangzhuangtai, userName, page);
//            } else {
                list = sampleMapper.selectInfo(syRequest, page);
//            }
        } else {
//            view = "dps_sy_Sample_View";
//            list = sampleMapper.selectInfo1(view, orgCode, signature,
//                    titCode, sampleState, sampleNo,
//                    reportNo, sampleName, sampleGcbw,
//                    titType, tiNo, lq, sampleDate,
//                    lookself, userName, shenpizhuangtai,
//                    qianzhangzhuangtai, userName, page);
        }

        return list;
    }

    @Override
    public Map<String, Object> tqdata(String titCode, String sampleNo) {
        Map<String, Object> map = new HashMap<String, Object>();
        if ("1106".equals(titCode)) {
            map = this.baseMapper.selectByBiaomingANDsampleno("TSQJ0103", sampleNo);
        } else if ("1105".equals(titCode)) {
            map = this.baseMapper.selectByBiaomingANDsampleno("TSQJ0101a", sampleNo);
        }
        if (oConvertUtils.isNotEmpty(map)) {
            map.remove("id");
            map.remove("sampleNo");
        }
        return map;
    }

    @Override
    public Map<String, Object> getSampleById(String id) {
        Map<String, Object> sample = this.baseMapper.getSampleById(id);
        String pTino = this.baseMapper.selectBgTiNo(sample.get("sampleNo").toString());
        sample.put("ptiNo", pTino);
        sample.put("gdsy", 1);
        sample.put("bj", "bj");
        return sample;
    }

    @Override
    public List<Map> getSyRenWu(String orgCode) {
        return this.baseMapper.getSyRenWu(orgCode);
    }

    @Override
    @Transactional
    public void copy(HttpServletRequest request, String id, String sampleDate, String type, String inspection) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
//            String departIds = loginUser.getDepartIds();
//            SysDepart depart1 = this.getDepartById(departIds);
        SysDepart depart1 = this.baseMapper.getByOrgCode(loginUser.getOrgCode());
        String orgCode = loginUser.getOrgCode();
        SyCodingRules rules = this.baseMapper.selectDepartRuleByUserOrgCode(orgCode);

        QueryWrapper<SyDpsSySample> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", id);
        SyDpsSySample sample1 = this.getOne(queryWrapper);
        SyDpsSySample sample2 = new SyDpsSySample();
        BeanUtils.copyProperties(sample1, sample2);
        sample2.setId(null);

        String sampleCodingRules = rules.getSampleCodingRules();
        String reportCodingRules = rules.getReportCodingRules();
        String recordCodingRules = rules.getRecordCodingRules();
        String codingDateFormat = rules.getCodingDateFormat();
        Integer codingFlowNumerDigit = rules.getCodingFlowNumerDigit();
        String reportingSheetCodingRules = rules.getReportingSheetCodingRules();
        String approvalTableCodingRules = rules.getApprovalTableCodingRules();

        Map<String, Object> data = this.selectId(rules.getDepartId(), sample2.getTitcode());
        if (data != null) {
            SyDpsJcTestitemtypeCodingrules typeCoding = this.selectTCById(data.get("id").toString());
            sampleCodingRules = typeCoding.getSamplecodingrules();
            reportCodingRules = typeCoding.getReportcodingrules();
            recordCodingRules = typeCoding.getRecordcodingrules();
            codingDateFormat = typeCoding.getCodingdateformat();
            codingFlowNumerDigit = typeCoding.getCodingflownumerdigit();
            reportingSheetCodingRules = typeCoding.getReportingsheetcodingrules();
            approvalTableCodingRules = typeCoding.getApprovaltablecodingrules();
        }
        sample2.setSampledate(sampleDate);
        if (oConvertUtils.isNotEmpty(inspection)) {
            sample2.setDepartid(depart1.getId());
            sample2.setOrgcode(depart1.getOrgCode());
        }

        SysDepart depart = this.selectByDepartId(sample2.getSampledepartid());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sample2.setSamplecreatedate(sdf.format(new Date()));
        sample2.setSamplecreateperson(loginUser.getUsername());

        sample2.setInsertno(null);
        sample2.setShenpizhuangtai(0);
        sample2.setQianzhangzhuangtai(0);
        sample2.setZhuanfazhuangtai(0);
        sample2.setJilubiaoqianzhangzhuangtai("-1");
        sample2.setBaogaoqianzhangzhuangtai("-1");
        sample2.setBaoyandanqianzhangzhuangtai("-1");
        sample2.setShenpibiaoqianzhangzhuangtai("-1");
        SyDpsJcTestitemtype itemType = this.selectTtByTitcode(sample2.getTitcode());
        String yp = "";
        String jl = "";
        String bg = "";
        String by = "";
        String sp = "";
        Map<String, Object> tiNo = this.selectTinoBySampleno(sample1.getSampleno());


        yp = ShtooneUtil.getCoding(sampleCodingRules, codingDateFormat, codingFlowNumerDigit, "NoFlowNumber",
                StringUtil.isNotEmpty(rules.getContractNumberCode()) ? rules.getContractNumberCode()
                        : rules.getContractNumber(),
                itemType.getTitsamplemark(), sample2.getSampledate(), tiNo.get("tiNo").toString());

        Map<String, Object> current = this.selectByNoFlowNumber(yp);
        String currentCode = "";
        if (current != null) {
            Long count = this.selectCount(yp);
            Integer c = Integer.valueOf(current.get("currentCode").toString());
            if (count < c) {
                // 获取所有同类型编号
                List<Map<String, Object>> ypbhList = this.selectSampleNoLike(yp);
                Map<String, Object> mypbhMap = new HashMap<String, Object>();
                for (int j = 0; j < ypbhList.size(); j++) {
                    mypbhMap.put(ypbhList.get(j).get("sampleNo").toString(), "sampleNo");
                }
                for (int i = 0; i < c; i++) {
                    String n = "";
                    if (i != 0) {
                        n = ShtooneUtil.autoGenericCode(String.valueOf(i),
                                Integer.valueOf(rules.getCodingFlowNumerDigit()));
                    }
                    String ypbh = ShtooneUtil.getCoding(sampleCodingRules, codingDateFormat, codingFlowNumerDigit, n, StringUtil.isNotEmpty(rules.getContractNumberCode()) ? rules.getContractNumberCode() : rules.getContractNumber(),
                            itemType.getTitsamplemark(), sample2.getSampledate(), tiNo.get("tiNo").toString());
                    if (mypbhMap.get(ypbh) == null) {
                        currentCode = n;
                        break;
                    }
                }
            } else {
                currentCode = ShtooneUtil.getFlowNumber(Integer.valueOf(codingFlowNumerDigit),
                        current.get("currentCode").toString());
                this.updateCurrentCodeById(currentCode, current.get("id").toString());
                currentCode = current.get("currentCode").toString();
            }
        } else {
            currentCode = ShtooneUtil.getFlowNumber(Integer.valueOf(codingFlowNumerDigit), "");
            this.addCodingFlowNumber(currentCode, yp);
            currentCode = "";
        }

        yp = ShtooneUtil.getCoding(sampleCodingRules, codingDateFormat, codingFlowNumerDigit, currentCode,
                StringUtil.isNotEmpty(rules.getContractNumberCode()) ? rules.getContractNumberCode()
                        : rules.getContractNumber(),
                itemType.getTitsamplemark(), sample2.getSampledate(), tiNo.get("tiNo").toString());
        jl = ShtooneUtil.getCoding(recordCodingRules, codingDateFormat, codingFlowNumerDigit, currentCode,
                StringUtil.isNotEmpty(rules.getContractNumberCode()) ? rules.getContractNumberCode()
                        : rules.getContractNumber(),
                itemType.getTitsamplemark(), sample2.getSampledate(), tiNo.get("tiNo").toString());
        bg = ShtooneUtil.getCoding(reportCodingRules, codingDateFormat, codingFlowNumerDigit, currentCode,
                StringUtil.isNotEmpty(rules.getContractNumberCode()) ? rules.getContractNumberCode()
                        : rules.getContractNumber(),
                itemType.getTitsamplemark(), sample2.getSampledate(), tiNo.get("tiNo").toString());
        by = ShtooneUtil.getCoding(reportingSheetCodingRules, codingDateFormat, codingFlowNumerDigit, currentCode,
                StringUtil.isNotEmpty(rules.getContractNumberCode()) ? rules.getContractNumberCode()
                        : rules.getContractNumber(),
                itemType.getTitsamplemark(), sample2.getSampledate(), tiNo.get("tiNo").toString());
        sp = ShtooneUtil.getCoding(approvalTableCodingRules, codingDateFormat, codingFlowNumerDigit, currentCode,
                StringUtil.isNotEmpty(rules.getContractNumberCode()) ? rules.getContractNumberCode()
                        : rules.getContractNumber(),
                itemType.getTitsamplemark(), sample2.getSampledate(), tiNo.get("tiNo").toString());

        sample2.setSampleno(yp);
        sample2.setSamplecreatedate(sdf.format(new Date()));
        sample2.setSamplestate(0);
        sample2.setSamplenonew(yp);
        sample2.setTablenumbernew(jl);
        sample2.setReportnonew(bg);
        sample2.setReportingsheetnonew(by);
        sample2.setApprovaltablenonew(sp);
        sample2.setReportdate("");

        this.save(sample2);
        SyDpsYyRenwudan renwudan = renwudanService.getOne(Wrappers.lambdaQuery(new SyDpsYyRenwudan()).eq(SyDpsYyRenwudan::getSampleNo, sample1.getSampleno()));
        SyDpsYyRenwudan renwudan1 = new SyDpsYyRenwudan();
        renwudan1.setSampleNo(sample2.getSampleno());
        renwudan1.setJianceyiju(renwudan.getJianceyiju());
        renwudan1.setPandingyiju(renwudan.getPandingyiju());
        renwudan1.setJiancerenyuan(renwudan.getJiancerenyuan());
        renwudan1.setJilurenyuan(renwudan.getJilurenyuan());
        renwudan1.setFuherenyuan(renwudan.getFuherenyuan());
        renwudan1.setQianfarenyuan(renwudan.getQianfarenyuan());
        renwudanService.add(renwudan1);

        SyDpsSyReportM reportm1 = this.selectRMBySampleNo(sample1.getSampleno());
        SyDpsSyReportM reportm2 = new SyDpsSyReportM();
        BeanUtils.copyProperties(reportm1, reportm2);

        reportm2.setSampleno(yp);
        reportm2.setReportno(bg);
        reportm2.setTablenumber(jl);
        reportm2.setReportcreatedate(sdf.format(new Date()));
        reportm2.setReporteditperson(loginUser.getUsername());
        reportMService.save(reportm2);

        List<SyDpsSyReportS> reportslist = this.selectRSBySampleNo(sample1.getSampleno());
        List<SyDpsSyReportS> res = new ArrayList<>(reportslist.size());
        for (int i = 0; i < reportslist.size(); i++) {
            SyDpsSyReportS reportS = new SyDpsSyReportS();
            BeanUtils.copyProperties(reportslist.get(i), reportS);
            reportS.setReportno(bg);
            reportS.setSampleno(yp);
            reportS.setEditstate(0);
            res.add(reportS);
//            reportSService.save(reportS);
        }
        reportSService.saveBatch(res, res.size());

        List<SyDpsSyTableheader> headerslist = this.selectTHBySampleNo(sample1.getSampleno());
        List<SyDpsSyTableheader> hs = new ArrayList<>(headerslist.size());

        for (int i = 0; i < headerslist.size(); i++) {
            SyDpsSyTableheader header = new SyDpsSyTableheader();
            BeanUtils.copyProperties(headerslist.get(i), header);

            if (oConvertUtils.isNotEmpty(inspection)) {
                header.setDepartid(depart1.getId());
            }

            header.setSampleno(yp);
            header.setTablenumber(jl);
            header.setReportno(bg);
            header.setReportingsheetno(by);
            header.setApprovaltableno(sp);

            header.setSamplenonew(yp);
            header.setTablenumbernew(jl);
            header.setReportnonew(bg);
            header.setReportingsheetnonew(by);
            header.setApprovaltablenonew(sp);

            SyDpsJcTestitem testItem = this.selectTIByTino(header.getTino());
            String jlypxx1 = "";
            String jlypxx2 = "";
            String jlypxx3 = "";
            jlypxx1 = "取样日期:" + header.getSampledate() + ";样品名称:" + header.getSamplename() + ";样品编号:" + header.getSampleno() + "(01);样品描述:"
                    + header.getSampledescribe() + ";样品数量:" + header.getSampleyangpinshuliang() + "/" + header.getSampleyangpinshuliang2() + "/" + header.getSampleyangpinshuliang3();

            jlypxx2 = "取样日期:" + header.getSampledate() + ";样品名称:" + header.getSamplename() + ";样品编号:" + header.getSampleno() + "(02);样品描述:"
                    + header.getSampledescribe() + ";样品数量:" + header.getSampleyangpinshuliang() + "/" + header.getSampleyangpinshuliang2() + "/" + header.getSampleyangpinshuliang3();

            jlypxx3 = "取样日期:" + header.getSampledate() + ";样品名称:" + header.getSamplename() + ";样品编号:" + header.getSampleno() + "(03);样品描述:"
                    + header.getSampledescribe() + ";样品数量:" + header.getSampleyangpinshuliang() + "/" + header.getSampleyangpinshuliang2() + "/" + header.getSampleyangpinshuliang3();

            if ("1".equals(String.valueOf(testItem.getTitype()))) {
                header.setYpxx(getYpxx(itemType.getTiyangpinxinxibg(), sample2));
            } else {
                header.setYpxx(getYpxx(itemType.getTiyangpinxinxijl(), sample2));
                if ("JJ0201a".equals(header.getTino())) {
                    header.setYpxx(jlypxx1);
                } else if ("JJ0201a2".equals(header.getTino())) {
                    header.setYpxx(jlypxx2);
                } else if ("JJ0201a3".equals(header.getTino())) {
                    header.setYpxx(jlypxx3);
                }
            }

            header.setId(null);
//            tableheaderService.save(header);
            hs.add(header);
//            Map<String, Object> map = this.selectBySamplenoFromTino(header.getTino(), headerslist.get(i).getSampleno(), headerslist.get(i).getTinotemp());

            String str = UUID.randomUUID().toString();
            this.insert1(header.getTino(), str, header.getTinotemp(), yp, jl, bg, depart.getId());
//            for (String key : map.keySet()) {
//                String value = map.get(key) == null ? null : map.get(key).toString();
//                if (key.equals("id")) {
//                    continue;
//                }
//                if (key.equals("sampleNo")) {
//                    value = yp;
//                }
//                if (key.equals("tableNumber")) {
//                    value = jl;
//                }
//                if (key.equals("reportNo")) {
//                    value = bg;
//                }
//                if (testItem.getTitype().equals("1") && !(key.equals("tiNo") || key.equals("tiNoTemp")
//                        || key.equals("sampleNo") || key.equals("tableNumber") || key.equals("reportNo")
//                        || key.equals("departId") || key.equals("jcjl") || key.equals("jlyj")
//                        || key.equals("jlyj_3d") || key.equals("remark_3d") || key.equals("shiyanriqi_3d")
//                        || key.equals("baogaoriqi_3d") || key.equals("cbryj"))) {
//                    if (testItem.getTino().equals("JB010101") && (key.equals("hanshuilv1") || key.equals("midu1")
//                            || key.equals("yexian1") || key.equals("suxian1") || key.equals("suxingzhishu1")
//                            || key.equals("choudu1") || key.equals("zuidaganmidu1")
//                            || key.equals("zuijiahanshuilv1") || key.equals("sanmishidu1")
//                            || key.equals("simishidu1") || key.equals("liumishidu1") || key.equals("jszbssl")
//                            || key.equals("jszbyjwhl"))) {
//                        // 土工
//                    } else if (testItem.getTino().equals("JB010201") && (key.equals("jszbtype")
//                            || key.equals("hnlzb") || key.equals("nkzb") || key.equals("klhlzb")
//                            || key.equals("yszzb") || key.equals("hslzb") || key.equals("jgxzb")
//                            || key.equals("mhsszb") || key.equals("rrhlzb") || key.equals("mgzzb")
//                            || key.equals("cjzzb") || key.equals("bguanmdzb") || key.equals("bganmdzb")
//                            || key.equals("mtjmdzb") || key.equals("djmdzb") || key.equals("zsmdzb")
//                            || key.equals("dsmdzb") || key.equals("xslzb") || key.equals("kxlzb")
//                            || key.equals("bguanxdmdzb") || key.equals("bganxdmdzb") || key.equals("mtjxdmdzb"))) {
//                        // 粗集料
//                    } else if (testItem.getTino().equals("JB010203")
//                            && (key.equals("jszbtype") || key.equals("hnlzb") || key.equals("nkzb")
//                            || key.equals("hslzb") || key.equals("sdlzb") || key.equals("jgxzb")
//                            || key.equals("jxlzb") || key.equals("ldsjzb") || key.equals("yjlzb")
//                            || key.equals("yszzb") || key.equals("bguanmdzb") || key.equals("bguanmdzb")
//                            || key.equals("djmdzb") || key.equals("kxlzb"))) {
//                        // 细集料
//                    } else if (testItem.getTino().equals("JB010507") && (key.equals("cailiaomingcheng1")
//                            || key.equals("cailiaomingcheng2") || key.equals("cailiaomingcheng3")
//                            || key.equals("cailiaomingcheng4") || key.equals("cailiaomingcheng5")
//                            || key.equals("cailiaomingcheng6") || key.equals("cailiaomingcheng7")
//                            || key.equals("cailiaomingcheng8") || key.equals("cailiaomingcheng9")
//                            || key.equals("guige1") || key.equals("guige2") || key.equals("guige3")
//                            || key.equals("guige4") || key.equals("guige5") || key.equals("guige6")
//                            || key.equals("guige7") || key.equals("guige8") || key.equals("guige9")
//                            || key.equals("changjia1") || key.equals("changjia2") || key.equals("changjia3")
//                            || key.equals("changjia4") || key.equals("changjia5") || key.equals("changjia6")
//                            || key.equals("changjia7") || key.equals("changjia8") || key.equals("changjia9"))) {
//                        // 钢筋机械连接试验检测报告
//                    } else if (testItem.getTino().equals("JB011003")
//                            && (key.equals("hanjiezl") || key.equals("hanjieff"))) {
//
//                    } else if (testItem.getTino().equals("JB011002")
//                            && (key.equals("hanjiezl") || key.equals("hanjieff"))) {
//
//                    } else {
//                        continue;
//                    }
//                }
//                if ("1".equals(type) && !(key.equals("tiNo") || key.equals("tiNoTemp") || key.equals("sampleNo")
//                        || key.equals("tableNumber") || key.equals("reportNo") || key.equals("departId"))) {
//                    value = "";
//                }
//                this.update1(header.getTino(), str, key, value);
//            }
        }

        tableheaderService.saveBatch(hs, hs.size());


    }

    @Override
    public List<Map> getTypeListByCode(String code) {
        return this.baseMapper.getTypeListByCode(code);
    }

    @Override
    public IPage<Map> searchPhbData(Map map) {
        Integer pageNo = Integer.valueOf(map.get("pageNo").toString());
        Integer pageSize = Integer.valueOf(map.get("pageSize").toString());
        Page<Map> page = new Page<>(pageNo, pageSize);
        String tiNo = map.get("tiNo").toString();
        String orgCode = null;
        String sampleNo = null;
        if (oConvertUtils.isNotEmpty(map.get("orgCode"))) {
            orgCode = map.get("orgCode").toString();
        } else {
            LoginUser user = (LoginUser) SecurityUtils.getSubject().getPrincipal();
            orgCode = user.getOrgCode();
        }
        if (oConvertUtils.isNotEmpty(map.get("sampleNo"))) {
            sampleNo = map.get("sampleNo").toString();
        }
        IPage<Map> maps = this.baseMapper.searchPhbData(tiNo, orgCode, sampleNo, page);
        return maps;
    }

    @Override
    public Map ycjcExtract(String wtdbh) {
        if (StringUtil.isNotEmpty(wtdbh)) {
            boolean numeric = isNumeric(wtdbh);
            if (!numeric) {
                return null;
            }
            Boolean checked = true;
            LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
            String rolename = sampleMapper.selectRolename(loginUser.getId());
            if(oConvertUtils.isNotEmpty(rolename)&&rolename.equals("中心试验室")){
                checked=false;
            }
            if (checked) {
                Map<String, Object> map = sampleMapper.selectbywtdh(wtdbh);
                if (oConvertUtils.isNotEmpty(map)) {
                    Map<String, Object> rest = sampleMapper.selectTitCodeBYSampleNo(map.get("sampleNo").toString());
                    Map<String, Object> baogaoriqi = sampleMapper.selectBaogaoriqiBySampleNO(map.get("sampleNo").toString());
                    if (rest != null) {
                        if (rest.get("titCode").toString().equals("0201") || rest.get("titCode").toString().equals("0220")) {
                            Map<String, Object> map1 = sampleMapper.selectShiyanriqiBySamplenoAndtino(map.get("sampleNo").toString(), "JJ0201a");
                            Map<String, Object> map2 = sampleMapper.selectShiyanriqiBySamplenoAndtino(map.get("sampleNo").toString(), "JJ0201a2");
                            Map<String, Object> map3 = sampleMapper.selectShiyanriqiBySamplenoAndtino(map.get("sampleNo").toString(), "JJ0201a3");
                            Map<String, Object> dmap1 = sampleMapper.selectShiyanriqiBySamplenoAndtino(map.get("sampleNo").toString(), "JJ0201a_DDHC");
                            Map<String, Object> dmap2 = sampleMapper.selectShiyanriqiBySamplenoAndtino(map.get("sampleNo").toString(), "JJ0201a_DDHC2");
                            Map<String, Object> dmap3 = sampleMapper.selectShiyanriqiBySamplenoAndtino(map.get("sampleNo").toString(), "JJ0201a_DDHC3");
                            Map<String, Object> dmap4 = sampleMapper.selectShiyanriqiBySamplenoAndtino(map.get("sampleNo").toString(), "JJ0201a_DDHC4");
                            Map<String, Object> dmap5 = sampleMapper.selectShiyanriqiBySamplenoAndtino(map.get("sampleNo").toString(), "JJ0201a_DDHC5");
                            map.put("shiyanriqi1", map1 == null ? "" : map1.get("shiyanriqi"));
                            map.put("shiyanriqi2", map2 == null ? "" : map2.get("shiyanriqi"));
                            map.put("shiyanriqi3", map3 == null ? "" : map3.get("shiyanriqi"));
                            map.put("dshiyanriqi1", dmap1 == null ? "" : dmap1.get("shiyanriqi"));
                            map.put("dshiyanriqi2", dmap2 == null ? "" : dmap2.get("shiyanriqi"));
                            map.put("dshiyanriqi3", dmap3 == null ? "" : dmap3.get("shiyanriqi"));
                            map.put("dshiyanriqi4", dmap4 == null ? "" : dmap4.get("shiyanriqi"));
                            map.put("dshiyanriqi5", dmap5 == null ? "" : dmap5.get("shiyanriqi"));
                            map.put("reportDate", baogaoriqi.get("baogaoriqi"));
                        } else if (rest.get("titCode").toString().equals("04")) {
                            Map<String,Object> baogaoriqi_3d = sampleMapper.selectFromJB010401(map.get("sampleNo").toString());
                            map.put("reportDate",baogaoriqi_3d.get("baogaoriqi_3d"));
                            Map<String, Object> baogaoriqi_28d = sampleMapper.selectBaogaoriqiBySamplenoAndtino(map.get("sampleNo").toString(),"JB010401");
                            map.put("reportDate_28d",baogaoriqi_28d.get("baogaoriqi"));
                        } else{
                            map.put("reportDate",baogaoriqi.get("baogaoriqi"));
                        }
                    }else {
                        map.put("reportDate","");
                    }
                }else {
                    map=new HashMap<>();
                    map.put("reportDate","");
                }
                return map;
            }else{
                return null;
            }
        }
        return null;
    }

    public boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }

    private String approval2(HttpServletRequest request, HttpServletResponse response,
                             String id, String type) throws Exception {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String msg = "";
        QueryWrapper<SyDpsSySample> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", id);
        SyDpsSySample sample = this.getOne(queryWrapper);

        sample.setShenpizhuangtai(2);

        DpsSyShenpirizhi shenpiLog = new DpsSyShenpirizhi();
        shenpiLog.setSampleid(sample.getId());
        shenpiLog.setShenpiren(loginUser.getRealname());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        shenpiLog.setShenpishijian(sdf.format(new Date()));
        shenpiLog.setShenpizhuangtai(sample.getShenpizhuangtai());
        shenpiLog.setShenpiyijian("");

        if (sample.getShenpizhuangtai() == 2) {
            sample.setShenpiren(loginUser.getRealname());
            sample.setShenpishijian(sdf.format(new Date()));

            SysDepart sysDepart = this.queryDepartByDepartid(sample.getDepartid());

            String qzlxString = "1";
            if (sysDepart != null) {
                if ("3".equals(sysDepart.getOrgType())) {
                    qzlxString = "2";
                } else if ("4".equals(sysDepart.getOrgType())) {
                    qzlxString = "3";
                }
            } else {
                return "获取部门为空!";
            }
            msg = signatureInit(request.getSession().getServletContext().getRealPath("/"), sample, qzlxString,
                    false, type);
        }
        if ("".equals(msg)) {
            shenpirizhiService.save(shenpiLog);
            this.saveOrUpdate(sample);
        }
        if ("".equals(msg)) {
            return "成功" + msg;
        } else {
            return "失败" + msg;
        }
    }

    private String signatureInit(String path, SyDpsSySample sample, String departType, boolean flag, String type) throws Exception {
        // 删除
        File file = new File(pdfUrl + sample.getId());
        if (file.exists()) {
            File[] files = file.listFiles();
            for (File f : files) {
                f.delete();
            }
        } else {
            file.mkdirs();
        }
        Integer index1 = getDataList(path, sample.getId(), 1, 3, departType, flag, sample.getSampleno());
        Integer index2 = getDataList(path, sample.getId(), 2, 0, departType, flag, sample.getSampleno());
        Integer index3 = getDataList(path, sample.getId(), 3, 1, departType, flag, sample.getSampleno());
        Integer index4 = getDataList(path, sample.getId(), 4, 4, departType, flag, sample.getSampleno());
        // 判断是否是手动生成
        if (!flag) {
            List<DpsSyQianzhangliucheng> listS = new ArrayList<>();
            if (index1 > 0) {
                List<Map<String, Object>> list = this.selectByLeixing(Integer.parseInt(departType), sample.getTitcode(), 3);
                if (list.size() == 0) {
                    list = this.selectByLeixing(Integer.parseInt(departType), "", 3);
                }
                if (list.size() == 0) {
                    return "报验单签章流程未配置";
                }
                addSignatureProcess(sample.getId(), listS, list, index1);
                sample.setBaoyandanqianzhangzhuangtai("0/" + list.size());
            } else {
                sample.setBaoyandanqianzhangzhuangtai("-1");
            }

            if (index2 > 0) {
                List<Map<String, Object>> list = this.selectByLeixing(Integer.parseInt(departType), sample.getTitcode(), 0);
                if (list.size() == 0) {
                    list = this.selectByLeixing(Integer.parseInt(departType), "", 0);
                }
                if (list.size() == 0) {
                    return "记录表签章流程未配置";
                }
                addSignatureProcess(sample.getId(), listS, list, index2);
                sample.setJilubiaoqianzhangzhuangtai("0/" + list.size());
            } else {
                sample.setJilubiaoqianzhangzhuangtai("-1");
            }
            if (index3 > 0) {
                List<Map<String, Object>> list = this.selectByLeixing(Integer.parseInt(departType), sample.getTitcode(), 1);
                if (list.size() == 0) {
                    list = this.selectByLeixing(Integer.parseInt(departType), "", 1);
                }
                if (list.size() == 0) {
                    return "报告表签章流程未配置";
                }
                addSignatureProcess(sample.getId(), listS, list, index3);
                sample.setBaogaoqianzhangzhuangtai("0/" + list.size());
            } else {
                sample.setBaogaoqianzhangzhuangtai("-1");
            }
            if (index4 > 0) {
                List<Map<String, Object>> list = this.selectByLeixing(Integer.parseInt(departType), sample.getTitcode(), 4);
                if (list.size() == 0) {
                    list = this.selectByLeixing(Integer.parseInt(departType), "", 4);
                }
                if (list.size() == 0) {
                    return "审批表签章流程未配置";
                }
                addSignatureProcess(sample.getId(), listS, list, index4);
                sample.setShenpibiaoqianzhangzhuangtai("0/" + list.size());
            } else {
                sample.setShenpibiaoqianzhangzhuangtai("-1");
            }
            qianzhangliuchengService.saveBatch(listS);
        }
        while (checkFile(path, sample.getId(), index1, index2, index3, index4)) {
            Thread.sleep(1000);
        }
        Thread.sleep(1000);
        String url = mergePdf(path, sample.getId(), index1, index2, index3, index4);

        File file1 = new File(url);
        String replaceAll = url.replaceAll("\\\\", "/");
        Map map = new HashMap();
        map.put("biz", replaceAll);
        map.put("file",file1);
            String post = HttpRequest.post("http://z.traiot.cn/jeecg-boot/sys/common/upload")
                    .header("Content-Type", "multipart/form-data")
                    .form(map)
                    .execute()
                    .body();
            cn.hutool.json.JSONObject jsonObject1 = new cn.hutool.json.JSONObject(post);
            Boolean success = (Boolean) jsonObject1.get("success");
            if(success){
                String PDfurl = jsonObject1.get("message").toString();
                this.baseMapper.updatePdfurl(sample.getId(),PDfurl);
            }else {
                throw new JeecgBootException("pdf生成失败,联系管理员");
            }
        if(sample.getIssign()==1){
            syDpsSySignatureService.createSign("检测记录", null, url, index4, index3, index2, sample.getSampleno());
        }
        return "";
    }

    // 获取数据表号初始化页面
    private int getDataList(String path, String id, Integer type1, Integer type2, String departType, boolean flag, String sampleNo)
            throws Exception {
        Integer c = 0;
        List<Map<String, Object>> list = this.selectByIdAndType(id, type2);
//        for (int i = 0; i < list.size(); i++) {
//            if ("JB010401".equals(list.get(i).get("tiNo"))) {
//                Map<String, Object> data = new HashMap<>();
//                data.putAll(list.get(i));
////                data.put("tiNo", "JB010401-3d");
//                list.add(i, data);
//                i++;
////            }
//        }
        String baogaoriqi = "";
        if (list.size() > 0) {
            String tiNo = this.selectOneTinoBySampleno(sampleNo);
            String s = this.selectBgdateBySampleno(sampleNo, String.valueOf(tiNo));
            if (StringUtil.isNotEmpty(s) && (!"/".equals(s))) {
                baogaoriqi = s;
            }
        }

        for (int i = 0; i < list.size(); i++) {
            String dataid = list.get(i).get("id").toString();
            String tiNo = list.get(i).get("tiNo").toString();
            Map<String, Object> data = getData(dataid);
            data.put("bg_baogaoriqi", baogaoriqi);
            String html = readHtml(path, data, departType, tiNo, flag);
            createHtml(path, id, html, "Basis-" + type1 + "-" + i);
            boolean horizontal = true;
            html = readBasisHtml(path, id, "Basis-" + type1 + "-" + i);
            if (html.indexOf("paperA4-horizontal") != -1) {
                horizontal = false;
            }
            createHtml(path, id, html, "Html-" + type1 + "-" + i);
            createPdf(path, id, "Html-" + type1 + "-" + i, "Pdf-" + type1 + "-" + i, horizontal);
        }
        return list.size() + c;
    }

    // 签章流程存储
    private void addSignatureProcess(String id, List<DpsSyQianzhangliucheng> listS, List<Map<String, Object>> list,
                                     Integer index) {
        for (int i = 0; i < list.size(); i++) {
            DpsSyQianzhangliucheng dpssyqzlc = new DpsSyQianzhangliucheng();
            dpssyqzlc.setSampleid(id);
            dpssyqzlc.setQianzhangliuchengid(list.get(i).get("id").toString());
            dpssyqzlc.setQianzhangzhuangtai(0);
            dpssyqzlc.setQianzhangleixing(Integer.valueOf(list.get(i).get("qianzhangleixing").toString()));
            dpssyqzlc.setLiuchengleixing(Integer.valueOf(list.get(i).get("liuchengleixing").toString()));
            dpssyqzlc.setLiuchengzhuangtaixuhao(Integer.valueOf(list.get(i).get("liuchengzhuangtaixuhao").toString()));
            dpssyqzlc.setLiuchengmingcheng(list.get(i).get("liuchengmingcheng").toString());
            dpssyqzlc.setQianzhangguanjianzi(list.get(i).get("qianzhangguanjianzi").toString());
            dpssyqzlc.setQianzhangyemianshuliang(index);
            listS.add(dpssyqzlc);
        }
    }

    // 检查pdf是否生成
    private boolean checkFile(String path, String id, Integer index1, Integer index2, Integer index3, Integer index4) {
        File file = null;
        for (int i = 0; i < index1; i++) {
            file = new File(pdfUrl + id + "/Pdf-1-" + i + ".pdf");
            if (!file.exists() || file.length() == 0) {
                return true;
            }
        }
        for (int i = 0; i < index2; i++) {
            file = new File(pdfUrl + id + "/Pdf-2-" + i + ".pdf");
            if (!file.exists() || file.length() == 0) {
                return true;
            }
        }
        for (int i = 0; i < index3; i++) {
            file = new File(pdfUrl + id + "/Pdf-3-" + i + ".pdf");
            if (!file.exists() || file.length() == 0) {
                return true;
            }
        }
        for (int i = 0; i < index4; i++) {
            file = new File(pdfUrl + id + "/Pdf-4-" + i + ".pdf");
            if (!file.exists() || file.length() == 0) {
                return true;
            }
        }
        return false;
    }

    // 合并PDF
    private String mergePdf(String path, String id, Integer index1, Integer index2, Integer index3, Integer index4)
            throws Exception {
        PdfWriter reader1 = new PdfWriter(pdfUrl + id + "/mergePdf.pdf");
        PdfReader reader2 = null;
        PdfDocument pdf1 = new PdfDocument(reader1);
        PdfDocument pdf2 = null;
        PdfMerger merger = new PdfMerger(pdf1);
        for (int i = 0; i < index4; i++) {
            try {
                reader2 = new PdfReader(pdfUrl + id + "/Pdf-4-" + i + ".pdf");
                pdf2 = new PdfDocument(reader2);
                merger.merge(pdf2, 1, pdf2.getNumberOfPages());
            } catch (Exception e) {
                throw e;
            } finally {
                if (pdf2 != null) {
                    pdf2.close();
                }
                if (reader2 != null) {
                    reader2.close();
                }
            }
        }
        for (int i = 0; i < index1; i++) {
            try {
                reader2 = new PdfReader(pdfUrl + id + "/Pdf-1-" + i + ".pdf");
                pdf2 = new PdfDocument(reader2);
                merger.merge(pdf2, 1, pdf2.getNumberOfPages());
            } catch (Exception e) {
                throw e;
            } finally {
                if (pdf2 != null) {
                    pdf2.close();
                }
                if (reader2 != null) {
                    reader2.close();
                }
            }
        }
        for (int i = 0; i < index3; i++) {
            try {
                reader2 = new PdfReader(pdfUrl + id + "/Pdf-3-" + i + ".pdf");
                pdf2 = new PdfDocument(reader2);
                merger.merge(pdf2, 1, pdf2.getNumberOfPages());
            } catch (Exception e) {
                throw e;
            } finally {
                if (pdf2 != null) {
                    pdf2.close();
                }
                if (reader2 != null) {
                    reader2.close();
                }
            }
        }
        for (int i = 0; i < index2; i++) {
            try {
                reader2 = new PdfReader(pdfUrl + id + "/Pdf-2-" + i + ".pdf");
                pdf2 = new PdfDocument(reader2);
                merger.merge(pdf2, 1, pdf2.getNumberOfPages());
            } catch (Exception e) {
                throw e;
            } finally {
                if (pdf2 != null) {
                    pdf2.close();
                }
                if (reader2 != null) {
                    reader2.close();
                }
            }
        }
        pdf1.close();
        reader1.close();
        return pdfUrl + id + "/mergePdf.pdf";
    }

    // 单个表获取数据
    private Map<String, Object> getData(String id) {
        Map<String, Object> map = this.selectByRSId(id);
        String tiNo = map.get("tiNo").toString();
        String sampleNo = map.get("sampleNo").toString();
        String tiNoTemp = map.get("tiNoTemp").toString();
        Map<String, Object> data = this.selectBysampleNoAndtiNoTemp(tiNo, sampleNo, tiNoTemp);
        data.putAll(map);
        return data;
    }

    // 读取页面替换数据
    private String readHtml(String path, Map<String, Object> data, String departType, String tiNo, boolean flag)
            throws Exception {
        File file = new File(htmlPath + tiNo + ".html");
        StringBuilder result = new StringBuilder();
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
        String s = null;
        while ((s = br.readLine()) != null) {
            result.append(System.lineSeparator() + s);
        }
        br.close();
        String html = result.toString();
        html = html.replace("../js", "../../html/js");
        html = html.replace("../css", "../../html/css");
        html = html.replace("<script src=\"../../html/js/topdf.js\"></script>",
                "<script src=\"../../html/js/topdf-java.js\"></script>");
        String jsonString = JSONObject.fromObject(data).toString();
        String string = jsonString.replace("\\", "\\\\");
        html = html.replace("${json}", string);
        //html = html.replace("${json}", JSONObject.fromObject(data).toString());
        if ("1".equals(departType)) {
            html = html.replace("${commonType}", "0");
        } else {
            html = html.replace("${commonType}", "1");
        }
        return html;
    }

    // 保存html
    private void createHtml(String path, String id, String html, String filename) throws Exception {
        File file = new File(path + "signature/pdf/" + id);
//        if (!file.exists()) {
//            file.mkdirs();
//        }
        file = new File(pdfUrl + id + "/" + filename + ".html");
        if (!file.exists()) {
            file.createNewFile();
        }
        FileOutputStream out = new FileOutputStream(file);
        out.write(html.getBytes("UTF-8"));
        out.flush();
        out.close();
    }

    // 读取基础页面
    private String readBasisHtml(String path, String id, String filename) throws Exception {
        File file = new File(pdfUrl + id + "/" + filename + ".html");
        StringBuilder result = new StringBuilder();
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
        String s = null;
        while ((s = br.readLine()) != null) {
            result.append(System.lineSeparator() + s);
        }
        br.close();
        return result.toString();
    }

    // 生成PDF
    private void createPdf(String path, String id, String filename1, String filename2, boolean horizontal)
            throws Exception {
        String path1 = pdfUrl + id + "/" + filename1 + ".html";
        String path2 = pdfUrl + id + "/" + filename2 + ".pdf";
        String str = "";
        if (horizontal) {
            str = cmd + " --enable-local-file-access --disable-smart-shrinking -B 0 " + path1 + " " + path2;
        } else {
            str = cmd + " --enable-local-file-access --disable-smart-shrinking -B 0 -O landscape " + path1 + " " + path2;
        }
        Runtime.getRuntime().exec(str);
        // Thread.sleep(1000);
    }

    @Override
    public IPage getGrid(Page<Map> page, String orgCode, String titCode, String sampleNo, String sampleName, String sampleGcbw, String date, String reportNo, Integer liuchengleixing, String type, String state, String userId, String reportDate, String s) {
        IPage result = null;
        String beginDate = "";
        String endDate = "";
        if (StringUtil.isNotEmpty(date)) {
            String[] dates = date.split(" - ");
            beginDate = dates[0];
            endDate = dates[2];
        }
        String beginReportDate = "";
        String endReportDate = "";
        if (StringUtil.isNotEmpty(reportDate)) {
            String[] dates = reportDate.split(" 至 ");
            beginReportDate = dates[0];
            endReportDate = dates[2];
        }
        if (StringUtil.isEmpty(state)) {
            result = this.baseMapper.getSignatureGrid(page, orgCode, titCode, sampleNo, sampleName, sampleGcbw, beginDate, endDate,
                    reportNo, liuchengleixing, type, state, userId, beginReportDate, endReportDate, "");
        } else {
            result = this.baseMapper.getSignatureGrid2(page, orgCode, titCode, sampleNo, sampleName, sampleGcbw, beginDate, endDate,
                    reportNo, liuchengleixing, type, state, userId, beginReportDate, endReportDate, "");
        }
        return result;
    }

    @Override
    public IPage getGrid3(Page<Map> page, String orgCode, String titCode, String sampleNo, String sampleName, String sampleGcbw, String date, String reportNo, String tiNo, String username, String state, String self, String shenpizhuangtai) {
        String beginDate = "";
        String endDate = "";
        if (StringUtil.isNotEmpty(date)) {
            String[] dates = date.split(" 至 ");
            beginDate = dates[0];
            endDate = dates[1];
        }
        return this.baseMapper.getGrid3(page, orgCode, titCode, sampleNo, sampleName, sampleGcbw, beginDate, endDate,
                reportNo, tiNo, username, state, self, shenpizhuangtai);
    }


    /**
     * 动态修改表的字段值
     *
     * @param tableName 表名
     * @param key1      字段1
     * @param value1    字段1的值
     * @param key2      字段2
     * @param value2    字段2的值
     * @param key3      字段3
     * @param value3    字段3的值
     */
    public void dynamicRevise(String tableName, String key1, String value1, String key2, String value2, String key3, String value3) {
        this.baseMapper.dynamicRevise(tableName, key1, value1, key2, value2, key3, value3);
    }


    private Jj0406 jj0406(Jj0406 jj0406, String wtid, String lq, String sylx) {
        List<Map<String, Object>> listSYJZB = this.baseMapper.selectSyjzb(wtid, lq, sylx);
        if (listSYJZB.size() > 0) {
            Map<String, Object> mapSYJZB = listSYJZB.get(0);
            String SYJID = (String) mapSYJZB.get("SYJID");
            if (lq.equals("3") && sylx.equals("100136")) {
                jj0406.setChengxingriqi1((String) mapSYJZB.get("ZZRQ"));
                jj0406.setShiyanshijian1((String) mapSYJZB.get("SYRQ"));
                jj0406.setShijianqiling1(lq);
                jj0406.setKzsjcc3d((String) mapSYJZB.get("SJCC"));
                jj0406.setKangzhepingjunzhi1((String) mapSYJZB.get("QDDBZ"));
            } else if (lq.equals("7") && sylx.equals("100136")) {
                jj0406.setChengxingriqi1((String) mapSYJZB.get("ZZRQ"));
                jj0406.setShiyanshijian1((String) mapSYJZB.get("SYRQ"));
                jj0406.setShijianqiling1(lq);
                jj0406.setKzsjcc3d((String) mapSYJZB.get("SJCC"));
                jj0406.setKangzhepingjunzhi1((String) mapSYJZB.get("QDDBZ"));
            } else if (lq.equals("3") && sylx.equals("100135")) {
                jj0406.setChengxingriqi1((String) mapSYJZB.get("ZZRQ"));
                jj0406.setShiyanshijian1((String) mapSYJZB.get("SYRQ"));
                jj0406.setShijianqiling1(lq);
                jj0406.setShouyamianji11((String) mapSYJZB.get("SJMJ"));
                jj0406.setKangyapingjunzhi1((String) mapSYJZB.get("QDDBZ"));
            } else if (lq.equals("7") && sylx.equals("100135")) {
                jj0406.setChengxingriqi1((String) mapSYJZB.get("ZZRQ"));
                jj0406.setShiyanshijian1((String) mapSYJZB.get("SYRQ"));
                jj0406.setShijianqiling1(lq);
                jj0406.setShouyamianji11((String) mapSYJZB.get("SJMJ"));
                jj0406.setKangyapingjunzhi1((String) mapSYJZB.get("QDDBZ"));
            } else if (lq.equals("28") && sylx.equals("100136")) {
                jj0406.setChengxingriqi2((String) mapSYJZB.get("ZZRQ"));
                jj0406.setShiyanshijian2((String) mapSYJZB.get("SYRQ"));
                jj0406.setShijianqiling2(lq);
                jj0406.setKzsjcc28d((String) mapSYJZB.get("SJCC"));
                jj0406.setKangzhepingjunzhi2((String) mapSYJZB.get("QDDBZ"));
            } else if (lq.equals("28") && sylx.equals("100135")) {
                jj0406.setChengxingriqi2((String) mapSYJZB.get("ZZRQ"));
                jj0406.setShiyanshijian2((String) mapSYJZB.get("SYRQ"));
                jj0406.setShijianqiling2(lq);
                jj0406.setShouyamianji21((String) mapSYJZB.get("SJMJ"));
                jj0406.setKangyapingjunzhi2((String) mapSYJZB.get("QDDBZ"));
            }
            jj0406 = jj0406_2(jj0406, SYJID, lq, sylx);
        }
        return jj0406;
    }

    private Jj0406 jj0406_2(Jj0406 jj0406, String SYJID, String lq, String sylx) {
        List<Map<String, Object>> listylj = this.baseMapper.selectYaLiJi(SYJID);
        int size = listylj.size();
        if (size > 0) {
            jj0406 = jj0406_3(jj0406, size, lq, sylx, listylj);
        }
        return jj0406;
    }

    private Jj0406 jj0406_3(Jj0406 jj0406, int size, String lq, String sylx, List<Map<String, Object>> listylj) {
        if (lq.equals("3") && sylx.equals("100136")) {
            for (int l = 0; l < size; l++) {
                Map<String, Object> mapylj = listylj.get(l);
                switch (l) {
                    case 0:
                        jj0406.setKangzhepohuaihezai11(String.valueOf(mapylj.get("KYLZ")));
                        jj0406.setKangzhedanzhi11(String.valueOf(mapylj.get("KYQD")));
                        break;
                    case 1:
                        jj0406.setKangzhepohuaihezai12(String.valueOf(mapylj.get("KYLZ")));
                        jj0406.setKangzhedanzhi12(String.valueOf(mapylj.get("KYQD")));
                        break;
                    case 2:
                        jj0406.setKangzhepohuaihezai13(String.valueOf(mapylj.get("KYLZ")));
                        jj0406.setKangzhedanzhi13(String.valueOf(mapylj.get("KYQD")));
                        break;
                    default:
                        break;
                }
            }
        } else if (lq.equals("7") && sylx.equals("100136")) {
            for (int l = 0; l < size; l++) {
                Map<String, Object> mapylj = listylj.get(l);
                switch (l) {
                    case 0:
                        jj0406.setKangzhepohuaihezai11(String.valueOf(mapylj.get("KYLZ")));
                        jj0406.setKangzhedanzhi11(String.valueOf(mapylj.get("KYQD")));
                        break;
                    case 1:
                        jj0406.setKangzhepohuaihezai12(String.valueOf(mapylj.get("KYLZ")));
                        jj0406.setKangzhedanzhi12(String.valueOf(mapylj.get("KYQD")));
                        break;
                    case 2:
                        jj0406.setKangzhepohuaihezai13(String.valueOf(mapylj.get("KYLZ")));
                        jj0406.setKangzhedanzhi13(String.valueOf(mapylj.get("KYQD")));
                        break;
                    default:
                        break;

                }
            }
        } else if (lq.equals("3") && sylx.equals("100135")) {
            for (int l = 0; l < size; l++) {
                Map<String, Object> mapylj = listylj.get(l);
                switch (l) {
                    case 0:
                        jj0406.setKangyapohuaihezai11(String.valueOf(mapylj.get("KYLZ")));
                        jj0406.setKangyaqiangdudanzhi11(String.valueOf(mapylj.get("KYQD")));
                        break;
                    case 1:
                        jj0406.setKangyapohuaihezai12(String.valueOf(mapylj.get("KYLZ")));
                        jj0406.setKangyaqiangdudanzhi12(String.valueOf(mapylj.get("KYQD")));
                        break;
                    case 2:
                        jj0406.setKangyapohuaihezai13(String.valueOf(mapylj.get("KYLZ")));
                        jj0406.setKangyaqiangdudanzhi13(String.valueOf(mapylj.get("KYQD")));
                        break;
                    case 3:
                        jj0406.setKangyapohuaihezai14(String.valueOf(mapylj.get("KYLZ")));
                        jj0406.setKangyaqiangdudanzhi14(String.valueOf(mapylj.get("KYQD")));
                        break;
                    case 4:
                        jj0406.setKangyapohuaihezai15(String.valueOf(mapylj.get("KYLZ")));
                        jj0406.setKangyaqiangdudanzhi15(String.valueOf(mapylj.get("KYQD")));
                        break;
                    case 5:
                        jj0406.setKangyapohuaihezai16(String.valueOf(mapylj.get("KYLZ")));
                        jj0406.setKangyaqiangdudanzhi16(String.valueOf(mapylj.get("KYQD")));
                        break;
                    default:
                        break;
                }
            }
        } else if (lq.equals("7") && sylx.equals("100135")) {
            for (int l = 0; l < size; l++) {
                Map<String, Object> mapylj = listylj.get(l);
                switch (l) {
                    case 0:
                        jj0406.setKangyapohuaihezai11(String.valueOf(mapylj.get("KYLZ")));
                        jj0406.setKangyaqiangdudanzhi11(String.valueOf(mapylj.get("KYQD")));
                        break;
                    case 1:
                        jj0406.setKangyapohuaihezai12(String.valueOf(mapylj.get("KYLZ")));
                        jj0406.setKangyaqiangdudanzhi12(String.valueOf(mapylj.get("KYQD")));
                        break;
                    case 2:
                        jj0406.setKangyapohuaihezai13(String.valueOf(mapylj.get("KYLZ")));
                        jj0406.setKangyaqiangdudanzhi13(String.valueOf(mapylj.get("KYQD")));
                        break;
                    case 3:
                        jj0406.setKangyapohuaihezai14(String.valueOf(mapylj.get("KYLZ")));
                        jj0406.setKangyaqiangdudanzhi14(String.valueOf(mapylj.get("KYQD")));
                        break;
                    case 4:
                        jj0406.setKangyapohuaihezai15(String.valueOf(mapylj.get("KYLZ")));
                        jj0406.setKangyaqiangdudanzhi15(String.valueOf(mapylj.get("KYQD")));
                        break;
                    case 5:
                        jj0406.setKangyapohuaihezai16(String.valueOf(mapylj.get("KYLZ")));
                        jj0406.setKangyaqiangdudanzhi16(String.valueOf(mapylj.get("KYQD")));
                        break;
                    default:
                        break;
                }
            }
        } else if (lq.equals("28") && sylx.equals("100136")) {
            for (int l = 0; l < size; l++) {
                Map<String, Object> mapylj = listylj.get(l);
                switch (l) {
                    case 0:
                        jj0406.setKangzhepohuaihezai21(String.valueOf(mapylj.get("KYLZ")));
                        jj0406.setKangzhedanzhi21(String.valueOf(mapylj.get("KYQD")));
                        break;
                    case 1:
                        jj0406.setKangzhepohuaihezai22(String.valueOf(mapylj.get("KYLZ")));
                        jj0406.setKangzhedanzhi22(String.valueOf(mapylj.get("KYQD")));
                        break;
                    case 2:
                        jj0406.setKangzhepohuaihezai23(String.valueOf(mapylj.get("KYLZ")));
                        jj0406.setKangzhedanzhi23(String.valueOf(mapylj.get("KYQD")));
                        break;
                    default:
                        break;
                }
            }
        } else if (lq.equals("28") && sylx.equals("100135")) {
            for (int l = 0; l < size; l++) {
                Map<String, Object> mapylj = listylj.get(l);
                switch (l) {
                    case 0:
                        jj0406.setKangyapohuaihezai21(String.valueOf(mapylj.get("KYLZ")));
                        jj0406.setKangyaqiangdudanzhi21(String.valueOf(mapylj.get("KYQD")));
                        break;
                    case 1:
                        jj0406.setKangyapohuaihezai22(String.valueOf(mapylj.get("KYLZ")));
                        jj0406.setKangyaqiangdudanzhi22(String.valueOf(mapylj.get("KYQD")));
                        break;
                    case 2:
                        jj0406.setKangyapohuaihezai23(String.valueOf(mapylj.get("KYLZ")));
                        jj0406.setKangyaqiangdudanzhi23(String.valueOf(mapylj.get("KYQD")));
                        break;
                    case 3:
                        jj0406.setKangyapohuaihezai24(String.valueOf(mapylj.get("KYLZ")));
                        jj0406.setKangyaqiangdudanzhi24(String.valueOf(mapylj.get("KYQD")));
                        break;
                    case 4:
                        jj0406.setKangyapohuaihezai25(String.valueOf(mapylj.get("KYLZ")));
                        jj0406.setKangyaqiangdudanzhi25(String.valueOf(mapylj.get("KYQD")));
                        break;
                    case 5:
                        jj0406.setKangyapohuaihezai26(String.valueOf(mapylj.get("KYLZ")));
                        jj0406.setKangyaqiangdudanzhi26(String.valueOf(mapylj.get("KYQD")));
                        break;
                    default:
                        break;
                }
            }
        }
        return jj0406;
    }

    private Jj0406a jj0406a(Jj0406a jj0406a, String wtid, String lq, String sylx) {
        List<Map<String, Object>> listSYJZB = this.baseMapper.selectSyjzb(wtid, lq, sylx);
        if (listSYJZB.size() > 0) {
            Map<String, Object> mapSYJZB = listSYJZB.get(0);
            String SYJID = (String) mapSYJZB.get("SYJID");
            if (lq.equals("28") && sylx.equals("100136")) {
                jj0406a.setYhtj((String) mapSYJZB.get("FBL"));
                jj0406a.setCxrq((String) mapSYJZB.get("ZZRQ"));
                jj0406a.setKzrq28d((String) mapSYJZB.get("SYRQ"));
                //jj0406a.setKzsjcc28d((String) mapSYJZB.get("SJCC"));
                jj0406a.setKzqdavg28d((String) mapSYJZB.get("QDDBZ"));
            } else if (lq.equals("28") && sylx.equals("100135")) {
                jj0406a.setYhtj((String) mapSYJZB.get("FBL"));
                jj0406a.setCxrq((String) mapSYJZB.get("ZZRQ"));
                //jj0406a.setKysjmj28d((String) mapSYJZB.get("SJCC"));
                jj0406a.setKyrq28d((String) mapSYJZB.get("SYRQ"));
                jj0406a.setKyqdavg28d((String) mapSYJZB.get("QDDBZ"));
            }
            jj0406a = jj0406a_2(jj0406a, SYJID, lq, sylx);
        }
        return jj0406a;
    }

    private Jj0406a jj0406a_2(Jj0406a jj0406a, String SYJID, String lq, String sylx) {
        List<Map<String, Object>> listylj = this.baseMapper.selectYaLiJi(SYJID);
        int size = listylj.size();
        if (size > 0) {
            jj0406a = jj0406a_3(jj0406a, size, lq, sylx, listylj);
        }
        return jj0406a;
    }

    private Jj0406a jj0406a_3(Jj0406a jj0406a, int size, String lq, String sylx, List<Map<String, Object>> listylj) {
        if (lq.equals("28") && sylx.equals("100136")) {
            for (int l = 0; l < size; l++) {
                Map<String, Object> mapylj = listylj.get(l);
                switch (l) {
                    case 0:
                        jj0406a.setKzhz28d1(String.valueOf(mapylj.get("KYLZ")));
                        jj0406a.setKzqd28d1(String.valueOf(mapylj.get("KYQD")));
                        break;
                    case 1:
                        jj0406a.setKzhz28d2(String.valueOf(mapylj.get("KYLZ")));
                        jj0406a.setKzqd28d2(String.valueOf(mapylj.get("KYQD")));
                        break;
                    case 2:
                        jj0406a.setKzhz28d3(String.valueOf(mapylj.get("KYLZ")));
                        jj0406a.setKzqd28d3(String.valueOf(mapylj.get("KYQD")));
                        break;
                    default:
                        break;
                }
            }
        } else if (lq.equals("28") && sylx.equals("100135")) {
            for (int l = 0; l < size; l++) {
                Map<String, Object> mapylj = listylj.get(l);
                switch (l) {
                    case 0:
                        jj0406a.setKyhz28d1(String.valueOf(mapylj.get("KYLZ")));
                        jj0406a.setKyqd28d1(String.valueOf(mapylj.get("KYQD")));
                        break;
                    case 1:
                        jj0406a.setKyhz28d2(String.valueOf(mapylj.get("KYLZ")));
                        jj0406a.setKyqd28d2(String.valueOf(mapylj.get("KYQD")));
                        break;
                    case 2:
                        jj0406a.setKyhz28d3(String.valueOf(mapylj.get("KYLZ")));
                        jj0406a.setKyqd28d3(String.valueOf(mapylj.get("KYQD")));
                        break;
                    case 3:
                        jj0406a.setKyhz28d4(String.valueOf(mapylj.get("KYLZ")));
                        jj0406a.setKyqd28d4(String.valueOf(mapylj.get("KYQD")));
                        break;
                    case 4:
                        jj0406a.setKyhz28d5(String.valueOf(mapylj.get("KYLZ")));
                        jj0406a.setKyqd28d5(String.valueOf(mapylj.get("KYQD")));
                        break;
                    case 5:
                        jj0406a.setKyhz28d6(String.valueOf(mapylj.get("KYLZ")));
                        jj0406a.setKyqd28d6(String.valueOf(mapylj.get("KYQD")));
                        break;
                    default:
                        break;
                }
            }
        }
        return jj0406a;
    }

    private Cs312 cs312(Cs312 cs312, String wtid, String lq, String sylx) {
        List<Map<String, Object>> listSYJZB = this.baseMapper.selectSyjzb(wtid, lq, sylx);
        if (listSYJZB.size() > 0) {
            Map<String, Object> mapSYJZB = listSYJZB.get(0);
            String SYJID = (String) mapSYJZB.get("SYJID");
            if (lq.equals("3") && sylx.equals("100136")) {
                cs312.setKzrq3d((String) mapSYJZB.get("ZZRQ"));
                cs312.setKzqdavg3d((String) mapSYJZB.get("QDDBZ"));
            } else if (lq.equals("28") && sylx.equals("100136")) {
                cs312.setKzrq28d((String) mapSYJZB.get("ZZRQ"));
                cs312.setKzqdavg28d((String) mapSYJZB.get("QDDBZ"));
            } else if (lq.equals("3") && sylx.equals("100135")) {
                cs312.setCxrq((String) mapSYJZB.get("ZZRQ"));
                cs312.setKyqdavg3d((String) mapSYJZB.get("QDDBZ"));
            } else if (lq.equals("28") && sylx.equals("100135")) {
                cs312.setCxrq((String) mapSYJZB.get("ZZRQ"));
                // cs312.setKyqdavg28d((String) mapSYJZB.get("QDDBZ"));
            }
            cs312 = cs312_2(cs312, SYJID, lq, sylx);
        }
        return cs312;
    }

    private Cs312 cs312_2(Cs312 cs312, String SYJID, String lq, String sylx) {
        List<Map<String, Object>> listylj = this.baseMapper.selectYaLiJi(SYJID);
        int size = listylj.size();
        if (size > 0) {
            cs312 = cs312_3(cs312, size, lq, sylx, listylj);
        }
        return cs312;
    }

    private Cs312 cs312_3(Cs312 cs312, int size, String lq, String sylx, List<Map<String, Object>> listylj) {
        if (lq.equals("3") && sylx.equals("100136")) {
            for (int l = 0; l < size; l++) {
                Map<String, Object> mapylj = listylj.get(l);
                switch (l) {
                    case 0:
                        // cs312.setKzhz3d1(String.valueOf(mapylj.get("KYLZ")));
                        cs312.setKzqd3d1(String.valueOf(mapylj.get("KYQD")));
                        break;
                    case 1:
                        // cs312.setKzhz3d2(String.valueOf(mapylj.get("KYLZ")));
                        cs312.setKzqd3d2(String.valueOf(mapylj.get("KYQD")));
                        break;
                    case 2:
                        // cs312.setKzhz3d3(String.valueOf(mapylj.get("KYLZ")));
                        cs312.setKzqd3d3(String.valueOf(mapylj.get("KYQD")));
                        break;
                    default:
                        break;
                }
            }
        } else if (lq.equals("28") && sylx.equals("100136")) {
            for (int l = 0; l < size; l++) {
                Map<String, Object> mapylj = listylj.get(l);
                switch (l) {
                    case 0:
                        // cs312.setKzhz28d1(String.valueOf(mapylj.get("KYLZ")));
                        cs312.setKzqd28d1(String.valueOf(mapylj.get("KYQD")));
                        break;
                    case 1:
                        // cs312.setKzhz28d2(String.valueOf(mapylj.get("KYLZ")));
                        cs312.setKzqd28d2(String.valueOf(mapylj.get("KYQD")));
                        break;
                    case 2:
                        // cs312.setKzhz28d3(String.valueOf(mapylj.get("KYLZ")));
                        cs312.setKzqd28d3(String.valueOf(mapylj.get("KYQD")));
                        break;
                    default:
                        break;
                }
            }
        } else if (lq.equals("3") && sylx.equals("100135")) {
            for (int l = 0; l < size; l++) {
                Map<String, Object> mapylj = listylj.get(l);
                switch (l) {
                    case 0:
                        cs312.setKyhz3d1(String.valueOf(mapylj.get("KYLZ")));
                        // cs312.setKyqd3d1(String.valueOf(mapylj.get("KYQD")));
                        break;
                    case 1:
                        cs312.setKyhz3d2(String.valueOf(mapylj.get("KYLZ")));
                        // cs312.setKyqd3d2(String.valueOf(mapylj.get("KYQD")));
                        break;
                    case 2:
                        cs312.setKyhz3d3(String.valueOf(mapylj.get("KYLZ")));
                        // cs312.setKyqd3d3(String.valueOf(mapylj.get("KYQD")));
                        break;
                    case 3:
                        cs312.setKyhz3d4(String.valueOf(mapylj.get("KYLZ")));
                        // cs312.setKyqd3d4(String.valueOf(mapylj.get("KYQD")));
                        break;
                    case 4:
                        cs312.setKyhz3d5(String.valueOf(mapylj.get("KYLZ")));
                        // cs312.setKyqd3d5(String.valueOf(mapylj.get("KYQD")));
                        break;
                    case 5:
                        cs312.setKyhz3d6(String.valueOf(mapylj.get("KYLZ")));
                        // cs312.setKyqd3d6(String.valueOf(mapylj.get("KYQD")));
                        break;
                    default:
                        break;
                }
            }
        } else if (lq.equals("28") && sylx.equals("100135")) {
            for (int l = 0; l < size; l++) {
                Map<String, Object> mapylj = listylj.get(l);
                switch (l) {
                    case 0:
                        cs312.setKyhz28d1(String.valueOf(mapylj.get("KYLZ")));
                        // cs312.setKyqd28d1(String.valueOf(mapylj.get("KYQD")));
                        break;
                    case 1:
                        cs312.setKyhz28d2(String.valueOf(mapylj.get("KYLZ")));
                        // cs312.setKyqd28d2(String.valueOf(mapylj.get("KYQD")));
                        break;
                    case 2:
                        cs312.setKyhz28d3(String.valueOf(mapylj.get("KYLZ")));
                        // cs312.setKyqd28d3(String.valueOf(mapylj.get("KYQD")));
                        break;
                    case 3:
                        cs312.setKyhz28d4(String.valueOf(mapylj.get("KYLZ")));
                        // cs312.setKyqd28d4(String.valueOf(mapylj.get("KYQD")));
                        break;
                    case 4:
                        cs312.setKyhz28d5(String.valueOf(mapylj.get("KYLZ")));
                        // cs312.setKyqd28d5(String.valueOf(mapylj.get("KYQD")));
                        break;
                    case 5:
                        cs312.setKyhz28d6(String.valueOf(mapylj.get("KYLZ")));
                        // cs312.setKyqd28d6(String.valueOf(mapylj.get("KYQD")));
                        break;
                    default:
                        break;
                }
            }
        }
        return cs312;
    }

    private Jj0501a jj0501a(Jj0501a jj0501a, List<TSyjzb> listAll, int tiNoTemp) {
//		if (listAll.size() > 6) {
//			for (int l = 0; l < 6; l++) {
//				SyjzbEntity entity = listAll.get(l);
//				jj0501a = jj0501a_1(jj0501a, entity, l);
//			}
//		} else {
//			for (int l = 0; l < listAll.size(); l++) {
//				SyjzbEntity entity = listAll.get(l);
//				jj0501a = jj0501a_1(jj0501a, entity, l);
//			}
//		}
        int qs = (tiNoTemp - 1) * 5;
        int js = qs + 5;
        if (js > listAll.size()) {
            js = listAll.size();
        }
        for (int l = qs; l < js; l++) {
            TSyjzb entity = listAll.get(l);
            if (qs >= 5) {
                jj0501a = jj0501a_1(jj0501a, entity, l - qs);
            } else {
                jj0501a = jj0501a_1(jj0501a, entity, l);
            }

        }

        return jj0501a;
    }

    private Jj0501a jj0501a_1(Jj0501a jj0501a, TSyjzb entity, int l) {
        String syjid = entity.getSyjid();
        switch (l) {
            case 0:
                jj0501a.setSjbh1(entity.getSjbh());
                // jj0501a.setJiegbw1(entity.getCjmc());
                jj0501a.setCxrq1(entity.getZzrq());
                jj0501a.setQddj1(entity.getSjqd());
                jj0501a.setSyrq1(entity.getSyrq());
                jj0501a.setLingqi1(String.valueOf(entity.getLq()));
                jj0501a.setSjcc1(entity.getSjcc());
                jj0501a.setKyqdpjz1(entity.getQddbz());
                jj0501a.setKyqdhsz1(entity.getQddbz());
                List<Map<String, Object>> listylj = this.baseMapper.selectYaLiJi(syjid);
                if (listylj.size() > 3) {
                    for (int p = 0; p < 3; p++) {
                        jj0501a = jj0501a_2(jj0501a, listylj, l, p);
                    }
                } else {
                    for (int p = 0; p < listylj.size(); p++) {
                        jj0501a = jj0501a_2(jj0501a, listylj, l, p);
                    }
                }
                break;
            case 1:
                jj0501a.setSjbh2(entity.getSjbh());
                // jj0501a.setJiegbw1(entity.getCjmc());
                jj0501a.setCxrq2(entity.getZzrq());
                jj0501a.setQddj2(entity.getSjqd());
                jj0501a.setSyrq2(entity.getSyrq());
                jj0501a.setLingqi2(String.valueOf(entity.getLq()));
                jj0501a.setSjcc2(entity.getSjcc());
                jj0501a.setKyqdpjz2(entity.getQddbz());
                jj0501a.setKyqdhsz2(entity.getQddbz());
                List<Map<String, Object>> listylj2 = this.baseMapper.selectYaLiJi(syjid);
                if (listylj2.size() > 3) {
                    for (int p = 0; p < 3; p++) {
                        jj0501a = jj0501a_2(jj0501a, listylj2, l, p);
                    }
                } else {
                    for (int p = 0; p < listylj2.size(); p++) {
                        jj0501a = jj0501a_2(jj0501a, listylj2, l, p);
                    }
                }
                break;
            case 2:
                jj0501a.setSjbh3(entity.getSjbh());
                // jj0501a.setJiegbw1(entity.getCjmc());
                jj0501a.setCxrq3(entity.getZzrq());
                jj0501a.setQddj3(entity.getSjqd());
                jj0501a.setSyrq3(entity.getSyrq());
                jj0501a.setLingqi3(String.valueOf(entity.getLq()));
                jj0501a.setSjcc3(entity.getSjcc());
                jj0501a.setKyqdpjz3(entity.getQddbz());
                jj0501a.setKyqdhsz3(entity.getQddbz());
                List<Map<String, Object>> listylj3 = this.baseMapper.selectYaLiJi(syjid);
                if (listylj3.size() > 3) {
                    for (int p = 0; p < 3; p++) {
                        jj0501a = jj0501a_2(jj0501a, listylj3, l, p);
                    }
                } else {
                    for (int p = 0; p < listylj3.size(); p++) {
                        jj0501a = jj0501a_2(jj0501a, listylj3, l, p);
                    }
                }
                break;
            case 3:
                jj0501a.setSjbh4(entity.getSjbh());
                // jj0501a.setJiegbw1(entity.getCjmc());
                jj0501a.setCxrq4(entity.getZzrq());
                jj0501a.setQddj4(entity.getSjqd());
                jj0501a.setSyrq4(entity.getSyrq());
                jj0501a.setLingqi4(String.valueOf(entity.getLq()));
                jj0501a.setSjcc4(entity.getSjcc());
                jj0501a.setKyqdpjz4(entity.getQddbz());
                jj0501a.setKyqdhsz4(entity.getQddbz());
                List<Map<String, Object>> listylj4 = this.baseMapper.selectYaLiJi(syjid);
                if (listylj4.size() > 3) {
                    for (int p = 0; p < 3; p++) {
                        jj0501a = jj0501a_2(jj0501a, listylj4, l, p);
                    }
                } else {
                    for (int p = 0; p < listylj4.size(); p++) {
                        jj0501a = jj0501a_2(jj0501a, listylj4, l, p);
                    }
                }
                break;
            case 4:
                jj0501a.setSjbh5(entity.getSjbh());
                // jj0501a.setJiegbw1(entity.getCjmc());
                jj0501a.setCxrq5(entity.getZzrq());
                jj0501a.setQddj5(entity.getSjqd());
                jj0501a.setSyrq5(entity.getSyrq());
                jj0501a.setLingqi5(String.valueOf(entity.getLq()));
                jj0501a.setSjcc5(entity.getSjcc());
                jj0501a.setKyqdpjz5(entity.getQddbz());
                jj0501a.setKyqdhsz5(entity.getQddbz());
                List<Map<String, Object>> listylj5 = this.baseMapper.selectYaLiJi(syjid);
                if (listylj5.size() > 3) {
                    for (int p = 0; p < 3; p++) {
                        jj0501a = jj0501a_2(jj0501a, listylj5, l, p);
                    }
                } else {
                    for (int p = 0; p < listylj5.size(); p++) {
                        jj0501a = jj0501a_2(jj0501a, listylj5, l, p);
                    }
                }
                break;
            // case 5:
            // jj0501a.setShijbh6(entity.getSjbh());
            // jj0501a.setJiegbw6(entity.getCjmc());
            // jj0501a.setZhijrq6(entity.getZzrq());
            // jj0501a.setLingq6(String.valueOf(entity.getLq()));
            // jj0501a.setShiyrq6(entity.getSyrq());
            // jj0501a.setShijcc6(entity.getSjcc());
            // jj0501a.setHuansqd6(entity.getQddbz());
            // jj0501a.setKangyqdpingjz6(entity.getQddbz());
            // List<Map<String, Object>> listylj6 = systemService
            // .findForJdbc(" select KYLZ,KYQD from F_Yaliji where SYJID = '" + syjid + "'
            // ORDER BY SJXH");
            // if (listylj6.size() > 3) {
            // for (int p = 0; p < 3; p++) {
            // jj0501a = jj0501a_2(jj0501a, listylj6, l, p);
            // }
            // } else {
            // for (int p = 0; p < listylj6.size(); p++) {
            // jj0501a = jj0501a_2(jj0501a, listylj6, l, p);
            // }
            // }
            // break;
            default:
                break;
        }
        return jj0501a;
    }

    private Jj0501a jj0501a_2(Jj0501a jj0501a, List<Map<String, Object>> listylj, int l, int p) {
        Map<String, Object> map = listylj.get(p);
        switch (l) {
            case 0:
                switch (p) {
                    case 0:
                        jj0501a.setJxhz11((String) map.get("KYLZ"));
                        jj0501a.setKyqd11((String) map.get("KYQD"));
                    case 1:
                        jj0501a.setJxhz12((String) map.get("KYLZ"));
                        jj0501a.setKyqd12((String) map.get("KYQD"));
                    case 2:
                        jj0501a.setJxhz13((String) map.get("KYLZ"));
                        jj0501a.setKyqd13((String) map.get("KYQD"));
                    default:
                        break;
                }
                break;
            case 1:
                switch (p) {
                    case 0:
                        jj0501a.setJxhz21((String) map.get("KYLZ"));
                        jj0501a.setKyqd21((String) map.get("KYQD"));
                    case 1:
                        jj0501a.setJxhz22((String) map.get("KYLZ"));
                        jj0501a.setKyqd22((String) map.get("KYQD"));
                    case 2:
                        jj0501a.setJxhz23((String) map.get("KYLZ"));
                        jj0501a.setKyqd23((String) map.get("KYQD"));
                    default:
                        break;
                }
                break;
            case 2:
                switch (p) {
                    case 0:
                        jj0501a.setJxhz31((String) map.get("KYLZ"));
                        jj0501a.setKyqd31((String) map.get("KYQD"));
                    case 1:
                        jj0501a.setJxhz32((String) map.get("KYLZ"));
                        jj0501a.setKyqd32((String) map.get("KYQD"));
                    case 2:
                        jj0501a.setJxhz33((String) map.get("KYLZ"));
                        jj0501a.setKyqd33((String) map.get("KYQD"));
                    default:
                        break;
                }
                break;
            case 3:
                switch (p) {
                    case 0:
                        jj0501a.setJxhz41((String) map.get("KYLZ"));
                        jj0501a.setKyqd41((String) map.get("KYQD"));
                    case 1:
                        jj0501a.setJxhz42((String) map.get("KYLZ"));
                        jj0501a.setKyqd42((String) map.get("KYQD"));
                    case 2:
                        jj0501a.setJxhz43((String) map.get("KYLZ"));
                        jj0501a.setKyqd43((String) map.get("KYQD"));
                    default:
                        break;
                }
                break;
            case 4:
                switch (p) {
                    case 0:
                        jj0501a.setJxhz51((String) map.get("KYLZ"));
                        jj0501a.setKyqd51((String) map.get("KYQD"));
                    case 1:
                        jj0501a.setJxhz52((String) map.get("KYLZ"));
                        jj0501a.setKyqd52((String) map.get("KYQD"));
                    case 2:
                        jj0501a.setJxhz53((String) map.get("KYLZ"));
                        jj0501a.setKyqd53((String) map.get("KYQD"));
                    default:
                        break;
                }
                break;
            // case 5:
            // switch (p) {
            // case 0:
            // jj0501a.setPohhz61((String) map.get("KYLZ"));
            // jj0501a.setKangyqddz61((String) map.get("KYQD"));
            // case 1:
            // jj0501a.setPohhz62((String) map.get("KYLZ"));
            // jj0501a.setKangyqddz62((String) map.get("KYQD"));
            // case 2:
            // jj0501a.setPohhz63((String) map.get("KYLZ"));
            // jj0501a.setKangyqddz63((String) map.get("KYQD"));
            // default:
            // break;
            // }
            // break;
            default:
                break;
        }
        return jj0501a;
    }

    private Jj0501b1 jj0501b1(Jj0501b1 jj0501b1, List<TSyjzb> listAll) {
        if (listAll.size() > 6) {
            for (int l = 0; l < 6; l++) {
                TSyjzb entity = listAll.get(l);
                jj0501b1 = jj0501b1_1(jj0501b1, entity, l);
            }
        } else {
            for (int l = 0; l < listAll.size(); l++) {
                TSyjzb entity = listAll.get(l);
                jj0501b1 = jj0501b1_1(jj0501b1, entity, l);
            }
        }
        return jj0501b1;
    }

    private Jj0501b1 jj0501b1_1(Jj0501b1 jj0501b1, TSyjzb entity, int l) {
        String syjid = entity.getSyjid();
        switch (l) {
            case 0:
                jj0501b1.setSjbh1(entity.getSjbh());
                // jj0501a.setJiegbw1(entity.getCjmc());
                jj0501b1.setCxrq1(entity.getZzrq());
                jj0501b1.setQddj1(entity.getSjqd());
                jj0501b1.setSyrq1(entity.getSyrq());
                jj0501b1.setLingqi1(String.valueOf(entity.getLq()));
                jj0501b1.setSjcc1(entity.getSjcc());
                //jj0501b1.(entity.getQddbz());
                jj0501b1.setKyqdhsz1(entity.getQddbz());
                List<Map<String, Object>> listylj = this.baseMapper.selectYaLiJi(syjid);
                if (listylj.size() > 3) {
                    for (int p = 0; p < 3; p++) {
                        jj0501b1 = jj0501b1_2(jj0501b1, listylj, l, p);
                    }
                } else {
                    for (int p = 0; p < listylj.size(); p++) {
                        jj0501b1 = jj0501b1_2(jj0501b1, listylj, l, p);
                    }
                }
                break;
            case 1:
                jj0501b1.setSjbh2(entity.getSjbh());
                // jj0501a.setJiegbw1(entity.getCjmc());
                jj0501b1.setCxrq2(entity.getZzrq());
                jj0501b1.setQddj2(entity.getSjqd());
                jj0501b1.setSyrq2(entity.getSyrq());
                jj0501b1.setLingqi2(String.valueOf(entity.getLq()));
                jj0501b1.setSjcc2(entity.getSjcc());
                //jj0501b1.setKyqdpjz2(entity.getQddbz());
                jj0501b1.setKyqdhsz2(entity.getQddbz());
                List<Map<String, Object>> listylj2 = this.baseMapper.selectYaLiJi(syjid);
                if (listylj2.size() > 3) {
                    for (int p = 0; p < 3; p++) {
                        jj0501b1 = jj0501b1_2(jj0501b1, listylj2, l, p);
                    }
                } else {
                    for (int p = 0; p < listylj2.size(); p++) {
                        jj0501b1 = jj0501b1_2(jj0501b1, listylj2, l, p);
                    }
                }
                break;
            case 2:
                jj0501b1.setSjbh3(entity.getSjbh());
                // jj0501a.setJiegbw1(entity.getCjmc());
                jj0501b1.setCxrq3(entity.getZzrq());
                jj0501b1.setQddj3(entity.getSjqd());
                jj0501b1.setSyrq3(entity.getSyrq());
                jj0501b1.setLingqi3(String.valueOf(entity.getLq()));
                jj0501b1.setSjcc3(entity.getSjcc());
                //jj0501b1.setKyqdpjz3(entity.getQddbz());
                jj0501b1.setKyqdhsz3(entity.getQddbz());
                List<Map<String, Object>> listylj3 = this.baseMapper.selectYaLiJi(syjid);
                if (listylj3.size() > 3) {
                    for (int p = 0; p < 3; p++) {
                        jj0501b1 = jj0501b1_2(jj0501b1, listylj3, l, p);
                    }
                } else {
                    for (int p = 0; p < listylj3.size(); p++) {
                        jj0501b1 = jj0501b1_2(jj0501b1, listylj3, l, p);
                    }
                }
                break;
            case 3:
                jj0501b1.setSjbh4(entity.getSjbh());
                // jj0501a.setJiegbw1(entity.getCjmc());
                jj0501b1.setCxrq4(entity.getZzrq());
                jj0501b1.setQddj4(entity.getSjqd());
                jj0501b1.setSyrq4(entity.getSyrq());
                jj0501b1.setLingqi4(String.valueOf(entity.getLq()));
                jj0501b1.setSjcc4(entity.getSjcc());
                //jj0501b1.setKyqdpjz4(entity.getQddbz());
                jj0501b1.setKyqdhsz4(entity.getQddbz());
                List<Map<String, Object>> listylj4 = this.baseMapper.selectYaLiJi(syjid);
                if (listylj4.size() > 3) {
                    for (int p = 0; p < 3; p++) {
                        jj0501b1 = jj0501b1_2(jj0501b1, listylj4, l, p);
                    }
                } else {
                    for (int p = 0; p < listylj4.size(); p++) {
                        jj0501b1 = jj0501b1_2(jj0501b1, listylj4, l, p);
                    }
                }
                break;
            case 4:
                jj0501b1.setSjbh5(entity.getSjbh());
                // jj0501a.setJiegbw1(entity.getCjmc());
                jj0501b1.setCxrq5(entity.getZzrq());
                jj0501b1.setQddj5(entity.getSjqd());
                jj0501b1.setSyrq5(entity.getSyrq());
                jj0501b1.setLingqi5(String.valueOf(entity.getLq()));
                jj0501b1.setSjcc5(entity.getSjcc());
                //jj0501b1.setKyqdpjz5(entity.getQddbz());
                jj0501b1.setKyqdhsz5(entity.getQddbz());
                List<Map<String, Object>> listylj5 = this.baseMapper.selectYaLiJi(syjid);
                if (listylj5.size() > 3) {
                    for (int p = 0; p < 3; p++) {
                        jj0501b1 = jj0501b1_2(jj0501b1, listylj5, l, p);
                    }
                } else {
                    for (int p = 0; p < listylj5.size(); p++) {
                        jj0501b1 = jj0501b1_2(jj0501b1, listylj5, l, p);
                    }
                }
                break;
            default:
                break;
        }
        return jj0501b1;
    }

    private Jj0501b1 jj0501b1_2(Jj0501b1 jj0501b1, List<Map<String, Object>> listylj, int l, int p) {
        Map<String, Object> map = listylj.get(p);
        switch (l) {
            case 0:
                switch (p) {
                    case 0:
                        jj0501b1.setJxhz11((String) map.get("KYLZ"));
                        jj0501b1.setKyqd11((String) map.get("KYQD"));
                    case 1:
                        jj0501b1.setJxhz12((String) map.get("KYLZ"));
                        jj0501b1.setKyqd12((String) map.get("KYQD"));
                    case 2:
                        jj0501b1.setJxhz13((String) map.get("KYLZ"));
                        jj0501b1.setKyqd13((String) map.get("KYQD"));
                    default:
                        break;
                }
                break;
            case 1:
                switch (p) {
                    case 0:
                        jj0501b1.setJxhz21((String) map.get("KYLZ"));
                        jj0501b1.setKyqd21((String) map.get("KYQD"));
                    case 1:
                        jj0501b1.setJxhz22((String) map.get("KYLZ"));
                        jj0501b1.setKyqd22((String) map.get("KYQD"));
                    case 2:
                        jj0501b1.setJxhz23((String) map.get("KYLZ"));
                        jj0501b1.setKyqd23((String) map.get("KYQD"));
                    default:
                        break;
                }
                break;
            case 2:
                switch (p) {
                    case 0:
                        jj0501b1.setJxhz31((String) map.get("KYLZ"));
                        jj0501b1.setKyqd31((String) map.get("KYQD"));
                    case 1:
                        jj0501b1.setJxhz32((String) map.get("KYLZ"));
                        jj0501b1.setKyqd32((String) map.get("KYQD"));
                    case 2:
                        jj0501b1.setJxhz33((String) map.get("KYLZ"));
                        jj0501b1.setKyqd33((String) map.get("KYQD"));
                    default:
                        break;
                }
                break;
            case 3:
                switch (p) {
                    case 0:
                        jj0501b1.setJxhz41((String) map.get("KYLZ"));
                        jj0501b1.setKyqd41((String) map.get("KYQD"));
                    case 1:
                        jj0501b1.setJxhz42((String) map.get("KYLZ"));
                        jj0501b1.setKyqd42((String) map.get("KYQD"));
                    case 2:
                        jj0501b1.setJxhz43((String) map.get("KYLZ"));
                        jj0501b1.setKyqd43((String) map.get("KYQD"));
                    default:
                        break;
                }
                break;
            case 4:
                switch (p) {
                    case 0:
                        jj0501b1.setJxhz51((String) map.get("KYLZ"));
                        jj0501b1.setKyqd51((String) map.get("KYQD"));
                    case 1:
                        jj0501b1.setJxhz52((String) map.get("KYLZ"));
                        jj0501b1.setKyqd52((String) map.get("KYQD"));
                    case 2:
                        jj0501b1.setJxhz53((String) map.get("KYLZ"));
                        jj0501b1.setKyqd53((String) map.get("KYQD"));
                    default:
                        break;
                }
                break;
            default:
                break;
        }
        return jj0501b1;
    }

    private Jj0501a1 jj0501a1(Jj0501a1 jj0501a1, List<TSyjzb> listAll) {
        if (listAll.size() > 6) {
            for (int l = 0; l < 6; l++) {
                TSyjzb entity = listAll.get(l);
                jj0501a1 = jj0501a1_1(jj0501a1, entity, l);
            }
        } else {
            for (int l = 0; l < listAll.size(); l++) {
                TSyjzb entity = listAll.get(l);
                jj0501a1 = jj0501a1_1(jj0501a1, entity, l);
            }
        }
        return jj0501a1;
    }

    private Jj0501a1 jj0501a1_1(Jj0501a1 jj0501a1, TSyjzb entity, int l) {
        String syjid = entity.getSyjid();
        switch (l) {
            case 0:
                jj0501a1.setSjbh1(entity.getSjbh());
                // jj0501a.setJiegbw1(entity.getCjmc());
                jj0501a1.setCxrq1(entity.getZzrq());
                jj0501a1.setQddj1(entity.getSjqd());
                jj0501a1.setSyrq1(entity.getSyrq());
                jj0501a1.setLingqi1(String.valueOf(entity.getLq()));
                jj0501a1.setSjcc1(entity.getSjcc());
                jj0501a1.setKyqdpjz1(entity.getQddbz());
                jj0501a1.setKyqdhsz1(entity.getQddbz());
                List<Map<String, Object>> listylj = this.baseMapper.selectYaLiJi(syjid);
                if (listylj.size() > 3) {
                    for (int p = 0; p < 3; p++) {
                        jj0501a1 = jj0501a1_2(jj0501a1, listylj, l, p);
                    }
                } else {
                    for (int p = 0; p < listylj.size(); p++) {
                        jj0501a1 = jj0501a1_2(jj0501a1, listylj, l, p);
                    }
                }
                break;
            case 1:
                jj0501a1.setSjbh2(entity.getSjbh());
                // jj0501a.setJiegbw1(entity.getCjmc());
                jj0501a1.setCxrq2(entity.getZzrq());
                jj0501a1.setQddj2(entity.getSjqd());
                jj0501a1.setSyrq2(entity.getSyrq());
                jj0501a1.setLingqi2(String.valueOf(entity.getLq()));
                jj0501a1.setSjcc2(entity.getSjcc());
                jj0501a1.setKyqdpjz2(entity.getQddbz());
                jj0501a1.setKyqdhsz2(entity.getQddbz());
                List<Map<String, Object>> listylj2 = this.baseMapper.selectYaLiJi(syjid);
                if (listylj2.size() > 3) {
                    for (int p = 0; p < 3; p++) {
                        jj0501a1 = jj0501a1_2(jj0501a1, listylj2, l, p);
                    }
                } else {
                    for (int p = 0; p < listylj2.size(); p++) {
                        jj0501a1 = jj0501a1_2(jj0501a1, listylj2, l, p);
                    }
                }
                break;
            case 2:
                jj0501a1.setSjbh3(entity.getSjbh());
                // jj0501a.setJiegbw1(entity.getCjmc());
                jj0501a1.setCxrq3(entity.getZzrq());
                jj0501a1.setQddj3(entity.getSjqd());
                jj0501a1.setSyrq3(entity.getSyrq());
                jj0501a1.setLingqi3(String.valueOf(entity.getLq()));
                jj0501a1.setSjcc3(entity.getSjcc());
                jj0501a1.setKyqdpjz3(entity.getQddbz());
                jj0501a1.setKyqdhsz3(entity.getQddbz());
                List<Map<String, Object>> listylj3 = this.baseMapper.selectYaLiJi(syjid);
                if (listylj3.size() > 3) {
                    for (int p = 0; p < 3; p++) {
                        jj0501a1 = jj0501a1_2(jj0501a1, listylj3, l, p);
                    }
                } else {
                    for (int p = 0; p < listylj3.size(); p++) {
                        jj0501a1 = jj0501a1_2(jj0501a1, listylj3, l, p);
                    }
                }
                break;
            case 3:
                jj0501a1.setSjbh4(entity.getSjbh());
                // jj0501a.setJiegbw1(entity.getCjmc());
                jj0501a1.setCxrq4(entity.getZzrq());
                jj0501a1.setQddj4(entity.getSjqd());
                jj0501a1.setSyrq4(entity.getSyrq());
                jj0501a1.setLingqi4(String.valueOf(entity.getLq()));
                jj0501a1.setSjcc4(entity.getSjcc());
                jj0501a1.setKyqdpjz4(entity.getQddbz());
                jj0501a1.setKyqdhsz4(entity.getQddbz());
                List<Map<String, Object>> listylj4 = this.baseMapper.selectYaLiJi(syjid);
                if (listylj4.size() > 3) {
                    for (int p = 0; p < 3; p++) {
                        jj0501a1 = jj0501a1_2(jj0501a1, listylj4, l, p);
                    }
                } else {
                    for (int p = 0; p < listylj4.size(); p++) {
                        jj0501a1 = jj0501a1_2(jj0501a1, listylj4, l, p);
                    }
                }
                break;
            case 4:
                jj0501a1.setSjbh5(entity.getSjbh());
                // jj0501a.setJiegbw1(entity.getCjmc());
                jj0501a1.setCxrq5(entity.getZzrq());
                jj0501a1.setQddj5(entity.getSjqd());
                jj0501a1.setSyrq5(entity.getSyrq());
                jj0501a1.setLingqi5(String.valueOf(entity.getLq()));
                jj0501a1.setSjcc5(entity.getSjcc());
                jj0501a1.setKyqdpjz5(entity.getQddbz());
                jj0501a1.setKyqdhsz5(entity.getQddbz());
                List<Map<String, Object>> listylj5 = this.baseMapper.selectYaLiJi(syjid);
                if (listylj5.size() > 3) {
                    for (int p = 0; p < 3; p++) {
                        jj0501a1 = jj0501a1_2(jj0501a1, listylj5, l, p);
                    }
                } else {
                    for (int p = 0; p < listylj5.size(); p++) {
                        jj0501a1 = jj0501a1_2(jj0501a1, listylj5, l, p);
                    }
                }
                break;
            default:
                break;
        }
        return jj0501a1;
    }

    private Jj0501a1 jj0501a1_2(Jj0501a1 jj0501a1, List<Map<String, Object>> listylj, int l, int p) {
        Map<String, Object> map = listylj.get(p);
        switch (l) {
            case 0:
                switch (p) {
                    case 0:
                        jj0501a1.setJxhz11((String) map.get("KYLZ"));
                        jj0501a1.setKyqd11((String) map.get("KYQD"));
                    case 1:
                        jj0501a1.setJxhz12((String) map.get("KYLZ"));
                        jj0501a1.setKyqd12((String) map.get("KYQD"));
                    case 2:
                        jj0501a1.setJxhz13((String) map.get("KYLZ"));
                        jj0501a1.setKyqd13((String) map.get("KYQD"));
                    default:
                        break;
                }
                break;
            case 1:
                switch (p) {
                    case 0:
                        jj0501a1.setJxhz21((String) map.get("KYLZ"));
                        jj0501a1.setKyqd21((String) map.get("KYQD"));
                    case 1:
                        jj0501a1.setJxhz22((String) map.get("KYLZ"));
                        jj0501a1.setKyqd22((String) map.get("KYQD"));
                    case 2:
                        jj0501a1.setJxhz23((String) map.get("KYLZ"));
                        jj0501a1.setKyqd23((String) map.get("KYQD"));
                    default:
                        break;
                }
                break;
            case 2:
                switch (p) {
                    case 0:
                        jj0501a1.setJxhz31((String) map.get("KYLZ"));
                        jj0501a1.setKyqd31((String) map.get("KYQD"));
                    case 1:
                        jj0501a1.setJxhz32((String) map.get("KYLZ"));
                        jj0501a1.setKyqd32((String) map.get("KYQD"));
                    case 2:
                        jj0501a1.setJxhz33((String) map.get("KYLZ"));
                        jj0501a1.setKyqd33((String) map.get("KYQD"));
                    default:
                        break;
                }
                break;
            case 3:
                switch (p) {
                    case 0:
                        jj0501a1.setJxhz41((String) map.get("KYLZ"));
                        jj0501a1.setKyqd41((String) map.get("KYQD"));
                    case 1:
                        jj0501a1.setJxhz42((String) map.get("KYLZ"));
                        jj0501a1.setKyqd42((String) map.get("KYQD"));
                    case 2:
                        jj0501a1.setJxhz43((String) map.get("KYLZ"));
                        jj0501a1.setKyqd43((String) map.get("KYQD"));
                    default:
                        break;
                }
                break;
            case 4:
                switch (p) {
                    case 0:
                        jj0501a1.setJxhz51((String) map.get("KYLZ"));
                        jj0501a1.setKyqd51((String) map.get("KYQD"));
                    case 1:
                        jj0501a1.setJxhz52((String) map.get("KYLZ"));
                        jj0501a1.setKyqd52((String) map.get("KYQD"));
                    case 2:
                        jj0501a1.setJxhz53((String) map.get("KYLZ"));
                        jj0501a1.setKyqd53((String) map.get("KYQD"));
                    default:
                        break;
                }
                break;
            default:
                break;
        }
        return jj0501a1;
    }

    private Jj0501c jj0501c(Jj0501c jj0501c, List<TSyjzb> listAll) {
        for (int l = 0; l < listAll.size(); l++) {
            TSyjzb entity = listAll.get(l);
            jj0501c = jj0501c_1(jj0501c, entity, l);
        }
        return jj0501c;
    }

    private Jj0501c jj0501c_1(Jj0501c jj0501c, TSyjzb entity, int l) {
        String syjid = entity.getSyjid();
        switch (l) {
            case 0:
                jj0501c.setSjbh1(entity.getSjbh());
                // jj0501c.setJiegbw(entity.getCjmc());
                jj0501c.setCxrq1(entity.getZzrq());
                jj0501c.setQddj1(entity.getSjqd());
                jj0501c.setLingqi1(String.valueOf(entity.getLq()));
                jj0501c.setSjcc1(entity.getSjcc());
                jj0501c.setSyrq1(entity.getSyrq());
                jj0501c.setKyqdcdz1(entity.getQddbz());
                // jj0501c.setQdbfb1();
                List<Map<String, Object>> listylj = this.baseMapper.selectYaLiJi(syjid);
                for (int p = 0; p < listylj.size(); p++) {
                    jj0501c = jj0501c_2(jj0501c, listylj, l, p);
                }
                break;
            case 1:
                jj0501c.setSjbh2(entity.getSjbh());
                // jj0501c.setJiegbw(entity.getCjmc());
                jj0501c.setCxrq2(entity.getZzrq());
                jj0501c.setQddj2(entity.getSjqd());
                jj0501c.setLingqi2(String.valueOf(entity.getLq()));
                jj0501c.setSjcc2(entity.getSjcc());
                jj0501c.setSyrq2(entity.getSyrq());
                jj0501c.setKyqdcdz2(entity.getQddbz());
                // jj0501c.setQdbfb2();
                List<Map<String, Object>> listylj2 = this.baseMapper.selectYaLiJi(syjid);
                for (int p = 0; p < listylj2.size(); p++) {
                    jj0501c = jj0501c_2(jj0501c, listylj2, l, p);
                }
                break;
            case 2:
                jj0501c.setSjbh2(entity.getSjbh());
                // jj0501c.setJiegbw(entity.getCjmc());
                jj0501c.setCxrq2(entity.getZzrq());
                jj0501c.setQddj2(entity.getSjqd());
                jj0501c.setLingqi2(String.valueOf(entity.getLq()));
                jj0501c.setSjcc3(entity.getSjcc());
                jj0501c.setSyrq2(entity.getSyrq());
                jj0501c.setKyqdcdz2(entity.getQddbz());
                // jj0501c.setQdbfb1();
                List<Map<String, Object>> listylj3 = this.baseMapper.selectYaLiJi(syjid);
                for (int p = 0; p < listylj3.size(); p++) {
                    jj0501c = jj0501c_2(jj0501c, listylj3, l, p);
                }
                break;
            default:
                break;
        }
        return jj0501c;
    }

    private Jj0501c jj0501c_2(Jj0501c jj0501c, List<Map<String, Object>> listylj, int l, int p) {
        Map<String, Object> map = listylj.get(p);
        switch (l) {
            case 0:
                switch (p) {
                    case 0:
                        jj0501c.setJxhz11((String) map.get("KYLZ"));
                        jj0501c.setKyqd11((String) map.get("KYQD"));
                    case 1:
                        jj0501c.setJxhz12((String) map.get("KYLZ"));
                        jj0501c.setKyqd12((String) map.get("KYQD"));
                    case 2:
                        jj0501c.setJxhz13((String) map.get("KYLZ"));
                        jj0501c.setKyqd13((String) map.get("KYQD"));
                    case 3:
                        jj0501c.setJxhz14((String) map.get("KYLZ"));
                        jj0501c.setKyqd14((String) map.get("KYQD"));
                    case 4:
                        jj0501c.setJxhz15((String) map.get("KYLZ"));
                        jj0501c.setKyqd15((String) map.get("KYQD"));
                    case 5:
                        jj0501c.setJxhz16((String) map.get("KYLZ"));
                        jj0501c.setKyqd16((String) map.get("KYQD"));
                    default:
                        break;
                }
                break;
            case 1:
                switch (p) {
                    case 0:
                        jj0501c.setJxhz21((String) map.get("KYLZ"));
                        jj0501c.setKyqd21((String) map.get("KYQD"));
                    case 1:
                        jj0501c.setJxhz22((String) map.get("KYLZ"));
                        jj0501c.setKyqd22((String) map.get("KYQD"));
                    case 2:
                        jj0501c.setJxhz23((String) map.get("KYLZ"));
                        jj0501c.setKyqd23((String) map.get("KYQD"));
                    case 3:
                        jj0501c.setJxhz24((String) map.get("KYLZ"));
                        jj0501c.setKyqd24((String) map.get("KYQD"));
                    case 4:
                        jj0501c.setJxhz25((String) map.get("KYLZ"));
                        jj0501c.setKyqd25((String) map.get("KYQD"));
                    case 5:
                        jj0501c.setJxhz26((String) map.get("KYLZ"));
                        jj0501c.setKyqd26((String) map.get("KYQD"));
                    default:
                        break;
                }
                break;
            case 2:
                switch (p) {
                    case 0:
                        jj0501c.setJxhz31((String) map.get("KYLZ"));
                        jj0501c.setKyqd31((String) map.get("KYQD"));
                    case 1:
                        jj0501c.setJxhz32((String) map.get("KYLZ"));
                        jj0501c.setKyqd32((String) map.get("KYQD"));
                    case 2:
                        jj0501c.setJxhz33((String) map.get("KYLZ"));
                        jj0501c.setKyqd33((String) map.get("KYQD"));
                    case 3:
                        jj0501c.setJxhz34((String) map.get("KYLZ"));
                        jj0501c.setKyqd34((String) map.get("KYQD"));
                    case 4:
                        jj0501c.setJxhz35((String) map.get("KYLZ"));
                        jj0501c.setKyqd35((String) map.get("KYQD"));
                    case 5:
                        jj0501c.setJxhz36((String) map.get("KYLZ"));
                        jj0501c.setKyqd36((String) map.get("KYQD"));
                    default:
                        break;
                }
                break;
            default:
                break;
        }
        return jj0501c;
    }


    private Jj0501d jj0501d(Jj0501d jj0501d, List<TSyjzb> listAll) {
        for (int l = 0; l < listAll.size(); l++) {
            TSyjzb entity = listAll.get(l);
            jj0501d = jj0501d_1(jj0501d, entity, l);
        }
        return jj0501d;
    }

    private Jj0501d jj0501d_1(Jj0501d jj0501d, TSyjzb entity, int l) {
        String syjid = entity.getSyjid();
        switch (l) {
            case 0:
                jj0501d.setSjbh1(entity.getSjbh());
                // jj0501d.setJiegbw(entity.getCjmc());
                jj0501d.setCxrq1(entity.getZzrq());
                jj0501d.setQddj1(entity.getSjqd());
                jj0501d.setLingqi1(String.valueOf(entity.getLq()));
                jj0501d.setSyrq1(entity.getSyrq());
                jj0501d.setKyqdcdz1(entity.getQddbz());
                List<Map<String, Object>> listylj = this.baseMapper.selectYaLiJi(syjid);
                for (int p = 0; p < listylj.size(); p++) {
                    jj0501d = jj0501d_2(jj0501d, listylj, l, p);
                }
                break;
            case 1:
                jj0501d.setSjbh2(entity.getSjbh());
                // jj0501d.setJiegbw(entity.getCjmc());
                jj0501d.setCxrq2(entity.getZzrq());
                jj0501d.setQddj2(entity.getSjqd());
                jj0501d.setLingqi2(String.valueOf(entity.getLq()));
                jj0501d.setSyrq2(entity.getSyrq());
                jj0501d.setKyqdcdz2(entity.getQddbz());
                List<Map<String, Object>> listylj2 = this.baseMapper.selectYaLiJi(syjid);
                for (int p = 0; p < listylj2.size(); p++) {
                    jj0501d = jj0501d_2(jj0501d, listylj2, l, p);
                }
                break;
            case 2:
                jj0501d.setSjbh3(entity.getSjbh());
                // jj0501d.setJiegbw(entity.getCjmc());
                jj0501d.setCxrq3(entity.getZzrq());
                jj0501d.setQddj3(entity.getSjqd());
                jj0501d.setLingqi3(String.valueOf(entity.getLq()));
                jj0501d.setSyrq3(entity.getSyrq());
                jj0501d.setKyqdcdz3(entity.getQddbz());
                List<Map<String, Object>> listylj3 = this.baseMapper.selectYaLiJi(syjid);
                for (int p = 0; p < listylj3.size(); p++) {
                    jj0501d = jj0501d_2(jj0501d, listylj3, l, p);
                }
                break;
            case 3:
                jj0501d.setSjbh4(entity.getSjbh());
                // jj0501d.setJiegbw(entity.getCjmc());
                jj0501d.setCxrq4(entity.getZzrq());
                jj0501d.setQddj4(entity.getSjqd());
                jj0501d.setLingqi4(String.valueOf(entity.getLq()));
                jj0501d.setSyrq4(entity.getSyrq());
                jj0501d.setKyqdcdz4(entity.getQddbz());
                List<Map<String, Object>> listylj4 = this.baseMapper.selectYaLiJi(syjid);
                for (int p = 0; p < listylj4.size(); p++) {
                    jj0501d = jj0501d_2(jj0501d, listylj4, l, p);
                }
                break;
            default:
                break;
        }
        return jj0501d;
    }

    private Jj0501d jj0501d_2(Jj0501d jj0501d, List<Map<String, Object>> listylj, int l, int p) {
        Map<String, Object> map = listylj.get(p);
        switch (l) {
            case 0:
                switch (p) {
                    case 0:
                        jj0501d.setJxhz11((String) map.get("KYLZ"));
                        jj0501d.setKyqd11((String) map.get("KYQD"));
                    case 1:
                        jj0501d.setJxhz12((String) map.get("KYLZ"));
                        jj0501d.setKyqd12((String) map.get("KYQD"));
                    case 2:
                        jj0501d.setJxhz13((String) map.get("KYLZ"));
                        jj0501d.setKyqd13((String) map.get("KYQD"));
                    default:
                        break;
                }
                break;
            case 1:
                switch (p) {
                    case 0:
                        jj0501d.setJxhz21((String) map.get("KYLZ"));
                        jj0501d.setKyqd21((String) map.get("KYQD"));
                    case 1:
                        jj0501d.setJxhz22((String) map.get("KYLZ"));
                        jj0501d.setKyqd22((String) map.get("KYQD"));
                    case 2:
                        jj0501d.setJxhz23((String) map.get("KYLZ"));
                        jj0501d.setKyqd23((String) map.get("KYQD"));
                    default:
                        break;
                }
                break;
            case 2:
                switch (p) {
                    case 0:
                        jj0501d.setJxhz31((String) map.get("KYLZ"));
                        jj0501d.setKyqd31((String) map.get("KYQD"));
                    case 1:
                        jj0501d.setJxhz32((String) map.get("KYLZ"));
                        jj0501d.setKyqd32((String) map.get("KYQD"));
                    case 2:
                        jj0501d.setJxhz33((String) map.get("KYLZ"));
                        jj0501d.setKyqd33((String) map.get("KYQD"));
                    default:
                        break;
                }
                break;
            case 3:
                switch (p) {
                    case 0:
                        jj0501d.setJxhz41((String) map.get("KYLZ"));
                        jj0501d.setKyqd41((String) map.get("KYQD"));
                    case 1:
                        jj0501d.setJxhz42((String) map.get("KYLZ"));
                        jj0501d.setKyqd42((String) map.get("KYQD"));
                    case 2:
                        jj0501d.setJxhz43((String) map.get("KYLZ"));
                        jj0501d.setKyqd43((String) map.get("KYQD"));
                    default:
                        break;
                }
                break;
            default:
                break;
        }
        return jj0501d;
    }

    private Jj1001bGj jj0501dGJ(Jj1001bGj jj1001bGJ, List<TSyjzb> listAll) {

        TSyjzb entity = listAll.get(0);
        jj1001bGJ = jj0501dGJ_1(jj1001bGJ, entity, 0);

        return jj1001bGJ;
    }

    private Jj1001bGj jj0501dGJ_1(Jj1001bGj jj0501dGJ, TSyjzb entity, int l) {
        String syjid = entity.getSyjid();
        switch (l) {
            case 0:
                jj0501dGJ.setZhij11(entity.getGczj());
                jj0501dGJ.setZhij21(entity.getGczj());
                //jj0501dGJ.setZhij31(entity.getGczj());
                jj0501dGJ.setGangjjiemj11(entity.getSjmj());
                jj0501dGJ.setGangjjiemj21(entity.getSjmj());
                //jj0501dGJ.setGangjjiemj31(entity.getSjmj());
                List<Map<String, Object>> listylj = this.baseMapper.selectWnj(syjid);
                for (int p = 0; p < listylj.size(); p++) {
                    jj0501dGJ = jj0501dGJ_2(jj0501dGJ, listylj, l, p);
                }
                break;
            default:
                break;
        }
        return jj0501dGJ;
    }

    private Jj1001bGj jj0501dGJ_2(Jj1001bGj jj0501dGJ, List<Map<String, Object>> listylj, int l, int p) {
        Map<String, Object> map = listylj.get(p);

        switch (p) {
            case 0:
                jj0501dGJ.setShijbianh11((String) map.get("SJXH"));
                jj0501dGJ.setJixianhez11((String) map.get("LZ"));
                jj0501dGJ.setJixianqdu11((String) map.get("LZQD"));
                break;
            case 1:
                jj0501dGJ.setShijbianh12((String) map.get("SJXH"));
                jj0501dGJ.setJixianhez12((String) map.get("LZ"));
                jj0501dGJ.setJixianqdu12((String) map.get("LZQD"));
                break;
            case 2:
                jj0501dGJ.setShijbianh13((String) map.get("SJXH"));
                jj0501dGJ.setJixianhez13((String) map.get("LZ"));
                jj0501dGJ.setJixianqdu13((String) map.get("LZQD"));
                break;
            case 3:
                jj0501dGJ.setShijbianh21((String) map.get("SJXH"));
                jj0501dGJ.setJixianhez21((String) map.get("LZ"));
                jj0501dGJ.setJixianqdu21((String) map.get("LZQD"));
                break;
            case 4:
                jj0501dGJ.setShijbianh22((String) map.get("SJXH"));
                jj0501dGJ.setJixianhez22((String) map.get("LZ"));
                jj0501dGJ.setJixianqdu22((String) map.get("LZQD"));
                break;
            case 5:
                jj0501dGJ.setShijbianh23((String) map.get("SJXH"));
                jj0501dGJ.setJixianhez23((String) map.get("LZ"));
                jj0501dGJ.setJixianqdu23((String) map.get("LZQD"));
                break;
            case 6:
                jj0501dGJ.setShijbianh31((String) map.get("SJXH"));
                jj0501dGJ.setJixianhez31((String) map.get("LZ"));
                jj0501dGJ.setJixianqdu31((String) map.get("LZQD"));
                break;
            case 7:
                jj0501dGJ.setShijbianh32((String) map.get("SJXH"));
                jj0501dGJ.setJixianhez32((String) map.get("LZ"));
                jj0501dGJ.setJixianqdu32((String) map.get("LZQD"));
                break;
            case 8:
                jj0501dGJ.setShijbianh33((String) map.get("SJXH"));
                jj0501dGJ.setJixianhez33((String) map.get("LZ"));
                jj0501dGJ.setJixianqdu33((String) map.get("LZQD"));
                break;
            default:
                break;
        }
        return jj0501dGJ;
    }

    private Jj1412 jj1412(Jj1412 jj1412, List<TSyjzb> listAll) {

        TSyjzb entity = listAll.get(0);
        jj1412 = jj1412_1(jj1412, entity, 0);

        return jj1412;
    }

    private Jj1412 jj1412_1(Jj1412 jj1412, TSyjzb entity, int l) {
        String syjid = entity.getSyjid();
        switch (l) {
            case 0:
                jj1412.setMuczhij1(entity.getGczj());
                jj1412.setGangjjiemj1(entity.getGczj());
                List<Map<String, Object>> listylj = this.baseMapper.selectWnj(syjid);
                if (listylj.size() > 3) {
                    jj1412.setMuczhij2(entity.getGczj());
                    jj1412.setGangjjiemj2(entity.getGczj());
                }
                if (listylj.size() > 6) {
                    jj1412.setMuczhij3(entity.getGczj());
                    jj1412.setGangjjiemj3(entity.getGczj());
                }
                for (int p = 0; p < listylj.size(); p++) {
                    jj1412 = jj1412_2(jj1412, listylj, l, p);
                }
                break;
            default:
                break;
        }
        return jj1412;
    }

    private Jj1412 jj1412_2(Jj1412 jj1412, List<Map<String, Object>> listylj, int l, int p) {
        Map<String, Object> map = listylj.get(p);

        switch (p) {
            case 0:
                jj1412.setShijbhao1((String) map.get("SJXH"));
                jj1412.setJixianhez1((String) map.get("LZ"));
                jj1412.setJixqdu1((String) map.get("LZQD"));
                jj1412.setDuankxshi1((String) map.get("LDCMS").toString());
                jj1412.setDuankljiekjul1((String) map.get("DKWZ").toString());
                break;
            case 1:
                jj1412.setShijbhao2((String) map.get("SJXH"));
                jj1412.setJixianhez2((String) map.get("LZ"));
                jj1412.setJixqdu2((String) map.get("LZQD"));
                jj1412.setDuankxshi2((String) map.get("LDCMS").toString());
                jj1412.setDuankljiekjul2((String) map.get("DKWZ").toString());
                break;
            case 2:
                jj1412.setShijbhao3((String) map.get("SJXH"));
                jj1412.setJixianhez3((String) map.get("LZ"));
                jj1412.setJixqdu3((String) map.get("LZQD"));
                jj1412.setDuankxshi3((String) map.get("LDCMS").toString());
                jj1412.setDuankljiekjul3((String) map.get("DKWZ").toString());
                break;
            case 3:
                jj1412.setShijbhao4((String) map.get("SJXH"));
                jj1412.setJixianhez4((String) map.get("LZ"));
                jj1412.setJixqdu4((String) map.get("LZQD"));
                jj1412.setDuankxshi4((String) map.get("LDCMS").toString());
                jj1412.setDuankljiekjul4((String) map.get("DKWZ").toString());
                break;
            case 4:
                jj1412.setShijbhao5((String) map.get("SJXH"));
                jj1412.setJixianhez5((String) map.get("LZ"));
                jj1412.setJixqdu5((String) map.get("LZQD"));
                jj1412.setDuankxshi5((String) map.get("LDCMS").toString());
                jj1412.setDuankljiekjul5((String) map.get("DKWZ").toString());
                break;
            case 5:
                jj1412.setShijbhao6((String) map.get("SJXH"));
                jj1412.setJixianhez6((String) map.get("LZ"));
                jj1412.setJixqdu6((String) map.get("LZQD"));
                jj1412.setDuankxshi6((String) map.get("LDCMS").toString());
                jj1412.setDuankljiekjul6((String) map.get("DKWZ").toString());
                break;
            case 6:
                jj1412.setShijbhao7((String) map.get("SJXH"));
                jj1412.setJixianhez7((String) map.get("LZ"));
                jj1412.setJixqdu7((String) map.get("LZQD"));
                jj1412.setDuankxshi7((String) map.get("LDCMS").toString());
                jj1412.setDuankljiekjul7((String) map.get("DKWZ").toString());
                break;
            case 7:
                jj1412.setShijbhao8((String) map.get("SJXH"));
                jj1412.setJixianhez8((String) map.get("LZ"));
                jj1412.setJixqdu8((String) map.get("LZQD"));
                jj1412.setDuankxshi8((String) map.get("LDCMS").toString());
                jj1412.setDuankljiekjul8((String) map.get("DKWZ").toString());
                break;
            case 8:
                jj1412.setShijbhao9((String) map.get("SJXH"));
                jj1412.setJixianhez9((String) map.get("LZ"));
                jj1412.setJixqdu9((String) map.get("LZQD"));
                jj1412.setDuankxshi9((String) map.get("LDCMS").toString());
                jj1412.setDuankljiekjul9((String) map.get("DKWZ").toString());
                break;
            default:
                break;
        }
        return jj1412;
    }

    private Jj1001a jj1001a(Jj1001a jj1001a, String wtid, String gangjpaih, String o) {
        List<TSyjzb> listsyj = this.baseMapper.getSyJzbByGjPh(wtid, gangjpaih);
        if (listsyj.size() > 0) {
            TSyjzb entity = listsyj.get(0);
            String syjid = entity.getSyjid();
            jj1001a = jj1001a_1(jj1001a, syjid, o);
        }
        return jj1001a;
    }

    private Jj1001a jj1001a_1(Jj1001a jj1001a, String syjid, String o) {
        List<Map<String, Object>> listwnj = this.baseMapper.selectWnj(syjid);
        if (o.equalsIgnoreCase("0")) {
            for (int l = 0; l < listwnj.size(); l++) {
                Map<String, Object> map = listwnj.get(l);
                switch (l) {
                    case 0:
//					jj1001a.setShijianbh1((String) map.get("WSBZ"));
                        jj1001a.setQfhz1((String) map.get("QFLZ"));
                        jj1001a.setJxhz1((String) map.get("LZ"));
                        jj1001a.setQfqd1((String) map.get("QFQD"));
                        jj1001a.setKlqd1((String) map.get("LZQD"));

                        if (oConvertUtils.isNotEmpty(map.get("SCL"))) {
                            jj1001a.setSclYsbj1((String) map.get("WSBZ"));
                            jj1001a.setDhscl1((String) map.get("SCL"));
                            jj1001a.setDhbj1((String) map.get("DHBZ"));
                        } else if (oConvertUtils.isNotEmpty(map.get("ZDLZSCL"))) {
                            jj1001a.setYslYsbj1((String) map.get("WSBZ"));
                            jj1001a.setZdlxys1((String) map.get("DHBZ"));
                            jj1001a.setZdlzysl1((String) map.get("ZDLZSCL"));
                        }
                        break;
                    case 1:
//					jj1001a.setShijianbh2((String) map.get("WSBZ"));
                        jj1001a.setQfhz2((String) map.get("QFLZ"));
                        jj1001a.setJxhz2((String) map.get("LZ"));
                        jj1001a.setQfqd2((String) map.get("QFQD"));
                        jj1001a.setKlqd2((String) map.get("LZQD"));
                        if (oConvertUtils.isNotEmpty(map.get("SCL"))) {
                            jj1001a.setSclYsbj2((String) map.get("WSBZ"));
                            jj1001a.setDhscl2((String) map.get("SCL"));
                            jj1001a.setDhbj2((String) map.get("DHBZ"));
                        } else if (oConvertUtils.isNotEmpty(map.get("ZDLZSCL"))) {
                            jj1001a.setYslYsbj2((String) map.get("WSBZ"));
                            jj1001a.setZdlxys2((String) map.get("DHBZ"));
                            jj1001a.setZdlzysl2((String) map.get("ZDLZSCL"));
                        }
                        break;
                    case 2:
//					jj1001a.setShijianbh3((String) map.get("WSBZ"));
                        jj1001a.setQfhz3((String) map.get("QFLZ"));
                        jj1001a.setJxhz3((String) map.get("LZ"));
                        jj1001a.setQfqd3((String) map.get("QFQD"));
                        jj1001a.setKlqd3((String) map.get("LZQD"));
                        if (oConvertUtils.isNotEmpty(map.get("SCL"))) {
                            jj1001a.setSclYsbj3((String) map.get("WSBZ"));
                            jj1001a.setDhscl3((String) map.get("SCL"));
                            jj1001a.setDhbj3((String) map.get("DHBZ"));
                        } else if (oConvertUtils.isNotEmpty(map.get("ZDLZSCL"))) {
                            jj1001a.setYslYsbj3((String) map.get("WSBZ"));
                            jj1001a.setZdlxys3((String) map.get("DHBZ"));
                            jj1001a.setZdlzysl3((String) map.get("ZDLZSCL"));
                        }
                        break;
                    default:
                        break;
                }
            }
        } else {
            for (int l = 0; l < listwnj.size(); l++) {
                Map<String, Object> map = listwnj.get(l);
                switch (l) {
                    case 0:
//					jj1001a.setShijianbh3((String) map.get("WSBZ"));
//					jj1001a.setQfhz3((String) map.get("QFLZ"));
//					jj1001a.setJxhz3((String) map.get("LZ"));
//					jj1001a.setQfqd3((String) map.get("QFQD"));
//					jj1001a.setKlqd3((String) map.get("LZQD"));
//					jj1001a.setScl_ysbj3((String) map.get("WSBZ"));
//					jj1001a.setDhbj3((String) map.get("DHBZ"));
//					jj1001a.setDhscl3((String) map.get("SCL"));
//					// jj1001a.setZuidlizyansl1((String) map.get("ZDLZSCL"));
//					jj1001a.setYsl_ysbj3((String) map.get("WSBZ"));
//					jj1001a.setZdlxys3((String) map.get("DHBZ"));
//					jj1001a.setZdlzysl3((String) map.get(""));
                        // jj1001a.setDuankxs1((String) map.get("LDCMS"));
                        break;
                    default:
                        break;
                }
            }
        }
        return jj1001a;
    }

    private Jj1001b jj1001b(Jj1001b jj1001b, String wtid, String gangjpaih, String o) {
        List<TSyjzb> listsyj = this.baseMapper.getSyJzbByGjPh(wtid, gangjpaih);
        if (listsyj.size() > 0) {
            TSyjzb entity = listsyj.get(0);
            // String syjid = entity.getSyjid();
            // if (o.equalsIgnoreCase("0")) {
            // jj1001b.setZhij11(entity.getGczj());
            // jj1001b.setZhij12(entity.getGczj());
            // jj1001b.setZhij13(entity.getGczj());
            // jj1001b.setGangjjiemj11(entity.getSjmj());
            // jj1001b.setGangjjiemj12(entity.getSjmj());
            // jj1001b.setGangjjiemj13(entity.getSjmj());
            // } else {
            // jj1001b.setZhij21(entity.getGczj());
            // jj1001b.setZhij22(entity.getGczj());
            // jj1001b.setZhij23(entity.getGczj());
            // jj1001b.setGangjjiemj21(entity.getSjmj());
            // jj1001b.setGangjjiemj22(entity.getSjmj());
            // jj1001b.setGangjjiemj23(entity.getSjmj());
            // }
            jj1001b = jj1001b_1(jj1001b, entity, o);
        }
        return jj1001b;
    }

    private Jj1001b jj1001b_1(Jj1001b jj1001b, TSyjzb entity, String o) {
        List<Map<String, Object>> listwnj = this.baseMapper.selectWnj(entity.getSyjid());
        if (o.equalsIgnoreCase("0")) {
            for (int l = 0; l < listwnj.size(); l++) {
                Map<String, Object> map = listwnj.get(l);
                switch (l) {
                    case 0:
                        jj1001b.setShijbh1((String) map.get("SJXH"));
                        jj1001b.setHjmj1(entity.getSjmj());
                        jj1001b.setJxhz1((String) map.get("LZ"));
                        jj1001b.setKlqd1((String) map.get("LZQD"));
                        jj1001b.setDlwz1((String) map.get("DKWZ"));
                        jj1001b.setDlxs1((String) map.get("LDCMS"));
                        break;
                    case 1:
                        jj1001b.setShijbh2((String) map.get("SJXH"));
                        jj1001b.setHjmj2(entity.getSjmj());
                        jj1001b.setJxhz2((String) map.get("LZ"));
                        jj1001b.setKlqd2((String) map.get("LZQD"));
                        jj1001b.setDlwz2((String) map.get("DKWZ"));
                        jj1001b.setDlxs2((String) map.get("LDCMS"));
                        break;
                    case 2:
                        jj1001b.setShijbh3((String) map.get("SJXH"));
                        jj1001b.setHjmj3(entity.getSjmj());
                        jj1001b.setJxhz3((String) map.get("LZ"));
                        jj1001b.setKlqd3((String) map.get("LZQD"));
                        jj1001b.setDlwz3((String) map.get("DKWZ"));
                        jj1001b.setDlxs3((String) map.get("LDCMS"));
                        break;
                    default:
                        break;
                }
            }
        } else {
            for (int l = 0; l < listwnj.size(); l++) {
                Map<String, Object> map = listwnj.get(l);
                switch (l) {
                    case 0:
                        jj1001b.setShijbh1((String) map.get("SJXH"));
                        jj1001b.setHjmj1(entity.getSjmj());
                        jj1001b.setJxhz1((String) map.get("LZ"));
                        jj1001b.setKlqd1((String) map.get("LZQD"));
                        jj1001b.setDlwz1((String) map.get("DKWZ"));
                        jj1001b.setDlxs1((String) map.get("LDCMS"));
                        break;
                    case 1:
                        jj1001b.setShijbh2((String) map.get("SJXH"));
                        jj1001b.setHjmj2(entity.getSjmj());
                        jj1001b.setJxhz2((String) map.get("LZ"));
                        jj1001b.setKlqd2((String) map.get("LZQD"));
                        jj1001b.setDlwz2((String) map.get("DKWZ"));
                        jj1001b.setDlxs2((String) map.get("LDCMS"));
                        break;
                    case 2:
                        jj1001b.setShijbh3((String) map.get("SJXH"));
                        jj1001b.setHjmj3(entity.getSjmj());
                        jj1001b.setJxhz3((String) map.get("LZ"));
                        jj1001b.setKlqd3((String) map.get("LZQD"));
                        jj1001b.setDlwz3((String) map.get("DKWZ"));
                        jj1001b.setDlxs3((String) map.get("LDCMS"));
                        break;
                    default:
                        break;
                }
            }
        }
        return jj1001b;
    }

    private Jglq15002 jglq15002(Jglq15002 jglq15002, String wtid, String gangjpaih, String o) {
        List<TSyjzb> listsyj = this.baseMapper.getSyJzbByGjPh(wtid, gangjpaih);
        if (listsyj.size() > 0) {
            TSyjzb entity = listsyj.get(0);
            String syjid = entity.getSyjid();
            jglq15002 = jglq15002_1(jglq15002, syjid, o);
        }
        return jglq15002;
    }

    private Jglq15002 jglq15002_1(Jglq15002 jglq15002, String syjid, String o) {
        List<Map<String, Object>> listwnj = this.baseMapper.selectWnj(syjid);

        for (int l = 0; l < listwnj.size(); l++) {
            Map<String, Object> map = listwnj.get(l);
            switch (l) {
                case 0:
                    jglq15002.setSybh1((String) map.get("WSBZ"));
                    jglq15002.setQfl1((String) map.get("QFLZ"));
                    jglq15002.setZdl1((String) map.get("LZ"));
                    jglq15002.setQfqd1((String) map.get("QFQD"));
                    jglq15002.setJxklqd1((String) map.get("LZQD"));
                    jglq15002.setYsbj1((String) map.get("WSBZ"));
                    jglq15002.setDhscl1((String) map.get("SCL"));
                    jglq15002.setDhbj1((String) map.get("DHBZ"));


                    break;
                case 1:
                    jglq15002.setSybh2((String) map.get("WSBZ"));
                    jglq15002.setQfl2((String) map.get("QFLZ"));
                    jglq15002.setZdl2((String) map.get("LZ"));
                    jglq15002.setQfqd2((String) map.get("QFQD"));
                    jglq15002.setJxklqd2((String) map.get("LZQD"));
                    jglq15002.setYsbj2((String) map.get("WSBZ"));
                    jglq15002.setDhscl2((String) map.get("SCL"));
                    jglq15002.setDhbj2((String) map.get("DHBZ"));
                    break;
                case 2:
                    jglq15002.setSybh3((String) map.get("WSBZ"));
                    jglq15002.setQfl3((String) map.get("QFLZ"));
                    jglq15002.setZdl3((String) map.get("LZ"));
                    jglq15002.setQfqd3((String) map.get("QFQD"));
                    jglq15002.setJxklqd3((String) map.get("LZQD"));
                    jglq15002.setYsbj3((String) map.get("WSBZ"));
                    jglq15002.setDhscl3((String) map.get("SCL"));
                    jglq15002.setDhbj3((String) map.get("DHBZ"));
                    break;
                default:
                    break;
            }
        }

        return jglq15002;
    }

    private Jj0520b jj0520b(Jj0520b jj0520b, List<TSyjzb> listAll) {

        if (listAll.size() > 6) {
            for (int l = 0; l < 6; l++) {
                TSyjzb entity = listAll.get(l);
                jj0520b = jj0520b_1(jj0520b, entity, l);
            }
        } else {
            for (int l = 0; l < listAll.size(); l++) {
                TSyjzb entity = listAll.get(l);
                jj0520b = jj0520b_1(jj0520b, entity, l);
            }
        }
        return jj0520b;
    }

    private Jj0520b jj0520b_1(Jj0520b jj0520b, TSyjzb entity, int l) {
        String syjid = entity.getSyjid();
        switch (l) {
            case 0:
                jj0520b.setShijbh1(entity.getSjbh());
                jj0520b.setChenxrq1(entity.getZzrq());
                jj0520b.setSamplelingqi1(entity.getLq().toString());
                jj0520b.setShiyrq1(entity.getSyrq());
                jj0520b.setShijcc11(entity.getSjcc());
                if (entity.getSylx().equals("100236")) {
                    jj0520b.setKangxiqdcdz1(entity.getQddbz());
                } else if (entity.getSylx().equals("100235")) {
                    jj0520b.setKangyaqdcdz1(entity.getQddbz());
                }
                List<Map<String, Object>> listylj = this.baseMapper.selectYaLiJi(syjid);
                if (listylj.size() > 3) {
                    for (int p = 0; p < 3; p++) {
                        jj0520b = jj0520b_2(jj0520b, listylj, l, p, entity.getSylx());
                    }
                } else {
                    for (int p = 0; p < listylj.size(); p++) {
                        jj0520b = jj0520b_2(jj0520b, listylj, l, p, entity.getSylx());
                    }
                }
                break;
            case 1:
                jj0520b.setShijbh2(entity.getSjbh());
                jj0520b.setChenxrq2(entity.getZzrq());
                jj0520b.setSamplelingqi2(entity.getLq().toString());
                jj0520b.setShiyrq2(entity.getSyrq());
                jj0520b.setShijcc21(entity.getSjcc());
                if (entity.getSylx().equals("100236")) {
                    jj0520b.setKangxiqdcdz2(entity.getQddbz());
                } else if (entity.getSylx().equals("100235")) {
                    jj0520b.setKangyaqdcdz2(entity.getQddbz());
                }
                List<Map<String, Object>> listylj2 = this.baseMapper.selectYaLiJi(syjid);
                if (listylj2.size() > 3) {
                    for (int p = 0; p < 3; p++) {
                        jj0520b = jj0520b_2(jj0520b, listylj2, l, p, entity.getSylx());
                    }
                } else {
                    for (int p = 0; p < listylj2.size(); p++) {
                        jj0520b = jj0520b_2(jj0520b, listylj2, l, p, entity.getSylx());
                    }
                }
                break;
            case 2:
                jj0520b.setShijbh3(entity.getSjbh());
                jj0520b.setChenxrq3(entity.getZzrq());
                jj0520b.setSamplelingqi3(entity.getLq().toString());
                jj0520b.setShiyrq3(entity.getSyrq());
                jj0520b.setShijcc31(entity.getSjcc());
                if (entity.getSylx().equals("100236")) {
                    jj0520b.setKangxiqdcdz3(entity.getQddbz());
                } else if (entity.getSylx().equals("100235")) {
                    jj0520b.setKangyaqdcdz3(entity.getQddbz());
                }
                List<Map<String, Object>> listylj3 = this.baseMapper.selectYaLiJi(syjid);
                for (int p = 0; p < listylj3.size(); p++) {
                    jj0520b = jj0520b_2(jj0520b, listylj3, l, p, entity.getSylx());
                }
                break;
            default:
                break;
        }
        return jj0520b;
    }

    private Jj0520b jj0520b_2(Jj0520b jj0520b, List<Map<String, Object>> listylj, int l, int p, String sylx) {
        Map<String, Object> map = listylj.get(p);
        switch (l) {
            case 0:
                switch (p) {
                    case 0:
                        if (sylx.equals("100236")) {
                            jj0520b.setKangxijxhz11((String) map.get("KYLZ"));
                            jj0520b.setKangxiqdcz11((String) map.get("KYQD"));
                        } else if (sylx.equals("100235")) {
                            jj0520b.setKangyajxhzone11((String) map.get("KYLZ"));
                            jj0520b.setKangyaqdczone11((String) map.get("KYQD"));
                        }
                    case 1:
                        if (sylx.equals("100236")) {
                            jj0520b.setKangxijxhz12((String) map.get("KYLZ"));
                            jj0520b.setKangxiqdcz12((String) map.get("KYQD"));
                        } else if (sylx.equals("100235")) {
                            jj0520b.setKangyajxhztwo11((String) map.get("KYLZ"));
                            jj0520b.setKangyaqdcztwo11((String) map.get("KYQD"));
                        }
                    case 2:
                        if (sylx.equals("100236")) {
                            jj0520b.setKangxijxhz13((String) map.get("KYLZ"));
                            jj0520b.setKangxiqdcz13((String) map.get("KYQD"));
                        } else if (sylx.equals("100235")) {
                            jj0520b.setKangyajxhzone12((String) map.get("KYLZ"));
                            jj0520b.setKangyaqdczone12((String) map.get("KYQD"));
                        }
                    case 3:
                        if (sylx.equals("100235")) {
                            jj0520b.setKangyajxhztwo12((String) map.get("KYLZ"));
                            jj0520b.setKangyaqdcztwo12((String) map.get("KYQD"));
                        }
                    case 4:
                        if (sylx.equals("100235")) {
                            jj0520b.setKangyajxhzone13((String) map.get("KYLZ"));
                            jj0520b.setKangyaqdczone13((String) map.get("KYQD"));
                        }
                    case 5:
                        if (sylx.equals("100235")) {
                            jj0520b.setKangyajxhztwo13((String) map.get("KYLZ"));
                            jj0520b.setKangyaqdcztwo13((String) map.get("KYQD"));
                        }
                    default:
                        break;
                }
                break;
            case 1:
                switch (p) {
                    case 0:
                        if (sylx.equals("100236")) {
                            jj0520b.setKangxijxhz21((String) map.get("KYLZ"));
                            jj0520b.setKangxiqdcz21((String) map.get("KYQD"));
                        } else if (sylx.equals("100235")) {
                            jj0520b.setKangyajxhzone21((String) map.get("KYLZ"));
                            jj0520b.setKangyaqdczone21((String) map.get("KYQD"));
                        }
                    case 1:
                        if (sylx.equals("100236")) {
                            jj0520b.setKangxijxhz22((String) map.get("KYLZ"));
                            jj0520b.setKangxiqdcz22((String) map.get("KYQD"));
                        } else if (sylx.equals("100235")) {
                            jj0520b.setKangyajxhztwo21((String) map.get("KYLZ"));
                            jj0520b.setKangyaqdcztwo21((String) map.get("KYQD"));
                        }
                    case 2:
                        if (sylx.equals("100236")) {
                            jj0520b.setKangxijxhz23((String) map.get("KYLZ"));
                            jj0520b.setKangxiqdcz23((String) map.get("KYQD"));
                        } else if (sylx.equals("100235")) {
                            jj0520b.setKangyajxhzone22((String) map.get("KYLZ"));
                            jj0520b.setKangyaqdczone22((String) map.get("KYQD"));
                        }
                    case 3:
                        if (sylx.equals("100235")) {
                            jj0520b.setKangyajxhztwo22((String) map.get("KYLZ"));
                            jj0520b.setKangyaqdcztwo22((String) map.get("KYQD"));
                        }
                    case 4:
                        if (sylx.equals("100235")) {
                            jj0520b.setKangyajxhzone23((String) map.get("KYLZ"));
                            jj0520b.setKangyaqdczone23((String) map.get("KYQD"));
                        }
                    case 5:
                        if (sylx.equals("100235")) {
                            jj0520b.setKangyajxhztwo23((String) map.get("KYLZ"));
                            jj0520b.setKangyaqdcztwo23((String) map.get("KYQD"));
                        }
                    default:
                        break;
                }
                break;
            case 2:
                switch (p) {
                    case 0:
                        if (sylx.equals("100236")) {
                            jj0520b.setKangxijxhz31((String) map.get("KYLZ"));
                            jj0520b.setKangxiqdcz31((String) map.get("KYQD"));
                        } else if (sylx.equals("100235")) {
                            jj0520b.setKangyajxhzone31((String) map.get("KYLZ"));
                            jj0520b.setKangyaqdczone31((String) map.get("KYQD"));
                        }
                    case 1:
                        if (sylx.equals("100236")) {
                            jj0520b.setKangxijxhz32((String) map.get("KYLZ"));
                            jj0520b.setKangxiqdcz32((String) map.get("KYQD"));
                        } else if (sylx.equals("100235")) {
                            jj0520b.setKangyajxhztwo31((String) map.get("KYLZ"));
                            jj0520b.setKangyaqdcztwo31((String) map.get("KYQD"));
                        }
                    case 2:
                        if (sylx.equals("100236")) {
                            jj0520b.setKangxijxhz33((String) map.get("KYLZ"));
                            jj0520b.setKangxiqdcz33((String) map.get("KYQD"));
                        } else if (sylx.equals("100235")) {
                            jj0520b.setKangyajxhzone32((String) map.get("KYLZ"));
                            jj0520b.setKangyaqdczone32((String) map.get("KYQD"));
                        }
                    case 3:
                        if (sylx.equals("100235")) {
                            jj0520b.setKangyajxhztwo32((String) map.get("KYLZ"));
                            jj0520b.setKangyaqdcztwo32((String) map.get("KYQD"));
                        }
                    case 4:
                        if (sylx.equals("100235")) {
                            jj0520b.setKangyajxhzone33((String) map.get("KYLZ"));
                            jj0520b.setKangyaqdczone33((String) map.get("KYQD"));
                        }
                    case 5:
                        if (sylx.equals("100235")) {
                            jj0520b.setKangyajxhztwo33((String) map.get("KYLZ"));
                            jj0520b.setKangyaqdcztwo33((String) map.get("KYQD"));
                        }
                    default:
                        break;
                }
                break;

            default:
                break;
        }
        return jj0520b;
    }

}
