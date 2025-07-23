package org.jeecg.modules.system.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.util.SqlInjectionUtil;
import org.jeecg.modules.system.mapper.SysDictMapper;
import org.jeecg.modules.system.model.DuplicateCheckVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * @Title: DuplicateCheckAction
 * @Description: 重复校验工具
 * @Author 张代浩
 * @Date 2019-03-25
 * @Version V1.0
 */
@Slf4j
@RestController
@RequestMapping("/sys/duplicate")
@Api(tags="重复校验")
public class DuplicateCheckController {

	@Autowired
	SysDictMapper sysDictMapper;

	/**
	 * 校验数据是否在系统中是否存在
	 *
	 * @return
	 */
	@RequestMapping(value = "/check", method = RequestMethod.GET)
	@ApiOperation("重复校验接口")
	public Result<Object> doDuplicateCheck(DuplicateCheckVo duplicateCheckVo, HttpServletRequest request) {
		Long num = null;

		//log.info("----duplicate check------："+ duplicateCheckVo.toString());
		//关联表字典（举例：sys_user,realname,id）
		//SQL注入校验（只限制非法串改数据库）
		final String[] sqlInjCheck = {duplicateCheckVo.getTableName(),duplicateCheckVo.getFieldName()};
		SqlInjectionUtil.filterContent(sqlInjCheck);
		if (StringUtils.isNotBlank(duplicateCheckVo.getDataId())) {
			// [2].编辑页面校验
			num = sysDictMapper.duplicateCheckCountSql(duplicateCheckVo);
		} else {
			// [1].添加页面校验
			num = sysDictMapper.duplicateCheckCountSqlNoDataId(duplicateCheckVo);
		}

		if (num == null || num == 0) {
			// 该值可用
			return Result.ok("该值可用！");
		} else {
			// 该值不可用
			log.info("该值不可用，系统中已存在！");
			return Result.error("该值不可用，系统中已存在！");
		}
	}

	/**
	 * //养护货架配置重复校验
	 *
	 * @return
	 */
	@RequestMapping(value = "/maintenan", method = RequestMethod.GET)
	@ApiOperation("重复校验接口")
	public Result<Object> doDuplicateCheck1(DuplicateCheckVo duplicateCheckVo, HttpServletRequest request) {
		Long num = null;

		//log.info("----duplicate check111111111111111------："+ duplicateCheckVo.toString());
		if (StringUtils.isNotBlank(duplicateCheckVo.getDataId())) {
			// [2].编辑页面校验
			num = sysDictMapper.maintenanceconfiguration(duplicateCheckVo);
			log.info("编辑页面");
		} else {
			// [1].添加页面校验
			num = sysDictMapper.maintenanceconfigurationNoDataId(duplicateCheckVo);
			log.info("添加页面");
		}

		if (num == null || num == 0) {
			// 该值可用
			return Result.ok("该值可用！");
		} else {
			// 该值不可用
			log.info("该值不可用，系统中已存在！");
			return Result.error("该值不可用，系统中已存在！");
		}
	}


	/**
	 * //制梁任务单编号/施工部位配置重复校验
	 *
	 * @return
	 */
	@RequestMapping(value = "/beamrwdcode", method = RequestMethod.GET)
	@ApiOperation("重复校验接口")
	public Result<Object> doDuplicateCheck3(DuplicateCheckVo duplicateCheckVo, HttpServletRequest request) {
		Long num = null;

		//log.info("----duplicate check333333333333------："+ duplicateCheckVo.toString());
		if (StringUtils.isNotBlank(duplicateCheckVo.getDataId())) {
			// [2].编辑页面校验
			num = sysDictMapper.beamrwdcodeCheckCount(duplicateCheckVo);
			log.info("编辑页面");
		} else {
			// [1].添加页面校验
			num = sysDictMapper.beamrwdcodeCheckCountNoDataId(duplicateCheckVo);
			log.info("添加页面");
		}

		if (num == null || num == 0) {
			// 该值可用
			return Result.ok("该值可用！");
		} else {
			// 该值不可用
			log.info("该值不可用，系统中已存在！");
			return Result.error("该值不可用，系统中已存在！");
		}
	}

	/**
	 * //梁列配置重复校验
	 *
	 * @return
	 */
	@RequestMapping(value = "/beamlie", method = RequestMethod.GET)
	@ApiOperation("重复校验接口")
	public Result<Object> doDuplicateCheck2(DuplicateCheckVo duplicateCheckVo, HttpServletRequest request) {
		Long num = null;

		//log.info("----duplicate check2222222222222------："+ duplicateCheckVo.toString());
		if (StringUtils.isNotBlank(duplicateCheckVo.getDataId())) {
			// [2].编辑页面校验
			num = sysDictMapper.beamlieconfiguration(duplicateCheckVo);
			log.info("编辑页面");
		} else {
			// [1].添加页面校验
			num = sysDictMapper.beamlieconfigurationNoDataId(duplicateCheckVo);
			log.info("添加页面");
		}

		if (num == null || num == 0) {
			// 该值可用
			return Result.ok("该值可用！");
		} else {
			// 该值不可用
			log.info("该值不可用，系统中已存在！");
			return Result.error("该值不可用，系统中已存在！");
		}
	}

	/**
	 * //梁座名称配置重复校验
	 *
	 * @return
	 */
	@RequestMapping(value = "/beamseat", method = RequestMethod.GET)
	@ApiOperation("重复校验接口")
	public Result<Object> doDuplicateCheck4(DuplicateCheckVo duplicateCheckVo, HttpServletRequest request) {
		Long num = null;

		//log.info("----duplicate check4444444444------："+ duplicateCheckVo.toString());
		if (StringUtils.isNotBlank(duplicateCheckVo.getDataId())) {
			// [2].编辑页面校验
			num = sysDictMapper.beamseatconfiguration(duplicateCheckVo);
			log.info("编辑页面");
		} else {
			// [1].添加页面校验
			num = sysDictMapper.beamseatconfigurationNoDataId(duplicateCheckVo);
			log.info("添加页面");
		}

		if (num == null || num == 0) {
			// 该值可用
			return Result.ok("该值可用！");
		} else {
			// 该值不可用
			log.info("该值不可用，系统中已存在！");
			return Result.error("该值不可用，系统中已存在！");
		}
	}
	/**
	 * //制梁台座编号重复校验
	 *
	 * @return
	 */
	@RequestMapping(value = "/beamcode", method = RequestMethod.GET)
	@ApiOperation("重复校验接口")
	public Result<Object> doDuplicateCheck5(DuplicateCheckVo duplicateCheckVo, HttpServletRequest request) {
		Long num = null;

		//log.info("----duplicate check5555555------："+ duplicateCheckVo.toString());
		if (StringUtils.isNotBlank(duplicateCheckVo.getDataId())) {
			// [2].编辑页面校验
			num = sysDictMapper.beamcodeconfiguration(duplicateCheckVo);
			log.info("编辑页面");
		} else {
			// [1].添加页面校验
			num = sysDictMapper.beamcodeconfigurationNoDataId(duplicateCheckVo);
		}

		if (num == null || num == 0) {
			// 该值可用
			return Result.ok("该值可用！");
		} else {
			// 该值不可用
			log.info("该值不可用，系统中已存在！");
			return Result.error("该值不可用，系统中已存在！");
		}
	}

	/**
	 * //沥青筛分试验重复校验
	 *
	 * @return
	 */
	@RequestMapping(value = "/shaifensy", method = RequestMethod.GET)
	@ApiOperation("重复校验接口")
	public Result<Object> doDuplicateCheck6(DuplicateCheckVo duplicateCheckVo, HttpServletRequest request) {
		Long num = null;
		//log.info("----duplicate check66666666------："+ duplicateCheckVo.toString());
		if (StringUtils.isNotBlank(duplicateCheckVo.getDataId())) {
			// [2].编辑页面校验
			num = sysDictMapper.shaifensyconfiguration(duplicateCheckVo);
			log.info("编辑页面");
		} else {
			// [1].添加页面校验
			num = sysDictMapper.shaifensyconfigurationNoDataId(duplicateCheckVo);
		}

		if (num == null || num == 0) {
			// 该值可用
			return Result.ok("该值可用！");
		} else {
			// 该值不可用
			log.info("该值不可用，系统中已存在！");
			return Result.error("该值不可用，系统中已存在！");
		}
	}
}
