package com.trtm.iot.trhnthtm.controller;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.trtm.iot.hnthtconsign.service.IHnthtConsignService;
import com.trtm.iot.trhnthtm.vo.TrHnthtMPage;
import com.trtm.iot.trhnthts.entity.TrHnthtS;
import com.trtm.iot.trhnthts.service.ITrHnthtSService;
import org.apache.poi.ss.usermodel.*;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.RedisUtil;
import com.trtm.iot.trhnthtm.entity.TrHnthtM;
import com.trtm.iot.trhnthtm.service.ITrHnthtMService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

/**
 * @Description: 混凝土回弹主表
 * @Author: jeecg-boot
 * @Date: 2021-09-13
 * @Version: V1.0
 */
@Api(tags = "混凝土回弹主表")
@RestController
@RequestMapping("/trhnthtm/trHnthtM")
@Slf4j
public class TrHnthtMController extends JeecgController<TrHnthtM, ITrHnthtMService> {
    @Autowired
    private ITrHnthtMService trHnthtMService;
    @Autowired
    private ITrHnthtSService trHnthtSService;
    @Autowired
    private IHnthtConsignService hnthtConsignService;
    @Autowired
    private RedisUtil redisUtil;

    /**
     * 分页列表查询
     *
     * @param trHnthtM
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "混凝土回弹主表-分页列表查询")
    @ApiOperation(value = "混凝土回弹主表-分页列表查询", notes = "混凝土回弹主表-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(TrHnthtM trHnthtM,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
        if (trHnthtM.getShebeiNo() == null) {
            if (!"null".equals(shebei)) {
                trHnthtM.setShebeiNo(shebei);
            } else {
                trHnthtM.setShebeiNo("空");
            }
        }
        trHnthtM.setCode("*" + trHnthtM.getCode() + "*");
        QueryWrapper<TrHnthtM> queryWrapper = QueryGenerator.initQueryWrapper(trHnthtM, req.getParameterMap());
        Page<TrHnthtM> page = new Page<TrHnthtM>(pageNo, pageSize);
        IPage<TrHnthtM> pageList = trHnthtMService.page(page, queryWrapper);
        return Result.OK(pageList);
    }


    /**
     * 添加混凝土回弹
     *
     * @param trHnthtMpage
     * @return
     */
    @AutoLog(value = "混凝土回弹主表-添加")
    @ApiOperation(value = "混凝土回弹主表-添加", notes = "混凝土回弹主表-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody TrHnthtMPage trHnthtMpage) {
        TrHnthtM trHnthtM = new TrHnthtM();
        BeanUtils.copyProperties(trHnthtMpage, trHnthtM);

        int i = trHnthtMService.saveMain(trHnthtM, trHnthtMpage.getTrHnthtS());
        if (i == 400) {
            return Result.error("添加失败");
        }
        String code = trHnthtMpage.getCode();
        hnthtConsignService.updateStatus(code);
        if (i == 300) {
            return Result.OK("修改成功");
        }
        return Result.OK("添加成功！");
    }
    @AutoLog(value = "混凝土回弹主表-添加")
    @ApiOperation(value = "混凝土回弹主表-添加", notes = "混凝土回弹主表-添加")
    @PostMapping(value = "/update")
    public Result<?> update(@RequestBody TrHnthtM trHnthtM) {
        String code = trHnthtM.getCode();
        String pdjg = trHnthtM.getPdjg();
        LambdaQueryWrapper<TrHnthtM> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TrHnthtM::getCode, code);
        List<TrHnthtM> list = trHnthtMService.list(queryWrapper);
        if (list.size() == 0) {
            return Result.error("当前code无对应数据！");
        }
        for (TrHnthtM hnthtM : list) {
            hnthtM.setPdjg(pdjg);
            hnthtM.setIsbaogao(1);
            trHnthtMService.updateById(hnthtM);
        }
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param trHnthtM
     * @return
     */
    @AutoLog(value = "混凝土回弹主表-编辑")
    @ApiOperation(value = "混凝土回弹主表-编辑", notes = "混凝土回弹主表-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody TrHnthtM trHnthtM) {
        trHnthtMService.updateById(trHnthtM);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "混凝土回弹主表-通过id删除")
    @ApiOperation(value = "混凝土回弹主表-通过id删除", notes = "混凝土回弹主表-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        trHnthtMService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "混凝土回弹主表-批量删除")
    @ApiOperation(value = "混凝土回弹主表-批量删除", notes = "混凝土回弹主表-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.trHnthtMService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "混凝土回弹主表-通过id查询")
    @ApiOperation(value = "混凝土回弹主表-通过id查询", notes = "混凝土回弹主表-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        TrHnthtM trHnthtM = trHnthtMService.getById(id);
        if (trHnthtM == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(trHnthtM);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param trHnthtM
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, TrHnthtM trHnthtM) {
        return super.exportXls(request, trHnthtM, TrHnthtM.class, "混凝土回弹主表");
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
        for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
            MultipartFile file = entity.getValue();
            if (!file.isEmpty()) {
                try {
                    InputStream inputStream = file.getInputStream();
                    // TODO: 在此处编写读取Excel的代码逻辑，将数据导入到 TrHnthtM 对象中

                    // 示例：使用 Apache POI 进行读取
                    Workbook workbook = WorkbookFactory.create(inputStream);
                    Sheet sheet = workbook.getSheetAt(0); // 假设导入的是第一个工作表
                    Iterator<Row> rowIterator = sheet.iterator();
                    // 跳过第一行表头
                    if (rowIterator.hasNext()) {
                        rowIterator.next(); // 跳过第一行
                    }
                    int a = 0;
                    UUID uuid = null;
                    String jcm = null;
                    Double thz = 0.0;
                    while (rowIterator.hasNext()) {
                        Row row = rowIterator.next();
                        if (row.getLastCellNum() == -1) {
                            System.out.println("当前行为空行");
                            continue;
                        }
                        TrHnthtM hnthtM = new TrHnthtM();
                        a+=1;
                        if (a == 1 || (a-1)%10 == 0) {
                            uuid = UUID.randomUUID();
                            // 读取每一行的单元格数据，根据需要进行处理
                            String cell1 = getCellValueAsString(row.getCell(1));
                            String cell2 = getCellValueAsString(row.getCell(2));
                            String cell3 = getCellValueAsString(row.getCell(3));
                            String cell4 = getCellValueAsString(row.getCell(4));
                            String cell5 = getCellValueAsString(row.getCell(5));
                            String cell6 = getCellValueAsString(row.getCell(6));
                            String cell7 = getCellValueAsString(row.getCell(7));
                            String cell8 = getCellValueAsString(row.getCell(8));
                            String cell9 = getCellValueAsString(row.getCell(9));
                            String cell10 = getCellValueAsString(row.getCell(10));
                            String cell11 = getCellValueAsString(row.getCell(11));
                            String cell12 = getCellValueAsString(row.getCell(12));
                            String cell13 = getCellValueAsString(row.getCell(13));

                            hnthtM.setShebeiNo(cell1);
                            hnthtM.setProjectname(cell2);
                            hnthtM.setSgbw(cell3);
                            Date date = null;
                            if (cell4 != null) {
                                try {
                                    date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(cell4);
                                    hnthtM.setChecktime(date); // 设置日期时间属性为 Date 类型
                                } catch (Exception e) {
                                    // 处理日期解析异常
                                    e.printStackTrace();
                                }
                            }
                            hnthtM.setChecktime(date);

                            hnthtM.setChecklocation(cell5);

                            hnthtM.setPlace(cell6);
                            hnthtM.setStrength(cell7);
                            hnthtM.setCode(cell8);
                            hnthtM.setTestingcount(Integer.getInteger(cell9));
                            thz = Double.valueOf(cell11);
                            hnthtM.setCarbonizedepth(Double.valueOf(cell11));
                            hnthtM.setDetectionangle(cell12);
                            hnthtM.setIspumping(cell13);
                            jcm = String.valueOf(cell10);
                            hnthtM.setDetectionsurface(cell10);
                            hnthtM.setTestid(String.valueOf(uuid));

                            double cell15 = row.getCell(18).getNumericCellValue();
                            hnthtM.setTestingmin(cell15);
                            double cell16 = row.getCell(19).getNumericCellValue();
                            hnthtM.setTestingaverage(cell16);
                            double cell17 = row.getCell(20).getNumericCellValue();
                            hnthtM.setStandarddeviation(cell17);
                            double cell18 = row.getCell(21).getNumericCellValue();
                            hnthtM.setEstimatedvalue(cell18);
                            double cell19 = row.getCell(22).getNumericCellValue();
                            hnthtM.setShowmin(String.valueOf(cell19));
                            double cell20 = row.getCell(23).getNumericCellValue();
                            hnthtM.setShowaverage(String.valueOf(cell20));
                            double cell21 = row.getCell(24).getNumericCellValue();
                            hnthtM.setShowstandarddev(String.valueOf(cell21));
                            double cell22 = row.getCell(25).getNumericCellValue();
                            hnthtM.setShowestimatedval(String.valueOf(cell22));

                            trHnthtMService.save(hnthtM);
                        }
                        TrHnthtS hnthtS = new TrHnthtS();

                        Cell cell14 = row.getCell(14);
                        Cell cell15 = row.getCell(15);
                        Cell cell16 = row.getCell(16);
                        Cell cell17 = row.getCell(17);
                        String cellValue14 = cell14.getStringCellValue();
                        String replace = cellValue14.replace("，", ",");
                        hnthtS.setNumber(replace);

                        hnthtS.setTestid(String.valueOf(uuid));
                        int Surface = 1;
                        if ("侧面".equals(jcm)){
                            Surface = 3;
                        }else if ("底面".equals(jcm)){
                            Surface = 2;
                        }
                        hnthtS.setSurface(Surface);
                        hnthtS.setCarbonization(thz);

                        hnthtS.setAverage(cell15.getNumericCellValue());
                        hnthtS.setCarbonize(cell16.getNumericCellValue());
                        hnthtS.setStrcar(String.valueOf(cell17.getNumericCellValue()));

                        trHnthtSService.save(hnthtS);
                    }
                    inputStream.close();
                } catch (Exception e) {
                    return Result.OK("导入成功");
                }
            }
        }

        return Result.OK("导入成功");
    }
    public String getCellValueAsString(Cell cell) {
        if (cell == null) {
            return "";  // 处理空单元格
        }

        String cellValue = "";
        FormulaEvaluator evaluator = cell.getSheet().getWorkbook().getCreationHelper().createFormulaEvaluator();

        switch (cell.getCellType()) {
            case STRING:
                cellValue = cell.getStringCellValue();
                break;
            case NUMERIC:
                cellValue = String.valueOf(cell.getNumericCellValue());
                break;
            case BOOLEAN:
                cellValue = String.valueOf(cell.getBooleanCellValue());
                break;
            case FORMULA:
                // 使用公式求值器评估公式
                switch (evaluator.evaluateFormulaCell(cell)) {
                    case STRING:
                        cellValue = cell.getStringCellValue(); // 使用单元格的 getStringCellValue()
                        break;
                    case NUMERIC:
                        cellValue = String.valueOf(cell.getNumericCellValue()); // 使用单元格的 getNumericCellValue()
                        break;
                    case BOOLEAN:
                        cellValue = String.valueOf(cell.getBooleanCellValue()); // 使用单元格的 getBooleanCellValue()
                        break;
                    case BLANK:
                        cellValue = "";  // 处理空值
                        break;
                    case ERROR:
                    default:
                        cellValue = "";  // 处理错误情况
                        break;
                }
                break;
            case BLANK:
                cellValue = "";
                break;
            default:
                cellValue = "Unsupported Cell Type";
                break;
        }

        return cellValue;
    }
}
