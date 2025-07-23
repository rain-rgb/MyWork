<template>
    <div id="paperA4">
        <div class="tableHeader">
            <j-j-head-report :formData.sync="reportData"></j-j-head-report>
        </div>
        <div class="tableContent">
            <table class="la-table">
                <tr class="not-print" style="background-color: rgb(178, 214, 237)">
                    <td colspan="4" style="height: 21px">
                        <a-radio-group v-model="reportData.rou_chk">
                            <a-radio value="rad1">保留到整数</a-radio>
                            <a-radio value="rad2">保留到1位小数</a-radio>
                        </a-radio-group>
                    </td>
                </tr>
                <tr>
                    <td>钢筋牌号</td>
                    <td><input v-model="reportData.sampleGangJinZhongLei" type="text" /></td>
                    <td>公称直径(mm)</td>
                    <td><input v-model="reportData.gjzj" type="text" /></td>
                </tr>
            </table>
            <table class="la-table next">
                <tr>
                    <td>生产批号</td>
                    <td>编号</td>
                    <td>理论重量(kg/m)</td>
                    <td>试件长度 (mm)</td>
                    <td>试件总长度 (mm)</td>
                    <td>试件总重量(kg)</td>
                    <td>重量偏差(%)</td>
                </tr>
                <tr>
                    <td rowspan="5"><a-textarea v-model="reportData.sampleShengChanPiHao" auto-size /></td>
                    <td><a-textarea v-model="reportData.shijianbh1" auto-size /></td>
                    <td rowspan="5"><input v-model="reportData.lilzhil1" type="text" /></td>
                    <td><input v-model="reportData.zongcdu1" type="text" /></td>
                    <td rowspan="5"><input v-model="reportData.sjzcd" type="text" /></td>
                    <td rowspan="5"><input v-model="reportData.yangpzhil1" type="text" /></td>
                    <!-- <td rowspan="5"><input v-model="reportData.zhilpianc1" type="text" /></td> -->
                    <td rowspan="5">
                        <div style="width: 80px">
                            {{
                                reportData.zhilpianc1
                                    ? reportData.rou_chk == 'rad1'
                                        ? Number(reportData.zhilpianc1).toFixed()
                                        : Number(reportData.zhilpianc1).toFixed(1)
                                    : ''
                            }}
                        </div>
                    </td>
                </tr>
                <tr>
                    <td><a-textarea v-model="reportData.shijianbh2" auto-size /></td>
                    <td><input v-model="reportData.zongcdu2" type="text" /></td>
                </tr>
                <tr>
                    <td><a-textarea v-model="reportData.shijianbh3" auto-size /></td>
                    <td><input v-model="reportData.zongcdu3" type="text" /></td>
                </tr>
                <tr>
                    <td><a-textarea v-model="reportData.shijianbh4" auto-size /></td>
                    <td><input v-model="reportData.zongcdu4" type="text" /></td>
                </tr>
                <tr>
                    <td><a-textarea v-model="reportData.shijianbh5" auto-size /></td>
                    <td><input v-model="reportData.zongcdu5" type="text" /></td>
                </tr>
            </table>
        </div>
        <div class="tableRemark">
            <j-j-tail-report :formData.sync="reportData"></j-j-tail-report>
        </div>
    </div>
</template>
<script>
import JJHeadReport from './head/JJHeadReport'
import JJTailReport from './tail/JJTailReport'

export default {
    name: 'JJ1001d',
    props: ['formData'],
    components: {
        JJHeadReport,
        JJTailReport,
    },
    data() {
        return {
            bg: {
                sampleGcbw: 'sampleGcbw',
                renwuNo: 'renwuNo',
                zhilpianc1: 'jcjg_zlpc',
            },
        }
    },
    mounted() {},
    computed: {
        reportData: {
            get: function () {
                return this.formData ? this.formData : {}
            },
            set: function (newVal) {
                this.$emit('update:formData', newVal)
                return newVal
            },
        },
    },
    methods: {},
}
</script>
<style lang="less" scoped>
#paperA4 {
    height: 1046px;
    width: 716px;
    background: #fff9ec;
    margin: 0 auto;
    padding: 18px 36px;
    font-family: SimSun;
    font-size: 12px;
    color: #000;
    .tableContent {
        margin-top: -1px;
        .la-table {
            width: 100%;
            margin: 0;
            border-right: 2px solid #000;
            border-left: 2px solid #000;
            color: #000;
            background-color: #fff9ec;
            td {
                padding: 1px;
                height: 33px;
                font-size: 12px;
                line-height: 18px;
                text-align: center;
                border: 1px solid #000;
                position: relative;
            }
        }
        .next {
            margin-top: -1px;
        }
        input {
            width: 100%;
            height: 100%;
            border: none;
            outline: none;
            text-align: center;
        }
        select {
            width: 100%;
            height: 100%;
            text-align-last: center;
            outline: none;
        }
        textarea {
            resize: none;
            border: none;
            width: 100%;
            font-size: 12px;
            color: #000;
            line-height: 16px;
            min-height: 18px;
            padding: 1px;
            text-align: center;
            // outline: none;
            // word-break: break-all;
            // overflow: hidden;
        }
    }
    .tableRemark {
        height: 420px;
    }
}
@media print {
    /* 隐藏不需要打印的部分 */
    .not-print {
        display: none !important;
    }
    select {
        border: none;
        appearance: none;
    }
}
</style>