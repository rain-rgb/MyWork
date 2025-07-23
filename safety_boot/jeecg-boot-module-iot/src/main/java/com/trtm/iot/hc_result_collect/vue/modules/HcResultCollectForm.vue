<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model :model="model" :rules="validatorRules" ref="form" slot="detail">
        <a-row>
          <a-col :span="24">
            <a-form-model-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="主键id" prop="blockid">
              <a-input placeholder="请输入主键id" v-model="model.blockid"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="日期" prop="paveday">
              <j-date placeholder="请选择日期" style="width: 100%"  v-model="model.paveday" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="开始时间" prop="begintime">
              <j-date placeholder="请选择开始时间" style="width: 100%"  v-model="model.begintime" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="结束时间" prop="endtime">
              <j-date placeholder="请选择结束时间" style="width: 100%"  v-model="model.endtime" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="工程名称" prop="projectname">
              <a-input placeholder="请输入工程名称" v-model="model.projectname"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="工程id" prop="projectid">
              <a-input placeholder="请输入工程id" v-model="model.projectid"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="标段名称" prop="sectionname">
              <a-input placeholder="请输入标段名称" v-model="model.sectionname"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="标段id" prop="sectionid">
              <a-input placeholder="请输入标段id" v-model="model.sectionid"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="分层名称" prop="layername">
              <a-input placeholder="请输入分层名称" v-model="model.layername"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="分层id" prop="layerindex">
              <a-input placeholder="请输入分层id" v-model="model.layerindex"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="
开始桩号" prop="beginstation">
              <a-input placeholder="请输入
开始桩号" v-model="model.beginstation"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="格式化后的开始桩号" prop="beginstationformat">
              <a-input placeholder="请输入格式化后的开始桩号" v-model="model.beginstationformat"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="结束桩号" prop="endstation">
              <a-input placeholder="请输入结束桩号" v-model="model.endstation"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="格式化后的结束桩号" prop="endstationformat">
              <a-input placeholder="请输入格式化后的结束桩号" v-model="model.endstationformat"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="幅,0-左幅,1-右幅,2-全幅" prop="roadside">
              <a-input-number placeholder="请输入幅,0-左幅,1-右幅,2-全幅" style="width: 100%" v-model="model.roadside" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="分层方式,0-自动,1-手动" prop="createtype">
              <a-input-number placeholder="请输入分层方式,0-自动,1-手动" style="width: 100%" v-model="model.createtype" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="摊铺厚度" prop="thickavg">
              <a-input placeholder="请输入摊铺厚度" v-model="model.thickavg"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="摊铺里程" prop="pavemileage">
              <a-input placeholder="请输入摊铺里程" v-model="model.pavemileage"  ></a-input>
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
    name: 'HcResultCollectForm',
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
           blockid: [
              { required: true, message: '请输入主键id!'},
           ],
        },
        url: {
          add: "/hc_result_collect/hcResultCollect/add",
          edit: "/hc_result_collect/hcResultCollect/edit",
          queryById: "/hc_result_collect/hcResultCollect/queryById"
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
