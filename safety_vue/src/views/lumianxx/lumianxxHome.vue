<template>
  <div>
    <index-bdc v-if="indexStyle==2"></index-bdc>
  </div>
</template>

<script>
import IndexBdc from './IndexBdc'
export default {
  name: 'lumianxxHome',
  components: {
    IndexBdc
  },
  data() {
    return {
      indexStyle:2
    }
  },
  created() {

  },
  methods: {

  }
}
</script>

<style scoped>

</style>