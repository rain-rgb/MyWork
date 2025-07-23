<template>
  <div class="tail">
    <table class="la-table bz">
      <tr>
        <td class="beizhuRemark" style="width: 300px">
          <div class="beizhu">注：</div>
          <textarea placeholder="请输入备注"></textarea>
          <!-- <div class="textarea" style="text-align: left">{{ `11111111111111` }}</div> -->
          <!-- <textarea v-model="reportData.remark" style="text-indent: 5em"></textarea> -->
        </td>
        <td style="width: 80px">
          <div>自检员</div>
          <div>日期</div>
        </td>
        <td>
          <input type="text" placeholder="请输入自检员"/>
          <input type="text" placeholder="请输入日期"/>
          <!-- <div>{{ `2222222222222` }}</div>
          <div>{{ `2222222222222` }}</div> -->
        </td>
        <td style="width: 80px">
          <div>现场监理</div>
          <div>日期</div>
        </td>
        <td>
          <input type="text" placeholder="请输入现场监理"/>
          <input type="text" placeholder="请输入日期"/>
          <!-- <div>{{ `2222222222222` }}</div>
          <div>{{ `2222222222222` }}</div> -->
        </td>
      </tr>
    </table>
    <!-- <table class="foot">
            <tr>
                <td>检测：</td>
                <td>记录：</td>
                <td>复核：</td>
                <td style="width: 130px">日期：</td>
                <td style="width: 50px">年</td>
                <td style="width: 50px">月</td>
                <td style="width: 20px">日</td>
            </tr>
        </table> -->
  </div>
</template>
<script>
export default {
  name: "JJTailReport",
  props: ["formData"],
  components: {},
  data() {
    return {};
  },
  mounted() {
    console.log(`JJHeadReport`);
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
  },
  methods: {},
};
</script>
<style lang="less" scoped>
.tail {
  height: 100%;
  .la-table {
    width: 100%;
    margin: 0;
    // border-right: 2px solid #000;
    // border-left: 2px solid #000;
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
    .beizhuRemark {
      // border-bottom: 2px solid #000;
      background-color: #fff9ec;
    }
    .beizhu {
      position: absolute;
      left: 2px;
      top: 4px;
      line-height: normal;
    }
    textarea {
      width: 100%;
      height: 70%;
      resize: none;
      border: none;
      outline: none;
      padding: 1px;
      margin-top: 10px;
      vertical-align: middle;
      background-color: #fff9ec;
    }
    .textarea {
      width: 100%;
      height: 70%;
      resize: none;
      border: none;
      outline: none;
      padding: 1px;
      margin-top: 20px;
      vertical-align: middle;
    }

    input {
      width: 100%;
      height: 30%;
      border: none;
      outline: none;
      text-align: center;
      background-color: #fff9ec;
    }
  }
  .bz {
    height: calc(100% - 26px);
    border-top: none;
    margin-top: -1px;
  }
  .foot {
    width: 100%;
  }
}
</style>
