<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <a-col :span="24">
            <a-form-model-item label="测点" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="cedian">
              <a-input v-model="model.cedian" placeholder="请输入测点"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="数据时间" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="datatime">
              <j-date placeholder="请选择数据时间" v-model="model.datatime"  style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="原始测值（频率）" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="abparam1">
              <a-input v-model="model.abparam1" placeholder="请输入原始测值（频率）"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="原始测值温度" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="abparam2">
              <a-input v-model="model.abparam2" placeholder="请输入原始测值温度"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="成果值" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="abparam3">
              <a-input v-model="model.abparam3" placeholder="请输入成果值"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="成果值温度（℃）" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="abparam4">
              <a-input v-model="model.abparam4" placeholder="请输入成果值温度（℃）"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="情况" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="abparam5">
              <a-input v-model="model.abparam5" placeholder="请输入情况"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="备用" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="abparam6">
              <a-input v-model="model.abparam6" placeholder="请输入备用"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="说明" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="note">
              <a-input v-model="model.note" placeholder="请输入说明"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="基底应力计测值；土压力计监测；单向测缝计测值；钢筋应力计测值；混凝土应变计组；无应力计监测" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="cediantype">
              <a-input v-model="model.cediantype" placeholder="请输入基底应力计测值；土压力计监测；单向测缝计测值；钢筋应力计测值；混凝土应变计组；无应力计监测"  ></a-input>
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
    name: 'JikengAnquanZhutiForm',
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
          add: "/jikeng/jikengAnquanZhuti/add",
          edit: "/jikeng/jikengAnquanZhuti/edit",
          queryById: "/jikeng/jikengAnquanZhuti/queryById"
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