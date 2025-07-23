<template>
  <view id="home">
    <cu-custom bgColor="bg-white" :isBack="true">
      <block slot="content">车辆调度</block>
    </cu-custom>
    <view class="section">
      <view class="map">
        <map
          scale="7"
          id="map"
          :latitude="latitude"
          :longitude="longitude"
          :markers="covers"
          style="width: 100%; height: 100%"
          @markertap="markertap"
        >
        </map>
      </view>
    </view>
    <u-popup :show="show" mode="left" @close="close" @open="open">
      <u-list @scrolltolower="scrolltolower">
        <u-list-item v-for="(item, index) in position" :key="index">
          <u-cell
            icon="map"
            :title="item.shebeiNo_dictText"
            :value="item.status_dictText"
            @click="shwoTab(item.id)"
          ></u-cell>
        </u-list-item>
      </u-list>
    </u-popup>
    <view class="section-text" v-if="carInfo">
      <view class="tab"
        ><u-icon class="tab-icon" name="close" @click="carInfo = false">></u-icon></view
      >
      <view class=""
        ><text>车牌号</text>:<span>{{ carList.shebeiNo_dictText }}</span></view
      >
      <!-- <view class=""><text>运输里程</text>:<span>{{}}</span></view> -->
      <view class=""
        ><text>速度</text>:<span>{{ carList.speed }}</span></view
      >
    </view>
    <!-- <view class="button" @click="showTab()"></view> -->
    <!-- <view class="button-right" @click="buttonRight()"></view> -->
    <view class="" @click="show = true"></view>
  </view>
</template>
<script>
import amapFile from "../../../common/util/amap-wx.js";
import timeSelector from "@/components/wing-time-selector/wing-time-selector.vue";
import dict from "../../component/dict/dict.vue";
// import api from "@/api/apis.js";
export default {
  name: "departlist",
  components: {
    timeSelector,
    dict,
  },
  data() {
    return {
      // gys: [],
      // mdd: [],
      // supplier: '',
      // gyslist: '',
      // mdidata: '',
      // Endto: '',
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
    };
  },
  onLoad() {
    // let subNVue = uni.getSubNVueById("video_mask");
    // console.log(subNVue,'subNVue-------------');
    // subNVue.show();
    //结束上一次监听
    // uni.$emit('getDataPop',this.position);
            
  },
  created() {
    // this.gystype()
    // this.mddtype()
    this.Maplocation();

  },

  methods: {
    buttonRight() {
      uni.navigateTo({
        //保留当前页面，跳转到应用内的某个页面
        url: "/pages/CarDispatch/CarInfo/CarDispatch",
      });
    },
    showTab() {
      uni.$emit("showTabFun", true);
    },
    shwoTab(id) {
      this.show = false;
      this.carInfo = true;
      this.carList = "";
      this.carList = this.position.filter((e) => {
        e.id == id;
      });
      this.position.forEach((e) => {
        if (e.id == id) {
          this.carList = e;
          console.log(e, "e----------------");
        }
      });
      console.log(this.carList, "this.carList----------------");
    },
    open() {
      // console.log('open');
    },
    close() {
      this.show = false;
      // console.log('close');
    },
    Maplocation() {
      this.$http
        .get(`/clgl/clxxRealdata/listView`, {
          params: { pageSize: 40 },
        })
        .then((res) => {
          this.position = res.data.result.records;
          this.position.forEach((e) => {
            e.status == 0
              ? (e.status_dictText = "在途运输")
              : e.status == 1
              ? (e.status_dictText = "卸料中")
              : e.status == 2
              ? (e.status_dictText = "场内空闲")
              : (e.status_dictText = "接料中");
          });
          this.locatlist(this.position);
          console.log("传输中")
          uni.$emit('getDataPop', this.position)
          // console.log(res.data.result.records, "pageSize: 40 ");
        });
    },
    //标记点用于在地图上显示标记的位置
    locatlist(e) {
      // console.log(e, '定位')
      e.forEach((item) => {
        this.latitude = parseFloat(item.latitude);
        this.longitude = parseFloat(item.longitude);
        this.covers.push({
          id: item.id,
          latitude: parseFloat(item.latitude),
          longitude: parseFloat(item.longitude),
          iconPath: "../../../static/carInfo/carP3x.png",
          width: 50,
          height: 50,
          // title:item.cph + "——"+item.danhao,
          callout: {
            content: `车牌号：${item.shebeiNo_dictText}`,
            color: "#323232", //文字颜色
            fontSize: 14, //文本大小
            borderRadius: 5, //边框圆角
            bgColor: "#FFFFFF", //背景颜色
            display: "ALWAYS", //常显
          },
        });
      });
      // console.log(this.covers)
    },
    // locatlist(e) {
    //   // console.log(e, '定位')
    //   e.forEach((item) => {
    //     this.latitude = parseFloat(item.ddlat);
    //     this.longitude = parseFloat(item.ddlng);
    //     this.covers.push({
    //       id: item.id,
    //       latitude: parseFloat(item.ddlat),
    //       longitude: parseFloat(item.ddlng),
    //       iconPath: "../../../static/shiti/jt.png",
    //       width: 50,
    //       height: 50,
    //       // title:item.cph + "——"+item.danhao,
    //       callout: {
    //         content: `车牌号：${item.cph}
    // 								发车单号：${item.danhao}`,
    //         color: "#323232", //文字颜色
    //         fontSize: 14, //文本大小
    //         borderRadius: 5, //边框圆角
    //         bgColor: "#FFFFFF", //背景颜色
    //         display: "ALWAYS", //常显
    //       },
    //     });
    //   });
    //   // console.log(this.covers)
    // },
    //点击标记点对应的气泡时触发，e.detail = {markerId}
    markertap(e) {
      let iddata = e.detail.markerId;
      let arr = this.position.filter((el) => {
        return el.id == iddata;
      });
      let params = encodeURIComponent(JSON.stringify(arr[0]));
      // console.log(params, "params----------------");
      // console.log(this.position, "this.position----------------");
      // console.log(iddata, arr, "点击标记点对应的气泡时触发----------------");
      uni.navigateTo({
        //保留当前页面，跳转到应用内的某个页面
        url: "/pages/CarDispatch/CarInfo/CarTrack?params=" + params,
      });
      this.$http
        .get(`/wbshebeidetail/wbshebeiDetail/reallist`, {
          params: {
            id: iddata,
          },
        })
        .then((res) => {
          // console.log(res.data.result[0])
          this.model = res.data.result[0];
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

.sectionall {
  width: 100%;
  height: 675upx;
  // border: 1px solid #1CBBB4;
  border-radius: 10px;
  background-color: #fff;
  position: absolute;
  top: 423px;
  // position: absolute;
  // top: 400px;
  .article {
    width: 700upx;
    height: 125upx;
    margin: 25px auto;
    text-align: center;
    // border: 1px solid #91CB74;
    border-radius: 10px;
    display: flex;
    flex-direction: row;
    box-shadow: 0px 0px 25px 0px rgba(149, 166, 182, 0.16);

    .article-txt {
      width: 233upx;
      height: 160upx;
      // border: 1px solid #ff0000;

      .transport {
        // width: 120upx;
        height: 28upx;
        font-size: 30upx;
        font-family: PingFang SC;
        font-weight: 500;
        color: #333333;
        line-height: 100upx;
      }

      .num {
        // width: 40upx;
        height: 25upx;
        font-size: 34upx;
        font-family: PingFang SC;
        font-weight: 800;
        color: #387ffd;
        line-height: 140upx;
      }
    }
  }

  .footer {
    width: 700upx;
    height: auto;
    // border: 1px solid #F37B1D;
    margin: 20px auto;

    .wrap-nav {
      width: 550upx;
      height: 40px;
      // border: 1px solid #1785FF;
      margin: 0 auto;
      padding: 29px 0;
      display: flex;
      flex-direction: row;
      justify-content: space-around;
      align-items: center;

      .screen-modal-item-name {
        flex: 3;
      }

      .screen-picker {
        flex: 8;
      }
    }
  }
}
.button {
  background-image: url(../../../static/rawmaterial/two.png);
  background-size: cover;
  background-position: center;
  width: 60upx;
  height: 60upx;
  position: absolute;
  z-index: 999;
  top: 800upx;
  left: 10upx;
}
.button-right {
  background-image: url(../../../static/shiti/h.png);
  background-size: cover;
  background-position: center;
  width: 60upx;
  height: 60upx;
  position: absolute;
  z-index: 999;
  top: 800upx;
  right: 10upx;
}
.section-text {
  width: 700upx;
  height: auto;
  border-radius: 10px;
  margin: 0 auto;
  background-color: #fff;
  color: #000000;
  font-weight: 600;
  font-size: 30upx;
  padding: 15px 15px;
  line-height: 55upx;
  position: absolute;
  z-index: 10;
  top: 200upx;
  left: 30upx;
  text {
    width: 130upx;
    display: inline-block;
    text-align: justify;
    text-align-last: justify;
  }
  span {
    color: #4c5971;
    font-size: 30upx;
    font-family: "PingFang-SC-Medium";
    font-weight: normal;
    padding: 0 15upx;
  }
}
.tab {
  display: flex;
  .tab-icon {
    margin-left: auto;
  }
}
</style>
