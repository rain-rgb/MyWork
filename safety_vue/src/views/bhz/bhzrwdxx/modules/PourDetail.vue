<template>
  <j-modal
    centered
    :title="title"
    :width="1200"
    :visible="visible"
    @ok="handleOk"
    :okButtonProps="{ class: { 'jee-hidden': disableSubmit } }"
    @cancel="handleCancel"
    cancelText="关闭"
  >
    <a-form :form="form">
      <j-form-container disabled>
        <a-card title="基础信息" :bordered="false" :headStyle="{ color: '#0785fd' }" :bodyStyle="{ padding: '10' }">
          <a-row>
            <a-col :span="12">
              <a-form-item label="所属机构" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-model="pourForm.sysOrgCode_dictText" disabled></a-input>
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="计划方量" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-model="pourForm.mete" disabled></a-input>
              </a-form-item>
            </a-col>
          </a-row>
          <a-row>
            <a-col :span="12">
              <a-form-item label="开盘日期" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-model="pourForm.begtim" disabled></a-input>
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="任务编号" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-model="pourForm.rwdcode" disabled></a-input>
              </a-form-item>
            </a-col>
          </a-row>
          <a-row>
            <a-col :span="12">
              <a-form-item label="浇筑部位" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-model="pourForm.conspos" disabled></a-input>
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="设计强度" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-model="pourForm.betlev" disabled></a-input>
              </a-form-item>
            </a-col>
          </a-row>
          <a-row>
            <a-col :span="12">
              <a-form-item label="坍落度(mm)/扩展度(mm)" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-model="pourForm.lands" disabled></a-input>
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="工程名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-model="pourForm.projname" disabled></a-input>
              </a-form-item>
            </a-col>
          </a-row>
          <a-row>
            <a-col :span="12">
              <a-form-item label="浇筑方式" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input v-model="pourForm.pour" disabled></a-input>
              </a-form-item>
            </a-col>
            <!-- <a-col :span="12">
                            <a-form-item label="环境等级" :labelCol="labelCol" :wrapperCol="wrapperCol">
                                <a-input v-model="pourForm.value" disabled></a-input>
                            </a-form-item>
                        </a-col> -->
          </a-row>
          <!-- <a-row>
                        <a-col :span="12">
                            <a-form-item label="短信群组" :labelCol="labelCol" :wrapperCol="wrapperCol">
                                <a-input v-model="pourForm.value" disabled></a-input>
                            </a-form-item>
                        </a-col>
                        <a-col :span="12">
                            <a-form-item label="施工班组" :labelCol="labelCol" :wrapperCol="wrapperCol">
                                <a-input v-model="pourForm.value" disabled></a-input>
                            </a-form-item>
                        </a-col>
                    </a-row> -->
        </a-card>
      </j-form-container>
    </a-form>
    <div class="process" v-show="processList.length > 0">
      <a-steps direction="horizontal" :current="current">
        <a-step v-for="(item,index) in processList" :key="index" :title="item.tile">
          <div v-show="item.status == 1" slot="description">
            <p>{{item.person}}</p>
            <p>{{item.time}}</p>
            <p>{{item.content}}</p>
          </div>
        </a-step>
      </a-steps>
    </div>
    <a-card title="配料通知单" :bordered="false" :headStyle="{ color: '#0785fd' }">
      <a-table
        :rowKey="(record, index) => index"
        size="middle"
        bordered
        :columns="columns1"
        :dataSource="dataSource1"
        :pagination="false"
      >
        <template slot="code" slot-scope="code, record">
          <a @click="codeDetail(record)">{{ code }}</a>
        </template>
        <span slot="checkStatus" slot-scope="checkStatus, record">
          <a-tag color="green" v-if="record.checkStatus == 0">审核通过</a-tag>
          <a-tag color="red" v-else-if="record.checkStatus == 1" >审核未通过</a-tag>
          <a-tag color="grey" v-else>未审核</a-tag>
        </span>
      </a-table>
    </a-card>
    <a-card title="执行情况" :bordered="false" :headStyle="{ color: '#0785fd' }">
      <a-table
        :rowKey="(record, index) => index"
        size="middle"
        bordered
        :columns="columns2"
        :dataSource="dataSource2"
        :pagination="false"
      >
        <template slot="tags" slot-scope="tags, record">
          <span>{{ (Number(record.metes) - Number(record.mete)).toFixed(2) }}</span>
        </template>
        <template slot="detail" slot-scope="detail, record">
          <a @click="bhzDetail(record)">查看</a>
        </template>
      </a-table>
    </a-card>
    <a-card title="转移记录" :bordered="false" :headStyle="{ color: '#0785fd' }">
      <a-table
        :rowKey="(record, index) => index"
        size="middle"
        bordered
        :columns="columns3"
        :dataSource="dataSource3"
        :pagination="false"
      ></a-table>
    </a-card>
    <a-card title="修改记录" :bordered="false" :headStyle="{ color: '#0785fd' }">
      <a-table
        :rowKey="(record, index) => index"
        size="middle"
        bordered
        :columns="columns4"
        :dataSource="dataSource4"
        :pagination="false"
      ></a-table>
    </a-card>

    <shigongphb-modal ref="modalForm"></shigongphb-modal>
    <bhz-table ref="bhzModal"></bhz-table>
  </j-modal>
</template>

<script>
import { getAction } from '@/api/manage'
import ShigongphbModal from '@/views/system/modules/ShigongphbModal'
import bhzTable from './bhzTable'

export default {
  name: 'PourDetail',
  components: {
    ShigongphbModal,
    bhzTable,
  },
  data() {
    return {
      pourForm: {},
      current: 0,
      processList: [],
      form: this.$form.createForm(this),
      visible: false,
      disableSubmit: true,
      title: '浇筑令详情',
      labelCol: {
        xs: { span: 24 },
        sm: { span: 6 },
      },
      wrapperCol: {
        xs: { span: 24 },
        sm: { span: 16 },
      },
      dataSource1: [],
      dataSource2: [],
      dataSource3: [],
      dataSource4: [],
      columns1: [
        {
          title: '#',
          dataIndex: '',
          key: 'rowIndex',
          width: 60,
          align: 'center',
          customRender: function (t, r, index) {
            return parseInt(index) + 1
          },
        },
        {
          title: '配料单号',
          align: 'center',
          dataIndex: 'code',
          key: 'code',
          scopedSlots: { customRender: 'code' },
        },
        {
          title: '理论配合比',
          align: 'center',
          dataIndex: 'code1',
          key: 'code1',
        },
        {
          title: '审核状态',
          align: 'center',
          dataIndex: 'checkStatus',
          key: 'checkStatus',
          scopedSlots: { customRender: 'checkStatus' },
        },
        {
          title: '配料方量',
          align: 'center',
          dataIndex: 'mete',
          key: 'mete',
        },
      ],
      columns2: [
        {
          title: '计划方量(m³)',
          align: 'center',
          dataIndex: 'mete',
          key: 'mete',
        },
        {
          title: '设备名称',
          align: 'center',
          dataIndex: 'shebeiNo',
          key: 'shebeiNo',
        },
        {
          title: '已完成方量(m³)',
          align: 'center',
          dataIndex: 'metes',
          key: 'metes',
        },
        {
          title: '盘数量',
          align: 'center',
          dataIndex: 'dishCount',
          key: 'dishCount',
        },
        {
          title: '执行进度',
          align: 'center',
          dataIndex: 'bfb',
          key: 'bfb',
          customRender: (text) => (text ? text + '%' : ''),
        },
        {
          title: '节超(m³)',
          align: 'center',
          dataIndex: 'jcfl',
          key: 'jcfl',
          scopedSlots: { customRender: 'tags' },
        },
        {
          title: '详情',
          align: 'center',
          dataIndex: 'detail',
          key: 'detail',
          scopedSlots: { customRender: 'detail' },
        },
      ],
      columns3: [
        {
          title: '原任务单编号',
          align: 'center',
          dataIndex: 'originalCode',
          key: 'originalCode',
        },
        {
          title: '任务单编号',
          align: 'center',
          dataIndex: 'taskCode',
          key: 'taskCode',
        },
        {
          title: '方量(m³)',
          align: 'center',
          dataIndex: 'mate',
          key: 'mate',
        },
        {
          title: '操作时间',
          align: 'center',
          dataIndex: 'handleTime',
          key: 'handleTime',
        },
        {
          title: '操作者',
          align: 'center',
          dataIndex: 'handler',
          key: 'handler',
        },
        {
          title: '类型',
          align: 'center',
          dataIndex: 'type',
          key: 'type',
        },
        {
          title: '转入类型',
          align: 'center',
          dataIndex: 'intoType',
          key: 'intoType',
        },
      ],
      columns4: [
        {
          title: '任务单编号',
          align: 'center',
          dataIndex: 'taskCode',
          key: 'taskCode',
        },
        {
          title: '操作人',
          align: 'center',
          dataIndex: 'updateBy',
          key: 'updateBy',
        },
        {
          title: '操作时间',
          align: 'center',
          dataIndex: 'updateTime',
          key: 'updateTime',
        },
        {
          title: '操作方式',
          align: 'center',
          dataIndex: 'updateWay',
          key: 'updateWay',
        },
      ],
    }
  },
  created() {},
  methods: {
    getList({ rwdcode, sysOrgCode }) {
      this.dataSource1 = []
      this.dataSource2 = []
      this.dataSource3 = []
      this.dataSource4 = []
      this.getProcess(rwdcode)
      let params = {
        Code: rwdcode,
        orgCode: sysOrgCode,
      }
      //获取基础信息和执行情况
      getAction('/system/bhzrenwudan/getDetail', params).then((res) => {
        if (res.code == 200) {
          this.dataSource1 = res.result.cslist
          this.dataSource2 = res.result.ts
        //   if (res.result.ts) {
        //     let arr = []
        //     arr.push(res.result.ts)
        //     this.dataSource2 = arr
        //   }
        } else {
          this.$message.warning(res.message)
        }
      })
      //获取转移记录
      let params2 = {
        code: rwdcode,
      }
      getAction('/taskTransfer/taskTransfer/getByTaskCode', params2).then((res) => {
        if (res.code == 200) {
          this.dataSource3.push(res.result)
        }
      })
      //获取修改记录
      let params3 = {
        code: rwdcode,
      }
      getAction('/taskUpdate/taskUpdate/queryByTaskCode', params3).then((res) => {
        if (res.code == 200) {
          this.dataSource4.push(res.result)
        }
      })
    },
    codeDetail(record) {
      this.$refs.modalForm.edit(record)
      this.$refs.modalForm.title = '详情'
      this.$refs.modalForm.disableSubmit = true
    },
    bhzDetail() {
      let codeStr = this.dataSource1.map(item => {
        return item.code
      }).join()
      this.$refs.bhzModal.code = codeStr
      this.$refs.bhzModal.visible = true
      this.$nextTick(() => {
        this.$refs.bhzModal.getbhzList2()
      })
    },
    getProcess(rwdcode) {
      this.current = 0
      this.processList = []
      if(rwdcode){
        let params = { code: rwdcode }
        getAction('/sys/systems/api/rwdprocess4', params).then(res => {
          if (res.success) {
            this.processList = res.result.reverse()
            let i = 0
            this.processList.forEach(item => {
              if (item.status == 1) {
                i++
              }
            })
            this.current = i
          } else {
            that.$message.warning(res.message)
          }
        })
      }
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
.process{
  padding: 30px
}

p{
  font-size: 12px;
  margin-bottom: 0;
}
</style>