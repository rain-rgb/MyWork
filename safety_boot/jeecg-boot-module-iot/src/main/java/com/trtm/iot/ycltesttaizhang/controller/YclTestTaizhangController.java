package com.trtm.iot.ycltesttaizhang.controller;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.trtm.iot.wztaizhang.entity.Wztaizhang;
import com.trtm.iot.wztaizhang.service.IWztaizhangService;
import com.trtm.iot.wztaizhang.service.impl.WztaizhangServiceImpl;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.oConvertUtils;
import com.trtm.iot.ycltesttaizhang.entity.YclTestTaizhang;
import com.trtm.iot.ycltesttaizhang.service.IYclTestTaizhangService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

/**
 * @Description: ycl_test_taizhang
 * @Author: jeecg-boot
 * @Date: 2023-05-15
 * @Version: V1.0
 */
@Api(tags = "ycl_test_taizhang")
@RestController
@RequestMapping("/ycltesttaizhang/yclTestTaizhang")
@Slf4j
public class YclTestTaizhangController extends JeecgController<YclTestTaizhang, IYclTestTaizhangService> {
    @Autowired
    private IYclTestTaizhangService yclTestTaizhangService;
    @Autowired
    private IWztaizhangService wztaizhangService;

    /**
     * 分页列表查询
     *
     * @param yclTestTaizhang
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "ycl_test_taizhang-分页列表查询")
    @ApiOperation(value = "ycl_test_taizhang-分页列表查询", notes = "ycl_test_taizhang-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(YclTestTaizhang yclTestTaizhang,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   String sys_depart_orgcode,
                                   HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        yclTestTaizhang.setSysOrgCode(sys_depart_orgcode+"*");
        QueryWrapper<YclTestTaizhang> queryWrapper = QueryGenerator.initQueryWrapper(yclTestTaizhang, req.getParameterMap());
        Page<YclTestTaizhang> page = new Page<YclTestTaizhang>(pageNo, pageSize);
        IPage<YclTestTaizhang> pageList = yclTestTaizhangService.page(page, queryWrapper);
        return Result.OK(pageList);
    }


    /**
     * 分页列表查询
     *
     * @param yclTestTaizhang
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "ycl_test_taizhang-分页列表查询")
    @ApiOperation(value = "ycl_test_taizhang-分页列表查询", notes = "ycl_test_taizhang-分页列表查询")
    @GetMapping(value = "/listwarn")
    public Result<?> queryPageListwarn(YclTestTaizhang yclTestTaizhang,String jinchangtime_begin,
                                       @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                       @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                       HttpServletRequest req) {
        Date date = new Date();
        SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String format = dateFormat.format(date);
        QueryWrapper<YclTestTaizhang> queryWrapper = new QueryWrapper<>();
        if(StringUtils.isNotBlank(yclTestTaizhang.getSysOrgCode())){
            queryWrapper.likeRight("sys_org_code",yclTestTaizhang.getSysOrgCode());
        }
        if(StringUtils.isNotBlank(jinchangtime_begin)){
            queryWrapper.between("jinchangtime",jinchangtime_begin,format);
        }
       // queryWrapper.eq("nodetype",1);
        queryWrapper.eq("reslut", "不合格").or().eq("cjreslut", "不合格");
        if(StringUtils.isNotBlank(yclTestTaizhang.getSysOrgCode())){
            queryWrapper.likeRight("sys_org_code",yclTestTaizhang.getSysOrgCode());
        }
        if(StringUtils.isNotBlank(jinchangtime_begin)){
            queryWrapper.between("jinchangtime",jinchangtime_begin,format);
        }
        Page<YclTestTaizhang> page = new Page<YclTestTaizhang>(pageNo, pageSize);
        IPage<YclTestTaizhang> pageList = yclTestTaizhangService.page(page, queryWrapper);
        return Result.OK(pageList);
    }


    /**
     * 瑞苍集团看板原材统计
     *
     * @param time 统计时间范围(本月、本年、全部)
     * @param orgCode 组织机构
     * @return
     */
    @AutoLog(value = "ycl_test_taizhang")
    @ApiOperation(value = "ycl_test_taizhang", notes = "ycl_test_taizhang")
    @GetMapping(value = "/getTj")
    public Result<?> getTj(String time, String orgCode) {
        Map<String, Object> map = new HashMap<>();
        String date = LocalDate.now().toString();
        String[] split = date.split("-");
        List<YclTestTaizhang> list = null;
        List<YclTestTaizhang> hgList = null;
        int bhs = 0;
        if ("thisMonth".equals(time)) {//统计本月的数据
            list = yclTestTaizhangService.getList(split[0] + "-" + split[1], orgCode);//本月所有数据
            hgList = yclTestTaizhangService.getHgList(split[0] + "-" + split[1], orgCode);//本月合格数据
             bhs = yclTestTaizhangService.getBhCount(split[0] + "-" + split[1], orgCode);
        } else if ("thisYear".equals(time)) {//统计本年的数据
            list = yclTestTaizhangService.getList(split[0], orgCode);//本月所有数据
            hgList = yclTestTaizhangService.getHgList(split[0], orgCode);//本月合格数据
            bhs = yclTestTaizhangService.getBhCount(split[0] , orgCode);
        } else if ("all".equals(time)) {//统计所有数据
            list = yclTestTaizhangService.getList("", orgCode);//总进场量
            hgList = yclTestTaizhangService.getHgList("", orgCode);//总合格数量
            bhs = yclTestTaizhangService.getBhCount("" , orgCode);
        }
        DecimalFormat df = new DecimalFormat("0.00");
        String percentStr = wztaizhangService.getPercentStr(hgList.size(), list.size());
        if (list.size() != 0) {
            map.put("hgl", percentStr);
        } else {
            map.put("hgl", "-");
        }
        if ("100.00%".equals(percentStr)) {
            map.put("bhl", "-");
        } else {
            map.put("bhl",df.format(bhs/(list.size() - hgList.size()))+"%" );
        }
        map.put("jcpc", list.size());
        map.put("bhgpc", (list.size() - hgList.size()));
        return Result.OK(map);
    }

    @AutoLog(value = "ycl_test_taizhang-分页列表查询")
    @ApiOperation(value = "ycl_test_taizhang-分页列表查询", notes = "ycl_test_taizhang-分页列表查询")
    @GetMapping(value = "/listByNode")
    public Result<?> listByNode(
            @RequestParam(name = "jctime", defaultValue = "2000-01-01") String jctime,
            @RequestParam(name = "nodetype", defaultValue = "6") String nodetype,
            @RequestParam(name = "orgCode", defaultValue = "A05A01A05A01") String orgCode,
            HttpServletRequest req) {
        QueryWrapper<YclTestTaizhang> queryWrapper = new QueryWrapper<>();
        queryWrapper.select(" d.depart_name gongyingshang,\n" +
                "\ttz.sys_org_code,\n" +
                " tz.nodetype, " +
                "\tIFNULL(count( 1 ),0)  cunfangplace, -- 进场批次\n" +
                "\tIFNULL(count( 1 ),0) shuliang, -- 检验批次\n" +
                "  IFNULL( sum( CASE WHEN  `reslut` = '合格' OR cjreslut='合格' THEN 1 ELSE 0 END),0)/count( 1 ) *100 count, -- 合格率\n" +
                "\tIFNULL(0,0) usepart -- 使用批次数");
        queryWrapper.last("tz LEFT JOIN sys_depart d on tz.sys_org_code = d.org_code\n" +
                "WHERE\n" +
                "1=1\n" +
                " and jinchangtime >= '" + jctime + "' -- 时间参数填写\n" +
                " and  nodetype = '" + nodetype + "' -- 材料类别选择\n" +
                " AND tz.sys_org_code LIKE '"+orgCode+"%'" +
                "GROUP BY\n" +
                "nodetype,\n" +
                "sys_org_code");

        List<YclTestTaizhang> pageList = yclTestTaizhangService.list(queryWrapper);
        return Result.OK(pageList);
    }


    /**
     * 添加
     *
     * @param yclTestTaizhang
     * @return
     */
    @AutoLog(value = "ycl_test_taizhang-添加")
    @ApiOperation(value = "ycl_test_taizhang-添加", notes = "ycl_test_taizhang-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody YclTestTaizhang yclTestTaizhang) {
        yclTestTaizhangService.save(yclTestTaizhang);
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param yclTestTaizhang
     * @return
     */
    @AutoLog(value = "ycl_test_taizhang-编辑")
    @ApiOperation(value = "ycl_test_taizhang-编辑", notes = "ycl_test_taizhang-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody YclTestTaizhang yclTestTaizhang) {
        yclTestTaizhangService.updateById(yclTestTaizhang);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "ycl_test_taizhang-通过id删除")
    @ApiOperation(value = "ycl_test_taizhang-通过id删除", notes = "ycl_test_taizhang-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        yclTestTaizhangService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "ycl_test_taizhang-批量删除")
    @ApiOperation(value = "ycl_test_taizhang-批量删除", notes = "ycl_test_taizhang-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.yclTestTaizhangService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "ycl_test_taizhang-通过id查询")
    @ApiOperation(value = "ycl_test_taizhang-通过id查询", notes = "ycl_test_taizhang-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        YclTestTaizhang yclTestTaizhang = yclTestTaizhangService.getById(id);
        if (yclTestTaizhang == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(yclTestTaizhang);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param yclTestTaizhang
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, YclTestTaizhang yclTestTaizhang) {
        return super.exportXls(request, yclTestTaizhang, YclTestTaizhang.class, "ycl_test_taizhang");
    }

    /**
     * 通过excel导入数据
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, YclTestTaizhang.class);
    }

}
