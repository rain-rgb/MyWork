<template>
 <a-row type="flex" :gutter="16">
    <a-col :md="5" :sm="24">
      <programme-listleft v-model="currentOrgCode"/>
    </a-col>
    <a-col :md="24-5" :sm="24">
      <programme-listright v-model="currentOrgCode"/>
    </a-col>
  </a-row>
</template>
<script>
import ProgrammeListleft from './modules/ProgrammeListleft'
import ProgrammeListright from './modules/programmeListright'
export default {
    name:'Programme',
    components:{
      ProgrammeListleft,
      ProgrammeListright
    },
    data () {
        return {
        description: '通讯录页面',
        currentOrgCode: ''
        }
    },
    methods:{

    }
}
</script>
<style  scoped>
@import '~@assets/less/common.less';
</style>