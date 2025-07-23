package com.trtm.iot.rebarTaskChecklist.controller;

import java.util.*;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.trtm.iot.rebarComponent.entity.RebarComponent;
import com.trtm.iot.rebarComponent.service.IRebarComponentService;
import com.trtm.iot.rebarComponent.entity.ComponentVo;
import com.trtm.iot.rebarComponent.vo.MaterialVo;
import com.trtm.iot.rebarComponent.vo.RebarComponentTaskVo;
import com.trtm.iot.rebarComponentMaterial.entity.RebarComponentMaterial;
import com.trtm.iot.rebarComponentMaterial.service.IRebarComponentMaterialService;
import com.trtm.iot.rebarComponentTask.entity.RebarComponentTask;
import com.trtm.iot.rebarComponentTask.service.IRebarComponentTaskService;
import com.trtm.iot.rebarFactory.service.IRebarFactoryService;
import com.trtm.iot.rebarTaskChecklist.entity.RebarTaskCheckVolist;
import com.trtm.iot.rebarTaskChecklist.entity.TaskCheckVo;
import com.trtm.iot.rebarWzcailiaonamedictMan.entity.RebarWzcailiaonamedictMan;
import com.trtm.iot.rebarWzcailiaonamedictMan.service.IRebarWzcailiaonamedictManService;
import com.trtm.iot.wzcailiaonamedictman.service.IWzcailiaonamedictManService;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import com.trtm.iot.rebarTaskChecklist.entity.RebarTaskChecklist;
import com.trtm.iot.rebarTaskChecklist.service.IRebarTaskChecklistService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.util.oConvertUtils;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

 /**
 * @Description: rebar_task_checklist
 * @Author: jeecg-boot
 * @Date:   2023-06-15
 * @Version: V1.0
 */
@Api(tags="rebar_task_checklist")
@RestController
@RequestMapping("/rebarTaskChecklist/rebarTaskChecklist")
@Slf4j
public class RebarTaskChecklistController extends JeecgController<RebarTaskChecklist, IRebarTaskChecklistService> {
	@Autowired
	private IRebarTaskChecklistService rebarTaskChecklistService;
	@Autowired
	private IWzcailiaonamedictManService wzcailiaonamedictManService;
	@Autowired
	private IRebarComponentService rebarComponentService;
	@Autowired
	private IRebarComponentTaskService rebarComponentTaskService;
	@Autowired
	private IRebarWzcailiaonamedictManService rebarWzcailiaonamedictManService;
	 @Autowired
	private IRebarComponentMaterialService rebarComponentMaterialService;
	 @Autowired
	 private IRebarFactoryService rebarFactoryService;
	 @Value("${jeecg.path.upload}")
	 private String upLoadPath;
	
	/**
	 * 分页列表查询
	 *
	 * @param rebarTaskChecklist
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "构件任务管理-分页列表查询")
	@ApiOperation(value="构件任务管理-分页列表查询", notes="构件任务管理-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(RebarTaskChecklist rebarTaskChecklist,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   String sys_depart_orgcode,
								   HttpServletRequest req) {
		LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
		Integer a = 1;
		QueryWrapper<RebarTaskChecklist> queryWrapper = rebarTaskChecklistService.getRebarTaskChecklistQueryWrapper(rebarTaskChecklist, sys_depart_orgcode, req,a);
		Page<RebarTaskChecklist> page = new Page<RebarTaskChecklist>(pageNo, pageSize);
		IPage<RebarTaskChecklist> pageList = rebarTaskChecklistService.page(page, queryWrapper);
		return Result.OK(pageList);
	}

	 /**
	  * 构件验收出库
	  *
	  * @param rebarTaskChecklist
	  * @param pageNo
	  * @param pageSize
	  * @param req
	  * @return
	  */
	 @AutoLog(value = "构件验收出库-分页列表查询")
	 @ApiOperation(value="构件验收出库-分页列表查询", notes="构件验收出库-分页列表查询")
	 @GetMapping(value = "/list2")
	 public Result<?> queryPageList2(RebarTaskChecklist rebarTaskChecklist,
									@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									@RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									String sys_depart_orgcode,
//									String status1,
//									String status2,
									HttpServletRequest req) {
		 LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
		 Integer a = 2;
		 QueryWrapper<RebarTaskChecklist> queryWrapper = rebarTaskChecklistService.getRebarTaskChecklistQueryWrapper(rebarTaskChecklist, sys_depart_orgcode, req,a);
		 Page<RebarTaskChecklist> page = new Page<RebarTaskChecklist>(pageNo, pageSize);
		 IPage<RebarTaskChecklist> pageList = rebarTaskChecklistService.page(page, queryWrapper);
		 return Result.OK(pageList);
	 }

	 /**
	 *   添加
	 *
	 * @param rebarTaskChecklist
	 * @return
	 */
	@AutoLog(value = "rebar_task_checklist-添加")
	@ApiOperation(value="rebar_task_checklist-添加", notes="rebar_task_checklist-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody RebarTaskChecklist rebarTaskChecklist) {
		rebarTaskChecklistService.save(rebarTaskChecklist);
		return Result.OK("添加成功！");
	}
	 /**
	  *   添加任务和对应的构件
	  *
	  * @param taskCheckVo
	  * @return
	  */
	 @AutoLog(value = "添加任务和对应的构件-启动任务确定按钮")
	 @ApiOperation(value="添加任务和对应的构件", notes="添加任务和对应的构件")
	 @PostMapping(value = "/addTaskCheck")
	 public Result<?> addTaskCheck(@RequestBody TaskCheckVo taskCheckVo) {
		 LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户信息
//		 taskCheckVo.setCreatePerson(loginUser.getRealname());
		 taskCheckVo.setCreatePerson("loginUser.getRealname()");
		 boolean b = rebarTaskChecklistService.addTaskCheck(taskCheckVo);
		 if (b){
			 return Result.OK("添加成功！");
		 }{
			 return Result.error("添加失败！");
		 }
	 }

	/**
	 *  编辑
	 *
	 * @param rebarTaskChecklist
	 * @return
	 */
	@AutoLog(value = "出库编辑")
	@ApiOperation(value="出库按钮", notes="rebar_task_checklist-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody RebarTaskCheckVolist rebarTaskCheckVolist) {
		DateTime date = DateUtil.date();
		if (rebarTaskCheckVolist.getAcceptancePersonnel()!=null){
			rebarTaskCheckVolist.setAcceptanceTime(date);
		}
		if (rebarTaskCheckVolist.getOutboundPersonnel()!=null){
			rebarTaskCheckVolist.setOutboundTime(date);
		}
		RebarTaskChecklist rebarTaskChecklist = new RebarTaskChecklist();
		//原来的任务复制过去
		BeanUtils.copyProperties(rebarTaskCheckVolist,rebarTaskChecklist);

		boolean b = rebarTaskChecklistService.updateById(rebarTaskChecklist);
		if (rebarTaskCheckVolist.getStatus().equals("5")&& b){
			//验收成功后,统计材料数量
			//当前任务对应的所有构件
			List<RebarComponentTaskVo> rebarComponentTaskList = rebarTaskCheckVolist.getRebarComponentTaskList();
			//每种构件对应的材料
			for (RebarComponentTaskVo rebarComponentTaskVo : rebarComponentTaskList) {
				String componentId = rebarComponentTaskVo.getComponentId();
				String componentNumber = rebarComponentTaskVo.getComponentNumber();
				//根据构件编号获取材料使用情况
				List<MaterialVo> materialVos = wzcailiaonamedictManService.queryMaterialByComponentId(componentId);
				for (MaterialVo materialVo : materialVos) {
					Double weight = materialVo.getWeight();
					String materialId = materialVo.getMaterialId();
					String materialNumber = materialVo.getMaterialNumber();
					//材料总数量=材料数*构件数
					Double materialAllNumber = (Integer.valueOf(componentNumber).intValue())*(new Double(materialNumber));
					RebarWzcailiaonamedictMan rebarWzcailiaonamedictMan = new RebarWzcailiaonamedictMan();
					rebarWzcailiaonamedictMan.setNumber(materialAllNumber);//数量
					rebarWzcailiaonamedictMan.setCailiaoname(materialVo.getMaterialName());//材料名
					rebarWzcailiaonamedictMan.setCailiaono(materialVo.getMaterialId());//材料编号
//					rebarWzcailiaonamedictMan.setNodetype(materialVo.getNodeType());//材料类型
					rebarWzcailiaonamedictMan.setNodetype("12");//材料类型
					rebarWzcailiaonamedictMan.setGuigexinghao(materialVo.getGuigexinghao());//规格型号
					rebarWzcailiaonamedictMan.setGuid(materialVo.getGuid());//材料字典的唯一id
					rebarWzcailiaonamedictMan.setSysOrgCode(rebarComponentTaskVo.getOrgCode());//部门编码***
					rebarWzcailiaonamedictMan.setSysOrgCodes(rebarComponentTaskVo.getOrgCodes());//分部分项编码***
					rebarWzcailiaonamedictMan.setCreateTime(new Date());//创建时间
					rebarWzcailiaonamedictMan.setIsdel(0);//逻辑删除标记

					boolean save = rebarWzcailiaonamedictManService.save(rebarWzcailiaonamedictMan);
					System.out.println("save = " + save);
				}
				//根据任务编号和构件编号修改构件状态
				String taskId = rebarTaskCheckVolist.getTaskId();
				boolean edited = rebarComponentService.editComponentByTaskIdAndComponentId(taskId,componentId);

			}
		}
		else {

		}


		return Result.OK("编辑成功!");
	}
	 /**
	  * 通过id查询任务和对应的构件
	  *
	  * @param id
	  * @return
	  */
	 @AutoLog(value = "通过id查询任务和对应的构件")
	 @ApiOperation(value="通过id查询任务和对应的构件", notes="通过id查询任务和对应的构件")
	 @GetMapping(value = "/queryByTaskId")
	 public Result<?> queryByTaskId(@RequestParam(name="id",required=true) String id,
									String orgCodes) {
		 TaskCheckVo taskCheckVo = new TaskCheckVo();
		 RebarTaskChecklist rebarTaskChecklist = rebarTaskChecklistService.getById(id);
		 if(rebarTaskChecklist==null) {
			 return Result.error("未找到对应任务数据");
		 }
		 BeanUtils.copyProperties(rebarTaskChecklist,taskCheckVo);
		 String taskId = rebarTaskChecklist.getTaskId();
		 List<RebarComponentTask> rebarComponentTaskList= rebarComponentTaskService.getComponentTaskListByTaskId(taskId);
		 if(rebarComponentTaskList==null) {
			 return Result.error("未找到对应构件数据");
		 }
		 List<RebarComponentTaskVo> rebarComponentTaskVos = new ArrayList<>();
		 List<String> images = new ArrayList<>();
		 //集合里有构件id和构件数量
		 for (RebarComponentTask componentTask : rebarComponentTaskList) {
			 RebarComponentTaskVo rebarComponentTaskVo1 = new RebarComponentTaskVo();
		 	//拿构件编号去查构件详情
			 String componentId = componentTask.getComponentId();
			 RebarComponent rebarComponent= rebarComponentService.getBycomponentId(componentId,orgCodes);
			 rebarComponentTaskVo1.setComponentNumber(componentTask.getComponentNumber());

			 BeanUtils.copyProperties(rebarComponent,rebarComponentTaskVo1);
			 //任务加上构件的图片集合

			 rebarComponentTaskVos.add(rebarComponentTaskVo1);
		 }
		 taskCheckVo.setRebarComponentTaskList(rebarComponentTaskVos);

		 //设置上加工厂名称
		 String factoryId = rebarTaskChecklist.getFactoryId();
		 String factoryName = rebarFactoryService.getFactoryName(factoryId);
		 taskCheckVo.setFactoryName(factoryName);
		 return Result.OK(taskCheckVo);
	 }
	 /**
	  *  编辑任务和对应的构件
	  *
	  * @param taskCheckVo
	  * @return
	  */
	 @AutoLog(value = "编辑任务和对应的构件")
	 @ApiOperation(value="编辑任务和对应的构件", notes="编辑任务和对应的构件")
	 @PutMapping(value = "/editTaskAndComponent")
	 public Result<?> editTaskAndComponent(@RequestBody TaskCheckVo taskCheckVo) {
	 	//先删除任务和对应的构件
		 boolean b = rebarTaskChecklistService.removeById(taskCheckVo.getId());
		 //删除任务对应的构件
		 for (RebarComponentTaskVo componentTask : taskCheckVo.getRebarComponentTaskList()) {
			 rebarComponentTaskService.deleteByTaskId(taskCheckVo.getTaskId());
		 }
		 rebarTaskChecklistService.addTaskCheck(taskCheckVo);
		 if (!taskCheckVo.getStatus().equals("0")||!taskCheckVo.getStatus().equals("1")||!taskCheckVo.getStatus().equals("2")){

		 }
		 return Result.OK("编辑成功!");
	 }
	 /**
	  *   删除任务和对应的构件
	  *
	  * @param id
	  * @return
	  */
	 @AutoLog(value = "删除任务和对应的构件")
	 @ApiOperation(value="删除任务和对应的构件", notes="删除任务和对应的构件")
	 @DeleteMapping(value = "/deleteTaskAndComponent")
	 @Transactional
	 public Result<?> deleteTaskAndComponent(@RequestParam(name="id",required=true) String id) {
		 RebarTaskChecklist taskChecklist = rebarTaskChecklistService.getById(id);
		 //先删除任务(逻辑删除)
		 rebarTaskChecklistService.deleteTaskById(id);
//		 rebarTaskChecklistService.removeById(id);
		 //删除任务对应的构件
		 String taskId = taskChecklist.getTaskId();
		 boolean b = rebarComponentTaskService.deleteByTaskId(taskId);
//		 for (RebarComponentTask componentTask : taskCheckVo.getRebarComponentTaskList()) {
//			 rebarComponentTaskService.removeById(componentTask.getTaskId());
//		 }
		 if (b) {
			 return Result.OK("删除成功");
		 }
		 return Result.error("删除失败");
	 }
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "rebar_task_checklist-通过id删除")
	@ApiOperation(value="rebar_task_checklist-通过id删除", notes="rebar_task_checklist-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		rebarTaskChecklistService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "rebar_task_checklist-批量删除")
	@ApiOperation(value="rebar_task_checklist-批量删除", notes="rebar_task_checklist-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.rebarTaskChecklistService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "rebar_task_checklist-通过id查询")
	@ApiOperation(value="rebar_task_checklist-通过id查询", notes="rebar_task_checklist-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		RebarTaskChecklist rebarTaskChecklist = rebarTaskChecklistService.getById(id);
		if(rebarTaskChecklist==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(rebarTaskChecklist);
	}

	 /**
	  * 任务界面领料按钮显示所需材料
	  * @param taskId
	  * @param req
	  * @return
	  */
	@AutoLog(value = "任务界面领料按钮显示所需材料")
	@ApiOperation(value = "任务界面领料按钮显示所需材料",notes = "通过任务id查询对应的构件")
	@GetMapping(value = "/queryMaterialByTask")
	public Result<?> queryMaterialByTask(@RequestParam(name="taskId",required=true) String taskId,
//	public Result<?> queryMaterialByTask(String taskId,
//										 @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
//										 @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
										 HttpServletRequest req){
//		通过任务id查询对应的构件
		if (taskId.equals("0")){
			return Result.OK(new ArrayList<>());
		}
		List<ComponentVo> componentVos = rebarComponentService.queryComponentByTaskId(taskId);
		if(componentVos==null) {
			return Result.OK(new ArrayList<>());
		}
//		List<MaterialVo> list1 = new ArrayList<>();
		List<RebarComponentMaterial> list = new ArrayList<>();
		for (ComponentVo componentVo : componentVos) {
			//		通过构件id查询构件所需材料
//			List<MaterialVo> materialVos = wzcailiaonamedictManService.queryMaterialByComponentId(componentVo.getComponentId());
			List<RebarComponentMaterial> rebarComponentMaterials = rebarComponentMaterialService.queryMaterialByComponentId(componentVo.getComponentId());
			if(rebarComponentMaterials==null) {
				return Result.error("未找到对应构件原料数据");
			}
//			for (MaterialVo materialVo : materialVos) {
//				materialVo.setId(UUID.randomUUID().toString());
//				Integer componentNumber =Integer.valueOf(componentVo.getComponentNumber()) ;
//				Integer materialNumber = Integer.valueOf(materialVo.getMaterialNumber());
//				Integer number=(componentNumber*materialNumber);
//				materialVo.setMaterialNumber(number.toString());
//				materialVo.setTaskId(taskId);
//				list.add(materialVo);
//			}
			for (RebarComponentMaterial componentMaterial : rebarComponentMaterials) {
				componentMaterial.setOrgCodes(componentVo.getOrgCodes());
				componentMaterial.setOrgCode(componentVo.getOrgCode());
				componentMaterial.setTaskId(taskId);
				list.add(componentMaterial);
			}
//			list.addAll(materialVos);
		}

		return Result.OK(list);
	}
	 /**
	  * 通过任务id查询对应的构件
	  *
	  * @param taskId
	  * @return
	  */
	 @AutoLog(value = "通过任务id查询对应的构件")
	 @ApiOperation(value="通过任务id查询对应的构件", notes="通过任务id查询对应的构件")
	 @GetMapping(value = "/queryComponentByTaskId")
//	 public Result<?> queryMaterialByComponentId(@RequestParam(name="componentId",required=true) String id) {
	 public Result<?> queryComponentByTaskId(@RequestParam(name="taskId",required=true) String taskId,
												 @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
												 @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
												 HttpServletRequest req) {
//		 List<WzcailiaonamedictMan> wzcailiaonamedictMEN = wzcailiaonamedictManService.queryMaterialByComponentId(id);
		 List<ComponentVo> componentVos = rebarComponentService.queryComponentByTaskId(taskId);
		 Page<Object> page = new Page<>(pageNo,pageSize);
//		 List<Wzcailiaonamedict> collect = wzcailiaonamedict.stream().skip((pageNo - 1) * pageSize).limit(pageSize).collect(Collectors.toList());
		 if(componentVos==null) {
			 return Result.error("未找到对应数据");
		 }
		 Page<ComponentVo> result =
				 new Page<>(pageNo, pageSize, page.getTotal());
		 result.setTotal(componentVos.size());
		 result.setRecords(componentVos);
		 return Result.OK(result);
	 }
	 /**
	  * 添加任务所需构件
	  *
	  * @param rebarComponentTask
	  * @return
	  */
	 @AutoLog(value = "添加任务所需构件")
	 @ApiOperation(value = "添加任务所需构件", notes = "添加任务所需构件")
	 @PostMapping(value = "/addComponentToTask")
	 public Result<?> addComponentToTask(@RequestBody RebarComponentTask rebarComponentTask) {
		 String componentId = rebarComponentTask.getComponentId();
		 String taskId = rebarComponentTask.getTaskId();
		 String componentNumber = rebarComponentTask.getComponentNumber();

		 RebarComponentTask rebarComponentTask1 = new RebarComponentTask();

		 rebarComponentTask1.setComponentId(componentId);
		 //需要先判断之前库里有没有这个构件,如果有的话就累加数量,没有的话直接加
		 rebarComponentTask1.setTaskId(taskId);
		 RebarComponentTask componentTask= rebarComponentTaskService.getComponentTask(componentId, taskId);
		 if(componentTask==null){
			 rebarComponentTask1.setId(UUID.randomUUID().toString().replace("-", ""));
			 rebarComponentTask1.setComponentNumber(componentNumber);
		 }else {
//            RebarComponentMaterial material= rebarComponentMaterialService.getComponentMaterial(componentId, materialId);
			 String componentNumber1 = componentTask.getComponentNumber();
			 Integer number2=Integer.valueOf(componentNumber1)+Integer.valueOf(componentNumber);
			 String s = number2.toString();
			 rebarComponentTask1.setComponentNumber(s);
			 rebarComponentTask1.setId(componentTask.getId());
			 boolean b = rebarComponentTaskService.removeById(componentTask.getId());
		 }
		 boolean save = rebarComponentTaskService.save(rebarComponentTask1);
		 if (save) {
			 return Result.OK("添加成功");
		 }
		 return Result.error("添加失败");
	 }

    /**
    * 导出构件任务管理excel
    *
    * @param request
    * @param rebarTaskChecklist
    */
    @RequestMapping(value = "/exportXls")
//    public ModelAndView exportXls(HttpServletRequest request, RebarTaskChecklist rebarTaskChecklist) {
//        return super.exportXls(request, rebarTaskChecklist, RebarTaskChecklist.class, "rebar_task_checklist");
    public ModelAndView exportXls(HttpServletRequest request,
								  RebarTaskChecklist rebarTaskChecklist,
								  @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
								  @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
								  String sys_depart_orgcode) {

		Integer a = 1;
		String title = "构件任务管理";
		QueryWrapper<RebarTaskChecklist> queryWrapper = rebarTaskChecklistService.getRebarTaskChecklistQueryWrapper(rebarTaskChecklist, sys_depart_orgcode, request,a);
		return getModelAndView(request, queryWrapper,title);
	}
	 /**
	  * 导出构件验收出库excel
	  *
	  * @param request
	  * @param rebarTaskChecklist
	  */
	 @RequestMapping(value = "/exportYanShouChuKuXls")
	 public ModelAndView exportYanShouChuKuXls(HttpServletRequest request,
											   RebarTaskChecklist rebarTaskChecklist,
											   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
											   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
											   String sys_depart_orgcode) {

		 Integer a = 2;
		 String title = "构件验收出库";
		 QueryWrapper<RebarTaskChecklist> queryWrapper = rebarTaskChecklistService.getRebarTaskChecklistQueryWrapper(rebarTaskChecklist, sys_depart_orgcode, request,a);
		 return getModelAndView(request, queryWrapper,title);
	 }

	 @NotNull
	 private ModelAndView getModelAndView(HttpServletRequest request, QueryWrapper<RebarTaskChecklist> queryWrapper,String title) {
		 // Step.1 组装查询条件
		 LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

		 // Step.2 获取导出数据
		 List<RebarTaskChecklist> pageList = rebarTaskChecklistService.list(queryWrapper);
		 List<RebarTaskChecklist> exportList = null;

		 // 过滤选中数据
		 String selections = request.getParameter("selections");
		 if (oConvertUtils.isNotEmpty(selections)) {
			 List<String> selectionList = Arrays.asList(selections.split(","));
			 exportList = pageList.stream().filter(item -> selectionList.contains(getId(item))).collect(Collectors.toList());
		 } else {
			 exportList = pageList;
		 }

		 // Step.3 AutoPoi 导出Excel
		 ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
		 mv.addObject(NormalExcelConstants.FILE_NAME, title); //此处设置的filename无效 ,前端会重更新设置一下
		 mv.addObject(NormalExcelConstants.CLASS, RebarTaskChecklist.class);
		 //update-begin--Author:liusq  Date:20210126 for：图片导出报错，ImageBasePath未设置--------------------
		 ExportParams exportParams=new ExportParams(title + "报表", "导出人:" + sysUser.getRealname(), title);
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
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, RebarTaskChecklist.class);
    }

}
