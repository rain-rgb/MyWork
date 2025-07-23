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
              {{ selectValue }}
            </a-form-item>
          </a-col>

          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="料仓名称">
              <j-search-select-tag placeholder="请选择料仓名称" v-model="queryParam.lcbianhao" :dictOptions="dictOption1">
              </j-search-select-tag>
              {{ selectValue1 }}
            </a-form-item>
          </a-col>

<!--          <a-col :xl="6" :lg="7" :md="8" :sm="24">-->
<!--            <a-form-item label="材料名称">-->
<!--              <j-search-select-tag placeholder="请选择材料名称" v-model="queryParam.cailiaono" :dictOptions="dictOption2">-->
<!--              </j-search-select-tag>-->
<!--              {{ selectValue2 }}-->
<!--            </a-form-item>-->
<!--          </a-col>-->
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="材料类别">
              <j-dict-select-tag placeholder="请选择材料类别" v-model="queryParam.nodetype"
                                 dictCode="nodeType"></j-dict-select-tag>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="批次">
              <a-input placeholder="请输入批次" v-model="queryParam.pici"></a-input>
            </a-form-item>
          </a-col>

          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="时间范围">
              <j-date placeholder="开始时间" v-model="queryParam.createTime_begin" :showTime="true"
                      dateFormat="YYYY-MM-DD HH:mm:ss"/>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="">
              <j-date placeholder="结束时间" v-model="queryParam.createTime_end" :showTime="true"
                      dateFormat="YYYY-MM-DD HH:mm:ss"/>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="自检状态">
              <j-dict-select-tag placeholder="请选择自检结果" v-model="queryParam.jianyanstate"
                                 dictCode="jianyanstate">
              </j-dict-select-tag>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="抽检">
              <j-dict-select-tag placeholder="请选择抽检结果" v-model="queryParam.choujianstate"
                                 dictCode="choujianstate">
              </j-dict-select-tag>
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
      <a-button @click="handleAdd" v-has="'wztaizhang:add'" type="primary" icon="plus">新增</a-button>
      <a-button type="primary" icon="download" v-has="'wztaizhang:dc'" @click="handleExportXls('wztaizhang')">导出
      </a-button>
      <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl"
                @change="handleImportExcel">
        <a-button type="primary" v-has="'wztaizhang:dr'" icon="import">导入</a-button>
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
        @change="handleTableChange">
        <span slot="jingzhongt" slot-scope="jingzhongt, record">
          <div v-if="record.danwei !== null || record.danwei !==''"> {{record.jingzhongt}}{{record.danwei}}</div>
           <div v-else > {{record.jingzhongt}}t</div>
        </span>

        <span slot="tags" slot-scope="tags, record">
          <a-tag color="orange" v-if="record.jianyanstate == '0'">未检验</a-tag>
          <a-tag color="purple" v-if="record.jianyanstate == '3'">检验中</a-tag>
          <a-tag color="green" v-if="record.jianyanstate == '1'">合格</a-tag>
          <a-tag color="red" v-if="record.jianyanstate == '2'">不合格</a-tag>
        </span>
        <span slot="tags2" slot-scope="tags2, record">
          <a-tag color="orange" v-if="record.choujianstate == '0'">未检验</a-tag>
          <a-tag color="purple" v-if="record.choujianstate == '3'">检验中</a-tag>
          <a-tag color="green" v-if="record.choujianstate == '1'">合格</a-tag>
          <a-tag color="red" v-if="record.choujianstate == '2'">不合格</a-tag>
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
          <a v-has="'wztaizhang:edit'" @click="handleEdit(record)">编辑</a>
          <a @click="handlePreview(record)">预览</a>
          <a-divider v-show="record.jianyanstate == '2'||record.choujianstate == '2'" type="vertical"/>
          <a v-show="record.jianyanstate == '2'||record.choujianstate == '2'" @click="handlePreview(record)">处置</a>
          <a-divider type="vertical"/>
          <a-dropdown>
            <a class="ant-dropdown-link">更多 <a-icon type="down"/></a>
            <a-menu slot="overlay">
              <a-menu-item>
                <a @click="handleDetail(record)">详情</a>
                <a @click="handleUpload(record)">试验上传</a>
              </a-menu-item>
              <a-menu-item v-has="'wztaizhang:del'">
                <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">
                  <a>删除</a>
                </a-popconfirm>
              </a-menu-item>
            </a-menu>
          </a-dropdown>
        </span>

      </a-table>
    </div>

    <wztaizhang-modal ref="modalForm" @ok="modalFormOk"></wztaizhang-modal>
    <WzUploadModal ref="modalUpload" @ok="modalFormOk"></WzUploadModal>
  </a-card>
</template>

<script>

import '@/assets/less/TableExpand.less'
import { mixinDevice } from '@/utils/mixin'
import { JeecgListMixin } from '@/mixins/JeecgListMixin'
import WztaizhangModal from './modules/WztaizhangModal'
import WzUploadModal from './modules/WzUploadModal'
import JSuperQuery from '@/components/jeecg/JSuperQuery.vue'
import { usershebeiList } from '@api/api'
import { userliaocangList } from '@api/api'
import { usercailiaoList } from '@api/api'
import Vue from 'vue'
import { SYS_DEPART_ORGCODE } from '@/store/mutation-types'
import { handertoken } from '@/mixins/getHanderToken'
import { base64Encode } from '@api/kkfileView'

export default {
  name: 'WztaizhangLists',
  mixins: [JeecgListMixin, mixinDevice, handertoken],
  components: {
    WztaizhangModal,
    WzUploadModal,
    JSuperQuery,
  },
  data() {
    return {
      selectValue2: '',
      selectValue1: '',
      selectValue: '',
      asyncSelectValue: '',
      dictOption: [],
      dictOption1: [],
      dictOption2: [],
      description: 'wztaizhang管理页面',
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
        // {
        //   title:'设备名称',
        //   align:"center",
        //   dataIndex: 'shebeibianhao_dictText'
        // },
        // {
        //   title:'料仓名称',
        //   align:"center",
        //   dataIndex: 'lcbianhao_dictText'
        // },
        {
          title: '材料名称',
          align: 'center',
          dataIndex: 'cailiaono_dictText'
        },
        {
          title: '数量',
          align: 'center',
          dataIndex: 'jingzhongt',
          scopedSlots: { customRender: 'jingzhongt' }

        },
        {
          title:'使用量',//毛重(吨)
          align:"center",
          dataIndex: 'maozhongt'
        },
        {
          title:'库存',//皮重(吨)
          align:"center",
          dataIndex: 'pizhongt'
        },
        {
          title: '自检状态',
          align: 'center',
          dataIndex: 'jianyanstate_dictText',
          scopedSlots: { customRender: 'tags' },
        },
        {
          title: '监理抽检',
          align: 'center',
          dataIndex: 'choujianstate_dicText',
          scopedSlots: { customRender: 'tags2' },
        },
        {
          title: '报告附件',
          align: 'center',
          dataIndex: 'baogaofile',
          key: 'baogaofile',
          scopedSlots: { customRender: 'fileSlot' },
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
        list: '/wztaizhang/wztaizhang/listsp',
        delete: '/wztaizhang/wztaizhang/delete',
        deleteBatch: '/wztaizhang/wztaizhang/deleteBatch',
        exportXlsUrl: '/wztaizhang/wztaizhang/exportXls',
        importExcelUrl: 'wztaizhang/wztaizhang/importExcel',

      },
      dictOptions: {},
      superFieldList: [],
    }
  },
  created() {
    this.getToken()
    this.getSuperFieldList()
    this.shebeiList()
    this.liaocangList()
    this.cailiaoList()
  },
  computed: {
    importExcelUrl: function () {
      return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`
    },
  },
  methods: {
    handleUpload(record){
      this.$refs.modalUpload.title = "试验上传";
      this.$refs.modalUpload.edit(record);
    },
    handlePreview(record) {
      if (record && record.baogaofile) {
        let url = window._CONFIG['onlinePreviewDomainURL'] + '?url=' + encodeURIComponent(base64Encode(record.baogaofile))
        window.open(url, '_blank')
      }
    },
    shebeiList: function () {
      var sys_depart_orgcode = Vue.ls.get('SYS_DEPART_ORGCODE')
      usershebeiList({
        sys_depart_orgcode: sys_depart_orgcode,
        sbtypes: '27,63'
      }).then(res => {
        this.dictOption = []
        let result = res.result
        result.forEach(re => {
          this.dictOption.push({ text: re.sbname, value: re.sbjno })
        })
        //console.log(res)
      })
    },
    liaocangList: function () {
      var sys_depart_orgcode = Vue.ls.get('SYS_DEPART_ORGCODE')
      userliaocangList({
        sys_depart_orgcode: sys_depart_orgcode,
      }).then(res => {
        this.dictOption1 = []
        let result = res.result
        result.forEach(re => {
          this.dictOption1.push({ text: re.name, value: re.guid })
        })
        //console.log(res)
      })
    },
    cailiaoList: function () {
      var sys_depart_orgcode = Vue.ls.get('SYS_DEPART_ORGCODE')
      usercailiaoList({
        sys_depart_orgcode: sys_depart_orgcode,
      }).then(res => {
        this.dictOption2 = []
        let result = res.result
        result.forEach(re => {
          this.dictOption2.push({ text: re.cailiaoname, value: re.cailiaono })
        })
        // console.log(res)
      })
    },
    initDictConfig() {
    },
    getSuperFieldList() {
      let fieldList = []
      fieldList.push({ type: 'string', value: 'jinchangshijian', text: '进场时间' })
      fieldList.push({ type: 'string', value: 'shebeibianhao', text: '设备编号' })
      fieldList.push({ type: 'string', value: 'lcbianhao', text: '料仓编号' })
      fieldList.push({ type: 'string', value: 'cailiaono', text: '材料编号' })
      // fieldList.push({type:'string',value:'maozhongt',text:'毛重(吨)'})
      // fieldList.push({type:'string',value:'pizhongt',text:'皮重(吨)'})
      fieldList.push({ type: 'string', value: 'jingzhongt', text: '净重(吨)' })
      fieldList.push({ type: 'string', value: 'pici', text: '批次' })
      fieldList.push({ type: 'string', value: 'guigexinghao', text: '规格类型' })
      this.superFieldList = fieldList
    }
  }
}
</script>
<style scoped>
@import '~@assets/less/common.less';
</style>