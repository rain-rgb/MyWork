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
            <a-form-model-item label="流程名称" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="liuchengmingcheng">
              <a-input v-model="model.liuchengmingcheng" placeholder="请输入流程名称"  ></a-input>
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
            <a-form-model-item label="记录表签章状态   默认-1：未启动签章" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="jilubiaoqianzhangzhuangtai">
              <a-input v-model="model.jilubiaoqianzhangzhuangtai" placeholder="请输入记录表签章状态   默认-1：未启动签章"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="报告签章状态  默认-1：未启动签章" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="baogaoqianzhangzhuangtai">
              <a-input v-model="model.baogaoqianzhangzhuangtai" placeholder="请输入报告签章状态  默认-1：未启动签章"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="报验单签章状态  默认-1：未启动签章 0:无报验单        1/3" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="baoyandanqianzhangzhuangtai">
              <a-input v-model="model.baoyandanqianzhangzhuangtai" placeholder="请输入报验单签章状态  默认-1：未启动签章 0:无报验单        1/3"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="审批表签章状态  默认-1：未启动签章 0：无审批表" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="shenpibiaoqianzhangzhuangtai">
              <a-input v-model="model.shenpibiaoqianzhangzhuangtai" placeholder="请输入审批表签章状态  默认-1：未启动签章 0：无审批表"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="外键，用户id，来源t_s_base_user表id字段" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="userid">
              <a-input v-model="model.userid" placeholder="请输入外键，用户id，来源t_s_base_user表id字段"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="自定义样品编号（雷榕项目）" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="sampleno">
              <a-textarea v-model="model.sampleno" rows="4" placeholder="请输入自定义样品编号（雷榕项目）" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="自定义报告编号（雷榕项目）" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="reportno">
              <a-textarea v-model="model.reportno" rows="4" placeholder="请输入自定义报告编号（雷榕项目）" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="样品名称" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="samplename">
              <a-input v-model="model.samplename" placeholder="请输入样品名称"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="工程部位/用途" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="samplegcbw">
              <a-input v-model="model.samplegcbw" placeholder="请输入工程部位/用途"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="外键，组织机构编码（T_S_depart）" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="orgcode">
              <a-input v-model="model.orgcode" placeholder="请输入外键，组织机构编码（T_S_depart）"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="外键，试验项目类型（dps_jc_testItemType表）" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="titcode">
              <a-input v-model="model.titcode" placeholder="请输入外键，试验项目类型（dps_jc_testItemType表）"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="审批时间,系统当前时间（YYYY-MM-dd HH:mm:ss）" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="shenpishijian">
              <a-input v-model="model.shenpishijian" placeholder="请输入审批时间,系统当前时间（YYYY-MM-dd HH:mm:ss）"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="外键，签章流程id，dps_jc_qianzhangliucheng" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="qianzhangliuchengid">
              <a-input v-model="model.qianzhangliuchengid" placeholder="请输入外键，签章流程id，dps_jc_qianzhangliucheng"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="取样人" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="samplequyangren">
              <a-input v-model="model.samplequyangren" placeholder="请输入取样人"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="签章关键字，来源数据字典 shtoone_qzgjz" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="qianzhangguanjianzi">
              <a-input v-model="model.qianzhangguanjianzi" placeholder="请输入签章关键字，来源数据字典 shtoone_qzgjz"  ></a-input>
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
    name: 'SyDpsSySignatureForm',
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
          add: "/syDpsSySignature/syDpsSySignature/add",
          edit: "/syDpsSySignature/syDpsSySignature/edit",
          queryById: "/syDpsSySignature/syDpsSySignature/queryById"
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