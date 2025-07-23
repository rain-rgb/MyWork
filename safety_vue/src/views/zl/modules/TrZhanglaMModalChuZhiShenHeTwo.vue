<template>
    <j-modal
        :title="title"
        :width="1400"
        :visible="visible"
        :maskClosable="false"
        switchFullscreen
        :okButtonProps="{ class: { 'jee-hidden': disableSubmit } }"
        @ok="handleOk"
        @cancel="handleCancel"
    >
        <a-tabs default-active-key="1">
            <a-tab-pane key="1" tab="智能张拉详情数据">
                <a-table
                    :rowKey="(record, index) => index"
                    :pagination="ipagination"
                    :columns="columns"
                    :loading="loading"
                    :data-source="data"
                    bordered
                    size="middle"
                    class="j-table-force-nowrap"
                    :rowClassName="setClass"
                >
                </a-table>
            </a-tab-pane>
        </a-tabs>
        <a-tabs @change="callback">
            <a-tab-pane v-for="item in holeidList" :key="item.holeid" :tab="item.gsbh">
                <a-form>
                    <j-form-container disabled>
                        <a-card
                            title="施工方处置信息"
                            :bordered="false"
                            :headStyle="{ color: '#0785fd' }"
                            :bodyStyle="{ padding: '10' }"
                            style="margin-top: 10px"
                        >
                            <a-row>
                                <a-col :span="12">
                                    <a-form-item label="问题原因" :labelCol="labelCol" :wrapperCol="wrapperCol">
                                        <a-textarea
                                            placeholder="无"
                                            v-model="overHandlerObj.problemReasons"
                                            :auto-size="{ minRows: 5, maxRows: 20 }"
                                        ></a-textarea>
                                    </a-form-item>
                                </a-col>
                                <a-col :span="12">
                                    <a-form-item label="处理方式" :labelCol="labelCol" :wrapperCol="wrapperCol">
                                        <a-input v-model="overHandlerObj.handleWay" placeholder="无"></a-input>
                                    </a-form-item>
                                </a-col>
                            </a-row>
                            <a-row>
                                <a-col :span="12">
                                    <a-form-item label="处理结果" :labelCol="labelCol" :wrapperCol="wrapperCol">
                                        <a-input v-model="overHandlerObj.handleResult" placeholder="无"></a-input>
                                    </a-form-item>
                                </a-col>
                                <a-col :span="12">
                                    <a-form-item label="处理时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
                                        <a-input v-model="overHandlerObj.handleTime" placeholder="无"></a-input>
                                    </a-form-item>
                                </a-col>
                            </a-row>
                            <a-row>
                                <a-col :span="12">
                                    <a-form-item label="处置人" :labelCol="labelCol" :wrapperCol="wrapperCol">
                                        <a-input v-model="overHandlerObj.handlePerson" placeholder="无"></a-input>
                                    </a-form-item>
                                </a-col>
                                <a-col :span="24">
                                    <a-form-item label="处置附件" :labelCol="labelCol1" :wrapperCol="wrapperCol1">
                                        <viewer :images="detailList">
                                            <img
                                                v-for="(img, index) in detailList"
                                                :key="index"
                                                style="
                                                    height: 100px;
                                                    width: 100px;
                                                    margin: 5px 10px 5px 10px;
                                                    float: left;
                                                "
                                                :src="img"
                                                alt=""
                                            />
                                        </viewer>
                                    </a-form-item>
                                </a-col>
                            </a-row>
                        </a-card>
                        <a-card
                            v-show="overLevel === 40 || overLevel === 60 || overLevel === 20"
                            title="监理单位审核"
                            :bordered="false"
                            :headStyle="{ color: '#0785fd' }"
                            :bodyStyle="{ padding: '10' }"
                            style="margin-top: 10px"
                        >
                            <a-row>
                                <a-col :span="12">
                                    <a-form-item label="监理审核" :labelCol="labelCol" :wrapperCol="wrapperCol">
                                        <a-input
                                            v-if="overHandlerObj.overproofStatus === 30"
                                            v-model="overHandlerObj.remark"
                                            placeholder="无"
                                        ></a-input>
                                        <a-input
                                            v-else
                                            v-model="overHandlerObj.supervisorApproval"
                                            placeholder="无"
                                        ></a-input>
                                    </a-form-item>
                                </a-col>
                                <a-col :span="12">
                                    <a-form-item label="审核结果" :labelCol="labelCol" :wrapperCol="wrapperCol">
                                        <a-input
                                            v-if="overHandlerObj.overproofStatus === 30"
                                            placeholder="驳回"
                                            default-value="驳回"
                                        ></a-input>
                                        <a-input
                                            v-else
                                            v-model="overHandlerObj.supervisorResult"
                                            placeholder="无"
                                        ></a-input>
                                    </a-form-item>
                                </a-col>
                            </a-row>
                            <a-row>
                                <a-col :span="12">
                                    <a-form-item label="审核人" :labelCol="labelCol" :wrapperCol="wrapperCol">
                                        <a-input v-model="overHandlerObj.approvalPerson" placeholder="无"></a-input>
                                    </a-form-item>
                                </a-col>
                                <a-col :span="12">
                                    <a-form-item label="监理处理时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
                                        <a-input
                                            v-model="overHandlerObj.supervisorHandleTime"
                                            placeholder="无"
                                        ></a-input>
                                    </a-form-item>
                                </a-col>
                            </a-row>
                            <a-row>
                                <a-col :span="24">
                                    <a-form-item label="审核附件" :labelCol="labelCol1" :wrapperCol="wrapperCol1">
                                        <viewer :images="detailList1">
                                            <img
                                                v-for="(img, index) in detailList1"
                                                :key="index"
                                                style="
                                                    height: 100px;
                                                    width: 100px;
                                                    margin: 5px 10px 5px 10px;
                                                    float: left;
                                                "
                                                :src="img"
                                                alt=""
                                            />
                                        </viewer>
                                    </a-form-item>
                                </a-col>
                            </a-row>
                        </a-card>
                        <a-card
                            v-show="overLevel === 60 || overLevel === 20"
                            title="指挥部核查"
                            :bordered="false"
                            :headStyle="{ color: '#0785fd' }"
                            :bodyStyle="{ padding: '10' }"
                            style="margin-top: 10px"
                        >
                            <a-row>
                                <a-col :span="12">
                                    <a-form-item label="指挥部核查" :labelCol="labelCol" :wrapperCol="wrapperCol">
                                        <a-input
                                            v-if="
                                                overHandlerObj.overproofStatus === 60 ||
                                                (overHandlerObj.overproofStatus !== 20 && overHandlerObj.headquartersRemark !== null&& overHandlerObj.headquartersRemark !== undefined)
                                            "
                                            v-model="overHandlerObj.headquartersRemark"

                                        ></a-input>
                                        <a-input
                                        v-else
                                            v-model="overHandlerObj.headquartersApproval"
                                            placeholder="无"
                                        ></a-input>
                                    </a-form-item>
                                </a-col>
                                <a-col :span="12">
                                    <a-form-item label="核查结果" :labelCol="labelCol" :wrapperCol="wrapperCol">
                                        <a-input
                                            v-if="
                                                overHandlerObj.overproofStatus === 60 ||
                                                (overHandlerObj.overproofStatus !== 20 && overHandlerObj.headquartersRemark !== null&& overHandlerObj.headquartersRemark !== undefined)
                                            "
                                            placeholder="驳回"
                                            default-value="驳回"
                                        ></a-input>
                                        <a-input
                                            v-else
                                            v-model="overHandlerObj.headquartersResult"
                                            placeholder="无"
                                        ></a-input>
                                    </a-form-item>
                                </a-col>
                            </a-row>
                            <a-row>
                                <a-col :span="12">
                                    <a-form-item label="核查人" :labelCol="labelCol" :wrapperCol="wrapperCol">
                                        <a-input v-model="overHandlerObj.headquartersPerson" placeholder="无"></a-input>
                                    </a-form-item>
                                </a-col>
                                <a-col :span="12">
                                    <a-form-item label="核查时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
                                        <a-input
                                            v-model="overHandlerObj.headquartersHandleTime"
                                            placeholder="无"
                                        ></a-input>
                                    </a-form-item>
                                </a-col>
                            </a-row>
                        </a-card>
                    </j-form-container>
                </a-form>
            </a-tab-pane>
        </a-tabs>

        <g-ch-modal ref="gch"></g-ch-modal>
    </j-modal>
</template>

<script>
import { getAction } from '@api/manage'
import GChModal from '@/views/zlyj/modules/GChModal'
export default {
    name: 'TrZhanglaMModalChuZhiShenHeTwo',
    components: {
        GChModal,
    },
    data() {
        return {
            overLevel: 0,
            data: [],
            holeidList: [],
            holeid: '',
            loading: false,
            ipagination: false,
            columns: [
                {
                    title: '过程',
                    align: 'center',
                    dataIndex: '',
                    customRender: (text, record, index) => {
                        let childrenVal = <a onClick={() => this.gchDetail(record)}>过程数据</a>
                        const obj = {
                            children: childrenVal,
                            attrs: {},
                        }
                        if (index === 0 || index % 6 === 0) {
                            obj.attrs.rowSpan = 6
                        } else {
                            obj.attrs.rowSpan = 0
                        }
                        return obj
                    },
                },
                {
                    title: '孔号',
                    align: 'center',
                    dataIndex: 'gsbh',
                    customRender: (text, record, index) => this.columnsInit(text, index, 6),
                },
                {
                    title: '张拉时间',
                    align: 'center',
                    dataIndex: 'zlsj',
                    customRender: (text, record, index) => this.columnsInit(text, index, 6),
                },
                {
                    title: '张拉断面',
                    align: 'center',
                    dataIndex: 'dh',
                    customRender: (text, record, index) => this.columnsInit(text, index, 3),
                },
                {
                    title: '记录点',
                    align: 'center',
                    dataIndex: 'recodePoint',
                },
                {
                    title: '初始行程(10/15%)',
                    align: 'center',
                    dataIndex: 'jdbfb10',
                },
                {
                    title: '第一行程(20%/30%)',
                    align: 'center',
                    dataIndex: 'jdbfb20',
                },
                {
                    title: '第二行程(50%-1段)',
                    align: 'center',
                    dataIndex: 'jdbfb50I',
                },
                {
                    title: '第三行程(50%-2段)',
                    align: 'center',
                    dataIndex: 'jdbfb50II',
                },
                {
                    title: '第四行程(100%)',
                    align: 'center',
                    dataIndex: 'jdbfb100',
                },
                {
                    title: '设计张力(KN)',
                    align: 'center',
                    dataIndex: 'sjzll',
                    customRender: (text, record, index) => this.columnsInit(text, index, 6),
                },
                {
                    title: '总伸长量(mm)',
                    align: 'center',
                    dataIndex: 'zscl',
                    customRender: (text, record, index) => this.columnsInit(text, index, 6),
                },
                {
                    title: '设计伸长量(mm)',
                    align: 'center',
                    dataIndex: 'llscl',
                    customRender: (text, record, index) => this.columnsInit(text, index, 6),
                },
                {
                    title: '伸长量误差(%)',
                    align: 'center',
                    dataIndex: 'sclper',
                    customRender: (text, record, index) => this.columnsInit(text, index, 6),
                },
                {
                    title: '超张百分比(%)',
                    align: 'center',
                    dataIndex: 'zzlb',
                    customRender: (text, record, index) => this.columnsInit(text, index, 6),
                },
                {
                    title: '持荷时间(s)',
                    align: 'center',
                    dataIndex: 'chsj',
                    customRender: (text, record, index) => this.columnsInit(text, index, 3),
                },
                {
                    title: '记录时间',
                    align: 'center',
                    dataIndex: 'jlsj',
                    customRender: (text, record, index) => this.columnsInit(text, index, 6),
                },
                {
                    title: '状态',
                    align: 'center',
                    dataIndex: 'overLevel',
                    customRender: (text, record, index) => {
                        let childrenVal
                        if (text == 0 || text == null) {
                            childrenVal = (
                                <div>
                                    <a-tag color="green">合格</a-tag>
                                </div>
                            )
                        } else {
                            childrenVal = (
                                <div>
                                    <a-tag color="red">不合格</a-tag>
                                </div>
                            )
                        }
                        const obj = {
                            children: childrenVal,
                            attrs: {},
                        }
                        if (index === 0 || index % 3 === 0) {
                            obj.attrs.rowSpan = 3
                        } else {
                            obj.attrs.rowSpan = 0
                        }
                        return obj
                    },
                },
                {
                    title: '预警原因',
                    align: 'center',
                    dataIndex: 'overReason',
                    customRender: (text, record, index) => this.columnsInit(text, index, 6),
                },
            ],
            title: '',
            width: 800,
            visible: false,
            disableSubmit: true,
            syjid: '',
            labelCol: {
                xs: { span: 24 },
                sm: { span: 6 },
            },
            wrapperCol: {
                xs: { span: 24 },
                sm: { span: 16 },
            },
            labelCol1: {
                xs: { span: 24 },
                sm: { span: 3 },
            },
            wrapperCol1: {
                xs: { span: 24 },
                sm: { span: 20 },
            },
            overHandlerObj: {},
            detailList: [],
            detailList1: [],
            url: {
                list1s: '/zhanglam/trZhanglaM/list1s',
                list: '/zhanglam/zhanglaYajiangOverHandler/list',
                holeidList: '/zhanglam/zhanglaYajiangOverHandler/holeidList47',
            },
        }
    },
    methods: {
        zhanglamessage() {
            this.loading = true
            //请求张拉每个孔的断面数据
            let param = { syjid: this.syjid, overLevel_begin: 1 }
            getAction(this.url.list1s, param)
                .then((res) => {
                    if (res.success) {
                        this.data = res.result
                    } else {
                        this.data = []
                    }
                })
                .finally(() => {
                    this.loading = false
                })
        },
        overHandler() {
            this.overHandlerObj = {}
            this.detailList = []
            this.detailList1 = []
            let param = { baseId: this.syjid, holeid: this.holeid }
            getAction(this.url.list, param).then((res) => {
                if (res.success) {
                    if (res.result.records.length > 0) {
                        this.overHandlerObj = res.result.records[0]
                        this.detailList = this.overHandlerObj.filePath2 ? this.overHandlerObj.filePath2.split(',') : []
                        this.detailList1 = this.overHandlerObj.filePath ? this.overHandlerObj.filePath.split(',') : []
                    }
                }
            })
        },
        getHoleidList() {
            let param = { syjid: this.syjid, type: 0, status: 10 }
            getAction(this.url.holeidList, param).then((res) => {
                if (res.success) {
                    this.holeidList = res.result
                    if (this.holeidList.length > 0) {
                        this.holeid = res.result[0].holeid
                        this.overHandler()
                    } else {
                        this.$message.warning('暂无处置孔道！')
                    }
                }
            })
        },
        callback(key) {
            this.holeid = key
            this.overHandler()
        },
        callchuzhishenhe(e) {
            this.syjid = e.syjid
            this.overLevel = e.zhanglaMList[0].overproofStatus
            this.visible = true
            this.zhanglamessage()
            this.getHoleidList()
        },
        handleOk() {
            this.visible = false
        },
        handleCancel() {
            this.visible = false
        },
        //表格合并
        columnsInit(text, index, num) {
            const obj = {
                children: text,
                attrs: {},
            }
            if (index === 0 || index % num === 0) {
                obj.attrs.rowSpan = num
            } else {
                obj.attrs.rowSpan = 0
            }
            return obj
        },
        setClass(record) {
            return record.overLevel == null || record.overLevel == 0 ? '' : 'rowColor'
        },
        gchDetail(record) {
            this.$refs.gch.getList(record)
        },
    },
}
</script>

<style scoped>
::v-deep .rowColor {
    color: red;
}
</style>
