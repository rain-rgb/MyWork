<template>
  <view class="container">
    <!-- 状态指示 -->
    <view class="status-banner" :class="zhengchangClass">
      {{ statusText }}
    </view>

    <!-- 设备核心信息 -->
    <view class="info-card">
      <view class="info-row">
        <text class="label">设备名称：</text>
        <text class="value">{{ currentDevice.shebeino_dictText }}</text>
      </view>
      <view class="info-row">
        <text class="label">设备编号：</text>
        <text class="value">{{ currentDevice.shebeino }}</text>
      </view>
    </view>

    <!-- 检查详情 -->
    <view class="info-card">
      <view class="info-group">
        <text class="sub-title">检查记录</text>
        <view class="info-row">
          <text class="label">检查方式：</text>
          <text class="value">{{ currentDevice.checkway || '未记录' }}</text>
        </view>
        <view class="info-row">
          <text class="label">检查人员：</text>
          <text class="value">{{ currentDevice.createBy_dictText }}</text>
        </view>
        <view class="info-row">
          <text class="label">检查时间：</text>
          <text class="value">{{ formatTime(currentDevice.createTime) }}</text>
        </view>
      </view>

      <view class="divider"></view>

      <view class="info-group">
        <text class="sub-title">详细内容</text>
        <view class="info-column">
          <text class="label">状况描述：</text>
          <text class="long-text">{{ currentDevice.miaoshu || '无异常报告' }}</text>
        </view>
        <view class="info-column">
          <text class="label">检查内容：</text>
          <text class="long-text">{{ currentDevice.checkcontent }}</text>
        </view>
      </view>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      currentDevice: {} // 初始化空对象
    }
  },
  computed: {
    zhengchangClass() {
      return this.currentDevice.zhengchang === 1 ? 'fault' : 'normal'
    },
    statusText() {
      return this.currentDevice.zhengchang === 1 ? '故障待处理' : '运行正常'
    }
  },
  onLoad(options) {
    this.loadDeviceData(options)
  },
  methods: {
    loadDeviceData(options) {
      try {
        if (options.payload) {
          const rawData = decodeURIComponent(options.payload)
          this.currentDevice = JSON.parse(rawData)
          uni.setNavigationBarTitle({ 
            title: this.currentDevice.shebeino_dictText 
          })
        }
      } catch (e) {
        console.error('数据加载失败:', e)
        uni.showToast({ title: '数据加载失败', icon: 'none' })
      }
    },
    formatTime(timeStr) {
      return timeStr ? timeStr.replace(' ', '  ') : '未记录时间'
    }
  }
}
</script>

<style lang="scss" scoped>
.container {
  background: #f8f9fa;
  min-height: 100vh;
  padding: 20rpx;
}

.status-banner {
  padding: 30rpx;
  margin: 20rpx;
  border-radius: 12rpx;
  font-size: 32rpx;
  font-weight: bold;

  &.normal {
    background: linear-gradient(135deg, #e8f5e9, #c8e6c9);
    color: #2e7d32;
  }

  &.fault {
    background: linear-gradient(135deg, #ffebee, #ffcdd2);
    color: #c62828;
  }
}

.info-card {
  background: white;
  border-radius: 16rpx;
  margin: 20rpx;
  padding: 24rpx;
  box-shadow: 0 4rpx 12rpx rgba(0,0,0,0.06);

  .sub-title {
    font-size: 30rpx;
    color: #333;
    font-weight: 500;
    margin-bottom: 24rpx;
    display: block;
  }
}

.info-row {
  display: flex;
  justify-content: space-between;
  padding: 16rpx 0;

  .label {
    color: #666;
    font-size: 28rpx;
    min-width: 160rpx;
  }

  .value {
    color: #333;
    font-size: 28rpx;
    flex: 1;
    text-align: right;
  }
}

.info-column {
  padding: 16rpx 0;

  .label {
    color: #666;
    font-size: 28rpx;
    margin-bottom: 8rpx;
    display: block;
  }

  .long-text {
    color: #333;
    font-size: 28rpx;
    line-height: 1.6;
    white-space: pre-wrap;
  }
}

.divider {
  height: 1rpx;
  background: #eee;
  margin: 24rpx 0;
}
</style>
