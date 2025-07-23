<template>
	<view id="home">
		<cu-custom bgColor="bg-white" :isBack="true">
			<block slot="content">发车单实时数据</block>
		</cu-custom>
		<view class="section">

			<view class="map">
				<map scale="7" id="map" :latitude="latitude" :longitude="longitude" :markers="covers"
					style="width: 100%; height:100%;" @markertap="markertap">

				</map>
			</view>
		</view>
		<view class="sectionall">
			<image src="../../../static/shiti/yj.png"
				style="background-size: 100% 100%; width: 50upx;height: 6upx;margin: -10px 46%;" mode=""></image>
			<view class="article">
				<view class="article-txt">
					<view class="transport">
						累计运输
					</view>
					<view class="num">
						{{Number(count.jcsl).toFixed(1)}}吨
					</view>
				</view>
				<view class="article-txt">
					<view class="transport">
						已达车次
					</view>
					<view class="num">
						{{count.arrived}}
					</view>
				</view>
				<view class="article-txt">
					<view class="transport">
						运输中
					</view>
					<view class="num">
						{{count.noarrive}}
					</view>
				</view>
			</view>
			<view class="footer">
				<view class="wrap-nav">
					<view class="screen-modal-item-name">供应商：</view>
					<view class="screen-picker">
						<!-- <picker @change="Pickgys" :range="gys"> -->
						<u--input placeholder="供应商" border="surround" v-model="model.ghdw" disabled
							suffixIcon="arrow-down"></u--input>
						<!-- </picker> -->
					</view>

				</view>
				<view class="wrap-nav">
					<view class="screen-modal-item-name">目的地：</view>

					<view class="screen-picker">
						<!-- <picker @change="Pickermdd" :range="mdd"> -->
						<u--input placeholder="目的地" border="surround" v-model="model.mdd" disabled
							suffixIcon="arrow-down">
						</u--input>
						<!-- </picker> -->
					</view>

				</view>
				<view class="wrap-nav">
					<view class="screen-modal-item-name">材料类型：</view>
					<view class="screen-picker">
						<u--input placeholder="材料类型" border="surround" v-model="model.cailiao" disabled
							suffixIcon="arrow-down">
						</u--input>
					</view>
				</view>
			</view>
		</view>
	</view>
</template>
<script>
	import amapFile from '../../../common/util/amap-wx.js';
	import timeSelector from '@/components/wing-time-selector/wing-time-selector.vue';
	import dict from '../../component/dict/dict.vue'
	import api from '@/api/apis.js'
	export default {
		name: 'departlist',
		components: {
			timeSelector,
			dict
		},
		data() {
			return {
				// gys: [],
				// mdd: [],
				// supplier: '',
				// gyslist: '',
				// mdidata: '',
				// Endto: '',
				count: '',
				// locationdata: {},
				latitude: 0,
				longitude: 0,
				covers: [],
				model: '',
			}
		},
		onLoad() {

		},
		created() {
			// this.gystype()
			// this.mddtype()
			this.Maplocation()
		},

		methods: {
			Maplocation() {
				this.$http.get(`/wbshebeidetail/wbshebeiDetail/reallist`).then(res => {
					this.count = res.data.data
					let position = res.data.result
					this.locatlist(position)
				})
			},
			//标记点用于在地图上显示标记的位置
			locatlist(e) {
				console.log(e, '定位')
				e.forEach(item => {
					this.latitude = parseFloat(item.ddlat)
					this.longitude = parseFloat(item.ddlng)
					this.covers.push({
						id: item.id,
						latitude: parseFloat(item.ddlat),
						longitude: parseFloat(item.ddlng),
						iconPath: '../../../static/shiti/jt.png',
						width: 50,
						height: 50,
						// title:item.cph + "——"+item.danhao,
						callout: {
							content: `车牌号：${item.cph}
									发车单号：${item.danhao}`,
							color: '#323232', //文字颜色
							fontSize: 14, //文本大小
							borderRadius: 5, //边框圆角
							bgColor: '#FFFFFF', //背景颜色
							display: 'ALWAYS', //常显
						}
					})
				})
				console.log(this.covers)

			},
			//点击标记点对应的气泡时触发，e.detail = {markerId}
			markertap(e) {
				console.log(e.detail.markerId)
				let iddata = e.detail.markerId
				this.$http.get(`/wbshebeidetail/wbshebeiDetail/reallist`, {
					params: {
						id: iddata
					}
				}).then(res => {
					// console.log(res.data.result[0])
					this.model = res.data.result[0]
				})
			},
		}
	}
</script>

<style lang="scss">
	#home {
		// background-color: #F2F5FE;
		width: 100%;
		height: 100%;
		background-color: #F2F5FE;
		// white-space: nowrap;
	}


	.section {
		width: 100%;
		height: 100%;
		margin: 0 auto;
        // border: 1px solid #ff5500;
		// background-color: #fff;
		.map {
			width: 100%;
			height: 790upx;
			height: 730upx;
			// border: 1px solid #ED1C24;
		}
	}

	.sectionall {
		width: 100%;
		height:675upx;
		// border: 1px solid #1CBBB4;
		border-radius: 10px;
		background-color: #fff;
		position: absolute;
		top:423px;
		// position: absolute;
		// top: 400px;
		.article {
			width: 700upx;
			height:125upx;
			margin: 25px auto;
			text-align: center;
			// border: 1px solid #91CB74;
			border-radius: 10px;
			display: flex;
			flex-direction: row;
			box-shadow: 0px 0px 25px 0px rgba(149, 166, 182, 0.16);

			.article-txt {
				width: 233upx;
				height: 160upx;
				// border: 1px solid #ff0000;


				.transport {
					// width: 120upx;
					height: 28upx;
					font-size: 30upx;
					font-family: PingFang SC;
					font-weight: 500;
					color: #333333;
					line-height: 100upx;
				}

				.num {
					// width: 40upx;
					height: 25upx;
					font-size: 34upx;
					font-family: PingFang SC;
					font-weight: 800;
					color: #387FFD;
					line-height: 140upx;
				}

			}
		}

		.footer {
			width: 700upx;
			height: auto;
			// border: 1px solid #F37B1D;
			margin: 20px auto;

			.wrap-nav {
				width: 550upx;
				height: 40px;
				// border: 1px solid #1785FF;
				margin: 0 auto;
				padding: 29px 0;
				display: flex;
				flex-direction: row;
				justify-content: space-around;
				align-items: center;

				.screen-modal-item-name {
					flex: 3;
				}

				.screen-picker {
					flex: 8;
				}
			}
		}
	}
</style>
