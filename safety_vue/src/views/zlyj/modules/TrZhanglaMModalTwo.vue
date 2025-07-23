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
          :data-source="data"
          bordered
          size="middle"
          class="j-table-force-nowrap"
          :rowClassName="setClass"
        >
        </a-table>
      </a-tab-pane>
    </a-tabs>

    <g-ch-modal ref="gch"></g-ch-modal>
  </j-modal>
</template>

<script>
import { getAction } from '@api/manage'
import GChModal from './GChModal.vue'

export default {
  name: 'TrZhanglaMModal',
  components: {
    GChModal,
  },
  data() {
    return {
      data: [],
      datas: [],
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
          title: '张拉百分比(%)',
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
      loading: false,
      disableSubmit: true,
      syjid: '',
      url: {
        list1s: '/zhanglam/trZhanglaM/list1s',
      },
    }
  },
  methods: {
    zhanglamessage: function () {
      //请求张拉每个孔的断面数据
      let param = { syjid: this.syjid }
      getAction(this.url.list1s, param).then((res) => {
        if (res.success) {
          this.data = res.result
        }else{
          this.data = []
        }
      }).finally(() => {
        this.loading = false
      })
    },
    handleOk() {
      this.visible = false
    },
    call(e) {
      this.syjid = e
      this.visible = true
      this.loading = true
      this.zhanglamessage()
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
      return (record.overLevel == null || record.overLevel == 0) ? '' : 'rowColor'
    },
    gchDetail(record) {
      this.$refs.gch.getList(record);
    },
  },
}
</script>

<style scoped>
::v-deep .rowColor{
  color: red;
}
</style>