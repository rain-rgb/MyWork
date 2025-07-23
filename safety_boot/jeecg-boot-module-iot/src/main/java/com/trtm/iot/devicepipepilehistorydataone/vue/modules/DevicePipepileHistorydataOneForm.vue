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
            <a-form-model-item label="桩号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="pileNo">
              <a-input v-model="model.pileNo" placeholder="请输入桩号"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="施工长度(m)" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="pileRealdep">
              <a-input v-model="model.pileRealdep" placeholder="请输入施工长度(m)"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="成桩时间" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="pileWorktime">
              <a-input v-model="model.pileWorktime" placeholder="请输入成桩时间"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="最大垂直度(%)" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="pileY">
              <a-input v-model="model.pileY" placeholder="请输入最大垂直度(%)"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="结束时间" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="pileTime">
              <a-input v-model="model.pileTime" placeholder="请输入结束时间"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="开始时间" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="pileStarttime">
              <a-input v-model="model.pileStarttime" placeholder="请输入开始时间"  ></a-input>
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
            <a-form-model-item label="平均速度(cm/min)" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="pileSpeed">
              <a-input v-model="model.pileSpeed" placeholder="请输入平均速度(cm/min)"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="接桩次数" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="times">
              <a-input-number v-model="model.times" placeholder="请输入接桩次数" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="设计桩长" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="pileDesigndep">
              <a-input v-model="model.pileDesigndep" placeholder="请输入设计桩长"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="关联软基任务单 device_mixpile_rwd" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="rjrwd">
              <a-input v-model="model.rjrwd" placeholder="请输入关联软基任务单 device_mixpile_rwd"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="唯一码" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="uuid">
              <a-input v-model="model.uuid" placeholder="请输入唯一码"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="里程" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="pileMileage">
              <a-input v-model="model.pileMileage" placeholder="请输入里程"  ></a-input>
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
          <a-col :span="24">
            <a-form-model-item label="桩经度" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="pileLgd">
              <a-input v-model="model.pileLgd" placeholder="请输入桩经度"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="桩纬度" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="pileLtd">
              <a-input v-model="model.pileLtd" placeholder="请输入桩纬度"  ></a-input>
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
    name: 'DevicePipepileHistorydataOneForm',
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
          add: "/devicepipepilehistorydataone/devicePipepileHistorydataOne/add",
          edit: "/devicepipepilehistorydataone/devicePipepileHistorydataOne/edit",
          queryById: "/devicepipepilehistorydataone/devicePipepileHistorydataOne/queryById"
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