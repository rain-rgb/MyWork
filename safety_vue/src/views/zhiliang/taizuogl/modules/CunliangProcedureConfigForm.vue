<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form :form="form" slot="detail">
        <a-row>
          <a-col :span="24">
            <a-form-item label="所属部门" :labelCol="labelCol" :wrapperCol="wrapperCol">
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
            <a-form-item label="当前层数" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['dangqianceng',validatorRules.dangqianceng]" placeholder="请输入当前层数"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="行" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['lianghang',validatorRules.lianghang]" placeholder="请输入行"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="列" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['lianglie',validatorRules.lianglie]" placeholder="请输入列"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="总层数" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['cengshu',validatorRules.cengshu]" placeholder="请输入总层数"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="当前梁坐的名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['liangzuoname',validatorRules.liangzuoname]" placeholder="请输入当前梁坐的名称"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="继电器设备id" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['uuid']" placeholder="请输入继电器设备id"  ></a-input>
            </a-form-item>
          </a-col>
<!--          <a-col :span="24">-->
<!--            <a-form-item label="创建时间" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--              <j-date placeholder="请选择创建时间" v-decorator="['createTime']" :trigger-change="true" style="width: 100%" dateFormat="YYYY-MM-DD HH:mm:ss"/>-->
<!--            </a-form-item>-->
<!--          </a-col>-->
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
  import JDate from '@/components/jeecg/JDate'
  import { duplicateCheck2,duplicateCheck4, usershebeiList } from '@api/api'
  import { SYS_DEPART_ORGCODE } from '@/store/mutation-types'
  import Vue from 'vue'
  import JselectDqDepart from '@comp/jeecgbiz/JselectDqDepart'

  export default {
    name: 'CunliangProcedureConfigForm',
    components: {
      JFormContainer,
      JDate,
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
        cunliangid:0,
        selectValue: '',
        asyncSelectValue: '',
        dictOption: [],
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
          cengshu: {
            rules: [
              { pattern: /^[0-9]*[1-9][0-9]*$/ , message: '请输入正确格式的总层数!'},
            ]
          },
          dangqianceng: {
            rules: [
              { pattern: /^[0-9]*[1-9][0-9]*$/ , message: '请输入正确格式的总层数!'},
            ]
          },
          lianglie: {
            rules: [
              { validator: this.validatelianglie},
            ]
          },
          lianghang: {
            rules: [
              { pattern: /^[0-9]*[1-9][0-9]*$/ , message: '请输入正确格式的梁行' },
            ]
          },
          liangzuoname: {
            rules: [
              { validator: this.validateliangzuoname },
            ]
          },
        },
        url: {
          add: "/cunliangprocedureconfig/cunliangProcedureConfig/add",
          edit: "/cunliangprocedureconfig/cunliangProcedureConfig/edit1",
          queryById: "/cunliangprocedureconfig/cunliangProcedureConfig/queryById"
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
        this.visible = true;
        this.cunliangid = record.id;
        this.$nextTick(() => {
          this.form.setFieldsValue(pick(this.model,'shebeino','lianghang','lianglie','cengshu','liangzuoname','createBy','createTime','sysOrgCode','dangqianceng','uuid'))
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
          sbtypes:'41'
        }).then(res=>{
          this.dictOption=[];
          let result=res.result;
          result.forEach(re=>{
            this.dictOption.push({text:re.sbname,value:re.sbjno})
          })
          //console.log(res)
        })
      },
      validatelianglie(rule, value, callback){
        if(!value){
          callback()
        }else{
          if(new RegExp(/^[0-9]*[1-9][0-9]*$/).test(value)){
            const form = this.form;
            const lianghang = form.getFieldValue('lianghang');
            const shebeino = form.getFieldValue('shebeino');
            const dangqianceng = form.getFieldValue('dangqianceng');
            console.log("lianghang",lianghang)
            var params = {
              tableName: 'cunliang_procedure_config',
              fieldName: 'lianglie',
              fieldVal: value,
              dataId: this.cunliangid,
              dataId1: lianghang,
              dataId2: shebeino,
              dataId3: dangqianceng
            };
            duplicateCheck2(params).then((res) => {
              console.log(res)
              if (res.success) {
                callback()
              } else {
                callback("梁列已存在!")
              }
            })
          }else{
            callback("请输入正确格式的梁列!")
          }
        }
      },
      validateliangzuoname(rule, value, callback){
        if(!value){
          callback()
        }else{
          if(value){
            const form = this.form;
            const shebeino = form.getFieldValue('shebeino');
            const dangqianceng = form.getFieldValue('dangqianceng');
            var params = {
              tableName: 'cunliang_procedure_config',
              fieldName: 'liangzuoname',
              fieldVal: value,
              dataId: this.cunliangid,
              dataId2: shebeino,
              dataId1: dangqianceng
            };
            duplicateCheck4(params).then((res) => {
              console.log(res)
              if (res.success) {
                callback()
              } else {
                callback("梁座名称已存在!")
              }
            })
          }else{
            callback("请输入正确格式的梁座名称!")
          }
        }
      },
      popupCallback(row){
        this.form.setFieldsValue(pick(row,'shebeino','lianghang','lianglie','cengshu','liangzuoname','createBy','createTime','sysOrgCode','dangqianceng','uuid'))
      },
    }
  }
</script>