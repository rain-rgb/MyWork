package com.trtm.sy.sylxqxgl.controller;

import com.trtm.sy.sylxqxgl.entity.SyDpsYyUserAsTestItemType;
import com.trtm.sy.sylxqxgl.entity.SyJlbResponse;
import com.trtm.sy.sylxqxgl.entity.SyLxResponse;
import com.trtm.sy.sylxqxgl.service.SyDpsYyUserAsTestItemTypeService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "sy_dps_yy_userastestitemtype")
@RestController
@RequestMapping("/sylxqxgl/")
@Slf4j
public class SyDpsYyUserAsTestItemTypeController extends JeecgController<SyDpsYyUserAsTestItemType, SyDpsYyUserAsTestItemTypeService> {

    @Autowired
    private SyDpsYyUserAsTestItemTypeService service;

    /**
     * 保存对应的角色关联试验类型和检测参数
     * @param entity
     * @return
     */
    @PostMapping("saveRoleSyLx")
    public Result<?> saveRoleSyLx(@RequestBody List<SyDpsYyUserAsTestItemType> entity) {
        service.saveRoleSyLx(entity);
        return Result.OK("保存成功");
    }

    @PostMapping("saveSyLx")
    public Result<?> saveSyLx(@RequestBody SyDpsYyUserAsTestItemType entity) {
        service.saveSyLx(entity);
        return Result.OK("保存成功");
    }

    @PostMapping("saveParameter")
    public Result<?> saveParameter(@RequestBody SyDpsYyUserAsTestItemType entity) {
        service.saveParameter(entity);
        return Result.OK("保存成功");
    }

    /**
     * 查找所有的实验类型进行
     * @return
     */
    @GetMapping("getSyLxData")
    public Result<?> getSyLxData() {
        List<SyLxResponse> list = service.getSyLxData();
        return Result.OK(list);
    }


    /**
     *  查询titCode根据角色id
     */
    @GetMapping("getSyLxByRoleId")
    public Result<?> getSyLxByRoleId(@RequestParam String roleId) {
        List<String> list = service.getSyLxByRoleId(roleId);
        return Result.OK(list);
    }


    /**
     * 查找所有的实验类型进行
     * @return
     */
    @GetMapping("getSyLxDataByTitCode")
    public Result<?> getSyLxDataByTitCode(@RequestParam String titCode) {
        List<SyJlbResponse> list = service.getSyLxDataByTitCode(titCode);
        return Result.OK(list);
    }

    /**
     *  查询titCode根据角色id
     */
    @GetMapping("getSyLxJlbByRoleId")
    public Result<?> getSyLxJlbByRoleId(@RequestParam String roleId, @RequestParam String titCode) {
        List<String> list = service.getSyLxJlbByRoleId(roleId, titCode);
        return Result.OK(list);
    }


}
