<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <a-col :span="24">
            <a-form-model-item label="所属类型" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="gblx" required>
              <j-dict-select-tag type="list" v-model="model.gblx"
                                 :trigger-change="true" dictCode="gblx" placeholder="请选择所属类型" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24" v-if="model.gblx==1">
            <a-form-item label="材料类型" :labelCol="labelCol" :wrapperCol="wrapperCol" required>
              <j-dict-select-tag type="list" v-model="model.nodetype"
                                 :trigger-change="true" dictCode="nodeTypeCP" placeholder="请输入材料类型" />
            </a-form-item>
          </a-col>
          <a-col :span="24" v-else>
            <a-form-item label="材料类型" :labelCol="labelCol" :wrapperCol="wrapperCol" required>
              <j-dict-select-tag type="list" v-model="model.nodetype"
                                 :trigger-change="true" dictCode="nodetypeCL" placeholder="请输入材料类型" />
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="材料名称" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="cailiaoname" required >
              <a-input v-model="model.cailiaoname" placeholder="请输入材料名称"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="规格型号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="guigexinghao" required>
              <a-input v-model="model.guigexinghao" placeholder="请输入规格型号"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="材料计量单位" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-dict-select-tag  v-model="model.wzcailiaojiliangdanwei"  dictCode="WZCaiLiaoJiLiangDanWei" placeholder="材料计量单位"/>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="描述" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="miaoshu">
              <a-input v-model="model.miaoshu" placeholder="请输入描述"  ></a-input>
            </a-form-model-item>
          </a-col>
        </a-row>
      </a-form-model>
    </j-form-container>
  </a-spin>
</template>

<script>

  import { httpAction, getAction } from '@api/manage'
  import { validateDuplicateValue } from '@/utils/util'

  export default {
    name: 'WzcailiaonamedictAllForm',
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
          add: "/wzcailiaonamedictall/wzcailiaonamedictAll/add",
          edit: "/wzcailiaonamedictall/wzcailiaonamedictAll/edit",
          queryById: "/wzcailiaonamedictall/wzcailiaonamedictAll/queryById"
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
    },
    methods: {
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