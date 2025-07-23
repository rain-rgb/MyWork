package com.trtm.sy.sysbjdjz.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;

@Data
public class SbJianDingJiaoZhunResponse {

    private String id;

    private String jiandingjiaozhunriqi;

    private String jiandingjiaozhunren;

    private String jiandingjiaozhundanwei;

    private String jiandingjiaozhunjieguo;

    private Integer jiandingjiaozhunleibie;

    private String jiandingjiaozhunbeizhu;

    private String chuangjianren;

    private String chuangjianriqi;

    private String shebeiid;

    private String shebeiname;
    private String shebeino;
}
