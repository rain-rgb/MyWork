package com.trtm.iot.lqbhzrecipe.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import com.trtm.iot.bhzMaterialCfg.entity.BhzMaterialCfg;
import com.trtm.iot.bhzMaterialCfg.service.IBhzMaterialCfgService;
import com.trtm.iot.lqbhz.entity.BhzLqBases;
import com.trtm.iot.lqbhz.service.IBhzLqBasesService;
import com.trtm.iot.lqbhzrecipe.entity.BhzLqPhbZibiao;
import com.trtm.iot.lqbhzrecipe.service.IBhzLqPhbZibiaoService;
import com.trtm.iot.lqbhzrecipe.vo.BhzLqRecipePage;
import com.trtm.iot.system.entity.ShebeiInfo;
import com.trtm.iot.system.service.IShebeiInfoService;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.common.util.oConvertUtils;
import com.trtm.iot.lqbhzrecipe.entity.BhzLqRecipe;
import com.trtm.iot.lqbhzrecipe.service.IBhzLqRecipeService;

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
import org.springframework.beans.BeanUtils;
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
 * @Description: bhz_lq_recipe
 * @Author: jeecg-boot
 * @Date: 2021-03-30
 * @Version: V1.0
 */
@Api(tags = "bhz_lq_recipe")
@RestController
@RequestMapping("/lqbhzrecipe/bhzLqRecipe")
@Slf4j
public class BhzLqRecipeController extends JeecgController<BhzLqRecipe, IBhzLqRecipeService> {
    @Autowired
    private IBhzLqRecipeService bhzLqRecipeService;
    @Autowired
    private IBhzLqBasesService bhzLqBasesService;
    @Autowired
    private IShebeiInfoService shebeiInfoService;
    @Autowired
    private IBhzLqPhbZibiaoService phbZibiaoService;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private IBhzMaterialCfgService materialCfgService;

    /**
     * 分页列表查询
     *
     * @param bhzLqRecipe
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "bhz_lq_recipe-分页列表查询")
    @ApiOperation(value = "bhz_lq_recipe-分页列表查询", notes = "bhz_lq_recipe-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(BhzLqRecipe bhzLqRecipe,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        if (bhzLqRecipe.getShebeibianhao() == null) {
            if (!"null".equals(shebei)) {
                bhzLqRecipe.setShebeibianhao(shebei);
            } else {
                bhzLqRecipe.setShebeibianhao("空");
            }
        }
        QueryWrapper<BhzLqRecipe> queryWrapper = QueryGenerator.initQueryWrapper(bhzLqRecipe, req.getParameterMap());
        Page<BhzLqRecipe> page = new Page<BhzLqRecipe>(pageNo, pageSize);
        IPage<BhzLqRecipe> pageList = bhzLqRecipeService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 分页列表查询
     *
     * @param bhzLqRecipe
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "bhz_lq_recipe-分页列表查询")
    @ApiOperation(value = "bhz_lq_recipe-分页列表查询", notes = "bhz_lq_recipe-分页列表查询")
    @GetMapping(value = "/list1")
    public Result<?> queryPageList1(BhzLqRecipe bhzLqRecipe,
                                    @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                    @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                    HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        if (bhzLqRecipe.getShebeibianhao() == null) {
            if (!"null".equals(shebei)) {
                bhzLqRecipe.setShebeibianhao(shebei);
            } else {
                bhzLqRecipe.setShebeibianhao("空");
            }
        }
        String[] split = shebei.split(",");
        List<String> shebeiList = Arrays.asList(split);
        List<ShebeiInfo> shebeiInfoList = shebeiInfoService.arrayOneshebeis(shebeiList, 1);
        List<String> SBList = new ArrayList<>();
        if (shebeiInfoList != null || shebeiInfoList.size() > 0) {
            for (ShebeiInfo shebeiInfo : shebeiInfoList) {
                SBList.add(shebeiInfo.getSbjno());
            }
            BhzLqBases bhzLqBases = bhzLqBasesService.selectBysbno(SBList);
            bhzLqRecipe.setPhbid(bhzLqBases.getFormulaNo());
        }
        QueryWrapper<BhzLqRecipe> queryWrapper = QueryGenerator.initQueryWrapper(bhzLqRecipe, req.getParameterMap());
        Page<BhzLqRecipe> page = new Page<BhzLqRecipe>(pageNo, pageSize);
        IPage<BhzLqRecipe> pageList = bhzLqRecipeService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 添加
     *
     * @param bhzLqRecipePage
     * @return
     */
    @AutoLog(value = "bhz_lq_recipe-添加")
    @ApiOperation(value = "bhz_lq_recipe-添加", notes = "bhz_lq_recipe-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody BhzLqRecipePage bhzLqRecipePage) {
        BhzLqRecipe bhzLqRecipe = new BhzLqRecipe();
        String uuid = UUID.randomUUID().toString();//随机生成唯一码UUID
        bhzLqRecipePage.setZhuziid(uuid);
        Date date = new Date();
        DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        bhzLqRecipePage.setLlshijian(date);
        BeanUtils.copyProperties(bhzLqRecipePage, bhzLqRecipe);
        bhzLqRecipeService.saveMain(bhzLqRecipe, bhzLqRecipePage.getBhzLqPhbZibiaoList());
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param
     * @return
     */
    @AutoLog(value = "bhz_lq_recipe-编辑")
    @ApiOperation(value = "bhz_lq_recipe-编辑", notes = "bhz_lq_recipe-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody BhzLqRecipePage bhzLqRecipePage) {

        BhzLqRecipe bhzLqRecipe = new BhzLqRecipe();
        BeanUtils.copyProperties(bhzLqRecipePage, bhzLqRecipe);
        BhzLqRecipe byId = bhzLqRecipeService.getById(bhzLqRecipe.getId());
        if (byId == null) {
            return Result.error("未找到对应数据");
        }
        bhzLqRecipeService.updateMain(bhzLqRecipe, bhzLqRecipePage.getBhzLqPhbZibiaoList());
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "bhz_lq_recipe-通过id删除")
    @ApiOperation(value = "bhz_lq_recipe-通过id删除", notes = "bhz_lq_recipe-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        bhzLqRecipeService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "bhz_lq_recipe-批量删除")
    @ApiOperation(value = "bhz_lq_recipe-批量删除", notes = "bhz_lq_recipe-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.bhzLqRecipeService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "bhz_lq_recipe-通过id查询")
    @ApiOperation(value = "bhz_lq_recipe-通过id查询", notes = "bhz_lq_recipe-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        BhzLqRecipe bhzLqRecipe = bhzLqRecipeService.getById(id);
        if (bhzLqRecipe == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(bhzLqRecipe);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param bhzLqRecipe
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, BhzLqRecipe bhzLqRecipe) {
        return super.exportXls(request, bhzLqRecipe, BhzLqRecipe.class, "bhz_lq_recipe");
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
        return super.importExcel(request, response, BhzLqRecipe.class);
    }


    @RequestMapping(value = "/getPhb", method = RequestMethod.POST)
    public Result<?> getPhb(HttpServletRequest request) {
        QueryWrapper<BhzLqRecipe> bhzRecipeQueryWrapper = new QueryWrapper<>();
        bhzRecipeQueryWrapper.eq("shebeibianhao", "ydlm1blq_01");
        bhzRecipeQueryWrapper.ge("llshijian", "2023-01-01");
        List<BhzLqRecipe> bhzRecipeList = bhzLqRecipeService.list(bhzRecipeQueryWrapper);

        List phbList = new ArrayList();
        for (BhzLqRecipe bhzRecipe : bhzRecipeList) {
            JSONObject phbObject = new JSONObject();
            String hhllx = bhzRecipe.getPhbid();
            phbObject.put("phbNo", hhllx);

            String phbName = "";
            if ("33".equals(hhllx)) {
                phbName = "SMA-13";
            }
            if ("36".equals(hhllx)) {
                phbName = "SMA-16";
            }
            if ("34".equals(hhllx)) {
                phbName = "SUP-20";
            }
            if ("75".equals(hhllx)) {
                phbName = "SUP-25";
            }
            phbObject.put("phbName", phbName);

            QueryWrapper<BhzLqPhbZibiao> zibiaoQueryWrapper = new QueryWrapper<>();
            zibiaoQueryWrapper.eq("zhuziid", bhzRecipe.getZhuziid());
            List<BhzLqPhbZibiao> lqPhbZibiaoList = phbZibiaoService.list(zibiaoQueryWrapper);

            for (BhzLqPhbZibiao bhzLqPhbZibiao : lqPhbZibiaoList) {
                String cailiaono = bhzLqPhbZibiao.getCailiaono();
                String phb = bhzLqPhbZibiao.getTianjiaji();
                String ggxh = bhzLqPhbZibiao.getGgxh();
                if ("石料1".equals(cailiaono) || "石1".equals(cailiaono)) {
                    phbObject.put("sl1", cailiaono);
                    phbObject.put("sl1phb", phb);
                    phbObject.put("sl1gglx", ggxh);
                }
                if ("石料2".equals(cailiaono) || "石2".equals(cailiaono)) {
                    phbObject.put("sl2", cailiaono);
                    phbObject.put("sl2phb", phb);
                    phbObject.put("sl2gglx", ggxh);
                }
                if ("石料3".equals(cailiaono) || "石3".equals(cailiaono)) {
                    phbObject.put("sl3", cailiaono);
                    phbObject.put("sl3phb", phb);
                    phbObject.put("sl3gglx", ggxh);
                }
                if ("石料4".equals(cailiaono) || "石4".equals(cailiaono)) {
                    phbObject.put("sl4", cailiaono);
                    phbObject.put("sl4phb", phb);
                    phbObject.put("sl4gglx", ggxh);
                }
                if ("粉料1".equals(cailiaono) || "粉料1".equals(cailiaono)) {
                    phbObject.put("fl1", cailiaono);
                    phbObject.put("fl1phb", phb);
                    phbObject.put("fl1gglx", ggxh);
                }
                if ("沥青".equals(cailiaono) || "沥青".equals(cailiaono)) {
                    phbObject.put("lq", cailiaono);
                    phbObject.put("lqphb", phb);
                    phbObject.put("lqgglx", ggxh);
                }
            }

            phbList.add(phbObject);

        }
        return Result.OK("请求成功！", phbList);
    }
}
