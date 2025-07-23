<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <a-col :span="24">
            <a-form-model-item label="设备编号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="sbbh">
              <a-input v-model="model.sbbh" placeholder="请输入设备编号"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="更新时间" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="updatetime">
              <j-date placeholder="请选择更新时间" v-model="model.updatetime"  style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="位置（内置、外置）" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="position">
              <a-input v-model="model.position" placeholder="请输入位置（内置、外置）"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="龄期（天）" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="lq">
              <a-input v-model="model.lq" placeholder="请输入龄期（天）"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="等效龄期（天）" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="equivalentlq">
              <a-input v-model="model.equivalentlq" placeholder="请输入等效龄期（天）"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="成熟度（°C*天）" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="maturity">
              <a-input v-model="model.maturity" placeholder="请输入成熟度（°C*天）"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="温度（°C混凝土强度监测）" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="temperature">
              <a-input v-model="model.temperature" placeholder="请输入温度（°C混凝土强度监测）"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="强度/预设强度（Mpa）" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="strength">
              <a-input v-model="model.strength" placeholder="请输入强度/预设强度（Mpa）"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="文件地址" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="file">
              <a-input v-model="model.file" placeholder="请输入文件地址"  ></a-input>
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
    name: 'DeviceConcreteHistorydataForm',
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
          add: "/device_concrete_historydata/deviceConcreteHistorydata/add",
          edit: "/device_concrete_historydata/deviceConcreteHistorydata/edit",
          queryById: "/device_concrete_historydata/deviceConcreteHistorydata/queryById"
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