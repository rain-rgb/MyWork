<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="设备名称">
              <j-search-select-tag
                placeholder="请选择设备名称"
                v-model="queryParam.shebeibianhao"
                :dictOptions="dictOption"
              >
              </j-search-select-tag>
              {{ selectValue }}
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="配合比编号">
              <a-input placeholder="请输入配合比编号" v-model="queryParam.code"></a-input>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="制定时间范围">
              <j-date
                placeholder="开始制定日期"
                v-model="queryParam.dattim_begin"
                :showTime="true"
                dateFormat="YYYY-MM-DD HH:mm:ss"
              />
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="">
              <j-date
                placeholder="结束制定日期"
                v-model="queryParam.dattim_end"
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
              <a @click="handleToggleSearch" style="margin-left: 8px">
                {{ toggleSearchStatus ? "收起" : "展开" }}
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
      <a-button @click="handleAdd" type="primary" icon="plus">新增</a-button>
      <a-button
        type="primary"
        v-has="'spjd:dc'"
        icon="download"
        @click="handleExportXls('混凝土首盘鉴定表信息')"
        >导出</a-button
      >
      <a-upload
        name="file"
        :showUploadList="false"
        :multiple="false"
        :headers="tokenHeader"
        :action="importExcelUrl"
        @change="handleImportExcel"
      >
        <a-button type="primary" v-has="'spjd:dr'" icon="import">导入</a-button>
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
        <span slot="status" slot-scope="status, record">
          <a-tag color="red" v-if="record.status == 0">未审核</a-tag>
          <a-tag color="green" v-if="record.status == 1">通过</a-tag>
          <a-tag color="yellow" v-if="record.status == 2">未通过</a-tag>
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
          <a v-has="'spjd:edit'" @click="handleEdit(record)">编辑</a>
          <!--          <a-divider type="vertical" />-->
          <a @click="handlePreview(record)">预览</a>
          <a-divider type="vertical" />
          <a-dropdown>
            <a class="ant-dropdown-link">更多 <a-icon type="down" /></a>
            <a-menu slot="overlay">
              <a-menu-item v-has="'spjd:sh'">
                <a
                  @click="
                    flag = 1;
                    id = record.id;
                  "
                  >审核</a
                >
              </a-menu-item>
              <a-menu-item>
                <a @click="handleDetail(record)">详情</a>
              </a-menu-item>
              <a-menu-item v-has="'spjd:del'">
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
    <HntspjdShenHe :flag="flag" :id="id" @change="change"></HntspjdShenHe>
    <shigongphb-tappraisal-modal
      ref="modalForm"
      @ok="modalFormOk"
    ></shigongphb-tappraisal-modal>
  </a-card>
</template>

<script>
import "@/assets/less/TableExpand.less";
import { mixinDevice } from "@/utils/mixin";
import { JeecgListMixin } from "@/mixins/JeecgListMixin";
import ShigongphbTappraisalModal from "./modules/ShigongphbTappraisalModal";
import JSuperQuery from "@/components/jeecg/JSuperQuery.vue";
import { usershebeiList } from "@api/api";
import { SYS_DEPART_ORGCODE } from "@/store/mutation-types";
import Vue from "vue";
import HntspjdShenHe from "@views/hntspjd/HntspjdShenHe";

export default {
  name: "ShigongphbTappraisalList",
  mixins: [JeecgListMixin, mixinDevice],
  components: {
    HntspjdShenHe,
    ShigongphbTappraisalModal,
    JSuperQuery,
  },
  data() {
    return {
      id: 0,
      flag: 0,
      selectValue: "",
      dictOption: [],
      description: "混凝土首盘鉴定表信息管理页面",
      // 表头
      columns: [
        {
          title: "#",
          dataIndex: "",
          key: "rowIndex",
          width: 60,
          align: "center",
          customRender: function (t, r, index) {
            return parseInt(index) + 1;
          },
        },
        {
          title: "审核状态",
          align: "center",
          dataIndex: "status",
          key: "status",
          scopedSlots: { customRender: "status" },
        },
        // {
        //   title:'生产线',
        //   align:"center",
        //   dataIndex: 'station'
        // },
        {
          title: "配合比号",
          align: "center",
          dataIndex: "code",
        },
        // {
        //   title:'砼标记',
        //   align:"center",
        //   dataIndex: 'tag'
        // },
        {
          title: "强度等级",
          align: "center",
          dataIndex: "betlev",
        },
        // {
        //   title:'抗渗等级',
        //   align:"center",
        //   dataIndex: 'filter'
        // },
        // {
        //   title:'抗冻等级',
        //   align:"center",
        //   dataIndex: 'freeze'
        // },
        // {
        //   title:'施工季节',
        //   align:"center",
        //   dataIndex: 'season'
        // },
        {
          title: "坍落度",
          align: "center",
          dataIndex: "lands",
        },
        // {
        //   title:'搅拌时间',
        //   align:"center",
        //   dataIndex: 'mixlast'
        // },
        // {
        //   title:'设计比例',
        //   align:"center",
        //   dataIndex: 'scale'
        // },
        // {
        //   title:'其他要求',
        //   align:"center",
        //   dataIndex: 'otherreq'
        // },
        // {
        //   title:'试验员',
        //   align:"center",
        //   dataIndex: 'exper'
        // },
        // {
        //   title:'审核员',
        //   align:"center",
        //   dataIndex: 'assoss'
        // },
        // {
        //   title:'制定日期',
        //   align:"center",
        //   dataIndex: 'dattim',
        //   customRender:function (text) {
        //     return !text?"":(text.length>10?text.substr(0,10):text)
        //   }
        // },
        // {
        //   title:'标识',
        //   align:"center",
        //   dataIndex: 'flag'
        // },
        // {
        //   title:'备注',
        //   align:"center",
        //   dataIndex: 'note'
        // },
        // {
        //   title:'原材料1',
        //   align:"center",
        //   dataIndex: 'm1'
        // },
        // {
        //   title:'用量1',
        //   align:"center",
        //   dataIndex: 'u1'
        // },
        // {
        //   title:'原材料2',
        //   align:"center",
        //   dataIndex: 'm2'
        // },
        // {
        //   title:'用量2',
        //   align:"center",
        //   dataIndex: 'u2'
        // },
        // {
        //   title:'原材料3',
        //   align:"center",
        //   dataIndex: 'm3'
        // },
        // {
        //   title:'用量3',
        //   align:"center",
        //   dataIndex: 'u3'
        // },
        // {
        //   title:'原材料4',
        //   align:"center",
        //   dataIndex: 'm4'
        // },
        // {
        //   title:'用量4',
        //   align:"center",
        //   dataIndex: 'u4'
        // },
        // {
        //   title:'原材料5',
        //   align:"center",
        //   dataIndex: 'm5'
        // },
        // {
        //   title:'用量5',
        //   align:"center",
        //   dataIndex: 'u5'
        // },
        // {
        //   title:'原材料6',
        //   align:"center",
        //   dataIndex: 'm6'
        // },
        // {
        //   title:'用量6',
        //   align:"center",
        //   dataIndex: 'u6'
        // },
        // {
        //   title:'原材料7',
        //   align:"center",
        //   dataIndex: 'm7'
        // },
        // {
        //   title:'用量7',
        //   align:"center",
        //   dataIndex: 'u7'
        // },
        // {
        //   title:'原材料8',
        //   align:"center",
        //   dataIndex: 'm8'
        // },
        // {
        //   title:'用量8',
        //   align:"center",
        //   dataIndex: 'u8'
        // },
        // {
        //   title:'原材料9',
        //   align:"center",
        //   dataIndex: 'm9'
        // },
        // {
        //   title:'用量9',
        //   align:"center",
        //   dataIndex: 'u9'
        // },
        // {
        //   title:'原材料10',
        //   align:"center",
        //   dataIndex: 'm10'
        // },
        // {
        //   title:'用量10',
        //   align:"center",
        //   dataIndex: 'u10'
        // },
        // {
        //   title:'原材料11',
        //   align:"center",
        //   dataIndex: 'm11'
        // },
        // {
        //   title:'用量11',
        //   align:"center",
        //   dataIndex: 'u11'
        // },
        // {
        //   title:'原材料12',
        //   align:"center",
        //   dataIndex: 'm12'
        // },
        // {
        //   title:'用量12',
        //   align:"center",
        //   dataIndex: 'u12'
        // },
        // {
        //   title:'原材料13',
        //   align:"center",
        //   dataIndex: 'm13'
        // },
        // {
        //   title:'用量13',
        //   align:"center",
        //   dataIndex: 'u13'
        // },
        // {
        //   title:'原材料14',
        //   align:"center",
        //   dataIndex: 'm14'
        // },
        // {
        //   title:'用量14',
        //   align:"center",
        //   dataIndex: 'u14'
        // },
        // {
        //   title:'原材料15',
        //   align:"center",
        //   dataIndex: 'm15'
        // },
        // {
        //   title:'用量15',
        //   align:"center",
        //   dataIndex: 'u15'
        // },
        // {
        //   title:'原材料16',
        //   align:"center",
        //   dataIndex: 'm16'
        // },
        // {
        //   title:'用量16',
        //   align:"center",
        //   dataIndex: 'u16'
        // },
        // {
        //   title:'原材料17',
        //   align:"center",
        //   dataIndex: 'm17'
        // },
        // {
        //   title:'用量17',
        //   align:"center",
        //   dataIndex: 'u17'
        // },
        // {
        //   title:'原材料18',
        //   align:"center",
        //   dataIndex: 'm18'
        // },
        // {
        //   title:'用量18',
        //   align:"center",
        //   dataIndex: 'u18'
        // },
        // {
        //   title:'原材料19',
        //   align:"center",
        //   dataIndex: 'm19'
        // },
        // {
        //   title:'用量19',
        //   align:"center",
        //   dataIndex: 'u19'
        // },
        // {
        //   title:'原材料20',
        //   align:"center",
        //   dataIndex: 'm20'
        // },
        // {
        //   title:'用量20',
        //   align:"center",
        //   dataIndex: 'u20'
        // },
        // {
        //   title:'原材料21',
        //   align:"center",
        //   dataIndex: 'm21'
        // },
        // {
        //   title:'用量21',
        //   align:"center",
        //   dataIndex: 'u21'
        // },
        // {
        //   title:'原材料22',
        //   align:"center",
        //   dataIndex: 'm22'
        // },
        // {
        //   title:'用量22',
        //   align:"center",
        //   dataIndex: 'u22'
        // },
        // {
        //   title:'原材料23',
        //   align:"center",
        //   dataIndex: 'm23'
        // },
        // {
        //   title:'用量23',
        //   align:"center",
        //   dataIndex: 'u23'
        // },
        // {
        //   title:'原材料24',
        //   align:"center",
        //   dataIndex: 'm24'
        // },
        // {
        //   title:'用量24',
        //   align:"center",
        //   dataIndex: 'u24'
        // },
        // {
        //   title:'原材料25',
        //   align:"center",
        //   dataIndex: 'm25'
        // },
        // {
        //   title:'用量25',
        //   align:"center",
        //   dataIndex: 'u25'
        // },
        // {
        //   title:'原材料26',
        //   align:"center",
        //   dataIndex: 'm26'
        // },
        // {
        //   title:'用量26',
        //   align:"center",
        //   dataIndex: 'u26'
        // },
        // {
        //   title:'原材料27',
        //   align:"center",
        //   dataIndex: 'm27'
        // },
        // {
        //   title:'用量27',
        //   align:"center",
        //   dataIndex: 'u27'
        // },
        // {
        //   title:'原材料28',
        //   align:"center",
        //   dataIndex: 'm28'
        // },
        // {
        //   title:'用量28',
        //   align:"center",
        //   dataIndex: 'u28'
        // },
        // {
        //   title:'原材料29',
        //   align:"center",
        //   dataIndex: 'm29'
        // },
        // {
        //   title:'用量29',
        //   align:"center",
        //   dataIndex: 'u29'
        // },
        // {
        //   title:'原材料30',
        //   align:"center",
        //   dataIndex: 'm30'
        // },
        // {
        //   title:'用量30',
        //   align:"center",
        //   dataIndex: 'u30'
        // },
        // {
        //   title:'原材料31',
        //   align:"center",
        //   dataIndex: 'm31'
        // },
        // {
        //   title:'用量31',
        //   align:"center",
        //   dataIndex: 'u31'
        // },
        // {
        //   title:'原材料32',
        //   align:"center",
        //   dataIndex: 'm32'
        // },
        // {
        //   title:'用量32',
        //   align:"center",
        //   dataIndex: 'u32'
        // },
        {
          title: "设备编号",
          align: "center",
          dataIndex: "shebeibianhao_dictText",
        },
        // {
        //   title:'控制权限',
        //   align:"center",
        //   dataIndex: 'sysOrgCode'
        // },
        {
          title: "创建人",
          align: "center",
          dataIndex: "createBy_dictText",
        },
        // {
        //   title:'理论配合比编号',
        //   align:"center",
        //   dataIndex: 'code1'
        // },
        {
          title: "工程名称",
          align: "center",
          dataIndex: "projname",
        },
        {
          title: "施工部位",
          align: "center",
          dataIndex: "conspos",
        },
        {
          title: "浇筑方式",
          align: "center",
          dataIndex: "pour",
        },
        // {
        //   title:'含水率（原材料4骨料1）',
        //   align:"center",
        //   dataIndex: 'mc4'
        // },
        // {
        //   title:'含水率（原材料5骨料2）',
        //   align:"center",
        //   dataIndex: 'mc5'
        // },
        // {
        //   title:'含水率（原材料6骨料3）',
        //   align:"center",
        //   dataIndex: 'mc6'
        // },
        // {
        //   title:'含水率（原材料7骨料4）',
        //   align:"center",
        //   dataIndex: 'mc7'
        // },
        // {
        //   title:'含水率（原材料8外加剂1）',
        //   align:"center",
        //   dataIndex: 'mc8'
        // },
        // {
        //   title:'含水率（原材料9外加剂2）',
        //   align:"center",
        //   dataIndex: 'mc9'
        // },
        // {
        //   title:'含水率（原材料10外加剂3）',
        //   align:"center",
        //   dataIndex: 'mc10'
        // },
        // {
        //   title:'实际用量（原材料4骨料1）',
        //   align:"center",
        //   dataIndex: 'ru4'
        // },
        // {
        //   title:'实际用量（原材料5骨料2）',
        //   align:"center",
        //   dataIndex: 'ru5'
        // },
        // {
        //   title:'实际用量（原材料6骨料3）',
        //   align:"center",
        //   dataIndex: 'ru6'
        // },
        // {
        //   title:'实际用量（原材料7骨料4）',
        //   align:"center",
        //   dataIndex: 'ru7'
        // },
        // {
        //   title:'实际用量（原材料8外加剂1）',
        //   align:"center",
        //   dataIndex: 'ru8'
        // },
        // {
        //   title:'实际用量（原材料9外加剂2）',
        //   align:"center",
        //   dataIndex: 'ru9'
        // },
        // {
        //   title:'实际用量（原材料10外加剂3）',
        //   align:"center",
        //   dataIndex: 'ru10'
        // },
        // {
        //   title:'鉴定编号',
        //   align:"center",
        //   dataIndex: 'identificationno'
        // },
        {
          title: "鉴定日期",
          align: "center",
          dataIndex: "identificationtime",
        },
        // {
        //   title:'鉴定设计塌落度(mm)',
        //   align:"center",
        //   dataIndex: 'jdsjlands'
        // },
        // {
        //   title:'鉴定实测塌落度1(mm)',
        //   align:"center",
        //   dataIndex: 'jdsclands1'
        // },
        // {
        //   title:'鉴定实测塌落度2(mm)',
        //   align:"center",
        //   dataIndex: 'jdsclands2'
        // },
        // {
        //   title:'鉴定实测塌落度3(mm)',
        //   align:"center",
        //   dataIndex: 'jdsclands3'
        // },
        // {
        //   title:'鉴定设计含水量（%）',
        //   align:"center",
        //   dataIndex: 'jdsjmc'
        // },
        // {
        //   title:'鉴定实测含水量1（%）',
        //   align:"center",
        //   dataIndex: 'jdscmc1'
        // },
        // {
        //   title:'鉴定实测含水量2（%）',
        //   align:"center",
        //   dataIndex: 'jdscmc2'
        // },
        // {
        //   title:'鉴定实测含水量3（%）',
        //   align:"center",
        //   dataIndex: 'jdscmc3'
        // },
        // {
        //   title:'鉴定设计出机温度（℃）',
        //   align:"center",
        //   dataIndex: 'jdsjcjwd'
        // },
        // {
        //   title:'鉴定实测出机温度1（℃）',
        //   align:"center",
        //   dataIndex: 'jdsccjwd1'
        // },
        // {
        //   title:'鉴定实测出机温度2（℃）',
        //   align:"center",
        //   dataIndex: 'jdsccjwd2'
        // },
        // {
        //   title:'鉴定实测出机温度3（℃）',
        //   align:"center",
        //   dataIndex: 'jdsccjwd3'
        // },
        // {
        //   title:'鉴定设计泌水率1',
        //   align:"center",
        //   dataIndex: 'jdsjmsl1'
        // },
        // {
        //   title:'鉴定设计泌水率2',
        //   align:"center",
        //   dataIndex: 'jdsjmsl2'
        // },
        // {
        //   title:'鉴定设计泌水率3',
        //   align:"center",
        //   dataIndex: 'jdsjmsl3'
        // },
        // {
        //   title:'原材料与申请单是否相符',
        //   align:"center",
        //   dataIndex: 'ismatch'
        // },
        {
          title: "鉴定结论",
          align: "center",
          dataIndex: "identificationresult",
        },
        {
          title: "鉴定附件",
          align: "center",
          dataIndex: "filePath",
          key: "filePath",
          scopedSlots: { customRender: "fileSlot" },
        },
        // {
        //   title:'试验员',
        //   align:"center",
        //   dataIndex: 'shiyanyuan'
        // },
        // {
        //   title:'技术主管',
        //   align:"center",
        //   dataIndex: 'technicalDirector'
        // },
        // {
        //   title:'监理工程师',
        //   align:"center",
        //   dataIndex: 'supervisionEngineer'
        // },
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
        list: "/hntspjd/shigongphbTappraisal/list",
        delete: "/hntspjd/shigongphbTappraisal/delete",
        deleteBatch: "/hntspjd/shigongphbTappraisal/deleteBatch",
        exportXlsUrl: "/hntspjd/shigongphbTappraisal/exportXls",
        importExcelUrl: "hntspjd/shigongphbTappraisal/importExcel",
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
    change(data) {
      this.flag = data;
    },
    handlePreview(record) {
      if (record && record.filePath) {
        let url =
          window._CONFIG["onlinePreviewDomainURL"] +
          "?url=" +
          encodeURIComponent(record.filePath);
        window.open(url, "_blank");
      }
    },
    shebeiList: function () {
      var sys_depart_orgcode = Vue.ls.get("SYS_DEPART_ORGCODE");
      usershebeiList({
        sys_depart_orgcode: sys_depart_orgcode,
        sbtypes: "0",
      }).then((res) => {
        this.dictOption = [];
        let result = res.result;
        result.forEach((re) => {
          this.dictOption.push({ text: re.sbname, value: re.sbjno });
        });
        //console.log(res)
      });
    },
    initDictConfig() {},
    getSuperFieldList() {
      let fieldList = [];
      fieldList.push({ type: "int", value: "station", text: "生产线", dictCode: "" });
      fieldList.push({ type: "string", value: "code", text: "配合比号", dictCode: "" });
      fieldList.push({ type: "string", value: "tag", text: "砼标记", dictCode: "" });
      fieldList.push({ type: "string", value: "betlev", text: "强度等级", dictCode: "" });
      fieldList.push({ type: "string", value: "filter", text: "抗渗等级", dictCode: "" });
      fieldList.push({ type: "string", value: "freeze", text: "抗冻等级", dictCode: "" });
      fieldList.push({ type: "string", value: "season", text: "施工季节", dictCode: "" });
      fieldList.push({ type: "string", value: "lands", text: "坍落度", dictCode: "" });
      fieldList.push({ type: "int", value: "mixlast", text: "搅拌时间", dictCode: "" });
      fieldList.push({ type: "string", value: "scale", text: "设计比例", dictCode: "" });
      fieldList.push({
        type: "string",
        value: "otherreq",
        text: "其他要求",
        dictCode: "",
      });
      fieldList.push({ type: "string", value: "exper", text: "试验员", dictCode: "" });
      fieldList.push({ type: "string", value: "assoss", text: "审核员", dictCode: "" });
      fieldList.push({ type: "date", value: "dattim", text: "制定日期" });
      fieldList.push({ type: "string", value: "flag", text: "标识", dictCode: "" });
      fieldList.push({ type: "string", value: "note", text: "备注", dictCode: "" });
      fieldList.push({ type: "string", value: "m1", text: "原材料1", dictCode: "" });
      fieldList.push({ type: "double", value: "u1", text: "用量1", dictCode: "" });
      fieldList.push({ type: "string", value: "m2", text: "原材料2", dictCode: "" });
      fieldList.push({ type: "double", value: "u2", text: "用量2", dictCode: "" });
      fieldList.push({ type: "string", value: "m3", text: "原材料3", dictCode: "" });
      fieldList.push({ type: "double", value: "u3", text: "用量3", dictCode: "" });
      fieldList.push({ type: "string", value: "m4", text: "原材料4", dictCode: "" });
      fieldList.push({ type: "double", value: "u4", text: "用量4", dictCode: "" });
      fieldList.push({ type: "string", value: "m5", text: "原材料5", dictCode: "" });
      fieldList.push({ type: "double", value: "u5", text: "用量5", dictCode: "" });
      fieldList.push({ type: "string", value: "m6", text: "原材料6", dictCode: "" });
      fieldList.push({ type: "double", value: "u6", text: "用量6", dictCode: "" });
      fieldList.push({ type: "string", value: "m7", text: "原材料7", dictCode: "" });
      fieldList.push({ type: "double", value: "u7", text: "用量7", dictCode: "" });
      fieldList.push({ type: "string", value: "m8", text: "原材料8", dictCode: "" });
      fieldList.push({ type: "double", value: "u8", text: "用量8", dictCode: "" });
      fieldList.push({ type: "string", value: "m9", text: "原材料9", dictCode: "" });
      fieldList.push({ type: "double", value: "u9", text: "用量9", dictCode: "" });
      fieldList.push({ type: "string", value: "m10", text: "原材料10", dictCode: "" });
      fieldList.push({ type: "double", value: "u10", text: "用量10", dictCode: "" });
      fieldList.push({ type: "string", value: "m11", text: "原材料11", dictCode: "" });
      fieldList.push({ type: "double", value: "u11", text: "用量11", dictCode: "" });
      fieldList.push({ type: "string", value: "m12", text: "原材料12", dictCode: "" });
      fieldList.push({ type: "double", value: "u12", text: "用量12", dictCode: "" });
      fieldList.push({ type: "string", value: "m13", text: "原材料13", dictCode: "" });
      fieldList.push({ type: "double", value: "u13", text: "用量13", dictCode: "" });
      fieldList.push({ type: "string", value: "m14", text: "原材料14", dictCode: "" });
      fieldList.push({ type: "double", value: "u14", text: "用量14", dictCode: "" });
      fieldList.push({ type: "string", value: "m15", text: "原材料15", dictCode: "" });
      fieldList.push({ type: "double", value: "u15", text: "用量15", dictCode: "" });
      fieldList.push({ type: "string", value: "m16", text: "原材料16", dictCode: "" });
      fieldList.push({ type: "double", value: "u16", text: "用量16", dictCode: "" });
      fieldList.push({ type: "string", value: "m17", text: "原材料17", dictCode: "" });
      fieldList.push({ type: "double", value: "u17", text: "用量17", dictCode: "" });
      fieldList.push({ type: "string", value: "m18", text: "原材料18", dictCode: "" });
      fieldList.push({ type: "double", value: "u18", text: "用量18", dictCode: "" });
      fieldList.push({ type: "string", value: "m19", text: "原材料19", dictCode: "" });
      fieldList.push({ type: "double", value: "u19", text: "用量19", dictCode: "" });
      fieldList.push({ type: "string", value: "m20", text: "原材料20", dictCode: "" });
      fieldList.push({ type: "double", value: "u20", text: "用量20", dictCode: "" });
      fieldList.push({ type: "string", value: "m21", text: "原材料21", dictCode: "" });
      fieldList.push({ type: "double", value: "u21", text: "用量21", dictCode: "" });
      fieldList.push({ type: "string", value: "m22", text: "原材料22", dictCode: "" });
      fieldList.push({ type: "double", value: "u22", text: "用量22", dictCode: "" });
      fieldList.push({ type: "string", value: "m23", text: "原材料23", dictCode: "" });
      fieldList.push({ type: "double", value: "u23", text: "用量23", dictCode: "" });
      fieldList.push({ type: "string", value: "m24", text: "原材料24", dictCode: "" });
      fieldList.push({ type: "double", value: "u24", text: "用量24", dictCode: "" });
      fieldList.push({ type: "string", value: "m25", text: "原材料25", dictCode: "" });
      fieldList.push({ type: "double", value: "u25", text: "用量25", dictCode: "" });
      fieldList.push({ type: "string", value: "m26", text: "原材料26", dictCode: "" });
      fieldList.push({ type: "double", value: "u26", text: "用量26", dictCode: "" });
      fieldList.push({ type: "string", value: "m27", text: "原材料27", dictCode: "" });
      fieldList.push({ type: "double", value: "u27", text: "用量27", dictCode: "" });
      fieldList.push({ type: "string", value: "m28", text: "原材料28", dictCode: "" });
      fieldList.push({ type: "double", value: "u28", text: "用量28", dictCode: "" });
      fieldList.push({ type: "string", value: "m29", text: "原材料29", dictCode: "" });
      fieldList.push({ type: "double", value: "u29", text: "用量29", dictCode: "" });
      fieldList.push({ type: "string", value: "m30", text: "原材料30", dictCode: "" });
      fieldList.push({ type: "double", value: "u30", text: "用量30", dictCode: "" });
      fieldList.push({ type: "string", value: "m31", text: "原材料31", dictCode: "" });
      fieldList.push({ type: "double", value: "u31", text: "用量31", dictCode: "" });
      fieldList.push({ type: "string", value: "m32", text: "原材料32", dictCode: "" });
      fieldList.push({ type: "double", value: "u32", text: "用量32", dictCode: "" });
      fieldList.push({
        type: "string",
        value: "shebeibianhao",
        text: "设备编号",
        dictCode: "",
      });
      fieldList.push({
        type: "string",
        value: "sysOrgCode",
        text: "控制权限",
        dictCode: "",
      });
      fieldList.push({ type: "string", value: "createBy", text: "创建人", dictCode: "" });
      fieldList.push({
        type: "string",
        value: "code1",
        text: "理论配合比编号",
        dictCode: "",
      });
      fieldList.push({
        type: "string",
        value: "projname",
        text: "工程名称",
        dictCode: "",
      });
      fieldList.push({
        type: "string",
        value: "conspos",
        text: "施工部位",
        dictCode: "",
      });
      fieldList.push({ type: "string", value: "pour", text: "浇筑方式", dictCode: "" });
      fieldList.push({
        type: "double",
        value: "mc4",
        text: "含水率（原材料4骨料1）",
        dictCode: "",
      });
      fieldList.push({
        type: "double",
        value: "mc5",
        text: "含水率（原材料5骨料2）",
        dictCode: "",
      });
      fieldList.push({
        type: "double",
        value: "mc6",
        text: "含水率（原材料6骨料3）",
        dictCode: "",
      });
      fieldList.push({
        type: "double",
        value: "mc7",
        text: "含水率（原材料7骨料4）",
        dictCode: "",
      });
      fieldList.push({
        type: "double",
        value: "mc8",
        text: "含水率（原材料8外加剂1）",
        dictCode: "",
      });
      fieldList.push({
        type: "double",
        value: "mc9",
        text: "含水率（原材料9外加剂2）",
        dictCode: "",
      });
      fieldList.push({
        type: "double",
        value: "mc10",
        text: "含水率（原材料10外加剂3）",
        dictCode: "",
      });
      fieldList.push({
        type: "double",
        value: "ru4",
        text: "实际用量（原材料4骨料1）",
        dictCode: "",
      });
      fieldList.push({
        type: "double",
        value: "ru5",
        text: "实际用量（原材料5骨料2）",
        dictCode: "",
      });
      fieldList.push({
        type: "double",
        value: "ru6",
        text: "实际用量（原材料6骨料3）",
        dictCode: "",
      });
      fieldList.push({
        type: "double",
        value: "ru7",
        text: "实际用量（原材料7骨料4）",
        dictCode: "",
      });
      fieldList.push({
        type: "double",
        value: "ru8",
        text: "实际用量（原材料8外加剂1）",
        dictCode: "",
      });
      fieldList.push({
        type: "double",
        value: "ru9",
        text: "实际用量（原材料9外加剂2）",
        dictCode: "",
      });
      fieldList.push({
        type: "double",
        value: "ru10",
        text: "实际用量（原材料10外加剂3）",
        dictCode: "",
      });
      fieldList.push({
        type: "string",
        value: "identificationno",
        text: "鉴定编号",
        dictCode: "",
      });
      fieldList.push({ type: "date", value: "identificationtime", text: "鉴定日期" });
      fieldList.push({
        type: "string",
        value: "jdsjlands",
        text: "鉴定设计塌落度(mm)",
        dictCode: "",
      });
      fieldList.push({
        type: "string",
        value: "jdsclands1",
        text: "鉴定实测塌落度1(mm)",
        dictCode: "",
      });
      fieldList.push({
        type: "string",
        value: "jdsclands2",
        text: "鉴定实测塌落度2(mm)",
        dictCode: "",
      });
      fieldList.push({
        type: "string",
        value: "jdsclands3",
        text: "鉴定实测塌落度3(mm)",
        dictCode: "",
      });
      fieldList.push({
        type: "double",
        value: "jdsjmc",
        text: "鉴定设计含水量（%）",
        dictCode: "",
      });
      fieldList.push({
        type: "double",
        value: "jdscmc1",
        text: "鉴定实测含水量1（%）",
        dictCode: "",
      });
      fieldList.push({
        type: "double",
        value: "jdscmc2",
        text: "鉴定实测含水量2（%）",
        dictCode: "",
      });
      fieldList.push({
        type: "double",
        value: "jdscmc3",
        text: "鉴定实测含水量3（%）",
        dictCode: "",
      });
      fieldList.push({
        type: "double",
        value: "jdsjcjwd",
        text: "鉴定设计出机温度（℃）",
        dictCode: "",
      });
      fieldList.push({
        type: "double",
        value: "jdsccjwd1",
        text: "鉴定实测出机温度1（℃）",
        dictCode: "",
      });
      fieldList.push({
        type: "double",
        value: "jdsccjwd2",
        text: "鉴定实测出机温度2（℃）",
        dictCode: "",
      });
      fieldList.push({
        type: "double",
        value: "jdsccjwd3",
        text: "鉴定实测出机温度3（℃）",
        dictCode: "",
      });
      fieldList.push({
        type: "double",
        value: "jdsjmsl1",
        text: "鉴定设计泌水率1",
        dictCode: "",
      });
      fieldList.push({
        type: "double",
        value: "jdsjmsl2",
        text: "鉴定设计泌水率2",
        dictCode: "",
      });
      fieldList.push({
        type: "double",
        value: "jdsjmsl3",
        text: "鉴定设计泌水率3",
        dictCode: "",
      });
      fieldList.push({
        type: "int",
        value: "ismatch",
        text: "原材料与申请单是否相符",
        dictCode: "",
      });
      fieldList.push({
        type: "string",
        value: "identificationresult",
        text: "鉴定结论",
        dictCode: "",
      });
      fieldList.push({
        type: "string",
        value: "filePath",
        text: "鉴定附件",
        dictCode: "",
      });
      fieldList.push({
        type: "string",
        value: "shiyanyuan",
        text: "试验员",
        dictCode: "",
      });
      fieldList.push({
        type: "string",
        value: "technicalDirector",
        text: "技术主管",
        dictCode: "",
      });
      fieldList.push({
        type: "string",
        value: "supervisionEngineer",
        text: "监理工程师",
        dictCode: "",
      });
      this.superFieldList = fieldList;
    },
  },
};
</script>
<style scoped>
@import "~@assets/less/common.less";
</style>
