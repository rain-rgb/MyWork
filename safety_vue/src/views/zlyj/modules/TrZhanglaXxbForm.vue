<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form :form="form" slot="detail">
        <a-row>
          <a-col :span="24">
            <a-form-item label="唯一码" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['syjid']" placeholder="请输入唯一码"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="工程名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['gcmc']" placeholder="请输入工程名称"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="预制梁场" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['yzlc']" placeholder="请输入预制梁场"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="构件编号" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['gjbh']" placeholder="请输入构件编号"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="设备编号" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['shebeibianhao']" placeholder="请输入设备编号"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="构件名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['gjmc']" placeholder="请输入构件名称"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="砼设计强度" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['snsjqd']" placeholder="请输入砼设计强度"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="施工时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['sgsj']" placeholder="请输入施工时间"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="砼试块强度" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['snskqd']" placeholder="请输入砼试块强度"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="张拉仪1编号" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['zly1']" placeholder="请输入张拉仪1编号"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="油表编号1" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['ybbh1']" placeholder="请输入油表编号1"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="标定日期1" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['bdrq1']" placeholder="请输入标定日期1"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="张拉仪2编号" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['zly2']" placeholder="请输入张拉仪2编号"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="油表编号2" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['ybbh2']" placeholder="请输入油表编号2"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="标定日期2" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['bdrq2']" placeholder="请输入标定日期2"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="承包单位,施工单位" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['cbunit']" placeholder="请输入承包单位,施工单位"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="监理单位" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['jlunit']" placeholder="请输入监理单位"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="土建合同号" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['htbh']" placeholder="请输入土建合同号"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="张拉参数u" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['zlcsu']" placeholder="请输入张拉参数u"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="张拉参数k" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['zlcsk']" placeholder="请输入张拉参数k"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="张拉参数ep" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['zlcsep']" placeholder="请输入张拉参数ep"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="张拉部位图片" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['zlbwpic']" placeholder="请输入张拉部位图片"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="跨梁" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['kualiang']" placeholder="请输入跨梁"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="伸长量计算式" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['scljss']" placeholder="请输入伸长量计算式"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="封锚情况描述" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['fmqkms']" placeholder="请输入封锚情况描述"  ></a-input>
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
            <a-form-item label="张拉任务单号" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['uuid']" placeholder="请输入张拉任务单号"  ></a-input>
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

  import { httpAction, getAction } from '@api/manage'
  import pick from 'lodash.pick'
  import { validateDuplicateValue } from '@/utils/util'
  import JFormContainer from '@comp/jeecg/JFormContainer'

  export default {
    name: 'TrZhanglaXxbForm',
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
          add: "/zhanglaxxb/trZhanglaXxb/add",
          edit: "/zhanglaxxb/trZhanglaXxb/edit",
          queryById: "/zhanglaxxb/trZhanglaXxb/queryById"
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
          this.form.setFieldsValue(pick(this.model,'syjid','gcmc','yzlc','gjbh','shebeibianhao','gjmc','snsjqd','sgsj','snskqd','zly1','ybbh1','bdrq1','zly2','ybbh2','bdrq2','cbunit','jlunit','htbh','zlcsu','zlcsk','zlcsep','zlbwpic','kualiang','scljss','fmqkms','memo','status','uuid'))
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
        this.form.setFieldsValue(pick(row,'syjid','gcmc','yzlc','gjbh','shebeibianhao','gjmc','snsjqd','sgsj','snskqd','zly1','ybbh1','bdrq1','zly2','ybbh2','bdrq2','cbunit','jlunit','htbh','zlcsu','zlcsk','zlcsep','zlbwpic','kualiang','scljss','fmqkms','memo','status','uuid'))
      },
    }
  }
</script>