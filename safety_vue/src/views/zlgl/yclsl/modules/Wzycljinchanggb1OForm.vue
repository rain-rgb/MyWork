<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <a-col :span="24">
            <a-form-model-item label="设备" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="shebeibianhao">
            <j-search-select-tag placeholder="请选择设备名称" v-model="model.shebeibianhao" :dictOptions="dictOption" >
              </j-search-select-tag>
              {{ selectValue }}
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="材料" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="cailiaono">
              <j-search-select-tag placeholder="请选择材料规格" v-model="model.cailiaono" :dictOptions="dictOption3" >
              </j-search-select-tag>
<!--              {{ model.cailiaono_dictText}}-->
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="批次" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="pici">
              <a-input v-model="model.pici" placeholder="请输入批次"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="供应商" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="gongyingshangdanweibianma">
                <j-search-select-tag placeholder="请选择供应商" v-model="model.gongyingshangdanweibianma" :dictOptions="dictOption2" >
                </j-search-select-tag>
<!--               {{ model.gongyingshangdanweibianma_dictText }}-->
            </a-form-model-item>
          </a-col>

          <a-col :span="24">
            <a-form-model-item label="料仓" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="lcbianhao">
              <j-search-select-tag placeholder="请选择料仓" v-model="model.lcbianhao" :dictOptions="dictOption4" >
              </j-search-select-tag>
<!--              {{ model.lcbianhao_dictText }}-->
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="净重(吨)" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="jingzhongt">
              <a-input v-model="model.jingzhongt" placeholder="请输入净重(吨)"  ></a-input>
            </a-form-model-item>
          </a-col>

          <a-col :span="24">
            <a-form-model-item label="卸货地点" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="liaocangid">
              <a-input v-model="model.liaocangid" placeholder="请输入卸货地点"  ></a-input>
            </a-form-model-item>
          </a-col>

          <a-col :span="24">
            <a-form-model-item label="推送状态" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="taizhangtj">
              <a-input v-model="model.taizhangtj_dictText" placeholder="请输入台账统计状态" style="width: 100%" />
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
  import Vue from "vue";
  import {gongyingshangList, usershebeiList} from "@api/api";

  export default {
    name: 'Wzycljinchanggb1OForm',
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
        dictOption2:[],
        dictOption3:[],
        dictOption4:[],
        selectValue:'',
        selectValue2:'',
        selectValue3:'',
        selectValue4:'',
        nodetype:null,
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
          add: "/yclsl/wzycljinchanggb/add",
          edit: "/yclsl/wzycljinchanggb/editts",
          queryById: "/yclsl/wzycljinchanggb/queryById"
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
      this.gongyingshangData();
      this.cailiaoData();
      this.liaocangData();

    },
    methods: {



      shebeiList:function (){
        var sys_depart_orgcode=Vue.ls.get('SYS_DEPART_ORGCODE');
        usershebeiList({
          sys_depart_orgcode:sys_depart_orgcode,
          sbtypes:'27'
        }).then(res=>{
          this.dictOption=[];
          let result=res.result;
          result.forEach(re=>{
            this.dictOption.push({text:re.sbname,value:re.sbjno})
          })
          //console.log(res)
        })
      },
      gongyingshangData:function (){
        var sys_depart_orgcode=Vue.ls.get('SYS_DEPART_ORGCODE');
        gongyingshangList({
          sys_depart_orgcode:sys_depart_orgcode,
          pageNo:1,
          pageSize:100
        }).then(res=>{
          this.dictOption2=[];
          let result=res.result.records;
          result.forEach(re=>{
            this.dictOption2.push({text:re.gongyingshangname,value:re.guid})
          })
        })
      },
      cailiaoData:function (){
        var sys_depart_orgcode=Vue.ls.get('SYS_DEPART_ORGCODE');
        let params = {
          sys_depart_orgcode:sys_depart_orgcode,
          ne:1,
          pageNo:1,
          pageSize:100
        }
        getAction('/wzcailiaonamedict/wzcailiaonamedict/list',params).then(res=>{
          this.dictOption3=[];
          let result=res.result.records;
          result.forEach(re=>{
            if(re.guigexinghao === null){
              re.guigexinghao =''
            }
            this.dictOption3.push({text:`${re.cailiaoname}${re.guigexinghao}`,value:re.cailiaono})
          })
        })
      },

      liaocangData:function (){
        var sys_depart_orgcode=Vue.ls.get('SYS_DEPART_ORGCODE');
        let params = {
          sys_depart_orgcode:sys_depart_orgcode,
          ne:1,
          pageNo:1,
          pageSize:100
        }
        getAction('/wzliaocang/wzliaocang/list',params).then(res=>{
          this.dictOption4=[];
          let result=res.result.records;
          result.forEach(re=>{
            if(re.guigexinghao === null){
              re.guigexinghao =''
            }
            this.dictOption4.push({text:`${re.sysOrgCode_dictText}-${re.name}${re.guigexinghao}`,value:re.guid})
          })
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