<template>
<div>
  <Homehsy v-if="indexStyle==1"></Homehsy>
</div>
</template>

<script>
import Homehsy from './Homehsy'
export default {
  name: 'hsyHome',
  components: {
    Homehsy
  },
  data() {
    return {
      indexStyle:1
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