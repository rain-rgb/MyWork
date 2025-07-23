<template>
  <!-- 拌合站生产动态查询 -->
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="设备名称">
              <j-search-select-tag placeholder="请选择设备名称" v-model="queryParam.shebeiNo" :dictOptions="dictOption">
              </j-search-select-tag>
              {{ selectValue }}
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="闭合情况">
              <j-search-select-tag
                placeholder="请选择超标等级"
                v-model="queryParam.isBiHe"
                :dictOptions="dictOption2"
              ></j-search-select-tag>
            </a-form-item>
          </a-col>

          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="生产时间范围">
              <j-date
                placeholder="开始生产时间"
                v-model="queryParam.productDatetime_begin"
                :showTime="true"
                dateFormat="YYYY-MM-DD HH:mm:ss"
              />
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="">
              <j-date
                placeholder="结束生产时间"
                v-model="queryParam.productDatetime_end"
                :showTime="true"
                dateFormat="YYYY-MM-DD HH:mm:ss"
              />
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="超标等级">
              <j-dict-select-tag
                placeholder="请选择超标等级"
                v-model="queryParam.overLevel"
                dictCode="over_level"
              ></j-dict-select-tag>
            </a-form-item>
          </a-col>
<!--          <a-col :xl="6" :lg="7" :md="8" :sm="24">-->
<!--            <a-form-item label="强度等级">-->
<!--              <j-search-select-tag placeholder="请选择强度等级" v-model="queryParam.strengthRank" :dictOptions="dictOption1"-->
<!--                                   @change="getQddj">-->
<!--              </j-search-select-tag>-->
<!--            </a-form-item>-->
<!--          </a-col>-->
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="浇筑部位">
              <a-input placeholder="请输入浇筑部位" v-model="poureLocation1" @change = "poureLocation">
              </a-input>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="浇筑令" >
              <a-checkbox  @change = "isjz('2')" > 未使用</a-checkbox>
            </a-form-item>
          </a-col>
          <!--          <a-col :xl="6" :lg="7" :md="8" :sm="24">-->
          <!--            <a-form-item label="切换工程">-->
          <!--              <j-dict-select-tag placeholder="请选择工程" v-model="queryParam.poureLocation" dictCode="RCXM"></j-dict-select-tag>-->
          <!--            </a-form-item>-->
          <!--          </a-col>-->
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <span style="float: left; overflow: hidden" class="table-page-search-submitButtons">
              <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
              <a-button type="primary" @click="searchReset" icon="reload" style="margin-left: 8px">重置</a-button>
              <!-- <a @click="handleToggleSearch" style="margin-left: 8px">
                {{ toggleSearchStatus ? '收起' : '展开' }}
                <a-icon :type="toggleSearchStatus ? 'up' : 'down'"/>
              </a> -->
            </span>
          </a-col>
        </a-row>
      </a-form>
    </div>
    <!-- 查询区域-END -->

    <!-- 操作按钮区域 -->
    <div class="table-operator">
      <a-button @click="handleAdd" v-has="'hntbhz:add'" type="primary" icon="plus">新增</a-button>
      <a-button type="primary" icon="download" @click="handleExportXls('拌合站主表')">导出</a-button>
      <!--      <a-button type="primary" v-has="'hntbhz:dc'" icon="download" @click="handleExportXls('拌合站主表')">导出</a-button>-->
      <a-upload
        name="file"
        :showUploadList="false"
        :multiple="false"
        :headers="tokenHeader"
        :action="importExcelUrl"
        @change="handleImportExcel"
      >
        <a-button type="primary" v-has="'hntbhz:dr'" icon="import">导入</a-button>
      </a-upload>
      <!-- 禁用高级查询区域 -->
      <!--      <j-super-query :fieldList="superFieldList" ref="superQueryModal" @handleSuperQuery="handleSuperQuery"></j-super-query>-->
      <!--      <a-dropdown v-if="selectedRowKeys.length > 0">-->
      <!--        <a-menu slot="overlay">-->
      <!--          <a-menu-item key="1" v-has="'hntbhz:del'" @click="batchDel"><a-icon type="delete"/>删除</a-menu-item>-->
      <!--        </a-menu>-->
      <!--        <a-button style="margin-left: 8px"> 批量操作 <a-icon type="down" /></a-button>-->
      <!--      </a-dropdown>-->
    </div>

    <!-- table区域-begin -->
    <div>
      <div class="ant-alert ant-alert-info" style="margin-bottom: 16px">
        <i class="anticon anticon-info-circle ant-alert-icon"></i> 已选择
        <a style="font-weight: 600">{{ selectedRowKeys.length }}</a
        >项
        <a style="margin-left: 24px" @click="onClearSelected">清空</a>
      </div>

      <a-table
        ref="table"
        size="middle"
        bordered
        rowKey="id"
        class="j-table-force-nowrap"
        :scroll="{ x: true }"
        :columns="columns"
        :dataSource="dataSource"
        :pagination="ipagination"
        :loading="loading"
        :rowSelection="{ selectedRowKeys: selectedRowKeys, onChange: onSelectChange }"
        @change="handleTableChange"
      >
        <span slot="tags" slot-scope="tags, record">
          <a-tag color="green" v-if="record.overLevel == '0'">合格</a-tag>
          <a-tag color="orange" v-if="record.overLevel == '1'">初级超标</a-tag>
          <a-tag color="yellow" v-if="record.overLevel == '2'">中级超标</a-tag>
          <a-tag color="red" v-if="record.overLevel == '3'">高级超标</a-tag>
        </span>
        <span v-if="record.overLevel != '0'&&record.overLevel != null" slot="tags1" slot-scope="tags1, record">
          <a-tag color="green" v-if="record.overproofStatus === 20">已闭合</a-tag>
          <a-tag color="red" v-if="record.overproofStatus !== 20">未闭合</a-tag>
        </span>

        <span slot="isjzl" slot-scope="isjzl, record">
          <a-tag color="green" v-if="record.isjzl == 1 ">已使用</a-tag>
          <a-tag color="grey" v-else-if="record.isjzl == 0 ">数据关联中</a-tag>
          <a-tag color="blue" v-else-if="record.isjzl == 28">已关联</a-tag>
          <a-tag color="blue" v-else-if="record.isjzl == 29">关联通过</a-tag>
          <a-tag color="orange" v-else-if="record.isjzl == 40">使用异常</a-tag>
          <a-tag v-else-if="record.isjzl == 30" color="red" v-else>未使用</a-tag>
        </span>


        <span slot="formulaNo" slot-scope="formulaNo, record">
          <a-tag color="green">{{ record.formulaNo }}</a-tag>
        </span>
        <template slot="htmlSlot" slot-scope="text">
          <div v-html="text"></div>
        </template>
        <template slot="imgSlot" slot-scope="text">
          <span v-if="!text" style="font-size: 12px; font-style: italic">无图片</span>
          <img
            v-else
            :src="getImgView(text)"
            height="25px"
            alt=""
            style="max-width: 80px; font-size: 12px; font-style: italic"
          />
        </template>
        <template slot="fileSlot" slot-scope="text">
          <span v-if="!text" style="font-size: 12px; font-style: italic">无文件</span>
          <a-button v-else :ghost="true" type="primary" icon="download" size="small" @click="downloadFile(text)">
            下载
          </a-button>
        </template>

        <span slot="action" slot-scope="text, record">
          <a @click="handleDetail(record)">详情</a>
          <!--          <a v-has="'hntbhz:edit'" @click="handleEdit(record)">编辑</a>-->

          <!--          <a-divider type="vertical"/>-->
          <!--          <a-dropdown>-->
          <!--            <a class="ant-dropdown-link">更多 <a-icon type="down"/></a>-->
          <!--            <a-menu slot="overlay">-->
          <!--              <a-menu-item>-->
          <!--                <a @click="handleDetail(record)">详情</a>-->
          <!--              </a-menu-item>-->
          <!--              <a-menu-item v-has="'hntbhz:del'">-->
          <!--                <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">-->
          <!--                  <a>删除</a>-->
          <!--                </a-popconfirm>-->
          <!--              </a-menu-item>-->
          <!--            </a-menu>-->
          <!--          </a-dropdown>-->
        </span>
      </a-table>
    </div>
    <bhz-cement-base-modal ref="modalForm" @ok="modalFormOk"/>
  </a-card>
</template>

<script>
import { JeecgListMixin } from '@/mixins/JeecgListMixin'
import BhzCementBaseModal from './modules/BhzCementBaseModal'
import { filterMultiDictText } from '@/components/dict/JDictSelectUtil'
import '@/assets/less/TableExpand.less'
import JSuperQuery from '@/components/jeecg/JSuperQuery.vue'
import { handertoken } from '@/mixins/getHanderToken'
import { usershebeiList } from '@/api/api'
import { SYS_DEPART_ORGCODE } from '@/store/mutation-types'
import Vue from 'vue'
import { getAction } from '@api/manage'

export default {
  name: 'BhzCementBaseListRC',
  mixins: [JeecgListMixin, handertoken],
  components: {
    BhzCementBaseModal,
    JSuperQuery,
  },
  data() {
    return {
      poureLocation1:'',
      isusejz:'',
      selectValue: '',
      asyncSelectValue: '',
      dictOption: [],
      dictOption1: [],
      dictOption2: [
        { text: '未闭合', value: '0' },
        { text: '已闭合', value: '1' },
      ],
      description: '拌合站主表管理页面',
      queryParam: {
        // sys_depart_project:'this.sys_depart_project'
      },
      // 表头
      columns: [
        {
          title: '序号',
          dataIndex: '',
          key: 'rowIndex',
          width: 60,
          align: 'center',
          customRender: function (t, r, index) {
            return parseInt(index) + 1
          },
        },
        {
          title: '标段',
          align: 'center',
          dataIndex: 'departName',
        },
        {
          title: '设备名称',
          align: 'center',
          dataIndex: 'shebeiNo',
        },
        {
          title: '施工配合比',
          align: 'center',
          dataIndex: 'formulaNo',
        },
        {
          title: '生产时间',
          align: 'center',
          dataIndex: 'productDatetime',
        },
        {
          title: '采集时间',
          align: 'center',
          dataIndex: 'collectDatetime',
        },
        {
          title: '浇筑部位',
          align: 'center',
          dataIndex: 'poureLocation',
        }, {
          title: '工程名称',
          align: 'center',
          dataIndex: 'projectName',
        },
        {
          title: '强度等级',
          align: 'center',
          dataIndex: 'strengthRank',
        },
        {
          title: '超标等级',
          align: 'center',
          dataIndex: 'overLevel_dictText', //overLevel_dictText
          key: 'overLevel_dictText',
          scopedSlots: { customRender: 'tags' },
        },

        {
          title: '超标参数',
          align: 'center',
          dataIndex: 'overParameter',
        },
        {
          title: '超标比例',
          align: 'center',
          dataIndex: 'overRatio',
        },

        {
          title: '闭合情况',
          align: 'center',
          dataIndex: 'overproofStatus',
          scopedSlots: { customRender: 'tags1' },
        },
        {
          title: '闭合时间',
          align: 'center',
          dataIndex: 'bhTime',
        },
        {
          title: '浇筑令',
          align: 'center',
          dataIndex: 'isjzl',
          key: 'isjzl',
          scopedSlots: { customRender: 'isjzl' },
        },

        {
          title: '操作',
          dataIndex: 'action',
          align: 'center',
          fixed: 'right',
          width: 100,
          scopedSlots: { customRender: 'action' },
        },
      ],
      isorter: {
        column: 'productDatetime',
        order: 'desc',
      },
      url: {
        list: '/hntbhz/bhzCementBase/productionList',
        // list: '/hntbhz/bhzCementBase/listrc',
        delete: '/hntbhz/bhzCementBase/delete',
        deleteBatch: '/hntbhz/bhzCementBase/deleteBatch',
        exportXlsUrl: '/hntbhz/bhzCementBase/listProduce',
        importExcelUrl: 'hntbhz/bhzCementBase/importExcel',
        getQddjList: 'hntbhz/bhzCementBase/getQddjList',
      },
      dictOptions: {},
      superFieldList: [],
    }
  },
  created() {
    this.getSuperFieldList()
    this.shebeiList()
    // this.getQddj()
  },
  computed: {
    importExcelUrl: function () {
      return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`
    },
  },

  methods: {
    isjz(a) {
      console.log("-----------------"+a)
      if(a === '2'){
        this.queryParam.renwudanstatus_begin = 10
        this.queryParam.renwudanstatus_end = 30
      }
    },
    poureLocation() {
      this.queryParam.poureLocation = '*'+ this.poureLocation1+'*'
    },
    searchQuery1() {
      this.loadData(1)
    },
    shebeiList: function () {
      var sys_depart_orgcode = Vue.ls.get('SYS_DEPART_ORGCODE')
      usershebeiList({
        sys_depart_orgcode: sys_depart_orgcode,
        sbtypes: '0',
      }).then((res) => {
        this.dictOption = []
        let result = res.result
        result.forEach((re) => {
          this.dictOption.push({ text: re.sbname, value: re.sbjno })
        })
        //console.log(res)
      })
    },
    getQddj: function () {
      var sys_depart_orgcode = Vue.ls.get('SYS_DEPART_ORGCODE')
      var params = {
        sys_depart_orgcode: sys_depart_orgcode
      }
      getAction(this.url.getQddjList, params).then(res => {
        this.dictOption1 = []
        let result = res.result
        result.forEach(re => {
          this.dictOption1.push({ text: re.strengthRank, value: re.strengthRank })
        })
      })
    },
    initDictConfig() {
    },
    getSuperFieldList() {
    },
  }
  ,
}
</script>
<style scoped>
@import '~@assets/less/common.less';
</style>