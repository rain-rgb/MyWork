<template>
  <a-card :loading="loading" :bordered="false" :body-style="{ padding: '0' }">
    <div class="salesCard">
      <a-tabs default-active-key="1" size="large" :tab-bar-style="{ marginBottom: '24px', paddingLeft: '16px' }">
       
        <a-tab-pane loading="true" tab="统计信息" key="1">
          <a-row>
            <a-col >
              <bar-and-multid-line title="" :height="height" :dataSource="dataSource1" />
            </a-col>
          </a-row>
        </a-tab-pane>
      </a-tabs>
    </div>
  </a-card>
</template>

<script>
import ACol from 'ant-design-vue/es/grid/Col'
import Vue from 'vue'
import ATooltip from 'ant-design-vue/es/tooltip/Tooltip'
import { getAction } from '@api/manage'
import BarAndMultidLine from '@comp/chart/SyIndexMultidLine'
import HeadInfo from '@/components/tools/HeadInfo.vue'
export default {
  name: 'SyIndex',
  components: {
    ATooltip,
    ACol,
    BarAndMultidLine,
    HeadInfo,
  },
  data() {
    return {
      dataSource1: [],
      height: 340,
      loading: true,
       url:{
          list:'/syguanlicuoshi/syGuanlicuoshi/statisticByZzType',
        },
    }
  },
  created() {
      setTimeout(() => {
        this.loading = !this.loading
      }, 1000)
      this.getDatas();//统计
    },
  methods: {
    getDatas: function () {
      //统计数据
      this.dataSource1 = []
      let sys_depart_orgcode = Vue.ls.get('SYS_DEPART_ORGCODE')
      getAction(this.url.list,{
        sys_depart_orgcode
      }).then((res) => {
        if (res.success) {
          let data = res.result
          if (data.length > 0) {
            for (let i = 0; i < data.length; i++) {
              this.dataSource1.push({
                type: data[i].zzTime ,
                zeroCount: data[i].zeroCount,
                oneCount: data[i].oneCount,
                twoCount: data[i].twoCount,
              })
            }
          }
        }
      })
    },
  },
}
</script>

<style>
</style>