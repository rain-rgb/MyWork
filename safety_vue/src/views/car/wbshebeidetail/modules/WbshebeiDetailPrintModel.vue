<template>
  <a-modal
    title="打印"
    :width="width"
    :visible="visible"
    :confirm-loading="confirmLoading"
    @ok="handleOk"
    @cancel="handleCancel">
    <a-card :bordered="false" :class="{'abcdefg':true}">
      <div class="no-print" style="text-align: right">
        <a-button ghost type="primary" @click="bindPrint">打印</a-button>
      </div>
      <!--      <div ref="imageWrapper">-->
      <div ref="print">
        <section  class="abcdefg">
          <div style="text-align: center">
            <p style="font-size: 24px;font-weight: 800">电子锁运输接收单</p>
          </div>
          <a-col :md="24" :sm="24">
            <div class="sign" style="text-align: left;height: inherit">
              <table border="2px" style=" border-collapse: collapse;width: 100%;text-align: center" bordercolor="black">
                <tr style="height: 50px">
                  <td class="tdSize">供货单位</td>
                  <td class="tdSize">{{ model.ghdw }}</td>
                  <td class="tdSize">编号</td>
                  <td class="tdSize">{{ model.danhao }}</td>
                </tr>
                <tr style="height: 50px">
                  <td class="tdSize">材料名称</td>
                  <td class="tdSize">{{ model.cailiao }}</td>
                  <td class="tdSize">规格</td>
                  <td class="tdSize">{{ model.guige }}</td>
                </tr>
                <tr style="height: 50px">
                  <td class="tdSize">锁号</td>
                  <td class="tdSize">{{ model.sbbh }}</td>
                  <td class="tdSize">车号</td>
                  <td class="tdSize">{{ model.cph }}</td>
                </tr>
                <tr style="height: 50px">
                  <td class="tdSize">出发时间</td>
                  <td class="tdSize">{{ model.cftime }}</td>
                  <td class="tdSize">到达时间</td>
                  <td class="tdSize">{{ model.ddtime }}</td>
                </tr>
                <tr style="height: 50px">
                  <td class="tdSize">运输状态</td>
                  <td class="tdSize">{{ model.hege }}</td>
                  <td class="tdSize">进场数量</td>
                  <td class="tdSize">{{ model.jcsl }}</td>
                </tr>
                <tr style="height: 50px">
                  <td class="tdSize">目的地</td>
                  <td class="tdSize">{{ model.mdd }}</td>
                  <td class="tdSize">厂家负责人</td>
                  <td class="tdSize">{{ model.fzr }}</td>
                </tr>
                <tr style="height: 50px">
                  <td class="tdSize">注入油罐编号</td>
                  <td class="tdSize"></td>
                  <td class="tdSize">/</td>
                  <td class="tdSize">/</td>
                </tr>
                <tr style="height: 50px">
                  <td colspan="4" id="container">
                  </td>
                </tr>
              </table>
              <div style="font-size: 21px;margin-top: 10px;font-weight: bold">
                <span style="">施工单位:</span> <span style="margin-left: 20%">驻地办:</span> <span style="margin-left: 20%">中心试验室:</span>
              </div>
            </div>
          </a-col>
        </section>
      </div>
      <!--      </div>-->
    </a-card>
  </a-modal>
</template>

<script>
import { getAction } from '@api/manage'
import html2canvas from 'html2canvas'
import printJS  from 'print-js'

export default {
  name: 'WbshebeiDetailPrintModel',
  components: {},
  data() {
    return {
      lineArr: [],
      center: [116.478935, 39.997761],
      width: 1000,
      visible: false,
      confirmLoading: false,
      id: 0,
      imgUrl:'',
      model: {},
      url: {
        list: '/wbshebeidetail/wbshebeiDetail/list',
        list1: '/suchingcarpeiz/suchingCarpeiz/listcl'
      },
    }
  },
  created() {
    console.log(this.img, 'img')
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
      this.model = {}
      let params = { id: id }
      getAction(this.url.list, params).then((res) => {
        if (res.success) {
          this.model = res.result.records[0]
          //console.log('this.model', this.model)
          this.getguiji(this.model)
        }
      })
    },
    initMaps() {
      this.map = new AMap.Map('container', {
        WebGLParams: {
          preserveDrawingBuffer: true
        },
        resizeEnable: false, //窗口大小调整
        center: [116.478935, 39.997761], //中心 firstArr: [116.478935, 39.997761],
        zoom: 10
      })
      //this.zidongjietu()
    },
//初始化地图
    initMap() {
      this.map = new AMap.Map('container', {
        WebGLParams: {
          preserveDrawingBuffer: true
        },
        resizeEnable: true, //窗口大小调整
        center: this.center, //中心 firstArr: [116.478935, 39.997761],
        zoom: 10
      })
      // 创建一个 Icon
      var startIcon = new AMap.Icon({
        // 图标尺寸
        size: new AMap.Size(25, 34),
        // 图标的取图地址
        image: '//a.amap.com/jsapi_demos/static/demo-center/icons/dir-marker.png',
        // 图标所用图片大小
        imageSize: new AMap.Size(135, 40),
        // 图标取图偏移量
        imageOffset: new AMap.Pixel(-9, -3)
      })
      // 将 icon 传入 marker
      var startMarker = new AMap.Marker({
        position: this.position1,
        icon: startIcon,
        offset: new AMap.Pixel(-13, -30)
      })
      // 创建一个 icon
      var endIcon = new AMap.Icon({
        size: new AMap.Size(25, 34),
        image: '//a.amap.com/jsapi_demos/static/demo-center/icons/dir-marker.png',
        imageSize: new AMap.Size(135, 40),
        imageOffset: new AMap.Pixel(-95, -3)
      })

      // 将 icon 传入 marker
      var endMarker = new AMap.Marker({
        position: this.position2,
        icon: endIcon,
        offset: new AMap.Pixel(-13, -30)
      })
      // 将 markers 添加到地图
      // 绘制轨迹
      this.polyline = new AMap.Polyline({
        map: this.map,
        path: this.lineArr,
        strokeColor: '#1296db',  //线颜色
        strokeOpacity: 1,     //线透明度
        strokeWeight: 6,      //线宽
        strokeStyle: 'solid'  //线样式
      })
     // console.log("this.polyline",this.polyline)
      this.map.add([startMarker, endMarker])
      this.map.setFitView()
      //this.zidongjietu()
    },
    // zidongjietu: function () {
    //   setTimeout(this.bindPrint, 1000)
    // },
    bindPrint() {
      html2canvas(this.$refs.print, {
        allowTaint: true,
        taintTest: false,
        useCORS: true,
        // width: document.body.clientWidth,
        height:1000,
        //dpi: window.devicePixelRatio * 4,
        // scale: 10
      }).then((canvas) => {
        const url = canvas.toDataURL()
        //console.log("url",url)
        printJS({
          printable: url, // 要打印的id
          type: 'image',
          style: '@page{size:auto;margin: 0cm 1cm 0cm 1cm;}' //去除页眉页脚
        })
      })
    },

    getguiji(model) {
      //console.log(model, 'model')
      this.lineArr = []
      this.center = []
      let params = { sn: model.sbbh, start_time: model.cftime, end_time: model.ddtime ,pageSize: 2000, pageNum: 1}
      getAction(this.url.list1, params).then((res => {
        if (res.success) {
          let data = res.result
          //console.log('data', data)
          if (data.length > 0) {
            for (let i = 0; i < data.length; i++) {
              this.lineArr.push([data[i].lon, data[i].lat])
              if (i <= data.length - 1) {
                this.center.push(data[i].lon, data[i].lat)
              }
            }
            this.position1 = this.lineArr[0]
            this.position2 = this.lineArr[data.length - 1]
            // console.log("this.lineArr",this.lineArr)
            this.initMap()
          } else {
            this.initMaps()
          }
        }
      }))
    }
    ,
  }

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