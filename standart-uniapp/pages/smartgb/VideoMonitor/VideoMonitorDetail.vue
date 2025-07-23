<template>
  <view id="pourdetail">
    <!-- <cu-custom bgColor="bg-white" :isBack="true" v-if="!isLandscape" :backRouterName="`/pages/smartgb/VideoMonitor/VideoMonitorlist`">
      <block slot="content">视频监控详情</block>
    </cu-custom> -->
    <myNav bgColor="bg-white" :isBack="true" v-if="!isLandscape">
      <block slot="content">视频监控详情</block>
    </myNav>
    <!-- <view class="main">
			<view class="main-item">
				<view class="biaoqian"></view>
				<view class="mainnew">主要信息</view>
			</view>
			<view class="main-item">
				<view class="main-item-name">
					开盘日期：<span>{{loaddata.begtim}}</span>
				</view>
			</view>
			<view class="main-item">
				<view class="main-item-name">
					任务编号：<span>{{loaddata.rwdcode}}</span>
				</view>
			</view>
			<view class="main-item">
				<view class="main-item-name">
					工程名称：<span>{{loaddata.projname}}</span>
				</view>
			</view>
			<view class="main-item">
				<view class="main-item-name">
					坍落度(mm)/扩展度(mm)：<span>{{loaddata.lands}}</span>
				</view>
			</view>
			<view class="main-item">
				<view class="main-item-name">
					强度等级：<span>{{loaddata.betlev}}</span>
				</view>
			</view>
			<view class="main-item">
				<view class="main-item-name">
					浇筑方式：<span>{{loaddata.pour}}</span>
				</view>
			</view>
			<view class="main-item">
				<view class="main-item-name">
					任务方量：<span>{{loaddata.mete}}</span>
				</view>
			</view>
			<view class="main-item">
				<view class="main-item-name">
					浇筑部位：<span>{{loaddata.conspos}}</span>
				</view>
			</view>
			<view class="main-item"></view>
		</view> -->
    <view
      class="progress"
      v-if="!isLandscape && !isRotate"
      :class="['rotate-box', isRotate ? 'rotate' : '']"
    >
      <iframe style="width: 100%; height: 100%" :src="videoUrl" frameborder="0"></iframe>
    </view>
    <view class="progress1" v-if="isRotate && !isLandscape" :class="['rotate-box', isRotate ? 'rotate' : '']">
      <!-- {{ isRotate }}isRotate -->
      <iframe style="width: 100%; height: 100%" :src="videoUrl" frameborder="0"></iframe>
    </view>
    <view class="progress2" v-if="isLandscape">
      <!-- {{ isLandscape }}isLandscape -->
      <iframe style="width: 100%; height: 100%" :src="videoUrl" frameborder="0"></iframe>
    </view>
    <view class="screen-modal-btn" :class="[ isRotate ? 'screen-modal-btn1' : 'screen-modal-btn']">
      <u-button text="旋转" @click="toggleRotate" type="primary" style="margin-bottom: 20upx;" v-if="!isLandscape"></u-button>
      <u-button text="刷新" @click="Refresh" type="primary"></u-button>
    </view>
  </view>
</template>

<script>
import smartbhapi from "../../../api/smartbh.js";
import dict from "../../component/dict/dict.vue";
// import myNav from "./component/my-componets/my-nav.vue";
export default {
  components: {
    dict,
    // myNav,
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
      isLandscape: false,
      isRotate: false, // 控制旋转状态
      noDelete:false,
    };
  },
  onShow() {
    // 窗口尺寸变化监听
    this.windowResizeCallback = (res) => {
      // this.debounce(this.changeVideo(res), 500);
      this.isLandscape = res.size.windowWidth > res.size.windowHeight;
      setTimeout(() => {
        this.getstedata();
      }, 500);
      console.log("方向变化:", this.isLandscape ? "横屏" : "竖屏");
    };
    uni.onWindowResize(this.windowResizeCallback);
    
    this.isRotate = uni.getStorageSync('Rotate_key');
    console.log(this.isRotate,'this.isRotate--------------------------------------------33333333333333333333');
  },
  onHide() {
    console.log('22222222222222222');
    // 移除监听
    uni.offWindowResize(this.windowResizeCallback);
  },
  // 页面卸载时自动清除（如页面关闭/跳转）
  onUnload() {
    if (!this.noDelete) {//刷新时不需要删除缓存
      uni.removeStorageSync('Rotate_key');  // ‌:ml-citation{ref="3,7" data="citationList"}
    }
    console.log('22222222222222222');
  },
  onLoad(options) {
    //console.log(options.code, "options.code")
    this.loaddata = JSON.parse(options.item);
    this.code = this.loaddata.rwdcode;
    // this.getloadlist()
    // this.$nextTick(() => {
    //   this.getstedata();
    // });
    this.getstedata();
    // setTimeout(()=>{
    //   this.getstedata();
    // },500)
  },
  methods: {
    toggleRotate() {
      this.isRotate = !this.isRotate; // 切换状态
      uni.setStorageSync('Rotate_key', this.isRotate);
      uni.redirectTo({
        url:
          "/pages/smartgb/VideoMonitor/VideoMonitorDetail?item=" +
          JSON.stringify(this.loaddata),
      });
      this.noDelete = true;
    },
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
    Refresh() {
      this.getstedata();
      uni.redirectTo({
        url:
          "/pages/smartgb/VideoMonitor/VideoMonitorDetail?item=" +
          JSON.stringify(this.loaddata),
      });
    },
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

  },
};
</script>

<style lang="scss" scoped>
#pourdetail {
  width: 100%;
  // height: 136vh;
  background-color: #f3f5fe;
  position: relative;

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
    // padding-top: 30upx;
    // padding-left: 30upx;
  }
  .progress1 {
    // width: 690upx;
    // height: 736upx;
    width: 100vh;
    height: 100vw;
    // margin: 0 auto;
    // margin-top: 30upx;
    border-radius: 16upx;
    background-color: #ffffff;
    position: relative;
    top: 438upx;
    left: -58.2%;
    // padding-top: 30upx;
    // padding-left: 30upx;
  }
  .progress2 {
    // width: 690upx;
    // height: 736upx;
    width: 100vw;
    height: 100vh;
    // margin: 0 auto;
    // margin-top: 30upx;
    border-radius: 16upx;
    background-color: #ffffff;
    // padding-top: 30upx;
    // padding-left: 30upx;
  }
}
.screen-modal-btn {
  margin-top: 30upx;
}

.screen-modal-btn1 {
  margin-top: 30upx;
  position: relative;
  top: 858upx;
    // left: -58.2%;
}

.rotate-box {
  width: 690upx;
  height: 736upx;
  transition: transform 0.3s ease; /* 过渡动画 */
}

.rotate {
  width: 690upx;
  height: 736upx;
  transform: rotate(90deg); /* 旋转90度 */
}
</style>
