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
            <a-form-model-item label="桩设计深度" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="pileDesigndep">
              <a-input v-model="model.pileDesigndep" placeholder="请输入桩设计深度"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="桩钻深" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="pileRealdep">
              <a-input v-model="model.pileRealdep" placeholder="请输入桩钻深"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="每米水泥用量(平均灰量)" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="usagePm">
              <a-input v-model="model.usagePm" placeholder="请输入每米水泥用量(平均灰量)"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="桩x角度(垂直度)" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="pileX">
              <a-input v-model="model.pileX" placeholder="请输入桩x角度(垂直度)"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="平均提桩速度" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="pileUspeed">
              <a-input v-model="model.pileUspeed" placeholder="请输入平均提桩速度"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="平均下桩速度" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="pileDspeed">
              <a-input v-model="model.pileDspeed" placeholder="请输入平均下桩速度"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="水泥浆比重(平均密度)" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="pileDensity">
              <a-input v-model="model.pileDensity" placeholder="请输入水泥浆比重(平均密度)"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="状态(0合格；1不合格)" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="status">
              <a-input-number v-model="model.status" placeholder="请输入状态(0合格；1不合格)" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="超标描述" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="problem">
              <a-input v-model="model.problem" placeholder="请输入超标描述"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="处置状态" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="handleStatus">
              <a-input-number v-model="model.handleStatus" placeholder="请输入处置状态" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="峰值电流" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="pileMaxelec">
              <a-input v-model="model.pileMaxelec" placeholder="请输入峰值电流"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="搅拌次数" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="stirCount">
              <a-input-number v-model="model.stirCount" placeholder="请输入搅拌次数" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="搅拌时长" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="stirTime">
              <a-input v-model="model.stirTime" placeholder="请输入搅拌时长"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="注浆量" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="slurryVolume">
              <a-input v-model="model.slurryVolume" placeholder="请输入注浆量"  ></a-input>
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
    name: 'DeviceMixpileHistorydataOneHbForm',
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
          add: "/devicemixpilehistorydataonehb/deviceMixpileHistorydataOneHb/add",
          edit: "/devicemixpilehistorydataonehb/deviceMixpileHistorydataOneHb/edit",
          queryById: "/devicemixpilehistorydataonehb/deviceMixpileHistorydataOneHb/queryById"
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