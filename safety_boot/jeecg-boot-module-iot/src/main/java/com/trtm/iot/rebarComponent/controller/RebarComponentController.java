package com.trtm.iot.rebarComponent.controller;

import java.util.*;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import com.trtm.iot.rebarComponent.entity.ComponentVo;
import com.trtm.iot.rebarComponent.vo.MaterialVo;
import com.trtm.iot.rebarComponentMaterial.entity.RebarComponentMaterial;
import com.trtm.iot.rebarComponentMaterial.service.IRebarComponentMaterialService;

import com.trtm.iot.rebarComponentTask.entity.RebarComponentTask;
import com.trtm.iot.rebarComponentTask.service.IRebarComponentTaskService;
import com.trtm.iot.wzcailiaonamedict.service.IWzcailiaonamedictService;
import com.trtm.iot.wzcailiaonamedictman.entity.WzcailiaonamedictMan;
import com.trtm.iot.wzcailiaonamedictman.service.IWzcailiaonamedictManService;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import com.trtm.iot.rebarComponent.entity.RebarComponent;
import com.trtm.iot.rebarComponent.service.IRebarComponentService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.util.oConvertUtils;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

/**
 * @Description: rebar_component
 * @Author: jeecg-boot
 * @Date: 2023-06-15
 * @Version: V1.0
 */
@Api(tags = "rebar_component")
@RestController
@RequestMapping("/rebarComponent/rebarComponent")
@Slf4j
public class RebarComponentController extends JeecgController<RebarComponent, IRebarComponentService> {
    @Autowired
    private IRebarComponentService rebarComponentService;
    @Autowired
    private IWzcailiaonamedictService wzcailiaonamedictService;
    @Autowired
    private IWzcailiaonamedictManService wzcailiaonamedictManService;
    @Autowired
    private IRebarComponentMaterialService rebarComponentMaterialService;
    @Autowired
    private IRebarComponentTaskService rebarComponentTaskService;
    @Value("${jeecg.path.upload}")
    private String upLoadPath;
    /**
     * 分页列表查询
     *
     * @param rebarComponent
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "rebar_component-分页列表查询")
    @ApiOperation(value = "rebar_component-分页列表查询", notes = "rebar_component-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(RebarComponent rebarComponent,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   String sys_depart_orgcode,
                                   String sys_depart_orgcodes,
                                   HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户信息
        if (sys_depart_orgcode != null && sys_depart_orgcode.length() != 0) {  //如果想要全局组织机构控制所显示的数据要加上这个
            rebarComponent.setOrgCode(sys_depart_orgcode + "*");
        } else {
            rebarComponent.setOrgCode("*" + sys_depart_orgcode + "*");
        }
        String componentName = rebarComponent.getComponentName();
        if (componentName != null) {
            rebarComponent.setComponentName("*" + componentName + "*");
        }
        rebarComponent.setIsDeleted(0);
        if (rebarComponent.getOrgCodes() != null && rebarComponent.getOrgCodes().length() != 0) {
            rebarComponent.setOrgCodes(rebarComponent.getOrgCodes() + "*");
        }
        QueryWrapper<RebarComponent> queryWrapper = QueryGenerator.initQueryWrapper(rebarComponent, req.getParameterMap());
        if (sys_depart_orgcodes != null && sys_depart_orgcodes.length() != 0) {
//            queryWrapper.isNotNull("org_codes");
//            queryWrapper.ne("org_codes",sys_depart_orgcodes);
            queryWrapper.eq("org_codes","1");
        }
        Page<RebarComponent> page = new Page<RebarComponent>(pageNo, pageSize);
        IPage<RebarComponent> pageList = rebarComponentService.page(page, queryWrapper);
        return Result.OK(pageList);
    }


    /**
     * 构件生产统计分页列表查询
     *
     * @param componentVo
     * @param pageNo
     * @param pageSize
     * @param componentVo
     * @return
     */
    @AutoLog(value = "构件生产统计分页列表查询")
    @ApiOperation(value = "构件生产统计分页列表查询", notes = "构件生产统计分页列表查询")
    @GetMapping(value = "/componentList")
    public Result<?> queryComponentPageList(
                                    ComponentVo componentVo,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   String sys_depart_orgcode
                                     ) {
        String sys_depart_orgcodes = "1";
        List<ComponentVo> componentVos = rebarComponentService.getComponentList(componentVo,pageNo,pageSize,sys_depart_orgcode,sys_depart_orgcodes);
        Page<ComponentVo> result = new Page<>(pageNo, pageSize);
        result.setTotal(componentVos.size());
        result.setRecords(componentVos);
        return Result.OK(result);
    }

    /**
     * 原材料用量统计分页列表查询
     *
     * @param materialVo
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "原材料用量统计分页列表查询")
    @ApiOperation(value = "原材料用量统计分页列表查询", notes = "原材料用量统计分页列表查询")
    @GetMapping(value = "/materialList")
    public Result<?> queryMaterialList(
//                                    RebarComponent rebarComponent,
            MaterialVo materialVo,
            @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
            @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
            String sys_depart_orgcode,
            HttpServletRequest req) {
        //获取有构件对应的材料
        List<MaterialVo> materialVos = wzcailiaonamedictManService.getMaterialList(materialVo,pageNo,pageSize,sys_depart_orgcode);
        Page<Object> page = new Page<>(pageNo, pageSize);
        Page<MaterialVo> result =
                new Page<>(pageNo, pageSize, page.getTotal());
        result.setTotal(materialVos.size());
        result.setRecords(materialVos);
        return Result.OK(result);
    }
    /**
     * 添加
     *
     * @param rebarComponent
     * @return
     */
    @AutoLog(value = "rebar_component-添加")
    @ApiOperation(value = "rebar_component-添加", notes = "rebar_component-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody RebarComponent rebarComponent) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户信息
        rebarComponent.setOrgCode(loginUser.getOrgCode());
        rebarComponent.setOrgCodes("1");
        rebarComponent.setComponentNumber(1);
//        rebarComponent.setWeight(0.0);
        if (rebarComponent.getOrgCodes()==null){
            rebarComponent.setOrgCodes("");
        }
        boolean b = rebarComponentService.addRebarComponent(rebarComponent);
        if (b){
            return Result.OK("添加成功！");
        }else {
            return Result.error("添加失败！");
        }

    }

    /**
     * 编辑
     *
     * @param rebarComponent
     * @return
     */
    @AutoLog(value = "rebar_component-编辑")
    @ApiOperation(value = "rebar_component-编辑", notes = "rebar_component-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody RebarComponent rebarComponent) {
        //编辑构件主表中构件数量
        rebarComponentService.updateById(rebarComponent);
//        //编辑构件和任务中间表的构件数量
//        rebarComponentTaskService.
//
//        RebarComponentTask componentTask = new RebarComponentTask();
//        BeanUtils.copyProperties(rebarComponent,componentTask);
//        rebarComponentTaskService.updateById(componentTask);
        return Result.OK("编辑成功!");
    }
    /**
     *   添加分部分项中的构件
     *
     * @param componentVo
     * @return
     */
    @AutoLog(value = "添加分部分项中的构件")
    @ApiOperation(value="添加分部分项中的构件", notes="添加分部分项中的构件")
    @PostMapping(value = "/addDepartComponent")
    public Result<?> addTaskCheck(@RequestBody ComponentVo componentVo) {
        String orgCode = componentVo.getOrgCode();

        boolean b = rebarComponentService.addTaskCheck(componentVo);
        if (b){
            return Result.OK("添加成功！");
        }{
            return Result.error("添加失败！");
        }
    }
    /**
     * 通过构件id和材料id编辑构件列表的材料
     *
     * @param
     * @return
     */
    @AutoLog(value = "通过构件id和材料id编辑构件列表的材料")
    @ApiOperation(value = "通过构件id和材料id编辑构件列表的材料", notes = "通过构件id和材料id编辑构件列表的材料")
    @PutMapping(value = "/editByComponentIdAndMaterial")
    @Transactional
    public Result<?> editByComponentIdAndMaterial(String componentId,
                                                  @RequestBody MaterialVo materialVo) {
        RebarComponentMaterial material = new RebarComponentMaterial();
        //先根据构件和材料编号查出
        RebarComponentMaterial componentMaterial = rebarComponentMaterialService.getComponentMaterial(materialVo.getComponentId(), materialVo.getMaterialId());
        if (componentMaterial!=null){
            String id = componentMaterial.getId();
            boolean b = rebarComponentMaterialService.removeById(id);

            material.setComponentId(materialVo.getComponentId());
            material.setId(id);
            material.setMaterialId(componentMaterial.getMaterialId());
            material.setMaterialNumber(materialVo.getMaterialNumber());
            material.setMaterialName(materialVo.getMaterialName());
            material.setMaterialType(materialVo.getGuigexinghao());
        }else {
            return Result.error("没找到相关数据!");
        }

        boolean save = rebarComponentMaterialService.save(material);
        if (save){
            return Result.OK("编辑成功!");
        }else {
            return Result.error("编辑失败!");
        }
//		rebarComponentService.removeById(id);
    }
    /**
     * 通过构件id和任务id编辑任务列表的构件
     *
     * @param
     * @return
     */
    @AutoLog(value = "通过构件id和任务id编辑任务列表的构件")
    @ApiOperation(value = "通过构件id和任务id编辑任务列表的构件", notes = "通过构件id和任务id编辑任务列表的构件")
    @PutMapping(value = "/editByComponentIdAndTaskId")
    @Transactional
    public Result<?> editByComponentIdAndTaskId(@RequestBody ComponentVo componentVo) {
        RebarComponentTask task = new RebarComponentTask();
        //先根据构件和材料编号查出
        RebarComponentTask componentTask = rebarComponentTaskService.getComponentTask(componentVo.getComponentId(), componentVo.getTaskId());
        if (componentTask!=null){
            String id = componentTask.getId();
            boolean b = rebarComponentTaskService.removeById(id);

            task.setComponentId(componentVo.getComponentId());
            task.setId(id);
            task.setTaskId(componentTask.getTaskId());
            task.setComponentNumber(componentVo.getComponentNumber());
        }else {
            return Result.error("没找到相关数据!");
        }

        boolean save = rebarComponentTaskService.save(task);
        if (save){
            return Result.OK("编辑成功!");
        }else {
            return Result.error("编辑失败!");
        }
//		rebarComponentService.removeById(id);
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "rebar_component-通过id删除")
    @ApiOperation(value = "rebar_component-通过id删除", notes = "rebar_component-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        boolean b = rebarComponentService.deletedById(id);
        if (b){
            return Result.OK("删除成功!");
        }else {
            return Result.error("删除失败!");
        }
//		rebarComponentService.removeById(id);
    }
    /**
     * 通过构件id和材料id删除构件列表的材料
     *
     * @param
     * @return
     */
    @AutoLog(value = "通过构件id和材料id删除构件列表的材料")
    @ApiOperation(value = "通过构件id和材料id删除构件列表的材料", notes = "通过构件id和材料id删除构件列表的材料")
    @DeleteMapping(value = "/deleteByComponentIdAndMaterialId")
    public Result<?> deleteByComponentIdAndMaterial(@RequestParam(name = "componentId", required = true) String componentId,
                                                    @RequestParam(name = "materialId", required = true) String materialId) {
        boolean b = rebarComponentMaterialService.deleteByComponentIdAndMaterial(componentId,materialId);
        if (b){
            return Result.OK("删除成功!");
        }else {
            return Result.error("删除失败!");
        }
//		rebarComponentService.removeById(id);
    }
    /**
     * 通过构件id和任务id删除任务列表的构件
     *
     * @param
     * @return
     */
    @AutoLog(value = "通过构件id和任务id删除任务列表的构件")
    @ApiOperation(value = "通过构件id和任务id删除任务列表的构件", notes = "通过构件id和任务id删除任务列表的构件")
    @DeleteMapping(value = "/deleteByComponentIdAndTaskId")
    public Result<?> deleteByComponentIdAndTaskId(@RequestParam(name = "componentId", required = true) String componentId,
                                                    @RequestParam(name = "taskId", required = true) String taskId) {
        boolean b = rebarComponentTaskService.deleteByComponentIdAndTaskId(componentId,taskId);
        if (b){
            return Result.OK("删除成功!");
        }else {
            return Result.error("删除失败!");
        }
//		rebarComponentService.removeById(id);
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "rebar_component-批量删除")
    @ApiOperation(value = "rebar_component-批量删除", notes = "rebar_component-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.rebarComponentService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "rebar_component-通过id查询")
    @ApiOperation(value = "rebar_component-通过id查询", notes = "rebar_component-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        RebarComponent rebarComponent = rebarComponentService.getById(id);
        if (rebarComponent == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(rebarComponent);
    }

    /**
     * 通过构件id查询构件所需材料
     *
     * @param id
     * @return
     */
    @AutoLog(value = "通过构件id查询构件所需材料")
    @ApiOperation(value = "通过构件id查询构件所需材料", notes = "通过构件id查询构件所需材料")
    @GetMapping(value = "/queryMaterialByComponentId")
//	 public Result<?> queryMaterialByComponentId(@RequestParam(name="componentId",required=true) String id) {
    public Result<?> queryMaterialByComponentId(@RequestParam(name = "componentId", required = true) String id,
                                                @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                HttpServletRequest req) {
//		 List<WzcailiaonamedictMan> wzcailiaonamedictMEN = wzcailiaonamedictManService.queryMaterialByComponentId(id);
        List<MaterialVo> materialVos = wzcailiaonamedictManService.queryMaterialByComponentId(id);
        Page<Object> page = new Page<>(pageNo, pageSize);
//		 List<Wzcailiaonamedict> collect = wzcailiaonamedict.stream().skip((pageNo - 1) * pageSize).limit(pageSize).collect(Collectors.toList());
        if (materialVos == null) {
            return Result.error("未找到对应数据");
        }
        Page<MaterialVo> result =
                new Page<>(pageNo, pageSize, page.getTotal());
        result.setTotal(materialVos.size());
        result.setRecords(materialVos);
        return Result.OK(result);
    }

    /**
     * 添加构件所需材料下拉框
     *
     * @param nodeType
     * @return
     */
    @AutoLog(value = "添加构件所需材料下拉框")
    @ApiOperation(value = "添加构件所需材料下拉框", notes = "添加构件所需材料下拉框")
    @GetMapping(value = "/queryMaterialByNodeType")
    public Result<?> queryMaterialByNodeType(@RequestParam(name = "nodeType", required = true) String nodeType) {

        List<WzcailiaonamedictMan> wzcailiaonamedictMEN = wzcailiaonamedictManService.queryMaterialByNodeType(nodeType);

        if (wzcailiaonamedictMEN == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(wzcailiaonamedictMEN);
    }

    /**
     * 添加构件所需材料
     *
     * @param rebarComponentMaterial
     * @return
     */
    @AutoLog(value = "添加构件所需材料")
    @ApiOperation(value = "添加构件所需材料", notes = "添加构件所需材料")
    @PostMapping(value = "/addMaterialToComponentId")
    public Result<?> addMaterialToComponentId(@RequestBody RebarComponentMaterial rebarComponentMaterial) {
        String componentId = rebarComponentMaterial.getComponentId();
        String materialId = rebarComponentMaterial.getMaterialId();
        String materialNumber = rebarComponentMaterial.getMaterialNumber();
        String materialName = rebarComponentMaterial.getMaterialName();//材料名称
        String materialType = rebarComponentMaterial.getMaterialType();//规格型号

        RebarComponentMaterial rebarComponentMaterial1 = new RebarComponentMaterial();
        rebarComponentMaterial1.setComponentId(componentId);
        //需要先判断之前库里有没有这个材料,如果有的话就累加数量,没有的话直接加
        rebarComponentMaterial1.setMaterialId(materialId);
        rebarComponentMaterial1.setMaterialName(materialName);
        rebarComponentMaterial1.setMaterialType(materialType);
        RebarComponentMaterial material= rebarComponentMaterialService.getComponentMaterial(componentId, materialId);
        if(material==null){
            rebarComponentMaterial1.setId(UUID.randomUUID().toString().replace("-", ""));
            rebarComponentMaterial1.setMaterialNumber(materialNumber);
        }else {
//            RebarComponentMaterial material= rebarComponentMaterialService.getComponentMaterial(componentId, materialId);
            Integer number2=Integer.valueOf(material.getMaterialNumber())+Integer.valueOf(materialNumber);
            String s = number2.toString();
            rebarComponentMaterial1.setMaterialNumber(s);
            rebarComponentMaterial1.setId(material.getId());
            boolean b = rebarComponentMaterialService.removeById(material.getId());
        }

        boolean save = rebarComponentMaterialService.save(rebarComponentMaterial1);

        //根据材料重量配置构件
//        extracted(componentId, materialId, rebarComponentMaterial1);
        if (save) {
            return Result.OK("添加成功");
        }
        return Result.error("添加失败");
    }

    private void extracted(String componentId, String materialId, RebarComponentMaterial rebarComponentMaterial1) {
        //拿着材料的数量和重量,去配置构件的重量
        //每添加一个材料,就给对应的构件累加重量
        //获取材料的数量
        String materialNumber1 = rebarComponentMaterial1.getMaterialNumber();
        //单个材料重量
        WzcailiaonamedictMan queryselectone1 = wzcailiaonamedictManService.queryselectone1(materialId);
        Double weight = queryselectone1.getWeight();
        //构件对应的单个材料重量
        if(weight==null){
            weight = Double.valueOf(1);
        }
        double v = Double.parseDouble(materialNumber1) * weight;
        String orgCodes = "1";
        RebarComponent rebarComponent = rebarComponentService.getBycomponentId(componentId, orgCodes);
        rebarComponent.setWeight(rebarComponent.getWeight()+v);
        rebarComponentService.updateById(rebarComponent);
    }


    /**
     * 导出excel
     *
     * @param request
     * @param rebarComponent
     */
    @AutoLog(value = "导出excel")
    @ApiOperation(value = "导出excel", notes = "导出excel")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, RebarComponent rebarComponent) {
        return super.exportXls(request, rebarComponent, RebarComponent.class, "钢筋构件");
    }
    /**
     * 导出构件统计excel
     * @param
     * @param
     * @param
     */
    @AutoLog(value = "导出构件统计excel")
    @ApiOperation(value = "导出构件统计excel", notes = "导出构件统计excel")
    @RequestMapping(value = "/componentExportXls")
    public ModelAndView componentExportXls(HttpServletRequest request,
                                           ComponentVo componentVo,
                                           @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                           @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                           String sys_depart_orgcode) {
        // Step.1 组装查询条件
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        String sys_depart_orgcodes = "1";
        // Step.2 获取导出数据
        List<ComponentVo> componentVos = rebarComponentService.getComponentList(componentVo,pageNo,pageSize,sys_depart_orgcode,sys_depart_orgcodes);

        List<ComponentVo> exportList = null;

        // 过滤选中数据
        String selections = request.getParameter("selections");
        if (oConvertUtils.isNotEmpty(selections)) {
            List<String> selectionList = Arrays.asList(selections.split(","));
            exportList = componentVos.stream().filter(item -> selectionList.contains(item.getId())).collect(Collectors.toList());
        } else {
            exportList = componentVos;
        }

        // Step.3 AutoPoi 导出Excel
        ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
        mv.addObject(NormalExcelConstants.FILE_NAME, "构件统计"); //此处设置的filename无效 ,前端会重更新设置一下
        mv.addObject(NormalExcelConstants.CLASS, ComponentVo.class);
        //update-begin--Author:liusq  Date:20210126 for：图片导出报错，ImageBasePath未设置--------------------
        ExportParams exportParams=new ExportParams("构件统计报表", "导出人:" + sysUser.getRealname(), "构件统计报表");
        exportParams.setImageBasePath(upLoadPath);
        //update-end--Author:liusq  Date:20210126 for：图片导出报错，ImageBasePath未设置----------------------
        mv.addObject(NormalExcelConstants.PARAMS,exportParams);
        mv.addObject(NormalExcelConstants.DATA_LIST, exportList);
        return mv;
    }
    /**
     * 导出材料统计excel
     * @param
     * @param
     * @param
     */
    @AutoLog(value = "导出材料统计excel")
    @ApiOperation(value = "导出材料统计excel", notes = "导出材料统计excel")
    @RequestMapping(value = "/materialExportXls")
    public ModelAndView materialExportXls(HttpServletRequest request,
                                           MaterialVo materialVo,
                                           @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                           @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                           String sys_depart_orgcode) {
        // Step.1 组装查询条件
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

        // Step.2 获取导出数据
        List<MaterialVo> materialVos = wzcailiaonamedictManService.getMaterialList(materialVo,pageNo,pageSize,sys_depart_orgcode);

        List<MaterialVo> exportList = null;

        // 过滤选中数据
        String selections = request.getParameter("selections");
        if (oConvertUtils.isNotEmpty(selections)) {
            List<String> selectionList = Arrays.asList(selections.split(","));
            exportList = materialVos.stream().filter(item -> selectionList.contains(item.getId())).collect(Collectors.toList());
        } else {
            exportList = materialVos;
        }

        // Step.3 AutoPoi 导出Excel
        ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
        mv.addObject(NormalExcelConstants.FILE_NAME, "材料统计"); //此处设置的filename无效 ,前端会重更新设置一下
        mv.addObject(NormalExcelConstants.CLASS, MaterialVo.class);
        //update-begin--Author:liusq  Date:20210126 for：图片导出报错，ImageBasePath未设置--------------------
        ExportParams exportParams=new ExportParams("材料统计报表", "导出人:" + sysUser.getRealname(), "材料统计报表");
        exportParams.setImageBasePath(upLoadPath);
        //update-end--Author:liusq  Date:20210126 for：图片导出报错，ImageBasePath未设置----------------------
        mv.addObject(NormalExcelConstants.PARAMS,exportParams);
        mv.addObject(NormalExcelConstants.DATA_LIST, exportList);
        return mv;
    }

    /**
     * 通过excel导入数据
     *
     * @param request
     * @param response
     * @return
     */
    @AutoLog(value = "通过excel导入数据")
    @ApiOperation(value = "通过excel导入数据", notes = "通过excel导入数据")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, RebarComponent.class);
    }

}
