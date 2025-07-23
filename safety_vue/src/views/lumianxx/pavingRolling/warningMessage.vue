<template>
  <!-- 告警信息 -->
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
            <a-form-item label="告警类型">
              <j-dict-select-tag
                style="width: 200px"
                type="list"
                placeholder="请选择参数类型"
              />
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="机械类型">
              <j-dict-select-tag
                style="width: 200px"
                type="list"
                placeholder="请选择机械类型"
              />
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="时间段">
              <j-date v-model="form.startTime" placeholder="开始" dateFormat="YYYY-MM-DD HH:mm:ss" />
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="">
              <j-date v-model="form.endTime" placeholder="结束" dateFormat="YYYY-MM-DD HH:mm:ss" />
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
    </div>

    <!-- 操作按钮区域 -->
    <!-- <div class="table-operator">
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
        :headers="tokenHeader"
        :action="importExcelUrl"
        @change="handleImportExcel"
      >
        <a-button type="primary" v-has="'hntbhz:dr'" icon="import">导入</a-button>
      </a-upload> -->
    <!-- 禁用高级查询区域 -->
    <!--      <j-super-query :fieldList="superFieldList" ref="superQueryModal" @handleSuperQuery="handleSuperQuery"></j-super-query>-->
    <!--      <a-dropdown v-if="selectedRowKeys.length > 0">-->
    <!--        <a-menu slot="overlay">-->
    <!--          <a-menu-item key="1" v-has="'hntbhz:del'" @click="batchDel"><a-icon type="delete"/>删除</a-menu-item>-->
    <!--        </a-menu>-->
    <!--        <a-button style="margin-left: 8px"> 批量操作 <a-icon type="down" /></a-button>-->
    <!--      </a-dropdown>-->
    <!-- </div> -->
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
        :pagination="pagination"
        :loading="loading"
        :rowSelection="{ selectedRowKeys: selectedRowKeys, onChange: onSelectChange }"
        @change="handleTableChange"
      >
        <span slot="warringT" slot-scope="warringT, record">
          <a-tag color="orange" v-if="record.alarmTypeId == '1'">碾压超速</a-tag>
          <a-tag color="yellow" v-if="record.alarmTypeId == '2'">碾压温度过低</a-tag>
          <a-tag color="red" v-if="record.alarmTypeId == '3'">摊铺超速</a-tag>
          <a-tag color="red" v-if="record.alarmTypeId == '4'">摊铺温度过低</a-tag>
          <a-tag color="red" v-if="record.alarmTypeId == '5'">碾压距摊铺机过远</a-tag>
        </span>
        <span slot="alarmLevel" slot-scope="alarmLevel, record">
          <a-tag color="green" v-if="record.alarmLevel == '0'">无级别</a-tag>
          <a-tag color="orange" v-if="record.alarmLevel == '1'">轻微</a-tag>
          <a-tag color="yellow" v-if="record.alarmLevel == '2'">一般</a-tag>
          <a-tag color="red" v-if="record.alarmLevel == '3'">严重</a-tag>
        </span>
        <span slot="tags" slot-scope="tags, record">
          <a-tag color="green" v-if="record.overLevel == '0'">合格</a-tag>
          <a-tag color="orange" v-if="record.overLevel == '1'">初级超标</a-tag>
          <a-tag color="yellow" v-if="record.overLevel == '2'">中级超标</a-tag>
          <a-tag color="red" v-if="record.overLevel == '3'">高级超标</a-tag>
        </span>
        <!-- <span slot="formulaNo" slot-scope="formulaNo, record">
          <a-tag color="green">{{ record.formulaNo }}</a-tag>
        </span> -->
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
          <!-- <a v-has="'hntbhz:edit'" @click="handleEdit(record)">编辑</a> -->

          <!-- <a-divider type="vertical" /> -->
          <a-dropdown>
            <a class="ant-dropdown-link" @click="handlePostion(record)">点击定位</a>
            <!-- <a-menu slot="overlay">
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
            </a-menu> -->
          </a-dropdown>
        </span>
      </a-table>
    </div>
    <div id="container" style="height: 75vh"></div>
  </a-card>
</template>
<script>
// import BarAndMultidLine from "@comp/chart/BarAndMultidLine";
// import LineChartMultid from "@comp/chart/BhzStafangliang";
// import { getAction } from "@api/manage";
// import { usershebeiList } from "@api/api";
// import * as echarts from "echarts";
// import { JeecgListMixin } from "@/mixins/JeecgListMixin";
// // import BhzCementBaseModal from './modules/BhzCementBaseModal'
// import { filterMultiDictText } from "@/components/dict/JDictSelectUtil";
// import "@/assets/less/TableExpand.less";
import JSuperQuery from "@/components/jeecg/JSuperQuery.vue";
// import { handertoken } from "@/mixins/getHanderToken";
// import { SYS_DEPART_ORGCODE } from "@/store/mutation-types";
import Vue from "vue";
export default {
  name: "warningMessage",
  //mixins: [JeecgListMixin],
  // mixins: [JeecgListMixin, handertoken],
  components: {
    // BarAndMultidLine,
    // Pie,
    // LineChartMultid,
    // BhzCementBaseModal,
    JSuperQuery,
  },
  data() {
    return {
      approval_remarks: null,
      selectValue: "",
      description: "拌合站主表管理页面",
      asyncSelectValue: "",
      dictOption: [],
      datalist: [],
      dataSource: [],
      fields: [],
      aliases: [],
      dataSource1: [],
      dataSource2: [],
      url: {
        // list: "/hntbhz/bhzCementBase/list6",
        // list2: "/hntbhz/bhzCementBase/list7",
        // delete: "/hntbhz/bhzCementBase/delete",
        // deleteBatch: "/hntbhz/bhzCementBase/deleteBatch",
        // exportXlsUrl: "/hntbhz/bhzCementBase/exportXls",
        // importExcelUrl: "hntbhz/bhzCementBase/importExcel",

        list: "/hntbhz/bhzCementBase/list",
        // list: '/hntbhz/bhzCementBase/listrc',
        delete: "/hntbhz/bhzCementBase/delete",
        deleteBatch: "/hntbhz/bhzCementBase/deleteBatch",
        exportXlsUrl: "/hntbhz/bhzCementBase/exportXls",
        importExcelUrl: "hntbhz/bhzCementBase/importExcel",
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
          title: "告警类型",
          align: "center",
          dataIndex: "alarmTypeId",
          scopedSlots: { customRender: "warringT" },
        },

        {
          title: "机械类型Id",
          align: "center",
          dataIndex: "machineTypeId",
        },
        {
          title: "告警级别",
          align: "center",
          dataIndex: "alarmLevel",
          scopedSlots: { customRender: "alarmLevel" },
        },
        {
          title: "设备sn号",
          align: "center",
          dataIndex: "snCode",
        },
        {
          title: "机械名称",
          align: "center",
          dataIndex: "machineName",
        },
        {
          title: "告警时间",
          align: "center",
          dataIndex: "dataTime",
        },
        {
          title: "标准值",
          align: "center",
          dataIndex: "standard",
        },
        {
          title: "真实值",
          align: "center",
          dataIndex: "actual",
        },
        {
          title: "经度",
          align: "center",
          dataIndex: "lon",
        },

        {
          title: "纬度",
          align: "center",
          dataIndex: "lat",
        },
        {
          title: "桩号",
          align: "center",
          dataIndex: "roadStation",
        },
        {
          title: "左右幅",
          align: "center",
          dataIndex: "roadOffset",
        },
        {
          title: "地图定位",
          dataIndex: "action",
          align: "center",
          fixed: "right",
          width: 147,
          scopedSlots: { customRender: "action" },
        },
      ],
      superFieldList: [],
      charts: null,
      charts1: null,
      dailyCharts: null,
      selectedRowKeys: [],
      firstArr: [116.478935, 39.997761],
      pagination: {
        total: 0,
        pageSize: 10,//每页中显示10条数据
        showSizeChanger: true,
        pageSizeOptions: ["10", "20", "50", "100"],//每页中显示的数据
        showTotal: total => `共有 ${total} 条数据`,  //分页中显示总的数据
        current:1
      },
      loading: true,
      // 查询参数
      queryParam: {
        page: 1,//第几页
        size: 10,//每页中显示数据的条数
        hosName: "",
        hosCode: "",
        province: "",
        city: ""
      },
      form:{
        startTime:"2021-05-03 00:00:00",
        endTime:"2021-07-03 00:00:00",
      }
    };
  },
  created() {
    // this.bhzsta();
    // this.fanglaing();
    // this.hegelv();
    // this.getToken();
    // this.getSuperFieldList();
    // this.shebeiList();
  },
  mounted() {
    // this.getecharts();
    // this.getecharts1();
    // this.getdailyCharts();
    // this.getcharts();
    // this.getcharts1();
    // console.log();
    this.username();
    this.initMap();
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
        }
      });
    },
    getData() {
      let param = {
        projectId: "jishu03@20200511123137",
        sectionId: "jishu03@20200511123342",
        startTime: this.form.startTime,
        endTime: this.form.endTime,
        pageSize: this.pagination.pageSize,
        pageNo: this.pagination.current,
      };
      this.$http1.post(`/api/pavement/alarm.shtml`, param).then((res) => {
        //告警信息
        if (res.code == 0) {
          this.dataSource = res.data.records;
          const pagination = { ...this.pagination };
          pagination.total = res.data.rowCount;
          this.pagination = pagination;
          console.log(this.dataSource,res, "getData 告警信息------------------------");
        }
        this.loading = false;
      });
    },
    handleTableChange(pagination) {
      this.pagination.current = pagination.current;
      this.pagination.pageSize = pagination.pageSize;
      this.queryParam.page = pagination.current;
      this.queryParam.size = pagination.pageSize;
      this.getData();
    },
    onClearSelected() {},
    handlePostion(e) {
      console.log(e, "handlePostion----------------------");
      this.firstArr = [e.lon, e.lat];
      this.initMap();
    },
    initMap() {
      var that = this;
      var shebeiNo = null;
      var lineArr = [
        [116.478935, 39.997761],
        [108.983569, 34.285675],
        [103.85094, 35.987496],
        [106.205794, 38.458831],
        [111.761777, 40.875595],
      ];
      // if (this.selectedValue == "") {
      //   shebeiNo = null;
      // } else {
      //   shebeiNo = this.selectedValue;
      // }
      // console.log(shebeiNo);
      // let param = { shebeiNo: shebeiNo };
      // getAction(this.url.querylist, param).then((res) => {
      //   console.log(res, "最新数据");
      //   this.listDate = [];
      //   if (res.result.length > 0) {
      //     this.listDate = res.result;
      //     [this.listDate[0].longitude, this.listDate[0].latitude] = wgs84togcj02(
      //       this.listDate[0].longitude,
      //       this.listDate[0].latitude
      //     );
      //     that.center = [this.listDate[0].longitude, this.listDate[0].latitude];
      //     that.centers.lng = this.listDate[0].longitude;
      //     that.centers.lat = this.listDate[0].latitude;
      //     // console.log(
      //     //   that.center,
      //     //   that.centers.lng,
      //     //   that.centers.lat,
      //     //   "that.center------------------------1111"
      //     // );
      //   } else {
      //     this.$message.error("数据请求失败！");
      //   }
      // });
      // if (that.selectedValue != "") {
      //   let param = { shebeiNo: that.selectedValue, isdel: 0 };
      //   getAction(that.url.circlelistone, param).then((res) => {
      //     that.circle.center = [];
      //     that.circle.radius = 0;
      //     if (res.result.length > 0) {
      //       console.log(res, "围栏数据");
      //       // [res.result[0].lng, res.result[0].lat] = wgs84togcj02(
      //       //   res.result[0].lng,
      //       //   res.result[0].lat
      //       // );
      //       that.circle.center.push(res.result[0].lng, res.result[0].lat);
      //       console.log(that.circle.center, "围栏坐标");
      //       that.circle.radius = res.result[0].radius;
      //       that.circle.id = res.result[0].id;
      //       console.log(that.circle.radius, "围栏半径");
      //     }
      //   });
      // }
      setTimeout(function () {
        that.map = new AMap.Map("container", {
          resizeEnable: true, //窗口大小调整
          center: [116.478935, 39.997761], //中心 firstArr: ,
          // center: that.center, //中心 firstArr: [116.478935, 39.997761],
          zoom: 28,
          mapStyle: "amap://styles/normal",
        });
        // 绘制还未经过的路线
        that.polyline = new AMap.Polyline({
          map: that.map,
          path: lineArr,
          showDir: true,
          strokeColor: "#77DDFF", // 线颜色--浅蓝色
          // strokeOpacity: 1,     //线透明度
          strokeWeight: 6, // 线宽
          // strokeStyle: "solid"  //线样式
          lineJoin: "round", // 折线拐点的绘制样式
        });
        // let startIcon = new AMap.Icon({
        //   //图标尺寸
        //   size: new AMap.Size(70, 70),
        //   //图标的取图地址
        //   image: carIcon1,
        //   //图标所用图片大小
        //   imageSize: new AMap.Size(70, 70),
        //   //图标取图偏移量
        //   //imageOffset:new AMap.Pixel(-9,-3)
        // });
        // let startIcon1 = new AMap.Icon({
        //   //图标尺寸
        //   size: new AMap.Size(70, 70),
        //   //图标的取图地址
        //   image: carIcon2,
        //   //图标所用图片大小
        //   imageSize: new AMap.Size(70, 70),
        //   //图标取图偏移量
        //   //imageOffset:new AMap.Pixel(-9,-3)
        // });
        // if (that.circle.radius != 0) {
        //   console.log("进入围栏");
        //   var circle = new AMap.Circle({
        //     center: that.circle.center,
        //     radius: that.circle.radius, //半径
        //     borderWeight: 3,
        //     strokeColor: "#FF33FF",
        //     strokeOpacity: 1,
        //     strokeWeight: 6,
        //     fillOpacity: 0.4,
        //     strokeStyle: "dashed",
        //     strokeDasharray: [10, 10],
        //     // 线样式还支持 'dashed'
        //     fillColor: "#1791fc",
        //     zIndex: 50,
        //   });
        //   circle.setMap(that.map);
        //   var circleEditor = new AMap.CircleEditor(that.map, circle);
        //   that.circleEditor = circleEditor;
        //   circleEditor.on("move", function (event) {
        //     console.log(event);
        //     that.move.lng = null;
        //     that.move.lat = null;
        //     that.move.lng = event.lnglat.lng;
        //     that.move.lat = event.lnglat.lat;
        //     // [that.move.lng, that.move.lat] = wgs84togcj02(
        //     //   event.lnglat.lng,
        //     //   event.lnglat.lat
        //     // );
        //     // console.log([that.move.lng, that.move.lat]);
        //     // that.$message.success("触发事件：move");
        //   });
        //   circleEditor.on("adjust", function (event) {
        //     console.log(event);
        //     that.adjust.radius = 0;
        //     that.adjust.radius = event.radius;
        //     // that.$message.success("触发事件：adjust");
        //   });
        //   circleEditor.on("end", function (event) {
        //     // that.$message.success("触发事件：end");
        //     that.circleedit();
        //     // event.target 即为编辑后的圆形对象
        //   });
        // }
        //

        // AMap.plugin("AMap.MoveAnimation", function () {});
        // that.listDate.forEach(function (item, index) {
        //   //item 就是当日按循环到的对象
        //   //index是循环的索引，从0开始
        //   if (item.shebeistatus == 1) {
        //     that.marker = new AMap.Marker({
        //       map: that.map,
        //       position: [item.longitude, item.latitude],
        //       label: {
        //         content: item.shebeiname + "--在线",
        //         direction: "top",
        //       },
        //       icon: startIcon,
        //       offset: new AMap.Pixel(-25, -13),
        //       autoRotation: true,
        //     });
        //   } else {
        //     that.marker = new AMap.Marker({
        //       map: that.map,
        //       position: [item.longitude, item.latitude],
        //       label: {
        //         content: item.shebeiname + "--离线",
        //         direction: "top",
        //       },
        //       icon: startIcon1,
        //       offset: new AMap.Pixel(-25, -13),
        //       autoRotation: true,
        //     });
        //   }
        // });
        that.map.setFitView(); //合适的视口
      }, 500);
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
          // this.dictOption.push({ text: re.sbname, value: re.sbjno });
          // this.datalist.push({ text: re.sbname, value: re.sbjno });
        });
        // this.datalist = JSON.parse(JSON.stringify(this.dictOption))
        //console.log(res)
      });
    },
    chaxun: function () {
      this.getData();
      // console.log(this.form.startTime,'startTime--------------------');
      // this.cx = this.selectedValue;
      // this.dates = this.approval_remarks;
      // this.bhzsta();
      // this.fanglaing();
      // this.hegelv();
    },
    chongzhi: function () {
      this.form = {
        startTime:"2021-05-03 00:00:00",
        endTime:"2021-07-03 00:00:00",
      }
      // this.dictOption = [];
      // this.selectedValue = null;
      // this.approval_remarks = null;
      this.chaxun();
    },
    onSelectChange(){},
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
    getecharts() {
      let that = this;
      var colorList = [
        "#73DDFF",
        "#73ACFF",
        "#FDD56A",
        "#FDB36A",
        "#FD866A",
        "#9E87FF",
        "#58D5FF",
      ];
      var arr = [
        { value: 123, name: "丙烯腈" },
        { value: 102, name: "环氧乙烷" },
        { value: 122, name: "非甲烷总烃" },
        { value: 137, name: "氯乙烯" },
        { value: 109, name: "氯乙烷" },
        { value: 111, name: "甲苯" },
      ];
      // setInterval(function () {
      //   for (let index in arr) {
      //     arr[index].value = (Math.random() * 50 + 100).toFixed(0);
      //   }
      //   myChart.setOption({
      //     series: [
      //       {
      //         data: arr,
      //       },
      //     ],
      //   });
      // }, 1000);
      let option = {
        color: colorList,
        // title: {
        //   text: "钢轮速度",
        //   // subtext:'50%',
        //   x: "center",
        //   y: "center",
        //   textStyle: {
        //     // color: "#fff",
        //     fontSize: 15,
        //   },
        // },
        legend: {
          orient: "vertical",
          left: "left",
          right: 60,
          data: ["丙烯腈", "环氧乙烷"],
          // data: ["TJ01", "TJ02", "TJ03", "TJ04"],
          textStyle: {
            // color: "#fff",
            // fontSize: 10,
          },
        },
        tooltip: {
          trigger: "item",
        },
        series: [
          {
            type: "pie",
            center: ["50%", "50%"],
            radius: ["50%", "95%"],
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
              formatter: "{a|{b}：{d}%}\n{hr|}",
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
      let chart = echarts.init(document.getElementById("PavingTcharts"));
      chart.setOption(option);
      //点击传递当前标段
      chart.on("click", function (params) {
        that.$refs.LeftTwoTab.dialogVisible = true;
        // that.departList = that.bdList.filter(
        //   (item) => item.departname == params.seriesName
        // );
        that.departList = [];
        let id = that.data[that.tag].id;
        let departname = that.data[that.tag].departname;
        that.data[that.tag].data.forEach((e) => {
          // params.dataIndex
          if (params.dataIndex + 1 == e.month) {
            // console.log(e.month, "e--------------------");
            that.departList.push({
              id,
              departname,
              month2: e.month2,
              qiangdudengji: that.tag1,
            });
          }
        });
        console.log(that.departList, "departList------------------------");
        // console.log(params, that.tag1, "departList------------------------");
      });
      //建议加上以下这一行代码，不加的效果图如下（当浏览器窗口缩小的时候）。超过了div的界限（红色边框）
      window.addEventListener("resize", function () {
        chart.resize();
      });
    },
    getecharts1() {
      let that = this;
      var colorList = [
        "#73DDFF",
        "#73ACFF",
        "#FDD56A",
        "#FDB36A",
        "#FD866A",
        "#9E87FF",
        "#58D5FF",
      ];
      var arr = [
        { value: 123, name: "丙烯腈" },
        { value: 102, name: "环氧乙烷" },
        { value: 122, name: "非甲烷总烃" },
        { value: 137, name: "氯乙烯" },
        { value: 109, name: "氯乙烷" },
        { value: 111, name: "甲苯" },
      ];
      // setInterval(function () {
      //   for (let index in arr) {
      //     arr[index].value = (Math.random() * 50 + 100).toFixed(0);
      //   }
      //   myChart.setOption({
      //     series: [
      //       {
      //         data: arr,
      //       },
      //     ],
      //   });
      // }, 1000);
      let option = {
        color: colorList,
        // title: {
        //   text: "钢轮速度",
        //   // subtext:'50%',
        //   x: "center",
        //   y: "center",
        //   textStyle: {
        //     // color: "#fff",
        //     fontSize: 15,
        //   },
        // },
        legend: {
          orient: "vertical",
          left: "left",
          right: 60,
          data: ["丙烯腈", "环氧乙烷"],
          // data: ["TJ01", "TJ02", "TJ03", "TJ04"],
          textStyle: {
            // color: "#fff",
            // fontSize: 10,
          },
        },
        tooltip: {
          trigger: "item",
        },
        series: [
          {
            type: "pie",
            center: ["50%", "50%"],
            radius: ["50%", "95%"],
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
              formatter: "{a|{b}：{d}%}\n{hr|}",
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
      let chart = echarts.init(document.getElementById("PavingTcharts1"));
      chart.setOption(option);
      //点击传递当前标段
      chart.on("click", function (params) {
        that.$refs.LeftTwoTab.dialogVisible = true;
        // that.departList = that.bdList.filter(
        //   (item) => item.departname == params.seriesName
        // );
        that.departList = [];
        let id = that.data[that.tag].id;
        let departname = that.data[that.tag].departname;
        that.data[that.tag].data.forEach((e) => {
          // params.dataIndex
          if (params.dataIndex + 1 == e.month) {
            // console.log(e.month, "e--------------------");
            that.departList.push({
              id,
              departname,
              month2: e.month2,
              qiangdudengji: that.tag1,
            });
          }
        });
        console.log(that.departList, "departList------------------------");
        // console.log(params, that.tag1, "departList------------------------");
      });
      //建议加上以下这一行代码，不加的效果图如下（当浏览器窗口缩小的时候）。超过了div的界限（红色边框）
      window.addEventListener("resize", function () {
        chart.resize();
      });
    },
    getcharts() {
      //对数据进行处理
      // let data = this.qdbzc;
      // let { xAxis, seriesData } = this.processData(data);
      // console.log(xAxis, seriesData, 'xAxis1, seriesData1xAxis1, seriesData1xAxis1, seriesData1xAxis1, seriesData1');
      const option = {
        legend: {
          type: "scroll",
          // data: name,
          data: ["右幅"],
          right: "5%",
          // pageIconColor: "white",
          pageIconInactiveColor: "#2f4554",
          textStyle: {
            // color: "#fff",
          },
        },
        tooltip: {
          trigger: "axis",
          axisPointer: {
            // type: "cross",
            label: {
              backgroundColor: "#6a7985",
            },
          },
        },
        dataZoom: [
          {
            show: true,
            height: 15,
            // xAxisIndex: [0],
            bottom: 10,
            start: 10,
            end: 80,
            handleIcon:
              "path://M306.1,413c0,2.2-1.8,4-4,4h-59.8c-2.2,0-4-1.8-4-4V200.8c0-2.2,1.8-4,4-4h59.8c2.2,0,4,1.8,4,4V413z",
            handleSize: "100%",
            handleStyle: {
              // color: "#5B3AAE",
            },
            textStyle: {
              // color: "rgba(204,187,225,0.5)",
            },
            // fillerColor: "rgba(67,55,160,0.4)",
            // borderColor: "rgba(204,187,225,0.5)",
          },
          {
            type: "inside",
            show: true,
            height: 25,
            start: 1,
            end: 35,
          },
        ],
        // color: ["#3ABADF", "#2DDCD7", "#F28E2A", "#005DFF"],
        yAxis: [
          {
            type: "value",
            // interval: 5,
            axisLabel: {
              formatter: "{value}",
              textStyle: {
                color: "#CFCFCF",
              },
            },
            axisLine: {
              lineStyle: {
                color: "#CFCFCF",
              },
            },
            splitLine: {
              // show: false,
            },
          },
        ],
        grid: {
          top: "12%",
          left: "3%",
          right: "7%",
          bottom: "15%",
          containLabel: true,
        },
        xAxis: [
          {
            data: [1, 2, 3, 4, 5, 6, 7, 8, 9],
            type: "category",
            boundaryGap: false,
            axisLabel: {
              // show: false, // 不显示坐标轴上的文字
            },
            axisTick: {
              // show: false,
              lineStyle: {
                color: "#CFCFCF",
              },
            },
            axisLine: {
              // show: false,
              lineStyle: {
                color: "#CFCFCF",
              },
            },
            splitLine: {
              // show: false,
            },
          },
        ],
        // series: seriesData,
        series: [
          {
            name: "右幅",
            smooth: true,
            type: "line",
            markLine: {
              silent: true,
              data: [
                {
                  yAxis: 5,
                },
              ],
            },
            data: [5, 5, 6, 2, 6, 7, 2, 7, 2, 5],
          },
        ],
      };
      if (this.charts) {
        this.charts.dispose();
      }
      this.charts = echarts.init(document.getElementById("Tempcharts"));
      this.charts.setOption(option);
    },
    getcharts1() {
      //对数据进行处理
      // let data = this.qdbzc;
      // let { xAxis, seriesData } = this.processData(data);
      // console.log(xAxis, seriesData, 'xAxis1, seriesData1xAxis1, seriesData1xAxis1, seriesData1xAxis1, seriesData1');
      const option = {
        legend: {
          type: "scroll",
          // data: name,
          data: ["右幅"],
          right: "5%",
          // pageIconColor: "white",
          pageIconInactiveColor: "#2f4554",
          textStyle: {
            // color: "#fff",
          },
        },
        tooltip: {
          trigger: "axis",
          axisPointer: {
            // type: "cross",
            label: {
              backgroundColor: "#6a7985",
            },
          },
        },
        dataZoom: [
          {
            show: true,
            height: 15,
            // xAxisIndex: [0],
            bottom: 10,
            start: 10,
            end: 80,
            handleIcon:
              "path://M306.1,413c0,2.2-1.8,4-4,4h-59.8c-2.2,0-4-1.8-4-4V200.8c0-2.2,1.8-4,4-4h59.8c2.2,0,4,1.8,4,4V413z",
            handleSize: "110%",
            handleStyle: {
              // color: "#5B3AAE",
            },
            textStyle: {
              // color: "rgba(204,187,225,0.5)",
            },
            // fillerColor: "rgba(67,55,160,0.4)",
            // borderColor: "rgba(204,187,225,0.5)",
          },
          {
            type: "inside",
            show: true,
            height: 25,
            start: 1,
            end: 35,
          },
        ],
        // color: ["#3ABADF", "#2DDCD7", "#F28E2A", "#005DFF"],
        yAxis: [
          {
            type: "value",
            // interval: 5,
            axisLabel: {
              formatter: "{value}",
              textStyle: {
                color: "#CFCFCF",
              },
            },
            axisLine: {
              lineStyle: {
                color: "#CFCFCF",
              },
            },
            splitLine: {
              // show: false,
            },
          },
        ],
        grid: {
          top: "12%",
          left: "3%",
          right: "7%",
          bottom: "15%",
          containLabel: true,
        },
        xAxis: [
          {
            data: [1, 2, 3, 4, 5, 6, 7, 8, 9],
            type: "category",
            boundaryGap: false,
            axisLabel: {
              // show: false, // 不显示坐标轴上的文字
            },
            axisTick: {
              // show: false,
              lineStyle: {
                color: "#CFCFCF",
              },
            },
            axisLine: {
              // show: false,
              lineStyle: {
                color: "#CFCFCF",
              },
            },
            splitLine: {
              // show: false,
            },
          },
        ],
        // series: seriesData,
        series: [
          {
            name: "右幅",
            smooth: true,
            type: "line",
            markLine: {
              silent: true,
              data: [
                {
                  yAxis: 5,
                },
              ],
            },
            data: [5, 5, 6, 2, 6, 7, 2, 7, 2, 5],
          },
        ],
      };
      if (this.charts1) {
        this.charts1.dispose();
      }
      this.charts1 = echarts.init(document.getElementById("Tempcharts1"));
      this.charts1.setOption(option);
    },
    getdailyCharts() {
      //对数据进行处理
      // let data = this.qdbzc;
      // let { xAxis, seriesData } = this.processData(data);
      // console.log(xAxis, seriesData, 'xAxis1, seriesData1xAxis1, seriesData1xAxis1, seriesData1xAxis1, seriesData1');
      var xData = (function () {
        var data = [];
        for (var i = 1; i < 13; i++) {
          data.push(i + "月份");
        }
        return data;
      })();

      const option = {
        // backgroundColor: "#344b58",
        title: {
          text: "合格率",
          // "subtext": "BY Wang Dingding",
          x: "8%",
          top: "10%",
          textStyle: {
            // color: '#fff',
            fontSize: "16",
          },
          subtextStyle: {
            color: "#90979c",
            fontSize: "16",
          },
        },
        tooltip: {
          trigger: "axis",
          axisPointer: {
            type: "shadow",
            textStyle: {
              color: "#fff",
            },
          },
        },
        grid: {
          borderWidth: 0,
          top: 110,
          bottom: 95,
          textStyle: {
            color: "#fff",
          },
        },
        legend: {
          // x: '4%',
          // top: '8%',
          textStyle: {
            color: "#90979c",
          },
          data: ["钢轮", "胶轮"],
        },

        calculable: true,
        xAxis: [
          {
            type: "category",
            axisLine: {
              lineStyle: {
                color: "#90979c",
              },
            },
            // "splitLine": {
            //     "show": false
            // },
            // "axisTick": {
            //     "show": false
            // },
            // "splitArea": {
            //     "show": false
            // },
            axisLabel: {
              interval: 0,
            },
            data: xData,
          },
        ],
        yAxis: [
          {
            type: "value",
            // "splitLine": {
            //     "show": false
            // },
            axisLine: {
              // lineStyle: {
              //     color: '#90979c'
              // }
            },
            // "axisTick": {
            //     "show": false
            // },
            // "axisLabel": {
            //     "interval": 0,

            // },
            // "splitArea": {
            //     "show": false
            // },
          },
        ],
        dataZoom: [
          {
            show: true,
            height: 30,
            xAxisIndex: [0],
            bottom: 30,
            start: 10,
            end: 80,
            handleIcon:
              "path://M306.1,413c0,2.2-1.8,4-4,4h-59.8c-2.2,0-4-1.8-4-4V200.8c0-2.2,1.8-4,4-4h59.8c2.2,0,4,1.8,4,4V413z",
            handleSize: "110%",
            handleStyle: {
              color: "#d3dee5",
            },
            textStyle: {
              color: "#fff",
            },
            borderColor: "#90979c",
          },
          {
            type: "inside",
            show: true,
            height: 15,
            start: 1,
            end: 35,
          },
        ],
        series: [
          {
            name: "钢轮",
            type: "bar",
            barMaxWidth: 35,
            itemStyle: {
              normal: {
                color: "rgba(255,144,128,1)",
                label: {
                  show: true,
                  textStyle: {
                    color: "#fff",
                  },
                  position: "inside",
                  formatter: function (p) {
                    return p.value > 0 ? p.value : "";
                  },
                },
              },
            },
            data: [709, 1917, 2455, 2610, 1719, 1433, 1544, 3285, 5208, 3372, 2484, 4078],
          },

          {
            name: "胶轮",
            type: "bar",
            barMaxWidth: 35,
            itemStyle: {
              normal: {
                color: "rgba(0,191,183,1)",
                barBorderRadius: 0,
                label: {
                  show: true,
                  position: "inside",
                  formatter: function (p) {
                    return p.value > 0 ? p.value : "";
                  },
                },
              },
            },
            data: [327, 1776, 507, 1200, 800, 482, 204, 1390, 1001, 951, 381, 220],
          },
        ],
      };
      if (this.dailyCharts) {
        this.dailyCharts.dispose();
      }
      this.dailyCharts = echarts.init(document.getElementById("dailyCount"));
      this.dailyCharts.setOption(option);
    },
  },
};
</script>
<style scope>
@import "~@assets/less/common.less";
.charts-box {
  display: flex;
}
#PavingTcharts {
  width: 100%;
  height: 200px;
}
#PavingTcharts1 {
  width: 100%;
  height: 200px;
}
#dailyCount {
  width: 100%;
  height: 400px;
}
#Tempcharts {
  width: 100%;
  height: 200px;
}
#Tempcharts1 {
  width: 100%;
  height: 200px;
}
</style>
