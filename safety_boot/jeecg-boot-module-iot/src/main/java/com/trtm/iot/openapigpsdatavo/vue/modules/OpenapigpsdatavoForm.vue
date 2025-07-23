<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model :model="model" :rules="validatorRules" ref="form" slot="detail">
        <a-row>
          <a-col :span="24">
            <a-form-model-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="机械终端安装关系id" prop="machineid">
              <a-input-number placeholder="请输入机械终端安装关系id" style="width: 100%" v-model="model.machineid" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="数据上传时间" prop="gpstime">
              <j-date placeholder="请选择数据上传时间" style="width: 100%"  v-model="model.gpstime" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="定位质量，1：单点、2：码差分、4：固定、5：浮动" prop="qualityindex">
              <a-input placeholder="请输入定位质量，1：单点、2：码差分、4：固定、5：浮动" v-model="model.qualityindex"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="北坐标" prop="north">
              <a-input placeholder="请输入北坐标" v-model="model.north"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="东坐标" prop="east">
              <a-input placeholder="请输入东坐标" v-model="model.east"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="经度" prop="lon">
              <a-input placeholder="请输入经度" v-model="model.lon"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="纬度" prop="lat">
              <a-input placeholder="请输入纬度" v-model="model.lat"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="摊铺温度" prop="temperature">
              <a-input placeholder="请输入摊铺温度" v-model="model.temperature"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="所在桩号" prop="roadstation">
              <a-input placeholder="请输入所在桩号" v-model="model.roadstation"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="位置相对道路中线偏移量" prop="offset">
              <a-input placeholder="请输入位置相对道路中线偏移量" v-model="model.offset"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="速度(m/s)" prop="velocity">
              <a-input placeholder="请输入速度(m/s)" v-model="model.velocity"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="温度离析(℃)" prop="tempdiff">
              <a-input placeholder="请输入温度离析(℃)" v-model="model.tempdiff"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="高程" prop="geoh">
              <a-input placeholder="请输入高程" v-model="model.geoh"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="压实度" prop="cmv">
              <a-input placeholder="请输入压实度" v-model="model.cmv"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="振动频率" prop="frequency">
              <a-input placeholder="请输入振动频率" v-model="model.frequency"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="运料车rfid" prop="rfid">
              <a-input placeholder="请输入运料车rfid" v-model="model.rfid"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="所属层，层属于后处理计算结果，实时性一般，初始值为65535，计算后会将其更新" prop="layerindex">
              <a-input placeholder="请输入所属层，层属于后处理计算结果，实时性一般，初始值为65535，计算后会将其更新" v-model="model.layerindex"  ></a-input>
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
    name: 'OpenapigpsdatavoForm',
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
          add: "/openapigpsdatavo/openapigpsdatavo/add",
          edit: "/openapigpsdatavo/openapigpsdatavo/edit",
          queryById: "/openapigpsdatavo/openapigpsdatavo/queryById"
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
