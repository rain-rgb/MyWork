package com.trtm.sy.sycspz.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.trtm.sy.sycspz.entity.MParamHangongxx;
import com.trtm.sy.sycspz.service.IMParamHangongxxService;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

/**
* @Description: m_param_hangongxx
* @Author: jeecg-boot
* @Date:   2024-01-03
* @Version: V1.0
*/
@Api(tags="sy_m_param_hangongxx")
@RestController
@RequestMapping("/sycspz/mParamHangongxx")
@Slf4j
public class MParamHangongxxController extends JeecgController<MParamHangongxx, IMParamHangongxxService> {
   @Autowired
   private IMParamHangongxxService mParamHangongxxService;
   
   /**
    * 分页列表查询
    *
    * @param mParamHangongxx
    * @param pageNo
    * @param pageSize
    * @param req
    * @return
    */
   @AutoLog(value = "m_param_hangongxx-分页列表查询")
   @ApiOperation(value="m_param_hangongxx-分页列表查询", notes="m_param_hangongxx-分页列表查询")
   @GetMapping(value = "/list")
   public Result<?> queryPageList(MParamHangongxx mParamHangongxx,
                                  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                  HttpServletRequest req) {
       QueryWrapper<MParamHangongxx> queryWrapper = QueryGenerator.initQueryWrapper(mParamHangongxx, req.getParameterMap());
       Page<MParamHangongxx> page = new Page<MParamHangongxx>(pageNo, pageSize);
       IPage<MParamHangongxx> pageList = mParamHangongxxService.page(page, queryWrapper);
       return Result.OK(pageList);
   }
   
   /**
    *   添加
    *
    * @param mParamHangongxx
    * @return
    */
   @AutoLog(value = "m_param_hangongxx-添加")
   @ApiOperation(value="m_param_hangongxx-添加", notes="m_param_hangongxx-添加")
   @PostMapping(value = "/add")
   public Result<?> add(@RequestBody MParamHangongxx mParamHangongxx) {
       mParamHangongxxService.save(mParamHangongxx);
       return Result.OK("添加成功！");
   }
   
   /**
    *  编辑
    *
    * @param mParamHangongxx
    * @return
    */
   @AutoLog(value = "m_param_hangongxx-编辑")
   @ApiOperation(value="m_param_hangongxx-编辑", notes="m_param_hangongxx-编辑")
   @PutMapping(value = "/edit")
   public Result<?> edit(@RequestBody MParamHangongxx mParamHangongxx) {
       mParamHangongxxService.updateById(mParamHangongxx);
       return Result.OK("编辑成功!");
   }
   
   /**
    *   通过id删除
    *
    * @param id
    * @return
    */
   @AutoLog(value = "m_param_hangongxx-通过id删除")
   @ApiOperation(value="m_param_hangongxx-通过id删除", notes="m_param_hangongxx-通过id删除")
   @DeleteMapping(value = "/delete")
   public Result<?> delete(@RequestParam(name="id",required=true) String id) {
       mParamHangongxxService.removeById(id);
       return Result.OK("删除成功!");
   }
   
   /**
    *  批量删除
    *
    * @param ids
    * @return
    */
   @AutoLog(value = "m_param_hangongxx-批量删除")
   @ApiOperation(value="m_param_hangongxx-批量删除", notes="m_param_hangongxx-批量删除")
   @DeleteMapping(value = "/deleteBatch")
   public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
       this.mParamHangongxxService.removeByIds(Arrays.asList(ids.split(",")));
       return Result.OK("批量删除成功!");
   }
   
   /**
    * 通过id查询
    *
    * @param id
    * @return
    */
   @AutoLog(value = "m_param_hangongxx-通过id查询")
   @ApiOperation(value="m_param_hangongxx-通过id查询", notes="m_param_hangongxx-通过id查询")
   @GetMapping(value = "/queryById")
   public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
       MParamHangongxx mParamHangongxx = mParamHangongxxService.getById(id);
       if(mParamHangongxx==null) {
           return Result.error("未找到对应数据");
       }
       return Result.OK(mParamHangongxx);
   }

   /**
   * 导出excel
   *
   * @param request
   * @param mParamHangongxx
   */
   @RequestMapping(value = "/exportXls")
   public ModelAndView exportXls(HttpServletRequest request, MParamHangongxx mParamHangongxx) {
       return super.exportXls(request, mParamHangongxx, MParamHangongxx.class, "m_param_hangongxx");
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
       return super.importExcel(request, response, MParamHangongxx.class);
   }

}
