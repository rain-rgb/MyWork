<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <a-col :span="24">
            <a-form-model-item label="设备唯一ID" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="devkey">
              <a-input-number v-model="model.devkey" placeholder="请输入设备唯一ID" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="设备名" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="devname">
              <a-input v-model="model.devname" placeholder="请输入设备名"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="0为模拟量，1为开关量" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="devtype">
              <a-input-number v-model="model.devtype" placeholder="请输入0为模拟量，1为开关量" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="设备地址(也是设备注册时的编号)" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="devaddr">
              <a-input v-model="model.devaddr" placeholder="请输入设备地址(也是设备注册时的编号)"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="模拟量一名称" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="devtempname">
              <a-input v-model="model.devtempname" placeholder="请输入模拟量一名称"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="模拟量一的值" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="devtempvalue">
              <a-input v-model="model.devtempvalue" placeholder="请输入模拟量一的值"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="模拟量二名称" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="devhuminame">
              <a-input v-model="model.devhuminame" placeholder="请输入模拟量二名称"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="模拟量二值" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="devhumivalue">
              <a-input v-model="model.devhumivalue" placeholder="请输入模拟量二值"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="设备状态  flase表示离线，true 表示在线" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="devstatus">
              <a-input v-model="model.devstatus" placeholder="请输入设备状态  flase表示离线，true 表示在线"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="设备所处纬度" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="devlng">
              <a-input-number v-model="model.devlng" placeholder="请输入设备所处纬度" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="设备所处经度" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="devlat">
              <a-input-number v-model="model.devlat" placeholder="请输入设备所处经度" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="模拟量一报警状态（0 表示不报警，1 表示超上限，2 表示超下限）" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="tempstatus">
              <a-input-number v-model="model.tempstatus" placeholder="请输入模拟量一报警状态（0 表示不报警，1 表示超上限，2 表示超下限）" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="模拟量二报警状态（0 表示不报警，1 表示超上限，2 表示超下限）" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="humistatus">
              <a-input-number v-model="model.humistatus" placeholder="请输入模拟量二报警状态（0 表示不报警，1 表示超上限，2 表示超下限）" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="模拟量一相关参数设置标志（0 表示不具备设置权限，1 表示有设置权限）" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="devdatatype1">
              <a-input v-model="model.devdatatype1" placeholder="请输入模拟量一相关参数设置标志（0 表示不具备设置权限，1 表示有设置权限）"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="模拟量二相关参数设置标志（0 表示不具备设置权限，1 表示有设置权限）" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="devdatatype2">
              <a-input v-model="model.devdatatype2" placeholder="请输入模拟量二相关参数设置标志（0 表示不具备设置权限，1 表示有设置权限）"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="设备节点号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="devpos">
              <a-input-number v-model="model.devpos" placeholder="请输入设备节点号" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="数据保存的时间点" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="timevalue">
              <j-date placeholder="请选择数据保存的时间点" v-model="model.timevalue"  style="width: 100%" />
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

  export default {
    name: 'DeviceForm',
    components: {
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
           devkey: [
              { required: true, message: '请输入设备唯一ID!'},
           ],
        },
        url: {
          add: "/device/device/add",
          edit: "/device/device/edit",
          queryById: "/device/device/queryById"
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