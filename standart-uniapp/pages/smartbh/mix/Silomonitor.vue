<template>
	<view class="wrap">
		<cu-custom bgColor="bg-white" :isBack="true">
			<block slot="content">料仓动态监测</block>
			<view slot="right" @click="screen">筛选</view>
		</cu-custom>
		<view class="sections">
			<scroll-view scroll-x class="nav bg-white">
				<view class="flex text-center">
					<view class="cu-item flex-sub" :class="0==TabCur?'text-blue cur':''" @tap="tabSelect" data-id="0">
						粉料罐信息
					</view>
					<view class="cu-item flex-sub" :class="1==TabCur?'text-blue cur':''" @tap="tabSelect" data-id="1">
						骨料仓信息
					</view>
				</view>
			</scroll-view>
			<view class="option" v-if="TabCur==0">
				<view class="option-box" v-for="(item,index) in listCroup" :key="index" @click="cldetails(item)">
					<p class="option-title">{{item.weight}}T</p>
					<view class="option-pro">
						<u-line-progress :percentage="item.weight" :showText="false" :height="30" activeColor="#f8af4f">
						</u-line-progress>
					</view>
					<p class="option-titlewe">{{item.name}}</p>
				</view>
				<view  class="option-view">
					<view class="option-button" v-for="(item) in buttonList" :key="item.name"  @click="changeBtn(item)">
						<button :class="item.status==1?'btn-b':'btn-w'">{{item.name}}</button>
					</view>
				</view>
			<!-- 	<scroll-view scroll-x class="nav bg-white">
					<view class="flex text-center">
						<view class="cu-item flex-sub" :class="3==TabCur1?'text-blue cur':''" @tap="tabSelect1" data-id="3">
							1号机组#
						</view>
						<view class="cu-item flex-sub" :class="4==TabCur1?'text-blue cur':''" @tap="tabSelect1" data-id="4">
							2号机组#
						</view>
					</view>
				</scroll-view> -->
				<!-- <view class="option" v-if="TabCur1==3">
					<view class="option-box" v-for="(item,index) in list1" :key="index" @click="cldetails(item)">
						<p class="option-title">{{item.weight}}T</p>
						<view class="option-pro">
							<u-line-progress :percentage="item.weight" :showText="false" :height="30" activeColor="#f8af4f">
							</u-line-progress>
						</view>
						<p class="option-titlewe">{{item.name}}</p>
					</view>
				</view>
				<view class="option" v-if="TabCur1==4">
					<view class="option-box" v-for="(item,index) in list1" :key="index" @click="cldetails(item)">
						<p class="option-title">{{item.weight}}T</p>
						<view class="option-pro">
							<u-line-progress :percentage="item.weight" :showText="false" :height="30" activeColor="#f8af4f">
							</u-line-progress>
						</view>
						<p class="option-titlewe">{{item.name}}</p>
					</view>
				</view> -->
			</view>
			<view class="option" v-if="TabCur==1">
				<view class="option-box" v-for="(item,index) in list2" :key="index" @click="cldtails2(item)">
					<p class="option-title">{{item.name}}</p>
					<view class="option-pro">
						<u-line-progress :percentage="item.weight" :showText="false" :height="30" activeColor="#68f0f6">
						</u-line-progress>
					</view>
					<p class="option-titlewe">{{item.weight}}T</p>
				</view>
			</view>
		</view>
		<u-modal :show="showzj" showCancelButton @confirm="zjconfirm" @cancel="zjcancel">
			<view class="modal">
				<view class="title">组织机构</view>
				<org @choose="Chooseorg"></org>
			</view>
		</u-modal>
		<u-modal :show="show" showCancelButton @confirm="handleconfirm" @cancel="handlecancel">
			<view class="modal-box">
				<view class="wrap_circle">批次:{{cldata.pici}}</view>
				<view class="wrap_circle">材料类型:{{cldata.cailiaoname}}</view>
				<view class="wrap_circle">规格类型:{{cldata.guigexinghao}}</view>
				<view class="wrap_circle">计量单位:{{cldata.danwei_dictText}}</view>
				<view class="wrap_circle">试验编号:{{cldata.pici}}</view>
				<view class="wrap_circle">料仓状态:{{cldata.liaocangStatus_dictText}}</view>
				<view class="wrap_circle">生产厂家:{{cldata.pici}}</view>
				<view class="wrap_circle">进场日期:{{cldata.jinchangTime}}</view>
				<view class="wrap_circle">检验日期:{{cldata.pici}}</view>
				<view class="wrap_circle">当前库存:{{cldata.kucun}}</view>
			</view>
		</u-modal>
		<u-modal :show="showt" showCancelButton @confirm="confirm" @cancel="cancel">
			<view class="modal-box">
				<view class="wrap_circle">批次:{{cldata.pici}}</view>
				<view class="wrap_circle">材料类型:{{cldata.cailiaoname}}</view>
				<view class="wrap_circle">规格类型:{{cldata.guigexinghao}}</view>
				<view class="wrap_circle">计量单位:{{cldata.danwei_dictText}}</view>
				<view class="wrap_circle">试验编号:{{cldata.pici}}</view>
				<view class="wrap_circle">料仓状态:{{cldata.liaocangStatus_dictText}}</view>
				<view class="wrap_circle">生产厂家:{{cldata.pici}}</view>
				<view class="wrap_circle">进场日期:{{cldata.jinchangTime}}</view>
				<view class="wrap_circle">检验日期:{{cldata.pici}}</view>
				<view class="wrap_circle">当前库存:{{cldata.kucun}}</view>
			</view>
		</u-modal>
	</view>
</template>

<script>
	import org from '../../component/organization/organization.vue'
	export default {
		components: {
			org,
		},
		data() {
			return {
				show: false,
				showt: false,
				showzj: false,
				TabCur: 0,
				TabCur1: 0,
				// img1: '../../../static/rawmaterial/y1.png',
				// img2: '../../../static/rawmaterial/y2.png',
				sys_depart_name: '',
				sys_depart_orgcode: '',
				shebeiNo: '',
				list1: [],
				list2: [],
				listData: [],
				listCroup:[],
				buttonList:[],
				numMax:0,
				cldata: {},
				orgcode: this.$store.getters.orgcode,
			}
		},
		created() {
			this.langc()
		},
		methods: {
			Chooseorg(choosevalue, choosekey) {
				// 组织机构
				// console.log(choosevalue, choosekey)
				this.sys_depart_name = choosekey
				this.sys_depart_orgcode = choosevalue
				this.shebeiNo = null
				// console.log(this.$store, '组织机构')

			},
			langc() {
				this.list1 = []
				this.list2 = []
				let params = {
					sys_depart_orgcode: this.sys_depart_orgcode ? this.sys_depart_orgcode : this.orgcode,
					pageSize: 80,
				}
				let lcdata = []
				this.listData = []
				this.$http.get(`/wzliaocang/wzliaocang/list`, {
					params
				}).then(res => {
					// console.log(res.data.result.records, '料仓信息')
					lcdata = res.data.result.records;
					this.listData  = lcdata;
					lcdata.forEach(item => {
						if (item.name.indexOf('1-') !== -1 || item.name.indexOf('2-') !== -1 || item.name.indexOf('3-') !== -1|| item.name.indexOf('4-') !== -1) {
							this.list1.push(item)
							// console.log(this.list1, item)
						} else if (item.name.indexOf('仓') !== -1) {
							this.list2.push(item)
							// console.log(this.list2, item)
						}
					})
					this.listCroup = [];
					let buttonLen = 0;
					this.list1.forEach(item => {
						if (item.name.indexOf('1-') !== -1 ) {
							this.listCroup.push(item)
						}
					})
					
					lcdata.forEach(item => {
						if (item.name.indexOf('1-') !== -1  )buttonLen =1;
						if (item.name.indexOf('2-') !== -1 )buttonLen =2;
						if (item.name.indexOf('3-') !== -1 )buttonLen =3;
						if (item.name.indexOf('4-') !== -1 )buttonLen =4;
						if(buttonLen>this.numMax){
							this.numMax = buttonLen;
						}
					})
					this.buttonList = [];
					for (let i = 0; i < this.numMax; i++) {
						this.buttonList.push({name:`${i+1}#组`,status:0,mark:`${i+1}-`})
					}
					this.buttonList[0].status = 1;
					console.log(this.list1 ,'this.list1 ---------------');
					console.log(this.listCroup,this.buttonList,buttonLen, "listCroup---------------")
					/* lcdata=res.data.result.records
					lcdata.forEach(item=>{
						console.log(item,'dddd')
						if(item.cailiaoname = '')
					}) */
				})
			},
			changeBtn(e){
				this.listCroup = [];
				this.buttonList.forEach(i => {
					if(i.name == e.name){
						i.status = 1;
					}else{
						i.status = 0;
					}
				})
				this.list1.forEach(item => {
					if (item.name.indexOf(e.mark) !== -1 ) {
						this.listCroup.push(item)
					}
				})
			},
			screen() {
				this.showzj = true
			},
			cldetails(e) {
				console.log(e)
				this.cldata = e
				this.show = true
			},
			cldtails2(e) {
				console.log(e)
				this.cldata = e
				this.showt = true
			},
			tabSelect(e) {
				// console.log(e)
				this.TabCur = e.currentTarget.dataset.id;
				if (this.TabCur == 1) {
					// this.listDatas();
				}
				this.scrollLeft = (e.currentTarget.dataset.id - 1) * 60
			},
			// tabSelect1(e) {
			// 	// console.log(e)
			// 	this.TabCur1 = e.currentTarget.dataset.id;
			// 	if (this.TabCur1 == 1) {
			// 		// this.listDatas();
			// 	}
			// 	this.scrollLeft = (e.currentTarget.dataset.id - 1) * 60
			// },
			handleconfirm() {
				this.show = false

			},
			handlecancel() {
				this.show = false
			},
			confirm() {
				this.showt = false

			},
			cancel() {
				this.showt = false
			},
			zjconfirm() {
				this.showzj = false
				this.langc()
			},
			zjcancel() {
				this.showzj = false
			},
		}
	}
</script>

<style lang="scss" scoped>
	.wrap {
		width: 100%;
		height: auto;
		background-color: #F2F5FE;

		// .sections {
		// 	width: 700upx;
		// 	height: 300upx;
		// 	margin: 0 auto;
		// 	border-radius: 10px;
		// 	background-color: #fff;
		.sections {
			width: 700upx;
			height: 300upx;
			margin: 10px auto;
			border-radius: 10px;
			background-color: #fff;
			/* width: 100%; */
			// background-color: white;
			height: auto;
			/* margin: 10px 5px 0px 5px; */
			/* border-radius: 5px; */
			color: #909399;
		}

		.sections uni-view {
			// padding: 5px 0px 0px 10px;
			position: relative;
			// right: 3px;
		}

		.sections span {
			padding-left: 10px;
			color: #606266;
		}

		.sections .box1 {
			display: flex;
			flex-wrap: wrap;
			align-items: center;
			justify-content: center;
		}

		.sections .box1 .box1-title {
			padding: 0;
			flex: 2;
		}

		.sections .box1 .box1-content {
			padding: 0;
			flex: 7;
		}


		.sections .box1 .box1-content uni-view {
			padding: 2px 0 0 2px;
		}

		// }
		.option {
			width: 680upx;
			height: auto;
			// border: 1px solid forestgreen;
			// background: hotpink;
			display: flex;
			flex-direction: row;
			margin: 0 auto;
			flex-wrap: wrap;
			justify-content: center;

			&-box {
				width: 167upx;
				height: 400upx;

				// border: 1px solid deepskyblue;
				.u-line-progress {
					position: relative;
					transform: rotate(270deg);
					top: 60px;
					right: 36px;
					width: 150px;
				}
			}

			&-title {
				text-align: center;
				color: #000;
			}

			&-pro {
				height: 305upx;
				// border: 1px solid green;
			}

			&-titlewe {
				text-align: center;
				font-size: 10px;
				color: #000;
			}

			&-view{
				width: 100%;
				display: flex;
				flex-direction: row;
				flex-wrap:wrap;
				justify-content: space-between;
				// background-color: rgb(143, 53, 53);
			}
			&-button{
				width: 45%;
				margin: 10upx;
			}
			.btn-b{
				background: #0f63ff;
				color: #ffffff;
			}
			.btn-w{
				background: #e2e3e4;
				color: #9d9d9d;
			}
		}

		.modal {
			// width:200upx;
			height: 300upx;

			// border: 1px solid deeppink;
			.title {
				line-height: 40px;
			}
		}

		.modal-box {
			width: 500upx;
			padding: 10px 0;
			height: auto;
			line-height: 30px;

			.wrap_circle {
				width: 500upx;
				margin: 0 auto;
			}
		}
	}
</style>
