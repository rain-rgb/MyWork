<template>
	<view id="wrap">
		<cu-custom bgColor="bg-white" :isBack="true">
			<block slot="content">编辑理论配合比</block>
		</cu-custom>
		<view class="section">
			<view class="titles">
				<view class="round"></view>
				<view class="title-font">基础信息</view>
			</view>
			<view class="group">
				<view class="title">强度等级：</view>
				<view class="content">
					<theory dictCode="betlev" :title="arrdata.betlevel" @choose="Choose3" ref="dj">
					</theory>
				</view>
			</view>
			<view class="group">
				<view class="title">抗渗等级:</view>
				<view class="content">
					<input type="text" placeholder="暂无" v-model="arrdata.filters" />
				</view>
			</view>
			<view class="group">
				<view class="title">抗冻等级:</view>
				<view class="content">
					<input type="text" placeholder="暂无" v-model="arrdata.freeze" />
				</view>
			</view>
			<view class="group">
				<view class="title">抗折等级:</view>
				<view class="content">
					<input type="text" placeholder="暂无" v-model="arrdata.bend" />
				</view>
			</view>
			<view class="group">
				<view class="title">坍落度：</view>
				<view class="content">
					<theory dictCode="lands" :title="arrdata.lands"  @choose="Choose2" ref="tld">
					</theory>
				</view>
			</view>
			<view class="group">
				<view class="title">搅拌时长:</view>
				<view class="content">
					<input type="number" placeholder="暂无" v-model="arrdata.mixlast" />
				</view>
			</view>
			<view class="group">
				<view class="title">设备名称：</view>
				<view class="content">
					<!-- <inputsel :sbtype="sbtype" :sbnames="arrdata.bhjno_dictText" title="暂无" @choose="Choose1">
					</inputsel> -->
					<selectshebei sbtype="0" :title="arrdata.bhjno_dictText" @choose="choose" ref="selectshebeis">
					</selectshebei>
				</view>
			</view>
			<view class="group">
				<view class="title">水胶比:</view>
				<view class="content">
					<input type="text" placeholder="暂无" v-model="arrdata.waterbindratio" />
				</view>
			</view>
		</view>
		<view class="sections">
			<view class="newfix">
				<view class="titletext">
					<view class="round"></view>
					<view class="title-font">材料用量信息</view>
				</view>
				<view class="buttonfix">
					<button class="mini-btn" style="color: #387FFD;background-color:#e3f2fd ;" size="mini"
						@click="increase"> 新增</button>
					<view class="imgurl" style="background-image: url(../../static/shiti/jia.png);width: 10px;height: 11px; background-size: 100% 100%;    position: relative;
					right: 57px;
					top: 8px;
					z-index: 2;"></view>
					<button class="mini-btn" style="color: #FF233D;background-color:#f7dbde;" size="mini"
						@tap="reduce">删除</button>
					<view class="imgurl" style="background-image: url(../../static/shiti/chu.png);width: 10px;height: 2px; background-size: 100% 100%;position: relative;
					right: 57px;
					top: 13px;
					z-index: 2;"></view>
				</view>
			</view>
			<!-- <view class="addlist" v-for="(d,indexs) in counter" :key="indexs"> -->
			<view class="addlist">
				<uni-table type="selection" border stripe emptyText="暂无更多数据" @selection-change="toggleRowSelection">
					<!-- 表头行 -->
					<uni-tr>
						<uni-th width="1" align="center">材料类型</uni-th>
						<uni-th width="1" align="center">材料名称</uni-th>
						<uni-th width="1" align="center">用量</uni-th>
					</uni-tr>
					<!-- 表格数据行 -->
					<uni-tr v-for="(tablelist,index) in arrdatas" :key="index">
						<uni-td align="center">{{tablelist.materialTypes}}</uni-td>
						<uni-td align="center">{{tablelist.materialname}}</uni-td>
						<uni-td align="center">{{tablelist.amount}}</uni-td>
					</uni-tr>


				</uni-table>
				<!-- <tbhzlist :content="arrdatas[indexs]" :tabindex="indexs" title="请选择材料类型" @choose7="choose7" @choose8="choose8"></tbhzlist> -->

				<!-- <tbhzlist  :materialname="materialname"  title="请选择材料类型"></tbhzlist>
				
				<tbhzlist  :materialcount="materialcount"  title="请选择材料类型"></tbhzlist> -->
			</view>
		</view>
		<u-modal :show="show" showCancelButton @cancel="cancel" @confirm="confirm">
			<view class="screen-modal-item">
				<tbhzlist title="请选择材料类型" @choose7="choose7 "></tbhzlist>
				<!-- <view class="screen-modal-item-name">组织机构：</view>
				<view class="screen-modal-item-input">
					<org @choose="Chooseorg"></org>
				</view> -->
			</view>
		</u-modal>
		<view>
			<button style="width:350px; height: 40px; background-color:#387FFD;color: #fff;text-align: center;"
				@click="submit">提交</button>
		</view>
	</view>
</template>
<script>
	import timeSelector from '@/components/wing-time-selector/wing-time-selector.vue';
	import api from '@/api/api.js'
	import theory from '@/pages/component/dict/theory.vue'
	import inputsel from '@/pages/component/inputsel.vue'
	import selectshebei from '@/pages/component/shebei/selectshebei.vue'
	import tbhzlist from '@/pages/component/tbhzlist/tbhzlist.vue'
	export default {
		components: {
			theory,
			inputsel,
			tbhzlist,
			timeSelector,
			selectshebei
		},
		data() {
			return {
				show: false,
				counter: 1,
				sbtype: '0',
				typelist: [],
				typecode: [],
				typename: [],
				typenamecode: [],
				index: -1,
				Mindex: -1,
				tabindex: 1,
				show: false,
				bhzRecipedetailList: {},
				bhzRecipedetailLists:{},
				bhjno: '',
				betlev: '',
				freeze: '',
				bend: '',
				mixlast: '',
				filters:'',
				lands: '',
				waterbindratio: '',
				
				model: {
					// attribute: '',
					// dattim: '',
					// conspos: '',
					// projgrade: '',
					// projname: '',
					// pour: '',
					
					// mete: '',
					// attamper: '',
					// note: '',
					// treeid: '',
					sbNumber: null,
					matervalue: '',
					matercode: '',
					// Materialtype: '',
					// Materialname: '',
					Materialcount: '',
					
					listdatas:[],
					arrdata: '',
					datalist:[],
					choosevalue: '',
					choosevalues: '',

					dictData: [],
					dictvalue: '',
					dictvalues: [],
					arrdatas: [],

					values: '',
					newdata: [],
					selectkey:[],
				}
			}
			},
			onLoad(options){
				var that = this
				// console.log(options)
				this.arrdata = JSON.parse(options.item)
				this.arrdatas = JSON.parse(options.table)
				this.model.datalist = JSON.parse(options.tables)
				// console.log(this.datalist,'jjjjj');
				this.counter = this.arrdatas.length
				
				this.bhjno = this.arrdata.bhjno;
				this.lands = this.arrdata.lands;
				this.betlev = this.arrdata.betlevel;
				this.filters = this.arrdata.filters;
				this.freeze = this.arrdata.freeze;
				this.bend = this.arrdata.bend;
				this.mixlast = this.arrdata.mixlast;
				this.waterbindratio = this.arrdata.waterbindratio
				
				// this.model.bhzRecipedetailList = this.arrdatas
				// console.log(this.arrdata, this.arrdatas)
			
				setTimeout(function() {
					// that.newlistdata(that.arrdatas
					that.detailslist(that.arrdata)
				}, 200)
			
			},
			created() {
				var that = this;
				// this.Materlistdata()
				setTimeout(function() {
					that.selectshebiesbname();
				}, 500)
			},
			methods: {
				
				// 筛选框
				confirm() {
					// this.getloadlist()
					// console.log(this.bhzRecipedetailList, 'this.model.bhzRecipedetailList')
					this.arrdatas.push(this.bhzRecipedetailList)
					console.log(this.arrdatas, "材料")
					// this.getloadlist()
					this.show = false
					this.model.datalist.push(this.bhzRecipedetailLists)
					console.log(this.model.datalist,'kkkk')
				},
				selectshebiesbname() {
					// console.log("1212")
					this.$refs.selectshebeis.call(this.arrdata.bhjno);
				},
				cancel() {
					this.show = false
				},
				increase() {
					// this.counter++
					this.show = true
				},
				reduce(){
					this.arrdatas.splice(this.selectkey,this.selectkey.length)
					this.model.datalist.splice(this.selectkey,this.selectkey.length)
					// console.log(this.arrdatas,this.selectkey.length,'删除后的数据');
					this.$forceUpdate()
				},
				//table中事件名(selection-change)开启多选时，当选择项发生变化时触发该事件，(toggleRowSelection)方法名  用于多选表格切换某一行的选中状态
				toggleRowSelection(e) {
					this.selectkey = e.detail.index
					// console.log(e, 'ttttttttttttt');
					
					// this.arrdatas.splice(e,1)
					// this.arrdatas.splice(index,1)
				},
				// toggleRowSelection(e) {
				// 	console.log(e)
				// },
				// newlistdata(e){
				// 	var that = this
				// 	console.log(e,"信息111")
				// 	e.forEach(function(it){
				// 		// it.materialTypes = that.lablist[it.materialTypes].title
				// 		console.log(it.materialTypes,'yyyy');
				// 		that.newdata.push(it)
				// 	})
				// 	console.log(that.newdata,"信息")
				// },
				choose7(y, x, z, t, index) {
					var that = this;
					// setTimeout(function() {
						
						// console.log(y, x, z, t, index, "22222222")
						if (x !== '' && y !== '' && z !== '') {
							// console.log("进入判断")
							// console.log(x, t, z, index)
							// splice表示截切字符串  如果有一个参数时 表示从当前位置一直截取到最后 如果有两个参数时 一个代表从当前位置开始 另一个参数代表截取的长度个数为多少  
							// that.model.bhzRecipedetailList.splice(index, 1)
							that.bhzRecipedetailList = {
								// amounts: t,
								materialname:z,
								materialTypes:x,
								amount:t,
							}
							that.bhzRecipedetailLists ={
								materialname: z,
								materialTypes: y,
								amount: t,
							}
							// console.log(that.bhzRecipedetailList, "失去焦点")
						}
						
					// }, 500)

				},
				// choose8(x, t, z, index) {
				// 	console.log(this.model.bhzRecipedetailList, "获取焦点输出上次数据")
				// 	if (x !== undefined &&t !== undefined && z !== undefined) {
				// 		console.log(x, t, z, index, "3333333")
				// 		//this.bhzRecipedetailList.splice(index, 1)
				// 		// this.bhzRecipedetailList.push({
				// 		// 	materialname: x,
				// 		// 	materialTypes: y,
				// 		// 	amount: z
				// 		// })
				// 		(this.model.bhzRecipedetailList, "获取焦点")
				// 	}
				// },
				choose(choosekey) {
					// var that = this;
					// console.log(this.bhjno, "222")
					// console.log(choosekey, 'wwwwwwwwwwwwww');
					// 设备名称
				
					this.bhjno = choosekey;
					// console.log(this.bhjno, "222")
					//console.log(that.model.bhjno,"1111")
					// if (choosekey == 1) {
					// 	this.model.sbNumber = choosevalue
					// 	// console.log(this.model.sbNumber, '00000000000000')
					// 	this.begintime = ''
					// 	this.endtime = ''
					// 	// this.submit()
					// }
					// if (choosekey == 2) {
					// 	this.model.sbNumber = choosevalue
					// 	// console.log(this.model.sbNumber, '00000000000000')
					// 	this.begintime = ''
					// 	this.endtime = ''
					// 	// this.submit()
					// }
					// if (choosekey == 3) {
					// 	this.model.sbNumber = choosevalue
					// 	// console.log(this.model.sbNumber, '00000000000000')
					// 	this.begintime = ''
					// 	this.endtime = ''
					// 	// this.submit()
					// }
				},
				Choose3(choosevalue) {
					this.betlev = choosevalue
					// console.log(this.betlev, "强度等级")
				},
				Choose2(choosevalue) {
					this.lands = choosevalue
					// console.log(this.lands, "坍落度")
				},
				// confirm() {
				// 	this.show = false
				// },
				Choose() {
					this.show = true
				},
				detailslist(e) {
					// console.log(e.bhjno_dictText)
					this.$refs.dj.call(e.betlevel_dictText)
					this.$refs.tld.call(e.lands_dictText)
					// this.$refs.shebei=e.bhjno_dictText
				},
				submit() {
					this.$http.put(`/bhzrecipe/bhzRecipe/edit`, {
						id: this.arrdata.id,
						uuid: this.arrdata.uuid,
						bhjno: this.arrdata.bhjno,
						betlevel: this.betlev,
						lands: this.lands,
						filters: this.arrdata.filters,
						freeze: this.arrdata.freeze,
						bend: this.arrdata.bend,
						mixlast: this.arrdata.mixlast,
						waterbindratio: this.arrdata.waterbindratio,
						bhzRecipedetailList: this.model.datalist
					}).then(res => {
						if (res.data.success == true) {
							uni.showToast({
								title: "添加成功",
								icon: 'none'
							})
							setTimeout(function() {
								uni.navigateTo({
									url: "/pages/theory/theory",
								})
							},500)
						}
					})
				}
			}
		}
</script>
<style lang="scss" scoped>
	#wrap {
		width: 100%;
	}

	.section {
		width: 700rpx;
		height: auto;
		background-color: #fff;
		margin: 15px auto;
		border-radius: 10px;
	}

	.group {
		margin-top: 1px;
		display: flex;
		// background: #FFFFFF;
		padding: 5px 10px;
		height: 50px;
		line-height: 50px;
		font-size: 14px;
	}

	.group .title {
		flex: 4;
		line-height: 30px;
		height: 30px;
		text-align: center;
	}

	.group .content {
		height: 34px;
		line-height: 34px;
		background-color: #f5f7fa;
		flex: 9;
		border-radius: 7px;
		// border: 1px solid #dadbde;
	}

	.group .content input {
		padding-top: 8px;
		padding-left: 5px;
		font-size: 14px;
	}

	.uni-input-placeholder {
		color: #c0c4cc;
	}

	.group .content button {
		text-align: left;
		padding-left: 5px;
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
		background-color: #fff;
		margin: 10px auto;
		border-radius: 10px;
		height: auto;
		border-bottom: 4px solid #dadbde;

	}

	.titles {
		width: 200rpx;
		height: 34px;
		// border: 1px solid #18BC37;
		display: flex;
		flex-direction: row;
		justify-content: space-around;
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

		// .mini-btn {
		// 	float: right;
		// }
	}



	.sections {
		width: 700rpx;
		height: auto;
		background-color: #fff;
		margin: 15px auto;
		border-radius: 10px;
		// display: flex;
		// justify-content: space-between;

		.newfix {
			width: 350px;
			display: flex;
			flex-direction: row;
			justify-content: space-between;
			align-items: center;

			.titletext {
				width: 133px;
				height: 34px;
				// border: 1px solid #18BC37;
				display: flex;
				flex-direction: row;
				justify-content: space-around;
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

			.buttonfix {
				// width: 130px;
				// border: 1px solid red;
				heigh: 40px;
				display: flex;

				.imgurl {
					width: 10px;
					height: 10px;
					// border:1px solid blue;
				}
			}

		}

	}
</style>
