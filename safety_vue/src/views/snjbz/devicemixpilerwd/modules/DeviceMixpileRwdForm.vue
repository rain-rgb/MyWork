<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
<!--          <a-col :span="24">-->
<!--            <a-form-model-item label="软基工单号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="rjrwd">-->
<!--              <a-input v-model="model.rjrwd" placeholder="请输入软基工单号"  ></a-input>-->
<!--            </a-form-model-item>-->
<!--          </a-col>-->
<!--          <a-col :span="24">-->
<!--            <a-form-model-item label="组织机构" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="orgcode">-->
<!--              <a-input v-model="model.orgcode" placeholder="请输入组织机构"  ></a-input>-->
<!--            </a-form-model-item>-->
<!--          </a-col>-->
          <a-col :span="24">
            <a-form-model-item label="组织机构" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="orgcode">
              <JselectDqDepart v-model="model.orgcode" multi  />
              <!--                <a-input v-model="model.orgcode" placeholder="请输入组织机构"  ></a-input>-->
            </a-form-model-item>
          </a-col>
          <a-col :span="24" >
            <a-form-item label="设备名称" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="设备"  required>
              <j-search-select-tag placeholder="请选择设备名称"  v-model="model.shebeino"  :dictOptions="dictOption" >
              </j-search-select-tag>
              {{ selectValue }}
            </a-form-item>
          </a-col>
          <a-col :span="24" v-has="'rwd:lc'">
            <a-form-model-item label="里程" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="mileage">
              <a-input v-model="model.mileage" placeholder="请输入里程"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="选择里程" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="mileageid">
              <JSelectDqProjName v-model="model.mileageid" ::multi="false"/>
            </a-form-item>
          </a-col>
<!--          <a-col :span="24">-->
<!--            <a-form-model-item label="里程id" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="mileageid">-->
<!--              <a-input v-model="model.mileageid" placeholder="请输入里程id"  ></a-input>-->
<!--            </a-form-model-item>-->
<!--          </a-col>-->
          <a-col :span="24">
            <a-form-model-item label="设计桩基数" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="descount">
              <a-input-number v-model="model.descount" placeholder="请输入设计桩基数" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="开工日期" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="starttime" required  >
              <j-date placeholder="请选择开工日期" v-model="model.starttime"  style="width: 100%" :showTime="true" dateFormat="YYYY-MM-DD" />
            </a-form-model-item>
          </a-col>
<!--          <a-col :span="24">-->
<!--            <a-form-model-item label="设计每根桩长" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="deslen">-->
<!--              <a-input v-model="model.deslen" placeholder="请输入设计每根桩长"  ></a-input>-->
<!--            </a-form-model-item>-->
<!--          </a-col>-->
<!--          <a-col :span="24">-->
<!--            <a-form-model-item label="通知人" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="msgperson">-->
<!--              <a-input v-model="model.msgperson" placeholder="请输入通知人"  ></a-input>-->
<!--            </a-form-model-item>-->
<!--          </a-col>-->
          <a-col :span="24">
            <a-form-item label="通知人" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-dict-select-tag type="list" v-model="model.msgperson"  dictCode="bhz_phone,names,uid,phonesname='11'"
                                 dictTable="bhz_phone" placeholder="请选择通知人"/>
            </a-form-item>
          </a-col>

          <a-col :span="24">
            <a-form-model-item label="截止日期" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="endtime">
              <j-date placeholder="请选择截止日期" v-model="model.endtime"  style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="备注" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="other">
              <a-input v-model="model.other" placeholder="请输入备注"  ></a-input>
            </a-form-model-item>
          </a-col>
<!--          <a-col :span="24">-->
<!--            <a-form-model-item label="工单状态，0：未开始，1：进行中；2：已完成，3：滞后" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="status">-->
<!--              <a-input-number v-model="model.status" placeholder="请输入工单状态，0：未开始，1：进行中；2：已完成，3：滞后" style="width: 100%" />-->
<!--            </a-form-model-item>-->
<!--          </a-col>-->
<!--          <a-col :span="24">-->
<!--            <a-form-model-item label="是否进行统计" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="alt">-->
<!--              <a-input-number v-model="model.alt" placeholder="请输入是否进行统计" style="width: 100%" />-->
<!--            </a-form-model-item>-->
<!--          </a-col>-->
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
  import JSelectDqProjName from '@comp/jeecgbiz/JselectDqProjName'
  import Vue from 'vue'

  export default {
    name: 'DeviceMixpileRwdForm',
    components: {
      JselectDqDepart,
      JFormContainer,
      JDate,
      JSelectDqProjName
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
          add: "/sys/sysDepartproject47/rjrwdadd47",
          edit: "/devicemixpilerwd/deviceMixpileRwd/edit",
          queryById: "/devicemixpilerwd/deviceMixpileRwd/queryById"
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
      }

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
          sbtypes:'16,19,53,54,61'
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