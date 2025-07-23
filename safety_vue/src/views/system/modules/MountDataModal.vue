<template>
    <j-modal
        centered
        :title="title"
        :width="1200"
        :visible="visible"
        @ok="handleOk"
        @cancel="handleCancel"
        cancelText="关闭"
        okText="挂载配料单"
    >

        <a-form layout="inline">

<!--            <a-divider type="horizontal" ><span style="color:#0785fd">施工方原因说明</span></a-divider>-->
            <a-row :gutter="[0, 24]">
             <a-col :span="24">
              <a-form-item label="说明原因" >
                <a-textarea placeholder="请填入说明原因" v-model="modelData.note":auto-size="{ minRows: 3, maxRows:6}"  style="width: 400px" ></a-textarea>
                <!-- <a-input v-model="bhzCementOverHandler.problemReasons" placeholder="无"></a-input> -->
              </a-form-item>

             </a-col>
            </a-row>

          <a-divider type="horizontal" ><span style="color:#0785fd">未用任务单数据选择</span></a-divider>

            <a-row :gutter="[0, 24]">
                <a-col :span="7">
                    <a-form-item label="浇筑部位">
                        <a-input placeholder="请输入浇筑部位" v-model="poureLocation" allowClear></a-input>
                    </a-form-item>
                </a-col>
                <a-col :span="7">
                    <a-form-item label="出料时间范围">
                        <j-date
                            placeholder="开始出料时间"
                            v-model="startTime"
                            :showTime="true"
                            dateFormat="YYYY-MM-DD HH:mm:ss"
                        />
                    </a-form-item>
                </a-col>
                <a-col :span="5">
                    <a-form-item>
                        <j-date
                            placeholder="结束出料时间"
                            v-model="endTime"
                            :showTime="true"
                            dateFormat="YYYY-MM-DD HH:mm:ss"
                        />
                    </a-form-item>
                </a-col>
                <a-col :span="4">
                    <a-form-item>
                        <a-button type="primary" @click="search">搜索</a-button>
                    </a-form-item>
                </a-col>
            </a-row>
            <a-table
                ref="table"
                size="middle"
                bordered
                rowKey="id"
                class="j-table-force-nowrap"
                :scroll="{ x: true }"
                :columns="columns"
                :dataSource="dataSource"
                :pagination="ipagination"
                :rowSelection="{ selectedRowKeys: selectedRowKeys, onChange: onSelectChange }"
                :loading="loading"
                @change="tableChange"
            >
                <span slot="stirDatetime" slot-scope="stirDatetime, record">
                    <a-tag color="green">{{ record.stirDatetime }}</a-tag>
                </span>
         <span slot="isjzl" slot-scope="isjzl, record">
          <a-tag color="green" v-if="record.isjzl == 1 ">已使用</a-tag>
          <a-tag color="grey" v-else-if="record.isjzl == 0 ">数据关联中</a-tag>
          <a-tag color="blue" v-else-if="record.isjzl == 28">已关联</a-tag>
          <a-tag color="blue" v-else-if="record.isjzl == 29">关联通过</a-tag>
          <a-tag color="orange" v-else-if="record.isjzl == 40">使用异常</a-tag>
          <a-tag color="red" v-else>未使用</a-tag>
        </span>
            </a-table>
        </a-form>
    </j-modal>
</template>

<script>
import { getAction, postAction } from '@/api/manage'

export default {
    name: 'MountDataModal',
    components: {},
    data() {
        return {
          labelCol: {
            xs: { span: 24 },
            sm: { span: 12 },
          },
          wrapperCol: {
            xs: { span: 24 },
            sm: { span: 16 },
          },
            dataSource: [],
            selectedRowKeys: [],
            selectionRows: [],
            startTime: '',
            endTime: '',
            poureLocation: '',
            modelData: {},
            loading: false,
            visible: false,
            title: '挂载数据',
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
                {
                    title: '设备名称',
                    align: 'center',
                    dataIndex: 'shebeiNo_dictText',
                },
                {
                    title: '浇筑部位',
                    align: 'center',
                    dataIndex: 'poureLocation',
                },
                {
                    title: '实际拌合时间(秒)',
                    align: 'center',
                    dataIndex: 'stirDatetime',
                    key: 'stirDatetime',
                    scopedSlots: { customRender: 'stirDatetime' },
                },
                {
                    title: '生产方量',
                    align: 'center',
                    dataIndex: 'estimateNumber',
                },
                {
                    title: '强度等级',
                    align: 'center',
                    dataIndex: 'strengthRank',
                },
                {
                    title: '出料时间',
                    align: 'center',
                    dataIndex: 'productDatetime',
                },
              {
                title: '浇筑令',
                align: 'center',
                dataIndex: 'isjzl',
                key: 'isjzl',
                scopedSlots: { customRender: 'isjzl' },
              },
            ],
            url: {
              //  bhTimeList: '/hntbhz/bhzCementBase/bhTimeList',
              bhTimeList: '/hntbhz/bhzCementBase/listrc3',
                updateBatch: '/hntbhz/bhzCementBase/updateBatch',
            },
        }
    },
    created() {},
    methods: {
        getListXq() {
            this.loading = true
            let params = {
                pageNo: this.ipagination.current,
                pageSize: this.ipagination.pageSize,
                id_begin:8082238,
                column: 'id',
                order: 'desc',
                renwudanstatus_begin:10,
                renwudanstatus_end:30,
                productDatetime_begin: this.startTime,
                productDatetime_end: this.endTime,
                poureLocation: this.poureLocation,
                shebeiNo: this.modelData.shebeibianhao,
               // superQueryParams: encodeURI(JSON.stringify([{"rule":"ne","type":"string","dictCode":"","val":"50","field":"renwudanstatus"}])),
               // superQueryMatchType: 'and',
            }
            getAction(this.url.bhTimeList, params)
                .then((res) => {
                    if (res.success) {
                        this.dataSource = res.result.records
                        if (res.result.total) {
                            this.ipagination.total = res.result.total
                        } else {
                            this.ipagination.total = 0
                        }
                    }
                })
                .finally(() => {
                    this.loading = false
                })
        },
        getList() {
            this.startTime = ''
            this.endTime = ''
            this.poureLocation = ''
            this.ipagination.current = 1
            this.ipagination.pageSize = 10
            this.selectedRowKeys = []
            this.selectionRows = []
            this.getListXq()
            this.visible = true
        },
        onSelectChange(selectedRowKeys, selectionRows) {
            this.selectedRowKeys = selectedRowKeys
            this.selectionRows = selectionRows
        },
        tableChange(pagination) {
            this.ipagination.current = pagination.current
            this.ipagination.pageSize = pagination.pageSize
            this.getListXq()
        },
        search() {
            this.ipagination.current = 1
            this.ipagination.pageSize = 10
            this.getListXq()
        },
        handleCancel() {
            this.visible = false
        },
        handleOk() {
            if (this.selectionRows.length > 0) {
                let ids = []
                this.selectionRows.forEach((item) => {
                    ids.push({
                        id: item.id,
                        estimateNumber: item.estimateNumber,
                    })
                })
                let params = {
                    ids: ids,
                    phbcode: this.modelData.code,
                    rwdcode: this.modelData.renwudan,
                    station: this.modelData.station,
                    sbjno: this.modelData.shebeibianhao,
                    note:this.modelData.note

                }
                if(!this.modelData.note){
                  this.$message.warning('请填写说明后再关联配料单！')

                }else{
                  postAction(this.url.updateBatch, params).then((res) => {
                    if (res.success) {
                      this.$message.success('保存成功！')
                      this.visible = false
                    } else {
                      this.$message.error('保存失败！')
                    }
                  })
                }

            } else {
                this.$message.warning('请选择数据后保存！')
            }
        },
    },
}
</script>
<style scoped>
</style>