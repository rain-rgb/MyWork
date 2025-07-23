<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="设备名称">
              <j-search-select-tag placeholder="请选择设备名称" v-model="queryParam.shebeino" :dictOptions="dictOption">
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
      <a-button @click="handleAdd" type="primary" icon="plus" v-has="'safetytcarreal:add'">新增</a-button>
      <a-button type="primary" icon="download" v-has="'safetytcarreal:dc'" @click="handleExportXls('车辆门禁考勤实时数据表')">导出
      </a-button>
      <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl"
                @change="handleImportExcel">
        <a-button type="primary" v-has="'safetytcarreal:dr'" icon="import">导入</a-button>
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

        <span slot="tags" slot-scope="tags, record">
          <a-tag color="blue" v-if="record.lorr ===1">左洞</a-tag>
          <a-tag color="blue" v-if="record.lorr ===2">右洞</a-tag>
       </span>

        <span slot="tags1" slot-scope="tags1, record">
          <a-tag color="blue" v-if="record.lorr ===1">进洞</a-tag>
          <a-tag color="blue" v-if="record.lorr ===2">出洞</a-tag>
       </span>

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
          <a v-has="'safetytcarreal:edit'" @click="handleEdit(record)">编辑</a>

          <a-divider type="vertical"/>
          <a-dropdown>
            <a class="ant-dropdown-link">更多 <a-icon type="down"/></a>
            <a-menu slot="overlay">
              <a-menu-item>
                <a @click="handleDetail(record)">详情</a>
              </a-menu-item>
              <a-menu-item v-has="'safetytcarreal:del'">
                <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">
                  <a>删除</a>
                </a-popconfirm>
              </a-menu-item>
            </a-menu>
          </a-dropdown>
        </span>

      </a-table>
    </div>

    <safety-tunnel-car-real-modal ref="modalForm" @ok="modalFormOk"></safety-tunnel-car-real-modal>
  </a-card>
</template>

<script>

import '@/assets/less/TableExpand.less'
import { mixinDevice } from '@/utils/mixin'
import { JeecgListMixin } from '@/mixins/JeecgListMixin'
import SafetyTunnelCarRealModal from './modules/SafetyTunnelCarRealModal'
import { usershebeiList } from '@api/api'
import Vue from 'vue'

export default {
  name: 'SafetyTunnelCarRealList',
  mixins: [JeecgListMixin, mixinDevice],
  components: {
    SafetyTunnelCarRealModal
  },
  data() {
    return {
      dictOption: [],
      description: '车辆门禁考勤实时数据表管理页面',
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
        {
          title: '车牌号',
          align: 'center',
          dataIndex: 'card'
        },
        {
          title: '进/出时间',
          align: 'center',
          dataIndex: 'enterTime'
        },
        // {
        //   title:'出去时间',
        //   align:"center",
        //   dataIndex: 'outTime'
        // },
        {
          title: '设备编号',
          align: 'center',
          dataIndex: 'shebeino_dictText'
        },
        // {
        //   title:'唯一标识',
        //   align:"center",
        //   dataIndex: 'guid'
        // },
        {
          title: '左右隧道标志',
          align: 'center',
          dataIndex: 'lorr',
          scopedSlots: { customRender: 'tags' },
        },
        {
          title: '隧道进出标志',
          align: 'center',
          dataIndex: 'ioflag',
          scopedSlots: { customRender: 'tags1' },
        },
        // {
        //   title:'卡号',
        //   align:"center",
        //   dataIndex: 'tagid'
        // },
        // {
        //   title: '类型',
        //   align: 'center',
        //   dataIndex: 'type'
        // },
        // {
        //   title:'司机',
        //   align:"center",
        //   dataIndex: 'owner'
        // },
        {
          title:'隧道名称',
          align:"center",
          dataIndex: 'tunnelname'
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
        list: '/safetyTunnelCarReal/safetyTunnelCarReal/list',
        delete: '/safetyTunnelCarReal/safetyTunnelCarReal/delete',
        deleteBatch: '/safetyTunnelCarReal/safetyTunnelCarReal/deleteBatch',
        exportXlsUrl: '/safetyTunnelCarReal/safetyTunnelCarReal/exportXls',
        importExcelUrl: 'safetyTunnelCarReal/safetyTunnelCarReal/importExcel',

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
        sbtypes: '45'
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
      fieldList.push({ type: 'string', value: 'card', text: '车牌号', dictCode: '' })
      fieldList.push({ type: 'string', value: 'enterTime', text: '进/出时间', dictCode: '' })
      fieldList.push({ type: 'string', value: 'outTime', text: '出去时间', dictCode: '' })
      fieldList.push({ type: 'string', value: 'shebeino', text: '设备编号', dictCode: '' })
      fieldList.push({ type: 'string', value: 'guid', text: '唯一标识', dictCode: '' })
      fieldList.push({ type: 'int', value: 'lorr', text: '左右隧道标志 1 左洞  2右洞', dictCode: '' })
      fieldList.push({ type: 'int', value: 'ioflag', text: '隧道进出标志 1 进洞  2 出洞', dictCode: '' })
      this.superFieldList = fieldList
    }
  }
}
</script>
<style scoped>
@import '~@assets/less/common.less';
</style>