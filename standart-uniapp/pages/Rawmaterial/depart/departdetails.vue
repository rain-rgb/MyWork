<template>
  <view class="wrap">
    <cu-custom bgColor="bg-white" :isBack="true">
      <block slot="content">发车单详情</block>
    </cu-custom>
    <view class="">
      <view class="Map">
        <view class="map_box">
          <!-- <map
            v-if="polyline[0].points.length == 0 && !show"
            id="map"
            style="width: 100%; height: 100%"
				:markers="markers"
				:polyline="polyline"
				:include-points="polyline[0].points"
				:latitude="polyline[0].points[0].latitude"
				:longitude="polyline[0].points[0].longitude"
            scale="8"
          >
          </map> -->
			<map
				v-if="polyline[0].points.length > 0 && !show"
				id="map"
				:markers="markers"
				:polyline="polyline"
				:include-points="polyline[0].points"
				:latitude="polyline[0].points[0].latitude"
				:longitude="polyline[0].points[0].longitude"
            style="width: 100%; height: 100%"
            scale="18"
			/>
        </view>
        <view class="article">
          <view class="process">
            <u-steps direction="column">
              <view class="" v-for="(item, index) in lclistdata" :key="index">
                <u-steps-item :title="item.title">
                  <view
                    style="
                      height: 50upx;
                      font-size: 26upx;
                      color: #9299a8;
                      padding: 10upx 20upx;
                    "
                    slot="desc"
                  >
                    {{ item.content }}
                  </view>
                  <u-icon :name="item.img" slot="icon" size="22"></u-icon>
                </u-steps-item>
              </view>
            </u-steps>
          </view>
          <view class="footer">
            <u-button
              v-if="listdata.isjiesuo == 0"
              @click="determine"
              style="
                width: 200px;
                border-radius: 10px;
                color: #fff;
                background-color: #387ffd;
                position: absolute;
                bottom: 21px;
                left: 88px;
              "
              text="确认到达"
            >
            </u-button>
            <u-button
              v-if="listdata.isjiesuo == 1 && listdata.sfhg == null"
              @click="check"
              style="
                width: 200px;
                border-radius: 10px;
                color: #fff;
                background-color: #f08322;
                position: absolute;
                bottom: 21px;
                left: 88px;
              "
              text="审核"
            >
            </u-button>
            <u-button
              v-if="listdata.isjiesuo == 1 && listdata.sfhg == '合格'"
              @click="Unlock"
              style="
                width: 200px;
                border-radius: 10px;
                color: #fff;
                background-color: #00d072;
                position: absolute;
                bottom: 21px;
                left: 88px;
              "
              text="解锁"
            >
            </u-button>
          </view>
        </view>
      </view>
    </view>
    <view class="bt-st">
      <u-modal
        class="u-popup__content1"
        :show="showqr"
        :title="titles"
        style="position: fixed; background-color: #ffffff"
        showCancelButton
        @cancel="cancelqr"
        @confirm="confirmqr"
      >
        <view style="padding: 30upx 10upx; font-size: 28rpx"> 确认到达地点： </view>
        <u-radio-group v-model="values" placement="row">
          <u-radio
            :customStyle="{ marginLeft: '15px' }"
            v-for="(item, index) in radiolist2"
            :key="index"
            :label="item.name"
            :name="item.status"
            @change="radioChanges"
          >
          </u-radio>
        </u-radio-group>
      </u-modal>
      <u-modal
        class="u-popup__content1"
        :show="showjs"
        :title="titlejs"
        style="position: fixed; background-color: #ffffff"
        showCancelButton
        @cancel="canceljs"
        @confirm="confirmjs"
      >
      </u-modal>
      <u-modal
        :show="show"
        style="position: fixed; background-color: #ffffff"
        showCancelButton
        @cancel="cancelsh"
        @confirm="confirmsh"
      >
        <view class="shen-box">
          <view class="section-text">
            <view class="title"> 详情 </view>
            <view class="">
              到达时间:<span>{{
                listdata.ddtime !== null ? listdata.ddtime : "暂无数据"
              }}</span>
            </view>
            <view class=""
              >出发时间:<span>{{
                listdata.cftime !== null ? listdata.cftime : "暂无数据"
              }}</span>
            </view>
            <view class=""
              >车牌号:<span>{{ listdata.cph !== null ? listdata.cph : "暂无数据" }}</span>
            </view>
            <view class=""
              >供货单位:<span>{{
                listdata.ghdw !== null ? listdata.ghdw : "暂无数据"
              }}</span></view
            >
            <view class=""
              >材料:<span>{{
                listdata.cailiao !== null ? listdata.cailiao : "暂无数据"
              }}</span>
            </view>
            <view class=""
              >规格:<span>{{
                listdata.guige !== null ? listdata.guige : "暂无数据"
              }}</span>
            </view>
            <view class=""
              >负责人:<span>{{ listdata.fzr !== null ? listdata.fzr : "暂无数据" }}</span>
            </view>
            <view class=""
              >发车数量:<span>{{
                listdata.jcsl !== null ? listdata.jcsl : "暂无数据"
              }}</span>
            </view>
            <view class="section-img">
              <view class="section-img-item" v-if="detailList1.length > 0">
                <image
                  v-for="(img, i) in detailList1"
                  :key="i"
                  @click="ViewImage1(i)"
                  style="width: 100px; height: 100px"
                  mode="aspectFill"
                  :src="img"
                >
                </image>
              </view>
              <view class="section-img-item" v-if="detailList2.length > 0">
                <image
                  v-for="(img, i) in detailList2"
                  :key="i"
                  @click="ViewImage2(i)"
                  style="width: 100px; height: 100px"
                  mode="aspectFill"
                  :src="img"
                >
                </image>
              </view>
            </view>
          </view>
          <view class="title"> 审核 </view>
          <view class="shen-top">
            <view> 审批结果： </view>
            <view>
              <u-radio-group v-model="value" placement="row">
                <u-radio
                  :customStyle="{ marginLeft: '15px' }"
                  v-for="(item, index) in radiolist1"
                  :key="index"
                  :label="item.name"
                  :name="item.status"
                  @change="radioChange"
                >
                </u-radio>
              </u-radio-group>
            </view>
          </view>
          <view class="wrap-nav" v-show="!value">
            <view class="text"> 拒收原因: </view>
            <view class="screen-picker">
              <view class="text-input">
                <u--input
                  placeholder="请输入拒收原因"
                  border="surround"
                  v-model="reason"
                  style="background-color: #f6f9fc"
                >
                </u--input>
              </view>
            </view>
          </view>
          <view class="wrap-img">
            <view class="upimg">
              <my-image-upload
                :uploadFilenames="samPicss"
                @input="ChooseImages"
                label="现场照片："
              >
              </my-image-upload>
            </view>
          </view>
        </view>
      </u-modal>
      <u-toast ref="uToast"></u-toast>
    </view>
  </view>
</template>

<script>
import amapFile from "../../../common/util/amap-wx.js";
import rawmaterialapi from "../../../api/rawmaterial.js";
import nowtime from "../../../common/util/nowtime.js";
export default {
  data() {
    return {
		transportsta:0,
      lclistdata: [],
      showqr: false,
      showjs: false,
      show: false,
      titles: "请确认是否到达",
      titlejs: "请确认是否解锁",
      titlesh: "请选择审核",
      titles: "确认到达",
      titlesh: "审核",
      img: "",
      mapKey: "782faea4097f5857c7f714c1850190a1",
      title: "map",
      latitude: 39.909,
      longitude: 116.39742,
    //   polyline: [],
	  //路线信息
      polyline: [
        {
          width: 8,
          points: [],
          arrowLine: true,
          color: "#3591FC",
        },
      ],
      markers: [],
      startlng: "", //起点经度
      startlat: "", //起点纬度
      endlng: "", //终点经度
      endlat: "", //终点纬度
      teid: "", //锁号
      time_begin: "", //开始时间
      time_end: "", //结束时间
      ListData: [],
      points: [],
      listdata: [],
      radiolist1: [
        {
          name: "合格",
          status: 1,
          disabled: false,
        },
        {
          name: "不合格",
          status: 0,
          disabled: false,
        },
      ],
      value: 1,
      radiolist2: [
        {
          name: "中转站",
          status: 1,
          disabled: false,
        },
        {
          name: "目的地",
          status: 0,
          disabled: false,
        },
      ],
      values: 0,
      valuesname: "",
      times: nowtime.date() + " " + nowtime.time(),
      samPicss: [],
      fileLists: "",
      reason: "",
      detailList1: [],
      detailList2: [],      //标记点(即移动标记物)
      markers: [
        {
          id: 1,
          width: 40,
          height: 40,
          latitude: 0,
          longitude: 0,
          iconPath: '../../../static/shiti/jt.png',
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
	  timer: null,
    };
  },
  onLoad(options) {
    // console.log(JSON.parse(options.item));
    this.listdata = JSON.parse(options.item);

    this.teid = this.listdata.sbbh;

	let currentDate = new Date(); 
   
    // this.time_end = this.listdata.ddtime;
    console.log(this.time_begin, "this.time_begin", this.time_end);
	this.shuaxinData(this.listdata.ddtime);
    // this.historyList();
  },
  onUnload() {  
    // 在页面卸载时清除定时器  
    if (this.timer) {  
      clearInterval(this.timer);  
      this.timer = null;  
    }  
  } ,
  created() {
    this.lclist();
  },
  methods: {
    lclist() {
      this.lclistdata = [];
      this.$http
        .get(`/wbshebeidetail/wbshebeiDetail/jdlist`, {
          params: {
            danhao: this.listdata.danhao,
          },
        })
        .then((res) => {
          console.log(res.data.result, "流程");
          let transport = res.data.result;
          if (transport.length > 0) {
            for (let y = 0; y < transport.length; y++) {
              if (transport[y].title == "发车申报") {
                if (transport[y].status == 0) {
                  this.lclistdata.push({
                    title: transport[y].title,
                    content: "",
                    img: "../../../static/shiti/o1.png",
                    status: transport[y].status,
                  });
                } else {
                  this.lclistdata.push({
                    title: transport[y].title,
                    content: transport[y].content,
                    img: "../../../static/shiti/o.png",
                    status: transport[y].status,
                  });
                }
              } else if (transport[y].title == "运输中") {
                if (transport[y].status == 0) {
                  this.lclistdata.push({
                    title: transport[y].title,
                    content: "",
                    img: "../../../static/shiti/t1.png",
                    status: transport[y].status,
                  });
                } else {
                  this.lclistdata.push({
                    title: transport[y].title,
                    content: transport[y].content,
                    img: "../../../static/shiti/t.png",
                    status: transport[y].status,
                  });
				  
                }
              } else if (transport[y].title == "到达") {
                if (transport[y].status == 0) {
                  this.lclistdata.push({
                    title: transport[y].title,
                    content: "",
                    img: "../../../static/shiti/h1.png",
                    status: transport[y].status,
                  });
				  this.time_begin = this.listdata.cftime;
				  this.time_end = this.formatDate(new Date());
				  this.historyList();
                } else {
					this.transportsta = 1
                  this.lclistdata.push({
                    title: transport[y].title,
                    content: transport[y].content,
                    img: "../../../static/shiti/h.png",
                    status: transport[y].status,
                  });
				  this.time_begin = this.listdata.cftime;
    			  this.time_end = this.listdata.ddtime;
				  this.historyList();
                }
              } else if (transport[y].title == "审核") {
                if (transport[y].status == 0) {
                  this.lclistdata.push({
                    title: transport[y].title,
                    content: "",
                    img: "../../../static/shiti/f1.png",
                    status: transport[y].status,
                  });
                } else {
                  this.lclistdata.push({
                    title: transport[y].title,
                    content: transport[y].content,
                    img: "../../../static/shiti/f.png",
                    status: transport[y].status,
                  });
                }
              } else if (transport[y].title == "解锁") {
                if (transport[y].status == 0) {
                  this.lclistdata.push({
                    title: transport[y].title,
                    content: "",
                    img: "../../../static/shiti/v1.png",
                    status: transport[y].status,
                  });
                } else {
                  this.lclistdata.push({
                    title: transport[y].title,
                    content: transport[y].content,
                    img: "../../../static/shiti/v.png",
                    status: transport[y].status,
                  });
                }
              } else {
                if (transport[y].status == 0) {
                  this.lclistdata.push({
                    title: transport[y].title,
                    content: "",
                    img: "../../../static/shiti/f1.png",
                    status: transport[y].status,
                  });
                } else {
                  this.lclistdata.push({
                    title: transport[y].title,
                    content: transport[y].content,
                    img: "../../../static/shiti/f.png",
                    status: transport[y].status,
                  });
                }
              }
            }
          }
        });
      this.listdata.jyimgfile ? this.detailList1.push(this.listdata.jyimgfile) : [];
      this.listdata.imgfile ? this.detailList2.push(this.listdata.imgfile) : [];
      console.log(this.detailList2, this.listdata.imgfile, "this.detailList2 ");
    },
	shuaxinData(date){
		if(!Boolean(date)){//到达时间没有值时实时刷新数据
			this.timer = setInterval(() => {
    			this.historyList();// 地图轨迹
			}, 1000*60);
		}else{
			this.historyList();// 地图轨迹
		}
	},
    // 地图轨迹
    historyList() {
      this.ListData = null;
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
      // let param = {
      // 	shebeiNo: params.shebeiNo,
      // 	uploadtime: this.begintime,
      // 	projectId: this.endtime,
      // };
      this.$http
        .get(`/suchingcarpeiz/suchingCarpeiz/listcl`, {
          // this.$http.get(`/wbshebeihistory/wbshebeiHistory/list1`, {
          params: {
            pageSize: 2000,
            pageNum: 1,
            sn: this.listdata.sbbh,
            // sn: 867192069426432,//this.listdata.sbbh
            teid: this.teid,
            start_time: this.time_begin,
            end_time: this.time_end,
          },
        })
        .then((res) => {
          if (res.data.result.length > 0) {
            console.log(res, "轨迹");
            this.ListData = res.data.result;
            // this.longitude = this.ListData[0].lon;
            // this.latitude = this.ListData[0].lat;
            // this.startlng = this.ListData[0].lon;
            // this.startlat = this.ListData[0].lat;
            // this.endlng = this.ListData[res.data.result.length - 1].lon;
            // this.endlat = this.ListData[res.data.result.length - 1].lat;
            for (var j = 0; j < this.ListData.length; j++) {
            //   this.points.push({
            //     longitude: this.ListData[j].lon,
            //     latitude: this.ListData[j].lat,
            //   });
			  this.polyline[0].points.push({
					longitude: this.ListData[j].lon,
					latitude: this.ListData[j].lat,
				});
            }
            this.loading = false;
            if (res.data.result.length > 0) {
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
            console.log(this.startlng, this.startlat, this.endlng, this.endlat);
          } else {
            this.$refs.uToast.show({
              type: "error",
              title: "暂无历史信息",
            });
          }
        });
      // this.guiji();
    },
    //设置地图
    initMapData() {
		 this.initMarkers();
	   const mapCtx = uni.createMapContext('map');  
 		// mapCtx.setZoomLevel(14); // 缩放级别，取值范围为3-20 
      // this.mapContext = uni.createMapContext("myMap", this);
    },
    //设置位置（最新定位）
    initMarkers() {
		console.log(this.polyline,'this.polyline');
      this.markers[0].latitude = this.polyline[0].points[this.polyline[0].points.length-1].latitude;
      this.markers[0].longitude = this.polyline[0].points[this.polyline[0].points.length-1].longitude;
    //   this.markers[0].latitude = this.polyline[0].points[0].latitude;
    //   this.markers[0].longitude = this.polyline[0].points[0].longitude;
    },
    guiji() {
      var myAmapFun = new amapFile.AMapWX({
        key: this.mapKey,
      });
      var points = [];
      uni.showLoading({
        title: "加载中",
      });
      this.$http
        .get(`/suchingcarpeiz/suchingCarpeiz/listcl`, {
          // this.$http.get(`/wbshebeihistory/wbshebeiHistory/list1`, {
          params: {
            pageSize: 10,
            pageNum: 1,
            sn: 867192069426432,
            teid: this.teid,
            start_time: this.time_begin,
            end_time: this.time_end,
          },
        })
        .then((res) => {
          uni.hideLoading();
          let listdata = res.data.result;
          for (var j = 0; j < listdata.length; j++) {
            this.points.push({
              longitude: listdata[j].lon,
              latitude: listdata[j].lat,
            });
          }
          console.log(this.points);
          console.log("历史轨迹数据");
          var that = this;
          setTimeout(function () {
            that.setData({
              polyline: [
                {
                  points: that.points,
                  color: "#0091ff",
                  width: 15,
                },
              ],
            });
            that.mapmarkers();
          }, 500);
        });
    },
    mapmarkers() {
      var that = this;
      this.markers = [
        {
          id: 1001,
          longitude: that.startlng,
          latitude: that.startlat,
          iconPath: "../../../static/icon/qidian.png",
          width: "30",
          height: "35",
        },
        {
          id: 1003,
          longitude: that.endlng,
          latitude: that.endlat,
          iconPath: "../../../static/icon/zhongdian.png",
          width: "30",
          height: "35",
        },
      ];
    },
    setData: function (obj) {
      let that = this;
      let keys = [];
      let val, data;
      Object.keys(obj).forEach(function (key) {
        keys = key.split(".");
        val = obj[key];
        data = that.$data;
        keys.forEach(function (key2, index) {
          if (index + 1 == keys.length) {
            that.$set(data, key2, val);
          } else {
            if (!data[key2]) {
              that.$set(data, key2, {});
            }
          }
          data = data[key2];
        });
      });
    },
    cancelqr() {
      this.showqr = false;
    },
    confirmqr() {
      let params = {};
      if (this.values == 0) {
        params = {
          ddtime: this.times,
          isjiesuo: 1,
          zhuangtai: 1,
          id: this.listdata.id,
        };
      } else {
        params = {
          id: this.listdata.id,
          zzstatus: 1,
        };
      }
      rawmaterialapi.elockEdit(params).then((res) => {
        if (res.data.success) {
          uni.showToast({
            title: "确认成功",
            icon: "none",
          });
          setTimeout(() => {
            this.showqr = false;
            uni.navigateTo({
              url: "/pages/Rawmaterial/depart/departlist",
            });
          }, 500);
        }
      });
    },
    canceljs() {
      this.showjs = false;
    },
    confirmjs() {
      rawmaterialapi
        .iselock({
          params: {
            teid: this.teid,
            id: this.listdata.id,
          },
        })
        .then((res) => {
          if (res.data.success) {
            uni.showToast({
              title: "解锁成功",
              icon: "none",
            });
            setTimeout(() => {
              this.showjs = false;
              uni.navigateTo({
                url: "/pages/Rawmaterial/depart/departlist",
              });
            }, 500);
          }
        });
    },
    cancelsh() {
      this.show = false;
    },
    confirmsh() {
      if (this.value == 0) {
        this.valuesname = "不合格";
      } else {
        this.valuesname = "合格";
      }
      let params = {
        reviewtime: this.times,
        id: this.listdata.id,
        reviewer: this.$store.getters.username,
        sfhg: this.valuesname,
        uppic: this.fileLists,
        rejection: this.reason,
      };
      rawmaterialapi.elockEdit(params).then((res) => {
        if (res.data.success) {
          uni.showToast({
            title: "审核成功",
            icon: "none",
          });
          setTimeout(() => {
            this.showqr = false;
            uni.navigateTo({
              url: "/pages/Rawmaterial/depart/departlist",
            });
          }, 500);
        }
      });
    },
    //确认到达
    determine() {
      this.showqr = true;
    },
    //解锁
    Unlock() {
      this.showjs = true;
    },
    //审核
    check() {
      // this.show = true;
      if (this.listdata.upsbbh) {
        this.UnlockInfo();
      } else {
        this.show = true;
      }
    },
    radioChange(n) {
      this.value = n;
      this.reason = "";
      console.log("radioChange", n);
    },
    radioChanges(n) {
      this.values = n;
      // console.log('radioChange', n);
    },
    //解锁接口
    UnlockInfo() {
      this.$http
        .get(`/sys/systems/sysMessages/jfElocksUp`, {
          params: {
            teid: this.listdata.upsbbh,
            id: this.listdata.id,
          },
        })
        .then((res) => {
          this.show = true;
        });
    },
    ChooseImages(uploadFilenames) {
      //console.log(uploadFilenames)
      this.fileLists = uploadFilenames.toString();
    },
    //查看图片
    ViewImage1(i) {
      uni.previewImage({
        urls: this.detailList1,
        current: i,
      });
    },
    ViewImage2(i) {
      uni.previewImage({
        urls: this.detailList2,
        current: i,
      });
    },
	formatDate(date) {
		var year = date.getFullYear();  
		
		var month = (1 + date.getMonth()).toString();  
		month = month.padStart(2, '0');  
		
		var day = date.getDate().toString();  
		day = day.padStart(2, '0');  
		
		var hours = date.getHours().toString();  
		hours = hours.padStart(2, '0');  
		
		var minutes = date.getMinutes().toString();  
		minutes = minutes.padStart(2, '0');  
		
		var seconds = date.getSeconds().toString();  
		seconds = seconds.padStart(2, '0');  
		
		return year + '-' + month + '-' + day + ' ' + hours + ':' + minutes + ':' + seconds;  
	} 
  },
};
</script>

<style lang="scss" scoped>
.wrap {
  width: 100%;
  height: auto;
  background-color: #f2f5fe;

  .Map {
    width: 100%;
    // height: 675px;
    position: relative;

    .map_box {
      height: 300px;
      // border: 1px solid #91CB74;
      width: 100%;
      // position: absolute;
      // top: 10rpx;
      // bottom: 0rpx;
      // left: 0rpx;
      // right: 0rpx;
    }

    .article {
      width: 100%;
      height: 780upx;
      // border: 1px solid #ED1C24;
      // position: absolute;
      // top: 160px;

      .process {
        width: 690upx;
        height: 600upx;
        margin: 0 auto;
        // margin-top: 30upx;
        border-radius: 16upx;
        background-color: #ffffff;
        border-radius: 10px;
        // border: 1px solid #5555ff;
        padding-top: 30upx;
        padding-left: 30upx;
        // position: absolute;
        // top: 450upx;
        // left: 30upx;
        // z-index:0;
        box-shadow: 0px 5px 5px #f2f3f3;
      }

      .footer {
        width: 750upx;
        height: 170upx;
        // border: 1px solid #F1A532;
        background: #ffffff;

        // margin-bottom: -60px;
        // position: absolute;
        // top:440px;
        border-radius: 20px 20px 0px 0px;
      }
    }
  }
  .u-popup__content1/deep/.u-popup__content {
    top: 120px !important;
    // background-color: #7e3c3c !important;
  }
  .bt-st/deep/.u-popup__content {
    background-color: #fff;
    position: relative;
    top: 25px;
    .shen-box {
      width: 550upx;
      display: flex;
      flex-direction: column;
      justify-content: center;
      align-items: center;
      // background-color: #c43d3d;
      .shen-top {
        width: 480upx;
        display: flex;
        // justify-content: center;
        align-items: center;
        // background-color: #c27171;
      }
      .title {
        font-size: 31upx;
        font-weight: bold;
        color: #606266;
        text-align: center;
        margin: 20upx 0;
      }
      .section-text {
        width: 480upx;
        color: #9299a8;
        font-size: 30upx;
        padding: 15px 30px;
        line-height: 45upx;
        margin: 10px 20px;
        border: 1px solid #3481ca;
        border-radius: 20upx;
        box-sizing: content-box;
        // background-color: #a16666;
        span {
          color: #4c5971;
          font-size: 25upx;
          font-family: "PingFang-SC-Medium";
          padding: 0 15upx;
        }
        .section-img {
          display: flex;
          padding: 0 0upx;
          .section-img-item {
            margin-right: 20upx;
          }
        }
      }
    }
    .wrap-img {
      width: 480upx;
      height: 100px;
      margin: 10px auto;
      display: flex;
      justify-content: center;
      align-items: center;
      // background-color: #a05f5f;
      .upimg {
        // display: flex;
        width: 480upx;
        height: 110px;
        // background-color: #a05f5f;
        .uptext {
          width: 200px;
          height: 20px;
          font-size: 15px;
          font-family: "PingFang SC";
          font-weight: bold;
          color: #4c5971;
        }
      }
    }
    .wrap-nav {
      width: 480upx;
      height: 40px;
      margin: 30upx 0 0 0;
      padding: 29px 0 0 0;
      display: flex;
      flex-direction: row;
      justify-content: space-around;
      align-items: center;
      // background-color: #794040;
      .screen-modal-item-name {
        flex: 3;
      }

      .screen-picker {
        flex: 8;
      }

      .text {
        font-size: 13px;
        font-family: PingFang SC;
        font-weight: 500;
        color: #333333;
        flex: 3;
      }
    }
  }
}
</style>
