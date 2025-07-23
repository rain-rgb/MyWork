<template>
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
        <a-card title = "油石比" :bordered="false" :headStyle="{color:'#0785fd'}" :bodyStyle="{padding:'10'}">
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
    import LineChartMultid from '@comp/chart/LqYouShiBi'
    import { getAction } from '@api/manage'
    import JSuperQuery from '@comp/jeecg/JSuperQuery'
    import { JeecgListMixin } from '@/mixins/JeecgListMixin'
    import { SYS_DEPART_ORGCODE } from '@/store/mutation-types'
    import { usershebeiList } from '@api/api'
    import Vue from 'vue'

    export default {
        name: 'ShengChanGuanLi',
        //mixins: [JeecgListMixin],
        components: {
            LineChartMultid,
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
                dataSource2: [],
                url: {
                    list:"/lqbhz/YouShiBi/getshiyoubi",
                },
                dictOptions:{}
            }
        },
        created() {
            this.shebeiList();
            this.fanglaing();

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
                this.fanglaing();
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
                    sbtypes:'1'
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

            fanglaing: function () {//油石比折线图
                let params = {sheBeiNo:this.cx};
                this.dataSource2 = [];
                getAction(this.url.list, params)
                    .then(res => {
                        console.log(res)
                        if (res.success) {
                            if(res.result.length>0){
                                for (let i = 0; i < res.result.length; i++) {
                                    if (res.result[i].youshibi){
                                        this.dataSource2.push({ type: res.result[i].time + '日', '油石比': res.result[i].youshibi });
                                    }
                                }
                            }
                            else{
                                this.$message.warn("该设备的油石比没有数据")
                            }
                        }
                    })
            },
        }
    }

</script>

<style scoped>

</style>
