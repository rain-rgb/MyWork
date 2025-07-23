<template>
  <div>
    <a-row :gutter="24" style="padding-bottom: 5px">
      <a-col :span="6">
        <j-date placeholder="开始时间" v-model="datatime_begin" :value="datatime_begin" :showTime="true"
                dateFormat="YYYY-MM-DD HH:mm:ss"/>
      </a-col>
      <a-col :span="6">
        <j-date placeholder="结束时间" v-model="datatime_end" :value="datatime_end" :showTime="true"
                dateFormat="YYYY-MM-DD HH:mm:ss"/>
      </a-col>
      <a-col :span="6">
        <a-button type="primary" @click="Query" icon="search">查询</a-button>
      </a-col>
    </a-row>
    <div id="container" style="height: 550px"></div>
    <a-row :gutter="24" v-has="'clxx:play'">
      <a-col :span="6">
        <a-button type="primary" @click="startAnimation">开始回放</a-button>
      </a-col>
      <a-col :span="6">
        <a-button type="primary" @click="pauseAnimation">暂停回放</a-button>
      </a-col>
      <a-col :span="6">
        <a-button type="primary" @click="resumeAnimation">继续回放</a-button>
      </a-col>
      <a-col :span="6">
        <a-button type="primary" @click="stopAnimation">结束回放</a-button>
      </a-col>
    </a-row>
  </div>
</template>

<script>

import { httpAction, getAction, postAction } from '@/api/manage'
import pick from 'lodash.pick'
import { validateDuplicateValue } from '@/utils/util'
import JFormContainer from '@/components/jeecg/JFormContainer'
import JDate from '@/components/jeecg/JDate'
import carIcon from '@/assets/img/car.png'
import { loadPlugins } from '@amap/amap-vue'
import carIcon1 from '@/assets/img/car3.png'
import coordtransform from '@/utils/coordtransform.js'
import carIconStart from "@assets/img/start.png";
import carIconEnd from "@assets/img/end.png";

export default {
  name: 'ClxxRealdataForm',
  components: {
    JFormContainer,
    JDate,
  },
  props: {
    //流程表单data
    formData: {
      type: Object,
      default: () => {
      },
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
  data() {

    return {
      datatime_begin: '',
      datatime_end: '',
      zoom: 15,
      lngdata: [],
      plugins: [],
      position1: [],
      position2: [],
      lineArr: [],
      content: '',
      shebeiNo: '',
      car: {
        path: [],
        dx: null,
        dy: null,
        pos: 0,
        angle: null,
        icon: {
          image: carIcon,
          imageSize: [21, 45],
        },
        offset: [-13, -25],
      },
      form: this.$form.createForm(this),
      model: {},
      center: [116.478935, 39.997761],
      labelCol: {
        xs: { span: 24 },
        sm: { span: 5 },
      },
      wrapperCol: {
        xs: { span: 24 },
        sm: { span: 16 },
      },
      confirmLoading: false,
      validatorRules: {},
      url: {
        list: '/clgl/frontDeviceHistorydata/lists',
        add: '/clgl/clxxRealdata/add',
        edit: '/clgl/clxxRealdata/edit',
        queryById: '/clgl/clxxRealdata/queryById',
        // tokens: '/clgl/frontDeviceHistorydata/tokens',
        tokens: '/suchingcarpeiz/suchingCarpeiz/listcl',
        historylist:'/com.trtm.iot.september/september/list1'
      }
    }
  },
  computed: {
    formDisabled() {
      if (this.formBpm === true) {
        if (this.formData.disabled === false) {
          return false
        }
        return true
      }
      return this.disabled
    },
    showFlowSubmitButton() {
      if (this.formBpm === true) {
        if (this.formData.disabled === false) {
          return true
        }
      }
      return false
    }
  },
  created() {
    //如果是流程中表单，则需要加载流程表单data
    this.showFlowData()
  },
  mounted() {
  },
  methods: {
    initMaps() {
      this.map = new AMap.Map('container', {
        resizeEnable: false, //窗口大小调整
        center: [116.478935, 39.997761], //中心 firstArr: [116.478935, 39.997761],
        zoom: 10
      })
    },
//初始化地图
    initMap() {
      this.map = new AMap.Map('container', {
        resizeEnable: true, //窗口大小调整
        center: this.center, //中心 firstArr: [116.478935, 39.997761],
        zoom: 17
      })
      //AMap.plugin('AMap.MoveAnimation', function(){});
      this.marker = new AMap.Marker({
        map: this.map,
        position: this.position1,
        label: {
          content: this.content,
          direction: 'bottom',
        },
        icon: carIcon1,
        offset: new AMap.Pixel(-13, -26),
        autoRotation: true,
        angle: -90,
      })
      // 点标记显示内容，HTML要素字符串
      let markerContentS = `<img style="width:40px" src="${carIconStart}">`;
      let markerContentE = `<img style="width:40px" src="${carIconEnd}">`;
      this.markerS = new AMap.Marker({
        map: this.map,
        position: this.lineArr[0],
        content: markerContentS,
        // 以 icon 的 [center bottom] 为原点
        offset: new AMap.Pixel(-13, -30)
      })
      this.markerE = new AMap.Marker({
        map: this.map,
        position: this.lineArr[this.lineArr.length-1],
        content: markerContentE,
        // 以 icon 的 [center bottom] 为原点
        offset: new AMap.Pixel(-13, -30)
      })
    },
    //初始化轨迹
    initroad() {
      var that = this
      //绘制还未经过的路线
      this.polyline = new AMap.Polyline({
        map: this.map,
        path: this.lineArr,
        showDir: true,
        strokeColor: '#28F', //线颜色--蓝色
        // strokeOpacity: 1,     //线透明度
        strokeWeight: 6 //线宽
        // strokeStyle: "solid"  //线样式
      })
      //绘制路过了的轨迹
      var passedPolyline = new AMap.Polyline({
        map: this.map,
        strokeColor: '#AF5', //线颜色-绿色
        //path: this.lineArr,
        // strokeOpacity: 1,     //线透明度
        strokeWeight: 6 //线宽
        // strokeStyle: "solid"  //线样式
      })
      this.marker.on('moving', e => {
        passedPolyline.setPath(e.passedPath)
        const index = e.passedPath.length
        this.map.setCenter(e.target.getPosition(), true)
        that.marker.setLabel({
          content: '<div>' + that.lngdata[index].datatime + '-' + that.content + '</div>',
          direction: 'top'
        })
      })
      this.map.setFitView() //合适的视口
    },
    add() {
      this.edit({})
    },
    detail(record) {//轨迹回放
      //this.form.resetFields()
      //this.model = Object.assign({}, record)
      this.visible = true
      this.Carguiji(record)
    },
    Query() {//搜索
      if (this.datatime_begin == '') {
        this.$message.warning('请选择开始时间')
        return
      }
      if (this.datatime_end == '') {
        this.$message.warning('请选择结束时间')
        return
      }
      // let params = { shebeiNo: this.shebeiNo, projectId: this.datatime_end, uploadtime: this.datatime_begin }
      let params = {
        pageSize: 2000,
        pageNum: 1,
        sn: this.content,
        // sn: 867192069426432,//this.listdata.sbbh
        // teid: this.teid,
        start_time: this.datatime_begin,
        end_time: this.datatime_end,
      };
      getAction(this.url.tokens, params).then((res) => {
        if (res.success) {
          if(res.result.length>0){
          let data = res.result;
          this.lngdata = []
          this.lngdata = data
          this.lineArr = []
          this.center = []
          for (let i = 0; i < data.length; i++) {
            this.lineArr.push([data[i].lon, data[i].lat])
            if (i <= data.length - 1) {
              this.center.push(data[0].lon, data[0].lat)
            }
          }
          this.position1 = this.lineArr[0]
          setTimeout(() => {
            this.initMap()
            this.initroad()
          }, 500);
          }else{
            // this.querys(this.shebeiNo,this.datatime_begin,this.datatime_end);
            console.log("运行后面的查询")
          }
        } else {
          setTimeout(() => {
            this.initMaps()
          }, 500);
          this.$message.error('暂无当前车辆轨迹!')
        }
      })
    },
    querys: function (shebeiNo, datatime_begin, datatime_end) {//如果前面没数据就去查询另一张表
      let params = { imei: shebeiNo, gpstime_begin: new Date(datatime_begin).getTime()/1000, gpstime_end: new Date(datatime_end).getTime()/1000 }
      getAction(this.url.historylist, params).then(res => {
        if (res.success) {
          if (res.result.length > 0) {
            let data = res.result
            this.lngdata = []
            this.lngdata = data
            this.lineArr = []
            this.center = []
            for (let i = 0; i < data.length; i++) {
              this.lineArr.push([data[i].lng, data[i].lat])
              if (i <= data.length - 1) {
                this.center.push(data[0].lng, data[0].lat)
              }
            }
            this.position1 = this.lineArr[0]
            this.initMap()
            this.initroad()
          } else {
            this.initMaps()
            this.$message.warning('再次确认当前时间范围内的历史轨迹!')
          }
        } else {
          this.initMaps()
          this.$message.error('再次确认当前车辆轨迹!')
        }
      })
    },

    Carguiji: function (record) {
      // this.shebeiNo = record.shebeiNo
      // let dt = new Date(record.datatime)
      // let y = dt.getFullYear()//年
      // let m = dt.getMonth() + 1//月
      // let d = dt.getDate()//日
      // if (m < 10) {
      //   m = 0 + '' + m
      // }
      // if (d < 10) {
      //   d = 0 + '' + d
      // }
      // let datatime_start = y + '-' + m + '-' + d + ' ' + '00:00:00'
      // console.log(datatime_start,'datatime_start');
      if(!Boolean(record.ddtime)){//到达时间没有值时获取当前时间
        let currentDate = new Date(); 
        this.datatime_end = this.formatDate(currentDate);
      }else{
        this.datatime_end = record.ddtime
      }
      this.datatime_begin = record.cftime
      this.content = record.sbbh
      // let params = { shebeiNo: this.shebeiNo, projectId: record.datatime, uploadtime: datatime_start }
      let params = {
            pageSize: 2000,
            pageNum: 1,
            sn: record.sbbh,
            // sn: 867192069426432,//this.listdata.sbbh
            // teid: this.teid,
            start_time: record.cftime,
            end_time: record.ddtime,
          };
      getAction(this.url.tokens, params).then((res) => {
        if (res.success) {
          if (res.result.length > 0) {
            let data = res.result
            this.lngdata = []
            this.lngdata = data
            this.lineArr = []
            this.center = []
            for (let i = 0; i < data.length; i++) {
              this.lineArr.push([data[i].lon, data[i].lat])
              if (i <= data.length - 1) {
                this.center.push(data[0].lon, data[0].lat)
              }
            }
            this.position1 = this.lineArr[0]
            setTimeout(() => {
              this.initMap()
              this.initroad()
            }, 500);
          } else {
            setTimeout(() => {
              this.initMaps()
            }, 500);
            this.$message.warning('暂无当前时间范围内的历史轨迹!')
          }
        } else {
          this.initMaps()
          this.$message.error('暂无当前车辆轨迹!')
        }
      })
    },
    formatDate(date) {
      let year = date.getFullYear();  
      
      let month = (1 + date.getMonth()).toString();  
      month = month.padStart(2, '0');  
      
      let day = date.getDate().toString();  
      day = day.padStart(2, '0');  
      
      let hours = date.getHours().toString();  
      hours = hours.padStart(2, '0');  
      
      let minutes = date.getMinutes().toString();  
      minutes = minutes.padStart(2, '0');  
      
      let seconds = date.getSeconds().toString();  
      seconds = seconds.padStart(2, '0');  
      
      return year + '-' + month + '-' + day + ' ' + hours + ':' + minutes + ':' + seconds;  
    },
    //开始动画
    startAnimation() {
      this.marker.moveAlong(this.lineArr, 200)
    },
    //暂停动画
    pauseAnimation() {
      this.marker.pauseMove()
    },
    //继续动画
    resumeAnimation() {
      this.marker.resumeMove()
    },
    //停止动画
    stopAnimation() {
      this.marker.stopMove()
    },
    edit(record) {
      this.form.resetFields()
      this.model = Object.assign({}, record)
      this.visible = true
      this.$nextTick(() => {
        // this.form.setFieldsValue(pick(this.model,'deviceType','shebeiNo_dictText','projectId','speed','temperature','longitude','latitude','datatime'))
      })
    },
    //渲染流程表单数据
    showFlowData() {
      if (this.formBpm === true) {
        let params = { id: this.formData.dataId }
        getAction(this.url.queryById, params).then((res) => {
          if (res.success) {
            this.edit(res.result)
          }
        })
      }
    },
    submitForm() {
      const that = this
      // 触发表单验证
      this.form.validateFields((err, values) => {
        if (!err) {
          that.confirmLoading = true
          let httpurl = ''
          let method = ''
          if (!this.model.id) {
            httpurl += this.url.add
            method = 'post'
          } else {
            httpurl += this.url.edit
            method = 'put'
          }
          let formData = Object.assign(this.model, values)
          console.log('表单提交数据', formData)
          httpAction(httpurl, formData, method).then((res) => {
            if (res.success) {
              that.$message.success(res.message)
              that.$emit('ok')
            } else {
              that.$message.warning(res.message)
            }
          }).finally(() => {
            that.confirmLoading = false
          })
        }

      })
    },
    popupCallback(row) {
      // this.form.setFieldsValue(pick(row,'deviceType','shebeiNo_dictText','projectId','speed','temperature','longitude','latitude','datatime'))
    },
  }
}
</script>
<style scoped>
#test-map {
  height: 650px;
}
</style>
<style>
.amap-marker-label {
  background-color: rgba(1, 2, 73, .0);
  color: #990055;
  border: 1px solid rgba(33, 150, 227, 0.50);
}
</style>