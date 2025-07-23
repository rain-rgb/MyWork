<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <a-col :span="24">
            <a-form-model-item label="组织机构" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="orgcode">
              <JselectDqDepart v-model="model.orgcode" multi  />
              <!--                <a-input v-model="model.orgcode" placeholder="请输入组织机构"  ></a-input>-->
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="摄像头名称" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="monitorname">
              <a-input v-model="model.monitorname" placeholder="请输入摄像头名称"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="序列号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="serialnumber">
              <a-input v-model="model.serialnumber" placeholder="请输入序列号" disabled ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="验证码" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="verificationcode">
              <a-input v-model="model.verificationcode" placeholder="请输入验证码" disabled  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="设备名称" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="remark">
              <j-search-select-tag placeholder="请选择设备名称"  v-model="model.remark" :dictOptions="dictOption" >
              </j-search-select-tag>
              {{ selectValue }}
<!--              <a-input v-model="model.remark" placeholder="请输入设备名称"  ></a-input>-->
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="排序号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="ordernum">
              <a-input-number v-model="model.ordernum" placeholder="请输入排序号" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="通道" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="channel">
              <a-input-number v-model="model.channel" placeholder="请输入通道" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="应用场景" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="usetype">
<!--              <a-input-number v-model="model.usetype" placeholder="请输入1.实验室；2.拌合站；3.钢筋场;4.梁场；5、现场" style="width: 100%" />-->
              <j-dict-select-tag  v-model="model.usetype"  dictCode="usetype" placeholder="请选择应用场景" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="经度" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="longitude">
              <a-input v-model="model.longitude" placeholder="请输入经度"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="纬度" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="latitude">
              <a-input v-model="model.latitude" placeholder="请输入纬度"  ></a-input>
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
  import JselectDqDepart from "@comp/jeecgbiz/JselectDqDepart";
  import { SYS_DEPART_ORGCODE } from '@/store/mutation-types';
  import { usershebeiList } from '@api/api'
  import Vue from 'vue'

  export default {
    name: 'MonitorForm',
    components: {
      JselectDqDepart
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
        selectValue: '',
        asyncSelectValue: '',
        dictOption: [],
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
          add: "/monitor/monitor/add",
          edit: "/monitor/monitor/edit",
          queryById: "/monitor/monitor/queryById"
        }
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
        usershebeiList({
          sys_depart_orgcode:sys_depart_orgcode,
          sbtypes:'21,23,50,16,19,53,9,10'
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