<template>
    <div id="paperA4">
        <div class="tableHeader">
            <j-b-head-report :formData.sync="reportData"></j-b-head-report>
        </div>
        <div class="tableContent">
            <table class="la-table">
                <tr>
                    <td style="width: 13%">焊接种类</td>
                    <td><input v-model="reportData.hanjiezl" type="text" /></td>
                    <td style="width: 13%">焊接方法</td>
                    <td><input v-model="reportData.hanjieff" type="text" /></td>
                </tr>
                <tr>
                    <td>生产厂家</td>
                    <td><a-textarea v-model="reportData.sampleShengChanChangJia" auto-size /></td>
                    <td>钢筋批号</td>
                    <td><a-textarea v-model="reportData.sampleShengChanPiHao" auto-size /></td>
                </tr>
                <tr>
                    <td>规格型号</td>
                    <td><input v-model="reportData.sampleGuiGeXingHao" type="text" /></td>
                    <td>公称直径(mm)</td>
                    <td><input v-model="reportData.gjzj" type="text" /></td>
                </tr>
                <tr>
                    <td>钢筋牌号</td>
                    <td><input v-model="reportData.sampleGangJinZhongLei" type="text" /></td>
                    <td>代表数量(个)</td>
                    <td><input v-model="reportData.sampleDaiBiaoShuLiang" type="text" /></td>
                </tr>
                <tr>
                    <td>焊工姓名及上岗证编号</td>
                    <td><input v-model="reportData.hgxmjsgz" type="text" /></td>
                    <td>取样地点</td>
                    <td><input v-model="reportData.sampleQuYangDiDian" type="text" /></td>
                </tr>
            </table>
            <table class="la-table next">
                <tr>
                    <td>编号</td>
                    <td>检测项目</td>
                    <td>技术指标</td>
                    <td>检测结果</td>
                    <td>结果判定</td>
                </tr>
                <tr>
                    <td rowspan="12"><input v-model="reportData.xuhao" type="text" /></td>
                    <td rowspan="3">抗拉强度(MPa)</td>
                    <td rowspan="3"><input v-model="reportData.jszb_klqd" type="text" /></td>
                    <td><input v-model="reportData.jcjg_klqd1" type="text" /></td>
                    <td rowspan="12"><input v-model="reportData.jgpd_klqd" type="text" /></td>
                </tr>
                <tr>
                    <td><input v-model="reportData.jcjg_klqd2" type="text" /></td>
                </tr>
                <tr>
                    <td><input v-model="reportData.jcjg_klqd3" type="text" /></td>
                </tr>
                <tr>
                    <td rowspan="3">拉伸断裂性质</td>
                    <td rowspan="3"><input v-model="reportData.jszb_lsdlxz" type="text" /></td>
                    <td><input v-model="reportData.jcjg_lsdlxz1" type="text" /></td>
                </tr>
                <tr>
                    <td><input v-model="reportData.jcjg_lsdlxz2" type="text" /></td>
                </tr>
                <tr>
                    <td><input v-model="reportData.jcjg_lsdlxz3" type="text" /></td>
                </tr>
                <tr>
                    <td rowspan="3">断口距焊口距离(mm)</td>
                    <td rowspan="3"><input v-model="reportData.jszb_dlwz" type="text" /></td>
                    <td><input v-model="reportData.jcjg_dlwz1" type="text" /></td>
                </tr>
                <tr>
                    <td><input v-model="reportData.jcjg_dlwz2" type="text" /></td>
                </tr>
                <tr>
                    <td><input v-model="reportData.jcjg_dlwz3" type="text" /></td>
                </tr>
                <tr>
                    <td rowspan="3">弯曲性能</td>
                    <td rowspan="3"><input v-model="reportData.jszb_wqxn" type="text" /></td>
                    <td><input v-model="reportData.jcjg_wqxn1" type="text" /></td>
                </tr>
                <tr>
                    <td><input v-model="reportData.jcjg_wqxn2" type="text" /></td>
                </tr>
                <tr>
                    <td><input v-model="reportData.jcjg_wqxn3" type="text" /></td>
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
    name: 'JB011002',
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