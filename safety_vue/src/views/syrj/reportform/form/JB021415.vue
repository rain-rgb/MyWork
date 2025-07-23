<template>
    <div id="paperA4">
        <div class="tableHeader">
            <j-b-head-report :formData.sync="reportData"></j-b-head-report>
        </div>
        <div class="tableContent">
            <table class="la-table">
                <tr>
                    <td style="width: 13%">取样地点</td>
                    <td colspan="3"><input v-model="reportData.sampleQuYangDiDian" type="text" /></td>
                </tr>
                <tr>
                    <td>生产厂家</td>
                    <td><a-textarea v-model="reportData.sampleShengChanChangJia" auto-size /></td>
                    <td style="width: 13%">进场日期</td>
                    <td><input v-model="reportData.jcrq" type="text" /></td>
                </tr>
                <tr>
                    <td>批号</td>
                    <td><a-textarea v-model="reportData.sampleShengChanPiHao" auto-size /></td>
                    <td>公称直径(mm)</td>
                    <td><input v-model="reportData.gjzj" type="text" /></td>
                </tr>
                <tr>
                    <td>牌号</td>
                    <td><input v-model="reportData.sampleGangJinZhongLei" type="text" /></td>
                    <td>代表数量(个)</td>
                    <td><input v-model="reportData.sampleDaiBiaoShuLiang" type="text" /></td>
                </tr>
                <tr>
                    <td>设计等级</td>
                    <td><input v-model="reportData.jtdj" type="text" /></td>
                    <td>连接形式</td>
                    <td><input v-model="reportData.jietouxs" type="text" /></td>
                </tr>
            </table>
            <table class="la-table next">
                <tr>
                    <td colspan="12">检测结果</td>
                </tr>
                <tr>
                    <td width="76px">序号</td>
                    <td width="16%">检测项目</td>
                    <td width="10%">技术指标</td>
                    <td colspan="9">检测结果</td>
                </tr>
                <tr>
                    <td rowspan="2"><a-textarea v-model="reportData.xh" auto-size /></td>
                    <td>
                        抗拉强度(MPa)
                        <select v-model="reportData.xs" style="height: 19px">
                            <option style="display: none" value="">{{ reportData.xs }}</option>
                            <option value=""></option>
                            <option value="钢筋拉断">钢筋拉断</option>
                            <option value="连接件破坏">连接件破坏</option>
                        </select>
                    </td>
                    <td><input v-model="reportData.kanglajishuzhibiao" type="text" /></td>
                    <td><input v-model="reportData.jixqdu1" type="text" /></td>
                    <td><input v-model="reportData.jixqdu2" type="text" /></td>
                    <td><input v-model="reportData.jixqdu3" type="text" /></td>
                    <td><input v-model="reportData.jixqdu4" type="text" /></td>
                    <td><input v-model="reportData.jixqdu5" type="text" /></td>
                    <td><input v-model="reportData.jixqdu6" type="text" /></td>
                    <td><input v-model="reportData.jixqdu7" type="text" /></td>
                    <td><input v-model="reportData.jixqdu8" type="text" /></td>
                    <td><input v-model="reportData.jixqdu9" type="text" /></td>
                </tr>
                <tr>
                    <td>接头试件破坏形式</td>
                    <td><a-textarea v-model="reportData.jietoujishuzhibiao" auto-size /></td>
                    <td><a-textarea v-model="reportData.duankxshi1" auto-size /></td>
                    <td><a-textarea v-model="reportData.duankxshi2" auto-size /></td>
                    <td><a-textarea v-model="reportData.duankxshi3" auto-size /></td>
                    <td><a-textarea v-model="reportData.duankxshi4" auto-size /></td>
                    <td><a-textarea v-model="reportData.duankxshi5" auto-size /></td>
                    <td><a-textarea v-model="reportData.duankxshi6" auto-size /></td>
                    <td><a-textarea v-model="reportData.duankxshi7" auto-size /></td>
                    <td><a-textarea v-model="reportData.duankxshi8" auto-size /></td>
                    <td><a-textarea v-model="reportData.duankxshi9" auto-size /></td>
                </tr>
                <tr>
                    <td colspan="3">结果判定</td>
                    <td colspan="3"><input v-model="reportData.jieguopanding1" type="text" /></td>
                    <td colspan="3"><input v-model="reportData.jieguopanding2" type="text" /></td>
                    <td colspan="3"><input v-model="reportData.jieguopanding3" type="text" /></td>
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
    name: 'JB021415',
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
        height: 380px;
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