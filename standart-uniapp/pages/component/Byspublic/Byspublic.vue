<template>
	<view>
		<view class="screen" >
			<view class="zzjg" @tap="showTree" v-if="zzjg">
				<text class="zzjg-title">
					{{orgname==''?"组织机构":orgname}}
				</text>
				<text class="cuIcon cuIcon-unfold"></text>
			</view>
			<view class="shebei" v-if="shebei">
				<picker @change="PickerChange" :value="index" :range="deviceName">
					<text class="">
						{{index>-1?deviceName[index]:'请选择设备名称'}}
					</text>
					<!-- <text class="cuIcon cuIcon-unfold"></text> -->
				</picker>
			</view>
			<view class="cblevel" v-if="cblevel">
				<picker @change="Cblevel" :value="index" :range="cblevelname">
					<text class="">
						{{cbindex>-1?cblevelname[cbindex]:'温度超标状态'}}
					</text>
					<text class="cuIcon cuIcon-unfold"></text>
				</picker>
			</view>
			<view class="betlev" v-if="jblevel">
				<picker @change="Jblevel" :value="index" :range="jblevelname">
					<text class="">
						{{scbindex>-1?jblevelname[scbindex]:'湿度超标状态'}}
					</text>
					<text class="cuIcon cuIcon-unfold"></text>
				</picker>
			</view>
			<view class="contentuse" v-if="contentuse">
				<picker @change="Contentuse" :value="index" :range="usename">
					<text class="">
						{{uindex>-1?usename[uindex]:'当前使用'}}
					</text>
					<text class="cuIcon cuIcon-unfold"></text>
				</picker>
			</view>
			
		<!-- 	<view class="begintime" v-if="time">
				<timeSelector showType="dateToTime" @btnConfirm="beginTime">
					<text class="">
						{{this.begintime==''?'开始时间':this.begintime}}
					</text>
					<text class="cuIcon cuIcon-unfold"></text>
				</timeSelector>
			</view>
			<view class="endtime" v-if="time">
				<timeSelector showType="dateToTime" @btnConfirm="endTime">
					<text class="">
						{{this.endtime==''?'结束时间':this.endtime}}
					</text>
					<text class="cuIcon cuIcon-unfold"></text>
				</timeSelector>
			</view> -->
			
		</view>
		<tki-tree ref="tkitree" @confirm="treeConfirm" @cancel="treeCancel" :selectParent="selectParent"
			:range="departTree" idKey='id' :foldAll="flod" rangeKey="departName" confirmColor="#4e8af7" />
	</view>
</template>

<script>
	import tkiTree from '@/components/tki-tree/tki-tree.vue'
	import timeSelector from '@/components/wing-time-selector/wing-time-selector.vue';
	import api from '@/api/apis.js'
	
	export default {
		name: 'Byspublic',
		components: {
			tkiTree,
			timeSelector
		},
		props: {
			sbtype: {
				type: String,
				default: false
			},
			cblevel: {
				type: Boolean,
				default: false
			},
			jblevel: {
				type: Boolean,
				default: false
			},
			hjblevel:{
				type: Boolean,
				default: false
			},
			betlev: {
				type: Boolean,
				default: false
			},
			contentuse: {
				type: String,
				default: ''
			},
			zzjg: {
				type: Boolean,
				default: false
			},
			shebei: {
				type: Boolean,
				default: true
			},
			time: {
				type: Boolean,
				default: true
			},
			
		},
		data() {
			return {
				cblevelname: [
					"未检测",
					"正常",
					"异常",
				],
				jblevelname: [
					"未检测",
					"正常",
					"异常",
				],
				
				departTree: [],
				selectParent: true,
				flod: false,
				orgname: '',
				orgcode: '',
				deviceName: [],
				devictype: [],
				index: -1,
				cbindex:-1,
				scbindex:-1,
				
				// begintime: '',
				// endtime: '',
				choosevalue: '',
				choosekey: '',
				
				
				dictData: [],
				dictvalue: '',
				dictvalues: [],
				values: '',
			}
		},
		created() {
			console.log(this.sbtype)
			this.queryMyDepartTree()
			this.deviceType()
			// this.getdictData();
		},
		methods: {
			
			// 开始时间
			// beginTime(e) {
			// 	console.log('确定时间为：', e);
			// 	this.begintime = e.key
			// 	this.choosevalue = e.key
			// 	this.choosekey = 3
			// 	this.Choose()
			// },
			// // 结束时间
			// endTime(e) {
			// 	console.log('确定时间为：', e);
			// 	this.endtime = e.key
			// 	this.choosevalue = e.key
			// 	this.choosekey = 4
			// 	this.Choose()
			// },
			// 选择组织机构
			treeConfirm(e) {
				this.index = -1
				this.begintime = ''
				this.endtime = ''
				this.orgname = e[0].title
				this.orgcode = e[0].orgCode
				this.choosevalue = e[0].orgCode
				this.choosekey = 1
				this.Choose()
			},
			// 选择设备
			PickerChange(e) {
				console.log(e)
				this.cbindex = -1
				this.scbindex = -1
				this.uindex=-1
				this.index = e.detail.value
				this.imei = this.devictype
				this.choosekey = 1
				this.choosevalue = this.devictype[this.index]
				this.begintime = ''
				this.endtime = ''
				this.Choose()
			},
			// 超标等级
			Cblevel(e){
				console.log(e)
				this.cbindex = e.detail.value
				this.choosekey = 2
				this.choosevalue = e.detail.value
				this.Choose()
			},
			Jblevel(e){
				console.log(e)
				this.scbindex = e.detail.value
				this.choosekey = 3
				this.choosevalue = e.detail.value
				this.Choose()
			},
			
			// 使用是否
			
			// 选择事件
			Choose() {
				this.$emit('choose', this.choosekey, this.choosevalue)
			},
			// 取消回调事件
			treeCancel(e) {
				console.log(e)
			},
			// 显示树形组件
			showTree() {
				this.$refs.tkitree._show();
			},
			// // 组织机构树
			queryMyDepartTree() {
				api.orgTree().then(res => {
					this.departTree = res.data.result
				}).catch(e => {
					console.log("请求错误", e)
				})
			},
			// 获取设备信息列表
			deviceType() {
				api.deviceType({
					params: {
						sys_depart_orgcode: this.orgcode,
						sbtypes: this.sbtype
					}
				}).then(res => {
					
					let devicelist = res.data.result
					this.deviceName = []
					this.devictype = []
					devicelist.forEach(e => {
						this.deviceName.push(e.sbname)
						this.devictype.push(e.sbjno)
					})
				}).catch(e => {
					console.log("请求错误", e)
				})

			},

		}
	}
</script>

<style>
	.screen {
		/* background-color: #FFFFFF; */
		display: flex;
	}

	/* .zzjg {
		flex: 1;
		border-right: 2upx solid #BBBBBB;
		color: #333333;
		text-align: center;
		padding: 20upx;
		overflow: hidden;
		text-overflow: ellipsis;
		white-space: nowrap;
	} */

	.shebei {
		flex: 1;
		/* border-right: 2upx solid #BBBBBB; */
		color:#c0c4cc;
		/* text-align: center; */
		/* padding: 20upx; */
		overflow: hidden;
		text-overflow: ellipsis;
		white-space: nowrap;
	}
	.cblevel{
		flex: 1;
		border-right: 2upx solid #BBBBBB;
		color: #333333;
		text-align: center;
		padding: 20upx;
		overflow: hidden;
		text-overflow: ellipsis;
		white-space: nowrap;
	}
	.betlev{
		flex: 1;
		border-right: 2upx solid #BBBBBB;
		color: #333333;
		text-align: center;
		padding: 20upx;
		overflow: hidden;
		text-overflow: ellipsis;
		white-space: nowrap;
	}
	.contentuse{
		flex: 1;
		border-right: 2upx solid #BBBBBB;
		color: #333333;
		/* text-align: center; */
		/* padding: 20upx; */
		overflow: hidden;
		text-overflow: ellipsis;
		white-space: nowrap;
	}
	/* .begintime {
		flex: 1;
		border-right: 2upx solid #BBBBBB;
		color: #333333;
		text-align: center;
		padding: 20upx;
		overflow: hidden;
		text-overflow: ellipsis;
		white-space: nowrap;
	} */

	/* .endtime {
		flex: 1;
		color: #333333;
		text-align: center;
		padding: 20upx;
		overflow: hidden;
		text-overflow: ellipsis;
		white-space: nowrap;
	} */
</style>

