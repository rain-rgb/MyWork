<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline">
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
              <a-button type="primary" @click="shebeiZB" icon="search">查询</a-button>
              <a-button type="primary" @click="chongzhi" icon="reload" style="margin-left: 8px">重置</a-button>
<!--              <a @click="handleToggleSearch" style="margin-left: 8px">-->
<!--                {{ toggleSearchStatus ? '收起' : '展开' }}-->
<!--                <a-icon :type="toggleSearchStatus ? 'up' : 'down'"/>-->
<!--              </a>-->
            </span>
          </a-col>
        </a-row>
      </a-form>
    </div>
    <!-- 查询区域-END -->


    <!-- table区域-begin -->
    <div>
      <div class="map-container">
        <a-row :gutter="20">
          <a-col :span="24">
            <amap style="width: 100%;height: 75vh" :zoom="11" :center="czuobiao" >
              <amap-marker :position="zuobiao"  :label= "xianshiwenzi"  />
            </amap>
          </a-col>
        </a-row>

      </div>


    </div>

  </a-card>
</template>


<script>
import '@/assets/less/TableExpand.less'
import {mixinDevice} from '@/utils/mixin'
import {JeecgListMixin} from '@/mixins/JeecgListMixin'
import JDictSelectTag from '@/components/dict/JDictSelectTag.vue'
import JSuperQuery from '@/components/jeecg/JSuperQuery.vue'
import JselectDqDepart from '@/components/jeecgbiz/JselectDqDepart.vue'
// import {postAction , getAction} from "@api/manage";
import {pushdepartidShebei} from '@/mixins/pushdepartidShebei'
import { JVXETypes } from '@/components/jeecg/JVxeTable'
import { getAction } from '@api/manage'
import { handertoken } from '@/mixins/getHanderToken'
import { usershebeiList } from '@/api/api'
import { SYS_DEPART_ORGCODE } from '@/store/mutation-types'
import Vue from 'vue'
export default {
  name: 'ShebeiMap',
  mixins:[handertoken],
  // mixins: [JeecgListMixin, mixinDevice,pushdepartidShebei],
  components: {
    JDictSelectTag,
    JSuperQuery,
    JselectDqDepart,
  },
  data() {
    return {
      selectValue: '',
      center: [120, 30],
      position: [120, 30],
      zuobiao:[120.333, 30.333],
      czuobiao:[120.333, 30.333],
      zoom: 8,
      pitch: 0,
      rotation: 0,
      // markerIcon: "",
      xianshiwenzi:{content: '暂无',
        direction: 'bottom',},
      opacity: 1,
      dictOption: [],
      url: {
        list: "/system/shebeiInfo/SBlist",
        sbjwd: "/system/shebeiInfo/SBJWD"
      },
      shebei:[],
    }
  },
  created() {
    this.getToken()
    this.shebeiList();
  },
  computed: {
    importExcelUrl: function () {
      return `${window._CONFIG['domianURL']}/${this.url.list}`;
    },
  },
  methods: {
    handleChange (selectedValue) {
      // this.dictOption = this.datalist
      console.log("selectedValue",selectedValue)
      this.selectedValue= selectedValue
      // this.callback()
    },
    chongzhi:function() {
      this.dictOption=[];
      position: [120, 30],
      that.xianshiwenzi = {content: '暂无', direction: 'bottom',}
    },
    // shebeiList:function (){
    //     getAction(this.url.list).then((res)=>{
    //         if (res.success){
    //           this.dictOption=[];
    //           console.log(res.result)
    //           let result=res.result;
    //           result.forEach(re=>{
    //             this.dictOption.push({text:re.sbname,value:re.sbjno})
    //           })
    //         }else {
    //           alert("失败")
    //         }
    //     })
    // },
    shebeiList:function (){
      var sys_depart_orgcode=Vue.ls.get('SYS_DEPART_ORGCODE');
      usershebeiList({
        sys_depart_orgcode:sys_depart_orgcode,
        sbtypes:'0,1,2'
      }).then(res=>{
        this.dictOption=[];
        let result=res.result;
        result.forEach(re=>{
          this.dictOption.push({text:re.sbname,value:re.sbjno})
        })
        //console.log(res)
      })
    },
    shebeiZB:function (){
      let that = this
      this.cx= this.selectedValue;
      let params = {sbjno:this.cx}
      getAction(this.url.sbjwd,params).then((res)=>{
        if (res.success){
          // console.log(res.result.latitude)
          if(res.result!=null){
            if(res.result.longitude!=null&&res.result.latitude!=null){
              that.xianshiwenzi = {content: res.result.sbname , direction: 'bottom',}
              that.zuobiao = [res.result.longitude,res.result.latitude]
              that.czuobiao = [res.result.longitude,res.result.latitude]
            }
            else {
              that.xianshiwenzi = {content: '暂无', direction: 'bottom',}
              this.$message.error("暂无设备数据")
            }
          }else {
            that.xianshiwenzi = {content: '暂无', direction: 'bottom',}
            this.$message.error("暂无设备数据")
          }
        }else {
          that.xianshiwenzi = {content: '暂无', direction: 'bottom',}
          this.$message.error("暂无设备数据")
        }
      })
    }

  }
}
</script>
<!--<style scoped>-->
<!--   @import '~@assets/less/common.less';-->
<!--</style>-->