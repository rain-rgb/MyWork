<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="设备名称">
              <j-search-select-tag placeholder="请选择设备名称" v-model="queryParam.shebeino" :dictOptions="dictOption" >
              </j-search-select-tag>
              {{ selectValue }}
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="上传时间范围">
              <j-date placeholder="开始时间" v-model="queryParam.gatherdate_begin" :showTime="true"
                      dateFormat="YYYY-MM-DD HH:mm:ss"/>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="">
              <j-date placeholder="结束时间" v-model="queryParam.gatherdate_end" :showTime="true"
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
      <a-button @click="handleAdd" v-has="'syj:add'" type="primary" icon="plus">新增</a-button>
      <a-button type="primary" icon="download" v-has="'syj:dc'" @click="handleExportXls('标养室主表')">导出</a-button>
      <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl"
                @change="handleImportExcel">
        <a-button type="primary" v-has="'syj:dr'" icon="import">导入</a-button>
      </a-upload>
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
        bordered
        rowKey="id"
        class="j-table-force-nowrap"
        :scroll="{x:true}"
        :columns="columns"
        :dataSource="dataSource"
        :pagination="ipagination"
        :loading="loading"
        :rowSelection="{selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"
        @change="handleTableChange"
        :customRow="handleClick"
        :rowClassName="setRowClassName"
      >

       <span slot="tags" slot-scope="tags, record">
          <a-tag color="green" v-if="record.temperature >= 18 || record.temperature <= 22">正常</a-tag>
          <a-tag color="red" v-else >异常</a-tag>
        </span>

        <span slot="tags1" slot-scope="tags1, record">
<!--          <a-tag color="orange" v-if="record.humstatus == '0'">未检测</a-tag>-->
          <a-tag color="green" v-if="record.humidity >= 95">正常</a-tag>
          <a-tag color="red" v-if="record.humidity < 95">异常</a-tag>
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
          <a  v-has="'syj:edit'" @click="handleEdit(record)">编辑</a>

          <a-divider type="vertical"/>
          <a-dropdown>
            <a class="ant-dropdown-link">更多 <a-icon type="down"/></a>
            <a-menu slot="overlay">
              <a-menu-item>
                <a @click="handleDetail(record)">详情</a>
              </a-menu-item>
              <a-menu-item v-has="'syj:del'">
                <a-popconfirm  title="确定删除吗?" @confirm="() => handleDelete(record.id)">
                  <a>删除</a>
                </a-popconfirm>
              </a-menu-item>
            </a-menu>
          </a-dropdown>
        </span>

      </a-table>
    </div>

    <BysSjjkModal ref="modalForm" @ok="modalFormOk"/>
  </a-card>
</template>

<script>

  import {JeecgListMixin} from '@/mixins/JeecgListMixin'
  import JDictSelectTag from '@/components/dict/JDictSelectTag.vue'
  import {filterMultiDictText} from '@/components/dict/JDictSelectUtil'
  import '@/assets/less/TableExpand.less'
  import JSuperQuery from '@/components/jeecg/JSuperQuery.vue'
  import BysSjjkModal from './modules/BysSjjkModal'
  import { handertoken } from '@/mixins/getHanderToken'
  import Vue from 'vue'
  import { SYS_DEPART_ORGCODE } from '@/store/mutation-types'
  import { usershebeiList } from '@api/api'
  export default {
    name: "BysSjjkBasesList",
    mixins: [JeecgListMixin,handertoken],
    components: {
      JDictSelectTag,
      BysSjjkModal,
      JSuperQuery
    },
    data() {
      return {
        selectValue: '',
        asyncSelectValue: '',
        dictOption: [],
        description: '标养室主表管理页面',
        // 表头
        columns: [
          {
            title: '序号',
            dataIndex: '',
            key: 'rowIndex',
            width: 60,
            align: "center",
            customRender: function (t, r, index) {
              return parseInt(index) + 1;
            }
          },
          {
              title: '设备名称',
              align: "center",
              dataIndex: 'shebeino_dictText',
          },
          {
            title: '上传时间 ',
            align: "center",
            dataIndex: 'gatherdate'
          },
          {
            title: '温度℃',
            align: "center",
            dataIndex: 'temperature'
          },
          {
            title: '湿度%',
            align: "center",
            dataIndex: 'humidity'
          },
          {
            title: '温度（18℃~22℃）',
            align: "center",
            dataIndex: 'temstatus',
            scopedSlots: { customRender: 'tags' },
          },
          {
            title: '湿度（≥95%）',
            align: "center",
            dataIndex: 'humstatus',
            scopedSlots: { customRender: 'tags1' },
          },
          {
            title: '操作',
            align: "center",
            fixed: "right",
            width: 147,
            scopedSlots: {customRender: 'action'},
          }
        ],
        url: {
          list: "/bys/bysReal/list",
          delete: "/bys/bysReal/delete",
          deleteBatch: "/bys/bysReal/deleteBatch",
          exportXlsUrl: "/bys/bysReal/exportXls",
          importExcelUrl: "bys/bysReal/importExcel",
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
        return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`;
      }
    },
    methods: {
      shebeiList:function (){
        var sys_depart_orgcode=Vue.ls.get('SYS_DEPART_ORGCODE');
        usershebeiList({
          sys_depart_orgcode:sys_depart_orgcode,
          sbtypes:'11'
        }).then(res=>{
          this.dictOption=[];
          let result=res.result;
          result.forEach(re=>{
            this.dictOption.push({text:re.sbname,value:re.sbjno})
          })
          //console.log(res)
        })
      },
      initDictConfig() {
      },
      getSuperFieldList() {
        let fieldList = [];
        fieldList.push({type: 'int', value: 'id', text: '', dictCode: ''})
        fieldList.push({type: 'Date', value: 'gatherDate', text: '上传时间', dictCode: ''})
        fieldList.push({type: 'string', value: 'shebeiNo', text: '设备编号', dictCode: ''})
        fieldList.push({type: 'string', value: 'temperature', text: '温度', dictCode: ''})
        fieldList.push({type: 'string', value: 'humidity', text: '湿度', dictCode: ''})
        fieldList.push({type: 'string', value: 'guid', text: '唯一标识', dictCode: ''})
        fieldList.push({type: 'string', value: 'forwardstatus', text: '数据转发状态', dictCode: ''})

        this.superFieldList = fieldList
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>