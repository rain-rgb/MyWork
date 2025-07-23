<template>
  <a-card :bordered="false">
    <div class="table-page-search-wrapper">
      <a-form layout="inline" >
        <a-row :gutter="24">
<!--          <a-col :xl="6" :lg="7" :md="8" :sm="24">-->
<!--            <a-form-item label="设备名称">-->
<!--              <j-search-select-tag placeholder="请选择设备名称" v-model="dictOption.text" :dictOptions="dictOption" @change="handleChange(dictOption.text)">-->
<!--              </j-search-select-tag>-->
<!--              {{ selectValue }}-->
<!--            </a-form-item>-->
<!--          </a-col>-->
<!--          <a-col :xl="6" :lg="7" :md="8" :sm="24">-->
<!--            <a-form-item label="组织机构" >-->
<!--              <JselectDqDepart v-model="sysOrgCode" multi  />-->
<!--            </a-form-item>-->
<!--          </a-col>-->
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="时间范围">
              <j-dict-select-tag style="width: 200px" type="list" v-model="approval_remarks"  dictCode="time1" />
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
<!--        <a-card title = "超标盘数" :bordered="false" :headStyle="{color:'#0785fd'}" :bodyStyle="{padding:'10'}">-->
<!--          <div>-->
<!--            <bar-multid :data-source="dataSource" :fields="fields" />-->
<!--          </div>-->
<!--        </a-card>-->
<!--        <a-card title = "合格率" :bordered="false" :headStyle="{color:'#0785fd'}" :bodyStyle="{padding:'10'}">-->
<!--          <div>-->
<!--            <pie :data-source="dataSource1"/>-->
<!--          </div>-->
<!--        </a-card>-->
        <a-card title = "方量" :bordered="false" :headStyle="{color:'#0785fd'}" :bodyStyle="{padding:'10'}">
          <div>
            <BarMultid :data-source="dataSource2" :fields="fields"/>
          </div>
        </a-card>
      </a-row>
    </div>
  </a-card>
</template>
<script>
import JselectDqDepart from '@/components/jeecgbiz/JselectDqDepart'
import BarMultid from '@comp/chart/ShuiWenTongJi'
// import Pie from '@comp/chart/BhzStaPie'
// import LineChartMultid from '@comp/chart/BhzStafangliang'
import { getAction } from '@api/manage'
import JSuperQuery from '@comp/jeecg/JSuperQuery'
import { JeecgListMixin } from '@/mixins/JeecgListMixin'
import { SYS_DEPART_ORGCODE } from '@/store/mutation-types'
import { usershebeiList } from '@api/api'
import Vue from 'vue'

export default {
  name: 'BhzSwBaseSta',
  //mixins: [JeecgListMixin],
  components: {
    BarMultid,
    JselectDqDepart,
    // Pie,
    // LineChartMultid,
    // JSuperQuery
  },
  data() {
    return {
      approval_remarks:1,
      selectValue: '',
      asyncSelectValue: '',
      dictOption: [],
      datalist: [],
      dataSource:[],
      fields:[],
      aliases:[],
      dataSource2: [],
      obj1:{type: '方量'},
      url: {
        list:"/swbhzStatistics/bhzSwStatistics/getFangliang"
      },
      dictOptions:{}
    }
  },
  created() {

    this.fanglaing();


  },
  methods: {
    selchange() {
      this.dictOption = this.datalist
    },

    chaxun:function(){
      this.cx= this.selectedValue;
      this.obj1={type: '方量'};
      // this.dates = this.approval_remarks
      this.dataSource2=[];
      this.fanglaing();
    },
    chongzhi:function() {
      this.dictOption=[];
      this.selectedValue = null;
      this.approval_remarks = 1
      this.chaxun();
    },


    fanglaing:function (){ //超标盘数柱状图
      let params = {sysOrgCode:this.cx,type:this.approval_remarks};
      this.dataSource=[];
      getAction(this.url.list,params)
        .then(res=>{
          this.fields=[];
          console.log(res)
          if(res.success) {
            let data=res.result;
            if(data.length>0){
              this.dataapi(data)
            }
            else{
              this.$message.warn("该部门没有水稳生产量")
            }
          }
        })
    },
    dataapi(data) {

      data.forEach(item => {
        if (this.approval_remarks===3){
          this.fields.push(item.stime+'月');
        }else if (this.approval_remarks === 2){
          this.fields.push('第'+item.stime + '周');
        } else {
          this.fields.push(item.stime);
        }
      });
      console.log(this.fields)
      this.fields.forEach((item, index) => {
        this.obj1[item] = data[index].fangliang

      })
      this.dataSource2.push(this.obj1)
      console.log(this.dataSource2)
    },



  }
}

</script>

<style scoped>

</style>
