package com.trtm.iot.lqbhz.entity;

/**
 * @Description: TODO
 * @author: scott
 * @date: 2021年11月11日 15:31
 */
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Description: TODO
 * @author: scott
 * @date: 2021年11月11日 8:58
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class YouShiBi implements Serializable {
    private String youshibi;//油石比
    private String shebeibianhao;//设备编号
    private String chuliaoshijian;//出料时间
    private String llysb;//理论石油比
}
