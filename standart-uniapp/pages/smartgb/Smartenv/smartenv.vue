<template>
  <view class="container">
    <!-- 导航栏 -->
    <view class="custom-navbar">
      <view class="nav-back" @click="goToLabellist">
        <uni-icons type="arrowleft" size="24" color="#333"></uni-icons>
      </view>
      <view class="navbar-title">环境监测数据</view>
      <view class="nav-placeholder"></view>
    </view>
    
    <!-- 设备选择区域 -->
    <view class="select-box">
      <view class="Task">
        设备名称:
        <view class="Task-input">
          <eq :sbtype="15" @choose="handleDeviceSelect"></eq>
        </view>
      </view>
    </view>
    
    <!-- 环境数据列表 -->
    <scroll-view 
      class="data-list"
      scroll-y="true"
      @scrolltolower="loadMore"
    >
      <!-- 数据卡片 -->
      <view 
        v-for="(item, index) in dataList" 
        :key="index" 
        class="data-item"
      >
        <view class="item-header">
          <view class="device-name">{{ item.devaddr_dictText || '环境监测设备' }}</view>
          <view class="time-value">{{ formatDateTime(item.timevalue) }}</view>
        </view>
        
        <view class="parameters-grid">
          <!-- 空气质量 -->
          <view class="parameter-item pm-item">
            <div class="param-name">PM2.5</div>
            <div class="param-value">
              <span>{{ item.pm25 || '--' }}</span>
              <span class="param-unit">μg/m³</span>
            </div>
          </view>
          
          <view class="parameter-item pm-item">
            <div class="param-name">PM10</div>
            <div class="param-value">
              <span>{{ item.pm10 || '--' }}</span>
              <span class="param-unit">μg/m³</span>
            </div>
          </view>
          
          <view class="parameter-item pm-item">
            <div class="param-name">TSP</div>
            <div class="param-value">
              <span>{{ item.tsp || '--' }}</span>
              <span class="param-unit">μg/m³</span>
            </div>
          </view>
          
          <!-- 噪音和天气 -->
          <view class="parameter-item noise-item">
            <div class="param-name">噪音水平</div>
            <div class="param-value">
              <span>{{ item.noise || '--' }}</span>
              <span class="param-unit">分贝</span>
            </div>
          </view>
          
          <view class="parameter-item weather-item">
            <div class="param-name">温度</div>
            <div class="param-value">
              <span>{{ item.temperature || '--' }}</span>
              <span class="param-unit">℃</span>
            </div>
          </view>
          
          <view class="parameter-item weather-item">
            <div class="param-name">湿度</div>
            <div class="param-value">
              <span>{{ item.humidity || '--' }}</span>
              <span class="param-unit">%</span>
            </div>
          </view>
          
          <!-- 其他参数 -->
          <view class="parameter-item env-item">
            <div class="param-name">大气压</div>
            <div class="param-value">
              <span>{{ item.atmpressure || '--' }}</span>
              <span class="param-unit">kPa</span>
            </div>
          </view>
          
          <view class="parameter-item weather-item">
            <div class="param-name">风速</div>
            <div class="param-value">
              <span>{{ item.windspeed || '--' }}</span>
              <span class="param-unit">m/s</span>
            </div>
          </view>
          
          <view class="parameter-item weather-item">
            <div class="param-name">风向</div>
            <div class="param-value">
              <uni-icons type="sound" size="16" color="#4a6cf7" />
              <span>{{ item.winddirection || item.wind || '--' }}</span>
            </div>
          </view>
        </view>
      </view>
      
      <!-- 加载状态 -->
      <view class="loading-section" v-if="loading">
        <view class="loading-spinner"></view>
        <text class="loading-text">加载中...</text>
      </view>
      
      <view v-if="noMore && dataList.length > 0" class="no-more-text">
        已加载全部数据
      </view>
      
      <!-- 空状态 -->
      <view v-if="!selectedDevice && dataList.length === 0" class="empty-state">
        <uni-icons type="cloud" size="60" color="#b0b0b0" />
        <text class="empty-text">请选择设备查看环境监测数据</text>
      </view>
      
      <view 
        v-if="selectedDevice && dataList.length === 0 && !loading" 
        class="empty-state"
      >
        <uni-icons type="info" size="60" color="#b0b0b0" />
        <text class="empty-text">该设备暂无环境监测数据</text>
      </view>
    </scroll-view>
  </view>
</template>

<script>
// 引入eq组件
import eq from '../../component/equipment/equipment.vue'

export default {
  components: {
    eq // 注册eq组件
  },
  data() {
    return {
      selectedDevice: null,
      dataList: [],
      currentPage: 1,
      pageSize: 10,
      total: 0,
      loading: false,
      noMore: false
    };
  },
  onLoad() {
    // 页面加载时获取所有设备的数据
    this.fetchData(true);
  },
  methods: {
    // 设备选择事件 - 修复筛选功能
    handleDeviceSelect(device) {
      console.log('选择的设备:', device);
      if (device ) {
        // 保存完整的设备对象
        this.selectedDevice = device;
        // 重置数据并加载所选设备的数据
        this.fetchData(true);
      } else {
        // 如果取消选择，则重新加载所有数据
        this.selectedDevice = null;
        this.fetchData(true);
      }
    },
    
    // 返回按钮 - 修改为返回Labellist页面
    goToLabellist() {
      uni.navigateTo({
        url: '/pages/Labellist/Labellist'
      });
    },
    
    // 格式化时间显示
    formatDateTime(dateStr) {
      if (!dateStr) return '';
      const date = new Date(dateStr);
      return date.toLocaleString('zh-CN', {
        year: 'numeric',
        month: '2-digit',
        day: '2-digit',
        hour: '2-digit',
        minute: '2-digit'
      }).replace(/\//g, '-');
    },
    
    // 获取环境数据 - 修复设备筛选
    async fetchData(reset = false) {
      if (this.loading) return;
      
      if (reset) {
        this.currentPage = 1;
        this.dataList = [];
        this.noMore = false;
      }
      
      this.loading = true;
      
      try {
        // 构建请求参数
        const params = {
          column: 'id',
          order: 'desc',
          pageNo: this.currentPage,
          pageSize: this.pageSize
        };
        
        // 如果有选择的设备，添加设备编号参数 (使用sbjno)
        if (this.selectedDevice ) {
          params.devaddr = this.selectedDevice;
          console.log('请求参数(带设备筛选):', params);
        } else {
          console.log('请求参数(所有设备):', params);
        }
        
        const res = await this.$http.get('/devicehistory/devicehistory/list', { params });
        
        if (res.data.success && res.data.result) {
          const result = res.data.result;
          this.total = result.total || 0;
          
          if (reset) {
            this.dataList = result.records || [];
          } else {
            this.dataList = [...this.dataList, ...(result.records || [])];
          }
          
          this.noMore = this.dataList.length >= this.total;
          console.log('数据加载成功:', this.dataList.length, '条记录');
        } else {
          console.error('数据加载失败:', res.data.message);
          uni.showToast({
            title: res.data.message || '数据加载失败',
            icon: 'none'
          });
        }
      } catch (error) {
        console.error('环境数据请求异常:', error);
        uni.showToast({
          title: '请求异常，请稍后再试',
          icon: 'none'
        });
      } finally {
        this.loading = false;
      }
    },
    
    // 加载更多
    loadMore() {
      if (!this.noMore && !this.loading) {
        this.currentPage++;
        this.fetchData();
      }
    }
  }
};
</script>

<style lang="scss">
.container {
  display: flex;
  flex-direction: column;
  background-color: #f5f7fa;
  min-height: 100vh;
  padding-bottom: env(safe-area-inset-bottom);
}

/* 顶部导航栏 */
.custom-navbar {
  display: flex;
  align-items: center;
  justify-content: space-between;
  height: 44px;
  padding: 0 16px;
  background-color: #fff;
  border-bottom: 1px solid #f0f0f0;
}

.nav-back {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
}

.navbar-title {
  font-size: 17px;
  font-weight: 600;
  color: #333;
}

.nav-placeholder {
  width: 40px;
}

/* 设备选择框 */
.select-box {
  background-color: #fff;
  padding: 16px;
  margin-bottom: 8px;
  border-radius: 10px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
}

.Task {
  background: #f5f7fa;
  border-radius: 8px;
  padding: 8px 12px;
  display: flex;
  align-items: center;
  color: #666;
  font-size: 16px;
}

.Task-input {
  flex: 1;
  margin-left: 10px;
}

/* 数据列表 */
.data-list {
  flex: 1;
  padding: 0 16px;
  padding-bottom: 20px;
}

/* 数据卡片 - 移除左侧边框 */
.data-item {
  background-color: #fff;
  border-radius: 12px;
  padding: 16px;
  margin-bottom: 16px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
}

.item-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 12px;
  padding-bottom: 12px;
  border-bottom: 1px solid #f0f0f0;
}

.device-name {
  font-size: 16px;
  font-weight: 600;
  color: #333;
}

.time-value {
  font-size: 12px;
  color: #999;
}

.parameters-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 12px;
  margin-bottom: 8px;
}

.parameter-item {
  background-color: #f9f9ff;
  padding: 10px;
  border-radius: 8px;
}

.param-name {
  font-size: 13px;
  color: #666;
  margin-bottom: 4px;
}

.param-value {
  font-size: 16px;
  font-weight: bold;
  color: #333;
  display: flex;
  align-items: center;
}

.param-unit {
  font-size: 12px;
  font-weight: normal;
  margin-left: 4px;
  color: #666;
}

/* 分类颜色 */
.pm-item {
  background-color: rgba(255, 193, 7, 0.1);
}

.weather-item {
  background-color: rgba(33, 150, 243, 0.1);
}

.noise-item {
  background-color: rgba(0, 188, 212, 0.1);
}

.env-item {
  background-color: rgba(139, 195, 74, 0.1);
}

/* 加载状态 */
.loading-section {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 30px 0;
}

.loading-spinner {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  border: 3px solid #e0e0e0;
  border-top-color: #4a6cf7;
  animation: spin 1s linear infinite;
  margin-bottom: 10px;
}

@keyframes spin {
  to { transform: rotate(360deg); }
}

.loading-text {
  font-size: 14px;
  color: #999;
}

.no-more-text {
  display: block;
  text-align: center;
  font-size: 14px;
  color: #999;
  padding: 20px 0;
}

/* 空状态 */
.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 60px 20px;
  text-align: center;
}

.empty-text {
  font-size: 15px;
  color: #999;
  margin-top: 20px;
}
</style>