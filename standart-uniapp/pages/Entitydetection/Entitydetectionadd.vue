<template>
	<view>
		<cu-custom bgColor="bg-white" :isBack="true">
			<block slot="content">新增检验委托</block>
		</cu-custom>
		<view class="section">
			<view class="group">
				<view class="title">请选择施工部位：</view>
				<view class="content">
					<button class="sgbw" @click="open">
						<text>
							{{this.model.conspos==''?"请选择施工部位":this.model.conspos}}
						</text>
						<!-- <text class="cuIcon cuIcon-unfold"></text> -->
					</button>
					<uni-popup ref="popup" type="bottom">
						<view style="background-color: #FFFFFF;">
							<scroll-view scroll-y="true" scroll-x="true">
								<projectname @choose="Choose3"></projectname>
							</scroll-view>
						</view>

					</uni-popup>
				</view>
			</view>
			<view class="group">
				<view class="title">工程名称：</view>
				<view class="content">
					<input type="text" placeholder="请输入工程名称" v-model="model.projectname" />
				</view>
			</view>
			<view class="group">
				<view class="title">施工部位：</view>
				<view class="content">
					<input type="text" placeholder="请输入施工部位" v-model="model.sgbwname" />
				</view>
			</view>
			<view class="group">
				<view class="title">试验类型：</view>
				<view class="content">
					<dict dictCode="component" title="请选择试验类型" @choose="Choose2">
					</dict>
				</view>
			</view>
			<view class="group">
				<view class="title"><span style="color: #E42424;">*</span>设备厂家：</view>
				<view class="content">
					<dict dictCode="shebeichangjia" title="请选择设备厂家" @choose="Choose4">
					</dict>
				</view>
			</view>
			<view class="group">
				<view class="title"><span style="color: #E42424;">*</span>设备名称：</view>
				<view class="content">
					<Byspublic :sbtype="sbtype" title="请选择设备名称" @choose="Choose1">
					</Byspublic>
				</view>
			</view>
			<view class="group">
				<view class="title">试验日期：</view>
				<view class="content">
					<view class="begintime">
						<timeSelector showType="dateToTime" @btnConfirm="beginTime">
							<text class="">
								{{this.begintime==''?'请选择试验日期':this.begintime}}
							</text>
							<!-- <text class="cuIcon cuIcon-unfold"></text> -->
						</timeSelector>
					</view>
				</view>
			</view>
		</view>
		<view>
			<button class="button" @click="submit">提交</button>
		</view>
	</view>
</template>

<script>
	import timeSelector from '@/components/wing-time-selector/wing-time-selector.vue';
	import dict from '../component/dict/theory.vue'
	import Byspublic from '../component/Byspublic/Byspublic.vue'
	import projectname from '../component/projectname/projectname.vue'
	export default {
		name: 'Entitydetectionadd',
		components: {
			dict,
			Byspublic,
			timeSelector,
			projectname,
			// yljpublic
			// timeSelector
		},
		data() {
			return {
				counter: [],
				sbtype: '14,42,43',
				typelist: [],
				typecode: [],
				typename: [],
				begintime: '',
				typenamecode: [],
				index: -1,
				Mindex: -1,
				tabindex: 1,
				model: {
					// attribute: '',
					// dattim: '',
					// conspos: '',
					// projgrade: '',
					// projname: '',
					// pour: '',
					projectname: '', // 工程名称
					sgbwname: '', // 施工部位
					component: '', //试验类型
					shebeichangjia: '', //设备厂家
					Testdate: '', // 试验日期
					conspos: '',
					treeid: '',
					// freeze: '',
					// bend: '',
					// mixlast: '',
					// lands: '',
					// waterbindratio: '',
					// mete: '',
					// attamper: '',
					// note: '',
					// treeid: '',
					shebeibianhao: null,
					// matervalue: '',
					// matercode: '',
					// Materialtype: '',
					// Materialname: '',
					// Materialcount: '',
					// bhzRecipedetailList: []

				},
				datalist: [],
				choosevalue: '',
				choosevalues: '',

			}
		},
		onLoad() {
			// this.listdata();
		},
		created() {
			// this.Materlistdata()
			// this.creatdata()
		},
		methods: {
			//施工部位
			// creatdata(){
			// 	this.$http.get(`/sys/sysDepartproject/queryMyDeptTreeList`,{params:{parentId:""}}).then(res => {
			// 		console.log(res.data.result,'施工部位');
			// 		this.datalist = res.data.result
			// 	})
			// },


			//新增按钮
			// add() {
			// 	this.counter.push({})
			// },
			// choose7(x,y,z,index){	
			// 	var that=this;
			// 	setTimeout(function(){
			// 		// console.log(x,y,z,index,"22222222")
			// 		if (x !== '' && y !== ''&&z!=='') {
			// 			console.log("进入判断")
			// 		// splice表示截切字符串  如果有一个参数时 表示从当前位置一直截取到最后 如果有两个参数时 一个代表从当前位置开始 另一个参数代表截取的长度个数为多少  
			// 			that.model.bhzRecipedetailList.splice(index, 1)
			// 			that.model.bhzRecipedetailList.push({
			// 				materialname: x,
			// 				// materialTypes: y,
			// 				amount: z
			// 			})
			// 			console.log(that.model.bhzRecipedetailList, "失去焦点")
			// 		}
			// 	},1500)

			// },
			// choose8(x,y,z,index){
			// 	console.log(this.model.bhzRecipedetailList, "获取焦点输出上次数据")
			// 	if (x !== undefined && y !== undefined&&z!==undefined) {
			// 		console.log(x,y,z,index,"3333333")
			// 		//this.bhzRecipedetailList.splice(index, 1)
			// 		// this.bhzRecipedetailList.push({
			// 		// 	materialname: x,
			// 		// 	materialTypes: y,
			// 		// 	amount: z
			// 		// })
			// 		console.log(this.model.bhzRecipedetailList, "获取焦点")
			// 	}
			// },
			Choose1(choosekey, choosevalue) {

				// console.log(choosevalue,'wwwwwwwwwwwwww');
				// 设备名称
				if (choosekey == 1) {
					this.model.shebeibianhao = choosevalue
					// console.log(this.model.sbNumber, '00000000000000')
					this.begintime = ''
					this.endtime = ''
					// this.submit()
				}
			},
			Choose2(choosevalue) {
				// console.log(choosevalue, "试验类型")
				this.model.component = choosevalue
			},
			Choose4(choosevalue) {
				// console.log(choosevalue, "设备厂家")
				this.model.shebeichangjia = choosevalue
			},
			Choose3(choosevalue) {
				// console.log(choosevalue, "当前施工部位")
				this.model.conspos = choosevalue.departName
				this.model.treeid = choosevalue.id
				setTimeout(() => {
					this.$refs.popup.close()
				}, 500);

			},
			open() {
				// 通过组件定义的ref调用uni-popup方法 ,如果传入参数 ，type 属性将失效 ，仅支持 ['top','left','bottom','right','center']
				this.$refs.popup.open('bottom')
			},

			beginTime(e) {
				// console.log('确定时间为：', e);
				this.begintime = e.key
				this.choosevalue = e.key
				this.model.Testdate = this.choosevalue
				// console.log(this.choosevalue,'iiiiiiiii');
				this.choosekey = 3
				// this.Choose()
			},
			submit() {
				if (!this.model.component) {
					uni.showToast({
						title: '请选择试验类型',
						icon: 'none'
					})
					return
				}
				this.$http.post(`/sys/sysDepartproject/renwudanxiafaadd`, this.model).then(res => {
					console.log(res, 'pppppp');
					if (res.data.success == true) {
						uni.showToast({
							title: "添加成功",
							icon: 'none'
						})
					}
				})
			}
		}
	}
</script>

<style scoped>
	.section {
		width: 350px;
		height: 438px;
		background-color: #fff;
		margin: 10px auto;
		padding: 10px 0;
		border-radius: 10px;
	}

	.group {
		margin-top: 15px;
		display: flex;
		background: #FFFFFF;
		padding: 5px 10px;
		height: 40px;
		line-height: 40px;
		font-size: 14px;
	}

	.group .title {
		flex: 5;
		line-height: 30px;
		height: 30px;
		text-align: center;
	}

	.group .content {
		height: 39px;
		line-height: 39px;
		background-color: #f5f7fa;
		flex: 9;
		border-radius: 5px;
		border: 1px solid #dadbde;
	}

	.group .content input {
		padding-top: 8px;
		padding-left: 5px;
		color: inherit;
		font-size: 14px;
	}

	.group .content button {
		text-align: left;
		padding-left: 5px;
		background-color: #f5f7fa;
		height: 39px;
		line-height: 39px;
		color: #c0c4cc;
	}

	.sgbw {
		height: 30px;
		line-height: 30px;
		background-color: #f1f1f1;
		flex: 9;
		border-radius: 3px;
	}

	.sgbw text {
		color: inherit;
		font-size: 14px;
	}

	.time {
		padding-left: 5px;
	}

	.addlist {
		/* border-bottom: 1px solid #ffaa00; */
		/* padding: 10px 0; */
	}

	.begintime {
		flex: 1;
		/* border-right: 2upx solid #BBBBBB; */
		color: #c0c4cc;
		/* text-align: center; */
		/* padding: 20upx; */
		overflow: hidden;
		text-overflow: ellipsis;
		white-space: nowrap;
	}

	.button {
		width: 700rpx;
		height: 100rpx;
		background-color: #387FFD;
		margin-top: 100rpx;
		color: #fff;
	}

	.uni-input-placeholder {
		color: #c0c4cc;
	}
</style>
