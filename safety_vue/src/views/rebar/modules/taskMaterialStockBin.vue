<template>
  <a-modal
    title="检验"
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
      <span slot="bdzj" slot-scope="text, record">
        <a-tag v-if="record.jianyanstate == 0" color="orange">
          未检验
        </a-tag>
        <a-tag v-if="record.jianyanstate == 1" color="green">
          合格
        </a-tag>
        <a-tag v-if="record.jianyanstate == 2" color="red">
          不合格
        </a-tag>
        <a-tag v-if="record.jianyanstate == 3" color="yellow">
          检验中
        </a-tag>
       
      </span>
      <template slot="maozhongt2" slot-scope="maozhongt2, record">
        <a-input-number
          v-if="record.editable"
          style="margin: -5px 0;"
          :min="0"
          :max="queryParam.materialNumbers * 1"
          :value="maozhongt2"
          @change="e => handlerChange(e, record)"
        />
        <template v-else>
          {{ maozhongt2 }}
        </template>
      </template>
      <span slot="choujianstate" slot-scope="text, record">
        <a-tag v-if="record.choujianstate == 0" color="orange">
          未检验
        </a-tag>
        <a-tag v-if="record.choujianstate == 1" color="green">
          合格
        </a-tag>
        <a-tag v-if="record.choujianstate == 2" color="red">
          不合格
        </a-tag>
        <a-tag v-if="record.choujianstate == 3" color="purple">
          检验中
        </a-tag>
      </span>
      <span slot="action" slot-scope="text, record">
        <a @click="handlerSaveNum(record)" v-if="record.editable">保存</a>
        <a @click="handlerEditNum(record)" v-else>编辑</a>
      </span>
    </a-table>
  </a-modal>
</template>

<script>
import '@/assets/less/TableExpand.less'
import { mixinDevice } from '@/utils/mixin'
import { JeecgListMixin } from '@/mixins/JeecgListMixin'
import { addRole, editRole, duplicateCheck } from '@/api/api'
import { deleteAction, postAction, getAction, putAction } from '@/api/manage'
import JselectDqDepart from '@comp/jeecgbiz/JselectDqDepart'
import { max } from 'xe-utils/methods'
export default {
  name: 'taskMaterialStockBin', //任务对话框
  mixins: [JeecgListMixin, mixinDevice],
  components: {
    JselectDqDepart
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
          title: '组织机构',
          align: 'center',
          dataIndex: 'sysOrgCode_dictText'
        },
        {
          title: '检验批次',
          align: 'center',
          dataIndex: 'pici'
        },
        {
          title: '规格类型',
          align: 'center',
          dataIndex: 'guigexinghao'
        },

        {
          title: '进场时间',
          align: 'center',
          dataIndex: 'jinchangshijian'
        },
        {
          title: '材料名称',
          align: 'center',
          dataIndex: 'cailiaono_dictText'
        },
        {
          title: '数量',
          align: 'center',
          dataIndex: 'zjcsl'
        },
        {
          title: '使用量',
          align: 'center',
          dataIndex: 'maozhongt'
        },
        {
          title: '库存',
          align: 'center',
          dataIndex: 'pizhongt'
        },
        {
          title: '自检状态',
          align: 'center',
          dataIndex: 'bdzj',
          scopedSlots: { customRender: 'bdzj' }
        },
        {
          title: '监理抽检',
          align: 'center',
          dataIndex: 'choujianstate',
          scopedSlots: {
            customRender: 'choujianstate'
          }
        },
        // {
        //   title: '报告附件',
        //   align: 'center',
        //   width: 200,
        //   dataIndex: 'baogaofile'
        // },
        {
          title: '本次使用量',
          align: 'center',
          dataIndex: 'maozhongt2',
          scopedSlots: { customRender: 'maozhongt2' }
        },
        {
          title: '操作',
          align: 'center',
          fixed: 'right',
          width: 147,
          scopedSlots: { customRender: 'action' }
        }
      ],
      url: {
        list: '/wztaizhang/wztaizhang/listRebar',
        delete: '/rebarTaskChecklist/rebarTaskChecklist/delete',
        deleteBatch: '/rebarTaskChecklist/rebarTaskChecklist/deleteBatch',
        exportXlsUrl: '/rebarTaskChecklist/rebarTaskChecklist/exportXls',
        importExcelUrl: 'rebarTaskChecklist/rebarTaskChecklist/importExcel'
      },
      queryParam: {
        taskId: 0,
        jianyanstate: 1,
        choujianstate: 1,
      },
      stockBin: [], //料仓
      inspectionLot: [], //检验批
      showTableAip: false //
    }
  },
  created() {
    //备份model原始值
    this.modelDefault = JSON.parse(JSON.stringify(this.model))
    this.getSelectData()
  },
  methods: {
    loadData(arg) {
      if (!this.showTableAip) return
      if (!this.url.list) {
        // this.$message.error("请设置url.list属性!")
        return
      }
      //加载数据 若传入参数1则加载第一页的内容
      if (arg === 1) {
        this.ipagination.current = 1
      }

      var params = this.getQueryParams() //查询条件
      this.loading = true
      getAction(this.url.list, params)
        .then(res => {
          //console.log(res)
          if (res.success) {
            //update-begin---author:zhangyafei    Date:20201118  for：适配不分页的数据列表------------
            this.dataSource = res.result.records.map(x => ({ ...x, maozhongt2: 0, editable: false }))
            if (res.result.total) {
              this.ipagination.total = res.result.total
            } else {
              this.ipagination.total = 0
            }
            //update-end---author:zhangyafei    Date:20201118  for：适配不分页的数据列表------------
          } else {
            this.$message.warning(res.message)
          }
        })
        .finally(() => {
          this.loading = false
        })
    },
    handlerChangeSelect2(value, _, record) {
      this.dataSource.find(x => x.id == record.id).taizhangId = value
    },
    handlerChangeSelect(guid, _, record) {
      getAction('/wztaizhang/wztaizhang/list', { lcbianhao: guid, pageNo: 1, pageSize: 3 }).then(res => {
        this.inspectionLot = res.result.records || []
        this.dataSource.find(x => x.id == record.id).liaocangId = guid
      })
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
    loadDataTable(guid, record) {
      this.queryParam.lcbianhao = guid
      this.queryParam.cailiaono = record.materialId
      this.queryParam.materialNumbers = record.materialNumber
      this.visible = true
      this.loadData(1)
    },
    add() {
      this.edit(this.modelDefault)
    },
    edit(record) {
      this.model = Object.assign({}, record)
      this.queryParam.lcbianhao = record.guid
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
      //dataSource里面的maozhongt2加起来
      let sum = this.dataSource.reduce((pre, cur) => pre + cur.maozhongt2, 0)
      if (sum > this.queryParam.materialNumbers) return this.$message.warning('本次使用量不能大于材料总数量')
      let x = this.dataSource.filter(x => {
        x.maozhongt = x.maozhongt2

        x.sysOrgCode = x.sysOrgCode
        x.inspectionLotNumber = x.pici
        x.guigexinghao = x.guigexinghao
        // 进场时间
        x.chuchangshijian = x.chuchangshijian + ' 00:00:00'
        x.createTime = x.createTime + ' 00:00:00'
        x.jinchangshijian = x.jinchangshijian + ' 00:00:00'
        x.storageName = x.cailiaono_dictText
        //数量
        x.uses = x.maozhongt
        //库存
        //自检状态
        //监理抽检
        return x.maozhongt2 > 0
      })
      this.$emit('getTableData', x, this.queryParam.cailiaono, this.queryParam.lcbianhao)
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
    handlerEditNum(record) {
      const newData = [...this.dataSource]
      let current = newData.find(x => x.id == record.id)
      if (current) {
        current.editable = true
        this.dataSource = newData
      }
    },
    handlerSaveNum(record) {
      const newData = [...this.dataSource]
      let current = newData.find(x => x.id == record.id)
      if (current) {
        delete current.editable
        this.dataSource = newData
      }
    },
    handlerChange(value, record) {
      const newData = [...this.dataSource]
      let current = newData.find(x => x.id == record.id)
      if (current) {
        current.maozhongt2 = value
        this.dataSource = newData
      }
    }
  }
}
</script>

<style scoped></style>
