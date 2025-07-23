<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <a-col :span="24">
            <a-form-model-item label="设备地址" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="devaddr">
              <a-input v-model="model.devaddr" placeholder="请输入设备地址"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="总数" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="total">
              <a-input-number v-model="model.total" placeholder="请输入总数" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="超标总数" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="chaototal">
              <a-input-number v-model="model.chaototal" placeholder="请输入超标总数" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="初级超标数" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="primaryTotal">
              <a-input-number v-model="model.primaryTotal" placeholder="请输入初级超标数" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="pm25超标总数" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="pm25chao">
              <a-input-number v-model="model.pm25chao" placeholder="请输入pm25超标总数" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="pm10超标总数" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="pm10chao">
              <a-input-number v-model="model.pm10chao" placeholder="请输入pm10超标总数" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="噪音超标总数" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="noisechao">
              <a-input-number v-model="model.noisechao" placeholder="请输入噪音超标总数" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="温度超标数" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="temchao">
              <a-input-number v-model="model.temchao" placeholder="请输入温度超标数" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="湿度超标数" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="humchao">
              <a-input-number v-model="model.humchao" placeholder="请输入湿度超标数" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="风力超标数" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="winchao">
              <a-input-number v-model="model.winchao" placeholder="请输入风力超标数" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="风速超标数" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="winsdchao">
              <a-input-number v-model="model.winsdchao" placeholder="请输入风速超标数" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="统计时间，以天为单位" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="statisticsTime">
              <j-date placeholder="请选择统计时间，以天为单位" v-model="model.statisticsTime"  style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="中级超标数" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="middleTotal">
              <a-input-number v-model="model.middleTotal" placeholder="请输入中级超标数" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="高级超标数" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="advanceTotal">
              <a-input-number v-model="model.advanceTotal" placeholder="请输入高级超标数" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="pm25初级超标数" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="pm25primary">
              <a-input-number v-model="model.pm25primary" placeholder="请输入pm25初级超标数" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="pm25中级超标数" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="pm25middle">
              <a-input-number v-model="model.pm25middle" placeholder="请输入pm25中级超标数" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="pm25高级超标数" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="pm25advance">
              <a-input-number v-model="model.pm25advance" placeholder="请输入pm25高级超标数" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="pm10初级超标数" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="pm10primary">
              <a-input-number v-model="model.pm10primary" placeholder="请输入pm10初级超标数" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="pm10中级超标数" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="pm10middle">
              <a-input-number v-model="model.pm10middle" placeholder="请输入pm10中级超标数" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="pm10高级超标数" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="pm10advance">
              <a-input-number v-model="model.pm10advance" placeholder="请输入pm10高级超标数" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="噪声初级超标数" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="noiseprimary">
              <a-input-number v-model="model.noiseprimary" placeholder="请输入噪声初级超标数" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="噪声中级超标数" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="noisemiddle">
              <a-input-number v-model="model.noisemiddle" placeholder="请输入噪声中级超标数" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="噪声高级超标数" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="noiseadvance">
              <a-input-number v-model="model.noiseadvance" placeholder="请输入噪声高级超标数" style="width: 100%" />
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
    name: 'DevicehistoryStaticForm',
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
          add: "/devicehistorystatic/devicehistoryStatic/add",
          edit: "/devicehistorystatic/devicehistoryStatic/edit",
          queryById: "/devicehistorystatic/devicehistoryStatic/queryById"
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