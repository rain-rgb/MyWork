<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline">
        <a-row :gutter="24">
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="设备名称">
              <j-search-select-tag placeholder="请选择设备名称" v-model="dictOption.text" :dictOptions="dictOption"
                                   @change="handleChange(dictOption.text)">
              </j-search-select-tag>
              {{ selectValue }}
            </a-form-item>
          </a-col>

          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
              <a-button type="primary" @click="shebeiZB" icon="search">查询</a-button>
              <a-button type="primary" @click="chongzhi" icon="reload" style="margin-left: 8px">重置</a-button>
            </span>
          </a-col>
        </a-row>
      </a-form>
    </div>
    <!-- 查询区域-END -->
    <!-- table区域-begin -->
    <div id="container" style="height: 90vh"></div>
  </a-card>
</template>


<script>
import '@/assets/less/TableExpand.less'
import {mixinDevice} from '@/utils/mixin'
import {JeecgListMixin} from '@/mixins/JeecgListMixin'
import JDictSelectTagcar from '@/components/dict/JSearchSelectTagcar'
import JSuperQuery from '@/components/jeecg/JSuperQuery.vue'
import JselectDqDepart from '@/components/jeecgbiz/JselectDqDepart.vue'
import {pushdepartidShebei} from '@/mixins/pushdepartidShebei'
import { JVXETypes } from '@/components/jeecg/JVxeTable'
import { getAction, postAction, putAction } from '@api/manage'
import { usershebeiList,carshebeiList } from '@api/api'
import { SYS_DEPART_ORGCODE } from '@/store/mutation-types'
import Vue from 'vue'
import carIcon1 from '@assets/img/gifcar.gif'
import carIcon2 from '@assets/img/carlixian.png'
import carTPJ from '@assets/img/tpj.png'
import carYLJ from '@assets/img/ylj.png'
import { handertoken } from '@/mixins/getHanderToken'
export default {
  name: 'swshebeiMap',
  mixins: [handertoken],
  components: {
    JDictSelectTagcar,
    JSuperQuery,
    JselectDqDepart,
  },
  data() {
    return {
      selectValue: '',
      selectedValue:'',
      center: [116.478935, 39.997761],
      centers:{
        lng:null,
        lat:null
      },
      position: [120, 30],
      zuobiao:[120.333, 30.333],
      czuobiao:[120.333, 30.333],
      zoom: 8,
      intervalId:null,
      plugins: [],
      validatorRules: {},
      pitch: 0,
      lineArr:[],
      listDate:[],
      rotation: 0,
      // markerIcon: "",
      xianshiwenzi:{content: '暂无',
        direction: 'bottom',},
      opacity: 1,
      dictOption: [],
      move:{
        lat:null,//纬度
        lng:null//经度
      },
      adjust:{
        radius:0,
      },
      circle:{
        id:0,
        center:[],
        radius:0
      },
      url: {
        list: "/system/shebeiInfo/SBlist",
        sbjwd: "/system/shebeiInfo/SBJWD",
        list1:'/tpny/frontDeviceRealdata/list1',

      },
      shebei:[],
      circleEditor:null,
    }
  },
  created() {
    this.getToken();
    this.shebeiList();
    //this.dataRefreh();
  },
  destroyed(){
    // 在页面销毁后，清除计时器
    this.clear();
  },
  mounted() {
    this.initMap();
  },
  computed: {
    importExcelUrl: function () {
      return `${window._CONFIG['domianURL']}/${this.url.list}`;
    },
  },
  methods: {
    initMap() {
      var that=this;
      var shebeiNo=null;
      if(this.selectedValue==''){
        shebeiNo=null;
      }else{
        shebeiNo=this.selectedValue
      }
      let param={shebeiNo:shebeiNo}
      getAction(this.url.list1,param).then(res=>{
        //console.log(res,"最新数据")
        this.listDate=[];
        if(res.success){
          if(res.result.length>0){
            this.listDate=res.result;
            that.center=[this.listDate[0].longitude,this.listDate[0].latitude];
            that.centers.lng=this.listDate[0].longitude;
            that.centers.lat=this.listDate[0].latitude;
          }else{
            this.$message.warning("暂无数据!")
          }
        }else{
          this.$message.error("数据请求失败！")
        }
      })
      setTimeout(function (){
        that.map = new AMap.Map("container", {
          resizeEnable: true, //窗口大小调整
          center: that.center, //中心 firstArr: [116.478935, 39.997761],
          zoom: 17,
          mapStyle: "amap://styles/normal"
        });
        let startIcon1=new AMap.Icon({
          //图标尺寸
          size:new AMap.Size(60,40),
          //图标的取图地址
          image:carTPJ,
          //图标所用图片大小
          imageSize:new AMap.Size(60,40),
          //图标取图偏移量
          //imageOffset:new AMap.Pixel(-9,-3)
        });

        let startIcon2=new AMap.Icon({
          //图标尺寸
          size:new AMap.Size(60,40),
          //图标的取图地址
          image:carYLJ,
          //图标所用图片大小
          imageSize:new AMap.Size(60,40),
          //图标取图偏移量
          //imageOffset:new AMap.Pixel(-9,-3)
        });
        //console.log(this.listDate)
            that.listDate.forEach(function (item){

              if(item.deviceType=="A"){
                that.marker = new AMap.Marker({
                  map: that.map,
                  position: [item.longitude, item.latitude],
                  label:{
                    content:item.shebeiNo+'\n'+"速度 "+item.speed+"km/h",//"温度 "+item.temperature+"℃"+'\n'+
                    direction: 'top',
                  },
                  icon: startIcon2,
                  offset: new AMap.Pixel(-25, -13),
                  autoRotation: true,
                });
              }else if(item.deviceType=="B"){
                that.marker = new AMap.Marker({
                  map: that.map,
                  position: [item.longitude, item.latitude],
                  label:{
                    content:item.shebeiNo+'\n'+"速度 "+item.speed+"m/min",//"温度 "+item.temperature+"℃"+'\n'+
                    direction: 'top',
                  },
                  icon: startIcon1,
                  offset: new AMap.Pixel(-25, -13),
                  autoRotation: true,
                });
              }
            })
        that.map.setFitView(); //合适的视口
      },1000)

    },

    // 定时刷新数据函数
    dataRefreh() {
      // 计时器正在进行中，退出函数
      if (this.intervalId != null) {
        return;
      }
      // 计时器为空，操作
      this.intervalId = setInterval(() => {
        console.log("定时20秒刷新一次定位信息" + new Date());
        this.initMap();
      }, 20000);
    },
    // 停止定时器
    clear() {
      clearInterval(this.intervalId); //清除计时器
      this.intervalId = null; //设置为null
    },
    shebeiZB:function (){
      this.initMap();//查询
    },
    handleChange (selectedValue) {
      console.log("selectedValue",selectedValue)
      this.selectedValue= selectedValue
    },
    chongzhi:function() {
      this.dictOption=[];
      this.selectedValue='';
      this.initMap();
    },
    shebeiList: function () {
      var sys_depart_orgcode = Vue.ls.get('SYS_DEPART_ORGCODE')
      usershebeiList({
        sys_depart_orgcode: sys_depart_orgcode,
        sbtypes: '24,25,46'
      }).then(res => {
        this.dictOption = []
        let result = res.result
        result.forEach(re => {
          this.dictOption.push({ text: re.sbname, value: re.sbjno })
        })
      })
    },


  }
}
</script>
<style scoped>
>>>.ant-card-body{
  padding: 10px;
}
</style>
<style>
.amap-marker-label{
  background-color:rgba(1,2,73,.0);
  color:red;
  border:0px solid rgba(33,150,227,0.50);
}

</style>