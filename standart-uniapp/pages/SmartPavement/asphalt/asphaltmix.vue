<template>
	<view class="wrap">
		<cu-custom bgColor="bg-white" :isBack="true">
			<block slot="content">沥青拌合</block>
			<view slot="right" @click="screen">筛选</view>
		</cu-custom>
		<view>
			<u-loading-page :loading="loading" loading-text="加载中请稍后..."></u-loading-page>
			<view class="pourlist">
				<view class="pourlist-item" v-for="(item,index) in listdata" :key="index">
					<view class="pourlist-item-sta">
						<image v-if="item.chaobiaodengji == 0" src="../../../static/pour/hege.png" mode=""></image>
						<image v-if="item.chaobiaodengji == 1" src="../../../static/pour/lv1.png" mode=""></image>
						<image v-if="item.chaobiaodengji == 2" src="../../../static/pour/lv2.png" mode=""></image>
						<image v-if="item.chaobiaodengji == 3" src="../../../static/pour/lv3.png" mode=""></image>
						<!-- <view :style="overLevel == 0 ? 'color:#F32F45;':'color:#52C57C;'">
							{{ item.overLevel == 0 ? '合格':'已审核' }}
						</view> -->
					</view>
					<view v-if="item.chaobiaodengji != 0&&item.chaobiaodengji!=null" class="pourlist-item-sta1">
						<image v-if="item.overproofStatus == 0"
							src="../../../static/pour/weichuzhi.png" mode="">
						</image>
						<image v-if="item.overproofStatus == 10"
							src="../../../static/pour/yichuzhi.png" mode="">
						</image>
						<image v-if="item.overproofStatus == 20"
							src="../../../static/pour/yishenhe.png" mode="">
						</image>
						<image v-if="item.overproofStatus == 30"
							src="../../../static/pour/bohui.png" mode=""></image>
					</view>
					<navigator :url="'/pages/SmartPavement/asphalt/asphaltmixdetails?item='+JSON.stringify(item)">
						<view class="section-text">
							<view>设备名称:<span>{{item.shebeibianhao_dictText !==null?item.shebeibianhao_dictText:'暂无数据'}}</span></view>
							<view>工程名称:<span>{{item.projectName !==null?item.projectName:'暂无数据'}}</span></view>
							<view>出料时间:<span>{{item.chuliaoshijian !==null?item.chuliaoshijian:'暂无数据'}}</span></view>
							<view>油石比:<span>{{item.youshibi !==null?item.youshibi:'暂无数据'}}</span></view>
							<view>理论油石比:<span>{{item.llysb !==null?item.llysb:'暂无数据'}}</span></view>
							<view>出料温度(℃):<span>{{item.chuliaowd !==null?item.chuliaowd:'暂无数据'}}</span></view>
							<view>总产量(kg):<span>{{item.zongchanliang !==null?item.zongchanliang:'暂无数据'}}</span>
							</view>
							<view>超标等级:<span
									:style="item.chaobiaodengji == 0?'color:green':item.chaobiaodengji == 1?'color:orange':item.chaobiaodengji == 2?'color:#ff5722':item.chaobiaodengji == 3?'color:red':''">{{item.chaobiaodengji_dictText}}</span>
							</view>
							<view>处理状态:<span
									:style="item.overproofStatus == 0?'color:green':item.overproofStatus == 10?'color:orange':item.overproofStatus == 20?'color:blue':item.overproofStatus == 30?'color:red':''">{{item.overproofStatus ==0?'未处理':item.overproofStatus ==10?'已处置':item.overproofStatus ==20?'监理已审核':item.overproofStatus ==30?'监理已驳回':'暂无数据'}}</span>
							</view>
						</view>
					</navigator>
				</view>
			</view>
		</view>
		<view>
			<u-modal :show="show" showConfirmButton showCancelButton @confirm="confirm" @cancel="cancel">
				<view class="select-box">
					<view class="Task">
						设备名称:
						<view class="Task-input">
							<eq :sbtype="sbtype" @choose="changevalue"></eq>
						</view>
					</view>
					<view class="model">
						<view class="title">超标等级：</view>
						<picker @change="Cblevel" :value="index" :range="cblevelname">
							<!-- <text class="" style="color: rgb(192, 196, 204);">
									<! {{pdresult== ''?"请选择判定结果":pdresult}} -->
							<!-- {{cbindex>-1?cblevelname[cbindex]:'请选择判定结果'}} -->
							<!-- </text> -->
							<u--input placeholder="请选择判定结果" border="surround" v-model="cblevelname[cbindex]" disabled
								suffixIcon="arrow-down">
							</u--input>
							<!-- <text class="cuIcon cuIcon-unfold"></text> -->
						</picker>
					</view>

					<view class="Task">
						开始时间:
						<view class="Task-input">
							<!-- <view class="screen-modal-item-name">时间：</view> -->
							<view class="screen-modal-item-input" @click="beginshow = true">
								<u--input placeholder="请选择开始时间" border="surround" v-model="begintimevalue" disabled
									suffixIcon="arrow-down"></u--input>
							</view>
						</view>
					</view>
					<view class="Task">
						结束时间:
						<view class="Task-input">
							<!-- <view class="screen-modal-item-name">时间：</view> -->
							<view class="screen-modal-item-input" @click="endshow = true">
								<u--input placeholder="请选择结束时间" border="surround" v-model="endtimevalue" disabled
									suffixIcon="arrow-down"></u--input>
							</view>
						</view>
					</view>
				</view>
			</u-modal>
		</view>
		<mx-date-picker :show="beginshow" :type="type" :value="value" :show-tips="true" :show-seconds="true"
			@confirm="confirmdate" @cancel="confirmdate" />

		<mx-date-picker :show="endshow" :type="type" :value="value" :show-tips="true" :show-seconds="true"
			@confirm="confirmend" @cancel="confirmend" />
	</view>
</template>

<script>
	import eq from '../../component/equipment/equipment.vue'
	import MxDatePicker from "@/components/mx-datepicker/mx-datepicker.vue";
	export default {
		name: 'asphaltmix',
		components: {
			eq,
			MxDatePicker
		},
		data() {
			return {
				pageNo: 1,
				listdata: {},
				loading: true,
				show: false,
				sbtype: '1',
				cblevelname: [
					"合格",
					"初级超标",
					"中级超标",
					"高级超标",
				],
				index: -1,
				pdresult: '',
				Number: '',
				beginshow: false,
				endshow: false,
				begintimevalue: '',
				endtimevalue: '',
				value: '',
				type: 'datetime',
				cbindex: -1,
				cbdj: ''
			}
		},
		onLoad() {
			this.lqdata()

		},
		onReachBottom() {
			uni.showNavigationBarLoading()
			this.pageNo++
			this.lqdata()
			uni.hideNavigationBarLoading()
		},
		methods: {
			lqdata() {
				this.$http.get(`/lqbhz/bhzLqBases/list`, {
					params: {
						order: 'desc',
						column: 'id',
						chaobiaodengji: this.cbdj,
						shebeibianhao: this.Number,
						chuliaoshijian_end: this.endtimevalue,
						chuliaoshijian_begin: this.begintimevalue,
						pageNo: this.pageNo,
						PageSize: 10,
					}
				}).then(res => {
					console.log(res, '沥青拌合')
					this.loading = false
					if (res.data.result.records.length == 0) {
						uni.showToast({
							title: '没有更多数据啦',
							icon: "loading"
						})
					}
					if (this.pageNo != 1) {
						this.listdata = this.listdata.concat(res.data.result.records)
					} else {
						this.listdata = res.data.result.records
					}
				})
			},
			changevalue(choosevalue) {
				// console.log(choosevalue,'ddd')
				this.Number = choosevalue
			},
			Cblevel(e) {
				// console.log(e)
				// this.pdresult = e.detail.value
				// if (e.detail.value == 0) {
				// 	e.detail.value = '合格'
				// } else if (e.detail.value == 1) {
				// 	e.detail.value = '不合格'
				// 	this.pdresult = e.detail.value
				// }
				this.cbindex = e.detail.value
				console.log(this.cbindex, 'ddd')
				this.choosekey = 3
				this.cbdj = e.detail.value
				// this.Choose()
			},
			screen() {
				this.show = true
			},
			confirm() {
				this.lqdata();
				this.show = false
				this.Number = ''
				this.cbindex = ''
				this.begintimevalue = ''
				this.endtimevalue = ''
			},
			cancel() {
				this.show = false
				this.Number = ''
				this.cbindex = ''
				this.begintimevalue = ''
				this.endtimevalue = ''
			},
			confirmdate(e) {
				this.begintimevalue = e.value
				this.beginshow = false
			},
			confirmend(e) {
				this.endtimevalue = e.value
				this.endshow = false
			},
		}
	}
</script>

<style lang="scss" scoped>
	.wrap {
		width: 100%;
		height: auto;
		background-color: #F2F5FF;

		.pourlist {
			width: 100%;
			margin-top: 30upx;

			&-item {
				position: relative;
				width: 690upx;
				// height: 462upx;
				margin: 0 auto;
				margin-bottom: 30upx;
				border-radius: 16upx;
				background-color: white;

				.hege {
					background-image: url(../../../static/pour/hege.png);
					background-repeat: no-repeat;
					background-size: 100% 100%;
				}

				&-sta {
					position: absolute;
					right: 0px;
					width: 165upx;
					height: 165upx;

					image {
						width: 100%;
						height: 100%;
					}
				}

				&-sta1 {
					position: absolute;
					right: 20upx;
					bottom: 20upx;
					width: 165upx;
					height: 165upx;

					image {
						width: 100%;
						height: 100%;
					}
				}
			}
		}

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

	.model {
		width: 275px;
		height: 80px;

		// border: 1px solid deeppink;
		.title {
			width: 90px;
			height: 30px;
			// border: 1px solid lawngreen;
		}
	}
</style>
