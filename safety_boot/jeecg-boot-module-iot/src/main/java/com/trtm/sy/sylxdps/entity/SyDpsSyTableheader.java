package com.trtm.sy.sylxdps.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecg.common.aspect.annotation.Dict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: sy_dps_sy_tableheader
 * @Author: jeecg-boot
 * @Date:   2023-02-10
 * @Version: V1.0
 */
@Data
@TableName("sy_dps_sy_tableheader")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="sy_dps_sy_tableheader对象", description="sy_dps_sy_tableheader")
public class SyDpsSyTableheader implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.ASSIGN_UUID)
    @ApiModelProperty(value = "id")
    private String id;
	/**departid*/
	@Excel(name = "departid", width = 15)
    @ApiModelProperty(value = "departid")
    private String departid;
	/**tablenumber*/
	@Excel(name = "tablenumber", width = 15)
    @ApiModelProperty(value = "tablenumber")
    private String tablenumber;
	/**renwuno*/
	@Excel(name = "renwuno", width = 15)
    @ApiModelProperty(value = "renwuno")
    private String renwuno;
	/**samplegcbw*/
	@Excel(name = "samplegcbw", width = 15)
    @ApiModelProperty(value = "samplegcbw")
    private String samplegcbw;
	/**shiyanyiju*/
	@Excel(name = "shiyanyiju", width = 15)
    @ApiModelProperty(value = "shiyanyiju")
    private String shiyanyiju;
	/**sampleno*/
	@Excel(name = "sampleno", width = 15)
    @ApiModelProperty(value = "sampleno")
    private String sampleno;
	/**sampledescribe*/
	@Excel(name = "sampledescribe", width = 15)
    @ApiModelProperty(value = "sampledescribe")
    private String sampledescribe;
	/**samplename*/
	@Excel(name = "samplename", width = 15)
    @ApiModelProperty(value = "samplename")
    private String samplename;
	/**shiyantiaojian*/
	@Excel(name = "shiyantiaojian", width = 15)
    @ApiModelProperty(value = "shiyantiaojian")
    private String shiyantiaojian;
	/**shiyanriqi*/
	@Excel(name = "shiyanriqi", width = 15)
    @ApiModelProperty(value = "shiyanriqi")
    private String shiyanriqi;
	/**yiqishebei*/
	@Excel(name = "yiqishebei", width = 15)
    @ApiModelProperty(value = "yiqishebei")
    private String yiqishebei;
	/**reportno*/
	@Excel(name = "reportno", width = 15)
    @ApiModelProperty(value = "reportno")
    private String reportno;
	/**remark*/
	@Excel(name = "remark", width = 15)
    @ApiModelProperty(value = "remark")
    private String remark;
	/**外键，试验项目编号/试验报告编号(dps_jc_testItem)*/
	@Excel(name = "外键，试验项目编号/试验报告编号(dps_jc_testItem)", width = 15)
    @ApiModelProperty(value = "外键，试验项目编号/试验报告编号(dps_jc_testItem)")
    private String tino;
	/**tinotemp*/
	@Excel(name = "tinotemp", width = 15)
    @ApiModelProperty(value = "tinotemp")
    private Integer tinotemp;
	/**gongchengmingcheng*/
	@Excel(name = "gongchengmingcheng", width = 15)
    @ApiModelProperty(value = "gongchengmingcheng")
    private String gongchengmingcheng;
	/**pandingyiju*/
	@Excel(name = "pandingyiju", width = 15)
    @ApiModelProperty(value = "pandingyiju")
    private String pandingyiju;
	/**shigongdanwei*/
	@Excel(name = "shigongdanwei", width = 15)
    @ApiModelProperty(value = "shigongdanwei")
    private String shigongdanwei;
	/**departfullname*/
	@Excel(name = "departfullname", width = 15)
    @ApiModelProperty(value = "departfullname")
    private String departfullname;
	/**gongzhangmingcheng*/
	@Excel(name = "gongzhangmingcheng", width = 15)
    @ApiModelProperty(value = "gongzhangmingcheng")
    private String gongzhangmingcheng;
	/**baogaoriqi*/
	@Excel(name = "baogaoriqi", width = 15)
    @ApiModelProperty(value = "baogaoriqi")
    private String baogaoriqi;
	/**hetonghao*/
	@Excel(name = "hetonghao", width = 15)
    @ApiModelProperty(value = "hetonghao")
    private String hetonghao;
	/**jianlidanwei*/
	@Excel(name = "jianlidanwei", width = 15)
    @ApiModelProperty(value = "jianlidanwei")
    private String jianlidanwei;
	/**samplequyangdidian*/
	@Excel(name = "samplequyangdidian", width = 15)
    @ApiModelProperty(value = "samplequyangdidian")
    private String samplequyangdidian;
	/**sampledaibiaoshuliang*/
	@Excel(name = "sampledaibiaoshuliang", width = 15)
    @ApiModelProperty(value = "sampledaibiaoshuliang")
    private String sampledaibiaoshuliang;
	/**sampleshengchanchangjia*/
	@Excel(name = "sampleshengchanchangjia", width = 15)
    @ApiModelProperty(value = "sampleshengchanchangjia")
    private String sampleshengchanchangjia;
	/**samplepihao*/
	@Excel(name = "samplepihao", width = 15)
    @ApiModelProperty(value = "samplepihao")
    private String samplepihao;
	/**sampleyanghutiaojian*/
	@Excel(name = "sampleyanghutiaojian", width = 15)
    @ApiModelProperty(value = "sampleyanghutiaojian")
    private String sampleyanghutiaojian;
	/**sampledate*/
	@Excel(name = "sampledate", width = 15)
    @ApiModelProperty(value = "sampledate")
    private String sampledate;
	/**samplechandi*/
	@Excel(name = "samplechandi", width = 15)
    @ApiModelProperty(value = "samplechandi")
    private String samplechandi;
	/**sampleshengchanriqi*/
	@Excel(name = "sampleshengchanriqi", width = 15)
    @ApiModelProperty(value = "sampleshengchanriqi")
    private String sampleshengchanriqi;
	/**sampleguigexinghao*/
	@Excel(name = "sampleguigexinghao", width = 15)
    @ApiModelProperty(value = "sampleguigexinghao")
    private String sampleguigexinghao;
	/**samplequyangren*/
	@Excel(name = "samplequyangren", width = 15)
    @ApiModelProperty(value = "samplequyangren")
    private String samplequyangren;
	/**samplechuchangbianhao*/
	@Excel(name = "samplechuchangbianhao", width = 15)
    @ApiModelProperty(value = "samplechuchangbianhao")
    private String samplechuchangbianhao;
	/**sampleqiangdudengji*/
	@Excel(name = "sampleqiangdudengji", width = 15)
    @ApiModelProperty(value = "sampleqiangdudengji")
    private String sampleqiangdudengji;
	/**samplehunningtuzhonglei*/
	@Excel(name = "samplehunningtuzhonglei", width = 15)
    @ApiModelProperty(value = "samplehunningtuzhonglei")
    private String samplehunningtuzhonglei;
	/**sampleshajiangzhonglei*/
	@Excel(name = "sampleshajiangzhonglei", width = 15)
    @ApiModelProperty(value = "sampleshajiangzhonglei")
    private String sampleshajiangzhonglei;
	/**sampleshengchanpihao*/
	@Excel(name = "sampleshengchanpihao", width = 15)
    @ApiModelProperty(value = "sampleshengchanpihao")
    private String sampleshengchanpihao;
	/**samplepinzhongdengji*/
	@Excel(name = "samplepinzhongdengji", width = 15)
    @ApiModelProperty(value = "samplepinzhongdengji")
    private String samplepinzhongdengji;
	/**sampleshejijiliang*/
	@Excel(name = "sampleshejijiliang", width = 15)
    @ApiModelProperty(value = "sampleshejijiliang")
    private String sampleshejijiliang;
	/**samplejieheliaozhonglei*/
	@Excel(name = "samplejieheliaozhonglei", width = 15)
    @ApiModelProperty(value = "samplejieheliaozhonglei")
    private String samplejieheliaozhonglei;
	/**sampleliqingbiaohao*/
	@Excel(name = "sampleliqingbiaohao", width = 15)
    @ApiModelProperty(value = "sampleliqingbiaohao")
    private String sampleliqingbiaohao;
	/**sampleliqingzhonglei*/
	@Excel(name = "sampleliqingzhonglei", width = 15)
    @ApiModelProperty(value = "sampleliqingzhonglei")
    private String sampleliqingzhonglei;
	/**samplejipeileixing*/
	@Excel(name = "samplejipeileixing", width = 15)
    @ApiModelProperty(value = "samplejipeileixing")
    private String samplejipeileixing;
	/**sampleshiyancengwei*/
	@Excel(name = "sampleshiyancengwei", width = 15)
    @ApiModelProperty(value = "sampleshiyancengwei")
    private String sampleshiyancengwei;
	/**sampleliqinghunheliaoleixing*/
	@Excel(name = "sampleliqinghunheliaoleixing", width = 15)
    @ApiModelProperty(value = "sampleliqinghunheliaoleixing")
    private String sampleliqinghunheliaoleixing;
	/**samplegangjinzhijing*/
	@Excel(name = "samplegangjinzhijing", width = 15)
    @ApiModelProperty(value = "samplegangjinzhijing")
    private String samplegangjinzhijing;
	/**samplegangjinzhonglei*/
	@Excel(name = "samplegangjinzhonglei", width = 15)
    @ApiModelProperty(value = "samplegangjinzhonglei")
    private String samplegangjinzhonglei;
	/**samplelingqi*/
	@Excel(name = "samplelingqi", width = 15)
    @ApiModelProperty(value = "samplelingqi")
    private String samplelingqi;
	/**samplewaijiajichanliang*/
	@Excel(name = "samplewaijiajichanliang", width = 15)
    @ApiModelProperty(value = "samplewaijiajichanliang")
    private String samplewaijiajichanliang;
	/**samplejiaobanfangshi*/
	@Excel(name = "samplejiaobanfangshi", width = 15)
    @ApiModelProperty(value = "samplejiaobanfangshi")
    private String samplejiaobanfangshi;
	/**jianliyijian*/
	@Excel(name = "jianliyijian", width = 15)
    @ApiModelProperty(value = "jianliyijian")
    private String jianliyijian;
	/**samplenonew*/
	@Excel(name = "samplenonew", width = 15)
    @ApiModelProperty(value = "samplenonew")
    private String samplenonew;
	/**reportnonew*/
	@Excel(name = "reportnonew", width = 15)
    @ApiModelProperty(value = "reportnonew")
    private String reportnonew;
	/**tablenumbernew*/
	@Excel(name = "tablenumbernew", width = 15)
    @ApiModelProperty(value = "tablenumbernew")
    private String tablenumbernew;
	/**wtrwno*/
	@Excel(name = "wtrwno", width = 15)
    @ApiModelProperty(value = "wtrwno")
    private String wtrwno;
	/**wtsgdw*/
	@Excel(name = "wtsgdw", width = 15)
    @ApiModelProperty(value = "wtsgdw")
    private String wtsgdw;
	/**自定义报验单编号*/
	@Excel(name = "自定义报验单编号", width = 15)
    @ApiModelProperty(value = "自定义报验单编号")
    private String reportingsheetnonew;
	/**自定义审批表编号*/
	@Excel(name = "自定义审批表编号", width = 15)
    @ApiModelProperty(value = "自定义审批表编号")
    private String approvaltablenonew;
	/**自定义报验单编号*/
	@Excel(name = "自定义报验单编号", width = 15)
    @ApiModelProperty(value = "自定义报验单编号")
    private String reportingsheetno;
	/**自定义审批表编号*/
	@Excel(name = "自定义审批表编号", width = 15)
    @ApiModelProperty(value = "自定义审批表编号")
    private String approvaltableno;
	/**ypxx*/
	@Excel(name = "ypxx", width = 15)
    @ApiModelProperty(value = "ypxx")
    private String ypxx;
	/**jlyj*/
	@Excel(name = "jlyj", width = 15)
    @ApiModelProperty(value = "jlyj")
    private String jlyj;
	/**总包单位*/
	@Excel(name = "总包单位", width = 15)
    @ApiModelProperty(value = "总包单位")
    private String zonbaodanwei;
	/**sampleshiyanzushu*/
	@Excel(name = "sampleshiyanzushu", width = 15)
    @ApiModelProperty(value = "sampleshiyanzushu")
    private String sampleshiyanzushu;
	/**samplechandi2*/
	@Excel(name = "samplechandi2", width = 15)
    @ApiModelProperty(value = "samplechandi2")
    private String samplechandi2;
	/**samplechandi3*/
	@Excel(name = "samplechandi3", width = 15)
    @ApiModelProperty(value = "samplechandi3")
    private String samplechandi3;
	/**samplequyangdidian2*/
	@Excel(name = "samplequyangdidian2", width = 15)
    @ApiModelProperty(value = "samplequyangdidian2")
    private String samplequyangdidian2;
	/**samplequyangdidian3*/
	@Excel(name = "samplequyangdidian3", width = 15)
    @ApiModelProperty(value = "samplequyangdidian3")
    private String samplequyangdidian3;
	/**sampledate2*/
	@Excel(name = "sampledate2", width = 15)
    @ApiModelProperty(value = "sampledate2")
    private String sampledate2;
	/**sampledate3*/
	@Excel(name = "sampledate3", width = 15)
    @ApiModelProperty(value = "sampledate3")
    private String sampledate3;
	/**samplegangjindengji*/
	@Excel(name = "samplegangjindengji", width = 15)
    @ApiModelProperty(value = "samplegangjindengji")
    private String samplegangjindengji;
	/**sampledaibiaoshuliang2*/
	@Excel(name = "sampledaibiaoshuliang2", width = 15)
    @ApiModelProperty(value = "sampledaibiaoshuliang2")
    private String sampledaibiaoshuliang2;
	/**sampledaibiaoshuliang3*/
	@Excel(name = "sampledaibiaoshuliang3", width = 15)
    @ApiModelProperty(value = "sampledaibiaoshuliang3")
    private String sampledaibiaoshuliang3;
	/**sampleguigexinghao2*/
	@Excel(name = "sampleguigexinghao2", width = 15)
    @ApiModelProperty(value = "sampleguigexinghao2")
    private String sampleguigexinghao2;
	/**sampleguigexinghao3*/
	@Excel(name = "sampleguigexinghao3", width = 15)
    @ApiModelProperty(value = "sampleguigexinghao3")
    private String sampleguigexinghao3;
	/**samplegongchengbihou*/
	@Excel(name = "samplegongchengbihou", width = 15)
    @ApiModelProperty(value = "samplegongchengbihou")
    private String samplegongchengbihou;
	/**原材进场委托单编号*/
	@Excel(name = "原材进场委托单编号", width = 15)
    @ApiModelProperty(value = "原材进场委托单编号")
    private String ycjcwtdbh;
	/**sampleyangpinshuliang*/
	@Excel(name = "sampleyangpinshuliang", width = 15)
    @ApiModelProperty(value = "sampleyangpinshuliang")
    private String sampleyangpinshuliang;
	/**样品数量3*/
	@Excel(name = "样品数量3", width = 15)
    @ApiModelProperty(value = "样品数量3")
    private String sampleyangpinshuliang3;
	/**样品数量2*/
	@Excel(name = "样品数量2", width = 15)
    @ApiModelProperty(value = "样品数量2")
    private String sampleyangpinshuliang2;
	/**samplechangdu*/
	@Excel(name = "samplechangdu", width = 15)
    @ApiModelProperty(value = "samplechangdu")
    private String samplechangdu;
	/**sampledaibiaoshuliang4*/
	@Excel(name = "sampledaibiaoshuliang4", width = 15)
    @ApiModelProperty(value = "sampledaibiaoshuliang4")
    private String sampledaibiaoshuliang4;
	/**sampledaibiaoshuliang5*/
	@Excel(name = "sampledaibiaoshuliang5", width = 15)
    @ApiModelProperty(value = "sampledaibiaoshuliang5")
    private String sampledaibiaoshuliang5;
	/**sampleguigexinghao4*/
	@Excel(name = "sampleguigexinghao4", width = 15)
    @ApiModelProperty(value = "sampleguigexinghao4")
    private String sampleguigexinghao4;
	/**sampleguigexinghao5*/
	@Excel(name = "sampleguigexinghao5", width = 15)
    @ApiModelProperty(value = "sampleguigexinghao5")
    private String sampleguigexinghao5;
	/**sampleyangpinshuliang4*/
	@Excel(name = "sampleyangpinshuliang4", width = 15)
    @ApiModelProperty(value = "sampleyangpinshuliang4")
    private String sampleyangpinshuliang4;
	/**sampleyangpinshuliang5*/
	@Excel(name = "sampleyangpinshuliang5", width = 15)
    @ApiModelProperty(value = "sampleyangpinshuliang5")
    private String sampleyangpinshuliang5;
	/**syjg*/
	@Excel(name = "syjg", width = 15)
    @ApiModelProperty(value = "syjg")
    private String syjg;
	/**buhegeyuanyin*/
	@Excel(name = "buhegeyuanyin", width = 15)
    @ApiModelProperty(value = "buhegeyuanyin")
    private String buhegeyuanyin;
	/**samplezhijianriqi*/
	@Excel(name = "samplezhijianriqi", width = 15)
    @ApiModelProperty(value = "samplezhijianriqi")
    private String samplezhijianriqi;
}
