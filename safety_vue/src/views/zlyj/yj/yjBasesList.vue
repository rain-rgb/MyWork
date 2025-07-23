<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="设备名称">
              <j-search-select-tag placeholder="请选择设备名称" v-model="queryParam.deviceno" :dictOptions="dictOption" >
              </j-search-select-tag>
              {{ selectValue }}
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="是否合格">
              <j-dict-select-tag placeholder="请选择是否合格" v-model="queryParam.result"
                                 dictCode="result"></j-dict-select-tag>
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
      <a-button type="primary" icon="download" v-has="'syj:dc'" @click="handleExportXls('压浆主表')">导出</a-button>
      <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl"
                @change="handleImportExcel">
        <a-button type="primary" v-has="'syj:dr'" icon="import">导入</a-button>
      </a-upload>
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
        <a-tag color="green" v-if="record.result == '0'">合格</a-tag>
         <a-tag color="red" v-if="record.result == '1'">不合格</a-tag>
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

    <yjBasesModal ref="modalForm" @ok="modalFormOk"/>
  </a-card>
</template>

<script>

  import {JeecgListMixin} from '@/mixins/JeecgListMixin'
  import JDictSelectTag from '@/components/dict/JDictSelectTag.vue'
  import {filterMultiDictText} from '@/components/dict/JDictSelectUtil'
  import '@/assets/less/TableExpand.less'
  import JSuperQuery from '@/components/jeecg/JSuperQuery.vue'
  import yjBasesModal from './modules/yjBasesModal'
  import { handertoken } from '@/mixins/getHanderToken'
  import { usershebeiList } from '@api/api'
  import Vue from 'vue'
  import { SYS_DEPART_ORGCODE } from '@/store/mutation-types'

  export default {
    name: "yjBasesList",
    mixins: [JeecgListMixin,handertoken],
    components: {
      JDictSelectTag,
      yjBasesModal,
      JSuperQuery
    },
    data() {
      return {
        selectValue: '',
        asyncSelectValue: '',
        dictOption: [],
        description: '压浆主表管理页面',
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
          // {
          //   title: '所属组织机构',
          //   align: "center",
          //   dataIndex: 'sysOrgCode_dictText'
          // },
          {
              title: '设备名称',
              align: "center",
              dataIndex: 'deviceno_dictText',
          },
          {
            title: '桩/桥号 ',
            align: "center",
            dataIndex: 'steelbeamno'
          },
          {
            title: '梁号 ',
            align: "center",
            dataIndex: 'componentparts'
          },
          {
            title: '压浆剂 ',
            align: "center",
            dataIndex: 'pulpingorientation'
          },
          {
            title: '水泥名称 ',
            align: "center",
            dataIndex: 'pulpingorder'
          },
          {
            title: '压浆时间(s)',
            align: "center",
            dataIndex: 'fluidity'
          },
          {
            title: '值班人员',
            align: "center",
            dataIndex: 'waterratio'
          },
          {
            title: '孔道数 ',
            align: "center",
            dataIndex: 'stirringtime'
          },
          {
            title: '进浆量 ',
            align: "center",
            dataIndex: 'pulpingvolume'
          },
          {
            title: '步骤次数 ',
            align: "center",
            dataIndex: 'enddate'
          },
          {
            title: '步骤参数 ',
            align: "center",
            dataIndex: 'standardvolume'
          },
          {
            title: '初始流动速度',
            align: "center",
            dataIndex: 'pulpingpressureout'
          },
          {
            title: '流动度(s) ',
            align: "center",
            dataIndex: 'engineeringsite'
          },
          {
            title: '是否合格',
            align: "center",
            dataIndex: 'result',
            key:'result',
            scopedSlots: { customRender: 'tags' },
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
          list: "/zl/YaJiang/list",
          delete: "/zl/YaJiang/delete",
          deleteBatch: "/zl/YaJiang/deleteBatch",
          exportXlsUrl: "/zl/YaJiang/exportXls",
          importExcelUrl: "zl/YaJiang/importExcel",
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
          sbtypes:'10'
        }).then(res=>{
          //console.log(res)
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
        let fieldList = [];
        fieldList.push({type: 'string', value: 'projectid', text: 'uuid', dictCode: ''})
        fieldList.push({type: 'string', value: 'uuid', text: 'token', dictCode: ''})
        fieldList.push({type: 'string', value: 'token', text: '设备编号', dictCode: ''})
        fieldList.push({type: 'string', value: 'platformdeviceid', text: '试验类型', dictCode: ''})
        fieldList.push({type: 'string', value: 'vendorno', text: '委托编号', dictCode: ''})
        fieldList.push({type: 'string', value: 'deviceno', text: '试件编号', dictCode: ''})
        fieldList.push({type: 'string', value: 'componentid', text: '制件日期', dictCode: ''})
        fieldList.push({type: 'string', value: 'componentparts', text: '试验日期', dictCode: ''})
        fieldList.push({type: 'string', value: 'beamtype', text: '试验完成时间'})
        fieldList.push({type: 'int', value: 'concretestrength', text: '龄期', dictCode: ''})
        fieldList.push({type: 'string', value: 'modulusofelasticity', text: '试件尺寸', dictCode: ''})
        fieldList.push({type: 'string', value: 'steelbeamno', text: '试件面积', dictCode: ''})
        fieldList.push({type: 'string', value: 'steelstrand', text: '试件数量', dictCode: ''})
        fieldList.push({type: 'string', value: 'pulpingorientation', text: '设计强度', dictCode: ''})
        fieldList.push({type: 'string', value: 'pulpingorder', text: '折算系数', dictCode: ''})
        fieldList.push({type: 'string', value: 'flowvelocity', text: '强度代表值', dictCode: ''})
        fieldList.push({type: 'string', value: 'mixproportion', text: '操作人员', dictCode: ''})
        fieldList.push({type: 'string', value: 'waterratio', text: '工程名称', dictCode: ''})
        fieldList.push({type: 'string', value: 'stirringtime', text: '品种编码', dictCode: ''})
        this.superFieldList = fieldList
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>