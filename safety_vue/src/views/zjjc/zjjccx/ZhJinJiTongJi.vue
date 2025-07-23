<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery1">
        <a-row :gutter="24">
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="设备名称">
              <j-search-select-tag
                placeholder="请选择设备名称"
                v-model="queryParam.shebeino"
                :dictOptions="dictOption"
              >
              </j-search-select-tag>
              {{ selectValue }}
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="时间范围">
              <j-search-select-tag
                style="width: 200px"
                v-model="approval_remarks"
                placeholder="请选择时间"
                :dictOptions="dictOption1"
                @change="onChange()"
              />
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <span
              style="float: left; overflow: hidden"
              class="table-page-search-submitButtons"
            >
              <a-button type="primary" @click="searchQuery1" icon="search">查询</a-button>
              <a-button
                type="primary"
                @click="searchReset1"
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

    <a-row>
      <a-card
        title="桩基检测数量"
        :bordered="false"
        :headStyle="{ color: '#0785fd' }"
        :bodyStyle="{ padding: '10' }"
      >
        <div class="chartBox">
          <bar-multid
            :data-source="dataSource"
            :fields="fields"
            style="width: 50%"
            title="成孔效果图"
          />
          <bar-multid
            :data-source="dataSource1"
            :fields="fields1"
            style="width: 50%"
            title="桩基效果图"
          />
        </div>
      </a-card>
      <a-card
        title="桩基评级统计分析"
        :bordered="false"
        :headStyle="{ color: '#0785fd' }"
        :bodyStyle="{ padding: '10' }"
      >
        <div class="chartBox">
          <pie :data-source="dataSource2" style="width: 40%" title="成孔评级效果图" />
          <pie :data-source="dataSource3" style="width: 40%" title="桩基评级效果图" />
        </div>
      </a-card>
    </a-row>
    <div></div>
  </a-card>
</template>

<script>
import { JeecgListMixin } from "@/mixins/JeecgListMixin";
import ZhJinJiCeBaseModal from "./modules/ZhJinJiCeBaseModal";
import { filterMultiDictText } from "@/components/dict/JDictSelectUtil";
import "@/assets/less/TableExpand.less";
import JSuperQuery from "@/components/jeecg/JSuperQuery.vue";
import { handertoken } from "@/mixins/getHanderToken";
import { mixinDevice } from "@/utils/mixin";
import Vue from "vue";
import { SYS_DEPART_ORGCODE } from "@/store/mutation-types";
import { usershebeiList } from "@api/api";
import { getAction } from "@api/manage";
import BarMultid from "@comp/chart/BhzCementSta";
import Pie from "@comp/chart/BhzStaPie";

export default {
  name: "ZhJinJiTongJi",
  mixins: [JeecgListMixin, mixinDevice, handertoken],
  components: {
    ZhJinJiCeBaseModal,
    JSuperQuery,
    BarMultid,
    Pie,
  },
  data() {
    return {
      selectValue: "",
      asyncSelectValue: "",
      dictOption: [],
      description: "桩基主表管理页面",
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
          title: "所属分部",
          align: "center",
          dataIndex: "bumen",
        },
        {
          title: "设备编号",
          align: "center",
          dataIndex: "shebeino_dictText",
        },
        {
          title: "工程名称",
          align: "center",
          dataIndex: "projectName",
        },
        {
          title: "检测单位",
          align: "center",
          dataIndex: "jcdw",
        },
        {
          title: "受检单位",
          align: "center",
          dataIndex: "sgdw",
        },
        {
          title: "施工部位",
          align: "center",
          dataIndex: "sgbw",
        },

        {
          title: "任务单号 ",
          align: "center",
          dataIndex: "liushuihao",
        },
        {
          title: "试桩类型",
          align: "center",
          dataIndex: "shizhuangleixing",
          scopedSlots: { customRender: "shizhuangleixing" },
        },
        {
          title: "测试日期 ",
          align: "center",
          dataIndex: "ceshitime",
        },
        {
          title: "上传时间",
          align: "center",
          dataIndex: "dangqiantime",
        },
        {
          title: "试桩编号",
          align: "center",
          dataIndex: "shizhuangno",
          scopedSlots: { customRender: "shizhuangno" },
        },
        {
          title: "桩径",
          align: "center",
          dataIndex: "zhuangjing",
        },
        {
          title: "桩长 ",
          align: "center",
          dataIndex: "zhuangchang",
        },
        {
          title: "管数",
          align: "center",
          dataIndex: "guanshu",
        },
        {
          title: "剖面数",
          align: "center",
          dataIndex: "poumianshu",
        },

        {
          title: "经度",
          align: "center",
          dataIndex: "jingdu",
        },
        {
          title: "纬度 ",
          align: "center",
          dataIndex: "weidu",
        },
        {
          title: "采样频率",
          align: "center",
          dataIndex: "caiyangpinlv",
        },
        {
          title: "采样长度",
          align: "center",
          dataIndex: "caiyanglength",
        },
        {
          title: "声测管内径",
          align: "center",
          dataIndex: "shengceguanneijing",
        },
        {
          title: "声测管外径",
          align: "center",
          dataIndex: "shengceguanwaijing",
        },
        {
          title: "方位角",
          align: "center",
          dataIndex: "fangweijiao",
        },
        {
          title: "轮径 ",
          align: "center",
          dataIndex: "lunjing",
        },
        {
          title: "线径 ",
          align: "center",
          dataIndex: "xianjing",
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
        list: "/chaoshengbo/chaoshengboSyjbsj/list",
        delete: "/chaoshengbo/chaoshengboSyjbsj/delete",
        deleteBatch: "/chaoshengbo/chaoshengboSyjbsj/deleteBatch",
        exportXlsUrl: "/chaoshengbo/chaoshengboSyjbsj/exportXls",
        importExcelUrl: "chaoshengbo/chaoshengboSyjbsj/importExcel",

        listBar: "/kongjingbasicinfo/kongjingBasicinfo/getListKJStatistics",
        listBar1: "/chaoshengbo/chaoshengboSyjbsj/getListZJStatistics",
        listPie: "/kongjingbasicinfo/kongjingBasicinfo/statisticsKJData",
        listPie1: "/chaoshengbo/chaoshengboSyjbsj/statisticsZJData",
      },
      dictOptions: {},
      superFieldList: [],
      approval_remarks: 0,
      dictOption1: [
        {
          text: "年",
          value: "0",
        },
        {
          text: "季",
          value: "1",
        },
        {
          text: "月",
          value: "2",
        },
        {
          text: "周",
          value: "3",
        },
        {
          text: "上年",
          value: "4",
        },
      ],
      dataSource: [],
      fields: [],
      dataSource1: [],
      fields1: [],
      dataSource2: [],
      dataSource3: [],
      dates: 0,
      chartStyle: { width: "49%", height: "350px" },
    };
  },
  created() {
    this.getToken();
    // this.getSuperFieldList();
    this.shebeiList();
    setTimeout(() => {
      this.listBar();
      this.listPie();
    }, 500);
  },
  computed: {
    importExcelUrl: function () {
      return `${window._CONFIG["domianURL"]}/${this.url.importExcelUrl}`;
    },
  },
  methods: {
    searchQuery1: function () {
      this.cx = this.selectedValue;
      this.dates = this.approval_remarks;
      this.listBar();
      this.listPie();
    },
    searchReset1() {
      this.cx = "";
      this.dates = "";
      this.approval_remarks = "";
      this.queryParam.shebeino = "";
      this.listBar();
      this.listPie();
    },
    onChange() {},
    listBar: function () {
      //柱状图
      let params = { shebeino: this.queryParam.shebeino, date: this.dates };
      console.log(params);
      this.dataSource = [];
      getAction(this.url.listBar, params).then((res) => {
        this.fields = [];
        // console.log(res)
        if (res.success) {
          let data = res.result;
          if (data.length > 0) {
            let dataBar = this.dataapi(data, `成孔效果图`);
            this.fields = dataBar[0];
            this.dataSource = dataBar[1];
            console.log(data, "data----------");
          } else {
            this.$message.warn("暂无成孔效果图数据");
            this.dataSource = [];
          }
        }
      });
      this.dataSource1 = [];
      getAction(this.url.listBar1, params).then((res) => {
        this.fields1 = [];
        // console.log(res)
        if (res.success) {
          let data = res.result;
          if (data.length > 0) {
            let dataBar = this.dataapi(data, `桩基效果图`);
            this.fields1 = dataBar[0];
            this.dataSource1 = dataBar[1];
          } else {
            this.$message.warn("暂无桩基效果图数据");
            this.dataSource1 = [];
          }
        }
      });
    },
    dataapi(data, title) {
      let obj1 = { type: "Ⅰ类" };
      let obj2 = { type: "Ⅱ类" };
      let obj3 = { type: "Ⅲ类" };
      let fields = [];
      let dataSource = [];
      // fields.push(title)
      // obj1[title] = data.level
      // obj2[title] = data.level2
      // obj3[title] = data.level3
      let dateTar = "testpiledate";
      if (title != "成孔效果图") {
        dateTar = "ceshitime";
      }
      data.forEach((item) => {
        if (this.dates == 0) {
          fields.push(item[dateTar] + "年");
        } else if (this.dates == 1) {
          fields.push("第" + Number(item[dateTar]).toFixed(0) + "季");
        } else if (this.dates == 2) {
          fields.push(item[dateTar] + "月");
        } else {
          fields.push(item[dateTar]);
        }
      });
      fields.forEach((item, index) => {
        obj1[item] = data[index].level1;
        obj2[item] = data[index].level2;
        obj3[item] = data[index].level3;
      });
      dataSource.push(obj1, obj2, obj3);
      return [fields, dataSource];
    },
    listPie: function () {
      //合格率饼图
      let params = { shebeino: this.queryParam.shebeino, date: this.dates };
      this.dataSource2 = [];
      getAction(this.url.listPie, params).then((res) => {
        if (res.success) {
          let data = res.result;
          let sum = data.level1 + data.level2 + data.level3;
          let level1 = (level1 / sum) * 100;
          let level2 = (level2 / sum) * 100;
          let level3 = (level3 / sum) * 100;
          if (data.level1 === 0 && data.level2 === 0 && data.level2 === 0) {
            // if (this.dates !== null && Number(this.dates) === 0){
            //   this.$message.warn("今年没有成孔饼图数据!")
            // }else if (Number(this.dates) === 1){
            //   this.$message.warn("本季没有成孔饼图数据!")
            // }else if (Number(this.dates) === 2){
            //   this.$message.warn("本月没有成孔饼图数据!")
            // }else  if (Number(this.dates) === 3){
            //   this.$message.warn("今周没有成孔饼图数据!")
            // }  else  if (Number(this.dates) === 4){
            //   this.$message.warn("上年没有成孔饼图数据!")
            // }
            this.$message.warn("暂无成孔评级效果数据!");
            // this.dataSource2.push(
            //   { item: 'Ⅰ类', count: 0 },
            //   { item: 'Ⅱ类', count: 0 },
            //   { item: 'Ⅲ类', count: 0 },);
            // console.log(message)
          } else {
            this.dataSource2.push(
              { item: "Ⅰ类", count: data.level1 },
              { item: "Ⅱ类", count: data.level2 },
              { item: "Ⅲ类", count: data.level3 }
            );
          }
        }
      });
      this.dataSource3 = [];
      getAction(this.url.listPie1, params).then((res) => {
        // console.log(res)
        if (res.success) {
          let data = res.result;
          let sum = data.level1 + data.level2 + data.level3;
          let level1 = (level1 / sum) * 100;
          let level2 = (level2 / sum) * 100;
          let level3 = (level3 / sum) * 100;
          if (data.level1 === 0 && data.level2 === 0 && data.level2 === 0) {
            // if (this.dates !== null && Number(this.dates) === 0){
            //   this.$message.warn("今年没有桩基饼图数据!")
            // }else if (Number(this.dates) === 1){
            //   this.$message.warn("本月没有桩基饼图数据!")
            // }else if (Number(this.dates) === 2){
            //   this.$message.warn("本周没有桩基饼图数据!")
            // }else {
            //   this.$message.warn("今天没有桩基饼图数据!")
            // }
            this.$message.warn("暂无桩基评级效果图数据!");
            // this.dataSource3.push(
            //   { item: 'Ⅰ类', count: 0 },
            //   { item: 'Ⅱ类', count: 0 },
            //   { item: 'Ⅲ类', count: 0 },);
            // console.log(message)
          } else {
            this.dataSource3.push(
              { item: "Ⅰ类", count: data.level1 },
              { item: "Ⅱ类", count: data.level2 },
              { item: "Ⅲ类", count: data.level3 }
            );
          }
        }
      });
    },
    shebeiList: function () {
      var sys_depart_orgcode = Vue.ls.get("SYS_DEPART_ORGCODE");
      usershebeiList({
        sys_depart_orgcode: sys_depart_orgcode,
        sbtypes: "14",
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
      fieldList.push({ type: "string", value: "batchNo", text: "唯一ID", dictCode: "" });
      fieldList.push({
        type: "string",
        value: "shebeiNo",
        text: "设备编号",
        dictCode: "",
      });
      fieldList.push({ type: "string", value: "workNo", text: "工单号", dictCode: "" });
      fieldList.push({ type: "string", value: "handlers", text: "操作者", dictCode: "" });
      fieldList.push({
        type: "string",
        value: "projectName",
        text: "工程名称",
        dictCode: "",
      });
      fieldList.push({
        type: "string",
        value: "jobLocation",
        text: "施工地点",
        dictCode: "",
      });
      fieldList.push({
        type: "string",
        value: "poureLocation",
        text: "浇筑部位",
        dictCode: "",
      });
      fieldList.push({
        type: "string",
        value: "cementVariety",
        text: "水泥品种",
        dictCode: "",
      });
      fieldList.push({
        type: "string",
        value: "additiveVariety",
        text: "外加剂品种",
        dictCode: "",
      });
      fieldList.push({
        type: "string",
        value: "formulaNo",
        text: "配方号",
        dictCode: "",
      });
      fieldList.push({
        type: "string",
        value: "strengthRank",
        text: "强度等级",
        dictCode: "",
      });
      fieldList.push({
        type: "int",
        value: "stirDatetime",
        text: "搅拌时间",
        dictCode: "",
      });
      fieldList.push({ type: "date", value: "saveDatetime", text: "保存时间" });
      fieldList.push({
        type: "string",
        value: "clientNo",
        text: "客户端编号",
        dictCode: "",
      });
      fieldList.push({ type: "int", value: "status", text: "状态", dictCode: "" });
      fieldList.push({ type: "date", value: "collectDatetime", text: "采集时间" });
      fieldList.push({
        type: "double",
        value: "estimateNumber",
        text: "方量",
        dictCode: "",
      });
      fieldList.push({ type: "date", value: "productDatetime", text: "出料时间" });
      fieldList.push({ type: "string", value: "slump", text: "坍落度", dictCode: "" });
      fieldList.push({
        type: "int",
        value: "overLevel",
        text: "超标等级",
        dictCode: "over_level",
      });
      fieldList.push({
        type: "int",
        value: "alertstate",
        text: "是否超标",
        dictCode: "",
      });
      fieldList.push({
        type: "string",
        value: "formulaId",
        text: "配方uuid(车结束符)",
        dictCode: "",
      });
      fieldList.push({
        type: "int",
        value: "timeOverLevel",
        text: "搅拌时间超标等级",
        dictCode: "",
      });
      this.superFieldList = fieldList;
    },
  },
};
</script>
<style scoped>
@import "~@assets/less/common.less";
.chartBox {
  display: flex;
  justify-content: space-between;
  /* background-color: #8a6e6e; */
}
</style>
