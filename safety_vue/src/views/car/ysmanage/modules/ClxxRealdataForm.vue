<template>
  <div>
    <a-row :gutter="24" style="padding-bottom: 5px">
      <a-col :span="6">
        <j-date
          placeholder="开始时间"
          v-model="datatime_begin"
          :value="datatime_begin"
          :showTime="true"
          dateFormat="YYYY-MM-DD HH:mm:ss"
        />
      </a-col>
      <a-col :span="6">
        <j-date
          placeholder="结束时间"
          v-model="datatime_end"
          :value="datatime_end"
          :showTime="true"
          dateFormat="YYYY-MM-DD HH:mm:ss"
        />
      </a-col>
      <a-col :span="6">
        <a-button type="primary" @click="Query" icon="search">查询</a-button>
      </a-col>
    </a-row>
    <a-spin :spinning="spinning" tip="加载中，耐心等待">
      <div id="container" style="height: 550px"></div>
    </a-spin>
    <a-row v-has="'car:play'" :gutter="24">
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
import { httpAction, getAction, postAction } from "@/api/manage";
import pick from "lodash.pick";
import { validateDuplicateValue } from "@/utils/util";
import JFormContainer from "@/components/jeecg/JFormContainer";
import JDate from "@/components/jeecg/JDate";
import carIcon from "@/assets/img/car.png";
import { loadPlugins } from "@amap/amap-vue";
import carIcon1 from "@/assets/img/car3.png";
import carIconStart from "@/assets/img/start.png";
import carIconEnd from "@/assets/img/end.png";
import coordtransform from "@/utils/coordtransform.js";

export default {
  name: "ClxxRealdataForm",
  components: {
    JFormContainer,
    JDate,
  },
  props: {
    //流程表单data
    formData: {
      type: Object,
      default: () => {},
      required: false,
    },
    //表单模式：true流程表单 false普通表单
    formBpm: {
      type: Boolean,
      default: false,
      required: false,
    },
    //表单禁用
    disabled: {
      type: Boolean,
      default: false,
      required: false,
    },
  },
  data() {
    return {
      spinning: false,
      datatime_begin: "",
      datatime_end: "",
      zoom: 15,
      lngdata: [],
      plugins: [],
      position1: [],
      position2: [],
      lineArr: [],
      content: "",
      content1: "",
      shebeiNo: "",
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
        list: "/clgl/frontDeviceHistorydata/lists",
        shebeiInfo: "/system/shebeiInfo/list",
        add: "/clgl/clxxRealdata/add",
        edit: "/clgl/clxxRealdata/edit",
        queryById: "/clgl/clxxRealdata/queryById",
        tokens: "/clgl/frontDeviceHistorydata/tokens",
        historylist: "/com.trtm.iot.september/september/list1",
      },
    };
  },
  computed: {
    formDisabled() {
      if (this.formBpm === true) {
        if (this.formData.disabled === false) {
          return false;
        }
        return true;
      }
      return this.disabled;
    },
    showFlowSubmitButton() {
      if (this.formBpm === true) {
        if (this.formData.disabled === false) {
          return true;
        }
      }
      return false;
    },
  },
  created() {
    //如果是流程中表单，则需要加载流程表单data
    // this.showFlowData()
  },
  mounted() {},
  methods: {
    initMaps() {
      this.map = new AMap.Map("container", {
        resizeEnable: false, //窗口大小调整
        center: [116.478935, 39.997761], //中心 firstArr: [116.478935, 39.997761],
        zoom: 10,
      });
    },
    //初始化地图
    initMap() {
      this.map = new AMap.Map("container", {
        resizeEnable: true, //窗口大小调整
        center: this.center, //中心 firstArr: [116.478935, 39.997761],
        zoom: 17,
      });
      //AMap.plugin('AMap.MoveAnimation', function(){});
      this.marker = new AMap.Marker({
        map: this.map,
        position: this.position1,
        label: {
          content: this.content,
          direction: "bottom",
        },
        icon: carIcon1,
        offset: new AMap.Pixel(-13, -26),
        autoRotation: true,
        angle: -90,
      });
      // 点标记显示内容，HTML要素字符串
      let markerContentS = `<img style="width:40px" src="${carIconStart}">`;
      let markerContentE = `<img style="width:40px" src="${carIconEnd}">`;
      this.markerS = new AMap.Marker({
        map: this.map,
        position: this.lineArr[0],
        content: markerContentS,
        // 以 icon 的 [center bottom] 为原点
        offset: new AMap.Pixel(-13, -30),
      });
      this.markerE = new AMap.Marker({
        map: this.map,
        position: this.lineArr[this.lineArr.length - 1],
        content: markerContentE,
        // 以 icon 的 [center bottom] 为原点
        offset: new AMap.Pixel(-13, -30),
      });
    },
    //初始化轨迹
    initroad() {
      var that = this;
      console.log(this.lineArr, "this.lineArr ");
      //绘制还未经过的路线
      this.polyline = new AMap.Polyline({
        map: this.map,
        path: this.lineArr,
        showDir: true,
        strokeColor: "#28F", //线颜色--蓝色
        // strokeOpacity: 1,     //线透明度
        strokeWeight: 6, //线宽
        // strokeStyle: "solid"  //线样式
      });
      //绘制路过了的轨迹
      var passedPolyline = new AMap.Polyline({
        map: this.map,
        strokeColor: "#AF5", //线颜色-绿色
        //path: this.lineArr,
        // strokeOpacity: 1,     //线透明度
        strokeWeight: 6, //线宽
        // strokeStyle: "solid"  //线样式
      });
      this.marker.on("moving", (e) => {
        passedPolyline.setPath(e.passedPath);
        const index = e.passedPath.length;
        this.map.setCenter(e.target.getPosition(), true);
        that.marker.setLabel({
          // content: '<div>' + that.lngdata[index].datatime + '-' + that.content1 + '</div>',
          content: "<div>" + that.content1 + "</div>",
          direction: "top",
        });
      });
      this.map.setFitView(); //合适的视口
    },
    add() {
      this.edit({});
    },
    detail(record) {
      //轨迹回放
      //this.form.resetFields()
      //this.model = Object.assign({}, record)
      this.visible = true;
      this.GetShebeiInfo(record);
    },
    Query() {
      //搜索
      this.spinning = true;
      console.log(`222222222222222222222222`);
      if (this.datatime_begin == "") {
        this.$message.warning("请选择开始时间");
        return;
      }
      if (this.datatime_end == "") {
        this.$message.warning("请选择结束时间");
        return;
      }
      let params = {
        shebeiNo: this.shebeiNo,
        projectId: this.datatime_end,
        uploadtime: this.datatime_begin,
      };
      postAction(this.url.tokens, params).then((res) => {
        if (res.success) {
          if (res.result.data) {
            // if(res.result.data.length>0){
            let data = res.result.data;
            this.lngdata = [];
            this.lngdata = data;
            this.lineArr = [];
            this.center = [];
            for (let i = 0; i < data.length; i++) {
              this.lineArr.push([data[i].lng, data[i].lat]);
              if (i <= data.length - 1) {
                this.center.push(data[0].lng, data[0].lat);
              }
            }
            this.position1 = this.lineArr[0];
            this.initMap();
            this.initroad();
          } else {
            this.querys(this.shebeiNo, this.datatime_begin, this.datatime_end);
            console.log("运行后面的查询");
          }
        } else {
          this.initMaps();
          this.$message.error("暂无当前车辆轨迹!");
        }
        this.spinning = false;
      });
    },
    querys: function (shebeiNo, datatime_begin, datatime_end) {
      //如果前面没数据就去查询另一张表
      let params = {
        imei: shebeiNo,
        gpstime_begin: new Date(datatime_begin).getTime() / 1000,
        gpstime_end: new Date(datatime_end).getTime() / 1000,
      };
      getAction(this.url.historylist, params).then((res) => {
        if (res.success) {
          if (res.result.length > 0) {
            let data = res.result;
            this.lngdata = [];
            this.lngdata = data;
            this.lineArr = [];
            this.center = [];
            for (let i = 0; i < data.length; i++) {
              this.lineArr.push([data[i].lng, data[i].lat]);
              if (i <= data.length - 1) {
                this.center.push(data[0].lng, data[0].lat);
              }
            }
            this.position1 = this.lineArr[0];
            this.initMap();
            this.initroad();
          } else {
            this.initMaps();
            this.$message.warning("再次确认当前时间范围内的历史轨迹!");
          }
        } else {
          this.initMaps();
          this.$message.error("再次确认当前车辆轨迹!");
        }
      });
    },

    Carguiji: function (record) {
      // this.shebeiNo = record.vehicle_dictText;
      this.spinning = true;
      // let dt = new Date(record.dattim)
      // let y = dt.getFullYear()//年
      // let m = dt.getMonth()//月
      // // let m = dt.getMonth() + 1//月
      // let d = dt.getDate()//日
      // if (m < 10) {
      //   m = 0 + '' + m
      // }
      // if (d < 10) {
      //   d = 0 + '' + d
      // }
      // let datatime_end = y + '-' + m + '-' + d + ' ' + '00:00:00'

      let datatime_end = this.changeTimeEnd(record.dattim);
      this.datatime_end = record.qianshoutime || datatime_end;
      this.datatime_begin = record.dattim;
      this.content = record.shebeiNo_dictText;
      this.content1 = record.vehicle + `-` + record.driver;
      let params = {
        shebeiNo: this.shebeiNo,
        projectId: this.datatime_end,
        uploadtime: record.dattim,
      };
      // let params = { shebeiNo: this.shebeiNo, projectId: record.dattim, uploadtime: datatime_start }
      postAction(this.url.tokens, params).then((res) => {
        if (res.success) {
          if (res.result.data) {
            // if (res.result.data.length > 0) {
            let data = res.result.data;
            this.lngdata = [];
            this.lngdata = data;
            this.lineArr = [];
            this.center = [];
            for (let i = 0; i < data.length; i++) {
              this.lineArr.push([data[i].lng, data[i].lat]);
              if (i <= data.length - 1) {
                this.center.push(data[0].lng, data[0].lat);
              }
            }
            this.position1 = this.lineArr[0];
            this.initMap();
            this.initroad();
          } else {
            this.initMaps();
            this.$message.warning("暂无当前时间范围内的历史轨迹!");
          }
        } else {
          this.initMaps();
          this.$message.error("暂无当前车辆轨迹!");
        }
        this.spinning = false;
      });
    },

    GetShebeiInfo: function (record) {
      let sbname = record.vehicle;
      // this.shebeiNo = record.vehicle;
      // this.shebeiNo = record.vehicle_dictText;
      // if(this.isLicenseNo(this.shebeiNo)){
      //   this.$message.error("未绑定gps!");
      //   return
      // }
      let params = {
        // sbjno: this.shebeiNo,
        sbname,
        sbtype: 35,
      };
      getAction(this.url.shebeiInfo, params).then((res) => {
        if (res.success) {
          if (res.result.records.length > 0) {
            this.shebeiNo = res.result.records[0].sbjno;
            this.Carguiji(record);
          } else {
            this.initMaps();
            this.$message.error("未绑定gps!");
          }
        } else {
          // this.$message.error("未绑定gps!");
        }
      });
    },
    // isLicenseNo(str) {//判断是否为车牌
    //   return /(^[\u4E00-\u9FA5]{1}[A-Z0-9]{6}$)|(^[A-Z]{2}[A-Z0-9]{2}[A-Z0-9\u4E00-\u9FA5]{1}[A-Z0-9]{4}$)|(^[\u4E00-\u9FA5]{1}[A-Z0-9]{5}[挂学警军港澳]{1}$)|(^[A-Z]{2}[0-9]{5}$)|(^(08|38){1}[A-Z0-9]{4}[A-Z0-9挂学警军港澳]{1}$)/.test(
    //     str
    //   );
    // },
    changeTimeEnd(originalDateTimeString) {
      // 解析日期时间字符串为Date对象
      const originalDateTime = new Date(originalDateTimeString);
      // 检查解析是否成功
      if (!isNaN(originalDateTime.getTime())) {
        // 给Date对象加上三小时
        originalDateTime.setHours(originalDateTime.getHours() + 3);
        let year = originalDateTime.getFullYear();
        let month = ("0" + (originalDateTime.getMonth() + 1)).slice(-2);
        let date = ("0" + originalDateTime.getDate()).slice(-2);
        let hours = ("0" + originalDateTime.getHours()).slice(-2);
        let min = ("0" + originalDateTime.getMinutes()).slice(-2);
        let sed = ("0" + originalDateTime.getSeconds()).slice(-2);
        const updatedDateTimeStringFormatted = `${year}-${month}-${date} ${hours}:${min}:${sed}`;
        console.log("格式化后的字符串:", updatedDateTimeStringFormatted);
        return updatedDateTimeStringFormatted;
      } else {
        console.error("无法解析日期时间字符串");
      }
    },
    //开始动画
    startAnimation() {
      this.marker.moveAlong(this.lineArr, 200);
    },
    //暂停动画
    pauseAnimation() {
      this.marker.pauseMove();
    },
    //继续动画
    resumeAnimation() {
      this.marker.resumeMove();
    },
    //停止动画
    stopAnimation() {
      this.marker.stopMove();
    },
    edit(record) {
      this.form.resetFields();
      this.model = Object.assign({}, record);
      this.visible = true;
      this.$nextTick(() => {
        // this.form.setFieldsValue(pick(this.model,'deviceType','shebeiNo_dictText','projectId','speed','temperature','longitude','latitude','datatime'))
      });
    },
    //渲染流程表单数据
    showFlowData() {
      console.log(`111111-----------`);
      // if (this.formBpm === true) {
      //   let params = { id: this.formData.dataId }
      //   getAction(this.url.queryById, params).then((res) => {
      //     if (res.success) {
      //       this.edit(res.result)
      //     }
      //   })
      // }
    },
    submitForm() {
      const that = this;
      // 触发表单验证
      this.form.validateFields((err, values) => {
        if (!err) {
          that.confirmLoading = true;
          let httpurl = "";
          let method = "";
          if (!this.model.id) {
            httpurl += this.url.add;
            method = "post";
          } else {
            httpurl += this.url.edit;
            method = "put";
          }
          let formData = Object.assign(this.model, values);
          console.log("表单提交数据", formData);
          httpAction(httpurl, formData, method)
            .then((res) => {
              if (res.success) {
                that.$message.success(res.message);
                that.$emit("ok");
              } else {
                that.$message.warning(res.message);
              }
            })
            .finally(() => {
              that.confirmLoading = false;
            });
        }
      });
    },
    popupCallback(row) {
      // this.form.setFieldsValue(pick(row,'deviceType','shebeiNo_dictText','projectId','speed','temperature','longitude','latitude','datatime'))
    },
  },
};
</script>
<style scoped>
#test-map {
  height: 650px;
}
</style>
<style>
.amap-marker-label {
  background-color: rgba(1, 2, 73, 0);
  color: #990055;
  border: 1px solid rgba(33, 150, 227, 0.5);
}
</style>
