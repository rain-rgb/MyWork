<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <a-col :span="24">
            <a-form-model-item label="唯一码" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="ssid">
              <a-input v-model="model.ssid" placeholder="请输入唯一码"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="孔号，和主表gsbh相同" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="holeid">
              <a-input v-model="model.holeid" placeholder="请输入孔号，和主表gsbh相同"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="设备编号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="shebeibianhao">
              <a-input v-model="model.shebeibianhao" placeholder="请输入设备编号"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="孔道名称" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="holename">
              <a-input v-model="model.holename" placeholder="请输入孔道名称"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="梁名称" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="sname">
              <a-input v-model="model.sname" placeholder="请输入梁名称"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="记录时间" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="jlsj">
              <a-input v-model="model.jlsj" placeholder="请输入记录时间"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="张拉次数" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="zlcs">
              <a-input v-model="model.zlcs" placeholder="请输入张拉次数"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="状态1" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="zt1">
              <a-input v-model="model.zt1" placeholder="请输入状态1"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="张拉力1(KN)" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="zll1">
              <a-input v-model="model.zll1" placeholder="请输入张拉力1(KN)"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="油压1（Mpa）" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="yy1">
              <a-input v-model="model.yy1" placeholder="请输入油压1（Mpa）"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="顶行程1（mm）" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="dxc1">
              <a-input v-model="model.dxc1" placeholder="请输入顶行程1（mm）"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="伸长量1（mm）" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="scl1">
              <a-input v-model="model.scl1" placeholder="请输入伸长量1（mm）"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="状态2" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="zt2">
              <a-input v-model="model.zt2" placeholder="请输入状态2"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="张拉力2（KN）" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="zll2">
              <a-input v-model="model.zll2" placeholder="请输入张拉力2（KN）"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="油压2（MPa）" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="yy2">
              <a-input v-model="model.yy2" placeholder="请输入油压2（MPa）"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="顶行程2（mm）" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="dxc2">
              <a-input v-model="model.dxc2" placeholder="请输入顶行程2（mm）"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="伸长量2（mm）" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="scl2">
              <a-input v-model="model.scl2" placeholder="请输入伸长量2（mm）"  ></a-input>
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
    name: 'TrZhanglaSSRealdataForm',
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
          add: "/zhanglassrealdata/trZhanglaSSRealdata/add",
          edit: "/zhanglassrealdata/trZhanglaSSRealdata/edit",
          queryById: "/zhanglassrealdata/trZhanglaSSRealdata/queryById"
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