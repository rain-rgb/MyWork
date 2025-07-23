<template>
  <view id="pourdetail">
    <cu-custom bgColor="bg-white" :isBack="true">
      <block slot="content">龙门吊详情</block>
    </cu-custom>
    <view class="main">
			<view class="main-item">
				<view class="biaoqian"></view>
				<view class="mainnew">主要信息</view>
			</view>
			<view class="main-item">
				<view class="main-item-name">
					设备名称：<span>{{loaddata.deviceCode_dictText}}</span>
				</view>
			</view>
			<view class="main-item">
				<view class="main-item-name">
					大车行程(m)：<span>{{loaddata.bigCarroute}}</span>
				</view>
			</view>
			<view class="main-item">
				<view class="main-item-name">
					数据上传时间：<span>{{loaddata.cranedate}}</span>
				</view>
			</view>
			<view class="main-item">
				<view class="main-item-name">
					主钩高度(m)：<span>{{loaddata.mainHookheight}}</span>
				</view>
			</view>
			<view class="main-item">
				<view class="main-item-name">
					预留副钩高度(m)：<span>{{loaddata.reservedVicehookheight}}</span>
				</view>
			</view>
			<view class="main-item">
				<view class="main-item-name">
					主钩吊重(t)：<span>{{loaddata.mainHookload}}</span>
				</view>
			</view>
			<view class="main-item">
				<view class="main-item-name">
					预留副钩吊重(t)：<span>{{loaddata.reservedVicehookload}}</span>
				</view>
			</view>
			<view class="main-item">
				<view class="main-item-name">
					小车行程(m)：<span>{{loaddata.smallCarroute}}</span>
				</view>
			</view>
			<view class="main-item">
				<view class="main-item-name">
					预留小车2行程(m)：<span>{{loaddata.reservedSmallcarroute}}</span>
				</view>
			</view>
			<view class="main-item">
				<view class="main-item-name">
					风速(m/s)：<span>{{loaddata.windSpeed}}</span>
				</view>
			</view>
			<view class="main-item">
				<view class="main-item-name">
					预留模拟量：<span>{{loaddata.reservedAnalogquantityone}}</span>
				</view>
			</view>
			<view class="main-item">
				<view class="main-item-name">
					预留模拟量：<span>{{loaddata.reservedAnalogquantitytwo}}</span>
				</view>
			</view>
			<view class="main-item">
				<view class="main-item-name">
					主钩载荷状态：<span>
						<view class="cu-tag bg-green" v-if="loaddata.mainHookstatus === 0">正常</view>
						<view class="cu-tag bg-red" v-if="loaddata.mainHookstatus === 1">预警</view>
            </span>
				</view>
			</view>
			<view class="main-item">
				<view class="main-item-name">
					预留副钩载荷状态：<span>
						<view class="cu-tag bg-green" v-if="loaddata.reservedVicehookstatus === 0">正常</view>
						<view class="cu-tag bg-red" v-if="loaddata.reservedVicehookstatus === 1">预警</view>
            </span>
				</view>
			</view>
			<view class="main-item">
				<view class="main-item-name">
					风速报警：<span>
						<view class="cu-tag bg-green" v-if="loaddata.windSpeedalarm === 0">正常</view>
						<view class="cu-tag bg-red" v-if="loaddata.windSpeedalarm === 1">预警</view>
          </span>
				</view>
			</view>
			<view class="main-item">
				<view class="main-item-name">
					大车左限位：<span>{{loaddata.bigCarleftlimit}}</span>
				</view>
			</view>
			<view class="main-item">
				<view class="main-item-name">
					大车右限位：<span>{{loaddata.bigCarrightlimit}}</span>
				</view>
			</view>
			<view class="main-item">
				<view class="main-item-name">
					小车前限位：<span>{{loaddata.smallCarbacklimit}}</span>
				</view>
			</view>
			<view class="main-item">
				<view class="main-item-name">
					预留小车2前限位：<span>{{loaddata.reservedSmallcarfrontlimit}}</span>
				</view>
			</view>
			<view class="main-item">
				<view class="main-item-name">
					预留小车2后限位：<span>{{loaddata.reservedSmallcarbacklimit}}</span>
				</view>
			</view>
			<view class="main-item">
				<view class="main-item-name">
					吊钩上限位：<span>{{loaddata.hookUplimit}}</span>
				</view>
			</view>
			<view class="main-item">
				<view class="main-item-name">
					预留副吊钩上限位：<span>{{loaddata.reservedVicehookuplimit}}</span>
				</view>
			</view>
			<view class="main-item">
				<view class="main-item-name">
					门限位：<span>{{loaddata.doorLimit}}</span>
				</view>
			</view>
			<view class="main-item">
				<view class="main-item-name">
					抗风防滑状态：<span>{{loaddata.windAntiskidstatus}}</span>
				</view>
			</view>
			<view class="main-item">
				<view class="main-item-name">
					电缆卷筒状态：<span>{{loaddata.wireDrumstatus}}</span>
				</view>
			</view>
			<view class="main-item">
				<view class="main-item-name">
					小车1制动器1：<span>{{loaddata.smallCar1brake1}}</span>
				</view>
			</view>
			<view class="main-item">
				<view class="main-item-name">
					小车1制动器2：<span>{{loaddata.smallCar1brake2}}</span>
				</view>
			</view>
			<view class="main-item">
				<view class="main-item-name">
					小车2制动器1：<span>{{loaddata.smallCar2brake2}}</span>
				</view>
			</view>
			<view class="main-item">
				<view class="main-item-name">
					大车支腿偏差：<span>{{loaddata.bigCarlegdeviation}}</span>
				</view>
			</view>
			<view class="main-item">
				<view class="main-item-name">
					累计时间：<span>{{loaddata.allTime}}</span>
				</view>
			</view>
			<view class="main-item">
				<view class="main-item-name">
					循环使用次数：<span>{{loaddata.allTimes}}</span>
				</view>
			</view>
			<view class="main-item">
				<view class="main-item-name">
					支腿偏差报警：<span>
						<view class="cu-tag bg-green" v-if="loaddata.bigcarAlm === 0">正常</view>
						<view class="cu-tag bg-red" v-if="loaddata.bigcarAlm === 1">预警</view>
            </span>
				</view>
			</view>
			<view class="main-item"></view>
		</view>
    <view class="main-item"></view>
  </view>
</template>

<script>
import smartbhapi from "../../../api/smartbh.js";
import dict from "../../component/dict/dict.vue";
export default {
  components: {
    dict,
  },
  data() {
    return {
      name: "",
      code: "",
      loaddata: {},
      stelist: [],
      shshow: false,
      delshow: false,
      note: "",
      radiolist1: [
        {
          name: "同意",
          status: 1,
          disabled: false,
        },
        {
          name: "不同意",
          status: 0,
          disabled: false,
        },
      ],
      radiovalue1: 1,
      videoUrl: "",
    };
  },
  onLoad(options) {
    //console.log(options.code, "options.code")
    this.loaddata = JSON.parse(options.item);
    this.code = this.loaddata.rwdcode;
    // this.getloadlist()
    this.getstedata();
  },
  methods: {
    // 获取任务单数据
    // getloadlist() {
    // 	this.loaddata = {}
    // 	smartbhapi.pourlist({
    // 		params: {
    // 			rwdcode: this.code
    // 		}
    // 	}).then(res => {
    // 		console.log(res, "任务单列表")
    // 		this.loaddata = res.data.result.records[0]
    // 	}).catch(e => {
    // 		console.log("请求错误", e)
    // 	})
    // },
    getstedata() {
      this.$http
        .get(`/sys/systems/sysMessages/tokenlist`, {
          params: {},
        })
        .then((res) => {
          //console.log(res, "任务单proste")
          let videotoken = res.data.result;
          this.videoUrl =
            "http://47.97.173.113:9271/VideoMonitor?id=" +
            this.loaddata.id +
            "&token=" +
            videotoken;
          console.log(this.videoUrl, "this.videoUrl");
        })
        .catch((e) => {
          console.log("请求错误", e);
        });
    },
    shenhesumit() {
      this.shshow = true;
    },
    shcancel() {
      this.shshow = false;
    },
    shconfirm() {
      let params = {
        id: this.loaddata.id,
        status: this.radiovalue1,
        note: this.note,
      };
      smartbhapi.bhzrwdedit(params).then((res) => {
        if (res.data.success) {
          uni.showToast({
            title: "审核成功",
            icon: "none",
          });
          setTimeout(() => {
            this.shshow = false;
            uni.navigateTo({
              url: "/pages/smartbh/pourorder/pourManage",
            });
          }, 500);
        }
      });
    },
    radioChange(n) {
      this.radiovalue1 = n;
      //console.log('radioChange', n);
    },
    editsumit() {
      uni.navigateTo({
        url:
          "/pages/smartbh/pourorder/pourAdd?code=" +
          this.loaddata.rwdcode +
          "&titlesta=1",
      });
    },
    delsumit() {
      this.delshow = true;
    },
    delcancel() {
      this.delshow = false;
    },
    delconfirm() {
      this.$http
        .delete("/system/bhzrenwudan/delete?id=" + this.loaddata.id + "")
        .then((res) => {
          if (res.data.success) {
            uni.showToast({
              title: "删除成功",
              icon: "none",
            });
            setTimeout(() => {
              this.delshow = false;
              uni.navigateTo({
                url: "/pages/smartbh/pourorder/pourManage",
              });
            }, 500);
          }
        });
    },
    change(e) {
      console.log("e", e);
      if (e.tile == "审核" && e.status == 0) {
        this.shshow = true;
      }
      if (e.tile == "配料" && e.status == 0 && this.loaddata.jzlsts > 0) {
        uni.navigateTo({
          url: "/pages/smartsy/shigongphb/shigongphbAdd?code=" + this.loaddata.rwdcode,
        });
      }
    },
  },
};
</script>

<style lang="scss" scoped>
#pourdetail {
  width: 100%;
  // height: 136vh;
  background-color: #f3f5fe;

  .main {
    width: 690upx;
    margin: 0 auto;
    margin-top: 30upx;
    background-color: white;
    border-radius: 16upx;

    .mainnew {
      color: #333333;
      font-size: 30upx;
      font-weight: bold;
    }

    &-item {
      display: flex;
      font-size: 28upx;
      padding-top: 30upx;

      &-name {
        margin-left: 30upx;
        text-align: left;
        color: #9299a8;

        span {
          margin-right: 30upx;
          color: #4c5971;
        }
      }
    }
  }

  .biaoqian {
    width: 12upx;
    height: 34upx;
    border-radius: 6upx;
    margin: 0 30upx;
    background-color: #4a7fff;
  }

  .progress {
    width: 690upx;
    height: 736upx;
    // width: 100vw;
    // height: 100vh;
    margin: 0 auto;
    margin-top: 30upx;
    border-radius: 16upx;
    background-color: #ffffff;
    padding-top: 30upx;
    padding-left: 30upx;
  }
}
</style>
