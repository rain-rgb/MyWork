<template>
	<view>
		<view class="smartbh">
			<!--  智慧拌合-->
			<view>
				<view class="smartbh-message">
					智慧拌合
				</view>
			</view>
			<view class="smartbh-label">
				<view class="smartbh-label-all" @click="handleNavs(0)">
					<view class="smartbh-label-all-img" style="background-image: url(../../static/label/jzjd.png);">
					</view>
					<view class="smartbh-label-all-title">浇筑进度</view>
				</view>
				<view class="smartbh-label-all" @click="handleNavs(1)">
					<view class="smartbh-label-all-img" style="background-image: url(../../static/label/yjbh.png);">
					</view>
					<view class="smartbh-label-all-title">预警及闭合率</view>
				</view>
				<view class="smartbh-label-all" @click="handleNavs(2)">
					<view class="smartbh-label-all-img" style="background-image: url(../../static/label/cbcz.png);">
					</view>
					<view class="smartbh-label-all-title">超标处置</view>
				</view>
				<view class="smartbh-label-all" @click="handleNavs(3)">
					<view class="smartbh-label-all-img" style="background-image: url(../../static/label/tj.png);">
					</view>
					<view class="smartbh-label-all-title">产能统计</view>
				</view>
				<view class="smartbh-label-all" @click="handleNavs(4)">
					<view class="smartbh-label-all-img" style="background-image: url(../../static/label/jzsq.png);">
					</view>
					<view class="smartbh-label-all-title">浇筑申请</view>
				</view>
				<view class="smartbh-label-all" @click="handleNavs(5)">
					<view class="smartbh-label-all-img" style="background-image: url(../../static/label/jzsh.png);">
					</view>
					<view class="smartbh-label-all-title">浇筑令管理</view>
				</view>
				<view class="smartbh-label-all">
					<view class="smartbh-label-all-img" style="background-image: url(../../static/label/jzxq.png);">
					</view>
					<view class="smartbh-label-all-title">浇筑详情查询</view>
				</view>
				<view class="smartbh-label-all" @click="handleNavs(6)">
					<view class="smartbh-label-all-img" style="background-image: url(../../static/label/ysgj.png);">
					</view>
					<view class="smartbh-label-all-title">运输轨迹追踪</view>
				</view>
				<view class="smartbh-label-all" @click="handleNavs(7)">
					<view class="smartbh-label-all-img" style="background-image: url(../../static/label/bhtime.png);">
					</view>
					<view class="smartbh-label-all-title">拌合时间查询</view>
				</view>
				<view class="smartbh-label-all" @click="handleNavs(8)">
					<view class="smartbh-label-all-img" style="background-image: url(../../static/label/materall.png);">
					</view>
					<view class="smartbh-label-all-title">材料用量查询</view>
				</view>
				<view class="smartbh-label-all" @click="handleNavs(9)">
					<view class="smartbh-label-all-img" style="background-image: url(../../static/label/cb.png);">
					</view>
					<view class="smartbh-label-all-title">超标查询</view>
				</view>
				<view class="after-add">
					<view class="smartbh-label-all-img"></view>
					<view class="smartbh-label-all-title"></view>
				</view>
				<view class="after-add">
					<view class="smartbh-label-all-img"></view>
					<view class="smartbh-label-all-title"></view>
				</view>
			</view>


		</view>
		<view class="smartbh">
			<!-- 智慧试验 -->
			<view>
				<view class="smartbh-message">
					智慧试验
				</view>
			</view>
			<view class="smartbh-label">
				<view class="smartbh-label-all" @click="handleNav(0)">
					<view class="smartbh-label-all-img" style="background-image: url(../../static/label/llph.png);">
					</view>
					<view class="smartbh-label-all-title">理论配合</view>
				</view>
				<view class="smartbh-label-all" @click="handleNav(1)">
					<view class="smartbh-label-all-img" style="background-image: url(../../static/label/pbtz.png);">
					</view>
					<view class="smartbh-label-all-title">配比通知单</view>
				</view>
				<view class="smartbh-label-all" @click="handleNav(2)">
					<view class="smartbh-label-all-img" style="background-image: url(../../static/label/pldtz.png);">
					</view>
					<view class="smartbh-label-all-title">配料单管理</view>
				</view>
				<view class="smartbh-label-all" @click="handleNav(3)">
					<view class="smartbh-label-all-img" style="background-image: url(../../static/label/yclqy.png);">
					</view>
					<view class="smartbh-label-all-title">原材检验</view>
				</view>
				<view class="smartbh-label-all" @click="handleNav(4)">
					<view class="smartbh-label-all-img" style="background-image: url(../../static/label/sywt.png);">
					</view>
					<view class="smartbh-label-all-title">试验委托</view>
				</view>
				<view class="smartbh-label-all" @click="handleNav(5)">
					<view class="smartbh-label-all-img" style="background-image: url(../../static/label/jzqy.png);">
					</view>
					<view class="smartbh-label-all-title">(见证)取样</view>
				</view>
				<view class="smartbh-label-all" @click="handleNav(6)">
					<view class="smartbh-label-all-img" style="background-image: url(../../static/label/sksj.png);">
					</view>
					<view class="smartbh-label-all-title">试块上架</view>
				</view>
				<view class="smartbh-label-all" @click="handleNav(7)">
					<view class="smartbh-label-all-img" style="background-image: url(../../static/label/skxj.png);">
					</view>
					<view class="smartbh-label-all-title">试块下架</view>
				</view>
				<view class="smartbh-label-all" @click="handleNav(8)">
					<view class="smartbh-label-all-img" style="background-image: url(../../static/label/rwwt.png);">
					</view>
					<view class="smartbh-label-all-title">任务委托先详情查询</view>
				</view>
				<view class="smartbh-label-all" @click="handleNav(9)">
					<view class="smartbh-label-all-img" style="background-image: url(../../static/label/cbcz.png);">
					</view>
					<view class="smartbh-label-all-title">超标处置</view>
				</view>
				<view class="smartbh-label-all" @click="handleNav(10)">
					<view class="smartbh-label-all-img" style="background-image: url(../../static/label/ylj.png);">
					</view>
					<view class="smartbh-label-all-title">压力机数据查询</view>
				</view>
				<view class="smartbh-label-all" @click="handleNav(11)">
					<view class="smartbh-label-all-img" style="background-image: url(../../static/label/wnj.png);">
					</view>
					<view class="smartbh-label-all-title">万能机数据查询</view>
				</view>
				<view class="smartbh-label-all" @click="handleNav(12)">
					<view class="smartbh-label-all-img" style="background-image: url(../../static/label/kykz.png);">
					</view>
					<view class="smartbh-label-all-title">抗压抗折机数据查询</view>
				</view>
				<view class="smartbh-label-all" @click="handleNav(13)">
					<view class="smartbh-label-all-img" style="background-image: url(../../static/label/bysdata.png);">
					</view>
					<view class="smartbh-labe l-all-title">标养室数据查询</view>
				</view>
				<view class="smartbh-label-all" @click="handleNav(14)">
					<view class="smartbh-label-all-img" style="background-image: url(../../static/label/gbht.png);">
					</view>
					<view class="smartbh-label-all-title">钢保回弹数据 查询</view>
				</view>
				<view class="smartbh-label-all" @click="handleNav(15)">
					<view class="smartbh-label-all-img" style="background-image: url(../../static/label/zjjc.png);">
					</view>
					<view class="smartbh-label-all-title">实体检测</view>
				</view>
				<view class="smartbh-label-all" @click="handleNav(16)">
					<view class="smartbh-label-all-img" style="background-image: url(../../static/label/zljc.png);">
					</view>
					<view class="smartbh-label-all-title">张拉检测</view>
				</view>
				<view class="smartbh-label-all" @click="handleNav(17)">
					<view class="smartbh-label-all-img" style="background-image: url(../../static/label/yj.png);">
					</view>
					<view class="smartbh-label-all-title">压浆检测</view>
				</view>
				<view class="after-add">
					<view class="smartbh-label-all-img"></view>
					<view class="smartbh-label-all-title"></view>
				</view>
				<view class="after-add">
					<view class="smartbh-label-all-img"></view>
					<view class="smartbh-label-all-title"></view>
				</view>
				<view class="after-add">
					<view class="smartbh-label-all-img"></view>
					<view class="smartbh-label-all-title"></view>
				</view>
				<view class="after-add">
					<view class="smartbh-label-all-img"></view>
					<view class="smartbh-label-all-title"></view>
				</view>
			</view>
			<!-- 智慧试验 -->

		</view>


	</view>
</template>

<script>
	export default {
		data() {
			return {

			}
		},
		methods: {
			handleNav(e) {
				if (e == 0) {
					uni.navigateTo({
						url: '/pages/theory/theory'
					})
				}
				if (e == 3) {
					uni.navigateTo({
						url: '/pages/Rawmaterial/Statistics'
					})
				}
				if (e == 15) {
					uni.navigateTo({
						url: '/pages/Entitydetection/Entitydetection'
					})
				}
				if (e == 1) {
					uni.navigateTo({
						url: '/pages/smartsy/shigongphb/shigongphbAdd'
					})
				}
				if (e == 2) {
					uni.navigateTo({
						url: '/pages/smartsy/shigongphb/shigongphbManage'
					})
				}
			},
			handleNavs(e) {
				if (e == 4) {
					uni.navigateTo({
						url: '/pages/smartbh/pourorder/pourAdd'
					})
				}
				if (e == 5) {
					uni.navigateTo({
						url: '/pages/smartbh/pourorder/pourManage'
					})
				}
			}
		}
	}
</script>

<style lang="scss" scoped>
	.smartbh {
		width: 99%;
		margin: 0 auto;
		/* height: 50%; */
		// flex: 1;
		// border: 1px solid #fff;
		background-color: #fff;

		// margin-top: 1px;
		&-message {
			color: #000000;
			font-size: 21px;
			margin-left: 17px;
			font-weight: Bold;
			font-family: "PingFang-SC-Bold";
		}

		&-label {
			width: 100%;
			// height: 400upx;
			// background-color: #E45656;
			margin: 8px auto;
			display: flex;
			flex-wrap: wrap;
			justify-content: space-around;

			&-label:not(:nth-child(4n)) {
				margin-right: calc(4% / 3);
			}

			&-all {
				width: 150upx;
				height: 130upx;
				// border: 1px solid pink;

				&-img {
					width: 60upx;
					height: 60upx;
					margin: 0 auto;
					background-size: 100% 100%;
					background-repeat: no-repeat;
					// margin: 0 auto;
				}

				&-title {
					// height: 27px;
					text-align: center;
					font-size: 10px;
					padding-top: 5px;
					font-family: "PingFang SC";
					font-weight: 500;
					color: #333333;
					// line-height: 32px;
				}
			}
		}

		.after-add {
			height: 0;
			padding: 0;
			margin: 0 //之前li设定值 0 之前li设定值;
		}
	}
</style>
