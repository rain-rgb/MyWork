<template>
	<view class="wrap">
		<cu-custom bgColor="bg-white" :isBack="true">
			<block slot="content">压浆数据监测</block>
			<view slot="right" @click="screen">筛选</view>
		</cu-custom>
		<view class="section" v-for="(item,index) in Yjlistdata" :key="index">
			<navigator :url="'/pages/smartlc/Yjlist/Yjdetails?item='+item.syjid">
				<view class="section-text">
				<view>工程名称:<span>{{item.gcmc!==null?item.gcmc:'暂无数据'}}</span></view>
				<view>梁板类型:<span>{{item.lblx !==null?item.lblx:'暂无数据'}}</span></view>
				<view>压浆时间:<span>{{item.yjsj !==null?item.yjsj:'暂无数据'}}</span></view>
				<view>设备名称:<span>{{item.yjsbbh_dictText !==null?item.yjsbbh_dictText:'暂无数据'}}</span></view>
				<view>梁号:<span>{{item.lianghao !==null?item.lianghao:'暂无数据'}}</span>
				</view>
				<view>施工部位:<span>{{item.sgbw !==null?item.sgbw:'暂无数据'}}</span></view>
				<view>掺减水剂量:<span>{{item.cjsjl !==null?item.cjsjl:'暂无数据'}}</span></view>
				<view>张拉时间:<span>{{item.zlsj !==null?item.zlsj:'暂无数据'}}</span></view>
				<view>压浆剂:<span>{{item.yajiangji !==null?item.yajiangji:'暂无数据'}}</span></view>
				<view>孔道数:<span>{{item.kongdaoshu !==null?item.kongdaoshu:'暂无数据'}}</span></view>
				<view>压浆方向:<span>{{item.yajiangfang !==null?item.yajiangfang:'暂无数据'}}</span></view>
				<view>压浆步骤:<span>{{item.yajiangbuzh !==null?item.yajiangbuzh:'暂无数据'}}</span></view>
				<view>初始流动速度:<span>{{item.chushisudu !==null?item.chushisudu:'暂无数据'}}</span></view>
				<view>流动值:<span>{{item.liudongdu !==null?item.liudongdu:'暂无数据'}}</span></view>
				<view>值班人员:<span>{{item.memo !==null?item.memo:'暂无数据'}}</span></view>
				<view>完成状态:<span
						:style="item.status ==0?'color:green;font-weight:bold':item.status ==1?'color:red;font-weight:bold':'color:orange;font-weight:bold'">{{item.status==0?'完成':item.status==1?'未完成':'暂无数据'}}</span>
					<!-- </view> -->
				</view>
				</view>
			</navigator>
		</view>
		<view>
			
			<u-modal :show="show" showConfirmButton showCancelButton @confirm="confirm" @cancel="cancel">
				<view class="select-box">
					<view class="Task">
						设备名称:
						<view class="Task-input">
							<!-- <u--input placeholder="请选择设备名称" border="surround" v-model="Taskno" @change="change">
							</u--input> -->
							<eq :sbtype="sbtype" @choose="changevalue"></eq>
						</view>
					</view>
					<view class="Task">
						开始时间:
						<view class="Task-input">
							<!-- <view class="screen-modal-item-name">时间：</view> -->
							<view class="screen-modal-item-input" @click="dateshow = true">
								<u--input placeholder="请选择时间" border="surround" v-model="datetimevalue" disabled
									suffixIcon="arrow-down"></u--input>
							</view>
						</view>
					</view>
					<!-- <view class="screen-modal-btn">
						<u-button text="取消" type="primary" plain @click="confirm"></u-button>
						<u-button type="primary" text="确认" @click="handleOk"></u-button>
					</view> -->
					<!-- <view class="Task">
						结束时间:
						<view class="Task-input">
							<u--input placeholder="请选择结束时间" border="surround" v-model="Sgposition" @change="changesg">
							</u--input>
						</view>
					</view> -->
				</view>
			
			</u-modal>
		</view>
		<mx-date-picker :show="dateshow" :type="type" :value="value" :show-tips="true" :show-seconds="true"
			@confirm="confirmdate" @cancel="confirmdate" />
	</view>
</template>

<script>
	import eq from '../../component/equipment/equipment.vue'
	import MxDatePicker from "@/components/mx-datepicker/mx-datepicker.vue";
	export default {
		name:'Yjlist',
		components: {
			eq,
			MxDatePicker
		},
		data() {
			return {
				Yjlistdata: [],
				show: false,
				sbtype: '10',
				pageNo: 1,
				datetimevalue: '',
				dateshow: false,
				type: 'datetime',
				value: '',
				Number: ''
			}
		},
		// onLoad() {
			
		// },
		onReachBottom() {
			uni.showNavigationBarLoading()
			this.pageNo++
			this.listdata()
			uni.hideNavigationBarLoading()
		},
		created() {
			this.listdata();
		},
		methods: {
             listdata() {
				 let params = {
					 column: 'id',
					 order: 'desc',
					 pageNo: this.pageNo,
					 PageSize: 10,
					 // sys_depart_orgcode: this.orgcode,
					 yjsbbh: this.Number,
					 // pdjg: this.pdjg,
					 syrq_begin: this.begintime,
					 syrq_end: this.endtime,
				 }
             	this.$http.get(`/yajiangm/trYajiangM/list`, {params}
             	).then(res => {
             		// this.listdatavalue = res.data.result.records
             		if (res.data.result.records.length == 0) {
             			uni.showToast({
             				title: '别下拉了,没有数据啦',
             				icon: 'none'
             			})
             		}
             		if (this.pageNo != 1) {
             			this.Yjlistdata = this.Yjlistdata.concat(res.data.result.records)
             		} else {
             			this.Yjlistdata = res.data.result.records
             		}
             		// console.log(this.listdatavalue, 'vvvvvvvvv')
             	})
             },
			 changevalue(choosevalue) {
			 	this.Number = choosevalue
			 },
			 screen() {
			 	this.show = true
			 },
			 confirm() {
			 	this.listdata()
			 	this.show = false
			 	this.Number = ''
			 	this.datetimevalue = ''
			 },
			 cancel() {
			 	this.show = false
			 	this.Number = ''
			 	this.datetimevalue = ''
			 },
			 confirmdate(e) {
			 	this.datetimevalue = e.value
			 	this.dateshow = false
			 },
		}
	}
</script>

<style lang="less" scoped>
	.wrap {
		width: 100%;
		height: auto;
		background-color: #F2F5FE;
		.section {
			width: 700upx;
			height: auto;
			border-radius: 10px;
			margin: 0 auto;
			background-color: #fff;

			.section-text {
				color: #9299A8;
				font-size: 30upx;
				padding: 15px 15px;
				line-height: 55upx;
				margin: 10px 0;

				span {
					color: #4C5971;
					font-size: 30upx;
					font-family: 'PingFang-SC-Medium';
					padding: 0 15upx;
				}
			}
		}
		.select-box {
			width: 100%;
			height: auto;
		
			// border: 1px solid seagreen;
			.Task {
				width: 100%;
				height: auto;
		
				// border: 1px solid hotpink;
				.Task-input {
					width: 100%;
					height: 70upx;
					margin: 10px 0;
					// border: 1px solid forestgreen;
				}
			}
		}
	}
</style>
