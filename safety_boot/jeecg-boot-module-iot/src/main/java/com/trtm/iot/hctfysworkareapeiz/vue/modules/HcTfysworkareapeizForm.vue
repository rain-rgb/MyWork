<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <a-col :span="24">
            <a-form-model-item label="项目" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="projectid">
              <a-input v-model="model.projectid" placeholder="请输入项目"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="标段" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="sectionid">
              <a-input v-model="model.sectionid" placeholder="请输入标段"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="开始桩号 面型工程没有桩号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="startstation">
              <a-input v-model="model.startstation" placeholder="请输入开始桩号 面型工程没有桩号"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="结束桩号 面型工程没有桩号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="endstation">
              <a-input v-model="model.endstation" placeholder="请输入结束桩号 面型工程没有桩号"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="碾压层数" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="layername">
              <a-input v-model="model.layername" placeholder="请输入碾压层数"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="施工高程" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="avgheights">
              <a-input v-model="model.avgheights" placeholder="请输入施工高程"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="设计宽度" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="thickavgs">
              <a-input v-model="model.thickavgs" placeholder="请输入设计宽度"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="施工日期" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="starttimes">
              <a-input v-model="model.starttimes" placeholder="请输入施工日期"  ></a-input>
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

  export default {
    name: 'HcTfysworkareapeizForm',
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
          add: "/hctfysworkareapeiz/hcTfysworkareapeiz/add",
          edit: "/hctfysworkareapeiz/hcTfysworkareapeiz/edit",
          queryById: "/hctfysworkareapeiz/hcTfysworkareapeiz/queryById"
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