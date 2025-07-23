<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form :form="form" slot="detail">
        <a-row>
          <a-col :span="24">
            <a-form-item label="唯一码" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['fguid']" placeholder="请输入唯一码"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="主键" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['syjid']" placeholder="请输入主键"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="设备编号" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['shebeibianhao']" placeholder="请输入设备编号"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="张拉时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['zlsj']" placeholder="请输入张拉时间"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="钢束编号" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['gsbh']" placeholder="请输入钢束编号"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="holeid" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['holeid']" placeholder="请输入holeid"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="钢束股数" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['gsgs']" placeholder="请输入钢束股数"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="弹性模量" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['txml']" placeholder="请输入弹性模量"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="设计张拉力 KN" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['sjzll']" placeholder="请输入设计张拉力 KN"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="回弹量mm" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['htl']" placeholder="请输入回弹量mm"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="总伸长量mm" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['zscl']" placeholder="请输入总伸长量mm"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="理论伸长量mm" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['llscl']" placeholder="请输入理论伸长量mm"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="允许偏差值" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['yxpc']" placeholder="请输入允许偏差值"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="延伸量误差%" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['sclper']" placeholder="请输入延伸量误差%"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="断丝及处理情况" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['wt']" placeholder="请输入断丝及处理情况"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="处理情况" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['clqk']" placeholder="请输入处理情况"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="备注" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['memo']" placeholder="请输入备注"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="完成状态(0为未完成,状态为1代表" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['status']" placeholder="请输入完成状态(0为未完成,状态为1代表"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="是否合格" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['hege']" placeholder="请输入是否合格"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="预张拉百分比" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['yzlb']" placeholder="请输入预张拉百分比"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="初张拉百分比" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['czlb']" placeholder="请输入初张拉百分比"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="终张拉百分比" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['zzlb']" placeholder="请输入终张拉百分比"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="记录时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['jlsj']" placeholder="请输入记录时间"  ></a-input>
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
    name: 'TrZhanglaMForm',
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
        },
        url: {
          add: "/zhanglam/trZhanglaM/add",
          edit: "/zhanglam/trZhanglaM/edit",
          queryById: "/zhanglam/trZhanglaM/queryById"
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
          this.form.setFieldsValue(pick(this.model,'fguid','syjid','shebeibianhao','zlsj','gsbh','holeid','gsgs','txml','sjzll','htl','zscl','llscl','yxpc','sclper','wt','clqk','memo','status','hege','yzlb','czlb','zzlb','jlsj'))
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
        this.form.setFieldsValue(pick(row,'fguid','syjid','shebeibianhao','zlsj','gsbh','holeid','gsgs','txml','sjzll','htl','zscl','llscl','yxpc','sclper','wt','clqk','memo','status','hege','yzlb','czlb','zzlb','jlsj'))
      },
    }
  }
</script>