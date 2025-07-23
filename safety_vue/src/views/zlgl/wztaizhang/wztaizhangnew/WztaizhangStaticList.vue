<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
<!--          <a-col :xl="6" :lg="7" :md="8" :sm="24">-->
<!--            <a-form-item label="供应商单位">-->
<!--              <j-search-select-tag placeholder="请选择供应商单位" v-model="queryParam.gongyingshangdanweibianma" :dictOptions="dictOption1">-->
<!--              </j-search-select-tag>-->
<!--              {{ selectValue1 }}-->
<!--            </a-form-item>-->
<!--          </a-col>-->
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="材料名称">
              <j-search-select-tag placeholder="请选择材料名称" v-model="queryParam.cailiaono" :dictOptions="dictOption2">
              </j-search-select-tag>
              {{ selectValue2 }}
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
<!--    <div class="table-operator">-->
<!--      <a-button @click="handleAdd" type="primary" icon="plus">新增</a-button>-->
<!--      <a-button type="primary" icon="download" @click="handleExportXls('wztaizhang_static')">导出</a-button>-->
<!--      <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl" @change="handleImportExcel">-->
<!--        <a-button type="primary" icon="import">导入</a-button>-->
<!--      </a-upload>-->
<!--      &lt;!&ndash; 高级查询区域 &ndash;&gt;-->
<!--      <j-super-query :fieldList="superFieldList" ref="superQueryModal" @handleSuperQuery="handleSuperQuery"></j-super-query>-->
<!--      <a-dropdown v-if="selectedRowKeys.length > 0">-->
<!--        <a-menu slot="overlay">-->
<!--          <a-menu-item key="1" @click="batchDel"><a-icon type="delete"/>删除</a-menu-item>-->
<!--        </a-menu>-->
<!--        <a-button style="margin-left: 8px"> 批量操作 <a-icon type="down" /></a-button>-->
<!--      </a-dropdown>-->
<!--    </div>-->

    <!-- table区域-begin -->
    <div>
      <div class="ant-alert ant-alert-info" style="margin-bottom: 16px;">
        <i class="anticon anticon-info-circle ant-alert-icon"></i> 已选择 <a style="font-weight: 600">{{ selectedRowKeys.length }}</a>项
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

        <template slot="htmlSlot" slot-scope="text">
          <div v-html="text"></div>
        </template>
        <template slot="imgSlot" slot-scope="text">
          <span v-if="!text" style="font-size: 12px;font-style: italic;">无图片</span>
          <img v-else :src="getImgView(text)" height="25px" alt="" style="max-width:80px;font-size: 12px;font-style: italic;"/>
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

          <a-divider type="vertical" />
          <a-dropdown>
            <a class="ant-dropdown-link">更多 <a-icon type="down" /></a>
            <a-menu slot="overlay">
              <a-menu-item>
                <a @click="handleDetail(record)">详情</a>
              </a-menu-item>
<!--              <a-menu-item>-->
<!--                <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">-->
<!--                  <a>删除</a>-->
<!--                </a-popconfirm>-->
<!--              </a-menu-item>-->
            </a-menu>
          </a-dropdown>
        </span>

      </a-table>
    </div>

    <wztaizhang-static-modal ref="modalForm" @ok="modalFormOk"></wztaizhang-static-modal>
  </a-card>
</template>

<script>

  import '@/assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import WztaizhangStaticModal from './modules/WztaizhangStaticModal'
  import Vue from 'vue'
  import { usercailiaoList } from '@api/api'
  import JSuperQuery from '@/components/jeecg/JSuperQuery.vue'

  export default {
    name: 'WztaizhangStaticList',
    mixins:[JeecgListMixin, mixinDevice],
    components: {
      JSuperQuery,
      WztaizhangStaticModal
    },
    data () {
      return {
        selectValue1: '',
        selectValue2: '',
        dictOption1: [],
        dictOption2: [],
        description: 'wztaizhang_static管理页面',
        // 表头
        columns: [
          {
            title: '#',
            dataIndex: '',
            key:'rowIndex',
            width:60,
            align:"center",
            customRender:function (t,r,index) {
              return parseInt(index)+1;
            }
          },
          {
            title: '组织机构',
            align: 'center',
            dataIndex: 'sysOrgCode_dictText'
          },
          {
            title:'材料名称',
            align:"center",
            dataIndex: 'cailiaono_dictText'
          },
          {
            title:'规格类型',
            align:"center",
            dataIndex: 'guigexinghao'
          },
          {
            title:'供应商单位',
            align:"center",
            dataIndex: 'gongyingshangdanweibianma_dictText'
          },
          {
            title:'进场总数量',
            align:"center",
            dataIndex: 'jingzhongt'
          },
          {
            title:'单位',
            align:"center",
            dataIndex: 'danwei'
          },
          {
            title:'批次总数',
            align:"center",
            dataIndex: 'picicount'
          },
          {
            title:'自检批次数',
            align:"center",
            dataIndex: 'jianyanstate'
          },
          {
            title:'抽检批次数',
            align:"center",
            dataIndex: 'choujianstate'
          },
          // {
          //   title: '操作',
          //   dataIndex: 'action',
          //   align:"center",
          //   fixed:"right",
          //   width:147,
          //   scopedSlots: { customRender: 'action' }
          // }
        ],
        url: {
          list: "/wztaizhangstatic/wztaizhangStatic/list",
          delete: "/wztaizhangstatic/wztaizhangStatic/delete",
          deleteBatch: "/wztaizhangstatic/wztaizhangStatic/deleteBatch",
          exportXlsUrl: "/wztaizhangstatic/wztaizhangStatic/exportXls",
          importExcelUrl: "wztaizhangstatic/wztaizhangStatic/importExcel",
          
        },
        dictOptions:{},
        superFieldList:[],
      }
    },
    created() {
    this.getSuperFieldList();
      this.cailiaoList()
    },
    computed: {
      importExcelUrl: function(){
        return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`;
      },
    },
    methods: {
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
      initDictConfig(){
      },
      getSuperFieldList(){
        let fieldList=[];
        fieldList.push({type:'string',value:'cailiaono',text:'材料编号'})
        fieldList.push({type:'string',value:'guigexinghao',text:'规格类型'})
        fieldList.push({type:'string',value:'gongyingshangdanweibianma',text:'供应商单位编码'})
        fieldList.push({type:'number',value:'jingzhongt',text:'jingzhongt'})
        fieldList.push({type:'int',value:'picicount',text:'picicount'})
        fieldList.push({type:'int',value:'jianyanstate',text:'jianyanstate'})
        fieldList.push({type:'int',value:'choujianstate',text:'choujianstate'})
        this.superFieldList = fieldList
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>