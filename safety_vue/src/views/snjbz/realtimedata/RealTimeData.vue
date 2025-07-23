<template>
    <div class="mixingPile">
        <div class="mixingPile-header">
            <div class="mixingPile-header-title">实时数据</div>
            <div class="mixingPile-header-select">
                <a-form layout="inline">
                    <!-- <a-form-item label="项目">
                        <j-search-select-tag
                            placeholder="请选择项目"
                            v-model="selectParam.project"
                            :dictOptions="projectOption"
                            style="width: 200px"
                        >
                        </j-search-select-tag>
                    </a-form-item> -->
                    <a-form-item label="设备">
                        <j-search-select-tag
                            placeholder="请选择设备"
                            v-model="selectParam.equipment"
                            :dictOptions="equipmentOption"
                            style="width: 200px"
                        >
                        </j-search-select-tag>
                    </a-form-item>
                    <a-form-item>
                        <a-button type="primary" @click="searchData" icon="search">搜索</a-button>
                    </a-form-item>
                </a-form>
            </div>
        </div>
        <div class="mixingPile-content">
            <div class="mixingPile-content-left">
                <a-collapse default-active-key="1" :expand-icon-position="'right'" :bordered="false">
                    <a-collapse-panel key="1" header="管桩测试项目">
                        <div
                            class="equipment"
                            @click="getData(item)"
                            v-for="(item, index) in equipmentList"
                            :key="index"
                        >
                            <div
                                class="equipment-state"
                                :class="{ bg: item.worksta == '上钻' || item.worksta == '下钻' }"
                            >
                                {{ item.worksta }}
                            </div>
                            <div class="equipment-num">名称：{{ item.shebeino_dictText }}</div>
                            <div class="equipment-num distance">编号：{{ item.shebeino }}</div>
                            <div class="equipment-content distance">
                                <img src="../../../assets/img/equipment.png" alt="" />
                                <div class="equipment-time">
                                    <div class="equipment-num distance">打桩次数：{{ item.times }}次</div>
                                    <div class="equipment-num distance">当前桩号：{{ item.pileno }}</div>
                                </div>
                            </div>
                        </div>
                    </a-collapse-panel>
                </a-collapse>
            </div>
            <div class="mixingPile-content-right">
                <a-tabs default-active-key="1" size="large">
                    <a-tab-pane loading="true" :forceRender="true" tab="运行状况" key="1">
                        <div class="type-name">
                            <span>设备号:{{ shebeino }}</span>
                            <span class="mileage">桩号:{{ pileno }}</span>
                            <span class="state">状态:{{ worksta }}</span>
                        </div>
                        <div class="chart-content">
                            <div class="chart-box">
                                <div class="pangle">
                                    <div class="pangle-box">
                                        <div class="middle-box"></div>
                                        <div class="height-line"></div>
                                        <div class="width-line"></div>
                                        <div class="middle-round"></div>
                                        <!-- <a-tooltip placement="bottom">
                                                <template slot="title">
                                                    <span>prompt text</span>
                                                </template>
                                                <div class="middle-round"></div>
                                            </a-tooltip> -->
                                    </div>
                                </div>
                                <div class="text-one">{{ GZData.pileY ? parseFloat(GZData.pileY * 100).toFixed(2) : 0 }}%</div>
                                <div class="text-two">垂直度</div>
                            </div>
                            <div class="chart-box">
                                <div ref="ybpOne" :style="chartStyle"></div>
                            </div>
                            <div class="chart-box">
                                <div ref="ybpTwo" :style="chartStyle"></div>
                            </div>
                        </div>
                        <div class="table-content">
                            <div class="table-content-left">
                                <div class="table-content-left-progress">
                                    <a-progress :percent="pileLength" :show-info="false" :stroke-width="19" />
                                </div>
                                <div class="table-content-left-title">有效桩长</div>
                                <div class="table-content-left-title number">{{ GZData.curdep || 0 }}m</div>
                                <div class="table-content-left-number">{{ pileLength }}%</div>
                            </div>
                            <div class="table-content-middle">
                                <a-table
                                    :dataSource="dataSource"
                                    size="middle"
                                    rowKey="id"
                                    bordered
                                    :scroll="{ y: 200 }"
                                    :columns="columns"
                                    :pagination="false"
                                >
                                </a-table>
                            </div>
                            <div class="table-content-right">
                                <div class="box-item">
                                    <div class="box-content item-background">设计桩长(m)</div>
                                    <div class="box-content flex-text">{{ GZData.designdep || 0 }}</div>
                                    <div class="box-content item-background">开始时间</div>
                                    <div class="box-content flex-text" style="line-height: 22px">
                                        {{ GZData.pileStarttime || 0 }}
                                    </div>
                                </div>
                                <div class="box-item">
                                    <div class="box-content item-background">离地桩长(m)</div>
                                    <div class="box-content flex-text">{{ GZData.grounddep ? parseFloat(GZData.grounddep).toFixed(2) : 0 }}</div>
                                    <div class="box-content item-background">最大垂直度(%)</div>
                                    <div class="box-content flex-text">{{ GZData.pileY ? parseFloat(GZData.pileY * 100).toFixed(2) : 0 }}</div>
                                </div>
                                <div class="box-item">
                                    <div class="box-content item-background">平均压桩力(KPa)</div>
                                    <div class="box-content flex-text">{{  GZData.pileAveupress ? parseFloat(GZData.pileAveupress).toFixed(2) : 0 }}</div>
                                </div>
                                <div class="box-item">
                                    <div class="box-content item-background">最大压桩力(KPa)</div>
                                    <div class="box-content flex-text">{{ GZData.pileUpress ? parseFloat(GZData.pileUpress).toFixed(2) : 0 }}</div>
                                </div>
                            </div>
                        </div>
                    </a-tab-pane>
                    <a-tab-pane loading="true" :forceRender="true" tab="曲线图" key="2">
                        <div class="type-name">
                            <span>设备号:{{ shebeino }}</span>
                            <span class="mileage">桩号:{{ pileno }}</span>
                            <span class="state">状态:{{ worksta }}</span>
                        </div>
                        <div>
                            <div ref="qxtOne" :style="chartStyle1"></div>
                            <div ref="qxtTwo" :style="chartStyle2"></div>
                        </div>
                    </a-tab-pane>
                </a-tabs>
            </div>
        </div>
    </div>
</template>
<script>
import { getAction } from '@/api/manage'

export default {
    name: 'RealTimeData',
    components: {},
    data() {
        return {
            shebeino: '',
            pileno: '',
            worksta: '',
            scheduledTask: null,
            selectParam: {},
            GZData: {},
            state: false,
            projectOption: [],
            equipmentOption: [],
            equipmentList: [],
            dataSource: [],
            columns: [
                {
                    title: '序号',
                    dataIndex: '',
                    key: 'rowIndex',
                    align: 'center',
                    width: 60,
                    customRender: function (t, r, index) {
                        return parseInt(index) + 1
                    },
                },
                {
                    title: '长度(m)',
                    align: 'center',
                    dataIndex: 'partDep',
                    customRender: function (t, r, index) {
                        return parseFloat(t).toFixed(2)
                    },
                },
                {
                    title: '时长',
                    align: 'center',
                    dataIndex: 'partPilec',
                },
                {
                    title: '接桩时间',
                    align: 'center',
                    dataIndex: 'partEndtime',
                },
            ],
            chartStyle: { width: '100%', height: '250px' }, //图表样式
            chartStyle1: { width: '850px', height: '320px' },
            chartStyle2: { width: '850px', height: '320px' },
            url: {
                listSb: 'devicepipepilerealdata/devicePipepileRealdata/list',
                // listGz: 'devicepipepilehistorydatapart/devicePipepileHistorydataPart/listGz',
                listGzTwo: 'devicepipepilerealdata/devicePipepileRealdata/listGz',
                listqxt: 'devicepipepilehistorydatapart/devicePipepileHistorydataPart/listqxt',
            },
        }
    },
    mounted() {
        this.getEquipment()
        this.getListSb()
        this.scheduledTask && clearInterval(this.scheduledTask)
        this.scheduledTask = setInterval(this.getListSb, 10000);
    },
    beforeDestroy() {
        clearInterval(this.scheduledTask)
    },
    methods: {
        getEquipment() {
            getAction(this.url.listSb).then((res) => {
                if (res.success) {
                    let data = res.result.records
                    data.forEach((item) => {
                        this.equipmentOption.push({ text: item.shebeino_dictText, value: item.shebeino })
                    })
                }
            })
        },
        //管桩设备
        getListSb() {
            let params = {
                shebeino: this.selectParam.equipment,
            }
            getAction(this.url.listSb, params).then((res) => {
                if (res.success) {
                    this.equipmentList = res.result.records
                    if (this.equipmentList.length > 0) {
                        this.getData(this.equipmentList[0])
                    }
                }
            })
        },
        //管桩数据
        getData(data) {
            this.shebeino = data.shebeino
            this.pileno = data.pileno
            this.worksta = data.worksta
            if (data.worksta !== '离线') {
                let params = {
                    shebeino: data.shebeino,
                    pileNo: data.pileno,
                }
                //管桩表格数据
                // getAction(this.url.listGz, params).then((res) => {
                //     if (res.success) {
                //         this.dataSource = res.result.records
                //     }
                // })
                //管桩仪表盘数据
                getAction(this.url.listGzTwo, params).then((res) => {
                    if (res.success) {
                        this.GZData = res.result
                        this.initEchartsYBP('ybpOne', 'cm/min', 0, 1000, parseFloat(this.GZData.speed).toFixed(2), '速度')
                        this.initEchartsYBP('ybpTwo', 'KPa', 0, 5000, parseFloat(this.GZData.pileAveupress).toFixed(2), '压桩力')
                    }
                })
                //管桩曲线图数据和管桩表格数据
                getAction(this.url.listqxt, params).then((res) => {
                    if (res.success) {
                        this.dataSource = res.result
                        let data = res.result
                        let partDepList = []
                        let partSpeedList = []
                        let partUpressList = []
                        let datatimeList = []
                        data.forEach((item) => {
                            partDepList.push(item.partDep)
                            partSpeedList.push(item.partSpeed)
                            partUpressList.push(item.partUpress)
                            datatimeList.push(item.datatime)
                        })
                        this.initEchartsOne(datatimeList, partSpeedList, partDepList)
                        this.initEchartsTwo(datatimeList, partUpressList)
                    }
                })
            } else {
                this.dataSource = []
                this.GZData = {}
                this.initEchartsYBP('ybpOne', 'cm/min', 0, 1000, 0, '速度')
                this.initEchartsYBP('ybpTwo', 'KPa', 0, 5000, 0, '压桩力')
                this.initEchartsOne()
                this.initEchartsTwo()
            }
        },
        searchData() {
            this.getListSb()
        },
        //仪表盘
        initEchartsYBP(chart, name, min, max, value, text) {
            let option = {
                title: [
                    {
                        text: text, //标题
                        left: 'center',
                        top: '82%',
                        textStyle: {
                            color: '#000',
                            fontSize: 18,
                            fontWeight: 600,
                        },
                    },
                ],
                series: [
                    {
                        type: 'gauge',
                        min: min, //起始最小刻度值
                        max: max, //最大刻度值
                        splitNumber: 5, //分隔份数
                        radius: '90%',
                        axisLine: {
                            lineStyle: {
                                width: 12,
                                color: [
                                    [0.3, '#67e0e3'],
                                    [0.7, '#37a2da'],
                                    [1, '#fd666d'],
                                ],
                            },
                        },
                        pointer: {
                            itemStyle: {
                                color: 'inherit',
                            },
                        },
                        axisTick: {
                            distance: 5,
                            length: 5,
                            lineStyle: {
                                color: '#626679',
                                width: 1,
                            },
                        },
                        splitLine: {
                            distance: 5,
                            length: 8,
                            lineStyle: {
                                color: '#626679',
                                width: 2,
                            },
                        },
                        axisLabel: {
                            color: 'inherit',
                            distance: 15,
                            fontSize: 12,
                        },
                        title: {
                            fontWeight: 'normal',
                            color: '#626679',
                            //里面文字的大小
                            fontSize: 12,
                        },
                        detail: {
                            valueAnimation: true,
                            formatter: '{value}',
                            color: 'inherit',
                            fontSize: 14,
                        },
                        data: [
                            {
                                value: value,
                                name: name,
                            },
                        ],
                    },
                ],
            }
            let myChart = this.$echarts.getInstanceByDom(this.$refs[chart])
            if (myChart == null) {
                myChart = this.$echarts.init(this.$refs[chart])
            }
            myChart.setOption(option)
        },
        //曲线图1
        initEchartsOne(xList, yListOne, yListTwo) {
            let option = {
                legend: {
                    data: ['速度(cm/min)', '深度(m)'],
                },
                tooltip: {
                    trigger: 'axis',
                },
                title: [
                    {
                        text: '速度变化曲线',
                        top: 10,
                        left: 10,
                        textStyle: {
                            fontSize: 16,
                        },
                    },
                    {
                        text: '深度变化曲线',
                        top: 10,
                        right: 10,
                        textStyle: {
                            fontSize: 16,
                        },
                    },
                ],
                xAxis: {
                    type: 'category',
                    boundaryGap: true,
                    axisLabel: {
                        interval: 3,
                        fontSize: 14,
                    },
                    axisTick: {
                        show: false, // 隐藏坐标轴的刻度线
                    },
                    data: xList,
                },
                yAxis: [
                    {
                        name: '速度(cm/min)',
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
                    {
                        name: '深度(m)',
                        nameTextStyle: {
                            align: 'center',
                            fontSize: 12,
                        },
                        axisLine: { show: true },
                        type: 'value',
                        // min: 0,
                        axisLabel: {
                            fontSize: 14,
                            // formatter: '{value} m',
                        },
                    },
                ],
                series: [
                    {
                        name: '速度(cm/min)',
                        type: 'line',
                        smooth: true,
                        yAxisIndex: 0,
                        data: yListOne,
                    },
                    {
                        name: '深度(m)',
                        type: 'line',
                        smooth: true,
                        yAxisIndex: 1,
                        data: yListTwo,
                    },
                ],
            }
            let myChart = this.$echarts.getInstanceByDom(this.$refs.qxtOne)
            if (myChart == null) {
                myChart = this.$echarts.init(this.$refs.qxtOne)
            }
            myChart.setOption(option)
        },
        //曲线图2
        initEchartsTwo(xList, yList) {
            let option = {
                legend: {
                    data: ['压桩力'],
                },
                tooltip: {
                    trigger: 'axis',
                },
                title: [
                    {
                        text: '压桩力变化曲线',
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
                        interval: 3,
                        fontSize: 14,
                    },
                    axisTick: {
                        show: false, // 隐藏坐标轴的刻度线
                    },
                    data: xList,
                },
                yAxis: [
                    {
                        name: '压桩力(KPa)',
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
                        name: '压桩力',
                        type: 'line',
                        smooth: true,
                        data: yList,
                    },
                ],
            }
            let myChart = this.$echarts.getInstanceByDom(this.$refs.qxtTwo)
            if (myChart == null) {
                myChart = this.$echarts.init(this.$refs.qxtTwo)
            }
            myChart.setOption(option)
        },
    },
    computed: {
        pileLength() {
            return (Number(this.GZData.curdep) / Number(this.GZData.designdep)) * 100 || 0
        },
    },
}
</script>
<style lang="less" scoped>
.mixingPile-header {
    height: 50px;
    background-color: #fff;
    border-radius: 3px;
    padding: 0 20px;
    display: flex;
    align-items: center;
    &-title {
        font-size: 14px;
        color: #101010;
        padding-left: 10px;
        position: relative;
    }
    &-title::before {
        position: absolute;
        width: 3px;
        height: 13px;
        background-color: #1989fa;
        left: 0;
        top: 50%;
        transform: translateY(-50%);
        content: '';
    }
    &-select {
        margin-left: 20px;
    }
}
.mixingPile-content {
    margin-top: 20px;
    height: calc(100% - 55px);
    display: flex;
    &-left {
        width: 270px;
        background-color: #fff;
        margin-right: 20px;
        height: 100%;
        .equipment {
            border: 1px solid #94c6f9;
            background-color: #eaedee;
            margin: 0 10px 20px;
            border-radius: 3px;
            position: relative;
            padding: 15px 25px 20px;
            cursor: pointer;
            .distance {
                margin-top: 10px;
            }
            .bg {
                background-color: #4eee94;
            }
            &-state {
                position: absolute;
                top: 6px;
                right: 0;
                height: 20px;
                width: 48px;
                background-color: #919da7;
                border-radius: 20px 0 0 20px;
                font-size: 12px;
                line-height: 20px;
                text-align: center;
                color: #fff;
            }
            &-num {
                font-size: 12px;
                color: #666;
            }
            &-content {
                display: flex;
                margin-left: -11px;
            }
            &-time {
                word-wrap: break-word;
            }
        }
    }
    &-right {
        height: 100%;
        width: calc(100% - 290px);
        background-color: #fff;
        overflow: hidden;
        padding-left: 25px;
        .type-name {
            height: 28px;
            font-size: 16px;
            color: #101010;
            font-weight: 700;
            .mileage {
                margin-left: 20px;
            }
            .state {
                margin-left: 30px;
            }
        }
        .chart-content {
            width: 100%;
            height: 250px;
            display: flex;
            align-items: center;
            .chart-box {
                width: 28%;
                .pangle {
                    display: flex;
                    justify-content: center;
                    .pangle-box {
                        position: relative;
                        width: 80px;
                        height: 80px;
                        border: 2px solid #4dbce6;
                        // text-align: center;
                        .middle-box {
                            width: 40px;
                            height: 40px;
                            border: 2px solid #4dbce6;
                            position: absolute;
                            left: 50%;
                            top: 50%;
                            transform: translate(-50%, -50%);
                        }
                        .middle-round {
                            width: 6px;
                            height: 6px;
                            border-radius: 50% 50%;
                            background-color: red;
                            position: absolute;
                            // cursor: pointer;
                            left: 50%;
                            top: 50%;
                            transform: translate(-50%, -50%);
                        }
                        .height-line {
                            position: absolute;
                            height: 2px;
                            top: 50%;
                            left: -30%;
                            right: -30%;
                            transform: translateY(-50%);
                            background-color: #949494;
                        }
                        .width-line {
                            position: absolute;
                            left: 50%;
                            top: -30%;
                            bottom: -30%;
                            width: 2px;
                            transform: translateX(-50%);
                            background-color: #949494;
                        }
                    }
                }
                .text-one {
                    text-align: center;
                    margin-top: 30px;
                    color: #1a88f7;
                    font-weight: 700;
                    font-size: 12px;
                }
                .text-two {
                    text-align: center;
                    margin-top: 10px;
                    color: #1a88f7;
                    font-weight: 700;
                    font-size: 12px;
                }
            }
        }
        .table-content {
            width: 100%;
            height: 320px;
            display: flex;
            align-items: center;
            margin-top: 20px;
            &-left {
                position: relative;
                height: 100%;
                margin-left: -50px;
                .number {
                    bottom: 2%;
                }
                &-progress {
                    width: 250px;
                    transform: rotate(90deg);
                    margin-top: 118px;
                }
                &-number {
                    position: absolute;
                    top: 36%;
                    left: 50%;
                    transform: translate(50%);
                }
                &-title {
                    position: absolute;
                    bottom: 10%;
                    left: 50%;
                    font-size: 18px;
                    font-weight: bold;
                    transform: translate(-50%);
                }
            }
            &-middle {
                width: 320px;
            }
            &-right {
                width: 410px;
                border: 1px solid #eaebf3;
                border-bottom: none;
                margin-left: 35px;
                .box-item {
                    display: flex;
                    font-weight: 700;
                    border-bottom: 1px solid #eee;
                    font-size: 14px;
                    .flex-text {
                        flex: 1;
                    }
                    .box-content {
                        width: 112px;
                        height: 45px;
                        line-height: 45px;
                        padding-left: 5px;
                        color: #606266;
                    }
                    .item-background {
                        background-color: #e7e9f2;
                    }
                }
            }
        }
    }
}
</style>