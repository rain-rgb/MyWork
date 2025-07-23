<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="样品名称">
              <a-input placeholder="请输入样品名称" v-model="queryParam.sampleName"></a-input>
              <!--              <sample-type v-model="queryParam.samplename" placeholder="请选择样品名称" :type="type"></sample-type>-->
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="样品编号">
              <a-input placeholder="请输入样品编号" v-model="queryParam.sampleNo"></a-input>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="使用部位">
              <a-input placeholder="请输入使用部位" v-model="queryParam.sampleGcbw"></a-input>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="试验类型">
              <sample-type
                v-model="queryParam.titCode"
                placeholder="请选择试验类型"
                :type="type"
              ></sample-type>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row :gutter="24">
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="试验状态">
              <j-dict-select-tag
                placeholder="请选择试验状态"
                v-model="stateN"
                @change="onChange"
                dictCode="samplestate"
              ></j-dict-select-tag>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="取样日期">
              <j-date
                placeholder="取样日期"
                v-model="queryParam.sampleDate"
                :showTime="true"
                dateFormat="YYYY-MM-DD"
              />
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="只看自己">
              <a-switch v-model="queryParam.lookself" default-checked/>
            </a-form-item>
          </a-col>

          <a-col :xl="6" :lg="7" :md="8" :sm="24">
                        <span style="float: left; overflow: hidden" class="table-page-search-submitButtons">
                            <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
                          <!--              <a-button type="primary" @click="searchReset" icon="reload" style="margin-left: 8px">重置</a-button>-->
                            <a @click="handleToggleSearch" style="margin-left: 8px">
                                {{ toggleSearchStatus ? '收起' : '展开' }}
                                <a-icon :type="toggleSearchStatus ? 'up' : 'down'"/>
                            </a>
                        </span>
          </a-col>
        </a-row>
      </a-form>
    </div>
    <!-- 查询区域-END -->

    <!-- 操作按钮区域 -->
    <div class="table-operator">
      <a-button @click="handleAdd" type="primary" icon="plus">新增</a-button>
      <a-button @click="copyData" type="primary" icon="snippets">复制</a-button>
      <a-button @click="insertData" type="primary" icon="arrow-down">插入</a-button>
      <a-button @click="commitShenhe" type="primary" icon="arrow-up">提交审核</a-button>
      <a-button @click="updateToSampleno" type="primary" icon="carry-out">修改编号</a-button>
      <a-button @click="del" type="primary" icon="delete">删除</a-button>
    </div>

    <!-- table区域-begin -->
    <div>
      <div class="ant-alert ant-alert-info" style="margin-bottom: 16px">
        <i class="anticon anticon-info-circle ant-alert-icon"></i> 已选择
        <a style="font-weight: 600">{{ selectedRowKeys.length }}</a
        >项
        <a style="margin-left: 24px" @click="onClearSelected">清空</a>
      </div>

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
        :rowSelection="{ selectedRowKeys: selectedRowKeys, onChange: onSelectChange }"
        class="j-table-force-nowrap"
        @change="handleTableChange"
      >

      <span slot="tags" slot-scope="tags, record">
          <a-tag v-if="record.sampleState == 0 && record.shenpizhuangtai == null && record.qiangzhangzhuangtai == null">待检</a-tag>
          <a-tag v-if="record.sampleState == 1 && record.shenpizhuangtai == null && record.qiangzhangzhuangtai == null">在检</a-tag>
          <a-tag v-if="record.sampleState == 2 && record.shenpizhuangtai == 1 && record.qiangzhangzhuangtai == null">待审批</a-tag>
          <a-tag v-if="record.sampleState == 2 && record.shenpizhuangtai == 2 && record.qiangzhangzhuangtai == null">签章中</a-tag>
          <a-tag v-if="record.sampleState == 2 && record.shenpizhuangtai == null && record.qiangzhangzhuangtai == 1">签章完成</a-tag>
          <a-tag v-if="record.sampleState == 1 && record.shenpizhuangtai == 3 && record.qiangzhangzhuangtai == null">签章退回</a-tag>
      </span>

        <template slot="sampleno" slot-scope="sampleno, record">
          <a @click="rePrint(record)">{{ sampleno }}</a>
        </template>

        <template slot="htmlSlot" slot-scope="text">
          <div v-html="text"></div>
        </template>
        <template slot="imgSlot" slot-scope="text">
          <span v-if="!text" style="font-size: 12px; font-style: italic">无图片</span>
          <img
            v-else
            :src="getImgView(text)"
            height="25px"
            alt=""
            style="max-width: 80px; font-size: 12px; font-style: italic"
          />
        </template>
        <template slot="fileSlot" slot-scope="text">
          <span v-if="!text" style="font-size: 12px; font-style: italic">无文件</span>
          <a-button
            v-else
            :ghost="true"
            type="primary"
            icon="download"
            size="small"
            @click="downloadFile(text)"
          >
            下载
          </a-button>
        </template>

        <span slot="action" slot-scope="text, record">
                    <a @click="handleEdit(record)">编辑</a>

                    <a-divider type="vertical"/>
                    <a-dropdown>
                        <a class="ant-dropdown-link">更多 <a-icon type="down"/></a>
                        <a-menu slot="overlay">
                            <a-menu-item>
                                <a @click="handleDetail(record)">详情</a>
                            </a-menu-item>
<!--                            <a-menu-item>-->
<!--                                <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">-->
<!--                                    <a>删除</a>-->
<!--                                </a-popconfirm>-->
<!--                            </a-menu-item>-->
                        </a-menu>
                    </a-dropdown>
                </span>
      </a-table>
    </div>

    <sy-dps-sy-sample-modal ref="modalForm" @ok="modalFormOk"></sy-dps-sy-sample-modal>
    <sample-copy-date ref="sample"></sample-copy-date>
    <report-form ref="reform"></report-form>
    <sample-update-sampleno ref="updateSampleno"></sample-update-sampleno>
    <insert-data ref="insertData" @ok="modalFormOk"></insert-data>
    <sample-delete ref="delSample"></sample-delete>
  </a-card>
</template>

<script>
import '@/assets/less/TableExpand.less'
import { mixinDevice } from '@/utils/mixin'
import { JeecgListMixin } from '@/mixins/JeecgListMixin'
import SyDpsSySampleModal from './modules/SyDpsSySampleModal'
import SampleCopyDate from './modules/SampleCopyDate'
import SampleType from '@/views/sy/dpssysample/modules/SampleType'
import ReportForm from '@/views/syrj/reportform/ReportForm'
import InsertData from '@/views/sy/dpssysample/modules/InsertData'
import { getAction } from '@api/manage'
import SampleUpdateSampleno from '@views/sy/dpssysample/modules/SampleUpdateSampleno'
import SampleDelete from '@views/sy/dpssysample/modules/SampleDelete'

export default {
  name: 'SyDpsSySampleList',
  mixins: [JeecgListMixin, mixinDevice],
  components: {
    SampleUpdateSampleno,
    SyDpsSySampleModal,
    SampleType,
    SampleCopyDate,
    ReportForm,
    InsertData,
    SampleDelete
  },
  data() {
    return {
      description: 'sy_dps_sy_sample管理页面',
      type: 0,
      // 表头
      columns: [
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
          title: '样品编号',
          align: 'center',
          dataIndex: 'sampleNo',
          scopedSlots: { customRender: 'sampleno' },
        },
        {
          title: '样品名称',
          align: 'center',
          dataIndex: 'sampleName',
        },
        {
          title: '取样地点',
          align: 'center',
          dataIndex: 'sampleQuYangDiDian',
        },
        {
          title: '使用部位',
          align: 'center',
          dataIndex: 'sampleGcbw',
        },
        {
          title: '取样日期',
          align: 'center',
          dataIndex: 'sampleDate',
        },
        {
          title: '取样人',
          align: 'center',
          dataIndex: 'sampleQuYangRen',
        },
        {
          title: '试验状态',
          align: 'center',
          dataIndex: 'sampleState_dictText',
          scopedSlots: { customRender: 'tags' },
        },
        {
          title: '报告编号',
          align: 'center',
          dataIndex: 'reportNoNew',
        },
        {
          title: '试验日期',
          align: 'center',
          dataIndex: 'reportCreateDate',
        },
        {
          title: '编辑人',
          align: 'center',
          dataIndex: 'sampleCreatePerson',
        },
        {
          title: '操作',
          dataIndex: 'action',
          align: 'center',
          fixed: 'right',
          width: 147,
          scopedSlots: { customRender: 'action' },
        },
      ],
      url: {
        list: '/sydpssysample/syDpsSySample/grid',
        delete: '/sydpssysample/syDpsSySample/delete',
        deleteBatch: '/sydpssysample/syDpsSySample/deleteBatch',
        exportXlsUrl: '/sydpssysample/syDpsSySample/exportXls',
        importExcelUrl: 'sydpssysample/syDpsSySample/importExcel',
      },
      queryParam: {
        titType: 0,
        sampleState: '',
        shenpizhuangtai: '',
        qiangzhangzhuangtai: '',
      },
      stateN: undefined,
      dictOptions: {},
    }
  },
  created() {
  },
  computed: {
    importExcelUrl: function () {
      return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`
    },
  },
  methods: {
    onChange(val) {
      this.queryParam.sampleState = val == 0 ? 0 : val == 1 || val == 5 ? 1 : 2
      this.queryParam.shenpizhuangtai = val == 2 ? 1 : val == 3 ? 2 : val == 5 ? 3 : ''
      this.queryParam.qiangzhangzhuangtai = val == 4 ? 1 : ''
    },
    copyData() {
      if (this.selectionRows.length == 1) {
        this.$refs.sample.sample = this.selectionRows[0]
        this.$refs.sample.id = this.selectionRows[0].id
        this.$refs.sample.visible = true
      } else {
        this.$message.error('请选择单行数据复制！')
      }
    },
    insertData() {
      if (this.selectionRows.length == 1) {
        this.$refs.insertData.edit(this.selectionRows[0])
      } else {
        this.$message.error('请选择单行!')
      }
    },
    updateToSampleno() {
      if (this.selectionRows.length == 1) {
        this.$refs.updateSampleno.sample = this.selectionRows[0]
        this.$refs.updateSampleno.visible = true
      } else {
        this.$message.error('请选择单行数据!')
      }
    },
    commitShenhe() {
      if (this.selectionRows.length == 1) {
        let url = '/sydpssysample/syDpsSySample/over/' + this.selectionRows[0].id
        getAction(url).then((res) => {
          if (res.success) {
            this.$message.success(res.message)
          } else {
            this.$message.error(res.message)
          }
        })
      } else {
        this.$message.error('请选择单行数据!')
      }
    },
    del() {
      if (this.selectionRows.length == 1) {
        this.$refs.delSample.sample = this.selectionRows[0]
        this.$refs.delSample.visible = true
      } else {
        this.$message.error('请选择单行数据进行删除!')
      }
    },
    initDictConfig() {
    },
    rePrint({ id }) {
      this.$refs.reform.show(id)
    },
  },
}
</script>
<style scoped>
@import '~@assets/less/common.less';
</style>