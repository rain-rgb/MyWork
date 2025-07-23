package com.trtm.sy.sydpssysignature.controller;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.utils.PdfMerger;
import com.trtm.sy.sydpssysample.entity.SyDpsSySample;
import com.trtm.sy.sydpssysample.entity.SysDepart;
import com.trtm.sy.sydpssysample.mapper.SyDpsSySampleMapper;
import com.trtm.sy.sydpssysample.service.ISyDpsSySampleService;
import com.trtm.sy.sydpssysignature.entity.SyDpsSySignature;
import com.trtm.sy.sydpssysignature.service.ISyDpsSySignatureService;
import com.trtm.sy.sylxdps.entity.SyDpsSyReportM;
import com.trtm.sy.sylxdps.mapper.SyDpsSyReportSMapper;
import com.xkcoding.http.util.StringUtil;
import net.sf.json.JSONObject;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.vo.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

/**
 * @Description: sy_dps_sy_signature
 * @Author: jeecg-boot
 * @Date: 2023-09-13
 * @Version: V1.0
 */
@Api(tags = "sy_dps_sy_signature")
@RestController
@RequestMapping("/syDpsSySignature/syDpsSySignature")
@Slf4j
public class SyDpsSySignatureController extends JeecgController<SyDpsSySignature, ISyDpsSySignatureService> {
    @Autowired
    private ISyDpsSySignatureService syDpsSySignatureService;
    @Autowired
    private ISyDpsSySampleService sampleService;


    SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    String cmd = "D:\\wkhtmltopdf\\bin\\wkhtmltopdf.exe";

    /**
     * 记录表,报告表，报验单签章分页查询接口
     */
    @RequestMapping(value = "/signatureGrid", method = RequestMethod.GET)
    public Result<?> signatureGrid(HttpServletRequest request, HttpServletResponse response,
                                   @RequestParam(value = "pageNo", required = false) Integer pageNo,
                                   @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                   @RequestParam(value = "orgCode", required = false) String orgCode,
                                   @RequestParam(value = "titCode", required = false) String titCode,
                                   @RequestParam(value = "sampleNo", required = false) String sampleNo,
                                   @RequestParam(value = "sampleName", required = false) String sampleName,
                                   @RequestParam(value = "sampleGcbw", required = false) String sampleGcbw,
                                   @RequestParam(value = "date", required = false) String date,
                                   @RequestParam(value = "reportNo", required = false) String reportNo,
                                   @RequestParam(value = "liuchengleixing", required = false) Integer liuchengleixing,
                                   @RequestParam(value = "type", required = false) String type,
                                   @RequestParam(value = "state", required = false) String state,
                                   @RequestParam(value = "reportDate", required = false) String reportDate) {

        Page<Map> page = new Page<>(pageNo, pageSize);
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        if (StringUtil.isEmpty(orgCode)) {
            orgCode = loginUser.getOrgCode();
        }
        IPage result = sampleService.getGrid(page, orgCode, titCode, sampleNo, sampleName, sampleGcbw, date,
                reportNo, liuchengleixing, type, state, loginUser.getId(), reportDate, "");
        return Result.OK(result);
    }

    /**
     * 签章流程分页查询接口
     */
    @RequestMapping(value = "/grid", method = RequestMethod.GET)
    public Result<?> grid(HttpServletRequest request, HttpServletResponse response,
                          @RequestParam(value = "pageNo", required = false) Integer pageNo,
                          @RequestParam(value = "pageSize", required = false) Integer pageSize,
                          @RequestParam(value = "orgCode", required = false) String orgCode,
                          @RequestParam(value = "titCode", required = false) String titCode,
                          @RequestParam(value = "sampleNo", required = false) String sampleNo,
                          @RequestParam(value = "sampleName", required = false) String sampleName,
                          @RequestParam(value = "sampleGcbw", required = false) String sampleGcbw,
                          @RequestParam(value = "date", required = false) String date,
                          @RequestParam(value = "reportNo", required = false) String reportNo,
                          @RequestParam(value = "tiNo", required = false) String tiNo,
                          @RequestParam(value = "state", required = false) String state,
                          @RequestParam(value = "self", required = false) String self,
                          @RequestParam(value = "shenpizhuangtai", required = false) String shenpizhuangtai) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        Page<Map> page = new Page<>(pageNo, pageSize);
        if (StringUtil.isEmpty(orgCode)) {
            orgCode = loginUser.getOrgCode();
        }
        IPage result = sampleService.getGrid3(page, orgCode, titCode, sampleNo, sampleName, sampleGcbw, date,
                reportNo, tiNo, loginUser.getUsername(), state, self, shenpizhuangtai);
        return Result.OK(result);
    }

    @RequestMapping(value = "/agree", method = RequestMethod.POST)
    @ResponseBody
    public Result<?>  agree(HttpServletRequest request, HttpServletResponse response,
                      @RequestParam("file") MultipartFile file) {
       syDpsSySignatureService.agree(request,file,response);
        return Result.OK();
    }
//    /**
//     * 签章生成接口
//     */
//    @RequestMapping(value = "/createSign", method = RequestMethod.POST)
//    public Result<?> createSign(HttpServletRequest request, HttpServletResponse response,
//                                @RequestBody HashMap<String, Object> map){
//        Map<String, String> personList = (Map<String, String>) map.get("personList");
//        String name = map.get("name").toString();
//        Integer bgnum = (Integer) map.get("bgnum");
//        Integer jlnum = (Integer) map.get("jlnum");
//        String sampleno = map.get("sampleno").toString();
//        String pdfurl = map.get("pdfurl").toString();
//        syDpsSySignatureService.createSign(name,personList,pdfurl,bgnum,jlnum,sampleno);
//        return Result.OK();
//    }
    /**
     * 签章流程分页查询接口（新）
     */
    @RequestMapping(value = "/gridNew", method = RequestMethod.GET)
    public Result<?> gridNew(HttpServletRequest request, HttpServletResponse response,
                          @RequestParam(value = "pageNo", required = false) Integer pageNo,
                          @RequestParam(value = "pageSize", required = false) Integer pageSize,
                          @RequestParam(value = "orgCode", required = false) String orgCode,
                          @RequestParam(value = "titCode", required = false) String titCode,
                          @RequestParam(value = "sampleNo", required = false) String sampleNo,
                          @RequestParam(value = "sampleName", required = false) String sampleName,
                          @RequestParam(value = "sampleGcbw", required = false) String sampleGcbw,
                          @RequestParam(value = "date", required = false) String date,
                          @RequestParam(value = "reportNo", required = false) String reportNo,
                          @RequestParam(value = "tiNo", required = false) String tiNo,
                          @RequestParam(value = "state", required = false) String state,
                          @RequestParam(value = "self", required = false) String self,
                          @RequestParam(value = "shenpizhuangtai", required = false) String shenpizhuangtai) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        Page<Map> page = new Page<>(pageNo, pageSize);
        if (StringUtil.isEmpty(orgCode)) {
            orgCode = loginUser.getOrgCode();
        }
        IPage result = syDpsSySignatureService.gridNew(page, orgCode, titCode, sampleNo, sampleName, sampleGcbw, date,
                reportNo, tiNo, loginUser.getUsername(), state, self, shenpizhuangtai);
        return Result.OK(result);
    }
    /**
     * 签署接口
     */
    @RequestMapping(value = "/sign", method = RequestMethod.POST)
    public Result<?> sign(HttpServletRequest request, HttpServletResponse response,
                                @RequestBody HashMap<String, Object> map){

        return Result.OK(syDpsSySignatureService.sign(map));
    }
}
