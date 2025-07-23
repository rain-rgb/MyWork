<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <a-col :span="24">
            <a-form-item label="所属部门" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="sysorgcode">
              <JselectDqDepart v-model="model.sysorgcode"   ::multi="false"  />
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="材料类型" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="cailiaotype">
              <j-dict-select-tag  v-model="model.cailiaotype"  dictCode="nodeType" placeholder="请选择材料类型" @change="getggxhList"/>
            </a-form-model-item>
          </a-col>
<!--          <a-col :span="24">-->
<!--            <a-form-model-item label="材料类型" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="cailiaotype">-->
<!--              <a-input v-model="model.cailiaotype" placeholder="请输入材料类型"  ></a-input>-->
<!--            </a-form-model-item>-->
<!--          </a-col>-->
          <a-col :span="24">
            <a-form-model-item label="规格型号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="ggxh">
              <j-search-select-tag placeholder="请选择目的地" v-model="model.ggxh" :dictOptions="ggxhdictOption" @change="getclno" >
              </j-search-select-tag>
<!--              <a-input v-model="model.ggxh" placeholder="请输入材料规格型号"  ></a-input>-->
            </a-form-model-item>
          </a-col>
<!--          <a-col :span="24">-->
<!--            <a-form-model-item label="材料编号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="cailaiobh">-->
<!--              <a-input v-model="model.cailaiobh" placeholder="请输入材料编号"  ></a-input>-->
<!--            </a-form-model-item>-->
<!--          </a-col>-->

          <a-col :span="24">
            <a-form-model-item label="采购数量" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="shuliang">
              <a-input v-model="model.shuliang" placeholder="请输入数量（单位吨）"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="要求到货时间" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="endtime">
              <j-date placeholder="请选择要求到货时间" v-model="model.endtime"  style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="目的地" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="mudidi">
              <j-search-select-tag placeholder="请选择目的地" v-model="model.mudidi" :dictOptions="dictOption" >
              </j-search-select-tag>
<!--              {{ selectValue }}-->
            </a-form-item>
<!--            <a-form-model-item label="目的地" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="mudidi">-->
<!--              <a-input v-model="model.mudidi" placeholder="请输入目的地"  ></a-input>-->
<!--            </a-form-model-item>-->
          </a-col>

          <a-col :span="24">
                  <a-form-model-item label="供应商" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="gysguid">
                    <j-search-select-tag placeholder="请选择供应商" v-model="model.gysguid" :dictOptions="gysdictOption" @change="getOption"  >
                    </j-search-select-tag>
<!--                     <a-input v-model="model.gysguid" placeholder="请输入供应商"  ></a-input>-->
                   </a-form-model-item>
            </a-col>

          <a-col :span="24">
            <a-form-model-item label="厂家联系人" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="lianxr">
              <a-input v-model="model.lianxr" placeholder="请输入厂家联系人"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="联系人号码" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="phone">
              <a-input v-model="model.phone" placeholder="请输入厂家联系人手机号"  ></a-input>
            </a-form-model-item>
          </a-col>


          <a-col :span="24">
          <a-form-model-item
            v-show="show"
            :labelCol="labelCol"
            :wrapperCol="wrapperCol"
            label="是否短信通知">
            <a-switch checkedChildren="是" unCheckedChildren="否" v-model="model.ismsg"/>
          </a-form-model-item>
          </a-col>

          <a-col :span="24">
            <a-form-model-item label="备注" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="beizhu">
              <a-input v-model="model.beizhu" placeholder="请输入备注"  ></a-input>
            </a-form-model-item>
          </a-col>



<!--          <a-col :span="24">-->
<!--            <a-form-model-item label="收货部门" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="shouhuodp">-->
<!--              <a-input v-model="model.shouhuodp" placeholder="请输入收货部门"  ></a-input>-->
<!--            </a-form-model-item>-->
<!--          </a-col>-->
<!--          <a-col :span="24">-->
<!--            <a-form-model-item label="订单所属组织机构" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="sysorgcode">-->
<!--              <a-input v-model="model.sysorgcode" placeholder="请输入订单所属组织机构"  ></a-input>-->
<!--            </a-form-model-item>-->
<!--          </a-col>-->
<!--          <a-col :span="24">-->
<!--            <a-form-model-item label="收货时间" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="shouhuotime">-->
<!--              <j-date placeholder="请选择收货时间" v-model="model.shouhuotime"  style="width: 100%" />-->
<!--            </a-form-model-item>-->
<!--          </a-col>-->
<!--          <a-col :span="24">-->
<!--            <a-form-model-item label="收料检验员" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="shouliaojy">-->
<!--              <a-input v-model="model.shouliaojy" placeholder="请输入收料检验员"  ></a-input>-->
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
  import JFormContainer from '@/components/jeecg/JFormContainer'
  import JDate from '@/components/jeecg/JDate'
  import JselectDqDepart from '@comp/jeecgbiz/JselectDqDepart'
  import { mudidiList,gysList,guigeList } from '@api/api'
  import JSuperQuery from '@/components/jeecg/JSuperQuery.vue'


  export default {
    name: 'SyshrwdForm',
    components: {
      JSuperQuery,
      JFormContainer,JDate,JselectDqDepart
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
        ggxhdictOption:[],
        gysdictOption:[],
        selectValue: '',
        dictOption: [],
        show:true,
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
          add: "/syshrwd/syshrwd/add",
          edit: "/syshrwd/syshrwd/edit",
          queryById: "/syshrwd/syshrwd/queryById"
        }
      }
    },
    computed: {
      formDisabled(){
        return this.disabled
      },
    },
    created () {
      this.getggxhList();
      this.getmudidiList();
      this.getgysList();
       //备份model原始值
      this.modelDefault = JSON.parse(JSON.stringify(this.model));

    },
    methods: {

      getclno:function (option){
        this.ggxhdictOption.forEach(re=>{
          if(re.value == option){
            this.model.cailaiobh = re.clno
            // console.log("option",re)
            return
          }
        })
      },

      async getggxhList(){
        guigeList({
          iselocks:0,
          nodetype:this.model.cailiaotype
        }).then(res=>{
          this.ggxhdictOption=[];
          let result=res.result;
          result.forEach(re=>{
            this.ggxhdictOption.push({text:re.guigexinghao,value:re.guigexinghao,clno:re.guid})
          })
          //console.log(res)
        })
      },


      getOption:function (option){

        this.gysdictOption.forEach(re=>{
          if(re.value == option){
            this.model.lianxr = re.lxr
            this.model.phone = re.phone
            // console.log("option",re)
             return
          }
        })
      },

      getmudidiList:function (){
        mudidiList().then(res=>{
          this.dictOption=[];
          let result=res.result;
          result.forEach(re=>{
            this.dictOption.push({text:re.departname,value:re.sysOrgCode})
          })
          //console.log(res)
        })
      },

      getgysList:function (){
        gysList().then(res=>{
          this.gysdictOption=[];
          let result=res.result;
          result.forEach(re=>{
            this.gysdictOption.push({text:re.gongyingshangname,value:re.guid,lxr:re.lianxiren,phone:re.lianxidianhua})
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