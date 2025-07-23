package com.trtm.iot.trzhanglarenwudan.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.common.util.oConvertUtils;
import com.trtm.iot.trzhanglarenwudan.entity.TrZhanglaRenwudan;
import com.trtm.iot.trzhanglarenwudan.service.ITrZhanglaRenwudanService;

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
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

/**
 * @Description: 张拉任务单
 * @Author: jeecg-boot
 * @Date: 2021-09-07
 * @Version: V1.0
 */
@Api(tags = "张拉任务单")
@RestController
@RequestMapping("/trzhanglarenwudan/trZhanglaRenwudan")
@Slf4j
public class TrZhanglaRenwudanController extends JeecgController<TrZhanglaRenwudan, ITrZhanglaRenwudanService> {
    @Autowired
    private ITrZhanglaRenwudanService trZhanglaRenwudanService;
    @Autowired
    private RedisUtil redisUtil;

    /**
     * 分页列表查询
     *
     * @param trZhanglaRenwudan
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "张拉任务单-分页列表查询")
    @ApiOperation(value = "张拉任务单-分页列表查询", notes = "张拉任务单-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(TrZhanglaRenwudan trZhanglaRenwudan,
                                   String sys_depart_project,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req, String sys_depart_orgcode) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        if (trZhanglaRenwudan.getShebeibianh() == null) {
            if (!"null".equals(shebei)) {
                trZhanglaRenwudan.setShebeibianh(shebei);
            } else {
                trZhanglaRenwudan.setShebeibianh("空");
            }

        }
//        if (sys_depart_orgcode != null && sys_depart_orgcode.length() != 0) {  //如果想要全局组织机构控制所显示的数据要加上这个
//            trZhanglaRenwudan.setSysOrgCode("*" + sys_depart_orgcode + "*");
//        }
        trZhanglaRenwudan.setSgbwname("*" + trZhanglaRenwudan.getSgbwname() + "*");
        QueryWrapper<TrZhanglaRenwudan> queryWrapper = QueryGenerator.initQueryWrapper(trZhanglaRenwudan, req.getParameterMap());
        queryWrapper.likeRight(sys_depart_project != null, "sgbwUuid", sys_depart_project);
        Page<TrZhanglaRenwudan> page = new Page<TrZhanglaRenwudan>(pageNo, pageSize);
        IPage<TrZhanglaRenwudan> pageList = trZhanglaRenwudanService.page(page, queryWrapper);
        return Result.OK(pageList);
    }


    /**
     * 任务单列表查询
     *
     * @param trZhanglaRenwudan
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "张拉任务单-分页列表查询")
    @ApiOperation(value = "张拉任务单-分页列表查询", notes = "张拉任务单-分页列表查询")
    @PostMapping(value = "/rwdlist")
    public Result<?> queryrwdList(TrZhanglaRenwudan trZhanglaRenwudan,
                                  @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                  @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                  HttpServletRequest req, String sys_depart_orgcode) {
        if (trZhanglaRenwudan.getWcstatus() == null) {
            trZhanglaRenwudan.setWcstatus(0);
        }
        if (!StringUtils.isEmpty(trZhanglaRenwudan.getSgbwname())) {
            trZhanglaRenwudan.setSgbwname(trZhanglaRenwudan.getSgbwname() + "*");
        }
        QueryWrapper<TrZhanglaRenwudan> queryWrapper = QueryGenerator.initQueryWrapper(trZhanglaRenwudan, req.getParameterMap());
        queryWrapper.orderByDesc("id");
        List<TrZhanglaRenwudan> pageList = trZhanglaRenwudanService.list(queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 添加
     *
     * @param trZhanglaRenwudan
     * @return
     */
    @AutoLog(value = "张拉任务单-添加")
    @ApiOperation(value = "张拉任务单-添加", notes = "张拉任务单-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody TrZhanglaRenwudan trZhanglaRenwudan) {
        trZhanglaRenwudanService.save(trZhanglaRenwudan);
        return Result.OK("添加成功！");
    }

    /**
     * 张拉任务单对外开放添加数据接口
     *
     * @param trZhanglaRenwudan
     * @return
     */
    @AutoLog(value = "张拉任务单-添加")
    @ApiOperation(value = "张拉任务单-添加", notes = "张拉任务单-添加")
    @PostMapping(value = "/addOpen")
    public Result<?> addOpen(@RequestBody TrZhanglaRenwudan trZhanglaRenwudan) {
        if (trZhanglaRenwudan.getUuid() != null) {
            TrZhanglaRenwudan trZhanglaRenwudan1 = trZhanglaRenwudanService.selectone(trZhanglaRenwudan.getUuid());
            if (trZhanglaRenwudan1 != null) {
                trZhanglaRenwudan.setId(trZhanglaRenwudan1.getId());
                boolean update = trZhanglaRenwudanService.updateById(trZhanglaRenwudan);
                if (update) {
                    return Result.OK("添加成功！");
                } else {
                    return Result.error("添加失败！");
                }
            } else {
                boolean save = trZhanglaRenwudanService.save(trZhanglaRenwudan);
                if (save) {
                    return Result.OK("添加成功！");
                } else {
                    return Result.error("添加失败！");
                }
            }
        }
        return Result.error("添加失败！");
    }

    /**
     * 编辑
     *
     * @param trZhanglaRenwudan
     * @return
     */
    @AutoLog(value = "张拉任务单-编辑")
    @ApiOperation(value = "张拉任务单-编辑", notes = "张拉任务单-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody TrZhanglaRenwudan trZhanglaRenwudan) {
        trZhanglaRenwudanService.updateById(trZhanglaRenwudan);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "张拉任务单-通过id删除")
    @ApiOperation(value = "张拉任务单-通过id删除", notes = "张拉任务单-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        trZhanglaRenwudanService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "张拉任务单-批量删除")
    @ApiOperation(value = "张拉任务单-批量删除", notes = "张拉任务单-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.trZhanglaRenwudanService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "张拉任务单-通过id查询")
    @ApiOperation(value = "张拉任务单-通过id查询", notes = "张拉任务单-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        TrZhanglaRenwudan trZhanglaRenwudan = trZhanglaRenwudanService.getById(id);
        if (trZhanglaRenwudan == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(trZhanglaRenwudan);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param trZhanglaRenwudan
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, TrZhanglaRenwudan trZhanglaRenwudan) {
        return super.exportXls(request, trZhanglaRenwudan, TrZhanglaRenwudan.class, "张拉任务单");
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
        return super.importExcel(request, response, TrZhanglaRenwudan.class);
    }

}
