<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <a-col :span="24">
            <a-form-item label="所属部门" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="sysorgcode">
              <JselectDqDepart v-model="model.sysorgcode"   ::multi="false"  />
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="设备编号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="sbjno">
              <a-input v-model="model.sbjno" placeholder="请输入设备编号"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="设备名称" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="sbname">
              <a-input v-model="model.sbname" placeholder="请输入设备名称"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="设备类型" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="sbtype">
              <a-input v-model="model.sbtype" placeholder="请输入设备类型"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="型号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="xinghao">
              <a-input v-model="model.xinghao" placeholder="请输入型号"  ></a-input>
            </a-form-model-item>
          </a-col>
<!--          <a-col :span="24">-->
<!--            <a-form-model-item label="设备所属机构" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="sysorgcode">-->
<!--              <a-input v-model="model.sysorgcode" placeholder="请输入设备所属机构"  ></a-input>-->
<!--            </a-form-model-item>-->
<!--          </a-col>-->
          <a-col :span="24">
            <a-form-model-item label="设备可做实验类型" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="exptype">
              <a-input v-model="model.exptype" placeholder="请输入设备可做实验类型"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="所属单位" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="ssdw">
              <a-input v-model="model.ssdw" placeholder="请输入所属单位"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="进场时间" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="jingchangtime">
              <j-date placeholder="请选择进场时间" v-model="model.jingchangtime"  style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="标定时间" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="biaodingtime">
              <j-date placeholder="请选择标定时间" v-model="model.biaodingtime"  style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="检验周期（单位天）" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="jyzq">
              <a-input v-model="model.jyzq" placeholder="请输入检验周期（单位天）"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="管理员" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="guanliyuan">
              <a-input v-model="model.guanliyuan" placeholder="请输入管理员"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="放置地点" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="adr">
              <a-input v-model="model.adr" placeholder="请输入放置地点"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="是否完好" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="isok">
              <a-input-number v-model="model.isok" placeholder="请输入是否完好" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="是否物联" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="iswulian">
              <a-input-number v-model="model.iswulian" placeholder="请输入是否物联" style="width: 100%" />
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
  import JselectDqDepart from '@comp/jeecgbiz/JselectDqDepart'

  export default {
    name: 'SyShebeiguanliForm',
    components: {
      JselectDqDepart
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
          add: "/syztgl/syShebeiguanli/add",
          edit: "/syztgl/syShebeiguanli/edit",
          queryById: "/syztgl/syShebeiguanli/queryById"
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