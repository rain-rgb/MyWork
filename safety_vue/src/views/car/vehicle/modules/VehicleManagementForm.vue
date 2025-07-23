<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <a-col :span="24">
            <a-form-item label="所属组织机构" :labelCol="labelCol" :wrapperCol="wrapperCol" >
              <JselectDqDepart v-model="model.sysOrgCode" ::multi="false"  />
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="车牌号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="numberPlate">
              <a-input v-model="model.numberPlate" placeholder="请输入车牌号"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="司机姓名" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="driverName">
              <a-input v-model="model.driverName" placeholder="请输入司机姓名"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="是否报警" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="overtimewarn">
              <j-dict-select-tag  v-model="model.overtimewarn"  dictCode="is_call" placeholder="请选择是否报警" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24" >
            <a-form-item label="运输超时通知号码组" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="warnphones">
              <j-dict-select-tag type="list" v-model="model.warnphones"   :trigger-change="true" dictCode="bhz_phone,names,uid,phonesname='20'" dictTable="bhz_phone" placeholder="请选择运输超时通知号码组"/>
            </a-form-item>
          </a-col>
        </a-row>
      </a-form-model>
    </j-form-container>
  </a-spin>
</template>

<script>

  import { httpAction, getAction } from '@/api/manage'
  import { validateDuplicateValue } from '@/utils/util'
  import JselectDqDepart from '@comp/jeecgbiz/JselectDqDepart'
  import { duplicateCheck } from '@api/api'

  export default {
    name: 'VehicleManagementForm',
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
          numberPlate:[{ required: false, message: '请输入车牌号!' },{validator: this.validatenumberPlate }],
        },
        url: {
          add: "/vehiclemanagement/vehicleManagement/add",
          edit: "/vehiclemanagement/vehicleManagement/edit",
          queryById: "/vehiclemanagement/vehicleManagement/queryById"
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
    },
    methods: {
      add () {
        this.edit(this.modelDefault);
      },
      edit (record) {
        this.model = Object.assign({}, record);
        this.visible = true;
      },
      validatenumberPlate(rule, value, callback){
        if(value && value.length>0){
          //校验授权标识是否存在
          var params = {
            tableName: 'vehicle_management',
            fieldName: 'number_plate',
            fieldVal: value,
            dataId: this.model.id
          };
          duplicateCheck(params).then((res) => {
            if (res.success) {
              callback()
            } else {
              callback("车牌号已存在!")
            }
          })
        }else{
          callback()
        }
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