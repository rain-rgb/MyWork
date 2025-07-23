<template>
  <div :style="{ padding: '0 0 32px 32px' }">
    <h4 :style="{ marginBottom: '20px' }">{{ title }}</h4>
    <v-chart :force-fit="true" :height="height" :data="data" :scale="scale" :padding="padding" :onClick="handleClick">
      <v-tooltip />
      <v-axis />
      <v-legend />
      <v-bar position="type*总方量" />
      <v-line position="type*y" color="x" />
      <v-point position="type*y" color="x" :size="4" :v-style="style" :shape="'circle'" />
    </v-chart>
  </div>
</template>

<script>
import { DataSet } from '@antv/data-set'
import { ChartEventMixins } from './mixins/ChartMixins'

export default {
  name: 'LineChartMultid',
  mixins: [ChartEventMixins],
  props: {
    title: {
      type: String,
      default: ''
    },
    dataSource: {
      type: Array,
      default: () => [
        { type: 'Jan', jeecg: 7.0, jeebt: 3.9, jeebts: 5.9, jeecgs: 7.0, 总方量: 8 },
        { type: 'Feb', jeecg: 6.9, jeebt: 4.2, jeebts: 3.2, jeecgs: 7.0, 总方量: 6 },
        { type: 'Mar', jeecg: 9.5, jeebt: 5.7, jeebts: 6.7, jeecgs: 7.0, 总方量: 4 },
        { type: 'Apr', jeecg: 14.5, jeebt: 8.5, jeebts: 5.5, jeecgs: 7.0, 总方量: 8 },
        { type: 'May', jeecg: 18.4, jeebt: 11.9, jeebts: 13.9, jeecgs: 7.0, 总方量: 8 },
        { type: 'Jun', jeecg: 21.5, jeebt: 15.2, jeebts: 12.2, jeecgs: 7.0, 总方量: 9 },
        { type: 'Jul', jeecg: 25.2, jeebt: 17.0, jeebts: 16.0, jeecgs: 7.0, 总方量: 8 },
        { type: 'Aug', jeecg: 26.5, jeebt: 16.6, jeebts: 17.6, jeecgs: 7.0, 总方量: 8 },
        { type: 'Sep', jeecg: 23.3, jeebt: 14.2, jeebts: 11.2, jeecgs: 7.0, 总方量: 10 },
        { type: 'Oct', jeecg: 18.3, jeebt: 10.3, jeebts: 7.3, jeecgs: 7.0, 总方量: 8 },
        { type: 'Nov', jeecg: 13.9, jeebt: 6.6, jeebts: 10.6, jeecgs: 7.0, 总方量: 8 },
        { type: 'Dec', jeecg: 9.6, jeebt: 4.8, jeebts: 7.8, jeecgs: 7.0, 总方量: 8 }
      ]
    },
    fields: {
      type: Array,
      default: () => ['jeecg', 'jeebt', 'jeebts', 'jeecgs']
    },
    // 别名，需要的格式：[{field:'name',alias:'姓名'}, {field:'sex',alias:'性别'}]
    aliases: {
      type: Array,
      default: () => [{ field: 'type', alias: '时间' }, { field: 'jeecg', alias: '高级超标率(%)' }
        , { field: 'jeebt', alias: '中级超标率(%)' }, { field: 'jeebts', alias: '初级超标率(%)' }
        , { field: 'jeecgs', alias: '合格率(%)' }]
    },
    height: {
      type: Number,
      default: 254
    }
  },
  data() {
    return {
      scale: [{
        type: 'cat',
        dataKey: 'x',
        min: 0,
        max: 1
      }],
      style: { stroke: '#fff', lineWidth: 1 },
      padding: ['auto', 'auto', 'auto', 'auto']
    }
  },
  computed: {
    data() {
      const dv = new DataSet.View().source(this.dataSource)
      dv.transform({
        type: 'fold',
        fields: this.fields,
        key: 'x',
        value: 'y'
      })
      let rows = dv.rows
      // 替换别名
      rows.forEach(row => {
        for (let item of this.aliases) {
          if (item.field === row.x) {
            row.x = item.alias
            break
          }
        }
      })
      return rows
    }
  }
}
</script>

<style scoped>
</style>