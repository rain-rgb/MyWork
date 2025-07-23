package com.trtm.sy.registertable.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.elasticsearch.common.xcontent.ToXContent;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.format.annotation.DateTimeFormat;


import java.io.IOException;
import java.util.Date;
import java.util.List;

@Data
@Document(indexName = "sy_ywt", shards = 1, replicas = 1)
public class SyYwt implements ToXContent {
    @Id
    private String id;
    /**
     * 委托单位
     */
    @Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_smart")
    private String wtdw;
    /**
     * 预委托编号
     */
    @Field(type = FieldType.Keyword)
    private String ywtbh;
    /**
     * 来样方式
     */
    @Field(type = FieldType.Text)
    private String lyfs;
    /**
     * 检测地点
     */
    @Field(type = FieldType.Text)
    private String jcdd;
    /**
     * 工程名称
     */
    @Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_smart")
    private String gcmc;
    /**
     * 见证人
     */
    @Field(type = FieldType.Text, index = false)
    private String jzr;
    /**
     * 见证人电话
     */
    @Field(type = FieldType.Text, index = false)
    private String jzrdh;
    /**
     * 见证单位
     */
    @Field(type = FieldType.Text)
    private String jzdw;
    /**
     * 监督机构
     */
    @Field(type = FieldType.Text)
    private String jdjg;
    /**
     * 监督机构联系人姓名
     */
    @Field(type = FieldType.Text, index = false)
    private String lxrxm;
    /**
     * 监督机构联系人电话
     */
    @Field(type = FieldType.Text, index = false)
    private String lxrdh;
    /**
     * 送样人姓名
     */
    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String syrxm;
    /**
     * 送样人电话
     */
    @Field(type = FieldType.Text, index = false)
    private String syrdh;
    /**
     * 工程部位
     */
    @Field(type = FieldType.Text)
    private String gcbw;
    /**
     * 自定义样品名称
     */
    @Field(type = FieldType.Text)
    private String customYpmc;
    /**
     * 生产单位
     */
    @Field(type = FieldType.Text)
    private String scdw;
    /**
     * 送样日期
     */
    @Field(type = FieldType.Text, index = false)
    private String songyrq;
    /**
     * 收样日期
     */
    @Field(type = FieldType.Text, index = false)
    private String shouyrq;
    /**
     * 生产日期
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Field(type = FieldType.Date, format = DateFormat.date_time)
    private String scrq;
    /**
     * 出场批号
     */
    @Field(type = FieldType.Text, index = false)
    private String ccph;
    /**
     * 样品规格
     */
    @Field(type = FieldType.Text, index = false)
    private String ypgg;
    /**
     * 委托单类型
     */
    @Field(type = FieldType.Text, index = false)
    private String wtdlx;
    /**
     * 施工单位
     */
    @Field(type = FieldType.Text, index = false)
    private String sgdw;
    /**
     * 委托日期
     */
    @Field(type = FieldType.Text, index = false)
    private String wtrq;
    /**
     * 证书编号
     */
    @Field(type = FieldType.Text, index = false)
    private String zsbh;
    /**
     * 委托备注
     */
    @Field(type = FieldType.Text, index = false)
    private String wtbz;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @Field(type = FieldType.Date, format = DateFormat.date_time)
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private Date createTime;

    @Field(type = FieldType.Text, index = false)
    private String createUser;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @Field(type = FieldType.Date, format = DateFormat.date_time)
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private Date updateTime;

    @Field(type = FieldType.Text, index = false)
    private String updateUser;
    /**
     * 提交类型
     */
    @Field(type = FieldType.Text)
    private Integer type;

//    @Field(type = FieldType.Object, index = false)
//    private List<SyYpYwt> syYps;
    /**
     * 状态(0：未导入；1：已导入；2：委托单删除）
     */
    @Field(type = FieldType.Text, index = false)
    private Integer status;

    @Field(type = FieldType.Keyword)
    private String untouched_customYpmc;

    @TableField(exist = false)
    private String isSequence;
    @TableField(exist = false)
    private String fieldName;

    @Override
    public XContentBuilder toXContent(XContentBuilder builder, Params params) throws IOException {
        return null;
    }


//    @Override
//    public XContentBuilder toXContent(XContentBuilder builder, Params params) throws IOException {
//        builder.startObject();
//        builder.field("id", this.id);
//        builder.field("wtdw", this.wtdw);
//        builder.field("ywtbh", this.ywtbh);
//        builder.field("lyfs", this.lyfs);
//        builder.field("jcdd", this.jcdd);
//        builder.field("syrxm", this.syrxm);
//        builder.field("syrdh", this.syrdh);
//        builder.field("gcmc", this.gcmc);
//        builder.field("jzr", this.jzr);
//        builder.field("jzrdh", this.jzrdh);
//        builder.field("jzdw", this.jzdw);
//        builder.field("zsbh", this.zsbh);
//        builder.field("jdjg", this.jdjg);
//        builder.field("lxrxm", this.lxrxm);
//        builder.field("lxrdh", this.lxrdh);
//        builder.field("sgdw", this.sgdw);
//        builder.field("gcbw", this.gcbw);
//        builder.field("wtdlx", this.wtdlx);
//        builder.field("wtbz", this.wtbz);
//        builder.field("status", this.status);
//        builder.field("createTime", this.createTime);
//        builder.field("createUser", this.createUser);

//        // 如果syYps列表不为空，则序列化它
//        if (this.syYps != null && !this.syYps.isEmpty()) {
//            builder.startArray("syYps"); // 开始数组
//            for (SyYpYwt syYp : this.syYps) {
//                builder.startObject();
//                builder.field("ypfl", syYp.getYpfl());
//                builder.field("gcbw", syYp.getGcbw());
//                builder.field("ypmc", syYp.getYpmc());
//                builder.field("customYpmc", syYp.getCustomYpmc());
//                builder.field("customPdyj", syYp.getCustomPdyj());
//                builder.field("customJccs", syYp.getCustomJccs());
//                builder.field("customSyyj", syYp.getCustomSyyj());
//                builder.field("pdyj", syYp.getPdyj());
//                builder.field("shouyrq", syYp.getShouyrq());
//                builder.field("ypczfs", syYp.getYpczfs());
//                builder.field("jcldjzh", syYp.getJcldjzh());
//                builder.field("ccph", syYp.getCcph());
//                builder.field("dbsl", syYp.getDbsl());
//                builder.field("scdw", syYp.getScdw());
//                builder.field("scrq",  syYp.getScrq());
//                builder.field("songyrq",  syYp.getSongyrq());
//                builder.field("ypLevel", syYp.getYpLevel());
//                builder.field("ypbz", syYp.getYpbz());
//                builder.field("ypgg", syYp.getYpgg());
//                builder.field("ypsl", syYp.getYpsl());
//                builder.field("wtbz", syYp.getWtbz());
//                if (syYp.getYpjccssList() != null && !syYp.getYpjccssList().isEmpty()) {
//                    builder.startArray("ypjccssList"); // 开始数组
//                    for (SyYpjccss syYpjccss : syYp.getYpjccssList()) {
//                        builder.startObject();
//                        builder.field("fdmids", syYpjccss.getFdmids());
//                        builder.field("gcmcs", syYpjccss.getGcmcs());
//                        builder.field("jccsid", syYpjccss.getJccsid());
//                        builder.field("jccsmc", syYpjccss.getJccsmc());
//                        builder.endObject();
//                    }
//                    builder.endArray();
//                }
//                builder.endObject();
//            }
//            builder.endArray(); // 结束数组
//        }
//        builder.endObject(); // 结束JSON对象
//        return builder; // 返回构建器对象以便链式调用
//    }
}
