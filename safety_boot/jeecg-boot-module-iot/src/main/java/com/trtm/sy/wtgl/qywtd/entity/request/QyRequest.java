package com.trtm.sy.wtgl.qywtd.entity.request;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(value="取样entity", description="取样entity")
public class QyRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer quyangdanid; //取样单id
    private String chengxingriqi; //成型日期
    private String quyangshijian; //取样日期
    private String yangpingshuliang; //样品数量
    private String yangpinmiaoshu; //样品描述
    private String imgs; //取样照片路径
    private String erweimaweiyima; //二维码唯一码
    private String erweimabianhao; //二维码编号
//    private Integer wtdid; //委托单id
    private Integer wtid; //委托单id

}
