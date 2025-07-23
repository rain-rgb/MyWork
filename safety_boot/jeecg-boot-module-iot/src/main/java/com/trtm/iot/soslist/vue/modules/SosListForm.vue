<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <a-col :span="24">
            <a-form-model-item label="报警记录id" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="sId">
              <a-input-number v-model="model.sId" placeholder="请输入报警记录id" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="用户id" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="userId">
              <a-input-number v-model="model.userId" placeholder="请输入用户id" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="用户名" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="userName">
              <a-input v-model="model.userName" placeholder="请输入用户名"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="设备id" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="deviceId">
              <a-input-number v-model="model.deviceId" placeholder="请输入设备id" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="location" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="location">
              <a-input v-model="model.location" placeholder="请输入location"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="纬度坐标" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="xPoint">
              <a-input v-model="model.xPoint" placeholder="请输入纬度坐标"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="经度坐标" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="yPoint">
              <a-input v-model="model.yPoint" placeholder="请输入经度坐标"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="报警时间" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="cTime">
              <j-date placeholder="请选择报警时间" v-model="model.cTime"  style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="报警类型" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="sosType">
              <a-input v-model="model.sosType" placeholder="请输入报警类型"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="status" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="status">
              <a-input v-model="model.status" placeholder="请输入status"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="adminId" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="adminId">
              <a-input v-model="model.adminId" placeholder="请输入adminId"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="报警消息" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="msg">
              <a-input v-model="model.msg" placeholder="请输入报警消息"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="所在楼层" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="floor">
              <a-input v-model="model.floor" placeholder="请输入所在楼层"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="报警图片路径" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="imgurl">
              <a-input v-model="model.imgurl" placeholder="请输入报警图片路径"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="手机号码" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="mobile">
              <a-input v-model="model.mobile" placeholder="请输入手机号码"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="isReal" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="isReal">
              <a-input v-model="model.isReal" placeholder="请输入isReal"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="rTime" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="rTime">
              <j-date placeholder="请选择rTime" v-model="model.rTime"  style="width: 100%" />
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
    name: 'SosListForm',
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
        },
        url: {
          add: "/soslist/sosList/add",
          edit: "/soslist/sosList/edit",
          queryById: "/soslist/sosList/queryById"
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