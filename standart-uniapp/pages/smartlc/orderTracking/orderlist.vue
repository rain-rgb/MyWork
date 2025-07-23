<template>
	<view class="wrap">
		<cu-custom bgColor="bg-white" :isBack="true">
			<block slot="content">订单详情</block>
		</cu-custom>
		<view class="top-two">
			<view class="left">
				<view class="left-text">
					<p class="blue">{{dcj}}</p>
					梁数量
				</view>
			</view>
			<view class="left">
				<view class="left-text">
					<p class="red">{{Completed}}</p>
					未完成
				</view>
			</view>
			<view class="left">
				<view class="left-text">
					<p class="green">{{incomplete}}</p>
					已完成
				</view>
			</view>
			<view class="left" @click="dcjl(dcj-Allcount)">
				<view class="left-text">
					<p class="blue">{{dcj-Allcount}}</p>
					待创建
				</view>
			</view>
			<view class="left">
				<view class="left-text">
					<p class="red">{{Allcount}}</p>
					已创建
				</view>
			</view>
			<view class="left">
				<view class="left-text">
					<p class="green">{{0}}</p>
					其他
				</view>
			</view>
		</view>
		<!-- 梁片信息 -->
		<view class="section">
			<view class="section-text">
				<view class="title">
					<view class="dot">
						<view class="round"></view>
					</view>
					<view class="font-title">
						<view class="title-font">梁片信息</view>
					</view>
				</view>
				<view class="boxder">
					<view class="section-model" v-for="(zlitem,index) in datalist" :key="index">
						<view class="model-img">
							<!-- <view  style="item.jianyanstate ==0?'background-image:url('../../static/shiti/bhgy.png'); width: 45px;height: 45px;background-size: 100% 100%;':item.jianyanstate ==1?'background-image: url('../../static/shiti/yihg.png'); width: 45px;height: 45px;background-size: 100% 100%;':item.jianyanstate ==2?'background-image:url('../../static/shiti/yihg.png') ; width: 45px;height: 45px;background-size: 100% 100%;':''">
							    <image style="width: 45px;height: 45px;background-size: 100% 100%;" :src="item.jianyanstate == 0 ? img: item.jianyanstate == 1?imgtwo: item.jianyanstate ==2?imgthree:''" mode=""></image>
							</view> -->
							<view>
								<image style="width: 50px;height: 63px;background-size: 100% 100%;" :src="src" mode="">

								</image>
							</view>
						</view>
						<view class="model-text">
							<view class="tex-all">
								<view class="text-top">
									{{zlitem.code !==''?zlitem.code:"暂无数据"}}
								</view>
								<view class="text-bottom">
									{{zlitem.conspos !==''?zlitem.conspos:'暂无数据'}}
								</view>
								<view class="text-bottom" v-if="!zlitem.status ==1" @click="check(zlitem.id)"
									:style="zlitem.status ==0?'color:red;font-weight:bold;font-family:cursive':zlitem.status ==1?'color:green;font-weight:bold;font-family:cursive':'color:blue;font-weight:bold;font-family:cursive'">
									{{zlitem.status ==0?'未审核':zlitem.status ==1?'已审核':'暂无数据'}}
								</view>
								<view class="text-bottom" @click="clickstatus(zlitem.id)"
									:style="zlitem.fengduanstatus ==0?'color:red;font-weight:bold;font-family:cursive':'color:blue;font-weight:bold;font-family:cursive'">
									{{zlitem.fengduanstatus ==0?'未绑定电子二维码':''}}
								</view>
							</view>
						</view>
						<view style="padding:5px 0;">
							<u-icon name="arrow-right" label="详情" labelPos="right" labelColor="blue"
								@click="clickJump(zlitem)" color="#c0c0c1" size="18">
							</u-icon>
						</view>
					</view>
				</view>
				<u-modal :show="show" closeOnClickOverlay @close="() => show = false" showConfirmButton showCancelButton
					@cancel="cancel" @confirm="confirm" :title="title" cancelColor="#000">
					<view class="radio">
						<view class="result">
							<view style="padding: 30upx 10upx;font-size: 28upx;">
								审批结果：
							</view>

							<view style="margin: -70upx 120upx;">
								<u-radio-group v-model="value1" placement="row">
									<u-radio :customStyle="{marginLeft: '15px'}" v-for="(item, index) in radiolist1"
										:key="index" :label="item.name" :name="item.status" @change="radioChange">
									</u-radio>
								</u-radio-group>
							</view>
						</view>
						<view class="opinion">
							<view style="padding: 30upx 10upx;font-size: 28upx;">
								审批意见：
							</view>
							<view style="margin:-85upx 150upx;width:430upx;">
								<u--input placeholder="请输入审批意见" border="surround" v-model="opinion" @change="change1">
								</u--input>
							</view>
						</view>
					</view>
				</u-modal>
			</view>
		</view>
		<!-- 梁信息 -->
		<view class="sections" v-if="iSshow">
			<view class="zdmb">
				<u-collapse @change="change" @close="close" @open="open">
					<u-collapse-item title="梁片信息追踪" name="Docs guide">
						<view class="tables">
							<view class="sectionwrap">
								<view class="section-all">
									<view class="section-text">
										<view class="title">
											<!-- <view class="round"></view> -->
											<view class="title-font">梁片信息追踪</view>
										</view>
										<view>工程名称:<span>{{zldata.projname}}</span></view>
										<view>任务单编号:<span>{{zldata.code}}</span></view>
										<view>台座:<span>{{zldata.taizuono}}</span></view>
										<view>创建时间:<span>{{zldata.dattim}}</span></view>
										<view>施工部位:<span>{{zldata.conspos}}</span></view>
									</view>
								</view>
								<view class=" section-all">
									<view class="title">
										<!-- <view class="round"></view> -->
										<view class="title-fonts">制梁任务单工序</view>
									</view>
									<view class="tablelist">
										<view class="process">
											<uni-steps :options="list2" active-color="#387FFD" :active="active"
												direction="column">
											</uni-steps>
										</view>
									</view>

								</view>
							</view>
						</view>
					</u-collapse-item>
				</u-collapse>
			</view>
			<!-- 浇筑令信息追踪 -->
			<view class="zdmb">
				<u-collapse @change="change" @close="close" @open="open">
					<u-collapse-item title="浇筑令信息追踪" name="Docs guide">
						<view class="tables" v-if="jzlshow">
							<view class="sectionwrap">
								<view class="main">
									<view class="main-item">
										<!-- <view class="biaoqian"></view> -->
										<view class="mainnew">浇筑令信息追踪</view>
									</view>
									<view class="main-item">
										<view class="main-item-name">
											组织机构：<span>{{listdata.sysOrgCode_dictText}}</span>
										</view>
									</view>
									<view class="main-item">
										<view class="main-item-name">
											开盘日期：<span>{{listdata.begtim}}</span>
										</view>
									</view>
									<view class="main-item">
										<view class="main-item-name">
											任务编号：<span>{{jzllistdata.rwdcode}}</span>
										</view>
									</view>
									<view class="main-item">
										<view class="main-item-name">
											工程名称：<span>{{listdata.projname}}</span>
										</view>
									</view>
									<view class="main-item">
										<view class="main-item-name">
											坍落度(mm)/扩展度(mm)：<span>{{listdata.lands}}</span>
										</view>
									</view>
									<view class="main-item">
										<view class="main-item-name">
											强度等级：<span>{{listdata.betlev}}</span>
										</view>
									</view>
									<view class="main-item">
										<view class="main-item-name">
											浇筑方式：<span>{{listdata.pour}}</span>
										</view>
									</view>
									<view class="main-item">
										<view class="main-item-name">
											任务方量：<span>{{listdata.mete}}</span>
										</view>
									</view>
									<view class="main-item">
										<view class="main-item-name">
											浇筑部位：<span>{{listdata.conspos}}</span>
										</view>
									</view>
									<view class="main-item"></view>
								</view>
								<view class="progress">
									<u-steps direction="column">
										<view v-for="(item,index) in stelist" :key="index">
											<u-steps-item :title="item.tile">
												<view
													style="height: 50upx;font-size: 26upx;color: #9299A8;padding: 10upx 20upx;"
													slot="desc">
													{{item.content}}
												</view>
												<u-icon :name="item.name" slot="icon" @click="changeicon(item)"
													size="22">
												</u-icon>
											</u-steps-item>
										</view>
									</u-steps>
								</view>
							</view>
						</view>
					</u-collapse-item>
				</u-collapse>

				<u-modal :show="shshow" style="position: fixed;background-color: #FFFFFF;" showCancelButton
					@cancel="shcancel" @confirm="shconfirm">
					<view>
						<view>
							<view style="padding: 20upx;">
								<view style="padding: 20upx;font-size: 18px;">
									审批结果：
								</view>
								<view
									style="padding-top: 6%;background-color: #F6F9FC;width: 100%;height: 40%;padding-bottom: 6%;border-radius: 14rpx;">
									<u-radio-group v-model="radiovalue1" placement="row">
										<u-radio :customStyle="{marginLeft: '15px'}" v-for="(item, index) in radiolist1"
											:key="index" :label="item.name" :name="item.status" @change="radioChange">
										</u-radio>
									</u-radio-group>
								</view>
							</view>
							<view style="padding: 20upx;">
								<view style="padding: 20upx;font-size: 18px;">
									审批意见：
								</view>
								<view style="margin-top: 20rpx;background-color: #F6F9FC;border-radius: 14rpx;">
									<u--input placeholder="请输入审批意见" border="surround" v-model="note"></u--input>
								</view>
							</view>
						</view>
					</view>
				</u-modal>
			</view>
		</view>
		<view v-show="jzladd" class="new-button">
			<u-button type="primary" style="width: 30%;border-radius: 5px;" text="下发浇筑令" @click="addjzl"></u-button>
			<u-button type="primary" style="width: 30%;border-radius: 5px;" text="新增张拉任务单" @click="addzl"></u-button>
			<u-button type="primary" style="width: 30%;border-radius: 5px;" text="新增压浆任务单" @click="addyj"></u-button>
		</view>

		<!-- 浇筑令信息 -->
		<!-- <view class="section">
			<view class="section-text">
				<view class="title">
					<view class="dot">
						<view class="round"></view>
					</view>
					<view class="font-title">
						<view class="title-font">浇筑令信息</view>
					</view>
				</view>
				<view class="boxder">
					<view class="section-model" v-for="(jzlitem,index) in listdata" :key="index">
						<view class="model-img">
							<view
								style="item.jianyanstate ==0?'background-image:url('../../static/shiti/bhgy.png'); width: 45px;height: 45px;background-size: 100% 100%;':item.jianyanstate ==1?'background-image: url('../../static/shiti/yihg.png'); width: 45px;height: 45px;background-size: 100% 100%;':item.jianyanstate ==2?'background-image:url('../../static/shiti/yihg.png') ; width: 45px;height: 45px;background-size: 100% 100%;':''">
								<image style="width: 45px;height: 45px;background-size: 100% 100%;"
									:src="item.jianyanstate == 0 ? img: item.jianyanstate == 1?imgtwo: item.jianyanstate ==2?imgthree:''"
									mode=""></image>
							</view>
							<view>
								<image style="width: 50px;height: 63px;background-size: 100% 100%;" :src="src" mode="">

								</image>
							</view>
						</view>
						<view class="model-text">
							<view class="tex-all">
								<view class="text-top">
									{{jzlitem.rwdcode !==''?jzlitem.rwdcode:"暂无数据"}}
								</view>
								<view class="text-bottom">
									{{jzlitem.conspos !==''?jzlitem.conspos:'暂无数据'}}
								</view>
								<view class="text-bottom" v-if="!jzlitem.jzlsts ==1"
									@click="checks(jzlstatus,jzlitem.id)"
									:style="jzlitem.jzlsts ==0?'color:red;font-weight:bold;font-family:cursive':jzlitem.jzlsts ==1?'color:green;font-weight:bold;font-family:cursive':'color:blue;font-weight:bold;font-family:cursive'">
									{{jzlitem.jzlsts ==0?'未审核':jzlitem.jzlsts ==1?'已审核':'暂无数据'}}
								</view>
							</view>
						</view>
						<u-icon name="arrow-right" color="#c0c0c1" size="18" @click="clickjzl(jzlitem)">
						</u-icon>
					</view>
				</view>
			</view> -->
	<!-- <view class="increase-img" @click="increase">

		</view> -->
	<!-- 制梁任务单下的详情模态框数据 -->

	<!-- 浇筑令的详情模态框数据 -->

	<!-- 新增制梁浇筑令按钮 -->
	<!-- <view class="add" :class="[popshows?'fa plus-circle go':'fa plus-circle aa']" @click="popshow">
			<u-icon name="plus-circle" color="#2979ff" size="50"></u-icon>
		</view>
		<view class="popconfirm" v-if="popshows">
			<view class="popconfirm-item" @click="addjzl">
				<image class="popconfirm-item-img" src="../../../static/smartsy/sampling.png" mode=""></image>
				<p>下发浇筑令</p>
			</view>
			<view class="popconfirm-item" @click="addzl">
				<image class="popconfirm-item-img" src="../../../static/smartsy/save.png" mode=""></image>
				<p>下发制梁任务单</p>
			</view>
		</view> -->

	</view>
</template>

<script>
	import smartbhapi from '../../../api/smartbh.js'
	export default {
		name: 'orderlist',
		data() {
			return {
				value1: 1,
				ids: '',
				opinion: '', //
				radiolist1: [{
						name: '同意',
						status: 1,
						disabled: false
					},
					{
						name: '不同意',
						status: 0,
						disabled: false
					}
				],
				items: '',
				src: '../../../static/label/lxlx.png',
				src1: '../../../static/label/wbsfj.png',
				src2: '../../../static/label/wztz.png',
				datalist: [],
				listdata: [],
				show: false,
				jzlshow: false,
				title: '梁信息审核',
				list2: [],
				active: -1,
				gongxuzd: [],
				rwdgongxu: [],
				zldata: {},
				popshows: false,
				stelist: [],
				jzldata: '',
				zlstatus: 0,
				jzlstatus: 1,
				zlrstat: '',
				ord: '',
				cuid: '',
				Allcount: '',
				Completed: '',
				incomplete: '',
				dcj: '',
				iSshow: false,
				jzladd: false,
				codeor: '',
				sgbu: '',
				sgbuorder: '',
				jzllistdata: '',
				sign: 1.,
				datastatus: [],
				shshow: false,
				note: '',
				radiolist1: [{
						name: '同意',
						status: 1,
						disabled: false
					},
					{
						name: '不同意',
						status: 0,
						disabled: false
					}
				],
				radiovalue1: 1,
				nameid: '',
				statusid: '',
				yzcode: ''
			}
		},
		onLoad(options) {
			this.items = JSON.parse(options.item)
			console.log(this.items, 'dddd')
			this.dcj = this.items.beamNum
			this.ord = this.items.orderno
			this.cuid = this.items.constructionSiteid
		},
		onShow() {
			this.lianglist()
			// this.getloadlist()
			this.Statistics()
			// this.getstedata()
		},
		methods: {
			// 制梁任务单数据
			lianglist() {
				let params = {
					oderno: this.items.orderno,
					sys_depart_orgcode: this.$store.getters.orgcode
				}
				this.$http.get(`/zhiliangrenwudan/zhiliangrenwudan/applist`, {
					params
				}).then(res => {
					// let zldatalist = res.data.result.records
					console.log(res, '梁信息')
					this.datalist = res.data.result.records
					this.datalist.forEach(e => {
						this.datastatus = e
						// console.log(this.datastatus.id, 'dddd')
					})

					this.Allcount = res.data.result.total
				})
			},
			// 待创建
			dcjl(e) {
				console.log(e)
				if (e !== 0) {
					uni.navigateTo({
						url: '/pages/smartlc/orderTracking/orderadd?items=' + JSON.stringify(this.items)
					})
				} else {
					uni.showToast({
						title: '任务单创建完成',
						icon: 'success'
					})
				}
				// if (e !== 0) {
				// 	var that = this;
				// 	that.nameid = ''
				// 	// 允许从相机和相册扫码
				// 	uni.scanCode({
				// 		success: (res) => {
				// 			if (res.scanType == 'QR_CODE') {
				// 				that.nameid = res.result;
				// 				this.$http.put(`/zhiliangrenwudan/zhiliangrenwudan/qredit`, {
				// 					qcode: that.nameid
				// 				}).then(res => {
				// 					console.log(res, '新建扫码')
				// 					if (res.data.code == 200) {
				// 						uni.showToast({
				// 							title: res.data.message,
				// 							icon: 'loading'
				// 						})
				// 						setTimeout(() => {
				// 							uni.navigateTo({
				// 								url: '/pages/smartlc/orderTracking/orderadd?items=' +
				// 									JSON.stringify(this.items) +
				// 									"&uuid=" + that.nameid
				// 							})
				// 						}, 300)

				// 					} else {
				// 						uni.showToast({
				// 							title: res.data.message,
				// 							icon: 'loading'
				// 						})
				// 					}
				// 				})
				// 			} else {
				// 				uni.showToast({
				// 					title: "请重新扫描二维码",
				// 					icon: 'none'
				// 				})
				// 			}
				// 			console.log("条码类型：" + res.scanType);
				// 			console.log("条码内容：" + res.result);
				// 		}
				// 	});

				// } else {
				// 	uni.showToast({
				// 		title: '任务单创建完成',
				// 		icon: 'success'
				// 	})
				// }
				// uni.navigateTo({
				// 	url: '/pages/smartlc/orderTracking/orderadd'
				// })
			},
			// 浇筑令新增信息
			getloadlist() {
				console.log(this.zlrstat, '制梁任务单审核按钮')
				let params = {
					orderno: this.codeor,
				}
				this.$http.get(`/bhzrwdxx/bhzrwdxx/list`, {
					params
				}).then(res => {
					this.listdata = []
					this.listdata = res.data.result.records[0]
					console.log(this.listdata)
					if (this.listdata) {
						this.jzlshow = true
						// this.jzldata = e
						// this.getloadlist()
						this.getstedata()
					} else {
						console.log('浇筑令新增按钮')
						this.jzladd = true
						this.jzlshow = false
						// if (this.zlrstat == 0) {
						// 	uni.showToast({
						// 		title: '请先去审核',
						// 		icon: 'none'
						// 	})
						// 	this.jzladd = false
						// }
					}
					console.log(this.listdata)
					// this.listdata = jzl
				})
			},
			// getloadlist() {
			// 	let params = {
			// 		orderno: this.codeor,
			// 	}
			// 	this.$http.get(`/bhzrwdxx/bhzrwdxx/list`, {
			// 		params
			// 	}).then(res => {
			// 		this.listdata = []
			// 		this.listdata = res.data.result.records[0]
			// 		console.log(this.listdata)
			// 		// this.listdata = jzl
			// 	})
			// },
			getstedata() {
				this.stelist = []
				// console.log(this.jzllist.rwdcode)
				smartbhapi.bhzrwdSte({
					params: {
						rwdcode: this.listdata.rwdcode
					},
				}).then(res => {
					//console.log(res, "任务单proste")
					let data = res.data.result
					if (data.length > 0) {
						for (let i = 0; i < data.length; i++) {
							if (data[i].tile == '创建') {
								if (data[i].status == 0) {
									this.stelist.push({
										tile: data[i].tile,
										content: '',
										name: '../../../static/pour/creates2.png',
										status: data[i].status
									})
								} else {
									this.stelist.push({
										tile: data[i].tile,
										content: '创建人' + data[i].person + ',创建时间' + data[i].time,
										name: '../../../static/pour/creates1.png',
										status: data[i].status
									})
								}
							} else if (data[i].tile == '审核') {
								if (data[i].status == 0) {
									this.stelist.push({
										tile: data[i].tile,
										content: '',
										name: '../../../static/pour/audit2.png',
										status: data[i].status
									})
								} else {
									this.stelist.push({
										tile: data[i].tile,
										content: data[i].content,
										name: '../../../static/pour/audit1.png',
										status: data[i].status
									})
								}
							} else if (data[i].tile == '配料') {
								if (data[i].status == 0) {
									this.stelist.push({
										tile: data[i].tile,
										content: '',
										name: '../../../static/pour/Ingredients2.png',
										status: data[i].status
									})
								} else {
									this.stelist.push({
										tile: data[i].tile,
										content: '配料人' + data[i].person + ',配料时间' + data[i].time,
										name: '../../../static/pour/Ingredients1.png',
										status: data[i].status
									})
								}
							} else if (data[i].tile == '生产中' || data[i].tile == '生产已滞后') {
								if (data[i].status == 0) {
									this.stelist.push({
										tile: data[i].tile,
										content: '',
										name: '../../../static/pour/production2.png',
										status: data[i].status
									})
								} else {
									this.stelist.push({
										tile: data[i].tile,
										content: data[i].person + '正在生产中,开始时间' + data[i].time,
										name: '../../../static/pour/production1.png',
										status: data[i].status
									})
								}
							} else {
								if (data[i].status == 0) {
									this.stelist.push({
										tile: data[i].tile,
										content: '',
										name: '../../../static/pour/finish2.png',
										status: data[i].status
									})
								} else {
									this.stelist.push({
										tile: data[i].tile,
										content: '您的工程已完成，完成度100%,完成时间' + data[i].time,
										name: '../../../static/pour/finish1.png',
										status: data[i].status
									})
								}
							}
						}
					}

				}).catch(e => {
					console.log("请求错误", e)
				})
			},
			//工序字典值
			getGXZD(data) {
				console.log(data)
				this.$http.get('/sys/dict/getDictItems/xuhao').then(res => {
					if (res.data.success) {
						console.log(res.data.result)
						this.gongxuzd = []
						let gxzdshuju = res.data.result
						let xuhao = []
						for (let i = 0; i < data.length; i++) {
							console.log(data[i])
							for (let j = 0; j < gxzdshuju.length; j++) {
								console.log(gxzdshuju[j])
								if (data[i] == gxzdshuju[j].value) {
									this.gongxuzd.push(gxzdshuju[j])
									console.log(this.gongxuzd)
								}
							}
						}
						// console.log(this.gongxuzd,'gongxuzd============')
						this.list2 = this.gongxuzd.map((item, index) => {
							console.log(this.gongxuzd, 'kkkkk')
							return {
								...item,
								...this.rwdgongxu[index]
							}
						})

						let data5 = this.list2.filter(function(item) {
							console.log(item)
							return item.status === 2
						})
						let data6 = this.list2.filter(function(item) {
							console.log(item)
							return item.status === 1
						})
						if (data6.length != 0) {
							this.active = data5.length
						} else {
							this.active = data5.length - 1
						}

					}
				})
			},
			// 制梁任务单数据
			liangnews() {
				let params = {
					id: this.zldata.uuid
				}
				let gxxh = []
				this.$http.get('/zhiliangrenwudan/zhiliangrenwudan/queryZhiliangGongxuByMainId', {
					params
				}).then(res => {
					console.log(res, 'gongxuid')
					res.data.result.forEach(e => {
						if (e.xuhao === 7) {
							let obj = {
								status: e.status,
								desc: `
												存梁时间:${e.cuntime || '--' }
												责任人:${e.responsible || '--'}
												梁所存层:${e.liangceng || '--'}
												存梁行:${e.cunlianghang || '--'}
												存梁列:${e.cunlianglie || '--'}
											 `
							}
							this.rwdgongxu.push(obj)
							console.log(this.rwdgongxu, '工序')
						} else {
							let obj = {
								status: e.status,
								desc: `
												 时间:${e.finishtime || '--'}
												 责任人:${e.responsible || '--'}
											`
							}
							this.rwdgongxu.push(obj)

							console.log(this.rwdgongxu, '工序2')
						}
						gxxh.push(e.xuhao.toLocaleString())

					})
					console.log(gxxh, '========================')
					this.getGXZD(gxxh)

				})
			},
			// 点击制梁信息审核弹框
			check(e) {
				this.ids = e
				this.show = true
				// console.log(e, id, '质量任务单')
				// uni.navigateTo({
				// 	url: '/pages/smartlc/orderTracking/Zlcheck?item=' + e + "&id=" + id + "&ddcode=" + this.ord
				// })
			},
			getSign(num) { //写一个函数
				return num % 2 == 0 //返回值为 参数模上2余数为0
			},
			// 制梁任务单详情弹框
			clickJump(e) {
				this.sign++
				let newSign = this.getSign(this.sign)
				console.log('获取点击结果', newSign)
				if (newSign) {
					this.codeor = e.code
					this.sgbu = e.conspos
					this.sgbuorder = e.projgrade
					this.getloadlist()
					this.zldata = e
					this.liangnews()
				}else{
					this.jzladd = false;
				}
				this.iSshow = !this.iSshow
			},
			//下发浇筑令
			addjzl() {
				// console.log(this.ord, this.cuid)
				console.log(this.datastatus.status)
				if (this.datastatus.status !== 0) {
					uni.navigateTo({
						url: "/pages/smartbh/pourorder/pourAdd?orderno=" + this.codeor + "&sgbuorder=" + this
							.projgrade + "&sgbu=" + this.sgbu + "&ddcode=" + this.ord
					})
				} else {
					uni.showToast({
						title: '请先去审核,再下发浇筑令',
						icon: 'none'
					})
				}
			},
			//张拉任务单
			addzl() {
				if (this.datastatus.status !== 0) {
					uni.navigateTo({
						url: "/pages/smartlc/orderTracking/zlTaskList"
					})
				} else {
					uni.showToast({
						title: '请先去审核,再下发张拉任务单',
						icon: 'none'
					})
				}
			},
			//压浆任务单
			addyj() {
				// if (this.datastatus.status !== 0) {
				// 	uni.navigateTo({
				// 		url: "/pages/smartlc/orderTracking/yjTaskList"
				// 	})
				// } else {
				// 	uni.showToast({
				// 		title: '请先去审核,再下发压浆任务单',
				// 		icon: 'none'
				// 	})
				// }
			},
			popshow() {
				this.popshows = !this.popshows
				console.log(this.popshows)
			},
			async Statistics() {
				await this.$http.get(`/zhiliangrenwudan/zhiliangrenwudan/list11`, {
					params: {
						xuhao: 7,
						status: 1,
						sys_depart_orgcode: this.$store.getters.orgcode
					}
				}).then(res => {
					// console.log(res.data.result.flag, '已完成')
					this.Completed = res.data.result.flag
				}).catch(err => {
					console.log(err)
				})
				await this.$http.get(`/zhiliangrenwudan/zhiliangrenwudan/list11`, {
					params: {
						xuhao: 7,
						status: 0,
						sys_depart_orgcode: this.$store.getters.orgcode
					}
				}).then(res => {
					// console.log(res, '未完成')
					this.incomplete = res.data.result.flag
				}).catch(err => {
					console.log(err)
				})
			},
			radioChange(n) {
				console.log('radioChange', n);
				this.value = n
			},
			// 审批意见
			change1(e) {
				console.log(e, '审批意见')
				this.opinion = e
			},
			//审核
			confirm() {
				let params = {
					status: this.value1,
					note: this.opinion,
					id: this.ids
				}
				this.$http.put(`/zhiliangrenwudan/zhiliangrenwudan/edit`, params).then(res => {
					console.log(res, '审核')
					if (res.data.success == true) {
						uni.showToast({
							title: "已完成审核",
							icon: 'success'
						})
						this.lianglist()
						setTimeout(() => {
							this.show = false
						}, 500)
					}
				})
			},
			cancel() {
				this.show = false
			},
			//折叠面板 方法
			open(e) {
				// console.log('open', e)
			},
			close(e) {
				// console.log('close', e)
			},
			change(e) {
				// console.log(e)
			},
			//浇筑令审核
			radioChange(n) {
				this.radiovalue1 = n
				//console.log('radioChange', n);
			},
			shcancel() {
				this.shshow = false
			},
			shconfirm() {
				let params = {
					id: this.listdata.id,
					status: this.radiovalue1,
					note: this.note
				}
				smartbhapi.bhzrwdedit(
					params
				).then(res => {
					if (res.data.success) {
						uni.showToast({
							title: "审核成功",
							icon: 'none'
						})
						this.getstedata()
						setTimeout(() => {
							this.shshow = false
						}, 500);
					}
				})
			},
			delconfirm() {
				this.$http.delete('/system/bhzrenwudan/delete?id=' + this.listdata.id + '').then(res => {
					if (res.data.success) {
						uni.showToast({
							title: "删除成功",
							icon: 'none'
						})
						setTimeout(() => {
							this.delshow = false
							uni.navigateTo({
								url: '/pages/smartbh/pourorder/pourManage'
							});
						}, 500);
					}
				})
			},
			changeicon(e) {
				console.log("e", e)
				if (e.tile == "审核" && e.status == 0) {
					this.shshow = true
				}
				if (e.tile == "配料" && this.listdata.jzlsts > 0 && this.listdata.jzlsts < 5) {
					uni.navigateTo({
						url: '/pages/smartsy/shigongphb/shigongphbAdd?code=' + this.listdata.rwdcode
					})
				}
			},
			// 质量任务单未验证已验证
			scanCodestatus(k) {
				// console.log(k,'llllll')
				this.yzcode = k
				let params = {
					id: this.yzcode,
					qcode: this.statusid
				}
				this.$http.put(`/zhiliangrenwudan/zhiliangrenwudan/edit`, params).then(res => {
					console.log(res, 'dddd')
					if (res.data.code == 200) {
						this.$tip.success('验证成功')
						this.lianglist()
					} else {
						this.$tip.error(res.data.message)
					}
					uni.showToast({
						title: res.data.message,
						icon: 'loading'
					})
				})
			},
			clickstatus(e) {
				if (this.datastatus.status !== 0) {
					let idcode = e
					var that = this;
					// that.nameid = ''
					that.statusid = ''
					// 允许从相机和相册扫码
					uni.scanCode({
						success: (res) => {
							// console.log("条码类型：" + res.scanType);
							// console.log("条码内容：" + res.result);
							let sub = res.result.slice(res.result.indexOf('=') + 1)
							// console.log(sub, '截取后字符串长度')
							if (res.scanType == 'QR_CODE') {
								that.statusid = sub;
								// console.log(that.statusid,'jjj')
								// setTimeout(() => {
								// 	uni.showToast({
								// 		title: "正在加载中，请稍侯",
								// 		icon: 'loading'
								// 	})
								// }, 1000)
							}
							this.scanCodestatus(idcode)

						}
					});
				} else {
					uni.showToast({
						title: '请先去审核,再绑定电子二维码',
						icon: 'none'
					})
				}


			}
		}
	}
</script>

<style lang="less" scoped>
	.wrap {
		width: 100%;
		height: auto;
		background-color: #F2F5FE;

		.top-two {
			width: 700upx;
			height: 350upx;
			margin: 0 auto;
			// margin-top: 80upx;
			// border-radius: 16upx;
			// border:1px solid #1785FF;
			display: flex;
			justify-content: space-between;
			flex-direction: row;
			flex-wrap: wrap;
			align-items: center;

			.left {
				width: 225upx;
				height: 150upx;
				border-radius: 10px;
				background-color: #ffffff;
				display: flex;
				justify-content: space-evenly;
				align-items: center;

				.left-img {
					width: 120upx;
					height: 110upx;
					// border: 1px solid blue;
					background: #cfe0fd;
					border-radius: 10px;

					.img {
						width: 60upx;
						height: 60upx;
						background-image: url(../../../static/shiti/five.png);
						// border: 1px solid #000;
						margin: 0 auto;
						line-height: 90upx;
						margin: 12px auto;
						background-size: 100% 92%;
						background-repeat: no-repeat;
					}
				}

				.left-text {
					width: 140upx;
					height: 100upx;
					// border: 1px solid #18BC37;
					color: #4C5971;
					text-align: center;
					font-size: 27upx;
					line-height: 50upx;
					font-family: 'DIN-Medium';

					.red {
						color: #FF233D;
						font-size: 50upx;
					}

					.orange {
						color: #FF8712;
						font-size: 50upx;
					}

					.green {
						color: #0FBF6E;
						font-size: 50upx;
					}

					.purple {
						color: #8333FA;
						font-size: 50upx;
					}

					.blue {
						color: #387FFD;
						font-size: 50upx;
					}

					.black {
						color: #333333;
						font-size: 50upx;
					}

				}
			}
		}

		.section {
			width: 700upx;
			height: auto;
			background-color: #fff;
			margin: 15px auto;
			border-radius: 10px;

			// display: flex;
			// flex-direction: row;
			.section-text {
				color: #9299A8;
				font-size: 30upx;
				padding: 0 15px;
				// line-height: 55upx;
				// margin: 10px 0;

				span {
					color: #4C5971;
					font-size: 30upx;
					font-family: 'PingFang-SC-Medium';

					// padding: 10upx 15upx;
					// width:300rpx;
					// height:70rpx;
					//                background: #F6F9FC;
					// border-radius: 12px;
					// display: inline-block;

				}

				.title {
					width: 300upx;
					height: 34px;
					// border: 1px solid #18BC37;
					display: flex;
					flex-direction: row;
					justify-content: space-between;
					align-items: center;

					.dot {
						flex: 3;

						.round {
							width: 6px;
							height: 17px;
							background: #387FFD;
							border-radius: 6px;
						}
					}

					.font-title {
						flex: 13;

						.title-font {
							font-size: 18px;
							font-family: ' PingFang SC';
							font-weight: bold;
							color: #333333;
						}
					}
				}

				.boxder {
					width: 100%;
					height: auto;
					// border: 1px solid deeppink;
					padding: 5px 0;
				}

				.radio {
					width: 280px;
					height: auto;
					border-radius: 10px;
					margin: 20px auto;
					background: #fff;
					// border: 1px solid #8DC63F;
				}

				.result {
					width: 280px;
					height: 100upx;
					// padding:10upx 10upx;
					// border: 1px solid #1785FF;
				}

				.opinion {
					width: 280px;
					height: 100upx;
					// padding:10upx 10upx;
					// border: 1px solid #55aa7f;
				}
			}

			.section-model {
				width: 600upx;
				height: auto;
				margin: 10px auto;
				border-radius: 4px;
				background-color: #F6F7FA;
				display: flex;
				flex-direction: row;
				justify-content: space-between;
				align-items: center;

				.model-img {
					width: 50px;
					height: 63px;
					// border: 1px solid #1CBBB4;
				}

				.model-text {
					width: 420upx;
					height: auto;
					// border: 1px solid #F1A532;

					.tex-all {
						width: 420upx;
						height: auto;
						// line-height: 25px;

						.text-top {
							font-size: 14px;
							font-family: PingFang SC;
							font-weight: bold;
							color: #333333;
						}

						.text-bottom {
							color: #6e7176;

						}
					}

				}
			}
		}

		.sections {
			width: 700upx;
			height: auto;
			// background-color: #fff;
			margin: 15px auto;
			border-radius: 10px;

			// border: 1px solid red;
		}

		.tables {
			width: 700upx;
			height: auto;
			margin: 5px 0;
			border-radius: 10px;
			background-color: #fff;
		}

		.tables/deep/.u-modal {
			overflow-y: auto;
			height: 560px;
		}

		.tables/deep/.u-popup__content {
			background-color: #F2F5FE;
		}

		.zdmb/deep/.u-collapse-item__content__text {
			padding: 0px;
		}

		.zdmb/deep/.u-collapse-item__content {
			height: auto;
		}

		.sectionwrap {
			width: 315px;
			height: auto;
			margin: 0 auto;
			padding: 10px 0;
			// overflow-y: auto;
			// border: 1px solid #ffaa00;

			.section-all {
				width: 630rpx;
				height: auto;
				background-color: #f6f7fa;
				margin: 10px 0;
				// border: 1px solid #ED1C24;
				// margin: 15px auto;
				border-radius: 10px;

				.section-text {
					color: #9299A8;
					font-size: 30upx;
					padding: 0 15px;
					line-height: 55upx;
					margin: 10px 0;

					span {
						color: #4C5971;
						font-size: 30upx;
						font-family: 'PingFang-SC-Medium';
						padding: 10upx 0px;
						// width:300rpx;
						// height:70rpx;
						//                background: #F6F9FC;
						// border-radius: 12px;
						// display: inline-block;

					}

					.title {
						width: 300upx;
						height: 34px;
						// border: 1px solid #18BC37;
						display: flex;
						flex-direction: row;
						justify-content: space-between;
						align-items: center;

						.round {
							width: 6px;
							height: 17px;
							background: #387FFD;
							border-radius: 6px;
						}

						.title-font {
							font-size: 18px;
							font-family: ' PingFang SC';
							font-weight: bold;
							color: #333333;
						}

					}
				}
			}

			.main {
				width: 620upx;
				margin: 0 auto;
				margin-top: 30upx;
				background-color: #f6f7fa;
				border-radius: 16upx;

				// border: 1px solid deeppink;
				.mainnew {
					color: #333333;
					font-size: 37upx;
					font-weight: bold;
					padding: 0 10px;
				}

				&-item {
					display: flex;
					font-size: 28upx;
					padding-top: 30upx;

					&-name {
						margin-left: 30upx;
						text-align: left;
						color: #9299A8;

						span {
							margin-right: 30upx;
							color: #4C5971;
						}
					}
				}
			}

			.biaoqian {
				width: 12upx;
				height: 34upx;
				border-radius: 6upx;
				margin: 0 30upx;
				background-color: #4A7FFF;
			}

			.progress {
				width: 620upx;
				height: auto;
				margin: 0 auto;
				margin-top: 30upx;
				border-radius: 16upx;
				background-color: #f6f7fa;
				padding-top: 30upx;
				padding-left: 30upx;
			}
		}

		.title-fonts {
			font-size: 18px;
			font-family: ' PingFang SC';
			font-weight: bold;
			color: #333333;
			padding: 0 15px;
		}

		.increase-img {
			width: 77px;
			height: 77px;
			background-image: url(../../../static/pour/add.png);
			background-size: 100% 100%;
			position: fixed;
			bottom: 115px;
			right: -11px;
		}

		.add {
			position: fixed;
			bottom: 120upx;
			right: 20upx;
			width: 100upx;
			height: 100upx;
			background-color: #FFFFFF;
			border-radius: 50upx;
			display: flex;
			justify-content: center;
			align-items: center;
			// background-image: url(../../../static/pour/add.png);
			// background-repeat: no-repeat;
			// background-size: 100% 100%;
		}

		.popconfirm {
			position: fixed;
			bottom: 217upx;
			right: 20upx;
			width: 240upx;
			// height: 155upx;
			border-radius: 16upx;
			background-color: white;
			border: 1px solid #E6E8ED;
			box-shadow: 3px 5px 13px 0px rgba(197, 207, 227, 0.46);

			&-item {
				display: flex;
				justify-content: center;
				align-items: center;
				margin-top: 20upx;
				font-size: 16px;
				// font-family: cursive;

				&-img {
					width: 30upx;
					height: 30upx;
					margin-right: 10upx;
				}
			}
		}

		.new-button {
			width: 750upx;
			height: auto;
			// border: 1px solid deeppink;
			display: flex;
			flex-direction: row;
		}
	}
</style>
