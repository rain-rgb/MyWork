<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <a-row>
            <a-col :span="12">
              <a-form-model-item label="设备名称" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="shebeino">
                <j-search-select-tag placeholder="请选择设备名称"  v-model="model.shebeino" :dictOptions="dictOption" >
                </j-search-select-tag>
              </a-form-model-item>
            </a-col>
            <a-col :span="12">
              <a-form-model-item label="设计桩长" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="designdep">
                <a-input v-model="model.designdep" placeholder="请输入设计桩长(m)"  ></a-input>
              </a-form-model-item>
            </a-col>
          </a-row>
          <a-row>
            <a-col :span="12">
              <a-form-model-item label="当前桩长" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="curdep">
                <a-input v-model="model.curdep" placeholder="请输入当前桩长度(m)"  ></a-input>
              </a-form-model-item>
            </a-col>
            <a-col :span="12">
              <a-form-model-item label="离地桩长" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="grounddep">
                <a-input v-model="model.grounddep" placeholder="请输入离地桩长(m)"  ></a-input>
              </a-form-model-item>
            </a-col>
          </a-row>
          <a-row>
            <a-col :span="12">
              <a-form-model-item label="最大垂直度" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="pileY">
                <a-input v-model="model.pileY" placeholder="请输入最大垂直度(%)"  ></a-input>
              </a-form-model-item>
            </a-col>
            <a-col :span="12">
              <a-form-model-item label="最大压桩力" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="pileUpress">
                <a-input v-model="model.pileUpress" placeholder="请输入最大压桩力(KN)"  ></a-input>
              </a-form-model-item>
            </a-col>
          </a-row>
          <a-row>
            <a-col :span="12">
              <a-form-model-item label="最大夹持力" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="pileDpress">
                <a-input v-model="model.pileDpress" placeholder="请输入最大夹持力(KN)"  ></a-input>
              </a-form-model-item>
            </a-col>
            <a-col :span="12">
              <a-form-model-item label="平均桩压力" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="pileAveupress">
                <a-input v-model="model.pileAveupress" placeholder="请输入平均桩压力(KN)"  ></a-input>
              </a-form-model-item>
            </a-col>
          </a-row>
          <a-row>
            <a-col :span="12">
              <a-form-model-item label="平均夹持力" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="pileAvedpress">
                <a-input v-model="model.pileAvedpress" placeholder="请输入平均夹持力(KN)"  ></a-input>
              </a-form-model-item>
            </a-col>
            <a-col :span="12">
              <a-form-model-item label="开始时间" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="pileStarttime">
                <a-input v-model="model.pileStarttime" placeholder="请输入开始时间"  ></a-input>
              </a-form-model-item>
            </a-col>
          </a-row>
          <a-row>
            <a-col :span="12">
              <a-form-model-item label="数据时间" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="datatime">
                <j-date placeholder="请选择数据时间" v-model="model.datatime"  style="width: 100%" />
              </a-form-model-item>
            </a-col>
            <a-col :span="12">
              <a-form-model-item label="速度" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="speed">
                <a-input v-model="model.speed" placeholder="请输入速度"  ></a-input>
              </a-form-model-item>
            </a-col>
          </a-row>
          <a-row>
            <a-col :span="12">
              <a-form-model-item label="接桩时间" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="piletime">
                <a-input v-model="model.piletime" placeholder="请输入接桩时间"  ></a-input>
              </a-form-model-item>
            </a-col>
            <a-col :span="12">
              <a-form-model-item label="有效桩长" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="pileEffdep">
                <a-input v-model="model.pileEffdep" placeholder="请输入有效桩长(m)"  ></a-input>
              </a-form-model-item>
            </a-col>
          </a-row>
          <a-row>
            <a-col :span="12">
              <a-form-model-item label="时长" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="pileWorktime">
                <a-input v-model="model.pileWorktime" placeholder="请输入时长"  ></a-input>
              </a-form-model-item>
            </a-col>
            <a-col :span="12">
              <a-form-model-item label="打桩次数" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="times">
                <a-input-number v-model="model.times" placeholder="请输入打桩次数" style="width: 100%" />
              </a-form-model-item>
            </a-col>
          </a-row>
          <a-col :span="24">
            <a-form-model-item label="工作状态" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="worksta">
              <a-input v-model="model.worksta" placeholder="请输入工作状态"  ></a-input>
            </a-form-model-item>
          </a-col>
        </a-row>
      </a-form-model>
    </j-form-container>
  </a-spin>
</template>

<script>

  import { httpAction, getAction } from '@/api/manage'
  import { validateDuplicateValue } from '@/utils/util'
  import JFormContainer from '@/components/jeecg/JFormContainer'
  import JDate from '@/components/jeecg/JDate'
  import JselectDqDepart from '@/components/jeecgbiz/JselectDqDepart'
  import {usershebeiList} from "@api/api";
  import { SYS_DEPART_ORGCODE } from '@/store/mutation-types'
  import Vue from 'vue'

  export default {
    name: 'DevicePipepileRealdataForm',
    components: {
      JselectDqDepart,
      JFormContainer,
      JDate
    },
    props: {
      //表单禁用
      disabled: {
        type: Boolean,
        default: false,
        required: false
      }
    },
    data () {
      return {
        dictOption: [],
        selectValue: '',
        model:{
         },
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
          add: "/devicepipepilerealdata/devicePipepileRealdata/add",
          edit: "/devicepipepilerealdata/devicePipepileRealdata/edit",
          queryById: "/devicepipepilerealdata/devicePipepileRealdata/queryById"
        }
      }
    },
    watch:{
      'model.orgcode'(){
        this.shebeiList();
      }
    },
    computed: {
      formDisabled(){
        return this.disabled
      },
    },
    created () {
       //备份model原始值
      this.modelDefault = JSON.parse(JSON.stringify(this.model));
      this.shebeiList();
    },
    methods: {
      shebeiList:function (){
        var sys_depart_orgcode=Vue.ls.get('SYS_DEPART_ORGCODE');
        if(this.model.orgcode !== null){
          sys_depart_orgcode = this.model.orgcode
        }
        usershebeiList({
          sys_depart_orgcode:sys_depart_orgcode,
          sbtypes:'61'
        }).then(res=>{
          this.dictOption=[];
          let result=res.result;
          result.forEach(re=>{
            this.dictOption.push({text:re.sbname,value:re.sbjno})
          })
          //console.log(res)
        })
      },
      add () {
        this.edit(this.modelDefault);
      },
      edit (record) {
        this.model = Object.assign({}, record);
        this.visible = true;
      },
      submitForm () {
        const that = this;
        // 触发表单验证
        this.$refs.form.validate(valid => {
          if (valid) {
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
            httpAction(httpurl,this.model,method).then((res)=>{
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
    }
  }
</script>