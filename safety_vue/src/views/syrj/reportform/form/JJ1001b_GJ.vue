<template>
    <div id="paperA4">
        <div class="tableHeader">
            <j-j-head-report :formData.sync="reportData"></j-j-head-report>
        </div>
        <div class="tableContent">
            <table class="la-table">
                <tr>
                    <td colspan="2">焊接种类</td>
                    <td colspan="3"><input v-model="reportData.hanjzl1" type="text" /></td>
                    <td colspan="3">钢筋牌号</td>
                    <td colspan="3">
                        <select v-model="reportData.sampleGangJinZhongLei">
                            <option style="display: none" value="">{{ reportData.sampleGangJinZhongLei }}</option>
                            <option value=""></option>
                            <option value="HPB235">HPB235</option>
                            <option value="HPB235E">HPB235E</option>
                            <option value="HPB300">HPB300</option>
                            <option value="HRB335">HRB335</option>
                            <option value="HRB335E">HRB335E</option>
                            <option value="HRB400">HRB400</option>
                            <option value="HRB400E">HRB400E</option>
                            <option value="HRB500">HRB500</option>
                            <option value="HRB500E">HRB500E</option>
                            <option value="HRB600">HRB600</option>
                            <option value="HRBF335">HRBF335</option>
                            <option value="HRBF400">HRBF400</option>
                            <option value="HRBF400E">HRBF400E</option>
                            <option value="HRBF500">HRBF500</option>
                            <option value="HRBF500E">HRBF500E</option>
                        </select>
                    </td>
                </tr>
                <tr>
                    <td colspan="2">代表数量</td>
                    <td colspan="3"><input v-model="reportData.sampleDaiBiaoShuLiang" type="text" /></td>
                    <td colspan="3">生产厂家</td>
                    <td colspan="3"><a-textarea v-model="reportData.sampleShengChanChangJia" auto-size /></td>
                </tr>
                <tr>
                    <td colspan="2">焊工姓名及上岗证编号</td>
                    <td colspan="9">
                        <select v-model="reportData.hgxmjsgz" style="border: none; appearance: none">
                            <option style="display: none" value="">{{ reportData.hgxmjsgz }}</option>
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
                    <td colspan="2">强度修约</td>
                    <td colspan="9">
                        <select v-model="reportData.xiuyuegz">
                            <option style="display: none" value="">
                                {{ reportData.xiuyuegz ? (reportData.xiuyuegz == 0 ? '1MPa' : '1 5 10MPa') : '' }}
                            </option>
                            <option value="0">1MPa</option>
                            <option value="1">1 5 10MPa</option>
                        </select>
                    </td>
                </tr>
                <tr>
                    <td rowspan="8" width="5%">拉伸</td>
                    <td width="20%">试件编号</td>
                    <td><a-textarea v-model="reportData.shijbianh11" auto-size /></td>
                    <td><a-textarea v-model="reportData.shijbianh12" auto-size /></td>
                    <td><a-textarea v-model="reportData.shijbianh13" auto-size /></td>
                    <td><a-textarea v-model="reportData.shijbianh21" auto-size /></td>
                    <td><a-textarea v-model="reportData.shijbianh22" auto-size /></td>
                    <td><a-textarea v-model="reportData.shijbianh23" auto-size /></td>
                    <td><a-textarea v-model="reportData.shijbianh31" auto-size /></td>
                    <td><a-textarea v-model="reportData.shijbianh32" auto-size /></td>
                    <td><a-textarea v-model="reportData.shijbianh33" auto-size /></td>
                </tr>
                <tr>
                    <td>公称直径(mm)</td>
                    <td colspan="3"><input v-model="reportData.zhij11" type="text" /></td>
                    <td colspan="3"><input v-model="reportData.zhij21" type="text" /></td>
                    <td colspan="3"><input v-model="reportData.zhij31" type="text" /></td>
                </tr>
                <tr>
                    <td>横截面积(mm²)</td>
                    <td colspan="3"><input v-model="reportData.gangjjiemj11" type="text" /></td>
                    <td colspan="3"><input v-model="reportData.gangjjiemj21" type="text" /></td>
                    <td colspan="3"><input v-model="reportData.gangjjiemj31" type="text" /></td>
                </tr>
                <tr>
                    <td>焊缝长度(mm)</td>
                    <td><input v-model="reportData.hanfcdu11" type="text" /></td>
                    <td><input v-model="reportData.hanfcdu12" type="text" /></td>
                    <td><input v-model="reportData.hanfcdu13" type="text" /></td>
                    <td><input v-model="reportData.hanfcdu21" type="text" /></td>
                    <td><input v-model="reportData.hanfcdu22" type="text" /></td>
                    <td><input v-model="reportData.hanfcdu23" type="text" /></td>
                    <td><input v-model="reportData.hanfcdu31" type="text" /></td>
                    <td><input v-model="reportData.hanfcdu32" type="text" /></td>
                    <td><input v-model="reportData.hanfcdu33" type="text" /></td>
                </tr>
                <tr>
                    <td>极限荷载(kN)</td>
                    <td><input v-model="reportData.jixianhez11" type="text" /></td>
                    <td><input v-model="reportData.jixianhez12" type="text" /></td>
                    <td><input v-model="reportData.jixianhez13" type="text" /></td>
                    <td><input v-model="reportData.jixianhez21" type="text" /></td>
                    <td><input v-model="reportData.jixianhez22" type="text" /></td>
                    <td><input v-model="reportData.jixianhez23" type="text" /></td>
                    <td><input v-model="reportData.jixianhez31" type="text" /></td>
                    <td><input v-model="reportData.jixianhez32" type="text" /></td>
                    <td><input v-model="reportData.jixianhez33" type="text" /></td>
                </tr>
                <tr>
                    <td>抗拉强度(MPa)</td>
                    <td><input v-model="reportData.jixianqdu11" type="text" /></td>
                    <td><input v-model="reportData.jixianqdu12" type="text" /></td>
                    <td><input v-model="reportData.jixianqdu13" type="text" /></td>
                    <td><input v-model="reportData.jixianqdu21" type="text" /></td>
                    <td><input v-model="reportData.jixianqdu22" type="text" /></td>
                    <td><input v-model="reportData.jixianqdu23" type="text" /></td>
                    <td><input v-model="reportData.jixianqdu31" type="text" /></td>
                    <td><input v-model="reportData.jixianqdu32" type="text" /></td>
                    <td><input v-model="reportData.jixianqdu33" type="text" /></td>
                </tr>
                <tr>
                    <td>断裂位置即断口距焊口距离(mm)</td>
                    <td><input v-model="reportData.duanklihkjuli11" type="text" /></td>
                    <td><input v-model="reportData.duanklihkjuli12" type="text" /></td>
                    <td><input v-model="reportData.duanklihkjuli13" type="text" /></td>
                    <td><input v-model="reportData.duanklihkjuli21" type="text" /></td>
                    <td><input v-model="reportData.duanklihkjuli22" type="text" /></td>
                    <td><input v-model="reportData.duanklihkjuli23" type="text" /></td>
                    <td><input v-model="reportData.duanklihkjuli31" type="text" /></td>
                    <td><input v-model="reportData.duanklihkjuli32" type="text" /></td>
                    <td><input v-model="reportData.duanklihkjuli33" type="text" /></td>
                </tr>
                <tr>
                    <td>断裂形式</td>
                    <td>
                        <select v-model="reportData.duankxshi1" style="border: none; appearance: none">
                            <option style="display: none" value="">{{ reportData.duankxshi1 }}</option>
                            <option value=""></option>
                            <option value="脆断">脆断</option>
                            <option value="塑断">塑断</option>
                            <option value="杯口状断裂">杯口状断裂</option>
                            <option value="延性断裂">延性断裂</option>
                            <option value="钢筋母材拉断">钢筋母材拉断</option>
                        </select>
                    </td>
                    <td>
                        <select v-model="reportData.duankxshi2" style="border: none; appearance: none">
                            <option style="display: none" value="">{{ reportData.duankxshi2 }}</option>
                            <option value=""></option>
                            <option value="脆断">脆断</option>
                            <option value="塑断">塑断</option>
                            <option value="杯口状断裂">杯口状断裂</option>
                            <option value="延性断裂">延性断裂</option>
                            <option value="钢筋母材拉断">钢筋母材拉断</option>
                        </select>
                    </td>
                    <td>
                        <select v-model="reportData.duankxshi3" style="border: none; appearance: none">
                            <option style="display: none" value="">{{ reportData.duankxshi3 }}</option>
                            <option value=""></option>
                            <option value="脆断">脆断</option>
                            <option value="塑断">塑断</option>
                            <option value="杯口状断裂">杯口状断裂</option>
                            <option value="延性断裂">延性断裂</option>
                            <option value="钢筋母材拉断">钢筋母材拉断</option>
                        </select>
                    </td>
                    <td>
                        <select v-model="reportData.duankxshi4" style="border: none; appearance: none">
                            <option style="display: none" value="">{{ reportData.duankxshi4 }}</option>
                            <option value=""></option>
                            <option value="脆断">脆断</option>
                            <option value="塑断">塑断</option>
                            <option value="杯口状断裂">杯口状断裂</option>
                            <option value="延性断裂">延性断裂</option>
                            <option value="钢筋母材拉断">钢筋母材拉断</option>
                        </select>
                    </td>
                    <td>
                        <select v-model="reportData.duankxshi5" style="border: none; appearance: none">
                            <option style="display: none" value="">{{ reportData.duankxshi5 }}</option>
                            <option value=""></option>
                            <option value="脆断">脆断</option>
                            <option value="塑断">塑断</option>
                            <option value="杯口状断裂">杯口状断裂</option>
                            <option value="延性断裂">延性断裂</option>
                            <option value="钢筋母材拉断">钢筋母材拉断</option>
                        </select>
                    </td>
                    <td>
                        <select v-model="reportData.duankxshi6" style="border: none; appearance: none">
                            <option style="display: none" value="">{{ reportData.duankxshi6 }}</option>
                            <option value=""></option>
                            <option value="脆断">脆断</option>
                            <option value="塑断">塑断</option>
                            <option value="杯口状断裂">杯口状断裂</option>
                            <option value="延性断裂">延性断裂</option>
                            <option value="钢筋母材拉断">钢筋母材拉断</option>
                        </select>
                    </td>
                    <td>
                        <select v-model="reportData.duankxshi7" style="border: none; appearance: none">
                            <option style="display: none" value="">{{ reportData.duankxshi7 }}</option>
                            <option value=""></option>
                            <option value="脆断">脆断</option>
                            <option value="塑断">塑断</option>
                            <option value="杯口状断裂">杯口状断裂</option>
                            <option value="延性断裂">延性断裂</option>
                            <option value="钢筋母材拉断">钢筋母材拉断</option>
                        </select>
                    </td>
                    <td>
                        <select v-model="reportData.duankxshi8" style="border: none; appearance: none">
                            <option style="display: none" value="">{{ reportData.duankxshi8 }}</option>
                            <option value=""></option>
                            <option value="脆断">脆断</option>
                            <option value="塑断">塑断</option>
                            <option value="杯口状断裂">杯口状断裂</option>
                            <option value="延性断裂">延性断裂</option>
                            <option value="钢筋母材拉断">钢筋母材拉断</option>
                        </select>
                    </td>
                    <td>
                        <select v-model="reportData.duankxshi9" style="border: none; appearance: none">
                            <option style="display: none" value="">{{ reportData.duankxshi9 }}</option>
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
                    <td rowspan="4">冷弯</td>
                    <td>试件编号</td>
                    <td><a-textarea v-model="reportData.bianhao11" auto-size /></td>
                    <td><a-textarea v-model="reportData.bianhao12" auto-size /></td>
                    <td><a-textarea v-model="reportData.bianhao13" auto-size /></td>
                    <td><a-textarea v-model="reportData.bianhao21" auto-size /></td>
                    <td><a-textarea v-model="reportData.bianhao22" auto-size /></td>
                    <td><a-textarea v-model="reportData.bianhao23" auto-size /></td>
                    <td><a-textarea v-model="reportData.bianhao31" auto-size /></td>
                    <td><a-textarea v-model="reportData.bianhao32" auto-size /></td>
                    <td><a-textarea v-model="reportData.bianhao33" auto-size /></td>
                </tr>
                <tr>
                    <td>弯曲直径(mm)</td>
                    <td colspan="3"><input v-model="reportData.yatouzj11" type="text" /></td>
                    <td colspan="3"><input v-model="reportData.yatouzj12" type="text" /></td>
                    <td colspan="3"><input v-model="reportData.yatouzj13" type="text" /></td>
                </tr>
                <tr>
                    <td>弯曲角度(° )</td>
                    <td colspan="3"><input v-model="reportData.wanqujd11" type="text" /></td>
                    <td colspan="3"><input v-model="reportData.wanqujd12" type="text" /></td>
                    <td colspan="3"><input v-model="reportData.wanqujd13" type="text" /></td>
                </tr>
                <tr>
                    <td>弯曲结果</td>
                    <td><a-textarea v-model="reportData.shiyanjg11" auto-size /></td>
                    <td><a-textarea v-model="reportData.shiyanjg12" auto-size /></td>
                    <td><a-textarea v-model="reportData.shiyanjg13" auto-size /></td>
                    <td><a-textarea v-model="reportData.shiyanjg21" auto-size /></td>
                    <td><a-textarea v-model="reportData.shiyanjg22" auto-size /></td>
                    <td><a-textarea v-model="reportData.shiyanjg23" auto-size /></td>
                    <td><a-textarea v-model="reportData.shiyanjg31" auto-size /></td>
                    <td><a-textarea v-model="reportData.shiyanjg32" auto-size /></td>
                    <td><a-textarea v-model="reportData.shiyanjg33" auto-size /></td>
                </tr>
                <tr>
                    <td colspan="2">焊接质量评述</td>
                    <td colspan="9"><input v-model="reportData.hanjzlpshu1" type="text" /></td>
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
    name: 'JJ1001b_GJ',
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
        height: 190px;
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