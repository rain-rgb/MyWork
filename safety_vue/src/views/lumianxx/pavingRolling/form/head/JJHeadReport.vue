<template>
  <div class="head">
    <!--项目名-->
    <div class="projectName">
      {{ formData.projectid_dictText + formData.sectionid_dictText }}
    </div>
    <!--表名-->
    <div class="tablename">
      <span class="tiTableName">{{ `施工过程记录表` }}</span>
      <!-- <span class="tiTableName">{{formData. }}</span> -->
      <!-- <div class="tiCustomTableNum">
        <span v-for="(item, i) in tiCustomTableNum" :key="i">{{ item }}</span>
      </div>
      <span class="page" style="display: none">第1页 共1页</span> -->
    </div>
    <!--表格内容-->
    <div class="tablecontent">
      <table class="table-noborder">
        <tr>
          <td style="width: 80px">施工单位：</td>
          <td style="height: 20px" v-if="dataTar">{{ dataTar.blockid }}</td>
          <td style="width: 230px; white-space: nowrap">
            &emsp;&emsp;&emsp;合同号：
            <span class="tableNumber" v-if="dataTar">{{ dataTar.hetong }}</span>
          </td>
        </tr>
        <tr>
          <td style="width: 80px">监理单位：</td>
          <td style="height: 20px" v-if="dataTar">{{ dataTar.sectionid }}</td>
          <td style="width: 230px; white-space: nowrap">
            &emsp;&emsp;&emsp;编&emsp;号：
            <span class="tableNumber">{{ `` }}</span>
          </td>
        </tr>
      </table>
    </div>
  </div>
</template>
<script>
export default {
  name: "JJHeadReport",
  props: ["formData","dataTar"],
  components: {},
  data() {
    return {};
  },
  mounted() {
    // console.log(this.formData,this,reportData,`reportData1`);
  },
  computed: {
    reportData: {
      get: function () {
        return this.formData ? this.formData : {};
      },
      set: function (newVal) {
        this.$emit("update:formData", newVal);
        return newVal;
      },
    },
    tiCustomTableNum() {
      return this.reportData.tiCustomTableNum
        ? this.reportData.tiCustomTableNum.split("/")
        : [];
    },
  },
  methods: {
    timestampToTime(timestamp) {
      var date = new Date(timestamp);
      var y = date.getFullYear();
      var m = date.getMonth() + 1;
      m = m < 10 ? "0" + m : m;
      var d = date.getDate();
      d = d < 10 ? "0" + d : d;
      let time = y + "年" + m + "月" + d + "日";
      return time;
    },
    onChange(time) {
      this.timestampToTime(time[0]) == this.timestampToTime(time[1])
        ? (this.reportData.shiyanriqi = this.timestampToTime(time[0]))
        : (this.reportData.shiyanriqi =
            this.timestampToTime(time[0]) + " 至 " + this.timestampToTime(time[1]));
    },
  },
};
</script>
<style lang="less" scoped>
.head {
  .projectName {
    text-align: center;
    font-size: 22px;
    font-weight: bold;
  }
  .tablename {
    padding: 0 0 10px 0;
    text-align: center;
    position: relative;
    font-size: 19px;
    .tiTableName {
      font-weight: bold;
    }
    .tiCustomTableNum {
      position: absolute;
      font-size: 12px;
      right: 0;
      bottom: -2px;
      font-weight: normal;
      display: flex;
      flex-direction: column;
      span {
        margin-top: -4px;
      }
    }
    .page {
      position: absolute;
      font-size: 12px;
      right: 0;
      top: 0;
    }
  }
  .tablecontent {
    .table-noborder {
      width: 100%;
      margin: 4px 0;
      border: none;
    }
  }
  .la-table {
    width: 100%;
    margin: 0;
    border: 2px solid #000;
    // border-bottom: none;
    color: #000;
    background-color: #fff9ec;
    td {
      padding: 1px;
      height: 26px;
      font-size: 12px;
      line-height: 18px;
      text-align: center;
      border: 1px solid #000;
      position: relative;
    }
  }
  input {
    width: 100%;
    height: 100%;
    border: none;
    outline: none;
    text-align: center;
  }
  textarea {
    resize: none;
    border: none;
    width: 100%;
    font-size: 12px;
    color: #000;
    line-height: 16px;
    min-height: 18px;
    padding: 1px;
    text-align: center;
    // outline: none;
    // word-break: break-all;
    // overflow: hidden;
  }
}
</style>
