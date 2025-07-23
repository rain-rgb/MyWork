<template>
    <div id="paperA4">
        <!-- 项目名 -->
        <div class="projectName">{{ reportData.departfullname }}</div>
        <!-- 表名 -->
        <div class="tablename">
            <span class="tiTableName">{{ reportData.tiTableName }}</span>
            <div class="tiCustomTableNum">
                <span v-for="(item, i) in tiCustomTableNum" :key="i">{{ item }}</span>
            </div>
            <span class="page" style="display: none">第1页 共1页</span>
        </div>
        <span class="fontStyle">
            施工单位：<input
                type="text"
                style="width: 300px; border-bottom: 1px solid rgb(0, 0, 0); text-align: left"
                v-model="reportData.shigongdanweiq"
            />
            合同号：<input
                type="text"
                style="width: 200px; border-bottom: 1px solid #000; text-align: left"
                v-model="reportData.hetonghaoq"
            />
            <br /><br />
            监理单位：<input
                type="text"
                style="width: 300px; border-bottom: 1px solid rgb(0, 0, 0); text-align: left"
                v-model="reportData.jianlidanweiq"
            />
            编&nbsp;号：<input
                type="text"
                style="width: 200px; border-bottom: 1px solid #000; text-align: left"
                v-model="reportData.reportingSheetNoNew"
            />
        </span>
        <br /><br />
        <div class="tableContent">
            <table class="la-table" style="border-top: 2px solid #000">
                <tbody>
                    <tr style="position: relative; height: 104px">
                        <td>
                            <span style="position: absolute; left: 2px; top: 2px">致监理工程师：</span>
                            <textarea
                                style="text-indent: 7em; font-family: SimSun"
                                v-model="reportData.cbryj"
                            ></textarea>
                            <!-- <textarea
                                style="text-indent: 7em; font-family: SimSun"
                                v-model="textareaValue"
                            ></textarea> -->
                            <div style="position: absolute; bottom: 2px; right: 10px">
                                <span>技术负责人(签字、公章)：</span>
                                <span
                                    >&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;年&emsp;&emsp;&emsp;月&emsp;&emsp;&emsp;日</span
                                >
                            </div>
                        </td>
                    </tr>
                    <tr>
                        <td style="text-align: left">
                            <span style="margin-right: 166px; margin-left: 9px">附件:1、材料出厂质里保证书等(如有)</span
                            ><span>2、材料自检验证报告</span>
                        </td>
                    </tr>
                </tbody>
            </table>
            <table class="la-table next">
                <tbody>
                    <tr>
                        <td style="width: 103.5px">材料名称</td>
                        <td><input type="text" v-model="reportData.clmc1" /></td>
                        <td><input type="text" v-model="reportData.clmc2" /></td>
                        <td><input type="text" v-model="reportData.clmc3" /></td>
                    </tr>
                    <tr>
                        <td>材料来源、产地</td>
                        <td><input type="text" v-model="reportData.sampleChanDi" /></td>
                        <td><input type="text" v-model="reportData.clcd2" /></td>
                        <td><input type="text" v-model="reportData.clcd3" /></td>
                    </tr>
                    <tr>
                        <td>材料规格</td>
                        <td><input type="text" v-model="reportData.sampleGuiGeXingHao" /></td>
                        <td><input type="text" v-model="reportData.ggph2" /></td>
                        <td><input type="text" v-model="reportData.ggph3" /></td>
                    </tr>
                    <tr>
                        <td>用途(工程或部位)</td>
                        <td><input type="text" v-model="reportData.yongtu1" /></td>
                        <td><input type="text" v-model="reportData.yongtu2" /></td>
                        <td><input type="text" v-model="reportData.yongtu3" /></td>
                    </tr>
                    <tr>
                        <td>本批次材料数量</td>
                        <td><input type="text" v-model="reportData.sampleDaiBiaoShuLiang" /></td>
                        <td><input type="text" v-model="reportData.cljcs2" /></td>
                        <td><input type="text" v-model="reportData.cljcs3" /></td>
                    </tr>
                </tbody>
            </table>
            <table class="la-table next">
                <tbody>
                    <tr>
                        <td style="width: 103.5px">内容</td>
                        <td colspan="3">施工单位自检</td>
                        <td colspan="3">监理单位自检</td>
                    </tr>
                    <tr>
                        <td>日期</td>
                        <td colspan="3">
                            <a-date-picker @change="onChange1">
                                <span style="color: #000; font-size: 12px">
                                    {{ reportData.ccrq1 ? reportData.ccrq1 : '请选择日期' }}
                                </span>
                            </a-date-picker>
                        </td>
                        <td colspan="3">
                            <a-date-picker @change="onChange2">
                                <span style="color: #000; font-size: 12px">
                                    {{ reportData.ccrq4 ? reportData.ccrq4 : '请选择日期' }}
                                </span>
                            </a-date-picker>
                        </td>
                    </tr>
                    <tr>
                        <td>取样地点</td>
                        <td><input type="text" v-model="reportData.qydd1" /></td>
                        <td><input type="text" v-model="reportData.qydd2" /></td>
                        <td><input type="text" v-model="reportData.qydd3" /></td>
                        <td><input type="text" v-model="reportData.qydd4" /></td>
                        <td><input type="text" v-model="reportData.qydd5" /></td>
                        <td><input type="text" v-model="reportData.qydd6" /></td>
                    </tr>
                    <tr>
                        <td>取样数量</td>
                        <td><input type="text" v-model="reportData.yjsysj1" /></td>
                        <td><input type="text" v-model="reportData.yjsysj2" /></td>
                        <td><input type="text" v-model="reportData.yjsysj3" /></td>
                        <td><input type="text" v-model="reportData.yjsysj4" /></td>
                        <td><input type="text" v-model="reportData.yjsysj5" /></td>
                        <td><input type="text" v-model="reportData.yjsysj6" /></td>
                    </tr>
                    <tr>
                        <td>试验日期</td>
                        <td colspan="3">
                            <a-range-picker @change="onChange3">
                                <span style="color: #000; font-size: 12px">
                                    {{ reportData.syrq1 ? reportData.syrq1 : '请选择日期' }}
                                </span>
                            </a-range-picker>
                        </td>
                        <td colspan="3">
                            <a-range-picker @change="onChange4">
                                <span style="color: #000; font-size: 12px">
                                    {{ reportData.syrq4 ? reportData.syrq4 : '请选择日期' }}
                                </span>
                            </a-range-picker>
                        </td>
                    </tr>
                    <tr>
                        <td>试验结果</td>
                        <td><input type="text" v-model="reportData.syjg1" /></td>
                        <td><input type="text" v-model="reportData.syjg2" /></td>
                        <td><input type="text" v-model="reportData.syjg3" /></td>
                        <td><input type="text" v-model="reportData.syjg4" /></td>
                        <td><input type="text" v-model="reportData.syjg5" /></td>
                        <td><input type="text" v-model="reportData.syjg6" /></td>
                    </tr>
                    <tr>
                        <td>试验报告编号</td>
                        <td colspan="3"><input type="text" v-model="reportData.reportNo" /></td>
                        <td colspan="3"><input type="text" v-model="reportData.diyreportNo" /></td>
                    </tr>
                </tbody>
            </table>
            <table class="la-table next" style="position: relative; height: 255px; border-bottom: 2px solid #000">
                <tbody>
                    <tr>
                        <td style="height: 225px">
                            <span class="starTitle">致项目经理部：</span>
                            <div style="position: absolute; top: 30px; text-align: left">
                                &emsp;&emsp;&emsp;&emsp;&emsp;上述材料的取样、试验等（<input
                                    class="proposal"
                                    type="checkbox"
                                    id="danyuan9_chk"
                                    name="danyuan5_chk"
                                    value="1"
                                />
                                <label for="danyuan9_chk">符合</label
                                ><input
                                    class="proposal"
                                    type="checkbox"
                                    id="danyuan10_chk"
                                    name="danyuan5_chk"
                                    value="2"
                                />
                                <label for="danyuan10_chk">不符合</label>）规程要求；经抽检，材料质量（<input
                                    class="proposal"
                                    type="checkbox"
                                    id="danyuan1_chk"
                                    name="danyuan1_chk"
                                    value="1"
                                /><span></span>
                                <label for="danyuan1_chk">符合</label>
                                <input
                                    class="proposal"
                                    type="checkbox"
                                    id="danyuan2_chk"
                                    name="danyuan1_chk"
                                    value="2"
                                />
                                <span></span><label for="danyuan2_chk">不符合</label>）本工程有关要求，（<input
                                    class="proposal"
                                    type="checkbox"
                                    id="danyuan5_chk"
                                    name="danyuan2_chk"
                                    value="1"
                                /><span></span>
                                <label for="danyuan5_chk">可以</label>
                                <input
                                    class="proposal"
                                    type="checkbox"
                                    id="danyuan6_chk"
                                    name="danyuan2_chk"
                                    value="2"
                                />
                                <span></span><label for="danyuan6_chk">不可以</label>） 在指定工程部位使用。
                            </div>
                            <div style="position: absolute; bottom: 35px; left: 160px">
                                <span>专业监理工程师（签字、公章）：</span>
                                <span
                                    >&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;年&emsp;&emsp;&emsp;月&emsp;&emsp;&emsp;日</span
                                >
                            </div>
                        </td>
                    </tr>
                    <tr>
                        <td style="height: 27px">
                            <span style="position: absolute; left: 10px; bottom: 6px"
                                >注：本表由施工单位填报一式二份，监理、施工单位各存一份。</span
                            >
                        </td>
                    </tr>
                </tbody>
            </table>
        </div>
    </div>
</template>
<script>
export default {
    name: 'BYDGHJ',
    props: ['formData'],
    components: {},
    data() {
        return {
            bg: {},
            // textareaValue: '\n\n     下列原材料经自检试验符合本工程有关要求，报请验证，并准予（进场）使用。',
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
        onChange1(time) {
            this.reportData.ccrq1 = this.timestampToTime(time)
        },
        onChange2(time) {
            this.reportData.ccrq4 = this.timestampToTime(time)
        },
        onChange3(time) {
            this.timestampToTime(time[0]) == this.timestampToTime(time[1])
                ? (this.reportData.syrq1 = this.timestampToTime(time[0]))
                : (this.reportData.syrq1 = this.timestampToTime(time[0]) + ' 至 ' + this.timestampToTime(time[1]))
        },
        onChange4(time) {
            this.timestampToTime(time[0]) == this.timestampToTime(time[1])
                ? (this.reportData.syrq4 = this.timestampToTime(time[0]))
                : (this.reportData.syrq4 = this.timestampToTime(time[0]) + ' 至 ' + this.timestampToTime(time[1]))
        },
    },
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
    color: #000;
    font-size: 12px;
    .projectName {
        text-align: center;
        font-size: 22px;
        font-weight: bold;
        color: #000;
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
    input {
        width: 100%;
        border: none;
        outline: none;
        text-align: center;
    }
    .la-table {
        width: 100%;
        margin: 0;
        border-left: 2px solid #000;
        border-right: 2px solid #000;
        color: #000;
        border-collapse: collapse;
        border-spacing: 0;
        td {
            height: 38px;
            border: 1px solid #000;
            text-align: center;
            line-height: 16px;
            .starTitle {
                position: absolute;
                left: 3px;
                top: 3px;
                line-height: normal;
            }
            .proposal {
                vertical-align: text-bottom;
                width: 20px;
                height: 13px;
            }
        }
    }
    .next {
        // border-top: none;
        margin-top: -1px;
        // margin-top: 30px;
    }
    textarea {
        resize: none;
        border: none;
        outline: none;
        height: 100%;
        width: 100%;
        padding: 1px;
        vertical-align: middle;
    }
}
</style>