<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <a-card  title="基础信息" :bordered="false" :headStyle="{color:'#0785fd'}" :bodyStyle="{padding:'10'}">
            <a-row>
              <a-col :span="12">
                <a-form-model-item label="所属部门" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="sysOrgCode">
                  <JselectDqDepart v-model="model.sysOrgCode"  />
                </a-form-model-item>
              </a-col>
              <a-col :span="12">
                <a-form-model-item label="设备名称" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="devaddr">
                  <!--              <a-input v-model="model.devaddr" placeholder="请输入设备名称"  ></a-input>-->
                  <j-search-select-tag placeholder="请选择设备名称"  v-model="model.devaddr" :dictOptions="dictOption" >
                  </j-search-select-tag>
                </a-form-model-item>
              </a-col>
            </a-row>

            <a-row>
              <a-col :span="12">
                <a-form-model-item label="是否报警" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="isCall">
                  <!--              <a-input-number v-model="model.isCall" placeholder="请输入是否报警:0=报警,1=不报警" style="width: 100%" />-->
                  <j-dict-select-tag  v-model="model.isCall"  dictCode="is_call" placeholder="请选择是否报警" />
                </a-form-model-item>
              </a-col>
              <a-col :span="12">
                <a-form-model-item label="初级预警号码组" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="primaryPhones">
                  <!--              <a-input v-model="model.primaryPhones" placeholder="请输入预警号码"  ></a-input>-->
                  <j-dict-select-tag type="list" v-model="model.primaryPhones" dictCode="bhz_phone,names,uid,phonesname='13'" dictTable="bhz_phone" placeholder="请选择初级预警号码组"/>
                </a-form-model-item>
              </a-col>
            </a-row>

            <a-row>
              <a-col :span="12">
                <a-form-model-item label="中级预警号码组" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="middlePhones">
                  <!--              <a-input v-model="model.primaryPhones" placeholder="请输入预警号码"  ></a-input>-->
                  <j-dict-select-tag type="list" v-model="model.middlePhones" dictCode="bhz_phone,names,uid,phonesname='14'" dictTable="bhz_phone" placeholder="请选择中级预警号码组"/>
                </a-form-model-item>
              </a-col>
              <a-col :span="12">
                <a-form-model-item label="高级预警号码组" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="advancePhones">
                  <!--              <a-input v-model="model.primaryPhones" placeholder="请输入预警号码"  ></a-input>-->
                  <j-dict-select-tag type="list" v-model="model.advancePhones" dictCode="bhz_phone,names,uid,phonesname='18'" dictTable="bhz_phone" placeholder="请选择高级预警号码组"/>
                </a-form-model-item>
              </a-col>
            </a-row>

          </a-card>

          <a-card title="预警值配置信息" :bordered="false" :headStyle="{color:'#0785fd'}" :bodyStyle="{padding:'10'}">
            <a-row>
              <a-col :span="8">
                <a-form-model-item label="PM2.5初级标准值(ug/m³)" :labelCol="labelCol1" :wrapperCol="wrapperCol1" prop="pm25y">
                  <a-input v-model="model.pm25y" placeholder="请输入PM2.5(ug/m³)"  ></a-input>
                </a-form-model-item>
              </a-col>
              <a-col :span="8">
                <a-form-model-item label="PM2.5中级标准值(ug/m³)" :labelCol="labelCol1" :wrapperCol="wrapperCol1" prop="pm25l">
                  <a-input v-model="model.pm25l" placeholder="请输入PM2.5(ug/m³)"  ></a-input>
                </a-form-model-item>
              </a-col>
              <a-col :span="8">
                <a-form-model-item label="PM2.5高级标准值(ug/m³)" :labelCol="labelCol1" :wrapperCol="wrapperCol1" prop="pm25c">
                  <a-input v-model="model.pm25c" placeholder="请输入PM2.5(ug/m³)"  ></a-input>
                </a-form-model-item>
              </a-col>
            </a-row>
            <a-row>
              <a-col :span="8">
                <a-form-model-item label="PM10初级标准值(ug/m³)" :labelCol="labelCol1" :wrapperCol="wrapperCol1" prop="pm10y">
                  <a-input v-model="model.pm10y" placeholder="请输入PM10(ug/m³)"  ></a-input>
                </a-form-model-item>
              </a-col>
              <a-col :span="8">
                <a-form-model-item label="PM10中级标准值(ug/m³)" :labelCol="labelCol1" :wrapperCol="wrapperCol1" prop="pm10l">
                  <a-input v-model="model.pm10l" placeholder="请输入PM10(ug/m³)"  ></a-input>
                </a-form-model-item>
              </a-col>
              <a-col :span="8">
                <a-form-model-item label="PM10高级标准值(ug/m³)" :labelCol="labelCol1" :wrapperCol="wrapperCol1" prop="pm10c">
                  <a-input v-model="model.pm10c" placeholder="请输入PM10(ug/m³)"  ></a-input>
                </a-form-model-item>
              </a-col>
            </a-row>

            <a-row>
              <a-divider type="horizontal" ><span style="color:#0785fd">白天</span></a-divider>
              <a-col :span="8">
                <a-form-model-item label="噪声初级标准值(dB)" :labelCol="labelCol1" :wrapperCol="wrapperCol1" prop="noise">
                  <a-input v-model="model.noisey" placeholder="请输入噪声(dB)"  ></a-input>
                </a-form-model-item>
              </a-col>
              <a-col :span="8">
                <a-form-model-item label="噪声中级标准值(dB)" :labelCol="labelCol1" :wrapperCol="wrapperCol1" prop="noise">
                  <a-input v-model="model.noisel" placeholder="请输入噪声(dB)"  ></a-input>
                </a-form-model-item>
              </a-col>
              <a-col :span="8">
                <a-form-model-item label="噪声高级标准值(dB)" :labelCol="labelCol1" :wrapperCol="wrapperCol1" prop="noise">
                  <a-input v-model="model.noisec" placeholder="请输入噪声(dB)"  ></a-input>
                </a-form-model-item>
              </a-col>
            </a-row>
            <a-row>
              <a-divider type="horizontal" ><span style="color:#0785fd">夜间</span></a-divider>
              <a-col :span="8">
                <a-form-model-item label="噪声初级标准值(dB)" :labelCol="labelCol1" :wrapperCol="wrapperCol1" prop="noise">
                  <a-input v-model="model.noiseyn" placeholder="请输入噪声(dB)"  ></a-input>
                </a-form-model-item>
              </a-col>
              <a-col :span="8">
                <a-form-model-item label="噪声中级标准值(dB)" :labelCol="labelCol1" :wrapperCol="wrapperCol1" prop="noise">
                  <a-input v-model="model.noiseln" placeholder="请输入噪声(dB)"  ></a-input>
                </a-form-model-item>
              </a-col>
              <a-col :span="8">
                <a-form-model-item label="噪声高级标准值(dB)" :labelCol="labelCol1" :wrapperCol="wrapperCol1" prop="noise">
                  <a-input v-model="model.noisecn" placeholder="请输入噪声(dB)"  ></a-input>
                </a-form-model-item>
              </a-col>
            </a-row>

          </a-card>

<!--          <a-col :span="24">-->
<!--            <a-form-model-item label="温度(℃)" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="temperature">-->
<!--              <a-input v-model="model.temperature" placeholder="请输入温度(℃)"  ></a-input>-->
<!--            </a-form-model-item>-->
<!--          </a-col>-->
<!--          <a-col :span="24">-->
<!--            <a-form-model-item label="湿度(%RH)（最高100）" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="humidity">-->
<!--              <a-input v-model="model.humidity" placeholder="请输入湿度(%RH)（最高100）"  ></a-input>-->
<!--            </a-form-model-item>-->
<!--          </a-col>-->
<!--          <a-col :span="24">-->
<!--            <a-form-model-item label="风力（最高18）" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="wind">-->
<!--              <a-input v-model="model.wind" placeholder="请输入风力（最高18）"  ></a-input>-->
<!--            </a-form-model-item>-->
<!--          </a-col>-->
<!--          <a-col :span="24">-->
<!--            <a-form-model-item label="风速(m/s) （最高不大于100）" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="windspeed">-->
<!--              <a-input v-model="model.windspeed" placeholder="请输入风速(m/s) （最高不大于100）"  ></a-input>-->
<!--            </a-form-model-item>-->
<!--          </a-col>-->
<!--          <a-col :span="24">-->
<!--            <a-form-model-item label="风向  （风向度最高360）" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="winddirection">-->
<!--              <a-input v-model="model.winddirection" placeholder="请输入风向  （风向度最高360）"  ></a-input>-->
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
  import JselectDqDepart from '@comp/jeecgbiz/JselectDqDepart'
  import { usershebeiList } from '@api/api'
  import Vue from 'vue'

  export default {
    name: 'DevicehistoryYujingForm',
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
        labelCol1: {
          xs: { span: 24 },
          sm: { span: 11 },
        },
        wrapperCol1: {
          xs: { span: 24 },
          sm: { span: 10 },
        },
        confirmLoading: false,
        validatorRules: {
        },
        url: {
          add: "/devicehistoryyujing/devicehistoryYujing/add",
          edit: "/devicehistoryyujing/devicehistoryYujing/edit",
          queryById: "/devicehistoryyujing/devicehistoryYujing/queryById"
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
          sbtypes:'15'
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