<template>
	<view class="wrap">
		<cu-custom bgColor="bg-white" :isBack="true">
			<block slot="content">张拉数据监测</block>
			<view slot="right" @click="screen">筛选</view>
		</cu-custom>
		<view class="section" v-for="(item,index) in Zllistdata" :key="index">
			<navigator :url="'/pages/smartlc/Zllist/Zldetails?item='+item.syjid">
				<view class="section-text">
					<view>工程名称:<span>{{item.gcmc!==null?item.gcmc:'暂无数据'}}</span></view>
					<view>梁号:<span>{{item.gjbh !==null?item.gjbh:'暂无数据'}}</span></view>
					<view>张拉梁型:<span>{{item.gjmc !==null?item.gjmc:'暂无数据'}}</span></view>
					<view>预制梁场:<span>{{item.yzlc !==null?item.yzlc:'暂无数据'}}</span></view>
					<view>设备名称:<span>{{item.shebeibianhao_dictText !==null?item.shebeibianhao_dictText:'暂无数据'}}</span>
					</view>
					<view>试件数量:<span>{{item.sjsl !==null?item.sjsl:'暂无数据'}}</span></view>
					<view>砼设计强度(MPa):<span>{{item.snsjqd !==null?item.snsjqd:'暂无数据'}}</span></view>
					<view>施工时间:<span>{{item.sgsj !==null?item.sgsj:'暂无数据'}}</span></view>
					<view>砼强度(MPa):<span>{{item.snskqd !==null?item.snskqd:'暂无数据'}}</span></view>
					<view>张拉加载速度:<span>{{item.zlcsu !==null?item.zlcsu:'暂无数据'}}</span></view>
					<view>张拉加载预应力:<span>{{item.zlcsk !==null?item.zlcsk:'暂无数据'}}</span></view>
					<view>起拱度:<span>{{item.zlcsep !==null?item.zlcsep:'暂无数据'}}</span></view>
					<view>完成状态:<span
							:style="item.status ==0?'color:green;font-weight:bold;font-family: cursive;':item.status ==1?'color:red;font-weight:bold;font-family: cursive;':'color:orange;font-weight:bold;font-family: cursive;'">{{item.status==0?'完成':item.status==1?'未完成':'暂无数据'}}</span>
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
		name: "Zllist",
		components: {
			eq,
			MxDatePicker
		},
		data() {
			return {
				Zllistdata: [],
				pageNo: 1,
				show: false,
				sbtype: '9',
				datetimevalue: '',
				dateshow: false,
				type: 'datetime',
				value: '',
				Number: ''
			}
		},
		onReachBottom() {
			uni.showNavigationBarLoading()
			this.pageNo++
			this.listdata()
			uni.hideNavigationBarLoading()
		},
		created() {
			this.listdata()
		},
		methods: {
			listdata() {
				let params = {
					column: 'id',
					order: 'desc',
					pageNo: this.pageNo,
					PageSize: 10,
					shebeibianhao: this.Number,
					sgsj: this.datetimevalue,

					// sys_depart_orgcode: this.orgcode,
					// shebeibianhao: this.sbNumber,
					// pdjg: this.pdjg,
					// syrq_begin: this.begintime,
					// syrq_end: this.endtim
				}
				this.$http.get(`/zhanglaxxb/trZhanglaXxb/list`, {
					params
				}).then(res => {
					if (res.data.result.records.length == 0) {
						uni.showToast({
							title: '别下拉了,没有数据啦',
							icon: 'none'
						})
					}
					if (this.pageNo != 1) {
						this.Zllistdata = this.Zllistdata.concat(res.data.result.records)
					} else {
						this.Zllistdata = res.data.result.records
					}
				})
			},
			changevalue(choosevalue) {
				// console.log(choosevalue,'dddd')
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
		},
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
