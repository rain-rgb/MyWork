<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="取样日期">
              <j-date placeholder="取样日期" v-model="queryParam.sampleDate" :showTime="true"
                      dateFormat="YYYY-MM-DD"/>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="报告编号">
              <a-input placeholder="请输入报告编号" v-model="queryParam.reportNo"></a-input>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="使用部位">
              <a-input placeholder="请输入使用部位" v-model="queryParam.sampleGcbw"></a-input>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="试验类型">
              <sample-type v-model="queryParam.titCode" placeholder="请选择试验类型" :type="type"></sample-type>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row :gutter="24">
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="试验状态">
              <j-dict-select-tag placeholder="请选择试验状态" v-model="queryParam.sampleState"
                                 dictCode="samplePHBState"></j-dict-select-tag>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="只看自己">
              <a-switch v-model="queryParam.lookself" default-checked/>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
              <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
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
      <a-button @click="copyData" type="primary" icon="snippets">复制</a-button>
      <a-button @click="insertData" type="primary" icon="arrow-down">插入</a-button>
      <a-button @click="commitShenhe" type="primary" icon="arrow-up">提交审核</a-button>
      <a-button @click="updateToSampleno" type="primary" icon="carry-out">修改编号</a-button>
      <a-button @click="del" type="primary" icon="delete">删除</a-button>

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

        <span slot="tags" slot-scope="tags, record">
          <a-tag v-if="record.sampleState == 0">待检</a-tag>
          <a-tag v-if="record.sampleState == 1">在检</a-tag>
          <a-tag v-if="record.sampleState == 2">已检</a-tag>
       </span>

        <template slot="reportno" slot-scope="reportno">
          <a @click="rePrint(reportno)">{{ reportno }}</a>
        </template>

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

    <sy-dps-sy-sample-p-h-b-modal ref="modalForm" @ok="modalFormOk"></sy-dps-sy-sample-p-h-b-modal>
    <sample-copy-date ref="sample"></sample-copy-date>
    <report-form ref="reform"></report-form>
    <sample-update-sampleno ref="updateSampleno"></sample-update-sampleno>
    <insert-data ref="insertData" @ok="modalFormOk"></insert-data>
    <sample-delete ref="delSample"></sample-delete>
  </a-card>
</template>

<script>

import '@/assets/less/TableExpand.less'
import { mixinDevice } from '@/utils/mixin'
import { JeecgListMixin } from '@/mixins/JeecgListMixin'
import SyDpsSySamplePHBModal from './modules/SyDpsSySamplePHBModal'
import SampleType from '@/views/sy/dpssysample/modules/SampleType'
import SampleCopyDate from './modules/SampleCopyDate'
import InsertData from '@/views/sy/dpssysample/modules/InsertData'
import SampleUpdateSampleno from '@views/sy/dpssysample/modules/SampleUpdateSampleno'
import ReportForm from '@/views/syrj/reportform/ReportForm'
import SampleDelete from '@views/sy/dpssysample/modules/SampleDelete'
import { getAction } from '@api/manage'

export default {
  name: 'SyDpsSySamplePHBList',
  mixins: [JeecgListMixin, mixinDevice],
  components: {
    SyDpsSySamplePHBModal,
    SampleCopyDate,
    InsertData,
    SampleUpdateSampleno,
    SampleType,
    ReportForm,
    SampleDelete
  },
  data() {
    return {
      type: 2,
      description: 'sy_dps_sy_sample管理页面',
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
        //   title: '样品编号',
        //   align: 'center',
        //   dataIndex: 'sampleno'
        // },
        // {
        //   title: '样品名称',
        //   align: 'center',
        //   dataIndex: 'samplename'
        // },
        // {
        //   title: '取样地点',
        //   align: 'center',
        //   dataIndex: 'samplequyangdidian'
        // },
        {
          title: '报告编号',
          align: 'center',
          dataIndex: 'reportNo',
          scopedSlots: { customRender: 'reportno' },
        },
        {
          title: '使用部位',
          align: 'center',
          dataIndex: 'sampleGcbw'
        },
        // {
        //   title:'样品描述',
        //   align:"center",
        //   dataIndex: 'sampledescribe'
        // },

        // {
        //   title:'产地',
        //   align:"center",
        //   dataIndex: 'samplechandi'
        // },
        // {
        //   title:'代表数量，试验组数',
        //   align:"center",
        //   dataIndex: 'sampledaibiaoshuliang'
        // },
        // {
        //   title:'规格型号',
        //   align:"center",
        //   dataIndex: 'sampleguigexinghao'
        // },
        // {
        //   title: '取样人',
        //   align: 'center',
        //   dataIndex: 'samplequyangren'
        // },
        // {
        //   title:'生产厂家（水泥，粉煤灰,沥青用到）',
        //   align:"center",
        //   dataIndex: 'sampleshengchanchangjia'
        // },
        // {
        //   title:'出厂编号（水泥样品用到）',
        //   align:"center",
        //   dataIndex: 'samplechuchangbianhao'
        // },
        // {
        //   title:'强度等级，使用数据字典（水泥样品用到）',
        //   align:"center",
        //   dataIndex: 'sampleqiangdudengji'
        // },
        // {
        //   title:'搅拌方式，使用数据字典',
        //   align:"center",
        //   dataIndex: 'samplejiaobanfangshi'
        // },
        // {
        //   title:'混凝土种类，使用材料字典',
        //   align:"center",
        //   dataIndex: 'samplehunningtuzhonglei'
        // },
        // {
        //   title:'养护条件，使用材料字典',
        //   align:"center",
        //   dataIndex: 'sampleyanghutiaojian'
        // },
        // {
        //   title:'砂浆种类',
        //   align:"center",
        //   dataIndex: 'sampleshajiangzhonglei'
        // },
        // {
        //   title:'备注',
        //   align:"center",
        //   dataIndex: 'sampleremark'
        // },
        // {
        //   title:'外键，组织机构id(T_S_depart)',
        //   align:"center",
        //   dataIndex: 'departid'
        // },
        // {
        //   title:'外键，组织机构编码（T_S_depart）',
        //   align:"center",
        //   dataIndex: 'orgcode'
        // },
        // {
        //   title:'是否删除（0：正常1：已删除）',
        //   align:"center",
        //   dataIndex: 'sampleisdel'
        // },

        // {
        //   title:'创建日期（yyyy-MM-dd）',
        //   align:"center",
        //   dataIndex: 'samplecreatedate'
        // },
        {
          title: '试验状态',
          align: 'center',
          dataIndex: 'sampleState',
          scopedSlots: { customRender: 'tags' }
        },
        {
          title: '取样日期',
          align: 'center',
          dataIndex: 'sampleDate'
        },
        // {
        //   title:'样品所属机构,当前登录用户所属机构，可选',
        //   align:"center",
        //   dataIndex: 'sampledepartid'
        // },
        // {
        //   title:'生产日期（外加剂，粉煤灰用到）',
        //   align:"center",
        //   dataIndex: 'sampleshengchanriqi'
        // },
        // {
        //   title:'生产批号（外加剂,粉煤灰用到）',
        //   align:"center",
        //   dataIndex: 'sampleshengchanpihao'
        // },
        // {
        //   title:'品种等级（石灰，粉煤灰用到）',
        //   align:"center",
        //   dataIndex: 'samplepinzhongdengji'
        // },
        // {
        //   title:'设计计量（%）',
        //   align:"center",
        //   dataIndex: 'sampleshejijiliang'
        // },
        // {
        //   title:'结合料种类',
        //   align:"center",
        //   dataIndex: 'samplejieheliaozhonglei'
        // },
        // {
        //   title:'沥青标号',
        //   align:"center",
        //   dataIndex: 'sampleliqingbiaohao'
        // },
        // {
        //   title:'沥青种类',
        //   align:"center",
        //   dataIndex: 'sampleliqingzhonglei'
        // },
        // {
        //   title:'级配类型',
        //   align:"center",
        //   dataIndex: 'samplejipeileixing'
        // },
        // {
        //   title:'试验层位',
        //   align:"center",
        //   dataIndex: 'sampleshiyancengwei'
        // },
        // {
        //   title:'沥青混合料类型',
        //   align:"center",
        //   dataIndex: 'sampleliqinghunheliaoleixing'
        // },
        // {
        //   title:'钢筋直径',
        //   align:"center",
        //   dataIndex: 'samplegangjinzhijing'
        // },
        // {
        //   title:'钢筋种类，使用数据字典',
        //   align:"center",
        //   dataIndex: 'samplegangjinzhonglei'
        // },
        // {
        //   title:'龄期 ',
        //   align:"center",
        //   dataIndex: 'samplelingqi'
        // },
        // {
        //   title:'外加剂掺量',
        //   align:"center",
        //   dataIndex: 'samplewaijiajichanliang'
        // },
        // {
        //   title:'外键，试验项目类型（dps_jc_testItemType表）',
        //   align:"center",
        //   dataIndex: 'titcode'
        // },
        // {
        //   title:'现场是否完成试验0：未完成1：已完成 默认给0',
        //   align:"center",
        //   dataIndex: 'mstatus'
        // },
        // {
        //   title:'见证取样 0：普通 1：见证取样 默认给0',
        //   align:"center",
        //   dataIndex: 'plusmark'
        // },
        // {
        //   title:'样品试验组数，默认值（水泥4，水泥混凝土1，钢筋1），水泥混凝土和钢筋在界面允许修改',
        //   align:"center",
        //   dataIndex: 'sampleshiyanzushu'
        // },
        // {
        //   title:'自定义样品编号（雷榕项目）',
        //   align:"center",
        //   dataIndex: 'samplenonew'
        // },

        // {
        //   title:'自定义记录编号（雷榕项目）',
        //   align:"center",
        //   dataIndex: 'tablenumbernew'
        // },
        // {
        //   title: '试验日期',
        //   align: 'center',
        //   dataIndex: 'reportdate'
        // },
        {
          title: '编辑人',
          align: 'center',
          dataIndex: 'sampleCreatePerson'
        },
        // {
        //   title:'insertno',
        //   align:"center",
        //   dataIndex: 'insertno'
        // },
        // {
        //   title:'试验结果 0：合格，1：不合格',
        //   align:"center",
        //   dataIndex: 'syjg'
        // },
        // {
        //   title:'不合格原因，由报表自动判断存储',
        //   align:"center",
        //   dataIndex: 'buhegeyuanyin'
        // },
        // {
        //   title:'是否留样 0：不留 1：留  默认给0',
        //   align:"center",
        //   dataIndex: 'shifouliuyang'
        // },
        // {
        //   title:'留样日期（YYYY-MM-dd）如果留样则填写',
        //   align:"center",
        //   dataIndex: 'liuyangriqi'
        // },
        // {
        //   title:'留样期限（天）如果留样则填写',
        //   align:"center",
        //   dataIndex: 'liuyangqixian'
        // },
        // {
        //   title:'留样处理情况   如果留样则填写',
        //   align:"center",
        //   dataIndex: 'liuyangchuli'
        // },
        // {
        //   title:'审批状态 0：未提交 1：未审核 2：审核通过  3：审核退回',
        //   align:"center",
        //   dataIndex: 'shenpizhuangtai'
        // },
        // {
        //   title:'记录表签章状态   默认-1：未启动签章',
        //   align:"center",
        //   dataIndex: 'jilubiaoqianzhangzhuangtai'
        // },
        // {
        //   title:'报告签章状态  默认-1：未启动签章',
        //   align:"center",
        //   dataIndex: 'baogaoqianzhangzhuangtai'
        // },
        // {
        //   title:'报验单签章状态  默认-1：未启动签章 0:无报验单        1/3',
        //   align:"center",
        //   dataIndex: 'baoyandanqianzhangzhuangtai'
        // },
        // {
        //   title:'审批表签章状态  默认-1：未启动签章 0：无审批表',
        //   align:"center",
        //   dataIndex: 'shenpibiaoqianzhangzhuangtai'
        // },
        // {
        //   title:'签章状态 0：未签完 1：已签完',
        //   align:"center",
        //   dataIndex: 'qianzhangzhuangtai'
        // },
        // {
        //   title:'转发状态0：未转 1：已转',
        //   align:"center",
        //   dataIndex: 'zhuanfazhuangtai'
        // },
        // {
        //   title:'外键，分部分项表id,来源dps_jc_project表id',
        //   align:"center",
        //   dataIndex: 'projectid'
        // },
        // {
        //   title:'审批人，当前登录用户名',
        //   align:"center",
        //   dataIndex: 'shenpiren'
        // },
        // {
        //   title:'审批时间,系统当前时间（YYYY-MM-dd HH:mm:ss）',
        //   align:"center",
        //   dataIndex: 'shenpishijian'
        // },
        // {
        //   title:'自定义报验单编号',
        //   align:"center",
        //   dataIndex: 'reportingsheetnonew'
        // },
        // {
        //   title:'自定义审批表编号',
        //   align:"center",
        //   dataIndex: 'approvaltablenonew'
        // },
        // {
        //   title:'委托id',
        //   align:"center",
        //   dataIndex: 'wtid'
        // },
        // {
        //   title:'配比通知单ID',
        //   align:"center",
        //   dataIndex: 'phbtzdid'
        // },
        // {
        //   title:'配比通知单编号',
        //   align:"center",
        //   dataIndex: 'phbtzdbh'
        // },
        // {
        //   title:'样品经度',
        //   align:"center",
        //   dataIndex: 'samplejidu'
        // },
        // {
        //   title:'样品纬度',
        //   align:"center",
        //   dataIndex: 'sampleweidu'
        // },
        // {
        //   title:'委托编号',
        //   align:"center",
        //   dataIndex: 'wtbh'
        // },
        // {
        //   title:'baogaoriqi',
        //   align:"center",
        //   dataIndex: 'baogaoriqi'
        // },
        // {
        //   title:'产地2',
        //   align:"center",
        //   dataIndex: 'samplechandi2'
        // },
        // {
        //   title:'产地3',
        //   align:"center",
        //   dataIndex: 'samplechandi3'
        // },
        // {
        //   title:'samplequyangdidian2',
        //   align:"center",
        //   dataIndex: 'samplequyangdidian2'
        // },
        // {
        //   title:'samplequyangdidian3',
        //   align:"center",
        //   dataIndex: 'samplequyangdidian3'
        // },
        // {
        //   title:'sampledate2',
        //   align:"center",
        //   dataIndex: 'sampledate2'
        // },
        // {
        //   title:'sampledate3',
        //   align:"center",
        //   dataIndex: 'sampledate3'
        // },
        // {
        //   title:'samplegangjindengji',
        //   align:"center",
        //   dataIndex: 'samplegangjindengji'
        // },
        // {
        //   title:'0：已收样 1：已提交 2：已分配  默认2',
        //   align:"center",
        //   dataIndex: 'syzt'
        // },
        // {
        //   title:'sampledaibiaoshuliang2',
        //   align:"center",
        //   dataIndex: 'sampledaibiaoshuliang2'
        // },
        // {
        //   title:'sampledaibiaoshuliang3',
        //   align:"center",
        //   dataIndex: 'sampledaibiaoshuliang3'
        // },
        // {
        //   title:'sampleguigexinghao2',
        //   align:"center",
        //   dataIndex: 'sampleguigexinghao2'
        // },
        // {
        //   title:'sampleguigexinghao3',
        //   align:"center",
        //   dataIndex: 'sampleguigexinghao3'
        // },
        // {
        //   title:'项目名称',
        //   align:"center",
        //   dataIndex: 'tinames'
        // },
        // {
        //   title:'samplegongchengbihou',
        //   align:"center",
        //   dataIndex: 'samplegongchengbihou'
        // },
        // {
        //   title:'原材进场委托单编号',
        //   align:"center",
        //   dataIndex: 'ycjcwtdbh'
        // },
        // {
        //   title:'整改状态',
        //   align:"center",
        //   dataIndex: 'zgzt'
        // },
        // {
        //   title:'sampleyangpinshuliang',
        //   align:"center",
        //   dataIndex: 'sampleyangpinshuliang'
        // },
        // {
        //   title:'样品数量2',
        //   align:"center",
        //   dataIndex: 'sampleyangpinshuliang2'
        // },
        // {
        //   title:'样品数量3',
        //   align:"center",
        //   dataIndex: 'sampleyangpinshuliang3'
        // },
        // {
        //   title:'samplechangdu',
        //   align:"center",
        //   dataIndex: 'samplechangdu'
        // },
        // {
        //   title:'samplelingqimax',
        //   align:"center",
        //   dataIndex: 'samplelingqimax'
        // },
        // {
        //   title:'sampledaibiaoshuliang4',
        //   align:"center",
        //   dataIndex: 'sampledaibiaoshuliang4'
        // },
        // {
        //   title:'sampledaibiaoshuliang5',
        //   align:"center",
        //   dataIndex: 'sampledaibiaoshuliang5'
        // },
        // {
        //   title:'sampleguigexinghao4',
        //   align:"center",
        //   dataIndex: 'sampleguigexinghao4'
        // },
        // {
        //   title:'sampleguigexinghao5',
        //   align:"center",
        //   dataIndex: 'sampleguigexinghao5'
        // },
        // {
        //   title:'sampleyangpinshuliang4',
        //   align:"center",
        //   dataIndex: 'sampleyangpinshuliang4'
        // },
        // {
        //   title:'sampleyangpinshuliang5',
        //   align:"center",
        //   dataIndex: 'sampleyangpinshuliang5'
        // },
        // {
        //   title:'shikuaichicun',
        //   align:"center",
        //   dataIndex: 'shikuaichicun'
        // },
        // {
        //   title:'sampleliuyangshuliang',
        //   align:"center",
        //   dataIndex: 'sampleliuyangshuliang'
        // },
        // {
        //   title:'samplezhijianriqi',
        //   align:"center",
        //   dataIndex: 'samplezhijianriqi'
        // },
        // {
        //   title:'rigidrreboundshebeino',
        //   align:"center",
        //   dataIndex: 'rigidrreboundshebeino'
        // },
        // {
        //   title:'rigidrreboundshebeicj',
        //   align:"center",
        //   dataIndex: 'rigidrreboundshebeicj'
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
        list: '/sydpssysample/syDpsSySample/grid',
        delete: '/sydpssysample/syDpsSySample/delete',
        deleteBatch: '/sydpssysample/syDpsSySample/deleteBatch',
        exportXlsUrl: '/sydpssysample/syDpsSySample/exportXls',
        importExcelUrl: 'sydpssysample/syDpsSySample/importExcel',

      },
      dictOptions: {},
      superFieldList: [],
      queryParam: {
        titType: 2
      },
    }
  },
  created() {
  },
  computed: {
    importExcelUrl: function () {
      return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`
    },
  },
  methods: {
    copyData() {
      if (this.selectionRows.length == 1) {
        this.$refs.sample.sample = this.selectionRows[0]
        this.$refs.sample.id = this.selectionRows[0].id
        this.$refs.sample.visible = true
      } else {
        this.$message.error('请选择单行数据复制！')
      }
    },
    insertData() {
      if (this.selectionRows.length == 1) {
        this.$refs.insertData.edit(this.selectionRows[0])
      } else {
        this.$message.error('请选择单行!')
      }
    },
    updateToSampleno() {
      if (this.selectionRows.length == 1) {
        this.$refs.updateSampleno.sample = this.selectionRows[0]
        this.$refs.updateSampleno.visible = true
      } else {
        this.$message.error('请选择单行数据!')
      }
    },
    commitShenhe() {
      if (this.selectionRows.length == 1) {
        let url = '/sydpssysample/syDpsSySample/over/' + this.selectionRows[0].id
        getAction(url).then((res) => {
          if (res.success) {
            this.$message.success(res.message)
          } else {
            this.$message.error(res.message)
          }
        })
      } else {
        this.$message.error('请选择单行数据!')
      }
    },
    del() {
      if (this.selectionRows.length == 1) {
        this.$refs.delSample.sample = this.selectionRows[0]
        this.$refs.delSample.visible = true
      } else {
        this.$message.error('请选择单行数据进行删除!')
      }
    },
    rePrint() {
      this.$refs.reform.visible = true
    },
    initDictConfig() {
    },
  }
}
</script>
<style scoped>
@import '~@assets/less/common.less';
</style>