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
              <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
                <a-button type="primary" @click="chaxun" icon="search">查询</a-button>
                <a-button type="primary" @click="chongzhi" icon="reload" style="margin-left: 8px">重置</a-button>
              </span>
            </a-col>
        </a-row>
      </a-form>
          <a-row :gutter="24">
            <a-col :sm="24" :md="12" :xl="10" :style="{ marginBottom: '24px' ,marginLeft:'20px',marginTop:'30px'}">
              <a-card  title="左洞人员定位统计">
                <a-card>
                  <div >人员统计：<span >{{mingxi.personsumz}}</span></div>
                </a-card>
                <a-card title="班组统计">
                  <a-row>
                    <a-col :span="12" v-for="(item,index) in banzuzlist" :key="index" :style="{ marginBottom: '5px'}" >
                      <a-card >
                        <span>{{item.departname}}：{{item.departmentid}}人</span>
                      </a-card>
                    </a-col>
                  </a-row>
                </a-card>
              </a-card>
            </a-col>
            <a-col :sm="24" :md="12" :xl="10" :style="{ marginBottom: '24px',marginLeft:'20px',marginTop:'30px'}">
              <a-card  title="右洞人员定位统计">
                <a-card>
                  <div >人员统计：<span >{{mingxi.personsumy}}</span></div>
                </a-card>
                <a-card title="班组统计">
                  <a-row>
                    <a-col :span="12" v-for="(item,index) in banzuylist" :key="index" :style="{ marginBottom: '5px'}">
                      <a-card>
                        <span>{{item.departname}}：{{item.departmentid}}人</span>
                      </a-card>
                    </a-col>
                  </a-row>
                </a-card>
              </a-card>
            </a-col>
          </a-row>
    </div>

  </a-card>
</template>

<script>
  import {getAction} from "@api/manage";
  import { usershebeiList } from '@api/api'
  import { SYS_DEPART_ORGCODE } from '@/store/mutation-types'
  import Vue from 'vue'
  export default {
    name: "DeviceTunnelPositiondataSta",
    components: {},
    data() {
      return {
        cx:'',
        selectValue: '',
        selectedValue:'',
        asyncSelectValue: '',
        dictOption: [],
        banzuzlist:[],
        banzuylist:[],
        mingxi:{
          personsumz:0,
          personsumy:0,
        },
        url:{
          list: "/devicetunnelposition/deviceTunnelPositiondata/tjlist",
          list1: "/devicetunnelposition/deviceTunnelPositiondata/list2"
        },
      }
    },
    created() {
      this.shebeiList();
      this.personnumzs();
      this.personnumys();
      this.banzustaz();
      this.banzustay();
    },
    methods: {
      handleChange (selectedValue) {
        // this.dictOption = this.datalist
        //console.log("selectedValue",selectedValue)
        this.selectedValue= selectedValue
        this.cx= this.selectedValue;
        // this.callback()
      },
      chaxun:function(){
        this.personnumzs();
        this.personnumys();
        this.banzustaz();
        this.banzustay();
      },
      chongzhi:function() {
        this.dictOption=[];
        this.selectedValue = null;
        this.chaxun();
      },
      personnumzs:function (){
        let params = {jzwz:'左洞',shebeino:this.cx};
        this.mingxi.personsumz = 0;
        getAction(this.url.list, params).then(res => {
          if (res.success) {
            let data = res.result;
            if (data[0] !== null){
              this.mingxi.personsumz = data[0].username;
            }
            //console.log("data",res.result)
          }
        })
      },
      banzustaz:function (){
        let params = {jzwz:'左洞',shebeino:this.cx};
        this.banzuzlist=[];
        getAction(this.url.list1, params).then(res => {
          if (res.success) {
            let data = res.result;
            this.banzuzlist = data;
            //console.log("this.banzulist",this.banzuzlist)
          }
        })
      },
      personnumys:function (){
        let params = {jzwz:'右洞',shebeino:this.cx};
        this.mingxi.personsumy = 0;
        getAction(this.url.list, params).then(res => {
          if (res.success) {
            let data = res.result;
            if (data[0] !== null){
              this.mingxi.personsumy = data[0].username;
            }
            //console.log("data++++++++++",res.result)
          }
        })
      },
      banzustay:function (){
        let params = {jzwz:'右洞',shebeino:this.cx};
        this.banzuylist=[];
        getAction(this.url.list1, params).then(res => {
          if (res.success) {
            let data = res.result;
            this.banzuylist = data;
            //console.log("this.banzulist",this.banzuylist)
          }
        })
      },
      shebeiList:function (){
        var sys_depart_orgcode=Vue.ls.get('SYS_DEPART_ORGCODE');
        usershebeiList({
          sys_depart_orgcode:sys_depart_orgcode,
          sbtypes:'39'
        }).then(res=>{
          this.dictOption=[];
          let result=res.result;
          result.forEach(re=>{
            this.dictOption.push({text:re.sbname,value:re.sbjno})
          })
          //console.log(res)
        })
      },
    }
  }
</script>

<style lang="less" scoped>
</style>