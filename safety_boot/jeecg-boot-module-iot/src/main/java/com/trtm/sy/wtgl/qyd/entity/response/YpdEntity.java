package com.trtm.sy.wtgl.qyd.entity.response;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Data
@ApiModel(value="返回样品单给前端新增样品", description="返回样品单给前端新增样品")
public class YpdEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer quyangdanid; //取样单id
    private Integer wztzid; //wztaizhang id
    private String titCode; //样品类型，试验类型
    private String cailiaoNo; //材料编号
    private String sampleName; //样品名称
    private String sampleDescribe; //样品描述
    private String sampleDate; //取样日期
    private String sampleYangPinShuLiang; //样品数量
    private String sampleDaiBiaoShuLiang; //代表数量
    private String sampleShengChanPiHao; //生产批号
    private String quyangdidian; //料仓编号
    private String sampleQuYangDiDian; // 取样地点
    private String sampleQuYangRen; //取样人
    private String shifouliuyang; // 是否留样
    private String ShiYanYiJu; //实验依据
    private String PanDingYiJu; //判定依据
    private String sampleRemark; //备注
    private String orgCode;
    private String usePart;
    private String pici;

    private List<Map<String, Object>> qyPic;
    private List<Map<String, Object>> syPic;

}
