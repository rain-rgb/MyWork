package com.trtm.iot.cunliangprocedureconfig.vo;

import lombok.Data;

@Data
public class CunliangVO {
    /**
     * 存梁总数
     */
    private int sum;
    /**
     * 空闲存梁数
     */
    private int cunliang;
    // 计算方法：1-空闲存梁数/（存梁总数/2）
}
