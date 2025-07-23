<template>
  <view class="device-list-container">
    <!-- 顶部导航栏 -->
    <view class="custom-navbar">
      <view class="nav-back" @click="goBack">
        <uni-icons type="back" size="24" color="#000"></uni-icons>
      </view>
      <view class="navbar-title">设备监测列表</view>
      <view class="nav-placeholder"></view>
    </view>
    
    <!-- 筛选按钮 -->
    <view class="filter-tabs">
      <view 
        class="filter-tab" 
        :class="{'active': activeFilter === null}"
        @click="changeFilter(null)"
      >
        全部
      </view>
      <view 
        class="filter-tab" 
        :class="{'active': activeFilter === 'online'}"
        @click="changeFilter('online')"
      >
        在线
      </view>
      <view 
        class="filter-tab" 
        :class="{'active': activeFilter === 'offline'}"
        @click="changeFilter('offline')"
      >
        离线
      </view>
    </view>
    
    <!-- 设备列表 -->
    <scroll-view scroll-y class="device-list" @scrolltolower="loadMore">
      <!-- 设备卡片 -->
      <view 
        v-for="(item, index) in filteredList" 
        :key="item.id" 
        class="device-item"
        @click="handleDeviceClick(item)"
      >
        <!-- 设备名称和状态 -->
        <view class="device-header">
          <view class="device-name">{{ item.shebeino_dictText || '未知设备' }}</view>
          <view class="device-status" :class="isOnline(item.datatime) ? 'online' : 'offline'">
            {{ isOnline(item.datatime) ? '在线' : '离线' }}
          </view>
        </view>
        
        <!-- 监测内容网格布局 -->
        <view class="monitor-content">
          <view v-if="parsedData[item.id] && Object.keys(parsedData[item.id]).length > 0" class="json-grid">
            <view 
              v-for="(value, key) in parsedData[item.id]" 
              :key="key" 
              class="json-item"
            >
              <view class="json-label">{{ key }}：</view>
              <view class="json-value">{{ value }}</view>
            </view>
          </view>
          <view v-else class="no-data">无监测数据</view>
        </view>
        
        <!-- 设备底部信息 -->
        <view class="device-footer">
          <uni-icons type="calendar" size="12" color="#999"></uni-icons>
          <text class="time-text">{{ formatDateTime(item.datatime) }}</text>
        </view>
      </view>
      
      <!-- 加载提示 -->
      <view class="load-more">
        <view v-if="loading" class="loading-section">
          <view class="loading-spinner"></view>
          <text class="loading-text">加载中...</text>
        </view>
        <text v-else-if="noMore" class="no-more-text">已加载全部</text>
      </view>
      
      <!-- 空状态 -->
      <view v-if="filteredList.length === 0 && !loading" class="empty-state">
        <view class="empty-icon">
          <text class="iconfont icon-empty"></text>
        </view>
        <text class="empty-text">暂无设备数据</text>
      </view>
    </scroll-view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      activeFilter: null,
      deviceList: [],
      parsedData: {},
      currentPage: 1,
      pageSize: 10,
      total: 0,
      loading: false,
      noMore: false,
      isRequesting: false
    };
  },
  computed: {
    filteredList() {
      if (!this.activeFilter) return this.deviceList;
      
      return this.deviceList.filter(item => {
        const onlineStatus = this.isOnline(item.datatime);
        return (this.activeFilter === 'online') ? onlineStatus : !onlineStatus;
      });
    }
  },
  onLoad() {
    this.loadDeviceList();
  },
  methods: {
    // 返回上一页
    goBack() {
      uni.navigateTo({
        url: '/pages/Labellist/Labellist'
      });
    },
    
    // 时间格式化
    formatDateTime(timeStr) {
      if (!timeStr) return '';
      const date = new Date(timeStr);
      
      const year = date.getFullYear();
      const month = (date.getMonth() + 1).toString().padStart(2, '0');
      const day = date.getDate().toString().padStart(2, '0');
      const hours = date.getHours().toString().padStart(2, '0');
      const minutes = date.getMinutes().toString().padStart(2, '0');
      
      return `${year}-${month}-${day} ${hours}:${minutes}`;
    },
    
    // 切换筛选状态
    changeFilter(status) {
      this.activeFilter = status;
    },
    
    // 判断是否是围堰设备
    isWeiyanDevice(item) {
      return item.shebeino_dictText && item.shebeino_dictText.includes('围堰');
    },
    
    // 处理设备点击事件
    handleDeviceClick(item) {
      if (this.isWeiyanDevice(item)) {
        if (!item.shebeino || !item.jiancebianma) {
          uni.showToast({
            title: '设备缺少必要参数',
            icon: 'none'
          });
          return;
        }
        
        // 构建完整的参数对象
        const params = {
          shebeino: item.shebeino,
          jiancebianma: item.jiancebianma,
          jiancetype: item.jiancetype,
        };
        
        // 使用URLSearchParams构建参数字符串
        const queryString = new URLSearchParams(params).toString();
        
        // 跳转到曲线详情页并携带所有参数
        uni.navigateTo({
          url: `/pages/smartgb/DeviceCheck/DeviceCurve?${queryString}`
        });
      } else {
        uni.showToast({
          title: '当前项目查询功能暂未开放',
          icon: 'none'
        });
      }
    },
    
    // 加载设备列表
    async loadDeviceList() {
      if (this.isRequesting || this.noMore) return;
      
      this.isRequesting = true;
      this.loading = true;
      
      try {
        const res = await this.$http.get('/jikeng/jikengWeiyCfg/list', {
          params: {
            sys_depart_orgcode: uni.getStorageSync('USER_INFO').orgCode,
            column: 'id',
            order: 'desc',
            pageNo: this.currentPage,
            pageSize: this.pageSize
          }
        });
        
        if (res.data.success) {
          const result = res.data.result;
          this.total = result?.total || 0;
          const newData = result?.records || [];
          
          // 处理JSON格式的监测内容
          newData.forEach(item => {
            try {
              this.$set(this.parsedData, item.id, JSON.parse(item.jsondata || '{}'));
            } catch (e) {
              this.$set(this.parsedData, item.id, {});
              console.error('JSON解析失败', e);
            }
          });
          
          // 分页数据合并
          if (this.currentPage === 1) {
            this.deviceList = newData;
          } else {
            const existingIds = new Set(this.deviceList.map(d => d.id));
            const uniqueNewData = newData.filter(item => !existingIds.has(item.id));
            this.deviceList = [...this.deviceList, ...uniqueNewData];
          }
          
          // 检查是否还有更多数据
          this.noMore = this.deviceList.length >= this.total;
        } else {
          uni.showToast({
            title: res.data.message || '数据加载失败',
            icon: 'none'
          });
        }
      } catch (error) {
        console.error('请求异常', error);
        uni.showToast({
          title: '网络请求异常',
          icon: 'none'
        });
      } finally {
        this.isRequesting = false;
        this.loading = false;
      }
    },
    
    // 加载更多
    loadMore() {
      if (!this.noMore) {
        this.currentPage += 1;
        this.loadDeviceList();
      }
    },
    
    // 判断设备状态
    isOnline(datetime) {
      if (!datetime) return false;
      
      try {
        const now = new Date().getTime();
        const deviceTime = new Date(datetime).getTime();
        return now - deviceTime <= 3600000;
      } catch (e) {
        return false;
      }
    }
  }
};
</script>

<style lang="scss">
.device-list-container {
  display: flex;
  flex-direction: column;
  height: 100vh;
  background-color: #f7f7f7;
  
  .custom-navbar {
    display: flex;
    align-items: center;
    justify-content: space-between;
    height: 44px;
    background-color: #ffffff;
    padding: 0 12px;
    box-shadow: 0 1px 3px rgba(0, 0, 0, 0.05);
    position: sticky;
    top: 0;
    z-index: 10;
    
    .nav-back {
      width: 40px;
      height: 40px;
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
      height: 40px;
    }
  }
  
  .filter-tabs {
    display: flex;
    padding: 10px 12px;
    background-color: #fff;
    position: sticky;
    top: 44px;
    z-index: 9;
    box-shadow: 0 1px 3px rgba(0, 0, 0, 0.05);
    
    .filter-tab {
      flex: 1;
      text-align: center;
      padding: 8px 0;
      margin: 0 4px;
      border-radius: 15px;
      background-color: #f5f5f5;
      font-size: 14px;
      color: #606266;
      transition: all 0.3s;
      
      &.active {
        background-color: #1890ff;
        color: #fff;
        font-weight: 500;
      }
    }
  }
  
  .device-list {
    flex: 1;
    padding: 10px 12px;
    background-color: #f7f7f7;
    height: calc(100vh - 110px);
    overflow-y: auto;
  }
  
  .device-item {
    background: #fff;
    border-radius: 8px;
    margin-bottom: 12px;
    padding: 16px;
    box-shadow: 0 1px 3px rgba(0, 0, 0, 0.05);
    
    .device-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 12px;
      
      .device-name {
        font-size: 16px;
        font-weight: bold;
        color: #333;
        flex: 1;
        white-space: nowrap;
        overflow: hidden;
        text-overflow: ellipsis;
        padding-right: 10px;
      }
      
      .device-status {
        font-size: 13px;
        padding: 4px 8px;
        border-radius: 12px;
        min-width: 50px;
        text-align: center;
        font-weight: 500;
        
        &.online {
          background-color: #e8f7f0;
          color: #07c160;
        }
        
        &.offline {
          background-color: #f8f8f8;
          color: #999;
        }
      }
    }
    
    .monitor-content {
      margin-bottom: 12px;
      padding: 8px 0;
      
      .json-grid {
        display: flex;
        flex-wrap: wrap;
        margin: 0 -5px;
      }
      
      .json-item {
        width: calc(33.333% - 10px);
        margin: 0 5px 10px;
        padding: 10px;
        background: #f9f9f9;
        border-radius: 6px;
        text-align: center;
      }
      
      .json-label {
        font-size: 12px;
        color: #666;
        margin-bottom: 4px;
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;
      }
      
      .json-value {
        font-size: 14px;
        font-weight: 500;
        color: #1890ff;
      }
      
      .no-data {
        text-align: center;
        color: #999;
        padding: 10px 0;
      }
    }
    
    .device-footer {
      display: flex;
      align-items: center;
      font-size: 12px;
      color: #999;
      padding-top: 10px;
      border-top: 1px dashed #eee;
    }
  }
  
  .load-more {
    text-align: center;
    padding: 15px 0;
    font-size: 14px;
    
    .loading-section {
      display: flex;
      justify-content: center;
      align-items: center;
    }
    
    .loading-spinner {
      width: 16px;
      height: 16px;
      border: 2px solid #f0f0f0;
      border-top: 2px solid #1890ff;
      border-radius: 50%;
      margin-right: 8px;
      animation: spin 1s linear infinite;
    }
    
    .loading-text,
    .no-more-text {
      color: #999;
    }
  }
  
  .empty-state {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    padding: 60px 20px;
    text-align: center;
    min-height: 300px;
    
    .empty-icon {
      width: 80px;
      height: 80px;
      font-size: 80px;
      color: #e0e0e0;
      margin-bottom: 16px;
    }
    
    .empty-text {
      font-size: 15px;
      color: #999;
    }
  }
  
  @keyframes spin {
    0% { transform: rotate(0deg); }
    100% { transform: rotate(360deg); }
  }
}
</style>