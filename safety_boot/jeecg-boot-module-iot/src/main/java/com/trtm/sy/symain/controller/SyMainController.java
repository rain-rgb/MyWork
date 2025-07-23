package com.trtm.sy.symain.controller;

import java.util.Arrays;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.trtm.sy.symainprocess.entity.SyMainProcess;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import com.trtm.sy.symain.entity.SyMain;
import com.trtm.sy.symain.service.ISyMainService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

/**
 * @Description: sy_main
 * @Author: jeecg-boot
 * @Date: 2022-09-08
 * @Version: V1.0
 */
@Api(tags = "sy_main")
@RestController
@RequestMapping("/syMain/syMain")
@Slf4j
public class SyMainController extends JeecgController<SyMain, ISyMainService> {
    @Autowired
    private ISyMainService syMainService;

    /**
     * 分页列表查询
     *
     * @param syMain
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "sy_main-分页列表查询")
    @ApiOperation(value = "sy_main-分页列表查询", notes = "sy_main-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(SyMain syMain,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<SyMain> queryWrapper = QueryGenerator.initQueryWrapper(syMain, req.getParameterMap());
        Page<SyMain> page = new Page<SyMain>(pageNo, pageSize);
        IPage<SyMain> pageList = syMainService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 添加
     *
     * @param syMain
     * @return
     */
    @AutoLog(value = "sy_main-添加")
    @ApiOperation(value = "sy_main-添加", notes = "sy_main-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody SyMain syMain) {
        syMainService.save(syMain);
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param syMain
     * @return
     */
    @AutoLog(value = "sy_main-编辑")
    @ApiOperation(value = "sy_main-编辑", notes = "sy_main-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody SyMain syMain) {
        syMainService.updateById(syMain);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "sy_main-通过id删除")
    @ApiOperation(value = "sy_main-通过id删除", notes = "sy_main-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        syMainService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "sy_main-批量删除")
    @ApiOperation(value = "sy_main-批量删除", notes = "sy_main-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.syMainService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

//	/**
//	 * 通过id查询
//	 *
//	 * @param id
//	 * @return
//	 */
//	@AutoLog(value = "sy_main-通过id查询")
//	@ApiOperation(value="sy_main-通过id查询", notes="sy_main-通过id查询")
//	@GetMapping(value = "/queryById")
//	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
//		SyMain syMain = syMainService.getById(id);
//		if(syMain==null) {
//			return Result.error("未找到对应数据");
//		}
//		return Result.OK(syMain);
//	}

    /**
     * 查询详情
     *
     * @param uuid 主表uuid
     * @return
     */
    @AutoLog(value = "sy_Main查询详情")
    @ApiOperation(value = "查询详情", notes = "跟据主表uuid查询详情")
    @GetMapping(value = "/getDetails")
    public Result<?> getDetails(@RequestParam(name = "uuid") String uuid) {
        List<SyMainProcess> details = syMainService.getDetails(uuid);
        if (details.size() > 0) {
            return Result.OK(details);
        }
        return Result.error("未找到对应数据,uuid:" + uuid);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param syMain
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, SyMain syMain) {
        return super.exportXls(request, syMain, SyMain.class, "sy_main");
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
        return super.importExcel(request, response, SyMain.class);
    }

}
