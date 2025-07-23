package com.trtm.iot.gqpx_employee_training.controller;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.trtm.api.utils.StringUtils;
import com.trtm.iot.gqpx_training_plan.entity.GqpxTrainingPlan;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import com.trtm.iot.gqpx_employee_training.entity.GqpxEmployeeTraining;
import com.trtm.iot.gqpx_employee_training.service.IGqpxEmployeeTrainingService;

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
 * @Description: 人员培训记录
 * @Author: jeecg-boot
 * @Date: 2024-11-29
 * @Version: V1.0
 */
@Api(tags = "人员培训记录")
@RestController
@RequestMapping("/gqpx_employee_training/gqpxEmployeeTraining")
@Slf4j
public class GqpxEmployeeTrainingController extends JeecgController<GqpxEmployeeTraining, IGqpxEmployeeTrainingService> {
    @Autowired
    private IGqpxEmployeeTrainingService gqpxEmployeeTrainingService;

    /**
     * 分页列表查询
     *
     * @param gqpxEmployeeTraining
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "人员培训记录-分页列表查询")
    @ApiOperation(value = "人员培训记录-分页列表查询", notes = "人员培训记录-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(GqpxEmployeeTraining gqpxEmployeeTraining,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<GqpxEmployeeTraining> queryWrapper = QueryGenerator.initQueryWrapper(gqpxEmployeeTraining, req.getParameterMap());
        Page<GqpxEmployeeTraining> page = new Page<GqpxEmployeeTraining>(pageNo, pageSize);
        IPage<GqpxEmployeeTraining> pageList = gqpxEmployeeTrainingService.page(page, queryWrapper);
        List<GqpxEmployeeTraining> records = pageList.getRecords();
        for (GqpxEmployeeTraining record : records) {
            Integer status = record.getStatus();
            Date completionTime = record.getCompletionTime();
//			status等于1并且completionTime大于当前时间
            if (status == 1 && completionTime.getTime() > new Date().getTime()) {
                record.setStatus(1);
            } else {
                record.setStatus(0);
            }
        }
        return Result.OK(pageList);
    }

    @AutoLog(value = "培训记录-根据培训计划查询")
    @ApiOperation(value = "培训记录-根据培训计划查询", notes = "培训记录-根据培训计划查询")
    @GetMapping(value = "/trainingList")
    public Result<?> trainingList(@RequestParam(name = "guid", required = true) String guid) {
        LambdaQueryWrapper<GqpxEmployeeTraining> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(GqpxEmployeeTraining::getTraningGuid, guid);
        queryWrapper.orderByAsc(GqpxEmployeeTraining::getStatus);
        List<GqpxEmployeeTraining> list = gqpxEmployeeTrainingService.list(queryWrapper);
//        for (GqpxEmployeeTraining record : list) {
//            Integer status = record.getStatus();
//            Integer colourStatus = record.getColourStatus();
//            String expirationTime = record.getExpirationTime();
////			status等于1并且completionTime大于当前时间
//                if (status == 1 && expirationTime.getTime() > new Date().getTime()) {
//                    record.setColourStatus(1);
//                } else {
//                    record.setColourStatus(0);
//                }
//                gqpxEmployeeTrainingService.updateById(record);
//        }

        return Result.OK(list);
    }

    @AutoLog(value = "查询用户的培训记录-根据用户查询")
    @ApiOperation(value = "查询用户的培训记录-根据用户查询", notes = "查询用户的培训记录-根据用户查询")
    @GetMapping(value = "/userList")
    public Result<?> userList(@RequestParam(name = "userid", required = true) String userid) {
        LambdaQueryWrapper<GqpxEmployeeTraining> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(GqpxEmployeeTraining::getUserId, userid);
        queryWrapper.orderByDesc(GqpxEmployeeTraining::getCreateTime);
        List<GqpxEmployeeTraining> list = gqpxEmployeeTrainingService.list(queryWrapper);
//        for (GqpxEmployeeTraining record : list) {
//            Integer status = record.getStatus();
//            Integer colourStatus = record.getColourStatus();
//                Date expirationTime = record.getExpirationTime();
////			status等于1并且completionTime大于当前时间
//                if (status == 1 && expirationTime.getTime() > new Date().getTime()) {
//                    record.setColourStatus(1);
//                } else {
//                    record.setColourStatus(0);
//                }
//                gqpxEmployeeTrainingService.updateById(record);
//            }
        return Result.OK(list);
    }

    /**
     * 添加
     *
     * @param gqpxEmployeeTraining
     * @return
     */
    @AutoLog(value = "人员培训记录-添加")
    @ApiOperation(value = "人员培训记录-添加", notes = "人员培训记录-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody GqpxEmployeeTraining gqpxEmployeeTraining) {
        gqpxEmployeeTrainingService.save(gqpxEmployeeTraining);
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param gqpxEmployeeTraining
     * @return
     */
    @AutoLog(value = "人员培训记录-编辑")
    @ApiOperation(value = "人员培训记录-编辑", notes = "人员培训记录-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody GqpxEmployeeTraining gqpxEmployeeTraining) {
        gqpxEmployeeTrainingService.updateById(gqpxEmployeeTraining);
        return Result.OK("编辑成功!");
    }

    /**
     * 扫码获取traningGuid更改人员对应的培训计划状态，完成签到
     *
     * @param gqpxEmployeeTraining
     * @return
     */
    @AutoLog(value = "人员培训记录-签到")
    @ApiOperation(value = "人员培训记录-签到", notes = "人员培训记录-签到")
    @PutMapping(value = "/signIn")
    public Result<?> signIn(@RequestBody GqpxEmployeeTraining gqpxEmployeeTraining) {
        String userId = gqpxEmployeeTraining.getUserId();
        String traningGuid = gqpxEmployeeTraining.getTraningGuid();
        if (StringUtils.isEmpty(userId) || StringUtils.isEmpty(traningGuid)) {
            return Result.error("签到失败!参数错误！");
        }

        LambdaQueryWrapper<GqpxEmployeeTraining> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(GqpxEmployeeTraining::getUserId, userId)
                .eq(GqpxEmployeeTraining::getTraningGuid, traningGuid);
        GqpxEmployeeTraining gqpxEmployeeTrainingOne = gqpxEmployeeTrainingService.getOne(queryWrapper);
        if (gqpxEmployeeTrainingOne == null) {
            return Result.error("签到失败!人员不在培训计划内或无对应的培训计划！");
        }

        gqpxEmployeeTrainingOne.setStatus(1);
        gqpxEmployeeTrainingOne.setCompletionTime(new Date());
        gqpxEmployeeTrainingService.updateById(gqpxEmployeeTrainingOne);
        gqpxEmployeeTrainingService.updateUserStatus(gqpxEmployeeTrainingOne);
        return Result.OK("签到成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "人员培训记录-通过id删除")
    @ApiOperation(value = "人员培训记录-通过id删除", notes = "人员培训记录-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        gqpxEmployeeTrainingService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "人员培训记录-批量删除")
    @ApiOperation(value = "人员培训记录-批量删除", notes = "人员培训记录-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.gqpxEmployeeTrainingService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "人员培训记录-通过id查询")
    @ApiOperation(value = "人员培训记录-通过id查询", notes = "人员培训记录-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        GqpxEmployeeTraining gqpxEmployeeTraining = gqpxEmployeeTrainingService.getById(id);
        if (gqpxEmployeeTraining == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(gqpxEmployeeTraining);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param gqpxEmployeeTraining
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, GqpxEmployeeTraining gqpxEmployeeTraining) {
        return super.exportXls(request, gqpxEmployeeTraining, GqpxEmployeeTraining.class, "人员培训记录");
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
        return super.importExcel(request, response, GqpxEmployeeTraining.class);
    }

}
