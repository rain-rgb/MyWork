<template>
  <a-modal
    title="级配曲线"
    :width="width"
    :visible="visible"
    :confirm-loading="confirmLoading"
    @ok="handleOk"
    @cancel="handleCancel">

    <a-card title="级配曲线" :bordered="false" :headStyle="{color:'#0785fd'}" :bodyStyle="{padding:'10'}">
      <div>
        <line-chart-multid-jipei :data-source="dataSource"/>
      </div>
    </a-card>
  </a-modal>
</template>

<script>
import { getAction } from '@api/manage'
import LineChartMultidJipei from '@comp/chart/LineChartMultidJipei'

export default {
  name: 'BhzLqBaseJipeiModel',
  components: { LineChartMultidJipei },
  data() {
    return {
      dataSource: [],
      width: 1000,
      visible: false,
      confirmLoading: false,
      id: 0,
      url: {
        list: '/bhzlqjipeistatistics/bhzLqJipeiStatistics/list1',
        list1: '/lqbhz/bhzLqBases/list'
      },
    }
  },
  created() {
  },
  methods: {
    show(id) {
      this.id = id[0]
      this.visible = true
      this.getData(this.id)
    },
    handleOk() {
      this.visible = false
    },
    handleCancel() {
      this.visible = false
      const demo = 0
      this.$emit('change', demo)
    },
    getData(id) {
      let params = { id: id }
      getAction(this.url.list1, params).then((res) => {
        if (res.success) {
          this.model = res.result.records[0]
          console.log('this.model', this.model)
          this.getjipei(this.model.guid)
        }
      })
    },
    getjipei(guid) {
      this.dataSource=[]
      let params = { baseid: guid }
      getAction(this.url.list, params).then(res => {
        console.log('res122', res)
        if (res.success) {
          let data = res.result
          if (data.length > 0) {
            data.forEach(re => {
              this.dataSource.push({
                type:re.shaikong+"筛孔",
                "jeecg":parseFloat(re.shangxian),
                "jeebt":parseFloat(re.zhongzhi),
                "jeecg1":parseFloat(re.xiaxian),
                "jeebt1":re.hechengjipei
              })
            })
          }else {
            this.$message.warn("没有配置级配标准!")
          }
        }
      })
    }
  },

}
</script>

<style scoped>
/*update_begin author:scott date:20191203 for:打印机打印的字体模糊问题 */
* {
  color: #000000 !important;
  -webkit-tap-highlight-color: #000000 !important;
}

/*update_end author:scott date:20191203 for:打印机打印的字体模糊问题 */

.abcdefg .ant-card-body {
  margin-left: 0%;
  margin-right: 0%;
  margin-bottom: 1%;
  border: 0px solid black;
  min-width: 800px;
  color: #000000 !important;
}

.tdSize {
  /*font-size: 20px;*/
  font-weight: bold;
  font-size: 20px;
}

#container {
  /*margin-top: 10px;*/
  /*left: 10%;*/
  width: 40%;
  height: 500px;
}
</style>