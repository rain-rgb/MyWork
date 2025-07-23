package com.trtm.sy.registertable.controller;

import cn.hutool.core.util.ObjectUtil;
import com.trtm.sy.registermodules.api.exception.BaseException;
import com.trtm.sy.registertable.mapper.SyFormBaseMapper;
import com.trtm.sy.registertable.model.SyForm;
import com.trtm.sy.registertable.model.SyFormRecord;
import com.trtm.sy.registertable.model.request.submissionReportRequest;
import com.trtm.sy.registertable.service.SyFormRecordService;
import com.trtm.sy.registertable.service.SyTableFormService;
import io.swagger.annotations.Api;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.base.controller.JeecgController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import javax.annotation.Resource;
import java.util.List;

@Api(tags = "formRecord")
@RestController
@RequestMapping("/sy/formRecord/")
public class SyFormRecordController extends JeecgController<SyFormRecord, SyFormRecordService> {
    @Autowired
    private SyFormRecordService recordService;
    @Autowired
    private SyTableFormService tableFormService;
    @Resource
    private SyFormBaseMapper syFormBaseMapper;

    /**
     * 根据样品id和表号查询原始记录表格数据
     *
     * @param sampleId    样品id
     * @param tableNumber 表号
     * @return 当前的数据结构和试验数据
     */
    @GetMapping("getRecordTableData")
    public Result<?> getTableData(@RequestParam String sampleId, @RequestParam String tableNumber,@RequestParam String type) {
        SyForm result = tableFormService.getTableData(sampleId, tableNumber);
        if (ObjectUtil.isNotNull(result)) {
            return Result.OK(result);
        }
        return Result.OK(recordService.getRecordFormByBh(tableNumber,sampleId, type));
    }

//    /**
//     * 提交原始记录报告
//     *
//     * @return ResponseData
//     */
//    @PostMapping("/submissionReport")
//    public Result<?> submissionReport(@RequestBody submissionReportRequest request) {
//        return Result.OK(recordService.submissionReport(request));
//    }
//
//    /**
//     * 获取原始记录中对应的表单中的提交状态
//     *
//     * @return ResponseData
//     */
//    @GetMapping("/reportSubmission")
//    public Result<?> reportSubmission(@RequestParam String sampleId) {
//        return Result.OK(recordService.reportSubmission(sampleId));
//    }
//
//    /**
//     * 保存表单设备数据
//     *
//     * @return ResponseData
//     */
//    @GetMapping("/datatimeExit")
//    public Result<?> datatimeExit(@RequestParam String datatime, @RequestParam String sbid) {
//        List<String> maps = syFormBaseMapper.datatimeExit(sbid);
//        maps.forEach(n -> {
//            if (datatime.equals(n)) {
//                throw new BaseException("您当前选择的设备在交叉时间内用于其他样品试验，请确认是否继续使用该设备。");
//            }
//        });
//        return Result.OK();
//    }
//
//    /**
//     * 附件形式的提交原始记录
//     *
//     * @return ResponseData
//     */
//    @GetMapping("/getRecordFile")
//    public Result<?> getRecordFile(@RequestParam String sampleId) {
//        return Result.OK(recordService.getRecordFile(sampleId));
//    }
//    /**
//     * 表单台账配置报告对应字段1
//     *
//     * @return ResponseData
//     */
//    @GetMapping("/getYpmcByJlbbh")
//    public Result<?> getYpmcByJlbbh(@RequestParam String jlbid) {
//        return Result.OK(recordService.getYpmcByJlbbh(jlbid));
//    }
//        /**
//     * 表单台账配置报告对应字段1
//     *
//     * @return ResponseData
//     */
//    @GetMapping("/getKeyByYpmc")
//    public Result<?> getKeyByYpmc(@RequestParam String ypmc) {
//        return Result.OK(recordService.getKeyByYpmc(ypmc));
//    }
}
