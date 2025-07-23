<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <a-col :span="24">
            <a-form-model-item label="设备名称" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="deviceCode">
              <a-input v-model="model.deviceCode_dictText" placeholder="请输入设备名称"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="设备类型" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="deviceType">
              <a-input-number v-model="model.deviceType" placeholder="请输入设备类型" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="大车横向行程" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="bigTransverseCarroute">
              <a-input-number v-model="model.bigTransverseCarroute" placeholder="请输入大车横向行程(m)" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="大车纵向行程" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="bigLongitudinalCarroute">
              <a-input-number v-model="model.bigLongitudinalCarroute" placeholder="请输入大车纵向行程(m)" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="数据上传时间" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="bridgedate">
              <j-date placeholder="请选择数据上传时间" v-model="model.bridgedate"  style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="支腿垂直度" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="verticalityLeg">
              <a-input-number v-model="model.verticalityLeg" placeholder="请输入支腿垂直度" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="大车水平度" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="carLevelness">
              <a-input-number v-model="model.carLevelness" placeholder="请输入大车水平度" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="1#天车高度" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="craneHeight1">
              <a-input-number v-model="model.craneHeight1" placeholder="请输入1#天车高度" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="1#天车吊重" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="craneCrane1">
              <a-input-number v-model="model.craneCrane1" placeholder="请输入1#天车吊重" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="1#天车横向行程" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="transverseCarroute1">
              <a-input-number v-model="model.transverseCarroute1" placeholder="请输入1#天车横向行程(m)" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="1#天车纵向行程" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="longitudinalCarroute1">
              <a-input-number v-model="model.longitudinalCarroute1" placeholder="请输入1#天车纵向行程(m)" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="1#天车吊钩载荷状态" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="hookstatus1">
              <a-input-number v-model="model.hookstatus1" placeholder="请输入1#天车吊钩载荷状态" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="1#天车吊钩制动器1状态" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="hookbrake1status1">
              <a-input-number v-model="model.hookbrake1status1" placeholder="请输入1#天车吊钩制动器1状态" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="1#天车吊钩制动器2状态" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="hookbrake2status1">
              <a-input-number v-model="model.hookbrake2status1" placeholder="请输入1#天车吊钩制动器2状态" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="1#天车左限位" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="carleftlimit1">
              <a-input-number v-model="model.carleftlimit1" placeholder="请输入1#天车左限位" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="1#天车右限位" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="carrightlimit1">
              <a-input-number v-model="model.carrightlimit1" placeholder="请输入1#天车右限位" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="1#天车前限位" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="carfrontlimit1">
              <a-input-number v-model="model.carfrontlimit1" placeholder="请输入1#天车前限位" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="1#天车后限位" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="carbacklimit1">
              <a-input-number v-model="model.carbacklimit1" placeholder="请输入1#天车后限位" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="门限位" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="doorLimit">
              <a-input-number v-model="model.doorLimit" placeholder="请输入门限位" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="抗风防滑状态" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="windAntiskidstatus">
              <a-input-number v-model="model.windAntiskidstatus" placeholder="请输入抗风防滑状态" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="电缆卷筒状态" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="wireDrumstatus">
              <a-input-number v-model="model.wireDrumstatus" placeholder="请输入电缆卷筒状态" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="累计时间" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="allTime">
              <a-input-number v-model="model.allTime" placeholder="请输入累计时间" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="循环使用次数" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="allTimes">
              <a-input-number v-model="model.allTimes" placeholder="请输入循环使用次数" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="预留1" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="reservedOne">
              <a-input-number v-model="model.reservedOne" placeholder="请输入预留1" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="预留2" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="reservedTwo">
              <a-input-number v-model="model.reservedTwo" placeholder="请输入预留2" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="2#天车高度" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="craneHeight2">
              <a-input-number v-model="model.craneHeight2" placeholder="请输入2#天车高度" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="2#天车吊重" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="craneCrane2">
              <a-input-number v-model="model.craneCrane2" placeholder="请输入2#天车吊重" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="2#天车横向行程" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="transverseCarroute2">
              <a-input-number v-model="model.transverseCarroute2" placeholder="请输入2#天车横向行程(m)" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="2#天车纵向行程" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="longitudinalCarroute2">
              <a-input-number v-model="model.longitudinalCarroute2" placeholder="请输入2#天车纵向行程(m)" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="2#天车吊钩载荷状态" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="hookstatus2">
              <a-input-number v-model="model.hookstatus2" placeholder="请输入2#天车吊钩载荷状态" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="2#天车吊钩制动器1状态" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="hookbrake1status2">
              <a-input-number v-model="model.hookbrake1status2" placeholder="请输入2#天车吊钩制动器1状态" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="2#天车吊钩制动器2状态" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="hookbrake2status2">
              <a-input-number v-model="model.hookbrake2status2" placeholder="请输入2#天车吊钩制动器2状态" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="2#天车左限位" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="carleftlimit2">
              <a-input-number v-model="model.carleftlimit2" placeholder="请输入2#天车左限位" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="2#天车右限位" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="carrightlimit2">
              <a-input-number v-model="model.carrightlimit2" placeholder="请输入2#天车右限位" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="2#天车前限位" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="carfrontlimit2">
              <a-input-number v-model="model.carfrontlimit2" placeholder="请输入2#天车前限位" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="2#天车后限位" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="carbacklimit2">
              <a-input-number v-model="model.carbacklimit2" placeholder="请输入2#天车后限位" style="width: 100%" />
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
    name: 'DeviceBridgeRealdataForm',
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
          add: "/jiaqiaoji/deviceBridgeRealdata/add",
          edit: "/jiaqiaoji/deviceBridgeRealdata/edit",
          queryById: "/jiaqiaoji/deviceBridgeRealdata/queryById"
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