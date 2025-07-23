<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <a-col :span="24">
            <a-form-model-item label="组织机构" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="orgid">
              <a-input v-model="model.orgid" placeholder="请输入组织机构"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="主表主键" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="orderid">
              <a-input v-model="model.orderid" placeholder="请输入主表主键"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="生产厂商" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="manufacturer">
              <a-input v-model="model.manufacturer" placeholder="请输入生产厂商"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="备注" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="remark">
              <a-input v-model="model.remark" placeholder="请输入备注"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="版本号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="version">
              <a-input v-model="model.version" placeholder="请输入版本号"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="材料主键" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="materialid">
              <a-input v-model="model.materialid" placeholder="请输入材料主键"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="材料编码" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="materialcode">
              <a-input v-model="model.materialcode" placeholder="请输入材料编码"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="材料名称" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="materialname">
              <a-input v-model="model.materialname" placeholder="请输入材料名称"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="规格型号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="materialmodel">
              <a-input v-model="model.materialmodel" placeholder="请输入规格型号"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="主单位" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="materialunit">
              <a-input v-model="model.materialunit" placeholder="请输入主单位"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="辅单位" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="auxiliaryunit">
              <a-input v-model="model.auxiliaryunit" placeholder="请输入辅单位"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="材料类别id（已废弃）" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="classid">
              <a-input v-model="model.classid" placeholder="请输入材料类别id（已废弃）"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="材料类别主键链（已废弃）" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="classfullid">
              <a-input v-model="model.classfullid" placeholder="请输入材料类别主键链（已废弃）"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="炉批号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="batchno">
              <a-input v-model="model.batchno" placeholder="请输入炉批号"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="实验报告号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="testreportno">
              <a-input v-model="model.testreportno" placeholder="请输入实验报告号"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="确认数量（核算数量）" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="netquantity">
              <a-input v-model="model.netquantity" placeholder="请输入确认数量（核算数量）"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="应收数量" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="roughquantity">
              <a-input v-model="model.roughquantity" placeholder="请输入应收数量"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="转化率" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="conversionrate">
              <a-input v-model="model.conversionrate" placeholder="请输入转化率"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="扣率" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="deductrate">
              <a-input v-model="model.deductrate" placeholder="请输入扣率"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="扣吨" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="deductquantity">
              <a-input v-model="model.deductquantity" placeholder="请输入扣吨"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="运单数量" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="orinetquantity">
              <a-input v-model="model.orinetquantity" placeholder="请输入运单数量"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="辅单位净重" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="auxiliarynetquantity">
              <a-input v-model="model.auxiliarynetquantity" placeholder="请输入辅单位净重"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="净重主单位" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="mainnetquantity">
              <a-input v-model="model.mainnetquantity" placeholder="请输入净重主单位"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="上传前id" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="oriitemid">
              <a-input v-model="model.oriitemid" placeholder="请输入上传前id"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="上传之前的磅单主键" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="oriorderid">
              <a-input v-model="model.oriorderid" placeholder="请输入上传之前的磅单主键"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="添加时间" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="createdat">
              <a-input v-model="model.createdat" placeholder="请输入添加时间"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="删除状态" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="isremoved">
              <a-input v-model="model.isremoved" placeholder="请输入删除状态"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="BIGINT	添加人" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="creatorid">
              <a-input v-model="model.creatorid" placeholder="请输入BIGINT	添加人"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="添加人" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="creatorname">
              <a-input v-model="model.creatorname" placeholder="请输入添加人"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="修改时间" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="updatedat">
              <a-input v-model="model.updatedat" placeholder="请输入修改时间"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="修改人" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="modifierid">
              <a-input v-model="model.modifierid" placeholder="请输入修改人"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="修改人" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="modifiername">
              <a-input v-model="model.modifiername" placeholder="请输入修改人"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="条码" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="itembarcode">
              <a-input v-model="model.itembarcode" placeholder="请输入条码"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="第三方组织机构" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="oriorgid">
              <a-input v-model="model.oriorgid" placeholder="请输入第三方组织机构"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="存放地" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="storageplace">
              <a-input v-model="model.storageplace" placeholder="请输入存放地"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="技证号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="skillcardno">
              <a-input v-model="model.skillcardno" placeholder="请输入技证号"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="单价" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="receiveprice">
              <a-input v-model="model.receiveprice" placeholder="请输入单价"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="运单重量" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="waybillweight">
              <a-input v-model="model.waybillweight" placeholder="请输入运单重量"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="业务类型" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="servicetype">
              <a-input v-model="model.servicetype" placeholder="请输入业务类型"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="操作类型" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="ordertype">
              <a-input v-model="model.ordertype" placeholder="请输入操作类型"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="上传状态" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="externaluploadstate">
              <a-input v-model="model.externaluploadstate" placeholder="请输入上传状态"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="冲红材料的原始材料ID" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="oriitemredid">
              <a-input v-model="model.oriitemredid" placeholder="请输入冲红材料的原始材料ID"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="类别名称" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="classname">
              <a-input v-model="model.classname" placeholder="请输入类别名称"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="类别编码" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="classcode">
              <a-input v-model="model.classcode" placeholder="请输入类别编码"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="类别全称" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="classfullname">
              <a-input v-model="model.classfullname" placeholder="请输入类别全称"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="第三方材料ID" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="orimaterialid">
              <a-input v-model="model.orimaterialid" placeholder="请输入第三方材料ID"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="第三方材料类别ID" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="oriclassid">
              <a-input v-model="model.oriclassid" placeholder="请输入第三方材料类别ID"  ></a-input>
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
    name: 'ZtwzjinmaterialForm',
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
          add: "/ztwzjinmaterial/ztwzjinmaterial/add",
          edit: "/ztwzjinmaterial/ztwzjinmaterial/edit",
          queryById: "/ztwzjinmaterial/ztwzjinmaterial/queryById"
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