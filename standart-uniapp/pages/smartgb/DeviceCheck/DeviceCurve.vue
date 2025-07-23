<template>
  <view class="curve-container">
    <!-- 顶部导航栏 -->
    <view class="custom-navbar">
      <view class="nav-back" @click="goBack">
        <uni-icons type="back" size="24" color="#000"></uni-icons>
      </view>
      <view class="navbar-title">{{ deviceTitle }}</view>
      <view class="nav-placeholder"></view>
    </view>
    
    <!-- 筛选条件 -->
    <view class="filter-section">
      <view class="filter-item">
        <text class="filter-label">设备编号：</text>
        <text class="filter-value">{{ shebeino }}</text>
      </view>
      <view class="filter-item">
        <text class="filter-label">监测类型：</text>
        <text class="filter-value">{{ jiancetype }}</text>
      </view>
    </view>
    
    <!-- 图表容器 -->
    <view class="charts-container" v-if="parsedData.length > 0 && selectedFields.length > 0">
      <view 
        v-for="(field, index) in selectedFields" 
        :key="index" 
        class="single-chart-container"
      >
        <view class="chart-title">{{ field }}</view>
        <view :id="'curve-chart-' + field" class="curve-chart"></view>
      </view>
    </view>
    
    <!-- 指标选择器 -->
    <view class="indicator-selector" v-if="curveFields.length > 0">
      <view class="selector-title">选择指标：</view>
      <view class="indicator-tags">
        <view 
          v-for="(field, index) in curveFields" 
          :key="index" 
          class="tag-item"
          :class="{'active': selectedFields.includes(field)}"
          @click="toggleField(field)"
        >
          {{ field }}
        </view>
      </view>
    </view>
    
    <!-- 加载提示 -->
    <view class="load-more" v-if="loading">
      <view class="loading-section">
        <view class="loading-spinner"></view>
        <text class="loading-text">加载中...</text>
      </view>
    </view>
    
    <!-- 空状态 -->
    <view class="empty-state" v-if="!loading && curveData.length === 0">
      <view class="empty-icon">
        <text class="iconfont icon-empty"></text>
      </view>
      <text class="empty-text">暂无数据</text>
    </view>
  </view>
</template>

<script>
import * as echarts from 'echarts';

export default {
  data() {
    return {
	  jiancetype:'',
      shebeino: '',
      jiancebianma: '',
      deviceTitle: '',
      curveData: [],       // 原始数据
      parsedData: [],      // 解析后的多曲线数据
      curveFields: [],     // 所有曲线字段
      selectedFields: [],  // 当前选中的曲线字段
      loading: false,
      charts: {},          // 存储所有图表实例
      chartInitialized: false // 图表是否已初始化
    };
  },
  onLoad(options) {
    // 获取从设备列表页传递的参数
    this.shebeino = options.shebeino || '';
    this.jiancebianma = options.jiancebianma || ''; 
	this.jiancetype = options.jiancetype || '';
	this.deviceTitle = `${this.jiancetype}`;
    
    // 加载曲线数据
    this.loadCurveData();
  },
  onReady() {
    // 使用定时器确保DOM完全渲染后再初始化图表
    setTimeout(() => {
      this.initCharts();
      this.chartInitialized = true;
    }, 300);
  },
  onUnload() {
    // 销毁所有图表实例
    Object.values(this.charts).forEach(chart => {
      if (chart) {
        chart.dispose();
      }
    });
    this.charts = {};
  },
  watch: {
    // 监听parsedData变化，确保数据更新后重新渲染图表
    parsedData() {
      if (this.chartInitialized) {
        this.updateCharts();
      }
    },
    // 监听selectedFields变化，添加或删除图表
    selectedFields() {
      if (this.chartInitialized) {
        this.updateChartList();
      }
    }
  },
  methods: {
    // 返回按钮跳转到设备检查页面
    goBack() {
      uni.navigateTo({
        url: '/pages/smartgb/DeviceCheck/DeviceCheck'
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
    
    // 加载曲线数据
    async loadCurveData() {
      if (!this.shebeino || !this.jiancebianma) {
        uni.showToast({
          title: '缺少必要参数',
          icon: 'none'
        });
        return;
      }
      
      this.loading = true;
      
      try {
        const res = await this.$http.get('/jikeng/jikengWeiyInfo/list', {
          params: {
            column: 'id',
            order: 'desc',
            shebeino: this.shebeino,
            jiancebianma: this.jiancebianma,
			jiancetype: this.jiancetype,
            pageSize: 100
          }
        });
        
        if (res.data.success) {
          const records = res.data.result?.records || [];
          
          // 处理数据
          this.curveData = records.map(item => {
            return {
              datetime: item.datatime || '',
              jsondata: item.jsondata || '{}'
            };
          }).reverse(); // 反转数据，使时间最早的数据在前
          
          // 解析JSON数据并提取所有字段
          this.parseCurveData();
          
          // 如果图表已初始化，则更新图表
          if (this.chartInitialized) {
            this.updateCharts();
          }
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
        this.loading = false;
      }
    },
    
    // 解析JSON数据并提取所有字段
    parseCurveData() {
      // 清空数据
      this.parsedData = [];
      const allFields = new Set();
      
      // 解析每条数据的jsondata
      this.curveData.forEach(item => {
        try {
          const jsonData = JSON.parse(item.jsondata);
          const parsedItem = { datetime: item.datetime };
          
          // 提取所有字段
          Object.keys(jsonData).forEach(key => {
            allFields.add(key);
            // 提取数值部分（移除单位）
            const valueMatch = jsonData[key].match(/[-+]?\d*\.\d+|\d+/);
            parsedItem[key] = valueMatch ? parseFloat(valueMatch[0]) : 0;
          });
          
          this.parsedData.push(parsedItem);
        } catch (e) {
          console.error('JSON解析失败', e);
        }
      });
      
      // 转换为数组并设置默认选中的字段
      this.curveFields = Array.from(allFields);
      if (this.curveFields.length > 0) {
        // 默认选中所有字段
        this.selectedFields = [...this.curveFields];
      }
    },
    
    // 切换曲线字段显示
    toggleField(field) {
      if (this.selectedFields.includes(field)) {
        // 如果已选中，则取消选中
        this.selectedFields = this.selectedFields.filter(f => f !== field);
        
        // 销毁对应的图表
        if (this.charts[field]) {
          this.charts[field].dispose();
          delete this.charts[field];
        }
      } else {
        // 如果未选中，则添加到选中列表
        this.selectedFields.push(field);
        
        // 使用定时器确保DOM更新后再初始化新图表
        setTimeout(() => {
          this.initChart(field);
          this.updateChart(field);
        }, 100);
      }
    },
    
    // 更新图表列表
    updateChartList() {
      // 销毁已移除的图表
      Object.keys(this.charts).forEach(chartKey => {
        if (!this.selectedFields.includes(chartKey)) {
          this.charts[chartKey].dispose();
          delete this.charts[chartKey];
        }
      });
      
      // 初始化新增的图表
      this.selectedFields.forEach(field => {
        if (!this.charts[field]) {
          this.initChart(field);
        }
      });
      
      // 更新所有图表数据
      this.updateCharts();
    },
    
    // 初始化所有图表
    initCharts() {
      // 先销毁所有现有图表
      Object.values(this.charts).forEach(chart => {
        if (chart) {
          chart.dispose();
        }
      });
      this.charts = {};
      
      // 为每个选中的字段初始化图表
      this.selectedFields.forEach(field => {
        this.initChart(field);
      });
      
      // 如果有数据，更新图表
      if (this.parsedData.length > 0) {
        this.updateCharts();
      }
    },
    
// 初始化单个图表
initChart(field) {
  // 获取DOM元素
  const chartDom = document.getElementById(`curve-chart-${field}`);
  if (!chartDom) {
    console.error(`未找到图表DOM元素: curve-chart-${field}`);
    return;
  }
  
  // 为不同的曲线生成不同的颜色
  const colors = [
    '#1890ff', '#2fc25b', '#f5a623', '#e53935', '#722ed1', 
    '#00a2ae', '#ff7d00', '#a87328', '#f5222d', '#7cb305'
  ];
  const index = this.selectedFields.indexOf(field);
  const color = colors[index % colors.length];
  
  // 准备自定义的x轴标签索引
  const customIndices = [29, 49, 69, 89].filter(index => index < this.parsedData.length);
  
  // 初始化图表
  const chart = echarts.init(chartDom);
  this.charts[field] = chart;
  
  // 设置图表配置
  const option = {
    title: {
      show: false
    },
    tooltip: {
      trigger: 'axis',
      axisPointer: {
        type: 'cross',
        crossStyle: {
          color: '#999'
        }
      }
    },
    grid: {
      left: '3%',
      right: '4%',
      bottom: '3%',
      containLabel: true
    },
    xAxis: {
      type: 'category',
      boundaryGap: false,
      data: [],
      axisLabel: {
        interval: (index, value) => {
          return customIndices.includes(index);
        },
        rotate: 45,
        fontSize: 10
      }
    },
    yAxis: {
      type: 'value',
      axisLabel: {
        formatter: '{value}'
      },
      splitLine: {
        show: true,
        lineStyle: {
          type: 'dashed'
        }
      }
    },
    series: [{
      name: field,
      type: 'line',
      data: [],
      smooth: true,
      itemStyle: {
        color: color
      },
      areaStyle: {
        color: {
          type: 'linear',
          x: 0,
          y: 0,
          x2: 0,
          y2: 1,
          colorStops: [{
            offset: 0, color: `${color}80` // 0% 处的颜色，添加透明度
          }, {
            offset: 1, color: `${color}00` // 100% 处的颜色，完全透明
          }],
          global: false // 缺省为 false
        }
      }
    }]
  };
  
  // 使用配置项显示图表
  chart.setOption(option);
  
  // 监听窗口大小变化，调整图表
  window.addEventListener('resize', () => {
    if (chart) {
      chart.resize();
    }
  });
},
    
    // 更新所有图表
    updateCharts() {
      this.selectedFields.forEach(field => {
        this.updateChart(field);
      });
    },
    
    // 更新单个图表数据
    updateChart(field) {
      const chart = this.charts[field];
      if (!chart || !this.parsedData || this.parsedData.length === 0) return;
      
      // 准备x轴数据（时间）
      const xData = this.parsedData.map(item => this.formatDateTime(item.datetime));
      
      // 更新图表配置
      const option = {
        xAxis: {
          data: xData
        },
        series: [{
          data: this.parsedData.map(item => item[field] || 0)
        }]
      };
      
      // 应用配置更新图表
      chart.setOption(option);
    }
  }
};
</script>

<style lang="scss">
.curve-container {
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
  
  .filter-section {
    display: flex;
    padding: 12px;
    background-color: #fff;
    margin-bottom: 10px;
    box-shadow: 0 1px 3px rgba(0, 0, 0, 0.05);
    
    .filter-item {
      flex: 1;
      display: flex;
      align-items: center;
      
      .filter-label {
        font-size: 14px;
        color: #666;
        margin-right: 8px;
      }
      
      .filter-value {
        font-size: 14px;
        color: #333;
        font-weight: 500;
      }
    }
  }
  
  .charts-container {
    display: grid;
    grid-template-columns: 1fr;
    gap: 16px;
    padding: 0 12px;
    margin-bottom: 10px;
  }
  
  .single-chart-container {
    background-color: #fff;
    border-radius: 8px;
    box-shadow: 0 1px 3px rgba(0, 0, 0, 0.05);
    overflow: hidden;
    
    .chart-title {
      padding: 12px;
      background-color: #f5f5f5;
      font-size: 15px;
      font-weight: 500;
      color: #333;
    }
    
    .curve-chart {
      width: 100%;
      height: 220px;
    }
  }
  
  .indicator-selector {
    background-color: #fff;
    padding: 12px;
    margin: 0 12px 10px;
    box-shadow: 0 1px 3px rgba(0, 0, 0, 0.05);
    border-radius: 8px;
    
    .selector-title {
      font-size: 14px;
      color: #333;
      margin-bottom: 8px;
      font-weight: 500;
    }
    
    .indicator-tags {
      display: flex;
      flex-wrap: wrap;
      gap: 8px;
    }
    
    .tag-item {
      padding: 6px 12px;
      border-radius: 12px;
      background-color: #f5f5f5;
      font-size: 13px;
      color: #606266;
      cursor: pointer;
      transition: all 0.3s;
      
      &.active {
        background-color: #1890ff;
        color: #fff;
      }
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
    
    .loading-text {
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
    min-height: 200px;
    
    .empty-icon {
      width: 60px;
      height: 60px;
      font-size: 60px;
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