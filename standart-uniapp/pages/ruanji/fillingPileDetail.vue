<template>
  <view id="mixpilerealtime">
    <cu-custom bgColor="bg-white" :isBack="true">
      <block slot="content">灌注桩详情</block>
    </cu-custom>
    <!-- <view class="video">
			<video :src="videosrc" controls></video>
		</view> -->
    <view class="title">实时参数</view>
    <view class="con" v-for="(item, index) in listdata" :key="index">
      <view class="con-left">
        <view class="con-left-imgbg" id="jingimg">
          <view class="con-left-imgbg-down" v-if="item.chaobiaodengji == '1'">
            <image
              src="../../static/ruanji/down.png"
              :style="{ height: ztheight + 'px' }"
            />
          </view>
        </view>
        <view class="con-left-sta">
			状态：
          <span style="color: grey" v-if="isLessThanMinutesAgo(item.updateTime)"
            >离线</span
          >
          <span
            style="color: green"
            v-else-if="item.chaobiaodengji == '0' || item.chaobiaodengji == null"
            >在线</span
          >
          <span style="color: blue" v-else-if="item.chaobiaodengji == '1'">下钻</span>
        </view>
        <view class="con-left-realzs">
          实际深度(m):<span>{{ parseFloat(item.pileRealdep).toFixed(2) || 0 }}m</span>
          <!-- 实时钻深：<span>{{ parseFloat(item.pileRealdep).toFixed(2) || 0 }}m</span> -->
        </view>
      </view>
      <view class="con-right">
        <view class="con-right-item">
          <view class="con-right-item-name">里程:</view>
          <view class="con-right-item-num">
            <view>
              {{ item.pileMileage }}
            </view>
          </view>
        </view>
        <view class="con-right-item">
          <view class="con-right-item-name">桩号:</view>
          <view class="con-right-item-num">{{ item.pileNo }}</view>
        </view>
        <view class="con-right-item">
          <view class="con-right-item-name">密度:</view>
          <view class="con-right-item-num">
            {{ parseFloat(item.pileDensity).toFixed(2) }} kg/m³
          </view>
        </view>
        <view class="con-right-item">
          <view class="con-right-item-name">倾角X:</view>
          <view class="con-right-item-num">
            <span v-if="item.pileX">{{ parseFloat(item.pileX).toFixed(2) }}</span>
          </view>
        </view>
        <view class="con-right-item">
          <view class="con-right-item-name">倾角y:</view>
          <view class="con-right-item-num">
            <span v-if="item.pileY">{{ parseFloat(item.pileY).toFixed(2) }}</span>
          </view>
        </view>
        <view class="con-right-item">
          <view class="con-right-item-name">时间:</view>
          <view class="con-right-item-num">{{ item.updateTime }}</view>
        </view>
        <!-- <view class="con-right-item">
					<text class="con-right-item-name">实时流量(L/min):</text>
					<text class="con-right-item-num">{{ (parseFloat(item.flowlst)).toFixed(2) }}</text>
				</view>
				<view class="con-right-item">
					<text class="con-right-item-name">实时电流(A):</text>
					<text class="con-right-item-num">{{ (parseFloat(item.elec)).toFixed(2)  }}</text>
				</view>
				<view class="con-right-item">
					<text class="con-right-item-name">实时速度(m/min):</text>
					<text class="con-right-item-num">{{ (parseFloat(item.speed)).toFixed(2) }}</text>
				</view>
				<view class="con-right-item">
					<text class="con-right-item-name">水平倾斜度(°):</text>
					<text class="con-right-item-num">{{ (parseFloat(item.x)).toFixed(2) }}</text>
				</view>
				<view class="con-right-item">
					<text class="con-right-item-name">垂直倾斜度(°):</text>
					<text class="con-right-item-num">{{ (parseFloat(item.y)).toFixed(2) }}</text>
				</view> -->
        <!-- <view class="con-right-item">
					<text class="con-right-item-name">每米喷浆量(L):</text>
					<text class="con-right-item-num">{{ (parseFloat(item.permgrout)).toFixed(2) }}</text>
				</view> -->
        <!-- <view class="con-right-item">
					<text class="con-right-item-name">水泥用量(m³):</text>
					<text class="con-right-item-num">{{ (parseFloat(item.grouting)).toFixed(2) }}</text>
				</view>
				<view class="con-right-item">
					<text class="con-right-item-name">当前时间:</text>
					<text class="con-right-item-num">{{nowtime}}</text>
				</view> -->
        <view class="con-right-item"></view>
      </view>
    </view>
    <view class="title">实时定位</view>
    <view class="location" v-if="mapshow">
      <iframe
        style="width: 100%; height: 100%"
        :src="
          'http://121.89.166.108:9080/#/mixpilerealtime?longitude=' +
          listdata[0].pileLgd +
          '&latitude=' +
          listdata[0].pileLtd
        "
        frameborder="0"
      ></iframe>
      <!-- <web-view style="width: 100%; height: 100%;" src="http://121.89.166.108:9080/#/mixpilerealtime?longitude=119.664304&latitude=33.961013"></web-view> -->
      <!-- <map style="width: 100%; height: 100%;" :latitude="latitude" :longitude="longitude"
				:markers="covers"></map> -->
    </view>

    <view style="height: 50upx"></view>
  </view>
</template>

<script>
import ruanjiapi from "../../api/ruanji.js";
export default {
  data() {
    return {
      mapshow: false,
      shebeino: "",
      id: "",
      listdata: [],
      nowtime: "",
      latitude: 0,
      longitude: 0,
      covers: [],
      videosrc: "",
      ztheight: 0,
      jingimgheight: 0,
    };
  },
  onPullDownRefresh() {
    uni.showNavigationBarLoading();
    this.getloadlist();
    uni.stopPullDownRefresh();
    uni.hideNavigationBarLoading();
  },
  onLoad(options) {
    // console.log(options)
    this.shebeino = options.shebeino;
    this.id = options.id;
    // this.getCameraId()
    this.getloadlist();
  },
  watch: {
    listdata: function () {
      this.$nextTick(function () {
        //你需要的操作逻辑
        this.handlerzt();
      });
    },
  },
  methods: {
    // 获取搅拌桩数据
    getloadlist() {
      this.nowtime = this.timeapi(new Date(), "hh:mm");
      let params = {
        id: this.id,
      };
      ruanjiapi
        .fillingPilelist({
          params,
        })
        .then((res) => {
          // console.log(res, "搅拌桩数据")
          if (res.data.result.records.length > 2) {
            this.listdata = res.data.result.records.slice(0, 2);
          } else {
            this.listdata = res.data.result.records;
          }
          let datas = this.listdata[0];
          this.huidian(datas);

          // this.handlerzt(datas)
        })
        .catch((e) => {
          console.log("请求错误", e);
        });
    },
    // 计算箭头高度
    handlerzt() {
      let query = uni.createSelectorQuery().in(this);
      query
        .select("#jingimg")
        .boundingClientRect((data) => {
          console.log(data);
          this.jingimgheight = data.height;
        })
        .exec();
      console.log(this.jingimgheight,'1111111mmmmmmmmmmmm');
      let num = Math.abs(this.listdata[0].pileRealdep) / 10;
    //   this.ztheight = num;
      this.ztheight = num * this.jingimgheight;
    },
    huidian(e) {
      // console.log(e)
      // console.log(e.ltdfloat)
      // console.log(e.lgdfloat)
      if (e.ltdfloat == 0) {
        // console.log(111)
        this.latitude = 39.909;
        this.longitude = 116.39742;
        this.mapshow = true;
        return;
      }
      this.latitude = parseFloat(e.ltdfloat);
      this.longitude = parseFloat(e.lgdfloat);
      this.covers = [
        {
          latitude: parseFloat(e.ltdfloat),
          longitude: parseFloat(e.lgdfloat),
          iconPath: "../../static/icon/snjbz.png",
          width: 30,
          height: 30,
          callout: {
            //自定义标记点上方的气泡窗口 点击有效
            content: `经度：${e.lgdfloat}
											纬度：${e.ltdfloat}
											高程：0`, //文本
            color: "#ffffff", //文字颜色
            fontSize: 14, //文本大小
            borderRadius: 2, //边框圆角
            bgColor: "#00c16f", //背景颜色
            display: "ALWAYS", //常显
          },
        },
      ];
      this.mapshow = true;
    },
    // 获取摄像头设备id
    getCameraId() {
      let params = {
        remark: this.shebeino,
      };
      this.$http
        .get("/monitor/monitor/list", {
          params,
        })
        .then((res) => {
          console.log(res, "视频设备id");
          let camerajk = res.data.result.records[0];
          console.log(camerajk);
          if (camerajk == "" || camerajk == undefined) {
            uni.showToast({
              title: "此搅拌桩没有安装视频设备！！",
              icon: "none",
            });
          } else {
            this.videosrc = camerajk.vaddress;
          }
          this.videoloading = true;
        });
    },
    timeapi(time, fmt) {
      var o = {
        "M+": time.getMonth() + 1, //月份
        "d+": time.getDate(), //日
        "h+": time.getHours(), //小时
        "m+": time.getMinutes(), //分
        "s+": time.getSeconds(), //秒
        "q+": Math.floor((time.getMonth() + 3) / 3), //季度
        S: time.getMilliseconds(), //毫秒
      };
      if (/(y+)/.test(fmt)) {
        fmt = fmt.replace(
          RegExp.$1,
          (time.getFullYear() + "").substr(4 - RegExp.$1.length)
        );
      }
      for (var k in o) {
        if (new RegExp("(" + k + ")").test(fmt)) {
          fmt = fmt.replace(
            RegExp.$1,
            RegExp.$1.length == 1 ? o[k] : ("00" + o[k]).substr(("" + o[k]).length)
          );
        }
      }
      return fmt;
    },
    isLessThanMinutesAgo(timeStr) {
      const parts = timeStr.split(" ");
      const dateParts = parts[0].split("-");
      const timeParts = parts[1].split(":");
      // 创建Date对象（基于本地时区）
      const targetDate = new Date(
        parseInt(dateParts[0]), // year
        parseInt(dateParts[1]) - 1, // month (0-based)
        parseInt(dateParts[2]), // day
        parseInt(timeParts[0]), // hours
        parseInt(timeParts[1]), // minutes
        parseInt(timeParts[2]) // seconds
      );
      // 获取当前时间（基于本地时区）
      const now = new Date();
      // 计算时间差（毫秒）
      const diff = now - targetDate;
      // 将时间差转换为分钟
      const diffInMinutes = diff / (1000 * 60);
      console.log(diffInMinutes > 10, "diffInMinutes > 10");
      // 检查时间差是否大于10分钟
      return diffInMinutes > 10;
    },
  },
};
</script>

<style lang="scss" scoped>
#mixpilerealtime {
  width: 100%;
  background-color: #f3f5fe;

  .title {
    font-size: 36upx;
    color: #333333;
    font-weight: 600;
    margin-top: 30upx;
    margin-left: 30upx;
  }

  .video {
    width: 100%;
    height: 430upx;

    video {
      width: 100%;
      height: 100%;
    }
  }

  .con {
    width: 690upx;
    height: 530upx;
    margin: 0 auto;
    margin-top: 30upx;
    border-radius: 16upx;
    background-color: white;
    display: flex;

    &-left {
      width: 40%;
      height: 100%;

      // background-color: red;
      &-imgbg {
        width: 90%;
        height: 65%;
        margin: 0 auto;
        margin-top: 40upx;
        background-image: url(../../static/ruanji/jing.png);
        background-repeat: no-repeat;
        background-size: 100% 100%;

        // background-color: blue;
        &-down {
          width: 15%;
          margin: 0 auto;
        }
      }

      &-sta {
        text-align: center;
        margin-top: 30upx;
        color: #9299a8;

        span {
          color: #4c5971;
        }
        &-text {
          margin-left: 2px;
        }
      }

      &-realzs {
        text-align: center;
        // margin-top: 20upx;
        color: #9299a8;

        span {
          color: #387ffd;
        }
      }
    }

    &-right {
      width: 60%;
      height: 100%;

      // background-color: blue;
      &-item {
        font-size: 28upx;
        margin-top: 20upx;
        // background-color: rgb(86, 86, 204);
        // position: relative;
        display: flex;
        justify-content: space-between;

        &-name {
          color: #9299a8;
          text-align: left;
          margin-left: 20upx;
        }

        &-num {
          color: #4c5971;
          //   float: right;
          margin-right: 20upx;
          text-align: right;
          width: 290upx;
        //   background-color: rgb(211, 91, 91);
          //   white-space: nowrap;
          text-overflow: ellipsis;
          overflow: hidden;
          word-break: break-all;
          //   text-align: left;
		  view{
          	display: inline-block;
			// text-align: left;
          	// background-color: rgb(145, 211, 91);
		  }
        }
      }
    }
  }

  .location {
    position: relative;
    width: 690upx;
    height: 370upx;
    margin: 0 auto;
    margin-top: 30upx;
  }
  .jwdbq {
    width: 400upx;
    height: 600upx;
    position: absolute;
    top: 0px;
    z-index: 100000;
    background-color: red;
  }
}
</style>
