<template>
	<view id="pourdetail">
		<cu-custom bgColor="bg-white" :isBack="true">
			<block slot="content">料仓详情</block>
		</cu-custom>
		<view class="main">
			<view class="main-item">
				<view class="biaoqian"></view>
				<view class="mainnew">主要信息</view>
			</view>
			<view class="main-item">
				<view class="main-item-name">
					所属部门：<span>{{loaddata.sysOrgCode_dictText}}</span>
				</view>
			</view>
			<view class="main-item">
				<view class="main-item-name">
					料仓名称：<span>{{loaddata.name}}</span>
				</view>
			</view>
			<view class="main-item">
				<view class="main-item-name">
					材料类型：<span>{{loaddata.cailiaono_dictText}}</span>
				</view>
			</view>
			<view class="main-item">
				<view class="main-item-name">
					规格类型：<span>{{loaddata.guigexinghao}}</span>
				</view>
			</view>
			<view class="main-item">
				<view class="main-item-name">
					重量：<span>{{loaddata.picizhong}}</span>
				</view>
			</view>
			<view class="main-item">
				<view class="main-item-name">
					计量单位：<span>{{loaddata.danwei_dictText}}</span>
				</view>
			</view>
			<view class="main-item">
				<view class="main-item-name">
					料仓状态：<span>{{loaddata.liaocangStatus_dictText}}</span>
				</view>
			</view>
			<view class="main-item">
				<view class="main-item-name">
					生产厂家：<span>{{loaddata.changjia}}</span>
				</view>
			</view>
			<view class="main-item" v-if="loaddata.liaoweino != null && loaddata.liaoweino != ''">
				<view class="main-item-name">
					料位编码：<span>{{loaddata.liaoweino}}</span>
				</view>
			</view>
			<view class="main-item" v-if="loaddata.liaoweino != null && loaddata.liaoweino != ''">
				<view class="main-item-name">
					料位传感器重量：<span>{{loaddata.weight}}</span>
				</view>
			</view>
			<view class="main-item"></view>
		</view>
		<view class="btn" style="">
			<u-button @click="handleEdit" style="width:150px;border-radius: 10px;color: #fff;background-color: #F08322;"
				text="编辑">
			</u-button>
			<u-button
				v-if="loaddata.infraredFence !== null && (loaddata.cailiaono == '6' || loaddata.cailiaono == '7' || loaddata.cailiaono == '8')"
				@click="handleEdit1" style="width:150px;border-radius: 10px;color: #fff;background-color: #22A3EE;"
				text="水泥仓开门">
			</u-button>
			

		</view>
		<view class="btn" style="display: flex">
			<u-button v-has="'liaocang:posttoOpen'" @click="showopen = true"
				style="width:150px;border-radius: 10px;color: #fff;background-color: #48AACC;" text="开门">
			</u-button>
			<u-button v-has="'liaocang:posttoClose'" @click="showclose = true"
				style="width:150px;border-radius: 10px;color: #fff;background-color: #48AACC;" text="关门">
			</u-button>
			<u-button v-has="'liaocang:posttoStd'" @click="showstd = true "
				style="width:150px;border-radius: 10px;color: #fff;background-color: #48AACC;" text="校秤">
			</u-button>
			<u-button v-has="'liaocang:posttoZero'" @click=" showzero = true "
				style="width:150px;border-radius: 10px;color: #fff;background-color: #48AACC;" text="校零">
			</u-button>
		</view>
          <u-modal :show="showopen" title="确认开门" showCancelButton @cancel="cancelopen" @confirm="handleCommond('posttoOpen')"></u-modal>
          <u-modal :show="showclose" title="确认关门" showCancelButton @cancel="cancelclose" @confirm="handleCommond('posttoClose')"></u-modal>
		  <u-modal :show="showzero" title="确认校零" showCancelButton @cancel="cancelzero" @confirm="handleCommond('posttoZero')"></u-modal>
		  <u-modal :show="showstd" title="确认校秤" showCancelButton @cancel="cancelstd" @confirm="handleCommond('posttoStd')">
			  <view class="modal-item">
			  	<view class="modal-item-name">校秤：</view>
			  	<view class="modal-item-input">
			  		<u--input placeholder="请输入校秤值" border="surround" v-model="jiaocheng"></u--input>
			  	</view>
			  </view>
		  </u-modal>
		  
		<u-modal :show="show" showCancelButton @cancel="cancelsh" @confirm="confirmsh">
			<view class="modal">
				<view class="modal-item">
					<view class="modal-item-name">料仓状态：</view>
					<view class="modal-item-input">
						<dict dictCode="liaocang_status" title="请选择料仓状态" @choose="changeStatus">
						</dict>
					</view>
				</view>
				<view class="modal-item" v-if="loaddata.infraredFence">
					<view class="modal-item-name">料仓门禁：</view>
					<view class="modal-item-input">
						<!-- <picker @change="changeEquipment" :range="equipmentList" range-key="sbname">
							<u--input placeholder="请选择设备名称" border="surround" v-model="equipmentName" disabled
								suffixIcon="arrow-down"></u--input>
						</picker> -->
						<u--input placeholder="无" border="surround" v-model="loaddata.infraredFence_dictText" disabled>
						</u--input>
					</view>
				</view>
				<view class="modal-item">
					<view class="modal-item-name">重量：</view>
					<view class="modal-item-input">
						<u--input placeholder="请输入重量" border="surround" v-model="picizhong"></u--input>
					</view>
				</view>
			</view>
		</u-modal>
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
				liaoweinojc:'',
				jiaocheng:'',
				show: false,
				showopen: false,
				showclose: false,
				showzero: false,
				showstd: false,
				loaddata: {},
				// equipmentList: [],
				// equipmentName: '',
				// shebeiNo: null,
				liaocangStatus: null,
				picizhong: ''
			}
		},
		onLoad(options) {
			this.loaddata = JSON.parse(options.item)
			console.log("this.loaddata.liaoweino"+this.loaddata.liaoweino)
			// this.getEquipmentlist()
		},
		methods: {
			// 获取设备列表
			// getEquipmentlist() {
			// 	let params = {
			// 		sys_depart_orgcode: this.$store.getters.orgcode,
			// 		sbtypes: '65'
			// 	}
			// 	this.$http.get('/sys/user/list3', {
			// 		params
			// 	}).then(res => {
			// 		this.equipmentList = res.data.result
			// 	})
			// },
			changeStatus(val) {
				this.liaocangStatus = val
			},
			// changeEquipment(e) {
			// 	this.equipmentName = this.equipmentList[e.detail.value].sbname
			// 	this.shebeiNo = this.equipmentList[e.detail.value].sbjno
			// },
			handleEdit() {
				this.liaocangStatus = null
				this.show = true
			},
			
			
			handleCommond(commond) {
				 let liaoweino = this.loaddata.liaoweino
				if(commond == "posttoStd" ){
					liaoweino = this.loaddata.liaoweino + '|'+this.jiaocheng
				}
				let params = {
					liaoweino: liaoweino,
				    danwei:commond
				}
				this.$http.post('/wzliaocang/wzliaocang/lw', params).then(res => {
					 if(res.data.code === 200 ) {
						 this.$tip.success(res.data.message)
						
						 this.jiaocheng = ''
					} else {
						this.$tip.toast("请求失败")
					}
					this.showopen=false
					this.showclose=false
					this.showzero=false
					this.showstd=false
				
				})
			},
			
		
			
			confirmsh() {
				let params = {
					id: this.loaddata.id,
					sysOrgCode: this.$store.getters.orgcode,
					liaocangStatus: this.liaocangStatus,
					picizhong: this.picizhong
				}
				this.$http.put('/wzliaocang/wzliaocang/edit', params).then(res => {
					if (res.data.success) {
						if (this.loaddata.infraredFence && this.liaocangStatus == 3) {
							this.$tip.success('合格仓红外栅栏预警已关闭')
						} else if (this.loaddata.infraredFence && (this.liaocangStatus == 2 || this
								.liaocangStatus == 4)) {
							this.$tip.success('红外栅栏已开启')
						} else {
							this.$tip.success(res.data.message)
						}
					} else {
						this.$tip.toast(res.data.message)
					}
					this.show = false
					setTimeout(() => {
						uni.navigateBack({
							delta: 1
						})
					}, 1000)
				})
			},

			handleEdit1() {
				let params = {
					infraredFence: this.loaddata.infraredFence,
					sysOrgCode: this.$store.getters.orgcode,
					liaocangStatus: this.liaocangStatus,
					picizhong: this.picizhong
				}
				this.$http.post('/wzliaocang/wzliaocang/openSN', params).then(res => {
					if (res.data.success) {
						this.$tip.toast(res.data.result)
						this.show = false

					}
					setTimeout(() => {
						uni.navigateBack({
							delta: 1
						})
					}, 1000)

				})
			},
			cancelopen() {
				this.showopen = false
			},
			cancelclose() {
				this.showclose = false
			},
			cancelstd() {
				this.showstd = false
			},
			cancelzero() {
				this.showzero = false
			},
			cancelsh() {
				this.show = false
			},
		}
	}
</script>

<style lang="scss" scoped>
	#pourdetail {
		width: 100%;
		background-color: #F3F5FE;

		.main {
			width: 690rpx;
			margin: 0 auto;
			margin-top: 30rpx;
			background-color: white;
			border-radius: 16rpx;

			&-item {
				display: flex;
				font-size: 28rpx;
				padding-top: 30rpx;

				.mainnew {
					color: #333333;
					font-size: 30rpx;
					font-weight: bold;
				}

				.biaoqian {
					width: 12rpx;
					height: 34rpx;
					border-radius: 6rpx;
					margin: 0 30rpx;
					background-color: #4A7FFF;
				}

				&-name {
					margin-left: 30rpx;
					text-align: left;
					color: #9299A8;

					span {
						margin-right: 30rpx;
						color: #4C5971;
					}
				}
			}
		}

		.btn {
			padding: 15px 0;
		}

		.modal {
			width: 100%;

			&-item {
				margin-bottom: 30rpx;

				&-name {
					color: #4C5971;
					font-size: 34rpx;
					margin-bottom: 30rpx;
				}
			}
		}
	}
</style>
