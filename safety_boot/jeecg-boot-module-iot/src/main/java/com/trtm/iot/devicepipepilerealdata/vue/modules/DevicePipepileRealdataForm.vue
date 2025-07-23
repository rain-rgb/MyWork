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
            <a-form-model-item label="设计桩长(m)" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="designdep">
              <a-input v-model="model.designdep" placeholder="请输入设计桩长(m)"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="当前桩长度(m)" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="curdep">
              <a-input v-model="model.curdep" placeholder="请输入当前桩长度(m)"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="离地桩长(m)" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="grounddep">
              <a-input v-model="model.grounddep" placeholder="请输入离地桩长(m)"  ></a-input>
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
            <a-form-model-item label="平均桩压力(KN)" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="pileAveupress">
              <a-input v-model="model.pileAveupress" placeholder="请输入平均桩压力(KN)"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="平均夹持力(KN)" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="pileAvedpress">
              <a-input v-model="model.pileAvedpress" placeholder="请输入平均夹持力(KN)"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="开始时间" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="pileStarttime">
              <a-input v-model="model.pileStarttime" placeholder="请输入开始时间"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="数据时间" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="datatime">
              <j-date placeholder="请选择数据时间" v-model="model.datatime"  style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="速度" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="speed">
              <a-input v-model="model.speed" placeholder="请输入速度"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="接桩时间" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="piletime">
              <a-input v-model="model.piletime" placeholder="请输入接桩时间"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="有效桩长(m)" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="pileEffdep">
              <a-input v-model="model.pileEffdep" placeholder="请输入有效桩长(m)"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="时长" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="pileWorktime">
              <a-input v-model="model.pileWorktime" placeholder="请输入时长"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="打桩次数" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="times">
              <a-input-number v-model="model.times" placeholder="请输入打桩次数" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="工作状态" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="worksta">
              <a-input v-model="model.worksta" placeholder="请输入工作状态"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="时间戳" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="ts">
              <a-input v-model="model.ts" placeholder="请输入时间戳"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="桩号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="pileno">
              <a-input v-model="model.pileno" placeholder="请输入桩号"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="经度" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="ltdfloat">
              <a-input v-model="model.ltdfloat" placeholder="请输入经度"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="纬度" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="lgdfloat">
              <a-input v-model="model.lgdfloat" placeholder="请输入纬度"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="里程" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="mileage">
              <a-input v-model="model.mileage" placeholder="请输入里程"  ></a-input>
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
    name: 'DevicePipepileRealdataForm',
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
          add: "/devicepipepilerealdata/devicePipepileRealdata/add",
          edit: "/devicepipepilerealdata/devicePipepileRealdata/edit",
          queryById: "/devicepipepilerealdata/devicePipepileRealdata/queryById"
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