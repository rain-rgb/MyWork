<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <a-col :span="24">
            <a-form-model-item label="设备编号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="shebeino">
              <a-input v-model="model.shebeino" placeholder="请输入设备编号"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="设计桩长(m)" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="designdep">
              <a-input v-model="model.designdep" placeholder="请输入设计桩长(m)"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="锤击次数" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="pileSecond">
              <a-input-number v-model="model.pileSecond" placeholder="请输入锤击次数" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="倾角下限" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="xx">
              <a-input v-model="model.xx" placeholder="请输入倾角下限"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="倾角上限" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="xs">
              <a-input v-model="model.xs" placeholder="请输入倾角上限"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="软基预警号码" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="jbzphones">
              <a-input v-model="model.jbzphones" placeholder="请输入软基预警号码"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="是否报警:0=报警,1=不报警" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="isCall">
              <a-input-number v-model="model.isCall" placeholder="请输入是否报警:0=报警,1=不报警" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="是否删除（0：未删除，1：已删除）" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="isdel">
              <a-input-number v-model="model.isdel" placeholder="请输入是否删除（0：未删除，1：已删除）" style="width: 100%" />
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
    name: 'DeviceHammeringOneCfgForm',
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
          add: "/deviceHammeringonecfg/deviceHammeringOneCfg/add",
          edit: "/deviceHammeringonecfg/deviceHammeringOneCfg/edit",
          queryById: "/deviceHammeringonecfg/deviceHammeringOneCfg/queryById"
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