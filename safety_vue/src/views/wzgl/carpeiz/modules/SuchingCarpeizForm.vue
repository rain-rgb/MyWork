<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <a-col :span='24'>
            <a-form-item label='所属部门' :labelCol='labelCol' :wrapperCol='wrapperCol' prop='sysOrgCode'>
              <JselectDqDepart v-model='model.sysOrgCode' />
            </a-form-item>
          </a-col>
          <a-col :span='24'>
            <a-form-item label='设备名称' :labelCol='labelCol' :wrapperCol='wrapperCol' prop='imei'>
              <j-search-select-tag placeholder='请选择设备名称' v-model='model.imei' :dictOptions='dictOption'>
              </j-search-select-tag>
            </a-form-item>
          </a-col>
          <a-col :span='24'>
            <a-form-item label='发车单手机号码' :labelCol='labelCol' :wrapperCol='wrapperCol' prop='zhydPhones'>
              <j-dict-select-tag type='list' v-model='model.zhydPhones' :trigger-change='true'
                                 dictCode="bhz_phone,names,uid,phonesname='20'" dictTable='bhz_phone'
                                 placeholder='请选择发车单手机号码' />
            </a-form-item>
          </a-col>
          <a-col :span='24'>
            <a-form-item label='是否报警' :labelCol='labelCol' :wrapperCol='wrapperCol' prop='isCall'>
              <j-dict-select-tag type='list' v-model='model.isCall' :trigger-change='true' dictCode='is_call'
                                 placeholder='请选择是否报警' />
            </a-form-item>
          </a-col>
<!--          <a-col :span="24">-->
<!--            <a-form-model-item label="发车单手机号码" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="zhydPhones">-->
<!--              <a-input v-model="model.zhydPhones" placeholder="请输入发车单手机号码"  ></a-input>-->
<!--            </a-form-model-item>-->
<!--          </a-col>-->
          <a-col :span="24">
            <a-form-model-item label="预计时间" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="remark">
              <a-input v-model="model.remark" placeholder="请输入预计时间"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label='时长单位' :labelCol='labelCol' :wrapperCol='wrapperCol' prop='unit'>
              <j-dict-select-tag type='list' v-model='model.unit' :trigger-change='true' dictCode='unit'
                                 placeholder='请选择时长单位' />
            </a-form-item>
          </a-col>
        </a-row>
      </a-form-model>
    </j-form-container>
  </a-spin>
</template>

<script>

  import { httpAction, getAction } from '@/api/manage'
  import { validateDuplicateValue } from '@/utils/util'
  import JFormContainer from '@/components/jeecg/JFormContainer'
  import JselectDqDepart from '@comp/jeecgbiz/JselectDqDepart'
  import { usershebeiList } from '@api/api'
  import Vue from 'vue'

  export default {
    name: 'SuchingCarpeizForm',
    components: {
      JFormContainer,
      JselectDqDepart
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
          add: "/suchingcarpeiz/suchingCarpeiz/add",
          edit: "/suchingcarpeiz/suchingCarpeiz/edit",
          queryById: "/suchingcarpeiz/suchingCarpeiz/queryById"
        }
      }
    },
    computed: {
      formDisabled(){
        return this.disabled
      },
    },
    created () {
      this.shebeiList();
       //备份model原始值
      this.modelDefault = JSON.parse(JSON.stringify(this.model));
    },
    methods: {
      shebeiList: function() {
        let sys_depart_orgcode = Vue.ls.get('SYS_DEPART_ORGCODE')
        usershebeiList({
          sys_depart_orgcode: sys_depart_orgcode,
          sbtypes: '35'
        }).then(res => {
          this.dictOption = []
          let result = res.result
          result.forEach(re => {
            this.dictOption.push({ text: re.sbname, value: re.sbjno })
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