<template>
<!-- 砼拌合站统计分析 -->
    <a-card :bordered="false">
      <div class="table-page-search-wrapper">
        <a-form layout="inline" >
          <a-row :gutter="24">
            <a-col :xl="6" :lg="7" :md="8" :sm="24">
              <a-form-item label="设备名称">
                <j-search-select-tag placeholder="请选择设备名称" v-model="dictOption.text" :dictOptions="dictOption" @change="handleChange(dictOption.text)">
                </j-search-select-tag>
                {{ selectValue }}
              </a-form-item>
            </a-col>
            <a-col :xl="6" :lg="7" :md="8" :sm="24">
              <a-form-item label="时间范围">
                <j-dict-select-tag style="width: 200px" type="list" v-model="approval_remarks"  dictCode="time" placeholder="请选择时间"/>
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
        <a-row>
          <a-card title = "超标盘数" :bordered="false" :headStyle="{color:'#0785fd'}" :bodyStyle="{padding:'10'}">
            <div>
              <bar-multid  :data-source="dataSource" :fields="fields" />
            </div>
          </a-card>
          <a-card title = "合格率" :bordered="false" :headStyle="{color:'#0785fd'}" :bodyStyle="{padding:'10'}">
            <div>
              <pie  :data-source="dataSource1"/>
            </div>
          </a-card>
          <a-card title = "方量" :bordered="false" :headStyle="{color:'#0785fd'}" :bodyStyle="{padding:'10'}">
            <div>
              <LineChartMultid  :data-source="dataSource2"/>
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
import { getAction } from '@api/manage'
import JSuperQuery from '@comp/jeecg/JSuperQuery'
import { JeecgListMixin } from '@/mixins/JeecgListMixin'
import { SYS_DEPART_ORGCODE } from '@/store/mutation-types'
import { usershebeiList } from '@api/api'
import Vue from 'vue'

export default {
  name: 'BhzCementBaseSta',
  //mixins: [JeecgListMixin],
  components: {
    BarMultid, Pie,
    LineChartMultid,
    // JSuperQuery
  },
  data() {
    return {
      approval_remarks:null,
      selectValue: '',
      asyncSelectValue: '',
      dictOption: [],
      datalist: [],
      dataSource:[],
      fields:[],
      aliases:[],
      dataSource1: [],
      dataSource2: [],
      url: {
        list:"/hntbhz/bhzCementBase/list6",
        list2:"/hntbhz/bhzCementBase/list7",
      },
      dictOptions:{}
    }
  },
  created() {
    this.shebeiList();
    this.bhzsta();
    this.fanglaing();
    this.hegelv();

    },
  methods: {
    handleChange (selectedValue) {
      // this.dictOption = this.datalist
      console.log("selectedValue",selectedValue)
      this.selectedValue= selectedValue
      // this.callback()
    },
    chaxun:function(){
      this.cx = this.selectedValue;
      this.dates = this.approval_remarks
      this.bhzsta();
      this.fanglaing();
      this.hegelv();
    },
    chongzhi:function() {
      this.dictOption = []
      this.selectedValue = null;
      this.approval_remarks = null
      this.chaxun();
    },
    shebeiList:function (){
      var sys_depart_orgcode=Vue.ls.get('SYS_DEPART_ORGCODE');
      usershebeiList({
        sys_depart_orgcode:sys_depart_orgcode,
        sbtypes:'0'
      }).then(res=>{
        this.dictOption=[];
        let result=res.result;
        result.forEach(re=>{
          this.dictOption.push({text:re.sbname,value:re.sbjno})
          this.datalist.push({text:re.sbname,value:re.sbjno})
        })
        // this.datalist = JSON.parse(JSON.stringify(this.dictOption))
        //console.log(res)
      })
    },
    bhzsta:function (){ //超标盘数柱状图
      let params = {shebeiNo:this.cx,date:this.dates};
      this.dataSource=[];
      getAction(this.url.list,params)
        .then(res=>{
          this.fields=[];
          // console.log(res)
        if(res.success) {
          let data=res.result;
          if(data.length > 0){
            this.dataapi(data)
          }
          else{
            this.$message.warn("该设备的超标盘数没有数据")
          }
        }
     })
    },
    dataapi(data) {
      let obj1 = {type: '初级超标'}
      let obj2 = {type: '中级超标'}
      let obj3 = {type: '高级超标'}
      let obj4 = {type: '合格'}
     // console.log("data",data)
      data.forEach(item => {
        if (item.date===0){
          this.fields.push(item.statisticsTime + '年');
        }else if (item.date === 1){
          this.fields.push('第'+item.statisticsTime + '季');
        }else if (item.date === 2) {
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
      this.dataSource.push(obj1, obj2, obj3,obj4)
      //console.log(this.dataSource)
    },
    hegelv:function (){//合格率饼图
      let params = {shebeiNo:this.cx,date:this.dates};
      this.dataSource1 = [];
      getAction(this.url.list2,params)
        .then(res=> {
          // console.log(res)
          if (res.success) {
            let data = res.result;
            if(data.prisum===0&&data.midsum===0&&data.advsum===0&&data.hegesum===0){
              if (data.date === 0){
                this.$message.warn("今年的合格率没有数据!")
              }else if(data.date === 1){
                this.$message.warn("本季的合格率没有数据!")
              }else if(data.date === 2){
                this.$message.warn("本月的合格率没有数据!")
              }else if(data.date === 3){
                this.$message.warn("本周的合格率没有数据!")
              }else {
                this.$message.warn("今天的合格率没有数据!")
              }
              // console.log(message)
            }
            else{
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
      let params = {shebeiNo:this.cx,date:this.dates};
      this.dataSource2 = [];
      getAction(this.url.list, params)
        .then(res => {
          // console.log(res)
          if (res.success) {
            if(res.result.length>0){
              for (let i = 0; i < res.result.length; i++) {
                if (res.result[i].date === 0){
                  this.dataSource2.push({ type: res.result[i].statisticsTime + '年', '方量': res.result[i].estimateNumber });
                }else if (res.result[i].date === 1){
                  this.dataSource2.push({ type: '第'+res.result[i].statisticsTime + '季', '方量': res.result[i].estimateNumber });
                }else if (res.result[i].date === 2) {
                  this.dataSource2.push({ type: res.result[i].statisticsTime + '月', '方量': res.result[i].estimateNumber });
                }else {
                  this.dataSource2.push({ type: res.result[i].statisticsTime , '方量': res.result[i].estimateNumber });
                }
              }
            }
            else{
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
