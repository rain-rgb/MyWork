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
            <a-form-item label="1:厚度、2:波形、3:剖面、4:网格、5:规范" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="['type', validatorRules.type]" placeholder="请输入1:厚度、2:波形、3:剖面、4:网格、5:规范" style="width: 100%" />
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="检测唯一ID" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['testid']" placeholder="请输入检测唯一ID"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="现浇板, 现浇墙, 现浇梁, 现浇柱, 预制板, 预制墙板, 预制梁, 预制柱, 预制桁架, 其他" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['targettype']" placeholder="请输入现浇板, 现浇墙, 现浇梁, 现浇柱, 预制板, 预制墙板, 预制梁, 预制柱, 预制桁架, 其他"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="测点总数" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="['zonecount']" placeholder="请输入测点总数" style="width: 100%" />
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="合格率" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="['passrate']" placeholder="请输入合格率" style="width: 100%" />
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="检测位置" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['checklocation']" placeholder="请输入检测位置"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="检测人" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['testerid']" placeholder="请输入检测人"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="检测时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-date placeholder="请选择检测时间" v-decorator="['checktime']" :trigger-change="true" style="width: 100%" />
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="主筋直径mm" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="['masterdiameter']" placeholder="请输入主筋直径mm" style="width: 100%" />
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="主筋间距mm" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="['masterspacing']" placeholder="请输入主筋间距mm" style="width: 100%" />
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="设计厚度mm" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="['designthickness']" placeholder="请输入设计厚度mm" style="width: 100%" />
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="纵向设计厚度" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="['zdesignthickness']" placeholder="请输入纵向设计厚度" style="width: 100%" />
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="箍筋直径mm" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="['subdiameter']" placeholder="请输入箍筋直径mm" style="width: 100%" />
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="箍筋间距mm" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="['subspacing']" placeholder="请输入箍筋间距mm" style="width: 100%" />
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="曲面直径" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="['curveddiameter']" placeholder="请输入曲面直径" style="width: 100%" />
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="任务单NO" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['code']" placeholder="请输入任务单NO"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="设备编号" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['shebeiNo']" placeholder="请输入设备编号"  ></a-input>
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
    name: 'TrGangjinbhcMForm',
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
          type: {
            rules: [
              { required: true, message: '请输入1:厚度、2:波形、3:剖面、4:网格、5:规范!'},
            ]
          },
        },
        url: {
          add: "/trgangjinbhcm/trGangjinbhcM/add",
          edit: "/trgangjinbhcm/trGangjinbhcM/edit",
          queryById: "/trgangjinbhcm/trGangjinbhcM/queryById"
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
          this.form.setFieldsValue(pick(this.model,'point','type','testid','targettype','zonecount','passrate','checklocation','testerid','checktime','masterdiameter','masterspacing','designthickness','zdesignthickness','subdiameter','subspacing','curveddiameter','code','shebeiNo'))
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
        this.form.setFieldsValue(pick(row,'point','type','testid','targettype','zonecount','passrate','checklocation','testerid','checktime','masterdiameter','masterspacing','designthickness','zdesignthickness','subdiameter','subspacing','curveddiameter','code','shebeiNo'))
      },
    }
  }
</script>