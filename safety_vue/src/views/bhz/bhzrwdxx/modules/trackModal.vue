<template>
    <j-modal
        :title="title"
        :width="width"
        :visible="visible"
        switchFullscreen
        @ok="handleOk"
        :okButtonProps="{ class: { 'jee-hidden': disableSubmit } }"
        @cancel="handleCancel"
        cancelText="关闭"
    >
        <div>
            <div id="container" style="height: 450px;width: 100%;"></div>
            <div class="btngroup">
                <a-button type="primary" @click="startAnimation">开始回放</a-button>
                <a-button type="primary" @click="pauseAnimation">暂停回放</a-button>
                <a-button type="primary" @click="resumeAnimation">继续回放</a-button>
                <a-button type="primary" @click="stopAnimation">结束回放</a-button>
            </div>
        </div>
    </j-modal>
</template>
<script>
import carIcon1 from '@/assets/img/car3.png'
import { httpAction, getAction, postAction } from '@/api/manage'

export default {
    name: 'trackModal',
    data() {
        return {
            title: '轨迹回放',
            width: 1400,
            visible: false,
            disableSubmit: true,
            map: null,
            polyline: null,
            passedPolyline: null,
            marker: null,
            lineArr: [[116.478935,39.997761],[116.478939,39.997825],[116.478912,39.998549],[116.478912,39.998549],[116.478998,39.998555],[116.478998,39.998555],[116.479282,39.99856],[116.479658,39.998528],[116.480151,39.998453],[116.480784,39.998302],[116.480784,39.998302],[116.481149,39.998184],[116.481573,39.997997],[116.481863,39.997846],[116.482072,39.997718],[116.482362,39.997718],[116.483633,39.998935],[116.48367,39.998968],[116.484648,39.999861]],
            center: [116.478935, 39.997761],
            position: [],
        }
    },
    methods: {
        //初始化地图和小车
        initMap() {
            this.map = new AMap.Map('container', {
                resizeEnable: true, //窗口大小调整
                center: this.center, //中心 firstArr: [116.478935, 39.997761],
                zoom: 17,
            })
            this.marker = new AMap.Marker({
                map: this.map,
                // position: this.position,
                position: [116.478935,39.997761],
                label: {
                    content: this.content,
                    direction: 'bottom',
                },
                icon: carIcon1,
                offset: new AMap.Pixel(-13, -26),
                autoRotation: true,
                angle: -90,
            })
        },
        //没数据时候的地图
        initMaps() {
            this.map = new AMap.Map('container', {
                resizeEnable: false, //窗口大小调整
                center: [116.478935, 39.997761], //中心 firstArr: [116.478935, 39.997761],
                zoom: 10,
            })
        },
        //初始化轨迹
        initroad() {
            //绘制还未经过的路线
            this.polyline = new AMap.Polyline({
                map: this.map,
                path: this.lineArr,
                showDir: true,
                strokeColor: '#28F', //线颜色--蓝色
                // strokeOpacity: 1,     //线透明度
                strokeWeight: 6, //线宽
                // strokeStyle: "solid"  //线样式
            })
            //绘制路过了的轨迹
            this.passedPolyline = new AMap.Polyline({
                map: this.map,
                strokeColor: '#AF5', //线颜色-绿色
                //path: this.lineArr,
                // strokeOpacity: 1,     //线透明度
                strokeWeight: 6, //线宽
                // strokeStyle: "solid"  //线样式
            })
            this.marker.on('moving', (e) => {
                this.passedPolyline.setPath(e.passedPath)
                // const index = e.passedPath.length
                // this.map.setCenter(e.target.getPosition(), true)
                // this.marker.setLabel({
                //     content: '<div>' + that.lngdata[index].datatime + '-' + that.content + '</div>',
                //     direction: 'top',
                // })
            })
            this.map.setFitView() //合适的视口
        },
        //获取车的轨迹
        Carguiji: function (record) {
            this.initMap()
            this.initroad()
            // this.shebeiNo = record.shebeiNo
            // let dt = new Date(record.datatime)
            // let y = dt.getFullYear() //年
            // let m = dt.getMonth() + 1 //月
            // let d = dt.getDate() //日
            // if (m < 10) {
            //     m = 0 + '' + m
            // }
            // if (d < 10) {
            //     d = 0 + '' + d
            // }
            // let datatime_start = y + '-' + m + '-' + d + ' ' + '00:00:00'
            // this.datatime_end = record.datatime
            // this.datatime_begin = datatime_start
            // this.content = record.shebeiNo_dictText
            // let params = { shebeiNo: this.shebeiNo, projectId: record.datatime, uploadtime: datatime_start }
            // postAction(this.url.tokens, params).then((res) => {
            //     if (res.success) {
            //         if (res.result.data.length > 0) {
            //             let data = res.result.data
            //             this.lngdata = []
            //             this.lngdata = data
            //             this.lineArr = []
            //             this.center = []
            //             for (let i = 0; i < data.length; i++) {
            //                 this.lineArr.push([data[i].lng, data[i].lat])
            //                 if (i <= data.length - 1) {
            //                     this.center.push(data[0].lng, data[0].lat)
            //                 }
            //             }
            //             this.position = this.lineArr[0]
            //             this.initMap()
            //             this.initroad()
            //         } else {
            //             this.initMaps()
            //             this.$message.warning('暂无当前时间范围内的历史轨迹!')
            //         }
            //     } else {
            //         this.initMaps()
            //         this.$message.error('暂无当前车辆轨迹!')
            //     }
            // })
        },
        //开始动画
        startAnimation() {
            this.marker.moveAlong(this.lineArr, 200)
        },
        //暂停动画
        pauseAnimation() {
            this.marker.pauseMove()
        },
        //继续动画
        resumeAnimation() {
            this.marker.resumeMove()
        },
        //停止动画
        stopAnimation() {
            this.marker.stopMove()
        },
        handleOk() {
            this.visible = false
        },
        handleCancel() {
            this.visible = false
        },
    },
}
</script>
<style scoped>
.btngroup{
    display: flex;
    justify-content: space-around;
    margin-top: 10px;
}
</style>