<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <a-col :span="24">
            <a-form-model-item label="材料名称" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="cailiaoname">
              <a-input v-model="model.cailiaoname" placeholder="请输入材料名称"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="材料编号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="cailiaono">
              <a-input v-model="model.cailiaono" placeholder="请输入材料编号"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="parentnode" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="parentnode">
              <a-input v-model="model.parentnode" placeholder="请输入parentnode"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="材料类型" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="nodetype">
              <a-input v-model="model.nodetype" placeholder="请输入材料类型"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="规格类型" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="guigexinghao">
              <a-input v-model="model.guigexinghao" placeholder="请输入规格类型"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="称重" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="ischengzhong">
              <a-input v-model="model.ischengzhong" placeholder="请输入称重"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="描述" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="miaoshu">
              <a-input v-model="model.miaoshu" placeholder="请输入描述"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="偏差" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="isyunxupiancha">
              <a-input v-model="model.isyunxupiancha" placeholder="请输入偏差"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="正偏差" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="zhengpiancha">
              <a-input v-model="model.zhengpiancha" placeholder="请输入正偏差"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="负偏差" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="fupiancha">
              <a-input v-model="model.fupiancha" placeholder="请输入负偏差"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="材料计量单位" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="wzcailiaojiliangdanwei">
              <a-input v-model="model.wzcailiaojiliangdanwei" placeholder="请输入材料计量单位"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="品牌" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="pinpai">
              <a-input v-model="model.pinpai" placeholder="请输入品牌"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="产地" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="chandi">
              <a-input v-model="model.chandi" placeholder="请输入产地"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="单价" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="danjia">
              <a-input v-model="model.danjia" placeholder="请输入单价"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="材料字典的唯一id" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="guid">
              <a-input v-model="model.guid" placeholder="请输入材料字典的唯一id"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="时间戳" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="ts">
              <a-input-number v-model="model.ts" placeholder="请输入时间戳" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="isdel" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="isdel">
              <a-input-number v-model="model.isdel" placeholder="请输入isdel" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="status" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="status">
              <a-input-number v-model="model.status" placeholder="请输入status" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="路面项目 1 原材料  2混合料" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="lmcailiaolx">
              <a-input-number v-model="model.lmcailiaolx" placeholder="请输入路面项目 1 原材料  2混合料" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="原材料上限" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="shangxian">
              <a-input-number v-model="model.shangxian" placeholder="请输入原材料上限" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="testid" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="testid">
              <a-input-number v-model="model.testid" placeholder="请输入testid" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="editperson" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="editperson">
              <a-input v-model="model.editperson" placeholder="请输入editperson"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="editdatetime" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="editdatetime">
              <a-input v-model="model.editdatetime" placeholder="请输入editdatetime"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="wzcailiaodanweihuansuan" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="wzcailiaodanweihuansuan">
              <a-input v-model="model.wzcailiaodanweihuansuan" placeholder="请输入wzcailiaodanweihuansuan"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="是否使用电子锁 0：否，1：是" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="iselocks">
              <a-input-number v-model="model.iselocks" placeholder="请输入是否使用电子锁 0：否，1：是" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="重量" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="weight">
              <a-input-number v-model="model.weight" placeholder="请输入重量" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="过磅类型: 0:无人过磅 1:人工过磅" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="gblx">
              <a-input-number v-model="model.gblx" placeholder="请输入过磅类型: 0:无人过磅 1:人工过磅" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="材料字典表id" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="allguid">
              <a-input v-model="model.allguid" placeholder="请输入材料字典表id"  ></a-input>
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
    name: 'RebarWzcailiaonamedictManForm',
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
          add: "/rebarWzcailiaonamedictMan/rebarWzcailiaonamedictMan/add",
          edit: "/rebarWzcailiaonamedictMan/rebarWzcailiaonamedictMan/edit",
          queryById: "/rebarWzcailiaonamedictMan/rebarWzcailiaonamedictMan/queryById"
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