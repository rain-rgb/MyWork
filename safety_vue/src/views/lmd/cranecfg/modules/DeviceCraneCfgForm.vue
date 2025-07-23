<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form :form="form" slot="detail">
        <a-row>
          <a-col :span="24">
            <a-form-item label="所属部门" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <JselectDqDepart v-decorator="['sysOrgCode']" multi  />
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="设备编号" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-search-select-tag placeholder="请选择设备名称" v-decorator="['deviceCode']" :dictOptions="dictOption" >
              </j-search-select-tag>
              {{ selectValue }}
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="大型设备手机号码组" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-dict-select-tag type="list" v-decorator="['cranePhones']" :trigger-change="true" dictCode="bhz_phone,names,uid,phonesname='24'"
                                 dictTable="bhz_phone" placeholder="请选择大型设备手机号码组"/>
            </a-form-item>
          </a-col>
<!--          <a-col :span="24">-->
<!--            <a-form-item label="主键uuid" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--              <a-input v-decorator="['uid']" placeholder="请输入主键uuid"  ></a-input>-->
<!--            </a-form-item>-->
<!--          </a-col>-->
          <a-col :span="24">
            <a-form-item label="主钩吊重超额重比例(%)" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="['mainHookloadlv']" placeholder="请输入主钩吊重超额重比例(%)" style="width: 100%" />
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="副钩吊重超额重比例(%)" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="['vicehookloadlv']" placeholder="请输入副钩吊重超额重比例(%)" style="width: 100%" />
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="副钩吊重超主钩吊重比例(%)" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="['mainVicelv']" placeholder="请输入副钩吊重超主钩吊重比例(%)" style="width: 100%" />
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="风速报警值" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="['windSpeedalarm']" placeholder="请输入风速报警值" style="width: 100%" />
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="激光测距偏差值" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="['slr']" placeholder="请输入激光测距偏差值" style="width: 100%" />
            </a-form-item>
          </a-col>
<!--          <a-col :span="24">-->
<!--            <a-form-item label="是否删除（0：未删除，1：已删除）" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--              <a-input-number v-decorator="['isdel']" placeholder="请输入是否删除（0：未删除，1：已删除）" style="width: 100%" />-->
<!--            </a-form-item>-->
<!--          </a-col>-->
          <a-col :span="24">
            <a-form-item label="是否报警" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-dict-select-tag type="list" v-decorator="['isCall']" :trigger-change="true" dictCode="is_call" placeholder="请选择是否报警" />
            </a-form-item>
          </a-col>
<!--          <a-col :span="24">-->
<!--            <a-form-item label="创建时间" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--              <j-date placeholder="请选择创建时间" v-decorator="['createTime']" :trigger-change="true" style="width: 100%" />-->
<!--            </a-form-item>-->
<!--          </a-col>-->
<!--          <a-col :span="24">-->
<!--            <a-form-item label="更新时间" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--              <j-date placeholder="请选择更新时间" v-decorator="['updateTime']" :trigger-change="true" style="width: 100%" />-->
<!--            </a-form-item>-->
<!--          </a-col>-->

<!--          <a-col :span="24">-->
<!--            <a-form-item label="创建人" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--              <a-input v-decorator="['createBy']" placeholder="请输入创建人"  ></a-input>-->
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
  import { usershebeiList } from '@api/api'
  import JselectDqDepart from '@comp/jeecgbiz/JselectDqDepart'
  import Vue from 'vue'

  export default {
    name: 'DeviceCraneCfgForm',
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
        },
        url: {
          add: "/devicecranecfg/deviceCraneCfg/add",
          edit: "/devicecranecfg/deviceCraneCfg/edit",
          queryById: "/devicecranecfg/deviceCraneCfg/queryById"
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
      this.shebeiList()
    },
    methods: {
      shebeiList:function (){
        var sys_depart_orgcode=Vue.ls.get('SYS_DEPART_ORGCODE');
        usershebeiList({
          sys_depart_orgcode:sys_depart_orgcode,
          sbtypes:'21,23,50'
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
        this.edit({});
      },
      edit (record) {
        this.form.resetFields();
        this.model = Object.assign({}, record);
        this.visible = true;
        this.$nextTick(() => {
          this.form.setFieldsValue(pick(this.model,'deviceCode','uid','mainHookloadlv','vicehookloadlv','windSpeedalarm','slr','isdel','isCall','cranePhones','createTime','updateTime','sysOrgCode','createBy','mainVicelv'))
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
      popupCallback(row){
        this.form.setFieldsValue(pick(row,'deviceCode','uid','mainHookloadlv','vicehookloadlv','windSpeedalarm','slr','isdel','isCall','cranePhones','createTime','updateTime','sysOrgCode','createBy','mainVicelv'))
      },
    }
  }
</script>