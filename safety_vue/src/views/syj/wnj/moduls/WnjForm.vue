<template>
  <a-spin :spinning="confirmLoading">
    <j-editable-table
      :ref="refKeys[0]"
      :loading="bhzSwCailiaoTable.loading"
      :columns="bhzSwCailiaoTable.columns"
      :dataSource="bhzSwCailiaoTable.dataSource"
      :maxHeight="300"
      :disabled="formDisabled"
      :rowNumber="true"
      :rowSelection="true"
      :actionButton="false"
    >
      <template v-slot:action="props">
        <a-tag color="green" v-if="props.value == '0'">合格</a-tag>
        <a-tag color="orange" v-if="props.value == '1'">初级超标</a-tag>
        <a-tag color="yellow" v-if="props.value == '2'">中级超标</a-tag>
        <a-tag color="red" v-if="props.value == '3'">高级超标</a-tag>
      </template>
    </j-editable-table>
    <div>
      <a-row>
        <a-col :span="24">
          <div class="imgButton" v-if="imgURl1.length > 0">
            <div class="viewerBox">
              <viewer v-if="imgURl1[0]">
                <img :src="imgURl1[0]" style="width: 50px; height: 50px" />
                <div>开始图片</div>
              </viewer>
              <viewer v-if="imgURl1[1]">
                <img :src="imgURl1[1]" style="width: 50px; height: 50px" />
                <div>破碎图片</div>
              </viewer>
            </div>
          </div>
          <div v-if="dataSource1.length > 0">
            <!-- <LineChartMultid
              :title="dataSource1[0].name"
              :data-source="dataSource1"
            ></LineChartMultid> -->
            <LineChartMultid
              :title="dataSource1[0].name"
              :data-source="dataList"
            ></LineChartMultid>
          </div>
        </a-col>
        <a-col :span="24">
          <div class="imgButton" v-if="imgURl2.length > 0">
            <div class="viewerBox">
              <viewer v-if="imgURl2[0]">
                <img :src="imgURl2[0]" style="width: 50px; height: 50px" />
                <div>开始图片</div>
              </viewer>
              <viewer v-if="imgURl2[1]">
                <img :src="imgURl2[1]" style="width: 50px; height: 50px" />
                <div>破碎图片</div>
              </viewer>
            </div>
          </div>
          <div v-if="dataSource2.length > 0">
            <!-- <LineChartMultid
              :title="dataSource2[0].name"
              :data-source="dataSource2"
            ></LineChartMultid> -->
          </div>
        </a-col>
        <a-col :span="24">
          <div class="imgButton" v-if="imgURl3.length > 0">
            <div class="viewerBox">
              <viewer v-if="imgURl3[0]">
                <img :src="imgURl3[0]" style="width: 50px; height: 50px" />
                <div>开始图片</div>
              </viewer>
              <viewer v-if="imgURl3[1]">
                <img :src="imgURl3[1]" style="width: 50px; height: 50px" />
                <div>破碎图片</div>
              </viewer>
            </div>
          </div>
          <div v-if="dataSource3.length > 0">
            <!-- <LineChartMultid
              :title="dataSource3[0].name"
              :data-source="dataSource3"
            ></LineChartMultid> -->
          </div>
        </a-col>
        <a-col :span="24">
          <div class="imgButton" v-if="imgURl4.length > 0">
            <div class="viewerBox">
              <viewer v-if="imgURl4[0]">
                <img :src="imgURl4[0]" style="width: 50px; height: 50px" />
                <div>开始图片</div>
              </viewer>
              <viewer v-if="imgURl4[1]">
                <img :src="imgURl4[1]" style="width: 50px; height: 50px" />
                <div>破碎图片</div>
              </viewer>
            </div>
          </div>
          <div v-if="dataSource4.length > 0">
            <!-- <LineChartMultid
              :title="dataSource4[0].name"
              :data-source="dataSource4"
            ></LineChartMultid> -->
          </div>
        </a-col>
        <a-col :span="24">
          <div class="imgButton" v-if="imgURl5.length > 0">
            <div class="viewerBox">
              <viewer v-if="imgURl5[0]">
                <img :src="imgURl5[0]" style="width: 50px; height: 50px" />
                <div>开始图片</div>
              </viewer>
              <viewer v-if="imgURl5[1]">
                <img :src="imgURl5[1]" style="width: 50px; height: 50px" />
                <div>破碎图片</div>
              </viewer>
            </div>
          </div>
          <div v-if="dataSource5.length > 0">
            <!-- <LineChartMultid
              :title="dataSource5[0].name"
              :data-source="dataSource5"
            ></LineChartMultid> -->
          </div>
        </a-col>
        <a-col :span="24">
          <div class="imgButton" v-if="imgURl6.length > 0">
            <div class="viewerBox">
              <viewer v-if="imgURl6[0]">
                <img :src="imgURl6[0]" style="width: 50px; height: 50px" />
                <div>开始图片</div>
              </viewer>
              <viewer v-if="imgURl6[1]">
                <img :src="imgURl6[1]" style="width: 50px; height: 50px" />
                <div>破碎图片</div>
              </viewer>
            </div>
          </div>
          <div v-if="dataSource6.length > 0">
            <!-- <LineChartMultid
              :title="dataSource6[0].name"
              :data-source="dataSource6"
            ></LineChartMultid> -->
          </div>
        </a-col>
      </a-row>
    </div>
    <!--      </a-tab-pane>-->
    <!--    </a-tabs>-->
  </a-spin>
</template>

<script>
import pick from "lodash.pick";
import { getAction } from "@/api/manage";
import { FormTypes, getRefPromise } from "@/utils/JEditableTableUtil";
import { JEditableTableMixin } from "@/mixins/JEditableTableMixin";
import { validateDuplicateValue } from "@/utils/util";
import JFormContainer from "@/components/jeecg/JFormContainer";
import JDate from "@/components/jeecg/JDate";
import JDictSelectTag from "@/components/dict/JDictSelectTag";
// import LineChartMultid from "@comp/chart/YaLijiLzqx";
import LineChartMultid from "@comp/chart/YaLijiLzqxNew";
export default {
  name: "WnjFrom",
  mixins: [JEditableTableMixin],
  components: {
    JFormContainer,
    JDate,
    JDictSelectTag,
    LineChartMultid,
  },
  data() {
    return {
      dataSource1: [],
      dataSource2: [],
      dataSource3: [],
      dataSource4: [],
      dataSource5: [],
      dataSource6: [],
      dataList: {
        dataSource1: [],
        dataSource2: [],
        dataSource3: [],
        dataSource4: [],
        dataSource5: [],
        dataSource6: [],
      },
      imgURl1: [],
      imgURl2: [],
      imgURl3: [],
      imgURl4: [],
      imgURl5: [],
      imgURl6: [],
      labelCol: {
        xs: { span: 24 },
        sm: { span: 6 },
      },
      wrapperCol: {
        xs: { span: 24 },
        sm: { span: 16 },
      },
      labelCol2: {
        xs: { span: 24 },
        sm: { span: 3 },
      },
      wrapperCol2: {
        xs: { span: 24 },
        sm: { span: 20 },
      },
      // 新增时子表默认添加几行空数据
      addDefaultRowNum: 1,
      validatorRules: {},
      refKeys: ["SyyljBases"],
      tableKeys: ["SyyljBases"],
      activeKey: "SyyljBases",
      // 万能机表信息
      bhzSwCailiaoTable: {
        loading: false,
        dataSource: [],
        columns: [
          {
            title: "原始标距",
            key: "wsbz",
            type: FormTypes.normal,
            width: "200px",
            placeholder: "请输入${title}",
          },
          {
            title: "断后标距",
            key: "dhbz",
            type: FormTypes.normal,
            width: "200px",
            placeholder: "请输入${title}",
          },
          {
            title: "最大力值",
            key: "lz",
            type: FormTypes.normal,
            width: "200px",
            placeholder: "请输入${title}",
          },
          {
            title: "抗拉强度",
            key: "lzqd",
            type: FormTypes.normal,
            width: "200px",
            placeholder: "请输入${title}",
          },
          {
            title: "屈服力值",
            key: "qflz",
            type: FormTypes.normal,
            width: "200px",
            placeholder: "请输入${title}",
          },
          {
            title: "屈服强度",
            type: FormTypes.normal,
            key: "qfqd",
            width: "200px",
            placeholder: "请输入${title}",
          },
          {
            title: "伸长率",
            type: FormTypes.normal,
            key: "scl",
            width: "200px",
            placeholder: "请输入${title}",
          },
          {
            title: "试验时间",
            type: FormTypes.normal,
            key: "sysj",
            width: "200px",
            placeholder: "请输入${title}",
          },
          {
            title: "最大力值伸长率",
            type: FormTypes.normal,
            key: "zdlzscl",
            width: "200px",
            placeholder: "请输入${title}",
          },
          {
            title: "拉断处描述",
            type: FormTypes.normal,
            key: "ldcms",
            width: "200px",
            placeholder: "请输入${title}",
          },
          {
            title: "断口位置",
            type: FormTypes.normal,
            key: "dkwz",
            width: "200px",
            placeholder: "请输入${title}",
          },
        ],
      },
      url: {
        add: "/syj/tSyjzb/add",
        edit: "/syj/tSyjzb/edit",
        queryById: "/syj/tSyjzb/queryById",
        SyyljBases: {
          list: "/syj/tSyjzb/selectwnjById",
        },
        list: "/syj/tSyjzb/selectwnjById",
      },
    };
  },
  props: {
    //流程表单data
    formData: {
      type: Object,
      default: () => {},
      required: false,
    },
    //表单模式：false流程表单 true普通表单
    formBpm: {
      type: Boolean,
      default: false,
      required: false,
    },
    //表单禁用
    disabled: {
      type: Boolean,
      default: false,
      required: false,
    },
  },
  computed: {
    formDisabled() {
      if (this.formBpm === true) {
        if (this.formData.disabled === false) {
          return false;
        }
        return true;
      }
      return this.disabled;
    },
    showFlowSubmitButton() {
      if (this.formBpm === true) {
        if (this.formData.disabled === false) {
          return true;
        }
      }
      return false;
    },
  },
  created() {
    //如果是流程中表单，则需要加载流程表单data
    this.showFlowData();
  },
  methods: {
    addBefore() {
      this.form.resetFields();
      this.bhzSwCailiaoTable.dataSource = [];
    },
    getAllTable() {
      let values = this.tableKeys.map((key) => getRefPromise(this, key));
      return Promise.all(values);
    },
    /** 调用完edit()方法之后会自动调用此方法 */
    editAfter() {
      let fieldval = pick(this.model); //,'sbbh_dictText','sylx','wtbh','zzrq','syrq','lq','sjcc','sjmj','sjsl','sjqd','zsxs','qddbz','pdjg','czry','cjmc','pzbm','gczj','area','fbl','beizhu','sjscsj'
      this.$nextTick(() => {
        this.form.setFieldsValue(fieldval);
      });
      // 加载子表数据
      if (this.model.syjid) {
        let params = { syjid: this.model.syjid };
        this.requestSubTableData(
          this.url.SyyljBases.list,
          params,
          this.bhzSwCailiaoTable
        );
        this.lzqx(); //力值过程值曲线图
      }
    },
    /** 整理成formData */
    classifyIntoFormData(allValues) {
      let main = Object.assign(this.model, allValues.formValue);
      return {
        ...main, // 展开
        bhzSwCailiaoList: allValues.tablesValue[0].values,
      };
    },
    //渲染流程表单数据
    showFlowData() {
      if (this.formBpm === true) {
        let params = { id: this.formData.dataId };
        getAction(this.url.queryById, params).then((res) => {
          if (res.success) {
            this.edit(res.result);
          }
        });
      }
    },
    validateError(msg) {
      this.$message.error(msg);
    },
    popupCallback(row) {
      // this.form.setFieldsValue(pick(row,'shebeibianhao','shiyanleixing','weituobianhao','zhijianriqi','shiyanriqi','lingqi','shijianchicun','shijianmianji','shiianshuliang','shejiqiangdu','zhesuanxishu','qiangdudaibiaozhi','pandingjieguo','caozuorenyuan','gongchengmingcheng','pinzhongbianma','gongchengzhijing','chengyamianji','shengchanchangjia','beizhu','shujushangchuanshijian'))
      this.form.setFieldsValue(pick(row)); //,'sbbh_dictText','sylx','wtbh','zzrq','syrq','lq','sjcc','sjmj','sjsl','sjqd','zsxs','qddbz','pdjg','czry','cjmc','pzbm','gczj','area','fbl','beizhu','sjscsj'
    },
    lzqx: function () {
      //力值过程值曲线图
      let params = { syjid: this.model.syjid };
      this.dataSource1 = [];
      this.dataSource2 = [];
      this.dataSource3 = [];
      this.dataSource4 = [];
      this.dataSource5 = [];
      this.dataSource6 = [];
      getAction(this.url.list, params).then((res) => {
        if (res.success) {
          let data = res.result;
          if (data.length > 0) {
            for (let i = 0; i < data.length; i++) {
              var lzgc1 = data[i].lzgc.split(",");
              var sjgc1 = data[i].sjgc.split(",");
              for (let j = 0; j < lzgc1.length; j++) {
                if (i === 0) {
                  this.dataSource1.push({
                    type: parseFloat(sjgc1[j]),
                    抗压力值: parseFloat(lzgc1[j]),
                    name: "试件序号" + data[i].sjxh,
                  });
                  if (this.imgURl1.length < 2) {
                    this.imgURl1.push(data[i].spic, data[i].pspic);
                  }
                }
                if (i === 1) {
                  this.dataSource2.push({
                    type: parseFloat(sjgc1[j]),
                    抗压力值: parseFloat(lzgc1[j]),
                    name: "试件序号" + data[i].sjxh,
                  });
                  if (this.imgURl2.length < 2) {
                    this.imgURl2.push(data[i].spic, data[i].pspic);
                  }
                }
                if (i === 2) {
                  this.dataSource3.push({
                    type: parseFloat(sjgc1[j]),
                    抗压力值: parseFloat(lzgc1[j]),
                    name: "试件序号" + data[i].sjxh,
                  });
                  if (this.imgURl3.length < 2) {
                    this.imgURl3.push(data[i].spic, data[i].pspic);
                  }
                }
                if (i === 3) {
                  this.dataSource4.push({
                    type: parseFloat(sjgc1[j]),
                    抗压力值: parseFloat(lzgc1[j]),
                    name: "试件序号" + data[i].sjxh,
                  });
                  if (this.imgURl4.length < 2) {
                    this.imgURl4.push(data[i].spic, data[i].pspic);
                  }
                }
                if (i === 4) {
                  this.dataSource5.push({
                    type: parseFloat(sjgc1[j]),
                    抗压力值: parseFloat(lzgc1[j]),
                    name: "试件序号" + data[i].sjxh,
                  });
                  if (this.imgURl6.length < 2) {
                    this.imgURl6.push(data[i].spic, data[i].pspic);
                  }
                }
                if (i === 5) {
                  this.dataSource6.push({
                    type: parseFloat(sjgc1[j]),
                    抗压力值: parseFloat(lzgc1[j]),
                    name: "试件序号" + data[i].sjxh,
                  });
                  if (this.imgURl6.length < 2) {
                    this.imgURl6.push(data[i].spic, data[i].pspic);
                  }
                }
              }
              this.dataList.dataSource1 = this.dataSource1;
              this.dataList.dataSource2 = this.dataSource2;
              this.dataList.dataSource3 = this.dataSource3;
              this.dataList.dataSource4 = this.dataSource4;
              this.dataList.dataSource5 = this.dataSource5;
              this.dataList.dataSource6 = this.dataSource6;
            }
          } else {
            this.$message.error("该设备的抗压力值没有数据");
          }
        }
      });
    },
  },
};
</script>

<style lang="less" scoped>
.imgButton {
  margin-top: 30px;
  position: absolute;
  left: 7%;
  top: -2%;

  .viewerBox {
    display: flex;

    img {
      margin-right: 20px;
    }
  }
}
</style>
