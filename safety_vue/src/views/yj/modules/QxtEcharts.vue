<template>
    <div ref="chart" :style="chartStyle"></div>
</template>

<script>
export default {
    name: 'QxtEcharts',
    props: {
        title: {
            type: String,
            default: '',
        },
        chartStyle: {
            type: Object,
            default: () => ({ width: '1000px', height: '450px' }),
        },
        chartData: {
            type: Object,
            default() {
                return {
                    xData: [],
                    series: [],
                }
            },
        },
    },
    mounted() {
        this.initChart()
    },
    data() {
        return {
            option: {
                legend: {},
                tooltip: {
                    trigger: 'axis',
                },
                grid: {
                    left: '4%',
                    right: '4%',
                    bottom: '5%',
                    containLabel: true,
                },
                title: [
                    {
                        text: this.title,
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
                        interval: 50,
                        fontSize: 14,
                    },
                    axisTick: {
                        show: false, // 隐藏坐标轴的刻度线
                    },
                    data: [],
                },
                yAxis: [
                    {
                        // name: '进浆压力(MPa)',
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
                        // name: '返浆压力(MPa)',
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
                // series: [
                //     {
                //         name: '进浆压力(MPa)',
                //         type: 'line',
                //         smooth: true,
                //         yAxisIndex: 0,
                //         data: yListOne,
                //     },
                //     {
                //         name: '返浆压力(MPa)',
                //         type: 'line',
                //         smooth: true,
                //         yAxisIndex: 1,
                //         data: yListTwo,
                //     },
                // ],
                series: [],
            },
        }
    },
    methods: {
        initChart(chartData) {
            if (chartData) {
                this.option.xAxis.data = chartData.xData
                this.option.series = chartData.series
            }
            let myChart = this.$echarts.getInstanceByDom(this.$refs.chart)
            if (myChart == null) {
                myChart = this.$echarts.init(this.$refs.chart)
            }
            myChart.setOption(this.option)
        },
    },
    watch: {
        chartData: {
            handler(newVal) {
                this.initChart(newVal)
            },
            deep: true,
        },
    },
}
</script>