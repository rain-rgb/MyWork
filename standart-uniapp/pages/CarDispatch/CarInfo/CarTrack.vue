<template>
  <view id="home">
    <cu-custom bgColor="bg-white" :isBack="true">
      <block slot="content">车辆轨迹回放</block>
    </cu-custom>
    <u-loading-page :loading="loading" loading-text="加载中..."></u-loading-page>
    <view>
      <map
        v-if="polyline[0].points.length > 0"
        id="myMap"
        :markers="markers"
        :polyline="polyline"
        :include-points="polyline[0].points"
        :latitude="polyline[0].points[0].latitude"
        :longitude="polyline[0].points[0].longitude"
        style="width: 100%; height: calc(100vh - 45px)"
      />
      <view class="hcp-bottom" v-if="!dateshow">
        <!-- <view class="u-page__tag-item" v-for="(item, index) in radios" :key="index">
          <u-tag
            :text="item.title"
            :plain="!item.checked"
            :name="index"
            @click="radioClick"
          >
          </u-tag>
        </view> -->
        <!-- <button v-if="startMove" @click="handleStopMove()">暂停移动</button> -->
        <button
          :class="buttonClass == '开始回放' ? 'buttonBlue' : 'button'"
          @click.stop="handleStartMove('开始回放')"
        >
          开始回放
        </button>
        <button
          :class="buttonClass == '五倍回放' ? 'buttonBlue' : 'button'"
          @click="handleStartMove5('五倍回放')"
        >
          五倍回放
        </button>
        <!-- :class="" -->
        <button
          :class="buttonClass == '十倍回放' ? 'buttonBlue' : 'button'"
          @click="handleStartMove10('十倍回放')"
        >
          十倍回放
        </button>
        <button
          :class="buttonClass == '十五倍回放' ? 'buttonBlue' : 'button'"
          @click="handleStartMove15('十五倍回放')"
        >
          十五倍回放
        </button>
        <button
          :class="buttonClass == '暂停回放' ? 'buttonBlue' : 'button'"
          @click="handleStopMove('暂停回放')"
        >
          暂停回放
        </button>
        <!-- <button
          :class="buttonClass == '继续回放' ? 'buttonBlue' : 'button'"
          @click="handleStartMove('继续回放')"
        >
          继续回放
        </button>
        <button
          :class="buttonClass == '停止回放' ? 'buttonBlue' : 'button'"
          @click="handleStartMove('停止回放')"
        >
          停止回放
        </button> -->
      </view>
      <!-- <view
        ><u--input
          placeholder="后置图标"
          suffixIcon="map-fill"
          suffixIconStyle="color: #909399"
        ></u--input
      ></view> -->
      <view class="screen-modal-item">
        <view class="screen-modal-item-input" @click="dateshow = true">
          <u--input
            placeholder="请选择时间"
            border="surround"
            v-model="datetimevalue"
            disabled
            suffixIcon="calendar"
          ></u--input>
        </view>
        <view>
          <button class="buttonSele" @click="Maplocation">查询</button>
        </view>
      </view>
      <mx-date-picker
        class="picker"
        :show="dateshow"
        :type="type"
        :value="value"
        :show-tips="true"
        :show-seconds="true"
        @confirm="confirmdate"
        @cancel="confirmdate"
      />
    </view>
  </view>
</template>
<script>
import amapFile from "../../../common/util/amap-wx.js";
import timeSelector from "@/components/wing-time-selector/wing-time-selector.vue";
import dict from "../../component/dict/dict.vue";
import api from "@/api/apis.js";
import MxDatePicker from "@/components/mx-datepicker/mx-datepicker.vue";
export default {
  name: "departlist",
  components: {
    timeSelector,
    dict,
    MxDatePicker,
  },
  data() {
    return {
      type: "rangetime",
      loading: true,
      value: "",
      datetimevalue: "",
      dateshow: false,
      buttonClass: "",
      count: "",
      // locationdata: {},
      latitude: 0,
      longitude: 0,
      covers: [],
      position: "",
      model: "",
      show: false,
      carInfo: false,
      carList: "",
      requestStatus: false,
      mapContext: null, //地图对象
      startMove: false, //是否开始回放
      nextPointIndex: 1, //下一个坐标点的索引
      durationTime: 0, //相邻两点动画持续时长默认1秒
      //路线信息
      polyline: [
        {
          width: 8,
          points: [],
          arrowLine: true,
          color: "#3591FC",
        },
      ],
      //标记点(即移动标记物)
      markers: [
        {
          id: 1,
          width: 40,
          height: 40,
          latitude: 0,
          longitude: 0,
          iconPath: "../../../static/carInfo/car3x.png",
          anchor: {
            x: 0.5,
            y: 1,
          },
          callout: {
            content: ``,
            color: "#323232", //文字颜色
            fontSize: 14, //文本大小
            borderRadius: 5, //边框圆角
            bgColor: "#FFFFFF", //背景颜色
            display: "ALWAYS", //常显
          },
        },
      ],
      radios: [
        {
          title: "开始回放",
          checked: true,
        },
        {
          title: "五倍回放",
          checked: false,
        },
        {
          title: "十倍回放",
          checked: false,
        },
        {
          title: "十五倍回放",
          checked: false,
        },
        {
          title: "暂停回放",
          checked: false,
        },
        {
          title: "继续回放",
          checked: false,
        },
        {
          title: "停止回放",
          checked: false,
        },
      ],
      params: "",
      begintime: "",
      endtime: "",
    };
  },
  onLoad(options) {
    this.params = JSON.parse(decodeURIComponent(options.params));
    this.chepai = this.params.shebeiNo_dictText;
    this.begintime = this.params.datatime.split(" ")[0] + " " + "00:00:00";
    this.endtime = this.params.datatime;
    this.markers[0].callout.content = `车牌号：${this.chepai}`;
    // console.log(params, "params ----------------------------");
    this.Maplocation();
  },
  created() {
    // this.getTrack();
  },

  methods: {
    // 时间选择
    confirmdate(e) {
      console.log(e);
      if (e) {
        this.datetimevalue = e.value.toString();
        this.begintime = e.value[0];
        this.endtime = e.value[1];
      }
      this.dateshow = false;
    },
    radioClick(name) {
      this.radios.map((item, index) => {
        item.checked = index === name ? true : false;
      });
    },
    Maplocation() {
      this.position = null;
      this.loading = true;
      this.buttonClass = "";
      this.polyline = [
        {
          width: 8,
          points: [],
          arrowLine: true,
          color: "#3591FC",
        },
      ];
      let params = this.params;
      let param = {
        shebeiNo: params.shebeiNo,
        uploadtime: this.begintime,
        projectId: this.endtime,
      };
      this.$http.post(`/clgl/frontDeviceHistorydata/tokens`, param).then((res) => {
        this.position = res.data.result.data;
        this.position.forEach((e) => {
          this.polyline[0].points.push({
            latitude: e.lat,
            longitude: e.lng,
          });
        });
        this.loading = false;

        if (res.data.result.data.length > 0) {
          console.log("成功");
        } else {
          console.log("失败");
          uni.showToast({
            title: "该时间段没有车辆信息",
            icon: "none",
            duration: 2000,
          });
        }
        this.initMapData();

        // this.durationTime = Math.ceil(30000 / this.polyline[0].points.length); //默认播放全程使用30秒，计算相连两点动画时长
        // console.log(res.data.result.data, "result-------------");
        // this.initMapData();

        // console.log(
        //   this.position,
        //   this.polyline[0].points,
        //   "车辆轨迹回放------------------"
        // );
      });
    },
    //设置地图
    initMapData() {
      this.initMarkers();
      this.mapContext = uni.createMapContext("myMap", this);
    },
    //设置位置（从起点开始）
    initMarkers() {
      this.markers[0].latitude = this.polyline[0].points[0].latitude;
      this.markers[0].longitude = this.polyline[0].points[0].longitude;
    },
    //开始回放
    handleStartMove(e) {
      // if (!this.startMove || e != this.buttonClass) {
      if (!this.startMove) {
        this.func(e, 15);
      }
      if (this.startMove && this.buttonClass != "开始回放") {
        uni.showToast({
          title: "请先暂停回放，再切换倍速",
          icon: "none",
          duration: 2000,
        });
      }
    },
    //五倍回放
    handleStartMove5(e) {
      if (!this.startMove) {
        console.log("1111111111111");
        this.func(e, 3);
      }
      if (this.startMove && this.buttonClass != "五倍回放") {
        uni.showToast({
          title: "请先暂停回放，再切换倍速",
          icon: "none",
          duration: 2000,
        });
      }
    },
    //十倍回放
    handleStartMove10(e) {
      if (!this.startMove) {
        this.func(e, 1.5);
      }
      if (this.startMove && this.buttonClass != "十倍回放") {
        uni.showToast({
          title: "请先暂停回放，再切换倍速",
          icon: "none",
          duration: 2000,
        });
      }
    },
    //十五倍回放
    handleStartMove15(e) {
      if (!this.startMove) {
        this.func(e, 1);
      }
      if (this.startMove && this.buttonClass != "十五倍回放") {
        uni.showToast({
          title: "请先暂停回放，再切换倍速",
          icon: "none",
          duration: 2000,
        });
      }
    },
    //停止移动
    handleStopMove(e) {
      this.startMove = false;
      this.buttonClass = e;
    },
    func(e, timeT) {
      this.startMove = false;
      this.startMove = true;
      this.buttonClass = e;
      this.durationTime = "";
      this.durationTime = Math.ceil((4 * timeT * 6000) / this.polyline[0].points.length); //默认播放全程使用60秒，计算相连两点动画时长
      // this.durationTime = Math.ceil(4 * timeT * 6000); //默认播放全程使用60秒，计算相连两点动画时长
      // this.debounce(this.movePoint1(), 2000);
      this.movePoint(e);
      console.log("按钮点击函数执行");
    },
    //移动坐标
    movePoint(e) {
      /*
			//也可以用这个方法
			this.mapContext.moveAlong({
				duration: 30000,
				markerId: this.markers[0].id,
				path: this.polyline[0].points
			})
			return
			*/
      console.log(this.durationTime, this.nextPointIndex, "1111111111111");
      this.mapContext.translateMarker({
        duration: this.durationTime,
        markerId: this.markers[0].id,
        destination: {
          latitude: this.polyline[0].points[this.nextPointIndex].latitude,
          longitude: this.polyline[0].points[this.nextPointIndex].longitude,
        },
        animationEnd: (res) => {
          //播放结束，继续移动到下一个点，最后一个点时结束移动
          if (this.nextPointIndex < this.polyline[0].points.length - 1) {
            this.nextPointIndex++;
            if (this.startMove) {
              this.movePoint();
            }
          } else {
            console.log("text--------------");
            this.buttonClass = "";
            this.nextPointIndex = 1;
            this.startMove = false;
          }
        },
      });
    },
  },
};
</script>

<style lang="scss">
#home {
  // background-color: #F2F5FE;
  width: 100%;
  height: 100%;
  background-color: #f2f5fe;
  // white-space: nowrap;
}

.section {
  width: 100%;
  height: 100%;
  margin: 0 auto;
  // border: 1px solid #ff5500;
  // background-color: #fff;
  .map {
    width: 100%;
    height: 1538upx;
    // height: 730upx;
    // border: 1px solid #ED1C24;
  }
}

.hcp-bottom {
  right: 0;
  top: 830upx;
  width: 180upx;
  position: fixed;
  z-index: 999;
}
.button {
  width: 160upx;
  color: #4c5971;
  font-size: 14upx;
  font-family: "PingFang-SC-Medium";
  font-weight: normal;
  padding: 0 15upx;
  font-weight: 600;
  background-color: rgba(189, 188, 188, 0.7);
  margin-bottom: 40upx;
}
.buttonBlue {
  width: 160upx;
  color: #ffffff;
  font-size: 14upx;
  font-family: "PingFang-SC-Medium";
  font-weight: normal;
  padding: 0 15upx;
  font-weight: 600;
  background-color: #0f63fe;
  margin-bottom: 40upx;
}
.screen {
  position: fixed;
  background: rgba(000, 000, 000, 0.3);
  left: 0;
  top: 0;
  width: 100%;
  height: 100%;

  &-modal {
    background-color: #ffffff;
    position: absolute;
    top: 50%;
    left: (750upx - 690upx) / 2;
    width: 690upx;
    transform: translateY(-50%);
    box-shadow: 0 0 20px 0 rgba(0, 0, 0, 0.1);
    border-radius: 12upx;
    padding: 20upx;

    &-item {
      text-align: left;
      // width: 90%;
      margin-bottom: 30upx;

      &-name {
        color: #4c5971;
        font-size: 30upx;
        margin-bottom: 30upx;
      }

      &-input {
      }
    }

    &-btn {
      display: flex;
    }
  }
}
.screen-modal-item {
  position: absolute;
  top: 150upx;
  display: flex;
  align-items: center;
  justify-content: center;
  // background-color: #fff;
  width: 100%;
  z-index: 990;
  .screen-modal-item-input {
    width: 78%;
    input {
      font-size: 10upx;
      // font: 600;
    }
  }
  .buttonSele {
    width: 120upx;
    color: #ffffff;
    font-size: 14upx;
    font-family: "PingFang-SC-Medium";
    font-weight: normal;
    padding: 0 15upx;
    font-weight: 600;
    background-color: #0f63fe;
    margin-left: 20upx;
    z-index: 990;
  }
  .buttonDont {
    // background-color: #fff !important;
  }
}
.picker {
  z-index: 999;
}
#myMap {
  z-index: 100;
}
</style>
