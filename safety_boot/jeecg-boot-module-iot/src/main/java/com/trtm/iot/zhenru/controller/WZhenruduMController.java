package com.trtm.iot.zhenru.controller;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.trtm.iot.rhdcx.entity.WRuanhuadianM;
import com.trtm.iot.zhenru.entity.WZhenruduM;
import com.trtm.iot.zhenru.service.IWZhenruduMService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @Description: w_zhenrudu_m
 * @Author: jeecg-boot
 * @Date:   2021-04-26
 * @Version: V1.0
 */
@Api(tags="w_zhenrudu_m")
@RestController
@RequestMapping("/zhenru/wZhenruduM")
@Slf4j
public class WZhenruduMController extends JeecgController<WZhenruduM, IWZhenruduMService> {
	@Autowired
	private IWZhenruduMService wZhenruduMService;
	@Autowired
	private RedisUtil redisUtil;
	/**
	 * 分页列表查询
	 *
	 * @param wZhenruduM
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "w_zhenrudu_m-分页列表查询")
	@ApiOperation(value="w_zhenrudu_m-分页列表查询", notes="w_zhenrudu_m-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(WZhenruduM wZhenruduM,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {

		LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
		String  shebei = String.valueOf(redisUtil.get(loginUser.getId() + "change"));
		if(wZhenruduM.getFsbbh()==null){
			if(!"null".equals(shebei)){
				wZhenruduM.setFsbbh(shebei);
			}else{
				wZhenruduM.setFsbbh("空");
			}
		}
		QueryWrapper<WZhenruduM> queryWrapper = QueryGenerator.initQueryWrapper(wZhenruduM, req.getParameterMap());
		Page<WZhenruduM> page = new Page<WZhenruduM>(pageNo, pageSize);
		IPage<WZhenruduM> pageList = wZhenruduMService.page(page, queryWrapper);
		return Result.OK(pageList);
	}

	/**
	 *   添加
	 *
	 * @param wZhenruduM
	 * @return
	 */
	@AutoLog(value = "w_zhenrudu_m-添加")
	@ApiOperation(value="w_zhenrudu_m-添加", notes="w_zhenrudu_m-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody WZhenruduM wZhenruduM) {
		wZhenruduMService.save(wZhenruduM);
		return Result.OK("添加成功！");
	}

	/**
	 * 对外开放添加数据接口
	 *
	 * @param wZhenruduM
	 * @return
	 */
	@AutoLog(value = "w_zhenrudu_m-添加")
	@ApiOperation(value = "w_zhenrudu_m-添加", notes = "w_zhenrudu_m-添加")
	@PostMapping(value = "/addOpen1")
	public Result<?> addOpen(@RequestBody WZhenruduM wZhenruduM){
		boolean save = wZhenruduMService.save(wZhenruduM);
		if (save) {
			return Result.OK("添加成功！");
		} else {
			return Result.error("添加失败！");
		}
	}
	/**
	 *  编辑
	 *
	 * @param wZhenruduM
	 * @return
	 */
	@AutoLog(value = "w_zhenrudu_m-编辑")
	@ApiOperation(value="w_zhenrudu_m-编辑", notes="w_zhenrudu_m-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody WZhenruduM wZhenruduM) {
		wZhenruduMService.updateById(wZhenruduM);
		return Result.OK("编辑成功!");
	}

	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "w_zhenrudu_m-通过id删除")
	@ApiOperation(value="w_zhenrudu_m-通过id删除", notes="w_zhenrudu_m-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		wZhenruduMService.removeById(id);
		return Result.OK("删除成功!");
	}

	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "w_zhenrudu_m-批量删除")
	@ApiOperation(value="w_zhenrudu_m-批量删除", notes="w_zhenrudu_m-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.wZhenruduMService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "w_zhenrudu_m-通过id查询")
	@ApiOperation(value="w_zhenrudu_m-通过id查询", notes="w_zhenrudu_m-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		WZhenruduM wZhenruduM = wZhenruduMService.getById(id);
		if(wZhenruduM==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(wZhenruduM);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param wZhenruduM
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, WZhenruduM wZhenruduM) {
        return super.exportXls(request, wZhenruduM, WZhenruduM.class, "w_zhenrudu_m");
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
        return super.importExcel(request, response, WZhenruduM.class);
    }

	/**
	 * 针入度接收接口
	 *
	 * @param
	 * @return
	 */
	@AutoLog(value = "针入度接收接口")
	@ApiOperation(value = "针入度接收接口", notes = "针入度接收接口")
	@PostMapping(value = "/addOpen")
	public Result<?> addOpen(@RequestBody String args) {
		JSONObject jsonObject = JSONObject.parseObject(args);
		WZhenruduM t = JSONObject.toJavaObject(jsonObject, WZhenruduM.class);
		t.setSubmittime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		String samplename = t.getSamplename();
		String aging = "";
		if(StringUtils.isNotBlank(samplename)){
			aging = samplename.contains("老化")?aging(t):"";
			//老化值aging大于65为合格
			if(StringUtils.isNotBlank(aging)){
				BigDecimal aging1 = new BigDecimal(aging);
				if(aging1.compareTo(new BigDecimal(65))>=0){
					t.setIsqualified("合格");
				}else{
					t.setIsqualified("不合格");
				}
			}

		}
		t.setAging(aging);
		if(StringUtils.isNotBlank(t.getSyjid())){
			QueryWrapper<WZhenruduM> queryWrapper = new QueryWrapper<WZhenruduM>();
			queryWrapper.eq("syjid",t.getSyjid());
			List<WZhenruduM> list = wZhenruduMService.list(queryWrapper);
			if(list.size()>0){
				boolean update = wZhenruduMService.update(t, queryWrapper);
				if (update) {
					return Result.OK("更新成功！");
				} else {
					return Result.error(201,"更新失败！");
				}
			}else{
				boolean save = wZhenruduMService.save(t);
				if (save) {
					return Result.OK("添加成功！");
				} else {
					return Result.error(201,"添加失败！");
				}
			}
		}else{
			return Result.error(201,"syjid不可为空！");
		}

	}

	private String aging(WZhenruduM t) {
		LambdaQueryWrapper<WZhenruduM> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.eq(WZhenruduM::getSampleno,t.getSampleno())
				.eq(WZhenruduM::getFsbbh,t.getFsbbh())
				.last("limit 1");
		WZhenruduM zhenruduM = wZhenruduMService.getOne(queryWrapper);
		if(zhenruduM!=null){
			BigDecimal zrdavg = zhenruduM.getZrdavg();
			BigDecimal zrdavgOld = t.getZrdavg();
			//求老化比zrdavgOld/zrdavg*100
			BigDecimal divide = zrdavgOld.divide(zrdavg, 2, BigDecimal.ROUND_HALF_UP);
			BigDecimal multiply = divide.multiply(new BigDecimal(100));
			return multiply.toString();
		}
		return "";
	}

}
