<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form :form="form" slot="detail">
        <a-row>
          <a-row>
            <a-col :span="12">
              <a-form-item label="设备编号" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-decorator="['shebeibanhao_dictText']" placeholder="请输入设备编号"  ></a-input>
              </a-form-item>
            </a-col>
            <!--          <a-col :span="24">-->
            <!--            <a-form-item label="数据类标志" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
            <!--              <a-input-number v-decorator="['dataType']" placeholder="请输入数据类标志" style="width: 100%" />-->
            <!--            </a-form-item>-->
            <!--          </a-col>-->
            <a-col :span="12">
              <a-form-item label="1#螺纹钢拉力(T)" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input-number v-decorator="['tension1']" placeholder="请输入1#螺纹钢拉力(放大100倍单位T)(实际显示为30.00T)" style="width: 100%" />
              </a-form-item>
            </a-col>
          </a-row>

          <a-row>
            <a-col :span="12">
              <a-form-item label="2#螺纹钢拉力(T)" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input-number v-decorator="['tension2']" placeholder="请输入2#螺纹钢拉力(放大100倍单位T)(实际显示为30.00T)" style="width: 100%" />
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="3#螺纹钢拉力(T)" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input-number v-decorator="['tension3']" placeholder="请输入3#螺纹钢拉力(放大100倍单位T)(实际显示为30.00T)" style="width: 100%" />
              </a-form-item>
            </a-col>
          </a-row>

          <a-row>
            <a-col :span="12">
              <a-form-item label="4#螺纹钢拉力(T)" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input-number v-decorator="['tension4']" placeholder="请输入4#螺纹钢拉力(放大100倍单位T)(实际显示为30.00T)" style="width: 100%" />
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="下支撑横梁水平度(度)" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input-number v-decorator="['levelness1']" placeholder="请输入下支撑横梁水平度（放大10倍单位度）(实际显示为1.0度)" style="width: 100%" />
              </a-form-item>
            </a-col>
          </a-row>

          <a-row>
            <a-col :span="12">
              <a-form-item label="下支撑横梁水平度(度)" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input-number v-decorator="['levelness2']" placeholder="请输入下支撑横梁水平度（放大10倍单位度）(实际显示为1.0度)" style="width: 100%" />
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="1#螺纹钢是否超重" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input-number v-decorator="['overweight1_dictText']" placeholder="请输入1#螺纹钢是否超重  0  正常  1  超重" style="width: 100%" />
              </a-form-item>
            </a-col>
          </a-row>

          <a-row>
            <a-col :span="12">
              <a-form-item label="2#螺纹钢是否超重" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input-number v-decorator="['overweight2_dictText']" placeholder="请输入2#螺纹钢是否超重 0  正常  1  超重" style="width: 100%" />
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="3#螺纹钢是否超重" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input-number v-decorator="['overweight3_dictText']" placeholder="请输入3#螺纹钢是否超重 0  正常  1  超重" style="width: 100%" />
              </a-form-item>
            </a-col>
          </a-row>

          <a-row>
            <a-col :span="12">
              <a-form-item label="4#螺纹钢是否超重" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input-number v-decorator="['overweight4_dictText']" placeholder="请输入4#螺纹钢是否超重 0  正常 1  超重" style="width: 100%" />
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="挂篮前端下沉量(mm)" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input-number v-decorator="['basket']" placeholder="请输入挂篮前端下沉量（放大10倍单位mm）" style="width: 100%" />
              </a-form-item>
            </a-col>
          </a-row>

          <a-row>
            <a-col :span="12">
              <a-form-item label="风速(m/s)" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input-number v-decorator="['windspeed']" placeholder="请输入风速（放大10倍 单位m/s）" style="width: 100%" />
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="风速报警" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input-number v-decorator="['windspeedwarn_dictText']" placeholder="请输入风速报警0---正常  1---报警" style="width: 100%" />
              </a-form-item>
            </a-col>
          </a-row>

          <a-row>
            <a-col :span="12">
              <a-form-item label="下托梁有效长度" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input-number v-decorator="['reservedOne']" placeholder="请输入下托梁有效长度" style="width: 100%" />
              </a-form-item>
            </a-col>
            <!--          <a-col :span="24">-->
            <!--            <a-form-item label="预留" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
            <!--              <a-input-number v-decorator="['reservedTwo']" placeholder="请输入预留" style="width: 100%" />-->
            <!--            </a-form-item>-->
            <!--          </a-col>-->
            <!--          <a-col :span="24">-->
            <!--            <a-form-item label="段结束符" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
            <!--              <a-input-number v-decorator="['sf']" placeholder="请输入段结束符" style="width: 100%" />-->
            <!--            </a-form-item>-->
            <!--          </a-col>-->
            <a-col :span="12">
              <a-form-item label="数据时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <j-date placeholder="请选择数据时间" v-decorator="['datatime']" :trigger-change="true" style="width: 100%" date-format="YYYY-MM-DD HH:mm:ss"/>
              </a-form-item>
            </a-col>
          </a-row>

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
    name: 'GualanBaseRealForm',
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
          add: "/gualanreal/gualanBaseReal/add",
          edit: "/gualanreal/gualanBaseReal/edit",
          queryById: "/gualanreal/gualanBaseReal/queryById"
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
          this.form.setFieldsValue(pick(this.model,'shebeibanhao_dictText','tension1','tension2','tension3','tension4','levelness1','levelness2','overweight1_dictText','overweight2_dictText','overweight3_dictText','overweight4_dictText','basket','windspeed','windspeedwarn_dictText','reservedOne','datatime'))
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
        this.form.setFieldsValue(pick(row,'shebeibanhao_dictText','tension1','tension2','tension3','tension4','levelness1','levelness2','overweight1_dictText','overweight2_dictText','overweight3_dictText','overweight4_dictText','basket','windspeed','windspeedwarn_dictText','reservedOne','datatime'))
      },
    }
  }
</script>