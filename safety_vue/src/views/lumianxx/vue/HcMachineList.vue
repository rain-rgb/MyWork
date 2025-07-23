<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form @keyup.enter.native="searchQuery" layout="inline">
        <a-row :gutter="24">
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="机械类型">
              <j-search-select-tag placeholder="请选择机械类型" v-model="queryParam.machinetypecode" :dictOptions="dictOption" >
              </j-search-select-tag>
              {{ selectValue }}
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
              <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
              <a-button type="primary" @click="searchReset" icon="reload" style="margin-left: 8px">重置</a-button>
<!--              <a @click="handleToggleSearch" style="margin-left: 8px">-->
<!--                {{ toggleSearchStatus ? '收起' : '展开' }}-->
<!--                <a-icon :type="toggleSearchStatus ? 'up' : 'down'"/>-->
<!--              </a>-->
            </span>
          </a-col>
        </a-row>
      </a-form>
    </div>
    <!-- 查询区域-END -->

    <!-- 操作按钮区域 -->
    <!--    <div class="table-operator">-->
    <!--      <a-button @click="handleAdd" icon="plus" type="primary">新增</a-button>-->
    <!--      <a-button @click="handleExportXls('机械设备')" icon="download" type="primary">导出</a-button>-->
    <!--      <a-upload :action="importExcelUrl" :headers="tokenHeader" :multiple="false" :showUploadList="false" @change="handleImportExcel" name="file">-->
    <!--        <a-button icon="import" type="primary">导入</a-button>-->
    <!--      </a-upload>-->
    <!-- 高级查询区域 -->
    <!--      <j-super-query :fieldList="superFieldList" @handleSuperQuery="handleSuperQuery" ref="superQueryModal"></j-super-query>-->
    <!--      <a-dropdown v-if="selectedRowKeys.length > 0">-->
    <!--        <a-menu slot="overlay">-->
    <!--          <a-menu-item @click="batchDel" key="1"><a-icon type="delete"/>删除</a-menu-item>-->
    <!--        </a-menu>-->
    <!--        <a-button style="margin-left: 8px"> 批量操作 <a-icon type="down" /></a-button>-->
    <!--      </a-dropdown>-->
    <!--    </div>-->

    <!-- table区域-begin -->
    <div>
      <div class="ant-alert ant-alert-info" style="margin-bottom: 16px;">
        <i class="anticon anticon-info-circle ant-alert-icon"></i> 已选择 <a
        style="font-weight: 600">{{ selectedRowKeys.length }}</a>项
        <a @click="onClearSelected" style="margin-left: 24px">清空</a>
      </div>

      <a-table
        :columns="columns"
        :dataSource="dataSource"
        :loading="loading"
        :pagination="ipagination"
        :rowSelection="{selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"
        :scroll="{x:true}"
        @change="handleTableChange"
        bordered
        class="j-table-force-nowrap"
        ref="table"
        rowKey="id"
        size="middle">
        <span slot="tags" slot-scope="tags, record">
          <a-tag color="red" v-if="record.iscall == '0'">报警</a-tag>
          <a-tag color="green" v-if="record.iscall == '1'">不报警</a-tag>
        </span>
        <template slot="htmlSlot" slot-scope="text">
          <div v-html="text"></div>
        </template>
        <template slot="imgSlot" slot-scope="text">
          <span style="font-size: 12px;font-style: italic;" v-if="!text">无图片</span>
          <img :src="getImgView(text)" alt="" height="25px" style="max-width:80px;font-size: 12px;font-style: italic;"
               v-else/>
        </template>
        <template slot="fileSlot" slot-scope="text">
          <span style="font-size: 12px;font-style: italic;" v-if="!text">无文件</span>
          <a-button
            :ghost="true"
            @click="downloadFile(text)"
            icon="download"
            size="small"
            type="primary"
            v-else>
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
                <a-popconfirm @confirm="() => handleDelete(record.id)" title="确定删除吗?">
                  <a>删除</a>
                </a-popconfirm>
              </a-menu-item>
            </a-menu>
          </a-dropdown>
        </span>

      </a-table>
    </div>

    <hc-machine-modal @ok="modalFormOk" ref="modalForm"></hc-machine-modal>
  </a-card>
</template>

<script>

import '@/assets/less/TableExpand.less'
import { mixinDevice } from '@/utils/mixin'
import { JeecgListMixin } from '@/mixins/JeecgListMixin'
import HcMachineModal from './modules/HcMachineModal'

export default {
  name: 'HcMachineList',
  mixins: [JeecgListMixin, mixinDevice],
  components: {
    HcMachineModal
  },
  data() {
    return {
      selectValue: '',
      description: '机械设备管理页面',
      dictOption: [{
        text: '震动碾',
        value: '0'
      }, {
        text: '双钢轮',
        value: '3'
      }, {
        text: '胶轮',
        value: '4'
      }, {
        text: '沥青摊铺机',
        value: '5'
      }, {
        text: '羊足碾',
        value: '10'
      }, {
        text: '单钢轮',
        value: '11'
      }, {
        text: '水温摊铺机',
        value: '12'
      }],
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
        //   title: '机械id',
        //   align: 'center',
        //   dataIndex: 'machineid'
        // },
        // {
        //   title: '终端sn号',
        //   align: 'center',
        //   dataIndex: 'terminalsncode'
        // },
        {
          title: '机械宽度(m)',
          align: 'center',
          dataIndex: 'machinesize'
        },
        {
          title: '机械名称',
          align: 'center',
          dataIndex: 'machinename'
        },
        {
          title: '机械类型',
          align: 'center',
          dataIndex: 'machinetypename'
        },
        {
          title: '开始使用时间',
          align: 'center',
          dataIndex: 'tmstarttime',
          customRender: function (text) {
            return !text ? '' : (text.length > 10 ? text.substr(0, 10) : text)
          }
        },
        {
          title: '天线高度(m)',
          align: 'center',
          dataIndex: 'tmwireheight'
        },
        {
          title: '是否报警',
          align: 'center',
          dataIndex: 'iscall_dictText',
          key: 'iscall_dictText',
          scopedSlots: { customRender: 'tags' },
        },
        // {
        //   title:'单双天线模式（0表示单天线，1表示双天线）',
        //   align:"center",
        //   dataIndex: 'tmantemode'
        // },
        // {
        //   title:'单天线AC',
        //   align:"center",
        //   dataIndex: 'tmwheelwidthsingle'
        // },
        // {
        //   title:'单天线BE长',
        //   align:"center",
        //   dataIndex: 'tmdistancefromwheel'
        // },
        // {
        //   title:'单天线DE长',
        //   align:"center",
        //   dataIndex: 'tmdistancefromcenter'
        // },
        // {
        //   title:'双天线CB长',
        //   align:"center",
        //   dataIndex: 'tmcentertomainx'
        // },
        // {
        //   title:'双天线GC长',
        //   align:"center",
        //   dataIndex: 'tmcentertomainy'
        // },
        // {
        //   title:'双天线FE长',
        //   align:"center",
        //   dataIndex: 'tmvicetomainx'
        // },
        // {
        //   title:'双天线GF长',
        //   align:"center",
        //   dataIndex: 'tmvicetomainy'
        // },
        // {
        //   title:'双天线AD长',
        //   align:"center",
        //   dataIndex: 'tmwheelwidthdouble'
        // },
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
        list: '/hc_machine/hcMachine/list',
        delete: '/hc_machine/hcMachine/delete',
        deleteBatch: '/hc_machine/hcMachine/deleteBatch',
        exportXlsUrl: '/hc_machine/hcMachine/exportXls',
        importExcelUrl: 'hc_machine/hcMachine/importExcel',

      },
      dictOptions: {},
      superFieldList: [],
    }
  },
  created() {
    this.getSuperFieldList()
  },
  computed: {
    importExcelUrl: function () {
      return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`
    },
  },
  methods: {
    initDictConfig() {
    },
    getSuperFieldList() {
      let fieldList = []
      fieldList.push({ type: 'string', value: 'sjid', text: '数据id' })
      fieldList.push({ type: 'string', value: 'machineid', text: '机械id' })
      fieldList.push({ type: 'string', value: 'terminalsncode', text: '终端sn号' })
      fieldList.push({ type: 'number', value: 'machinesize', text: '机械宽度(m)' })
      fieldList.push({ type: 'string', value: 'machinename', text: '机械名称' })
      fieldList.push({
        type: 'int',
        value: 'machinetypecode',
        text: '机械类型编码，0：振动碾，1：冲击碾，3：双钢轮，4：胶轮，5：沥青摊铺机，10：羊足碾，11：单钢轮，12：水稳摊铺机'
      })
      fieldList.push({ type: 'string', value: 'machinetypename', text: '机械类型' })
      fieldList.push({ type: 'date', value: 'tmstarttime', text: '安装开始使用时间' })
      fieldList.push({ type: 'date', value: 'tmendtime', text: '结束时间，3000-01-01 00:00:00表示未结束仍在使用，小于当前时间表示已结束' })
      fieldList.push({ type: 'number', value: 'tmwireheight', text: '天线高度(m)' })
      fieldList.push({ type: 'int', value: 'tmantemode', text: '单双天线模式（0表示单天线，1表示双天线）' })
      fieldList.push({ type: 'number', value: 'tmwheelwidthsingle', text: '单天线AC' })
      fieldList.push({ type: 'number', value: 'tmdistancefromwheel', text: '单天线BE长' })
      fieldList.push({ type: 'number', value: 'tmdistancefromcenter', text: '单天线DE长' })
      fieldList.push({ type: 'number', value: 'tmcentertomainx', text: '双天线CB长' })
      fieldList.push({ type: 'number', value: 'tmcentertomainy', text: '双天线GC长' })
      fieldList.push({ type: 'number', value: 'tmvicetomainx', text: '双天线FE长' })
      fieldList.push({ type: 'number', value: 'tmvicetomainy', text: '双天线GF长' })
      fieldList.push({ type: 'number', value: 'tmwheelwidthdouble', text: '双天线AD长' })
      this.superFieldList = fieldList
    }
  }
}
</script>
<style scoped>
@import '~@assets/less/common.less';
</style>
