<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <a-col :span="24">
            <a-form-model-item label="设备序列号，全局唯一" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="deviceid">
              <a-input v-model="model.deviceid" placeholder="请输入设备序列号，全局唯一"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="gps设备上报的时间" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="devicetime">
              <a-input-number v-model="model.devicetime" placeholder="请输入gps设备上报的时间" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="到达服务器时间" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="arrivedtime">
              <a-input-number v-model="model.arrivedtime" placeholder="请输入到达服务器时间" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="综合计算后位置更新时间" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="updatetime">
              <a-input-number v-model="model.updatetime" placeholder="请输入综合计算后位置更新时间" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="最后位置定位时间" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="validpoistiontime">
              <a-input-number v-model="model.validpoistiontime" placeholder="请输入最后位置定位时间" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="计算出来的纬度" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="callat">
              <a-input-number v-model="model.callat" placeholder="请输入计算出来的纬度" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="计算出来的经度" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="callon">
              <a-input-number v-model="model.callon" placeholder="请输入计算出来的经度" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="高度(米)" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="altitude">
              <a-input-number v-model="model.altitude" placeholder="请输入高度(米)" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="定位精度米" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="radius">
              <a-input-number v-model="model.radius" placeholder="请输入定位精度米" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="速度（米）" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="speed">
              <a-input-number v-model="model.speed" placeholder="请输入速度（米）" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="方向（0 - 360）" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="course">
              <a-input-number v-model="model.course" placeholder="请输入方向（0 - 360）" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="总里程米" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="totaldistance">
              <a-input-number v-model="model.totaldistance" placeholder="请输入总里程米" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="总油量，单位毫升" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="totaloil">
              <a-input-number v-model="model.totaloil" placeholder="请输入总油量，单位毫升" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="总非行驶油耗，单位毫升" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="totalnotrunningad">
              <a-input-number v-model="model.totalnotrunningad" placeholder="请输入总非行驶油耗，单位毫升" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="分开统计的时候第1个油箱，单位毫升" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="masteroil">
              <a-input-number v-model="model.masteroil" placeholder="请输入分开统计的时候第1个油箱，单位毫升" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="分开统计的时候第2个油箱，单位毫升" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="auxoil">
              <a-input-number v-model="model.auxoil" placeholder="请输入分开统计的时候第2个油箱，单位毫升" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="分开统计的时候第3个油箱，单位毫升" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="thirdoil">
              <a-input-number v-model="model.thirdoil" placeholder="请输入分开统计的时候第3个油箱，单位毫升" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="分开统计的时候第4个油箱，单位毫升" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="fourthoil">
              <a-input-number v-model="model.fourthoil" placeholder="请输入分开统计的时候第4个油箱，单位毫升" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="油量模拟量1" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="srcad0">
              <a-input-number v-model="model.srcad0" placeholder="请输入油量模拟量1" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="油量模拟量2" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="srcad1">
              <a-input-number v-model="model.srcad1" placeholder="请输入油量模拟量2" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="油量模拟量3" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="srcad2">
              <a-input-number v-model="model.srcad2" placeholder="请输入油量模拟量3" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="油量模拟量4" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="srcad3">
              <a-input-number v-model="model.srcad3" placeholder="请输入油量模拟量4" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="对应部标状态值" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="status">
              <a-input-number v-model="model.status" placeholder="请输入对应部标状态值" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="对应部标状态文字描述" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="strstatus">
              <a-input v-model="model.strstatus" placeholder="请输入对应部标状态文字描述"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="对应部标状态英文文字描述" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="strstatusen">
              <a-input v-model="model.strstatusen" placeholder="请输入对应部标状态英文文字描述"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="对应部标报警值" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="alarm">
              <a-input-number v-model="model.alarm" placeholder="请输入对应部标报警值" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="对应部标报警文字描述" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="stralarm">
              <a-input v-model="model.stralarm" placeholder="请输入对应部标报警文字描述"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="对应部标报警英文文字描述" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="stralarmsen">
              <a-input v-model="model.stralarmsen" placeholder="请输入对应部标报警英文文字描述"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="对应部标视频报警值" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="videoalarm">
              <a-input-number v-model="model.videoalarm" placeholder="请输入对应部标视频报警值" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="对应部标视频报警文字描述" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="strvideoalarm">
              <a-input v-model="model.strvideoalarm" placeholder="请输入对应部标视频报警文字描述"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="对应部标视频报警英文文字描述" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="strvideoalarmen">
              <a-input v-model="model.strvideoalarmen" placeholder="请输入对应部标视频报警英文文字描述"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="视频信号丢失状态" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="videosignalloststatus">
              <a-input-number v-model="model.videosignalloststatus" placeholder="请输入视频信号丢失状态" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="视频遮挡状态" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="videosignalcoverstatus">
              <a-input-number v-model="model.videosignalcoverstatus" placeholder="请输入视频遮挡状态" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="视频异常驾驶行为状态" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="videobehavior">
              <a-input-number v-model="model.videobehavior" placeholder="请输入视频异常驾驶行为状态" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="视频疲劳程度" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="videofatiguedegree">
              <a-input-number v-model="model.videofatiguedegree" placeholder="请输入视频疲劳程度" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="位置信息来源定位类型 (gps,wifi,cell)" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="gotsrc">
              <a-input v-model="model.gotsrc" placeholder="请输入位置信息来源定位类型 (gps,wifi,cell)"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="基站信号强度" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="rxlevel">
              <a-input-number v-model="model.rxlevel" placeholder="请输入基站信号强度" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="gps有效数" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="gpsvalidnum">
              <a-input-number v-model="model.gpsvalidnum" placeholder="请输入gps有效数" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="外部电压，单位0.01V 默认是-1,0代表断电了" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="exvoltage">
              <a-input-number v-model="model.exvoltage" placeholder="请输入外部电压，单位0.01V 默认是-1,0代表断电了" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="电压，单位0.01V 默认是-1,0代表断电了" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="voltagev">
              <a-input-number v-model="model.voltagev" placeholder="请输入电压，单位0.01V 默认是-1,0代表断电了" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="电量百分比，有的设备百分比是通过上报次数计算百分比" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="voltagepercent">
              <a-input-number v-model="model.voltagepercent" placeholder="请输入电量百分比，有的设备百分比是通过上报次数计算百分比" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="是否移动，是否在运动 0代表没运动 1代表在运动" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="moving">
              <a-input-number v-model="model.moving" placeholder="请输入是否移动，是否在运动 0代表没运动 1代表在运动" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="最后停留的lat" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="parklat">
              <a-input-number v-model="model.parklat" placeholder="请输入最后停留的lat" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="最后停留的lon" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="parklon">
              <a-input-number v-model="model.parklon" placeholder="请输入最后停留的lon" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="最后停留时间" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="parktime">
              <a-input-number v-model="model.parktime" placeholder="请输入最后停留时间" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="停留了多久" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="parkduration">
              <a-input-number v-model="model.parkduration" placeholder="请输入停留了多久" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="温度1，单位 1/100度， 65535 代表无效" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="temp1">
              <a-input-number v-model="model.temp1" placeholder="请输入温度1，单位 1/100度， 65535 代表无效" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="温度2，单位 1/100度， 65535 代表无效" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="temp2">
              <a-input-number v-model="model.temp2" placeholder="请输入温度2，单位 1/100度， 65535 代表无效" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="温度3，单位 1/100度， 65535 代表无效" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="temp3">
              <a-input-number v-model="model.temp3" placeholder="请输入温度3，单位 1/100度， 65535 代表无效" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="温度4，单位 1/100度， 65535 代表无效" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="temp4">
              <a-input-number v-model="model.temp4" placeholder="请输入温度4，单位 1/100度， 65535 代表无效" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="湿度1" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="humi1">
              <a-input-number v-model="model.humi1" placeholder="请输入湿度1" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="湿度2" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="humi2">
              <a-input-number v-model="model.humi2" placeholder="请输入湿度2" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="IO 状态位" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="iostatus">
              <a-input-number v-model="model.iostatus" placeholder="请输入IO 状态位" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="1代表正在超速 2代表未超速状态" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="currentoverspeedstate">
              <a-input-number v-model="model.currentoverspeedstate" placeholder="请输入1代表正在超速 2代表未超速状态" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="正反转(0:未知；1：正转（空车）2:反转（重车）；3：停转)" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="rotatestatus">
              <a-input-number v-model="model.rotatestatus" placeholder="请输入正反转(0:未知；1：正转（空车）2:反转（重车）；3：停转)" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="载重状态 0x00：空车；0x01：半载；0x02：超载；0x03：满载 0x04 装载 0x05 卸载" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="loadstatus">
              <a-input-number v-model="model.loadstatus" placeholder="请输入载重状态 0x00：空车；0x01：半载；0x02：超载；0x03：满载 0x04 装载 0x05 卸载" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="重量 单位0.1kg" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="weight">
              <a-input-number v-model="model.weight" placeholder="请输入重量 单位0.1kg" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="载重里面的ad0,目前只做1路" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="srcweightad0">
              <a-input-number v-model="model.srcweightad0" placeholder="请输入载重里面的ad0,目前只做1路" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="上报类型" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="reportmode">
              <a-input-number v-model="model.reportmode" placeholder="请输入上报类型" style="width: 100%" />
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
    name: 'DeviceDataForm',
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
           deviceid: [
              { required: true, message: '请输入设备序列号，全局唯一!'},
           ],
        },
        url: {
          add: "/device_data/deviceData/add",
          edit: "/device_data/deviceData/edit",
          queryById: "/device_data/deviceData/queryById"
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