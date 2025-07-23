package com.trtm.iot.hntbhz.vo;

import com.trtm.iot.hntbhz.entity.BhzCementBase;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class UpdateBatchById  implements Serializable {
    private static final long serialVersionUID = 1L;

    private List<BhzCementBase> ids;
    private  String phbcode;
    private String rwdcode;
    private Integer station;
    private String sbjno;
    private String note;
    private int renwudanstatus = 28;

}
