package com.trtm.sy.wtgl.qywtd.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * 查询条件
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class QueryVo implements Serializable {
    private static final long serialVersionUID = 1L;

    private String cailiaono;//材料编号
    private String titCode;//试验类型
    private String usePart;//使用部位
    private String guids;//材料厂家guid
    private String jinchangshijian;//进场时间
    private Integer delegateState;//委托状态
    private String quyangren;//取样人
    private String quyangshijian;//取样时间
    private String shouyangren;//收样人
    private String shouyangshijian;//收样时间
    private Integer quyangzhuangtai;//取样状态
    private Integer shouyangzhuangtai;//收样状态
    private String sys_depart_orgcode;//组织机构
    private String nodeType;//材料类型
    private Integer zhipaizhuangtai;//指派状态
    private List<String> caiLiaoNos;
    private String guigexinghao;//规格型号
    private String pici;//批次
    private String orgCode;
}
