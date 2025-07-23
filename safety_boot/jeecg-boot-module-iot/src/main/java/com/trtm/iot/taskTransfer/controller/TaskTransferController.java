package com.trtm.iot.taskTransfer.controller;

import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import com.trtm.iot.taskTransfer.entity.TaskTransfer;
import com.trtm.iot.taskTransfer.service.ITaskTransferService;

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
 * @Description: 任务单转移
 * @Author: 李思怡
 * @Date: 2022-11-23
 * @Version: V1.0
 */
@Api(tags = "任务单转移")
@RestController
@RequestMapping("/taskTransfer/taskTransfer")
@Slf4j
public class TaskTransferController extends JeecgController<TaskTransfer, ITaskTransferService> {
    @Autowired
    private ITaskTransferService taskTransferService;

    /**
     * 分页列表查询
     *
     * @param taskTransfer
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "任务单转移-分页列表查询")
    @ApiOperation(value = "任务单转移-分页列表查询", notes = "任务单转移-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(TaskTransfer taskTransfer,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<TaskTransfer> queryWrapper = QueryGenerator.initQueryWrapper(taskTransfer, req.getParameterMap());
        Page<TaskTransfer> page = new Page<TaskTransfer>(pageNo, pageSize);
        IPage<TaskTransfer> pageList = taskTransferService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 添加
     *
     * @param taskTransfer
     * @return
     */
    @AutoLog(value = "任务单转移-添加")
    @ApiOperation(value = "任务单转移-添加", notes = "任务单转移-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody TaskTransfer taskTransfer) {
        taskTransferService.save(taskTransfer);
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param taskTransfer
     * @return
     */
    @AutoLog(value = "任务单转移-编辑")
    @ApiOperation(value = "任务单转移-编辑", notes = "任务单转移-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody TaskTransfer taskTransfer) {
        taskTransferService.updateById(taskTransfer);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "任务单转移-通过id删除")
    @ApiOperation(value = "任务单转移-通过id删除", notes = "任务单转移-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        taskTransferService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "任务单转移-批量删除")
    @ApiOperation(value = "任务单转移-批量删除", notes = "任务单转移-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.taskTransferService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "任务单转移-通过id查询")
    @ApiOperation(value = "任务单转移-通过id查询", notes = "任务单转移-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        TaskTransfer taskTransfer = taskTransferService.getById(id);
        if (taskTransfer == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(taskTransfer);
    }

    /**
     * 通过任务单编号查询任务单转移记录
     *
     * @param code
     * @return
     */
    @AutoLog(value = "通过任务单编号查询任务单转移记录")
    @ApiOperation(value = "通过任务单编号查询任务单转移记录", notes = "通过任务单编号查询任务单转移记录")
    @GetMapping(value = "/getByTaskCode")
    public Result<?> getByTaskCode(@RequestParam(name = "code") String code, String sysOrgCode) {
        QueryWrapper<TaskTransfer> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("task_code", code);
        queryWrapper.eq(sysOrgCode != null && !"".equals(sysOrgCode), "sys_org_code", sysOrgCode);
        TaskTransfer taskTransfer = taskTransferService.getOne(queryWrapper);
        if (taskTransfer == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(taskTransfer);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param taskTransfer
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, TaskTransfer taskTransfer) {
        return super.exportXls(request, taskTransfer, TaskTransfer.class, "任务单转移");
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
        return super.importExcel(request, response, TaskTransfer.class);
    }

}
