<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="设备名称">
              <j-search-select-tag placeholder="请选择设备名称" v-model="queryParam.equipmentcode" :dictOptions="dictOption" >
              </j-search-select-tag>
              {{ selectValue }}
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="时间范围">
              <j-date placeholder="开始时间" v-model="queryParam.starttime_begin" :showTime="true"
                      dateFormat="YYYY-MM-DD HH:mm:ss"/>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="">
              <j-date placeholder="结束时间" v-model="queryParam.starttime_end" :showTime="true"
                      dateFormat="YYYY-MM-DD HH:mm:ss"/>
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
      <!--      <a-button @click="handleAdd" type="primary" icon="plus">新增</a-button>-->
            <a-button v-has="'snjbz:dc'" type="primary" icon="download" @click="handleExportXls('水泥搅拌桩主表')">导出</a-button>
      <!--      <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl" @change="handleImportExcel">-->
      <!--        <a-button type="primary" icon="import">导入</a-button>-->
      <!--      </a-upload>-->
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
        <a-tag color="orange" v-if="record.recordstatus == '1'">开始</a-tag>
        <a-tag color="yellow" v-if="record.recordstatus == '2'">暂停</a-tag>
         <a-tag color="red" v-if="record.recordstatus == '3'">结束</a-tag>
       </span>
        <span slot="pipestatustags" slot-scope="pipestatustags, record">
        <a-tag color="orange" v-if="record.pipestatus == '1'">下钻</a-tag>
        <a-tag color="yellow" v-if="record.pipestatus == '2'">提钻</a-tag>
         <a-tag color="red" v-if="record.pipestatus == '3'">复下</a-tag>
          <a-tag color="green" v-if="record.pipestatus == '4'">复提</a-tag>
       </span>
        <span slot="nozzlestatustags" slot-scope="nozzlestatustags, record">
        <a-tag color="orange" v-if="record.nozzlestatus == '0'">喷浆</a-tag>
        <a-tag color="green" v-if="record.nozzlestatus == '1'">止喷</a-tag>
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

        <span  slot="action" slot-scope="text, record">
          <a v-has="'snjbz:edit'" @click="handleEdit(record)">编辑</a>

<!--          <a-divider type="vertical"/>-->
          <a-dropdown>
            <a class="ant-dropdown-link">更多 <a-icon type="down"/></a>
            <a-menu slot="overlay">
              <a-menu-item>
                <a @click="handleDetail(record)">详情</a>
              </a-menu-item>
              <a-menu-item v-has="'snjbz:del'">
                <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">
                  <a>删除</a>
                </a-popconfirm>
              </a-menu-item>
            </a-menu>
          </a-dropdown>
        </span>

      </a-table>
    </div>

    <smwsegmentata-base-modal ref="modalForm" @ok="modalFormOk"></smwsegmentata-base-modal>
  </a-card>
</template>

<script>

import '@/assets/less/TableExpand.less'
import { mixinDevice } from '@/utils/mixin'
import { JeecgListMixin } from '@/mixins/JeecgListMixin'
import SmwsegmentataBaseModal from './modules/SmwsegmentataBaseModal'
import JSuperQuery from '@/components/jeecg/JSuperQuery.vue'
import { handertoken } from '@/mixins/getHanderToken'
import Vue from 'vue'
import { SYS_DEPART_ORGCODE } from '@/store/mutation-types'
import { usershebeiList } from '@api/api'

export default {
  name: 'SmwsegmentataBaseList',
  mixins: [JeecgListMixin, mixinDevice, handertoken],
  components: {
    SmwsegmentataBaseModal,
    JSuperQuery,
  },
  data() {
    return {
      selectValue: '',
      asyncSelectValue: '',
      dictOption: [],
      description: '水泥搅拌桩主表管理页面',
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
          title: '设备名称',
          align: 'center',
          dataIndex: 'equipmentcode_dictText'
        },
        {
          title: '桩号',
          align: 'center',
          dataIndex: 'constructionstakenum'
        },
        {
          title: '开始时间',
          align: 'center',
          dataIndex: 'starttime'
        },
        {
          title: '记录状态 ',
          align: 'center',
          dataIndex: 'recordstatus',
          key: 'recordstatus',
          scopedSlots: { customRender: 'tags' },
        },
        {
          title: '钻杆状态',
          align: 'center',
          dataIndex: 'pipestatus',
          key: 'pipestatus',
          scopedSlots: { customRender: 'pipestatustags' },
        },
        {
          title: '喷浆状态',
          align: 'center',
          dataIndex: 'nozzlestatus',
          key: 'nozzlestatus',
          scopedSlots: { customRender: 'nozzlestatustags' },
        },
        {
          title: '水灰比',
          align: 'center',
          dataIndex: 'waterashratio'
        },
        {
          title: '段浆量(L)',
          align: 'center',
          dataIndex: 'segmentpulp'
        },
        {
          title: '段灰量(kg)',
          align: 'center',
          dataIndex: 'segmentash'
        },
        {
          title: '累计浆量(L)',
          align: 'center',
          dataIndex: 'totalpulp'
        },
        {
          title: '累计灰量(kg)',
          align: 'center',
          dataIndex: 'totalash'
        },
        {
          title: '电流(A)',
          align: 'center',
          dataIndex: 'electricity'
        },
        {
          title: '密度(g/cm3)',
          align: 'center',
          dataIndex: 'density'
        },
        {
          title: '喷压(MPa)',
          align: 'center',
          dataIndex: 'pressure'
        },
        {
          title: '速度(cm/min)',
          align: 'center',
          dataIndex: 'speed'
        },
        {
          title: '深度(m)',
          align: 'center',
          dataIndex: 'currentdepth'
        },
        {
          title: '最大深度(m)',
          align: 'center',
          dataIndex: 'maxdepth'
        },
        {
          title: '流量',
          align: 'center',
          dataIndex: 'flow'
        },
        {
          title: '前后倾角',
          align: 'center',
          dataIndex: 'xangle'
        },
        {
          title: '左右倾角',
          align: 'center',
          dataIndex: 'yangle'
        },
        // {
        //   title:'经度',
        //   align:"center",
        //   dataIndex: 'longitudu'
        // },
        // {
        //   title:'纬度',
        //   align:"center",
        //   dataIndex: 'latitude'
        // },
        // {
        //   title:'与子表关联',
        //   align:"center",
        //   dataIndex: 'guid'
        // },
        // {
        //   title: '状态在线不在线',
        //   align: 'center',
        //   dataIndex: 'status'
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
        list: '/Snjbz/smwsegmentataBase/list',
        delete: '/Snjbz/smwsegmentataBase/delete',
        deleteBatch: '/Snjbz/smwsegmentataBase/deleteBatch',
        exportXlsUrl: '/Snjbz/smwsegmentataBase/exportXls',
        importExcelUrl: 'Snjbz/smwsegmentataBase/importExcel',

      },
      dictOptions: {},
      superFieldList: [],
    }
  },
  created() {
    this.getToken();
    this.getSuperFieldList();
    this.shebeiList();
  },
  computed: {
    importExcelUrl: function () {
      return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`
    },
  },
  methods: {
    shebeiList:function (){
      var sys_depart_orgcode=Vue.ls.get('SYS_DEPART_ORGCODE');
      usershebeiList({
        sys_depart_orgcode:sys_depart_orgcode,
        sbtypes:'16'
      }).then(res=>{
        console.log(res)
        this.dictOption=[];
        let result=res.result;
        result.forEach(re=>{
          this.dictOption.push({text:re.sbname,value:re.sbjno})
        })

      })
    },
    initDictConfig() {
    },
    getSuperFieldList() {
      let fieldList = []
      fieldList.push({ type: 'string', value: 'equipmentcode', text: '设备编码', dictCode: '' })
      fieldList.push({ type: 'string', value: 'constructionstakenum', text: '桩号', dictCode: '' })
      fieldList.push({ type: 'Text', value: 'starttime', text: '开始时间', dictCode: '' })
      fieldList.push({ type: 'int', value: 'recordstatus', text: '记录状态 ', dictCode: '' })
      fieldList.push({ type: 'int', value: 'pipestatus', text: '钻杆状态', dictCode: '' })
      fieldList.push({ type: 'int', value: 'nozzlestatus', text: '喷浆状态', dictCode: '' })
      fieldList.push({ type: 'double', value: 'waterashratio', text: '水灰比', dictCode: '' })
      fieldList.push({ type: 'double', value: 'segmentpulp', text: '段浆量(L)', dictCode: '' })
      fieldList.push({ type: 'double', value: 'segmentash', text: '段灰量(kg)', dictCode: '' })
      fieldList.push({ type: 'double', value: 'totalpulp', text: '累计浆量(L)', dictCode: '' })
      fieldList.push({ type: 'double', value: 'totalash', text: '累计灰量(kg)', dictCode: '' })
      fieldList.push({ type: 'double', value: 'electricity', text: '电流(A)', dictCode: '' })
      fieldList.push({ type: 'double', value: 'density', text: '密度(g/cm3)', dictCode: '' })
      fieldList.push({ type: 'double', value: 'pressure', text: '喷压(MPa)', dictCode: '' })
      fieldList.push({ type: 'double', value: 'speed', text: '速度(cm/min)', dictCode: '' })
      fieldList.push({ type: 'double', value: 'currentdepth', text: '深度(m)', dictCode: '' })
      fieldList.push({ type: 'double', value: 'maxdepth', text: '最大深度(m)', dictCode: '' })
      fieldList.push({ type: 'double', value: 'flow', text: '流量', dictCode: '' })
      fieldList.push({ type: 'double', value: 'xangle', text: '前后倾角', dictCode: '' })
      fieldList.push({ type: 'double', value: 'yangle', text: '左右倾角', dictCode: '' })
      fieldList.push({ type: 'double', value: 'longitudu', text: '经度', dictCode: '' })
      fieldList.push({ type: 'double', value: 'latitude', text: '纬度', dictCode: '' })
      fieldList.push({ type: 'string', value: 'guid', text: '与子表关联', dictCode: '' })
      fieldList.push({ type: 'int', value: 'status', text: '状态在线不在线', dictCode: '' })
      this.superFieldList = fieldList
    }
  }
}
</script>
<style scoped>
@import '~@assets/less/common.less';
</style>