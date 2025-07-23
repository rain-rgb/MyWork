<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form :form="form" slot="detail">
        <a-row>
<!--          <a-row>-->
<!--            <a-col :span="12">-->
<!--              <a-form-item label="recordid" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--                <a-input-number v-decorator="['recordid']" placeholder="请输入recordid" style="width: 100%" />-->
<!--              </a-form-item>-->
<!--            </a-col>-->
<!--            <a-col :span="12">-->
<!--              <a-form-item label="types" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--                <a-input-number v-decorator="['types']" placeholder="请输入types" style="width: 100%" />-->
<!--              </a-form-item>-->
<!--            </a-col>-->
<!--          </a-row>-->

          <a-row>
            <a-col :span="12">
              <a-form-item label="设备名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-decorator="['shebeino_dictText']" placeholder="请输入设备名称"  ></a-input>
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="姓名" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-decorator="['names']" placeholder="请输入姓名"  ></a-input>
              </a-form-item>
            </a-col>
          </a-row>

          <a-row>
            <a-col :span="12">
              <a-form-item label="部门名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-decorator="['departname']" placeholder="请输入部门名称"  ></a-input>
              </a-form-item>
            </a-col>
<!--            <a-col :span="12">-->
<!--              <a-form-item label="openid" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--                <a-input v-decorator="['openid']" placeholder="请输入openid"  ></a-input>-->
<!--              </a-form-item>-->
<!--            </a-col>-->
            <a-col :span="12">
              <a-form-item label="开门时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <j-date placeholder="请选择开门时间" v-decorator="['opentime']" :trigger-change="true" style="width: 100%" />
              </a-form-item>
            </a-col>
          </a-row>

          <a-row>
<!--            <a-col :span="12">-->
<!--              <a-form-item label="doorid" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--                <a-input v-decorator="['doorid']" placeholder="请输入doorid"  ></a-input>-->
<!--              </a-form-item>-->
<!--            </a-col>-->
            <a-col :span="12">
              <a-form-item label="备注" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-decorator="['info']" placeholder="请输入备注"  ></a-input>
              </a-form-item>
            </a-col>
          </a-row>

<!--          <a-row>-->
<!--            <a-col :span="12">-->
<!--              <a-form-item label="adminid" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--                <a-input-number v-decorator="['adminid']" placeholder="请输入adminid" style="width: 100%" />-->
<!--              </a-form-item>-->
<!--            </a-col>-->
<!--            <a-col :span="12">-->
<!--              <a-form-item label="pic" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--                <a-input v-decorator="['pic']" placeholder="请输入pic"  ></a-input>-->
<!--              </a-form-item>-->
<!--            </a-col>-->
<!--          </a-row>-->

<!--          <a-row>-->
<!--            <a-col :span="12">-->
<!--              <a-form-item label="adddate" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--                <j-date placeholder="请选择adddate" v-decorator="['adddate']" :trigger-change="true" style="width: 100%" />-->
<!--              </a-form-item>-->
<!--            </a-col>-->
<!--            <a-col :span="12">-->
<!--              <a-form-item label="isoffline" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--                <a-input-number v-decorator="['isoffline']" placeholder="请输入isoffline" style="width: 100%" />-->
<!--              </a-form-item>-->
<!--            </a-col>-->
<!--          </a-row>-->

<!--          <a-row>-->
<!--            <a-col :span="12">-->
<!--              <a-form-item label="capturepic" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--                <a-input v-decorator="['capturepic']" placeholder="请输入capturepic"  ></a-input>-->
<!--              </a-form-item>-->
<!--            </a-col>-->
<!--            <a-col :span="12">-->
<!--              <a-form-item label="isopen" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--                <a-input-number v-decorator="['isopen']" placeholder="请输入isopen" style="width: 100%" />-->
<!--              </a-form-item>-->
<!--            </a-col>-->
<!--          </a-row>-->

<!--          <a-row>-->
<!--            <a-col :span="12">-->
<!--              <a-form-item label="serialno" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--                <a-input v-decorator="['serialno']" placeholder="请输入serialno"  ></a-input>-->
<!--              </a-form-item>-->
<!--            </a-col>-->
<!--            <a-col :span="12">-->
<!--              <a-form-item label="isupload" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--                <a-input-number v-decorator="['isupload']" placeholder="请输入isupload" style="width: 100%" />-->
<!--              </a-form-item>-->
<!--            </a-col>-->
<!--          </a-row>-->

<!--          <a-row>-->
<!--            <a-col :span="12">-->
<!--              <a-form-item label="uploaddate" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--                <j-date placeholder="请选择uploaddate" v-decorator="['uploaddate']" :trigger-change="true" style="width: 100%" />-->
<!--              </a-form-item>-->
<!--            </a-col>-->
<!--            <a-col :span="12">-->
<!--              <a-form-item label="温度" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--                <a-input v-decorator="['temperature']" placeholder="请输入温度"  ></a-input>-->
<!--              </a-form-item>-->
<!--            </a-col>-->
<!--          </a-row>-->

<!--          <a-row>-->
<!--            <a-col :span="12">-->
<!--              <a-form-item label="standard" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--                <a-input v-decorator="['standard']" placeholder="请输入standard"  ></a-input>-->
<!--              </a-form-item>-->
<!--            </a-col>-->
<!--            <a-col :span="12">-->
<!--              <a-form-item label="用户ID" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--                <a-input v-decorator="['cid']" placeholder="请输入用户ID"  ></a-input>-->
<!--              </a-form-item>-->
<!--            </a-col>-->
<!--          </a-row>-->

          <a-row>
<!--            <a-col :span="24">-->
<!--              <a-form-item label="部门ID" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--                <a-input v-decorator="['departmentid']" placeholder="请输入部门ID"  ></a-input>-->
<!--              </a-form-item>-->
<!--            </a-col>-->
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
    name: 'EntranceGuardRecordRealForm',
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
          add: "/entranceguardrecordreal/entranceGuardRecordReal/add",
          edit: "/entranceguardrecordreal/entranceGuardRecordReal/edit",
          queryById: "/entranceguardrecordreal/entranceGuardRecordReal/queryById"
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
          this.form.setFieldsValue(pick(this.model,'recordid','types','openid','opentime','doorid','info','adminid','pic','adddate','isoffline','capturepic','isopen','serialno','isupload','uploaddate','temperature','standard','cid','names','shebeino_dictText','departmentid','departname'))
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
        this.form.setFieldsValue(pick(row,'recordid','types','openid','opentime','doorid','info','adminid','pic','adddate','isoffline','capturepic','isopen','serialno','isupload','uploaddate','temperature','standard','cid','names','shebeino_dictText','departmentid','departname'))
      },
    }
  }
</script>