<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <a-col :span="24">
            <a-form-model-item label="设备编号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="deviceCode">
              <a-input v-model="model.deviceCode_dictText" placeholder="请输入设备编号"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="设备类型" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="deviceType">
              <a-input-number v-model="model.deviceType" placeholder="请输入设备类型2桥门机3架桥机" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="主钩小车位移" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="mainHookroute">
              <a-input-number v-model="model.mainHookroute" placeholder="请输入主钩小车位移" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="数据上传时间" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="towerdate">
              <j-date placeholder="请选择数据上传时间" v-model="model.towerdate"  style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="主钩吊钩高度" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="mainHookheight">
              <a-input-number v-model="model.mainHookheight" placeholder="请输入主钩吊钩高度" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="副钩吊钩高度" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="reservedVicehookheight">
              <a-input-number v-model="model.reservedVicehookheight" placeholder="请输入副钩吊钩高度" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="主钩吊钩重量" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="mainHookload">
              <a-input-number v-model="model.mainHookload" placeholder="请输入主钩吊钩重量" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="副钩吊钩重量" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="reservedVicehookload">
              <a-input-number v-model="model.reservedVicehookload" placeholder="请输入副钩吊钩重量" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="主钩力矩值" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="mainHookmoment">
              <a-input-number v-model="model.mainHookmoment" placeholder="请输入主钩力矩值" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="副钩小车位移" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="reservedViceroute">
              <a-input-number v-model="model.reservedViceroute" placeholder="请输入副钩小车位移" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="实时风速" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="windSpeed">
              <a-input-number v-model="model.windSpeed" placeholder="请输入实时风速" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="副钩力矩值" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="reservedVicemoment">
              <a-input-number v-model="model.reservedVicemoment" placeholder="请输入副钩力矩值" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="回转角度" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="angleRotation">
              <a-input-number v-model="model.angleRotation" placeholder="请输入回转角度" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="主钩吊钩载荷状态0重量正常1重量预警" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="mainHookstatus">
              <a-input-number v-model="model.mainHookstatus" placeholder="请输入主钩吊钩载荷状态0重量正常1重量预警" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="副钩吊钩载荷状态0重量正常1重量预警" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="reservedVicehookstatus">
              <a-input-number v-model="model.reservedVicehookstatus" placeholder="请输入副钩吊钩载荷状态0重量正常1重量预警" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="主钩吊钩载荷状态0重量正常1吊重超重" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="mainHookstatus2">
              <a-input-number v-model="model.mainHookstatus2" placeholder="请输入主钩吊钩载荷状态0重量正常1吊重超重" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="副钩吊钩载荷状态0重量正常1吊重超重" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="reservedVicehookstatus2">
              <a-input-number v-model="model.reservedVicehookstatus2" placeholder="请输入副钩吊钩载荷状态0重量正常1吊重超重" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="幅度" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="amplitude">
              <a-input-number v-model="model.amplitude" placeholder="请输入幅度" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="累计时间(单位小时)" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="allTime">
              <a-input-number v-model="model.allTime" placeholder="请输入累计时间(单位小时)" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="循环使用次数" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="allTimes">
              <a-input-number v-model="model.allTimes" placeholder="请输入循环使用次数" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="碰撞危险" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="collisionHazard">
              <a-input-number v-model="model.collisionHazard" placeholder="请输入碰撞危险" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="预留" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="reservedOne">
              <a-input-number v-model="model.reservedOne" placeholder="请输入预留" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="预留" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="reservedTwo">
              <a-input-number v-model="model.reservedTwo" placeholder="请输入预留" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="悬臂倾角" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="cantileverAngle">
              <a-input-number v-model="model.cantileverAngle" placeholder="请输入悬臂倾角" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="倾斜" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="tilt">
              <a-input-number v-model="model.tilt" placeholder="请输入倾斜" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="主钩吊钩升降速度" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="mainHookspeed">
              <a-input-number v-model="model.mainHookspeed" placeholder="请输入主钩吊钩升降速度" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="主钩小车移动速度" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="mainCarspeed">
              <a-input-number v-model="model.mainCarspeed" placeholder="请输入主钩小车移动速度" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="副钩吊钩升降速度" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="reservedVicehookSpeed">
              <a-input-number v-model="model.reservedVicehookSpeed" placeholder="请输入主钩小车移动速度" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="副钩小车移动速度" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="reservedVicecarSpeed">
              <a-input-number v-model="model.reservedVicecarSpeed" placeholder="请输入主钩小车移动速度" style="width: 100%" />
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
    name: 'DeviceTowerRealdataForm',
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
          add: "/tadiao/deviceTowerRealdata/add",
          edit: "/tadiao/deviceTowerRealdata/edit",
          queryById: "/tadiao/deviceTowerRealdata/queryById"
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