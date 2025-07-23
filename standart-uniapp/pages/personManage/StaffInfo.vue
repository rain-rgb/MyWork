<template>
  <view class="detail-container">
    <!-- 头部信息卡 -->
    <view class="header-card">
      <view class="avatar-section">
        <u-avatar size="120" src="/static/avatar-manager.png"></u-avatar>
      </view>
      <view class="title-group">
        <text class="name">{{ detailData.xingming || '未登记姓名' }}</text>
       <view class="meta-info">
         <view class="info-line">
           <text>人员ID：{{ detailData.userid || '无' }}</text>
         </view>
         <view class="info-line">
           <text>职务：{{ detailData.zhiwu || '无' }}</text>
         </view>
       </view>
      </view>
    </view>

    <!-- 核心信息模块 -->
    <view class="info-section">
      <!-- 身份信息 -->
      <view class="info-card">
        <view class="card-header">
          <u-icon name="account" size="24"></u-icon>
          <text>身份信息</text>
        </view>
        <view class="info-grid">
          <view class="info-item">
            <label>证件号码：</label>
            <text>{{ detailData.shenfenz || '未登记' }}</text>
          </view>
          <view class="info-item">
            <label>人员类型：</label>
            <text>{{ detailData.renyuantype || '管理人员' }}</text>
          </view>
          <view class="info-item">
            <label>联系方式：</label>
            <text>{{ detailData.dianhua || '未登记' }}</text>
          </view>
        </view>
      </view>

      <!-- 项目信息 -->
      <view class="info-card">
        <view class="card-header">
          <u-icon name="home-fill" size="24"></u-icon>
          <text>项目信息</text>
        </view>
        <view class="info-grid">
          <view class="info-item">
            <label>所属项目：</label>
            <text class="project-name">{{ detailData.sysOrgCode_dictText }}</text>
          </view>
          <view class="info-item">
            <label>建设单位：</label>
            <text>{{ detailData.jianshechu || '未登记' }}</text>
          </view>
          <view class="info-item">
            <label>法人单位：</label>
            <text>{{ detailData.farendanwei || '未关联' }}</text>
          </view>
        </view>
      </view>

<!-- 考勤记录卡片 -->
<view class="info-card">
  <!-- 标题栏 -->
  <view class="card-header">
    <text class="iconfont icon-clock"></text>
    <text class="card-title">最新考勤</text>
  </view>

  <!-- 加载状态 -->
  <view v-if="loading" class="custom-loading">
    <view class="loading-spinner"></view>
    <text>数据加载中...</text>
  </view>

  <!-- 考勤列表 -->
  <view v-else class="attendance-list">
    <view v-if="attendanceList.length === 0" class="empty-tips">
      <text>暂无打卡记录</text>
    </view>

    <view 
      v-for="(item, index) in attendanceList" 
      :key="index" 
      class="attendance-item"
    >
      <view class="status-icon">
        <text class="iconfont icon-check"></text>
      </view>
      <view class="time-info">
        <text class="date">{{ formatDate(item.kaoqriq) }}</text>
        <text class="time">{{ formatTime(item.kaoqriq) }}</text>
      </view>
      <text class="status-tag success">打卡成功</text>
    </view>
  </view>
</view>
    </view>
  </view>
</template>

<script>
export default {
 // 1. data
 data() {
   return {
     detailData: {},
     attendanceList: [],
     loading: false
   };
 },
 
 // 2. 页面 / 组件 钩子 —— **都跟 computed、methods 平级**
 onLoad(options) {
   this.loadData(decodeURIComponent(options.params));
   this.$nextTick(() => this.loadAttendance());
 },
 created() {
   // 如果你想在 H5 端也执行，可留
   this.loadAttendance();
 },
 mounted() {
   // 如果只在小程序端执行可留
   // this.loadAttendance();
 },
 
 // 3. computed 块 —— 一定要跟上面的钩子之间有逗号
 computed: {
   hasProjectInfo() {
     const t = this.detailData.sysOrgCode_dictText;
     return t && t !== 'null' && t !== 'undefined';
   }
 },
 
 // 4. methods
  methods: {
	  async loadAttendance() {
	    if (!this.detailData.shenfenz) return;
	    this.loading = true;
	    try {
	      const response = await this.$http.get(`/staffbase/staffBaseWork/list`, {
	        params: {  // 统一通过 params 对象传参
	          shenfenz: this.detailData.shenfenz,  // 无需手动编码，封装层会自动处理
	          column: 'kaoqriq',   // 排序字段
	          order: 'desc',       // 排序方式
	          pageSize: 3,         // 保持原有分页大小
	          _: Date.now()        // 时间戳防缓存
	        },
	        header: { 
	          'Cache-Control': 'no-cache'  // 保留缓存控制
	        }
	      });
	  
	      if (response.data?.success) {
	        this.attendanceList = response.data.result.records || [];
	        uni.showToast({ title: '读取到数据', icon: 'none' });
	      } else {
	        this.attendanceList = [];
	        uni.showToast({ title: '数据为空', icon: 'none' });
	      }
	    } catch (e) {
	      uni.showToast({ title: '数据加载失败', icon: 'none' });
	    } finally {
	      this.loading = false;
	    }
	  },
	  
	  formatDate(time) {
	    return time ? time.split(' ')[0] : '--';
	  },
	  
	  formatTime(time) {
	    return time ? (time.split(' ')[1] || '').substr(0, 5) : '--';
	  },
    loadData(params) {
      try {
        this.detailData = JSON.parse(params)
      } catch(e) {
        uni.showToast({ title: '数据解析失败', icon: 'error' })
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.detail-container {
  padding: 24rpx;
  
  .header-card {
    background: linear-gradient(135deg, #409EFF, #337ecc);
    border-radius: 16rpx;
    padding: 32rpx;
    display: flex;
    align-items: center;
    
    .avatar-section {
      margin-right: 32rpx;
    }
    
    .title-group {
      .name {
        font-size: 40rpx;
        color: #fff;
        margin-bottom: 16rpx;
        display: block;
      }
      
      .meta-info {
        font-size: 24rpx;
        color: rgba(255,255,255,0.8);
        
        .divider {
          margin: 0 16rpx;
        }
      }
    }
  }

  .info-section {
    margin-top: 40rpx;
    
    .info-card {
      background: #fff;
      border-radius: 12rpx;
      margin-bottom: 32rpx;
      box-shadow: 0 4rpx 12rpx rgba(0,0,0,0.05);
      
      .card-header {
        padding: 24rpx;
        border-bottom: 1rpx solid #eee;
        display: flex;
        align-items: center;
        
        text {
          margin-left: 16rpx;
          font-weight: 500;
        }
      }
      
      .info-grid {
        padding: 24rpx;
        
        .info-item {
          display: flex;
          justify-content: space-between;
          padding: 20rpx 0;
          
          label {
            color: #666;
            min-width: 160rpx;
          }
          
          .project-name {
            color: #409EFF;
            font-weight: 500;
          }
        }
      }
    }
  }
}
/* 自定义图标 */
.iconfont {
  font-family: "iconfont" !important;
  font-size: 24rpx;
}

/* 加载动画 */
.custom-loading {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 30rpx;
  
  .loading-spinner {
    width: 40rpx;
    height: 40rpx;
    border: 4rpx solid #ddd;
    border-top-color: #2979ff;
    border-radius: 50%;
    animation: spin 1s linear infinite;
    margin-right: 15rpx;
  }
}

/* 考勤项样式 */
.attendance-item {
  display: flex;
  align-items: center;
  padding: 25rpx;
  border-bottom: 1rpx solid #eee;

  .status-icon {
    width: 50rpx;
    .icon-check {
      color: #4cd964;
      font-size: 36rpx;
    }
  }

  .time-info {
    flex: 1;
    display: flex;
    flex-direction: column;
    .date {
      font-size: 28rpx;
      color: #333;
    }
    .time {
      font-size: 24rpx;
      color: #666;
    }
  }

  .status-tag {
    padding: 8rpx 15rpx;
    border-radius: 6rpx;
    &.success {
      background: #e8f5e9;
      color: #4caf50;
    }
  }
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}
</style>