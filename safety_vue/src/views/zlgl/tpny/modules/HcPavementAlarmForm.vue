<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <a-col :span="24">
            <a-form-model-item label="告警类型" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="alarmtypeid">
              <a-input-number v-model="model.alarmtypeid" placeholder="请输入告警类型，1碾压超速，2碾压温度过低，3摊铺超速，4摊铺温度过低，5碾压距摊铺机过远" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="机械类型id" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="machinetypeid">
              <a-input v-model="model.machinetypeid" placeholder="请输入机械类型id"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="告警级别，0为无级别，1为轻微，2为一般，3为严重" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="alarmlevel">
              <a-input-number v-model="model.alarmlevel" placeholder="请输入告警级别，0为无级别，1为轻微，2为一般，3为严重" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="设备sn号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="sncode">
              <a-input v-model="model.sncode" placeholder="请输入设备sn号"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="机械id" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="machineid">
              <a-input v-model="model.machineid" placeholder="请输入机械id"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="机械名称" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="machinename">
              <a-input v-model="model.machinename" placeholder="请输入机械名称"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="告警时间" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="datatime">
              <j-date placeholder="请选择告警时间" v-model="model.datatime"  style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="标准值" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="standard">
              <a-input v-model="model.standard" placeholder="请输入标准值"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="真实值" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="actual">
              <a-input v-model="model.actual" placeholder="请输入真实值"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="经度" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="lon">
              <a-input v-model="model.lon" placeholder="请输入经度"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="纬度" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="lat">
              <a-input v-model="model.lat" placeholder="请输入纬度"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="桩号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="roadstation">
              <a-input v-model="model.roadstation" placeholder="请输入桩号"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="左右幅，左负右正" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="roadoffset">
              <a-input v-model="model.roadoffset" placeholder="请输入左右幅，左负右正"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="告警内容" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="alarminfo">
              <a-input v-model="model.alarminfo" placeholder="请输入告警内容"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="projectid" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="projectid">
              <a-input v-model="model.projectid" placeholder="请输入projectid"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="sectionid" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="sectionid">
              <a-input v-model="model.sectionid" placeholder="请输入sectionid"  ></a-input>
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
    name: 'HcPavementAlarmForm',
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
          add: "/hpa/hcPavementAlarm/add",
          edit: "/hpa/hcPavementAlarm/edit",
          queryById: "/hpa/hcPavementAlarm/queryById"
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