<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <a-col :span="24">
            <a-form-model-item label="外键，原材进场登记表ID" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="yuancaijinchangdengjiid">
              <a-input-number v-model="model.yuancaijinchangdengjiid" placeholder="请输入外键，原材进场登记表ID" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="外键，原材进场登记表ID" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="yuancaijinchangdengjiid">
              <a-input-number v-model="model.yuancaijinchangdengjiid" placeholder="请输入外键，原材进场登记表ID" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="取样状态（0：未取样  1：已取样）默认0" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="quyangzhuangtai">
              <a-input-number v-model="model.quyangzhuangtai" placeholder="请输入取样状态（0：未取样  1：已取样）默认0" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="取样状态（0：未取样  1：已取样）默认0" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="quyangzhuangtai">
              <a-input-number v-model="model.quyangzhuangtai" placeholder="请输入取样状态（0：未取样  1：已取样）默认0" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="外键，取样人，获取当前登录用户外键T_S_Base_User表的UserName字段" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="quyangren">
              <a-input v-model="model.quyangren" placeholder="请输入外键，取样人，获取当前登录用户外键T_S_Base_User表的UserName字段"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="外键，取样人，获取当前登录用户外键T_S_Base_User表的UserName字段" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="quyangren">
              <a-input v-model="model.quyangren" placeholder="请输入外键，取样人，获取当前登录用户外键T_S_Base_User表的UserName字段"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="取样时间（YYYY-MM-dd HH:mm:ss）" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="quyangshijian">
              <a-input v-model="model.quyangshijian" placeholder="请输入取样时间（YYYY-MM-dd HH:mm:ss）"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="取样时间（YYYY-MM-dd HH:mm:ss）" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="quyangshijian">
              <a-input v-model="model.quyangshijian" placeholder="请输入取样时间（YYYY-MM-dd HH:mm:ss）"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="外键，收样人，获取当前登录用户外键T_S_Base_User表的UserName字段" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="shouyangren">
              <a-input v-model="model.shouyangren" placeholder="请输入外键，收样人，获取当前登录用户外键T_S_Base_User表的UserName字段"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="外键，收样人，获取当前登录用户外键T_S_Base_User表的UserName字段" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="shouyangren">
              <a-input v-model="model.shouyangren" placeholder="请输入外键，收样人，获取当前登录用户外键T_S_Base_User表的UserName字段"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="收样时间（YYYY-MM-dd HH:mm:ss）" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="shouyangshijian">
              <a-input v-model="model.shouyangshijian" placeholder="请输入收样时间（YYYY-MM-dd HH:mm:ss）"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="收样时间（YYYY-MM-dd HH:mm:ss）" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="shouyangshijian">
              <a-input v-model="model.shouyangshijian" placeholder="请输入收样时间（YYYY-MM-dd HH:mm:ss）"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="收样状态（0：未收样  1：已收样 2：已提交 3：在检）默认0" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="shouyangzhuangtai">
              <a-input-number v-model="model.shouyangzhuangtai" placeholder="请输入收样状态（0：未收样  1：已收样 2：已提交 3：在检）默认0" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="收样状态（0：未收样  1：已收样 2：已提交 3：在检）默认0" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="shouyangzhuangtai">
              <a-input-number v-model="model.shouyangzhuangtai" placeholder="请输入收样状态（0：未收样  1：已收样 2：已提交 3：在检）默认0" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="外键，见证人，获取当前登录用户外键T_S_Base_User表的UserName字段" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="jianzhengren">
              <a-input v-model="model.jianzhengren" placeholder="请输入外键，见证人，获取当前登录用户外键T_S_Base_User表的UserName字段"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="外键，见证人，获取当前登录用户外键T_S_Base_User表的UserName字段" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="jianzhengren">
              <a-input v-model="model.jianzhengren" placeholder="请输入外键，见证人，获取当前登录用户外键T_S_Base_User表的UserName字段"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="见证时间（YYYY-MM-dd HH:mm:ss）" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="jianzhengshijian">
              <a-input v-model="model.jianzhengshijian" placeholder="请输入见证时间（YYYY-MM-dd HH:mm:ss）"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="见证时间（YYYY-MM-dd HH:mm:ss）" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="jianzhengshijian">
              <a-input v-model="model.jianzhengshijian" placeholder="请输入见证时间（YYYY-MM-dd HH:mm:ss）"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="见证状态（0：未见证  1：已见证）默认0" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="jianzhengzhuangtai">
              <a-input-number v-model="model.jianzhengzhuangtai" placeholder="请输入见证状态（0：未见证  1：已见证）默认0" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="见证状态（0：未见证  1：已见证）默认0" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="jianzhengzhuangtai">
              <a-input-number v-model="model.jianzhengzhuangtai" placeholder="请输入见证状态（0：未见证  1：已见证）默认0" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="委托单编号，唯一" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="weituodanbianhao">
              <a-input v-model="model.weituodanbianhao" placeholder="请输入委托单编号，唯一"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="委托单编号，唯一" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="weituodanbianhao">
              <a-input v-model="model.weituodanbianhao" placeholder="请输入委托单编号，唯一"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="样品处理方式" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="yangpinchulifangshi">
              <a-textarea v-model="model.yangpinchulifangshi" rows="4" placeholder="请输入样品处理方式" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="样品处理方式" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="yangpinchulifangshi">
              <a-textarea v-model="model.yangpinchulifangshi" rows="4" placeholder="请输入样品处理方式" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="取样地点" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="quyangdidian">
              <a-textarea v-model="model.quyangdidian" rows="4" placeholder="请输入取样地点" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="取样地点" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="quyangdidian">
              <a-textarea v-model="model.quyangdidian" rows="4" placeholder="请输入取样地点" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="报告份数" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="baogaofenshu">
              <a-input-number v-model="model.baogaofenshu" placeholder="请输入报告份数" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="报告份数" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="baogaofenshu">
              <a-input-number v-model="model.baogaofenshu" placeholder="请输入报告份数" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="检测项目" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="jiancexiangmu">
              <a-textarea v-model="model.jiancexiangmu" rows="4" placeholder="请输入检测项目" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="检测项目" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="jiancexiangmu">
              <a-textarea v-model="model.jiancexiangmu" rows="4" placeholder="请输入检测项目" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="委托人" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="weituoren">
              <a-input v-model="model.weituoren" placeholder="请输入委托人"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="委托人" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="weituoren">
              <a-input v-model="model.weituoren" placeholder="请输入委托人"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="委托日期" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="weituoriqi">
              <a-input v-model="model.weituoriqi" placeholder="请输入委托日期"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="委托日期" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="weituoriqi">
              <a-input v-model="model.weituoriqi" placeholder="请输入委托日期"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="施工单位" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="shigongdanwei">
              <a-textarea v-model="model.shigongdanwei" rows="4" placeholder="请输入施工单位" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="施工单位" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="shigongdanwei">
              <a-textarea v-model="model.shigongdanwei" rows="4" placeholder="请输入施工单位" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="监理单位" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="jianglidanwei">
              <a-textarea v-model="model.jianglidanwei" rows="4" placeholder="请输入监理单位" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="监理单位" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="jianglidanwei">
              <a-textarea v-model="model.jianglidanwei" rows="4" placeholder="请输入监理单位" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="施工单位合同号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="shigongdanweihetonghao">
              <a-textarea v-model="model.shigongdanweihetonghao" rows="4" placeholder="请输入施工单位合同号" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="施工单位合同号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="shigongdanweihetonghao">
              <a-textarea v-model="model.shigongdanweihetonghao" rows="4" placeholder="请输入施工单位合同号" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="监理单位合同号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="jianlidanweihetonghao">
              <a-textarea v-model="model.jianlidanweihetonghao" rows="4" placeholder="请输入监理单位合同号" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="监理单位合同号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="jianlidanweihetonghao">
              <a-textarea v-model="model.jianlidanweihetonghao" rows="4" placeholder="请输入监理单位合同号" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="外键，监理单位组织机构id" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="jianlidanweidepartid">
              <a-input v-model="model.jianlidanweidepartid" placeholder="请输入外键，监理单位组织机构id"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="外键，监理单位组织机构id" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="jianlidanweidepartid">
              <a-input v-model="model.jianlidanweidepartid" placeholder="请输入外键，监理单位组织机构id"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="外键，监理单位组织机构编码" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="jianlidanweiorgcode">
              <a-input v-model="model.jianlidanweiorgcode" placeholder="请输入外键，监理单位组织机构编码"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="外键，监理单位组织机构编码" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="jianlidanweiorgcode">
              <a-input v-model="model.jianlidanweiorgcode" placeholder="请输入外键，监理单位组织机构编码"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="外键，任务执行人（由收样人分配），获取当前登录用户外键T_S_Base_User表的UserName字段" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="renwuzhixingren">
              <a-input v-model="model.renwuzhixingren" placeholder="请输入外键，任务执行人（由收样人分配），获取当前登录用户外键T_S_Base_User表的UserName字段"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="外键，任务执行人（由收样人分配），获取当前登录用户外键T_S_Base_User表的UserName字段" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="renwuzhixingren">
              <a-input v-model="model.renwuzhixingren" placeholder="请输入外键，任务执行人（由收样人分配），获取当前登录用户外键T_S_Base_User表的UserName字段"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="执行状态（0：未执行 1：执行中 2：已完成）" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="zhixingzhuangtai">
              <a-input-number v-model="model.zhixingzhuangtai" placeholder="请输入执行状态（0：未执行 1：执行中 2：已完成）" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="执行状态（0：未执行 1：执行中 2：已完成）" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="zhixingzhuangtai">
              <a-input-number v-model="model.zhixingzhuangtai" placeholder="请输入执行状态（0：未执行 1：执行中 2：已完成）" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="外键，样品编号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="sampleno">
              <a-textarea v-model="model.sampleno" rows="4" placeholder="请输入外键，样品编号" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="外键，样品编号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="sampleno">
              <a-textarea v-model="model.sampleno" rows="4" placeholder="请输入外键，样品编号" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="二维码编号(处理后字段，id+序号)" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="erweimabianhao">
              <a-input v-model="model.erweimabianhao" placeholder="请输入二维码编号(处理后字段，id+序号)"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="二维码编号(处理后字段，id+序号)" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="erweimabianhao">
              <a-input v-model="model.erweimabianhao" placeholder="请输入二维码编号(处理后字段，id+序号)"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="二维码UUID" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="erweimaweiyima">
              <a-input v-model="model.erweimaweiyima" placeholder="请输入二维码UUID"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="二维码UUID" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="erweimaweiyima">
              <a-input v-model="model.erweimaweiyima" placeholder="请输入二维码UUID"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="委托状态（0：未提交 1：已提交）默认0，已提交时需更新原材登记表的委托状态为已委托" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="weituozhuangtai">
              <a-input-number v-model="model.weituozhuangtai" placeholder="请输入委托状态（0：未提交 1：已提交）默认0，已提交时需更新原材登记表的委托状态为已委托" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="委托状态（0：未提交 1：已提交）默认0，已提交时需更新原材登记表的委托状态为已委托" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="weituozhuangtai">
              <a-input-number v-model="model.weituozhuangtai" placeholder="请输入委托状态（0：未提交 1：已提交）默认0，已提交时需更新原材登记表的委托状态为已委托" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="指派的取样人名称，获取当前登录用户外键T_S_Base_User表的UserName字段" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="zhipaiquyangren">
              <a-input v-model="model.zhipaiquyangren" placeholder="请输入指派的取样人名称，获取当前登录用户外键T_S_Base_User表的UserName字段"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="指派的取样人名称，获取当前登录用户外键T_S_Base_User表的UserName字段" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="zhipaiquyangren">
              <a-input v-model="model.zhipaiquyangren" placeholder="请输入指派的取样人名称，获取当前登录用户外键T_S_Base_User表的UserName字段"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="指派时间（YYYY-MM-dd HH:mm:ss）" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="zhipaishijian">
              <a-input v-model="model.zhipaishijian" placeholder="请输入指派时间（YYYY-MM-dd HH:mm:ss）"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="指派时间（YYYY-MM-dd HH:mm:ss）" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="zhipaishijian">
              <a-input v-model="model.zhipaishijian" placeholder="请输入指派时间（YYYY-MM-dd HH:mm:ss）"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="指派人，获取当前登录用户外键T_S_Base_User表的UserName字段" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="zhipairen">
              <a-input v-model="model.zhipairen" placeholder="请输入指派人，获取当前登录用户外键T_S_Base_User表的UserName字段"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="指派人，获取当前登录用户外键T_S_Base_User表的UserName字段" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="zhipairen">
              <a-input v-model="model.zhipairen" placeholder="请输入指派人，获取当前登录用户外键T_S_Base_User表的UserName字段"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="指派状态（0：未指派1：已指派）" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="zhipaizhuangtai">
              <a-input-number v-model="model.zhipaizhuangtai" placeholder="请输入指派状态（0：未指派1：已指派）" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="指派状态（0：未指派1：已指派）" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="zhipaizhuangtai">
              <a-input-number v-model="model.zhipaizhuangtai" placeholder="请输入指派状态（0：未指派1：已指派）" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="指派的取样人id，获取当前登录用户外键T_S_Base_User表的id字段" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="zhipaiquyangrenid">
              <a-input v-model="model.zhipaiquyangrenid" placeholder="请输入指派的取样人id，获取当前登录用户外键T_S_Base_User表的id字段"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="指派的取样人id，获取当前登录用户外键T_S_Base_User表的id字段" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="zhipaiquyangrenid">
              <a-input v-model="model.zhipaiquyangrenid" placeholder="请输入指派的取样人id，获取当前登录用户外键T_S_Base_User表的id字段"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="beizhu" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="beizhu">
              <a-input v-model="model.beizhu" placeholder="请输入beizhu"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="beizhu" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="beizhu">
              <a-input v-model="model.beizhu" placeholder="请输入beizhu"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="fujian" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="fujian">
              <a-input v-model="model.fujian" placeholder="请输入fujian"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="fujian" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="fujian">
              <a-input v-model="model.fujian" placeholder="请输入fujian"  ></a-input>
            </a-form-model-item>
          </a-col>
        </a-row>
      </a-form-model>
    </j-form-container>
  </a-spin>
</template>

<script>

  import { httpAction, getAction } from '@/api/manage'
  import { validateDuplicateValue } from '@/utils/util'

  export default {
    name: 'SyDpsYyYuancaiquyangweituoFormF',
    components: {
    },
    props: {
      //表单禁用
      disabled: {
        type: Boolean,
        default: false,
        required: false
      }
    },
    data () {
      return {
        model:{
         },
        labelCol: {
          xs: { span: 24 },
          sm: { span: 5 },
        },
        wrapperCol: {
          xs: { span: 24 },
          sm: { span: 16 },
        },
        confirmLoading: false,
        validatorRules: {
        },
        url: {
          add: "/sywtgl/syDpsYyYuancaiquyangweituo/add",
          edit: "/sywtgl/syDpsYyYuancaiquyangweituo/edit",
          queryById: "/sywtgl/syDpsYyYuancaiquyangweituo/queryById"
        }
      }
    },
    computed: {
      formDisabled(){
        return this.disabled
      },
    },
    created () {
       //备份model原始值
      this.modelDefault = JSON.parse(JSON.stringify(this.model));
    },
    methods: {
      add () {
        this.edit(this.modelDefault);
      },
      edit (record) {
        this.model = Object.assign({}, record);
        this.visible = true;
      },
      submitForm () {
        const that = this;
        // 触发表单验证
        this.$refs.form.validate(valid => {
          if (valid) {
            that.confirmLoading = true;
            let httpurl = '';
            let method = '';
            if(!this.model.id){
              httpurl+=this.url.add;
              method = 'post';
            }else{
              httpurl+=this.url.edit;
               method = 'put';
            }
            httpAction(httpurl,this.model,method).then((res)=>{
              if(res.success){
                that.$message.success(res.message);
                that.$emit('ok');
              }else{
                that.$message.warning(res.message);
              }
            }).finally(() => {
              that.confirmLoading = false;
            })
          }
         
        })
      },
    }
  }
</script>