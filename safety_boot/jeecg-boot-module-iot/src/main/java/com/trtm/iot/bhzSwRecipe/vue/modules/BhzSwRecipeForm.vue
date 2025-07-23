<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form :form="form" slot="detail">
        <a-row>
          <a-col :span="24">
            <a-form-item label="llysb" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['llysb']" placeholder="请输入llysb"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="设备编号" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['shebeibianhao']" placeholder="请输入设备编号"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="配比名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['lilunname']" placeholder="请输入配比名称"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="施工部位" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['llbuwei']" placeholder="请输入施工部位"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="录入时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-date placeholder="请选择录入时间" v-decorator="['llshijian', validatorRules.llshijian]" :trigger-change="true" style="width: 100%" />
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="默认" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="['llmoren']" placeholder="请输入默认" style="width: 100%" />
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="备注" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['llbeizhu']" placeholder="请输入备注"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="llkd" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['llkd']" placeholder="请输入llkd"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="llhd" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['llhd']" placeholder="请输入llhd"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="llmd" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['llmd']" placeholder="请输入llmd"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="混合料类型" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['hhllx']" placeholder="请输入混合料类型"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="生产配合比审批状态" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['phbsp']" placeholder="请输入生产配合比审批状态"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="工程名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['gcmc']" placeholder="请输入工程名称"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="配合比编号" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['phbid']" placeholder="请输入配合比编号"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="组织机构id" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['departid']" placeholder="请输入组织机构id"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="唯一id" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['zhuziid']" placeholder="请输入唯一id"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="是否删除" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['isdel']" placeholder="请输入是否删除"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="时间戳" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['ts']" placeholder="请输入时间戳"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="项目id" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['projectid']" placeholder="请输入项目id"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col v-if="showFlowSubmitButton" :span="24" style="text-align: center">
            <a-button @click="submitForm">提 交</a-button>
          </a-col>
        </a-row>
      </a-form>
    </j-form-container>
  </a-spin>
</template>

<script>

  import { httpAction, getAction } from '@/api/manage'
  import pick from 'lodash.pick'
  import { validateDuplicateValue } from '@/utils/util'
  import JFormContainer from '@/components/jeecg/JFormContainer'
  import JDate from '@/components/jeecg/JDate'

  export default {
    name: 'BhzSwRecipeForm',
    components: {
      JFormContainer,
      JDate,
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
        model: {},
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
          llshijian: {
            rules: [
              { required: true, message: '请输入录入时间!'},
            ]
          },
        },
        url: {
          add: "/bhzSwRecipe/bhzSwRecipe/add",
          edit: "/bhzSwRecipe/bhzSwRecipe/edit",
          queryById: "/bhzSwRecipe/bhzSwRecipe/queryById"
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
          this.form.setFieldsValue(pick(this.model,'llysb','shebeibianhao','lilunname','llbuwei','llshijian','llmoren','llbeizhu','llkd','llhd','llmd','hhllx','phbsp','gcmc','phbid','departid','zhuziid','isdel','ts','projectid'))
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
        this.form.setFieldsValue(pick(row,'llysb','shebeibianhao','lilunname','llbuwei','llshijian','llmoren','llbeizhu','llkd','llhd','llmd','hhllx','phbsp','gcmc','phbid','departid','zhuziid','isdel','ts','projectid'))
      },
    }
  }
</script>
