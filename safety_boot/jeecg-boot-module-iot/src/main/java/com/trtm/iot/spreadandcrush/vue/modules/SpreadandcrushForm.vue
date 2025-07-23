<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model :model="model" :rules="validatorRules" ref="form" slot="detail">
        <a-row>
          <a-col :span="24">
            <a-form-model-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="桩号" prop="station">
              <a-input placeholder="请输入桩号" v-model="model.station"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="结构层，数值越大为越高层" prop="layer">
              <a-input placeholder="请输入结构层，数值越大为越高层" v-model="model.layer"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="左右幅，0为左幅，1为右幅" prop="offset">
              <a-input-number placeholder="请输入左右幅，0为左幅，1为右幅" style="width: 100%" v-model="model.offset" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="摊铺温度（°C）" prop="pavingtemperature">
              <a-input placeholder="请输入摊铺温度（°C）" v-model="model.pavingtemperature"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="温度离析（°C）" prop="temperaturesegregation">
              <a-input placeholder="请输入温度离析（°C）" v-model="model.temperaturesegregation"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="摊铺速度（m/min）" prop="pavingspeed">
              <a-input-number placeholder="请输入摊铺速度（m/min）" style="width: 100%" v-model="model.pavingspeed" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="初压温度（°C）钢轮最高温度" prop="temperaturefirst">
              <a-input placeholder="请输入初压温度（°C）钢轮最高温度" v-model="model.temperaturefirst"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="复压温度（°C）胶轮最高温度" prop="temperaturerepeat">
              <a-input placeholder="请输入复压温度（°C）胶轮最高温度" v-model="model.temperaturerepeat"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="初压遍数（遍）钢轮遍数" prop="timesfirst">
              <a-input placeholder="请输入初压遍数（遍）钢轮遍数" v-model="model.timesfirst"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="复压遍数（遍）胶轮遍数" prop="timesrepeat">
              <a-input placeholder="请输入复压遍数（遍）胶轮遍数" v-model="model.timesrepeat"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="钢轮欠压率" prop="steelrate">
              <a-input placeholder="请输入钢轮欠压率" v-model="model.steelrate"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="胶轮欠压率" prop="rubberrate">
              <a-input placeholder="请输入胶轮欠压率" v-model="model.rubberrate"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="1:摊铺；2:碾压" prop="type">
              <a-input-number placeholder="请输入1:摊铺；2:碾压" style="width: 100%" v-model="model.type" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="设备编号" prop="shebeino">
              <a-input placeholder="请输入设备编号" v-model="model.shebeino"  ></a-input>
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
    name: 'SpreadandcrushForm',
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
           temperaturerepeat: [
              { required: true, message: '请输入复压温度（°C）胶轮最高温度!'},
           ],
        },
        url: {
          add: "/spreadandcrush/spreadandcrush/add",
          edit: "/spreadandcrush/spreadandcrush/edit",
          queryById: "/spreadandcrush/spreadandcrush/queryById"
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
