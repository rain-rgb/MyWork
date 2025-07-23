<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <a-col :span="24">
            <a-form-model-item label="项目名称" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="projectname">
              <a-input v-model="model.projectname" placeholder="请输入项目名称"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="测点名称" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="cedianname">
              <a-input v-model="model.cedianname" placeholder="请输入测点名称"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="监测编码" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="jiancebianma">
              <a-input v-model="model.jiancebianma" placeholder="请输入监测编码"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="监测数据" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="jsondata">
              <a-input v-model="model.jsondata" placeholder="请输入监测数据"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="设备编号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="shebeino">
              <a-input v-model="model.shebeino" placeholder="请输入设备编号"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="数据时间" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="datatime">
              <j-date placeholder="请选择数据时间" v-model="model.datatime"  style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="监测类别：SY:渗透水压力监测;GP:表面位移监测;NW:水位监测" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="jiancetype">
              <a-input v-model="model.jiancetype" placeholder="请输入监测类别：SY:渗透水压力监测;GP:表面位移监测;NW:水位监测"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="SY:渗透水压力监测时 表示  围堰顶部高程" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="gaocheng">
              <a-input v-model="model.gaocheng" placeholder="请输入SY:渗透水压力监测时 表示  围堰顶部高程"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="SY:渗透水压力监测时 表示 孔深" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="kongshen">
              <a-input v-model="model.kongshen" placeholder="请输入SY:渗透水压力监测时 表示 孔深"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="SY:渗透水压力监测时 表示 见水" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="jianshui">
              <a-input v-model="model.jianshui" placeholder="请输入SY:渗透水压力监测时 表示 见水"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="经度" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="jingdu">
              <a-input v-model="model.jingdu" placeholder="请输入经度"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="维度" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="weidu">
              <a-input v-model="model.weidu" placeholder="请输入维度"  ></a-input>
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
    name: 'JikengWeiyCfgForm',
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
          add: "/jikeng/jikengWeiyCfg/add",
          edit: "/jikeng/jikengWeiyCfg/edit",
          queryById: "/jikeng/jikengWeiyCfg/queryById"
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