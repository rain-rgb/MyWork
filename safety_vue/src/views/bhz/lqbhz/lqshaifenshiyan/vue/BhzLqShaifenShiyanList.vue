<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="设备名称">
              <j-search-select-tag placeholder="请选择设备名称" v-model="queryParam.shebeibianhao" :dictOptions="dictOption">
              </j-search-select-tag>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
              <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
              <a-button type="primary" @click="searchReset" icon="reload" style="margin-left: 8px">重置</a-button>
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
      <a-button type="primary" icon="download" @click="handleExportXls('沥青筛分试验数据信息表')">导出</a-button>
      <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl"
                @change="handleImportExcel">
        <a-button type="primary" icon="import">导入</a-button>
      </a-upload>
      <!-- 高级查询区域 -->
      <!--      <j-super-query :fieldList="superFieldList" ref="superQueryModal" @handleSuperQuery="handleSuperQuery"></j-super-query>-->
      <!--      <a-dropdown v-if="selectedRowKeys.length > 0">-->
      <!--        <a-menu slot="overlay">-->
      <!--          <a-menu-item key="1" @click="batchDel"><a-icon type="delete"/>删除</a-menu-item>-->
      <!--        </a-menu>-->
      <!--        <a-button style="margin-left: 8px"> 批量操作 <a-icon type="down" /></a-button>-->
      <!--      </a-dropdown>-->
    </div>

    <!-- table区域-begin -->
    <div>
      <div class="ant-alert ant-alert-info" style="margin-bottom: 16px;">
        <i class="anticon anticon-info-circle ant-alert-icon"></i> 已选择 <a
        style="font-weight: 600">{{ selectedRowKeys.length }}</a>项
        <a style="margin-left: 24px" @click="onClearSelected">清空</a>
      </div>

      <a-table
        ref="table"
        size="middle"
        :scroll="{x:true}"
        bordered
        rowKey="id"
        :columns="columns"
        :dataSource="dataSource"
        :pagination="ipagination"
        :loading="loading"
        :rowSelection="{selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"
        class="j-table-force-nowrap"
        @change="handleTableChange"
        :customRow="handleClick"
        :rowClassName="setRowClassName"
      >

        <template slot="htmlSlot" slot-scope="text">
          <div v-html="text"></div>
        </template>
        <template slot="imgSlot" slot-scope="text">
          <span v-if="!text" style="font-size: 12px;font-style: italic;">无图片</span>
          <img v-else :src="getImgView(text)" height="25px" alt=""
               style="max-width:80px;font-size: 12px;font-style: italic;"/>
        </template>
        <template slot="fileSlot" slot-scope="text">
          <span v-if="!text" style="font-size: 12px;font-style: italic;">无文件</span>
          <a-button
            v-else
            :ghost="true"
            type="primary"
            icon="download"
            size="small"
            @click="downloadFile(text)">
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
              <a-menu-item>
                <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">
                  <a>删除</a>
                </a-popconfirm>
              </a-menu-item>
            </a-menu>
          </a-dropdown>
        </span>

      </a-table>
    </div>

    <bhz-lq-shaifen-shiyan-modal ref="modalForm" @ok="modalFormOk"></bhz-lq-shaifen-shiyan-modal>
  </a-card>
</template>

<script>

import '@/assets/less/TableExpand.less'
import { mixinDevice } from '@/utils/mixin'
import { JeecgListMixin } from '@/mixins/JeecgListMixin'
import BhzLqShaifenShiyanModal from './modules/BhzLqShaifenShiyanModal'
import { usershebeiList } from '@api/api'
import Vue from 'vue'
import { getAction } from '@api/manage'

export default {
  name: 'BhzLqShaifenShiyanList',
  mixins: [JeecgListMixin, mixinDevice],
  components: {
    BhzLqShaifenShiyanModal
  },
  data() {
    return {
      dictOption: [],
      selectValue: '',
      description: '沥青筛分试验数据信息表管理页面',
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
          }
        },
        // {
        //   title: '组织机构',
        //   align: 'center',
        //   dataIndex: 'departid'
        // },
        {
          title: '设备编号',
          align: 'center',
          dataIndex: 'sbjno_dictText'
        },
        {
          title: '施工部位',
          align: 'center',
          dataIndex: 'poureLocation'
        },
        {
          title: '材料名称',
          align: 'center',
          dataIndex: 'cailiaomingcheng_dictText'
        },
        {
          title: '级配标准',
          align: 'center',
          dataIndex: 'jipeibiaozhun_dictText'
        },
        {
          title: '筛孔53',
          align: 'center',
          dataIndex: 'shaikong53'
        },
        {
          title: '筛孔37.5',
          align: 'center',
          dataIndex: 'shaikong375'
        },
        {
          title: '筛孔31.5',
          align: 'center',
          dataIndex: 'shaikong315'
        },
        {
          title: '筛孔26.5',
          align: 'center',
          dataIndex: 'shaikong265'
        },
        {
          title: '筛孔19',
          align: 'center',
          dataIndex: 'shaikong19'
        },
        {
          title: '筛孔16',
          align: 'center',
          dataIndex: 'shaikong16'
        },
        {
          title: '筛孔13.2',
          align: 'center',
          dataIndex: 'shaikong132'
        },
        {
          title: '筛孔9.5',
          align: 'center',
          dataIndex: 'shaikong95'
        },
        {
          title: '筛孔4.75',
          align: 'center',
          dataIndex: 'shaikong475'
        },
        {
          title: '筛孔2.36',
          align: 'center',
          dataIndex: 'shaikong236'
        },
        {
          title: '筛孔1.18',
          align: 'center',
          dataIndex: 'shaikong118'
        },
        {
          title: '筛孔0.6',
          align: 'center',
          dataIndex: 'shaikong06'
        },
        {
          title: '筛孔0.3',
          align: 'center',
          dataIndex: 'shaikong03'
        },
        {
          title: '筛孔0.15',
          align: 'center',
          dataIndex: 'shaikong015'
        },
        {
          title: '筛孔0.075',
          align: 'center',
          dataIndex: 'shaikong0075'
        },
        {
          title: '是否使用',
          align: 'center',
          dataIndex: 'isuse_dictText'
        },
        {
          title: '操作',
          dataIndex: 'action',
          align: 'center',
          fixed: 'right',
          width: 147,
          scopedSlots: { customRender: 'action' }
        }
      ],
      url: {
        list: '/bhzlqshaifenshiyan/bhzLqShaifenShiyan/list',
        delete: '/bhzlqshaifenshiyan/bhzLqShaifenShiyan/delete',
        deleteBatch: '/bhzlqshaifenshiyan/bhzLqShaifenShiyan/deleteBatch',
        exportXlsUrl: '/bhzlqshaifenshiyan/bhzLqShaifenShiyan/exportXls',
        importExcelUrl: 'bhzlqshaifenshiyan/bhzLqShaifenShiyan/importExcel'

      },
      dictOptions: {},
      superFieldList: [],
    }
  },
  created() {
    this.getSuperFieldList()
    this.shebeiList()
  },
  computed: {
    importExcelUrl: function () {
      return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`
    },
  },
  methods: {
    shebeiList: function () {
      var sys_depart_orgcode = Vue.ls.get('SYS_DEPART_ORGCODE')
      usershebeiList({
        sys_depart_orgcode: sys_depart_orgcode,
        sbtypes: '1'
      }).then(res => {
        this.dictOption = []
        let result = res.result
        result.forEach(re => {
          this.dictOption.push({ text: re.sbname, value: re.sbjno })
        })
        //console.log(res)
      })
    },
    initDictConfig() {
    },
    getSuperFieldList() {
      let fieldList = []
      fieldList.push({ type: 'string', value: 'departid', text: '组织机构', dictCode: '' })
      fieldList.push({ type: 'string', value: 'sbjno', text: '设备编号', dictCode: '' })
      fieldList.push({ type: 'string', value: 'poureLocation', text: '施工部位', dictCode: '' })
      fieldList.push({ type: 'string', value: 'cailiaomingcheng', text: '材料名称', dictCode: '' })
      fieldList.push({ type: 'string', value: 'jipeibiaozhun', text: '级配标准', dictCode: '' })
      fieldList.push({ type: 'string', value: 'shaikong53', text: '筛孔53', dictCode: '' })
      fieldList.push({ type: 'string', value: 'shaikong375', text: '筛孔37.5', dictCode: '' })
      fieldList.push({ type: 'string', value: 'shaikong315', text: '筛孔31.5', dictCode: '' })
      fieldList.push({ type: 'string', value: 'shaikong265', text: '筛孔26.5', dictCode: '' })
      fieldList.push({ type: 'string', value: 'shaikong19', text: '筛孔19', dictCode: '' })
      fieldList.push({ type: 'string', value: 'shaikong16', text: '筛孔16', dictCode: '' })
      fieldList.push({ type: 'string', value: 'shaikong132', text: '筛孔13.2', dictCode: '' })
      fieldList.push({ type: 'string', value: 'shaikong95', text: '筛孔9.5', dictCode: '' })
      fieldList.push({ type: 'string', value: 'shaikong475', text: '筛孔4.75', dictCode: '' })
      fieldList.push({ type: 'string', value: 'shaikong236', text: '筛孔2.36', dictCode: '' })
      fieldList.push({ type: 'string', value: 'shaikong118', text: '筛孔1.18', dictCode: '' })
      fieldList.push({ type: 'string', value: 'shaikong06', text: '筛孔0.6', dictCode: '' })
      fieldList.push({ type: 'string', value: 'shaikong03', text: '筛孔0.3', dictCode: '' })
      fieldList.push({ type: 'string', value: 'shaikong015', text: '筛孔0.15', dictCode: '' })
      fieldList.push({ type: 'string', value: 'shaikong0075', text: '筛孔0.075', dictCode: '' })
      fieldList.push({ type: 'int', value: 'isuse', text: '是否使用:0=未使用,1=已使用', dictCode: '' })
      this.superFieldList = fieldList
    }
  }
}
</script>
<style scoped>
@import '~@assets/less/common.less';
</style>