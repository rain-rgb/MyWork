<template>
  <a-card :bordered="false">
    <a-row :gutter="false">
      <a-col :sm="24" :md="12" :xl="4" :style="{ marginBottom: '24px'}"><span style="font-size: 22px;font-weight: bold">压浆未合格：{{ oversum }}</span>
      </a-col>
      <a-col :sm="24" :md="12" :xl="4" :style="{ marginBottom: '24px'}"><span style="font-size: 22px;font-weight: bold">本月未合格：{{ monthOverSum }}</span>
      </a-col>
    </a-row>
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="设备名称">
              <j-search-select-tag placeholder="请选择设备名称" v-model="queryParam.yjsbbh" :dictOptions="dictOption">
              </j-search-select-tag>
              {{ selectValue }}
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="是否完成">
              <j-dict-select-tag placeholder="请选择是否完成" v-model="queryParam.status"
                                 dictCode="mstatus"></j-dict-select-tag>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="时间范围">
              <j-date placeholder="开始时间" v-model="queryParam.yjsj_begin" :showTime="true"
                      dateFormat="YYYY-MM-DD HH:mm:ss" />
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="">
              <j-date placeholder="结束时间" v-model="queryParam.yjsj_end" :showTime="true"
                      dateFormat="YYYY-MM-DD HH:mm:ss" />
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
              <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
              <a-button type="primary" @click="searchReset" icon="reload" style="margin-left: 8px">重置</a-button>
              <a @click="handleToggleSearch" style="margin-left: 8px">
                {{ toggleSearchStatus ? '收起' : '展开' }}
                <a-icon :type="toggleSearchStatus ? 'up' : 'down'" />
              </a>
            </span>
          </a-col>
        </a-row>
      </a-form>
    </div>
    <!-- 查询区域-END -->

    <!-- 操作按钮区域 -->
    <div class="table-operator">
      <a-button @click="handleAdd" v-has="'tryj:add'" type="primary" icon="plus">新增</a-button>
      <a-button type="primary" v-has="'tryj:dc'" icon="download" @click="handleExportXls('压浆主表信息')">导出
      </a-button>
      <a-upload name="file" v-has="'tryj:dr'" :showUploadList="false" :multiple="false" :headers="tokenHeader"
                :action="importExcelUrl" @change="handleImportExcel">
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
        <span slot="status" slot-scope="status, record">
        <a-tag color="green" v-if="record.status == '1'">已完成</a-tag>
         <a-tag color="red" v-if="record.status == '0'">未完成</a-tag>
       </span>
        <span slot="ismt" slot-scope="ismt, record">
           <a-tag color="grey" v-if="record.ismt==0">未推送</a-tag>
          <a-tag color="green" v-if="record.ismt==1">推送成功</a-tag>
          <a-tag color="red" v-if="record.ismt==2">推送失败</a-tag>
          <a-tag color="yellow" v-if="record.ismt==3">未关联任务单</a-tag>
        </span>
        <template slot="htmlSlot" slot-scope="text">
          <div v-html="text"></div>
        </template>
        <template slot="imgSlot" slot-scope="text">
          <span v-if="!text" style="font-size: 12px;font-style: italic;">无图片</span>
          <img v-else :src="getImgView(text)" height="25px" alt=""
               style="max-width:80px;font-size: 12px;font-style: italic;" />
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
          <a v-has="'tryj:edit'" @click="handleEdit(record)">编辑</a>
          <a @click="showmadel1(record.syjid)">详情</a>
          <a-divider type="vertical" />
          <a-dropdown>
            <a @click="reset(record)">重置推送</a>
          </a-dropdown>
        </span>

      </a-table>
    </div>
    <TrYajiangMSModal ref="TrYajiangMSModal"></TrYajiangMSModal>
    <tr-yajiang-m-modal ref="modalForm" @ok="modalFormOk"></tr-yajiang-m-modal>
  </a-card>
</template>

<script>

import '@/assets/less/TableExpand.less'
import { mixinDevice } from '@/utils/mixin'
import { JeecgListMixin } from '@/mixins/JeecgListMixin'
import TrYajiangMModal from './modules/TrYajiangMModal'
import JSuperQuery from '@/components/jeecg/JSuperQuery.vue'
import { usershebeiList } from '@api/api'
import { handertoken } from '@/mixins/getHanderToken'
import TrYajiangMSModal from '@views/zlyj/modules/TrYajiangMSModal'
import Vue from 'vue'
import { getAction } from '@api/manage'

export default {
  name: 'TrYajiangMList',
  mixins: [JeecgListMixin, mixinDevice, handertoken],
  components: {
    TrYajiangMModal,
    JSuperQuery,
    TrYajiangMSModal
  },
  data() {
    return {
      oversum: '',
      monthOverSum: '',
      dictOption: [],
      selectValue: '',
      description: '压浆主表信息管理页面',
      // 表头
      columns: [
        {
          title: '#',
          dataIndex: '',
          key: 'rowIndex',
          align: 'center',
          customRender: function(t, r, index) {
            return parseInt(index) + 1
          }
        },
        {
          title: '工程名称',
          align: 'center',
          dataIndex: 'gcmc'
        },
        {
          title: '梁板类型',
          align: 'center',
          dataIndex: 'lblx'
        },
        {
          title: '压浆时间',
          align: 'center',
          dataIndex: 'yjsj'
        },
        {
          title: '设备名称',
          align: 'center',
          dataIndex: 'yjsbbh_dictText'
        },
        {
          title: '梁号',
          align: 'center',
          dataIndex: 'lianghao'
        },
        {
          title: '施工部位',
          align: 'center',
          dataIndex: 'sgbw'
        },
        {
          title: '掺减水剂量',
          align: 'center',
          dataIndex: 'cjsjl'
        },
        {
          title: '张拉时间',
          align: 'center',
          dataIndex: 'zlsj'
        },
        {
          title: '压浆剂',
          align: 'center',
          dataIndex: 'yajiangji'
        },
        // {
        //   title: '水泥名称',
        //   align: 'center',
        //   dataIndex: 'snmc'
        // },
        {
          title: '孔道数',
          align: 'center',
          dataIndex: 'kongdaoshu'
        },
        {
          title: '压浆方向',
          align: 'center',
          dataIndex: 'yajiangfang'
        },
        {
          title: '压浆步骤',
          align: 'center',
          dataIndex: 'yajiangbuzh'
        },
        {
          title: '初始流动速度',
          align: 'center',
          dataIndex: 'chushisudu'
        },
        {
          title: '流动度',
          align: 'center',
          dataIndex: 'liudongdu'
        },
        {
          title: '值班人员',
          align: 'center',
          dataIndex: 'memo'
        },
        {
          title: '推送质保资料',
          align: 'center',
          dataIndex: 'ismt',
          scopedSlots: { customRender: 'ismt' }
        },
        {
          title: '完成状态',
          align: 'center',
          dataIndex: 'status',
          scopedSlots: { customRender: 'status' }
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
        list: '/yajiangm/trYajiangM/list',
        delete: '/yajiangm/trYajiangM/delete',
        deleteBatch: '/yajiangm/trYajiangM/deleteBatch',
        exportXlsUrl: '/yajiangm/trYajiangM/exportXls',
        importExcelUrl: 'yajiangm/trYajiangM/importExcel',
        yjtjUrl: '/tryajiangstatistics/trYajiangStatistics/yjwarn',
        resetPush: '/yajiangm/trYajiangM/resetPush'
      },
      dictOptions: {},
      superFieldList: []
    }
  },
  created() {
    this.getToken()
    this.shebeiList()
    this.getSuperFieldList()
    this.tongji()
  },
  computed: {
    importExcelUrl: function() {
      return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`
    }
  },
  methods: {
    tongji: function() {
      var sys_depart_orgcode = Vue.ls.get('SYS_DEPART_ORGCODE')
      getAction(this.url.yjtjUrl, { sys_depart_orgcode })
        .then(res => {
          var data = res.result
          this.oversum = data.oversum
          this.monthOverSum = data.monthOverSum
        })
    },
    showmadel1(syjid) {
      this.$refs.TrYajiangMSModal.title = '详情'
      this.$refs.TrYajiangMSModal.call(syjid)
    },
    shebeiList: function() {
      var sys_depart_orgcode = Vue.ls.get('SYS_DEPART_ORGCODE')
      usershebeiList({
        sys_depart_orgcode: sys_depart_orgcode,
        sbtypes: '10'
      }).then(res => {
        //console.log(res)
        this.dictOption = []
        let result = res.result
        result.forEach(re => {
          this.dictOption.push({ text: re.sbname, value: re.sbjno })
        })

      })
    },
    reset(record) {
      try {
        // 调用重置接口，根据实际情况修改请求参数
        getAction(this.url.resetPush, { id: record.id })
        this.$message.success('重置推送成功')
        // 刷新表格数据
        this.loadData()
      } catch (error) {
        console.error('重置推送失败', error)
        this.$message.error('重置推送失败，请稍后重试')
      }
    },
    initDictConfig() {
    },
    getSuperFieldList() {
      let fieldList = []
      fieldList.push({ type: 'string', value: 'syjid', text: '32位全球唯一码', dictCode: '' })
      fieldList.push({ type: 'string', value: 'sgdw', text: '施工单位', dictCode: '' })
      fieldList.push({ type: 'string', value: 'jldw', text: '监理单位', dictCode: '' })
      fieldList.push({ type: 'string', value: 'htbh', text: '合同号', dictCode: '' })
      fieldList.push({ type: 'string', value: 'gcmc', text: '工程名称', dictCode: '' })
      fieldList.push({ type: 'string', value: 'yjsj', text: '压浆时间', dictCode: '' })
      fieldList.push({ type: 'string', value: 'zhbw', text: '桩号及部位', dictCode: '' })
      fieldList.push({ type: 'string', value: 'sgbw', text: '施工部位', dictCode: '' })
      fieldList.push({ type: 'string', value: 'gjjg', text: '构件结构', dictCode: '' })
      fieldList.push({ type: 'string', value: 'gjbh', text: '构件编号及长度', dictCode: '' })
      fieldList.push({ type: 'string', value: 'qw', text: '气温', dictCode: '' })
      fieldList.push({ type: 'string', value: 'cjsjl', text: '掺减水剂量', dictCode: '' })
      fieldList.push({ type: 'string', value: 'cpzjl', text: '掺膨胀剂量', dictCode: '' })
      fieldList.push({ type: 'string', value: 'sw', text: '水温', dictCode: '' })
      fieldList.push({ type: 'string', value: 'shuijiaobi', text: '水胶比', dictCode: '' })
      fieldList.push({ type: 'string', value: 'snyl', text: '构件压浆水泥用量', dictCode: '' })
      fieldList.push({ type: 'string', value: 'yjwd', text: '压浆温度', dictCode: '' })
      fieldList.push({ type: 'string', value: 'msl', text: '泌水率', dictCode: '' })
      fieldList.push({ type: 'string', value: 'beiyong', text: '备用', dictCode: '' })
      fieldList.push({ type: 'string', value: 'yjsbbh', text: '压浆设备编号', dictCode: '' })
      fieldList.push({ type: 'string', value: 'lblx', text: '梁板类型', dictCode: '' })
      fieldList.push({ type: 'string', value: 'lianghao', text: '梁号', dictCode: '' })
      fieldList.push({ type: 'string', value: 'zlsj', text: '张拉时间', dictCode: '' })
      fieldList.push({ type: 'string', value: 'yajiangji', text: '压浆剂', dictCode: '' })
      fieldList.push({ type: 'string', value: 'snmc', text: '水泥名称', dictCode: '' })
      fieldList.push({ type: 'string', value: 'kongdaoshu', text: '孔道数', dictCode: '' })
      fieldList.push({ type: 'string', value: 'yajiangfang', text: '压浆方向', dictCode: '' })
      fieldList.push({ type: 'string', value: 'yajiangbuzh', text: '压浆步骤', dictCode: '' })
      fieldList.push({ type: 'string', value: 'yajiangguoc', text: '压浆过程', dictCode: '' })
      fieldList.push({ type: 'string', value: 'chushisudu', text: '初始流动速度', dictCode: '' })
      fieldList.push({ type: 'string', value: 'memo', text: '备注', dictCode: '' })
      fieldList.push({ type: 'string', value: 'liudongdu', text: 'liudongdu', dictCode: '' })
      fieldList.push({ type: 'string', value: 'status', text: '完成状态(0为未完成,状态为1代表', dictCode: '' })
      fieldList.push({ type: 'string', value: 'guid', text: '32位全球唯一码', dictCode: '' })
      fieldList.push({ type: 'string', value: 'uuid', text: '压浆任务单下发时任务单id（由设备返回）', dictCode: '' })
      fieldList.push({ type: 'string', value: 'issend', text: 'issend', dictCode: '' })
      this.superFieldList = fieldList
    }
  }
}
</script>
<style scoped>
@import '~@assets/less/common.less';
</style>