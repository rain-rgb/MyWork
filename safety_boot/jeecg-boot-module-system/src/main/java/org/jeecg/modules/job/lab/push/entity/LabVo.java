package org.jeecg.modules.job.lab.push.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author lis1
 * @date 2022/12/13
 * @time 10:28
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LabVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private String labId;

    private String labName;

    private Integer companyId;

    private Integer projectId;

    private String gmtCreate;

    private String createId;

}
