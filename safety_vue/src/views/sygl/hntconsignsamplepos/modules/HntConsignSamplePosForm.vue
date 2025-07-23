<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form :form="form" slot="detail">
        <a-row>
<!--          <a-col :span="24">-->
<!--            <a-form-item label="外键，组织机构id,只能是试验类型的组织机构" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--              <a-input v-decorator="['departid']" placeholder="请输入外键，组织机构id,只能是试验类型的组织机构"  ></a-input>-->
<!--            </a-form-item>-->
<!--          </a-col>-->
<!--          <a-col :span="24">-->
<!--            <a-form-item label="外键，组织机构编码" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--              <a-input v-decorator="['orgcode']" placeholder="请输入外键，组织机构编码"  ></a-input>-->
<!--            </a-form-item>-->
<!--          </a-col>-->
          <a-col :span="24">
            <a-form-item label="所属部门" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <JselectDqDepart v-decorator="['sysOrgCode']" ::multi="false"  />
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="货架名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['huojianame',validatorRules.huojianame]" placeholder="请输入货架名称"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="货架排行" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['huojiahang',validatorRules.huojiahang]" placeholder="请输入货架排行"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="货架排列" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['huojialie',validatorRules.huojialie]" placeholder="请输入货架排列"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="货架从下到上总层数" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['huojiacenshu',validatorRules.huojiacenshu]" placeholder="请输入货架从下到上总层数"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
<!--            <a-form-item label="外键，标养室id" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--              <a-input v-decorator="['temperatureid']" placeholder="请输入外键，标养室id"  ></a-input>-->
<!--            </a-form-item>-->
            <a-form-item label="标养室" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-search-select-tag placeholder="请选择标养室" v-decorator="['temperatureid']" :dictOptions="dictOption"  >
              </j-search-select-tag>
              {{ selectValue }}
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

  import { httpAction, getAction } from '@api/manage'
  import pick from 'lodash.pick'
  import { validateDuplicateValue } from '@/utils/util'
  import JFormContainer from '@comp/jeecg/JFormContainer'
  import JselectDqDepart from '@comp/jeecgbiz/JselectDqDepart'
  import Vue from 'vue'
  import { duplicateCheck1, usershebeiList } from '@api/api'

  export default {
    name: 'HntConsignSamplePosForm',
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
        yanghuid:0,
        selectValue:'',
        dictOption:[],
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
          huojianame: {
            rules: [
              { validator: this.validateHuojianame},
            ]
          },
          huojiahang: {
            rules: [
              { validator: this.validateHuojiahang},
            ]
          },
          huojialie: {
            rules: [
              { pattern: /^[0-9]*[1-9][0-9]*$/ , message: '请输入正确格式的货架排列!'},
            ]
          },
          huojiacenshu: {
            rules: [
              { pattern: /^[0-9]*[1-9][0-9]*$/ , message: '请输入正确格式的货架从下到上总层数!'},
            ]
          },
        },
        url: {
          add: "/hntconsignsamplepos/hntConsignSamplePos/add",
          edit: "/hntconsignsamplepos/hntConsignSamplePos/edit",
          queryById: "/hntconsignsamplepos/hntConsignSamplePos/queryById"
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
        this.yanghuid = this.model.id
        this.visible = true;
        this.$nextTick(() => {
          this.form.setFieldsValue(pick(this.model,'departid','sysOrgCode','huojianame','huojiahang','huojialie','huojiacenshu','temperatureid'))
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
      shebeiList:function (){
        var sys_depart_orgcode=Vue.ls.get('SYS_DEPART_ORGCODE');
        usershebeiList({
          sys_depart_orgcode:sys_depart_orgcode,
          sbtypes:'11'
        }).then(res=>{
          this.dictOption=[];
          let result=res.result;
          result.forEach(re=>{
            this.dictOption.push({text:re.sbname,value:re.sbjno})
          })
          //console.log(res)
        })
      },
      validateHuojianame(rule, value, callback){
        if(!value){
          callback()
        }else{
         // if(new RegExp(/^[a-zA-Z0-9]+$/).test(value)){
          if(new RegExp(/^[0-9]*[1-9][0-9]*$/).test(value)){
            const form = this.form;
            const sysOrgCode = form.getFieldValue('sysOrgCode');
            var params = {
              tableName: 'hnt_consign_sample_pos',
              fieldName: 'huojianame',
              fieldVal: value,
              dataId : this.yanghuid,
              dataId1: sysOrgCode
            };
            duplicateCheck1(params).then((res) => {
              console.log(res)
              if (res.success) {
                callback()
              } else {
                callback("货架名称已存在!")
              }
            })
          }else{
            callback("请输入正确格式的货架名称!")
          }
        }
      },
      validateHuojiahang(rule, value, callback){
        if(!value){
          callback()
        }else{
          if(new RegExp(/^[0-9]*[1-9][0-9]*$/).test(value)){
            const form = this.form;
            const sysOrgCode = form.getFieldValue('sysOrgCode');
            var params = {
              tableName: 'hnt_consign_sample_pos',
              fieldName: 'huojiahang',
              fieldVal: value,
              dataId : this.yanghuid,
              dataId1: sysOrgCode
            };
            duplicateCheck1(params).then((res) => {
              console.log(res)
              if (res.success) {
                callback()
              } else {
                callback("货架排行已存在!")
              }
            })
          }else{
            callback("请输入正确格式的货架排行!")
          }
        }
      },
      popupCallback(row){
        this.form.setFieldsValue(pick(row,'departid','sysOrgCode','huojianame','huojiahang','huojialie','huojiacenshu','temperatureid'))
      },
    }
  }
</script>