<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <a-col :span="24" >
            <a-form-item label="设备名称" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="设备"  required>
              <j-search-select-tag placeholder="请选择设备名称"  v-model="model.shebeino"  :dictOptions="dictOption" >
              </j-search-select-tag>
              {{ selectValue }}
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="拌叶片的总枚数" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="mixpileZ">
              <a-input v-model="model.mixpileZ" placeholder="请输入拌叶片的总枚数"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="搅拌叶片的宽度(m)" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="mixpileH">
              <a-input v-model="model.mixpileH" placeholder="请输入搅拌叶片的宽度(m)"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="搅拌头的回转数(r/min)" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="mixpileN">
              <a-input v-model="model.mixpileN" placeholder="请输入搅拌头的回转数(r/min)"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="搅拌叶片与搅拌轴的垂直夹角" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="mixpileB">
              <a-input v-model="model.mixpileB" placeholder="请输入搅拌叶片与搅拌轴的垂直夹角"  ></a-input>
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
  import {usershebeiList} from "@api/api";
  import { SYS_DEPART_ORGCODE } from '@/store/mutation-types'
  import JSelectDqProjName from '@comp/jeecgbiz/JselectDqProjName'
  import Vue from 'vue'

  export default {
    name: 'MixpileShebeicfgForm',
    components: {
      JSelectDqProjName
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
        dictOption: [],
        selectValue: '',
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
          add: "/mixpileshebeicfg/mixpileShebeicfg/add",
          edit: "/mixpileshebeicfg/mixpileShebeicfg/edit",
          queryById: "/mixpileshebeicfg/mixpileShebeicfg/queryById"
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
        if(this.model.orgcode !== null){
          sys_depart_orgcode = this.model.orgcode
        }
        usershebeiList({
          sys_depart_orgcode:sys_depart_orgcode,
          sbtypes:'16,53'
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