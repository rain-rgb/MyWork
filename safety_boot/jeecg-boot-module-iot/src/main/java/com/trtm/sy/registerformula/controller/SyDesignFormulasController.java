package com.trtm.sy.registerformula.controller;


import com.trtm.sy.registerformula.model.SyDesignFormulas;
import com.trtm.sy.registerformula.model.SyDesignFormulasRequest;
import com.trtm.sy.registerformula.service.SyDesignFormulasService;
import io.swagger.annotations.Api;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(tags = "sy_design_formulas")
@RestController
@RequestMapping("/sy/syDesignFormulas/")
public class SyDesignFormulasController extends JeecgController<SyDesignFormulas, SyDesignFormulasService> {

    @Autowired
    private SyDesignFormulasService designFormulasService;


    @PostMapping("saveCalc")
    public Result<?> saveCalc(@RequestBody SyDesignFormulasRequest request) {
        designFormulasService.saveCalc(request);
        return Result.OK("保存成功");
    }


    @PostMapping("/calcFormulas/{tableNumber}")
    public Result<?> calcFormulas(@PathVariable String tableNumber, @RequestBody HashMap<String, Object> map) {
        Map<String, Object> result = designFormulasService.calcFormulas(tableNumber, map);
        return Result.OK(result);
    }

    @GetMapping("getCalc")
    public Result<?> getCalc(@RequestParam String tableNumber) {
        List<SyDesignFormulas> result = designFormulasService.getCalc(tableNumber);
        return Result.OK(result);
    }
}
