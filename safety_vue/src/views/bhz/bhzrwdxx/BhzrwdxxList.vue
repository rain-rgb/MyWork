<template>
  <a-card :bordered="false">
    <div class="materialtj">
      <a-button type="primary" ghost>未审核：{{ unreviewed }}条</a-button>
      <a-button type="primary" ghost>已审核未配料：{{ reviewNotIngredients }}条</a-button>
      <a-button type="primary" ghost
        >已配料未生产：{{ ingredientsNotProduced }}条</a-button
      >
      <a-button type="primary" ghost>生产中：{{ inProduction }}条</a-button>
      <a-button type="primary" ghost>已完成：{{ done }}条</a-button>
      <a-button type="primary" ghost>滞后任务：{{ lag }}条</a-button>
    </div>
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="浇筑部位">
              <a-input
                placeholder="请输入浇筑部位"
                v-model="queryParam.conspos"
              ></a-input>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="状态">
              <j-dict-select-tag
                placeholder="请选择"
                v-model="queryParam.status"
                dictCode="task_status"
              ></j-dict-select-tag>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="开盘日期范围">
              <j-date
                placeholder="开始日期"
                v-model="queryParam.dattim_begin"
                :showTime="true"
                dateFormat="YYYY-MM-DD HH:mm:ss"
              />
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="">
              <j-date
                placeholder="结束日期"
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
            </span>
          </a-col>
        </a-row>
      </a-form>
    </div>
    <!-- 查询区域-END -->

    <!-- 操作按钮区域 -->
    <!--    <div class="table-operator">-->
    <!--      <a-button @click="handleAdd" type="primary" icon="plus">新增</a-button>-->
    <!--      <a-button type="primary" icon="download" @click="handleExportXls('bhzrwdxx')">导出</a-button>-->
    <!--      <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl" @change="handleImportExcel">-->
    <!--        <a-button type="primary" icon="import">导入</a-button>-->
    <!--      </a-upload>-->
    <!-- 高级查询区域 -->
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
        <template slot="tags" slot-scope="tags, record">
          <span>{{
            record.jzlsts == 5
              ? (Number(record.metes) - Number(record.mete)).toFixed(2)
              : ""
          }}</span>
        </template>
        <template slot="bfb" slot-scope="text, record, index">
          <a-progress
            :strokeColor="getPercentColor(record.bfb)"
            :format="getPercentFormat"
            :percent="getFlowRateNumber(record.bfb)"
            style="width: 100px"
          />
        </template>
        <span slot="jzlsts" slot-scope="jzlsts, record">
          <a-tag color="red" v-if="record.jzlsts == 0">待配料</a-tag>
          <a-tag color="yellow" v-if="record.jzlsts == 1">待配料</a-tag>
          <!-- <a-tag color="orange" v-if="record.jzlsts == 2">审核未配料</a-tag> -->
          <a-tag color="orange" v-if="record.jzlsts == 3">配料未生产</a-tag>
          <a-tag color="blue" v-if="record.jzlsts == 4">生产中</a-tag>
          <a-tag color="green" v-if="record.jzlsts == 5">已完成</a-tag>
          <a-tag color="red" v-if="record.jzlsts == 6">已滞后</a-tag>
        </span>

        <span slot="action" slot-scope="text, record">
          <a @click="qualityDetail(record)">质量</a>

           <a-divider type="vertical" />
          <a-dropdown>
            <a class="ant-dropdown-link">更多 <a-icon type="down" /></a>
            <a-menu slot="overlay">
              <!-- <a-menu-item>
                <a @click="handleDetail(record)">详情</a>
              </a-menu-item> -->
              <a-menu-item>
                <a @click="pourDetail(record)">浇筑令详情</a>
              </a-menu-item>
              <a-menu-item>
                <a @click="transportDetail(record)">运输生产详情</a>
              </a-menu-item>
              <a-menu-item>
                <a @click="wztaizhangDetail(record)">检验批详情</a>
              </a-menu-item>
              <a-menu-item>
                <a @click="jiaozhuwanchengDetail(record)">浇筑完成</a>
              </a-menu-item>
              <!-- <a-menu-item>
                <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">
                  <a>删除</a>
                </a-popconfirm>
              </a-menu-item> -->
            </a-menu>
          </a-dropdown>
        </span>
      </a-table>
    </div>

    <bhzrwdxx-modal ref="modalForm" @ok="modalFormOk"></bhzrwdxx-modal>
    <pour-detail ref="pour"></pour-detail>
    <transport-detail ref="transport"></transport-detail>
    <wztaizhang-detail ref="wztaizhangDetail"></wztaizhang-detail>
    <quality-detail ref="qualityDetail"></quality-detail>
  </a-card>

</template>

<script>
import "@/assets/less/TableExpand.less";
import { mixinDevice } from "@/utils/mixin";
import { JeecgListMixin } from "@/mixins/JeecgListMixin";
import BhzrwdxxModal from "./modules/BhzrwdxxModal";
import PourDetail from "./modules/PourDetail";
import TransportDetail from "./modules/TransportDetail";
import wztaizhangDetail from "./modules/wztaizhangDetail";

import { getAction } from "@/api/manage";
import QualityDetail from '@views/bhz/bhzrwdxx/modules/qualityDetail.vue'


export default {
  name: "BhzrwdxxList",
  mixins: [JeecgListMixin, mixinDevice],
  components: {
    QualityDetail,
    BhzrwdxxModal,
    PourDetail,
    TransportDetail,
    wztaizhangDetail,
  },
  data() {
    return {
      description: "bhzrwdxx管理页面",
      unreviewed: "0",
      reviewNotIngredients: "0",
      ingredientsNotProduced: "0",
      inProduction: "0",
      done: "0",
      lag: "0",
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
          title: "浇筑令编号",
          align: "center",
          dataIndex: "rwdcode",
        },
        {
          title: "开盘日期",
          align: "center",
          dataIndex: "begtim",
          customRender: function (text) {
            return !text ? "" : text.length > 10 ? text.substr(0, 10) : text;
          },
        },
        {
          title: "状态",
          align: "center",
          dataIndex: "jzlsts",
          scopedSlots: { customRender: "jzlsts" },
        },
        {
          title: "浇筑部位",
          align: "center",
          dataIndex: "conspos",
        },
        {
          title: "强度等级",
          align: "center",
          dataIndex: "betlev",
        },
        {
          title: "任务方量",
          align: "center",
          dataIndex: "mete",
        },
        {
          title: "实际方量",
          align: "center",
          dataIndex: "metes",
        },
        {
          title: "节超方量",
          align: "center",
          dataIndex: "jcfl",
          scopedSlots: { customRender: "tags" },
        },
        {
          title: "运输方量",
          align: "center",
          dataIndex: "note",
        },
        {
          title: "确认完成人",
          align: "center",
          dataIndex: "wcren",
        },
        {
          title: "确认完成时间",
          align: "center",
          dataIndex: "wctime",
        },
        {
          title: "进度",
          align: "center",
          dataIndex: "bfb",
          width: 147,
          fixed: "right",
          scopedSlots: { customRender: "bfb" },
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
        list: "/bhzrwdxx/bhzrwdxx/listjzl",
        delete: "/bhzrwdxx/bhzrwdxx/delete",
        deleteBatch: "/bhzrwdxx/bhzrwdxx/deleteBatch",
        exportXlsUrl: "/bhzrwdxx/bhzrwdxx/exportXls",
        importExcelUrl: "bhzrwdxx/bhzrwdxx/importExcel",
        wancheng: '/bhzrwdxx/bhzrwdxx/wancheng'
      },
      dictOptions: {},
      superFieldList: [],
    };
  },
  created() {
    this.getSuperFieldList();
    this.getTJ();
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
      fieldList.push({
        type: "int",
        value: "station",
        text: "生产线（0公用 1 第一生产线  2 第二生产线）",
      });
      fieldList.push({ type: "string", value: "rwdcode", text: "任务单编号" });
      fieldList.push({ type: "date", value: "dattim", text: "创建时间" });
      fieldList.push({ type: "string", value: "attribute", text: "任务性质" });
      fieldList.push({ type: "string", value: "recipe", text: "1线配合比编号" });
      fieldList.push({ type: "string", value: "recipes", text: "2线配合比编号" });
      fieldList.push({ type: "string", value: "contract", text: "合同信息" });
      fieldList.push({ type: "string", value: "customer", text: "客户名称" });
      fieldList.push({ type: "string", value: "projname", text: "工程名称" });
      fieldList.push({ type: "string", value: "projtype", text: "工程类别" });
      fieldList.push({ type: "string", value: "projgrade", text: "工程级别" });
      fieldList.push({ type: "number", value: "projarea", text: "开工面积" });
      fieldList.push({ type: "string", value: "projadr", text: "施工地址" });
      fieldList.push({ type: "number", value: "distance", text: "运输距离" });
      fieldList.push({ type: "string", value: "conspos", text: "施工部位" });
      fieldList.push({ type: "string", value: "pour", text: "浇筑方式" });
      fieldList.push({ type: "string", value: "variety", text: "产品种类" });
      fieldList.push({ type: "string", value: "betlev", text: "强度等级" });
      fieldList.push({ type: "string", value: "filter", text: "抗渗等级" });
      fieldList.push({ type: "string", value: "freeze", text: "抗冻等级" });
      fieldList.push({ type: "string", value: "lands", text: "坍落度" });
      fieldList.push({ type: "string", value: "cement", text: "水泥品种" });
      fieldList.push({ type: "string", value: "stone", text: "石子种类" });
      fieldList.push({ type: "string", value: "bnsize", text: "骨料粒径" });
      fieldList.push({ type: "string", value: "addliq", text: "外加剂要求" });
      fieldList.push({ type: "string", value: "request", text: "技术要求" });
      fieldList.push({ type: "number", value: "mixlast", text: "搅拌时间" });
      fieldList.push({ type: "number", value: "mete", text: "任务方量" });
      fieldList.push({ type: "date", value: "begtim", text: "浇注日期" });
      fieldList.push({ type: "date", value: "endtim", text: "截止日期" });
      fieldList.push({ type: "string", value: "attamper", text: "调度人员" });
      fieldList.push({ type: "string", value: "flag", text: "标识" });
      fieldList.push({ type: "string", value: "note", text: "备注" });
      fieldList.push({ type: "int", value: "isdel", text: "是否删除 0未删除 1已删除" });
      fieldList.push({ type: "int", value: "jzlsts", text: "0 未审核  1已审核" });
      fieldList.push({ type: "string", value: "treeid", text: "施工部位节点id" });
      fieldList.push({ type: "number", value: "metes", text: "生产方量" });
      fieldList.push({ type: "number", value: "bfb", text: "进度百分比" });
      fieldList.push({ type: "int", value: "jzlsts", text: "jzlsts" });
      this.superFieldList = fieldList;
    },
    pourDetail(record) {
      this.$refs.pour.getList(record);
      this.$refs.pour.pourForm = Object.assign({}, record);
      this.$refs.pour.visible = true;
    },
    getPercentColor(value) {
      let p = Number(value);
      if (p >= 90 && p < 100) {
        return "rgb(244, 240, 89)";
      } else if (p >= 100) {
        return "red";
      } else {
        return "rgb(16, 142, 233)";
      }
    },
    getPercentFormat(value) {
      if (value == 100) {
        return "完成";
      } else {
        return value + "%";
      }
    },
    getFlowRateNumber(value) {
      return Number(value);
    },
    getTJ() {
      let params = {
        orgCode: this.sys_depart_orgcode,
      };
      getAction("/system/bhzrenwudan/taskSta3", params).then((res) => {
        let list = res.result;

        list.forEach((re) => {
          if (re.status == 0) {
            this.unreviewed = re.iszjzl;
          } else if (re.status == 1 || re.status == 2) {
            this.reviewNotIngredients = re.iszjzl;
          } else if (re.status == 3) {
            this.ingredientsNotProduced = re.iszjzl;
          } else if (re.status == 4) {
            this.inProduction = re.iszjzl;
          } else if (re.status == 5) {
            this.done = re.iszjzl;
          } else if (re.status == 6) {
            this.lag = re.iszjzl;
          }
        });
      });
    },
    transportDetail(record) {
      this.$refs.transport.getTransport(record.rwdcode);
      this.$refs.transport.visible = true;
    },
    wztaizhangDetail(record) {
      this.$refs.wztaizhangDetail.getWztaizhang(record.rwdcode);
      this.$refs.wztaizhangDetail.visible = true;
    },
    qualityDetail(record) {
      this.$refs.qualityDetail.getQuality(record);
      this.$refs.qualityDetail.visible = true;
    },
    jiaozhuwanchengDetail(record) {
      try {
        // 调用重置接口，根据实际情况修改请求参数
        getAction(this.url.wancheng, { code: record.rwdcode }).then((res) => {
          this.$message.success(res.result)
          // 刷新表格数据
          this.loadData()
        })
      } catch (error) {
        console.error('设置失败', error)
        this.$message.error('设置失败，请稍后重试')
      }
    },
  },
};
</script>
<style scoped>
@import "~@assets/less/common.less";
.materialtj {
  margin-bottom: 24px;
  display: flex;
  justify-content: space-around;
}
</style>
