package com.trtm.iot.whwgpy.controller;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import com.trtm.iot.whwgpy.entity.WHwgpy;
import com.trtm.iot.whwgpy.service.IWHwgpyService;

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
 * @Description: w_hwgpy
 * @Author: jeecg-boot
 * @Date:   2023-04-26
 * @Version: V1.0
 */
@Api(tags="w_hwgpy")
@RestController
@RequestMapping("/whwgpy/wHwgpy")
@Slf4j
public class WHwgpyController extends JeecgController<WHwgpy, IWHwgpyService> {
	@Autowired
	private IWHwgpyService wHwgpyService;
	
	/**
	 * 分页列表查询
	 *
	 * @param wHwgpy
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "w_hwgpy-分页列表查询")
	@ApiOperation(value="w_hwgpy-分页列表查询", notes="w_hwgpy-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(WHwgpy wHwgpy,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<WHwgpy> queryWrapper = QueryGenerator.initQueryWrapper(wHwgpy, req.getParameterMap());
		Page<WHwgpy> page = new Page<WHwgpy>(pageNo, pageSize);
		IPage<WHwgpy> pageList = wHwgpyService.page(page, queryWrapper);
		List<WHwgpy> records = pageList.getRecords();
		for (WHwgpy record : records) {
			byte[] bytes = record.getLighfile();
			if (bytes!=null) {
				// 获取文件内容并保存到字符串中
				String fileContent = StandardCharsets.UTF_8.decode(ByteBuffer.wrap(bytes)).toString();


				// 处理科学计数法
				String processedContent = convertScientificNotation(fileContent);

				// 输出原始字符串
				System.out.println("原始字符串: " + fileContent);
				System.out.println("原始字符串: " + processedContent);

				// 替换换行符为分号
				fileContent = processedContent.replaceAll("\\r?\\n", ";");

				record.setLighfileString(fileContent);
			}
		}
		return Result.OK(pageList);
	}
	 private static String convertScientificNotation(String input) {
		 // 正则表达式匹配科学计数法格式的数字
		 String regex = "([+-]?\\d*\\.\\d+([eE][+-]?\\d+)?)";
		 Pattern pattern = Pattern.compile(regex);
		 Matcher matcher = pattern.matcher(input);

		 StringBuffer sb = new StringBuffer();

		 while (matcher.find()) {
			 // 将科学计数法转换为浮点数
			 double value = Double.parseDouble(matcher.group());
			 if (value != 0) {
				 // 使用DecimalFormat格式化浮点数，保留小数点后6位
				 DecimalFormat df = new DecimalFormat("#.########");
				 String formattedValue = df.format(value);

				 // 将格式化后的浮点数替换回原始字符串
				 matcher.appendReplacement(sb, formattedValue);
 			 }
		 }
		 matcher.appendTail(sb);

		 return sb.toString();
	 }
	/**
	 *   添加
	 *
	 * @param wHwgpy
	 * @return
	 */
	@AutoLog(value = "w_hwgpy-添加")
	@ApiOperation(value="w_hwgpy-添加", notes="w_hwgpy-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody WHwgpy wHwgpy) {
		wHwgpyService.save(wHwgpy);
		return Result.OK("添加成功！");
	}
	@AutoLog(value = "w_hwgpy-添加对外开放")
	@ApiOperation(value="w_hwgpy-添加对外开放", notes="w_hwgpy-添加对外开放")
	@PostMapping(value = "/addOpen")
	public Result<?> addOpen(@RequestParam("customer") String customer,
							@RequestParam("projectId") Integer projectId,
							@RequestParam("projectName") String projectName,
							@RequestParam("product") String product,
							@RequestParam("grade") String grade,
							@RequestParam("source") String source,
							@RequestParam("result") Integer result,
							@RequestParam("datetime") String datetimeStr,
							@RequestParam("type") Integer type,
							@RequestParam("access") String access,
							@RequestParam("sbsAccess") String sbsAccess,
							@RequestParam("lighFile") MultipartFile lighFile, HttpServletResponse response) {
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date datetime = dateFormat.parse(datetimeStr);

			WHwgpy whwgpyEntity = new WHwgpy();
			whwgpyEntity.setCustomer(customer);
			whwgpyEntity.setProjectid(projectId);
			whwgpyEntity.setProjectname(projectName);
			whwgpyEntity.setProduct(product);
			whwgpyEntity.setGrade(grade);
			whwgpyEntity.setSource(source);
			whwgpyEntity.setResult(result);
			whwgpyEntity.setDatetime(datetime);
			whwgpyEntity.setType(type);
			whwgpyEntity.setAccess(access);
			whwgpyEntity.setSbsaccess(sbsAccess);

			byte[] bytes = lighFile.getBytes();

			whwgpyEntity.setLighfile(bytes);

			wHwgpyService.save(whwgpyEntity);
		} catch (Exception e) {
			return Result.error("数据异常！"+e);
		}
		return Result.OK("接收成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param wHwgpy
	 * @return
	 */
	@AutoLog(value = "w_hwgpy-编辑")
	@ApiOperation(value="w_hwgpy-编辑", notes="w_hwgpy-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody WHwgpy wHwgpy) {
		wHwgpyService.updateById(wHwgpy);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "w_hwgpy-通过id删除")
	@ApiOperation(value="w_hwgpy-通过id删除", notes="w_hwgpy-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		wHwgpyService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "w_hwgpy-批量删除")
	@ApiOperation(value="w_hwgpy-批量删除", notes="w_hwgpy-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.wHwgpyService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "w_hwgpy-通过id查询")
	@ApiOperation(value="w_hwgpy-通过id查询", notes="w_hwgpy-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		WHwgpy wHwgpy = wHwgpyService.getById(id);
		if(wHwgpy==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(wHwgpy);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param wHwgpy
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, WHwgpy wHwgpy) {
        return super.exportXls(request, wHwgpy, WHwgpy.class, "w_hwgpy");
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
        return super.importExcel(request, response, WHwgpy.class);
    }

}
