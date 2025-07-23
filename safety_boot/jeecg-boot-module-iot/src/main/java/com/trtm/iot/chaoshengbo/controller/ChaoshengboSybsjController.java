package com.trtm.iot.chaoshengbo.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.trtm.iot.chaoshengbo.entity.ChaoshengboSybsj;
import com.trtm.iot.chaoshengbo.service.IChaoshengboSybsjService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

/**
* @Description: chaoshengbo_sybsj
* @Author: jeecg-boot
* @Date:   2021-04-16
* @Version: V1.0
*/
@Api(tags="chaoshengbo_sybsj")
@RestController
@RequestMapping("/chaoshengbo/chaoshengboSybsj")
@Slf4j
public class ChaoshengboSybsjController extends JeecgController<ChaoshengboSybsj, IChaoshengboSybsjService> {
   @Resource
   private IChaoshengboSybsjService chaoshengboSybsjService;

   /**
    * 分页列表查询
    *
    * @param chaoshengboSybsj
    * @param pageNo
    * @param pageSize
    * @param req
    * @return
    */
   @AutoLog(value = "chaoshengbo_sybsj-分页列表查询")
   @ApiOperation(value="chaoshengbo_sybsj-分页列表查询", notes="chaoshengbo_sybsj-分页列表查询")
   @GetMapping(value = "/list")
   public Result<?> queryPageList(ChaoshengboSybsj chaoshengboSybsj,
                                  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                  HttpServletRequest req) {
       QueryWrapper<ChaoshengboSybsj> queryWrapper = QueryGenerator.initQueryWrapper(chaoshengboSybsj, req.getParameterMap());
       Page<ChaoshengboSybsj> page=new Page(pageNo,pageSize);
       IPage<ChaoshengboSybsj> pageList = chaoshengboSybsjService.page(page, queryWrapper);
       return Result.OK(pageList);
   }
    /**
     * 分页列表查询
     *
     * @param chaoshengboSybsj
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "chaoshengbo_sybsj-分页列表查询")
    @ApiOperation(value="chaoshengbo_sybsj-分页列表查询", notes="chaoshengbo_sybsj-分页列表查询")
    @GetMapping(value = "/list1")
    public Result<?> queryPageList1(ChaoshengboSybsj chaoshengboSybsj,
                                   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<ChaoshengboSybsj> queryWrapper = QueryGenerator.initQueryWrapper(chaoshengboSybsj, req.getParameterMap());
        List<ChaoshengboSybsj> pageList = chaoshengboSybsjService.list(queryWrapper);
        return Result.OK(pageList);
    }
   /**
    *   chaoshengbo_sybsj对外开放添加数据接口
    *
    * @param chaoshengboSybsj
    * @return
    */
   @AutoLog(value = "chaoshengbo_sybsj-添加")
   @ApiOperation(value="chaoshengbo_sybsj-添加", notes="chaoshengbo_sybsj-添加")
   @PostMapping(value = "/addOpen")
   public Result<?> addOpen(@RequestBody ChaoshengboSybsj chaoshengboSybsj) {
       chaoshengboSybsjService.save(chaoshengboSybsj);
       return Result.OK("添加成功！");
   }

    /**
     *   添加
     *
     * @param chaoshengboSybsj
     * @return
     */
    @AutoLog(value = "chaoshengbo_sybsj-添加")
    @ApiOperation(value="chaoshengbo_sybsj-添加", notes="chaoshengbo_sybsj-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody ChaoshengboSybsj chaoshengboSybsj) {
        chaoshengboSybsjService.save(chaoshengboSybsj);
        return Result.OK("添加成功！");
    }

   /**
    *  编辑
    *
    * @param chaoshengboSybsj
    * @return
    */
   @AutoLog(value = "chaoshengbo_sybsj-编辑")
   @ApiOperation(value="chaoshengbo_sybsj-编辑", notes="chaoshengbo_sybsj-编辑")
   @PutMapping(value = "/edit")
   public Result<?> edit(@RequestBody ChaoshengboSybsj chaoshengboSybsj) {
       chaoshengboSybsjService.updateById(chaoshengboSybsj);
       return Result.OK("编辑成功!");
   }

   /**
    *   通过id删除
    *
    * @param id
    * @return
    */
   @AutoLog(value = "chaoshengbo_sybsj-通过id删除")
   @ApiOperation(value="chaoshengbo_sybsj-通过id删除", notes="chaoshengbo_sybsj-通过id删除")
   @DeleteMapping(value = "/delete")
   public Result<?> delete(@RequestParam(name="id",required=true) String id) {
       chaoshengboSybsjService.removeById(id);
       return Result.OK("删除成功!");
   }

   /**
    *  批量删除
    *
    * @param ids
    * @return
    */
   @AutoLog(value = "chaoshengbo_sybsj-批量删除")
   @ApiOperation(value="chaoshengbo_sybsj-批量删除", notes="chaoshengbo_sybsj-批量删除")
   @DeleteMapping(value = "/deleteBatch")
   public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
       this.chaoshengboSybsjService.removeByIds(Arrays.asList(ids.split(",")));
       return Result.OK("批量删除成功!");
   }

   /**
    * 通过id查询
    *
    * @param id
    * @return
    */
   @AutoLog(value = "chaoshengbo_sybsj-通过id查询")
   @ApiOperation(value="chaoshengbo_sybsj-通过id查询", notes="chaoshengbo_sybsj-通过id查询")
   @GetMapping(value = "/queryById")
   public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
       ChaoshengboSybsj chaoshengboSybsj = chaoshengboSybsjService.getById(id);
       if(chaoshengboSybsj==null) {
           return Result.error("未找到对应数据");
       }
       return Result.OK(chaoshengboSybsj);
   }

   /**
   * 导出excel
   *
   * @param request
   * @param chaoshengboSybsj
   */
   @RequestMapping(value = "/exportXls")
   public ModelAndView exportXls(HttpServletRequest request, ChaoshengboSybsj chaoshengboSybsj) {
       return super.exportXls(request, chaoshengboSybsj, ChaoshengboSybsj.class, "chaoshengbo_sybsj");
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
       return super.importExcel(request, response, ChaoshengboSybsj.class);
   }

}
