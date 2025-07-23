<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <a-col :span="24">
            <a-form-item label="设备名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-search-select-tag placeholder="请选择设备名称" v-model="model.shebeino" :dictOptions="dictOption">
              </j-search-select-tag>
              {{ selectValue }}
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="是否报警" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-dict-select-tag type="list" v-model="model.alertOrNot" :trigger-change="true" dictCode="is_call"
                                 placeholder="请选择是否报警"/>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="压浆温度最小值" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="yajiangCmin">
              <a-input v-model="model.yajiangCmin" placeholder="请输入压浆温度最小值"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="压浆温度最大值" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="yajiangCmax">
              <a-input v-model="model.yajiangCmax" placeholder="请输入压浆温度最大值"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="压浆压力最小值" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="yajiangYlmin">
              <a-input v-model="model.yajiangYlmin" placeholder="请输入压浆压力最小值"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="压浆压力最大值" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="yajiangYlmax">
              <a-input v-model="model.yajiangYlmax" placeholder="请输入压浆压力最大值"  ></a-input>
            </a-form-model-item>
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
    name: 'TrYajiangConfigureForm',
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
          add: "/tryajiangconfigure/trYajiangConfigure/add",
          edit: "/tryajiangconfigure/trYajiangConfigure/edit",
          queryById: "/tryajiangconfigure/trYajiangConfigure/queryById"
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
          sbtypes: '10'
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