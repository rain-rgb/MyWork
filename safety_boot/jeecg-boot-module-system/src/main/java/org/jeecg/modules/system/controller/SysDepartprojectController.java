package org.jeecg.modules.system.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.exceptions.ClientException;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.beamorder.entity.BeamOrder;
import com.trtm.iot.beamorder.service.IBeamOrderService;
import com.trtm.iot.bhzcfg.entity.BhzPhone;
import com.trtm.iot.bhzcfg.service.IBhzPhoneService;
import com.trtm.iot.car.entity.SchedulingCar;
import com.trtm.iot.car.service.ISchedulingCarService;
import com.trtm.iot.cunliangprocedureconfig.service.ICunliangProcedureConfigService;
import com.trtm.iot.devicemixpilerwd.entity.DeviceMixpileRwd;
import com.trtm.iot.devicemixpilerwd.service.IDeviceMixpileRwdService;
import com.trtm.iot.hc_project.entity.HcProject;
import com.trtm.iot.hc_project.service.IHcProjectService;
import com.trtm.iot.hc_section.entity.HcSection;
import com.trtm.iot.hc_section.service.IHcSectionService;
import com.trtm.iot.hctfysworkareaconfiguration.entity.HcTfysworkareaConfiguration;
import com.trtm.iot.hctfysworkareapeiz.entity.HcTfysworkareapeiz;
import com.trtm.iot.hctfysworkareapeiz.service.IHcTfysworkareapeizService;
import com.trtm.iot.hnthtconsign.entity.HnthtConsign;
import com.trtm.iot.hnthtconsign.service.IHnthtConsignService;
import com.trtm.iot.liangjiasheguanli.entity.LiangJiasheguanli;
import com.trtm.iot.liangjiasheguanli.service.ILiangJiasheguanliService;
import com.trtm.iot.syj.entity.TSyjzb;
import com.trtm.iot.syj.service.ITSyjzbService;
import com.trtm.iot.system.entity.Bhzrenwudan;
import com.trtm.iot.system.entity.ShebeiInfo;
import com.trtm.iot.system.service.IBhzrenwudanService;
import com.trtm.iot.system.service.IShebeiInfoService;
import com.trtm.iot.trzhanglarenwudan.entity.TrZhanglaRenwudan;
import com.trtm.iot.trzhanglarenwudan.service.ITrZhanglaRenwudanService;
import com.trtm.iot.yajiangrenwudan.entity.TrYajiangRenwudan;
import com.trtm.iot.yajiangrenwudan.service.ITrYajiangRenwudanService;
import com.trtm.iot.zhilianggongxu.entity.ZhiliangGongxu;
import com.trtm.iot.zhilianggongxu.service.IZhiliangGongxuService;
import com.trtm.iot.zhiliangqrcode.service.IZhiliangqrcodeService;
import com.trtm.iot.zhiliangrenwudan.entity.Zhiliangrenwudan;
import com.trtm.iot.zhiliangrenwudan.service.IZhiliangrenwudanService;
import com.trtm.iot.zhiliangrenwudan.vo.ZhiliangrenwudanPage;
import com.xkcoding.http.util.StringUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.constant.CacheConstant;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.system.message.entity.SysMessageCore;
import org.jeecg.common.system.message.service.ISysMessageCoreService;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.util.JwtUtil;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.*;
import org.jeecg.modules.message.entity.SysMessage;
import org.jeecg.modules.message.handle.ISendMsgHandle;
import org.jeecg.modules.message.handle.enums.SendMsgStatusEnum;
import org.jeecg.modules.message.handle.enums.SendMsgTypeEnum;
import org.jeecg.modules.message.service.ISysMessageService;
import org.jeecg.modules.system.entity.SysDepart;
import org.jeecg.modules.system.entity.SysDepartproject;
import org.jeecg.modules.system.entity.SysDepartprojectgml;
import org.jeecg.modules.system.entity.SysUser;
import org.jeecg.modules.system.model.DepartIdprojectModel;
import org.jeecg.modules.system.model.SysDepartTreeprojectModel;
import org.jeecg.modules.system.service.ISysDepartService;
import org.jeecg.modules.system.service.ISysDepartprojectService;
import org.jeecg.modules.system.service.ISysUserDepartService;
import org.jeecg.modules.system.service.ISysUserService;
import org.jeecg.modules.system.service.impl.SysBaseApiImpl;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 分部分项 前端控制器
 * <p>
 *
 * @Author: Steve @Since： 2019-01-22
 */
@RestController
@RequestMapping("/sys/sysDepartproject")
@Slf4j
@Api(tags = "分部分项 前端控制器")
public class SysDepartprojectController {
    @Autowired
    private ISchedulingCarService schedulingCarService;
    @Autowired
    private ISysDepartprojectService sysDepartprojectService;
    @Autowired
    public RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private ISysUserService sysUserService;
    @Autowired
    private ISysUserDepartService sysUserDepartService;
    @Autowired
    private ISysDepartService sysDepartService;
    @Autowired
    private IBhzrenwudanService bhzrenwudanService;
    @Autowired
    private IHnthtConsignService hnthtConsignService;
    @Autowired
    private IZhiliangrenwudanService zhiliangrenwudanService;
    @Autowired
    private ITrZhanglaRenwudanService trZhanglaRenwudanService;
    @Autowired
    private ITrYajiangRenwudanService trYajiangRenwudanService;
    @Autowired
    private IZhiliangGongxuService zhiliangGongxuService;
    @Autowired
    private IShebeiInfoService shebeiInfoService;
    @Autowired
    private ICunliangProcedureConfigService cunliangProcedureConfigService;
    @Autowired
    private IBhzPhoneService bhzPhoneService;
    @Autowired
    private IDeviceMixpileRwdService deviceMixpileRwdService;
    @Autowired
    private IBeamOrderService beamOrderService;
    @Autowired
    private SysBaseApiImpl sysBaseApi;
    @Autowired
    private ISysMessageService sysMessageService;
    private volatile String Code = "2022";
    @Autowired
    private IZhiliangqrcodeService zhiliangqrcodeService;
    @Autowired
    private ILiangJiasheguanliService liangJiasheguanliService;
    @Autowired
    private ITSyjzbService syjzbService;
    @Autowired
    private ISysMessageCoreService sysMessageCoreService;
    @Autowired
    private IHcTfysworkareapeizService hcTfysworkareapeizService;
    @Autowired
    private IHcSectionService hcSectionService;
    @Autowired
    private IHcProjectService hcProjectService;
    @Autowired
    private IHcProjectService projectService;
    /**
     *   土方添加
     *
     * @param hcTfysworkareapeiz
     * @return
     */
    @AutoLog(value = "土方压实配置表-添加")
    @ApiOperation(value="土方压实配置表-添加", notes="土方压实配置表-添加")
    @PostMapping(value = "/tfadd")
    public Result<?> tfadd(@RequestBody HcTfysworkareapeiz hcTfysworkareapeiz) throws ClientException {
        String projgrade = hcTfysworkareapeiz.getMileageid();
        if (StrUtil.isNotBlank(projgrade)) {
            String A = projgrade.substring(0, 1);
            int y = projgrade.indexOf(A, projgrade.indexOf(A) + 1);
            String projgrade1 = projgrade.substring(0, y);
            String projectname = "";
            int j = 0;
            for (int x = projgrade.length(); x > 0; x = x - y) {
                String projectnames = projgrade.substring(0, x);
                SysDepartproject selectprojname = sysDepartprojectService.selectprojname(projectnames);
                if (selectprojname != null) {
                    if (selectprojname.getWbsStructureType() != null){
                        projectname = selectprojname.getDepartName();
                        if (j==0) {
                            hcTfysworkareapeiz.setMileage(projectname);//桩号
                        }else if (selectprojname.getOrgType().equals("3")){
                            hcTfysworkareapeiz.setXiangmu(projectname);
                        }else if (selectprojname.getOrgType().equals("6")){
                            hcTfysworkareapeiz.setBiaoduan(projectname);
                        }else if (selectprojname.getOrgType().equals("9")){
                            hcTfysworkareapeiz.setDanwei(projectname);
                        }
                    }
                }
                j++;
            }
        }
        QueryWrapper<HcSection> queryWrapper2 = new QueryWrapper<>();
        queryWrapper2.eq("sectionId",hcTfysworkareapeiz.getSectionid());
        HcSection one1 = hcSectionService.getOne(queryWrapper2);
        QueryWrapper<HcProject> queryWrapper3 = new QueryWrapper<>();
        queryWrapper3.eq("pjId",hcTfysworkareapeiz.getProjectid());
        HcProject one2 = hcProjectService.getOne(queryWrapper3);

        String startstation1 = getStartstation(hcTfysworkareapeiz.getStartstation());
        String endstation1 = getEndstation(hcTfysworkareapeiz.getEndstation());
        String getSbjno = startstation1 +"-"+ endstation1;
        String overReason = "路基施工任务单已下发、请相关管理人员做好监督！";
        try {
            fsyjxx(hcTfysworkareapeiz,one2.getPjname()+one1.getSectionname(),getSbjno,overReason);
        } catch (ClientException e) {
            e.printStackTrace();
        }

        hcTfysworkareapeizService.save(hcTfysworkareapeiz);
        return Result.OK("添加成功！");
    }

    // 处置人推送
    public void fsyjxx(HcTfysworkareapeiz one1, String name, String getSbjno, String overReason) throws ClientException {
        //超过三十五
        if (overReason.length() > 35) {
            overReason = overReason.substring(0,32)+"...";
        }
        Date date = new Date();
        SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        QueryWrapper<HcProject> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("pjId",one1.getProjectid());
        HcProject one = projectService.getOne(queryWrapper);
        com.alibaba.fastjson.JSONObject obj = new JSONObject();
        obj.put("sbname", name);//标段名称
        obj.put("time", sdf.format(date));//推送时间
//        obj.put("time", getEndtime);//推送时间
        obj.put("startstation", getSbjno);//施工桩号
        obj.put("interval", one1.getStarttimes());//施工时间
        obj.put("content", overReason);
        SysMessage sysMessage3 = new SysMessage();
        sysMessage3.setEsTitle(one.getPjname()+"超标预警");
        sysMessage3.setEsType("1");//短信类型  1短信
        BhzPhone bhzPhone = bhzPhoneService.selectBhzPhone(one1.getConstruction());
        BhzPhone bhzPhone1 = bhzPhoneService.selectBhzPhone(one1.getSupervisory());
        String phone = bhzPhone.getPhones()+","+bhzPhone1.getPhones();
        sysMessage3.setEsReceiver(phone);//手机号
        sysMessage3.setEsContent(obj.toString());//短信内容
        sysMessage3.setEsSendStatus("0");//推送状态0未推送
        sysMessage3.setEsSendNum(0);//推送总次数
        sysMessage3.setRemark(one.getPjname());
        sysMessage3.setCreateTime(new Date());

        boolean b = DySmsHelper.sendSms(phone, obj, DySmsEnum.SMS_CB_TFYSPZ);
        if (b){
            sysMessage3.setEsSendNum(1);
            log.info("拌合站超时预警短信发送成功"+obj);
        }else {
            sysMessage3.setEsSendNum(2);
            log.info("拌合站超时预警短信发送失败"+obj);
        }
        sysMessageService.save(sysMessage3);
    }

    public String getStartstation(String startstation){
        String startsta = null;
        if (startstation != null){
            if(startstation.length() == 2){
                startsta = "K0+0" + startstation;
            }else if (startstation.length() == 3){
                startsta = "K0+" + startstation;
            }else if (startstation.length() > 3){
                String substring = startstation.substring(0, startstation.length() - 3);
                String substring1 = startstation.substring(startstation.length() - 3);
                startsta = "K" + substring + "+" + substring1;
            }
        }
        return startsta;
    }

    public String getEndstation(String endstation){
        String startsta = null;
        if (endstation != null){
            if(endstation.length() == 2){
                startsta = "K0+0" + endstation;
            }else if (endstation.length() == 3){
                startsta = "K0+" + endstation;
            }else if (endstation.length() > 3){
                String substring = endstation.substring(0, endstation.length() - 3);
                String substring1 = endstation.substring(endstation.length() - 3);
                startsta = "K" + substring + "+" + substring1;
            }
        }
        return startsta;
    }

    /**
     * 查询数据 查出我的分部分项,并以树结构数据格式响应给前端
     *
     * @return
     */
    @ApiOperation(value = "查询数据 查出我的分部分项,并以树结构数据格式响应给前端", notes = "查询数据 查出我的分部分项,并以树结构数据格式响应给前端")
    @RequestMapping(value = "/querygml", method = RequestMethod.GET)
    public Result<?> querygml(String orgCode) {
        QueryWrapper<SysDepartproject> queryWrapper = new QueryWrapper<>();
        queryWrapper.likeRight("org_code",orgCode);
        queryWrapper.eq("wbs_structure_type","跨");
        List<SysDepartprojectgml> list3 = new ArrayList<>();
        List<SysDepartproject> list = sysDepartprojectService.list(queryWrapper);
        if(list.size() > 0){
            for (SysDepartproject sysDepartproject :list){
                SysDepartprojectgml sysDepartprojectgml = new SysDepartprojectgml();
                QueryWrapper<SysDepartproject> queryWrapper1 = new QueryWrapper<>();
                queryWrapper1.likeRight("org_code",sysDepartproject.getOrgCode());
                queryWrapper1.eq("wbs_structure_type","梁（板）预制");
                List<SysDepartproject> list1 = sysDepartprojectService.list(queryWrapper1);
                int js = 0;
                if (list1.size() > 0){
                    for (SysDepartproject sysDepart :list1){
                        sysDepart.setDelFlag("0");
                        QueryWrapper<Zhiliangrenwudan> queryWrapper2 = new QueryWrapper<>();
                        queryWrapper2.eq("ProjGrade",sysDepart.getOrgCode());
                        List<Zhiliangrenwudan> list2 = zhiliangrenwudanService.list(queryWrapper2);
                        if (list2.size() > 0){
                            Zhiliangrenwudan zhiliangrenwudan = list2.get(0);
                            ZhiliangGongxu selectuuid = zhiliangGongxuService.selectuuid(zhiliangrenwudan.getUuid(), 7);
                            if (selectuuid != null){
                                if (selectuuid.getStatus() == 2){
                                    sysDepart.setDelFlag("2");
                                    js ++;
                                }
                            }
                        }
                    }
                    if (js == 0){
                        sysDepartproject.setDelFlag("0");
                    }else if(js < list1.size()){
                        sysDepartproject.setDelFlag("1");
                    }else {
                        sysDepartproject.setDelFlag("2");
                    }
                }
                BeanUtils.copyProperties(sysDepartproject,sysDepartprojectgml);
                sysDepartprojectgml.setSysdplist(list1);
                list3.add(sysDepartprojectgml);
            }
        }
        return Result.OK(list3);
    }

    /**
     * 查询数据 查出我的分部分项,并以树结构数据格式响应给前端
     *
     * @return
     */
    @ApiOperation(value = "查询数据 查出我的分部分项,并以树结构数据格式响应给前端", notes = "查询数据 查出我的分部分项,并以树结构数据格式响应给前端")
    @RequestMapping(value = "/queryMyDeptTreeList", method = RequestMethod.GET)
    public Result<List<SysDepartTreeprojectModel>> queryMyDeptTreeList(String parentId) {
        Result<List<SysDepartTreeprojectModel>> result = new Result<>();
        LoginUser user = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        try {
            if (oConvertUtils.isNotEmpty(user.getUserIdentity()) && user.getUserIdentity().equals(CommonConstant.USER_IDENTITY_2)) {
                List<SysDepart> selectqueryorgcode = sysDepartService.selectqueryorgcode(user.getDepartIds());
                String orgcodes = "";
                for (SysDepart sysDepart : selectqueryorgcode) {
                    if (orgcodes.equals("")) {
                        orgcodes = "" + sysDepart.getOrgCode() + "";
                    } else {
                        orgcodes = orgcodes + "," + sysDepart.getOrgCode() + "";
                    }
                }
                List<SysDepartTreeprojectModel> list = sysDepartprojectService.queryMyDeptTreeList(orgcodes, parentId);
                result.setResult(list);
                result.setMessage(CommonConstant.USER_IDENTITY_2.toString());
                result.setSuccess(true);
            } else {
                result.setMessage(CommonConstant.USER_IDENTITY_1.toString());
                result.setSuccess(true);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return result;
    }

    /**
     * 查询数据 查出所有部门,并以树结构数据格式响应给前端（查询所有的父节点）
     *
     * @return
     */
    @ApiOperation(value = "查询数据 查出所有部门,并以树结构数据格式响应给前端（查询所有的父节点）", notes = "查询数据 查出所有部门,并以树结构数据格式响应给前端（查询所有的父节点）")
    @RequestMapping(value = "/queryTreeList", method = RequestMethod.GET)
    public Result<List<SysDepartTreeprojectModel>> queryTreeList(String parentId) {
        Result<List<SysDepartTreeprojectModel>> result = new Result<>();
        LoginUser user = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        String orgCode = user.getOrgCode();
        try {
            // 从内存中读取
//			List<SysDepartTreeModel> list =FindsDepartsChildrenUtil.getSysDepartTreeList();
//			if (CollectionUtils.isEmpty(list)) {
//				list = sysDepartService.queryTreeList();
//			}
            List<SysDepartTreeprojectModel> list = sysDepartprojectService.queryTreeList(parentId, orgCode);
            result.setResult(list);
            result.setSuccess(true);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return result;
    }

    /**
     * 查询数据 查出所有部门,并以树结构数据格式响应给前端（查询出当前节点下的子节点）
     *
     * @return
     */
    @RequestMapping(value = "/queryTreeListSon", method = RequestMethod.GET)
    public Result<List<SysDepartTreeprojectModel>> queryTreeListSon(String parentId) {
        Result<List<SysDepartTreeprojectModel>> result = new Result<>();
        try {
            // 从内存中读取
//			List<SysDepartTreeModel> list =FindsDepartsChildrenUtil.getSysDepartTreeList();
//			if (CollectionUtils.isEmpty(list)) {
//				list = sysDepartService.queryTreeList();
//			}
            List<SysDepartTreeprojectModel> list = sysDepartprojectService.queryTreeLists(parentId);
            result.setResult(list);
            result.setSuccess(true);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return result;
    }

    /**
     * 查询数据 查出所有部门,并以树结构数据格式响应给前端（查询出当前节点下的子节点）
     *
     * @return
     */
    @RequestMapping(value = "/queryTreeListgml", method = RequestMethod.GET)
    public Result<List<SysDepartTreeprojectModel>> queryTreeListgml(String orgCode,String beiz) {
        Result<List<SysDepartTreeprojectModel>> result = new Result<>();
        try {
            // 从内存中读取
//			List<SysDepartTreeModel> list =FindsDepartsChildrenUtil.getSysDepartTreeList();
//			if (CollectionUtils.isEmpty(list)) {
//				list = sysDepartService.queryTreeList();
//			}
            List<SysDepartTreeprojectModel> list = sysDepartprojectService.queryTreeListgml(orgCode,beiz);
            result.setResult(list);
            result.setSuccess(true);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return result;
    }
    /**
     * 查询数据 查出所有部门,并以树结构数据格式响应给前端（查询出当前节点下的子节点）
     *
     * @return
     */
    @RequestMapping(value = "/queryTreeListSon1", method = RequestMethod.GET)
    public Result<List<SysDepartTreeprojectModel>> queryTreeListSon1(String parentId) {
        Result<List<SysDepartTreeprojectModel>> result = new Result<>();
        try {
            // 从内存中读取
//			List<SysDepartTreeModel> list =FindsDepartsChildrenUtil.getSysDepartTreeList();
//			if (CollectionUtils.isEmpty(list)) {
//				list = sysDepartService.queryTreeList();
//			}
            List<SysDepartTreeprojectModel> list = sysDepartprojectService.queryTreeLists(parentId);
            if (list.size() == 0) {
                list = null;
            }
            result.setResult(list);
            result.setSuccess(true);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return result;
    }

    /**
     * 添加新数据 添加用户新建的部门对象数据,并保存到数据库
     *
     * @param sysDepart
     * @return
     */
    //@RequiresRoles({"admin"})
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @CacheEvict(value = {CacheConstant.SYS_DEPARTS_CACHE_PROJECT, CacheConstant.SYS_DEPART_IDS_CACHE_PROJECT}, allEntries = true)
    public Result<SysDepartproject> add(@RequestBody SysDepartproject sysDepart, HttpServletRequest request) {
        Result<SysDepartproject> result = new Result<SysDepartproject>();
        String username = JwtUtil.getUserNameByToken(request);
        try {
            sysDepart.setCreateBy(username);
            sysDepartprojectService.saveDepartData(sysDepart, username);
            //清除部门树内存
            // FindsDepartsChildrenUtil.clearSysDepartTreeList();
            // FindsDepartsChildrenUtil.clearDepartIdModel();
            result.success("添加成功！");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            result.error500("操作失败");
        }
        return result;
    }

    /**
     * 编辑数据 编辑部门的部分数据,并保存到数据库
     *
     * @param sysDepart
     * @return
     */
    //@RequiresRoles({"admin"})
    @RequestMapping(value = "/edit", method = RequestMethod.PUT)
    @CacheEvict(value = {CacheConstant.SYS_DEPARTS_CACHE_PROJECT, CacheConstant.SYS_DEPART_IDS_CACHE_PROJECT}, allEntries = true)
    public Result<SysDepartproject> edit(@RequestBody SysDepartproject sysDepart, HttpServletRequest request) {
        String username = JwtUtil.getUserNameByToken(request);
        sysDepart.setUpdateBy(username);
        Result<SysDepartproject> result = new Result<SysDepartproject>();
        SysDepartproject sysDepartEntity = sysDepartprojectService.getById(sysDepart.getId());
        if (sysDepartEntity == null) {
            result.error500("未找到对应实体");
        } else {
            boolean ok = sysDepartprojectService.updateDepartDataById(sysDepart, username);
            // TODO 返回false说明什么？
            if (ok) {
                //清除部门树内存
                //FindsDepartsChildrenUtil.clearSysDepartTreeList();
                //FindsDepartsChildrenUtil.clearDepartIdModel();
                result.success("修改成功!");
            }
        }
        return result;
    }

    @GetMapping(value = "/edit1")
    //@CacheEvict(value= {CacheConstant.SYS_DEPARTS_CACHE_PROJECT,CacheConstant.SYS_DEPART_IDS_CACHE_PROJECT}, allEntries=true)
    public Result<SysDepartproject> edit1(SysDepartproject sysDepart, HttpServletRequest request, String sysOrgCodes) {
        String username = JwtUtil.getUserNameByToken(request);
        sysDepart.setUpdateBy(username);
        Result<SysDepartproject> result = new Result<SysDepartproject>();
        SysDepartproject sysDepartEntity = sysDepartprojectService.getById(sysDepart.getId());
        if (sysDepartEntity == null) {
            result.error500("未找到对应实体");
        } else {
            boolean ok = false;
            if (sysDepartEntity.getOrgCodes() == null) {//当前项目没有分配过组织机构权限
                ok = sysDepartprojectService.updateDepartDataByIds(sysDepart, username, sysOrgCodes);
            } else {
                ok = sysDepartprojectService.insertsave(sysDepart, sysOrgCodes);

                //当前项目已经分配过组织机构权限需要新增一份
            }
            // TODO 返回false说明什么？
            if (ok) {
                //清除部门树内存
                //FindsDepartsChildrenUtil.clearSysDepartTreeList();
                //FindsDepartsChildrenUtil.clearDepartIdModel();
                result.success("关联成功!");
            }
        }
        return result;
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    //@RequiresRoles({"admin"})
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    @CacheEvict(value = {CacheConstant.SYS_DEPARTS_CACHE_PROJECT, CacheConstant.SYS_DEPART_IDS_CACHE_PROJECT}, allEntries = true)
    public Result<SysDepartproject> delete(@RequestParam(name = "id", required = true) String id) {

        Result<SysDepartproject> result = new Result<SysDepartproject>();
        SysDepartproject sysDepart = sysDepartprojectService.getById(id);
        if (sysDepart == null) {
            result.error500("未找到对应实体");
        } else {
            boolean ok = sysDepartprojectService.delete(id);
            if (ok) {
                //清除部门树内存
                //FindsDepartsChildrenUtil.clearSysDepartTreeList();
                // FindsDepartsChildrenUtil.clearDepartIdModel();
                result.success("删除成功!");
            }
        }
        return result;
    }


    /**
     * 批量删除 根据前端请求的多个ID,对数据库执行删除相关部门数据的操作
     *
     * @param ids
     * @return
     */
    //@RequiresRoles({"admin"})
    @RequestMapping(value = "/deleteBatch", method = RequestMethod.DELETE)
    @CacheEvict(value = {CacheConstant.SYS_DEPARTS_CACHE_PROJECT, CacheConstant.SYS_DEPART_IDS_CACHE_PROJECT}, allEntries = true)
    public Result<SysDepartproject> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {

        Result<SysDepartproject> result = new Result<SysDepartproject>();
        if (ids == null || "".equals(ids.trim())) {
            result.error500("参数不识别！");
        } else {
            this.sysDepartprojectService.deleteBatchWithChildren(Arrays.asList(ids.split(",")));
            result.success("删除成功!");
        }
        return result;
    }

    /**
     * 查询数据 添加或编辑页面对该方法发起请求,以树结构形式加载所有部门的名称,方便用户的操作
     *
     * @return
     */
    @RequestMapping(value = "/queryIdTree", method = RequestMethod.GET)
    public Result<List<DepartIdprojectModel>> queryIdTree() {
//		Result<List<DepartIdModel>> result = new Result<List<DepartIdModel>>();
//		List<DepartIdModel> idList;
//		try {
//			idList = FindsDepartsChildrenUtil.wrapDepartIdModel();
//			if (idList != null && idList.size() > 0) {
//				result.setResult(idList);
//				result.setSuccess(true);
//			} else {
//				sysDepartprojectService.queryTreeList();
//				idList = FindsDepartsChildrenUtil.wrapDepartIdModel();
//				result.setResult(idList);
//				result.setSuccess(true);
//			}
//			return result;
//		} catch (Exception e) {
//			log.error(e.getMessage(),e);
//			result.setSuccess(false);
//			return result;
//		}
        Result<List<DepartIdprojectModel>> result = new Result<>();
        try {
            List<DepartIdprojectModel> list = sysDepartprojectService.queryDepartIdTreeList();
            result.setResult(list);
            result.setSuccess(true);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return result;
    }

    /**
     * <p>
     * 部门搜索功能方法,根据关键字模糊搜索相关部门
     * </p>
     *
     * @param keyWord
     * @return
     */
    @RequestMapping(value = "/searchBy", method = RequestMethod.GET)
    public Result<List<SysDepartTreeprojectModel>> searchBy(@RequestParam(name = "keyWord", required = true) String keyWord, @RequestParam(name = "myDeptSearch", required = false) String myDeptSearch) {
        Result<List<SysDepartTreeprojectModel>> result = new Result<List<SysDepartTreeprojectModel>>();
        //部门查询，myDeptSearch为1时为我的部门查询，登录用户为上级时查只查负责部门下数据
        LoginUser user = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        String departIds = null;
        if (oConvertUtils.isNotEmpty(user.getUserIdentity()) && user.getUserIdentity().equals(CommonConstant.USER_IDENTITY_2)) {
            departIds = user.getDepartIds();
        }
        List<SysDepartTreeprojectModel> treeList = this.sysDepartprojectService.searhBy(keyWord, myDeptSearch, departIds);
        if (treeList == null || treeList.size() == 0) {
            result.setSuccess(false);
            result.setMessage("未查询匹配数据！");
            return result;
        }
        result.setResult(treeList);
        return result;
    }


    /**
     * 导出excel
     *
     * @param request
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(SysDepartproject sysDepart, HttpServletRequest request) {
        LoginUser user = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        // Step.1 组装查询条件
        QueryWrapper<SysDepartproject> queryWrapper = QueryGenerator.initQueryWrapper(sysDepart, request.getParameterMap());
        queryWrapper.likeRight("org_codes", user.getOrgCode());
        queryWrapper.orderByAsc("org_code");
        //Step.2 AutoPoi 导出Excel
        ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
        List<SysDepartproject> pageList = sysDepartprojectService.list(queryWrapper);
        //按字典排序
        Collections.sort(pageList, new Comparator<SysDepartproject>() {
            @Override
            public int compare(SysDepartproject arg0, SysDepartproject arg1) {
                return arg0.getOrgCode().compareTo(arg1.getOrgCode());
            }
        });
        //导出文件名称
        mv.addObject(NormalExcelConstants.FILE_NAME, "分部分项列表");
        mv.addObject(NormalExcelConstants.CLASS, SysDepartproject.class);
        mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("WBS分部分项数据", "导出人:" + user.getRealname(), "导出信息"));
        mv.addObject(NormalExcelConstants.DATA_LIST, pageList);
        return mv;
    }

    /**
     * 通过excel导入数据
     *
     * @param request
     * @param response
     * @return
     */
    //@RequiresRoles({"admin"})
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    @CacheEvict(value = {CacheConstant.SYS_DEPARTS_CACHE_PROJECT, CacheConstant.SYS_DEPART_IDS_CACHE_PROJECT}, allEntries = true)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        List<String> errorMessageList = new ArrayList<>();
        List<SysDepartproject> listSysDeparts = null;
        Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
        for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
            MultipartFile file = entity.getValue();// 获取上传文件对象
            ImportParams params = new ImportParams();
            params.setTitleRows(2);
            params.setHeadRows(1);
            params.setNeedSave(true);
            try {
                // orgCode编码长度
                int codeLength = 3;
                listSysDeparts = ExcelImportUtil.importExcel(file.getInputStream(), SysDepartproject.class, params);
                // 筛选不为空的记录
                listSysDeparts = listSysDeparts.stream()
                        .filter(depart -> depart.getDepartName() != null && !depart.getDepartName().isEmpty())
                        .collect(Collectors.toList());
                //按长度排序
                Collections.sort(listSysDeparts, new Comparator<SysDepartproject>() {
                    @Override
                    public int compare(SysDepartproject arg0, SysDepartproject arg1) {
                        return arg0.getOrgCode().length() - arg1.getOrgCode().length();
                    }
                });

                int num = 0;
                for (SysDepartproject sysDepart : listSysDeparts) {
                    String orgCode = sysDepart.getOrgCode();
                    if (orgCode.length() > codeLength) {
                        String parentCode = orgCode.substring(0, orgCode.length() - codeLength);
                        QueryWrapper<SysDepartproject> queryWrapper = new QueryWrapper<SysDepartproject>();
                        queryWrapper.eq("org_code", parentCode);
                        try {
                            SysDepartproject parentDept = sysDepartprojectService.getOne(queryWrapper);
                            if (!parentDept.equals(null)) {
                                sysDepart.setParentId(parentDept.getId());
                                sysDepart.setOrgCodes(parentDept.getOrgCodes());
                                sysDepart.setPici(parentDept.getPici());
                            } else {
                                sysDepart.setParentId("");
                            }
                        } catch (Exception e) {
                            //没有查找到parentDept
                        }
                    } else {
                        sysDepart.setParentId("");
                    }
                    sysDepart.setDelFlag(CommonConstant.DEL_FLAG_0.toString());
                    ImportExcelUtil.importDateSaveOne(sysDepart, ISysDepartprojectService.class, errorMessageList, num, CommonConstant.SQL_INDEX_UNIQ_DEPART_ORG_CODE);
                    num++;
                }
                //清空部门缓存
                Set keys3 = redisTemplate.keys(CacheConstant.SYS_DEPARTS_CACHE_PROJECT + "*");
                Set keys4 = redisTemplate.keys(CacheConstant.SYS_DEPART_IDS_CACHE_PROJECT + "*");
                redisTemplate.delete(keys3);
                redisTemplate.delete(keys4);
                return ImportExcelUtil.imporReturnRes(errorMessageList.size(), listSysDeparts.size() - errorMessageList.size(), errorMessageList);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                return Result.error("文件导入失败:" + e.getMessage());
            } finally {
                try {
                    file.getInputStream().close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return Result.error("文件导入失败！");
    }


    /**
     * 查询所有部门信息
     *
     * @return
     */
    @GetMapping("listAll")
    public Result<List<SysDepartproject>> listAll(@RequestParam(name = "id", required = false) String id) {
        Result<List<SysDepartproject>> result = new Result<>();
        LambdaQueryWrapper<SysDepartproject> query = new LambdaQueryWrapper<SysDepartproject>();
        query.orderByAsc(SysDepartproject::getOrgCode);
        if (oConvertUtils.isNotEmpty(id)) {
            String arr[] = id.split(",");
            query.in(SysDepartproject::getId, arr);
        }
        List<SysDepartproject> ls = this.sysDepartprojectService.list(query);
        result.setSuccess(true);
        result.setResult(ls);
        return result;
    }

    /**
     * 查询数据 查出所有部门,并以树结构数据格式响应给前端
     *
     * @return
     */
    @RequestMapping(value = "/queryTreeByKeyWord", method = RequestMethod.GET)
    public Result<Map<String, Object>> queryTreeByKeyWord(@RequestParam(name = "keyWord", required = false) String keyWord) {
        Result<Map<String, Object>> result = new Result<>();
        try {
            Map<String, Object> map = new HashMap<String, Object>();
            List<SysDepartTreeprojectModel> list = sysDepartprojectService.queryTreeByKeyWord(keyWord);
            //根据keyWord获取用户信息
            LambdaQueryWrapper<SysUser> queryUser = new LambdaQueryWrapper<SysUser>();
            queryUser.eq(SysUser::getDelFlag, CommonConstant.DEL_FLAG_0);
            queryUser.and(i -> i.like(SysUser::getUsername, keyWord).or().like(SysUser::getRealname, keyWord));
            List<SysUser> sysUsers = this.sysUserService.list(queryUser);
            map.put("userList", sysUsers);
            map.put("departList", list);
            result.setResult(map);
            result.setSuccess(true);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return result;
    }

    /**
     * 根据部门编码获取部门信息
     *
     * @param orgCode
     * @return
     */
    @GetMapping("/getDepartName")
    public Result<SysDepartproject> getDepartName(@RequestParam(name = "orgCode") String orgCode) {
        Result<SysDepartproject> result = new Result<>();
        LambdaQueryWrapper<SysDepartproject> query = new LambdaQueryWrapper<>();
        query.eq(SysDepartproject::getOrgCode, orgCode);
        SysDepartproject sysDepart = sysDepartprojectService.getOne(query);
        result.setSuccess(true);
        result.setResult(sysDepart);
        return result;
    }

    /**
     * 根据部门编码和项目类型获取部门信息(梁等)
     *
     * @param sysDepartproject
     * @return
     */
    @GetMapping("/getDepartNames")
    public Result<?> getDepartNames(SysDepartproject sysDepartproject, HttpServletRequest req) {
        if (!StringUtil.isEmpty(sysDepartproject.getOrgCode())) {
            sysDepartproject.setOrgCode(sysDepartproject.getOrgCode() + "*");
        }
        QueryWrapper<SysDepartproject> queryWrapper = QueryGenerator.initQueryWrapper(sysDepartproject, req.getParameterMap());
        List<SysDepartproject> sysDepart = sysDepartprojectService.list(queryWrapper);
        return Result.OK(sysDepart);
    }


    /**
     * 根据部门id获取用户信息
     *
     * @param id
     * @return
     */
    @GetMapping("/getUsersByDepartId")
    public Result<List<SysUser>> getUsersByDepartId(@RequestParam(name = "id") String id) {
        Result<List<SysUser>> result = new Result<>();
        List<SysUser> sysUsers = sysUserDepartService.queryUserByDepId(id);
        result.setSuccess(true);
        result.setResult(sysUsers);
        return result;
    }

    /**
     * 添加
     *
     * @param bhzrenwudan
     * @return
     */
    @AutoLog(value = "任务单浇筑令-添加")
    @ApiOperation(value = "任务单浇筑令-添加", notes = "任务单浇筑令-添加")
    @PostMapping(value = "/rewudanadd")
    public Result<?> add(@RequestBody Bhzrenwudan bhzrenwudan) {
        String projgrade = "";

        if (StringUtils.isNotBlank(bhzrenwudan.getProjgrade())) {
            projgrade = bhzrenwudan.getProjgrade();
        } else {
            String treeid = bhzrenwudan.getTreeid();
            if (StrUtil.isNotBlank(treeid)) {
                SysDepartproject selectprojnames = sysDepartprojectService.selectprojnames(treeid);
                projgrade = selectprojnames.getOrgCode();
                bhzrenwudan.setProjgrade(projgrade);
            }
        }
        if (StringUtils.isNotBlank(projgrade)) {
            String A = projgrade.substring(0, 1);
            int y = projgrade.indexOf(A, projgrade.indexOf(A) + 1);
            String projgrade1 = projgrade.substring(0, y);
            String projectname = "";
            for (int x = projgrade.length(); x > 0; x = x - y) {
                String projectnames = projgrade.substring(0, x);
                SysDepartproject selectprojname = sysDepartprojectService.selectprojname(projectnames);
                if (selectprojname != null) {
                    if (projectname.equals("")) {
                        projectname = selectprojname.getDepartName();
                        bhzrenwudan.setTreeid(selectprojname.getId());
                        bhzrenwudan.setWbsStructureType(StringUtils.isNotBlank(selectprojname.getWbsStructureType()) ? selectprojname.getWbsStructureType() : "");
                    } else {
                        projectname = selectprojname.getDepartName() + " | " + projectname;
                    }

                }
            }
            if (StringUtils.isNotBlank(bhzrenwudan.getRequest())) {// 手动输入浇筑部位
                bhzrenwudan.setConspos(bhzrenwudan.getRequest());
            } else {
                bhzrenwudan.setConspos(projectname);// 选择浇筑部位
            }


            if (StringUtils.isEmpty(bhzrenwudan.getProjname())) {
                SysDepartproject selectprojname = sysDepartprojectService.selectprojname(projgrade1);
                bhzrenwudan.setProjname(selectprojname.getDepartName());
            }
            SysDepartproject selectprojname1 = sysDepartprojectService.selectprojname(projgrade);
            bhzrenwudan.setSysOrgCode(selectprojname1.getOrgCodes());
        }

        bhzrenwudan.setDattim(new Date());
        bhzrenwudan.setCode(DateUtils.codeFormat("RWD-"));
//		if(bhzrenwudan.getStation() == null || bhzrenwudan.getStation() == 0){
//
//		}else if(bhzrenwudan.getStation() == 1){
//			bhzrenwudan.setCode("RWD-"+format.format(date));
//			//bhzrenwudan.setRecipe("SCX-"+format.format(date)); 此处不需要生成配合比编号
//		}else {
//			bhzrenwudan.setCode("RWD-"+format.format(date));
//			//bhzrenwudan.setRecipes("SCX-"+format.format(date)); 此处不需要生成配合比编号
//		}
        bhzrenwudan.setStatus(0);
        bhzrenwudanService.save(bhzrenwudan);
        if (addSms(bhzrenwudan)) {
            return Result.OK("添加成功！现下发通知短信！");
        } else {
            return Result.OK("添加成功！暂未配置通知短信下发人员！");
        }
    }

    /**
     * 编辑
     *
     * @param bhzrenwudan
     * @return
     */
    @AutoLog(value = "任务单浇筑令-编辑")
    @ApiOperation(value = "任务单浇筑令-编辑", notes = "任务单浇筑令-编辑")
    @PutMapping(value = "/renwudanedit")
    public Result<?> edit(@RequestBody Bhzrenwudan bhzrenwudan) {
        String projgrade = "";

        if (StringUtils.isNotBlank(bhzrenwudan.getProjgrade())) {
            projgrade = bhzrenwudan.getProjgrade();
        } else {
            String treeid = bhzrenwudan.getTreeid();
            if (StrUtil.isNotBlank(treeid)) {
                SysDepartproject selectprojnames = sysDepartprojectService.selectprojnames(treeid);
                projgrade = selectprojnames.getOrgCode();
                bhzrenwudan.setProjgrade(projgrade);
            }
        }
        if (StringUtils.isNotBlank(projgrade)) {
            String A = projgrade.substring(0, 1);
            int y = projgrade.indexOf(A, projgrade.indexOf(A) + 1);
            String projgrade1 = projgrade.substring(0, y);
            String projectname = "";
            for (int x = projgrade.length(); x > 0; x = x - y) {
                String projectnames = projgrade.substring(0, x);
                SysDepartproject selectprojname = sysDepartprojectService.selectprojname(projectnames);
                if (selectprojname != null) {
                    if (projectname.equals("")) {
                        projectname = selectprojname.getDepartName();
                        bhzrenwudan.setTreeid(selectprojname.getId());
                    } else {
                        projectname = selectprojname.getDepartName() + " | " + projectname;
                    }

                }
            }

            if (StringUtils.isNotBlank(bhzrenwudan.getConspos())) {// 不为空则是手动输入浇筑部位
                bhzrenwudan.setConspos(projectname);// 选择浇筑部位
            }

            if (StringUtils.isEmpty(bhzrenwudan.getProjname())) {
                SysDepartproject selectprojname = sysDepartprojectService.selectprojname(projgrade1);
                bhzrenwudan.setProjname(selectprojname.getDepartName());
            }
            SysDepartproject selectprojname1 = sysDepartprojectService.selectprojname(projgrade);
            bhzrenwudan.setSysOrgCode(selectprojname1.getOrgCodes());
        }
        bhzrenwudan.setStatus(0);
        bhzrenwudanService.updateById(bhzrenwudan);
        return Result.OK("编辑成功!");
    }

    /**
     * 张拉任务单添加
     *
     * @param trZhanglaRenwudan
     * @return
     */
    @AutoLog(value = "张拉任务单-添加")
    @ApiOperation(value = "张拉任务单-添加", notes = "张拉任务单-添加")
    @PostMapping(value = "/Zlrwdadd")
    public Result<?> Zlrwdadd(@RequestBody TrZhanglaRenwudan trZhanglaRenwudan) {
        String projgrade = "";
        ShebeiInfo selectshebeione = shebeiInfoService.selectshebeione(trZhanglaRenwudan.getShebeibianh());
        if (StringUtils.isNotBlank(trZhanglaRenwudan.getSgbwuuid())) {
            projgrade = trZhanglaRenwudan.getSgbwuuid();
        } else {
            String treeid = trZhanglaRenwudan.getTreeid();
            if (StrUtil.isNotBlank(treeid)) {
                SysDepartproject selectprojnames = sysDepartprojectService.selectprojnames(treeid);
                projgrade = selectprojnames.getOrgCode();
                trZhanglaRenwudan.setSgbwuuid(projgrade);
            }
        }
        if (StringUtils.isNotBlank(projgrade)) {
            String A = projgrade.substring(0, 1);
            int y = projgrade.indexOf(A, projgrade.indexOf(A) + 1);
            String projgrade1 = projgrade.substring(0, y);
            String projectname = "";
            for (int x = projgrade.length(); x > 0; x = x - y) {
                String projectnames = projgrade.substring(0, x);
                SysDepartproject selectprojname = sysDepartprojectService.selectprojname(projectnames);
                if (selectprojname != null) {
                    if (projectname.equals("")) {
                        projectname = selectprojname.getDepartName();
                        trZhanglaRenwudan.setTreeid(selectprojname.getId());
                    } else {
                        projectname = selectprojname.getDepartName() + " | " + projectname;
                    }

                }
            }
            trZhanglaRenwudan.setSgbwname(projectname);
            SysDepartproject selectprojname = sysDepartprojectService.selectprojname(projgrade1);
            SysDepartproject selectprojname1 = sysDepartprojectService.selectprojname(projgrade);
            trZhanglaRenwudan.setProjectname(selectprojname.getDepartName());
            trZhanglaRenwudan.setSysOrgCode(StringUtils.isNotBlank(selectprojname1.getOrgCodes()) ? selectprojname1.getOrgCodes() : selectshebeione.getSysOrgCode());
        } else {

            trZhanglaRenwudan.setSysOrgCode(selectshebeione.getSysOrgCode());
        }
        trZhanglaRenwudan.setUuid(DateUtils.codeFormat("ZL-RWD-"));
        trZhanglaRenwudanService.save(trZhanglaRenwudan);
        return Result.OK("添加成功！");
    }

    /**
     * syjzb添加orgcode和施工部位
     *
     * @param trZhanglaRenwudan
     * @return
     */
    @AutoLog(value = "张拉任务单-添加")
    @ApiOperation(value = "张拉任务单-添加", notes = "张拉任务单-添加")
    @GetMapping(value = "/syjadd")
    public Result<?> syjadd(TrZhanglaRenwudan trZhanglaRenwudan) {
        if (trZhanglaRenwudan.getId() != null) {
            QueryWrapper<TSyjzb> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("id", trZhanglaRenwudan.getId());
            TSyjzb one = syjzbService.getOne(queryWrapper);

            if (one != null) {
                String projgrade = "";
                ShebeiInfo selectshebeione = shebeiInfoService.selectshebeione(trZhanglaRenwudan.getShebeibianh());
                if (StringUtils.isNotBlank(trZhanglaRenwudan.getSgbwuuid())) {
                    projgrade = trZhanglaRenwudan.getSgbwuuid();
                } else {
                    String treeid = trZhanglaRenwudan.getTreeid();
                    if (StrUtil.isNotBlank(treeid)) {
                        SysDepartproject selectprojnames = sysDepartprojectService.selectprojnames(treeid);
                        projgrade = selectprojnames.getOrgCode();
                        trZhanglaRenwudan.setSgbwuuid(projgrade);
                    }
                }
                if (StringUtils.isNotBlank(projgrade)) {
                    String A = projgrade.substring(0, 1);
                    int y = projgrade.indexOf(A, projgrade.indexOf(A) + 1);
                    String projectname = "";
                    for (int x = projgrade.length(); x > 0; x = x - y) {
                        String projectnames = projgrade.substring(0, x);
                        SysDepartproject selectprojname = sysDepartprojectService.selectprojname(projectnames);
                        if (selectprojname != null) {
                            if (projectname.equals("")) {
                                projectname = selectprojname.getDepartName();
                            } else {
                                projectname = selectprojname.getDepartName() + " | " + projectname;
                            }

                        }
                    }
                    one.setProjectname(projectname);
                    SysDepartproject selectprojname1 = sysDepartprojectService.selectprojname(projgrade);
                    one.setOrgcode(StringUtils.isNotBlank(selectprojname1.getOrgCode()) ? selectprojname1.getOrgCode() : selectshebeione.getSysOrgCode());
                } else {
                    one.setOrgcode(selectshebeione.getSysOrgCode());
                }
                syjzbService.updateById(one);
            }
        }
        return Result.OK("添加成功！");
    }

    /**
     * 张拉任务单修改
     *
     * @param trZhanglaRenwudan
     * @return
     */
    @AutoLog(value = "张拉任务单-编辑")
    @ApiOperation(value = "张拉任务单-编辑", notes = "张拉任务单-编辑")
    @PutMapping(value = "/Zlrwdedit")
    public Result<?> Zlrwdedit(@RequestBody TrZhanglaRenwudan trZhanglaRenwudan) {
        String projgrade = "";
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户信息
        ShebeiInfo selectshebeione = shebeiInfoService.selectshebeione(trZhanglaRenwudan.getShebeibianh());
        if (StringUtils.isNotBlank(trZhanglaRenwudan.getSgbwuuid())) {
            projgrade = trZhanglaRenwudan.getSgbwuuid();
        } else {
            String treeid = trZhanglaRenwudan.getTreeid();
            if (StrUtil.isNotBlank(treeid)) {
                SysDepartproject selectprojnames = sysDepartprojectService.selectprojnames(treeid);
                projgrade = selectprojnames.getOrgCode();
                trZhanglaRenwudan.setSgbwuuid(projgrade);
            }
        }
        if (StringUtils.isNotBlank(projgrade)) {
            String A = projgrade.substring(0, 1);
            int y = projgrade.indexOf(A, projgrade.indexOf(A) + 1);
            String projgrade1 = projgrade.substring(0, y);
            String projectname = "";
            for (int x = projgrade.length(); x > 0; x = x - y) {
                String projectnames = projgrade.substring(0, x);
                SysDepartproject selectprojname = sysDepartprojectService.selectprojname(projectnames);
                if (selectprojname != null) {
                    if (projectname.equals("")) {
                        projectname = selectprojname.getDepartName();
                        trZhanglaRenwudan.setTreeid(selectprojname.getId());
                    } else {
                        projectname = selectprojname.getDepartName() + " | " + projectname;
                    }

                }
            }
            trZhanglaRenwudan.setSgbwname(projectname);
            SysDepartproject selectprojname = sysDepartprojectService.selectprojname(projgrade1);
            SysDepartproject selectprojname1 = sysDepartprojectService.selectprojname(projgrade);
            trZhanglaRenwudan.setProjectname(selectprojname.getDepartName());
            trZhanglaRenwudan.setSysOrgCode(StringUtils.isNotBlank(selectprojname1.getOrgCodes()) ? selectprojname1.getOrgCodes() : selectshebeione.getSysOrgCode());
        } else {
//            ShebeiInfo selectshebeione = shebeiInfoService.selectshebeione(trZhanglaRenwudan.getShebeibianh());
            trZhanglaRenwudan.setSysOrgCode(selectshebeione.getSysOrgCode());
        }
        trZhanglaRenwudanService.updateById(trZhanglaRenwudan);
        return Result.OK("编辑成功！");
    }

    /**
     * 压浆任务单添加
     *
     * @param trYajiangRenwudan
     * @return
     */
    @AutoLog(value = "压浆任务单-添加")
    @ApiOperation(value = "压浆任务单-添加", notes = "压浆任务单-添加")
    @PostMapping(value = "/yjrenwudanadd")
    public Result<?> add(@RequestBody TrYajiangRenwudan trYajiangRenwudan) {
        String projgrade = "";
        ShebeiInfo selectshebeione = shebeiInfoService.selectshebeione(trYajiangRenwudan.getShebeibianhao());
        if (StringUtils.isNotBlank(trYajiangRenwudan.getSgbwuuid())) {
            projgrade = trYajiangRenwudan.getSgbwuuid();
        } else {
            String treeid = trYajiangRenwudan.getTreeid();
            if (StrUtil.isNotBlank(treeid)) {
                SysDepartproject selectprojnames = sysDepartprojectService.selectprojnames(treeid);
                projgrade = selectprojnames.getOrgCode();
                trYajiangRenwudan.setSgbwuuid(projgrade);
            }
        }
        if (StringUtils.isNotBlank(projgrade)) {
            String A = projgrade.substring(0, 1);
            int y = projgrade.indexOf(A, projgrade.indexOf(A) + 1);
            String projgrade1 = projgrade.substring(0, y);
            String projectname = "";
            for (int x = projgrade.length(); x > 0; x = x - y) {
                String projectnames = projgrade.substring(0, x);
                SysDepartproject selectprojname = sysDepartprojectService.selectprojname(projectnames);
                if (selectprojname != null) {
                    if (projectname.equals("")) {
                        projectname = selectprojname.getDepartName();
                        trYajiangRenwudan.setTreeid(selectprojname.getId());
                    } else {
                        projectname = selectprojname.getDepartName() + " | " + projectname;
                    }

                }
            }
            trYajiangRenwudan.setSgbwname(projectname);
            SysDepartproject selectprojname = sysDepartprojectService.selectprojname(projgrade1);
            SysDepartproject selectprojname1 = sysDepartprojectService.selectprojname(projgrade);
            trYajiangRenwudan.setProjectname(selectprojname.getDepartName());
            trYajiangRenwudan.setSysOrgCode(StringUtils.isNotBlank(selectprojname1.getOrgCodes()) ? selectprojname1.getOrgCodes() : selectshebeione.getSysOrgCode());
        } else {

            trYajiangRenwudan.setSysOrgCode(selectshebeione.getSysOrgCode());
        }
        trYajiangRenwudan.setUuid(DateUtils.codeFormat("YJ-RWD-"));
        trYajiangRenwudanService.save(trYajiangRenwudan);
        return Result.OK("添加成功！");
    }

    /**
     * 压浆任务单编辑
     *
     * @param trYajiangRenwudan
     * @return
     */
    @AutoLog(value = "压浆任务单-编辑")
    @ApiOperation(value = "压浆任务单-编辑", notes = "压浆任务单-编辑")
    @PutMapping(value = "/yjrenwudanedit")
    public Result<?> edit(@RequestBody TrYajiangRenwudan trYajiangRenwudan) {
        String projgrade = "";
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户信息
        if (StringUtils.isNotBlank(trYajiangRenwudan.getSgbwuuid())) {
            projgrade = trYajiangRenwudan.getSgbwuuid();
        } else {
            String treeid = trYajiangRenwudan.getTreeid();
            if (StrUtil.isNotBlank(treeid)) {
                SysDepartproject selectprojnames = sysDepartprojectService.selectprojnames(treeid);
                projgrade = selectprojnames.getOrgCode();
                trYajiangRenwudan.setSgbwuuid(projgrade);
            }
        }
        if (StringUtils.isNotBlank(projgrade)) {
            String A = projgrade.substring(0, 1);
            int y = projgrade.indexOf(A, projgrade.indexOf(A) + 1);
            String projgrade1 = projgrade.substring(0, y);
            String projectname = "";
            for (int x = projgrade.length(); x > 0; x = x - y) {
                String projectnames = projgrade.substring(0, x);
                SysDepartproject selectprojname = sysDepartprojectService.selectprojname(projectnames);
                if (selectprojname != null) {
                    if (projectname.equals("")) {
                        projectname = selectprojname.getDepartName();
                        trYajiangRenwudan.setTreeid(selectprojname.getId());
                    } else {
                        projectname = selectprojname.getDepartName() + " | " + projectname;
                    }

                }
            }
            trYajiangRenwudan.setSgbwname(projectname);
            SysDepartproject selectprojname = sysDepartprojectService.selectprojname(projgrade1);
            SysDepartproject selectprojname1 = sysDepartprojectService.selectprojname(projgrade);
            trYajiangRenwudan.setProjectname(selectprojname.getDepartName());
            trYajiangRenwudan.setSysOrgCode(StringUtils.isNotBlank(selectprojname1.getOrgCodes()) ? selectprojname1.getOrgCodes() : loginUser.getOrgCode());
        } else {
            ShebeiInfo selectshebeione = shebeiInfoService.selectshebeione(trYajiangRenwudan.getShebeibianhao());
            trYajiangRenwudan.setSysOrgCode(selectshebeione.getSysOrgCode());
        }
        trYajiangRenwudanService.updateById(trYajiangRenwudan);
        return Result.OK("编辑成功!");
    }

    /**
     * 添加
     *
     * @param hnthtConsign
     * @return
     */
    @AutoLog(value = "检测试验任务单下发信息表-添加")
    @ApiOperation(value = "检测试验任务单下发信息表-添加", notes = "检测试验任务单下发信息表-添加")
    @PostMapping(value = "/renwudanxiafaadd")
    public Result<?> add(@RequestBody HnthtConsign hnthtConsign) {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        String projgrade = hnthtConsign.getSgbwguid();
        ShebeiInfo selectshebeione = shebeiInfoService.selectshebeione(hnthtConsign.getShebeibianhao());
        String projectname = "";
        if (StrUtil.isNotBlank(projgrade)) {
            String A = projgrade.substring(0, 1);
            int y = projgrade.indexOf(A, projgrade.indexOf(A) + 1);
            String projgrade1 = projgrade.substring(0, y);
            for (int x = projgrade.length(); x > 0; x = x - y) {
                String projectnames = projgrade.substring(0, x);
                SysDepartproject selectprojname = sysDepartprojectService.selectprojname(projectnames);
                if (selectprojname != null) {
                    if (projectname.equals("")) {
                        projectname = selectprojname.getDepartName();
                        hnthtConsign.setTreeid(selectprojname.getId());
                    } else {
                        projectname = selectprojname.getDepartName() + " | " + projectname;

                    }

                }
            }
            hnthtConsign.setSgbwname(projectname);
            SysDepartproject selectprojname = sysDepartprojectService.selectprojname(projgrade1);
            SysDepartproject selectprojname1 = sysDepartprojectService.selectprojname(projgrade);
            hnthtConsign.setProjectname(selectprojname.getDepartName());
            hnthtConsign.setSysOrgCode(StringUtils.isNotBlank(selectprojname1.getOrgCodes()) ? selectprojname1.getOrgCodes() : selectshebeione.getSysOrgCode());
        } else {
            hnthtConsign.setSgbwname(hnthtConsign.getSgbwname());
            hnthtConsign.setProjectname(hnthtConsign.getProjectname());
            hnthtConsign.setSysOrgCode(selectshebeione.getSysOrgCode());
        }
        hnthtConsign.setUuid(DateUtils.codeFormat("SY-RWD-"));
        hnthtConsignService.save(hnthtConsign);
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param hnthtConsign
     * @return
     */
    @AutoLog(value = "检测试验任务单下发信息表-编辑")
    @ApiOperation(value = "检测试验任务单下发信息表-编辑", notes = "检测试验任务单下发信息表-编辑")
    @PutMapping(value = "/renwudanxiafaedit")
    public Result<?> edit(@RequestBody HnthtConsign hnthtConsign) {
//        String projgrade = hnthtConsign.getSgbwguid();
//        ShebeiInfo selectshebeione = shebeiInfoService.selectshebeione(hnthtConsign.getShebeibianhao());
//        if (StrUtil.isNotBlank(projgrade)) {
//            String A = projgrade.substring(0, 1);
//            int y = projgrade.indexOf(A, projgrade.indexOf(A) + 1);
//            String projgrade1 = projgrade.substring(0, y);
//            String projectname = "";
//            for (int x = projgrade.length(); x > 0; x = x - y) {
//                String projectnames = projgrade.substring(0, x);
//                SysDepartproject selectprojname = sysDepartprojectService.selectprojname(projectnames);
//                if (selectprojname != null) {
//                    if (projectname.equals("")) {
//                        projectname = selectprojname.getDepartName();
//                        hnthtConsign.setTreeid(selectprojname.getId());
//                    } else {
//                        projectname = selectprojname.getDepartName() + " | " + projectname;
//                    }
//
//                }
//            }
//            hnthtConsign.setSgbwname(projectname);
//            SysDepartproject selectprojname = sysDepartprojectService.selectprojname(projgrade1);
//            SysDepartproject selectprojname1 = sysDepartprojectService.selectprojname(projgrade);
//            if(StringUtils.isBlank(hnthtConsign.getProjectname())){
//                hnthtConsign.setProjectname( selectprojname != null?selectprojname.getDepartName():"" );
//            }
//            hnthtConsign.setSysOrgCode(selectprojname1 != null  &&  StringUtils.isNotBlank(selectprojname1.getOrgCodes())  ? selectprojname1.getOrgCodes() : selectshebeione.getSysOrgCode());
//        } else {
//            hnthtConsign.setSgbwname(hnthtConsign.getSgbwname());
//            hnthtConsign.setProjectname(hnthtConsign.getProjectname());
//            hnthtConsign.setSysOrgCode(selectshebeione.getSysOrgCode());
//        }
        hnthtConsignService.updateById(hnthtConsign);
        return Result.OK("编辑成功!");
    }

    /**
     * 中铁一局对接分部分项
     *
     * @return
     */
    @AutoLog(value = "任务单（制梁）表信息-添加")
    @ApiOperation(value = "任务单（制梁）表信息-添加", notes = "任务单（制梁）表信息-添加")
    @PostMapping(value = "/ztyjfbfx")
    public Result<?> ztyjfbfx(@RequestBody SchedulingCar schedulingCar) {
        String projgrade = schedulingCar.getProjgrade();

        if (StrUtil.isNotBlank(projgrade)) {
            String A = projgrade.substring(0, 1);
            int y = projgrade.indexOf(A, projgrade.indexOf(A) + 1);
            String projgrade1 = projgrade.substring(0, y);
            String projectname = "";
            for (int x = projgrade.length(); x > 0; x = x - y) {
                String projectnames = projgrade.substring(0, x);
                SysDepartproject selectprojname = sysDepartprojectService.selectprojname(projectnames);
                if (selectprojname != null) {
                    if (projectname.equals("")) {
                        projectname = selectprojname.getDepartName();
                    } else {
                        projectname = selectprojname.getDepartName() + " | " + projectname;
                    }

                }
            }
            schedulingCar.setConspos(projectname);
            SysDepartproject selectprojname = sysDepartprojectService.selectprojname(projgrade1);
            schedulingCar.setProjname(selectprojname.getDepartName());
            schedulingCarService.updateById(schedulingCar);
        }

        return Result.OK("添加成功！");
    }


    /**
     * 制梁任务单添加
     *
     * @param zhiliangrenwudanPage
     * @return
     */
    @AutoLog(value = "任务单（制梁）表信息-添加")
    @ApiOperation(value = "任务单（制梁）表信息-添加", notes = "任务单（制梁）表信息-添加")
    @PostMapping(value = "/zlrenwudanadd")
    public Result<?> add(@RequestBody ZhiliangrenwudanPage zhiliangrenwudanPage) {
        Zhiliangrenwudan zhiliangrenwudan = new Zhiliangrenwudan();
        String uuid = UUID.randomUUID().toString();//随机生成唯一码UUID
        zhiliangrenwudanPage.setUuid(uuid);
        String projgrade = zhiliangrenwudanPage.getProjgrade();
        //新增功能
        LiangJiasheguanli liangJiasheguanli = new LiangJiasheguanli();
        liangJiasheguanli.setOrgCode(projgrade);
        if (zhiliangrenwudanPage.getProductiontime1() != null) {
            liangJiasheguanli.setJihuaTime(zhiliangrenwudanPage.getProductiontime1());
        }

        if (StrUtil.isNotBlank(projgrade)) {
            String A = projgrade.substring(0, 1);
            int y = projgrade.indexOf(A, projgrade.indexOf(A) + 1);
            String projgrade1 = projgrade.substring(0, y);
            String projectname = "";
            for (int x = projgrade.length(); x > 0; x = x - y) {
                String projectnames = projgrade.substring(0, x);
                SysDepartproject selectprojname = sysDepartprojectService.selectprojname(projectnames);
                if (selectprojname != null) {
                    //新增功能
                    String departName = selectprojname.getDepartName();
                    if (departName.contains("#")) {
                        liangJiasheguanli.setLiangpian(departName);
                    } else if (departName.contains("跨")) {
                        liangJiasheguanli.setKuaname(departName);
                    } else if (departName.contains("桥")) {
                        liangJiasheguanli.setProjectname(departName);
                    }

                    if (projectname.equals("")) {
                        projectname = selectprojname.getDepartName();
                    } else {
                        projectname = selectprojname.getDepartName() + " | " + projectname;
                    }

                }
            }
            zhiliangrenwudanPage.setConspos(projectname);
            SysDepartproject selectprojname = sysDepartprojectService.selectprojname(projgrade1);
            SysDepartproject selectprojname1 = sysDepartprojectService.selectprojname(projgrade);
            zhiliangrenwudanPage.setProjname(selectprojname.getDepartName());
            zhiliangrenwudanPage.setSysOrgCode(selectprojname1.getOrgCodes());
            //新增功能
            liangJiasheguanli.setSysOrgCode(selectprojname1.getOrgCodes());
        }

        Date date = new Date();
        int num = (int) (Math.random() * 900 + 100);
        if (zhiliangrenwudanPage.getCode() == null) {
            zhiliangrenwudanPage.setCode(DateUtils.codeFormat("ZLRWD-"));
            liangJiasheguanli.setCode(DateUtils.codeFormat("ZLRWD-"));
        } else {
            zhiliangrenwudanPage.setCode("ZLRWD-" + zhiliangrenwudanPage.getCode() + "-" + num);
            liangJiasheguanli.setCode("ZLRWD-" + zhiliangrenwudanPage.getCode() + "-" + num);
        }
        //新增功能
        QueryWrapper<LiangJiasheguanli> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("projectname", liangJiasheguanli.getProjectname());
        queryWrapper.eq("kuaname", liangJiasheguanli.getKuaname());
        queryWrapper.eq("liangpian", liangJiasheguanli.getLiangpian());
        LiangJiasheguanli one = liangJiasheguanliService.getOne(queryWrapper);
        if (one != null) {
            LiangJiasheguanli liangJiasheguanli1 = liangJiasheguanli.setId(one.getId());
            liangJiasheguanliService.updateById(liangJiasheguanli1);
        } else {
            liangJiasheguanliService.save(liangJiasheguanli);
        }

        zhiliangrenwudanPage.setDattim(date);
        BeanUtils.copyProperties(zhiliangrenwudanPage, zhiliangrenwudan);
        if (zhiliangrenwudanPage.getZhiliangGongxuList().size() == 0) {
            List<ZhiliangGongxu> list = new ArrayList<>();
            for (int i = 0; i < 7; i++) {
                if (i == 0) {
                    ZhiliangGongxu zhiliangGongxu = new ZhiliangGongxu();
                    zhiliangGongxu.setXuhao(1);
                    zhiliangGongxu.setRemark("1");
                    zhiliangGongxu.setUnit(1);
                    zhiliangGongxu.setCreateBy(zhiliangrenwudan.getCreateBy());
                    list.add(zhiliangGongxu);
                } else if (i == 1) {
                    ZhiliangGongxu zhiliangGongxu = new ZhiliangGongxu();
                    zhiliangGongxu.setXuhao(11);
                    zhiliangGongxu.setRemark("1");
                    zhiliangGongxu.setUnit(1);
                    zhiliangGongxu.setCreateBy(zhiliangrenwudan.getCreateBy());
                    list.add(zhiliangGongxu);
                } else if (i == 2) {
                    ZhiliangGongxu zhiliangGongxu = new ZhiliangGongxu();
                    zhiliangGongxu.setXuhao(2);
                    zhiliangGongxu.setRemark("2");
                    zhiliangGongxu.setUnit(1);
                    zhiliangGongxu.setCreateBy(zhiliangrenwudan.getCreateBy());
                    list.add(zhiliangGongxu);
                } else if (i == 3) {
                    ZhiliangGongxu zhiliangGongxu = new ZhiliangGongxu();
                    zhiliangGongxu.setXuhao(4);
                    zhiliangGongxu.setRemark("240");
                    zhiliangGongxu.setUnit(1);
                    zhiliangGongxu.setCreateBy(zhiliangrenwudan.getCreateBy());
                    list.add(zhiliangGongxu);
                } else if (i == 4) {
                    ZhiliangGongxu zhiliangGongxu = new ZhiliangGongxu();
                    zhiliangGongxu.setXuhao(5);
                    zhiliangGongxu.setRemark("12");
                    zhiliangGongxu.setUnit(1);
                    zhiliangGongxu.setCreateBy(zhiliangrenwudan.getCreateBy());
                    list.add(zhiliangGongxu);
                } else if (i == 5) {
                    ZhiliangGongxu zhiliangGongxu = new ZhiliangGongxu();
                    zhiliangGongxu.setXuhao(10);
                    zhiliangGongxu.setRemark("12");
                    zhiliangGongxu.setUnit(1);
                    zhiliangGongxu.setCreateBy(zhiliangrenwudan.getCreateBy());
                    list.add(zhiliangGongxu);
                } else {
                    ZhiliangGongxu zhiliangGongxu = new ZhiliangGongxu();
                    zhiliangGongxu.setXuhao(7);
                    zhiliangGongxu.setRemark("1");
                    zhiliangGongxu.setUnit(1);
                    zhiliangGongxu.setCreateBy(zhiliangrenwudan.getCreateBy());
                    list.add(zhiliangGongxu);
                }
                zhiliangrenwudanPage.setZhiliangGongxuList(list);
            }
        }
        zhiliangrenwudanService.saveMain(zhiliangrenwudan, zhiliangrenwudanPage.getZhiliangGongxuList());
        return Result.OK("添加成功！");
    }

    /**
     * 任务单（制梁）编辑
     *
     * @param zhiliangrenwudanPage
     * @return
     */
    @AutoLog(value = "任务单（制梁）表信息-编辑")
    @ApiOperation(value = "任务单（制梁）表信息-编辑", notes = "任务单（制梁）表信息-编辑")
    @PutMapping(value = "/zlrenwudanedit")
    public Result<?> edit(@RequestBody ZhiliangrenwudanPage zhiliangrenwudanPage) {
        QueryWrapper<ZhiliangGongxu> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.eq("uuid", zhiliangrenwudanPage.getUuid());
        queryWrapper1.eq("xuhao", 7);
        queryWrapper1.eq("status", 1);
        ZhiliangGongxu zhiliangGongxu = zhiliangGongxuService.getOne(queryWrapper1);
        if (zhiliangGongxu != null) {//存梁工序已确认不可编辑
            return Result.error("该任务单不可编辑");
//			QueryWrapper<CunliangProcedureConfig> queryWrapper = new QueryWrapper<>();
//			queryWrapper.eq("shebeino", zhiliangGongxu.getShebeino());
//			queryWrapper.eq("lianghang", zhiliangGongxu.getCunlianghang());
//			queryWrapper.eq("lianglie", zhiliangGongxu.getCunlianglie());
//			queryWrapper.eq("liangzuoname",zhiliangGongxu.getTaizuoname());
//			queryWrapper.eq("dangqianceng", zhiliangGongxu.getLiangceng());
//			CunliangProcedureConfig cunliangProcedureConfig = cunliangProcedureConfigService.getOne(queryWrapper);
//			if (cunliangProcedureConfig != null) {
//				if ("1".equals(cunliangProcedureConfig.getDangqianceng())) {
//					cunliangProcedureConfigService.updatedata(cunliangProcedureConfig.getShebeino(),cunliangProcedureConfig.getLiangzuoname());
//				}
//				if ("2".equals(cunliangProcedureConfig.getDangqianceng())) {
//					cunliangProcedureConfigService.updatedatas(cunliangProcedureConfig.getShebeino(),cunliangProcedureConfig.getLiangzuoname());
//				}
//			}
        } else {
            Zhiliangrenwudan zhiliangrenwudan = new Zhiliangrenwudan();
            String projgrade = zhiliangrenwudanPage.getProjgrade();
            if (projgrade != null) {
                String A = projgrade.substring(0, 1);
                int y = projgrade.indexOf(A, projgrade.indexOf(A) + 1);
                String projgrade1 = projgrade.substring(0, y);
                String projectname = "";
                for (int x = projgrade.length(); x > 0; x = x - y) {
                    String projectnames = projgrade.substring(0, x);
                    SysDepartproject selectprojname = sysDepartprojectService.selectprojname(projectnames);
                    if (selectprojname != null) {
                        if (projectname.equals("")) {
                            projectname = selectprojname.getDepartName();
                        } else {
                            projectname = selectprojname.getDepartName() + " | " + projectname;
                        }

                    }
                }
                zhiliangrenwudanPage.setConspos(projectname);
                SysDepartproject selectprojname = sysDepartprojectService.selectprojname(projgrade1);
                SysDepartproject selectprojname1 = sysDepartprojectService.selectprojname(projgrade);
                zhiliangrenwudanPage.setProjname(selectprojname.getDepartName());
                zhiliangrenwudanPage.setSysOrgCode(selectprojname1.getOrgCodes());
            }
            BeanUtils.copyProperties(zhiliangrenwudanPage, zhiliangrenwudan);
            Zhiliangrenwudan zhiliangrenwudanEntity = zhiliangrenwudanService.getById(zhiliangrenwudan.getId());
            if (zhiliangrenwudanEntity == null) {
                return Result.error("未找到对应数据");
            }
            zhiliangrenwudanService.updateMain(zhiliangrenwudan, zhiliangrenwudanPage.getZhiliangGongxuList());
            return Result.OK("编辑成功!");
        }
    }

    /**
     * 添加
     *
     * @param deviceMixpileRwd
     * @return
     */
    @AutoLog(value = "软基工单表-添加")
    @ApiOperation(value = "软基工单表-添加", notes = "软基工单表-添加")
    @PostMapping(value = "/rjrwdadd")
    public Result<?> rjrwdadd(@RequestBody DeviceMixpileRwd deviceMixpileRwd) {
        String projgrade = "";
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        if (StringUtils.isNotBlank(deviceMixpileRwd.getMileageid())) {
            projgrade = deviceMixpileRwd.getMileageid();
        } else {
            String treeid = deviceMixpileRwd.getTreeid();
            if (StrUtil.isNotBlank(treeid)) {
                SysDepartproject selectprojnames = sysDepartprojectService.selectprojnames(treeid);
                projgrade = selectprojnames.getOrgCode();
                deviceMixpileRwd.setMileageid(projgrade);
            }
        }
        if (StringUtils.isNotBlank(projgrade)) {
            SysDepartproject selectprojname1 = sysDepartprojectService.selectprojname(projgrade);
            deviceMixpileRwd.setOrgcode(StringUtils.isNotBlank(selectprojname1.getOrgCodes()) ? selectprojname1.getOrgCodes() : loginUser.getOrgCode());
        } else {
            ShebeiInfo selectshebeione = shebeiInfoService.selectshebeione(deviceMixpileRwd.getShebeino());
            deviceMixpileRwd.setOrgcode(selectshebeione.getSysOrgCode());
        }
        deviceMixpileRwd.setRjrwd(DateUtils.codeFormat("RJRWD-"));
        deviceMixpileRwdService.save(deviceMixpileRwd);
        return Result.OK("添加成功！");
    }


    @AutoLog(value = "软基工单表-添加")
    @ApiOperation(value = "软基工单表-添加", notes = "软基工单表-添加")
    @PostMapping(value = "/rjrwdadd47")
    public Result<?> rjrwdadd47(@RequestBody DeviceMixpileRwd deviceMixpileRwd) {
        String projgrade = "";
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        if (StringUtils.isNotBlank(deviceMixpileRwd.getMileageid())) {
            projgrade = deviceMixpileRwd.getMileageid();
        } else {
            String treeid = deviceMixpileRwd.getTreeid();
            if (StrUtil.isNotBlank(treeid)) {
                SysDepartproject selectprojnames = sysDepartprojectService.selectprojnames(treeid);
                projgrade = selectprojnames.getOrgCode();
                deviceMixpileRwd.setMileageid(projgrade);
            }
        }
        if (StringUtils.isNotBlank(projgrade)) {
            SysDepartproject selectprojname1 = sysDepartprojectService.selectprojname(projgrade);
            deviceMixpileRwd.setMileage(selectprojname1.getDescription());// 里程名称
            deviceMixpileRwd.setTreeid(selectprojname1.getTreeid());// wbstreeid
            deviceMixpileRwd.setOrgcode(StringUtils.isNotBlank(selectprojname1.getOrgCodes()) ? selectprojname1.getOrgCodes() : loginUser.getOrgCode());
        } else {
            ShebeiInfo selectshebeione = shebeiInfoService.selectshebeione(deviceMixpileRwd.getShebeino());
            deviceMixpileRwd.setOrgcode(selectshebeione.getSysOrgCode());
        }
        deviceMixpileRwd.setRjrwd(DateUtils.codeFormat("RJRWD-"));
        deviceMixpileRwdService.save(deviceMixpileRwd);
        return Result.OK("添加成功！");
    }


    /**
     * 编辑
     *
     * @param deviceMixpileRwd
     * @return
     */
    @AutoLog(value = "软基工单表-编辑")
    @ApiOperation(value = "软基工单表-编辑", notes = "软基工单表-编辑")
    @PutMapping(value = "/rjrwdedit")
    public Result<?> rjrwdedit(@RequestBody DeviceMixpileRwd deviceMixpileRwd) {
        String projgrade = "";
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息
        if (StringUtils.isNotBlank(deviceMixpileRwd.getMileageid())) {
            projgrade = deviceMixpileRwd.getMileageid();
        } else {
            String treeid = deviceMixpileRwd.getTreeid();
            if (StrUtil.isNotBlank(treeid)) {
                SysDepartproject selectprojnames = sysDepartprojectService.selectprojnames(treeid);
                projgrade = selectprojnames.getOrgCode();
                deviceMixpileRwd.setMileageid(projgrade);
            }
        }
        if (StringUtils.isNotBlank(projgrade)) {
            SysDepartproject selectprojname1 = sysDepartprojectService.selectprojname(projgrade);
            deviceMixpileRwd.setOrgcode(StringUtils.isNotBlank(selectprojname1.getOrgCodes()) ? selectprojname1.getOrgCodes() : loginUser.getOrgCode());
        } else {
            ShebeiInfo selectshebeione = shebeiInfoService.selectshebeione(deviceMixpileRwd.getShebeino());
            deviceMixpileRwd.setOrgcode(selectshebeione.getSysOrgCode());
        }
        deviceMixpileRwdService.updateById(deviceMixpileRwd);
        return Result.OK("编辑成功!");
    }

    /**
     * 添加
     *
     * @param beamOrder
     * @return
     */
    @SneakyThrows
    @AutoLog(value = "粱订单信息表-添加")
    @ApiOperation(value = "粱订单信息表-添加", notes = "粱订单信息表-添加")
    @PostMapping(value = "/beamorderadd")
    public Result<?> add(@RequestBody BeamOrder beamOrder) {
        String projgrade = "";

        if (StringUtils.isNotBlank(beamOrder.getConstructionSiteid())) {
            projgrade = beamOrder.getConstructionSiteid();
        } else {
            String treeid = beamOrder.getTreeid();
            if (StrUtil.isNotBlank(treeid)) {
                SysDepartproject selectprojnames = sysDepartprojectService.selectprojnames(treeid);
                projgrade = selectprojnames.getOrgCode();
                beamOrder.setConstructionSiteid(projgrade);
            }
        }
        ShebeiInfo selectshebeione = shebeiInfoService.selectshebeione(beamOrder.getShebeino());
        if (StringUtils.isNotBlank(projgrade)) {
            String A = projgrade.substring(0, 1);
            int y = projgrade.indexOf(A, projgrade.indexOf(A) + 1);
            String projgrade1 = projgrade.substring(0, y);
            String projectname = "";
            for (int x = projgrade.length(); x > 0; x = x - y) {
                String projectnames = projgrade.substring(0, x);
                SysDepartproject selectprojname = sysDepartprojectService.selectprojname(projectnames);
                if (selectprojname != null) {
                    if (projectname.equals("")) {
                        projectname = selectprojname.getDepartName();
                    } else {
                        projectname = selectprojname.getDepartName() + " | " + projectname;
                    }

                }
            }
            beamOrder.setConstructionSite(projectname);
            SysDepartproject selectprojname1 = sysDepartprojectService.selectprojname(projgrade);
            beamOrder.setTreeid(selectprojname1.getId());
            beamOrder.setSysOrgCode(selectprojname1.getOrgCodes());
        } else {
            beamOrder.setSysOrgCode(selectshebeione.getSysOrgCode());
        }
        String uuid = UUID.randomUUID().toString();//随机生成唯一码UUID
        DateFormat format1 = new SimpleDateFormat("yyyyMMddHHmm");
        int num = (int) (Math.random() * 900 + 100);
        beamOrder.setUuid(uuid);
        if (org.springframework.util.StringUtils.isEmpty(beamOrder.getOrderno())) {
            beamOrder.setOrderno(format1.format(new Date()) + num);
        }
        beamOrderService.save(beamOrder);
        QueryWrapper<BhzPhone> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("uid", beamOrder.getOrderRecipient());
        BhzPhone one = bhzPhoneService.getOne(queryWrapper);
        SysMessage sysMessage = new SysMessage();
        if (one != null) {
            JSONObject obj = new JSONObject();
            obj.put("sbname", selectshebeione.getSbname());
            obj.put("time", DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));//数据上传时间;
            obj.put("content", beamOrder.getOrderno() + "订单已下发,请尽快生产梁");
            ISendMsgHandle sendMsgHandle = null;
            sysMessage.setEsTitle("梁板订单接收通知");
            sysMessage.setEsReceiver(one.getPhones());
            sysMessage.setEsContent(obj.toString());//短信内容
            sysMessage.setEsType("1");
            sysMessage.setCreateTime(new Date());
            sysMessage.setRemark(beamOrder.getShebeino());
            if (beamOrder.getIssend() == 1) {
                sysMessage.setEsSendStatus("0");//推送状态0未推送
                sysMessage.setEsSendNum(0);//推送总次数
            } else {
                sysMessage.setEsSendStatus("-1");//推送状态-1 不需要推送
                sysMessage.setEsSendNum(0);//推送总次数
                sysMessage.setEsResult("不需要推送");
            }
            sysMessageService.save(sysMessage);
            if ("0".equals(sysMessage.getEsSendStatus())) {
                Integer sendNum = sysMessage.getEsSendNum();
                sendMsgHandle = (ISendMsgHandle) Class.forName(SendMsgTypeEnum.SMS.getImplClass()).newInstance();
                try {
                    sendMsgHandle.SendMsg(sysMessage.getEsReceiver(), sysMessage.getEsTitle(),
                            sysMessage.getEsContent());
                    // 发送消息成功
                    sysMessage.setEsSendStatus(SendMsgStatusEnum.SUCCESS.getCode());
                } catch (Exception e) {
                    e.printStackTrace();
                    // 发送消息出现异常
                    sysMessage.setEsSendStatus(SendMsgStatusEnum.FAIL.getCode());
                }
                sysMessage.setEsSendNum(++sendNum);
                sysMessageService.updateById(sysMessage);
            }
        }
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param beamOrder
     * @return
     */
    @AutoLog(value = "粱订单信息表-编辑")
    @ApiOperation(value = "粱订单信息表-编辑", notes = "粱订单信息表-编辑")
    @PutMapping(value = "/beamorderedit")
    public Result<?> edit(@RequestBody BeamOrder beamOrder) {
        QueryWrapper<Zhiliangrenwudan> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("oderno", beamOrder.getOrderno());
        List<Zhiliangrenwudan> zhiliangrenwudanList = zhiliangrenwudanService.list(queryWrapper);
        if (zhiliangrenwudanList.size() > 0) {
            return Result.error("已创建制梁任务单, 禁止编辑!");
        } else {
            String projgrade = "";

            if (StringUtils.isNotBlank(beamOrder.getConstructionSiteid())) {
                projgrade = beamOrder.getConstructionSiteid();
            } else {
                String treeid = beamOrder.getTreeid();
                if (StrUtil.isNotBlank(treeid)) {
                    SysDepartproject selectprojnames = sysDepartprojectService.selectprojnames(treeid);
                    projgrade = selectprojnames.getOrgCode();
                    beamOrder.setConstructionSiteid(projgrade);
                }
            }
            ShebeiInfo selectshebeione = shebeiInfoService.selectshebeione(beamOrder.getShebeino());
            if (StringUtils.isNotBlank(projgrade)) {
                String A = projgrade.substring(0, 1);
                int y = projgrade.indexOf(A, projgrade.indexOf(A) + 1);
                String projgrade1 = projgrade.substring(0, y);
                String projectname = "";
                for (int x = projgrade.length(); x > 0; x = x - y) {
                    String projectnames = projgrade.substring(0, x);
                    SysDepartproject selectprojname = sysDepartprojectService.selectprojname(projectnames);
                    if (selectprojname != null) {
                        if (projectname.equals("")) {
                            projectname = selectprojname.getDepartName();
                        } else {
                            projectname = selectprojname.getDepartName() + " | " + projectname;
                        }

                    }
                }
                beamOrder.setConstructionSite(projectname);
                SysDepartproject selectprojname1 = sysDepartprojectService.selectprojname(projgrade);
                beamOrder.setSysOrgCode(selectprojname1.getOrgCodes());
            } else {
                beamOrder.setSysOrgCode(selectshebeione.getSysOrgCode());
            }
            beamOrderService.updateById(beamOrder);
            return Result.OK("编辑成功!");
        }
    }

    @GetMapping(value = "/queryByOrgcode")
    public Result<?> queryByOrgcode(@RequestParam(name = "id", required = true) String id) {
        QueryWrapper<SysDepartproject> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("org_code", id);
        SysDepartproject one = sysDepartprojectService.getOne(queryWrapper);

        QueryWrapper<SysDepartproject> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.eq("parent_id", one.getId());
        SysDepartproject one1 = sysDepartprojectService.getOne(queryWrapper1);

        if (one1 != null) {
            return Result.OK("date", "0");
        }
        return Result.OK("date", "1");
    }

    @GetMapping(value = "/querySyLxList")
    public Result<?> querySyLxList() {
        QueryWrapper<SysDepartproject> wrapper = new QueryWrapper<>();
        wrapper.lambda().likeRight(SysDepartproject::getOrgCode, "A98").eq(SysDepartproject::getMemo, 0);
        List<SysDepartproject> list = sysDepartprojectService.list(wrapper);
        return Result.OK(list);
    }

    public Boolean addSms(Bhzrenwudan bhzrenwudan) {
        QueryWrapper<BhzPhone> bhzPhoneQueryWrapper = new QueryWrapper<>();
        bhzPhoneQueryWrapper.eq("phonesname", 29);
        bhzPhoneQueryWrapper.eq("sys_org_code", bhzrenwudan.getSysOrgCode());
        BhzPhone one = bhzPhoneService.getOne(bhzPhoneQueryWrapper);
        if (ObjectUtil.isNotEmpty(one)) {
            SysMessageCore sysSms = new SysMessageCore();
            sysSms.setEsTitle("浇筑令下发提醒");
            sysSms.setEsType("1");
            sysSms.setEsReceiver(one.getPhones());
            JSONObject obj = new JSONObject();
            obj.put("sbname", bhzrenwudan.getProjname());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            obj.put("time", sdf.format(bhzrenwudan.getBegtim()));
            obj.put("content", "浇筑令已下发，请相关人员管理平台按时下发配料单！！");
            sysSms.setEsContent(obj.toString());
            sysSms.setEsSendStatus("0");
            sysSms.setEsSendNum(0);
            return sysMessageCoreService.save(sysSms);
        }
        return false;
    }

}
