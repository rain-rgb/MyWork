<template>
  <web-view :src="toUrl"></web-view>
</template>

<script>
export default {
  data() {
    return {
      toUrl: ''
    }
  },
  onLoad(option) {
    this.toUrl = option.url
    // 传入需要跳转的链接 使用web-view标签进行跳转
  }
}
</script>

<style lang="scss">
</style>