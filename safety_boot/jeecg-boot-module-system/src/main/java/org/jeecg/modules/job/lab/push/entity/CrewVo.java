package org.jeecg.modules.job.lab.push.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author lis1
 * @date 2022/12/15
 * @time 11:04
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CrewVo implements Serializable {

    private static final long serialVersionUID = 1L;

    // 机组id(设备id)
    private String crewId;

    // 拌合站id
    private String mixingId;

    // 企业id
    private Integer companyId;

    // 项目id
    private Integer projectId;

    // 机组名称(设备名称)
    private String crewName;

    // 创建人
    private String createId;
}
