package com.trtm.iot.clkreal.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.trtm.iot.clkreal.entity.FrontDischargeholeRealdata;
import com.trtm.iot.clkreal.service.IFrontDischargeholeRealdataService;
import com.trtm.iot.lqbhz.entity.BhzLqBases;
import com.trtm.iot.lqbhz.service.IBhzLqBasesService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
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

/**
 * @Description: 出料口测温实时数据表
 * @Author: jeecg-boot
 * @Date: 2021-05-06
 * @Version: V1.0
 */
@Api(tags = "出料口测温实时数据表")
@RestController
@RequestMapping("/clkreal/frontDischargeholeRealdata")
@Slf4j
public class FrontDischargeholeRealdataController extends JeecgController<FrontDischargeholeRealdata, IFrontDischargeholeRealdataService> {
    @Autowired
    private IFrontDischargeholeRealdataService frontDischargeholeRealdataService;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private IBhzLqBasesService bhzLqBasesService;

    /**
     * 分页列表查询
     *
     * @param
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "出料口测温实时数据表-分页列表查询")
    @ApiOperation(value = "出料口测温实时数据表-分页列表查询", notes = "出料口测温实时数据表-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(BhzLqBases bhzLqBases,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        //查询到他的设备编号
        if (bhzLqBases.getShebeibianhao() == null) {
            if (!"null".equals(shebei)) {
                bhzLqBases.setShebeibianhao(shebei);
            } else {
                bhzLqBases.setShebeibianhao("空");
            }
        }
        QueryWrapper<BhzLqBases> queryWrapper = QueryGenerator.initQueryWrapper(bhzLqBases, req.getParameterMap());
        Page<BhzLqBases> page = new Page<BhzLqBases>(pageNo, pageSize);
        IPage<BhzLqBases> pageList = bhzLqBasesService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 添加
     *
     * @param frontDischargeholeRealdata
     * @return
     */
    @AutoLog(value = "出料口测温实时数据表-添加")
    @ApiOperation(value = "出料口测温实时数据表-添加", notes = "出料口测温实时数据表-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody FrontDischargeholeRealdata frontDischargeholeRealdata) {
        frontDischargeholeRealdataService.save(frontDischargeholeRealdata);
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param frontDischargeholeRealdata
     * @return
     */
    @AutoLog(value = "出料口测温实时数据表-编辑")
    @ApiOperation(value = "出料口测温实时数据表-编辑", notes = "出料口测温实时数据表-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody FrontDischargeholeRealdata frontDischargeholeRealdata) {
        frontDischargeholeRealdataService.updateById(frontDischargeholeRealdata);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "出料口测温实时数据表-通过id删除")
    @ApiOperation(value = "出料口测温实时数据表-通过id删除", notes = "出料口测温实时数据表-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        frontDischargeholeRealdataService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "出料口测温实时数据表-批量删除")
    @ApiOperation(value = "出料口测温实时数据表-批量删除", notes = "出料口测温实时数据表-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.frontDischargeholeRealdataService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "出料口测温实时数据表-通过id查询")
    @ApiOperation(value = "出料口测温实时数据表-通过id查询", notes = "出料口测温实时数据表-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        FrontDischargeholeRealdata frontDischargeholeRealdata = frontDischargeholeRealdataService.getById(id);
        if (frontDischargeholeRealdata == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(frontDischargeholeRealdata);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param frontDischargeholeRealdata
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, FrontDischargeholeRealdata frontDischargeholeRealdata) {
        return super.exportXls(request, frontDischargeholeRealdata, FrontDischargeholeRealdata.class, "出料口测温实时数据表");
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
        return super.importExcel(request, response, FrontDischargeholeRealdata.class);
    }

}
