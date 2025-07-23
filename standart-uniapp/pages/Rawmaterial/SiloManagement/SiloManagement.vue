<template>
	<view id="silomangage">
		<cu-custom bgColor="bg-white" :isBack="true">
			<block slot="content">料仓管理</block>
			<view slot="right" @click="screen">筛选</view>
		</cu-custom>
		<view class="silolist">
			<view v-for="(item,index) in listdata" :key="index" @click="checkDetail(item)" class="silolist-item">
				<view class="silolist-item-sta">
					<image v-if="item.liaocangStatus == '1'" src="../../../static/pour/jcz.png" mode=""></image>
					<image v-if="item.liaocangStatus == '2'" src="../../../static/pour/jyz.png" mode=""></image>
					<image v-if="item.liaocangStatus == '3'" src="../../../static/pour/hege.png" mode=""></image>
					<image v-if="item.liaocangStatus == '4'" src="../../../static/pour/djy.png" mode=""></image>
				</view>
				<view class="silolist-item-con">
					<view class="silolist-item-con-name">
						料仓名称：<span>{{ item.name }}</span>
					</view>
					<view class="silolist-item-con-name">
						规格：<span>{{ item.guigexinghao }}</span>
					</view>
					<view class="silolist-item-con-name">
						库存：<span>{{ item.kucun }}</span>
					</view>
					<view class="silolist-item-con-name">
						负责人：<span>{{ item.updateBy || item.createBy }}</span>
					</view>
					<view class="silolist-item-con-name" v-if="item.liaoweino != null && item.liaoweino != ''" >
						料位编码：<span>{{ item.liaoweino }}</span>
					</view>
					<view class="silolist-item-con-name"></view>
				</view>
			</view>
			<view style="height: 40rpx;"></view>
		</view>
		<view class="screen" v-if="show">
			<view class="screen-modal">
				<view class="screen-modal-item">
					<view class="screen-modal-item-name">料仓名称：</view>
					<view class="screen-modal-item-input">
						<u--input placeholder="请输入料仓名称" border="surround" v-model="name"></u--input>
					</view>
				</view>
				<view class="screen-modal-item">
					<view class="screen-modal-item-name">材料类型：</view>
					<view class="screen-modal-item-input">
						<dict dictCode="cailiaono" title="请选择材料类型" @choose="changeMaterial">
						</dict>
					</view>
				</view>
				<view class="screen-modal-btn">
					<u-button style="width: 120px;" text="取消" type="primary" plain @click="confirm"></u-button>
					<u-button style="width: 120px;" type="primary" text="确认" @click="handleOk"></u-button>
				</view>
			</view>
		</view>
	</view>
</template>

<script>
	import dict from '@/pages/component/dict/dict.vue'
	export default {
		components: {
			dict
		},
		data() {
			return {
				show: false,
				listdata: [],
				pageNo: 1,
				orgCode: '',
				name: '',
				cailiaono: null
			}
		},
		onReachBottom() {
			this.pageNo++
			this.getloadlist()
		},
		// onPullDownRefresh() {
		//     this.pageNo = 1
		// 	this.listdata = []
		// 	this.name = ''
		// 	this.cailiaono = null
		// 	this.getloadlist()
		// 	uni.stopPullDownRefresh();
		// },
		onLoad() {
			this.orgCode = this.$store.getters.orgcode
		},
		onShow(){
			this.getloadlist()
		},
		methods: {
			checkDetail(data) {
				uni.navigateTo({
					url: '/pages/Rawmaterial/SiloManagement/SiloManageDetail?item=' + JSON.stringify(data)
				})
			},
			// 打开筛选框
			screen() {
				this.cailiaono = null
				this.show = true
			},
			// 确认筛选
			handleOk() {
				this.pageNo = 1
				this.listdata = []
				this.getloadlist()
				this.show = false
			},
			// 关闭筛选框
			confirm() {
				this.show = false
			},
			//材料类型
			changeMaterial(val) {
				this.cailiaono = val
			},
			// 获取料仓数据
			getloadlist() {
				let params = {
					column: 'id',
					order: 'desc',
					pageNo: this.pageNo,
					pageSize: 10,
					sys_depart_orgcode: this.orgCode,
					name: this.name,
					cailiaono: this.cailiaono
				}
				this.$http.get('/wzliaocang/wzliaocang/list', {
					params
				}).then(res => {
					if (res.data.result.records.length == 0) {
						uni.showToast({
							title: '没有更多数据了',
							icon: 'none'
						})
					} else {
						this.listdata = [...this.listdata, ...res.data.result.records]
					}
				})
			}
		}
	}
</script>

<style lang="scss" scoped>
	#silomangage {
		background-color: #F3F5FE;

		.silolist {
			width: 100%;
			margin-top: 30rpx;

			&-item {
				position: relative;
				width: 690rpx;
				margin: 0 auto;
				margin-bottom: 30rpx;
				border-radius: 16rpx;
				background-color: white;

				&-sta {
					position: absolute;
					right: 0px;
					width: 165rpx;
					height: 165rpx;

					image {
						width: 100%;
						height: 100%;
					}
				}

				&-con {
					width: 610rpx;
					// height: 336rpx;
					margin: 0 auto;

					&-name {
						padding-top: 30rpx;
						color: #9299A8;
						font-size: 28rpx;

						span {
							color: #4C5971;
						}
					}
				}

			}
		}

		.screen {
			position: fixed;
			background: rgba(000, 000, 000, .3);
			left: 0;
			top: 0;
			width: 100%;
			height: 100%;

			&-modal {
				background-color: #FFFFFF;
				position: absolute;
				top: 50%;
				left: (750rpx - 690rpx) / 2;
				width: 690rpx;
				transform: translateY(-50%);
				box-shadow: 0 0 20px 0 rgba(0, 0, 0, 0.1);
				border-radius: 14rpx;
				padding: 40rpx;

				&-item {
					text-align: left;
					margin-bottom: 40rpx;

					&-name {
						color: #4C5971;
						font-size: 34rpx;
						margin-bottom: 40rpx;
					}

				}

				&-btn {
					display: flex;
				}
			}
		}

	}
</style>