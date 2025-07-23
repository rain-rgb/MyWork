<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form :form="form" slot="detail">
        <a-row>
          <a-col :span="24">
            <a-form-item label="构件序号" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['point']" placeholder="请输入构件序号"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="检测时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-date placeholder="请选择检测时间" v-decorator="['checktime']" :trigger-change="true" style="width: 100%" />
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="检测位置" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['checklocation']" placeholder="请输入检测位置"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="检测ID" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['testid']" placeholder="请输入检测ID"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="检测人" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['testerpeople']" placeholder="请输入检测人"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="构件部位" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['place']" placeholder="请输入构件部位"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="强度等级" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['strength']" placeholder="请输入强度等级"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="测区数量" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="['testingcount']" placeholder="请输入测区数量" style="width: 100%" />
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="检测面" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['detectionsurface']" placeholder="请输入检测面"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="浇筑面 0：浇筑侧面 1：浇筑底面" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="['pouringsurface']" placeholder="请输入浇筑面 0：浇筑侧面 1：浇筑底面" style="width: 100%" />
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="标准碳化深度值" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="['carbonizedepth']" placeholder="请输入标准碳化深度值" style="width: 100%" />
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="检测角度" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['detectionangle']" placeholder="请输入检测角度"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="是否泵送   是/否" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['ispumping']" placeholder="请输入是否泵送   是/否"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="检测依据" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['detectionstandard']" placeholder="请输入检测依据"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="浇筑日期" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-date placeholder="请选择浇筑日期" v-decorator="['pouringdate']" :trigger-change="true" style="width: 100%" />
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="测试最小数值" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="['testingmin']" placeholder="请输入测试最小数值" style="width: 100%" />
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="测区平均数值" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="['testingaverage']" placeholder="请输入测区平均数值" style="width: 100%" />
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="标准差" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="['standarddeviation']" placeholder="请输入标准差" style="width: 100%" />
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="推定值" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="['estimatedvalue']" placeholder="请输入推定值" style="width: 100%" />
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="测区最小换算值" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['showmin']" placeholder="请输入测区最小换算值"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="测区平均换算值" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['showaverage']" placeholder="请输入测区平均换算值"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="标准差" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['showstandarddev']" placeholder="请输入标准差"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="推定值" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['showestimatedval']" placeholder="请输入推定值"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="任务单编号" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['code']" placeholder="请输入任务单编号"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="试验设备编号" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['shebeiNo']" placeholder="请输入试验设备编号"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="项目名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['projectname']" placeholder="请输入项目名称"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="施工部位" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['sgbw']" placeholder="请输入施工部位"  ></a-input>
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
    name: 'TrHnthtMForm',
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
        },
        url: {
          add: "/trhnthtm/trHnthtM/add",
          edit: "/trhnthtm/trHnthtM/edit",
          queryById: "/trhnthtm/trHnthtM/queryById"
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
          this.form.setFieldsValue(pick(this.model,'point','checktime','checklocation','testid','testerpeople','place','strength','testingcount','detectionsurface','pouringsurface','carbonizedepth','detectionangle','ispumping','detectionstandard','pouringdate','testingmin','testingaverage','standarddeviation','estimatedvalue','showmin','showaverage','showstandarddev','showestimatedval','code','shebeiNo','projectname','sgbw'))
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
        this.form.setFieldsValue(pick(row,'point','checktime','checklocation','testid','testerpeople','place','strength','testingcount','detectionsurface','pouringsurface','carbonizedepth','detectionangle','ispumping','detectionstandard','pouringdate','testingmin','testingaverage','standarddeviation','estimatedvalue','showmin','showaverage','showstandarddev','showestimatedval','code','shebeiNo','projectname','sgbw'))
      },
    }
  }
</script>