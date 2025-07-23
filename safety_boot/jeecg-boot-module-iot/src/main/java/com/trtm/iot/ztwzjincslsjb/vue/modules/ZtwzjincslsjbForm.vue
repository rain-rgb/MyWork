<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <a-col :span="24">
            <a-form-model-item label="制单人" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="maker">
              <a-input v-model="model.maker" placeholder="请输入制单人"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="备注" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="remark">
              <a-input v-model="model.remark" placeholder="请输入备注"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="审核人" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="auditor">
              <a-input v-model="model.auditor" placeholder="请输入审核人"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="版本号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="version">
              <a-input v-model="model.version" placeholder="请输入版本号"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="端上的组织机构名称" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="oriorgname">
              <a-input v-model="model.oriorgname" placeholder="请输入端上的组织机构名称"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="组织机构" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="orgid">
              <a-input v-model="model.orgid" placeholder="请输入组织机构"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="账期" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="orderdate">
              <a-input v-model="model.orderdate" placeholder="请输入账期"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="过磅日期" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="recordeddate">
              <a-input v-model="model.recordeddate" placeholder="请输入过磅日期"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="磅单编号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="ordercode">
              <a-input v-model="model.ordercode" placeholder="请输入磅单编号"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="单据来源" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="orderorigin">
              <a-input-number v-model="model.orderorigin" placeholder="请输入单据来源" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="业务类型" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="servicetype">
              <a-input-number v-model="model.servicetype" placeholder="请输入业务类型" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="操作类型" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="ordertype">
              <a-input-number v-model="model.ordertype" placeholder="请输入操作类型" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="制单时间" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="makerdate">
              <a-input v-model="model.makerdate" placeholder="请输入制单时间"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="打印次数" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="printtimes">
              <a-input-number v-model="model.printtimes" placeholder="请输入打印次数" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="车牌号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="platenumber">
              <a-input v-model="model.platenumber" placeholder="请输入车牌号"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="供应商ID" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="supplierid">
              <a-input-number v-model="model.supplierid" placeholder="请输入供应商ID" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="供应商名称" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="suppliername">
              <a-input v-model="model.suppliername" placeholder="请输入供应商名称"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="冲红状态" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="isred">
              <a-input v-model="model.isred" placeholder="请输入冲红状态"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="审核状态" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="isaudit">
              <a-input v-model="model.isaudit" placeholder="请输入审核状态"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="审核日期" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="auditdate">
              <a-input v-model="model.auditdate" placeholder="请输入审核日期"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="第三方组织机构" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="oriorgid">
              <a-input v-model="model.oriorgid" placeholder="请输入第三方组织机构"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="过磅端主键" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="oriorderid">
              <a-input v-model="model.oriorderid" placeholder="请输入过磅端主键"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="添加人" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="creatorid">
              <a-input v-model="model.creatorid" placeholder="请输入添加人"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="添加人姓名" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="creatorname">
              <a-input v-model="model.creatorname" placeholder="请输入添加人姓名"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="添加时间" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="createdat">
              <a-input v-model="model.createdat" placeholder="请输入添加时间"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="修改人" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="modifierid">
              <a-input v-model="model.modifierid" placeholder="请输入修改人"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="修改人姓名" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="modifiername">
              <a-input v-model="model.modifiername" placeholder="请输入修改人姓名"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="修改时间" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="updatedat">
              <a-input v-model="model.updatedat" placeholder="请输入修改时间"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="删除状态" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="isremoved">
              <a-input v-model="model.isremoved" placeholder="请输入删除状态"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="引用主键" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="orderdataid">
              <a-input v-model="model.orderdataid" placeholder="请输入引用主键"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="识别车牌" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="discernplatenumber">
              <a-input v-model="model.discernplatenumber" placeholder="请输入识别车牌"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="料仓全称" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="stockbinfullname">
              <a-input v-model="model.stockbinfullname" placeholder="请输入料仓全称"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="料仓名称" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="stockbinname">
              <a-input v-model="model.stockbinname" placeholder="请输入料仓名称"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="料仓主键" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="stockbinid">
              <a-input v-model="model.stockbinid" placeholder="请输入料仓主键"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="第三方料仓主键" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="oristockbinid">
              <a-input v-model="model.oristockbinid" placeholder="请输入第三方料仓主键"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="入场时间" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="entertime">
              <a-input v-model="model.entertime" placeholder="请输入入场时间"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="出场时间" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="exittime">
              <a-input v-model="model.exittime" placeholder="请输入出场时间"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="排序时间" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="sorttime">
              <a-input v-model="model.sorttime" placeholder="请输入排序时间"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="是否退料" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="isreturn">
              <a-input v-model="model.isreturn" placeholder="请输入是否退料"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="出场状态" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="isexit">
              <a-input v-model="model.isexit" placeholder="请输入出场状态"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="是否称皮重" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="istare">
              <a-input v-model="model.istare" placeholder="请输入是否称皮重"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="转换率计算方式 true乘法 false 除法" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="ismultiplication">
              <a-input v-model="model.ismultiplication" placeholder="请输入转换率计算方式 true乘法 false 除法"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="运单结算(true代表确认数量netQuantity 为运单数量，false则代表确认数量netQuantity 为称重数量)" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="isuseorinetquantity">
              <a-input v-model="model.isuseorinetquantity" placeholder="请输入运单结算(true代表确认数量netQuantity 为运单数量，false则代表确认数量netQuantity 为称重数量)"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="抵扣系数" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="deductionrate">
              <a-input v-model="model.deductionrate" placeholder="请输入抵扣系数"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="毛重" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="roughquantity">
              <a-input v-model="model.roughquantity" placeholder="请输入毛重"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="皮重" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="tarequantity">
              <a-input v-model="model.tarequantity" placeholder="请输入皮重"  ></a-input>
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
            <a-form-model-item label="辅单位净重" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="auxiliarynetquantity">
              <a-input v-model="model.auxiliarynetquantity" placeholder="请输入辅单位净重"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="运单重量" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="waybillweight">
              <a-input v-model="model.waybillweight" placeholder="请输入运单重量"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="单据条形码" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="orderbarcode">
              <a-input v-model="model.orderbarcode" placeholder="请输入单据条形码"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="区分一车多料、直进直出、普通进料标识" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="weighttype">
              <a-input v-model="model.weighttype" placeholder="请输入区分一车多料、直进直出、普通进料标识"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="复称状态" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="isaffirm">
              <a-input v-model="model.isaffirm" placeholder="请输入复称状态"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="供应商第三方主键" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="orisupplierid">
              <a-input v-model="model.orisupplierid" placeholder="请输入供应商第三方主键"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="组织机构名称" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="orgname">
              <a-input v-model="model.orgname" placeholder="请输入组织机构名称"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="项目部组织ID" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="realorgid">
              <a-input v-model="model.realorgid" placeholder="请输入项目部组织ID"  ></a-input>
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
    name: 'ZtwzjincslsjbForm',
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
          add: "/ztwzjincslsjb/ztwzjincslsjb/add",
          edit: "/ztwzjincslsjb/ztwzjincslsjb/edit",
          queryById: "/ztwzjincslsjb/ztwzjincslsjb/queryById"
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