<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <a-col :span="24">
            <a-form-model-item label="设备rollerId" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="rollerid">
              <a-input v-model="model.rollerid" placeholder="请输入设备rollerId"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="速度（km/h）" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="velocity">
              <a-input v-model="model.velocity" placeholder="请输入速度（km/h）"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="gps经度" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="lon">
              <a-input v-model="model.lon" placeholder="请输入gps经度"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="gps纬度" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="lat">
              <a-input v-model="model.lat" placeholder="请输入gps纬度"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="施工时间" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="gpstime">
              <j-date placeholder="请选择施工时间" v-model="model.gpstime"  style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="标段id" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="sectionid">
              <a-input v-model="model.sectionid" placeholder="请输入标段id"  ></a-input>
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
    name: 'LjyadataForm',
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
           rollerid: [
              { required: true, message: '请输入设备rollerId'},
           ],
        },
        url: {
          add: "/ljyadata/ljyadata/add",
          edit: "/ljyadata/ljyadata/edit",
          queryById: "/ljyadata/ljyadata/queryById"
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