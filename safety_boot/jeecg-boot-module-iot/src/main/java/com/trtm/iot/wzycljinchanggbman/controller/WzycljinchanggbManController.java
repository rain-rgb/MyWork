package com.trtm.iot.wzycljinchanggbman.controller;

import java.util.*;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.trtm.iot.wzcailiaonamedict.entity.Wzcailiaonamedict;
import com.trtm.iot.wzcailiaonamedictman.entity.WzcailiaonamedictMan;
import com.trtm.iot.wzcailiaonamedictman.service.IWzcailiaonamedictManService;
import com.trtm.iot.wzgongyingshang.entity.Wzgongyingshang;
import com.trtm.iot.wzgongyingshangman.entity.WzgongyingshangMan;
import com.trtm.iot.wzgongyingshangman.service.IWzgongyingshangManService;
import com.trtm.iot.wzliaocang.entity.Wzliaocang;
import com.trtm.iot.wzliaocang.service.IWzliaocangService;
import com.trtm.iot.yclsl.entity.Wzycljinchanggb;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.poi.ss.formula.functions.T;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.common.util.oConvertUtils;
import com.trtm.iot.wzycljinchanggbman.entity.WzycljinchanggbMan;
import com.trtm.iot.wzycljinchanggbman.service.IWzycljinchanggbManService;

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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

import static org.jeecg.common.util.DateUtils.getTimestampten;

/**
 * @Description: wzycljinchanggb_man
 * @Author: jeecg-boot
 * @Date: 2022-08-08
 * @Version: V1.0
 */
@Api(tags = "wzycljinchanggb_man")
@RestController
@RequestMapping("/wzycljinchanggbman/wzycljinchanggbMan")
@Slf4j
public class WzycljinchanggbManController extends JeecgController<WzycljinchanggbMan, IWzycljinchanggbManService> {
    @Autowired
    private IWzycljinchanggbManService wzycljinchanggbManService;
    @Autowired
    RedisUtil redisUtil;
    @Autowired
    private IWzliaocangService wzliaocangService;
    @Autowired
    IWzcailiaonamedictManService wzcailiaonamedictManService;
    @Autowired
    IWzgongyingshangManService wzgongyingshangManService;
    @Value("${jeecg.path.upload}")
    private String upLoadPath;

    /**
     * 分页列表查询
     *
     * @param wzycljinchanggbMan
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "wzycljinchanggb_man-分页列表查询")
    @ApiOperation(value = "wzycljinchanggb_man-分页列表查询", notes = "wzycljinchanggb_man-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(WzycljinchanggbMan wzycljinchanggbMan,
                                   String nodetypeCP,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        if (wzycljinchanggbMan.getShebeibianhao() == null) {
            if (!"null".equals(shebei)) {
                wzycljinchanggbMan.setShebeibianhao(shebei);
            } else {
                wzycljinchanggbMan.setShebeibianhao("空");
            }
        }
        List<String> cailiaoList = wzycljinchanggbManService.getCailiaoByNodetype(nodetypeCP);
        QueryWrapper<WzycljinchanggbMan> queryWrapper = QueryGenerator.initQueryWrapper(wzycljinchanggbMan, req.getParameterMap());
        queryWrapper.in(nodetypeCP != null && cailiaoList.size() != 0, "cailiaono", cailiaoList);
        queryWrapper.ne("isdel", "2");
        Page<WzycljinchanggbMan> page = new Page<WzycljinchanggbMan>(pageNo, pageSize);
        IPage<WzycljinchanggbMan> pageList = wzycljinchanggbManService.page(page, queryWrapper);
//        List<WzycljinchanggbMan> records = pageList.getRecords();
//        for (WzycljinchanggbMan record : records) {
//            if (record.getTaizhangtj() != 1) {
//                record.setRemark("未推送");
//            } else {
//                if (record.getTaizhangid() != null && record.getTaizhangid() != 0 ) {
//                    Integer jyStatus = wzycljinchanggbManService.getJYStatus(record.getTaizhangid());
//                    if ( jyStatus == 1) {
//                        record.setRemark("推送成功");
//                    } else {
//                        record.setRemark("推送失败");
//                    }
//                } else {
//                    record.setRemark("推送失败");
//                }
//            }
//        }
        return Result.OK(pageList);
    }

    /**
     * 添加
     *
     * @param wzycljinchanggbMan
     * @return
     */
    @AutoLog(value = "wzycljinchanggb_man-添加")
    @ApiOperation(value = "wzycljinchanggb_man-添加", notes = "wzycljinchanggb_man-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody WzycljinchanggbMan wzycljinchanggbMan) {
        String uuid = UUID.randomUUID().toString();//随机生成唯一码UUID
        wzycljinchanggbMan.setGuid(uuid);
        Integer ts = getTimestampten();//获取当前系统的时间戳（10位）
        wzycljinchanggbMan.setTs(ts);
        wzycljinchanggbMan.setIsdel("0");
        wzycljinchanggbManService.save(wzycljinchanggbMan);
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param wzycljinchanggbMan
     * @return
     */
    @AutoLog(value = "wzycljinchanggb_man-编辑")
    @ApiOperation(value = "wzycljinchanggb_man-编辑", notes = "wzycljinchanggb_man-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody WzycljinchanggbMan wzycljinchanggbMan) {
        Integer ts = getTimestampten();//获取当前系统的时间戳（10位）
        wzycljinchanggbMan.setTs(ts);
        wzycljinchanggbMan.setIsdel("1");
        wzycljinchanggbMan.setTaizhangtj(0);
        wzycljinchanggbManService.updateById(wzycljinchanggbMan);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "wzycljinchanggb_man-通过id删除")
    @ApiOperation(value = "wzycljinchanggb_man-通过id删除", notes = "wzycljinchanggb_man-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        WzycljinchanggbMan wzycljinchanggbMan = new WzycljinchanggbMan();
        int integer = Integer.parseInt(id);
        wzycljinchanggbMan.setId(integer);
        wzycljinchanggbMan.setIsdel("2");
        wzycljinchanggbManService.updateById(wzycljinchanggbMan);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "wzycljinchanggb_man-批量删除")
    @ApiOperation(value = "wzycljinchanggb_man-批量删除", notes = "wzycljinchanggb_man-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.wzycljinchanggbManService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "wzycljinchanggb_man-通过id查询")
    @ApiOperation(value = "wzycljinchanggb_man-通过id查询", notes = "wzycljinchanggb_man-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        WzycljinchanggbMan wzycljinchanggbMan = wzycljinchanggbManService.getById(id);
        if (wzycljinchanggbMan == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(wzycljinchanggbMan);
    }
//	/**
//	 * 导出excel
//	 *
//	 * @param request
//	 * @param wzycljinchanggbMan
//	 */
//	@RequestMapping(value = "/exportXls")
//	public ModelAndView exportXls(HttpServletRequest request, WzycljinchanggbMan wzycljinchanggbMan) {
//
//		return super.exportXls(request, wzycljinchanggbMan, WzycljinchanggbMan.class, "wzycljinchanggb_man");
//	}

    /**
     * 导出excel
     *
     * @param request
     * @param wzycljinchanggbMan
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, WzycljinchanggbMan wzycljinchanggbMan) {
        return exportXls1(request, wzycljinchanggbMan, WzycljinchanggbMan.class, "wzycljinchanggb_man");
    }

    /**
     * 导出excel
     *
     * @param request
     */
    protected ModelAndView exportXls1(HttpServletRequest request, WzycljinchanggbMan wzycljinchanggbMan, Class<WzycljinchanggbMan> clazz, String title) {
        // Step.1 组装查询条件
        QueryWrapper<WzycljinchanggbMan> queryWrapper = QueryGenerator.initQueryWrapper(wzycljinchanggbMan, request.getParameterMap());
//		queryWrapper.select("jinchuliaodanNo,"+"cailiaoNo,"+"jinchangshijian,"+"chuchangshijian,"+"pici,"
//				+"cheliangbianhao,"+"gongyingshangdanweibianma,"+"qianchepai,"+"maozhong,"+"pizhong,"
//				+"jingzhong,"+"kouzhong,"+"koulv,"+"sibangyuan,"+"shebeibianhao,"+"isdel,"+"maozhongt,"
//				+"pizhongt,"+"lcbianhao,"+"jingzhongt,"+"shifouhege");
        queryWrapper.ne("isdel", "2");
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

        // Step.2 获取导出数据
        List<WzycljinchanggbMan> pageList = wzycljinchanggbManService.list(queryWrapper);
        List<WzycljinchanggbMan> exportList = null;
        for (WzycljinchanggbMan wzycljinchanggb1 : pageList) {
            String cailiaono = wzycljinchanggb1.getCailiaono();
            if (cailiaono != null) {
                WzcailiaonamedictMan wzcailiaonamedict = wzcailiaonamedictManService.queryselectone1(cailiaono);
                if (wzcailiaonamedict != null) {
                    String cailiaoname = wzcailiaonamedict.getCailiaoname();
                    if (cailiaoname != null) {
                        wzycljinchanggb1.setCailiaono(cailiaoname);
                    }
                }
            }
            String lcbianhao = wzycljinchanggb1.getLcbianhao();
            if (lcbianhao != null) {
                Wzliaocang queryselectone = wzliaocangService.queryselectone(lcbianhao);
                if (queryselectone != null) {
                    String Name = queryselectone.getName();
                    if (Name != null) {
                        wzycljinchanggb1.setLcbianhao(Name);
                    }
                }
            }
            String gongyingshangdanweibianma = wzycljinchanggb1.getGongyingshangdanweibianma();
            if (gongyingshangdanweibianma != null) {
                WzgongyingshangMan querywzone = wzgongyingshangManService.selectnameone(gongyingshangdanweibianma);
                if (querywzone != null) {
                    String gongyingshangname = querywzone.getGongyingshangname();
                    if (gongyingshangname != null) {
                        wzycljinchanggb1.setGongyingshangdanweibianma(gongyingshangname);
                    }
                }
            }
        }
        // 过滤选中数据
        String selections = request.getParameter("selections");
        if (oConvertUtils.isNotEmpty(selections)) {
            List<String> selectionList = Arrays.asList(selections.split(","));
            exportList = pageList.stream().filter(item -> selectionList.contains(getId(item))).collect(Collectors.toList());
        } else {
            exportList = pageList;
        }

        // Step.3 AutoPoi 导出Excel
        ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
        mv.addObject(NormalExcelConstants.FILE_NAME, title); //此处设置的filename无效 ,前端会重更新设置一下
        mv.addObject(NormalExcelConstants.CLASS, clazz);
        //update-begin--Author:liusq  Date:20210126 for：图片导出报错，ImageBasePath未设置--------------------
        ExportParams exportParams = new ExportParams(title + "报表", "导出人:" + sysUser.getRealname(), title);
        exportParams.setImageBasePath(upLoadPath);
        //update-end--Author:liusq  Date:20210126 for：图片导出报错，ImageBasePath未设置----------------------
        mv.addObject(NormalExcelConstants.PARAMS, exportParams);
        mv.addObject(NormalExcelConstants.DATA_LIST, exportList);
        return mv;
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
        return super.importExcel(request, response, WzycljinchanggbMan.class);
    }

    /**
     * 批量添加
     *
     * @param wzycljinchanggbMan
     * @return
     */
    @AutoLog(value = "批量添加")
    @ApiOperation(value = "批量添加", notes = "批量添加")
    @PostMapping(value = "/ListAdd")
    public Result<?> ListAdd(@RequestBody List<WzycljinchanggbMan> list) {
        System.out.println("list = " + list);
        wzycljinchanggbManService.saveListWzycljinchanggb(list);
        return Result.OK("添加成功！");

    }





}
