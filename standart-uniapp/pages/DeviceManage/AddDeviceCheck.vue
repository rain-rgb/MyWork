<template>
  <view class="container">
    <cu-custom bgColor="bg-white" :isBack="true">
      <block slot="content">新增设备检查</block>
    </cu-custom>

    <view class="form-card">
      <!-- 设备编号 -->
      <view class="form-item">
        <text class="label required">设备编号</text>
        <u-input 
          v-model="form.shebeino"
          placeholder="输入设备编号（例：_10966）"
          clearable
        />
      </view>

      <!-- 设备名称 -->
      <view class="form-item">
        <text class="label required">设备名称</text>
        <u-input
          v-model="form.shebeino_dictText"
          placeholder="输入设备名称（例：挖掘机-071）"
          clearable
        />
      </view>

      <!-- 检查内容 -->
      <view class="form-item">
        <text class="label required">检查内容</text>
        <u-textarea
          v-model="form.checkcontent"
          placeholder="输入详细检查内容（例：检查液压油、冷却液液面...）"
          autoHeight
          maxlength="500"
          :count="true"
        />
      </view>

      <!-- 状况描述 -->
      <view class="form-item">
        <text class="label">状况描述</text>
        <u-textarea
          v-model="form.miaoshu"
          placeholder="输入设备状况（例：良好）"
          autoHeight
          maxlength="100"
        />
      </view>

      <!-- 检查方式 -->
       <view class="form-item">
              <text class="label required">检查方式</text>
              <u-input
                v-model="form.checkway"
                placeholder="请输入检查方式（如：现场检查、远程监控等）"
                clearable
              />
       </view>

      <!-- 设备状态 -->
      <view class="form-item">
        <text class="label required">设备状态</text>
        <u-radio-group v-model="form.zhengchang">
          <u-radio :name="0" label="正常"></u-radio>
          <u-radio :name="1" label="故障"></u-radio>
        </u-radio-group>
      </view>

      <!-- 提交按钮 -->
      <view class="form-buttons">
        <u-button 
          type="primary"
          @click="handleSubmit"
          :loading="submitting"
        >
          {{ submitting ? '提交中...' : '提交检查记录' }}
        </u-button>
      </view>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      submitting: false,
      form: {
        shebeino: '',          // 设备编号（必填）
        shebeino_dictText: '', // 设备名称（必填）
        checkcontent: '',      // 检查内容（必填）
        miaoshu: '',           // 状况描述
        checkway: '',   // 检查方式（必填）
        zhengchang: 0,         // 设备状态（必填）
        // 以下字段自动生成
        createBy: '',
        createBy_dictText: '',
        sysOrgCode: '',
        createTime: ''
      }
    }
  },
  
  onShow() {
    this.initUserInfo()
  },

  methods: {
    // 初始化用户信息
    initUserInfo() {
      const userInfo = uni.getStorageSync('userInfo')
      this.form.createBy = userInfo.username
      this.form.createBy_dictText = userInfo.realname
      this.form.sysOrgCode = userInfo.orgCode
    },

    // 表单验证
    validate() {
      const requiredFields = [
        { field: 'shebeino', msg: '设备编号不能为空' },
        { field: 'shebeino_dictText', msg: '设备名称不能为空' },
        { field: 'checkcontent', msg: '检查内容不能为空' }
      ]

      for (const { field, msg } of requiredFields) {
        if (!this.form[field]?.trim()) {
          uni.showToast({ title: msg, icon: 'none' })
          return false
        }
      }

      if (this.form.checkcontent.length < 10) {
        uni.showToast({ title: '检查内容需至少10个字', icon: 'none' })
        return false
      }
	   // 增加特殊字符校验
	        if (/[<>&]/.test(this.form.checkway)) {
	          uni.showToast({ title: '检查方式包含非法字符', icon: 'none' })
	          return false
	        }

      return true
    },

    // 生成时间戳
    generateTimestamp() {
      const now = new Date()
      return `${now.getFullYear()}-${(now.getMonth()+1).toString().padStart(2,'0')}-${now.getDate().toString().padStart(2,'0')} ` +
             `${now.getHours().toString().padStart(2,'0')}:${now.getMinutes().toString().padStart(2,'0')}:${now.getSeconds().toString().padStart(2,'0')}`
    },

    // 提交数据
    async handleSubmit() {
      if (!this.validate() || this.submitting) return
      
      this.submitting = true
      try {
        const params = {
          ...this.form,
          createTime: this.generateTimestamp(),
          // 空字段保持null
          note: null,
          chuliren: null,
          chulitime: null,
          updateTime: null,
          handlereslut: null,
          handlepic: null,
          updateBy: null,
          checkpic: null,
          handleway: null
        }

        console.log('提交参数:', JSON.stringify(params, null, 2))

        const res = await this.$http.post('shebeiinfo/shebeiOverhaul/add', params)
        
        if (res.data.code === 200) {
          uni.showToast({
                  title: '提交成功',
                  icon: 'success',
                  duration: 1500,
                  success: () => {
                    // 添加延时确保Toast显示完整
                    setTimeout(() => {
                      // 关键修改：使用reLaunch关闭所有页面打开目标页
                      uni.reLaunch({
                        url: '/pages/DeviceManage/DeviceCheck'
                      })
                    }, 1500)
					},
					})
        } else {
          throw new Error(res.data.message || '接口返回错误')
        }
      } catch (e) {
        console.error('提交失败:', e)
        uni.showToast({ title: `提交失败: ${e.message}`, icon: 'none' })
      } finally {
        this.submitting = false
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.container {
  padding: 30rpx;
  background: #f8f9fa;
  min-height: 100vh;
}

.form-card {
  background: #fff;
  border-radius: 20rpx;
  padding: 40rpx;
  box-shadow: 0 4rpx 12rpx rgba(0,0,0,0.1);
}

.form-item {
  margin-bottom: 40rpx;

  .label {
    font-size: 30rpx;
    color: #606266;
    margin-bottom: 20rpx;
    
    &.required::after {
      content: '*';
      color: #f56c6c;
      margin-left: 8rpx;
    }
  }
}

.form-buttons {
  margin-top: 60rpx;
}

::v-deep .u-textarea__field {
  min-height: 200rpx;
  padding: 20rpx;
  background: #f8f9fa;
  border-radius: 12rpx;
}
</style>
