<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-divider type="horizontal" ><span style="color:#0785fd">基础配置</span></a-divider>
        <a-row>
          <a-col :span="12">
            <a-form-model-item label="所属部门" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="sysOrgCode">
              <JselectDqDepart v-model="model.sysOrgCode"  />
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="设备名称" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="shebeiNo">
              <!--              <a-input v-model="model.devaddr" placeholder="请输入设备名称"  ></a-input>-->
              <j-search-select-tag placeholder="请选择设备名称"  v-model="model.shebeiNo" :dictOptions="dictOption" >
              </j-search-select-tag>
            </a-form-model-item>
          </a-col>
        </a-row>

        <a-divider type="horizontal" ><span style="color:#0785fd">整桩预警配置</span></a-divider>
        <a-row>
           <a-col :span="24">
             <a-form-model-item label="施工桩长" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="pileRealdep">
               <a-input v-model="model.pileRealdep" placeholder="请输入施工桩长"  ></a-input>
             </a-form-model-item>
           </a-col>
        </a-row>
        <a-row>
          <a-col :span="12">
            <a-form-model-item label="倾角 下限" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="pileYx">
              <a-input v-model="model.pileYx" placeholder="请输入倾角 最大垂直度 ≤1%"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="倾角 上限" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="pileY">
              <a-input v-model="model.pileY" placeholder="请输入倾角 最大垂直度 ≤1%"  ></a-input>
            </a-form-model-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :span="12">
            <a-form-model-item label="桩间距" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="pileAway">
              <a-input v-model="model.pileAway" placeholder="请输入桩间距"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="桩尖监测 " :labelCol="labelCol" :wrapperCol="wrapperCol" prop="pileJiance">
              <a-input v-model="model.pileJiance" placeholder="请输入桩尖监测 "  ></a-input>
            </a-form-model-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :span="12">
            <a-form-model-item label="压桩力" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="pilePress">
              <a-input v-model="model.pilePress" placeholder="请输入压桩力"  ></a-input>
            </a-form-model-item>
          </a-col>
<!--          <a-col :span="24">-->
<!--            <a-form-model-item label="预警手机号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="phone">-->
<!--              <a-input v-model="model.phone" placeholder="请输入预警手机号"  ></a-input>-->
<!--            </a-form-model-item>-->
<!--          </a-col>-->
          <a-col :span="12">
            <a-form-model-item label="预警手机号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="phone">
              <!--              <a-input v-model="model.primaryPhones" placeholder="请输入预警号码"  ></a-input>-->
              <j-dict-select-tag type="list" v-model="model.phone" dictCode="bhz_phone,names,uid,phonesname='13'" dictTable="bhz_phone" placeholder="请选择初级预警号码组"/>
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
  import { usershebeiList } from '@api/api'
  import Vue from "vue";

  export default {
    name: 'PipepileYujingForm',
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
        dictOption:[],
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
          add: "/pipepileYujing/pipepileYujing/add",
          edit: "/pipepileYujing/pipepileYujing/edit",
          queryById: "/pipepileYujing/pipepileYujing/queryById"
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
          sbtypes:'61'
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