<template>
	<view class="wrap">
		<cu-custom bgColor="bg-white" :isBack="true">
			<block slot="content">梁详情信息</block>
			<!-- <view slot="right" @click="screen">筛选</view> -->
		</cu-custom>
		<view class="section">
			<view class="section-text">
				<view>工程名称:<span>{{item.projname}}</span></view>
				<view>任务单编号:<span>{{item.code}}</span></view>
				<view>台座:<span>{{item.taizuono}}</span></view>
				<view>创建时间:<span>{{item.dattim}}</span></view>
				<view>施工部位:<span>{{item.conspos}}</span></view>
			</view>
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
						<u--input placeholder="请输入验收意见" border="surround" v-model="ysresult"  @change="changes"></u--input>
					</view>
				</view>
			</view>
		</u-modal>
		<u-button style="background-color:#387FFD;width: 20%;color:#fff;border-radius: 10px;" @click="show = true">
			验收</u-button>
	</view>
</template>

<script>
	export default {
		name: 'infodetails',
		data() {
			return {
				item: '',
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
			this.item = JSON.parse(options.item)
		},
		methods: {
			radioChange(e) {
				console.log(e, '验收结果')
			},
			changes(e){
				console.log(e,'验收意见')
			},
			cancel() {
				this.show = false
			},
			confirm() {
				this.show = false
				
			}
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
		.radio{
			width:700px;
			height:auto;
			// border: 1px solid #8DC63F;
		}
		.result{
			width:300px;
			height:100upx;
			// padding:10upx 10upx;
			// border: 1px solid #1785FF;
		}
		.opinion{
			width: 300px;
			height: 100upx;
			// padding:10upx 10upx;
			// border: 1px solid #55aa7f;
		}
	}
</style>
