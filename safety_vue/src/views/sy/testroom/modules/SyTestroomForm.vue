<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <a-col :span="24">
            <a-form-model-item label="试验室" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="testroom">
              <a-input v-model="model.testroom" placeholder="请输入试验室"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="检测室编号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="roomType">
              <a-input v-model="model.roomType" placeholder="检测室编号"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="面积" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="roomArea">
              <a-input v-model="model.roomArea" placeholder="请输入面积"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="负责人" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="fuzer">
              <a-input v-model="model.fuzer" placeholder="请输入负责人"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="设备数" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="shebeishu">
              <a-input v-model="model.shebeishu" placeholder="请输入设备数"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="人员数" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="renyuanshu">
              <a-input v-model="model.renyuanshu" placeholder="请输入人员数"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="环境设备" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="huanjsb">
              <a-input v-model="model.huanjsb" placeholder="请输入环境设备"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="温度要求" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="wendu1">
              <a-input v-model="model.wendu1" placeholder="请输入温度要求"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="实时温度" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="wendu2">
              <a-input v-model="model.wendu2" placeholder="请输入实时温度"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="湿度要求" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="shidu1">
              <a-input v-model="model.shidu1" placeholder="请输入湿度要求"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="实时湿度" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="shidu2">
              <a-input v-model="model.shidu2" placeholder="请输入实时湿度"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="设备列表" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="shebeis">
              <a-input v-model="model.shebeis" placeholder="例：电液何服万能试验机(Lx-022),1;钢筋弯曲试验机(Lx-026),2"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="人员列表" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="renyuans">
              <a-input v-model="model.renyuans" placeholder="例：李XX,主任;赵XX,检测员;"  ></a-input>
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
    name: 'SyTestroomForm',
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
          add: "/testroom/syTestroom/add",
          edit: "/testroom/syTestroom/edit",
          queryById: "/testroom/syTestroom/queryById"
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