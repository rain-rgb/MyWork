package com.trtm.iot.taskUpdate.controller;

import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.base.controller.JeecgController;
import com.trtm.iot.taskUpdate.entity.TaskUpdate;
import com.trtm.iot.taskUpdate.service.ITaskUpdateService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

/**
 * @Description: 任务单修改记录
 * @Author: lis1
 * @Date: 2022-11-29
 * @Version: V1.0
 */
@Api(tags = "任务单修改记录")
@RestController
@RequestMapping("/taskUpdate/taskUpdate")
@Slf4j
public class TaskUpdateController extends JeecgController<TaskUpdate, ITaskUpdateService> {
    @Autowired
    private ITaskUpdateService taskUpdateService;

    /**
     * 分页列表查询
     *
     * @param taskUpdate
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "任务单修改记录-分页列表查询")
    @ApiOperation(value = "任务单修改记录-分页列表查询", notes = "任务单修改记录-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(TaskUpdate taskUpdate,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<TaskUpdate> queryWrapper = QueryGenerator.initQueryWrapper(taskUpdate, req.getParameterMap());
        Page<TaskUpdate> page = new Page<TaskUpdate>(pageNo, pageSize);
        IPage<TaskUpdate> pageList = taskUpdateService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 添加
     *
     * @param taskUpdate
     * @return
     */
    @AutoLog(value = "任务单修改记录-添加")
    @ApiOperation(value = "任务单修改记录-添加", notes = "任务单修改记录-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody TaskUpdate taskUpdate) {
        taskUpdateService.save(taskUpdate);
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param taskUpdate
     * @return
     */
    @AutoLog(value = "任务单修改记录-编辑")
    @ApiOperation(value = "任务单修改记录-编辑", notes = "任务单修改记录-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody TaskUpdate taskUpdate) {
        taskUpdateService.updateById(taskUpdate);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "任务单修改记录-通过id删除")
    @ApiOperation(value = "任务单修改记录-通过id删除", notes = "任务单修改记录-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        taskUpdateService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "任务单修改记录-批量删除")
    @ApiOperation(value = "任务单修改记录-批量删除", notes = "任务单修改记录-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.taskUpdateService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "任务单修改记录-通过id查询")
    @ApiOperation(value = "任务单修改记录-通过id查询", notes = "任务单修改记录-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        TaskUpdate taskUpdate = taskUpdateService.getById(id);
        if (taskUpdate == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(taskUpdate);
    }

    /**
     * 通过任务单编号查询修改记录
     *
     * @param code
     * @return
     */
    @AutoLog(value = "通过任务单编号查询修改记录")
    @ApiOperation(value = "通过任务单编号查询修改记录", notes = "通过任务单编号查询修改记录")
    @GetMapping(value = "/queryByTaskCode")
    public Result<?> queryByTaskCode(@RequestParam(name = "code") String code, String sysOrgCode) {
        QueryWrapper<TaskUpdate> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("task_code",code);
        queryWrapper.eq(sysOrgCode != null && !"".equals(sysOrgCode), "sys_org_code", sysOrgCode);
        TaskUpdate taskUpdate = taskUpdateService.getOne(queryWrapper);
        if (taskUpdate == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(taskUpdate);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param taskUpdate
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, TaskUpdate taskUpdate) {
        return super.exportXls(request, taskUpdate, TaskUpdate.class, "任务单修改记录");
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
        return super.importExcel(request, response, TaskUpdate.class);
    }

}
