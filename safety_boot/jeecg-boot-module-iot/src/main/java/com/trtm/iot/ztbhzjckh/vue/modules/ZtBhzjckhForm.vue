<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <a-col :span="24">
            <a-form-model-item label="分包单位" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="customer">
              <a-input v-model="model.customer" placeholder="请输入分包单位"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="合同编号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="taiz">
              <a-input v-model="model.taiz" placeholder="请输入合同编号"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="单位工程" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="projname">
              <a-input v-model="model.projname" placeholder="请输入单位工程"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="分部工程" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="conspos">
              <a-input v-model="model.conspos" placeholder="请输入分部工程"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="分项工程" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="projadr">
              <a-input v-model="model.projadr" placeholder="请输入分项工程"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="发货日期" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="dattim">
              <j-date placeholder="请选择发货日期" v-model="model.dattim"  style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="25以上(65)" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="aboveew">
              <a-input-number v-model="model.aboveew" placeholder="请输入25以上(65)" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="25/20(80)" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="abovees">
              <a-input-number v-model="model.abovees" placeholder="请输入25/20(80)" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="20/10(90)" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="aboveys">
              <a-input-number v-model="model.aboveys" placeholder="请输入20/10(90)" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="10/0(100)" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="abovehg">
              <a-input-number v-model="model.abovehg" placeholder="请输入10/0(100)" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="0/-10(95)" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="abovejs">
              <a-input-number v-model="model.abovejs" placeholder="请输入0/-10(95)" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="-10/-20(75)" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="abovejes">
              <a-input-number v-model="model.abovejes" placeholder="请输入-10/-20(75)" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="-20/-40(50)" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="abovejss">
              <a-input-number v-model="model.abovejss" placeholder="请输入-20/-40(50)" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="-40/-60(-40)" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="abovejls">
              <a-input-number v-model="model.abovejls" placeholder="请输入-40/-60(-40)" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label=">-60(-60)" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="abovebhg">
              <a-input-number v-model="model.abovebhg" placeholder="请输入>-60(-60)" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="得分" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="scores">
              <a-input v-model="model.scores" placeholder="请输入得分"  ></a-input>
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
    name: 'ZtBhzjckhForm',
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
          add: "/ztbhzjckh/ztBhzjckh/add",
          edit: "/ztbhzjckh/ztBhzjckh/edit",
          queryById: "/ztbhzjckh/ztBhzjckh/queryById"
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