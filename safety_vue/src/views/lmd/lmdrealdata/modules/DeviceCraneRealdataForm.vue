<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form :form="form" slot="detail">
        <a-row>
          <a-row>
            <a-col :span="12">
              <a-form-item label="设备名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-decorator="['deviceCode_dictText']" placeholder="请输入设备名称"  ></a-input>
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="风速" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input-number v-decorator="['windSpeed']" placeholder="请输入风速" style="width: 100%" />
              </a-form-item>
            </a-col>
<!--            <a-col :span="12">-->
<!--              <a-form-item label="支腿偏差报警" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--                <a-input-number v-decorator="['bigcarAlm']" placeholder="请输入支腿偏差报警" style="width: 100%" />-->
<!--              </a-form-item>-->
<!--            </a-col>-->
            <!--            <a-col :span="12">-->
            <!--              <a-form-item label="设备类型" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
            <!--                <a-input-number v-decorator="['deviceType_dictText']" placeholder="请输入设备类型2桥门机3架桥机" style="width: 100%" />-->
            <!--              </a-form-item>-->
            <!--            </a-col>-->
          </a-row>

          <a-row>
            <a-col :span="12">
              <a-form-item label="大车行程" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input-number v-decorator="['bigCarroute']" placeholder="请输入大车行程" style="width: 100%" />
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="数据上传时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <j-date placeholder="请选择数据上传时间" v-decorator="['cranedate']" :trigger-change="true" style="width: 100%" />
              </a-form-item>
            </a-col>
          </a-row>

          <a-row>
            <a-col :span="12">
              <a-form-item label="主钩高度" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input-number v-decorator="['mainHookheight']" placeholder="请输入主钩高度" style="width: 100%" />
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="预留副钩高度" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input-number v-decorator="['reservedVicehookheight']" placeholder="请输入预留副钩高度" style="width: 100%" />
              </a-form-item>
            </a-col>
          </a-row>

          <a-row>
            <a-col :span="12">
              <a-form-item label="主钩吊重" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input-number v-decorator="['mainHookload']" placeholder="请输入主钩吊重" style="width: 100%" />
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="预留副钩吊重" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input-number v-decorator="['reservedVicehookload']" placeholder="请输入预留副钩吊重" style="width: 100%" />
              </a-form-item>
            </a-col>
          </a-row>

          <a-row>
            <a-col :span="12">
              <a-form-item label="小车行程" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input-number v-decorator="['smallCarroute']" placeholder="请输入小车行程" style="width: 100%" />
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="预留小车2行程" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input-number v-decorator="['reservedSmallcarroute']" placeholder="请输入预留小车2行程" style="width: 100%" />
              </a-form-item>
            </a-col>
          </a-row>

          <a-row>
            <a-col :span="12">
              <a-form-item label="预留模拟量" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input-number v-decorator="['reservedAnalogquantityone']" placeholder="请输入预留模拟量" style="width: 100%" />
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="预留模拟量" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input-number v-decorator="['reservedAnalogquantitytwo']" placeholder="请输入预留模拟量" style="width: 100%" />
              </a-form-item>
            </a-col>
          </a-row>

          <a-row>
<!--            <a-col :span="12">-->
<!--              <a-form-item label="主钩载荷状态" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--                <a-input-number v-decorator="['mainHookstatus']" placeholder="请输入主钩载荷状态" style="width: 100%" />-->
<!--              </a-form-item>-->
<!--            </a-col>-->
          </a-row>

<!--          <a-row>-->
<!--            <a-col :span="12">-->
<!--              <a-form-item label="预留副钩载荷状态" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--                <a-input-number v-decorator="['reservedVicehookstatus']" placeholder="请输入预留副钩载荷状态" style="width: 100%" />-->
<!--              </a-form-item>-->
<!--            </a-col>-->
<!--            <a-col :span="12">-->
<!--              <a-form-item label="风速报警" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--                <a-input-number v-decorator="['windSpeedalarm']" placeholder="请输入风速报警" style="width: 100%" />-->
<!--              </a-form-item>-->
<!--            </a-col>-->
<!--          </a-row>-->

          <a-row>
            <a-col :span="12">
              <a-form-item label="大车左限位" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input-number v-decorator="['bigCarleftlimit']" placeholder="请输入大车左限位" style="width: 100%" />
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="大车右限位" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input-number v-decorator="['bigCarrightlimit']" placeholder="请输入大车右限位" style="width: 100%" />
              </a-form-item>
            </a-col>
          </a-row>

          <a-row>
            <a-col :span="12">
              <a-form-item label="小车前限位" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input-number v-decorator="['smallCarfrontlimit']" placeholder="请输入小车前限位" style="width: 100%" />
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="小车后限位" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input-number v-decorator="['smallCarbacklimit']" placeholder="请输入小车后限位" style="width: 100%" />
              </a-form-item>
            </a-col>
          </a-row>

          <a-row>
            <a-col :span="12">
              <a-form-item label="预留小车2前限位" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input-number v-decorator="['reservedSmallcarfrontlimit']" placeholder="请输入预留小车2前限位" style="width: 100%" />
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="预留小车2后限位" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input-number v-decorator="['reservedSmallcarbacklimit']" placeholder="请输入预留小车2后限位" style="width: 100%" />
              </a-form-item>
            </a-col>
          </a-row>

          <a-row>
            <a-col :span="12">
              <a-form-item label="吊钩上限位" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input-number v-decorator="['hookUplimit']" placeholder="请输入吊钩上限位" style="width: 100%" />
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="预留副吊钩上限位" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input-number v-decorator="['reservedVicehookuplimit']" placeholder="请输入预留副吊钩上限位" style="width: 100%" />
              </a-form-item>
            </a-col>
          </a-row>

          <a-row>
            <a-col :span="12">
              <a-form-item label="门限位" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input-number v-decorator="['doorLimit']" placeholder="请输入门限位" style="width: 100%" />
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="小车1制动器1" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input-number v-decorator="['smallCar1brake1']" placeholder="请输入小车1制动器1" style="width: 100%" />
              </a-form-item>
            </a-col>
<!--            <a-col :span="12">-->
<!--              <a-form-item label="抗风防滑状态" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--                <a-input-number v-decorator="['windAntiskidstatus']" placeholder="请输入抗风防滑状态" style="width: 100%" />-->
<!--              </a-form-item>-->
<!--            </a-col>-->
          </a-row>

          <a-row>
<!--            <a-col :span="12">-->
<!--              <a-form-item label="电缆卷筒状态" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--                <a-input-number v-decorator="['wireDrumstatus']" placeholder="请输入电缆卷筒状态" style="width: 100%" />-->
<!--              </a-form-item>-->
<!--            </a-col>-->

          </a-row>

          <a-row>
            <a-col :span="12">
              <a-form-item label="小车1制动器2" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input-number v-decorator="['smallCar1brake2']" placeholder="请输入小车1制动器2" style="width: 100%" />
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="小车2制动器1" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input-number v-decorator="['smallCar2brake1']" placeholder="请输入小车2制动器1" style="width: 100%" />
              </a-form-item>
            </a-col>
          </a-row>

          <a-row>
            <a-col :span="12">
              <a-form-item label="小车2制动器2" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input-number v-decorator="['smallCar2brake2']" placeholder="请输入小车2制动器2" style="width: 100%" />
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="大车支腿偏差" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input-number v-decorator="['bigCarlegdeviation']" placeholder="请输入大车支腿偏差" style="width: 100%" />
              </a-form-item>
            </a-col>
          </a-row>

          <a-row>
            <a-col :span="12">
              <a-form-item label="累计时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input-number v-decorator="['allTime']" placeholder="请输入累计时间" style="width: 100%" />
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="循环使用次数" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input-number v-decorator="['allTimes']" placeholder="请输入循环使用次数" style="width: 100%" />
              </a-form-item>
            </a-col>
          </a-row>

          <!--          <a-row>-->

          <!--            <a-col :span="12">-->
          <!--              <a-form-item label="预留" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
          <!--                <a-input-number v-decorator="['reservedOne']" placeholder="请输入预留" style="width: 100%" />-->
          <!--              </a-form-item>-->
          <!--            </a-col>-->
          <!--          </a-row>-->

          <!--          <a-col :span="24">-->
          <!--            <a-form-item label="预留" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
          <!--              <a-input-number v-decorator="['reservedTwo']" placeholder="请输入预留" style="width: 100%" />-->
          <!--            </a-form-item>-->
          <!--          </a-col>-->
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
    name: 'DeviceCraneRealdataForm',
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
          add: "/devicecranerealdata/deviceCraneRealdata/add",
          edit: "/devicecranerealdata/deviceCraneRealdata/edit",
          queryById: "/devicecranerealdata/deviceCraneRealdata/queryById"
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
          this.form.setFieldsValue(pick(this.model,'deviceCode_dictText','deviceType','bigCarroute','cranedate','mainHookheight','reservedVicehookheight','mainHookload','reservedVicehookload','smallCarroute','reservedSmallcarroute','windSpeed','reservedAnalogquantityone','reservedAnalogquantitytwo','mainHookstatus','reservedVicehookstatus','windSpeedalarm','bigCarleftlimit','bigCarrightlimit','smallCarfrontlimit','smallCarbacklimit','reservedSmallcarfrontlimit','reservedSmallcarbacklimit','hookUplimit','reservedVicehookuplimit','doorLimit','windAntiskidstatus','wireDrumstatus','smallCar1brake1','smallCar1brake2','smallCar2brake1','smallCar2brake2','bigCarlegdeviation','allTime','allTimes','bigcarAlm','reservedOne','reservedTwo'))
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
        this.form.setFieldsValue(pick(row,'deviceCode_dictText','deviceType','bigCarroute','cranedate','mainHookheight','reservedVicehookheight','mainHookload','reservedVicehookload','smallCarroute','reservedSmallcarroute','windSpeed','reservedAnalogquantityone','reservedAnalogquantitytwo','mainHookstatus','reservedVicehookstatus','windSpeedalarm','bigCarleftlimit','bigCarrightlimit','smallCarfrontlimit','smallCarbacklimit','reservedSmallcarfrontlimit','reservedSmallcarbacklimit','hookUplimit','reservedVicehookuplimit','doorLimit','windAntiskidstatus','wireDrumstatus','smallCar1brake1','smallCar1brake2','smallCar2brake1','smallCar2brake2','bigCarlegdeviation','allTime','allTimes','bigcarAlm','reservedOne','reservedTwo'))
      },
    }
  }
</script>