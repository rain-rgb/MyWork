package com.trtm.sy.registertable.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.trtm.sy.registertable.model.SyForm;
import com.trtm.sy.registertable.model.SyTableForm;
import com.trtm.sy.registertable.model.request.SyTableFormRequest;
import com.trtm.sy.registertable.service.SyFormRecordService;
import com.trtm.sy.registertable.service.SyFormReportService;
import com.trtm.sy.registertable.service.SyTableFormService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@Api(tags = "tableForm")
@RestController
@RequestMapping("/sy/tableForm/")
public class SyTableFormController extends JeecgController<SyTableForm, SyTableFormService> {

    @Autowired
    private SyTableFormService tableFormService;
    @Autowired
    private SyFormRecordService recordService;
    @Autowired
    private SyFormReportService reportService;



    /**
     * 查询所有的记录表和报告表
     *
     * @param request
     * @return 分页对象
     */
    @GetMapping("queryFormList")
    public Result<?> queryFormList(SyTableFormRequest request) {
        IPage<SyTableForm> pageResult = tableFormService.queryFormList(request);
        return Result.OK(pageResult);
    }

    /**
     * 添加原始记录和报告
     *
     * @param request 入参
     * @return 返参
     */
    @ApiOperation("添加原始记录和报告")
    @PostMapping("addSyTableForm")
    public Result<?> addSyTableForm(@RequestBody SyTableForm request) {
        return Result.OK(tableFormService.addSyTableForm(request));
    }

    /**
     * 删除原始记录和报告
     *
     * @param request 入参
     * @return 返参
     */
    @ApiOperation("删除原始记录和报告")
    @PostMapping("deleteSyTableForm")
    public Result<?> deleteSyTableForm(@RequestBody SyTableForm request) {
        return Result.OK(tableFormService.deleteSyTableForm(request));
//        return Result.OK("暂不支持删除");
    }

    /**
     * 查询对应的记录表表格样式数据
     *
     * @param bh 表号
     * @return 当前表格的结构样式数据
     */
    @GetMapping("getSyFormRecord")
    public Result<?> getSyFormRecord(String bh) {
        SyForm recordFormByBh = recordService.getRecordFormByBh(bh,"","");
        return Result.OK(recordFormByBh);
    }

    /**
     * 查询对应的报告表格数据
     *
     * @param bh 表号
     * @return 当前表格的结构样式数据
     */
    @GetMapping("getSyFormReport")
    public Result<?> getSyFormReport(String bh) {
        SyForm reportFormByBh = reportService.getReportFormByBh(bh);
        return Result.OK(reportFormByBh);
    }

    /**
     * 存储当前样品的表格样式和样品试验数据到ES
     *
     * @param entity 表格的结构样式和数据
     * @return
     */
    @PostMapping("saveTableData")
    public Result<?> saveTableData(@RequestBody SyForm entity) {
        tableFormService.saveTableData(entity);
        return Result.OK();
    }

    /**
     * 根据样品id和表号查询ES中的数据
     *
     * @param sampleId    样品id
     * @param tableNumber 表号
     * @return 当前的数据结构和试验数据
     */
    @GetMapping("getTableData")
    public Result<?> getTableData(@RequestParam String sampleId, @RequestParam String tableNumber) {
        SyForm result = tableFormService.getTableData(sampleId, tableNumber);
        return Result.OK(result);
    }


}
