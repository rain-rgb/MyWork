<template>
  <div class="logo">
    <router-link :to="{path:'/dashboard/analysis'}">
<!--name:'dashboard'   点击之后到这个位置-->
      <!-- update-begin- author:sunjianlei --- date:20190814 --- for: logo颜色根据主题颜色变化 -->
      <!-- <img v-if="navTheme === 'dark'" src="~@/assets/logo-white.png" alt="logo">
      <img v-else src="~@/assets/logo4.png" alt="logo"> -->
      <!-- update-begin- author:sunjianlei --- date:20190814 --- for: logo颜色根据主题颜色变化 -->

      <!-- <h1 v-if="showTitle">{{ title }}</h1> -->
    </router-link>
  </div>
</template>

<script>
  import { mixin } from '@/utils/mixin.js'

  export default {
    name: 'Logo',
    mixins: [mixin],
    props: {
      title: {
        type: String,
        default: '',//Jeecg-Boot Pro IoT platform
        required: false
      },
      showTitle: {
        type: Boolean,
        default: true,
        required: false
      }
    },
    data(){

    }
  }
</script>
<style lang="less" scoped>
  /*缩小首页布 局顶部的高度*/
  @height: 59px;

  .sider {
    box-shadow: none !important;
    .logo {
      height: @height !important;
      line-height: @height !important;
      box-shadow: none !important;
      transition: background 300ms;

      a {
        color: white;
        &:hover {
          color: rgba(255, 255, 255, 0.8);
        }
      }
    }

    &.light .logo {
      background-color: @primary-color;
    }
  }
</style>