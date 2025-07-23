package com.trtm.iot.gscsConstructionRecord.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.trtm.iot.gscsConstructionRecord.entity.ConstructionStatusVo;

import com.trtm.iot.gscsDept.service.IGscsDeptService;
import com.trtm.iot.gscsSectionDept.entity.GscsSectionDept;
import com.trtm.iot.gscsSectionDept.service.IGscsSectionDeptService;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import com.trtm.iot.gscsConstructionRecord.entity.GscsConstructionRecord;
import com.trtm.iot.gscsConstructionRecord.service.IGscsConstructionRecordService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;


import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.vo.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

/**
 * @Description: 班组安全管控系统未施工记录表
 * @Author: jeecg-boot
 * @Date: 2022-01-25
 * @Version: V1.0
 */
@Api(tags = "班组安全管控系统未施工记录表")
@RestController
@RequestMapping("/gscsConstructionRecord/gscsConstructionRecord")
@Slf4j
public class GscsConstructionRecordController extends JeecgController<GscsConstructionRecord, IGscsConstructionRecordService> {
    @Autowired
    private IGscsConstructionRecordService gscsConstructionRecordService;

    @Autowired
    private IGscsDeptService gscsDeptService;

    @Autowired
    private IGscsSectionDeptService gscsSectionDeptService;

    /**
     * 分页列表查询
     *
     * @param gscsConstructionRecord
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "班组安全管控系统未施工记录表-分页列表查询")
    @ApiOperation(value = "班组安全管控系统未施工记录表-分页列表查询", notes = "班组安全管控系统未施工记录表-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(GscsConstructionRecord gscsConstructionRecord,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<GscsConstructionRecord> queryWrapper = QueryGenerator.initQueryWrapper(gscsConstructionRecord, req.getParameterMap());
        Page<GscsConstructionRecord> page = new Page<GscsConstructionRecord>(pageNo, pageSize);
        IPage<GscsConstructionRecord> pageList = gscsConstructionRecordService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 添加
     *
     * @param gscsConstructionRecord
     * @return
     */
    @AutoLog(value = "班组安全管控系统未施工记录表-添加")
    @ApiOperation(value = "班组安全管控系统未施工记录表-添加", notes = "班组安全管控系统未施工记录表-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody GscsConstructionRecord gscsConstructionRecord) {
        gscsConstructionRecordService.save(gscsConstructionRecord);
        return Result.OK("添加成功！");
    }

    /**
     * 获取施工状态统计
     *
     * @return
     */
    @AutoLog(value = "获取施工状态统计")
    @ApiOperation(value = "获取施工状态统计", notes = "获取施工状态统计")
    @GetMapping(value = "/getConstructionStatusSta")
    public Result<?> getConstructionStatusSta(@RequestParam(value = "orgCode", required = false, defaultValue = "") String orgCode) {
        String section;
        if (orgCode.isEmpty()) {
            LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
            section = loginUser.getOrgCode();//获取当前用户所属标段
        } else {
            section = orgCode;
        }
        ConstructionStatusVo constructionStatus = gscsConstructionRecordService.getConstructionStatusSta(section);
        return Result.OK(constructionStatus);
    }

    /**
     * 获取施工状态历史记录（统计）
     *
     * @return
     */
    @AutoLog(value = "获取施工状态历史记录")
    @ApiOperation(value = "获取施工状态历史记录", notes = "获取施工状态历史记录")
    @GetMapping(value = "/getHistoryConstructionStatusSta")
    public Result<?> getHistoryConstructionStatusSta(@RequestParam(value = "orgCode", required = false, defaultValue = "")String orgCode){
        String section;
        if (orgCode.isEmpty()) {
            LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
            section = loginUser.getOrgCode();//获取当前用户所属标段
        } else {
            section = orgCode;
        }
        List list = gscsConstructionRecordService.getHistoryConstructionStatusSta(section);
        return Result.OK(list);
    }

    /**
     * 获取当日施工状态（非统计）
     * 获取当日班前会是否上传
     *
     * @return
     */
    @AutoLog(value = "获取当日班前会是否上传")
    @ApiOperation(value = "获取当日班前会是否上传", notes = "获取当日班前会是否上传")
    @GetMapping(value = "/getConstructionStatus")
    public Result<?> getConstructionStatus() {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String section = loginUser.getOrgCode();//获取当前用户所属标段
        List<GscsSectionDept> deptList = gscsSectionDeptService.getDeptList(section);//获取当前标段下班组列表
        HashMap<String, Boolean> map = new HashMap<>();
        for (GscsSectionDept gscsSectionDept : deptList) {
            String deptId = gscsSectionDept.getDeptId();
            Boolean constructionStatus = gscsConstructionRecordService.getConstructionStatus(deptId);
            String deptName = gscsDeptService.selectDeptName(deptId);
            map.put(deptName, constructionStatus);
        }
        return Result.OK(map);
    }

    /**
     * 编辑
     *
     * @param gscsConstructionRecord
     * @return
     */
    @AutoLog(value = "班组安全管控系统未施工记录表-编辑")
    @ApiOperation(value = "班组安全管控系统未施工记录表-编辑", notes = "班组安全管控系统未施工记录表-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody GscsConstructionRecord gscsConstructionRecord) {
        gscsConstructionRecordService.updateById(gscsConstructionRecord);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "班组安全管控系统未施工记录表-通过id删除")
    @ApiOperation(value = "班组安全管控系统未施工记录表-通过id删除", notes = "班组安全管控系统未施工记录表-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        gscsConstructionRecordService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "班组安全管控系统未施工记录表-批量删除")
    @ApiOperation(value = "班组安全管控系统未施工记录表-批量删除", notes = "班组安全管控系统未施工记录表-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.gscsConstructionRecordService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "班组安全管控系统未施工记录表-通过id查询")
    @ApiOperation(value = "班组安全管控系统未施工记录表-通过id查询", notes = "班组安全管控系统未施工记录表-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        GscsConstructionRecord gscsConstructionRecord = gscsConstructionRecordService.getById(id);
        if (gscsConstructionRecord == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(gscsConstructionRecord);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param gscsConstructionRecord
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, GscsConstructionRecord gscsConstructionRecord) {
        return super.exportXls(request, gscsConstructionRecord, GscsConstructionRecord.class, "班组安全管控系统未施工记录表");
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
        return super.importExcel(request, response, GscsConstructionRecord.class);
    }

}
