<template>
  <div>
    <router-view />
    <GlobalFooter/>
  </div>

</template>

<script>
import GlobalFooter from '@comp/page/GlobalFooter'
  export default {
    name: "BlankLayout",
    components: {
      GlobalFooter
    },
  }
</script>

<style scoped>

</style>