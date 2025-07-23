<template>
    <div>
        <a-card title="各标段管桩监控情况统计" :bordered="false" :headStyle="{ color: '#0785fd' }">
            <a-table
                :rowKey="(record, index) => index"
                size="middle"
                bordered
                :data-source="dataSource"
                :columns="columns"
                :loading="loading"
                :scroll="{ y: 400 }"
                :pagination="false"
            >
            </a-table>
        </a-card>
        <a-card title="各标段不合格情况统计" :bordered="false" :headStyle="{ color: '#0785fd' }">
            <div ref="zztChart" :style="chartStyle"></div>
        </a-card>
    </div>
</template>

<script>
import { getAction } from '@api/manage'
import Vue from 'vue'
export default {
    name: 'PipeStatistics',
    components: {},
    data() {
        return {
            sys_depart_orgcode: '',
            loading: false,
            chartStyle: { width: '100%', height: '400px' },
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
                    title: '标段',
                    align: 'center',
                    dataIndex: 'xiangmu',
                },
                {
                    title: '数量',
                    align: 'center',
                    dataIndex: 'zongshu',
                },
                {
                    title: '合格数量',
                    align: 'center',
                    dataIndex: 'hegeshu',
                },
                {
                    title: '合格率（%）',
                    align: 'center',
                    dataIndex: 'hegelv',
                },
                {
                    title: '闭合率（%）',
                    align: 'center',
                    dataIndex: 'bihelv',
                },
            ],
            url: {
                rclist: '/devicepipepilehistorydataone/devicePipepileHistorydataOne/rclist',
            },
        }
    },
    mounted() {
        this.sys_depart_orgcode = Vue.ls.get('SYS_DEPART_ORGCODE')
        this.getList()
    },
    methods: {
        getList() {
            this.loading = true
            let params = { orgCode: this.sys_depart_orgcode }
            getAction(this.url.rclist, params)
                .then((res) => {
                    if (res.success) {
                        let xList = []
                        let zzycList = [] //桩长不合格
                        let qjycList = [] //倾斜度不合格
                        this.dataSource = res.result
                        this.dataSource.forEach((item) => {
                            xList.push(item.xiangmu)
                            zzycList.push(item.findzzyc)
                            qjycList.push(item.findqjyc)
                        })
                        this.initEchartsZZT('zztChart', xList, zzycList, qjycList)
                    }
                })
                .finally(() => {
                    this.loading = false
                })
        },
        //柱状图
        initEchartsZZT(chart, xList, yListOne, yListTwo) {
            let option = {
                color: ['#4F81BD', '#C0504D'],
                legend: {
                    data: ['桩长不合格', '倾斜度不合格'],
                },
                tooltip: {
                    trigger: 'axis',
                    axisPointer: {
                        type: 'shadow',
                    },
                },
                grid: {
                    left: '3%',
                    right: '4%',
                    bottom: '3%',
                    containLabel: true,
                },
                xAxis: {
                    type: 'category',
                    axisLabel: {
                        fontSize: 14,
                    },
                    axisTick: {
                        show: false,
                    },
                    data: xList,
                },
                yAxis: {
                    type: 'value',
                },
                series: [
                    {
                        type: 'bar',
                        name: '桩长不合格',
                        // barWidth: '60%',
                        data: yListOne,
                    },
                    {
                        type: 'bar',
                        name: '倾斜度不合格',
                        // barWidth: '60%',
                        data: yListTwo,
                    },
                ],
            }
            let myChart = this.$echarts.getInstanceByDom(this.$refs[chart])
            if (myChart == null) {
                myChart = this.$echarts.init(this.$refs[chart])
            }
            myChart.setOption(option)
        },
    },
}
</script>

<style scoped>
</style>