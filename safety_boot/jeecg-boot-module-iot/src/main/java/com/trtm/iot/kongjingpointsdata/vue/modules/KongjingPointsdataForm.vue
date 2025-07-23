<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <a-col :span="24">
            <a-form-model-item label="基本信息 id" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="basicinfoid">
              <a-input v-model="model.basicinfoid" placeholder="请输入基本信息 id"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="通道号 0、1、2、3（其依次对 应 X、X'、Y、Y'）" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="passway">
              <a-input-number v-model="model.passway" placeholder="请输入通道号 0、1、2、3（其依次对 应 X、X'、Y、Y'）" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="深度 单位 m" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="depth">
              <a-input-number v-model="model.depth" placeholder="请输入深度 单位 m" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="声时 单位 us" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="soundtime">
              <a-input-number v-model="model.soundtime" placeholder="请输入声时 单位 us" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="泥浆声速 单位km/s" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="soundspeed">
              <a-input-number v-model="model.soundspeed" placeholder="请输入泥浆声速 单位km/s" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="波幅" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="amplitude">
              <a-input-number v-model="model.amplitude" placeholder="请输入波幅" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="方位角" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="azimuth">
              <a-input-number v-model="model.azimuth" placeholder="请输入方位角" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="半径 单位mm" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="radius">
              <a-input-number v-model="model.radius" placeholder="请输入半径 单位mm" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="增益 " :labelCol="labelCol" :wrapperCol="wrapperCol" prop="gain">
              <a-input-number v-model="model.gain" placeholder="请输入增益 " style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="延时 单位us" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="delay">
              <a-input-number v-model="model.delay" placeholder="请输入延时 单位us" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="增强声时 单位us" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="enhancetime">
              <a-input-number v-model="model.enhancetime" placeholder="请输入增强声时 单位us" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="修正距离 单位mm" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="correctiondistance">
              <a-input-number v-model="model.correctiondistance" placeholder="请输入修正距离 单位mm" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="修正方向 0:上 90:左 180:下 270: 右" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="correctiondirection">
              <a-input-number v-model="model.correctiondirection" placeholder="请输入修正方向 0:上 90:左 180:下 270: 右" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="噪声档位 范围[0-40]每档 50us 间 隔" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="adjustinggear">
              <a-input-number v-model="model.adjustinggear" placeholder="请输入噪声档位 范围[0-40]每档 50us 间 隔" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="测点时间 记录离始测点的时间; 始测点为0s ; 单位秒" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="testtime">
              <a-input-number v-model="model.testtime" placeholder="请输入测点时间 记录离始测点的时间; 始测点为0s ; 单位秒" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="数据  波形数值" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="wavedata">
              <a-input v-model="model.wavedata" placeholder="请输入数据  波形数值"  ></a-input>
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
    name: 'KongjingPointsdataForm',
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
           basicinfoid: [
              { required: true, message: '请输入基本信息 id!'},
           ],
        },
        url: {
          add: "/kongjingpointsdata/kongjingPointsdata/add",
          edit: "/kongjingpointsdata/kongjingPointsdata/edit",
          queryById: "/kongjingpointsdata/kongjingPointsdata/queryById"
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