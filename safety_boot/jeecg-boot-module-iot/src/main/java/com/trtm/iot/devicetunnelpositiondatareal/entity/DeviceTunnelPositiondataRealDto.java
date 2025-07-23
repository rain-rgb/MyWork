package com.trtm.iot.devicetunnelpositiondatareal.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zhangqi
 * @date 2023/3/14
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeviceTunnelPositiondataRealDto extends DeviceTunnelPositiondataReal {

    String orgCode;
    String sbname;
    String sbtype;
}
