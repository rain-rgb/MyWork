<template>
  <!-- 摊铺速度 -->
  <a-card :bordered="false">
    <div class="table-page-search-wrapper">
      <a-form layout="inline">
        <a-row :gutter="24">
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="组织机构">
              <j-search-select-tag placeholder="请选择组织机构"> </j-search-select-tag>
              <!-- {{ selectValue }} -->
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="工程名称">
              <j-dict-select-tag style="width: 200px" type="list" placeholder="" />
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="选择工程">
              <j-dict-select-tag style="width: 200px" type="list" placeholder="" />
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="选择标段">
              <j-dict-select-tag style="width: 200px" type="list" placeholder="" />
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="选择结构层">
              <j-dict-select-tag
                style="width: 200px"
                type="list"
                placeholder="请选择设备"
              />
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="开始桩号">
              <j-dict-select-tag
                style="width: 200px"
                type="list"
                placeholder="请选择设备"
              />
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="结束桩号">
              <j-dict-select-tag
                style="width: 200px"
                type="list"
                placeholder="请选择设备"
              />
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="时间范围">
              <j-date placeholder="开始" dateFormat="YYYY-MM-DD HH:mm:ss" />
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="">
              <j-date placeholder="结束" dateFormat="YYYY-MM-DD HH:mm:ss" />
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <span
              style="float: left; overflow: hidden"
              class="table-page-search-submitButtons"
            >
              <a-button type="primary" @click="chaxun" icon="search">查询</a-button>
              <a-button
                type="primary"
                @click="chongzhi"
                icon="reload"
                style="margin-left: 8px"
                >重置</a-button
              >
            </span>
          </a-col>
        </a-row>
      </a-form>
      <a-row>
        <a-card
          title="摊铺速度统计"
          :bordered="false"
          :headStyle="{ color: '#0785fd' }"
          :bodyStyle="{ padding: '10' }"
        >
          <div>
            <!-- <LineChartMultid :data-source="dataSource2" /> -->
          </div>
          <div>
            <div id="pavingspeed"></div>
          </div>
        </a-card>
      </a-row>
    </div>

    <!-- 操作按钮区域 -->
    <div class="table-operator">
      <a-button @click="handleAdd" v-has="'hntbhz:add'" type="primary" icon="plus"
        >新增</a-button
      >
      <a-button
        type="primary"
        v-has="'hntbhz:dc'"
        icon="download"
        @click="handleExportXls('拌合站主表')"
        >导出</a-button
      >
      <a-upload
        name="file"
        :showUploadList="false"
        :multiple="false"
        :action="importExcelUrl"
        @change="handleImportExcel"
      >
        <!-- :headers="tokenHeader" -->
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
          <a v-has="'hntbhz:edit'" @click="handleEdit(record)">编辑</a>

          <a-divider type="vertical" />
          <a-dropdown>
            <a class="ant-dropdown-link">更多 <a-icon type="down" /></a>
            <a-menu slot="overlay">
              <a-menu-item>
                <a @click="handleDetail(record)">详情</a>
              </a-menu-item>
              <a-menu-item v-has="'hntbhz:del'">
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
  </a-card>
</template>
<script>
// import BarAndMultidLine from "@comp/chart/BarAndMultidLine";
// import LineChartMultid from "@comp/chart/BhzStafangliang";
// import { getAction } from "@api/manage";
// import { usershebeiList } from "@api/api";
import * as echarts from "echarts";
// import { JeecgListMixin } from "@/mixins/JeecgListMixin";
// import BhzCementBaseModal from './modules/BhzCementBaseModal'
import { filterMultiDictText } from "@/components/dict/JDictSelectUtil";
import "@/assets/less/TableExpand.less";
import JSuperQuery from "@/components/jeecg/JSuperQuery.vue";
import { handertoken } from "@/mixins/getHanderToken";
import { SYS_DEPART_ORGCODE } from "@/store/mutation-types";
import Vue from "vue";
export default {
  name: "pavingspeed",
  // mixins: [JeecgListMixin, handertoken],
  components: {
    // BarAndMultidLine,
    // // Pie,
    // LineChartMultid,
    // JSuperQuery
    // BhzCementBaseModal,
    JSuperQuery,
  },
  data() {
    return {
      approval_remarks: null,
      selectValue: "",
      description: '拌合站主表管理页面',
      asyncSelectValue: "",
      dictOption: [],
      datalist: [],
      dataSource: [],
      fields: [],
      aliases: [],
      dataSource1: [],
      dataSource2: [],
      url: {
        list: '/hntbhz/bhzCementBase/list',
        // list: '/hntbhz/bhzCementBase/listrc',
        delete: '/hntbhz/bhzCementBase/delete',
        deleteBatch: '/hntbhz/bhzCementBase/deleteBatch',
        exportXlsUrl: '/hntbhz/bhzCementBase/exportXls',
        importExcelUrl: 'hntbhz/bhzCementBase/importExcel',
      },
      dictOptions: {},
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
        // {
        //   title:'所属组织机构',
        //   align:"center",
        //   dataIndex: 'sysOrgCode_dictText'
        // },
        {
          title: "设备名称",
          align: "center",
          dataIndex: "shebeiNo_dictText",
        },

        {
          title: "工程名称",
          align: "center",
          dataIndex: "projectName",
        },
        {
          title: "配方号/配比单号",
          align: "center",
          dataIndex: "formulaNo",
          scopedSlots: { customRender: "formulaNo" },
        },
        {
          title: "施工地点",
          align: "center",
          dataIndex: "jobLocation",
        },
        {
          title: "浇筑部位",
          align: "center",
          dataIndex: "poureLocation",
        },
        {
          title: "出料时间",
          align: "center",
          dataIndex: "productDatetime",
        },
        {
          title: "强度等级",
          align: "center",
          dataIndex: "strengthRank",
        },
        {
          title: "操作者",
          align: "center",
          dataIndex: "handlers",
        },
        {
          title: "方量",
          align: "center",
          dataIndex: "estimateNumber",
        },

        {
          title: "坍落度",
          align: "center",
          dataIndex: "slump",
        },
        {
          title: "超标等级",
          align: "center",
          dataIndex: "overLevel_dictText", //overLevel_dictText
          key: "overLevel_dictText",
          scopedSlots: { customRender: "tags" },
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
      superFieldList: [],
      ipagination:{
        total: 0,
        pageSize: 10,//每页中显示10条数据
        showSizeChanger: true,
        pageSizeOptions: ["10", "20", "50", "100"],//每页中显示的数据
        showTotal: total => `共有 ${total} 条数据`,  //分页中显示总的数据
        current:1
      },
      loading: true,
      form: {
        startTime: "2020-05-19",
        endTime: "2020-05-19",
      },
      selectedRowKeys:[],
      tokenHeader:[],
    };
  },
  created() {
    // this.bhzsta();
    // this.fanglaing();
    // this.hegelv();
    // this.getToken();
    // this.getSuperFieldList();
    // this.shebeiList();

    this.username();
  },
  mounted() {
    // this.getecharts();
  },
  computed: {
    importExcelUrl: function () {
      return `${window._CONFIG["domianURL"]}/${this.url.importExcelUrl}`;
    },
  },
  methods: {
    username() {
      let param = { appId: "KFPT1", pw: 123456789 };
      this.$http1.post(`/api/token/get.shtml`, param).then((res) => {
        if (res.message == "请求成功") {
          this.$store.state.key = res.data.token;
          // console.log(res.data.token, "res.data.data.token---------------------");
          this.getData();
          this.getChartOne();
          // this.getChartTwo();
          // this.getChartThree();
        }
      });
    },
    getData() {
      let param = {
        projectId: "jishu01@20201209210207",
        sectionId: "jishu01@20210428143945",
        startTime: this.form.startTime,
        endTime: this.form.endTime,
        pageSize: this.ipagination.pageSize,
        pageNo: this.ipagination.current,
      };
      this.$http1.post(`/api/pavement/stationData.shtml`, param).then((res) => {
        if (res.code == 0) {
          this.dataSource = res.data.records;
          const pagination = { ...this.pagination };
          pagination.total = res.data.rowCount;
          this.ipagination = pagination;
          console.log(this.dataSource,res, "getData 摊铺速度------------------------");
        }
        this.loading = false;
      });
    },
    //摊铺速度统计
    getChartOne() {
      let param = {
        projectId: "jishu01@20200813165458",
        sectionId: "jishu01@20201023010249",
        startTime: this.form.startTime,
        endTime: this.form.endTime,
      };
      this.$http1.post(`/api/pavement/paveSpeedPass.shtml`, param).then((res) => {
        if (res.code === 0) {
          let arr = res.data;
          this.getecharts(arr);
          // this.getecharts1(arr);
          console.log(res, "getChartOne 摊铺速度统计------------------------");
        }
        this.loading = false;
      });
    },
    getecharts(data) {
      let that = this;
      var colorList = [
        "green",
        "red",
      ];
      var arr = [
        { value: 0, name: "正常" },
        { value: 0, name: "过快" },
      ];
      arr[0].value = data.normal;
      arr[1].value = (1 - data.normal);
      let option = {
        color: colorList,
        legend: {
          orient: "vertical",
          left: "left",
          right: 60,
          data: ["正常", "过快"],
          // data: ["TJ01", "TJ02", "TJ03", "TJ04"],
          textStyle: {
            // color: "#fff",
            // fontSize: 10,
          },
        },
        tooltip: {
          trigger: "item",
          formatter: function (params) {
              var str = '';
              //首先要看看params是怎样的数据结构，里面有什么内容;
              console.log(params);
              //我这边是根据我的params结构进行操作
        		  str += params.marker+" "+ params.name+": "+params.value+`(${params.value.toFixed(2)})`+"</br>"
            return str
          },
        },
        series: [
          {
            type: "pie",
            center: ["50%", "50%"],
            radius: ["40%", "70%"],
            clockwise: true,
            avoidLabelOverlap: true,
            hoverOffset: 15,
            itemStyle: {
              normal: {
                color: function (params) {
                  return colorList[params.dataIndex];
                },
              },
            },
            label: {
              show: true,
              position: "outside",
              formatter: "{a|{b}}\n{hr|}",
              // formatter: "{a|{b}：{d}%}\n{hr|}",
              rich: {
                hr: {
                  backgroundColor: "t",
                  borderRadius: 3,
                  width: 3,
                  height: 3,
                  padding: [3, 3, 0, -12],
                },
                a: {
                  padding: [-30, 15, -20, 15],
                },
              },
            },
            labelLine: {
              normal: {
                length: 20,
                length2: 30,
                lineStyle: {
                  width: 1,
                },
              },
            },
            data: arr,
          },
        ],
      };
      let chart = echarts.init(document.getElementById("pavingspeed"));
      chart.setOption(option);
      //建议加上以下这一行代码，不加的效果图如下（当浏览器窗口缩小的时候）。超过了div的界限（红色边框）
      window.addEventListener("resize", function () {
        chart.resize();
      });
    },
    // getDatas(Starttime, Endtime) {
    //   //中间部分产能统计数据
    //   this.dataSource1 = [];
    //   let params = { statisticsTime_begin: Starttime, statisticsTime_end: Endtime };
    //   getAction(this.url.list1, params).then((res) => {
    //     if (res.success) {
    //       let data = res.result;
    //       if (data.length > 0) {
    //         for (let i = 0; i < data.length; i++) {
    //           this.dataSource1.push({
    //             type: data[i].statisticsTime + "月",
    //             jeecg: data[i].advancedlv,
    //             jeebt: data[i].middlelv,
    //             jeebts: data[i].primarylv,
    //             jeecgs: data[i].hegelv,
    //             总方量: data[i].estimateNumber,
    //           });
    //         }
    //       }
    //       //console.log("this.dataSource1",this.dataSource1)
    //     }
    //   });
    // },
    bhzsta: function () {
      //超标盘数柱状图
      let params = { shebeiNo: this.cx, date: this.dates };
      this.dataSource = [];
      getAction(this.url.list, params).then((res) => {
        this.fields = [];
        console.log(res);
        if (res.success) {
          let data = res.result;
          if (data.length > 0) {
            // this.dataapi(data);
          } else {
            this.$message.warn("该设备的超标盘数没有数据");
          }
        }
      });
    },
    fanglaing: function () {
      //方量折线图
      let params = { shebeiNo: this.cx, date: this.dates };
      this.dataSource2 = [];
      getAction(this.url.list, params).then((res) => {
        // console.log(res)
        if (res.success) {
          if (res.result.length > 0) {
            for (let i = 0; i < res.result.length; i++) {
              if (res.result[i].date === 0) {
                this.dataSource2.push({
                  type: res.result[i].statisticsTime + "年",
                  方量: res.result[i].estimateNumber,
                });
              } else if (res.result[i].date === 1) {
                this.dataSource2.push({
                  type: "第" + res.result[i].statisticsTime + "季",
                  方量: res.result[i].estimateNumber,
                });
              } else if (res.result[i].date === 2) {
                this.dataSource2.push({
                  type: res.result[i].statisticsTime + "月",
                  方量: res.result[i].estimateNumber,
                });
              } else {
                this.dataSource2.push({
                  type: res.result[i].statisticsTime,
                  方量: res.result[i].estimateNumber,
                });
              }
            }
          } else {
            this.$message.warn("该设备的方量没有数据");
          }
        }
      });
    },
    // shebeiList: function () {
    //   var sys_depart_orgcode = Vue.ls.get("SYS_DEPART_ORGCODE");
    //   usershebeiList({
    //     sys_depart_orgcode: sys_depart_orgcode,
    //     sbtypes: "0",
    //   }).then((res) => {
    //     this.dictOption = [];
    //     let result = res.result;
    //     result.forEach((re) => {
    //       this.dictOption.push({ text: re.sbname, value: re.sbjno });
    //       this.datalist.push({ text: re.sbname, value: re.sbjno });
    //     });
    //     // this.datalist = JSON.parse(JSON.stringify(this.dictOption))
    //     //console.log(res)
    //   });
    // },
    chaxun: function () {
      this.cx = this.selectedValue;
      this.dates = this.approval_remarks;
      this.bhzsta();
      this.fanglaing();
      this.hegelv();
    },
    chongzhi: function () {
      this.dictOption = [];
      this.selectedValue = null;
      this.approval_remarks = null;
      this.chaxun();
    },
    dataapi(data) {
      let obj1 = { type: "初级超标" };
      let obj2 = { type: "中级超标" };
      let obj3 = { type: "高级超标" };
      let obj4 = { type: "合格" };
      // console.log("data",data)
      data.forEach((item) => {
        if (item.date === 0) {
          this.fields.push(item.statisticsTime + "年");
        } else if (item.date === 1) {
          this.fields.push("第" + item.statisticsTime + "季");
        } else if (item.date === 2) {
          this.fields.push(item.statisticsTime + "月");
        } else {
          this.fields.push(item.statisticsTime);
        }
      });
      this.fields.forEach((item, index) => {
        obj1[item] = data[index].primaryDish;
        obj2[item] = data[index].middleDish;
        obj3[item] = data[index].advancedDish;
        obj4[item] = data[index].hegeDish;
      });
      this.dataSource.push(obj1, obj2, obj3, obj4);
      //console.log(this.dataSource)
    },
    getSuperFieldList() {
      let fieldList = [];
      fieldList.push({ type: "string", value: "batchNo", text: "唯一ID", dictCode: "" });
      fieldList.push({ type: "string", value: "shebeiNo", text: "设备编号", dictCode: "" });
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
      fieldList.push({ type: "string", value: "formulaNo", text: "配方号", dictCode: "" });
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
      fieldList.push({ type: "int", value: "alertstate", text: "是否超标", dictCode: "" });
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
    handleImportExcel(){},
    handleTableChange(){},
    handleAdd(){},
    onClearSelected(){},
    onSelectChange(){},
  },

};
</script>
<style scope>
@import '~@assets/less/common.less';
#pavingspeed {
  width: 100%;
  height: 250px;
}
</style>
