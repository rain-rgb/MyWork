<template>
  <!-- 砼超标统计-->
  <a-card :bordered="false">
    <div class="table-page-search-wrapper">
      <a-form layout="inline">
        <a-row :gutter="24">
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="设备名称">
              <j-search-select-tag
                placeholder="请选择设备名称"
                v-model="dictOption.text"
                :dictOptions="dictOption"
                @change="handleChange(dictOption.text)"
              >
              </j-search-select-tag>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="出料时间范围">
              <j-date
                placeholder="开始出料时间"
                v-model="queryParam.statime"
                :showTime="true"
                dateFormat="YYYY-MM-DD HH:mm:ss"
              />
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="">
              <j-date
                placeholder="结束出料时间"
                v-model="queryParam.endtime"
                :showTime="true"
                dateFormat="YYYY-MM-DD HH:mm:ss"
              />
            </a-form-item>
          </a-col>
          <!--          <a-col :xl="6" :lg="7" :md="8" :sm="24">-->
          <!--            <a-form-item label="分组类型">-->
          <!--              <a-radio-group v-model="value" @change="onChange" v-for="(k, index) in radioData" :key="index">-->
          <!--                <a-radio :style="radioStyle" :value="k.value">{{ k.name }}</a-radio>-->
          <!--              </a-radio-group>-->
          <!--            </a-form-item>-->
          <!--          </a-col>-->
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <span
              style="float: left; overflow: hidden"
              class="table-page-search-submitButtons"
            >
              <a-button type="primary" @click="chaxun" icon="search">查询</a-button>
              <!--              <a-button  @click="searchReset" icon="reload" style="margin-left: 8px">重置</a-button>-->
            </span>
          </a-col>
        </a-row>
      </a-form>
      <div>
        <a-table
          :dataSource="dataSource2"
          size="default"
          rowKey="key"
          :columns="columns1"
          :customRow="handleClick"
          :rowClassName="setRowClassName"
        >
          <span slot="chaobiaodengji" slot-scope="chaobiaodengji, record">
            <a-tag color="orange" v-if="record.chaobiaodengji === 1">初级超标</a-tag>
            <a-tag color="yellow" v-if="record.chaobiaodengji === 2">中级超标</a-tag>
            <a-tag color="red" v-if="record.chaobiaodengji === 3">高级超标</a-tag>
          </span>
        </a-table>
      </div>
      <a-row>
        <div style="margintop: 20px">
          <a-row :gutter="24">
            <a-col :xl="8" :lg="12" :md="12" :sm="24" :xs="24">
              <indexhntPie title="超标" :height="height" :dataSource="pieData" />
            </a-col>
            <a-col :xl="8" :lg="12" :md="12" :sm="24" :xs="24">
              <indexhntPie title="处置" :height="height" :dataSource="pieData1" />
            </a-col>
            <a-col :xl="8" :lg="12" :md="12" :sm="24" :xs="24">
              <indexhntPie title="审核" :height="height" :dataSource="pieData2" />
            </a-col>
          </a-row>
        </div>
      </a-row>
    </div>
  </a-card>
</template>
<script>
import BarMultid from "@comp/chart/BhzCementSta";
import Pie from "@comp/chart/BhzStaPie";
import LineChartMultid from "@comp/chart/BhzStafangliang";
import indexhntPie from "@comp/chart/indexhntPie";
import { getAction } from "@api/manage";
import JSuperQuery from "@comp/jeecg/JSuperQuery";
import { JeecgListMixin } from "@/mixins/JeecgListMixin";
import { SYS_DEPART_ORGCODE } from "@/store/mutation-types";
import { usershebeiList } from "@api/api";
import Vue from "vue";

export default {
  name: "BhzConcreteExceedanceStatistics",
  // mixins: [JeecgListMixin],
  components: {
    BarMultid,
    Pie,
    LineChartMultid,
    indexhntPie,
    JSuperQuery,
  },
  data() {
    return {
      queryParam: {
        statime: null,
        endtime: null,
      },
      height: 340,
      columns1: [
        {
          title: "超标等级",
          align: "center",
          dataIndex: "chaobiaodengji",
          scopedSlots: { customRender: "chaobiaodengji" },
        },
        {
          title: "超标数(盘)",
          align: "center",
          dataIndex: "chaobiaopanshu",
        },
        {
          title: "超标率(%)",
          align: "center",
          dataIndex: "chaobiaolv",
        },
        {
          title: "处置数(盘)",
          align: "center",
          dataIndex: "chuzhipanshu",
        },
        {
          title: "处置率(%)",
          align: "center",
          dataIndex: "chuzhilv",
        },
        {
          title: "闭合数(盘)",
          align: "center",
          dataIndex: "bihepanshu",
        },
        {
          title: "闭合率(%)",
          align: "center",
          dataIndex: "bihelv",
        },
      ],
      pieData: [],
      pieData1: [],
      pieData2: [],
      approval_remarks: null,
      selectValue: "",
      asyncSelectValue: "",
      dictOption: [],
      datalist: [],
      dataSource: [],
      fields: [],
      aliases: [],
      dataSource1: [],
      dataSource2: [],
      url: {
        list: "/hntbhz/bhzCementBase/listyubh",
      },
      dictOptions: {},
      value: 1,
      radioStyle: {
        height: "30px",
        lineHeight: "30px",
      },
      // radioData: [
      //   { name: '全局', value: 1, },
      //   { name: '下属部门', value: 2, },
      //   { name: '拌和机', value: 3, },
      // ]
    };
  },
  created() {
    this.shebeiList();
    this.getDatas(); //超标详情
  },
  methods: {
    getDatas: function () {
      this.dataSource2 = [];
      let params = {
        productDatetime_begin: this.queryParam.statime,
        productDatetime_end: this.queryParam.statime,
        shebeiNo: this.selectValue,
      };
      debugger;
      getAction(this.url.list, params).then((res) => {
        let result = res.result;
        if (res.success) {
          this.dataSource2.push(
            {
              key: 1,
              chaobiaodengji: 1,
              chaobiaopanshu: result.cjchao,
              chaobiaolv: result.cjchaolv,
              bihepanshu: result.cjbh,
              bihelv: result.cjbihelv,
              chuzhipanshu: result.cjcz,
              chuzhilv: result.cjczlv,
            },
            {
              key: 2,
              chaobiaodengji: 2,
              chaobiaopanshu: result.zjchao,
              chaobiaolv: result.zjchaolv,
              bihepanshu: result.zjbh,
              bihelv: result.zjbihelv,
              chuzhipanshu: result.zjcz,
              chuzhilv: result.zjczlv,
            },
            {
              key: 3,
              chaobiaodengji: 3,
              chaobiaopanshu: result.gjchao,
              chaobiaolv: result.gjchaolv,
              bihepanshu: result.gjbh,
              bihelv: result.gjbihelv,
              chuzhipanshu: result.gjcz,
              chuzhilv: result.gjczlv,
            }
          );

          this.pieData.push(
            { item: "初级超标", count: result.cjchao },
            { item: "中级超标", count: result.zjchao },
            { item: "高级超标", count: result.gjchao }
          );
          this.pieData1.push(
            { item: "初级处置", count: result.cjcz },
            { item: "中级处置", count: result.zjcz },
            { item: "高级处置", count: result.gjcz }
          );
          this.pieData2.push(
            { item: "初级审核", count: result.cjbh },
            { item: "中级审核", count: result.zjbh },
            { item: "高级审核", count: result.gjbh }
          );
        }
      });
    },
    // onChange(e) {
    //   console.log('radio checked', e.target.value)
    // },
    handleChange(selectedValue) {
      // this.dictOption = this.datalist
      console.log("selectedValue", selectedValue);
      this.selectValue = selectedValue;
      // this.callback()
    },
    chaxun: function () {
      (this.pieData = []), (this.pieData1 = []), (this.pieData2 = []), this.getDatas();
    },
    searchReset: function () {
      this.dictOption.text = "";
      this.queryParam.endtime = null;
      this.queryParam.statime = null;
      this.chaxun();
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
          this.datalist.push({ text: re.sbname, value: re.sbjno });
        });
        // this.datalist = JSON.parse(JSON.stringify(this.dictOption))
        //console.log(res)
      });
    },
  },
};
</script>

<style scoped></style>
