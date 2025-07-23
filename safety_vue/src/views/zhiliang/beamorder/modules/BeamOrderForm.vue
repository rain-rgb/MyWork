<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <a-row>
            <a-col :span="12">
              <a-form-model-item label="订单编号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="orderno" >
                <a-input v-model="model.orderno" placeholder="请输入订单编号"  ></a-input>
              </a-form-model-item>
            </a-col>
            <a-col :span="12">
              <a-form-model-item label="粱型" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="beamType">
<!--                <a-input v-model="model.beamType" placeholder="请输入粱型"  ></a-input>-->
                <j-dict-select-tag type="list" v-model="model.beamType" :trigger-change="true" dictCode="beamType" placeholder="请选择粱型"/>
              </a-form-model-item>
            </a-col>
          </a-row>

          <a-row>
            <a-col :span="24">
              <a-form-model-item label="施工部位" :labelCol="labelCol1" :wrapperCol="wrapperCol1" prop="constructionSite">
                <a-input v-model="model.constructionSite" placeholder="请输入施工部位"  ></a-input>
              </a-form-model-item>
            </a-col>
          </a-row>

          <a-row>
            <a-col :span="12">
              <a-form-model-item label="请选择施工部位" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="constructionSiteid">
                <JSelectDqProjName  v-model="model.constructionSiteid" ::multi="false" />
                <!--              <a-input v-model="model.constructionSiteid" placeholder="请选择施工部位"  ></a-input>-->
              </a-form-model-item>
            </a-col>
            <a-col :span="12">
              <a-form-model-item label="交付日期" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="deliveryDate" required >
                <j-date placeholder="请选择交付日期" v-model="model.deliveryDate"  style="width: 100%" />
              </a-form-model-item>
            </a-col>
          </a-row>

          <a-row>
            <a-col :span="12">
              <a-form-model-item label="梁数量" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="beamNum">
                <a-input-number v-model="model.beamNum" placeholder="请输入梁数量" style="width: 100%" />
              </a-form-model-item>
            </a-col>
            <a-col :span="12">
              <a-form-model-item label="梁场(设备名称)" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="shebeino">
                <j-search-select-tag placeholder="请选择梁场设备名称" v-model="model.shebeino" :dictOptions="dictOption" >
                </j-search-select-tag>
                <!--              <a-input v-model="model.shebeino" placeholder="请输入梁场(设备编号)"  ></a-input>-->
              </a-form-model-item>
            </a-col>
          </a-row>

          <a-row>
            <a-col :span="24">
              <a-form-model-item label="制梁要求" :labelCol="labelCol1" :wrapperCol="wrapperCol1" prop="remark">
                <a-textarea placeholder="请输入制梁要求" v-model="model.remark" :rows="8" />
<!--                <a-input v-model="model.remark" placeholder="请输入制梁要求"  ></a-input>-->
              </a-form-model-item>
            </a-col>
          </a-row>

          <a-row>
            <a-col :span="12">
              <a-form-model-item label="顶板宽" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="roofwid">
                <a-input v-model="model.roofwid" placeholder="请输入顶板宽"  ></a-input>
              </a-form-model-item>
            </a-col>
            <a-col :span="12">
              <a-form-model-item label="底板宽" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="bottomwid">
                <a-input v-model="model.bottomwid" placeholder="请输入底板宽"  ></a-input>
              </a-form-model-item>
            </a-col>
          </a-row>

<!--          <a-col :span="24">-->
<!--            <a-form-model-item label="订单进度" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="orderProgress">-->
<!--              <a-input v-model="model.orderProgress" placeholder="请输入订单进度"  ></a-input>-->
<!--            </a-form-model-item>-->
<!--          </a-col>-->
<!--          <a-col :span="24">-->
<!--            <a-form-model-item label="0：未开始 1：生产中 2：已完成 3：滞后" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="orderStatus">-->
<!--              <a-input-number v-model="model.orderStatus" placeholder="请输入0：未开始 1：生产中 2：已完成 3：滞后" style="width: 100%" />-->
<!--            </a-form-model-item>-->
<!--          </a-col>-->
<!--          <a-col :span="24">-->
<!--            <a-form-model-item label="是否删除（0：否 1：是）" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="isdel">-->
<!--              <a-input-number v-model="model.isdel" placeholder="请输入是否删除（0：否 1：是）" style="width: 100%" />-->
<!--            </a-form-model-item>-->
<!--          </a-col>-->
          <a-row>
            <a-col :span="12">
              <a-form-model-item label="梁高" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="beamhig">
                <a-input v-model="model.beamhig" placeholder="请输入梁高"  ></a-input>
              </a-form-model-item>
            </a-col>
            <a-col :span="12">
              <a-form-model-item label="是否通知接收人" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="orderRecipient">
                <j-dict-select-tag type="list" v-model="model.issend" :trigger-change="true" dictCode="yn" placeholder="请选择是否通知接收人"/>
<!--                <a-input v-model="model.orderRecipient" placeholder="请输入订单接收人"  ></a-input>-->
              </a-form-model-item>
            </a-col>
          </a-row>

          <a-col :span="24">
            <a-form-model-item label="订单接收人" :labelCol="labelCol1" :wrapperCol="wrapperCol1" prop="orderRecipient">
              <j-dict-select-tag type="list" v-model="model.orderRecipient" :trigger-change="true" dictCode="bhz_phone,names,uid,phonesname='12'" dictTable="bhz_phone" placeholder="请选择订单接收人"/>
              <!--                <a-input v-model="model.orderRecipient" placeholder="请输入订单接收人"  ></a-input>-->
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
  import JSelectDqProjName from '@comp/jeecgbiz/JselectDqProjName'
  import { usershebeiList } from '@api/api'
  import Vue from 'vue'

  export default {
    name: 'BeamOrderForm',
    components: {
      JSelectDqProjName
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
        dictOption:[],
        model:{
         },
        labelCol: {
          xs: { span: 24 },
          sm: { span: 6 },
        },
        wrapperCol: {
          xs: { span: 24 },
          sm: { span: 16 },
        },
        labelCol1: {
          xs: { span: 24 },
          sm: { span: 3 },
        },
        wrapperCol1: {
          xs: { span: 24 },
          sm: { span: 20 },
        },
        confirmLoading: false,
        validatorRules: {
          deliveryDate:[
            { required: true, message: '请选择交付日期!' }
          ]
        },
        url: {
          add: "/sys/sysDepartproject/beamorderadd",
          edit: "/sys/sysDepartproject/beamorderedit",
          queryById: "/beamorder/beamOrder/queryById"
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
      this.shebeiList();
    },
    methods: {
      shebeiList:function (){
        var sys_depart_orgcode=Vue.ls.get('SYS_DEPART_ORGCODE');
        usershebeiList({
          sys_depart_orgcode:sys_depart_orgcode,
          sbtypes:'41'
        }).then(res=>{
          this.dictOption=[];
          let result=res.result;
          result.forEach(re=>{
            this.dictOption.push({text:re.sbname,value:re.sbjno})
          })
          //console.log(res)
        })
      },
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