<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <a-col :span="24">
            <a-form-model-item label="设备编号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="sbjno">
              <j-search-select-tag placeholder="请选择设备名称" v-model="model.sbjno" :dictOptions="dictOption" >
              </j-search-select-tag>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="生产批次时间（h）" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="hour">
              <a-input-number v-model="model.hour" placeholder="请输入生产批次时间（h）" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="粗集料(t)" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="cujiliao">
              <a-input-number v-model="model.cujiliao" placeholder="请输入粗集料(t)" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="细集料(t)" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="xijiliao">
              <a-input-number v-model="model.xijiliao" placeholder="请输入细集料(t)" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="水泥(t)" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="shuini">
              <a-input-number v-model="model.shuini" placeholder="请输入水泥(t)" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="矿粉(t)" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="kuangfeng">
              <a-input-number v-model="model.kuangfeng" placeholder="请输入矿粉(t)" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="粉煤灰(t)" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="fenmeihui">
              <a-input-number v-model="model.fenmeihui" placeholder="请输入粉煤灰(t)" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="减水剂(t)" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="jianshuiji">
              <a-input-number v-model="model.jianshuiji" placeholder="请输入减水剂(t)" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="其他(t)" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="other">
              <a-input-number v-model="model.other" placeholder="请输入其他(t)" style="width: 100%" />
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
  import Vue from "vue";
  import {usershebeiList} from "@api/api";

  export default {
    name: 'WzyclConfigForm',
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
        dictOption:[],
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
          add: "/wztaizhang/wzyclConfig/add",
          edit: "/wztaizhang/wzyclConfig/edit",
          queryById: "/wztaizhang/wzyclConfig/queryById"
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
      this.shebeiList();
    },
    methods: {
      shebeiList:function (){
        var sys_depart_orgcode=Vue.ls.get('SYS_DEPART_ORGCODE');
        usershebeiList({
          sys_depart_orgcode:sys_depart_orgcode,
          sbtypes:'27'
        }).then(res=>{
          this.dictOption=[];
          let result=res.result;
          result.forEach(re=>{
            this.dictOption.push({text:re.sbname,value:re.sbjno})
          })
          //console.log(res)
        })
      },
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