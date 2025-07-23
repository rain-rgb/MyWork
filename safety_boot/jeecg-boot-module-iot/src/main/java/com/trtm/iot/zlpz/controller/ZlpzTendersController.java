package com.trtm.iot.zlpz.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.trtm.iot.zlpz.entity.ZlpzTenders;
import com.trtm.iot.zlpz.service.IZlpzTendersService;
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
import java.util.List;

/**
 * @Description: zlpz_tenders
 * @Author: jeecg-boot
 * @Date:   2023-03-20
 * @Version: V1.0
 */
@Api(tags="zlpz_tenders")
@RestController
@RequestMapping("/zlpz/zlpzTenders")
@Slf4j
public class ZlpzTendersController extends JeecgController<ZlpzTenders, IZlpzTendersService> {
	@Autowired
	private IZlpzTendersService zlpzTendersService;

	/**
	 * 分页列表查询
	 *
	 * @param zlpzTenders
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "zlpz_tenders-分页列表查询")
	@ApiOperation(value="zlpz_tenders-分页列表查询", notes="zlpz_tenders-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(ZlpzTenders zlpzTenders,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<ZlpzTenders> queryWrapper = QueryGenerator.initQueryWrapper(zlpzTenders, req.getParameterMap());
		Page<ZlpzTenders> page = new Page<ZlpzTenders>(pageNo, pageSize);
		IPage<ZlpzTenders> pageList = zlpzTendersService.page(page, queryWrapper);
		return Result.OK(pageList);
	}

	/**
	 *   添加
	 *
	 * @param zlpzTenders
	 * @return
	 */
	@AutoLog(value = "zlpz_tenders-添加")
	@ApiOperation(value="zlpz_tenders-添加", notes="zlpz_tenders-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody ZlpzTenders zlpzTenders) {
		zlpzTendersService.save(zlpzTenders);
		return Result.OK("添加成功！");
	}

	/**
	 *  编辑
	 *
	 * @param zlpzTenders
	 * @return
	 */
	@AutoLog(value = "zlpz_tenders-编辑")
	@ApiOperation(value="zlpz_tenders-编辑", notes="zlpz_tenders-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody ZlpzTenders zlpzTenders) {
		zlpzTendersService.updateById(zlpzTenders);
		return Result.OK("编辑成功!");
	}

	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "zlpz_tenders-通过id删除")
	@ApiOperation(value="zlpz_tenders-通过id删除", notes="zlpz_tenders-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		zlpzTendersService.removeById(id);
		return Result.OK("删除成功!");
	}

	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "zlpz_tenders-批量删除")
	@ApiOperation(value="zlpz_tenders-批量删除", notes="zlpz_tenders-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.zlpzTendersService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "zlpz_tenders-通过id查询")
	@ApiOperation(value="zlpz_tenders-通过id查询", notes="zlpz_tenders-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		ZlpzTenders zlpzTenders = zlpzTendersService.getById(id);
		if(zlpzTenders==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(zlpzTenders);
	}

	/**
	 * 通过xmid查询
	 *
	 * @param id
	 * @return
	 */
	@GetMapping(value = "/queryByXmId")
	public Result<?> queryByXmId(String project) {
		LambdaQueryWrapper<ZlpzTenders> queryWrapper = new LambdaQueryWrapper<ZlpzTenders>();
		queryWrapper.eq(ZlpzTenders::getXmid,project);
		List<ZlpzTenders> list = zlpzTendersService.list(queryWrapper);

		if(list==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(list);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param zlpzTenders
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, ZlpzTenders zlpzTenders) {
        return super.exportXls(request, zlpzTenders, ZlpzTenders.class, "zlpz_tenders");
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
        return super.importExcel(request, response, ZlpzTenders.class);
    }


}
