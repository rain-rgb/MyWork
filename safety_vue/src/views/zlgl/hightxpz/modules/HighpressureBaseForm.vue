<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form :form="form" slot="detail">
        <a-row>
          <a-col :span="24">
            <a-form-item label="设备编码" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['equipmentcode']" placeholder="请输入设备编码"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="桩号" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['constructionstakenum']" placeholder="请输入桩号"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="客户端当前记录产生时间（实施采集时间）" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-date placeholder="请选择客户端当前记录产生时间（实施采集时间）" v-decorator="['realtime']" :trigger-change="true" style="width: 100%" />
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="开始时间（该根桩的开始时间）" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <j-date placeholder="请选择开始时间（该根桩的开始时间）" v-decorator="['starttime']" :trigger-change="true" style="width: 100%" />
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="记录状态：1-开始，2-暂停，3-结束,4-报警；" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="['recordstatus']" placeholder="请输入记录状态：1-开始，2-暂停，3-结束,4-报警；" style="width: 100%" />
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="钻杆状态：1-下钻，2-提钻，3-复下，4-复提；" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="['pipestatus']" placeholder="请输入钻杆状态：1-下钻，2-提钻，3-复下，4-复提；" style="width: 100%" />
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="喷浆状态：1-喷浆，0-止喷" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="['nozzlestatus']" placeholder="请输入喷浆状态：1-喷浆，0-止喷" style="width: 100%" />
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="喷射模式：1-单管法，6-二重管法，3-三重管法" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="['pumpmode']" placeholder="请输入喷射模式：1-单管法，6-二重管法，3-三重管法" style="width: 100%" />
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="记录模式：0-深度模式，1-时间模式" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="['recordmode']" placeholder="请输入记录模式：0-深度模式，1-时间模式" style="width: 100%" />
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="钻杆模式：0-单杆模式，1-接杆模式" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="['pipemode']" placeholder="请输入钻杆模式：0-单杆模式，1-接杆模式" style="width: 100%" />
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="水灰比" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="['waterashratio']" placeholder="请输入水灰比" style="width: 100%" />
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="记录间隔：间隔的数值，深度对应单位 cm，时间对应单位 s" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="['recordinterval']" placeholder="请输入记录间隔：间隔的数值，深度对应单位 cm，时间对应单位 s" style="width: 100%" />
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="段浆量(L)" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="['segmentpulp']" placeholder="请输入段浆量(L)" style="width: 100%" />
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="段灰量(kg)" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="['segmentash']" placeholder="请输入段灰量(kg)" style="width: 100%" />
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="累计浆量（L）" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="['totalpulp']" placeholder="请输入累计浆量（L）" style="width: 100%" />
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="累计灰量（KG）" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="['totalash']" placeholder="请输入累计灰量（KG）" style="width: 100%" />
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="累计水量(L)" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="['totalwater']" placeholder="请输入累计水量(L)" style="width: 100%" />
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="累计气量(m3)" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="['totalair']" placeholder="请输入累计气量(m3)" style="width: 100%" />
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="累计返浆量(L)" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="['totalreturnpulp']" placeholder="请输入累计返浆量(L)" style="width: 100%" />
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="水喷压(MPa)" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="['waterpressure']" placeholder="请输入水喷压(MPa)" style="width: 100%" />
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="水流量(L)" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="['waterflow']" placeholder="请输入水流量(L)" style="width: 100%" />
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="气流量(m3)" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="['airflow']" placeholder="请输入气流量(m3)" style="width: 100%" />
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="气喷压(MPa)" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="['airpressure']" placeholder="请输入气喷压(MPa)" style="width: 100%" />
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="返浆量(L)" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="['returnpulp']" placeholder="请输入返浆量(L)" style="width: 100%" />
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="返浆压力(MPa)" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="['returnpulppressure']" placeholder="请输入返浆压力(MPa)" style="width: 100%" />
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="密度(g/cm3)" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="['density']" placeholder="请输入密度(g/cm3)" style="width: 100%" />
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="浆喷压(MPa)" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="['pressure']" placeholder="请输入浆喷压(MPa)" style="width: 100%" />
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="下钻/提钻速度(cm/min)" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="['speed']" placeholder="请输入下钻/提钻速度(cm/min)" style="width: 100%" />
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="钻速(r/min)" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="['drillingrate']" placeholder="请输入钻速(r/min)" style="width: 100%" />
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="深度(m)" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="['currentdepth']" placeholder="请输入深度(m)" style="width: 100%" />
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="最大深度(m)" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="['maxdepth']" placeholder="请输入最大深度(m)" style="width: 100%" />
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="流量" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="['flow']" placeholder="请输入流量" style="width: 100%" />
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="前后倾角" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="['xangle']" placeholder="请输入前后倾角" style="width: 100%" />
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="左右倾角" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="['yangle']" placeholder="请输入左右倾角" style="width: 100%" />
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="倾斜度" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="['gradient']" placeholder="请输入倾斜度" style="width: 100%" />
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="电流(A)" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="['electricity']" placeholder="请输入电流(A)" style="width: 100%" />
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="经度" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="['longitudu']" placeholder="请输入经度" style="width: 100%" />
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="纬度" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input-number v-decorator="['latitude']" placeholder="请输入纬度" style="width: 100%" />
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="关联id" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-decorator="['guid']" placeholder="请输入关联id"  ></a-input>
            </a-form-item>
          </a-col>
          <a-col v-if="showFlowSubmitButton" :span="24" style="text-align: center">
            <a-button @click="submitForm">提 交</a-button>
          </a-col>
        </a-row>
      </a-form>
    </j-form-container>
  </a-spin>
</template>

<script>

  import { httpAction, getAction } from '@/api/manage'
  import pick from 'lodash.pick'
  import { validateDuplicateValue } from '@/utils/util'
  import JFormContainer from '@/components/jeecg/JFormContainer'
  import JDate from '@/components/jeecg/JDate'  

  export default {
    name: 'HighpressureBaseForm',
    components: {
      JFormContainer,
      JDate,
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
          add: "/Hightxpz/highpressureBase/add",
          edit: "/Hightxpz/highpressureBase/edit",
          queryById: "/Hightxpz/highpressureBase/queryById"
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
    },
    methods: {
      add () {
        this.edit({});
      },
      edit (record) {
        this.form.resetFields();
        this.model = Object.assign({}, record);
        this.visible = true;
        this.$nextTick(() => {
          this.form.setFieldsValue(pick(this.model,'equipmentcode','constructionstakenum','realtime','starttime','recordstatus','pipestatus','nozzlestatus','pumpmode','recordmode','pipemode','waterashratio','recordinterval','segmentpulp','segmentash','totalpulp','totalash','totalwater','totalair','totalreturnpulp','waterpressure','waterflow','airflow','airpressure','returnpulp','returnpulppressure','density','pressure','speed','drillingrate','currentdepth','maxdepth','flow','xangle','yangle','gradient','electricity','longitudu','latitude','guid'))
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
        this.form.setFieldsValue(pick(row,'equipmentcode','constructionstakenum','realtime','starttime','recordstatus','pipestatus','nozzlestatus','pumpmode','recordmode','pipemode','waterashratio','recordinterval','segmentpulp','segmentash','totalpulp','totalash','totalwater','totalair','totalreturnpulp','waterpressure','waterflow','airflow','airpressure','returnpulp','returnpulppressure','density','pressure','speed','drillingrate','currentdepth','maxdepth','flow','xangle','yangle','gradient','electricity','longitudu','latitude','guid'))
      },
    }
  }
</script>