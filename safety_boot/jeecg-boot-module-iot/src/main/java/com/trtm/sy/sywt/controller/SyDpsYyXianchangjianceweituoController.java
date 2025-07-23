package com.trtm.sy.sywt.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.trtm.sy.sywt.entity.SyDpsYyXianchangjianceweituo;
import com.trtm.sy.sywt.entity.XcWtRequest;
import com.trtm.sy.sywt.entity.XcWtResponse;
import com.trtm.sy.sywt.service.ISyDpsYyXianchangjianceweituoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Map;

/**
 * @Description: sy_dps_yy_xianchangjianceweituo
 * @Author: jeecg-boot
 * @Date: 2023-06-28
 * @Version: V1.0
 */
@Api(tags = "sy_dps_yy_xianchangjianceweituo")
@RestController
@RequestMapping("/sywt/syDpsYyXianchangjianceweituo")
@Slf4j
public class SyDpsYyXianchangjianceweituoController extends JeecgController<SyDpsYyXianchangjianceweituo, ISyDpsYyXianchangjianceweituoService> {
    @Autowired
    private ISyDpsYyXianchangjianceweituoService syDpsYyXianchangjianceweituoService;

    /**
     * 分页列表查询
     *
     * @param syDpsYyXianchangjianceweituo
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "sy_dps_yy_xianchangjianceweituo-分页列表查询")
    @ApiOperation(value = "sy_dps_yy_xianchangjianceweituo-分页列表查询", notes = "sy_dps_yy_xianchangjianceweituo-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(SyDpsYyXianchangjianceweituo syDpsYyXianchangjianceweituo,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<SyDpsYyXianchangjianceweituo> queryWrapper = QueryGenerator.initQueryWrapper(syDpsYyXianchangjianceweituo, req.getParameterMap());
        Page<SyDpsYyXianchangjianceweituo> page = new Page<SyDpsYyXianchangjianceweituo>(pageNo, pageSize);
        IPage<SyDpsYyXianchangjianceweituo> pageList = syDpsYyXianchangjianceweituoService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 添加
     *
     * @param syDpsYyXianchangjianceweituo
     * @return
     */
    @AutoLog(value = "sy_dps_yy_xianchangjianceweituo-添加")
    @ApiOperation(value = "sy_dps_yy_xianchangjianceweituo-添加", notes = "sy_dps_yy_xianchangjianceweituo-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody SyDpsYyXianchangjianceweituo syDpsYyXianchangjianceweituo) {
        syDpsYyXianchangjianceweituoService.save(syDpsYyXianchangjianceweituo);
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param syDpsYyXianchangjianceweituo
     * @return
     */
    @AutoLog(value = "sy_dps_yy_xianchangjianceweituo-编辑")
    @ApiOperation(value = "sy_dps_yy_xianchangjianceweituo-编辑", notes = "sy_dps_yy_xianchangjianceweituo-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody SyDpsYyXianchangjianceweituo syDpsYyXianchangjianceweituo) {
        syDpsYyXianchangjianceweituoService.updateById(syDpsYyXianchangjianceweituo);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "sy_dps_yy_xianchangjianceweituo-通过id删除")
    @ApiOperation(value = "sy_dps_yy_xianchangjianceweituo-通过id删除", notes = "sy_dps_yy_xianchangjianceweituo-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        syDpsYyXianchangjianceweituoService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "sy_dps_yy_xianchangjianceweituo-批量删除")
    @ApiOperation(value = "sy_dps_yy_xianchangjianceweituo-批量删除", notes = "sy_dps_yy_xianchangjianceweituo-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.syDpsYyXianchangjianceweituoService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "sy_dps_yy_xianchangjianceweituo-通过id查询")
    @ApiOperation(value = "sy_dps_yy_xianchangjianceweituo-通过id查询", notes = "sy_dps_yy_xianchangjianceweituo-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        SyDpsYyXianchangjianceweituo syDpsYyXianchangjianceweituo = syDpsYyXianchangjianceweituoService.getById(id);
        if (syDpsYyXianchangjianceweituo == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(syDpsYyXianchangjianceweituo);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param syDpsYyXianchangjianceweituo
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, SyDpsYyXianchangjianceweituo syDpsYyXianchangjianceweituo) {
        return super.exportXls(request, syDpsYyXianchangjianceweituo, SyDpsYyXianchangjianceweituo.class, "sy_dps_yy_xianchangjianceweituo");
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
        return super.importExcel(request, response, SyDpsYyXianchangjianceweituo.class);
    }


    /**
     * 现场委托单保存接口
     */
    @PostMapping("/addXCWTD")
    public Result<?> addXCWTD(@RequestBody SyDpsYyXianchangjianceweituo xianchangjianceweituo) {
        try {
            syDpsYyXianchangjianceweituoService.saveWeiTuoDan(xianchangjianceweituo);
            return Result.ok("新增委托成功");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("新增委托失败");
        }
    }

    /**
     * 查询现场委托样品管理页面数据
     * @param request
     * @return
     */
    @GetMapping("/XCWTDFPList")
    public Result<?> getListXcWt(XcWtRequest request) {
        IPage<XcWtResponse> responseList = syDpsYyXianchangjianceweituoService.getListXcWt(request);
        return Result.OK(responseList);
    }


    @GetMapping("/getSampleDetailById")
    public Result<?> getXcWtById(@RequestParam Integer id) {
        Map<String, Object> result = syDpsYyXianchangjianceweituoService.getXcWtById(id);
        return Result.OK(result);
    }





}
