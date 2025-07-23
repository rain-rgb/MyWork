<template>
    <div id="paperA4">
        <div class="tableHeader">
            <div class="projectName">物联网指令单</div>
            <div class="tiCustomTableNum">浙公路（JL）</div>
            <div class="number">
                <div>编号：</div>
                <span>{{ instrData.batchNo }}</span>
            </div>
        </div>
        <div class="tableContent">
            <table class="la-table">
                <tr>
                    <td style="width: 10%">标题</td>
                    <td style="text-align: left" colspan="3">{{ instrData.shebeiNo }}材料使用预警</td>
                </tr>
                <tr>
                    <td>主送</td>
                    <td style="text-align: left" colspan="3">{{ instrData.constructionUnit }}</td>
                </tr>
                <tr>
                    <td>抄送</td>
                    <td style="text-align: left; width: 35%">{{ instrData.supervisionUnit }}</td>
                    <td style="width: 20%">签收人</td>
                    <td style="text-align: left; width: 35%">{{ instrData.receiver }}</td>
                </tr>
                <tr>
                    <td style="height: 400px" colspan="4">
                        <span class="beizhu">{{ instrData.constructionUnit }}：</span>
                        <span class="content">
                            {{ instrData.saveDatetime }}，系统检测到{{ instrData.shebeiNo }}于{{
                                instrData.productDatetime
                            }}的混凝土材料用量中{{ instrData.overReason }}，相关负责人未在1小时内进行闭合。
                        </span>
                        <span class="tail"
                            >请负责人及时进行处置，并根据招标文件要求罚款
                            <select v-model="instrData.penaltyAmount">
                                <option style="display: none" value="">{{ instrData.penaltyAmount }}</option>
                                <option value="2000">2000</option>
                                <option value="3000">3000</option>
                                <option value="4000">4000</option>
                                <option value="5000">5000</option>
                            </select>
                            元。
                        </span>
                    </td>
                </tr>
            </table>
            <table class="la-table next">
                <tr>
                    <td class="end">专业监理工程师签字、日期</td>
                    <td class="msg">
                        <div v-if="supervision.length > 0">
                            <div>{{ supervision[0].realname }}</div>
                            <div>{{ supervision[0].signatureTime }}</div>
                        </div>
                    </td>
                    <td class="end">总监理工程师（签字、公章）日期</td>
                    <td class="msg">
                        <div v-if="supervision.length > 1">
                            <div>{{ supervision[1].realname }}</div>
                            <div>{{ supervision[1].signatureTime }}</div>
                        </div>
                    </td>
                </tr>
                <tr>
                    <td class="end">交送日期</td>
                    <td class="msg"></td>
                    <td class="end">施工单位收到签字、日期</td>
                    <td class="msg">
                        <div v-if="supervision.length > 2">
                            <div>{{ supervision[2].realname }}</div>
                            <div>{{ supervision[2].signatureTime }}</div>
                        </div>
                    </td>
                </tr>
            </table>
        </div>
    </div>
</template>
<script>
export default {
    name: 'instruct',
    props: ['instructData'],
    data() {
        return {}
    },
    mounted() {},
    computed: {
        instrData: {
            get: function () {
                return this.instructData || {}
            },
            set: function (newVal) {
                this.$emit('update:instructData', newVal)
            },
        },
        supervision(){
            return this.instructData.bhzSupervisionOrderSubList || []
        }
    },
    methods: {},
}
</script>
<style lang="less" scoped>
#paperA4 {
    height: 1042px;
    width: 716px;
    background: #fff9ec;
    margin: 0 auto;
    padding: 18px 36px;
    font-family: SimSun;
    font-size: 13px;
    color: #000;
    .tableHeader {
        position: relative;
        .projectName {
            height: 60px;
            line-height: 60px;
            padding-top: 15px;
            text-align: center;
            font-size: 22px;
            font-weight: bold;
        }
        .tiCustomTableNum {
            position: absolute;
            right: 17px;
            top: 0;
        }
        .number {
            height: 55px;
            display: flex;
            justify-content: flex-end;
            align-items: center;
            margin-right: 13px;
        }
    }
    .tableContent {
        .la-table {
            width: 100%;
            color: #000;
            border: 1px solid #000;
            td {
                padding: 5px;
                height: 45px;
                font-size: 16px;
                line-height: 18px;
                text-align: center;
                border: 1px solid #000;
                position: relative;
                select {
                    width: 46px;
                    height: auto;
                    text-align: center;
                    outline: none;
                    border: none;
                    appearance: none;
                }
            }
            .beizhu {
                position: absolute;
                left: 5px;
                top: 20px;
                text-align: left;
                line-height: normal;
            }
            .content {
                position: absolute;
                height: auto;
                line-height: 23px;
                left: 5px;
                top: 55px;
                text-align: left;
                text-indent: 2em;
            }
            .tail {
                position: absolute;
                left: 5px;
                top: 120px;
                text-align: left;
                text-indent: 2em;
            }
            .end {
                height: 80px;
                width: 20%;
            }
            .msg {
                height: 80px;
                width: 30%;
                line-height: 30px;
            }
        }
        .next {
            margin-top: -1px;
        }
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