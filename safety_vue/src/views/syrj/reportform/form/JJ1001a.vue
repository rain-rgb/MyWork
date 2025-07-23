<template>
    <div id="paperA4">
        <div class="tableHeader">
            <j-j-head-report :formData.sync="reportData"></j-j-head-report>
        </div>
        <div class="tableContent">
            <table class="la-table">
                <tr>
                    <td style="width: 70px">牌号</td>
                    <td style="width: 100px"><input v-model="reportData.sampleGangJinZhongLei" type="text" /></td>
                    <td>生产批号</td>
                    <td><a-textarea v-model="reportData.sampleShengChanPiHao" auto-size /></td>
                    <td>公称直径(mm)</td>
                    <td style="width: 70px"><input v-model="reportData.gjzj" type="text" /></td>
                </tr>
            </table>
            <table class="la-table next">
                <tr>
                    <td colspan="2">试件编号</td>
                    <td><a-textarea v-model="reportData.shijianbh1" auto-size /></td>
                    <td><a-textarea v-model="reportData.shijianbh2" auto-size /></td>
                    <td><a-textarea v-model="reportData.shijianbh3" auto-size /></td>
                    <td><a-textarea v-model="reportData.shijianbh4" auto-size /></td>
                </tr>
                <tr>
                    <td colspan="2">公称截面积(mm²)</td>
                    <td><input v-model="reportData.gcjmj1" type="text" /></td>
                    <td><input v-model="reportData.gcjmj2" type="text" /></td>
                    <td><input v-model="reportData.gcjmj3" type="text" /></td>
                    <td><input v-model="reportData.gcjmj4" type="text" /></td>
                </tr>
                <tr>
                    <td colspan="6">
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
                    <td rowspan="2">屈服强度</td>
                    <td style="width: 110px">屈服荷载(kN)</td>
                    <td><input v-model="reportData.qfhz1" type="text" /></td>
                    <td><input v-model="reportData.qfhz2" type="text" /></td>
                    <td><input v-model="reportData.qfhz3" type="text" /></td>
                    <td><input v-model="reportData.qfhz4" type="text" /></td>
                </tr>
                <tr>
                    <td>屈服强度(MPa)</td>
                    <td><input v-model="reportData.qfqd1" type="text" /></td>
                    <td><input v-model="reportData.qfqd2" type="text" /></td>
                    <td><input v-model="reportData.qfqd3" type="text" /></td>
                    <td><input v-model="reportData.qfqd4" type="text" /></td>
                </tr>
                <tr>
                    <td rowspan="2">抗拉强度</td>
                    <td>极限荷载(kN)</td>
                    <td><input v-model="reportData.jxhz1" type="text" /></td>
                    <td><input v-model="reportData.jxhz2" type="text" /></td>
                    <td><input v-model="reportData.jxhz3" type="text" /></td>
                    <td><input v-model="reportData.jxhz4" type="text" /></td>
                </tr>
                <tr>
                    <td>抗拉强度(MPa)</td>
                    <td><input v-model="reportData.klqd1" type="text" /></td>
                    <td><input v-model="reportData.klqd2" type="text" /></td>
                    <td><input v-model="reportData.klqd3" type="text" /></td>
                    <td><input v-model="reportData.klqd4" type="text" /></td>
                </tr>
                <tr>
                    <td rowspan="2">抗震力学特征值</td>
                    <td>
                        <span class="gs">R<sup>0</sup><sub>el</sub></span
                        >/<span class="gs">R<sub>el</sub></span>
                    </td>
                    <td><input v-model="reportData.qutebi1" disabled type="text" /></td>
                    <td><input v-model="reportData.qutebi2" disabled type="text" /></td>
                    <td><input v-model="reportData.qutebi3" disabled type="text" /></td>
                    <td><input v-model="reportData.qutebi4" disabled type="text" /></td>
                </tr>
                <tr>
                    <td>
                        <span class="gs">R<sup>0</sup><sub>m</sub></span
                        >/ <span class="gs">R<sup>0</sup><sub>el</sub></span>
                    </td>
                    <td><input v-model="reportData.qiangqubi1" disabled type="text" /></td>
                    <td><input v-model="reportData.qiangqubi2" disabled type="text" /></td>
                    <td><input v-model="reportData.qiangqubi3" disabled type="text" /></td>
                    <td><input v-model="reportData.qiangqubi4" disabled type="text" /></td>
                </tr>
                <tr>
                    <td rowspan="3">断后伸长率</td>
                    <td>原始标距(mm)</td>
                    <td><input v-model="reportData.scl_ysbj1" type="text" /></td>
                    <td><input v-model="reportData.scl_ysbj2" type="text" /></td>
                    <td><input v-model="reportData.scl_ysbj3" type="text" /></td>
                    <td><input v-model="reportData.scl_ysbj4" type="text" /></td>
                </tr>
                <tr>
                    <td>断后标距(mm)</td>
                    <td><input v-model="reportData.dhbj1" type="text" /></td>
                    <td><input v-model="reportData.dhbj2" type="text" /></td>
                    <td><input v-model="reportData.dhbj3" type="text" /></td>
                    <td><input v-model="reportData.dhbj4" type="text" /></td>
                </tr>
                <tr>
                    <td>断后伸长率(%)</td>
                    <td><input v-model="reportData.dhscl1" type="text" /></td>
                    <td><input v-model="reportData.dhscl2" type="text" /></td>
                    <td><input v-model="reportData.dhscl3" type="text" /></td>
                    <td><input v-model="reportData.dhscl4" type="text" /></td>
                </tr>
                <tr>
                    <td rowspan="3">
                        最大力总延伸率<br />
                        <select v-model="reportData.ysl_type" style="height: 22px; width: 80px">
                            <option style="display: none" value="">{{ reportData.ysl_type }}</option>
                            <option value="手工法">手工法</option>
                            <option value="引伸计法">引伸计法</option>
                        </select>
                    </td>
                    <td>原始标距(mm)</td>
                    <td><input v-model="reportData.ysl_ysbj1" type="text" /></td>
                    <td><input v-model="reportData.ysl_ysbj2" type="text" /></td>
                    <td><input v-model="reportData.ysl_ysbj3" type="text" /></td>
                    <td><input v-model="reportData.ysl_ysbj4" type="text" /></td>
                </tr>
                <tr>
                    <td>断后标距(mm)</td>
                    <td><input v-model="reportData.zdlxys1" type="text" /></td>
                    <td><input v-model="reportData.zdlxys2" type="text" /></td>
                    <td><input v-model="reportData.zdlxys3" type="text" /></td>
                    <td><input v-model="reportData.zdlxys4" type="text" /></td>
                </tr>
                <tr>
                    <td>最大力总延伸率(%)</td>
                    <td><input v-model="reportData.zdlzysl1" type="text" /></td>
                    <td><input v-model="reportData.zdlzysl2" type="text" /></td>
                    <td><input v-model="reportData.zdlzysl3" type="text" /></td>
                    <td><input v-model="reportData.zdlzysl4" type="text" /></td>
                </tr>
                <tr>
                    <td colspan="2">断口形式</td>
                    <td>
                        <select v-model="reportData.duankxs1" style="border: none; appearance: none">
                            <option style="display: none" value="">{{ reportData.duankxs1 }}</option>
                            <option value=""></option>
                            <option value="塑断">塑断</option>
                            <option value="脆段">脆段</option>
                            <option value="杯状">杯状</option>
                            <option value="平口状">平口状</option>
                            <option value="延性断裂">延性断裂</option>
                        </select>
                    </td>
                    <td>
                        <select v-model="reportData.duankxs2" style="border: none; appearance: none">
                            <option style="display: none" value="">{{ reportData.duankxs2 }}</option>
                            <option value=""></option>
                            <option value="塑断">塑断</option>
                            <option value="脆段">脆段</option>
                            <option value="杯状">杯状</option>
                            <option value="平口状">平口状</option>
                            <option value="延性断裂">延性断裂</option>
                        </select>
                    </td>
                    <td>
                        <select v-model="reportData.duankxs3" style="border: none; appearance: none">
                            <option style="display: none" value="">{{ reportData.duankxs3 }}</option>
                            <option value=""></option>
                            <option value="塑断">塑断</option>
                            <option value="脆段">脆段</option>
                            <option value="杯状">杯状</option>
                            <option value="平口状">平口状</option>
                            <option value="延性断裂">延性断裂</option>
                        </select>
                    </td>
                    <td>
                        <select v-model="reportData.duankxs4" style="border: none; appearance: none">
                            <option style="display: none" value="">{{ reportData.duankxs4 }}</option>
                            <option value=""></option>
                            <option value="塑断">塑断</option>
                            <option value="脆段">脆段</option>
                            <option value="杯状">杯状</option>
                            <option value="平口状">平口状</option>
                            <option value="延性断裂">延性断裂</option>
                        </select>
                    </td>
                </tr>
                <tr>
                    <td rowspan="3">弯曲性能</td>
                    <td>弯心直径(mm)</td>
                    <td><input v-model="reportData.wq_wqzj1" type="text" /></td>
                    <td><input v-model="reportData.wq_wqzj2" type="text" /></td>
                    <td><input v-model="reportData.wq_wqzj3" type="text" /></td>
                    <td><input v-model="reportData.wq_wqzj4" type="text" /></td>
                </tr>
                <tr>
                    <td>弯曲角度(°)</td>
                    <td><input v-model="reportData.wq_wqjd1" type="text" /></td>
                    <td><input v-model="reportData.wq_wqjd2" type="text" /></td>
                    <td><input v-model="reportData.wq_wqjd3" type="text" /></td>
                    <td><input v-model="reportData.wq_wqjd4" type="text" /></td>
                </tr>
                <tr>
                    <td>试验结果</td>
                    <td><input v-model="reportData.wq_syjg1" type="text" /></td>
                    <td><input v-model="reportData.wq_syjg2" type="text" /></td>
                    <td><input v-model="reportData.wq_syjg3" type="text" /></td>
                    <td><input v-model="reportData.wq_syjg4" type="text" /></td>
                </tr>
                <tr>
                    <td rowspan="5">反向弯曲试验</td>
                    <td>弯心直径(mm)</td>
                    <td><input v-model="reportData.fw_wqzj1" type="text" /></td>
                    <td><input v-model="reportData.fw_wqzj2" type="text" /></td>
                    <td><input v-model="reportData.fw_wqzj3" type="text" /></td>
                    <td><input v-model="reportData.fw_wqzj4" type="text" /></td>
                </tr>
                <tr>
                    <td>弯曲角度(°)</td>
                    <td><input v-model="reportData.fw_wqjd1" type="text" /></td>
                    <td><input v-model="reportData.fw_wqjd2" type="text" /></td>
                    <td><input v-model="reportData.fw_wqjd3" type="text" /></td>
                    <td><input v-model="reportData.fw_wqjd4" type="text" /></td>
                </tr>
                <tr>
                    <td>热处理时间</td>
                    <td><input v-model="reportData.fw_rclsj1" type="text" /></td>
                    <td><input v-model="reportData.fw_rclsj2" type="text" /></td>
                    <td><input v-model="reportData.fw_rclsj3" type="text" /></td>
                    <td><input v-model="reportData.fw_rclsj4" type="text" /></td>
                </tr>
                <tr>
                    <td>热处理温度（℃）</td>
                    <td><input v-model="reportData.fw_rclwd1" type="text" /></td>
                    <td><input v-model="reportData.fw_rclwd2" type="text" /></td>
                    <td><input v-model="reportData.fw_rclwd3" type="text" /></td>
                    <td><input v-model="reportData.fw_rclwd4" type="text" /></td>
                </tr>
                <tr>
                    <td>试验结果</td>
                    <td><input v-model="reportData.fw_syjg1" type="text" /></td>
                    <td><input v-model="reportData.fw_syjg2" type="text" /></td>
                    <td><input v-model="reportData.fw_syjg3" type="text" /></td>
                    <td><input v-model="reportData.fw_syjg4" type="text" /></td>
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
    name: 'JJ1001a',
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
                height: 24px;
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
        height: 90px;
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