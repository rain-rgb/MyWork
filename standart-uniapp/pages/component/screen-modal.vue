<template>
	<view class="screen" v-if="show" :content='content' @confirm="confirm">
		<view class="screen-modal">
			<view class="screen-modal-item">
				<view class="screen-modal-item-name">工程名称：</view>
				<view class="screen-modal-item-input">
					<picker @change="ChangeTestType" :range="testType">
						<u--input placeholder="请选择工程名称" border="surround" v-model="value" disabled @change="change"
							suffixIcon="arrow-down"></u--input>
					</picker>
				</view>
			</view>
			<view class="screen-modal-item">
				<view class="screen-modal-item-name">时间：</view>
				<view class="screen-modal-item-input">
					<u--input placeholder="请选择时间" border="surround" v-model="value" disabled @change="change"
						suffixIcon="arrow-down"></u--input>
				</view>
			</view>
			<view class="screen-modal-btn">
				<u-button text="取消" @click="confirm"></u-button>
				<u-button type="primary" text="确认" @click="handleOk"></u-button>
			</view>
		</view>
	</view>
</template>

<script>
</script>

<style>
	.screen {
		position: fixed;
		background: rgba(000, 000, 000, .3);
		left: 0;
		top: 0;
		width: 100%;
		height: 100%;

		&-modal {
			background-color: #FFFFFF;
			position: absolute;
			top: 50%;
			left: (750upx - 690upx) / 2;
			width: 690upx;
			transform: translateY(-50%);
			box-shadow: 0 0 20px 0 rgba(0, 0, 0, 0.1);
			border-radius: 12upx;
			padding: 20upx;

			&-item {
				text-align: left;
				// width: 90%;
				margin-bottom: 30upx;

				&-name {
					color: #4C5971;
					font-size: 30upx;
					margin-bottom: 30upx;
				}

				&-input {}
			}

			&-btn {
				display: flex;
			}
		}


	}
</style>
