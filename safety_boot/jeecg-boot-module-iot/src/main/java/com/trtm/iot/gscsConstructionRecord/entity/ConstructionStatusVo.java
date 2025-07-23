package com.trtm.iot.gscsConstructionRecord.entity;

import lombok.Data;

@Data
public class ConstructionStatusVo {
    /** 当前日期 */
    String currentTime;

    /** 标段下班组数 */
    Integer deptCount;

    /** 未施工统计 */
    Integer noConstruction;

    /** 已提交班前会统计 */
    Integer memCommit;

    /** 施工未提交统计 */
    Integer ConstructionUncommitted;
}
