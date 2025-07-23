<template>
  <a-row type="flex" :gutter="16">
    <a-col :md="5" :sm="24">
      <management-list-left v-model="currentOrgCode"/>
    </a-col>
    <a-col :md="24-5" :sm="24">
      <management-list-right v-model="currentOrgCode"/>
    </a-col>
  </a-row>
</template>

<script>
  import ManagementListLeft from './modules/ManagementListLeft'
  import ManagementListRight from './modules/ManagementListRight'

  export default {
    name: 'AddressList',
    components: { ManagementListLeft, ManagementListRight },
    data() {
      return {
        description: '风险计划管理页面',
        currentOrgCode: ''
      }
    },

    methods: {}
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>