<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="材料类型">
              <j-dict-select-tag
                placeholder="请选择"
                v-model="queryParam.state"
                dictCode="nodetype"
              ></j-dict-select-tag>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="时间范围">
              <j-date
                placeholder="开始时间"
                v-model="queryParam.createtime_begin"
                :showTime="true"
                dateFormat="YYYY-MM-DD HH:mm:ss"
              />
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="结束时间">
              <j-date
                placeholder="结束时间"
                v-model="queryParam.createtime_end"
                :showTime="true"
                dateFormat="YYYY-MM-DD HH:mm:ss"
              />
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <span
              style="float: left; overflow: hidden"
              class="table-page-search-submitButtons"
            >
              <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
              <a-button
                type="primary"
                @click="searchReset"
                icon="reload"
                style="margin-left: 8px"
                >重置</a-button
              >
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
      <!-- <a-button @click="handleAdd" type="primary" icon="plus">新增</a-button>
      <a-button type="primary" icon="download" @click="handleExportXls('原材料检验委托申请表信息')">导出</a-button>
      <a-upload
        name="file"
        :showUploadList="false"
        :multiple="false"
        :headers="tokenHeader"
        :action="importExcelUrl"
        @change="handleImportExcel"
      >
        <a-button type="primary" icon="import">导入</a-button>
      </a-upload> -->
      <!-- 高级查询区域 -->
      <!-- <j-super-query
        :fieldList="superFieldList"
        ref="superQueryModal"
        @handleSuperQuery="handleSuperQuery"
      ></j-super-query> -->
      <a-dropdown v-if="selectedRowKeys.length > 0">
        <a-menu slot="overlay">
          <a-menu-item key="1" @click="batchDel"
            ><a-icon type="delete" />删除</a-menu-item
          >
        </a-menu>
        <a-button style="margin-left: 8px"> 批量操作 <a-icon type="down" /></a-button>
      </a-dropdown>
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
        :scroll="{ x: true }"
        bordered
        rowKey="id"
        :columns="columns"
        :dataSource="dataSource"
        :pagination="ipagination"
        :loading="loading"
        :rowSelection="{ selectedRowKeys: selectedRowKeys, onChange: onSelectChange }"
        class="j-table-force-nowrap"
        @change="handleTableChange"
        :customRow="handleClick"
        :rowClassName="setRowClassName"
      >
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
          <a-button
            v-else
            :ghost="true"
            type="primary"
            icon="download"
            size="small"
            @click="downloadFile(text)"
          >
            下载
          </a-button>
        </template>

        <span slot="action" slot-scope="text, record">
          <a @click="handleEdit(record)">编辑</a>

          <a-divider type="vertical" />
          <a-dropdown>
            <a class="ant-dropdown-link">更多 <a-icon type="down" /></a>
            <a-menu slot="overlay">
              <a-menu-item>
                <a @click="handleDetail(record)">详情</a>
              </a-menu-item>
              <a-menu-item>
                <a-popconfirm
                  title="确定删除吗?"
                  @confirm="() => handleDelete(record.id)"
                >
                  <a>删除</a>
                </a-popconfirm>
              </a-menu-item>
            </a-menu>
          </a-dropdown>
        </span>
      </a-table>
    </div>

    <wztaizhangweituodan-modal
      ref="modalForm"
      @ok="modalFormOk"
    ></wztaizhangweituodan-modal>
  </a-card>
</template>

<script>
import "@/assets/less/TableExpand.less";
import { mixinDevice } from "@/utils/mixin";
import { JeecgListMixin } from "@/mixins/JeecgListMixin";
import WztaizhangweituodanModal from "./modules/WztaizhangweituodanModal";
import JSuperQuery from "@/components/jeecg/JSuperQuery.vue";

export default {
  name: "WztaizhangweituodanList",
  mixins: [JeecgListMixin, mixinDevice],
  components: {
    WztaizhangweituodanModal,
    JSuperQuery,
  },
  data() {
    return {
      description: "原材料检验委托申请表信息管理页面",
      // 表头
      columns: [
        {
          title: "序号",
          dataIndex: "",
          key: "rowIndex",
          width: 60,
          align: "center",
          customRender: function (t, r, index) {
            return parseInt(index) + 1;
          },
        },
        {
          title: "工程名称",
          align: "center",
          dataIndex: "gongchengmingchen",
        },
        {
          title: "施工部位",
          align: "center",
          dataIndex: "shigongbuwei",
        },
        {
          title: "材料类型",
          align: "center",
          dataIndex: "state_dictText",
        },
        // {
        //   title:'报表',
        //   align:"center",
        //   dataIndex: 'biaohao'
        // },
        // {
        //   title:'标准代号',
        //   align:"center",
        //   dataIndex: 'biaozhundaihao'
        // },
        // {
        //   title:'委托单位',
        //   align:"center",
        //   dataIndex: 'weituodanwei'
        // },
        {
          title: "委托编号",
          align: "center",
          dataIndex: "weituobianhao",
        },
        {
          title: "样品名称",
          align: "center",
          dataIndex: "yangpingname",
        },
        // {
        //   title:'送样编号',
        //   align:"center",
        //   dataIndex: 'songyangno'
        // },
        {
          title: "产地厂名",
          align: "center",
          dataIndex: "chandichangming",
        },
        {
          title: "试样数量",
          align: "center",
          dataIndex: "shiyangnum",
        },
        {
          title: "规格种类",
          align: "center",
          dataIndex: "guigezhonglei",
        },
        {
          title: "取样地点",
          align: "center",
          dataIndex: "quyangdidian",
        },
        {
          title: "代表数量(吨)",
          align: "center",
          dataIndex: "daibiaonum",
        },
        {
          title: "样品描述",
          align: "center",
          dataIndex: "yangpingmiaoshu",
        },
        // {
        //   title:'取样日期',
        //   align:"center",
        //   dataIndex: 'quyangtime'
        // },

        // {
        //   title:'检验标准',
        //   align:"center",
        //   dataIndex: 'jianyanbiaozhun'
        // },
        // {
        //   title:'试验项目',
        //   align:"center",
        //   dataIndex: 'shiyanxiangmu'
        // },
        // {
        //   title:'收样日期',
        //   align:"center",
        //   dataIndex: 'shouyangtime'
        // },
        // {
        //   title:'试样验收意见',
        //   align:"center",
        //   dataIndex: 'yanshouyijian'
        // },
        // {
        //   title:'取样人',
        //   align:"center",
        //   dataIndex: 'quyangren'
        // },
        {
          title: "取样见证人",
          align: "center",
          dataIndex: "quyangjianzhengren",
        },
        // {
        //   title:'委托人',
        //   align:"center",
        //   dataIndex: 'weituoren'
        // },
        // {
        //   title:'委托单位负责人',
        //   align:"center",
        //   dataIndex: 'weituodanweifuzeren'
        // },
        // {
        //   title:'住址及邮编',
        //   align:"center",
        //   dataIndex: 'zhuzhaijiyoubian'
        // },
        // {
        //   title:'联系电话',
        //   align:"center",
        //   dataIndex: 'lianxiphone'
        // },
        // {
        //   title:'收样人',
        //   align:"center",
        //   dataIndex: 'shouyangren'
        // },
        // {
        //   title:'预计报告发出日期',
        //   align:"center",
        //   dataIndex: 'yujifachutime',
        //   customRender:function (text) {
        //     return !text?"":(text.length>10?text.substr(0,10):text)
        //   }
        // },
        // {
        //   title:'tzid',
        //   align:"center",
        //   dataIndex: 'tzid'
        // },
        // {
        //   title:'moduleid',
        //   align:"center",
        //   dataIndex: 'moduleid'
        // },
        // {
        //   title:'modulename',
        //   align:"center",
        //   dataIndex: 'modulename'
        // },
        // {
        //   title:'是否录入',
        //   align:"center",
        //   dataIndex: 'shifouluru'
        // },
        // {
        //   title:'是否删除',
        //   align:"center",
        //   dataIndex: 'isdel'
        // },
        // {
        //   title:'材料编号',
        //   align:"center",
        //   dataIndex: 'cailiaono'
        // },
        {
          title: "批次",
          align: "center",
          dataIndex: "pici",
        },
        // {
        //   title:'设备编号',
        //   align:"center",
        //   dataIndex: 'shebeibianhao'
        // },
        // {
        //   title:'净重（吨）',
        //   align:"center",
        //   dataIndex: 'jingzhongt'
        // },
        // {
        //   title:'进场时间',
        //   align:"center",
        //   dataIndex: 'jinchangshijian'
        // },
        // {
        //   title:'收样id',
        //   align:"center",
        //   dataIndex: 'shouyangid'
        // },
        {
          title: "创建时间",
          align: "center",
          dataIndex: "createtime",
          // customRender:function (text) {
          //   return !text?"":(text.length>10?text.substr(0,10):text)
          // }
        },
        {
          title: "操作",
          dataIndex: "action",
          align: "center",
          fixed: "right",
          width: 147,
          scopedSlots: { customRender: "action" },
        },
      ],
      url: {
        list: "/wztaizhangweituodan/wztaizhangweituodan/list",
        delete: "/wztaizhangweituodan/wztaizhangweituodan/delete",
        deleteBatch: "/wztaizhangweituodan/wztaizhangweituodan/deleteBatch",
        exportXlsUrl: "/wztaizhangweituodan/wztaizhangweituodan/exportXls",
        importExcelUrl: "wztaizhangweituodan/wztaizhangweituodan/importExcel",
      },
      dictOptions: {},
      superFieldList: [],
    };
  },
  created() {
    this.getSuperFieldList();
  },
  computed: {
    importExcelUrl: function () {
      return `${window._CONFIG["domianURL"]}/${this.url.importExcelUrl}`;
    },
  },
  methods: {
    initDictConfig() {},
    getSuperFieldList() {
      let fieldList = [];
      fieldList.push({ type: "string", value: "state", text: "报告类型", dictCode: "" });
      fieldList.push({ type: "string", value: "biaohao", text: "报表", dictCode: "" });
      fieldList.push({
        type: "string",
        value: "biaozhundaihao",
        text: "标准代号",
        dictCode: "",
      });
      fieldList.push({
        type: "string",
        value: "weituodanwei",
        text: "委托单位",
        dictCode: "",
      });
      fieldList.push({
        type: "string",
        value: "weituobianhao",
        text: "委托编号",
        dictCode: "",
      });
      fieldList.push({
        type: "string",
        value: "yangpingname",
        text: "样品名称",
        dictCode: "",
      });
      fieldList.push({
        type: "string",
        value: "songyangno",
        text: "送样编号",
        dictCode: "",
      });
      fieldList.push({
        type: "string",
        value: "chandichangming",
        text: "产地厂名",
        dictCode: "",
      });
      fieldList.push({
        type: "string",
        value: "shiyangnum",
        text: "试样数量",
        dictCode: "",
      });
      fieldList.push({
        type: "string",
        value: "guigezhonglei",
        text: "规格种类",
        dictCode: "",
      });
      fieldList.push({
        type: "string",
        value: "quyangdidian",
        text: "取样地点",
        dictCode: "",
      });
      fieldList.push({
        type: "string",
        value: "daibiaonum",
        text: "代表数量(吨)",
        dictCode: "",
      });
      fieldList.push({
        type: "string",
        value: "yangpingmiaoshu",
        text: "样品描述",
        dictCode: "",
      });
      fieldList.push({
        type: "string",
        value: "quyangtime",
        text: "取样日期",
        dictCode: "",
      });
      fieldList.push({
        type: "string",
        value: "gongchengmingchen",
        text: "工程名称",
        dictCode: "",
      });
      fieldList.push({
        type: "string",
        value: "shigongbuwei",
        text: "施工部位",
        dictCode: "",
      });
      fieldList.push({
        type: "string",
        value: "jianyanbiaozhun",
        text: "检验标准",
        dictCode: "",
      });
      fieldList.push({
        type: "string",
        value: "shiyanxiangmu",
        text: "试验项目",
        dictCode: "",
      });
      fieldList.push({
        type: "string",
        value: "shouyangtime",
        text: "收样日期",
        dictCode: "",
      });
      fieldList.push({
        type: "string",
        value: "yanshouyijian",
        text: "试样验收意见",
        dictCode: "",
      });
      fieldList.push({
        type: "string",
        value: "quyangren",
        text: "取样人",
        dictCode: "",
      });
      fieldList.push({
        type: "string",
        value: "quyangjianzhengren",
        text: "取样见证人",
        dictCode: "",
      });
      fieldList.push({
        type: "string",
        value: "weituoren",
        text: "委托人",
        dictCode: "",
      });
      fieldList.push({
        type: "string",
        value: "weituodanweifuzeren",
        text: "委托单位负责人",
        dictCode: "",
      });
      fieldList.push({
        type: "string",
        value: "zhuzhaijiyoubian",
        text: "住址及邮编",
        dictCode: "",
      });
      fieldList.push({
        type: "string",
        value: "lianxiphone",
        text: "联系电话",
        dictCode: "",
      });
      fieldList.push({
        type: "string",
        value: "shouyangren",
        text: "收样人",
        dictCode: "",
      });
      fieldList.push({ type: "date", value: "yujifachutime", text: "预计报告发出日期" });
      fieldList.push({ type: "int", value: "tzid", text: "tzid", dictCode: "" });
      fieldList.push({
        type: "string",
        value: "moduleid",
        text: "moduleid",
        dictCode: "",
      });
      fieldList.push({
        type: "string",
        value: "modulename",
        text: "modulename",
        dictCode: "",
      });
      fieldList.push({
        type: "string",
        value: "shifouluru",
        text: "是否录入",
        dictCode: "",
      });
      fieldList.push({ type: "string", value: "isdel", text: "是否删除", dictCode: "" });
      fieldList.push({
        type: "string",
        value: "cailiaono",
        text: "材料编号",
        dictCode: "",
      });
      fieldList.push({ type: "string", value: "pici", text: "批次", dictCode: "" });
      fieldList.push({
        type: "string",
        value: "shebeibianhao",
        text: "设备编号",
        dictCode: "",
      });
      fieldList.push({
        type: "string",
        value: "jingzhongt",
        text: "净重（吨）",
        dictCode: "",
      });
      fieldList.push({
        type: "string",
        value: "jinchangshijian",
        text: "进场时间",
        dictCode: "",
      });
      fieldList.push({
        type: "string",
        value: "shouyangid",
        text: "收样id",
        dictCode: "",
      });
      fieldList.push({ type: "date", value: "createtime", text: "创建时间" });
      this.superFieldList = fieldList;
    },
  },
};
</script>
<style scoped>
@import "~@assets/less/common.less";
</style>
