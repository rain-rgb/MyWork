<template>
	<view class="wrap">
		<cu-custom bgColor="bg-white" :isBack="true">
			<block slot="content">理论配合比详情页</block>
		</cu-custom>
		<view class="section" >
			<view class="section-text">
				<view class="title">
					<view class="round"></view>
					<view class="title-font">基础信息</view>
				</view>
				<!-- <view class="">配合比编号:<span>{{newlist.code!==null?newlist.code:'暂无数据'}}</span></view> -->
				<view class="">强度等级:<span>{{newlist.betlevel!==null?newlist.betlevel:'暂无数据'}}</span></view>
				<view class="">抗渗等级:<span>{{newlist.filters!==null?newlist.filters:'暂无数据'}}</span></view>
				<view class="">抗冻等级:<span>{{newlist.freeze !==null?newlist.freeze:'暂无数据'}}</span></view>
				<view class="">抗折等级:<span>{{newlist.bend !==null?newlist.bend:'暂无数据'}}</span></view>
				<view class="">坍落度:<span>{{newlist.lands !==null?newlist.lands:'暂无数据'}}</span></view>
				<view class="">搅拌时长:<span>{{newlist.mixlast !==null?newlist.mixlast:'暂无数据'}}</span></view>
				<view class="">建立时间:<span>{{newlist.createdate !==null?newlist.createdate:'暂无数据'}}</span></view>
				<view class="">拌合机名称:<span>{{newlist.bhjno_dictText !==null?newlist.bhjno_dictText:'暂无数据'}}</span>
				</view>
				<view class="">水胶比:<span>{{newlist.waterbindratio !==null?newlist.waterbindratio:'暂无数据'}}</span>
				</view>
			</view>
		</view>
		<view class="sections">
			<view class="newfix">
				<view class="titletext">
					<view class="round"></view>
					<view class="title-font">材料用量信息</view>
				</view>

			</view>
			<view class="addlist">
				<uni-table border stripe emptyText="暂无更多数据">
					<!-- 表头行 -->
					<uni-tr>
						<uni-th width="1" align="center">材料类型</uni-th>
						<uni-th width="1" align="center">材料名称</uni-th>
						<uni-th width="1" align="center">用量</uni-th>
					</uni-tr>
					<!-- 表格数据行 -->
					<uni-tr v-for="(tablelist,index) in arraydata" :key="index">
						<uni-td align="center">{{tablelist.materialTypes}}</uni-td>
						<uni-td align="center">{{tablelist.materialname}}</uni-td>
						<uni-td align="center">{{tablelist.amount}}</uni-td>
					</uni-tr>


				</uni-table>
			</view>
		</view>
		<view class="button_all">
			<view class="button_bj">
				<u-button type="primary" size="normal" @click="edit" text="编辑"></u-button>
			</view>
			<view class="button_sc">
				<u-button type="warning" size="normal" @click="deleted" text="删除"></u-button>
			</view>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				newlist: '',
				arraydata: [],
				arraydatas:[],
				listData: [],
				tbhzlist: [],
				lablist: [{
						title: "其他",
					},
					{
						title: "细骨料",
					},
					{
						title: "大石",
					},
					{
						title: "中石",
					},
					{
						title: "小石",
					},
					{
						title: "水",
					},
					{
						title: "水泥/沥青",
					},
					{
						title: "矿粉",
					},
					{
						title: "粉煤灰",
					},
					{
						title: "外加剂",
					},
				],
			}
		},
		onLoad(options) {
			// console.log(JSON.parse(options.item), 'jjjjjjjjjjjjj');
			this.newlist = JSON.parse(options.item)
			this.listdata()
		},
		created() {
			// this.listdata()
		},
		methods: {
			listdata() {
				var that = this
				this.$http.get("/bhzrecipe/bhzRecipe/queryBhzRecipedetailByMainId", {
					params: {
						id: this.newlist.uuid
					}
				}).then(res => {
					// this.listdatavalue = res.data.result.records
					// console.log(res, 'klkkkkkkkk');
					this.tbhzlist = res.data.result
					// console.log(that.lablist[1].title, 'ddddddddd');
					this.tbhzlist.forEach(function(it) { //使用function来forEach循环data中数据不能使用this拿必须先将this赋值给that
					// console.log(it,'材料');
						it.status = it.materialTypes
						it.materialTypes = that.lablist[it.materialTypes].title
						// it.status = that.lablist[it.status].value()
						console.log(it.status,it.materialTypes,'rrrrrrrrrrr');
						that.arraydata.push({
							materialTypes:it.materialTypes,
							materialname:it.materialname,
							amount:it.amount
						})
						that.arraydatas.push({
							materialTypes:it.status,
							materialname:it.materialname,
							amount:it.amount
						})
						console.log(that.arraydata);
					})
				})
			},
			edit() {
				let item = this.newlist
				let table = this.arraydata
				let tables = this.arraydatas
				// console.log(table, 'trtttttttt');
				uni.navigateTo({
					url: "/pages/theory/theoryedit?item=" + JSON.stringify(item) + "&table=" + JSON.stringify(table)+ "&tables=" + JSON.stringify(tables),
				})
			},
			deleted() {
				let id = this.newlist.id
				this.$http.delete(`/bhzrecipe/bhzRecipe/delete?id=${id}`).then(res => {
					if (res.data.success) {
						uni.showToast({
							title: '删除成功',
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
	#home {
		width: 100%;
		height: auto;
	}

	.section {
		width: 700rpx;
		height: auto;
		background-color: #fff;
		margin: 15px auto;
		border-radius: 10px;

		// display: flex;
		// flex-direction: row;
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
				padding: 10upx 15upx;
				// width:300rpx;
				// height:70rpx;
				//                background: #F6F9FC;
				// border-radius: 12px;
				// display: inline-block;

			}

			.title {
				width: 200rpx;
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
				width: 160px;
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

	.button_all {
		width: 300px;
		display: flex;
		margin: 0 auto;
		// border:1px solid red;
		justify-content: space-between;
	}

	.button_bj {
		width: 110px;
		// height: auto;
	}

	.button_sc {
		width: 110px;
		// height: auto;
	}
</style>
