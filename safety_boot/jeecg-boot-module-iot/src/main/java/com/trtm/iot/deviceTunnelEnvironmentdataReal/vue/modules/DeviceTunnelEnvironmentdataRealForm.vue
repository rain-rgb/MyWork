<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <a-col :span="24">
            <a-form-model-item label="地点" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="site">
              <a-input v-model="model.site" placeholder="请输入地点"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="设备编号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="shebeino">
              <a-input v-model="model.shebeino" placeholder="请输入设备编号"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="数据上传时间" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="datatime">
              <j-date placeholder="请选择数据上传时间" v-model="model.datatime"  style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="0无报警，1低报警，2高报警" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="alarmtype">
              <a-input-number v-model="model.alarmtype" placeholder="请输入0无报警，1低报警，2高报警" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="氧气" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="oxygen">
              <a-input v-model="model.oxygen" placeholder="请输入氧气"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="甲烷" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="methane">
              <a-input v-model="model.methane" placeholder="请输入甲烷"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="硫化氢" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="hSulfide">
              <a-input v-model="model.hSulfide" placeholder="请输入硫化氢"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="二氧化碳" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="cDioxide">
              <a-input v-model="model.cDioxide" placeholder="请输入二氧化碳"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="一氧化碳" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="cMonoxide">
              <a-input v-model="model.cMonoxide" placeholder="请输入一氧化碳"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="风速" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="windspeed">
              <a-input v-model="model.windspeed" placeholder="请输入风速"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="数据时间" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="uploadtime">
              <a-input v-model="model.uploadtime" placeholder="请输入数据时间"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="温度" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="temperature">
              <a-input v-model="model.temperature" placeholder="请输入温度"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="guid" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="guid">
              <a-input v-model="model.guid" placeholder="请输入guid"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="粉尘" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="dust">
              <a-input v-model="model.dust" placeholder="请输入粉尘"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="噪声" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="noise">
              <a-input v-model="model.noise" placeholder="请输入噪声"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="PM1.0" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="pm10">
              <a-input v-model="model.pm10" placeholder="请输入PM1.0"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="PM2.0" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="pm20">
              <a-input v-model="model.pm20" placeholder="请输入PM2.0"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="PM10" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="pm10">
              <a-input v-model="model.pm10" placeholder="请输入PM10"  ></a-input>
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
    name: 'DeviceTunnelEnvironmentdataRealForm',
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
          add: "/deviceTunnelEnvironmentdataReal/deviceTunnelEnvironmentdataReal/add",
          edit: "/deviceTunnelEnvironmentdataReal/deviceTunnelEnvironmentdataReal/edit",
          queryById: "/deviceTunnelEnvironmentdataReal/deviceTunnelEnvironmentdataReal/queryById"
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