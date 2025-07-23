package com.trtm.sy.registertable.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Document(indexName = "qualitydata", shards = 1, replicas = 0)
public class SyForm implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field(type = FieldType.Keyword)
    private String sampleId;

    @Field(type = FieldType.Keyword)
    private String type;

    @Field(type = FieldType.Text)
    private String sbbh;

    @Field(type = FieldType.Keyword)
    private String reportNumber;

    private String 标题;


    private String 施工单位;

    @Field(type = FieldType.Keyword)
    private String 合同号;

    @Field(type = FieldType.Keyword)
    private String 记录编号;

    private String 监理单位;

    private String 编号;

    private String 批准人;

    private String 记录人;

    private String 复核人;

    @Field(type = FieldType.Date)
    private String 日期;

    @Field(type = FieldType.Keyword)
    private String bgbh;

    private List<List<BaseForm>> baseFormLists;

}
