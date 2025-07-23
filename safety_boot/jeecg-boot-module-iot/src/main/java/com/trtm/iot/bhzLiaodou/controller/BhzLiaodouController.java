package com.trtm.iot.bhzLiaodou.controller;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.trtm.iot.bhzLiaodou.utils.CustomHttpUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.DateUtils;
import org.jeecg.common.util.oConvertUtils;
import com.trtm.iot.bhzLiaodou.entity.BhzLiaodou;
import com.trtm.iot.bhzLiaodou.service.IBhzLiaodouService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

/**
 * @Description: bhz_liaodou
 * @Author: jeecg-boot
 * @Date: 2023-01-29
 * @Version: V1.0
 */
@Api(tags = "bhz_liaodou")
@RestController
@RequestMapping("/bhzLiaodou/bhzLiaodou")
@Slf4j
public class BhzLiaodouController extends JeecgController<BhzLiaodou, IBhzLiaodouService> {
    @Autowired
    private IBhzLiaodouService bhzLiaodouService;

    /**
     * 分页列表查询
     *
     * @param bhzLiaodou
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "bhz_liaodou-分页列表查询")
    @ApiOperation(value = "bhz_liaodou-分页列表查询", notes = "bhz_liaodou-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(BhzLiaodou bhzLiaodou,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<BhzLiaodou> queryWrapper = QueryGenerator.initQueryWrapper(bhzLiaodou, req.getParameterMap());
        Page<BhzLiaodou> page = new Page<BhzLiaodou>(pageNo, pageSize);
        IPage<BhzLiaodou> pageList = bhzLiaodouService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 添加
     *
     * @param bhzLiaodou
     * @return
     */
    @AutoLog(value = "bhz_liaodou-添加")
    @ApiOperation(value = "bhz_liaodou-添加", notes = "bhz_liaodou-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody BhzLiaodou bhzLiaodou) {
        bhzLiaodouService.save(bhzLiaodou);
        return Result.OK("添加成功！");
    }

    /**
     * 批量添加
     */
    @AutoLog(value = "bhz_liaodou-批量添加")
    @ApiOperation(value = "bhz_liaodou-批量添加", notes = "bhz_liaodou-批量添加")
    @PostMapping(value = "/addBatch")
    public Result<?> addBatch(HttpServletRequest request) throws IOException {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        byte buffer[] = CustomHttpUtils.getRequestPostBytes(request);
        String json = new String(buffer, request.getCharacterEncoding());
        List<BhzLiaodou> bhzLiaodouList = JSON.parseArray(json, BhzLiaodou.class);
        for (BhzLiaodou bhzLiaodou : bhzLiaodouList) {
            if (org.apache.commons.lang.StringUtils.isBlank(bhzLiaodou.getMaterialNo())) {
                return Result.error("数据新增失败，MaterialNo不能为空："+bhzLiaodou);
            }
//            else if(org.apache.commons.lang.StringUtils.isBlank(bhzLiaodou.getSysOrgCode())){
//                return Result.error("数据新增失败，SysOrgCode不能为空："+bhzLiaodou);
//            }
            else{
                String orgCode = bhzLiaodou.getSysOrgCode();
                if(bhzLiaodou.getSysOrgCode()==null){
                    orgCode = loginUser.getOrgCode();
                }
                BhzLiaodou bhzLiaodou1 = bhzLiaodouService.selectByMaterialNo(bhzLiaodou.getMaterialNo(),orgCode);

//                Calendar calendar = Calendar.getInstance();
//                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                String format = formatter.format(calendar.getTime());
//                bhzLiaodou.setBaocunshijian(format);
                if (bhzLiaodou1 == null) {
                    bhzLiaodou.setCreatedBy(loginUser.getUsername());
                    bhzLiaodou.setCreatedTime(new Date());
                    boolean save = bhzLiaodouService.save(bhzLiaodou);
                    if (true != save) {
                        return Result.error("数据新增失败："+bhzLiaodou);
                    }
                } else {
                    bhzLiaodou.setId(bhzLiaodou1.getId());
                    bhzLiaodou.setUpdatedBy(loginUser.getUsername());
                    bhzLiaodou.setUpdatedTime(new Date());
                    boolean updateById = bhzLiaodouService.updateById(bhzLiaodou);
                    if (true != updateById) {
                        return Result.error("数据更新失败："+bhzLiaodou);
                    }
                }
            }
        }

        return Result.OK();

    }

    /**
     * 编辑
     *
     * @param bhzLiaodou
     * @return
     */
    @AutoLog(value = "bhz_liaodou-编辑")
    @ApiOperation(value = "bhz_liaodou-编辑", notes = "bhz_liaodou-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody BhzLiaodou bhzLiaodou) {
        bhzLiaodouService.updateById(bhzLiaodou);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "bhz_liaodou-通过id删除")
    @ApiOperation(value = "bhz_liaodou-通过id删除", notes = "bhz_liaodou-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        bhzLiaodouService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "bhz_liaodou-批量删除")
    @ApiOperation(value = "bhz_liaodou-批量删除", notes = "bhz_liaodou-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.bhzLiaodouService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "bhz_liaodou-通过id查询")
    @ApiOperation(value = "bhz_liaodou-通过id查询", notes = "bhz_liaodou-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        BhzLiaodou bhzLiaodou = bhzLiaodouService.getById(id);
        if (bhzLiaodou == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(bhzLiaodou);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param bhzLiaodou
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, BhzLiaodou bhzLiaodou) {
        return super.exportXls(request, bhzLiaodou, BhzLiaodou.class, "bhz_liaodou");
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
        return super.importExcel(request, response, BhzLiaodou.class);
    }

}
