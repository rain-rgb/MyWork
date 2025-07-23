<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form :form="form" slot="detail">
        <a-divider type="horizontal" ><span style="color:#0785fd">基础配置</span></a-divider>
        <a-row>
          <a-col :span="12">
            <a-form-item label="所属部门" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <JselectDqDepart v-decorator="['sysOrgCode']" multi  />
            </a-form-item>
          </a-col>
          <a-col :span="12" >
            <a-form-item label="设备名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-search-select-tag placeholder="请选择设备名称" v-decorator="['shebeino']" :dictOptions="dictOption" >
              </j-search-select-tag>
              {{ selectValue }}
            </a-form-item>
          </a-col>
        </a-row>
          <a-row>
            <a-col :span="12">
              <a-form-item label="搅拌桩预警号码组" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <j-dict-select-tag type="list" v-decorator="['jbzphones']" :trigger-change="true" dictCode="bhz_phone,names,uid,phonesname='9'"
                                   dictTable="bhz_phone" placeholder="请选择搅拌桩预警号码组"/>
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="是否报警" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <j-dict-select-tag type="list" v-decorator="['isCall']" :trigger-change="true" dictCode="is_call" placeholder="请选择是否报警" />
              </a-form-item>
            </a-col>
          </a-row>
        <a-row>
        <a-col :span="12">
          <a-form-model-item label="施工工艺" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="isCall">
            <j-dict-select-tag type="list" v-model="model.sggy" :trigger-change="true" dictCode="sggy" placeholder="请选择施工工艺" style="width: 100%"/>
          </a-form-model-item>
        </a-col>
        </a-row>
          <a-divider type="horizontal" ><span style="color:#0785fd">整桩预警配置</span></a-divider>
          <a-row>
            <a-col :span="12">
              <a-form-item label="设计桩长(m)" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-decorator="['designdep']" placeholder="请输入设计桩长"  ></a-input>
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="每米水泥用量(KG)" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-decorator="['cement']" placeholder="请输入水泥浆用量"  ></a-input>
              </a-form-item>
            </a-col>
          </a-row>
        <a-row>
          <a-col :span="12">
<!--            <a-form-item label="平均灰量" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--              <a-input v-decorator="['pileMinelec']" placeholder="请输入平均灰量"  ></a-input>-->
<!--            </a-form-item>-->
            <a-form-model-item label="平均灰量" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="responsible">
              <a-input v-model="model.pileMinelec" placeholder="请输入平均灰量"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
<!--            <a-form-item label="平均密度" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--              <a-input v-decorator="['pileDensity']" placeholder="请输入平均密度"  ></a-input>-->
<!--            </a-form-item>-->
            <a-form-model-item label="平均密度" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="responsible">
              <a-input v-model="model.pileDensity" placeholder="请输入平均密度"  ></a-input>
            </a-form-model-item>
          </a-col>
        </a-row>
          <a-row>
            <a-col :span="12">
              <a-form-item label="倾角下限" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-decorator="['xx']" placeholder="请输入倾角下限"  ></a-input>
              </a-form-item>
            </a-col>

            <a-col :span="12">
              <a-form-item label="倾角上限" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-decorator="['xs']" placeholder="请输入倾角上限"  ></a-input>
              </a-form-item>
            </a-col>

          </a-row>

        <a-row>
          <a-col :span="12">
            <a-form-item label="平均密度" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['density']" placeholder="请输入平均密度"  ></a-input>
            </a-form-item>
          </a-col>

        </a-row>

        <a-divider type="horizontal" ><span style="color:#0785fd">提钻预警配置</span></a-divider>

        <a-row>
          <a-col :span="12">
            <a-form-item label="速度下限(m/min)" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['uspeedx']" placeholder="请输入提钻速度下限"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="速度上限(m/min)" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['uspeeds']" placeholder="请输入提钻速度上限"  ></a-input>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :span="12">
            <a-form-item label="压力下限(MPa)" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['upressx']" placeholder="请输入提钻压力下限"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="压力上限(MPa)" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['upress']" placeholder="请输入提钻压力上限"  ></a-input>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :span="12">
            <a-form-item label="每米水泥用量(KG)" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['upbetonx']" placeholder="请输入提钻水泥浆用量下限"  ></a-input>
            </a-form-item>
          </a-col>
<!--          <a-col :span="12">-->
<!--            <a-form-item label="水泥用量上限(KG)" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--              <a-input v-decorator="['upbetons']" placeholder="/"  disabled="true" ></a-input>-->
<!--            </a-form-item>-->
<!--          </a-col>-->
        </a-row>

        <a-divider type="horizontal" ><span style="color:#0785fd">下钻预警配置</span></a-divider>


        <a-row>
          <a-col :span="12">
            <a-form-item label="速度下限(m/min)" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['dspeedx']" placeholder="请输入下钻速度下限"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="速度上限(m/min)" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['dspeeds']" placeholder="请输入下钻速度上限"  ></a-input>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :span="12">
            <a-form-item label="压力下限(MPa)" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['dpressx']" placeholder="请输入下钻压力下限"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="压力上限(MPa)" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['dpress']" placeholder="请输入下钻压力上限"  ></a-input>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row>
          <a-col :span="12">
            <a-form-item label="每米水泥用量(KG)" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['downbetonx']" placeholder="请输入下钻水泥浆用量下限"  ></a-input>
            </a-form-item>
          </a-col>
<!--          <a-col :span="12">-->
<!--            <a-form-item label="水泥用量上限(KG)" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--              <a-input v-decorator="['downbetons']" placeholder="/" disabled="true"  ></a-input>-->
<!--            </a-form-item>-->
<!--          </a-col>-->
        </a-row>








        <a-row>
          <a-col v-if="showFlowSubmitButton" :span="24" style="text-align: center">
            <a-button @click="submitForm">提 交</a-button>
          </a-col>
        </a-row>
      </a-form>
    </j-form-container>
  </a-spin>
</template>

<script>

import pick from 'lodash.pick'
import { getAction, httpAction } from '@/api/manage'
import { FormTypes,getRefPromise } from '@/utils/JEditableTableUtil'
import { JEditableTableMixin } from '@/mixins/JEditableTableMixin'
import { validateDuplicateValue } from '@/utils/util'
import JFormContainer from '@/components/jeecg/JFormContainer'
import JDate from '@/components/jeecg/JDate'
import JselectDqDepart from '@/components/jeecgbiz/JselectDqDepart'
import { usershebeiList } from '@api/api'
import { SYS_DEPART_ORGCODE } from '@/store/mutation-types'
import Vue from 'vue'

  export default {
    name: 'DeviceMixpileOneCfgForm',
    components: {
      JFormContainer,
      JDate,
      JselectDqDepart
    },
    props: {
      //流程表单data
      formData: {
        type: Object,
        default: ()=>{},
        required: false
      },
      //表单模式：true流程表单 false普通表单
      formBpm: {
        type: Boolean,
        default: false,
        required: false
      },
      //表单禁用
      disabled: {
        type: Boolean,
        default: false,
        required: false
      }
    },
    data () {
      return {
        dictOption: [],
        selectValue: '',
        form: this.$form.createForm(this),
        model: {},
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
          add: "/devicemixpileoneCfg/deviceMixpileOneCfg/add",
          edit: "/devicemixpileoneCfg/deviceMixpileOneCfg/edit",
          queryById: "/devicemixpileoneCfg/deviceMixpileOneCfg/queryById"
        }
      }
    },
    computed: {
      formDisabled(){
        if(this.formBpm===true){
          if(this.formData.disabled===false){
            return false
          }
          return true
        }
        return this.disabled
      },
      showFlowSubmitButton(){
        if(this.formBpm===true){
          if(this.formData.disabled===false){
            return true
          }
        }
        return false
      }
    },
    created () {
      //如果是流程中表单，则需要加载流程表单data
      this.showFlowData();
      this.shebeiList();
    },
    methods: {
      shebeiList:function (){
        var sys_depart_orgcode=Vue.ls.get('SYS_DEPART_ORGCODE');
        if(this.model.sysOrgCode !== null){
          sys_depart_orgcode = this.model.sysOrgCode
        }
        usershebeiList({
          sys_depart_orgcode:sys_depart_orgcode,
          sbtypes:'16,19,53,54'
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
        this.edit({});
      },
      edit (record) {
        this.form.resetFields();
        this.model = Object.assign({}, record);
        this.visible = true;
        this.$nextTick(() => {
          // this.form.setFieldsValue(pick(this.model,'shebeino','designdep','upbetonx','upbetons','uspeedx','downbetonx','downbetons','dspeedx','cement','xx','xs','jbzphones','isCall','sysOrgCode','createTime','updateTime','isdel','createBy'))
          this.form.setFieldsValue(pick(this.model,'sysOrgCode','shebeino','upbetonx','uspeedx','downbetonx','dspeedx','xx','xs','designdep','cement','jbzphones','isCall','dpressx','dpress','dspeeds','upressx','upress','uspeeds','upbetons','downbetons'))

        })
      },
      //渲染流程表单数据
      showFlowData(){
        if(this.formBpm === true){
          let params = {id:this.formData.dataId};
          getAction(this.url.queryById,params).then((res)=>{
            if(res.success){
              this.edit (res.result);
            }
          });
        }
      },
      submitForm () {
        const that = this;
        // 触发表单验证
        this.form.validateFields((err, values) => {
          if (!err) {
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
            let formData = Object.assign(this.model, values);
            console.log("表单提交数据",formData)
            httpAction(httpurl,formData,method).then((res)=>{
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
      popupCallback(row){
        this.form.setFieldsValue(pick(row,'shebeino','designdep','upbetonx','upbetons','uspeedx','downbetonx','downbetons','dspeedx','cement','xx','xs','jbzphones','isCall','sysOrgCode','createTime','updateTime','isdel','createBy','dpressx','dpress','dspeeds','upressx','upress','uspeeds'))
      },
    }
  }
</script>