<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <a-col :span="24">
            <a-form-model-item label="样品编号(根据基础配置的编号规则生成)" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="sampleno">
              <a-input v-model="model.sampleno" placeholder="请输入样品编号(根据基础配置的编号规则生成)"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="样品名称" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="samplename">
              <a-input v-model="model.samplename" placeholder="请输入样品名称"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="样品描述" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="sampledescribe">
              <a-input v-model="model.sampledescribe" placeholder="请输入样品描述"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="工程部位/用途" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="samplegcbw">
              <a-input v-model="model.samplegcbw" placeholder="请输入工程部位/用途"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="取样日期（yyyy-MM-dd）" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="sampledate">
              <a-input v-model="model.sampledate" placeholder="请输入取样日期（yyyy-MM-dd）"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="取样地点/取样位置" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="samplequyangdidian">
              <a-input v-model="model.samplequyangdidian" placeholder="请输入取样地点/取样位置"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="样品状态（0：待检1：在检2：已检）" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="samplestate">
              <a-input-number v-model="model.samplestate" placeholder="请输入样品状态（0：待检1：在检2：已检）" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="真实名字" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="realname">
              <a-input v-model="model.realname" placeholder="请输入真实名字"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="取样人" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="samplequyangren">
              <a-input v-model="model.samplequyangren" placeholder="请输入取样人"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="创建人（系统登录用户）" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="samplecreateperson">
              <a-input v-model="model.samplecreateperson" placeholder="请输入创建人（系统登录用户）"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="创建日期（yyyy-MM-dd）" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="samplecreatedate">
              <a-input v-model="model.samplecreatedate" placeholder="请输入创建日期（yyyy-MM-dd）"  ></a-input>
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
            <a-form-model-item label="强度等级，使用数据字典（水泥样品用到）" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="sampleqiangdudengji">
              <a-input v-model="model.sampleqiangdudengji" placeholder="请输入强度等级，使用数据字典（水泥样品用到）"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="龄期 " :labelCol="labelCol" :wrapperCol="wrapperCol" prop="samplelingqi">
              <a-input v-model="model.samplelingqi" placeholder="请输入龄期 "  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="报告日期（来源编辑报告时填写的时间）" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="reportcreatedate">
              <a-input v-model="model.reportcreatedate" placeholder="请输入报告日期（来源编辑报告时填写的时间）"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="报告编号(根据基础设置的规则生成)" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="reportno">
              <a-input v-model="model.reportno" placeholder="请输入报告编号(根据基础设置的规则生成)"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="报告编辑人" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="reporteditperson">
              <a-input v-model="model.reporteditperson" placeholder="请输入报告编辑人"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="真实名字" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="realname2">
              <a-input v-model="model.realname2" placeholder="请输入真实名字"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="试验项目名称" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="titname">
              <a-input v-model="model.titname" placeholder="请输入试验项目名称"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="外键（dps_jc_testItemType）试验项目类型" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="tittype">
              <a-input v-model="model.tittype" placeholder="请输入外键（dps_jc_testItemType）试验项目类型"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="testname" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="testname">
              <a-input v-model="model.testname" placeholder="请输入testname"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="见证取样 0：普通 1：见证取样 默认给0" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="plusmark">
              <a-input-number v-model="model.plusmark" placeholder="请输入见证取样 0：普通 1：见证取样 默认给0" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="样品编号(根据基础配置的编号规则生成)" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="samplenonew">
              <a-input v-model="model.samplenonew" placeholder="请输入样品编号(根据基础配置的编号规则生成)"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="自定义报告编号（雷榕项目）" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="reportnonew">
              <a-textarea v-model="model.reportnonew" rows="4" placeholder="请输入自定义报告编号（雷榕项目）" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="自定义记录编号（雷榕项目）" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="tablenumbernew">
              <a-textarea v-model="model.tablenumbernew" rows="4" placeholder="请输入自定义记录编号（雷榕项目）" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="审批状态 0：未提交 1：未审核 2：审核通过  3：审核退回" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="shenpizhuangtai">
              <a-input-number v-model="model.shenpizhuangtai" placeholder="请输入审批状态 0：未提交 1：未审核 2：审核通过  3：审核退回" style="width: 100%" />
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
    name: 'SyDpsSySampleView2Form',
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
          add: "/sylxdps/syDpsSySampleView2/add",
          edit: "/sylxdps/syDpsSySampleView2/edit",
          queryById: "/sylxdps/syDpsSySampleView2/queryById"
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