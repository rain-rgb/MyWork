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
                    <td colspan="13">检测结果</td>
                </tr>
                <tr>
                    <td width="3%">序号</td>
                    <td colspan="2">检测项目</td>
                    <td width="9%">技术指标</td>
                    <td colspan="9">检测结果</td>
                </tr>
                <tr>
                    <td rowspan="4">1</td>
                    <td rowspan="4" width="5%">拉伸</td>
                    <td width="15%">焊缝长度(mm)</td>
                    <td><input v-model="reportData.hfcdzb" type="text" /></td>
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
                    <td>抗拉强度(MPa)</td>
                    <td><input v-model="reportData.jszb" type="text" /></td>
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
                    <td>断裂位置(mm)</td>
                    <td><input v-model="reportData.dlwzzb" type="text" /></td>
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
                    <td><a-textarea v-model="reportData.dkxszb" auto-size /></td>
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
                    <td rowspan="3">2</td>
                    <td rowspan="3">冷弯</td>
                    <td>弯曲直径(mm)</td>
                    <td><input v-model="reportData.wqzjzb" type="text" /></td>
                    <td colspan="3"><input v-model="reportData.yatouzj11" type="text" /></td>
                    <td colspan="3"><input v-model="reportData.yatouzj12" type="text" /></td>
                    <td colspan="3"><input v-model="reportData.yatouzj13" type="text" /></td>
                </tr>
                <tr>
                    <td>弯曲角度(° )</td>
                    <td><input v-model="reportData.wqjdzb" type="text" /></td>
                    <td colspan="3"><input v-model="reportData.wanqujd11" type="text" /></td>
                    <td colspan="3"><input v-model="reportData.wanqujd12" type="text" /></td>
                    <td colspan="3"><input v-model="reportData.wanqujd13" type="text" /></td>
                </tr>
                <tr>
                    <td>弯曲结果</td>
                    <td><a-textarea v-model="reportData.wqjgzb" auto-size /></td>
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
                    <td colspan="3">结果判定</td>
                    <td colspan="4"><input v-model="reportData.jieguopanding1" type="text" /></td>
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
    name: 'JB011002_GJ',
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
        height: 225px;
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