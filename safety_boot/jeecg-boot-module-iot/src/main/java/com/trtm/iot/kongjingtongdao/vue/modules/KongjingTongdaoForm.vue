<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <a-col :span="24">
            <a-form-model-item label="guid" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="guid">
              <a-input v-model="model.guid" placeholder="请输入guid"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="通道号 0、1、2、3（其依次对 应X、X'、Y、Y'）" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="channelnum">
              <a-input-number v-model="model.channelnum" placeholder="请输入通道号 0、1、2、3（其依次对 应X、X'、Y、Y'）" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="状态 0：未启用 1：启用并 显示" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="status">
              <a-input-number v-model="model.status" placeholder="请输入状态 0：未启用 1：启用并 显示" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="中心距 单位mm" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="centerdistance">
              <a-input-number v-model="model.centerdistance" placeholder="请输入中心距 单位mm" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="增益" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="gain">
              <a-input-number v-model="model.gain" placeholder="请输入增益" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="延时 单位us" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="delay">
              <a-input-number v-model="model.delay" placeholder="请输入延时 单位us" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="校零时间 单位us" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="zerotime">
              <a-input-number v-model="model.zerotime" placeholder="请输入校零时间 单位us" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="GPS 状态 0 无效 1 有效" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="gpsvalid">
              <a-input-number v-model="model.gpsvalid" placeholder="请输入GPS 状态 0 无效 1 有效" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="经度" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="gpslongitude">
              <a-input-number v-model="model.gpslongitude" placeholder="请输入经度" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="纬度" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="gpslatitude">
              <a-input-number v-model="model.gpslatitude" placeholder="请输入纬度" style="width: 100%" />
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
    name: 'KongjingTongdaoForm',
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
          add: "/kongjingtongdao/kongjingTongdao/add",
          edit: "/kongjingtongdao/kongjingTongdao/edit",
          queryById: "/kongjingtongdao/kongjingTongdao/queryById"
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