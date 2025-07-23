<template>
  <!-- 砼材料误差分析-->
  <a-card :bordered="false">
    <div class="table-page-search-wrapper">
      <a-form layout="inline">
        <a-row :gutter="24">
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="组织机构">
              <j-search-select-tag placeholder="请选择组织机构">
              </j-search-select-tag>
              <!-- {{ selectValue }} -->
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="工程名称">
              <j-search-select-tag placeholder="请选择设备名称" v-model="dictOption.text" :dictOptions="dictOption"
                @change="handleChange(dictOption.text)">
              </j-search-select-tag>
              {{ selectValue }}
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="浇筑部位">
              <j-search-select-tag placeholder="请选择设备名称" v-model="dictOption.text" :dictOptions="dictOption"
                @change="handleChange(dictOption.text)">
              </j-search-select-tag>
              {{ selectValue }}
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="设备名称">
              <j-search-select-tag placeholder="请选择设备名称" v-model="dictOption.text" :dictOptions="dictOption"
                @change="handleChange(dictOption.text)">
              </j-search-select-tag>
              {{ selectValue }}
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="出料时间">
              <j-date placeholder="开始出料时间" dateFormat="YYYY-MM-DD HH:mm:ss" />
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="">
              <j-date placeholder="结束出料时间" dateFormat="YYYY-MM-DD HH:mm:ss" />
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
              <a-button type="primary" @click="chaxun" icon="search">查询</a-button>
              <a-button type="primary" @click="chongzhi" icon="reload" style="margin-left: 8px">重置</a-button>
            </span>
          </a-col>
        </a-row>
      </a-form>
      <div>
        <!-- <a-table :columns="columns" :data-source="data" :scroll="{ x: 1300 }">
          <a slot="action" slot-scope="text" href="javascript:;">action</a>
        </a-table> -->
      </div>
      <a-row>
        <a-card title="" :bordered="false" :headStyle="{ color: '#0785fd' }" :bodyStyle="{ padding: '20' }">
          <div style="marginTop:20px">
            <a-row>
              <a-col :xl="12" :lg="12" :md="12" :sm="24" :xs="24">
                <LineChartMultid title="材料用量走势图" :data-source="dataSource2" />
              </a-col>
              <a-col :xl="12" :lg="12" :md="12" :sm="24" :xs="24">
                <LineChartMultid title="误差走势图" :data-source="dataSource2" />
              </a-col>
            </a-row>
          </div>
        </a-card>
      </a-row>
    </div>
  </a-card>
</template>
<script>
import BarMultid from '@comp/chart/BhzCementSta'
import Pie from '@comp/chart/BhzStaPie'
import LineChartMultid from '@comp/chart/BhzStafangliang'
import indexhntPie from "@comp/chart/indexhntPie";
import { getAction } from '@api/manage'
import JSuperQuery from '@comp/jeecg/JSuperQuery'
import { JeecgListMixin } from '@/mixins/JeecgListMixin'
import { SYS_DEPART_ORGCODE } from '@/store/mutation-types'
import { usershebeiList } from '@api/api'
import Vue from 'vue'

export default {
  name: 'BhzMaterialScienceErrorAnalysis',
  //mixins: [JeecgListMixin],
  components: {
    BarMultid, Pie,
    LineChartMultid,
    indexhntPie,
    // JSuperQuery
  },
  data() {
    return {
      height: 340,
      columns: [
        {
          title: '超标等级',
          align: 'center',
          dataIndex: 'chaobiaodengji',
          scopedSlots: { customRender: 'chaobiaodengji' }
        },
        {
          title: '超标数(盘)',
          align: 'center',
          dataIndex: 'chaobiaopanshu',
        },
        {
          title: '超标率(%)',
          align: 'center',
          dataIndex: 'chaobiaolv',
        },
        {
          title: '处置数(盘)',
          align: 'center',
          dataIndex: 'chuzhipanshu',
        },
        {
          title: '处置率(%)',
          align: 'center',
          dataIndex: 'chuzhilv',
        },
        {
          title: '闭合数(盘)',
          align: 'center',
          dataIndex: 'bihepanshu',
        },
        {
          title: '闭合率(%)',
          align: 'center',
          dataIndex: 'bihelv',
        },
      ],
      pieData: [
        { count: 27644, item: "01月" },
      ],
      data: [
        // {
        //   key: '1',
        //   name: 'John Brown',
        //   age: 32,
        //   address: 'New York Park',
        // },
        // {
        //   key: '2',
        //   name: 'Jim Green',
        //   age: 40,
        //   address: 'London Park',
        // },
      ],
      approval_remarks: null,
      selectValue: '',
      asyncSelectValue: '',
      dictOption: [],
      datalist: [],
      dataSource: [],
      fields: [],
      aliases: [],
      dataSource1: [],
      dataSource2: [],
      url: {
        list: "/hntbhz/bhzCementBase/list6",
        list2: "/hntbhz/bhzCementBase/list7",
      },
      dictOptions: {},
    }
  },
  created() {
    this.shebeiList();
    this.bhzsta();
    this.fanglaing();
    this.hegelv();

  },
  methods: {
    onChange(e) {
      console.log('radio checked', e.target.value);
    },
    handleChange(selectedValue) {
      // this.dictOption = this.datalist
      console.log("selectedValue", selectedValue)
      this.selectedValue = selectedValue
      // this.callback()
    },
    chaxun: function () {
      this.cx = this.selectedValue;
      this.dates = this.approval_remarks
      this.bhzsta();
      this.fanglaing();
      this.hegelv();
    },
    chongzhi: function () {
      this.dictOption = []
      this.selectedValue = null;
      this.approval_remarks = null
      this.chaxun();
    },
    shebeiList: function () {
      var sys_depart_orgcode = Vue.ls.get('SYS_DEPART_ORGCODE');
      usershebeiList({
        sys_depart_orgcode: sys_depart_orgcode,
        sbtypes: '0'
      }).then(res => {
        this.dictOption = [];
        let result = res.result;
        result.forEach(re => {
          this.dictOption.push({ text: re.sbname, value: re.sbjno })
          this.datalist.push({ text: re.sbname, value: re.sbjno })
        })
        // this.datalist = JSON.parse(JSON.stringify(this.dictOption))
        //console.log(res)
      })
    },
    bhzsta: function () { //超标盘数柱状图
      let params = { shebeiNo: this.cx, date: this.dates };
      this.dataSource = [];
      getAction(this.url.list, params)
        .then(res => {
          this.fields = [];
          // console.log(res)
          if (res.success) {
            let data = res.result;
            if (data.length > 0) {
              this.dataapi(data)
            }
            else {
              this.$message.warn("该设备的超标盘数没有数据")
            }
          }
        })
    },
    dataapi(data) {
      let obj1 = { type: '初级超标' }
      let obj2 = { type: '中级超标' }
      let obj3 = { type: '高级超标' }
      let obj4 = { type: '合格' }
      // console.log("data",data)
      data.forEach(item => {
        if (item.date === 0) {
          this.fields.push(item.statisticsTime + '年');
        } else if (item.date === 1) {
          this.fields.push('第' + item.statisticsTime + '季');
        } else if (item.date === 2) {
          this.fields.push(item.statisticsTime + '月');
        } else {
          this.fields.push(item.statisticsTime);
        }
      })
      this.fields.forEach((item, index) => {
        obj1[item] = data[index].primaryDish
        obj2[item] = data[index].middleDish
        obj3[item] = data[index].advancedDish
        obj4[item] = data[index].hegeDish
      })
      this.dataSource.push(obj1, obj2, obj3, obj4)
      //console.log(this.dataSource)
    },
    hegelv: function () {//合格率饼图
      let params = { shebeiNo: this.cx, date: this.dates };
      this.dataSource1 = [];
      getAction(this.url.list2, params)
        .then(res => {
          // console.log(res)
          if (res.success) {
            let data = res.result;
            if (data.prisum === 0 && data.midsum === 0 && data.advsum === 0 && data.hegesum === 0) {
              if (data.date === 0) {
                this.$message.warn("今年的合格率没有数据!")
              } else if (data.date === 1) {
                this.$message.warn("本季的合格率没有数据!")
              } else if (data.date === 2) {
                this.$message.warn("本月的合格率没有数据!")
              } else if (data.date === 3) {
                this.$message.warn("本周的合格率没有数据!")
              } else {
                this.$message.warn("今天的合格率没有数据!")
              }
              // console.log(message)
            }
            else {
              this.dataSource1.push(
                { item: '初级超标', count: data.prisum },
                { item: '中级超标', count: data.midsum },
                { item: '高级超标', count: data.advsum },
                { item: '合格', count: data.hegesum });
            }
          }
        })
    },
    fanglaing: function () {//方量折线图
      let params = { shebeiNo: this.cx, date: this.dates };
      this.dataSource2 = [];
      getAction(this.url.list, params)
        .then(res => {
          // console.log(res)
          if (res.success) {
            if (res.result.length > 0) {
              for (let i = 0; i < res.result.length; i++) {
                if (res.result[i].date === 0) {
                  this.dataSource2.push({ type: res.result[i].statisticsTime + '年', '方量': res.result[i].estimateNumber });
                } else if (res.result[i].date === 1) {
                  this.dataSource2.push({ type: '第' + res.result[i].statisticsTime + '季', '方量': res.result[i].estimateNumber });
                } else if (res.result[i].date === 2) {
                  this.dataSource2.push({ type: res.result[i].statisticsTime + '月', '方量': res.result[i].estimateNumber });
                } else {
                  this.dataSource2.push({ type: res.result[i].statisticsTime, '方量': res.result[i].estimateNumber });
                }
              }
            }
            else {
              this.$message.warn("该设备的方量没有数据")
            }
          }
        })
    },
  }
}

</script>

<style scoped>

</style>
