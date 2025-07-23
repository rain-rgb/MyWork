package com.trtm.iot.zhangla.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.trtm.iot.system.entity.ShebeiInfo;
import com.trtm.iot.system.service.IShebeiInfoService;
import com.trtm.iot.zhangla.entity.TrZhangla;
import com.trtm.iot.zhangla.service.ITrZhanglaService;
import com.trtm.iot.zhanglaxxb.entity.TrZhanglaXxb;
import com.trtm.iot.zhanglaxxb.service.ITrZhanglaXxbService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.aspect.annotation.PermissionData;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
* @Description: tr_zhangla
* @Author: jeecg-boot
* @Date:   2021-03-16
* @Version: V1.0
*/
@Api(tags="tr_zhangla")
@RestController
@RequestMapping("/zhangla/trZhangla")
@Slf4j
public class TrZhanglaController extends JeecgController<TrZhangla, ITrZhanglaService> {
   @Autowired
   private ITrZhanglaService trZhanglaService;
    @Autowired
    private ITrZhanglaXxbService trZhanglaXxbService;
    @Autowired
    private IShebeiInfoService shebeiInfoService;
   @Autowired
   private RedisUtil redisUtil;
   /**
    * 分页列表查询
    *
    * @param trZhangla
    * @param pageNo
    * @param pageSize
    * @param req
    * @return
    */
   @AutoLog(value = "tr_zhangla-分页列表查询")
   @ApiOperation(value="tr_zhangla-分页列表查询", notes="tr_zhangla-分页列表查询")
   @GetMapping(value = "/list")
   public Result<?> queryPageList(TrZhangla trZhangla,
                                  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                  HttpServletRequest req) {
       LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户信息
       String shebei = String.valueOf(redisUtil.get(loginUser.getId()+"change"));
       if(trZhangla.getDeviceno()==null){
           if (shebei!=null){
               trZhangla.setDeviceno(shebei);
           }
       }

       QueryWrapper<TrZhangla> queryWrapper = QueryGenerator.initQueryWrapper(trZhangla, req.getParameterMap());
       Page<TrZhangla> page = new Page<TrZhangla>(pageNo, pageSize);
       IPage<TrZhangla> pageList = trZhanglaService.page(page, queryWrapper);
       return Result.OK(pageList);
   }

//    @AutoLog(value = "tr_zhangla-分页列表超标查询")
//    @ApiOperation(value="tr_zhangla-分页列表超标查询", notes="tr_zhangla-分页列表超标查询")
//    @GetMapping(value = "/gxList")
//    public Result<?> queryPagegxList(String orgCode, HttpServletRequest req) {
//        List<ShebeiInfo> shebeiInfos = shebeiInfoService.selectbyorgcode(orgCode+"%");
//        for (ShebeiInfo shebeiInfo :shebeiInfos){
//            QueryWrapper<TrZhanglaXxb> queryWrapper = new QueryWrapper<>();
//            queryWrapper.select("");
//            queryWrapper.eq("shebeibianhao",shebeiInfo);
//        }
//        return Result.OK();
//    }

    /**
     * 分页列表超标查询
     *
     * @param trZhangla
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "tr_zhangla-分页列表超标查询")
    @ApiOperation(value="tr_zhangla-分页列表超标查询", notes="tr_zhangla-分页列表超标查询")
    @GetMapping(value = "/chaoBiaoList")
    @PermissionData(pageComponent = "zhangla/ZhangLaCBList")
    public Result<?> queryPageChaoBiaoList(TrZhangla trZhangla,
                                   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                   HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId()+"change"));
        if(trZhangla.getDeviceno()==null){
            if (shebei!=null){
                trZhangla.setDeviceno(shebei);
            }
        }
        QueryWrapper<TrZhangla> queryWrapper = QueryGenerator.initQueryWrapper(trZhangla, req.getParameterMap());
        Page<TrZhangla> page = new Page<TrZhangla>(pageNo, pageSize);
        IPage<TrZhangla> pageList = trZhanglaService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
    *   添加
    *
    * @param trZhangla
    * @return
    */
   @AutoLog(value = "tr_zhangla-添加")
   @ApiOperation(value="tr_zhangla-添加", notes="tr_zhangla-添加")
   @PostMapping(value = "/add")
   public Result<?> add(@RequestBody TrZhangla trZhangla) {
       trZhanglaService.save(trZhangla);
       return Result.OK("添加成功！");
   }

   /**
    *  编辑
    *
    * @param trZhangla
    * @return
    */
   @AutoLog(value = "tr_zhangla-编辑")
   @ApiOperation(value="tr_zhangla-编辑", notes="tr_zhangla-编辑")
   @PutMapping(value = "/edit")
   public Result<?> edit(@RequestBody TrZhangla trZhangla) {
       trZhanglaService.updateById(trZhangla);
       return Result.OK("编辑成功!");
   }

   /**
    *   通过id删除
    *
    * @param id
    * @return
    */
   @AutoLog(value = "tr_zhangla-通过id删除")
   @ApiOperation(value="tr_zhangla-通过id删除", notes="tr_zhangla-通过id删除")
   @DeleteMapping(value = "/delete")
   public Result<?> delete(@RequestParam(name="id",required=true) String id) {
       trZhanglaService.removeById(id);
       return Result.OK("删除成功!");
   }

   /**
    *  批量删除
    *
    * @param ids
    * @return
    */
   @AutoLog(value = "tr_zhangla-批量删除")
   @ApiOperation(value="tr_zhangla-批量删除", notes="tr_zhangla-批量删除")
   @DeleteMapping(value = "/deleteBatch")
   public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
       this.trZhanglaService.removeByIds(Arrays.asList(ids.split(",")));
       return Result.OK("批量删除成功!");
   }

   /**
    * 通过id查询
    *
    * @param id
    * @return
    */
   @AutoLog(value = "tr_zhangla-通过id查询")
   @ApiOperation(value="tr_zhangla-通过id查询", notes="tr_zhangla-通过id查询")
   @GetMapping(value = "/queryById")
   public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
       TrZhangla trZhangla = trZhanglaService.getById(id);
       if(trZhangla==null) {
           return Result.error("未找到对应数据");
       }
       return Result.OK(trZhangla);
   }

    /**
     * 查询
     * @param type
     * @return
     */
   @AutoLog(value = "tr_zhangla-查询详情")
   @ApiOperation(value="tr_zhangla-查询详情", notes="tr_zhangla-查询详情")
   @GetMapping(value = "/queryDetails")
   public Result<?> queryDetails(@RequestParam Integer type,@RequestParam String shebeiNo,@RequestParam Integer pageNo,@RequestParam Integer pageSize){
        Map map = trZhanglaService.queryDetails(type,shebeiNo,pageNo,pageSize);
        return Result.OK(map);
   }

    /**
     * 查询
     * @param syjid,type
     * @return
     */
    @AutoLog(value = "tr_zhangla-查询详情")
    @ApiOperation(value="tr_zhangla-查询详情", notes="tr_zhangla-查询详情")
    @GetMapping(value = "/queryDe")
    public Result<?> queryDe(@RequestParam String syjid, @RequestParam Integer type){
        Map map = trZhanglaService.queryDe(syjid, type);
        return Result.OK(map);
    }



   /**
   * 导出excel
   *
   * @param request
   * @param trZhangla
   */
   @RequestMapping(value = "/exportXls")
   public ModelAndView exportXls(HttpServletRequest request, TrZhangla trZhangla) {
       return super.exportXls(request, trZhangla, TrZhangla.class, "tr_zhangla");
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
       return super.importExcel(request, response, TrZhangla.class);
   }

}
