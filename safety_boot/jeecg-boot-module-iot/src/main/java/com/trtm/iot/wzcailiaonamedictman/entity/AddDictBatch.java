package com.trtm.iot.wzcailiaonamedictman.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.trtm.iot.wzcailiaonamedict.entity.Wzcailiaonamedict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @Description: wzcailiaonamedict_man
 * @Author: jeecg-boot
 * @Date:   2022-08-08
 * @Version: V1.0
 */
@Data

@ApiModel(value="wzcailiaonamedict_man对象", description="wzcailiaonamedict_man")
public class AddDictBatch implements Serializable {
    private static final long serialVersionUID = 1L;

    List<WzcailiaonamedictMan> wzcailiaonamedictManList;
    List<Wzcailiaonamedict> wzcailiaonamedictList;
    String sysOrgCode;
    Integer iselocks;
    Integer lmcailiaolx;
}
