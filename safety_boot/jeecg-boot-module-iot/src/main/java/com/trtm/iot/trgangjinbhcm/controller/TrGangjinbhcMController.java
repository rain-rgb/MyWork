package com.trtm.iot.trgangjinbhcm.controller;

import java.io.FileInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.hutool.http.HttpRequest;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.trtm.iot.hnthtconsign.service.IHnthtConsignService;
import com.trtm.iot.trgangjinbhcm.vo.TrGangjinbhcMPage;
import com.trtm.iot.trgangjinbhcs.entity.TrGangjinbhcS;
import com.trtm.iot.trgangjinbhcs.service.ITrGangjinbhcSService;
import org.apache.poi.common.usermodel.HyperlinkType;
import org.apache.poi.ss.usermodel.*;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.common.util.oConvertUtils;
import com.trtm.iot.trgangjinbhcm.entity.TrGangjinbhcM;
import com.trtm.iot.trgangjinbhcm.service.ITrGangjinbhcMService;

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
 * @Description: 钢保数据检测主表
 * @Author: jeecg-boot
 * @Date: 2021-09-10
 * @Version: V1.0
 */
@Api(tags = "钢保数据检测主表")
@RestController
@RequestMapping("/trgangjinbhcm/trGangjinbhcM")
@Slf4j
public class TrGangjinbhcMController extends JeecgController<TrGangjinbhcM, ITrGangjinbhcMService> {
    @Autowired
    private ITrGangjinbhcMService trGangjinbhcMService;
    @Autowired
    private ITrGangjinbhcSService trGangjinbhcSService;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private IHnthtConsignService hnthtConsignService;

    /**
     * 分页列表查询
     *
     * @param trGangjinbhcM
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "钢保数据检测主表-分页列表查询")
    @ApiOperation(value = "钢保数据检测主表-分页列表查询", notes = "钢保数据检测主表-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(TrGangjinbhcM trGangjinbhcM,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        if (trGangjinbhcM.getShebeiNo() == null) {
            if (!"null".equals(shebei)) {
                trGangjinbhcM.setShebeiNo(shebei);
            } else {
                trGangjinbhcM.setShebeiNo("空");
            }
        }
        trGangjinbhcM.setCode("*" + trGangjinbhcM.getCode() + "*");
        QueryWrapper<TrGangjinbhcM> queryWrapper = QueryGenerator.initQueryWrapper(trGangjinbhcM, req.getParameterMap());
        Page<TrGangjinbhcM> page = new Page<TrGangjinbhcM>(pageNo, pageSize);
        IPage<TrGangjinbhcM> pageList = trGangjinbhcMService.page(page, queryWrapper);
        return Result.OK(pageList);
    }


    /**
     * 添加钢保主子表信息
     *
     * @param trGangjinbhcMPage
     * @return
     */
    @AutoLog(value = "钢保数据检测主表-添加")
    @ApiOperation(value = "钢保数据检测主表-添加", notes = "钢保数据检测主表-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody TrGangjinbhcMPage trGangjinbhcMPage) {
        TrGangjinbhcM trGangjinbhcM = new TrGangjinbhcM();
        BeanUtils.copyProperties(trGangjinbhcMPage, trGangjinbhcM);
        int i = trGangjinbhcMService.saveMain(trGangjinbhcM, trGangjinbhcMPage.getTrGangjinbhcS());
        if (i == 400) {
            return Result.error("添加失败！");
        }
        String code = trGangjinbhcM.getCode();
        hnthtConsignService.updateStatus(code);
        return Result.OK("添加成功！");
    }

    /**
     * 修改试验参数
     *
     * @param
     * @return
     */
    @AutoLog(value = "钢保数据检测主表-添加")
    @ApiOperation(value = "钢保数据检测主表-添加", notes = "钢保数据检测主表-添加")
    @PostMapping(value = "/update")
    public Result<?> update(@RequestBody TrGangjinbhcM trGangjinbhcM) {
        String code = trGangjinbhcM.getCode();
        String hege = trGangjinbhcM.getHege();
        String piancha = trGangjinbhcM.getPiancha();
        String xiuzheng = trGangjinbhcM.getXiuzheng();
        LambdaQueryWrapper<TrGangjinbhcM> Qwrapper = new LambdaQueryWrapper<>();
        Qwrapper.eq(TrGangjinbhcM::getCode,code);
        List<TrGangjinbhcM> list = trGangjinbhcMService.list(Qwrapper);
        if (list.size()==0){
            return Result.error("当前code无对应数据！");
        }
        for (TrGangjinbhcM Gangjinbhc : list) {
            Gangjinbhc.setHege(hege);
            Gangjinbhc.setPiancha(piancha);
            Gangjinbhc.setXiuzheng(xiuzheng);
            Gangjinbhc.setIsbaogao(1);
            trGangjinbhcMService.updateById(Gangjinbhc);
        }
        return Result.OK("数据修改成功！");
    }
    /**
     * 编辑
     *
     * @param trGangjinbhcM
     * @return
     */
    @AutoLog(value = "钢保数据检测主表-编辑")
    @ApiOperation(value = "钢保数据检测主表-编辑", notes = "钢保数据检测主表-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody TrGangjinbhcM trGangjinbhcM) {
        trGangjinbhcMService.updateById(trGangjinbhcM);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "钢保数据检测主表-通过id删除")
    @ApiOperation(value = "钢保数据检测主表-通过id删除", notes = "钢保数据检测主表-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        trGangjinbhcMService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "钢保数据检测主表-批量删除")
    @ApiOperation(value = "钢保数据检测主表-批量删除", notes = "钢保数据检测主表-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.trGangjinbhcMService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "钢保数据检测主表-通过id查询")
    @ApiOperation(value = "钢保数据检测主表-通过id查询", notes = "钢保数据检测主表-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        TrGangjinbhcM trGangjinbhcM = trGangjinbhcMService.getById(id);
        if (trGangjinbhcM == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(trGangjinbhcM);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param trGangjinbhcM
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, TrGangjinbhcM trGangjinbhcM) {
        return super.exportXls(request, trGangjinbhcM, TrGangjinbhcM.class, "钢保数据检测主表");
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
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
        int importedCount = 0;
        for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
            MultipartFile file = entity.getValue();
            if (!file.isEmpty()) {
                try {
                    InputStream inputStream = file.getInputStream();
                    // TODO: 在此处编写读取Excel的代码逻辑，将数据导入到 TrHnthtM 对象中

                    // 示例：使用 Apache POI 进行读取
                    // 示例：使用 Apache POI 进行读取
                    Workbook workbook = WorkbookFactory.create(inputStream);
                    Sheet sheet = workbook.getSheetAt(0); // 假设导入的是第一个工作表
                    Iterator<Row> rowIterator = sheet.iterator();
                    // 跳过第一行表头
                    if (rowIterator.hasNext()) {
                        rowIterator.next(); // 跳过第一行
                    }
                    int a = 1;
                    while (rowIterator.hasNext()) {
                        Row row = rowIterator.next();
                        // 读取每一行的单元格数据，根据需要进行处理
                        Cell cell1 = row.getCell(1);
                        String cell1pd = String.valueOf(cell1);
                        if (cell1pd == null || cell1pd.equals("")){
                            return Result.OK("已成功导入 " + importedCount + " 条数据。", importedCount);
                        }
                        Cell cell2 = row.getCell(2);
                        Cell cell3 = row.getCell(3);
                        Cell cell4 = row.getCell(4);
                        Cell cell5 = row.getCell(5);
                        Cell cell6 = row.getCell(6);
                        Cell cell7 = row.getCell(7);
                        Cell cell8 = row.getCell(8);
                        Cell cell9 = row.getCell(9);
                        Cell cell10 = row.getCell(10);
                        Cell cell11 = row.getCell(11);
                        Cell cell12 = row.getCell(12);

                        TrGangjinbhcM gangjinbhcM = new TrGangjinbhcM();
                        gangjinbhcM.setShebeiNo(String.valueOf(cell1));
                        gangjinbhcM.setCode(String.valueOf(cell2));
                        gangjinbhcM.setProjectname(String.valueOf(cell3));
                        gangjinbhcM.setSgbw(String.valueOf(cell4));
                        gangjinbhcM.setChecklocation(String.valueOf(cell4));
                        DataFormatter dataFormatter = new DataFormatter();
                        if (cell5 != null) {
                            Date date = null;
                            String dateString = dataFormatter.formatCellValue(cell5);
                            try {
                                date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dateString);
                                gangjinbhcM.setChecktime(date); // 设置日期时间属性为 Date 类型
                            } catch (Exception e) {
                                // 处理日期解析异常
                                e.printStackTrace();
                            }
                        }
                        String s1 = String.valueOf(cell6.toString());
                        int type = 5;
                        switch (s1) {
                            case "规范":
                                type = 5;
                                break;
                            case "精细扫描":
                                type = 6;
                                break;
                            case "波形":
                                type = 2;
                                break;
                            case "剖面":
                                type = 3;
                                break;
                            case "网格":
                                type = 4;
                                break;
                            case "厚度":
                                type = 1;
                                break;
                            default:
                                // 处理未知类型的情况
                                break;
                        }
                        gangjinbhcM.setType(type);
                        gangjinbhcM.setTargettype(String.valueOf(cell7));
                        gangjinbhcM.setZonecount((int) cell8.getNumericCellValue());
                        Double d = cell9.getNumericCellValue();
                        gangjinbhcM.setPassrate(d * 100);
                        gangjinbhcM.setMasterdiameter((int) cell10.getNumericCellValue());
                        gangjinbhcM.setMasterspacing((int) cell11.getNumericCellValue());
                        gangjinbhcM.setDesignthickness((int) cell12.getNumericCellValue());
                        UUID uuid = UUID.randomUUID();
                        gangjinbhcM.setTestid(String.valueOf(uuid));
                        gangjinbhcM.setPoint("DR001");
                        trGangjinbhcMService.save(gangjinbhcM);

                        // 处理超链接单元格
                        if (cell4 != null && cell4.getHyperlink() != null) {
                            Hyperlink hyperlink = cell4.getHyperlink();
                            if (hyperlink.getType() == HyperlinkType.DOCUMENT) {
                                Sheet sheetToImport = workbook.getSheetAt(a); // 获取要导入的工作表
                                Iterator<Row> zbrowIterator = sheetToImport.iterator();
                                // 跳过第一行表头
                                if (zbrowIterator.hasNext()) {
                                    zbrowIterator.next(); // 跳过第一行
                                }
                                while (zbrowIterator.hasNext()) {
                                    Row zbrow = zbrowIterator.next();
                                    // 读取每一行的单元格数据，根据需要进行处理
                                    Cell zbcell2 = zbrow.getCell(2);
                                    String zbcell22 = String.valueOf(zbcell2);
                                    if (zbcell22 == null || zbcell22.equals("")){
                                        break;
                                    }
                                    Cell zbcell3 = zbrow.getCell(3);
                                    Cell zbcell4 = zbrow.getCell(4);

                                    // 在这里处理从超链接工作簿中读取的单元格数据
                                    TrGangjinbhcS gangjinbhcS = new TrGangjinbhcS();
                                    gangjinbhcS.setTestid(String.valueOf(uuid));
                                    FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();

                                    // 将公式计算结果转换为整数
                                    if (zbcell2 != null) {
                                        CellValue cellValue2 = evaluator.evaluate(zbcell2);
                                        //四舍五入
                                        gangjinbhcS.setThickness((int) Math.round(cellValue2.getNumberValue()));
                                    }

                                    String stringCellValue = zbcell3.getStringCellValue();
                                    gangjinbhcS.setStrthickness(stringCellValue);

                                    double columnIndex = zbcell4.getNumericCellValue();
                                    gangjinbhcS.setDesignthickness((int) columnIndex);

                                    trGangjinbhcSService.save(gangjinbhcS);
                                }
                            }
                        }
                        a++;
                        importedCount++;
                    }
                    inputStream.close();
                } catch (Exception e) {
                    System.out.println(e);
                    return Result.OK("已成功导入 " + importedCount + " 条数据。", importedCount);
                }
            }
        }

        return Result.OK("已成功导入 " + importedCount + " 条数据。", importedCount);
    }


    @AutoLog(value = "钢筋保护层曲线")
    @ApiOperation(value = "钢筋保护层曲线", notes = "钢筋保护层曲线")
    @GetMapping(value = "/getgbhgl")
    public Result<?> getgbhgl(String projectid, String targetType,
                              HttpServletRequest req) {
        List<Map> getgbhgl = trGangjinbhcMService.getgbhgl(projectid, targetType);

        return Result.OK(getgbhgl);
    }

}
