<template>
	<view id="home">
		<cu-custom bgColor="bg-white" :isBack="true">
			<block slot="content">梁信息列表</block>
			<!-- <view slot="right" @click="screen">筛选</view> -->
		</cu-custom>
		<!-- <view class="top-two">
			<view class="left" @click="change(3)">
				<view class="left-text">
					<p class="red">{{totaldata.arrivesum}}</p>
					到场车次
				</view>
			</view>
			<view class="left" @click="change(1)">
				<view class="left-text">
					<p class="orange">{{totaldata.hege}}</p>
					合格数
				</view>
			</view>
			<view class="left" @click="change(2)">
				<view class="left-text">
					<p class="green">{{totaldata.finish}}</p>
					已完成
				</view>
			</view>
		</view> -->
		<!-- <view class="top-two">
			<view class="left" @click="change(0)">
				<view class="left-text">
					<p class="purple">{{totaldata.noarrive}}</p>
					在途车次
				</view>
			</view>
			<view class="left" @click="change(4)">
				<view class="left-text">
					<p class="blue">{{totaldata.failed}}</p>
					退场数
				</view>
			</view>
			<view class="left" @click="change(5)">
				<view class="left-text">
					<p class="black">{{totaldata.arrivednosh}}</p>
					总数量
				</view>
			</view>
		</view> -->
		<view class="section">
			<!-- <navigator :url="'/pages/smartlc/infolist/infodetails?item='+JSON.stringify(item)"> -->
			<view class="section-text">
				<view class="standard" style="color: #FF0000;">{{shitilist.active}}</view>
				<view>工程名称:<span>{{shitilist.projname}}</span></view>
				<view>所属组织机构:<span>{{shitilist.sysOrgCode_dictText}}</span></view>
				<view>任务单编号:<span>{{shitilist.code}}</span></view>
				<view>台座:<span>{{shitilist.taizuono}}</span></view>
				<view>创建时间:<span>{{shitilist.dattim}}</span></view>
				<view>浇注日期:<span>{{shitilist.begtim}}</span></view>
				<view>截止日期:<span>{{shitilist.endtim}}</span></view>
				<view>计划生产时间:<span>{{shitilist.productiontime}}</span></view>
				<view>施工部位:<span>{{shitilist.conspos}}</span></view>
				<view>生产线:<span>{{shitilist.station_dictText}}</span></view>
				<view>坍落度:<span>{{shitilist.lands}}</span></view>
				<view>搅拌时间:<span>{{shitilist.mixlast}}</span></view>
				<view>强度等级:<span>{{shitilist.betlev}}</span></view>
				<view>任务方量:<span>{{shitilist.mete}}</span></view>
				<view>浇筑方式:<span>{{shitilist.pour}}</span></view>
				<view>创建人:<span>{{shitilist.createBy}}</span></view>
			</view>
			<!-- </navigator> -->
		</view>
		<u-modal :show="show" showCancelButton @cancel="cancel" @confirm="confirm">
			<view class="radio">
				<view class="result">
					<view style="padding: 30upx 10upx;font-size: 28upx;">
						验收结果:
					</view>

					<view style="margin: -70upx 120upx;">
						<u-radio-group v-model="value" placement="row">
							<u-radio :customStyle="{marginLeft: '15px'}" v-for="(item, index) in radiolist" :key="index"
								:label="item.name" :name="item.status" @change="radioChange">
							</u-radio>
						</u-radio-group>
					</view>
				</view>
				<view class="opinion">
					<view style="padding: 30upx 10upx;font-size: 28upx;">
						验收意见:
					</view>
					<view style="margin:-85upx 150upx;width:430upx;">
						<u--input placeholder="请输入验收意见" border="surround" v-model="ysresult" @change="changes">
						</u--input>
					</view>
				</view>
			</view>
		</u-modal>
		<u-button style="background-color:#387FFD;width: 20%;color:#fff;border-radius: 10px;" @click="show = true">
			验收</u-button>
	</view>
</template>
<script>
	import timeSelector from '@/components/wing-time-selector/wing-time-selector.vue';
	import api from '@/api/apis.js'
	export default {
		name: 'infolist',
		components: {
			timeSelector,
		},
		data() {
			return {
				shitilist: {},
				sbtype: '55',
				pageNo: 1,
				urls:`/zhiliangrenwudan/zhiliangrenwudan/hlist`,
				id: '',
				value: 0,
				show: false,
				title: '标题',
				ysresult: '',
				content: 'uView的目标是成为uni-app生态最优秀的UI框架',
				radiolist: [{
						name: '合格',
						status: 1,
						disabled: false
					},
					{
						name: '不合格',
						status: 0,
						disabled: false
					}
				],
			}
		},
		onLoad(options) {
			this.id = options.id
			
			
		},
		onReachBottom() {
			uni.showNavigationBarLoading()
			this.pageNo++
			this.textlist()
			uni.hideNavigationBarLoading()
		},
		created() {
			this.textlist()
		},

		methods: {
			textlist() {
				console.log(this.id)
				this.$http.get(`/zhiliangrenwudan/zhiliangrenwudan/hlist`, {
					params: {
						qcode:this.id
					}
				}).then(res => {
					// console.log(res,'扫码获取新数据')
					this.shitilist = res.data.result.records[0]
				})
			},
			radioChange(e) {
				console.log(e, '验收结果')
				this.result = e
			},
			changes(e) {
				console.log(e, '验收意见')
				this.yijian = e
			},
			cancel() {
				this.show = false
			},
			confirm() {
				let params = {
					id: this.shitilist.id,
					sfhg: this.result,
					acceptOpinion: this.yijian
				}
				this.$http.put('/zhiliangrenwudan/zhiliangrenwudan/edit', params).then(res => {
				 console.log(res, '验收结果')
			 })
				this.show = false
				setTimeout(() => {
					uni.navigateBack({
						delta: 1
					})
				}, 2000)
			}
		}
	}
</script>

<style lang="scss" scoped>
	#home {
		// background-color: #F2F5FE;
		width: 100%;
		height: auto;
		background-color: #F2F5FE;
		// white-space: nowrap;
	}

	.newcolor {
		color: #000;
		font-size: 30upx;
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

	.increase-img {
		width: 24%;
		height: 87px;
		background-image: url(../../../static/pour/add.png);
		background-size: 100% 100%;
		position: fixed;
		bottom: 176px;
		right: -18px;
	}

	.radio {
		width: 300px;
		height: auto;
		// border: 1px solid #8DC63F;
	}

	.result {
		width: 300px;
		height: 100upx;
		// padding:10upx 10upx;
		// border: 1px solid #1785FF;
	}

	.opinion {
		width: 300px;
		height: 100upx;
		// padding:10upx 10upx;
		// border: 1px solid #55aa7f;
	}
</style>
