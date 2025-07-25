package org.jeecg.modules.system.controller;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.trtm.iot.bhzSupervisionOrder.entity.BhzSupervisionOrder;
import com.trtm.iot.bhzSupervisionOrder.service.IBhzSupervisionOrderService;
import com.trtm.iot.bhzSupervisionOrderSub.entity.BhzSupervisionOrderSub;
import com.trtm.iot.bhzSupervisionOrderSub.service.IBhzSupervisionOrderSubService;
import com.trtm.iot.bhzcfg.entity.BhzCallCfg;
import com.trtm.iot.bhzcfg.service.IBhzCallCfgService;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.exception.JeecgBootException;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.oss.entity.OSSFile;
import org.jeecg.modules.oss.service.IOSSFileService;
import org.jeecg.modules.system.entity.SysAnnouncement;
import org.jeecg.modules.system.entity.SysAnnouncementSend;
import org.jeecg.modules.system.entity.SysUser;
import org.jeecg.modules.system.model.AnnouncementSendModel;
import org.jeecg.modules.system.service.ISysAnnouncementSendService;
import org.jeecg.modules.system.service.ISysAnnouncementService;
import org.jeecg.modules.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import lombok.extern.slf4j.Slf4j;

/**
 * @Title: Controller
 * @Description: 用户通告阅读标记表
 * @Author: jeecg-boot
 * @Date: 2019-02-21
 * @Version: V1.0
 */
@RestController
@RequestMapping("/sys/sysAnnouncementSend")
@Slf4j
public class SysAnnouncementSendController {
    @Autowired
    private ISysAnnouncementSendService sysAnnouncementSendService;
    @Autowired
    private ISysAnnouncementService sysAnnouncementService;
    @Autowired
    private IBhzCallCfgService bhzCallCfgService;
    @Autowired
    private IBhzSupervisionOrderService bhzSupervisionOrderService;
    @Autowired
    private IBhzSupervisionOrderSubService bhzSupervisionOrderSubService;
    @Autowired
    private ISysUserService sysUserService;
    @Autowired
    private IOSSFileService iossFileService;


    /**
     * 分页列表查询
     *
     * @param sysAnnouncementSend
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @GetMapping(value = "/list")
    public Result<IPage<SysAnnouncementSend>> queryPageList(SysAnnouncementSend sysAnnouncementSend,
                                                            @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                            @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                            HttpServletRequest req) {
        Result<IPage<SysAnnouncementSend>> result = new Result<IPage<SysAnnouncementSend>>();
        QueryWrapper<SysAnnouncementSend> queryWrapper = new QueryWrapper<SysAnnouncementSend>(sysAnnouncementSend);
        Page<SysAnnouncementSend> page = new Page<SysAnnouncementSend>(pageNo, pageSize);
        //排序逻辑 处理
        String column = req.getParameter("column");
        String order = req.getParameter("order");
        if (oConvertUtils.isNotEmpty(column) && oConvertUtils.isNotEmpty(order)) {
            if ("asc".equals(order)) {
                queryWrapper.orderByAsc(oConvertUtils.camelToUnderline(column));
            } else {
                queryWrapper.orderByDesc(oConvertUtils.camelToUnderline(column));
            }
        }
        IPage<SysAnnouncementSend> pageList = sysAnnouncementSendService.page(page, queryWrapper);
        //log.info("查询当前页："+pageList.getCurrent());
        //log.info("查询当前页数量："+pageList.getSize());
        //log.info("查询结果数量："+pageList.getRecords().size());
        //log.info("数据总数："+pageList.getTotal());
        result.setSuccess(true);
        result.setResult(pageList);
        return result;
    }

    /**
     * 添加
     *
     * @param sysAnnouncementSend
     * @return
     */
    @PostMapping(value = "/add")
    public Result<SysAnnouncementSend> add(@RequestBody SysAnnouncementSend sysAnnouncementSend) {
        Result<SysAnnouncementSend> result = new Result<SysAnnouncementSend>();
        try {
            sysAnnouncementSendService.save(sysAnnouncementSend);
            result.success("添加成功！");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            result.error500("操作失败");
        }
        return result;
    }

    /**
     * 编辑
     *
     * @param sysAnnouncementSend
     * @return
     */
    @PutMapping(value = "/edit")
    public Result<SysAnnouncementSend> eidt(@RequestBody SysAnnouncementSend sysAnnouncementSend) {
        Result<SysAnnouncementSend> result = new Result<SysAnnouncementSend>();
        SysAnnouncementSend sysAnnouncementSendEntity = sysAnnouncementSendService.getById(sysAnnouncementSend.getId());
        if (sysAnnouncementSendEntity == null) {
            result.error500("未找到对应实体");
        } else {
            boolean ok = sysAnnouncementSendService.updateById(sysAnnouncementSend);
            //TODO 返回false说明什么？
            if (ok) {
                result.success("修改成功!");
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
    @DeleteMapping(value = "/delete")
    public Result<SysAnnouncementSend> delete(@RequestParam(name = "id", required = true) String id) {
        Result<SysAnnouncementSend> result = new Result<SysAnnouncementSend>();
        SysAnnouncementSend sysAnnouncementSend = sysAnnouncementSendService.getById(id);
        if (sysAnnouncementSend == null) {
            result.error500("未找到对应实体");
        } else {
            boolean ok = sysAnnouncementSendService.removeById(id);
            if (ok) {
                result.success("删除成功!");
            }
        }

        return result;
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @DeleteMapping(value = "/deleteBatch")
    public Result<SysAnnouncementSend> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        Result<SysAnnouncementSend> result = new Result<SysAnnouncementSend>();
        if (ids == null || "".equals(ids.trim())) {
            result.error500("参数不识别！");
        } else {
            this.sysAnnouncementSendService.removeByIds(Arrays.asList(ids.split(",")));
            result.success("删除成功!");
        }
        return result;
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/queryById")
    public Result<SysAnnouncementSend> queryById(@RequestParam(name = "id", required = true) String id) {
        Result<SysAnnouncementSend> result = new Result<SysAnnouncementSend>();
        SysAnnouncementSend sysAnnouncementSend = sysAnnouncementSendService.getById(id);
        if (sysAnnouncementSend == null) {
            result.error500("未找到对应实体");
        } else {
            result.setResult(sysAnnouncementSend);
            result.setSuccess(true);
        }
        return result;
    }

    /**
     * @param json
     * @return
     * @功能：更新用户系统消息阅读状态
     */
    @PutMapping(value = "/editByAnntIdAndUserId")
    public Result<SysAnnouncementSend> editById(@RequestBody JSONObject json) {
        Result<SysAnnouncementSend> result = new Result<SysAnnouncementSend>();
        String anntId = json.getString("anntId");
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        String userId = sysUser.getId();
        LambdaUpdateWrapper<SysAnnouncementSend> updateWrapper = new UpdateWrapper().lambda();
        updateWrapper.set(SysAnnouncementSend::getReadFlag, CommonConstant.HAS_READ_FLAG);
        updateWrapper.set(SysAnnouncementSend::getReadTime, new Date());
        updateWrapper.last("where annt_id ='" + anntId + "' and user_id ='" + userId + "'");
        SysAnnouncementSend announcementSend = new SysAnnouncementSend();
        sysAnnouncementSendService.update(announcementSend, updateWrapper);
        result.setSuccess(true);
        return result;
    }

    /**
     * @return
     * @功能：获取我的消息
     */
    @GetMapping(value = "/getMyAnnouncementSend")
    public Result<IPage<AnnouncementSendModel>> getMyAnnouncementSend(AnnouncementSendModel announcementSendModel,
                                                                      @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                                      @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        Result<IPage<AnnouncementSendModel>> result = new Result<IPage<AnnouncementSendModel>>();
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        String userId = sysUser.getId();
        announcementSendModel.setUserId(userId);
        announcementSendModel.setPageNo((pageNo - 1) * pageSize);
        announcementSendModel.setPageSize(pageSize);
        Page<AnnouncementSendModel> pageList = new Page<AnnouncementSendModel>(pageNo, pageSize);
        pageList = sysAnnouncementSendService.getMyAnnouncementSendPage(pageList, announcementSendModel);
        result.setResult(pageList);
        result.setSuccess(true);
        return result;
    }

    /**
     * @return
     * @功能：一键已读
     */
    @PutMapping(value = "/readAll")
    public Result<SysAnnouncementSend> readAll() {
        Result<SysAnnouncementSend> result = new Result<SysAnnouncementSend>();
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        String userId = sysUser.getId();
        LambdaUpdateWrapper<SysAnnouncementSend> updateWrapper = new UpdateWrapper().lambda();
        updateWrapper.set(SysAnnouncementSend::getReadFlag, CommonConstant.HAS_READ_FLAG);
        updateWrapper.set(SysAnnouncementSend::getReadTime, new Date());
        updateWrapper.last("where user_id ='" + userId + "'");
        SysAnnouncementSend announcementSend = new SysAnnouncementSend();
        sysAnnouncementSendService.update(announcementSend, updateWrapper);
        result.setSuccess(true);
        result.setMessage("全部已读");
        return result;
    }


    @GetMapping(value = "/add1")
    public Result<?> add1(String userId, String signatureOpinion, Integer status, String batchNo, String copyUserId) {
//        String column = "%"+batchNo+"%";
//        OSSFile ossFile = iossFileService.selectByUrlColumn(column);
//        if(ossFile == null){
//
//        }
        status = status + 1;
        //校验当前用户是否与设置用户一致
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        String loginUserId = loginUser.getId();
        String username = loginUser.getUsername();
        boolean match = matchUserName(username, batchNo, status);
        if(!match){
            throw new JeecgBootException("当前用户无权签署");
        }

        QueryWrapper<SysUser> sysUserQueryWrapper = new QueryWrapper<>();
        sysUserQueryWrapper.eq("id", userId);
        SysUser sysUser = sysUserService.getOne(sysUserQueryWrapper);
        BhzSupervisionOrderSub sub = new BhzSupervisionOrderSub();
        sub.setBatchNo(batchNo);
        sub.setStatus(status);
        sub.setUsername(sysUser.getUsername());
        sub.setRealname(sysUser.getRealname());
        sub.setSignatureOpinion(signatureOpinion);
        sub.setSignatureTime(new Date());
        boolean save = bhzSupervisionOrderSubService.save(sub);
        if (save) {
            QueryWrapper<BhzSupervisionOrder> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("batch_no", batchNo);
            BhzSupervisionOrder one = bhzSupervisionOrderService.getOne(queryWrapper);
            one.setStatus(status);
            bhzSupervisionOrderService.updateById(one);
            QueryWrapper<SysAnnouncement> announcementQuery = new QueryWrapper<>();
            announcementQuery.eq("order_no", one.getBhzNo());
            SysAnnouncement sysAnnouncement = sysAnnouncementService.getOne(announcementQuery);
            String id = sysAnnouncement.getId();
            switch (status) {
                case 1:
                    addSendData(id, userId);
                    break;
                case 2:
                    updateSendData(id);
                    addSendData(id, userId);
                    addSendData(id, copyUserId);
                    break;
                case 3:
                    updateSendData(id);
                    addSendData(id, userId);
                    break;
            }
        }

        return Result.OK("添加成功！");
    }

    //更新状态
    public void updateSendData(String annt_id) {
        QueryWrapper<SysAnnouncementSend> sendQueryWrapper = new QueryWrapper<>();
        sendQueryWrapper.eq("annt_id", annt_id);
        sendQueryWrapper.eq("read_flag", 0);
        List<SysAnnouncementSend> send = sysAnnouncementSendService.list(sendQueryWrapper);
        for (SysAnnouncementSend sysAnnouncementSend : send) {
            sysAnnouncementSend.setReadFlag("1");
            sysAnnouncementSend.setReadTime(new Date());
            sysAnnouncementSendService.updateById(sysAnnouncementSend);
        }


    }
    //添加记录
    public void addSendData(String annt_id, String user_id) {
        SysAnnouncementSend send = new SysAnnouncementSend();
        send.setAnntId(annt_id);
        send.setUserId(user_id);
        send.setReadFlag("0");
        send.setCreateTime(new Date());
        sysAnnouncementSendService.save(send);

    }
    //校验用户
    public boolean matchUserName(String loginUserName,String batchNo,Integer status){
        QueryWrapper<BhzSupervisionOrder> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("batch_no", batchNo);
        BhzSupervisionOrder one = bhzSupervisionOrderService.getOne(queryWrapper);
        String shebeiNo = one.getShebeiNo();
        QueryWrapper<BhzCallCfg> callCfgQueryWrapper = new QueryWrapper<>();
        callCfgQueryWrapper.eq("bhjno",shebeiNo);
        BhzCallCfg cfg = bhzCallCfgService.getOne(callCfgQueryWrapper);
        String name ="";
        if(status == 1){
            name = cfg.getShperson();
        }else if(status == 2){
            name = cfg.getSupervisorPerson();
        }else if(status ==3){
            name = cfg.getCzperson();
        }
        if(name.equals(loginUserName)){
            return true;
        }
        return false;
    }


}
