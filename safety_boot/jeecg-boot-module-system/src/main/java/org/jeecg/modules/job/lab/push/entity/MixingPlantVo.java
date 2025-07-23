package org.jeecg.modules.job.lab.push.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author lis1
 * @date 2022/12/14
 * @time 10:35
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MixingPlantVo implements Serializable {

    private static final long serialVersionUID = 1L;

    // 拌合站ID
    private String mixId;

    // 机组名称
    private String mixName;

    // 企业ID
    private Integer companyId;

    // 项目ID
    private Integer projectId;

    // 创建人
    private String createId;

}
