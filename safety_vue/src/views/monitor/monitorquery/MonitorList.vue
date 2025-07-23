<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="设备名称">
              <j-search-select-tag placeholder="请选择设备名称" v-model="queryParam.remark" :dictOptions="dictOption" >
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
      <!--      <a-button @click="handleAdd" type="primary" icon="plus">新增</a-button>-->
      <!--      <a-button type="primary" icon="download" @click="handleExportXls('monitor')">导出</a-button>-->
      <!--      <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl" @change="handleImportExcel">-->
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
          <a-tag color="red" v-if="record.workstate === 1">离线</a-tag>
          <a-tag color="green" v-if="record.workstate === 0">在线</a-tag>
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
<!--          <a @click="handleEdit(record)">编辑</a>-->
           <a-divider type="vertical"/>
          <a @click="showVideo(record)">播放</a>
          <a-divider type="vertical"/>
          <a-dropdown>
            <a class="ant-dropdown-link">更多 <a-icon type="down"/></a>
            <a-menu slot="overlay">
              <a-menu-item>
                <a @click="handleDetail(record)">详情</a>
              </a-menu-item>
<!--                            <a-menu-item>-->
<!--                              <a-popconfirm title="确定不显示吗?" @confirm="() => handleDelete(record.id)">-->
<!--                                <a>删除</a>-->
<!--                              </a-popconfirm>-->
<!--                            </a-menu-item>-->
            </a-menu>
          </a-dropdown>
        </span>

      </a-table>
    </div>
    <!--    <videoplay :flag="flag" :id="id" :token="videotoken"></videoplay>-->
    <monitor-modal ref="modalForm" @ok="modalFormOk"></monitor-modal>
  </a-card>
</template>

<script>

import '@/assets/less/TableExpand.less'
import { mixinDevice } from '@/utils/mixin'
import { JeecgListMixin } from '@/mixins/JeecgListMixin'
import MonitorModal from './modules/MonitorModal'
import { usershebeiList } from '@api/api'
import { getAction, postAction, postActions } from '@api/manage'
import { axios } from '@/utils/request'
import Vue from 'vue'

export default {
  name: 'MonitorList',
  mixins: [JeecgListMixin, mixinDevice],
  components: {
    MonitorModal
  },
  data() {
    return {
      dictOption: [],
      id: '',
      videotoken: '',
      // flag: 0,
      description: 'monitor管理页面',
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
        //   title:'id',
        //   align:"center",
        //   dataIndex: 'id'
        // },
        {
          title: '摄像头名称',
          align: 'center',
          dataIndex: 'monitorname'
        },
        {
          title: '序列号',
          align: 'center',
          dataIndex: 'serialnumber'
        },
        {
          title: '验证码',
          align: 'center',
          dataIndex: 'verificationcode'
        },
        {
          title: '设备名称',
          align: 'center',
          dataIndex: 'remark_dictText'
        },
        {
          title: '排序号',
          align: 'center',
          dataIndex: 'ordernum'
        },
        {
          title: '通道',
          align: 'center',
          dataIndex: 'channel'
        },
        {
          title: '应用场景',
          align: 'center',
          dataIndex: 'usetype_dictText'
        },
        {
          title: '经度',
          align: 'center',
          dataIndex: 'longitude'
        },
        {
          title: '纬度',
          align: 'center',
          dataIndex: 'latitude'
        },
        {
          title: '组织机构',
          align: 'center',
          dataIndex: 'orgcode_dictText'
        },
        {
          title: '视频工作状态',
          align: 'center',
          dataIndex: 'workstate',
          scopedSlots: { customRender: 'tags' }
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
        list: '/monitor/monitor/list1',
        delete: '/monitor/monitor/delete',
        deleteBatch: '/monitor/monitor/deleteBatch',
        exportXlsUrl: '/monitor/monitor/exportXls',
        importExcelUrl: 'monitor/monitor/importExcel',
        tokenlist: '/sys/systems/sysMessages/tokenlist'
      },
      dictOptions: {},
      superFieldList: [],
    }
  },
  created() {
    this.getSuperFieldList()
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
        sbtypes:'21,23,50,16,19,53'
      }).then(res=>{
        this.dictOption=[];
        let result=res.result;
        result.forEach(re=>{
          this.dictOption.push({text:re.sbname,value:re.sbjno})
        })
        //console.log(res)
      })
    },
    showVideo(record) {
      // debugger
      // this.flag = 0
      this.id = record.id
      // let param = {
      //   apiid: '1', apisceret: 'CB093DD1D932456C9D33B2E25CD9CFF5'
      // }
      this.videotoken = ''
      getAction(this.url.tokenlist, {}).then(res => {
          if (res.success) {
            console.log("token",res)
            this.videotoken = res.result
            window.open('http://47.97.173.113:9271/VideoMonitor?id=' + this.id + '&token=' + this.videotoken)
          }
        }
      )
      // postActions('dataprovider/gettoken', param).then(res => {
      //   //console.log('res', res)
      //   this.videotoken = res.data.data.token
      //   //console.log('videotoken', this.videotoken)
      //   window.open('http://47.97.173.113:9271/VideoMonitor?id=' + this.id + '&token=' + this.videotoken)
      //   //console.log(res, 'url')
      // })
      //console.log('id', this.id)

    },
    // change(data) {
    //   this.flag = data
    // },
    initDictConfig() {
    },
    getSuperFieldList() {
      let fieldList = []
      fieldList.push({ type: 'string', value: 'monitorname', text: '摄像头名称' })
      fieldList.push({ type: 'string', value: 'serialnumber', text: '序列号' })
      fieldList.push({ type: 'string', value: 'verificationcode', text: '验证码' })
      fieldList.push({ type: 'string', value: 'remark', text: '设备名称' })
      fieldList.push({ type: 'int', value: 'ordernum', text: '排序号' })
      fieldList.push({ type: 'int', value: 'channel', text: '通道' })
      fieldList.push({ type: 'int', value: 'usetype', text: '应用场景' })
      fieldList.push({ type: 'string', value: 'longitude', text: '经度' })
      fieldList.push({ type: 'string', value: 'latitude', text: '纬度' })
      fieldList.push({ type: 'string', value: 'orgcode', text: '组织机构' })
      this.superFieldList = fieldList
    }
  }
}
</script>
<style scoped>
@import '~@assets/less/common.less';
</style>