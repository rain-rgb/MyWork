<template>
    <j-modal
        centered
        :title="title"
        :width="1200"
        :visible="visible"
        switchFullscreen
        @ok="handleOk"
        :okButtonProps="{ class: { 'jee-hidden': disableSubmit } }"
        @cancel="handleCancel"
        cancelText="关闭"
    >
        <a-form :form="form">
            <j-form-container disabled>
                <a-row>
                    <a-col :span="12">
                        <a-form-item label="组织机构" :labelCol="labelCol" :wrapperCol="wrapperCol">
                            <a-input v-model="siloForm.sysOrgCode_dictText"></a-input>
                        </a-form-item>
                    </a-col>
                    <a-col :span="12">
                        <a-form-item label="料仓名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
                            <a-input v-model="siloForm.name"></a-input>
                        </a-form-item>
                    </a-col>
                </a-row>
                <a-row>
                    <a-col :span="12">
                        <a-form-item label="料斗号" :labelCol="labelCol" :wrapperCol="wrapperCol">
                            <a-input v-model="siloForm.cailiaoname"></a-input>
                        </a-form-item>
                    </a-col>
                    <a-col :span="12">
                        <a-form-item label="材料类型" :labelCol="labelCol" :wrapperCol="wrapperCol">
                            <a-input v-model="siloForm.cailiaono_dictText"></a-input>
                        </a-form-item>
                    </a-col>
                </a-row>
                <a-row>
                    <a-col :span="12">
                        <a-form-item label="规格类型" :labelCol="labelCol" :wrapperCol="wrapperCol">
                            <a-input v-model="siloForm.guigexinghao"></a-input>
                        </a-form-item>
                    </a-col>
                    <a-col :span="12">
                        <a-form-item label="实时库存数量" :labelCol="labelCol" :wrapperCol="wrapperCol">
                            <a-input v-model="siloForm.sskucun"></a-input>
                        </a-form-item>
                    </a-col>
                </a-row>
                <a-row>
                    <a-col :span="12">
                        <a-form-item label="料仓状态" :labelCol="labelCol" :wrapperCol="wrapperCol">
                            <a-input v-model="siloForm.liaocangStatus_dictText"></a-input>
                        </a-form-item>
                    </a-col>
                    <a-col :span="12">
                        <a-form-item label="抽检状态" :labelCol="labelCol" :wrapperCol="wrapperCol">
                            <a-input v-model="siloForm.choujianzt"></a-input>
                        </a-form-item>
                    </a-col>
                </a-row>
                <a-row>
                    <a-col :span="12">
                        <a-form-item label="累计进场数量" :labelCol="labelCol" :wrapperCol="wrapperCol">
                            <a-input v-model="siloForm.ljjinchang"></a-input>
                        </a-form-item>
                    </a-col>
                    <a-col :span="12">
                        <a-form-item label="累计使用数量" :labelCol="labelCol" :wrapperCol="wrapperCol">
                            <a-input v-model="siloForm.ljshiyong"></a-input>
                        </a-form-item>
                    </a-col>
                </a-row>
                <a-row>
                    <a-col :span="12">
                        <a-form-item label="库存损耗累计修正" :labelCol="labelCol" :wrapperCol="wrapperCol">
                            <a-input v-model="siloForm.ljxiuzheng"></a-input>
                        </a-form-item>
                    </a-col>
                </a-row>
            </j-form-container>
            <a-tabs v-model="activeKey">
                <a-tab-pane key="1" tab="明细" :forceRender="true">
                    <a-table
                        rowKey="id"
                        bordered
                        size="middle"
                        :columns="columns1"
                        :dataSource="dataSource1"
                        :pagination="false"
                        class="j-table-force-nowrap"
                    >
                    </a-table>
                </a-tab-pane>
                <a-tab-pane key="2" tab="检测详情">
                    <a-table
                        rowKey="id"
                        bordered
                        size="middle"
                        :columns="columns2"
                        :dataSource="dataSource2"
                        :pagination="false"
                        class="j-table-force-nowrap"
                    >
                      <span slot="tags2" slot-scope="tags2, record">
  <a-tag color="orange" v-if="record.testStatus == '0'">未检验</a-tag>
  <a-tag color="purple" v-if="record.testStatus == '3'">检验中</a-tag>
  <a-tag color="green" v-if="record.testStatus == '1'">合格</a-tag>
  <a-tag color="red" v-if="record.testStatus == '2'">不合格</a-tag>
</span>

                      <template slot="report" slot-scope="report, record">
                            <a-button type="primary" @click="checkFile(record)">查看</a-button>
                        </template>
                    </a-table>
                </a-tab-pane>
                <a-tab-pane key="3" tab="使用详情">
                    <a-table
                        rowKey="id"
                        bordered
                        size="middle"
                        :columns="columns3"
                        :dataSource="dataSource3"
                        :pagination="false"
                        class="j-table-force-nowrap"
                    >
                    </a-table>
                </a-tab-pane>
                <a-tab-pane key="4" tab="修正详情">
                    <a-table
                        rowKey="id"
                        bordered
                        size="middle"
                        :columns="columns4"
                        :dataSource="dataSource4"
                        :pagination="false"
                        class="j-table-force-nowrap"
                    >
                    </a-table>
                </a-tab-pane>
            </a-tabs>
        </a-form>
    </j-modal>
</template>

<script>
import { getAction } from '@/api/manage'
import Vue from 'vue'

export default {
    name: 'SiloAnalysisDetail',
    components: {},
    data() {
        return {
            siloForm: {},
            guid: '',
            form: this.$form.createForm(this),
            visible: false,
            disableSubmit: true,
            title: '详情',
            labelCol: {
                xs: { span: 24 },
                sm: { span: 6 },
            },
            wrapperCol: {
                xs: { span: 24 },
                sm: { span: 16 },
            },
            activeKey: '1',
            dataSource1: [],
            dataSource2: [],
            dataSource3: [],
            dataSource4: [],
            columns1: [
                {
                    title: '地磅',
                    align: 'center',
                    dataIndex: 'shebeibianhao_dictText',
                    key: 'shebeibianhao_dictText',
                },
                {
                    title: '料仓',
                    align: 'center',
                    dataIndex: 'lcbianhao_dictText',
                    key: 'lcbianhao_dictText',
                },
                {
                    title: '供应商单位',
                    align: 'center',
                    dataIndex: 'gongyingshangdanweibianma_dictText',
                    key: 'gongyingshangdanweibianma_dictText',
                },
                {
                    title: '进场时间',
                    align: 'center',
                    dataIndex: 'jinchangshijian',
                    key: 'jinchangshijian',
                },
                {
                    title: '出场时间',
                    align: 'center',
                    dataIndex: 'chuchangshijian',
                    key: 'chuchangshijian',
                },
                {
                    title: '进出材料单',
                    align: 'center',
                    dataIndex: 'jinchuliaodanno',
                    key: 'jinchuliaodanno',
                },
                {
                    title: '车牌号',
                    align: 'center',
                    dataIndex: 'qianchepai',
                    key: 'qianchepai',
                },
                {
                    title: '司磅员',
                    align: 'center',
                    dataIndex: 'sibangyuan',
                    key: 'sibangyuan',
                },
                // {
                //     title: '毛重(吨)',
                //     align: 'center',
                //     dataIndex: 'maozhongt',
                //     key: 'maozhongt',
                // },
                // {
                //     title: '皮重(吨)',
                //     align: 'center',
                //     dataIndex: 'pizhongt',
                //     key: 'pizhongt',
                // },
                {
                    title: '数量',
                    align: 'center',
                    dataIndex: 'jingzhongt',
                    key: 'jingzhongt',
                },
            ],
            columns2: [
                {
                    title: '样品编号',
                    align: 'center',
                    dataIndex: 'sampleNumber',
                    key: 'sampleNumber',
                },
                {
                    title: '试验名称',
                    align: 'center',
                    dataIndex: 'testName',
                    key: 'testName',
                },
                {
                    title: '取样时间',
                    align: 'center',
                    dataIndex: 'samplingTime',
                    key: 'samplingTime',
                },
                {
                    title: '试验时间',
                    align: 'center',
                    dataIndex: 'testTime',
                    key: 'testTime',
                },
              {
                title: '状态',
                align: 'center',
                dataIndex: 'testStatus',
                scopedSlots: { customRender: 'tags2' }
              },
                {
                    title: '试验员',
                    align: 'center',
                    dataIndex: 'tester',
                    key: 'tester',
                },
                {
                    title: '报告详情',
                    align: 'center',
                    dataIndex: 'report',
                    key: 'report',
                    scopedSlots: { customRender: 'report' },
                },
            ],
            columns3: [
                {
                    title: '配料单号',
                    align: 'center',
                    dataIndex: 'dosingOrderNumber',
                    key: 'dosingOrderNumber',
                },
                {
                    title: '配料时间',
                    align: 'center',
                    dataIndex: 'dosingTime',
                    key: 'dosingTime',
                },
                {
                    title: '工程部位',
                    align: 'center',
                    dataIndex: 'projectPart',
                    key: 'projectPart',
                },
                {
                    title: '使用量',
                    align: 'center',
                    dataIndex: 'uses',
                    key: 'uses',
                },
                {
                    title: '料仓',
                    align: 'center',
                    dataIndex: 'storageId_dictText',
                    key: 'storageId_dictText',
                },
                {
                    title: '检验批编号',
                    align: 'center',
                    dataIndex: 'inspectionLotNumber',
                    key: 'inspectionLotNumber',
                },
            ],
            columns4: [
                {
                    title: '修正人',
                    align: 'center',
                    dataIndex: 'updateBy',
                    key: 'updateBy',
                },
                {
                    title: '修正值',
                    align: 'center',
                    dataIndex: 'updateValue',
                    key: 'updateValue',
                },
                {
                    title: '修正时间',
                    align: 'center',
                    dataIndex: 'updateTime',
                    key: 'updateTime',
                },
                {
                    title: '修正原因',
                    align: 'center',
                    dataIndex: 'remark',
                    key: 'remark',
                },
            ],
            url: {
                list1: '/yclsl/wzycljinchanggb/list47',
                list2: '/ycltd/yclTestDetail/list',
                list3: '/yclud/yclUsageDetail/list',
                list4: '/wzliaocang/wzliaocangXz/list',
            },
        }
    },
    created() {},
    methods: {
        getList() {
            this.dataSource1 = []
            this.dataSource2 = []
            this.dataSource3 = []
            this.dataSource4 = []
            let params = {
              lcbianhao: this.guid,
            }
            //获取明细
            getAction(this.url.list1, params).then((res) => {
                if (res.code == 200) {
                    this.dataSource1 = res.result.records
                }
            })
            //获取检测详情
            let params2 = {
                storageId: this.guid,
            }
            getAction(this.url.list2, params2).then((res) => {
                if (res.code == 200) {
                    this.dataSource2 = res.result.records
                }
            })
            //获取使用详情
            let params3 = {
                storageId: this.guid,
            }
            getAction(this.url.list3, params3).then((res) => {
                if (res.code == 200) {
                    this.dataSource3 = res.result.records
                }
            })
            //获取修正详情
            let params4 = {
                guid: this.guid,
            }
            getAction(this.url.list4, params4).then((res) => {
                if (res.code == 200) {
                    this.dataSource4 = res.result.records
                }
            })
        },
        handleCancel() {
            this.visible = false
        },
        handleOk() {
            this.visible = false
        },
    },
}
</script>
<style scoped>
</style>