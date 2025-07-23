package com.trtm.sy.cacl.controller;

import com.trtm.sy.cacl.model.SyCalcEquation;
import com.trtm.sy.cacl.service.SyCalcEquationService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(tags = "试验计算公式配置")
@RestController
@Slf4j
@RequestMapping("/SyCalcEquation")
public class CalcController extends JeecgController<SyCalcEquation, SyCalcEquationService> {

    @Autowired
    private SyCalcEquationService calcEquationService;


    @PostMapping("/saveSyCalcEquation")
    public Result<?> saveSyCalcEquation(@RequestBody List<SyCalcEquation> calcEquation) {
        calcEquationService.calcEquationService(calcEquation);
        return Result.OK("保存成功");
    }


}
