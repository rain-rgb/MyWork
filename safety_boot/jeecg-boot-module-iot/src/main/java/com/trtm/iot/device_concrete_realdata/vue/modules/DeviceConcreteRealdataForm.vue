<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <a-col :span="24">
            <a-form-model-item label="施工地址" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="sgdz">
              <a-input v-model="model.sgdz" placeholder="请输入施工地址"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="工程名称" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="gcmc">
              <a-input v-model="model.gcmc" placeholder="请输入工程名称"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="项目名称" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="xmmc">
              <a-input v-model="model.xmmc" placeholder="请输入项目名称"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="设备编号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="sbbh">
              <a-input v-model="model.sbbh" placeholder="请输入设备编号"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="数据更新时间" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="updatetime">
              <j-date placeholder="请选择数据更新时间" v-model="model.updatetime"  style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="浇筑时间" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="jiaozhudate">
              <j-date placeholder="请选择浇筑时间" v-model="model.jiaozhudate"  style="width: 100%" />
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
            <a-form-model-item label="外置等效龄期（天）" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="equivalentlqoutside">
              <a-input v-model="model.equivalentlqoutside" placeholder="请输入外置等效龄期（天）"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="成熟度（°C*天）" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="maturity">
              <a-input v-model="model.maturity" placeholder="请输入成熟度（°C*天）"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="外置成熟度（°C*天）" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="maturityoutside">
              <a-input v-model="model.maturityoutside" placeholder="请输入外置成熟度（°C*天）"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="内置温度（°C）" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="temperature">
              <a-input v-model="model.temperature" placeholder="请输入内置温度（°C）"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="外置温度（°C）" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="temperatureoutside">
              <a-input v-model="model.temperatureoutside" placeholder="请输入外置温度（°C）"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="强度（Mpa）" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="strength">
              <a-input v-model="model.strength" placeholder="请输入强度（Mpa）"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="外置强度（Mpa）" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="strengthoutside">
              <a-input v-model="model.strengthoutside" placeholder="请输入外置强度（Mpa）"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="时间过程值" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="time">
              <a-textarea v-model="model.time" rows="4" placeholder="请输入时间过程值" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="成熟度过程值" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="maturitygcz">
              <a-textarea v-model="model.maturitygcz" rows="4" placeholder="请输入成熟度过程值" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="外置成熟度过程值" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="maturityoutsidegcz">
              <a-textarea v-model="model.maturityoutsidegcz" rows="4" placeholder="请输入外置成熟度过程值" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="强度过程值" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="strengthgcz">
              <a-textarea v-model="model.strengthgcz" rows="4" placeholder="请输入强度过程值" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="外置强度过程值" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="longtextoutsidegcz">
              <a-textarea v-model="model.longtextoutsidegcz" rows="4" placeholder="请输入外置强度过程值" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="温度过程值" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="tempgcz">
              <a-textarea v-model="model.tempgcz" rows="4" placeholder="请输入温度过程值" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="外置温度过程值" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="tempoutsidegcz">
              <a-textarea v-model="model.tempoutsidegcz" rows="4" placeholder="请输入外置温度过程值" />
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
    name: 'DeviceConcreteRealdataForm',
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
          add: "/device_concrete_realdata/deviceConcreteRealdata/add",
          edit: "/device_concrete_realdata/deviceConcreteRealdata/edit",
          queryById: "/device_concrete_realdata/deviceConcreteRealdata/queryById"
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