<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form :form="form" slot="detail">
        <a-row>
          <a-col :span="24">
            <a-form-item label="设备编号" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['shebeibianhao']" placeholder="请输入设备编号"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="工程名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['projectName']" placeholder="请输入工程名称"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="施工地点" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['jobLocation']" placeholder="请输入施工地点"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="浇筑部位" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['poureLocation']" placeholder="请输入浇筑部位"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="配方号" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['formulaNo']" placeholder="请输入配方号"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="强度等级" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['strengthRank']" placeholder="请输入强度等级"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="初级超标多少盘" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="['primaryDish']" placeholder="请输入初级超标多少盘" style="width: 100%" />
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="初级处置多少盘" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="['primaryHandle']" placeholder="请输入初级处置多少盘" style="width: 100%" />
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="初级处置百分点" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="['primaryPercent']" placeholder="请输入初级处置百分点" style="width: 100%" />
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="中级超标多少盘" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="['middleDish']" placeholder="请输入中级超标多少盘" style="width: 100%" />
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="中级处置多少盘" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="['middleHandle']" placeholder="请输入中级处置多少盘" style="width: 100%" />
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="中级处置百分点" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="['middlePercent']" placeholder="请输入中级处置百分点" style="width: 100%" />
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="高级超标多少盘" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="['advancedDish']" placeholder="请输入高级超标多少盘" style="width: 100%" />
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="高级处置多少盘" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="['advancedHandle']" placeholder="请输入高级处置多少盘" style="width: 100%" />
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="高级处置百分点" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="['advancedPercent']" placeholder="请输入高级处置百分点" style="width: 100%" />
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="总盘数" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="['allDish']" placeholder="请输入总盘数" style="width: 100%" />
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="总超标盘数" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="['allOverproofDish']" placeholder="请输入总超标盘数" style="width: 100%" />
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="总处理盘数" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="['allHandleDish']" placeholder="请输入总处理盘数" style="width: 100%" />
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="总体超标处置率" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="['allPercent']" placeholder="请输入总体超标处置率" style="width: 100%" />
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="方量" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="['estimateNumber']" placeholder="请输入方量" style="width: 100%" />
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="统计时间，以天为单位" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-date placeholder="请选择统计时间，以天为单位" v-decorator="['statisticsTime']" :trigger-change="true" style="width: 100%" />
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
    name: 'BhzLqStatisticsForm',
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
          add: "/lqbhzStatistics/bhzLqStatistics/add",
          edit: "/lqbhzStatistics/bhzLqStatistics/edit",
          queryById: "/lqbhzStatistics/bhzLqStatistics/queryById"
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
          this.form.setFieldsValue(pick(this.model,'shebeibianhao','projectName','jobLocation','poureLocation','formulaNo','strengthRank','primaryDish','primaryHandle','primaryPercent','middleDish','middleHandle','middlePercent','advancedDish','advancedHandle','advancedPercent','allDish','allOverproofDish','allHandleDish','allPercent','estimateNumber','statisticsTime'))
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
        this.form.setFieldsValue(pick(row,'shebeibianhao','projectName','jobLocation','poureLocation','formulaNo','strengthRank','primaryDish','primaryHandle','primaryPercent','middleDish','middleHandle','middlePercent','advancedDish','advancedHandle','advancedPercent','allDish','allOverproofDish','allHandleDish','allPercent','estimateNumber','statisticsTime'))
      },
    }
  }
</script>