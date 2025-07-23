package com.trtm.iot.gscsConstructionRecord.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.trtm.iot.gscsConstructionRecord.entity.ConstructionStatusVo;
import com.trtm.iot.gscsConstructionRecord.entity.GscsConstructionRecord;
import com.trtm.iot.gscsConstructionRecord.mapper.GscsConstructionRecordMapper;
import com.trtm.iot.gscsConstructionRecord.service.IGscsConstructionRecordService;
import com.trtm.iot.gscsSectionDept.entity.GscsSectionDept;
import com.trtm.iot.gscsSectionDept.mapper.GscsSectionDeptMapper;
import com.trtm.iot.gscsToolboxTalk.entity.GscsToolboxTalk;
import com.trtm.iot.gscsToolboxTalk.mapper.GscsToolboxTalkMapper;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Description: 班组安全管控系统未施工记录表
 * @Author: jeecg-boot
 * @Date: 2022-01-25
 * @Version: V1.0
 */
@Service
public class GscsConstructionRecordServiceImpl extends ServiceImpl<GscsConstructionRecordMapper, GscsConstructionRecord> implements IGscsConstructionRecordService {

    @Resource
    GscsConstructionRecordMapper gscsConstructionRecordMapper;

    @Resource
    GscsToolboxTalkMapper gscsToolboxTalkMapper;

    @Resource
    GscsSectionDeptMapper gscsSectionDeptMapper;

    /**
     * 获取今日三种施工和班组会上传状态统计
     *
     * @return
     */
    @Override
    public ConstructionStatusVo getConstructionStatusSta(String section) {
        String nowDate = getNowDate();
        Integer noConstruction = getNoConstruction(nowDate,section);
        Integer commitCount = getCommitCount(nowDate,section);
        Integer deptCount = getDeptCount(section);
        ConstructionStatusVo constructionStatusVo = new ConstructionStatusVo();
        constructionStatusVo.setNoConstruction(noConstruction);
        constructionStatusVo.setMemCommit(commitCount);
        constructionStatusVo.setDeptCount(deptCount);
        constructionStatusVo.setCurrentTime(nowDate);
        //  班组总数 - 已提交班前会数量 - 未施工班组数 = 施工未提交数量
        constructionStatusVo.setConstructionUncommitted(deptCount - noConstruction - commitCount);
        return constructionStatusVo;
    }

    /**
     * 获取历史施工状态记录
     *
     * @return
     */
    @Override
    public List<ConstructionStatusVo> getHistoryConstructionStatusSta(String section) {
        Integer deptCount = getDeptCount(section);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        List<ConstructionStatusVo> list = new ArrayList<>();
        //得到当日至15天前的所有日期
        for (int i = 0; i < 15; i++) {
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DAY_OF_MONTH, -(15 - i));
            Date time = cal.getTime();
            String formatTime = simpleDateFormat.format(time);
            Integer commitCount = getCommitCount(formatTime,section);
            Integer noConstruction = getNoConstruction(formatTime,section);
            ConstructionStatusVo constructionStatusVo = new ConstructionStatusVo();
            constructionStatusVo.setNoConstruction(noConstruction);
            constructionStatusVo.setMemCommit(commitCount);
            constructionStatusVo.setDeptCount(deptCount);
            //  班组总数 - 已提交班前会数量 - 未施工班组数 = 施工未提交数量
            constructionStatusVo.setConstructionUncommitted(deptCount - noConstruction - commitCount);
            constructionStatusVo.setCurrentTime(formatTime);
            list.add(constructionStatusVo);
        }
        return list;
    }

    //查询当前班组当日是否上传班前会
    @Override
    public Boolean getConstructionStatus(String deptId) {
        QueryWrapper<GscsToolboxTalk> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("upload_date", getNowDate()).eq("dept_id", deptId);
        GscsToolboxTalk gscsToolboxTalk = gscsToolboxTalkMapper.selectOne(queryWrapper);
        if (gscsToolboxTalk != null) {
            return true;
        }
        return false;
    }

    //获取当日时间
    public static String getNowDate() {
        Date now = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return simpleDateFormat.format(now);
    }

    //查询标段下班组总数
    public Integer getDeptCount(String section) {
        QueryWrapper<GscsSectionDept> queryWrapper = new QueryWrapper();
        queryWrapper.likeRight("section", section);
        return gscsSectionDeptMapper.selectCount(queryWrapper);
    }

    //查询当日已提交班前会班组数量
    public Integer getCommitCount(String date,String section) {
        QueryWrapper<GscsToolboxTalk> queryWrapper = new QueryWrapper();
        queryWrapper.eq("upload_date", date).likeRight("section",section);
        return gscsToolboxTalkMapper.selectCount(queryWrapper);
    }

    //查询当日未施工班组数量
    public Integer getNoConstruction(String date,String section) {
        QueryWrapper<GscsConstructionRecord> queryWrapper = new QueryWrapper();
        queryWrapper.eq("upload_date", date).eq("is_construction", 1).likeRight("section",section);
        return gscsConstructionRecordMapper.selectCount(queryWrapper);
    }
}
