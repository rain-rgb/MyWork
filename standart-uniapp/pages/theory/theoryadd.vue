<template>
	<view id="wrap">
		<cu-custom bgColor="bg-white" :isBack="true">
			<block slot="content">新增理论配合比</block>
		</cu-custom>
		<view class="section">
			<view class="titles">
				<view class="round"></view>
				<view class="title-font">基础信息</view>
			</view>
			<view class="group">
				<view class="title">强度等级：</view>
				<view class="content" >
					<theory  dictCode="betlev" title="请选择强度等级" @choose="Choose3">
					</theory>
				</view>
			</view>
			<view class="group">
				<view class="title">抗渗等级:</view>
				<view class="content">
					<input  type="text" placeholder="请输入抗渗等级" v-model="model.filters" />
				</view>
			</view>
			<view class="group">
				<view class="title">抗冻等级:</view>
				<view class="content">
					<input type="text" placeholder="请输入抗冻等级" v-model="model.freeze" />
				</view>
			</view>
			<view class="group">
				<view class="title">抗折等级:</view>
				<view class="content">
					<input type="text" placeholder="请输入抗折等级" v-model="model.bend" />
				</view>
			</view>
			<view class="group">
				<view class="title">坍落度：</view>
				<view class="content">
					<theory dictCode="lands" title="请选择坍落度" @choose="Choose2">
					</theory>
				</view>
			</view>
			<view class="group">
				<view class="title">搅拌时长:</view>
				<view class="content">
					<input type="number" placeholder="默认不写会设为默认值120" v-model="model.mixlast" />
				</view>
			</view>
			<view class="group">
				<view class="title">设备名称：</view>
				<view class="content">
					<Byspublic :sbtype="sbtype" title="请选择设备名称" @choose="Choose1">
					</Byspublic>
				</view>
			</view>
			<view class="group">
				<view class="title">水胶比:</view>
				<view class="content">
					<input type="text" placeholder="请输入水胶比" v-model="model.waterbindratio" />
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
						@click="reduce">删除</button>
					<view class="imgurl" style="background-image: url(../../static/shiti/chu.png);width: 10px;height: 2px; background-size: 100% 100%;position: relative;
					right: 57px;
					top: 13px;
					z-index: 2;"></view>
				</view>
			</view>
			<view class="addlist" v-for="(d,indexs) in counter" :key="indexs">
				<tbhzlist :tabindex="indexs" title="请选择材料类型"  @choose7="choose7" @choose8="choose8"></tbhzlist>

				<!-- <tbhzlist  :materialname="materialname"  title="请选择材料类型"></tbhzlist>
				
				<tbhzlist  :materialcount="materialcount"  title="请选择材料类型"></tbhzlist> -->
			</view>
		</view>
		<view>
			<button style="width:350px; height: 40px; background-color:#387FFD;color: #fff;text-align: center;"
				@click="submit">提交</button>
		</view>
	</view>
</template>
<script>
	import timeSelector from '@/components/wing-time-selector/wing-time-selector.vue';
	import api from '@/api/api.js'
	import theory from '@/pages/component/dict/dict.vue'
	import Byspublic from '@/pages/component/Byspublic/Byspublic.vue'
	import tbhzlist from '@/pages/component/tbhzlist/tbhzlist.vue'
	export default {
		components: {
			theory,
			Byspublic,
			tbhzlist,
			timeSelector
		},
		data() {
			return {
				counter: [],
				sbtype: '0',
				typelist: [],
				typecode: [],
				typename: [],
				typenamecode: [],
				index: -1,
				Mindex: -1,
				tabindex: 1,
				show: false,
				model: {
					// attribute: '',
					// dattim: '',
					// conspos: '',
					// projgrade: '',
					// projname: '',
					// pour: '',
					filters: '',
					betlevel: '',
					freeze: '',
					bend: '',
					mixlast: '',
					lands: '',
					waterbindratio: '',
					// mete: '',
					// attamper: '',
					// note: '',
					// treeid: '',
					bhjno: '',
					// Materialtype: '',
					// Materialname: '',
					Materialcount: '',
					bhzRecipedetailList: []

				},
				choosevalue: '',
				choosevalues: '',
			}
		},
		methods: {
			increase() {
				this.counter++
			},
			reduce() {
				this.counter--
			},
			choose7(y, x, z, t, index) {
				var that = this;
				setTimeout(function() {
					// console.log(x,y,z,index,"22222222")
					if (x !== '' && y !== '' && z !== '') {
						// console.log("进入判断")
						// splice表示截切字符串  如果有一个参数时 表示从当前位置一直截取到最后 如果有两个参数时 一个代表从当前位置开始 另一个参数代表截取的长度个数为多少  
						that.model.bhzRecipedetailList.splice(index, 1)
						that.model.bhzRecipedetailList.push({
							materialname: z,
							materialTypes: y,
							amount: t,
						})
						// console.log(that.model.bhzRecipedetailList, "失去焦点")
					}
				}, 1500)

			},
			choose8(x, y, z, index) {
				// console.log(this.model.bhzRecipedetailList, "获取焦点输出上次数据")
				if (x !== undefined && y !== undefined && z !== undefined) {
					// console.log(x, y, z, index, "3333333")
					//this.bhzRecipedetailList.splice(index, 1)
					// this.bhzRecipedetailList.push({
					// 	materialname: x,
					// 	materialTypes: y,
					// 	amount: z
					// })
					// console.log(this.model.bhzRecipedetailList, "获取焦点")
				}
			},
			Choose1(choosekey, choosevalue) {

				// console.log(choosevalue,'wwwwwwwwwwwwww');
				// 设备名称
				if (choosekey == 1) {
					this.model.bhjno = choosevalue
					// console.log(this.model.sbNumber, '00000000000000')
					this.begintime = ''
					this.endtime = ''
					// this.submit()
				}

			},
			Choose3(choosevalue) {
				// console.log(choosevalue, "强度等级")
				this.model.betlevel = choosevalue
			},
			Choose2(choosevalue) {
				// console.log(choosevalue, "坍落度")
				this.model.lands = choosevalue
			},
			confirm() {
				this.show = false
			},
			Choose() {
				this.show = true
			},
			submit() {
				if (!this.model.waterbindratio) {
					// console.log("111111111111");
					uni.showToast({
						title: '请选择水胶比',
						icon: 'none'
					})
					return
				}
				this.$http.post(`/bhzrecipe/bhzRecipe/add`, this.model).then(res => {
					// console.log(res, 'pppppp');
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
		height: 47px;
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
		border: 1px solid #dadbde;
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
		height: 130px;
		// border-bottom: 4px solid #dadbde;
		box-shadow: 0px 5px 4px #8aaeec;

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

				// .mini-btn {
				// 	// flex-direction: row;
				// }
			}

		}

	}
</style>
