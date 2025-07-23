<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form :form="form" slot="detail">
        <a-row>
          <a-col :span="24">
            <a-form-item label="唯一ID" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['batchNo', validatorRules.batchNo]" placeholder="请输入唯一ID"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="设备编号" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['shebeiNo']" placeholder="请输入设备编号"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="施工部位" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['jobLocation']" placeholder="请输入施工部位"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="切面名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['sectionName']" placeholder="请输入切面名称"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="切面里程" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['sectionLength']" placeholder="请输入切面里程"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="切面宽度" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['sectionWidth']" placeholder="请输入切面宽度"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="开挖方式" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['excavationType']" placeholder="请输入开挖方式"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="围岩级别" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['wallRockLevel']" placeholder="请输入围岩级别"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="测点" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['meauringPoint']" placeholder="请输入测点"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="测量值（单位m）" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="['meauringNumber']" placeholder="请输入测量值（单位m）" style="width: 100%" />
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="单次变形量（单位mm）" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="['onceDeformation']" placeholder="请输入单次变形量（单位mm）" style="width: 100%" />
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="间隔时间  （单位天）" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="['intervalTime']" placeholder="请输入间隔时间  （单位天）" style="width: 100%" />
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="变形速率" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="['deformationRate']" placeholder="请输入变形速率" style="width: 100%" />
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="单次是否超标 (0:未超标，1:超标)" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="['onceStatus']" placeholder="请输入单次是否超标 (0:未超标，1:超标)" style="width: 100%" />
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="累计是否超标  (0:未超标，1:超标)" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="['accumulatedStatus']" placeholder="请输入累计是否超标  (0:未超标，1:超标)" style="width: 100%" />
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="累计变形量单位（mm）" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="['accumulatedDeformation']" placeholder="请输入累计变形量单位（mm）" style="width: 100%" />
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="是否判断超标并报警0为没有判断过1为已判断并且进行过了生产统计      40为数据异常" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="['alertstate']" placeholder="请输入是否判断超标并报警0为没有判断过1为已判断并且进行过了生产统计      40为数据异常" style="width: 100%" />
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="测量时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-date placeholder="请选择测量时间" v-decorator="['birthTime']" :trigger-change="true" style="width: 100%" />
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="数据上传时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-date placeholder="请选择数据上传时间" v-decorator="['uploadTime']" :trigger-change="true" style="width: 100%" />
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="状态" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="['isUse']" placeholder="请输入状态" style="width: 100%" />
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="报警级别(0,代表合格，1代表黄色报警，21代表红色报警)" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="['callLevel']" placeholder="请输入报警级别(0,代表合格，1代表黄色报警，21代表红色报警)" style="width: 100%" />
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="检测类型（0:速率，1累计）" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="['superviseType']" placeholder="请输入检测类型（0:速率，1累计）" style="width: 100%" />
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="callSum" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="['callSum']" placeholder="请输入callSum" style="width: 100%" />
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="间隔时间(距离初次测试时间)" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="['intervalFirstTime']" placeholder="请输入间隔时间(距离初次测试时间)" style="width: 100%" />
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="测量状态（0：正常 ,1：重新布设）" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['measuringStatus']" placeholder="请输入测量状态（0：正常 ,1：重新布设）"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="超标问题中文描述" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['problemRecord']" placeholder="请输入超标问题中文描述"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="临建再统计后的测点" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['measuringPointLj']" placeholder="请输入临建再统计后的测点"  ></a-input>
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
    name: 'WeiyanBaseForm',
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
          batchNo: {
            rules: [
              { required: true, message: '请输入唯一ID!'},
            ]
          },
        },
        url: {
          add: "/weiyan/weiyanBase/add",
          edit: "/weiyan/weiyanBase/edit",
          queryById: "/weiyan/weiyanBase/queryById"
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
          this.form.setFieldsValue(pick(this.model,'batchNo','shebeiNo','jobLocation','sectionName','sectionLength','sectionWidth','excavationType','wallRockLevel','meauringPoint','meauringNumber','onceDeformation','intervalTime','deformationRate','onceStatus','accumulatedStatus','accumulatedDeformation','alertstate','birthTime','uploadTime','isUse','callLevel','superviseType','callSum','intervalFirstTime','measuringStatus','problemRecord','measuringPointLj'))
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
        this.form.setFieldsValue(pick(row,'batchNo','shebeiNo','jobLocation','sectionName','sectionLength','sectionWidth','excavationType','wallRockLevel','meauringPoint','meauringNumber','onceDeformation','intervalTime','deformationRate','onceStatus','accumulatedStatus','accumulatedDeformation','alertstate','birthTime','uploadTime','isUse','callLevel','superviseType','callSum','intervalFirstTime','measuringStatus','problemRecord','measuringPointLj'))
      },
    }
  }
</script>