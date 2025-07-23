<template>
    <div class="preview">
        <div class="preview-header">
            <div class="box">
                <div class="box-left" style="background-color: rgb(85, 165, 247)">
                    <img class="img" src="../../../assets/img/project.png" alt="" />
                </div>
                <div class="box-right">
                    <div class="box-right-title">项目</div>
                    <div class="box-right-name">管桩测试项目</div>
                </div>
            </div>
            <div class="box">
                <div class="box-left" style="background-color: rgb(255, 146, 82)">
                    <img class="img" src="../../../assets/img/eqnumber.png" alt="" />
                </div>
                <div class="box-right">
                    <div class="box-right-title">设备数</div>
                    <div class="box-right-name">{{ quantity.sbs }}台</div>
                </div>
            </div>
            <div class="box">
                <div class="box-left" style="background-color: rgb(126, 204, 9)">
                    <img class="img" src="../../../assets/img/pilesnumber.png" alt="" />
                </div>
                <div class="box-right">
                    <div class="box-right-title">打桩数</div>
                    <div class="box-right-name">{{ quantity.dzs }}根</div>
                </div>
            </div>
            <div class="box">
                <div class="box-left" style="background-color: rgb(255, 171, 35)">
                    <img class="img" src="../../../assets/img/meters.png" alt="" />
                </div>
                <div class="box-right">
                    <div class="box-right-title">总米数</div>
                    <div class="box-right-name">{{ Number(quantity.zms).toFixed(2) }}m</div>
                </div>
            </div>
            <div class="box">
                <div class="box-left" style="background-color: rgb(255, 171, 35)">
                    <img class="img" src="../../../assets/img/sections.png" alt="" />
                </div>
                <div class="box-right">
                    <div class="box-right-title">总节数</div>
                    <div class="box-right-name">{{ quantity.zjs }}节</div>
                </div>
            </div>
            <div class="box">
                <div class="box-left" style="background-color: rgb(177, 121, 143)">
                    <img class="img" src="../../../assets/img/details.png" alt="" />
                </div>
                <div class="box-right" style="display: flex; justify-content: center; align-items: center">
                    <a-button type="primary" @click="checkDetail">详情</a-button>
                </div>
            </div>
        </div>
        <div class="preview-middle">
            <div class="middle-left">
                <div class="equipment">
                    <production-pie title="今日生产数据" :height="height" :dataSource="dataSource"></production-pie>
                </div>
                <div class="number">
                    <div class="title">今日打桩数</div>
                    <div class="production">{{ piling }}<span>根</span></div>
                </div>
            </div>
            <div class="middle-right">
                <div ref="qxtOne" :style="chartStyle1"></div>
            </div>
        </div>
        <div class="preview-footer">
            <div class="map">
                <div id="container" style="width: 100%; height: 100%"></div>
            </div>
            <div ref="qxtTwo" :style="chartStyle2"></div>
        </div>

        <project-statistics ref="modalStatistics"></project-statistics>
    </div>
</template>
<script>
import { getAction } from '@/api/manage'
import * as echarts from 'echarts'
import ProductionPie from './modules/ProductionPie.vue'
import ProjectStatistics from './modules/ProjectStatistics.vue'
import amapIcon from '@/assets/img/amap-icon.png'

export default {
    name: 'TotalPreview',
    components: {
        ProductionPie,
        ProjectStatistics,
    },
    data() {
        return {
            dataSource: [
                { item: '在线设备', count: 1 },
                { item: '离线设备', count: 1 },
            ],
            equipmentList: [],
            chartStyle1: { width: '800px', height: '320px' },
            chartStyle2: { width: '50%', height: '100%' },
            height: 270,
            piling: 0,
            quantity: {
                sbs: 0,
                dzs: 0,
                zms: 0,
                zjs: 0,
            },
            url: {
                listYm: 'devicepipepilehistorydataone/devicePipepileHistorydataOne/listYm',
                listDzs: 'devicepipepilehistorydataone/devicePipepileHistorydataOne/listDzs',
                listGz: 'devicepipepilehistorydataone/devicePipepileHistorydataOne/listGz',
                listXq: 'devicepipepilestatistics/devicePipepileStatistics/listXq',
                listDT: 'devicepipepilerealdata/devicePipepileRealdata/list',
                listSb: 'devicepipepilerealdata/devicePipepileRealdata/listSb',
            },
            center: [120.153576, 30.287459],
            map: null,
            infoWindow: null,
        }
    },
    mounted() {
        this.getListYM()
        this.getListDzs()
        this.getListGz()
        this.getListSb()
        this.initMaps()
    },
    methods: {
        getListYM() {
            getAction(this.url.listYm).then((res) => {
                if (res.success) {
                    this.quantity = res.result[0]
                }
            })
        },
        getListDzs() {
            getAction(this.url.listDzs).then((res) => {
                if (res.success) {
                    let data = res.result.reverse()
                    let zsList = []
                    let timeList = []
                    let bhgsList = []
                    data.forEach((item) => {
                        zsList.push(item.zs)
                        timeList.push(item.time)
                        bhgsList.push(item.bhgs)
                    })
                    this.initEchartsOne(timeList, zsList, bhgsList)
                } else {
                    this.initEchartsOne()
                }
            })
        },
        getListGz() {
            getAction(this.url.listGz).then((res) => {
                if (res.success) {
                    let data = res.result.reverse()
                    let qjnumberList = []
                    let zcnumberList = []
                    let timeList = []
                    data.forEach((item) => {
                        qjnumberList.push(item.qjnumber)
                        zcnumberList.push(item.zcnumber)
                        timeList.push(item.time)
                    })
                    this.initEchartsTwo(timeList, zcnumberList, qjnumberList)
                } else {
                    this.initEchartsTwo()
                }
            })
        },
        getListSb() {
            getAction(this.url.listSb).then((res) => {
                if (res.success) {
                    this.dataSource[0].count = res.result.zx
                    this.dataSource[1].count = res.result.lx
                }
            })
        },
        //曲线图
        initEchartsOne(xList, yListOne, yListTwo) {
            let option = {
                tooltip: {
                    trigger: 'axis',
                },
                title: [
                    {
                        text: '打桩数',
                        top: 10,
                        left: 10,
                        textStyle: {
                            fontSize: 16,
                        },
                    },
                ],
                xAxis: {
                    type: 'category',
                    boundaryGap: true,
                    axisLabel: {
                        interval: 0,
                        fontSize: 14,
                    },
                    axisTick: {
                        show: false, // 隐藏坐标轴的刻度线
                    },
                    data: xList,
                },
                yAxis: [
                    {
                        nameTextStyle: {
                            align: 'center',
                            fontSize: 12,
                        },
                        axisLine: { show: true },
                        type: 'value',
                        // min: 0,
                        axisLabel: {
                            fontSize: 14,
                            // formatter: '{value} (cm/min)',
                        },
                    },
                ],
                series: [
                    {
                        name: '总打桩',
                        type: 'line',
                        smooth: true,
                        data: yListOne,
                    },
                    {
                        name: '异常桩',
                        type: 'line',
                        smooth: true,
                        data: yListTwo,
                    },
                ],
            }
            let myChart = echarts.getInstanceByDom(this.$refs.qxtOne)
            if (myChart == null) {
                myChart = echarts.init(this.$refs.qxtOne)
            }
            myChart.setOption(option)
        },
        initEchartsTwo(xList, yListOne, yListTwo) {
            let option = {
                legend: {
                    data: ['桩长异常', '压力异常'],
                },
                tooltip: {
                    trigger: 'axis',
                },
                title: [
                    {
                        text: '预警信息',
                        top: 10,
                        left: 10,
                        textStyle: {
                            fontSize: 16,
                        },
                    },
                ],
                xAxis: {
                    type: 'category',
                    boundaryGap: false,
                    axisLabel: {
                        interval: 0,
                        fontSize: 12,
                    },
                    axisTick: {
                        show: false, // 隐藏坐标轴的刻度线
                    },
                    data: xList,
                },
                yAxis: [
                    {
                        nameTextStyle: {
                            align: 'center',
                            fontSize: 12,
                        },
                        axisLine: { show: true },
                        type: 'value',
                        // min: 0,
                        axisLabel: {
                            fontSize: 14,
                            // formatter: '{value} (cm/min)',
                        },
                    },
                ],
                series: [
                    {
                        name: '桩长异常',
                        type: 'line',
                        smooth: true,
                        data: yListOne,
                    },
                    {
                        name: '压力异常',
                        type: 'line',
                        smooth: true,
                        data: yListTwo,
                    },
                ],
            }
            let myChart = echarts.getInstanceByDom(this.$refs.qxtTwo)
            if (myChart == null) {
                myChart = echarts.init(this.$refs.qxtTwo)
            }
            myChart.setOption(option)
        },
        checkDetail() {
            this.$refs.modalStatistics.getList()
        },
        //初始化地图
        initMaps() {
            getAction(this.url.listDT).then((res) => {
                if (res.success) {
                    this.equipmentList = res.result.records
                    this.equipmentList.forEach((item) => {
                        //创建点标记
                        let marker = new AMap.Marker({
                            position: [item.ltdfloat, item.lgdfloat],
                            label: {
                                content: item.shebeino_dictText,
                                direction: 'top',
                            },
                            icon: amapIcon,
                            offset: new AMap.Pixel(-13, -30), //点标记显示位置偏移量，默认值为Pixel(-10,-34)
                        })
                        let infoWindow = new AMap.InfoWindow({
                            content: `<div>
                                        <div>${item.shebeino_dictText}</div>
                                        <div>编号:${item.shebeino}</div>
                                        <div>状态:${item.worksta}</div>
                                    </div>`, //使用默认信息窗体框样式，显示信息内容
                            offset: new AMap.Pixel(0, -60),
                        })
                        // 将 marker 添加到地图
                        this.map.add(marker)
                        marker.on('click', (e) => {
                            infoWindow.open(this.map, e.target.getPosition())
                            this.map.setZoomAndCenter(18, e.target.getPosition())
                        })
                    })
                }
            })
            this.map = new AMap.Map('container', {
                resizeEnable: true, //是否监控地图容器尺寸变化
                center: this.center, //中心点
                zoom: 16, //初始化地图层级
                features: ['road', 'building', 'point'],
            })
        },
    },
}
</script>
<style lang="less" scoped>
.preview {
    height: auto;
    padding: 10px 15px 0;
    &-header {
        // height: 77px;
        display: flex;
        justify-content: space-between;
        align-items: center;
        .box {
            width: 15%;
            height: 77px;
            display: flex;
            &-left {
                width: 80px;
                height: 77px;
                display: flex;
                justify-content: center;
                align-items: center;
                .img {
                    width: 40px;
                    height: 40px;
                }
            }
            &-right {
                width: calc(100% - 80px);
                height: 77px;
                background-color: #fff;
                text-align: center;
                &-title {
                    margin-top: 11px;
                    font-size: 20px;
                    color: #1989fa;
                    font-weight: 700;
                }
                &-name {
                    height: 20px;
                    overflow: hidden;
                    text-overflow: ellipsis;
                    word-break: break-all;
                }
            }
        }
    }
    &-middle {
        margin-top: 20px;
        height: 320px;
        display: flex;
        .middle-left {
            width: 33%;
            height: 320px;
            margin-right: 20px;
            background-color: #fff;
            padding: 20px;
            display: flex;
            align-items: center;
            .equipment {
                width: 70%;
                margin-top: 28px;
            }
            .number {
                width: 30%;
                text-align: center;
                .title {
                    font-size: 18px;
                    font-weight: bold;
                }
                .production {
                    margin-top: 20px;
                    font-size: 50px;
                    color: #ffab23;
                    span {
                        margin-left: 5px;
                        font-size: 20px;
                        color: #1989fa;
                    }
                }
            }
        }
        .middle-right {
            background-color: #fff;
            flex: 1;
        }
    }
    &-footer {
        background: #fff;
        margin-top: 20px;
        height: 460px;
        display: flex;
        .map {
            width: 50%;
            height: 100%;
        }
    }
}
.amap-info-close {
    right: 8px;
}
</style>