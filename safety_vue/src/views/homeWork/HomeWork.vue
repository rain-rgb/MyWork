<template>
  <div class="wrapper">
    <div class="gutter-example">
      <a-row :gutter="16" class="rowPadding">
        <a-col class="gutter-row" :span="6">
          <a-card size="small" style="width: 400px">
            <a-row>
              <a-col :span="8" style="width: 100px">
                <img :src="imageUrl" style="width: 32px; height: 38px" alt="" />
              </a-col>
              <a-row>
                <a-col :span="8">
                  当前用户:<span style="font-weight: 600; color: #000">贾大福</span>
                </a-col>
                <a-col :span="8"> 职位:<span>安质部长</span> </a-col>
                <a-col :span="9">
                  所属机构:<span style="font-weight: 600; color: #000">XX高速1标</span>
                </a-col>
              </a-row>
            </a-row>
          </a-card>
        </a-col>
        <a-col
          class="gutter-row"
          :span="3"
          v-for="(item, key) in itemLable"
          :key="key"
          :rowScow="wrapperCol"
        >
          <div class="gutter-box">
            <a-card size="small" style="height: 67px">
              <img :src="item.img" style="height: 15px" alt="" />
              <span
                :class="[
                  key == 4 ? 'warning' : 'normal',
                  key == 5 ? 'residual' : 'normal',
                ]"
                style="font-weight: 600; font-size: 17px; margin-left: 5px"
                >{{ item.lable }}:</span
              >
              <span
                :class="[
                  key == 4 ? 'warning' : 'normal',
                  key == 5 ? 'residual' : 'normal',
                ]"
                style="margin-left: 10px; font-weight: 600; font-size: 16px"
                >{{ item.value }}</span
              >
            </a-card>
          </div>
        </a-col>
      </a-row>
    </div>
    <div>
      <a-row :gutter="16">
        <a-col :span="9" class="rowPadding">
          <div>
            <a-card title="我的待办事项" style="width: 100%">
              <a slot="extra" href="#">
                <a-radio-group default-value="a" button-style="solid">
                  <a-radio-button value="a"> 全部 </a-radio-button>
                  <a-radio-button value="b"> 风险 </a-radio-button>
                  <a-radio-button value="c"> 隐患 </a-radio-button>
                </a-radio-group>
              </a>
              <a-table
                :columns="col"
                :data-source="data"
                bordered
                :customRow="handleClick"
                :rowClassName="setRowClassName"
              >
                <span slot="status" slot-scope="status, scope">
                  <span
                    v-has="'program:add'"
                    @click="handleAdd"
                    type="primary"
                    icon="plus"
                  >
                    <a-tag @click="openWindow(scope)" color="#e59918">
                      {{ progect(status) }}
                    </a-tag>
                  </span>
                </span>
                <span slot="busy" slot-scope="busy">
                  <a-tag v-if="busy === 0" color="blue" @click="handleAdd">
                    整改中
                  </a-tag>
                </span>
              </a-table>
            </a-card>
          </div>
        </a-col>
        <a-col :span="15" class="rowPadding">
          <div>
            <a-card title="项目安全管理进度">
              <a slot="extra" href="#"></a>
              <a-table
                :columns="columns"
                :data-source="data"
                bordered
                :customRow="handleClick"
                :rowClassName="setRowClassName"
              >
                <span slot="state" slot-scope="state">
                  <a-tag v-if="state === 0" color="volcano"> 整改中 </a-tag>
                </span>
              </a-table>
            </a-card>
          </div>
        </a-col>
      </a-row>
    </div>
    <a-row :gutter="16">
      <a-col :span="9">
        <a-card title="我的统计分析">
          <a slot="extra" href="#"> </a>
          <!-- <a-table  :data-source="data" bordered> -->
          <a-card :bordered="false" :body-style="{ padding: '0' }">
            <div class="salesCard" style="height: 360px">
              <!-- <a-tabs
                    default-active-key="1"
                    size="large"
                    :tab-bar-style="{ marginBottom: '24px', paddingLeft: '16px' }"
                  > -->
              <!-- <a-tab-pane loading="true" tab="销售额" key="1"> -->

              <bar :dataSource="barData" />

              <!-- </a-tab-pane> -->
              <!-- </a-tabs> -->
            </div>
          </a-card>
          <!-- </a-table> -->
        </a-card>
      </a-col>
      <a-col :span="8">
        <a-card title="风险提醒">
          <a slot="extra" href="#">
            <a-radio-group default-value="a" button-style="solid">
              <a-radio-button value="a"> 全部 </a-radio-button>
              <a-radio-button value="b"> 风险 </a-radio-button>
              <a-radio-button value="c"> 隐患 </a-radio-button>
            </a-radio-group>
          </a>
          <a-table
            :columns="list"
            :data-source="data"
            bordered
            :customRow="handleClick"
            :rowClassName="setRowClassName"
          >
            <span slot="state" slot-scope="state">
              <a-tag v-if="state === 0" color="volcano"> 整改中 </a-tag>
            </span>
          </a-table>
        </a-card>
      </a-col>
      <a-col :span="7">
        <a-card title="残留风险">
          <a slot="extra" href="#">
            <a-radio-group default-value="a" button-style="solid">
              <a-radio-button value="a"> 全部 </a-radio-button>
              <a-radio-button value="b"> 风险 </a-radio-button>
              <a-radio-button value="c"> 隐患 </a-radio-button>
            </a-radio-group>
          </a>
          <a-table
            :columns="column"
            :data-source="data"
            bordered
            :customRow="handleClick"
            :rowClassName="setRowClassName"
          >
          </a-table>
        </a-card>
      </a-col>
    </a-row>
    <programme-modal ref="programmeModal" @ok="modalFormOk"></programme-modal>
  </div>
</template>
<script>
import { JeecgListMixin } from "@/mixins/JeecgListMixin";
import ProgrammeModal from "./components/ProgrammeModal";
import RankList from "@/components/chart/RankList";
import Bar from "@/components/chart/Bar";
const rankList = [];
for (let i = 0; i < 7; i++) {
  rankList.push({
    name: "白鹭岛 " + (i + 1) + " 号店",
    total: 1234.56 - i * 100,
  });
}
const barData = [
  {
    x: "巡检总数",
    y: 100,
  },
  {
    x: "巡检数",
    y: 120,
  },
  {
    x: "通过数",
    y: 140,
  },
  {
    x: "整改数",
    y: 160,
  },
  {
    x: "整改完成数",
    y: 180,
  },
];
const columns = [
  {
    title: "#",
    key: "rowIndex",
    width: 50,
    dataIndex: "",
    align: "center",
    customRender: (t, r, i) => parseInt(i) + 1,
  },
  {
    title: "所属机构",
    dataIndex: "Belonging ",
    width: 90,
    key: "Belonging ",
  },
  {
    title: "部门",
    width: 90,
    dataIndex: "department",
    key: "department",
  },
  {
    title: "巡检位置",
    width: 90,
    dataIndex: "position",
    key: "position",
  },
  {
    title: "内容",
    width: 90,
    dataIndex: "article",
    key: "article",
    ellipsis: true,
  },
  {
    title: "控制措施",
    width: 90,
    dataIndex: "measures",
    key: "measures",
  },
  {
    title: "类型",
    width: 90,
    dataIndex: "type",
    key: "type",
  },
  {
    title: "状态",
    width: 90,
    dataIndex: "state",
    scopedSlots: { customRender: "state" },
  },
];
const col = [
  {
    title: "#",
    key: "rowIndex",
    dataIndex: "",
    width: 50,
    align: "center",
    customRender: (t, r, i) => parseInt(i) + 1,
  },
  {
    title: "巡检位置",
    dataIndex: "Belonging ",
    width: 90,
    key: "Belonging ",
  },
  {
    title: "目前状态",
    dataIndex: "status",
    width: 90,
    key: "status",
    scopedSlots: { customRender: "status" },
  },
  {
    title: "隐患内容",
    dataIndex: "article",
    width: 90,
    key: "article",
    ellipsis: true,
  },
  {
    title: "处置措施",
    dataIndex: "measures",
    width: 90,
    key: "measures",
  },
  {
    title: "类型",
    dataIndex: "type",
    width: 90,
    key: "type",
  },
  {
    title: "操作",
    dataIndex: "busy",
    width: 90,
    scopedSlots: { customRender: "busy" },
  },
];
const list = [
  {
    title: "#",
    key: "rowIndex",
    width: 50,
    dataIndex: "",
    align: "center",
    customRender: (t, r, i) => parseInt(i) + 1,
  },
  {
    title: "所属机构",
    dataIndex: "Belonging ",
    width: 90,
    key: "Belonging ",
    // ellipsis: true,
  },
  {
    title: "部门",
    dataIndex: "department",
    width: 90,
    key: "department",
  },
  {
    title: "发生位置",
    dataIndex: "position",
    width: 90,
    key: "position",
  },
  {
    title: "风险内容",
    dataIndex: "article",
    width: 90,
    key: "article",
    ellipsis: true,
  },
  {
    title: "风险等级",
    dataIndex: "measures",
    width: 90,
    key: "measures",
  },
];
const column = [
  {
    title: "#",
    key: "rowIndex",
    dataIndex: "",
    align: "center",
    width: 50,
    customRender: (t, r, i) => parseInt(i) + 1,
  },
  {
    title: "残留风险",
    width: 400,
    dataIndex: "section",
    key: "section ",
    ellipsis: true,
  },
];
const data = [];
for (let i = 0; i < 5; i++) {
  data.push({
    number: i.toString(),
    Belonging: `Edrward ${i}`,
    department: 32,
    address: `London Park no. ${i}`,
    state: 0,
    status: 0,
    busy: 0,
    article: "今年将承担法定率是积分不是分局经",
    section: "今年将承担",
  });
}
export default {
  name: "homeWork",
  components: { RankList, Bar, ProgrammeModal },
  data() {
    this.cacheData = data.map((item) => ({ ...item }));
    return {
      loading: true,
      center: null,
      rankList,
      barData,
      data,
      col,
      list,
      column,
      columns,
      editingKey: "",
      wrapperCol: {
        xs: { span: 24 },
        sm: { span: 16 },
      },
      itemLable: [
        {
          img: require("@/assets/img/waring.png"),
          lable: "风险待落实",
          value: 5,
        },
        {
          img: require("@/assets/img/yinhuan.png"),
          lable: "已发现隐患数",
          value: 6,
        },
        {
          img: require("@/assets/img/zhenggai.png"),
          lable: "隐患待整改数",
          value: 5,
        },
        {
          img: require("@/assets/img/fuhe.png"),
          lable: "隐患待复核数",
          value: 5,
        },
        {
          img: require("@/assets/img/redian.png"),
          lable: "预警提醒",
          value: 5,
        },
        {
          img: require("@/assets/img/redian.png"),
          lable: "残留风险",
          value: 5,
        },
      ],
      imageUrl: require("@/assets/img/user.png"),
    };
  },
  created() {},
  methods: {
    handleAdd: function () {
      this.$refs.programmeModal.add();
      this.$refs.programmeModal.title = "新增";
      this.$refs.programmeModal.disableSubmit = false;
    },
    openWindow(e) {
      console.log(e);
    },
    progect(status) {
      if (status === 0) {
        return "整改中";
      } else {
        return "处理";
      }
    },
    // handleChange(value, key, column) {
    //   const newData = [...this.data]
    //   const target = newData.filter((item) => key === item.key)[0]
    //   if (target) {
    //     target[column] = value
    //     this.data = newData
    //   }
    // },
    //   const newData = [...this.data]
    //   const target = newData.filter((item) => key === item.key)[0]
    //   this.editingKey = key
    //   if (target) {
    //     target.editable = true
    //     this.data = newData
    //   }
    // },
    // save(key) {
    //   const newData = [...this.data]
    //   const newCacheData = [...this.cacheData]
    //   const target = newData.filter((item) => key === item.key)[0]
    //   const targetCache = newCacheData.filter((item) => key === item.key)[0]
    //   if (target && targetCache) {
    //     delete target.editable
    //     this.data = newData
    //     Object.assign(targetCache, target)
    //     this.cacheData = newCacheData
    //   }
    //   this.editingKey = ''
    // },
    // cancel(key) {
    //   const newData = [...this.data]
    //   const target = newData.filter((item) => key === item.key)[0]
    //   this.editingKey = ''
    //   if (target) {
    //     Object.assign(target, this.cacheData.filter((item) => key === item.key)[0])
    //     delete target.editable
    //     this.data = newData
    //   }
    // },
  },
};
</script>
<style scoped>
.gutter-example >>> .ant-row > div {
  background: transparent;
  border: 0;
}
/* .gutter-box {
    padding: 10px 0;
  } */
.rowPadding {
  padding: 5px;
}
.normal {
  color: #000;
}
.warning {
  color: #f00707;
}
.residual {
  color: #d8d8d8;
}
.ant-radio-button-wrapper {
  background: #e8eeee;
}
/* .ant-radio-button-wrapper.ant-radio-button-wrapper-checked {
  background: #009688;
} */
>>> .ant-card-head-title {
  color: #000;
  font-weight: 700;
}
>>> .ant-card-extra {
  float: right;
  margin-left: auto;
  padding: 0 0;
  color: rgba(0, 0, 0, 0.65);
  font-weight: normal;
  font-size: 14px;
}
.editable-row-operations a {
  margin-right: 8px;
}
>>> .ant-table-thead > tr > th {
  text-align: center;
}
>>> .ant-table-tbody .ant-table-row td {
  text-align: center;
}
</style>
