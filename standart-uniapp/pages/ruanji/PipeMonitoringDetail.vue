<template>
  <view id="pipedata">
    <cu-custom bgColor="bg-white" :isBack="true">
      <block slot="content">设备监控详情</block>
    </cu-custom>
    <view class="top">
      <view class="top-name">设备监控——{{ listData.shebeino_dictText }}</view>

      <view class="video">
        <video :src="videosrc" controls></video>
      </view>
      <view class="top-content">
        <view class="top-content-left">
          <view class="num"
            >{{ GZData.curdep ? parseFloat(GZData.curdep).toFixed(2) : 0 }}m</view
          >
          <view class="pipe">
            <view class="pipe-num" :style="{ height: pipeHigh }"></view>
          </view>
          <view>深度(m)</view>
        </view>
        <view class="top-content-right">
          <view
            ><text>{{ listData.worksta }}</text></view
          >
          <view class="">{{ listData.pileno }}</view>
          <view
            >深度:
            <text>{{ GZData.curdep ? parseFloat(GZData.curdep).toFixed(2) : 0 }}</text
            >m</view
          >
          <view
            >电流: <text>{{ GZData.elml ? parseFloat(GZData.elml).toFixed(2) : 0 }}</text
            >A</view
          >
          <view
            >速度:
            <text>{{ GZData.speed ? parseFloat(GZData.speed).toFixed(2) : 0 }}</text
            >cm/min</view
          >
          <view
            >压力:
            <text>{{
              GZData.pileAveupress ? parseFloat(GZData.pileAveupress).toFixed(2) : 0
            }}</text
            >MPa
          </view>
          <view
            >夹持力:
            <text>{{
              GZData.pileAvedpress ? parseFloat(GZData.pileAvedpress).toFixed(2) : 0
            }}</text
            >MPa
          </view>
        </view>
      </view>
    </view>
    <view class="charts">
      <view class="charts-box">
        <qiun-data-charts type="gauge" :opts="dBoptsOne" :chartData="dBDataOne" />
      </view>
      <view class="charts-box">
        <qiun-data-charts type="gauge" :opts="dBoptsTwo" :chartData="dBDataTwo" />
      </view>
      <view class="charts-box">
        <qiun-data-charts type="gauge" :opts="dBoptsThree" :chartData="dBDataThree" />
      </view>
      <view class="charts-box">
        <qiun-data-charts type="gauge" :opts="dBoptsFour" :chartData="dBDataFour" />
      </view>
    </view>
    <view class="graph">
      <view class="graph-title"
        ><text class="biaoqian"></text><text>速度/深度变化曲线</text></view
      >
      <view class="graph-charts">
        <qiun-data-charts type="mix" :opts="graphOptsOne" :chartData="graphDataOne" />
      </view>
    </view>
    <view class="graph">
      <view class="graph-title"
        ><text class="biaoqian"></text><text>压桩力变化曲线</text></view
      >
      <view class="graph-charts">
        <qiun-data-charts type="line" :opts="graphOptsTwo" :chartData="graphDataTwo" />
      </view>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      listData: {},
      GZData: {},
      //仪表盘
      dBDataOne: {},
      dBDataTwo: {},
      dBDataThree: {},
      dBDataFour: {},
      dBoptsOne: {},
      dBoptsTwo: {},
      dBoptsThree: {},
      dBoptsFour: {},
      //曲线图
      graphDataOne: {},
      graphDataTwo: {},
      graphOptsOne: {
        padding: [0, 10, 15, 20],
        legend: {
          position: "top",
        },
        xAxis: {
          disableGrid: true,
        },
        yAxis: {
          gridType: "dash",
          dashLength: 2,
          data: [
            {
              position: "left",
              title: "速度(cm/min)",
            },
            {
              position: "right",
              title: "深度(m)",
              textAlign: "left",
            },
          ],
        },
        extra: {
          mix: {
            line: {
              width: 2,
            },
          },
        },
      },
      graphOptsTwo: {
        padding: [0, 10, 15, 20],
        legend: {
          position: "top",
        },
        xAxis: {
          disableGrid: true,
        },
        yAxis: {
          gridType: "dash",
          dashLength: 2,
          showTitle: true,
          data: [
            {
              title: "压桩力(Mpa)",
            },
          ],
        },
        extra: {
          line: {
            type: "curve",
            width: 2,
            activeType: "hollow",
          },
        },
      },
      videosrc: "",
    };
  },
  onLoad(options) {
    this.listData = JSON.parse(options.item);
    // console.log(this.listData)
    this.getData();
    this.getCameraId();
  },
  computed: {
    pipeHigh() {
      let high = (this.GZData.curdep / this.GZData.designdep) * 100 || 0;
      return (high > 100 ? 100 : high) + "%";
    },
  },
  methods: {
    getData() {
      this.dBoptsOne = this.getYBPOpts("cm/min", "速度", 0, 500);
      this.dBoptsTwo = this.getYBPOpts("MPs", "压力", 0, 500);
      this.dBoptsThree = this.getYBPOpts("A", "电流", 0, 500);
      this.dBoptsFour = this.getYBPOpts("MPs", "夹持力", 0, 500);
      if (this.listData.worksta != "离线") {
        let params = {
          shebeino: this.listData.shebeino,
          pileNo: this.listData.pileno,
        };
        //管桩仪表盘数据
        this.$http
          .get("/devicepipepilerealdata/devicePipepileRealdata/listGz", {
            params,
          })
          .then((res) => {
            if (res.data.success) {
              this.GZData = res.data.result || {};
              this.dBDataOne = {
                categories: [
                  {
                    value: 1,
                    color: "#22BDF7",
                  },
                ],
                series: [
                  {
                    name: "完成率",
                    data: this.GZData.speed / 500,
                  },
                ],
              };
              this.dBDataTwo = {
                categories: [
                  {
                    value: 1,
                    color: "#22BDF7",
                  },
                ],
                series: [
                  {
                    name: "完成率",
                    data: this.GZData.pileAveupress / 500,
                  },
                ],
              };
              this.dBDataThree = {
                categories: [
                  {
                    value: 1,
                    color: "#22BDF7",
                  },
                ],
                series: [
                  {
                    name: "完成率",
                    data: this.GZData.elml / 500,
                  },
                ],
              };
              this.dBDataFour = {
                categories: [
                  {
                    value: 1,
                    color: "#22BDF7",
                  },
                ],
                series: [
                  {
                    name: "完成率",
                    data: this.GZData.pileAvedpress / 500,
                  },
                ],
              };
            }
          });
        //管桩曲线图数据
        this.$http
          .get("/devicepipepilehistorydatapart/devicePipepileHistorydataPart/listqxt", {
            params,
          })
          .then((res) => {
            if (res.data.success) {
              let data = res.data.result;
              let partDepList = [];
              let partSpeedList = [];
              let partUpressList = [];
              let datatimeList = [];
              data.forEach((item) => {
                partDepList.push(item.partDep);
                partSpeedList.push(item.partSpeed);
                partUpressList.push(item.partUpress);
                datatimeList.push(item.datatime);
              });
              this.graphDataOne = {
                categories: datatimeList,
                series: [
                  {
                    name: "速度",
                    type: "line",
                    style: "curve",
                    data: partSpeedList,
                  },
                  {
                    name: "深度",
                    index: 1,
                    type: "line",
                    style: "curve",
                    data: partDepList,
                  },
                ],
              };
              this.graphDataTwo = {
                categories: datatimeList,
                series: [
                  {
                    name: "压桩力",
                    data: partUpressList,
                  },
                ],
              };
            }
          });
      } else {
        let data = {
          categories: [
            {
              value: 1,
              color: "#22BDF7",
            },
          ],
          series: [
            {
              name: "完成率",
              data: 0,
            },
          ],
        };
        this.GZData = {};
        this.dBDataOne = Object.assign({}, data);
        this.dBDataTwo = Object.assign({}, data);
        this.dBDataThree = Object.assign({}, data);
        this.dBDataFour = Object.assign({}, data);
        this.graphDataOne = {
          categories: [2, 4, 6, 8],
          series: [
            {
              name: "速度",
              type: "line",
              style: "curve",
              data: [],
            },
            {
              name: "深度",
              index: 1,
              type: "line",
              style: "curve",
              data: [],
            },
          ],
        };
        this.graphDataTwo = {
          categories: [2, 4, 6, 8],
          series: [
            {
              name: "压桩力",
              data: [],
            },
          ],
        };
      }
    },
    //获取仪表盘opts
    getYBPOpts(tname, subname, min, max) {
      let opt = {
        title: {
          name: tname,
          fontSize: 13,
          color: "#666666",
          offsetY: 30,
        },
        subtitle: {
          name: subname,
          fontSize: 14,
          color: "#666666",
          offsetY: 50,
        },
        extra: {
          gauge: {
            type: "default",
            width: 10,
            labelColor: "#22BDF7",
            startAngle: 0.75,
            endAngle: 0.25,
            startNumber: min, //开始数值
            endNumber: max, //结束数值
            splitLine: {
              fixRadius: 0,
              splitNumber: 5,
              width: 10,
              color: "#FFFFFF",
              childNumber: 5,
              childWidth: 5,
            },
            pointer: {
              width: 10,
              color: "auto",
            },
          },
        },
      };
      return opt;
    },
    // 获取摄像头设备id
    getCameraId() {
      let params = {
        remark: this.listData.shebeino,
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
  },
};
</script>

<style lang="scss" scoped>
#pipedata {
  background-color: #f3f5fe;

  .top {
    text-align: center;

    &-name {
      font-size: 40rpx;
      font-weight: bold;
      margin-top: 20rpx;
    }

    &-number {
      margin-top: 20rpx;
    }

    &-content {
      display: flex;
      //   margin: 30rpx 0;
      //   position: relative;
      width: 690rpx;
      // height: 462rpx;
      margin: 30rpx auto;
      margin-bottom: 30rpx;
      padding-bottom: 10rpx;
      border-radius: 16rpx;
      background-color: white;

      &-left {
        width: 50%;
        position: relative;

        .num {
          position: absolute;
          color: #22bdf7;
          bottom: 20%;
          right: 15%;
        }

        .pipe {
          width: 40rpx;
          height: 200rpx;
          margin: 20rpx auto 10rpx;
          background-color: #cccccc;

          &-num {
            width: 100%;
            background-color: #22bdf7;
          }
        }
      }

      &-right {
        width: 50%;
        text-align: left;
        font-weight: bold;
        margin-top: 20rpx;

        text {
          color: #22bdf7;
        }
      }
    }
    .video {
      width: 100%;
      height: 430upx;

      video {
        width: 100%;
        height: 100%;
      }
    }
  }

  .charts {
    display: flex;
    flex-wrap: wrap;
    justify-content: space-around;
    width: 690rpx;
    // height: 462rpx;
    margin: 30rpx auto;
    margin-bottom: 30rpx;
    border-radius: 16rpx;
    background-color: white;

    &-box {
      width: 340rpx;
      height: 300rpx;
    }
  }

  .graph {
    margin-top: 20rpx;
    width: 690rpx;
    // height: 462rpx;
    margin: 30rpx auto;
    margin-bottom: 30rpx;
    border-radius: 16rpx;
    background-color: white;

    &-title {
      display: flex;
      align-items: center;
      font-size: 36rpx;
      font-weight: 600;
      color: #333333;
      padding-top: 10px;
      margin-left: 20rpx;

      .biaoqian {
        width: 12rpx;
        height: 34rpx;
        border-radius: 6rpx;
        margin-right: 20rpx;
        color: #4a7fff;
        background-color: #4a7fff;
      }
    }

    &-charts {
      width: 100%;
      height: 350rpx;
    }
  }
}
</style>
