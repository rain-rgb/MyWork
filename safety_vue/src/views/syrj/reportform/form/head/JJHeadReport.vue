<template>
    <div class="head">
        <!--项目名-->
        <div class="projectName">{{ reportData.departfullname }}</div>
        <!--表名-->
        <div class="tablename">
            <span class="tiTableName">{{ reportData.tiTableName }}</span>
            <div class="tiCustomTableNum">
                <span v-for="(item, i) in tiCustomTableNum" :key="i">{{ item }}</span>
            </div>
            <span class="page" style="display: none">第1页 共1页</span>
        </div>
        <!--表格内容-->
        <div class="tablecontent">
            <table class="table-noborder">
                <tr>
                    <td style="width: 80px">试验室名称：</td>
                    <td style="height: 28px">{{ reportData.gongZhangMingCheng }}</td>
                    <td style="width: 230px; white-space: nowrap">
                        &emsp;&emsp;&emsp;记录编号：
                        <span class="tableNumber">{{ reportData.tableNumber }}</span>
                    </td>
                </tr>
            </table>
            <table class="la-table" style="width: 100%">
                <tr>
                    <td style="width: 100px">工程名称</td>
                    <td colspan="3"><a-textarea v-model="reportData.gongchengmingcheng" auto-size /></td>
                </tr>
                <tr>
                    <td>工程部位/用途</td>
                    <td><a-textarea v-model="reportData.sampleGcbw" auto-size /></td>
                    <td>任务编号</td>
                    <td><a-textarea v-model="reportData.renwuNo" auto-size /></td>
                </tr>
                <tr>
                    <td>样品信息</td>
                    <td colspan="3"><a-textarea v-model="reportData.ypxx" auto-size /></td>
                </tr>
                <tr>
                    <td>试验检测日期</td>
                    <td style="width: 39%">
                        <a-range-picker @change="onChange">
                            <span style="color: #000; font-size: 12px">
                                {{ reportData.shiyanriqi ? reportData.shiyanriqi : '请选择日期' }}
                            </span>
                        </a-range-picker>
                    </td>
                    <td>试验条件</td>
                    <td><a-textarea v-model="reportData.shiyantiaojian" auto-size /></td>
                </tr>
                <tr>
                    <td>检测依据</td>
                    <td><a-textarea v-model="reportData.shiyanyiju" auto-size /></td>
                    <td>判定依据</td>
                    <td><a-textarea v-model="reportData.pandingyiju" auto-size /></td>
                </tr>
                <tr>
                    <td>主要仪器设备<br />名称及编号</td>
                    <td colspan="3">
                        <a-textarea v-model="reportData.yiqishebei" auto-size />
                    </td>
                </tr>
            </table>
        </div>
    </div>
</template>
<script>
export default {
    name: 'JJHeadReport',
    props: ['formData'],
    components: {},
    data() {
        return {}
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
        tiCustomTableNum() {
            return this.reportData.tiCustomTableNum ? this.reportData.tiCustomTableNum.split('/') : []
        },
    },
    methods: {
        timestampToTime(timestamp) {
            var date = new Date(timestamp)
            var y = date.getFullYear()
            var m = date.getMonth() + 1
            m = m < 10 ? '0' + m : m
            var d = date.getDate()
            d = d < 10 ? '0' + d : d
            let time = y + '年' + m + '月' + d + '日'
            return time
        },
        onChange(time) {
            this.timestampToTime(time[0]) == this.timestampToTime(time[1])
                ? (this.reportData.shiyanriqi = this.timestampToTime(time[0]))
                : (this.reportData.shiyanriqi = this.timestampToTime(time[0]) + ' 至 ' + this.timestampToTime(time[1]))
        },
    },
}
</script>
<style lang="less" scoped>
.head {
    .projectName {
        text-align: center;
        font-size: 22px;
        font-weight: bold;
    }
    .tablename {
        padding: 0 0 10px 0;
        text-align: center;
        position: relative;
        font-size: 19px;
        .tiTableName {
            font-weight: bold;
        }
        .tiCustomTableNum {
            position: absolute;
            font-size: 12px;
            right: 0;
            bottom: -2px;
            font-weight: normal;
            display: flex;
            flex-direction: column;
            span {
                margin-top: -4px;
            }
        }
        .page {
            position: absolute;
            font-size: 12px;
            right: 0;
            top: 0;
        }
    }
    .tablecontent {
        .table-noborder {
            width: 100%;
            margin: 4px 0;
            border: none;
        }
    }
    .la-table {
        width: 100%;
        margin: 0;
        border: 2px solid #000;
        // border-bottom: none;
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
    input {
        width: 100%;
        height: 100%;
        border: none;
        outline: none;
        text-align: center;
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
</style>