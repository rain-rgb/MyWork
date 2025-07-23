<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row >

          <a-col :span="48">
            <a-form-model-item label="闭合措施" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="closesteps">
              <a-input v-model="model.closesteps" placeholder="请输入闭合措施"  ></a-input>
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
  import JImageUpload from "@comp/jeecg/JImageUpload";

  export default {
    name: 'MixpileOneOverHandlerForm',
    components: {JImageUpload
    },
    
   
    props: {
      handlestate:0,
      process:0,// 0闭合

      //表单禁用
      disabled: {
        type: Boolean,
        default: false,
        required: false
      }
    },
    data () {
      return {
        isMultiple : true,
      
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
          add: "/syRegistered/syRegistered/changestatus",
        //   edit: "/mixpileoneoverhandler/mixpileOneOverHandler/editNext",
        //   queryById: "/mixpileoneoverhandler/mixpileOneOverHandler/queryById"
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
      add (record) {
        this.edit(record);

      },
      edit (record) {
        this.model = Object.assign({}, record.handler);

          this.model.id =  record.id;
          this.model.closestatus = record.closestatus;
          this.model.closesteps = record.closesteps;

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
            
            httpurl+=this.url.add;
            method = 'post';
           
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