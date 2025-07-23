package com.trtm.sy.tableform.controller;

import com.trtm.sy.sylxdps.entity.SyDpsJcTestitemtype;
import com.trtm.sy.tableform.model.TableForm;
import com.trtm.sy.tableform.service.TableFormService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(tags = "试验记录表")
@RestController
@Slf4j
@RequestMapping("/tableForm")
public class TableFormController extends JeecgController<TableForm, TableFormService> {

    @Autowired
    private TableFormService formService;

    @GetMapping("/getFormList")
    public Result<?> getFormList(@RequestParam String orgCode) {
//        formService.getFormList(orgCode);
        return null;
    }

    @GetMapping("/getExperimentType")
    public Result<?> getExperimentType(@RequestParam String roleId) {
        List<SyDpsJcTestitemtype> res = formService.getExperimentType(roleId);
        return Result.OK(res);
    }

    @GetMapping("/getExperimentChridList")
    public Result<?> getExperimentChridList(@RequestParam String roleId,@RequestParam String titcode) {
        List<TableForm> res = formService.getExperimentChridList(roleId,titcode);
        return Result.OK(res);
    }

}
