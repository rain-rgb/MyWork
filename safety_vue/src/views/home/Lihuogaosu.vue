<template>
  <div class="page-header-index-wide">
    <a-row :gutter="24">
      <a-col :span="24">
        <a-card
          title="隧道简介"
          :loading="loading"
          :bordered="false"
          :body-style="{ padding: '0' }"
        >
          <a-carousel effect="fade" :autoplay="true">
            <div style="display: inline-block; margin-bottom: 20px">
              <div style="width: 70%; display: inline-block">
                <img style="height: 50vh; width: 100vh" :src="imgUrl1" />
              </div>
              <div style="width: 30%; display: inline-block">
                <h2 style="margin-top: 20px">马鞍山隧道:</h2>
                <h1 style="margin-top: 10px">隧道为分离式隧道</h1>
                <p style="margin-top: 10px; margin-right: 4%">
                  右线长度3050m，进口洞门桩号 ~ 出口洞门桩号K7+885~K10+935
                </p>
                <p style="margin-right: 4%">
                  左线长度3025m，进口洞门桩号 ~ 出口洞门桩号ZK7+900~ZK10+925
                </p>
                <h1 style="margin-top: 10px">隧道围岩分级：</h1>
                <p style="margin-top: 10px; margin-right: 4%">
                  隧址区地层岩性结构较为单一，奥陶系下统冶里组、亮甲山组含燧石结核、条带白云岩；寒武系上统崮山组、长山组、凤山组、中统张夏组厚层状白云岩、竹叶状灰岩夹白云岩、泥质条带夹竹叶状灰岩、黄绿色页岩、白云质鲡状灰岩灰色厚层鲡状灰岩，质硬、性脆，强度较高，完整性较好，稳定性较好。
                </p>
                <p style="margin-right: 4%">
                  局部受到S3向斜影响，岩体较破碎，稳定性较差。总体看，断层不发育，岩体强度较高，完整性较好，工程地质条件较好。
                </p>
                <h1 style="margin-top: 10px">施工方法：</h1>
                <p style="margin-top: 10px; margin-right: 4%">
                  设计中明洞部分考虑采用明挖顺作法施工(放坡开挖，逆做防护)，暗洞采用新奥法施工;并根据隧道洞口位置、地形地势、进洞条件、边仰坡稳定情况灵活采用各种措施，包括明洞、临时边坡喷锚支护等。
                </p>
              </div>
            </div>
            <div style="display: inline-block; margin-bottom: 20px">
              <div style="width: 70%; display: inline-block">
                <img style="height: 50vh; width: 100vh" :src="imgUrl2" />
              </div>
              <div style="width: 30%; display: inline-block">
                <h2 style="margin-top: 20px">黄岩隧道:</h2>
                <h1 style="margin-top: 10px">隧道为分离式隧道</h1>
                <p style="margin-top: 10px; margin-right: 4%">
                  右线长度3265m，进口洞门桩号 ~ 出口洞门桩号K17+085~K20+350
                </p>
                <p style="margin-right: 4%">
                  左线长度3292m，进口洞门桩号 ~ 出口洞门桩号ZK17+083~ZK20+375
                </p>
                <h1 style="margin-top: 10px">隧道围岩分级：</h1>
                <p style="margin-top: 10px; margin-right: 4%">
                  本隧道围岩以奥陶系中统上马家沟组(O2s)灰岩、白云质灰岩;奥陶系中统下马家沟组(O2x)泥灰岩、含白云质石膏岩、灰岩。其中含白云质石膏岩，强度低，为软岩或较软岩，稳定性较差。进出口段围岩以第四系离石组粉质粘土为主。
                </p>
                <h1 style="margin-top: 10px">施工方法：</h1>
                <p style="margin-top: 10px; margin-right: 4%">
                  设计中明洞部分考虑采用明挖顺作法施工(放坡开挖，逆做防护)，暗洞采用新奥法施工;并根据隧道洞口位置、地形地势、进洞条件、边仰坡稳定情况灵活采用各种措施，包括明洞、临时边坡喷锚支护等。
                </p>
              </div>
            </div>
            <div style="display: inline-block; margin-bottom: 20px">
              <div style="display: inline-block; width: 70%">
                <img style="height: 50vh; width: 100vh" :src="imgUrl3" />
              </div>
              <div style="display: inline-block; width: 30%">
                <h2 style="margin-top: 20px">潘家山隧道:</h2>
                <h1 style="margin-top: 10px">隧道为分离式隧道</h1>
                <p style="margin-top: 10px; margin-right: 4%">
                  右线长度4607m，进口洞门桩号 ~ 出口洞门桩号YK83+232~YK87+839
                </p>
                <p style="margin-right: 4%">
                  左线长度4685m，进口洞门桩号 ~ 出口洞门桩号ZK83+208~ZK87+893
                </p>
                <h1 style="margin-top: 10px">隧道岩体工程地质特征：</h1>
                <p style="margin-top: 10px; margin-right: 4%">
                  根据勘察，本隧道围岩围岩以二马营组第一亚组下部强风化黄绿色厚层中细粒长石石英砂岩、长石砂岩，以及铜川组紫色泥岩与黄绿色中层~厚层细粒一粗粒长石砂岩互层为主。其中泥岩，质软，易风化;砂岩，质硬、性脆，强度较高。总之，本隧道围岩岩性变化较大，洞体内水文地质条件较为复杂。
                </p>
                <h1 style="margin-top: 10px">施工方法：</h1>
                <p style="margin-top: 10px; margin-right: 4%">
                  设计中明洞部分考虑采用明挖顺作法施工(放坡开挖，逆做防护)，暗洞采用新奥法施工;并根据隧道洞口位置、地形地势、进洞条件、边仰坡稳定情况灵活采用各种措施，包括明洞、临时边坡喷锚支护等。
                </p>
              </div>
            </div>
            <div style="display: inline-block; margin-bottom: 20px">
              <div style="display: inline-block; width: 70%">
                <img style="height: 50vh; width: 100vh" :src="imgUrl4" />
              </div>
              <div style="display: inline-block; width: 30%">
                <h2 style="margin-top: 20px">丁家湾一号:</h2>
                <h1 style="margin-top: 10px">隧道为分离式隧道</h1>
                <p style="margin-top: 10px; margin-right: 4%">
                  右线长度1013m，进口洞门桩号 ~ 出口洞门桩号K196+935~K97+948
                </p>
                <p style="margin-right: 4%">
                  左线长度956m，进口洞门桩号 ~ 出口洞门桩号ZK96+964~ZK97+920
                </p>
                <h1 style="margin-top: 10px">围岩工程地质评价：</h1>
                <p style="margin-top: 10px; margin-right: 4%">
                  根据勘察，本隧道围岩由三叠系下统刘家沟组砂岩、砂岩夹泥岩或砂质泥岩组成，整体上属于较软岩~较坚硬岩，其节理裂隙较发育~发育，围岩质量总体上较好。隧道穿越的围岩。根据岩体质量可分为两段，前半段围岩以中薄层状砂岩夹泥质砂岩为主，属于较软岩，围岩质量相对较差，后半段为中-厚层状砂岩，岩体节理裂较发育，呈较完整~完整状，围岩完整性较好。隧道洞口岩体风化较严重，节理裂隙较发育-发育，强风化层一般厚4.0~16.0m，围岩质量相对较差。
                </p>
                <h1 style="margin-top: 10px">施工方法：</h1>
                <p style="margin-top: 10px; margin-right: 4%">
                  设计中明洞部分考虑采用明挖顺作法施工(放坡开挖，逆做防护)，暗洞采用新奥法施工；并根据隧道洞口位置、地形地势、进洞条件、边仰坡稳定情况灵活采用各种措施，包括明洞、临时边坡喷锚支护等。
                </p>
              </div>
            </div>
            <div style="display: inline-block; margin-bottom: 20px">
              <div style="display: inline-block; width: 70%">
                <img style="height: 50vh; width: 100vh" :src="imgUrl5" />
              </div>
              <div style="display: inline-block; width: 30%">
                <h2 style="margin-top: 20px">问腰隧道:</h2>
                <h1 style="margin-top: 10px">隧道为分离式隧道</h1>
                <p style="margin-top: 10px; margin-right: 4%">
                  右线长度2955m，进口洞门桩号 ~ 出口洞门桩号K123+415~K126+370
                </p>
                <p style="margin-right: 4%">
                  左线长度2965m，进口洞门桩号 ~ 出口洞门桩号ZK123+385~ZK126+350
                </p>
                <p style="margin-right: 4%; color: red">此隧道为瓦斯隧道</p>
                <h1 style="margin-top: 10px">施工方法：</h1>
                <p style="margin-top: 10px; margin-right: 4%">
                  设计中明洞部分考虑采用明挖顺作法施工(放坡开挖，逆做防护)，暗洞采用新奥法施工;并根据隧道洞口位置、地形地势、进洞条件、边仰坡稳定情况灵活采用各种措施，包括明洞、临时边坡喷锚支护等。
                </p>
              </div>
            </div>
            <div style="display: inline-block; margin-bottom: 20px">
              <div style="display: inline-block; width: 70%">
                <img style="height: 50vh; width: 100vh" :src="imgUrl6" />
              </div>
              <div style="display: inline-block; width: 30%">
                <h2 style="margin-top: 20px">问腰隧道:</h2>
                <h1 style="margin-top: 10px">隧道为分离式隧道</h1>
                <p style="margin-top: 10px; margin-right: 4%">
                  右线长度2955m，进口洞门桩号 ~ 出口洞门桩号K123+415~K126+370
                </p>
                <p style="margin-right: 4%">
                  左线长度2965m，进口洞门桩号 ~ 出口洞门桩号ZK123+385~ZK126+350
                </p>
                <p style="margin-right: 4%; color: red">此隧道为瓦斯隧道</p>
                <h1 style="margin-top: 10px">施工方法：</h1>
                <p style="margin-top: 10px; margin-right: 4%">
                  设计中明洞部分考虑采用明挖顺作法施工(放坡开挖，逆做防护)，暗洞采用新奥法施工;并根据隧道洞口位置、地形地势、进洞条件、边仰坡稳定情况灵活采用各种措施，包括明洞、临时边坡喷锚支护等。
                </p>
              </div>
            </div>
            <div style="display: inline-block; margin-bottom: 20px">
              <div style="display: inline-block; width: 70%">
                <img style="height: 50vh; width: 100vh" :src="imgUrl7" />
              </div>
              <div style="display: inline-block; width: 30%">
                <h2 style="margin-top: 20px">太岳山隧道:</h2>
                <h1 style="margin-top: 10px">隧道为分离式隧道</h1>
                <p style="margin-top: 10px; margin-right: 4%">
                  右线长度11160m，进口洞门桩号 ~ 出口洞门桩号K133+660~K144+820
                </p>
                <p style="margin-right: 4%">
                  左线长度11135m，进口洞门桩号 ~ 出口洞门桩号ZK133+705~ZK144+840
                </p>
                <p style="margin-right: 4%; color: red">有斜井</p>
                <h1 style="margin-top: 10px">施工方法：</h1>
                <p style="margin-top: 10px; margin-right: 4%">
                  设计中明洞部分考虑采用明挖顺作法施工(放坡开挖，逆做防护)，暗洞采用新奥法施工;并根据隧道洞口位置、地形地势、进洞条件、边仰坡稳定情况灵活采用各种措施，包括明洞、临时边坡喷锚支护等。
                </p>
              </div>
            </div>
            <div style="display: inline-block; margin-bottom: 20px">
              <div style="display: inline-block; width: 70%">
                <img style="height: 50vh; width: 100vh" :src="imgUrl8" />
              </div>
              <div style="display: inline-block; width: 30%">
                <h2 style="margin-top: 20px">太岳山隧道:</h2>
                <h1 style="margin-top: 10px">隧道为分离式隧道</h1>
                <p style="margin-top: 10px; margin-right: 4%">
                  右线长度11160m，进口洞门桩号 ~ 出口洞门桩号K133+660~K144+820
                </p>
                <p style="margin-right: 4%">
                  左线长度11135m，进口洞门桩号 ~ 出口洞门桩号ZK133+705~ZK144+840
                </p>
                <h1 style="margin-top: 10px">施工方法：</h1>
                <p style="margin-top: 10px; margin-right: 4%">
                  设计中明洞部分考虑采用明挖顺作法施工(放坡开挖，逆做防护)，暗洞采用新奥法施工;并根据隧道洞口位置、地形地势、进洞条件、边仰坡稳定情况灵活采用各种措施，包括明洞、临时边坡喷锚支护等。
                </p>
              </div>
            </div>
          </a-carousel>
        </a-card>
      </a-col>
    </a-row>
    <a-row :gutter="24">
      <a-card
        :loading="loading"
        :class="{ 'anty-list-cust': true }"
        :bordered="false"
        :style="{ marginTop: '24px' }"
      >
        <a-tabs
          default-active-key="1"
          size="large"
          :tab-bar-style="{ marginBottom: '24px', paddingLeft: '16px' }"
        >
          <a-tab-pane loading="true" tab="隧道人员定位" key="1">
            <a-table
              :dataSource="dataSource1"
              size="default"
              :columns="columns1"
              rowKey="id"
              :pagination="ipagination1"
              @change="tableChange1"
              :customRow="handleClick"
              :rowClassName="setRowClassName"
            >
            </a-table>
          </a-tab-pane>
          <a-tab-pane loading="true" tab="门禁考勤" key="2">
            <a-table
              :dataSource="dataSource2"
              size="default"
              rowKey="id"
              :columns="columns2"
              :pagination="ipagination1"
              @change="tableChange1"
              :customRow="handleClick"
              :rowClassName="setRowClassName"
            >
            </a-table>
          </a-tab-pane>
        </a-tabs>
      </a-card>
    </a-row>
  </div>
</template>

<script>
import dqjyfb from "@/assets/img/dqj1fb.png";
import dqjefb from "@/assets/img/dqj2fb.png";
import lqsfb from "@/assets/img/lq4fb.png";
import lqwfb from "@/assets/img/lq5fb.png";
import ztjtyfb from "@/assets/img/ztjt1fb.png";
import ztjtefb from "@/assets/img/ztjt2fb.png";
import ztjtsfb from "@/assets/img/ztjt3fb.png";
import ztjtsifb from "@/assets/img/ztjt4fb.png";
import { getAction } from "@api/manage";

export default {
  name: "Lihuogaosu",
  components: {
    dqjyfb,
    dqjefb,
    lqsfb,
    lqwfb,
    ztjtyfb,
    ztjtefb,
    ztjtsfb,
    ztjtsifb,
  },
  data() {
    return {
      imgUrl1: dqjyfb,
      imgUrl2: dqjefb,
      imgUrl3: lqsfb,
      imgUrl4: lqwfb,
      imgUrl5: ztjtyfb,
      imgUrl6: ztjtefb,
      imgUrl7: ztjtsfb,
      imgUrl8: ztjtsifb,
      loading: true,
      indexBottomTab: "1",
      ipagination1: {
        current: 1,
        pageSize: 5,
        pageSizeOptions: ["10", "20", "30"],
        showTotal: (total, range) => {
          return range[0] + "-" + range[1] + " 共" + total + "条";
        },
        showQuickJumper: true,
        showSizeChanger: true,
        total: 0,
      },
      dataSource1: [],
      columns1: [
        {
          title: "人员姓名",
          align: "center",
          dataIndex: "username",
        },
        {
          title: "卡号",
          align: "center",
          dataIndex: "card",
        },
        {
          title: "数据上传时间",
          align: "center",
          dataIndex: "datatime",
        },
        {
          title: "距离洞口距离",
          align: "center",
          dataIndex: "dv",
        },
        {
          title: "定位时间",
          align: "center",
          dataIndex: "dvtime",
        },
        {
          title: "定位基站名称",
          align: "center",
          dataIndex: "jz",
        },
        {
          title: "距离基站距离",
          align: "center",
          dataIndex: "jzdv",
        },
        {
          title: "基站距离洞口距离",
          align: "center",
          dataIndex: "jzkou",
        },
        {
          title: "设备名称",
          align: "center",
          dataIndex: "shebeino",
        },
        {
          title: "基站位置",
          align: "center",
          dataIndex: "jzwz",
        },
        {
          title: "班组编号",
          align: "center",
          dataIndex: "departmentid",
        },
        {
          title: "班组名称",
          align: "center",
          dataIndex: "departname",
        },
        {
          title: "X坐标",
          align: "center",
          dataIndex: "nx",
        },
        {
          title: "Y坐标",
          align: "center",
          dataIndex: "ny",
        },
        {
          title: "电池电量",
          align: "center",
          dataIndex: "voltages",
        },
        // {
        //   title:'报警状态',
        //   align:"center",
        //   dataIndex: 'alarms'
        // },
        {
          title: "上报时间",
          align: "center",
          dataIndex: "uploadtime",
        },
      ],
      dataSource2: [],
      columns2: [
        {
          title: "设备名称",
          align: "center",
          dataIndex: "shebeino_dictText",
        },
        {
          title: "姓名",
          align: "center",
          dataIndex: "names",
        },
        {
          title: "开门时间",
          align: "center",
          dataIndex: "opentime",
        },
        {
          title: "班组",
          align: "center",
          dataIndex: "departname",
        },
        {
          title: "备注",
          align: "center",
          dataIndex: "info",
        },
      ],
      url: {
        list: "/devicetunnelposition/deviceTunnelPositiondata/list3",
        list1: "/entranceGuardRecord/entranceGuardRecord/list",
      },
    };
  },
  created() {
    this.rydw();
    this.mjkq();
    setTimeout(() => {
      this.loading = !this.loading;
    }, 1000);
  },
  methods: {
    callback(key) {
      console.log(key);
    },
    tableChange1(pagination) {
      this.ipagination1.current = pagination.current;
      this.ipagination1.pageSize = pagination.pageSize;
    },
    rydw() {
      //隧道人员定位
      let params = { jzwz: "洞" };
      getAction(this.url.list, params).then((res) => {
        if (res.success) {
          //console.log("++++++++++++++++++",res)
          this.dataSource1 = res.result;
        }
      });
    },
    mjkq() {
      //门禁考勤
      let params = { column: "id", order: "desc" };
      getAction(this.url.list1, params).then((res) => {
        if (res.success) {
          //console.log("____________________",res)
          this.dataSource2 = res.result.records;
        }
      });
    },
  },
};
</script>

<style scoped></style>
