<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <a-col :span="24">
            <a-form-model-item label="设备编号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="teid">
              <a-input v-model="model.teid" placeholder="请输入设备编号"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="时间" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="time">
              <j-date placeholder="请选择时间" v-model="model.time"  style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="纬度" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="lat">
              <a-input v-model="model.lat" placeholder="请输入纬度"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="经度" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="lng">
              <a-input v-model="model.lng" placeholder="请输入经度"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="方向" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="direction">
              <a-input-number v-model="model.direction" placeholder="请输入方向" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="高度" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="height">
              <a-input-number v-model="model.height" placeholder="请输入高度" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="车辆状态" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="carState">
              <a-input-number v-model="model.carState" placeholder="请输入车辆状态" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="teState" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="teState">
              <a-input-number v-model="model.teState" placeholder="请输入teState" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="预警状态" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="alarmState">
              <a-input-number v-model="model.alarmState" placeholder="请输入预警状态" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="电量" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="batteryVol">
              <a-input-number v-model="model.batteryVol" placeholder="请输入电量" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="基站" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="baseStation">
              <a-input v-model="model.baseStation" placeholder="请输入基站"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="数据包类型" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="packetType">
              <a-input v-model="model.packetType" placeholder="请输入数据包类型"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="sublock" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="sublock">
              <a-input v-model="model.sublock" placeholder="请输入sublock"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="业务编号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="businessId">
              <a-input v-model="model.businessId" placeholder="请输入业务编号"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="射频识别" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="rfid">
              <a-input v-model="model.rfid" placeholder="请输入射频识别"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="温度" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="temp">
              <a-input-number v-model="model.temp" placeholder="请输入温度" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="湿度" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="humidity">
              <a-input-number v-model="model.humidity" placeholder="请输入湿度" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="里程" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="mileage">
              <a-input-number v-model="model.mileage" placeholder="请输入里程" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="油量" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="fuel">
              <a-input-number v-model="model.fuel" placeholder="请输入油量" style="width: 100%" />
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
    name: 'WbshebeiHistoryForm',
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
          add: "/wbshebeihistory/wbshebeiHistory/add",
          edit: "/wbshebeihistory/wbshebeiHistory/edit",
          queryById: "/wbshebeihistory/wbshebeiHistory/queryById"
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