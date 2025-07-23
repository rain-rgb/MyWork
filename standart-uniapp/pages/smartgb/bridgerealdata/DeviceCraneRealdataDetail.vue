<template>
  <view id="pourdetail">
    <cu-custom bgColor="bg-white" :isBack="true">
      <block slot="content">架桥机详情</block>
    </cu-custom>
    <view class="main">
			<view class="main-item">
				<view class="biaoqian"></view>
				<view class="mainnew">主要信息</view>
			</view>
			<view class="main-item" v-for="item in columns" :key="item">
				<view class="main-item-name" v-if="item.dataIndex == `hookstatus1` ">
					{{ item.title }}：<span>
						<view class="cu-tag bg-green" v-if="loaddata[item.dataIndex] === 0">载荷正常</view>
						<view class="cu-tag bg-red" v-if="loaddata[item.dataIndex] === 1">载荷预警</view>
            </span>
				</view>
				<view class="main-item-name" v-else-if="item.dataIndex == `hookbrake1status1` ">
					{{ item.title }}：<span>
						<view class="cu-tag bg-green" v-if="loaddata[item.dataIndex] === 0">重量正常</view>
						<view class="cu-tag bg-red" v-if="loaddata[item.dataIndex] === 1">重量预警</view>
            </span>
				</view>
				<view class="main-item-name" v-else-if="item.dataIndex == `hookbrake2status1` ">
					{{ item.title }}：<span>
						<view class="cu-tag bg-green" v-if="loaddata[item.dataIndex] === 0">重量正常</view>
						<view class="cu-tag bg-red" v-if="loaddata[item.dataIndex] === 1">重量预警</view>
            </span>
				</view>
				<view class="main-item-name" v-else-if="item.dataIndex == `wireDrumstatus` ">
					{{ item.title }}：<span>
						<view class="cu-tag bg-green" v-if="loaddata[item.dataIndex] === 0">正常</view>
						<view class="cu-tag bg-red" v-if="loaddata[item.dataIndex] === 1">预警</view>
            </span>
				</view>
				<view class="main-item-name" v-else-if="item.dataIndex == `windAntiskidstatus` ">
					{{ item.title }}：<span>
						<view class="cu-tag bg-green" v-if="loaddata[item.dataIndex] === 0">正常</view>
						<view class="cu-tag bg-red" v-if="loaddata[item.dataIndex] === 1">预警</view>
            </span>
				</view>
				<view class="main-item-name" v-else-if="item.dataIndex == `hookstatus2` ">
					{{ item.title }}：<span>
						<view class="cu-tag bg-green" v-if="loaddata[item.dataIndex] === 0">载荷正常</view>
						<view class="cu-tag bg-red" v-if="loaddata[item.dataIndex] === 1">载荷预警</view>
            </span>
				</view>
				<view class="main-item-name" v-else-if="item.dataIndex == `hookbrake1status2` ">
					{{ item.title }}：<span>
						<view class="cu-tag bg-green" v-if="loaddata[item.dataIndex] === 0">重量正常</view>
						<view class="cu-tag bg-red" v-if="loaddata[item.dataIndex] === 1">重量预警</view>
            </span>
				</view>
				<view class="main-item-name" v-else-if="item.dataIndex == `hookbrake2status2` ">
					{{ item.title }}：<span>
						<view class="cu-tag bg-green" v-if="loaddata[item.dataIndex] === 0">重量正常</view>
						<view class="cu-tag bg-red" v-if="loaddata[item.dataIndex] === 1">重量预警</view>
            </span>
				</view>
				<view class="main-item-name" v-else>
					{{ item.title }}：<span>{{loaddata[item.dataIndex]}}</span>
				</view>
			</view>
			<!-- <view class="main-item">
				<view class="main-item-name">
					设备名称：<span>{{loaddata.deviceCode_dictText}}</span>
				</view>
			</view>
			<view class="main-item">
				<view class="main-item-name">
					主钩载荷状态：<span>
						<view class="cu-tag bg-green" v-if="loaddata.mainHookstatus === 0">正常</view>
						<view class="cu-tag bg-red" v-if="loaddata.mainHookstatus === 1">预警</view>
            </span>
				</view>
			</view> -->
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
      columns: [
        // {
        //   title: '#',
        //   dataIndex: '',
        //   key:'rowIndex',
        //   width:60,
        //   align:"center",
        //   customRender:function (t,r,index) {
        //     return parseInt(index)+1;
        //   }
        // },
        {
          title:'设备名称',
          align:"center",
          dataIndex: 'deviceCode_dictText'
        },
        // {
        //   title:'设备类型2桥门机3架桥机',
        //   align:"center",
        //   dataIndex: 'deviceType'
        // },
        {
          title:'大车横向行程(m)',
          align:"center",
          dataIndex: 'bigTransverseCarroute'
        },
        {
          title:'大车纵向行程(m)',
          align:"center",
          dataIndex: 'bigLongitudinalCarroute'
        },
        {
          title:'上传时间',
          align:"center",
          dataIndex: 'bridgedate',
          customRender:function (text) {
            return !text?"":(text.length>10?text:text)
          }
        },
        {
          title:'支腿垂直度',
          align:"center",
          dataIndex: 'verticalityLeg'
        },
        {
          title:'大车水平度',
          align:"center",
          dataIndex: 'carLevelness'
        },
        {
          title:'1#天车高度',
          align:"center",
          dataIndex: 'craneHeight1'
        },
        {
          title:'1#天车吊重',
          align:"center",
          dataIndex: 'craneCrane1'
        },
        {
          title:'1#天车横向行程',
          align:"center",
          dataIndex: 'transverseCarroute1'
        },
        {
          title:'1#天车纵向行程',
          align:"center",
          dataIndex: 'longitudinalCarroute1'
        },
        {
          title:'1#天车吊钩载荷状态',
          align:"center",
          dataIndex: 'hookstatus1',
          scopedSlots: { customRender: 'tags' }
        },
        {
          title:'1#天车吊钩制动器1状态',
          align:"center",
          dataIndex: 'hookbrake1status1',
          scopedSlots: { customRender: 'tags1' }
        },
        {
          title:'1#天车吊钩制动器2状态',
          align:"center",
          dataIndex: 'hookbrake2status1',
          scopedSlots: { customRender: 'tags2' }
        },
        {
          title:'1#天车左限位',
          align:"center",
          dataIndex: 'carleftlimit1'
        },
        {
          title:'1#天车右限位',
          align:"center",
          dataIndex: 'carrightlimit1'
        },
        {
          title:'1#天车前限位',
          align:"center",
          dataIndex: 'carfrontlimit1'
        },
        {
          title:'1#天车后限位',
          align:"center",
          dataIndex: 'carbacklimit1'
        },
        {
          title:'门限位',
          align:"center",
          dataIndex: 'doorLimit'
        },
        {
          title:'抗风防滑状态',
          align:"center",
          dataIndex: 'windAntiskidstatus',
          scopedSlots: { customRender: 'tags3' }
        },
        {
          title:'电缆卷筒状态',
          align:"center",
          dataIndex: 'wireDrumstatus',
          scopedSlots: { customRender: 'tags4' }
        },
        {
          title:'累计时间',
          align:"center",
          dataIndex: 'allTime'
        },
        {
          title:'循环使用次数',
          align:"center",
          dataIndex: 'allTimes'
        },
        // {
        //   title:'预留1',
        //   align:"center",
        //   dataIndex: 'reservedOne'
        // },
        // {
        //   title:'预留2',
        //   align:"center",
        //   dataIndex: 'reservedTwo'
        // },
        {
          title:'2#天车高度',
          align:"center",
          dataIndex: 'craneHeight2'
        },
        {
          title:'2#天车吊重',
          align:"center",
          dataIndex: 'craneCrane2'
        },
        {
          title:'2#天车横向行程',
          align:"center",
          dataIndex: 'transverseCarroute2'
        },
        {
          title:'2#天车纵向行程',
          align:"center",
          dataIndex: 'longitudinalCarroute2'
        },
        {
          title:'2#天车吊钩载荷状态',
          align:"center",
          dataIndex: 'hookstatus2',
          scopedSlots: { customRender: 'tags5' }
        },
        {
          title:'2#天车吊钩制动器1状态',
          align:"center",
          dataIndex: 'hookbrake1status2',
          scopedSlots: { customRender: 'tags6' }
        },
        {
          title:'2#天车吊钩制动器2状态',
          align:"center",
          dataIndex: 'hookbrake2status2',
          scopedSlots: { customRender: 'tags7' }
        },
        {
          title:'2#天车左限位',
          align:"center",
          dataIndex: 'carleftlimit2'
        },
        {
          title:'2#天车右限位',
          align:"center",
          dataIndex: 'carrightlimit2'
        },
        {
          title:'2#天车前限位',
          align:"center",
          dataIndex: 'carfrontlimit2'
        },
        {
          title:'2#天车后限位',
          align:"center",
          dataIndex: 'carbacklimit2'
        },
        // {
        //   title: '操作',
        //   dataIndex: 'action',
        //   align:"center",
        //   fixed:"right",
        //   width:147,
        //   scopedSlots: { customRender: 'action' }
        // }
      ],
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
