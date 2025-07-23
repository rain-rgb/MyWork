<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <a-col :span="24">
            <a-form-model-item label="设备名称" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="shebeino">
              <!--              <a-input v-model="model.devaddr" placeholder="请输入设备名称"  ></a-input>-->
              <j-search-select-tag placeholder="请选择设备名称"  v-model="model.shebeino" :dictOptions="dictOption" >
              </j-search-select-tag>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="设计桩长(m)" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="designdep">
              <a-input v-model="model.designdep" placeholder="请输入设计桩长(m)"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="锤击次数" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="pileSecond">
              <a-input-number v-model="model.pileSecond" placeholder="请输入锤击次数" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="倾角下限" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="xx">
              <a-input v-model="model.xx" placeholder="请输入倾角下限"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="倾角上限" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="xs">
              <a-input v-model="model.xs" placeholder="请输入倾角上限"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="预警手机号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="jbzphones">
              <!--              <a-input v-model="model.primaryPhones" placeholder="请输入预警号码"  ></a-input>-->
              <j-dict-select-tag type="list" v-model="model.jbzphones" :trigger-change="true" dictCode="bhz_phone,names,uid,phonesname='9'" dictTable="bhz_phone" placeholder="请选择预警号码组"/>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="是否报警" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="isCall">
              <j-dict-select-tag type="list" v-model="model.isCall" :trigger-change="true" dictCode="is_call" placeholder="请选择是否报警" style="width: 100%"/>
            </a-form-model-item>
          </a-col>
<!--          <a-col :span="24">-->
<!--            <a-form-model-item label="是否删除" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="isdel">-->
<!--              <a-input-number v-model="model.isdel" placeholder="请输入是否删除" style="width: 100%" />-->
<!--            </a-form-model-item>-->
<!--          </a-col>-->
        </a-row>
      </a-form-model>
    </j-form-container>
  </a-spin>
</template>

<script>

  import { httpAction, getAction } from '@/api/manage'
  import { validateDuplicateValue } from '@/utils/util'
  import { usershebeiList } from '@api/api'
  import Vue from "vue";

  export default {
    name: 'DeviceHammeringOneCfgForm',
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
          add: "/deviceHammeringonecfg/deviceHammeringOneCfg/add",
          edit: "/deviceHammeringonecfg/deviceHammeringOneCfg/edit",
          queryById: "/deviceHammeringonecfg/deviceHammeringOneCfg/queryById"
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
          sbtypes:'76'
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