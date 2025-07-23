<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <a-col :span="24">
<!--            <a-form-model-item label="设备编号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="shebeiNo">-->
<!--              <a-input v-model="model.shebeiNo" placeholder="请输入设备编号"  ></a-input>-->
<!--            </a-form-model-item>-->
            <a-form-item label="设备名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-search-select-tag placeholder="请选择设备名称" v-model="model.shebeiNo" :dictOptions="dictOption">
              </j-search-select-tag>
              {{ selectValue }}
            </a-form-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :span="24">
            <a-form-item label="是否报警" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-dict-select-tag type="list" v-model="model.alertOrNot" :trigger-change="true" dictCode="is_call"
                                 placeholder="请选择是否报警"/>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :span="24">
            <a-form-item label="张拉初级:" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-model="model.tensionChu" placeholder="请输入预警范围(注意：正数)"  ></a-input>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :span="24">
            <a-form-item label="张拉中级:" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-model="model.tensionZhong" placeholder="请输入预警范围(注意：正数)"  ></a-input>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :span="24">
            <a-form-item label="张拉高级:" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-model="model.tensionGao" placeholder="请输入预警范围(注意：正数)"  ></a-input>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :span="24">
            <a-form-item label="伸长率初级:" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-model="model.elongationChu" placeholder="请输入预警范围(注意：正数)"  ></a-input>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :span="24">
            <a-form-item label="伸长率中级:" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-model="model.elongationZhong" placeholder="请输入预警范围(注意：正数)"  ></a-input>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :span="24">
            <a-form-item label="伸长率高级:" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-model="model.elongationGao" placeholder="请输入预警范围(注意：正数)"  ></a-input>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :span="24">
            <a-form-item label="短信预警负责人:" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-dict-select-tag type="list" v-model="model.names" placeholder="请选择短信预警负责人"
                                 dictCode="bhz_phone,names,uid,phonesname='5'" dictTable="bhz_phone"></j-dict-select-tag>
            </a-form-item>
          </a-col>
        </a-row>
<!--        <a-row>-->
<!--          <a-col :span="24">-->
<!--            <a-form-item label="手机号码:" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--              <a-input v-model="model.phones" placeholder="请输入手机号码"  ></a-input>-->
<!--            </a-form-item>-->
<!--          </a-col>-->
<!--        </a-row>-->
        <a-row>
          <a-col :span="24">
            <a-form-item label="是否发送:" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-dict-select-tag type="list" v-model="model.yesornot" :trigger-change="true" dictCode="yesornot"
                                 placeholder="请选择是否发送"/>
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
  import { usershebeiList } from '@api/api'
  import Vue from "vue";

  export default {
    name: 'TrZhanglaConfigureForm',
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
          add: "/trzhanglaconfigure/trZhanglaConfigure/add",
          edit: "/trzhanglaconfigure/trZhanglaConfigure/edit",
          queryById: "/trzhanglaconfigure/trZhanglaConfigure/queryById"
        }
      }
    },
    computed: {
      formDisabled(){
        return this.disabled
      },
    },
    created () {
      this.shebeiList()
       //备份model原始值
      this.modelDefault = JSON.parse(JSON.stringify(this.model));
    },
    methods: {
      shebeiList: function () {
        var sys_depart_orgcode = Vue.ls.get('SYS_DEPART_ORGCODE')
        usershebeiList({
          sys_depart_orgcode: sys_depart_orgcode,
          sbtypes: '9'
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