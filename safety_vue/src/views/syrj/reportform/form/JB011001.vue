<template>
    <div id="paperA4">
        <div class="tableHeader">
            <j-b-head-report :formData.sync="reportData"></j-b-head-report>
        </div>
        <div class="tableContent">
            <table class="la-table">
                <tr>
                    <td>取样地点</td>
                    <td><a-textarea v-model="reportData.sampleQuYangDiDian" auto-size /></td>
                    <td>取样日期</td>
                    <td><input v-model="reportData.sampleDate" type="text" /></td>
                </tr>
                <tr>
                    <td>生产厂家</td>
                    <td><a-textarea v-model="reportData.sampleShengChanChangJia" auto-size /></td>
                    <td>生产批号</td>
                    <td><a-textarea v-model="reportData.sampleShengChanPiHao" auto-size /></td>
                </tr>
                <tr>
                    <td>钢筋牌号</td>
                    <td><input v-model="reportData.sampleGangJinZhongLei" type="text" /></td>
                    <td>公称直径(mm)</td>
                    <td><input v-model="reportData.gjzj" type="text" /></td>
                </tr>
                <tr>
                    <td>代表数量</td>
                    <td colspan="3">
                        <input v-model="reportData.sampleDaiBiaoShuLiang" type="text" />
                    </td>
                </tr>
            </table>
            <table class="la-table next">
                <tr>
                    <td style="width: 100px">编号</td>
                    <td style="width: 110px">检测项目</td>
                    <td>技术指标</td>
                    <td colspan="4">检测结果</td>
                    <td>结果判定</td>
                </tr>
                <tr>
                    <td rowspan="7"><a-textarea v-model="reportData.bh1" auto-size /></td>
                    <td>屈服强度(MPa)</td>
                    <td><input v-model="reportData.jszb_qfqd" type="text" /></td>
                    <td><input v-model="reportData.jcjg_qfqd1" type="text" /></td>
                    <td><input v-model="reportData.jcjg_qfqd2" type="text" /></td>
                    <td><input v-model="reportData.jcjg_qfqd3" type="text" /></td>
                    <td><input v-model="reportData.jcjg_qfqd4" type="text" /></td>
                    <td><input v-model="reportData.jgpd_qfqd" type="text" /></td>
                </tr>
                <tr>
                    <td>抗拉强度(MPa)</td>
                    <td><input v-model="reportData.jszb_klqd" type="text" /></td>
                    <td><input v-model="reportData.jcjg_klqd1" type="text" /></td>
                    <td><input v-model="reportData.jcjg_klqd2" type="text" /></td>
                    <td><input v-model="reportData.jcjg_klqd3" type="text" /></td>
                    <td><input v-model="reportData.jcjg_klqd4" type="text" /></td>
                    <td><input v-model="reportData.jgpd_klqd" type="text" /></td>
                </tr>
                <tr>
                    <td>实测屈服强度/屈服特征值</td>
                    <td><input v-model="reportData.jszb_qutebi" type="text" /></td>
                    <td><input v-model="reportData.jcjg_qutebi1" type="text" /></td>
                    <td><input v-model="reportData.jcjg_qutebi2" type="text" /></td>
                    <td><input v-model="reportData.jcjg_qutebi3" type="text" /></td>
                    <td><input v-model="reportData.jcjg_qutebi4" type="text" /></td>
                    <td><input v-model="reportData.jgpd_qutebi" type="text" /></td>
                </tr>
                <tr>
                    <td>抗拉强度/屈服强度</td>
                    <td><input v-model="reportData.jszb_qiangqubi" type="text" /></td>
                    <td><input v-model="reportData.jcjg_qiangqubi1" type="text" /></td>
                    <td><input v-model="reportData.jcjg_qiangqubi2" type="text" /></td>
                    <td><input v-model="reportData.jcjg_qiangqubi3" type="text" /></td>
                    <td><input v-model="reportData.jcjg_qiangqubi4" type="text" /></td>
                    <td><input v-model="reportData.jgpd_qiangqubi" type="text" /></td>
                </tr>
                <tr>
                    <td>断后伸长率(%)</td>
                    <td><input v-model="reportData.jszb_dhscl" type="text" /></td>
                    <td><input v-model="reportData.jcjg_dhscl1" type="text" /></td>
                    <td><input v-model="reportData.jcjg_dhscl2" type="text" /></td>
                    <td><input v-model="reportData.jcjg_dhscl3" type="text" /></td>
                    <td><input v-model="reportData.jcjg_dhscl4" type="text" /></td>
                    <td><input v-model="reportData.jgpd_dhscl" type="text" /></td>
                </tr>
                <tr>
                    <td>断口形式</td>
                    <td><input v-model="reportData.jszb_dkxs" type="text" /></td>
                    <td><input v-model="reportData.duankxs1" type="text" /></td>
                    <td><input v-model="reportData.duankxs2" type="text" /></td>
                    <td><input v-model="reportData.duankxs3" type="text" /></td>
                    <td><input v-model="reportData.duankxs4" type="text" /></td>
                    <td><input v-model="reportData.jgpd_dhxs" type="text" /></td>
                </tr>
                <tr>
                    <td>最大力总延伸率(%)</td>
                    <td><input v-model="reportData.jszb_zdlzysl" type="text" /></td>
                    <td><input v-model="reportData.jcjg_zdlzysl1" type="text" /></td>
                    <td><input v-model="reportData.jcjg_zdlzysl2" type="text" /></td>
                    <td><input v-model="reportData.jcjg_zdlzysl3" type="text" /></td>
                    <td><input v-model="reportData.jcjg_zdlzysl4" type="text" /></td>
                    <td><input v-model="reportData.jgpd_zdlzysl" type="text" /></td>
                </tr>
                <tr>
                    <td><a-textarea v-model="reportData.bh2" auto-size /></td>
                    <td>弯曲性能</td>
                    <td><input v-model="reportData.jszb_wqxn" type="text" /></td>
                    <td><input v-model="reportData.jcjg_wqxn1" type="text" /></td>
                    <td><input v-model="reportData.jcjg_wqxn2" type="text" /></td>
                    <td><input v-model="reportData.jcjg_wqxn3" type="text" /></td>
                    <td><input v-model="reportData.jcjg_wqxn4" type="text" /></td>
                    <td><input v-model="reportData.jgpd_wqxn" type="text" /></td>
                </tr>
                <tr>
                    <td><a-textarea v-model="reportData.bh3" auto-size /></td>
                    <td>反向弯曲</td>
                    <td>
                        <select v-model="reportData.jszb_fxwq">
                            <option style="display: none" value="">{{ reportData.jszb_fxwq }}</option>
                            <option value="/">/</option>
                            <option value="无裂纹">无裂纹</option>
                        </select>
                    </td>
                    <td><input v-model="reportData.jcjg_fxwq1" type="text" /></td>
                    <td><input v-model="reportData.jcjg_fxwq2" type="text" /></td>
                    <td><input v-model="reportData.jcjg_fxwq3" type="text" /></td>
                    <td><input v-model="reportData.jcjg_fxwq4" type="text" /></td>
                    <td><input v-model="reportData.jgpd_fxwq" type="text" /></td>
                </tr>
                <tr>
                    <td><a-textarea v-model="reportData.bh4" auto-size /></td>
                    <td>重量偏差(%)</td>
                    <td><input v-model="reportData.jszb_zlpc" type="text" /></td>
                    <td colspan="4"><input v-model="reportData.jcjg_zlpc" type="text" /></td>
                    <td><input v-model="reportData.jgpd_zlpc" type="text" /></td>
                </tr>
            </table>
        </div>
        <div class="tableRemark">
            <j-b-tail-report :formData.sync="reportData"></j-b-tail-report>
        </div>
    </div>
</template>
<script>
import JBHeadReport from './head/JBHeadReport'
import JBTailReport from './tail/JBTailReport'

export default {
    name: 'JB011001',
    props: ['formData'],
    components: {
        JBHeadReport,
        JBTailReport,
    },
    data() {
        return {
            bg: {},
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
        height: 315px;
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