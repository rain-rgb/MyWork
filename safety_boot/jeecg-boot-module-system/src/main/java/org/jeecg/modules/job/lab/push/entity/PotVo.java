package org.jeecg.modules.job.lab.push.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author lis1
 * @date 2023/1/9
 * @time 11:30
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PotVo implements Serializable {

    private static final long serialVersionUID = 1L;

    //料仓id
    private String potId;

    //拌合站id
    private String mixingId;

    //设备id
    private String crewId;

    //料仓名称
    private String potName;

    //罐体存储物名称(材料名称)
    private String storageName;

    //罐体存储物容量(材料容量)
    private String storageCapacity;

    //罐体存储物状态：0待检,1合格,2不合格(材料状态)
    private String storageStatus;

    //创建人
    private String createId;

}
