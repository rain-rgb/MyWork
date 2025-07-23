<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <a-col :span="24">
            <a-form-model-item label="设备编号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="shebeino">
              <a-input v-model="model.shebeino" placeholder="请输入设备编号"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="桩号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="pileno">
              <a-input v-model="model.pileno" placeholder="请输入桩号"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="接桩序号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="partPilec">
              <a-input v-model="model.partPilec" placeholder="请输入接桩序号"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="时长" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="pileWorktime">
              <a-input v-model="model.pileWorktime" placeholder="请输入时长"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="接桩时间" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="piletime">
              <a-input v-model="model.piletime" placeholder="请输入接桩时间"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="平均速度(cm/min)" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="speed">
              <a-input v-model="model.speed" placeholder="请输入平均速度(cm/min)"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="最大垂直度(%)" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="pileY">
              <a-input v-model="model.pileY" placeholder="请输入最大垂直度(%)"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="最大压桩力(KN)" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="pileUpress">
              <a-input v-model="model.pileUpress" placeholder="请输入最大压桩力(KN)"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="最大夹持力(KN)" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="pileDpress">
              <a-input v-model="model.pileDpress" placeholder="请输入最大夹持力(KN)"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="桩长(m)" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="pileDep">
              <a-input v-model="model.pileDep" placeholder="请输入桩长(m)"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="数据时间" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="datatime">
              <j-date placeholder="请选择数据时间" v-model="model.datatime"  style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="时间戳" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="ts">
              <a-input v-model="model.ts" placeholder="请输入时间戳"  ></a-input>
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
    name: 'DevicePipepileReportForm',
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
          add: "/devicepipepilereport/devicePipepileReport/add",
          edit: "/devicepipepilereport/devicePipepileReport/edit",
          queryById: "/devicepipepilereport/devicePipepileReport/queryById"
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