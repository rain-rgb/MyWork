<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form :form="form" slot="detail">
        <a-row>
<!--          <a-col :span="24">-->
<!--            <a-form-item label="文件全路径" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--              <a-input v-decorator="['filename']" placeholder="请输入文件全路径"  ></a-input>-->
<!--            </a-form-item>-->
<!--          </a-col>-->
          <a-col :span="24">
            <a-form-item label="设备名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="['imgtype_dictText']" placeholder="请输入设备名称" style="width: 100%" />
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="数据上传时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-date placeholder="请选择数据上传时间" v-decorator="['uploadtime']" :trigger-change="true" style="width: 100%" />
            </a-form-item>
          </a-col>

          <a-col :span="24">
            <a-form-item label="预警时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-date placeholder="请选择预警时间" v-decorator="['sendtime']" :trigger-change="true" style="width: 100%" />
            </a-form-item>
          </a-col>
<!--            <a-col :span="12">-->
<!--              <a-form-item label="预警发送状态" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--                <a-input-number v-decorator="['sendstatus']" placeholder="请输入预警发送状态" style="width: 100%" />-->
<!--              </a-form-item>-->
<!--            </a-col>-->

<!--          <a-col :span="24">-->
<!--            <a-form-item label="数据GUID" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--              <a-input v-decorator="['guid']" placeholder="请输入数据GUID"  ></a-input>-->
<!--            </a-form-item>-->
<!--          </a-col>-->

          <a-col :span="24">
            <a-form-item label="预警信息" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['alertmsg']" placeholder="请输入预警信息"  ></a-input>
            </a-form-item>
          </a-col>

          <viewer :images="detailList">
            <img
              v-for="(item,index) in detailList"
              :key="index"
              style="height:450px;width: 550px;margin: 5px 10px 5px 10px;justify-content:center"
              :src="['http://web.traiot.cn/' + item.icon] "
              alt="">
          </viewer>
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
    name: 'SafetyHelmetAlertForm',
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
        detailList:[],
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
          add: "/safetyhelmet/safetyHelmetAlert/add",
          edit: "/safetyhelmet/safetyHelmetAlert/edit",
          queryById: "/safetyhelmet/safetyHelmetAlert/queryById",
          list:"/safetyhelmet/safetyHelmetAlert/list1"
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
          this.form.setFieldsValue(pick(this.model,'filename','uploadtime','alertmsg','sendstatus','guid','sendtime','imgtype_dictText'))
          this.getData();
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
      getData:function (){//图片显示
        let params = {id:this.model.id}
        getAction(this.url.list,params).then((res)=> {
            this.detailList = [];
            if (res.success) {
              let data = res.result;
              console.log(data, "+______________________________+")
              if (data[0].filename !== null && data[0].filename !=='') {
                this.detailList.push({icon:data[0].filename})
              }
              console.log(this.detailList, "++++++++++++++++")
            }
          }
        );
      },
      popupCallback(row){
        this.form.setFieldsValue(pick(row,'filename','uploadtime','alertmsg','sendstatus','guid','sendtime','imgtype_dictText'))
      },
    }
  }
</script>