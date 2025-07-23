<template>
  <view class="wrap">
    <cu-custom bgColor="bg-white" :isBack="true">
      <block slot="content">视频监控</block>
      <view slot="right" @click="screen">筛选</view>
    </cu-custom>
    <!-- <view class="wrap-title">
      <view class="wrap-title-left">
        <view class="wrap-title-left-img" @click="changeData(0)">
          <image class="wrap-title-left-image" src="../../../static/home/jcc.png"></image>
        </view>
        <h2 @click="clickmaterial(0)">进场材料</h2>
      </view>
      <view class="wrap-title-left">
        <view class="wrap-title-left-img" @click="changeData(1)">
          <image class="wrap-title-left-image" src="../../../static/home/jcc.png"></image>
        </view>
        <h2 @click="clickmaterial(1)">出场材料</h2>
      </view>
    </view> -->
    <view class="wrap-section">
      <u-loading-page :loading="loading" loading-text="加载中请稍后..."></u-loading-page>
      <view class="wrap-section-box" v-for="(item, index) in Materialdata" :key="index">
        <navigator
          :url="'/pages/smartgb/VideoMonitor/VideoMonitorDetail?item=' + encodeURIComponent(JSON.stringify((({vaddress, ...rest}) => rest)(item)))"
        >
          <!-- <view class="wrap-section-box-sta green" v-show="num == 0">
            <view style="color: #fff"> 进场 </view>
          </view>
          <view class="wrap-section-box-sta blue" v-show="num == 1">
            <view style="color: #fff"> 出场 </view>
          </view> -->
          <view class="wrap-section-box-name"
            >摄像头名称:<span>{{ item.monitorname }}</span></view
          >
          <view class="wrap-section-box-name"
            >应用场景:<span>{{ item.usetype_dictText }}</span></view
          >
          <view class="wrap-section-box-name"
            >组织机构:<span>{{ item.orgcode_dictText }}</span></view
          >
          <view class="wrap-section-box-name"  v-if="item.workstate !== null"
            >视频工作状态:<span>
						<view class="cu-tag bg-grey" v-if="item.workstate === 1">离线</view>
						<view class="cu-tag bg-green" v-if="item.workstate === 0">在线</view>
              </span></view
          >
        </navigator>
      </view>
    </view>
    <view>
      <u-modal
        :show="show"
        showConfirmButton
        showCancelButton
        @confirm="confirm"
        @cancel="cancel"
      >
        <view class="select-box">
          <view class="Task">
            设备名称:
            <view class="Task-input">
              <!-- <u--input placeholder="请选择设备名称" border="surround" v-model="Taskno" @change="change">
							</u--input> -->
              <eq :sbtype="sbtype" @choose="changevalue"></eq>
            </view>
          </view>
          <!-- <view class="Task">
            供应商:
            <view class="Task-input">
              <picker @change="Pickgys" :range="gys">
                <u--input
                  placeholder="请选择供应商"
                  border="surround"
                  v-model="supplier"
                  disabled
                  suffixIcon="arrow-down"
                ></u--input>
              </picker>
            </view>
          </view>
          <view class="Task">
            材料规格:
            <view class="Task-input">
              <picker @change="PickSpecifications" :range="Specifications">
                <u--input
                  placeholder="请选择规格"
                  border="surround"
                  v-model="specs"
                  disabled
                  suffixIcon="arrow-down"
                >
                </u--input>
              </picker>
            </view>
          </view>
          <view class="Task">
            开始时间:
            <view class="Task-input">
              <view class="screen-modal-item-input" @click="beginshow = true">
                <u--input
                  placeholder="请选择开始时间"
                  border="surround"
                  v-model="begintimevalue"
                  disabled
                  suffixIcon="arrow-down"
                ></u--input>
              </view>
            </view>
          </view>
          <view class="Task">
            结束时间:
            <view class="Task-input">
              <view class="screen-modal-item-input" @click="endshow = true">
                <u--input
                  placeholder="请选择结束时间"
                  border="surround"
                  v-model="endtimevalue"
                  disabled
                  suffixIcon="arrow-down"
                ></u--input>
              </view>
            </view>
          </view> -->
        </view>
      </u-modal>
    </view>
    <mx-date-picker
      :show="beginshow"
      :type="type"
      :value="value"
      :show-tips="true"
      :show-seconds="true"
      @confirm="confirmdate"
      @cancel="confirmdate"
    />

    <mx-date-picker
      :show="endshow"
      :type="type"
      :value="value"
      :show-tips="true"
      :show-seconds="true"
      @confirm="confirmend"
      @cancel="confirmend"
    />
  </view>
</template>

<script>
import eq from "../../component/equipment/equipment.vue";
import MxDatePicker from "@/components/mx-datepicker/mx-datepicker.vue";
export default {
  components: {
    eq,
    MxDatePicker,
  },

  data() {
    return {
      Materialdata: [],
      pageNo: 1,
      loading: true,
      show: false,
      sbtype: "21,23,50,16,19,53",
      Number: "",
      beginshow: false,
      type: "datetime",
      value: "",
      begintimevalue: "",
      endtimevalue: "",
      endshow: false,
      supplier: "",
      gysguid: "",
      gys: [],
      gysid: [],
      Specifications: [],
      orgcode: this.$store.getters.orgcode,
      gyslist: "",
      specs: "",
      specNum: "",
      maters: [],
      num: 0,
      status: 0,
    };
  },
  created() {
    this.Materiallist();
   // this.gystype();
   // this.clmtype();
  },
  onReachBottom() {
    uni.showNavigationBarLoading();
    this.pageNo++;
    if (this.num == 0) {
      this.Materiallist();
    } else if (this.num == 1) {
      this.ccmaterial();
    }
    uni.hideNavigationBarLoading();
  },
  methods: {
    Materiallist(e) {
      this.num = e;
      // this.Materialdata = []
      this.$http
        .get(`/monitor/monitor/list1`, {
          params: {
            column: "id",
            order: "desc",
            pageNo: this.pageNo,
            PageSize: 10,
            remark: this.Number,
            createTime_begin: this.begintimevalue,
            createTime_end: this.endtimevalue,
            gongyingshangdanweibianma: this.gysguid,
            cailiaono: this.specNum,
          },
        })
        .then((res) => {
          this.num = 0;
          this.jclistdata;
          this.loading = false;
          if (res.data.result.records.length == 0) {
            uni.showToast({
              title: "没有更多数据了",
              icon: "none",
            });
          }
          // this.Materialdata = res.data.result.records;
          // console.log(this.Materialdata)
          // this.Materialdata.push(...res.data.result.jc.records);
          if (this.pageNo != 1) {
            this.Materialdata = this.Materialdata.concat(res.data.result.records);
            console.log(this.Materialdata, "00000-------------");
          } else {
            this.Materialdata = res.data.result.records;
            console.log(this.Materialdata, "11111------------");
          }
        });
    },
    ccmaterial(e) {
      this.num = e;
      this.$http
        .get(`/yclcl/wzyclchuchanggb/list`, {
          params: {
            column: "id",
            order: "desc",
            pageNo: this.pageNo,
            PageSize: 10,
          },
        })
        .then((res) => {
          // console.log(res);
          this.num = 1;
          if (this.pageNo != 1) {
            this.Materialdata = this.Materialdata.concat(res.data.result.records);
            console.log(this.Materialdata, "3333333333-------------");
          } else {
            this.Materialdata = res.data.result.records;
            console.log(this.Materialdata, "22222222------------");
          }
        });
    },
    changeData(e) {
      this.pageNo = 1;
      this.Materialdata = [];
      if (e == 0) {
        this.Materiallist(e);
      } else if (e == 1) {
        this.ccmaterial(e);
      }
    },
    clickmaterial(e) {
      if (e == 0) {
        uni.navigateTo({
          url: "/pages/smartgb/Smartgb/smartgbjc",
        });
      } else if (e == 1) {
        uni.navigateTo({
          url: "/pages/smartgb/Smartgb/smartgbcc",
        });
      }
    },
    screen() {
      this.show = true;
    },
    changevalue(choosevalue) {
      this.Number = choosevalue;
    },
    confirm() {
      this.Materiallist();
      this.show = false;
      this.Number = "";
      this.begintimevalue = "";
      this.endtimevalue = "";
      this.supplier = "";
      this.specs = "";
    },
    cancel() {
      this.show = false;
      this.Number = "";
      this.begintimevalue = "";
      this.endtimevalue = "";
      this.supplier = "";
      this.specs = "";
    },
    confirmdate(e) {
      this.begintimevalue = e.value;
      this.beginshow = false;
    },
    confirmend(e) {
      this.endtimevalue = e.value;
      this.endshow = false;
    },
   
    Pickgys(e) {
      // console.log(e)
      this.index = e.detail.value;
      this.choosekey = 3;
      this.supplier = this.gys[this.index];
      this.gysguid = this.gysid[this.index];
    },
  
    PickSpecifications(e) {
      //console.log(e)
      this.index = e.detail.value;
      this.choosekey = 7;
      this.specs = this.Specifications[this.index];
      this.specNum = this.maters[this.index];
      // this.clgg = this.Carnumbers
      // console.log(this.specs, this.specNum, "jjjjjjjjjjjj");
      // this.begintime = ''
      // this.endtime = ''
    },
  },
};
</script>

<style scoped lang="scss">
.wrap {
  width: 100%;
  height: auto;
  background-color: #f2f5fe;

  &-title {
    width: 700upx;
    height: 260upx;
    // border: 1px solid forestgreen;
    margin: 20px auto;
    display: flex;
    flex-direction: row;
    justify-content: space-between;

    &-left {
      width: 320upx;
      height: 260upx;
      border-radius: 10px;
      background-color: #fff;
      display: flex;
      flex-direction: column;
      justify-content: space-around;
      align-items: center;

      &-jctj {
        // text-align: center;
        height: 30upx;
      }

      &-imag {
        img {
          width: 35px;
          height: 30px;
        }
      }

      h2 {
        text-align: center;
      }
    }
  }

  &-section {
    width: 700upx;
    height: auto;
    // border: 1px solid gold;
    margin: 30px auto;

    &-box {
      width: 700upx;
      height: auto;
      background-color: #fff;
      margin: 10px auto;
      border-radius: 10px;

      .green {
        background-image: url(../../../static/home/gr.png);
        background-repeat: no-repeat;
        background-size: 100% 40%;
      }

      .blue {
        background-image: url(../../../static/home/bl.png);
        background-repeat: no-repeat;
        background-size: 100% 40%;
      }

      &-sta {
        position: absolute;
        right: 11px;
        width: 165upx;
        height: 165upx;

        view {
          position: absolute;
          transform: rotate(0deg);
          top: 20upx;
          left: 65upx;
        }
      }

      &-name {
        padding: 5px 15px;
        color: #9299bb;
        font-size: 15px;

        span {
          color: #4c5971;
          font-size: 15px;
          font-family: "PingFang-SC-Medium";
          padding: 0 10px;
          // font-weight:bold;
        }
      }
    }
  }

  .select-box {
    width: 100%;
    height: auto;

    .Task {
      width: 100%;
      height: auto;

      .Task-input {
        width: 100%;
        height: 70upx;
        margin: 10px 0;
      }
    }
  }
}
.wrap-title-left-image {
  width: 35px;
  height: 30px;
}
</style>
