<template>
  <a-modal
    title="领料"
    :width="1200"
    :visible="visible"
    :confirmLoading="confirmLoading"
    @ok="handleOk"
    @cancel="handleCancel"
    cancelText="关闭"
    wrapClassName="ant-modal-cust-warp"
    style="top:5%;height: 85%;overflow-y: hidden"
  >
    <a-table
      ref="table"
      size="middle"
      :scroll="{ x: true }"
      bordered
      rowKey="id"
      :columns="columns"
      :dataSource="dataSource"
      :pagination="ipagination"
      :loading="loading"
      class="j-table-force-nowrap"
      @change="handleTableChange"
    >
      <template slot="htmlSlot" slot-scope="text">
        <div v-html="text"></div>
      </template>
      <template slot="imgSlot" slot-scope="text">
        <span v-if="!text" style="font-size: 12px;font-style: italic;">无图片</span>
        <img
          v-else
          :src="getImgView(text)"
          height="25px"
          alt=""
          style="max-width:80px;font-size: 12px;font-style: italic;"
        />
      </template>
      <template slot="fileSlot" slot-scope="text">
        <span v-if="!text" style="font-size: 12px;font-style: italic;">无文件</span>
        <a-button v-else :ghost="true" type="primary" icon="download" size="small" @click="downloadFile(text)">
          下载
        </a-button>
      </template>
      <!--  -->
      <template slot="liaocang" slot-scope="liaocang, record">
        <a-select
          placeholder="请选择料仓"
          show-search
          allowClear
          option-filter-prop="children"
          @change="(value, option) => handlerChangeSelect(value, option, record)"
          style="width: 100%;"
        >
          <a-select-option :value="undefined">请选择</a-select-option>
          <a-select-option v-for="item in stockBin" :key="`${item.guid}`">
            {{ item.name }}
          </a-select-option>
        </a-select>
      </template>
      <template slot="lingyongmaterialNumber" slot-scope="text, record">
        <span v-if="!record.lingyongmaterialNumber">{{ 0 }}</span>
        <span v-else>{{ record.lingyongmaterialNumber }}</span>
      </template>
      <template slot="pici" slot-scope="pici, record">
        <span v-if="!record.pici">未选择</span>
        <span v-else>{{ pici }}</span>
      </template>
      <!--  -->
    </a-table>
    <taskMaterialStockBin ref="taskMaterialStockBin" @getTableData="getTableData" />
  </a-modal>
</template>

<script>
import '@/assets/less/TableExpand.less'
import { mixinDevice } from '@/utils/mixin'
import { JeecgListMixin } from '@/mixins/JeecgListMixin'
import { addRole, editRole, duplicateCheck } from '@/api/api'
import { deleteAction, postAction, getAction, putAction } from '@/api/manage'
import JselectDqDepart from '@comp/jeecgbiz/JselectDqDepart'
import taskMaterialStockBin from './taskMaterialStockBin.vue'
export default {
  name: 'taskMaterialGet', //任务对话框
  mixins: [JeecgListMixin, mixinDevice],
  components: {
    JselectDqDepart,
    taskMaterialStockBin
  },
  props: ['dataSourceParaent', 'disableSubmitButton'],
  data() {
    return {
      title: '操作',
      visible: false,
      roleDisabled: false,
      disableSubmit: false,
      model: {},
      layout: {
        labelCol: { span: 3 },
        wrapperCol: { span: 14 }
      },
      confirmLoading: false,
      validatorRules: {
        roleName: [
          { required: true, message: '请输入角色名称!' }
          // { min: 2, max: 30, message: '长度在 2 到 30 个字符', trigger: 'blur' }
        ],
        roleCode: [
          { required: true, message: '请输入角色名称!' }
          // { min: 0, max: 64, message: '长度不超过 64 个字符', trigger: 'blur' },
          // { validator: this.validateRoleCode}
        ],
        description: [
          // { min: 0, max: 126, message: '长度不超过 126 个字符', trigger: 'blur' }
        ]
      },
      dataSource: [],
      // 表头
      columns: [
        {
          title: '序号',
          dataIndex: '',
          key: 'rowIndex',
          width: 60,
          align: 'center',
          customRender: function(t, r, index) {
            return parseInt(index) + 1
          }
        },
        {
          title: '材料编号',
          align: 'center',
          dataIndex: 'materialId'
        },

        {
          title: '材料名称',
          align: 'center',
          dataIndex: 'materialName'
        },

        {
          title: '材料型号',
          align: 'center',
          dataIndex: 'materialType'
        },
        {
          title: '数量',
          align: 'center',
          dataIndex: 'materialNumber'
        },
        {
          title: '已领用数量',
          align: 'center',
          dataIndex: 'lingyongmaterialNumber',
          scopedSlots: {
            customRender: 'lingyongmaterialNumber'
          }
        },
        {
          title: '料仓',
          align: 'center',
          width: 200,
          dataIndex: 'liaocang',
          scopedSlots: {
            customRender: 'liaocang'
          }
        },
        {
          title: '检验批次',
          align: 'center',
          width: 200,
          dataIndex: 'pici',
          scopedSlots: {
            customRender: 'pici'
          }
        }
      ],
      url: {
        list: '/rebarTaskChecklist/rebarTaskChecklist/queryMaterialByTask',
        delete: '/rebarTaskChecklist/rebarTaskChecklist/delete',
        deleteBatch: '/rebarTaskChecklist/rebarTaskChecklist/deleteBatch',
        exportXlsUrl: '/rebarTaskChecklist/rebarTaskChecklist/exportXls',
        importExcelUrl: 'rebarTaskChecklist/rebarTaskChecklist/importExcel'
      },
      queryParam: {
        taskId: 0
      },
      stockBin: [], //料仓
      inspectionLot: [], //检验批
      dialogTableDatas: []
    }
  },
  created() {
    //备份model原始值
    this.modelDefault = JSON.parse(JSON.stringify(this.model))
    this.getSelectData()
  },
  methods: {
    handlerChangeSelect2(value, _, record) {
      this.dataSource.find(x => x.id == record.id).taizhangId = value
    },
    handlerChangeSelect(guid, option, record) {
      this.$refs.taskMaterialStockBin.showTableAip = true
      this.$refs.taskMaterialStockBin.loadDataTable(guid, record)
      // getAction('/wztaizhang/wztaizhang/list', { lcbianhao: guid, pageNo: 1, pageSize: 3 }).then(res => {
      //   // this.inspectionLot = res.result.records || []
      //   // this.dataSource.find(x => x.id == record.id).liaocangId = guid
      // })
    },
    getSelectData() {
      getAction('/wzliaocang/wzliaocang/list', {
        cailiaono: 12,
        sysOrgCode: this.$store.state.user.info.orgCode,
        pageNo: 1,
        pageSize: 999
      }).then(res => {
        this.stockBin = res.result.records || []
      })
    },
    loadDataTable(record) {
      this.queryParam.taskId = record.taskId
      this.loadData(1)
    },
    add() {
      this.edit(this.modelDefault)
    },
    edit(record) {
      this.model = Object.assign({}, record)
      this.queryParam.taskId = record.taskId
      this.visible = true
      //编辑页面禁止修改角色编码
      if (this.model.id) {
        this.roleDisabled = true
      } else {
        this.roleDisabled = false
      }
    },
    close() {
      // this.$refs.form.clearValidate();
      // this.$emit('close');
      this.visible = false
      // this.dataSource=[]
    },
    async handleOk() {
      const res = await postAction(
        '/rebarTaskMaterialLiaocangTaizhang/rebarTaskMaterialLiaocangTaizhang/adds',
        this.dataSource
      )
      this.$emit('ok')
      this.visible = false
    },
    handleCancel() {
      this.close()
    },
    validateRoleCode(rule, value, callback) {
      if (/[\u4E00-\u9FA5]/g.test(value)) {
        callback('角色编码不可输入汉字!')
      } else {
        let params = {
          tableName: 'sys_role',
          fieldName: 'role_code',
          fieldVal: value,
          dataId: this.model.id
        }
        duplicateCheck(params).then(res => {
          if (res.success) {
            callback()
          } else {
            callback(res.message)
          }
        })
      }
    },
    getTableData(dialogTableDatas, materialId, guid) {
      let current = this.dataSource.find(x => x.materialId == materialId)
      current.yclUsageDetail = dialogTableDatas
      current.lingyongmaterialNumber = dialogTableDatas.reduce((pre, cur) => pre + cur.maozhongt2, 0)
      current.pici = dialogTableDatas.reduce((pre, cur) => pre + cur.pici + ',', '')
      current.liaocangId = guid
      // let currentBin= this.stockBin.find(x=>x.guid==guid)
      current.code = ''
      this.$forceUpdate()
    }
  }
}
</script>

<style scoped></style>
