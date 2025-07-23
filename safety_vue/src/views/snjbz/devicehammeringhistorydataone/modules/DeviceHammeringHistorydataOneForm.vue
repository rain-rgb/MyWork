<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <a-col :span="24">
            <a-form-model-item label="设备名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-input v-model="model.shebeino_dictText" placeholder="请输入设备名称"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="里程" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="pileMileage">
              <a-input v-model="model.pileMileage" placeholder="请输入里程"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="桩号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="pileNo">
              <a-input v-model="model.pileNo" placeholder="请输入桩号"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="设计桩长" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="pileDesigndep">
              <a-input-number v-model="model.pileDesigndep" placeholder="请输入设计桩长" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="锤击次数" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="pileSecond">
              <a-input-number v-model="model.pileSecond" placeholder="请输入锤击次数" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="施工长度(m)" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="pileRealdep">
              <a-input v-model="model.pileRealdep" placeholder="请输入施工长度(m)"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="成桩时间" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="pileWorktime">
              <a-input v-model="model.pileWorktime" placeholder="请输入成桩时间"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="最大垂直度(%)" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="pileY">
              <a-input v-model="model.pileY" placeholder="请输入最大垂直度(%)"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="结束时间" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="pileTime">
              <a-input v-model="model.pileTime" placeholder="请输入结束时间"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="开始时间" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="pileStarttime">
              <a-input v-model="model.pileStarttime" placeholder="请输入开始时间"  ></a-input>
            </a-form-model-item>
          </a-col>
<!--          <a-col :span="24">-->
<!--            <a-form-model-item label="关联软基任务单 device_mixpile_rwd" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="rjrwd">-->
<!--              <a-input v-model="model.rjrwd" placeholder="请输入关联软基任务单 device_mixpile_rwd"  ></a-input>-->
<!--            </a-form-model-item>-->
<!--          </a-col>-->
<!--          <a-col :span="24">-->
<!--            <a-form-model-item label="唯一码" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="uuid">-->
<!--              <a-input v-model="model.uuid" placeholder="请输入唯一码"  ></a-input>-->
<!--            </a-form-model-item>-->
<!--          </a-col>-->
          <a-col :span="24">
            <a-form-model-item label="数据时间" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="datatime">
              <j-date placeholder="请选择数据时间" v-model="model.datatime"  style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="桩经度" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="pileLgd">
              <a-input v-model="model.pileLgd" placeholder="请输入桩经度"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="桩纬度" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="pileLtd">
              <a-input v-model="model.pileLtd" placeholder="请输入桩纬度"  ></a-input>
            </a-form-model-item>
          </a-col>
<!--          <a-col :span="24">-->
<!--            <a-form-model-item label="是否统计 0 未统计 1 已统计" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="istongji">-->
<!--              <a-input-number v-model="model.istongji" placeholder="请输入是否统计 0 未统计 1 已统计" style="width: 100%" />-->
<!--            </a-form-model-item>-->
<!--          </a-col>-->
<!--          <a-col :span="24">-->
<!--            <a-form-model-item label="超标等级0：合格；1不合格" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="chaobiaodengji">-->
<!--              <a-input-number v-model="model.chaobiaodengji" placeholder="请输入超标等级0：合格；1不合格" style="width: 100%" />-->
<!--            </a-form-model-item>-->
<!--          </a-col>-->
<!--          <a-col :span="24">-->
<!--            <a-form-model-item label="图片地址" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="address">-->
<!--              <a-input v-model="model.address" placeholder="请输入图片地址"  ></a-input>-->
<!--            </a-form-model-item>-->
<!--          </a-col>-->
<!--          <a-col :span="24">-->
<!--            <a-form-model-item label="0未处置；10：处置；20：闭合，30驳回" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="overproofStatus">-->
<!--              <a-input-number v-model="model.overproofStatus" placeholder="请输入0未处置；10：处置；20：闭合，30驳回" style="width: 100%" />-->
<!--            </a-form-model-item>-->
<!--          </a-col>-->
<!--          <a-col :span="24">-->
<!--            <a-form-model-item label="异常原因" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="ycyy">-->
<!--              <a-input v-model="model.ycyy" placeholder="请输入异常原因"  ></a-input>-->
<!--            </a-form-model-item>-->
<!--          </a-col>-->
<!--          <a-col :span="24">-->
<!--            <a-form-model-item label="视频地址" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="spurl">-->
<!--              <a-input v-model="model.spurl" placeholder="请输入视频地址"  ></a-input>-->
<!--            </a-form-model-item>-->
<!--          </a-col>-->
<!--          <a-col :span="24">-->
<!--            <a-form-model-item label="质保资料数据推送0未推送1推送成功40推送失败" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="zbzl">-->
<!--              <a-input-number v-model="model.zbzl" placeholder="请输入质保资料数据推送0未推送1推送成功40推送失败" style="width: 100%" />-->
<!--            </a-form-model-item>-->
<!--          </a-col>-->
<!--          <a-col :span="24">-->
<!--            <a-form-model-item label="管桩评级" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="gzpj">-->
<!--              <a-input v-model="model.gzpj" placeholder="请输入管桩评级"  ></a-input>-->
<!--            </a-form-model-item>-->
<!--          </a-col>-->
<!--          <a-col :span="24">-->
<!--            <a-form-model-item label="横截面积" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="csarea">-->
<!--              <a-input-number v-model="model.csarea" placeholder="请输入横截面积" style="width: 100%" />-->
<!--            </a-form-model-item>-->
<!--          </a-col>-->
<!--          <a-col :span="24">-->
<!--            <a-form-model-item label="吨位" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="tonnage">-->
<!--              <a-input-number v-model="model.tonnage" placeholder="请输入吨位" style="width: 100%" />-->
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

  export default {
    name: 'DeviceHammeringHistorydataOneForm',
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
          add: "/devicehammeringhistorydataone/deviceHammeringHistorydataOne/add",
          edit: "/devicehammeringhistorydataone/deviceHammeringHistorydataOne/edit",
          queryById: "/devicehammeringhistorydataone/deviceHammeringHistorydataOne/queryById"
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