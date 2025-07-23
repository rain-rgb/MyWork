package com.trtm.sy.wtgl.qywtd.controller;

import java.text.ParseException;
import java.util.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.trtm.iot.wztaizhang.entity.Wztaizhang;
import com.trtm.iot.wztaizhang.service.IWztaizhangService;
import com.trtm.sy.wtgl.qywtd.entity.QueryVo;
import com.trtm.sy.wtgl.qywtd.entity.SyDpsYyYuancaiquyangweituo;
import com.trtm.sy.wtgl.qywtd.entity.request.QyRequest;
import com.trtm.sy.wtgl.qywtd.entity.request.ZpQyRequest;
import com.trtm.sy.wtgl.qywtd.entity.response.ClResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import com.trtm.sy.wtgl.qywtd.service.ISyDpsYyYuancaiquyangweituoService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

/**
 * @Description: sy_dps_yy_yuancaiquyangweituo
 * @Author: jeecg-boot
 * @Date: 2023-02-23
 * @Version: V1.0
 */
@Api(tags = "sy_dps_yy_yuancaiquyangweituo")
@RestController
@RequestMapping("/qywtd/syDpsYyYuancaiquyangweituo")
@Slf4j
public class SyDpsYyYuancaiquyangweituoController extends JeecgController<SyDpsYyYuancaiquyangweituo, ISyDpsYyYuancaiquyangweituoService> {
    @Autowired
    private ISyDpsYyYuancaiquyangweituoService syDpsYyYuancaiquyangweituoService;
    @Autowired
    private IWztaizhangService wztaizhangService;

    /**
     * 分页列表查询
     *
     * @param syDpsYyYuancaiquyangweituo
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "sy_dps_yy_yuancaiquyangweituo-分页列表查询")
    @ApiOperation(value = "sy_dps_yy_yuancaiquyangweituo-分页列表查询", notes = "sy_dps_yy_yuancaiquyangweituo-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(SyDpsYyYuancaiquyangweituo syDpsYyYuancaiquyangweituo,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<SyDpsYyYuancaiquyangweituo> queryWrapper = QueryGenerator.initQueryWrapper(syDpsYyYuancaiquyangweituo, req.getParameterMap());
        Page<SyDpsYyYuancaiquyangweituo> page = new Page<SyDpsYyYuancaiquyangweituo>(pageNo, pageSize);
        IPage<SyDpsYyYuancaiquyangweituo> pageList = syDpsYyYuancaiquyangweituoService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 添加
     *
     * @param syDpsYyYuancaiquyangweituo
     * @return
     */
    @AutoLog(value = "sy_dps_yy_yuancaiquyangweituo-添加")
    @ApiOperation(value = "sy_dps_yy_yuancaiquyangweituo-添加", notes = "sy_dps_yy_yuancaiquyangweituo-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody SyDpsYyYuancaiquyangweituo syDpsYyYuancaiquyangweituo) {
        syDpsYyYuancaiquyangweituoService.save(syDpsYyYuancaiquyangweituo);
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param syDpsYyYuancaiquyangweituo
     * @return
     */
    @AutoLog(value = "sy_dps_yy_yuancaiquyangweituo-编辑")
    @ApiOperation(value = "sy_dps_yy_yuancaiquyangweituo-编辑", notes = "sy_dps_yy_yuancaiquyangweituo-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody SyDpsYyYuancaiquyangweituo syDpsYyYuancaiquyangweituo) {
        syDpsYyYuancaiquyangweituoService.updateById(syDpsYyYuancaiquyangweituo);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "sy_dps_yy_yuancaiquyangweituo-通过id删除")
    @ApiOperation(value = "sy_dps_yy_yuancaiquyangweituo-通过id删除", notes = "sy_dps_yy_yuancaiquyangweituo-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        syDpsYyYuancaiquyangweituoService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "sy_dps_yy_yuancaiquyangweituo-批量删除")
    @ApiOperation(value = "sy_dps_yy_yuancaiquyangweituo-批量删除", notes = "sy_dps_yy_yuancaiquyangweituo-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.syDpsYyYuancaiquyangweituoService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "sy_dps_yy_yuancaiquyangweituo-通过id查询")
    @ApiOperation(value = "sy_dps_yy_yuancaiquyangweituo-通过id查询", notes = "sy_dps_yy_yuancaiquyangweituo-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        SyDpsYyYuancaiquyangweituo syDpsYyYuancaiquyangweituo = syDpsYyYuancaiquyangweituoService.getById(id);
        if (syDpsYyYuancaiquyangweituo == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(syDpsYyYuancaiquyangweituo);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param syDpsYyYuancaiquyangweituo
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, SyDpsYyYuancaiquyangweituo syDpsYyYuancaiquyangweituo) {
        return super.exportXls(request, syDpsYyYuancaiquyangweituo, SyDpsYyYuancaiquyangweituo.class, "sy_dps_yy_yuancaiquyangweituo");
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
        return super.importExcel(request, response, SyDpsYyYuancaiquyangweituo.class);
    }

    /**
     * 分页查询wztaizhang委托
     */
    @AutoLog(value = "分页查询wztaizhang委托")
    @ApiOperation(value = "分页查询wztaizhang委托", notes = "")
    @GetMapping("/findWzTaiZhang")
    public Result<?> findWzTaiZhang(Wztaizhang wztaizhang,
                                    @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                    @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                    HttpServletRequest req) {
        wztaizhang.setJinchangshijian("*" + wztaizhang.getJinchangshijian() + "*");
        wztaizhang.setUsePart("*" + wztaizhang.getUsePart() + "*");
        QueryWrapper<Wztaizhang> queryWrapper = QueryGenerator.initQueryWrapper(wztaizhang, req.getParameterMap());
        Page<Wztaizhang> page = new Page<>(pageNo, pageSize);
        IPage<Wztaizhang> pageList = wztaizhangService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 新增委托
     */

    @ApiOperation(value = "新增委托", notes = "")
    @PostMapping("/insertDelegate/{id}")
    public Result<?> insertDelegate(@PathVariable Integer id, @RequestBody SyDpsYyYuancaiquyangweituo weiTuo) {
        try {
            syDpsYyYuancaiquyangweituoService.insertDelegate(id, weiTuo);
        } catch (ParseException e) {
            e.printStackTrace();
            return Result.error("新增失败");
        }
        return Result.OK();
    }

    /**
     * 撤销委托
     */

    @ApiOperation(value = "撤销委托", notes = "")
    @DeleteMapping("/deleteDelegate/{id}")
    public Result<?> deleteDelegate(@PathVariable Integer id) {
        syDpsYyYuancaiquyangweituoService.deleteDelegate(id);
        return Result.OK();
    }

    /**
     * 取样管理数据查询接口
     *
     * @param queryVo
     * @param pageNo
     * @param pageSize
     * @return
     */
    @ApiOperation(value = "根据条件查询委托", notes = "")
    @GetMapping("/selectByVo")
    public Result<?> selectByVo(QueryVo queryVo, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        IPage pageList = syDpsYyYuancaiquyangweituoService.selectByVo(queryVo, pageNo, pageSize);
        return Result.OK(pageList);
    }


    /**
     * 取样管理展示
     */
    @ApiOperation(value = "取样管理展示", notes = "")
    @GetMapping("/selectQuYangList")
    public Result<?> selectQuYangList(@RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                      @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        IPage pageList = syDpsYyYuancaiquyangweituoService.selectQuYangList(pageNo, pageSize);
        return Result.OK(pageList);
    }

    /**
     * 指派取样人
     */
    @ApiOperation(value = "指派取样人", notes = "")
    @PostMapping("/zhiPaiQuYang")
    public Result<?> zhiPaiQuYang(@RequestBody ZpQyRequest zpQyRequest) {
        try {
            if (!syDpsYyYuancaiquyangweituoService.zhiPaiQuYang(zpQyRequest)) {
                return Result.error("已指派");
            }
        } catch (ParseException e) {
            e.printStackTrace();
            return Result.error("请重试");
        }
        return Result.ok("指派成功");
    }

    /**
     * 取样
     */
    @ApiOperation(value = "取样", notes = "")
    @PostMapping("/quYang")
    public Result<?> quYang(@RequestBody QyRequest qyRequest) {
        syDpsYyYuancaiquyangweituoService.quYang(qyRequest);
        return Result.OK();
    }

    /**
     * 取样信息保存
     */
    @ApiOperation(value = "取样信息编辑保存", notes = "")
    @PostMapping("/saveQuYangData")
    public Result<?> saveQuYangData(@RequestBody QyRequest qyRequest) {
        syDpsYyYuancaiquyangweituoService.saveQuYangData(qyRequest);
        return Result.OK();
    }

    /**
     * 取样状态更改
     */
    @ApiOperation(value = "取样信息编辑保存", notes = "")
    @PostMapping("/updateQyZt")
    public Result<?> updateQyZt(@RequestBody QyRequest qyRequest) {
        syDpsYyYuancaiquyangweituoService.updateQyZt(qyRequest);
        return Result.OK();
    }

    /**
     * 收样
     */
    @ApiOperation(value = "收样", notes = "")
    @PostMapping("/shouYang")
    public Result<?> shouYang(@RequestBody QyRequest qyRequest) {
        syDpsYyYuancaiquyangweituoService.shouYang(qyRequest);
        return Result.OK();
    }

    /**
     * 见证
     */
    @ApiOperation(value = "见证", notes = "")
    @GetMapping("/jiAnZhEng")
    public Result<?> jiAnZhEng(@RequestParam(value = "erweimabianhao", required = false) String erweimabianhao) {
        Map map = syDpsYyYuancaiquyangweituoService.jiAnZhEng(erweimabianhao);
        return Result.OK(map);
    }


    /**
     * 取样管理列表查询
     *
     * @return
     */
    @ApiOperation(value = "取样收样样品管理列表查询", notes = "")
    @GetMapping("/getSyList")
    public Result<?> getSyList(@RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                               @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                               QueryVo queryVo) {
        IPage<Map> pageList = syDpsYyYuancaiquyangweituoService.getSyList(queryVo, pageNo, pageSize);
        return Result.OK(pageList);
    }

    /**
     * 收样样品管理列表查询
     *
     * @return
     */
    @ApiOperation(value = "取样收样样品管理列表查询", notes = "")
    @GetMapping("/getYgList")
    public Result<?> getYgList(@RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                               @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                               QueryVo queryVo) {
        IPage<Map> pageList = syDpsYyYuancaiquyangweituoService.getYgList(queryVo, pageNo, pageSize);
        return Result.OK(pageList);
    }


    /**
     * 查询委托页面的数据
     *
     * @return
     */
    @ApiOperation(value = "取样收样样品管理列表查询", notes = "")
    @GetMapping("/selectList")
    public Result<?> selectList(@RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                QueryVo queryVo) {
        IPage<SyDpsYyYuancaiquyangweituo> pageList = syDpsYyYuancaiquyangweituoService.selectList(pageNo, pageSize, queryVo);
        return Result.OK(pageList);
    }


    /**
     * 取样管理页面展示wztaizhang取样状态
     */
    @GetMapping("/getWzTz")
    public Result<?> getWzTz(@RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                             @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, QueryVo queryVo) {
        IPage<Map> list = wztaizhangService.getWzTz(pageNo, pageSize, queryVo);
        return Result.OK(list);
    }


    /**
     * 获取查询条件框中的材料类型和材料名称
     */
    @GetMapping("/queryConditionCl")
    public Result<?> getQueryConditionCl(String nodeType, String sysOrgCode) {
        List<ClResponse> list = syDpsYyYuancaiquyangweituoService.getQueryConditionCl(nodeType, sysOrgCode);
        return Result.OK(list);
    }


    /**
     * 获取试验委托部门信息带入
     * @return
     */
    @GetMapping("/getDepartData")
    public Result<?> getDepartData(@RequestParam String orgCode) {
        Map map = syDpsYyYuancaiquyangweituoService.getDepartData(orgCode);
        return Result.OK(map);
    }



}
