<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline">
        <a-row :gutter="24">
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="设备名称">
              <JDictSelectTagcar
                placeholder="请选择设备名称"
                dropdownStyle="color:red"
                v-model="dictOption.text"
                :dictOptions="dictOption"
                @change="handleChange(dictOption.text)"
              >
              </JDictSelectTagcar>
              {{ selectValue }}
            </a-form-item>
          </a-col>

          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <span
              style="float: left; overflow: hidden"
              class="table-page-search-submitButtons"
            >
              <a-button type="primary" @click="shebeiZB" icon="search">查询</a-button>
              <a-button
                type="primary"
                @click="chongzhi"
                icon="reload"
                style="margin-left: 8px"
                >重置</a-button
              >
              <!--                            <a @click="handleToggleSearch" style="margin-left: 8px">-->
              <!--                              {{ toggleSearchStatus ? '收起' : '展开' }}-->
              <!--                              <a-icon :type="toggleSearchStatus ? 'up' : 'down'"/>-->
              <!--                            </a>-->
            </span>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="颜色标准">
              <a-button type="primary"> 此颜色的设备代表在线</a-button>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="颜色标准">
              <a-button type="danger"> 此颜色的设备代表离线</a-button>
            </a-form-item>
          </a-col>
        </a-row>
      </a-form>
    </div>
    <!-- 查询区域-END -->
    <!-- table区域-begin -->
    <div id="container" style="height: 75vh"></div>

    <a-row>
      <a-col :span="6">
        <a-button
          style="margin-left: 25%"
          icon="plus"
          type="primary"
          @click="circleEditoradd(map)"
          >新建电子围栏</a-button
        >
      </a-col>
      <a-col :span="6">
        <a-button
          type="primary"
          style="margin-left: 25%"
          icon="edit"
          @click="circleEditoropen(circleEditor)"
          >开始编辑电子围栏</a-button
        >
      </a-col>
      <a-col :span="6">
        <a-button
          type="primary"
          style="margin-left: 25%"
          icon="check"
          @click="circleEditorclose(circleEditor)"
          >保存电子围栏</a-button
        >
      </a-col>
      <a-col :span="6">
        <a-button type="danger" style="margin-left: 25%" icon="delete" @click="circledel"
          >删除电子围栏</a-button
        >
      </a-col>
    </a-row>
  </a-card>
</template>

<script>
import "@/assets/less/TableExpand.less";
import { mixinDevice } from "@/utils/mixin";
import { JeecgListMixin } from "@/mixins/JeecgListMixin";
import JDictSelectTagcar from "@/components/dict/JSearchSelectTagcar";
import JSuperQuery from "@/components/jeecg/JSuperQuery.vue";
import JselectDqDepart from "@/components/jeecgbiz/JselectDqDepart.vue";
import { pushdepartidShebei } from "@/mixins/pushdepartidShebei";
import { JVXETypes } from "@/components/jeecg/JVxeTable";
import { getAction, postAction, putAction } from "@api/manage";
import { usershebeiList, carshebeiList } from "@api/api";
import { SYS_DEPART_ORGCODE } from "@/store/mutation-types";
import Vue from "vue";
import carIcon1 from "@assets/img/gifcar.gif";
import carIcon2 from "@assets/img/carlixian.png";
import { wgs84togcj02 } from "@/utils/coordtransform.js";

export default {
  name: "VehicleQSC",
  //mixins: [JeecgListMixin],
  components: {
    JDictSelectTagcar,
    JSuperQuery,
    JselectDqDepart,
  },
  data() {
    return {
      selectValue: "",
      selectedValue: "",
      center: [116.478935, 39.997761],
      centers: {
        lng: null,
        lat: null,
      },
      position: [120, 30],
      zuobiao: [120.333, 30.333],
      czuobiao: [120.333, 30.333],
      zoom: 8,
      intervalId: null,
      plugins: [],
      validatorRules: {},
      pitch: 0,
      lineArr: [],
      listDate: [],
      rotation: 0,
      // markerIcon: "",
      xianshiwenzi: { content: "暂无", direction: "bottom" },
      opacity: 1,
      dictOption: [],
      move: {
        lat: null, //纬度
        lng: null, //经度
      },
      adjust: {
        radius: 0,
      },
      circle: {
        id: 0,
        center: [],
        radius: 0,
      },
      url: {
        list: "/system/shebeiInfo/SBlist",
        sbjwd: "/system/shebeiInfo/SBJWD",
        queryListQSC: "/clgl/clxxRealdata/queryListQSC",
        circleadd: "/frontDeviceWeilan/frontDeviceWeilan/add",
        circleedit: "/frontDeviceWeilan/frontDeviceWeilan/edit",
        circlelistone: "/frontDeviceWeilan/frontDeviceWeilan/querylist",
        circleisdel: "/frontDeviceWeilan/frontDeviceWeilan/isdel",
      },
      shebei: [],
      circleEditor: null,
    };
  },
  created() {
    this.shebeiList();
    this.dataRefreh();
  },
  destroyed() {
    // 在页面销毁后，清除计时器
    this.clear();
  },
  mounted() {
    this.initMap();
  },
  computed: {
    importExcelUrl: function () {
      return `${window._CONFIG["domianURL"]}/${this.url.list}`;
    },
  },
  methods: {
    initMap() {
      var that = this;
      var shebeiNo = null;
      if (this.selectedValue == "") {
        shebeiNo = null;
      } else {
        shebeiNo = this.selectedValue;
      }
      console.log(shebeiNo);
      let param = { shebeiNo: shebeiNo };
      getAction(this.url.queryListQSC, param).then((res) => {
        console.log(res, "最新数据");
        this.listDate = [];
        if (res.result.length > 0) {
          this.listDate = res.result;
          [this.listDate[0].longitude, this.listDate[0].latitude] = wgs84togcj02(
            this.listDate[0].longitude,
            this.listDate[0].latitude
          );
          that.center = [this.listDate[0].longitude, this.listDate[0].latitude];
          that.centers.lng = this.listDate[0].longitude;
          that.centers.lat = this.listDate[0].latitude;
          // console.log(
          //   that.center,
          //   that.centers.lng,
          //   that.centers.lat,
          //   "that.center------------------------1111"
          // );
        } else {
          this.$message.error("数据请求失败！");
        }
      });
      if (that.selectedValue != "") {
        let param = { shebeiNo: that.selectedValue, isdel: 0 };
        getAction(that.url.circlelistone, param).then((res) => {
          that.circle.center = [];
          that.circle.radius = 0;
          if (res.result.length > 0) {
            console.log(res, "围栏数据");
            // [res.result[0].lng, res.result[0].lat] = wgs84togcj02(
            //   res.result[0].lng,
            //   res.result[0].lat
            // );
            that.circle.center.push(res.result[0].lng, res.result[0].lat);
            console.log(that.circle.center, "围栏坐标");
            that.circle.radius = res.result[0].radius;
            that.circle.id = res.result[0].id;
            console.log(that.circle.radius, "围栏半径");
          }
        });
      }
      setTimeout(function () {
        that.map = new AMap.Map("container", {
          resizeEnable: true, //窗口大小调整
          center: that.center, //中心 firstArr: [116.478935, 39.997761],
          zoom: 17,
          mapStyle: "amap://styles/normal",
        });

        let startIcon = new AMap.Icon({
          //图标尺寸
          size: new AMap.Size(70, 70),
          //图标的取图地址
          image: carIcon1,
          //图标所用图片大小
          imageSize: new AMap.Size(70, 70),
          //图标取图偏移量
          //imageOffset:new AMap.Pixel(-9,-3)
        });
        let startIcon1 = new AMap.Icon({
          //图标尺寸
          size: new AMap.Size(70, 70),
          //图标的取图地址
          image: carIcon2,
          //图标所用图片大小
          imageSize: new AMap.Size(70, 70),
          //图标取图偏移量
          //imageOffset:new AMap.Pixel(-9,-3)
        });
        if (that.circle.radius != 0) {
          console.log("进入围栏");
          var circle = new AMap.Circle({
            center: that.circle.center,
            radius: that.circle.radius, //半径
            borderWeight: 3,
            strokeColor: "#FF33FF",
            strokeOpacity: 1,
            strokeWeight: 6,
            fillOpacity: 0.4,
            strokeStyle: "dashed",
            strokeDasharray: [10, 10],
            // 线样式还支持 'dashed'
            fillColor: "#1791fc",
            zIndex: 50,
          });
          circle.setMap(that.map);
          var circleEditor = new AMap.CircleEditor(that.map, circle);
          that.circleEditor = circleEditor;
          circleEditor.on("move", function (event) {
            console.log(event);
            that.move.lng = null;
            that.move.lat = null;
            // that.move.lng = event.lnglat.lng;
            // that.move.lat = event.lnglat.lat;
            [that.move.lng, that.move.lat] = wgs84togcj02(
              event.lnglat.lng,
              event.lnglat.lat
            );
            // console.log([that.move.lng, that.move.lat]);
            // that.$message.success("触发事件：move");
          });
          circleEditor.on("adjust", function (event) {
            console.log(event);
            that.adjust.radius = 0;
            that.adjust.radius = event.radius;
            // that.$message.success("触发事件：adjust");
          });
          circleEditor.on("end", function (event) {
            // that.$message.success("触发事件：end");
            that.circleedit();
            // event.target 即为编辑后的圆形对象
          });
        }
        //

        AMap.plugin("AMap.MoveAnimation", function () {});
        that.listDate.forEach(function (item, index) {
          //item 就是当日按循环到的对象
          //index是循环的索引，从0开始
          if (item.shebeistatus == 1) {
            that.marker = new AMap.Marker({
              map: that.map,
              position: [item.longitude, item.latitude],
              label: {
                content: item.shebeiname + "--在线",
                direction: "top",
              },
              icon: startIcon,
              offset: new AMap.Pixel(-25, -13),
              autoRotation: true,
            });
          } else {
            that.marker = new AMap.Marker({
              map: that.map,
              position: [item.longitude, item.latitude],
              label: {
                content: item.shebeiname + "--离线",
                direction: "top",
              },
              icon: startIcon1,
              offset: new AMap.Pixel(-25, -13),
              autoRotation: true,
            });
          }
        });
        that.map.setFitView(); //合适的视口
      }, 500);
    },
    circleEditoradd: function (map) {
      if (this.selectedValue == "") {
        return this.$message.error("请先选择一个设备去设置电子围栏");
      }
      var that = this;
      this.circle = new AMap.Circle({
        center: this.center,
        radius: 1000, //半径
        borderWeight: 1,
        strokeColor: "#FF33FF",
        strokeOpacity: 1,
        strokeWeight: 6,
        fillOpacity: 0.4,
        strokeStyle: "dashed",
        strokeDasharray: [10, 10],
        // 线样式还支持 'dashed'
        fillColor: "#afb4db",
        zIndex: 50,
      });
      this.circle.setMap(map);
      this.$message.success("请为围栏选择合适的半径!!");
      var circleEditor = new AMap.CircleEditor(map, this.circle);
      this.circleEditor = circleEditor;
      circleEditor.on("move", function (event) {
        console.log(event);
        that.move.lng = null;
        that.move.lat = null;
        [event.lnglat.lng, event.lnglat.lat] = wgs84togcj02(
          event.lnglat.lng,
          event.lnglat.lat
        );
        that.move.lng = event.lnglat.lng;
        that.move.lat = event.lnglat.lat;
        // that.$message.success("触发事件：move");
      });
      circleEditor.on("adjust", function (event) {
        console.log(event);
        that.adjust.radius = 0;
        that.adjust.radius = event.radius;
        // that.$message.success("触发事件：adjust");
      });
      circleEditor.on("end", function (event) {
        // that.$message.success("触发事件：end");
        // event.target 即为编辑后的圆形对象
      });
    },
    circleEditoropen: function (circleEditor) {
      var that = this;
      circleEditor.open();
    },
    circleEditorclose: function (circleEditor) {
      circleEditor.close();
      this.circleadd();
    },
    circleEditordel: function (circleEditor) {
      this.map.remove(marker);
    },
    circleadd: function () {
      var center = null;
      var lng = null;
      var lat = null;
      if (this.move.lng != null && this.move.lat != null) {
        // [this.move.lng, this.move.lat] = wgs84togcj02(this.move.lng, this.move.lat);
        // console.log(this.move.lng + "," + this.move.lat,'this.move.lng + "," + this.move.lat 11111111-----------');
        center = this.move.lng + "," + this.move.lat;
        lng = this.move.lng;
        lat = this.move.lat;
      } else {
        [this.centers.lng, this.centers.lat] = wgs84togcj02(
          this.centers.lng,
          this.centers.lat
        );
        center = this.centers.lng + "," + this.centers.lat;
        lng = this.centers.lng;
        lat = this.centers.lat;
      }
      console.log(center);
      if (this.selectedValue == "") {
        return this.$message.error("选择一个设备再进行设置围栏！");
      }
      let param = {
        shebeiNo: this.selectedValue,
        sid: 362318,
        name: this.selectedValue,
        center: center,
        radius: this.adjust.radius,
        lng: lng,
        lat: lat,
      };
      postAction(this.url.circleadd, param).then((res) => {
        console.log(res);
        if (res.success) {
          this.$message.success(res.message);
        } else {
          this.$message.error(res.message);
        }
      });
    },
    circleedit: function () {
      var center = null;
      var lng = null;
      var lat = null;
      if (this.move.lng != null && this.move.lat != null) {
        // [this.centers.lng, this.centers.lat] = wgs84togcj02(
        //   this.centers.lng,
        //   this.centers.lat
        // );
        center = this.move.lng + "," + this.move.lat;
        lng = this.move.lng;
        lat = this.move.lat;
      } else {
        center = this.centers.lng + "," + this.centers.lat;
        lng = this.centers.lng;
        lat = this.centers.lat;
      }
      console.log(center);
      if (this.selectedValue == "") {
        return this.$message.error("选择一个设备再进行设置围栏！");
      }
      if (this.circle.id == 0) {
        return this.$message.error("此设备为查询到围栏！");
      }
      let param = {
        id: this.circle.id,
        shebeiNo: this.selectedValue,
        sid: 362318,
        name: this.selectedValue,
        center: center,
        radius: this.adjust.radius,
        lng: lng,
        lat: lat,
      };
      putAction(this.url.circleedit, param).then((res) => {
        console.log(res);
        if (res.success) {
          this.$message.success(res.message);
        } else {
          this.$message.error(res.message);
        }
      });
    },
    circledel: function () {
      if (this.circle.id == 0) {
        this.$message.error("请先选择一个设备！");
        return;
      }
      console.log(this.circle.id);
      let param = { id: this.circle.id };
      putAction(this.url.circleisdel, param).then((res) => {
        if (res.success) {
          this.$message.success(res.message);
          this.initMap();
        } else {
          this.$message.error(res.message);
        }
      });
    },
    // 定时刷新数据函数
    dataRefreh() {
      // 计时器正在进行中，退出函数
      if (this.intervalId != null) {
        return;
      }
      // 计时器为空，操作
      this.intervalId = setInterval(() => {
        console.log("定时50秒刷新一次定位信息" + new Date());
        this.initMap();
      }, 50000);
    },
    // 停止定时器
    clear() {
      clearInterval(this.intervalId); //清除计时器
      this.intervalId = null; //设置为null
    },
    shebeiZB: function () {
      this.initMap(); //查询
    },
    handleChange(selectedValue) {
      console.log("selectedValue", selectedValue);
      this.selectedValue = selectedValue;
    },
    chongzhi: function () {
      this.dictOption = [];
      this.selectedValue = "";
      this.initMap();
    },
    shebeiList: function () {
      var sys_depart_orgcode = Vue.ls.get("SYS_DEPART_ORGCODE");
      console.log(sys_depart_orgcode);
      carshebeiList({
        sys_depart_orgcode: sys_depart_orgcode,
        sbtypes: "73",
      }).then((res) => {
        this.dictOption = [];
        let result = res.result;
        result.forEach((re) => {
          var xinxi = "";
          if (re.status == 1) {
            xinxi = "在线";
          } else {
            xinxi = "离线";
          }
          this.dictOption.push({
            text: re.sbname,
            value: re.sbjno,
            color: re.status,
            text1: xinxi,
          });
        });
        console.log(this.dictOption);
      });
    },
  },
};
</script>
<style scoped>
>>> .ant-card-body {
  padding: 10px;
}
</style>
<style>
.amap-marker-label {
  background-color: rgba(1, 2, 73, 0);
  color: #990055;
  border: 0px solid rgba(33, 150, 227, 0.5);
}
</style>
