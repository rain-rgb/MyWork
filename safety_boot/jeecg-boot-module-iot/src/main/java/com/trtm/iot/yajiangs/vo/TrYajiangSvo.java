package com.trtm.iot.yajiangs.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecgframework.poi.excel.annotation.Excel;

import java.io.Serializable;

/**
 * @Description: 压浆实时数据表
 * @Author: jeecg-boot
 * @Date:   2021-09-06
 * @Version: V1.0
 */
@Data
public class TrYajiangSvo implements Serializable {
    private String uuid;
    private String zlsj;
    private String yjsj;
    private String yjwd;
    private String shuijiaobi;
    private String yjd;
    private String lljl;
    private String sjjl;
    private String sjmd;
    private String sjmdm;
    private String jinjiangyal;
    private String wysj;
    private Integer isOverLevel;
    private Integer overproofStatus;
}
