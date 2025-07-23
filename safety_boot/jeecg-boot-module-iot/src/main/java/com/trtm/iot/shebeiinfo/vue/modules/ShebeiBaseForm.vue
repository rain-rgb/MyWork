<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <a-col :span="24">
            <a-form-model-item label="设备编号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="shebeino">
              <a-input-number v-model="model.shebeino" placeholder="请输入设备编号" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="设备名称" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="shebeiname">
              <a-input v-model="model.shebeiname" placeholder="请输入设备名称"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="设备类型" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="sbtype">
              <a-input-number v-model="model.sbtype" placeholder="请输入设备类型" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="合格证附件资料" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="hegezheng">
              <a-input v-model="model.hegezheng" placeholder="请输入合格证附件资料"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="规格" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="guige">
              <a-input v-model="model.guige" placeholder="请输入规格"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="设备厂家" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="changjia">
              <a-input v-model="model.changjia" placeholder="请输入设备厂家"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="出厂编号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="chuchangbianhao">
              <a-input v-model="model.chuchangbianhao" placeholder="请输入出厂编号"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="出厂日期" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="chuchangriqi">
              <a-input v-model="model.chuchangriqi" placeholder="请输入出厂日期"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="责任人" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="zerenren">
              <a-input v-model="model.zerenren" placeholder="请输入责任人"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="验收状态" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="yanshouzhuangtai">
              <a-input v-model="model.yanshouzhuangtai" placeholder="请输入验收状态"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="进场时间" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="jingchangshijian">
              <a-input v-model="model.jingchangshijian" placeholder="请输入进场时间"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="退场时间" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="tuichangshijian">
              <a-input v-model="model.tuichangshijian" placeholder="请输入退场时间"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="0 没有运行轨迹；1 有运行轨迹" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="isgps">
              <a-input-number v-model="model.isgps" placeholder="请输入0 没有运行轨迹；1 有运行轨迹" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="gps设备编号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="gpsshebeino">
              <a-input v-model="model.gpsshebeino" placeholder="请输入gps设备编号"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="备用" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="note">
              <a-input v-model="model.note" placeholder="请输入备用"  ></a-input>
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
    name: 'ShebeiBaseForm',
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
          add: "/shebeiinfo/shebeiBase/add",
          edit: "/shebeiinfo/shebeiBase/edit",
          queryById: "/shebeiinfo/shebeiBase/queryById"
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