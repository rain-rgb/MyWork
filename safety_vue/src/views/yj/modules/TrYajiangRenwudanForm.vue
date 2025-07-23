<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form :form="form" slot="detail">
        <a-row>
          <a-row>
            <a-col :span="12">
              <a-form-item label="设备名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <j-search-select-tag  placeholder="请选择设备名称" v-decorator="['shebeibianhao', validatorRules.shebeibianhao]" :dictOptions="dictOption">
                </j-search-select-tag>
                {{ selectValue }}
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="请选择施工部位" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <JSelectDqProjName  v-decorator="['sgbwuuid', validatorRules.sgbwuuid]" ::multi="false"  />
              </a-form-item>
            </a-col>
<!--            <a-col :span="12">-->
<!--              <a-form-item label="任务单号" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--                <a-input v-decorator="['uuid']" placeholder="请输入任务单号"  ></a-input>-->
<!--              </a-form-item>-->
<!--            </a-col>-->
          </a-row>

<!--          <a-col :span="24">-->
<!--            <a-form-item label="所属组织机构" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--              <a-input v-decorator="['sysOrgCode']" placeholder="请输入所属组织机构"  ></a-input>-->
<!--            </a-form-item>-->
<!--          </a-col>-->
          <a-row>
            <a-col :span="24">
              <a-form-item label="工程名称" :labelCol="labelCol1" :wrapperCol="wrapperCol1">
                <a-input v-decorator="['projectname']" placeholder="请输入工程名称" :disabled="true"></a-input>
              </a-form-item>
            </a-col>
            <a-col :span="24">
              <a-form-item label="施工部位" :labelCol="labelCol1" :wrapperCol="wrapperCol1">
                <a-input v-decorator="['sgbwname']" placeholder="请输入施工部位名称" :disabled="true"></a-input>
              </a-form-item>
            </a-col>
          </a-row>
<!--          <a-row>-->
<!--            <a-col :span="12">-->
<!--              <a-form-item label="梁场" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--                <a-input v-decorator="['girderplace']" placeholder="请输入梁场"  ></a-input>-->
<!--              </a-form-item>-->
<!--            </a-col>-->
<!--            <a-col :span="12">-->
<!--              <a-form-item label="构件名称（梁名称）" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--                <a-input v-decorator="['component']" placeholder="请输入构件名称（梁名称）"  ></a-input>-->
<!--              </a-form-item>-->
<!--            </a-col>-->
<!--          </a-row>-->
          <a-row>
<!--            <a-col :span="12">-->
<!--              <a-form-item label="台座" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--                <a-input v-decorator="['pedestal']" placeholder="请输入台座"  ></a-input>-->
<!--              </a-form-item>-->
<!--            </a-col>-->
            <a-col :span="12">
              <a-form-item label="压浆日期" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-date-picker placeholder="请选择压浆日期" v-decorator="['yjdate']" style="width: 100%" :trigger-change="true" />
              </a-form-item>
            </a-col>
          </a-row>

<!--          <a-col :span="24">-->
<!--            <a-form-item label="压浆任务状态码：0：未使用  1：已使用" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--              <a-input v-decorator="['status']" placeholder="请输入压浆任务状态码：0：未使用  1：已使用"  ></a-input>-->
<!--            </a-form-item>-->
<!--          </a-col>-->

<!--          <a-col :span="24">-->
<!--            <a-form-item label="组织机构" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--              <a-input v-decorator="['departid']" placeholder="请输入组织机构"  ></a-input>-->
<!--            </a-form-item>-->
<!--          </a-col>-->
<!--          <a-col :span="24">-->
<!--            <a-form-item label="orgcode" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--              <a-input v-decorator="['orgcode']" placeholder="请输入orgcode"  ></a-input>-->
<!--            </a-form-item>-->
<!--          </a-col>-->
<!--          <a-col :span="24">-->
<!--            <a-form-item label="departname" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--              <a-input v-decorator="['departname']" placeholder="请输入departname"  ></a-input>-->
<!--            </a-form-item>-->
<!--          </a-col>-->
<!--          <a-col :span="24">-->
<!--            <a-form-item label="创建时间" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--              <j-date placeholder="请选择创建时间" v-decorator="['createTime']" :trigger-change="true" style="width: 100%" />-->
<!--            </a-form-item>-->
<!--          </a-col>-->
<!--          <a-col :span="24">-->
<!--            <a-form-item label="修改时间" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--              <j-date placeholder="请选择修改时间" v-decorator="['updateTime']" :trigger-change="true" style="width: 100%" />-->
<!--            </a-form-item>-->
<!--          </a-col>-->
<!--          <a-col :span="24">-->
<!--            <a-form-item label="创建人" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--              <a-input v-decorator="['createBy']" placeholder="请输入创建人"  ></a-input>-->
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
  import JSelectDqProjName from '@comp/jeecgbiz/JselectDqProjName'
  import { usershebeiList } from '@api/api'
  import Vue from 'vue'

  export default {
    name: 'TrYajiangRenwudanForm',
    components: {
      JFormContainer,
      JDate,
      JSelectDqProjName
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
        selectValue:'',
        dictOption: [],
        form: this.$form.createForm(this),
        model: {},
        labelCol: {
          xs: { span: 24 },
          sm: { span: 6 },
        },
        wrapperCol: {
          xs: { span: 24 },
          sm: { span: 16 },
        },
        labelCol1: {
          xs: { span: 24 },
          sm: { span: 3 },
        },
        wrapperCol1: {
          xs: { span: 24 },
          sm: { span: 20 },
        },
        confirmLoading: false,
        validatorRules: {
          shebeibianhao: {
            rules: [{
              required: true, message: '请选择设备!'
            }]
          },
          sgbwuuid:{
            rules: [{
              required: true, message: '请选择施工部位!'
            }]
          }
        },
        url: {
          add: "/sys/sysDepartproject47/yjrenwudanadd",
          edit: "/sys/sysDepartproject47/yjrenwudanedit",
          queryById: "/yajiangrenwudan/trYajiangRenwudan/queryById"
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
      this.shebeiList();
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
          this.form.setFieldsValue(pick(this.model,'uuid','projectname','girderplace','component','sgbwuuid','sgbwname','pedestal','status','yjdate','departid','orgcode','departname','shebeibianhao','sysOrgCode','createTime','updateTime','createBy'))
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
      shebeiList:function (){
        var sys_depart_orgcode=Vue.ls.get('SYS_DEPART_ORGCODE');
        usershebeiList({
          sys_depart_orgcode:sys_depart_orgcode,
          sbtypes:'10'
        }).then(res=>{
          //console.log(res)
          this.dictOption=[];
          let result=res.result;
          result.forEach(re=>{
            this.dictOption.push({text:re.sbname,value:re.sbjno})
          })

        })
      },
      popupCallback(row){
        this.form.setFieldsValue(pick(row,'uuid','projectname','girderplace','component','sgbwuuid','sgbwname','pedestal','status','yjdate','departid','orgcode','departname','shebeibianhao','sysOrgCode','createTime','updateTime','createBy'))
      },
    }
  }
</script>