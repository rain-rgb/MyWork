package com.trtm.iot.chaoshengbo.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.trtm.iot.chaoshengbo.entity.ChaoshengboSybltsj;
import com.trtm.iot.chaoshengbo.service.IChaoshengboSybltsjService;
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
import sun.misc.BASE64Encoder;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.sql.*;
import java.util.*;

/**
* @Description: chaoshengbo_sybltsj
* @Author: jeecg-boot
* @Date:   2021-04-15
* @Version: V1.0
*/
@Api(tags="chaoshengbo_sybltsj")
@RestController
@RequestMapping("/chaoshengbo/chaoshengboSybltsj")
@Slf4j
public class ChaoshengboSybltsjController extends JeecgController<ChaoshengboSybltsj, IChaoshengboSybltsjService> {
   @Autowired
   private IChaoshengboSybltsjService chaoshengboSybltsjService;

   /**
    * 分页列表查询
    *
    * @param chaoshengboSybltsj
    * @param pageNo
    * @param pageSize
    * @param req
    * @return
    */
   @AutoLog(value = "chaoshengbo_sybltsj-分页列表查询")
   @ApiOperation(value="chaoshengbo_sybltsj-分页列表查询", notes="chaoshengbo_sybltsj-分页列表查询")
   @GetMapping(value = "/list")
   public Result<?> queryPageList(ChaoshengboSybltsj chaoshengboSybltsj,
                                  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                  HttpServletRequest req) {
       QueryWrapper<ChaoshengboSybltsj> queryWrapper = QueryGenerator.initQueryWrapper(chaoshengboSybltsj, req.getParameterMap());
       Page<ChaoshengboSybltsj> page = new Page<ChaoshengboSybltsj>(pageNo, pageSize);
       IPage<ChaoshengboSybltsj> pageList = chaoshengboSybltsjService.page(page, queryWrapper);
       return Result.OK(pageList);
   }
    /**
     * 分页列表查询
     *
     * @param chaoshengboSybltsj
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "chaoshengbo_sybltsj-分页列表查询")
    @ApiOperation(value="chaoshengbo_sybltsj-分页列表查询", notes="chaoshengbo_sybltsj-分页列表查询")
    @GetMapping(value = "/list1")
    public Result<?> queryPageList1(ChaoshengboSybltsj chaoshengboSybltsj,
                                   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                   HttpServletRequest req,HttpServletResponse response) {
        QueryWrapper<ChaoshengboSybltsj> queryWrapper = QueryGenerator.initQueryWrapper(chaoshengboSybltsj, req.getParameterMap());
        List<ChaoshengboSybltsj> pageList = chaoshengboSybltsjService.list(queryWrapper);
       return Result.OK(pageList);
    }
    /**
     * 分页列表查询
     *
     * @param chaoshengboSybltsj
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "chaoshengbo_sybltsj-分页列表查询")
    @ApiOperation(value="chaoshengbo_sybltsj-分页列表查询", notes="chaoshengbo_sybltsj-分页列表查询")
    @GetMapping(value = "/list2")
    public void queryPageList2(ChaoshengboSybltsj chaoshengboSybltsj,
                                                           @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                                           @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                                           HttpServletRequest req, HttpServletResponse response) {
        QueryWrapper<ChaoshengboSybltsj> queryWrapper = QueryGenerator.initQueryWrapper(chaoshengboSybltsj, req.getParameterMap());
        List<ChaoshengboSybltsj> pageList = chaoshengboSybltsjService.list(queryWrapper);
        for (ChaoshengboSybltsj sybltsj : pageList) {
            try {
                byte[] pic=sybltsj.getImagedata();
                response.setContentType("image/png");
                OutputStream outs = response.getOutputStream();
                outs.write(pic);
                outs.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //return Result.OK(pageList);
    }

    public static String convertBlobToBase64String(byte[] blob) {
        String result = "";
        if(null != blob) {
            try {
                InputStream msgContent = new ByteArrayInputStream(blob);
                ByteArrayOutputStream output = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int n = 0;
                while (-1 != (n = msgContent.read(buffer))) {
                    output.write(buffer, 0, n);
                }
                result =new BASE64Encoder().encode(output.toByteArray()) ;
                output.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }else {
            return null;
        }
    }

    /**
    *   添加
    *
    * @param chaoshengboSybltsj
    * @return
    */
   @AutoLog(value = "chaoshengbo_sybltsj-添加")
   @ApiOperation(value="chaoshengbo_sybltsj-添加", notes="chaoshengbo_sybltsj-添加")
   @PostMapping(value = "/add")
   public Result<?> add(@RequestBody ChaoshengboSybltsj chaoshengboSybltsj) {
       chaoshengboSybltsjService.save(chaoshengboSybltsj);
       return Result.OK("添加成功！");
   }

    /**
     *   桩基波列图对外开放添加数据接口(chaoshengbo_sybltsj)
     *
     * @param chaoshengboSybltsj
     * @return
     */
    @AutoLog(value = "chaoshengbo_sybltsj-添加")
    @ApiOperation(value="chaoshengbo_sybltsj-添加", notes="chaoshengbo_sybltsj-添加")
    @PostMapping(value = "/addOpen")
    public Result<?> addOpen(@RequestBody ChaoshengboSybltsj chaoshengboSybltsj) {
        chaoshengboSybltsjService.save(chaoshengboSybltsj);
        return Result.OK("添加成功！");
    }

   /**
    *  编辑
    *
    * @param chaoshengboSybltsj
    * @return
    */
   @AutoLog(value = "chaoshengbo_sybltsj-编辑")
   @ApiOperation(value="chaoshengbo_sybltsj-编辑", notes="chaoshengbo_sybltsj-编辑")
   @PutMapping(value = "/edit")
   public Result<?> edit(@RequestBody ChaoshengboSybltsj chaoshengboSybltsj) {
       chaoshengboSybltsjService.updateById(chaoshengboSybltsj);
       return Result.OK("编辑成功!");
   }

   /**
    *   通过id删除
    *
    * @param id
    * @return
    */
   @AutoLog(value = "chaoshengbo_sybltsj-通过id删除")
   @ApiOperation(value="chaoshengbo_sybltsj-通过id删除", notes="chaoshengbo_sybltsj-通过id删除")
   @DeleteMapping(value = "/delete")
   public Result<?> delete(@RequestParam(name="id",required=true) String id) {
       chaoshengboSybltsjService.removeById(id);
       return Result.OK("删除成功!");
   }

   /**
    *  批量删除
    *
    * @param ids
    * @return
    */
   @AutoLog(value = "chaoshengbo_sybltsj-批量删除")
   @ApiOperation(value="chaoshengbo_sybltsj-批量删除", notes="chaoshengbo_sybltsj-批量删除")
   @DeleteMapping(value = "/deleteBatch")
   public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
       this.chaoshengboSybltsjService.removeByIds(Arrays.asList(ids.split(",")));
       return Result.OK("批量删除成功!");
   }

   /**
    * 通过id查询
    *
    * @param id
    * @return
    */
   @AutoLog(value = "chaoshengbo_sybltsj-通过id查询")
   @ApiOperation(value="chaoshengbo_sybltsj-通过id查询", notes="chaoshengbo_sybltsj-通过id查询")
   @GetMapping(value = "/queryById")
   public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
       ChaoshengboSybltsj chaoshengboSybltsj = chaoshengboSybltsjService.getById(id);
       if(chaoshengboSybltsj==null) {
           return Result.error("未找到对应数据");
       }
       return Result.OK(chaoshengboSybltsj);
   }

   /**
   * 导出excel
   *
   * @param request
   * @param chaoshengboSybltsj
   */
   @RequestMapping(value = "/exportXls")
   public ModelAndView exportXls(HttpServletRequest request, ChaoshengboSybltsj chaoshengboSybltsj) {
       return super.exportXls(request, chaoshengboSybltsj, ChaoshengboSybltsj.class, "chaoshengbo_sybltsj");
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
       return super.importExcel(request, response, ChaoshengboSybltsj.class);
   }

}
