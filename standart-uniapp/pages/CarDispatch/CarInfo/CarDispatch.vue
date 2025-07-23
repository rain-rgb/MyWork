<template>
  <view id="home">
    <cu-custom bgColor="bg-white" :isBack="true">
      <block slot="content">车辆调度管理</block>
      <!-- <view slot="right" @click="screen">筛选</view> -->
    </cu-custom>
    <view class="top-two">
      <view class="left" @click="change(3)">
        <view class="left-text">
          <p class="red">{{ carstatj.cszt }}</p>
          已登记/辆
        </view>
      </view>
      <view class="left" @click="change(1)">
        <view class="left-text">
          <p class="orange">{{ carstatj.ysz }}</p>
          在途中/辆
        </view>
      </view>
      <view class="left" @click="change(2)">
        <view class="left-text">
          <p class="green">{{ carstatj.ywc }}</p>
          卸料中/辆
        </view>
      </view>
    </view>
    <view class="top-two">
      <view class="left" @click="change(0)">
        <view class="left-text">
          <p class="purple">{{ carstatj.kx }}</p>
          场内空闲/辆
        </view>
      </view>
      <view class="left" @click="change(4)">
        <view class="left-text">
          <p class="blue">{{ carstatj.jlz }}</p>
          接料中/辆
        </view>
      </view>
      <view class="left" @click="change(5)">
        <view class="left-text">
          <!-- <p class="black">{{ carstatj.jlz }}</p> -->
          <p class="black">0</p>
          已超时/辆
        </view>
      </view>
    </view>
    <view class="section" v-for="(item, index) in position" :key="index">
      <!-- <navigator
        :url="
          '/pages/ConcreteOrder/orderProgress/orderProgressDetails?item=' +
          JSON.stringify(item)
        "
      > -->
      <view class="section-text">
        <!-- <view v-if="item.isjiesuo == 0" class="zt"></view>
          <view v-if="item.isjiesuo == 1 && item.sfhg == null" class="wsh"></view>
          <view v-if="item.isjiesuo == 1 && item.sfhg == '不合格'" class="tc"></view>
          <view v-if="item.isjiesuo == 1 && item.sfhg == '合格'" class="djs"></view>
          <view v-if="item.isjiesuo == 2" class="ywc"></view> -->
        <view v-if="index != 0 ? true : false">
          <view class=""
            ><text>车牌号:</text
            ><span>{{
              item.shebeiNo_dictText !== null ? item.shebeiNo_dictText : "暂无数据"
            }}</span></view
          >
          <view class=""
            ><text>运输里程:</text>
            <span>{{ item.miles !== null ? item.miles : "暂无数据" }}</span>
          </view>
          <view class=""
            ><text>速度:</text>
            <span>{{ item.speed !== null ? item.speed : "暂无数据" }}</span>
          </view>
        </view>
        <view class="startCar-box" v-else>
          <view class="startCar">
            <text class="start-t">明星车辆</text>
            <view class="start-img">
              <view v-for="(item, index) in 5" :key="index">
                <image src="../../../static/carInfo/start3x.png"></image>
              </view>
            </view>
          </view>
          <view class="">
            <text>车牌号:</text
            ><span>{{
              item.shebeiNo_dictText !== null ? item.shebeiNo_dictText : "暂无数据"
            }}</span></view
          >
          <view class=""
            ><text>运输里程:</text>
            <span>{{ item.miles !== null ? item.miles : "暂无数据" }}</span>
          </view>
          <view class=""
            ><text>速度:</text>
            <span>{{ item.speed !== null ? item.speed : "暂无数据" }}</span>
          </view>
        </view>
      </view>
      <!-- </navigator> -->
    </view>
    <!-- <view class="">
      <view class="increase-img" @click="increase" v-has="'peihe:add'"></view>
    </view> -->
    <!-- 查询设备 -->
    <view class="screen" v-if="show" @confirm="confirm">
      <view class="screen-modal">
        <view class="screen-modal-item">
          <view class="screen-modal-item-name">设备名称：</view>
          <picker @change="PickerChange" :range="deviceNames">
            <u--input
              placeholder="请选择设备名称"
              border="surround"
              v-model="deviceName"
              disabled
              suffixIcon="arrow-down"
            ></u--input>
          </picker>
        </view>
        <view class="screen-modal-btn">
          <u-button text="取消" @click="confirm"></u-button>
          <u-button type="primary" text="确认" @click="Choose"></u-button>
        </view>
      </view>
    </view>
  </view>
</template>
<script>
// import dict from '../../component/dict/dict.vue'
import timeSelector from "@/components/wing-time-selector/wing-time-selector.vue";
// import Byspublic from '../../component/Byspublic/Byspublic.vue'
import api from "@/api/apis.js";
export default {
  name: "departlist",
  components: {
    // dict,
    timeSelector,
    // Byspublic
  },
  data() {
    return {
      shitilist: [],
      listdatavalue: "",
      deviceNames: [],
      devictype: [],
      deviceName: "",
      choosekey: "",
      choosevalue: "",
      sbNumber: null,
      orgcode: "",
      sbtype: "55",
      index: -1,
      show: false,
      begintime: "",
      endtime: "",
      pageNo: 1,
      totaldata: {},
      jiesuo: "",
      sh: "",
      urls: `/wbshebeidetail/wbshebeiDetail/list`,
      position: "",
      carstatj: {
        cszt: 0,
        ysz: 0,
        ywc: 0,
        kx: 0,
        jlz: 0,
      },
      carMileage: [],
    };
  },
  onLoad() {
    this.textlist();
    this.tabledata();
  },
  onReachBottom() {
    uni.showNavigationBarLoading();
    this.pageNo++;
    this.textlist();
    uni.hideNavigationBarLoading();
  },
  created() {
    this.deviceType();
    this.Maplocation();
  },

  methods: {
    //获取定位信息
    Maplocation() {
      this.$http
        .get(`/clgl/clxxRealdata/listView`, {
          params: { pageSize: 40 },
        })
        .then((res) => {
          this.position = res.data.result.records;
          this.position = this.position.filter((e) => {
            return e.ifid != "离线";
          });
          this.position.forEach((e) => {
            e.status == 1
              ? this.carstatj.ysz++
              : e.status == 2
              ? this.carstatj.ywc++
              : e.status == 3
              ? this.carstatj.kx++
              : this.carstatj.jlz++;
            // if()
          });
          this.getCarMonth();
          // this.carstatj.cszt = res.data.result.total;
          this.carstatj.cszt = this.position.length;
          // this.locatlist(this.position);
          console.log(this.position, this.carstatj, "pageSize: 412130 ");
        });
    },
    // 获取当前月车辆里程
    getCarMonth() {
      this.$http
        .get(`/car/carMiles/list1`, {
          params: { pageSize: 40 },
        })
        .then((res) => {
          this.carMileage = res.data.result;
          this.position.forEach((e) => {
            this.carMileage.forEach((item) => {
              if (e.shebeiNo_dictText == item.shebeiNo) {
                this.$set(e, "miles", item.miles);
              }
            });
          });
          this.position.forEach((e) => {
            if (!e.miles) {
              e.miles = " ";
            }
          });
          this.position.sort(this.order);
          console.log(this.carMileage, this.position, "获取当前月车辆里程-------------");
        });
    },
    order(a, b) {
      console.log(a.miles, "a.miles ==================");
      if (a.miles < b.miles) {
        return 1;
      } else if (a.miles > b.miles) {
        return -1;
      } else if (a.miles == b.miles) {
        return 0;
      }
    },
    deviceType() {
      api
        .deviceType({
          params: {
            sys_depart_orgcode: this.orgcode,
            sbtypes: this.sbtype,
          },
        })
        .then((res) => {
          // console.log(res.data.result)
          let devicelist = res.data.result;
          this.deviceNames = [];
          this.devictype = [];
          devicelist.forEach((e) => {
            this.deviceNames.push(e.sbjno);
            this.devictype.push(e.sbjno);
          });
        })
        .catch((e) => {
          console.log("请求错误", e);
        });
    },
    // 设备名称
    PickerChange(e) {
      // console.log(e)
      // this.cbindex = -1
      this.index = e.detail.value;
      this.imei = this.devictype[this.index];
      this.choosekey = 2;
      this.choosevalue = this.devictype[this.index];
      this.deviceName = this.deviceNames[this.index];
      this.begintime = "";
      this.endtime = "";
      // this.Choose()
    },

    Choose(choosekey, choosevalue) {
      // console.log(this.choosekey,this.choosevalue);
      // 设备名称
      if (this.choosekey == 2) {
        this.sbNumber = this.choosevalue;
        // console.log(this.sbNumber)
        this.begintime = "";
        this.endtime = "";
      }

      this.textlist();
      this.show = false;
    },
    change(e) {
      console.log("this.jiesuo", this.jiesuo);
      this.shitilist = [];
      this.pageNo = 1;
      this.urls = `/wbshebeidetail/wbshebeiDetail/list`;
      // console.log(e, 'ffffffffffff')
      if (e == 0) {
        this.sh = "";
        if (this.jiesuo == 0) {
          this.jiesuo = "";
        } else {
          this.jiesuo = 0;
        }
      } else if (e == 3) {
        this.sh = "";
        if (this.jiesuo == 1) {
          this.jiesuo = "";
        } else {
          this.urls = `/wbshebeidetail/wbshebeiDetail/list1`;
          this.jiesuo = 1;
        }
      } else if (e == 2) {
        this.sh = "";
        if (this.jiesuo == 2) {
          this.jiesuo = "";
        } else {
          this.jiesuo = 2;
        }
      } else if (e == 4) {
        if (this.jiesuo == 1 && this.sh == "不合格") {
          this.jiesuo = "";
          this.sh = "";
        } else {
          this.jiesuo = 1;
          this.sh = "不合格";
        }
      } else if (e == 1) {
        if (this.jiesuo == 1 && this.sh == "合格") {
          this.jiesuo = "";
          this.sh = "";
        } else {
          this.jiesuo = 1;
          this.sh = "合格";
        }
      }
      this.textlist();
    },
    textlist() {
      console.log("this.jiesuoeed", this.jiesuo);
      this.$http
        .get(this.urls, {
          params: {
            column: "id",
            order: "desc",
            pageNo: this.pageNo,
            PageSize: 10,
            sbbh: this.sbNumber,
            isjiesuo: this.jiesuo,
            sfhg: this.sh,
            // shebeibianhao: this.sbNumber,
          },
        })
        .then((res) => {
          if (res.data.result.records.length == 0) {
            uni.showToast({
              title: "别下拉啦 没有更多数据了",
              icon: "none",
            });
          }
          if (this.pageNo != 1) {
            this.shitilist = this.shitilist.concat(res.data.result.records);
          } else {
            this.shitilist = res.data.result.records;
          }
        });
    },
    tabledata() {
      this.$http.get(`/wbshebeidetail/wbshebeiDetail/list3`).then((res) => {
        console.log(res.data.result, "标签统计");
        this.totaldata = res.data.result;
      });
    },

    increase() {
      uni.navigateTo({
        url: "/pages/Rawmaterial/depart/departadd",
      });
    },
    screen() {
      this.deviceName = "";
      this.show = true;
    },
    handleOk() {
      this.textlist();
      this.show = false;
    },
    confirm() {
      this.show = false;
    },
  },
};
</script>

<style lang="scss" scoped>
#home {
  // background-color: #F2F5FE;
  width: 100%;
  height: auto;
  background-color: #f2f5fe;
  // white-space: nowrap;
}

.bg {
  width: 100%;
  height: 150upx;
  background-color: #ffffff;
  position: fixed;
}

.header {
  width: 100%;
  height: 100upx;
  display: flex;

  // background-color: #506CE2;
  &-left {
    width: 20%;
    display: flex;
    justify-content: center;
    align-items: flex-end;

    &-tagpage {
      width: 34upx;
      height: 37upx;
      margin-left: 30upx;
    }

    &-screen {
      width: 34upx;
      height: 37upx;
      margin-left: 30upx;
    }

    // background-color: red;
  }

  &-center {
    width: 60%;
    // background-color: blue;
    display: flex;
    align-items: flex-end;
    overflow: hidden;
    white-space: nowrap;

    .center {
      width: 40%;
      text-align: center;
      font-size: 38upx;
      color: #ffffff;
    }

    .side {
      width: 30%;
      text-align: center;
      color: rgba(255, 255, 255, 0.4);
      // overflow: hidden;
      // white-space: nowrap;
    }
  }
  &-right {
    width: 20%;
    display: flex;
    justify-content: center;
    align-items: flex-end;

    &-tagpage {
      width: 34upx;
      height: 37upx;
      margin-left: 30upx;
    }

    &-screen {
      width: 34upx;
      height: 37upx;
      margin-left: 30upx;
    }

    // background-color: red;
  }
}

.newcolor {
  color: #000;
  font-size: 30upx;
}

.top-two {
  width: 700upx;
  height: 180upx;
  margin: 0 auto;
  // margin-top: 80upx;
  // border-radius: 16upx;
  // border:1px solid #1785FF;
  display: flex;
  justify-content: space-between;
  align-items: center;

  .left {
    width: 225upx;
    height: 150upx;
    border-radius: 10px;
    background-color: #ffffff;
    display: flex;
    justify-content: space-evenly;
    align-items: center;

    .left-img {
      width: 120upx;
      height: 110upx;
      // border: 1px solid blue;
      background: #cfe0fd;
      border-radius: 10px;

      .img {
        width: 60upx;
        height: 60upx;
        background-image: url(../../../static/shiti/five.png);
        // border: 1px solid #000;
        margin: 0 auto;
        line-height: 90upx;
        margin: 12px auto;
        background-size: 100% 92%;
        background-repeat: no-repeat;
      }
    }

    .left-text {
      width: 160upx;
      height: 100upx;
      // border: 1px solid #18BC37;
      color: #4c5971;
      text-align: center;
      font-size: 27upx;
      line-height: 50upx;
      font-family: "DIN-Medium";

      .red {
        color: #ff233d;
        font-size: 50upx;
      }

      .orange {
        color: #ff8712;
        font-size: 50upx;
      }

      .green {
        color: #0fbf6e;
        font-size: 50upx;
      }

      .purple {
        color: #8333fa;
        font-size: 50upx;
      }

      .blue {
        color: #387ffd;
        font-size: 50upx;
      }

      .black {
        color: #333333;
        font-size: 50upx;
      }
    }
  }
}

.newcolor/deep/.cu-bar.fixed {
  position: fixed;
  width: 100%;
  top: 22px;
  z-index: 1024;
}

.scrolls-y {
  width: 100%;
  height: auto;
  // white-space: nowrap;
}

.section {
  width: 700upx;
  height: auto;
  border-radius: 10px;
  margin: 0 auto;
  background-color: #fff;

  .section-text {
    color: #9299a8;
    font-size: 30upx;
    padding: 15px 15px;
    line-height: 55upx;
    margin: 10px 0;
    text {
      width: 130upx;
      display: inline-block;
      color: #000;
      font-weight: 600;
      // text-align: justify;
      // text-align-last: justify;
    }
    span {
      color: #4c5971;
      font-size: 30upx;
      font-family: "PingFang-SC-Medium";
      padding: 0 15upx;
    }
  }
}

.zt {
  width: 197px;
  height: 145px;
  background-image: url(../../../static/shiti/zt.png);
  background-size: 44% 62%;
  background-repeat: no-repeat;
  position: absolute;
  right: -98px;
  margin-top: -15px;
  -webkit-transform: rotate(30deg);
  transform: rotate(0deg);
}

.wsh {
  width: 197px;
  height: 145px;
  background-image: url(../../../static/shiti/dsh.png);
  background-size: 44% 62%;
  background-repeat: no-repeat;
  position: absolute;
  right: -98px;
  margin-top: -15px;
  -webkit-transform: rotate(30deg);
  transform: rotate(0deg);
}

.tc {
  width: 197px;
  height: 145px;
  background-image: url(../../../static/shiti/tc.png);
  background-size: 44% 62%;
  background-repeat: no-repeat;
  position: absolute;
  right: -98px;
  margin-top: -15px;
  -webkit-transform: rotate(30deg);
  transform: rotate(0deg);
}

.djs {
  width: 197px;
  height: 145px;
  background-image: url(../../../static/shiti/djs.png);
  background-size: 44% 62%;
  background-repeat: no-repeat;
  position: absolute;
  right: -98px;
  margin-top: -15px;
  -webkit-transform: rotate(30deg);
  transform: rotate(0deg);
}

.ywc {
  width: 197px;
  height: 145px;
  background-image: url(../../../static/shiti/ywc.png);
  background-size: 44% 62%;
  background-repeat: no-repeat;
  position: absolute;
  right: -98px;
  margin-top: -15px;
  -webkit-transform: rotate(30deg);
  transform: rotate(0deg);
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

  .fixstyle {
    width: 320px;
    height: 37px;
    border: 1px solid #c4c9d0;
    background-color: #f5f7fa;
    border-radius: 5px;
    line-height: 37px;
    display: flex;
  }
}

.increase-img {
  width: 24%;
  height: 87px;
  background-image: url(../../../static/pour/add.png);
  background-size: 100% 100%;
  position: fixed;
  bottom: 176px;
  right: -18px;
}
.startCar-box {
}
.startCar {
  background-color: #0f63ff;
  border-radius: 20upx 20upx 20upx 0;
  width: 350upx;
  height: 140upx;
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative;
  top: -40upx;
  left: -27upx;
  .start-t {
    color: #fff !important;
  }
  .start-img {
    display: flex;
    flex-direction: row;
    // background-color: #fff;
  }
  image {
    width: 40upx;
    height: 40upx;
    display: block;
  }
}
</style>
