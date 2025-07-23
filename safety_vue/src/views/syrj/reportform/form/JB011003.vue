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
                    <td>编号</td>
                    <td>检测项目</td>
                    <td style="width: 100px">技术指标</td>
                    <td>检测结果</td>
                    <td>结果判定</td>
                </tr>
                <tr>
                    <td><input v-model="reportData.bianhao1" type="text" /></td>
                    <td rowspan="3">抗拉强度(MPa)</td>
                    <td rowspan="6"><a-textarea v-model="reportData.jszb_klqd" auto-size /></td>
                    <td><input v-model="reportData.jcjg_klqd1" type="text" /></td>
                    <td rowspan="6"><input v-model="reportData.jgpd_klqd" type="text" /></td>
                </tr>
                <tr>
                    <td><input v-model="reportData.bianhao2" type="text" /></td>
                    <td><input v-model="reportData.jcjg_klqd2" type="text" /></td>
                </tr>
                <tr>
                    <td><input v-model="reportData.bianhao3" type="text" /></td>
                    <td><input v-model="reportData.jcjg_klqd3" type="text" /></td>
                </tr>
                <tr>
                    <td><input v-model="reportData.bianhao4" type="text" /></td>
                    <td rowspan="3">拉伸断裂性质</td>
                    <td><input v-model="reportData.jcjg_lsdlxz1" type="text" /></td>
                </tr>
                <tr>
                    <td><input v-model="reportData.bianhao5" type="text" /></td>
                    <td><input v-model="reportData.jcjg_lsdlxz2" type="text" /></td>
                </tr>
                <tr>
                    <td><input v-model="reportData.bianhao6" type="text" /></td>
                    <td><input v-model="reportData.jcjg_lsdlxz3" type="text" /></td>
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
    name: 'JB011003',
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