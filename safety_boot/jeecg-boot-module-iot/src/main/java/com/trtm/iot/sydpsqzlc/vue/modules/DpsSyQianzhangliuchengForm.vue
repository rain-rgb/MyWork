<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <a-col :span="24">
            <a-form-model-item label="外键，样品编号，sy_dps_sy_sample" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="sampleid">
              <a-input v-model="model.sampleid" placeholder="请输入外键，样品编号，sy_dps_sy_sample"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="外键，签章流程id，dps_jc_qianzhangliucheng" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="qianzhangliuchengid">
              <a-input v-model="model.qianzhangliuchengid" placeholder="请输入外键，签章流程id，dps_jc_qianzhangliucheng"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="外键，签章id，dps_jc_CAUsbKey" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="caid">
              <a-input v-model="model.caid" placeholder="请输入外键，签章id，dps_jc_CAUsbKey"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="签章人，当前登录用户名" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="qianzhangren">
              <a-input v-model="model.qianzhangren" placeholder="请输入签章人，当前登录用户名"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="签章时间" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="qianzhangshijian">
              <a-input v-model="model.qianzhangshijian" placeholder="请输入签章时间"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="签章状态 0：未签章 1：已签章 2：已退回" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="qianzhangzhuangtai">
              <a-input-number v-model="model.qianzhangzhuangtai" placeholder="请输入签章状态 0：未签章 1：已签章 2：已退回" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="签章类型 1：施工单位 2：监理单位" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="qianzhangleixing">
              <a-input-number v-model="model.qianzhangleixing" placeholder="请输入签章类型 1：施工单位 2：监理单位" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="流程类型 1：记录表 2：报告表 3:报验单 4：审批表 5：评定表" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="liuchengleixing">
              <a-input-number v-model="model.liuchengleixing" placeholder="请输入流程类型 1：记录表 2：报告表 3:报验单 4：审批表 5：评定表" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="流程状态序号（值等于 流程类型值+填写值）" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="liuchengzhuangtaixuhao">
              <a-input-number v-model="model.liuchengzhuangtaixuhao" placeholder="请输入流程状态序号（值等于 流程类型值+填写值）" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="流程名称" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="liuchengmingcheng">
              <a-input v-model="model.liuchengmingcheng" placeholder="请输入流程名称"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="签章关键字，来源数据字典 shtoone_qzgjz" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="qianzhangguanjianzi">
              <a-input v-model="model.qianzhangguanjianzi" placeholder="请输入签章关键字，来源数据字典 shtoone_qzgjz"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="当前流程需要签章页面数量" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="qianzhangyemianshuliang">
              <a-input-number v-model="model.qianzhangyemianshuliang" placeholder="请输入当前流程需要签章页面数量" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="evaluateid" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="evaluateid">
              <a-input v-model="model.evaluateid" placeholder="请输入evaluateid"  ></a-input>
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
    name: 'DpsSyQianzhangliuchengForm',
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
          add: "/sydpsqzlc/dpsSyQianzhangliucheng/add",
          edit: "/sydpsqzlc/dpsSyQianzhangliucheng/edit",
          queryById: "/sydpsqzlc/dpsSyQianzhangliucheng/queryById"
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