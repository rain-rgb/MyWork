<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model :model="model" :rules="validatorRules" ref="form" slot="detail">
        <a-row>
          <a-col :span="24">
            <a-form-model-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="标段ID" prop="sectionid">
              <a-input placeholder="请输入标段ID" v-model="model.sectionid"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="标段名" prop="sectionname">
              <a-input placeholder="请输入标段名" v-model="model.sectionname"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="左右幅，0是其他，1是双幅，2是单幅" prop="offsettype">
              <a-input placeholder="请输入左右幅，0是其他，1是双幅，2是单幅" v-model="model.offsettype"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="设置单遍复遍，0为单遍，1为复遍" prop="timestype">
              <a-input placeholder="请输入设置单遍复遍，0为单遍，1为复遍" v-model="model.timestype"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="分层模式：0不分层，2路基块自动分层，5路面信息化自动分层，6土方压实自动分层，7土方压实手动分层(压路机手点击开始结束)" prop="stratifymode">
              <a-input placeholder="请输入分层模式：0不分层，2路基块自动分层，5路面信息化自动分层，6土方压实自动分层，7土方压实手动分层(压路机手点击开始结束)" v-model="model.stratifymode"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="标段边界北东数据，格式：北坐标,东坐标 北坐标,东坐标" prop="sectionboundary">
              <a-input placeholder="请输入标段边界北东数据，格式：北坐标,东坐标 北坐标,东坐标" v-model="model.sectionboundary"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="标段边界经纬度数据，格式：[[经度,纬度],[经度,纬度]]" prop="sectionwgsboundary">
              <a-input placeholder="请输入标段边界经纬度数据，格式：[[经度,纬度],[经度,纬度]]" v-model="model.sectionwgsboundary"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="开始施工日期" prop="sectionstartdate">
              <j-date placeholder="请选择开始施工日期" style="width: 100%"  v-model="model.sectionstartdate" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="监理单位id" prop="sectionsupcompanyid">
              <a-input placeholder="请输入监理单位id" v-model="model.sectionsupcompanyid"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="监理单位名称" prop="sectionsupcompanyname">
              <a-input placeholder="请输入监理单位名称" v-model="model.sectionsupcompanyname"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="施工单位id" prop="sectionbuildercompanyid">
              <a-input placeholder="请输入施工单位id" v-model="model.sectionbuildercompanyid"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="施工单位名称" prop="sectionbuildercompanyname">
              <a-input placeholder="请输入施工单位名称" v-model="model.sectionbuildercompanyname"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="对应的标段编码" prop="orgcode">
              <a-input placeholder="请输入对应的标段编码" v-model="model.orgcode"  ></a-input>
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
    name: 'HcSectionForm',
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
          add: "/hc_section/hcSection/add",
          edit: "/hc_section/hcSection/edit",
          queryById: "/hc_section/hcSection/queryById"
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
