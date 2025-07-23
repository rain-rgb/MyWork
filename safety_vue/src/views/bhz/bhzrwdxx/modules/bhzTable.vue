<template>
    <j-modal
        :title="title"
        :width="1200"
        :visible="visible"
        @ok="handleOk"
        :okButtonProps="{ class: { 'jee-hidden': disableSubmit } }"
        @cancel="handleCancel"
        cancelText="关闭"
    >
        <a-table
            ref="table"
            size="middle"
            bordered
            rowKey="id"
            class="j-table-force-nowrap"
            :columns="columns"
            :dataSource="dataSource"
            :pagination="ipagination"
            :loading="loading"
            @change="handleTableChange"
        >
            <span slot="tags" slot-scope="tags, record">
                <a-tag color="green" v-if="record.overLevel == '0'">合格</a-tag>
                <a-tag color="orange" v-if="record.overLevel == '1'">初级超标</a-tag>
                <a-tag color="yellow" v-if="record.overLevel == '2'">中级超标</a-tag>
                <a-tag color="red" v-if="record.overLevel == '3'">高级超标</a-tag>
            </span>
            <span slot="formulaNo" slot-scope="formulaNo, record">
                <a-tag color="green">{{ record.formulaNo }}</a-tag>
            </span>
            <template slot="detail" slot-scope="detail, record">
                <a @click="materialDetail(record)">查看</a>
            </template>
        </a-table>

        <material-use ref="materialUse"></material-use>
    </j-modal>
</template>

<script>
import { getAction } from '@/api/manage'
import MaterialUse from './MaterialUse'

export default {
    name: 'bhzTable',
    components: {
        MaterialUse
    },
    data() {
        return {
            title: '拌台站生产动态查询',
            visible: false,
            loading: false,
            disableSubmit: true,
            code: '',
            ipagination: {
                current: 1,
                pageSize: 10,
                pageSizeOptions: ['10', '20', '30'],
                showTotal: (total, range) => {
                    return range[0] + '-' + range[1] + ' 共' + total + '条'
                },
                showQuickJumper: true,
                showSizeChanger: true,
                total: 0,
            },
            dataSource: [],
            columns: [
                {
                    title: '序号',
                    dataIndex: '',
                    key: 'rowIndex',
                    width: 60,
                    align: 'center',
                    customRender: function (t, r, index) {
                        return parseInt(index) + 1
                    },
                },
                // {
                //   title:'所属组织机构',
                //   align:"center",
                //   dataIndex: 'sysOrgCode_dictText'
                // },
                // {
                //     title: '设备名称',
                //     align: 'center',
                //     dataIndex: 'shebeiNo_dictText',
                // },
                // {
                //     title: '工程名称',
                //     align: 'center',
                //     dataIndex: 'projectName',
                // },
                // {
                //     title: '配方号/配比单号',
                //     align: 'center',
                //     dataIndex: 'formulaNo',
                //     scopedSlots: { customRender: 'formulaNo' },
                // },
                // {
                //     title: '施工地点',
                //     align: 'center',
                //     dataIndex: 'jobLocation',
                // },
                // {
                //     title: '浇筑部位',
                //     align: 'center',
                //     dataIndex: 'poureLocation',
                // },
                // {
                //     title: '强度等级',
                //     align: 'center',
                //     dataIndex: 'strengthRank',
                // },
                {
                    title: '搅拌时间',
                    align: 'center',
                    dataIndex: 'stirDatetime',
                },
                {
                    title: '方量',
                    align: 'center',
                    dataIndex: 'estimateNumber',
                },
                {
                    title: '车次号',
                    align: 'center',
                    dataIndex: 'carno',
                },
                // {
                //     title: '坍落度',
                //     align: 'center',
                //     dataIndex: 'slump',
                // },
                {
                    title: '出料时间',
                    align: 'center',
                    dataIndex: 'productDatetime',
                },
                {
                    title: '超标等级',
                    align: 'center',
                    dataIndex: 'overLevel_dictText',
                    key: 'overLevel_dictText',
                    scopedSlots: { customRender: 'tags' },
                },
                {
                    title: '操作者',
                    align: 'center',
                    dataIndex: 'handlers',
                },
                {
                    title: '详情',
                    align:"center",
                    dataIndex: 'detail',
                    key: 'detail',
                    scopedSlots: { customRender: 'detail' },
                }
            ],
        }
    },
    methods: {
        getbhzList() {
            this.loading = true
            let params = {
                pageNo: this.ipagination.current,
                pageSize: this.ipagination.pageSize,
                workNo: this.code
            }
            getAction('/hntbhz/bhzCementBase/list', params).then((res) => {
                if (res.success) {
                    this.dataSource = res.result.records
                    if (res.result.total) {
                        this.ipagination.total = res.result.total
                    } else {
                        this.ipagination.total = 0
                    }
                } else {
                    this.$message.warning(res.message)
                }
            })
            .finally(() => {
                this.loading = false
            })
        },
        getbhzList2() {
            this.loading = true
            let params2 = {
                pageNo: this.ipagination.current,
                pageSize: this.ipagination.pageSize,
                workNos: this.code
            }
            getAction('/hntbhz/bhzCementBase/listByWorkno1', params2).then((res) => {
                if (res.success) {
                    this.dataSource = res.result.records
                    if (res.result.total) {
                        this.ipagination.total = res.result.total
                    } else {
                        this.ipagination.total = 0
                    }
                } else {
                    this.$message.warning(res.message)
                }
            })
            .finally(() => {
                this.loading = false
            })
        },
        handleTableChange(pagination) {
            this.ipagination = pagination
            this.getbhzList2()
        },
        materialDetail(record){
            this.$refs.materialUse.getUseList(record.batchNo)
            this.$refs.materialUse.visible = true;
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