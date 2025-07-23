<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model :model="model" :rules="validatorRules" ref="form" slot="detail">
        <a-row>
          <a-col :span="24">
            <a-form-model-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="工作区id 唯一" prop="blockid">
              <a-input placeholder="请输入工作区id 唯一" v-model="model.blockid"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="工作区名称" prop="blockname">
              <a-input placeholder="请输入工作区名称" v-model="model.blockname"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="所属层" prop="layername">
              <a-input placeholder="请输入所属层" v-model="model.layername"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="开始桩号 面型工程没有桩号" prop="startstation">
              <a-input placeholder="请输入开始桩号 面型工程没有桩号" v-model="model.startstation"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="结束桩号 面型工程没有桩号" prop="endstation">
              <a-input placeholder="请输入结束桩号 面型工程没有桩号" v-model="model.endstation"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="开始时间" prop="starttime">
              <j-date placeholder="请选择开始时间" style="width: 100%"  v-model="model.starttime" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="结束时间" prop="endtime">
              <j-date placeholder="请选择结束时间" style="width: 100%"  v-model="model.endtime" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="工作时长（h）" prop="worktime">
              <a-input placeholder="请输入工作时长（h）" v-model="model.worktime"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="施工长度（m） 面型工程没有长度" prop="workmile">
              <a-input placeholder="请输入施工长度（m） 面型工程没有长度" v-model="model.workmile"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="
施工面积（㎡）" prop="workarea">
              <a-input placeholder="请输入
施工面积（㎡）" v-model="model.workarea"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="工作区边界坐标，格式：[[北,东]]" prop="border">
              <a-input placeholder="请输入工作区边界坐标，格式：[[北,东]]" v-model="model.border"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="开振占比（%）" prop="vibrateratio">
              <a-input placeholder="请输入开振占比（%）" v-model="model.vibrateratio"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="平均碾压遍数" prop="timesavg">
              <a-input placeholder="请输入平均碾压遍数" v-model="model.timesavg"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="
平均振动遍数" prop="timesvibrateavg">
              <a-input placeholder="请输入
平均振动遍数" v-model="model.timesvibrateavg"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="平均碾压速度（km/h）" prop="speedavg">
              <a-input placeholder="请输入平均碾压速度（km/h）" v-model="model.speedavg"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="平均厚度（m）" prop="thickavg">
              <a-input placeholder="请输入平均厚度（m）" v-model="model.thickavg"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="告警条数" prop="alarmnum">
              <a-input placeholder="请输入告警条数" v-model="model.alarmnum"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="施工质量，0正常，1异常，2异常已处理" prop="qualitystat">
              <a-input placeholder="请输入施工质量，0正常，1异常，2异常已处理" v-model="model.qualitystat"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="施工状态，0施工中，1已完工" prop="workstat">
              <a-input placeholder="请输入施工状态，0施工中，1已完工" v-model="model.workstat"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="是否报检，0未报检，1已报检" prop="inspectstat">
              <a-input placeholder="请输入是否报检，0未报检，1已报检" v-model="model.inspectstat"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="projectid" prop="projectid">
              <a-input placeholder="请输入projectid" v-model="model.projectid"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="sectionid" prop="sectionid">
              <a-input placeholder="请输入sectionid" v-model="model.sectionid"  ></a-input>
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
    name: 'HcTfysworkareaForm',
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
          add: "/hc_tfysworkarea/hcTfysworkarea/add",
          edit: "/hc_tfysworkarea/hcTfysworkarea/edit",
          queryById: "/hc_tfysworkarea/hcTfysworkarea/queryById"
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
