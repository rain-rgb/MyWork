<template>
    <div id="paperA4">
        <div class="tableHeader">
            <j-j-head-report :formData.sync="reportData"></j-j-head-report>
        </div>
        <div class="tableContent">
            <table class="la-table">
                <tr>
                    <td>钢筋牌号</td>
                    <td colspan="3"><input v-model="reportData.sampleGangJinZhongLei" type="text" /></td>
                </tr>
                <tr>
                    <td>接头形式</td>
                    <td><input v-model="reportData.jietouxs" type="text" /></td>
                    <td>公称直径(mm)</td>
                    <td><input v-model="reportData.gjzj" type="text" /></td>
                </tr>
                <tr>
                    <td>焊工姓名及上岗证编号</td>
                    <td colspan="3">
                        <select v-model="reportData.hgxmjsgz" style="border: none; appearance: none">
                            <option style="display: none" value="">{{ reportData.xiuyuegz }}</option>
                            <option value="何平T522122197304072011">何平T522122197304072011</option>
                            <option value="牛晓战T410527197302285095">牛晓战T410527197302285095</option>
                            <option value="黄师明T612430198211200419">黄师明T612430198211200419</option>
                            <option value="杨平T6102528198608096017">杨平T6102528198608096017</option>
                        </select>
                    </td>
                </tr>
            </table>
            <table class="la-table next">
                <tr>
                    <td colspan="5">
                        强度修约
                        <select v-model="reportData.xiuyuegz" style="width: 100px">
                            <option style="display: none" value="">
                                {{ reportData.xiuyuegz ? (reportData.xiuyuegz == 0 ? '1MPa' : '1 5 10MPa') : '' }}
                            </option>
                            <option value="0">1MPa</option>
                            <option value="1">1 5 10MPa</option>
                        </select>
                    </td>
                </tr>
                <tr>
                    <td rowspan="7">拉伸性能</td>
                    <td>试件编号</td>
                    <td><a-textarea v-model="reportData.shijbh1" auto-size /></td>
                    <td><a-textarea v-model="reportData.shijbh2" auto-size /></td>
                    <td><a-textarea v-model="reportData.shijbh3" auto-size /></td>
                </tr>
                <tr>
                    <td>横截面积(mm²)</td>
                    <td><input v-model="reportData.hjmj1" type="text" /></td>
                    <td><input v-model="reportData.hjmj2" type="text" /></td>
                    <td><input v-model="reportData.hjmj3" type="text" /></td>
                </tr>
                <tr>
                    <td>焊缝长度(mm)</td>
                    <td><input v-model="reportData.jtcd1" type="text" /></td>
                    <td><input v-model="reportData.jtcd2" type="text" /></td>
                    <td><input v-model="reportData.jtcd3" type="text" /></td>
                </tr>
                <tr>
                    <td>极限荷载(kN)</td>
                    <td><input v-model="reportData.jxhz1" type="text" /></td>
                    <td><input v-model="reportData.jxhz2" type="text" /></td>
                    <td><input v-model="reportData.jxhz3" type="text" /></td>
                </tr>
                <tr>
                    <td>抗拉强度(MPa)</td>
                    <td><input v-model="reportData.klqd1" type="text" /></td>
                    <td><input v-model="reportData.klqd2" type="text" /></td>
                    <td><input v-model="reportData.klqd3" type="text" /></td>
                </tr>
                <tr>
                    <td>断口距焊口距离(mm)</td>
                    <td><input v-model="reportData.dlwz1" type="text" /></td>
                    <td><input v-model="reportData.dlwz2" type="text" /></td>
                    <td><input v-model="reportData.dlwz3" type="text" /></td>
                </tr>
                <tr>
                    <td>断裂形式</td>
                    <td>
                        <select v-model="reportData.dlxs1" style="width: 160px; border: none; appearance: none">
                            <option style="display: none" value="">{{ reportData.dlxs1 }}</option>
                            <option value=""></option>
                            <option value="脆断">脆断</option>
                            <option value="塑断">塑断</option>
                            <option value="杯口状断裂">杯口状断裂</option>
                            <option value="延性断裂">延性断裂</option>
                            <option value="钢筋母材拉断">钢筋母材拉断</option>
                        </select>
                    </td>
                    <td>
                        <select v-model="reportData.dlxs2" style="width: 160px; border: none; appearance: none">
                            <option style="display: none" value="">{{ reportData.dlxs2 }}</option>
                            <option value=""></option>
                            <option value="脆断">脆断</option>
                            <option value="塑断">塑断</option>
                            <option value="杯口状断裂">杯口状断裂</option>
                            <option value="延性断裂">延性断裂</option>
                            <option value="钢筋母材拉断">钢筋母材拉断</option>
                        </select>
                    </td>
                    <td>
                        <select v-model="reportData.dlxs3" style="width: 160px; border: none; appearance: none">
                            <option style="display: none" value="">{{ reportData.dlxs3 }}</option>
                            <option value=""></option>
                            <option value="脆断">脆断</option>
                            <option value="塑断">塑断</option>
                            <option value="杯口状断裂">杯口状断裂</option>
                            <option value="延性断裂">延性断裂</option>
                            <option value="钢筋母材拉断">钢筋母材拉断</option>
                        </select>
                    </td>
                </tr>
                <tr>
                    <td rowspan="4">弯曲性能</td>
                    <td>试件编号</td>
                    <td><a-textarea v-model="reportData.wq_sjbh1" auto-size /></td>
                    <td><a-textarea v-model="reportData.wq_sjbh2" auto-size /></td>
                    <td><a-textarea v-model="reportData.wq_sjbh3" auto-size /></td>
                </tr>
                <tr>
                    <td>弯曲角度(°)</td>
                    <td><input v-model="reportData.wqjd1" type="text" /></td>
                    <td><input v-model="reportData.wqjd2" type="text" /></td>
                    <td><input v-model="reportData.wqjd3" type="text" /></td>
                </tr>
                <tr>
                    <td>弯曲直径(mm)</td>
                    <td><input v-model="reportData.wqzj1" type="text" /></td>
                    <td><input v-model="reportData.wqzj2" type="text" /></td>
                    <td><input v-model="reportData.wqzj3" type="text" /></td>
                </tr>
                <tr>
                    <td>试验结果</td>
                    <td><a-textarea v-model="reportData.wqsyjg1" auto-size /></td>
                    <td><a-textarea v-model="reportData.wqsyjg2" auto-size /></td>
                    <td><a-textarea v-model="reportData.wqsyjg3" auto-size /></td>
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
    name: 'JJ1001b',
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
                qfqd1: 'jcjg_qfqd1',
                qfqd2: 'jcjg_qfqd2',
                qfqd3: 'jcjg_qfqd3',
                qfqd4: 'jcjg_qfqd4',
                klqd1: 'jcjg_klqd1',
                klqd2: 'jcjg_klqd2',
                klqd3: 'jcjg_klqd3',
                klqd4: 'jcjg_klqd4',
                qutebi1: 'jcjg_qutebi1',
                qutebi2: 'jcjg_qutebi2',
                qutebi3: 'jcjg_qutebi3',
                qutebi4: 'jcjg_qutebi4',
                qiangqubi1: 'jcjg_qiangqubi1',
                qiangqubi2: 'jcjg_qiangqubi2',
                qiangqubi3: 'jcjg_qiangqubi3',
                qiangqubi4: 'jcjg_qiangqubi4',
                dhscl1: 'jcjg_dhscl1',
                dhscl2: 'jcjg_dhscl2',
                dhscl3: 'jcjg_dhscl3',
                dhscl4: 'jcjg_dhscl4',
                zdlzysl1: 'jcjg_zdlzysl1',
                zdlzysl2: 'jcjg_zdlzysl2',
                zdlzysl3: 'jcjg_zdlzysl3',
                zdlzysl4: 'jcjg_zdlzysl4',
                duankxs1: 'duankxs1',
                duankxs2: 'duankxs2',
                duankxs3: 'duankxs3',
                duankxs4: 'duankxs4',
                wq_syjg1: 'jcjg_wqxn1',
                wq_syjg2: 'jcjg_wqxn2',
                wq_syjg3: 'jcjg_wqxn3',
                wq_syjg4: 'jcjg_wqxn4',
                fw_syjg1: 'jcjg_fxwq1',
                fw_syjg2: 'jcjg_fxwq2',
                fw_syjg3: 'jcjg_fxwq3',
                fw_syjg4: 'jcjg_fxwq4',
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
                height: 26px;
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
        height: 240px;
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