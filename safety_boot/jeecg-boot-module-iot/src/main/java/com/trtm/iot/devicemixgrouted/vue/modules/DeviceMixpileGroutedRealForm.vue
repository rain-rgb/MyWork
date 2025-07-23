<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <a-col :span="24">
            <a-form-model-item label="设备编号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="sbbh">
              <a-input v-model="model.sbbh" placeholder="请输入设备编号"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="里程" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="pileMileage">
              <a-input v-model="model.pileMileage" placeholder="请输入里程"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="桩号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="pileNo">
              <a-input v-model="model.pileNo" placeholder="请输入桩号"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="设计深度" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="pileDesigndep">
              <a-input v-model="model.pileDesigndep" placeholder="请输入设计深度"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="实际灌浆深度" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="pileRealdep">
              <a-input v-model="model.pileRealdep" placeholder="请输入实际灌浆深度"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="灌浆时长" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="pileWorktime">
              <a-input v-model="model.pileWorktime" placeholder="请输入灌浆时长"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="成桩时间" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="pileTime">
              <j-date placeholder="请选择成桩时间" v-model="model.pileTime"  style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="密度（水泥浆比重）" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="pileDensity">
              <a-input v-model="model.pileDensity" placeholder="请输入密度（水泥浆比重）"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="处置状态" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="handlestate">
              <a-input v-model="model.handlestate" placeholder="请输入处置状态"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="开始时间" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="pileStarttime">
              <j-date placeholder="请选择开始时间" v-model="model.pileStarttime"  style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="alertstate" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="alertstate">
              <a-textarea v-model="model.alertstate" rows="4" placeholder="请输入alertstate" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="超标 0合格，1不合格" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="chaobiaodengji">
              <a-input v-model="model.chaobiaodengji" placeholder="请输入超标 0合格，1不合格"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="超标原因" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="problem">
              <a-input v-model="model.problem" placeholder="请输入超标原因"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="倾角-x" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="pileX">
              <a-input v-model="model.pileX" placeholder="请输入倾角-x"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="倾角-y" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="pileY">
              <a-input v-model="model.pileY" placeholder="请输入倾角-y"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="桩经度" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="pileLgd">
              <a-input v-model="model.pileLgd" placeholder="请输入桩经度"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="桩经度" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="pileLtd">
              <a-input v-model="model.pileLtd" placeholder="请输入桩经度"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="ts" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="ts">
              <a-input v-model="model.ts" placeholder="请输入ts"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="唯一码" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="uuid">
              <a-input v-model="model.uuid" placeholder="请输入唯一码"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="软基任务单号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="mixrwd">
              <a-input v-model="model.mixrwd" placeholder="请输入软基任务单号"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="灌浆开始时间" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="gjstarttime">
              <a-input v-model="model.gjstarttime" placeholder="请输入灌浆开始时间"  ></a-input>
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
    name: 'DeviceMixpileGroutedRealForm',
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
          add: "/devicemixgrouted/deviceMixpileGroutedReal/add",
          edit: "/devicemixgrouted/deviceMixpileGroutedReal/edit",
          queryById: "/devicemixgrouted/deviceMixpileGroutedReal/queryById"
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