<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline">
        <a-row :gutter="24">
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="设备名称">
              <JDictSelectTagcar
                placeholder="请选择设备名称"
                dropdownStyle="color:red"
                v-model="shebeiNo"
                :dictOptions="dictOption"
              >
              </JDictSelectTagcar>
            </a-form-item>
          </a-col>

          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <span
              style="float: left; overflow: hidden"
              class="table-page-search-submitButtons"
            >
              <a-button type="primary" @click="shebeiZB" icon="search">查询</a-button>
              <a-button
                type="primary"
                @click="chongzhi"
                icon="reload"
                style="margin-left: 8px"
              >重置</a-button
              >
            </span>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="颜色标准">
              <a-button type="primary"> 此颜色的设备代表在线</a-button>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="颜色标准">
              <a-button type="danger"> 此颜色的设备代表离线</a-button>
            </a-form-item>
          </a-col>
        </a-row>
      </a-form>
    </div>
    <!-- 查询区域-END -->
    <!-- table区域-begin -->
    <div id="container" style="height: 75vh"></div>

    <a-row v-show="this.shebeiNo && this.shebeiNow == this.shebeiNo">
      <a-col :span="5" :offset="3">
        <a-button icon="plus" type="primary" @click="addFence">新增电子围栏</a-button>
      </a-col>
      <a-col :span="5" :offset="3">
        <a-button icon="plus" type="check" @click="saveFence">保存电子围栏</a-button>
      </a-col>
      <a-col :span="5" :offset="3">
        <a-button type="danger" icon="delete" @click="delFence">删除电子围栏</a-button>
      </a-col>
    </a-row>
  </a-card>
</template>

<script>
import '@/assets/less/TableExpand.less'
import JDictSelectTagcar from '@/components/dict/JSearchSelectTagcar'
import { getAction, postAction, deleteAction } from '@api/manage'
import { carshebeiList } from '@api/api'
import Vue from 'vue'
import carIcon1 from '@assets/img/gifcar.gif'
import carIcon2 from '@assets/img/carlixian.png'
import { wgs84togcj02 } from '@/utils/coordtransform.js'

export default {
  name: 'VehicleElectronicFence',
  components: {
    JDictSelectTagcar,
  },
  data() {
    return {
      mouseTool: null,
      polygonObj: null,//保存围栏数据的覆盖物
      overlays: [],//保存的围栏数据path
      shebeiNo: null,
      shebeiNow: null,
      listDate: {},//车数据
      center: [116.478935, 39.997761],
      intervalId: null,
      dictOption: [],
      url: {
        list: '/dianzhiweilan/dianzhiweilan/list',//电子围栏
        querylist: '/clgl/clxxRealdata/querylist',//车
        delete: '/dianzhiweilan/dianzhiweilan/delete',//删除
        addbatch: '/dianzhiweilan/dianzhiweilan/addbatch',//保存
      },
    }
  },
  created() {
    this.shebeiList()
    // this.dataRefreh()
  },
  destroyed() {
    // 在页面销毁后，清除计时器
    // clearInterval(this.intervalId) //清除计时器
  },
  mounted() {
    this.initMap()
  },
  computed: {},
  methods: {
    initMap() {
      this.listDate = {}
      this.overlays = []
      this.shebeiNow = this.shebeiNo
      if (this.shebeiNo) {
        //车数据
        let param1 = { shebeiNo: this.shebeiNo }
        getAction(this.url.querylist, param1).then((res) => {
          // console.log(res, '车数据')
          if (res.success) {
            if (res.result.length > 0) {
              this.listDate = res.result[0]
              // [this.listDate.longitude, this.listDate.latitude] = wgs84togcj02(this.listDate.longitude, this.listDate.latitude)
              this.center = [this.listDate.longitude, this.listDate.latitude]
            }
          } else {
            this.$message.error(res.message)
          }
        })
        //围栏数据
        let param2 = { shebeibianhao: this.shebeiNo }
        getAction(this.url.list, param2).then((res) => {
          // console.log(res, '围栏数据')
          if (res.success) {
            if (res.result.records.length > 0) {
              let arr = res.result.records
              this.overlays = arr.map(item => {
                return [item.jindu, item.weidu]
              })
            } else {
              this.$message.warning('暂无围栏！')
            }
          } else {
            this.$message.error(res.message)
          }
        })
      }
      setTimeout(() => {
        this.map = new AMap.Map('container', {
          resizeEnable: true, //窗口大小调整
          center: this.center, //中心 firstArr: [116.478935, 39.997761],
          zoom: 17,
          mapStyle: 'amap://styles/normal',
        })
        if (this.overlays.length > 0) {
          this.polygonObj = new AMap.Polygon({
            path: this.overlays,//会将坐标点转换成LngLat类
            fillColor: '#00b0ff',
            fillOpacity: 0.7,
            strokeColor: '#80d8ff',
            strokeOpacity: 0.7,
            map: this.map
          })
          // this.map.setFitView([this.polygonObj])//合适的视口
        } else {
          this.polygonObj = null
        }
        AMap.plugin(['AMap.MouseTool'], () => {
          this.mouseTool = new AMap.MouseTool(this.map)
          this.mouseTool.on('draw', (e) => {
            let polygon = e.obj
            this.polygonObj = polygon
            this.overlays = polygon.getPath()
            this.mouseTool.close()
          })
        })
        //在线车图标
        let startIcon = new AMap.Icon({
          //图标尺寸
          size: new AMap.Size(70, 70),
          //图标的取图地址
          image: carIcon1,
          //图标所用图片大小
          imageSize: new AMap.Size(70, 70),
        })
        //离线车图标
        let startIcon1 = new AMap.Icon({
          //图标尺寸
          size: new AMap.Size(70, 70),
          //图标的取图地址
          image: carIcon2,
          //图标所用图片大小
          imageSize: new AMap.Size(70, 70),
          //图标取图偏移量
          //imageOffset:new AMap.Pixel(-9,-3)
        })
        //车
        if (Object.keys(this.listDate).length != 0) {
          this.marker = new AMap.Marker({
            map: this.map,
            position: this.center,
            label: {
              content: this.listDate.shebeiname + (this.listDate.shebeistatus == 1 ? '--在线' : '--离线'),
              direction: 'top',
            },
            icon: this.listDate.shebeistatus == 1 ? startIcon : startIcon1,
            offset: new AMap.Pixel(-25, -13),
            autoRotation: true,
          })
        }
        this.map.setFitView() //合适的视口
      }, 1000)
    },

    // 定时刷新数据函数
    dataRefreh() {
      // 计时器为空，操作
      this.intervalId = setInterval(() => {
        // console.log('定时50秒刷新一次定位信息' + new Date())
        this.initMap()
        this.$message.success('刷新成功！')
      }, 50000)
    },
    shebeiZB() {
      this.initMap() //查询
    },
    chongzhi() {
      this.polygonObj = null
      this.dictOption = []
      this.shebeiNo = null
      this.initMap()
    },
    shebeiList() {
      let sys_depart_orgcode = Vue.ls.get('SYS_DEPART_ORGCODE')
      carshebeiList({
        sys_depart_orgcode: sys_depart_orgcode,
        sbtypes: '35',
      }).then((res) => {
        this.dictOption = []
        let result = res.result
        result.forEach((re) => {
          let xinxi = ''
          if (re.status == 1) {
            xinxi = '在线'
          } else {
            xinxi = '离线'
          }
          this.dictOption.push({
            text: re.sbname,
            value: re.sbjno,
            color: re.status,
            text1: xinxi,
          })
        })
        // console.log(this.dictOption)
      })
    },
    //新增
    addFence() {
      if (this.polygonObj) {
        return this.$message.warning('围栏已存在，请先删除后新增！')
      }
      this.mouseTool.polygon({
        fillColor: '#00b0ff',
        fillOpacity: 0.7,
        strokeColor: '#80d8ff',
        strokeOpacity: 0.7
      })
      this.$message.success('单击右键退出围栏编辑！')
    },
    //保存
    saveFence() {
      if (this.polygonObj) {
        let param = this.overlays.map(item => {
          return {
            jindu: item.lng,
            weidu: item.lat,
            shebeibianhao: this.shebeiNo
          }
        })
        postAction(this.url.addbatch, param).then((res) => {
          if (res.success) {
            this.$message.success('保存成功！')
          } else {
            this.$message.error(res.message)
          }
        })
        // console.log(param, this.overlays, '保存的围栏数据')
      } else {
        this.$message.warning('当前无围栏保存！请单击右键退出围栏编辑后保存！')
      }
    },
    //删除
    delFence() {
      if (this.polygonObj) {
        let param = { shebeibianhao: this.shebeiNo }
        deleteAction(this.url.delete, param).then((res) => {
          if (res.success) {
            this.polygonObj.setMap()
            this.polygonObj = null
            this.overlays = []
            this.$message.success('删除成功！')
          } else {
            this.$message.error(res.message)
          }
        })
      } else {
        this.$message.warning('当前无可删除围栏！')
      }
    }
  },
}
</script>
<style scoped>
.ant-card-body {
  padding: 10px;
}
</style>