<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="号码类型">
              <j-dict-select-tag placeholder="请选择号码类型" v-model="queryParam.phonesname" dictCode="phonesname"/>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="联系人">
              <a-input placeholder="请输入联系人" v-model="queryParam.names"/>
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
<!--      <a-button type="primary" icon="download" @click="handleExportXls('拌合站手机号码配置')">导出</a-button>-->
<!--      <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl"-->
<!--                @change="handleImportExcel">-->
<!--        <a-button type="primary" icon="import">导入</a-button>-->
<!--      </a-upload>-->
      <!-- 高级查询区域 -->
<!--      <j-super-query :fieldList="superFieldList" ref="superQueryModal"-->
<!--                     @handleSuperQuery="handleSuperQuery"></j-super-query>-->
<!--      <a-dropdown v-if="selectedRowKeys.length > 0">-->
<!--        <a-menu slot="overlay">-->
<!--          <a-menu-item key="1" @click="batchDel">-->
<!--            <a-icon type="delete"/>-->
<!--            删除-->
<!--          </a-menu-item>-->
<!--        </a-menu>-->
<!--        <a-button style="margin-left: 8px"> 批量操作-->
<!--          <a-icon type="down"/>-->
<!--        </a-button>-->
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
         <a-tag color="green" v-if="record.phonesname === 1">初级超标号码组</a-tag>
         <a-tag color="cyan" v-if="record.phonesname === 2">中级超标号码组</a-tag>
         <a-tag color="blue" v-if="record.phonesname === 3">高级超标号码组</a-tag>
         <a-tag color="blue" v-if="record.phonesname === 19">拌合站温度超标号码组</a-tag>
         <a-tag color="purple" v-if="record.phonesname === 4">试验室不合格号码组</a-tag>
         <a-tag color="pink" v-if="record.phonesname === 5">张拉压浆号码组</a-tag>
         <a-tag color="red" v-if="record.phonesname === 6">智慧用电超标号码组</a-tag>
         <a-tag color="red" v-if="record.phonesname === 7">围栏预警号码组</a-tag>
         <a-tag color="orange" v-if="record.phonesname === 8">标养室预警号码组</a-tag>
         <a-tag color="orange" v-if="record.phonesname === 9">水泥搅拌桩预警号码组</a-tag>
         <a-tag color="orange" v-if="record.phonesname === 10">大型设备预警号码组</a-tag>
         <a-tag color="orange" v-if="record.phonesname === 11">软基工单通知号码组</a-tag>
         <a-tag color="orange" v-if="record.phonesname === 12">梁板订单接收人号码组</a-tag>
         <a-tag color="yellow" v-if="record.phonesname === 13">环境初级预警号码组</a-tag>
         <a-tag color="orange" v-if="record.phonesname === 14">环境中级预警号码组</a-tag>
         <a-tag color="red" v-if="record.phonesname === 15">环境高级预警号码组</a-tag>
         <a-tag color="blue" v-if="record.phonesname === 16">设备状态预警号码组</a-tag>
         <a-tag color="blue" v-if="record.phonesname === 17">拌合站开关机预警号码组</a-tag>
         <a-tag color="cyan" v-if="record.phonesname === 20">运输超时预警号码组</a-tag>
         <a-tag color="blue" v-if="record.phonesname === 21">梁厂工序完成手机号码组</a-tag>
         <a-tag color="cyan" v-if="record.phonesname === 22">二级围栏预警号码组</a-tag>
         <a-tag color="orange" v-if="record.phonesname === 24">大型设备预警号码组(新)</a-tag>
         <a-tag color="orange" v-if="record.phonesname === 29">浇筑令下发接收人员号码组</a-tag>
           <a-tag color="orange" v-if="record.phonesname === 33">土方压实施工人员</a-tag>
         <a-tag color="orange" v-if="record.phonesname === 34">土方压实监理人员</a-tag>
         <a-tag color="blue" v-if="record.phonesname === 35">锚下预应力张拉预警号码组</a-tag>
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

    <bhz-phone-modal ref="modalForm" @ok="modalFormOk"></bhz-phone-modal>
  </a-card>
</template>

<script>

import '@/assets/less/TableExpand.less'
import {mixinDevice} from '@/utils/mixin'
import {JeecgListMixin} from '@/mixins/JeecgListMixin'
import BhzPhoneModal from './modules/BhzPhoneModal'
import JSuperQuery from '@/components/jeecg/JSuperQuery.vue'

export default {
  name: 'BhzPhoneList',
  mixins: [JeecgListMixin, mixinDevice],
  components: {
    BhzPhoneModal,
    JSuperQuery,
  },
  data() {
    return {
      description: '拌合站手机号码配置管理页面',
      // 表头
      columns: [
        {
          title: '#',
          dataIndex: '',
          key: 'rowIndex',
          width: 60,
          align: "center",
          customRender: function (t, r, index) {
            return parseInt(index) + 1;
          }
        },
        {
          title: '联系人',
          align: "center",
          dataIndex: 'names'
        },
        {
          title: '手机号码',
          align: "center",
          dataIndex: 'phones'
        },
        {
          title: '号码类型',
          align: "center",
          dataIndex: 'phonesname_dictText',
          scopedSlots: {customRender: 'tags'},
        },
        {
          title: '所属机构',
          align: "center",
          dataIndex: 'sysOrgCode_dictText'
        },
        {
          title: '创建时间',
          align: "center",
          dataIndex: 'createTime',
          customRender: function (text) {
            return !text ? "" : (text.length > 10 ? text.substr(0, 10) : text)
          }
        },
        {
          title: '创建人',
          align: "center",
          dataIndex: 'createBy'
        },
        {
          title: '修改时间',
          align: "center",
          dataIndex: 'updateTime',
          customRender: function (text) {
            return !text ? "" : (text.length > 10 ? text.substr(0, 10) : text)
          }
        },
        {
          title: '修改人',
          align: "center",
          dataIndex: 'updateBy'
        },
        {
          title: '操作',
          dataIndex: 'action',
          align: "center",
          fixed: "right",
          width: 147,
          scopedSlots: {customRender: 'action'}
        }
      ],
      url: {
        list: "/bhzcfg/bhzPhone/list",
        delete: "/bhzcfg/bhzPhone/delete",
        deleteBatch: "/bhzcfg/bhzPhone/deleteBatch",
        exportXlsUrl: "/bhzcfg/bhzPhone/exportXls",
        importExcelUrl: "bhzcfg/bhzPhone/importExcel",

      },
      dictOptions: {},
      superFieldList: [],
    }
  },
  created() {
    this.getSuperFieldList();
  },
  computed: {
    importExcelUrl: function () {
      return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`;
    },
  },
  methods: {
    initDictConfig() {
    },
    getSuperFieldList() {
      let fieldList = [];
      fieldList.push({type: 'string', value: 'uid', text: 'UUid主键', dictCode: ''})
      fieldList.push({type: 'string', value: 'names', text: '联系人', dictCode: ''})
      fieldList.push({type: 'string', value: 'phones', text: '手机号码', dictCode: ''})
      fieldList.push({
        type: 'int',
        value: 'phonesname',
        text: '号码组:1=初级超标号码组,2=中级超标号码组,3=高级超标号码组,4=试验机不合格号码组 5=张拉压浆号码组',
        dictCode: ''
      })
      fieldList.push({type: 'string', value: 'sysOrgCode', text: '所属机构', dictCode: ''})
      fieldList.push({type: 'date', value: 'createTime', text: '创建时间'})
      fieldList.push({type: 'date', value: 'updateTime', text: '修改时间'})
      fieldList.push({type: 'string', value: 'createBy', text: '创建人', dictCode: ''})
      fieldList.push({type: 'string', value: 'updateBy', text: '修改人', dictCode: ''})
      this.superFieldList = fieldList
    }
  }
}
</script>
<style scoped>
@import '~@assets/less/common.less';
</style>