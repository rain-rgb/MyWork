package org.jeecg.modules.job.lab.push.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author lis1
 * @date 2023/1/10
 * @time 15:43
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SandStorageVo implements Serializable {

    private static final long serialVersionUID = 1L;

    //原材料id
    private String sandId;

    //拌合站id
    private String mixingId;

    //沙石名称
    private String sandName;

    //存储
    private int storage;

    //罐体存储物状态：1合格,2不合格(材料状态)
    private String storageStatus;

    //创建人
    private String createId;
}
