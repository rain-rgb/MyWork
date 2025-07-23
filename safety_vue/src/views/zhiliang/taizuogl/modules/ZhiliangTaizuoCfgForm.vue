<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form :form="form" slot="detail">
        <a-row>
          <a-col :span="24">
            <a-form-item label="所属组织机构" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <JselectDqDepart v-decorator="['sysOrgCode']" ::multi="false"  />
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="梁场设备名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-search-select-tag placeholder="请选择梁场设备名称" v-decorator="['shebeino']" :dictOptions="dictOption" >
              </j-search-select-tag>
              {{ selectValue }}
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="台座编号" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['taizuono',validatorRules.taizuono]" placeholder="请输入台座编号" :disabled="true"></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="台座名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['taizuoname']" placeholder="请输入台座名称"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col v-if="showFlowSubmitButton" :span="24" style="text-align: center">
            <a-button @click="submitForm">提 交</a-button>
          </a-col>
        </a-row>
      </a-form>
    </j-form-container>
  </a-spin>
</template>

<script>

  import { httpAction, getAction } from '@/api/manage'
  import pick from 'lodash.pick'
  import { validateDuplicateValue } from '@/utils/util'
  import JFormContainer from '@/components/jeecg/JFormContainer'
  import JselectDqDepart from '@comp/jeecgbiz/JselectDqDepart'
  import { duplicateCheck5, usershebeiList } from '@api/api'
  import Vue from 'vue'
  import { SYS_DEPART_ORGCODE } from '@/store/mutation-types'

  export default {
    name: 'ZhiliangTaizuoCfgForm',
    components: {
      JFormContainer,
      JselectDqDepart
    },
    props: {
      //流程表单data
      formData: {
        type: Object,
        default: ()=>{},
        required: false
      },
      //表单模式：true流程表单 false普通表单
      formBpm: {
        type: Boolean,
        default: false,
        required: false
      },
      //表单禁用
      disabled: {
        type: Boolean,
        default: false,
        required: false
      }
    },
    data () {
      return {
        selectValue: '',
        asyncSelectValue: '',
        dictOption: [],
        zhiliangtzid:0,
        form: this.$form.createForm(this),
        model: {},
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
          taizuono: {
            rules: [
              { validator: this.validateTaizuono},
            ]
          },
        },
        url: {
          add: "/zhiliangtaizuocfg/zhiliangTaizuoCfg/add1",
          edit: "/zhiliangtaizuocfg/zhiliangTaizuoCfg/edit",
          queryById: "/zhiliangtaizuocfg/zhiliangTaizuoCfg/queryById"
        }
      }
    },
    computed: {
      formDisabled(){
        if(this.formBpm===true){
          if(this.formData.disabled===false){
            return false
          }
          return true
        }
        return this.disabled
      },
      showFlowSubmitButton(){
        if(this.formBpm===true){
          if(this.formData.disabled===false){
            return true
          }
        }
        return false
      }
    },
    created () {
      //如果是流程中表单，则需要加载流程表单data
      this.showFlowData();
      this.shebeiList();
    },
    methods: {
      add () {
        this.edit({});
      },
      edit (record) {
        this.form.resetFields();
        this.model = Object.assign({}, record);
        this.zhiliangtzid = this.model.id
        this.visible = true;
        this.$nextTick(() => {
          this.form.setFieldsValue(pick(this.model,'taizuono','taizuoname','sysOrgCode','shebeino'))
        })
      },
      //渲染流程表单数据
      showFlowData(){
        if(this.formBpm === true){
          let params = {id:this.formData.dataId};
          getAction(this.url.queryById,params).then((res)=>{
            if(res.success){
              this.edit (res.result);
            }
          });
        }
      },
      submitForm () {
        const that = this;
        // 触发表单验证
        this.form.validateFields((err, values) => {
          if (!err) {
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
            let formData = Object.assign(this.model, values);
            console.log("表单提交数据",formData)
            httpAction(httpurl,formData,method).then((res)=>{
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
      validateTaizuono(rule, value, callback){
        if(!value){
          callback()
        }else{
          if(value){
            const form = this.form;
            const shebeino = form.getFieldValue('shebeino');
            var params = {
              tableName: 'zhiliang_taizuo_cfg',
              fieldName: 'taizuono',
              fieldVal: value,
              dataId:this.zhiliangtzid,
              dataId1: shebeino
            };
            duplicateCheck5(params).then((res) => {
              console.log(res)
              if (res.success) {
                callback()
              } else {
                callback("制梁台座编号已存在!")
              }
            })
          }else{
            callback("请输入正确格式的台座编号!")
          }
        }
      },
      shebeiList:function (){
        var sys_depart_orgcode=Vue.ls.get('SYS_DEPART_ORGCODE');
        usershebeiList({
          sys_depart_orgcode:sys_depart_orgcode,
          sbtypes:'44'
        }).then(res=>{
          this.dictOption=[];
          let result=res.result;
          result.forEach(re=>{
            this.dictOption.push({text:re.sbname,value:re.sbjno})
          })
          //console.log(res)
        })
      },
      popupCallback(row){
        this.form.setFieldsValue(pick(row,'taizuono','taizuoname','sysOrgCode','shebeino'))
      },
    }
  }
</script>