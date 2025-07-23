<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form :form="form" slot="detail">
        <a-row>
          <a-col :span="24">
            <a-form-item label="sectid" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['sectid']" placeholder="请输入sectid"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="wbsIsbn" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['wbsIsbn']" placeholder="请输入wbsIsbn"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="wbsParent" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['wbsParent']" placeholder="请输入wbsParent"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="wbsName" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['wbsName']" placeholder="请输入wbsName"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="wbsMoney" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="['wbsMoney']" placeholder="请输入wbsMoney" style="width: 100%" />
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="wbsLevel" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="['wbsLevel']" placeholder="请输入wbsLevel" style="width: 100%" />
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="hasChild" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="['hasChild']" placeholder="请输入hasChild" style="width: 100%" />
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="deleFlag" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="['deleFlag']" placeholder="请输入deleFlag" style="width: 100%" />
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="wbsUnit" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['wbsUnit']" placeholder="请输入wbsUnit"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="wbsAmount" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['wbsAmount']" placeholder="请输入wbsAmount"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="wbsAmount2" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['wbsAmount2']" placeholder="请输入wbsAmount2"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="wbsZhBegi" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['wbsZhBegi']" placeholder="请输入wbsZhBegi"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="wbsZhEnd" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['wbsZhEnd']" placeholder="请输入wbsZhEnd"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="wbsMapno" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['wbsMapno']" placeholder="请输入wbsMapno"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="wbsType1" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['wbsType1']" placeholder="请输入wbsType1"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="wbsType2" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['wbsType2']" placeholder="请输入wbsType2"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="wbsType3" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['wbsType3']" placeholder="请输入wbsType3"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="sortorder" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="['sortorder']" placeholder="请输入sortorder" style="width: 100%" />
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="1 代表已经更新 0 代表未更新" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="['status']" placeholder="请输入1 代表已经更新 0 代表未更新" style="width: 100%" />
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="ids" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="['ids', validatorRules.ids]" placeholder="请输入ids" style="width: 100%" />
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

  export default {
    name: 'PmsComWbsForm',
    components: {
      JFormContainer,
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
          ids: {
            rules: [
              { required: true, message: '请输入ids!'},
            ]
          },
        },
        url: {
          add: "/pmscomwbs/pmsComWbs/add",
          edit: "/pmscomwbs/pmsComWbs/edit",
          queryById: "/pmscomwbs/pmsComWbs/queryById"
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
          this.form.setFieldsValue(pick(this.model,'sectid','wbsIsbn','wbsParent','wbsName','wbsMoney','wbsLevel','hasChild','deleFlag','wbsUnit','wbsAmount','wbsAmount2','wbsZhBegi','wbsZhEnd','wbsMapno','wbsType1','wbsType2','wbsType3','sortorder','status','ids'))
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
        this.form.setFieldsValue(pick(row,'sectid','wbsIsbn','wbsParent','wbsName','wbsMoney','wbsLevel','hasChild','deleFlag','wbsUnit','wbsAmount','wbsAmount2','wbsZhBegi','wbsZhEnd','wbsMapno','wbsType1','wbsType2','wbsType3','sortorder','status','ids'))
      },
    }
  }
</script>