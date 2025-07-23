<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
            <a-col :span="24">
              <a-form-model-item label="所属部门" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <JselectDqDepart v-decorator="['sysOrgCode']" ::multi="false"  v-model="model.sysOrgCode"/>
              </a-form-model-item>
            </a-col>
            <a-col :span="24">
              <a-form-model-item label="材料名称" :labelCol="labelCol" :wrapperCol="wrapperCol"
                                 :rules="[{ required: true, message: '材料名称不能为空' }]"   prop="cailiaoname">
                <a-input v-model="model.cailiaoname" placeholder="请输入材料名称"  ></a-input>
              </a-form-model-item>
            </a-col>
<!--            <a-col :span="24">-->
<!--              <a-form-model-item label="材料编号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="cailiaono">-->
<!--                <a-input v-model="model.cailiaono" placeholder="默认不写会自动生成材料编号" defaultValue="默认不写会自动生成材料编号" ></a-input>-->
<!--              </a-form-model-item>-->
<!--            </a-col>-->
          <a-col :span="24">
            <a-form-model-item label="材料类型" :labelCol="labelCol" :wrapperCol="wrapperCol"
                               :rules="[{ required: true, message: '材料类型不能为空' }]"
                               prop="nodetype">
<!--              <a-input v-model="model.nodetype" placeholder="请输入材料类型"  ></a-input>-->
              <j-dict-select-tag  v-model="model.nodetype"  dictCode="nodetypeCP" placeholder="请选择材料类型"/>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="材料规格" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="guigexinghao">
              <a-input v-model="model.guigexinghao" placeholder="请输入材料规格"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="材料计量单位" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-dict-select-tag  v-model="model.wzcailiaojiliangdanwei"  dictCode="WZCaiLiaoJiLiangDanWei" placeholder="材料计量单位"/>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="路面项目" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-dict-select-tag  v-model="model.lmcailiaolx"  dictCode="lmcailiaolx" placeholder="路面项目"/>
            </a-form-item>
          </a-col>
          <a-col :span="24">

            <a-form-item label="是否使用电子锁" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-dict-select-tag  v-model="model.iselocks"  dictCode="yn" placeholder="请选择是否使用电子锁"/>
            </a-form-item>
          </a-col>


        </a-row>
      </a-form-model>
    </j-form-container>
  </a-spin>
</template>

<script>

  import { httpAction, getAction } from '@/api/manage'
  import pick from 'lodash.pick'
  import { validateDuplicateValue } from '@/utils/util'
  import JFormContainer from '@/components/jeecg/JFormContainer'
  import JDate from '@/components/jeecg/JDate'
  import JselectDqDepart from '@comp/jeecgbiz/JselectDqDepart'
  import Vue from "vue";
  import {DEPART_ID } from '@/store/mutation-types'

  export default {
    name: 'WzcailiaonamedictManForm',
    components: {
      JFormContainer,
      JDate,
      JselectDqDepart,
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
        form: this.$form.createForm(this),
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
          add: "/wzcailiaonamedictman/wzcailiaonamedictMan/add",
          edit: "/wzcailiaonamedictman/wzcailiaonamedictMan/edit",
          queryById: "/wzcailiaonamedictman/wzcailiaonamedictMan/queryById"
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
       //备份model原始值
      //this.modelDefault = JSON.parse(JSON.stringify(this.model));
    },
    methods: {
      add () {
        this.edit({});
      },
      edit (record) {
        this.form.resetFields();
        this.model = Object.assign({}, record);
        this.visible = true;
        this.$nextTick(() => {
          this.form.setFieldsValue(pick(this.model,'sysOrgCode','cailiaoname','cailiaono','parentnode','nodetype','guigexinghao','wzcailiaojiliangdanwei','ischengzhong','miaoshu','isyunxupiancha','zhengpiancha','fupiancha','wzcailiaodanweihuansuan','pinpai','chandi','danjia','createBy','createTime','guid','ts','isdel','status','lmcailiaolx','shangxian','updateBy','updateTime',"iselocks"))
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
            console.log("表单提交数据1",values)
            formData['wzcailiaodanweihuansuan'] = Vue.ls.get(DEPART_ID);
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
        this.form.setFieldsValue(pick(row,'sysOrgCode','cailiaoname','cailiaono','parentnode','nodetype','guigexinghao','wzcailiaojiliangdanwei','ischengzhong','miaoshu','isyunxupiancha','zhengpiancha','fupiancha','wzcailiaodanweihuansuan','pinpai','chandi','danjia','createBy','createTime','guid','ts','isdel','status','lmcailiaolx','shangxian','updateBy','updateTime',"iselocks"))
      },
    }
  }
</script>