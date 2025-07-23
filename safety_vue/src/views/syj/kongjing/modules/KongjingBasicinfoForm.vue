<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <!--        <a-row>-->
        <!--          <a-col :span="24">-->
        <!--            <a-form-model-item label="设备商标识" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="vendorid">-->
        <!--              <a-input v-model="model.vendorid" placeholder="请输入设备商标识"  ></a-input>-->
        <!--            </a-form-model-item>-->
        <!--          </a-col>-->
        <!--          <a-col :span="24">-->
        <!--            <a-form-model-item label="流水号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="serialno">-->
        <!--              <a-input v-model="model.serialno" placeholder="请输入流水号"  ></a-input>-->
        <!--            </a-form-model-item>-->
        <!--          </a-col>-->
        <!--          <a-col :span="24">-->
        <!--            <a-form-model-item label="测试仪编号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="machineid">-->
        <!--              <a-input v-model="model.machineid" placeholder="请输入测试仪编号"  ></a-input>-->
        <!--            </a-form-model-item>-->
        <!--          </a-col>-->
        <!--          <a-col :span="24">-->
        <!--            <a-form-model-item label="试桩编号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="pileno">-->
        <!--              <a-input v-model="model.pileno" placeholder="请输入试桩编号"  ></a-input>-->
        <!--            </a-form-model-item>-->
        <!--          </a-col>-->
        <!--          <a-col :span="24">-->
        <!--            <a-form-model-item label="检测日期(格式：yyyy-MM-dd)" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="testpiledate">-->
        <!--              <a-input v-model="model.testpiledate" placeholder="请输入检测日期(格式：yyyy-MM-dd)"  ></a-input>-->
        <!--            </a-form-model-item>-->
        <!--          </a-col>-->
        <!--          <a-col :span="24">-->
        <!--            <a-form-model-item label="检测时间(格式：HH:mm:ss)" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="testpiletime">-->
        <!--              <a-input v-model="model.testpiletime" placeholder="请输入检测时间(格式：HH:mm:ss)"  ></a-input>-->
        <!--            </a-form-model-item>-->
        <!--          </a-col>-->
        <!--          <a-col :span="24">-->
        <!--            <a-form-model-item label="检测人员" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="worker">-->
        <!--              <a-input v-model="model.worker" placeholder="请输入检测人员"  ></a-input>-->
        <!--            </a-form-model-item>-->
        <!--          </a-col>-->
        <!--          <a-col :span="24">-->
        <!--            <a-form-model-item label="检测单位" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="testunit">-->
        <!--              <a-input v-model="model.testunit" placeholder="请输入检测单位"  ></a-input>-->
        <!--            </a-form-model-item>-->
        <!--          </a-col>-->
        <!--          <a-col :span="24">-->
        <!--            <a-form-model-item label="施工单位" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="constructionunit">-->
        <!--              <a-input v-model="model.constructionunit" placeholder="请输入施工单位"  ></a-input>-->
        <!--            </a-form-model-item>-->
        <!--          </a-col>-->
        <!--          <a-col :span="24">-->
        <!--            <a-form-model-item label="工程名称" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="projectname">-->
        <!--              <a-input v-model="model.projectname" placeholder="请输入工程名称"  ></a-input>-->
        <!--            </a-form-model-item>-->
        <!--          </a-col>-->
        <!--          <a-col :span="24">-->
        <!--            <a-form-model-item label="孔深(单位：m)" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="depth">-->
        <!--              <a-input-number v-model="model.depth" placeholder="请输入孔深(单位：m)" style="width: 100%" />-->
        <!--            </a-form-model-item>-->
        <!--          </a-col>-->
        <!--          <a-col :span="24">-->
        <!--            <a-form-model-item label="孔径(单位：mm)" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="diameter">-->
        <!--              <a-input-number v-model="model.diameter" placeholder="请输入孔径(单位：mm)" style="width: 100%" />-->
        <!--            </a-form-model-item>-->
        <!--          </a-col>-->
        <!--          <a-col :span="24">-->
        <!--            <a-form-model-item label="始测深度(单位：m)" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="begindepth">-->
        <!--              <a-input-number v-model="model.begindepth" placeholder="请输入始测深度(单位：m)" style="width: 100%" />-->
        <!--            </a-form-model-item>-->
        <!--          </a-col>-->
        <!--          <a-col :span="24">-->
        <!--            <a-form-model-item label="标高(单位：m)" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="height">-->
        <!--              <a-input-number v-model="model.height" placeholder="请输入标高(单位：m)" style="width: 100%" />-->
        <!--            </a-form-model-item>-->
        <!--          </a-col>-->
        <!--          <a-col :span="24">-->
        <!--            <a-form-model-item label="管数" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="pipenums">-->
        <!--              <a-input-number v-model="model.pipenums" placeholder="请输入管数" style="width: 100%" />-->
        <!--            </a-form-model-item>-->
        <!--          </a-col>-->
        <!--          <a-col :span="24">-->
        <!--            <a-form-model-item label="移距(单位：mm)" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="shift">-->
        <!--              <a-input-number v-model="model.shift" placeholder="请输入移距(单位：mm)" style="width: 100%" />-->
        <!--            </a-form-model-item>-->
        <!--          </a-col>-->
        <!--          <a-col :span="24">-->
        <!--            <a-form-model-item label="偏移角" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="angle">-->
        <!--              <a-input-number v-model="model.angle" placeholder="请输入偏移角" style="width: 100%" />-->
        <!--            </a-form-model-item>-->
        <!--          </a-col>-->
        <!--          <a-col :span="24">-->
        <!--            <a-form-model-item label="采集方式  0：自动提升 1：手动提升 2：手动(4s/m) 3：手动(6s/m) 4：手动(8s/m)" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="method">-->
        <!--              <a-input-number v-model="model.method" placeholder="请输入采集方式  0：自动提升 1：手动提升 2：手动(4s/m) 3：手动(6s/m) 4：手动(8s/m)" style="width: 100%" />-->
        <!--            </a-form-model-item>-->
        <!--          </a-col>-->
        <!--          <a-col :span="24">-->
        <!--            <a-form-model-item label="数字滤波 0：未启动 1：启动" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="filter">-->
        <!--              <a-input-number v-model="model.filter" placeholder="请输入数字滤波 0：未启动 1：启动" style="width: 100%" />-->
        <!--            </a-form-model-item>-->
        <!--          </a-col>-->
        <!--          <a-col :span="24">-->
        <!--            <a-form-model-item label="泥浆补偿 泥浆密度补偿 0:未启动 大于0：启动" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="density">-->
        <!--              <a-input-number v-model="model.density" placeholder="请输入泥浆补偿 泥浆密度补偿 0:未启动 大于0：启动" style="width: 100%" />-->
        <!--            </a-form-model-item>-->
        <!--          </a-col>-->
        <!--          <a-col :span="24">-->
        <!--            <a-form-model-item label="波形增强 0：未启动 1：启动" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="waveenhance">-->
        <!--              <a-input-number v-model="model.waveenhance" placeholder="请输入波形增强 0：未启动 1：启动" style="width: 100%" />-->
        <!--            </a-form-model-item>-->
        <!--          </a-col>-->
        <!--          <a-col :span="24">-->
        <!--            <a-form-model-item label="判读算法 0：自动判读 1：幅值判读 2：方差判读" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="algorithm">-->
        <!--              <a-input-number v-model="model.algorithm" placeholder="请输入判读算法 0：自动判读 1：幅值判读 2：方差判读" style="width: 100%" />-->
        <!--            </a-form-model-item>-->
        <!--          </a-col>-->
        <!--          <a-col :span="24">-->
        <!--            <a-form-model-item label="幅值参数 " :labelCol="labelCol" :wrapperCol="wrapperCol" prop="amplitude">-->
        <!--              <a-input-number v-model="model.amplitude" placeholder="请输入幅值参数 " style="width: 100%" />-->
        <!--            </a-form-model-item>-->
        <!--          </a-col>-->
        <!--          <a-col :span="24">-->
        <!--            <a-form-model-item label="方差" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="variance">-->
        <!--              <a-input-number v-model="model.variance" placeholder="请输入方差" style="width: 100%" />-->
        <!--            </a-form-model-item>-->
        <!--          </a-col>-->
        <!--          <a-col :span="24">-->
        <!--            <a-form-model-item label="主频" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="frequency">-->
        <!--              <a-input-number v-model="model.frequency" placeholder="请输入主频" style="width: 100%" />-->
        <!--            </a-form-model-item>-->
        <!--          </a-col>-->
        <!--          <a-col :span="24">-->
        <!--            <a-form-model-item label="起点 0：负 1：正" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="origin">-->
        <!--              <a-input-number v-model="model.origin" placeholder="请输入起点 0：负 1：正" style="width: 100%" />-->
        <!--            </a-form-model-item>-->
        <!--          </a-col>-->
        <!--          <a-col :span="24">-->
        <!--            <a-form-model-item label="首波方式 0：负 1：正" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="mode">-->
        <!--              <a-input-number v-model="model.mode" placeholder="请输入首波方式 0：负 1：正" style="width: 100%" />-->
        <!--            </a-form-model-item>-->
        <!--          </a-col>-->
        <!--          <a-col :span="24">-->
        <!--            <a-form-model-item label="检测标准 公路、铁路、 建筑、广东规范等" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="standard">-->
        <!--              <a-input v-model="model.standard" placeholder="请输入检测标准 公路、铁路、 建筑、广东规范等"  ></a-input>-->
        <!--            </a-form-model-item>-->
        <!--          </a-col>-->
        <!--          <a-col :span="24">-->
        <!--            <a-form-model-item label="采集方向 0：下放 1：上拉" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="direction">-->
        <!--              <a-input-number v-model="model.direction" placeholder="请输入采集方向 0：下放 1：上拉" style="width: 100%" />-->
        <!--            </a-form-model-item>-->
        <!--          </a-col>-->
        <!--          <a-col :span="24">-->
        <!--            <a-form-model-item label="罗盘修正 0：未修正 1：已修 正" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="compass">-->
        <!--              <a-input-number v-model="model.compass" placeholder="请输入罗盘修正 0：未修正 1：已修 正" style="width: 100%" />-->
        <!--            </a-form-model-item>-->
        <!--          </a-col>-->
        <!--          <a-col :span="24">-->
        <!--            <a-form-model-item label="罗盘基准角 范围[0-360]" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="compassangle">-->
        <!--              <a-input-number v-model="model.compassangle" placeholder="请输入罗盘基准角 范围[0-360]" style="width: 100%" />-->
        <!--            </a-form-model-item>-->
        <!--          </a-col>-->
        <!--          <a-col :span="24">-->
        <!--            <a-form-model-item label="采样间隔 单位us" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="sampleinterval">-->
        <!--              <a-input-number v-model="model.sampleinterval" placeholder="请输入采样间隔 单位us" style="width: 100%" />-->
        <!--            </a-form-model-item>-->
        <!--          </a-col>-->
        <!--          <a-col :span="24">-->
        <!--            <a-form-model-item label="采样长度 1024、2048" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="samplelength">-->
        <!--              <a-input-number v-model="model.samplelength" placeholder="请输入采样长度 1024、2048" style="width: 100%" />-->
        <!--            </a-form-model-item>-->
        <!--          </a-col>-->
        <!--          <a-col :span="24">-->
        <!--            <a-form-model-item label="高通 单位kHz" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="highfilter">-->
        <!--              <a-input-number v-model="model.highfilter" placeholder="请输入高通 单位kHz" style="width: 100%" />-->
        <!--            </a-form-model-item>-->
        <!--          </a-col>-->
        <!--          <a-col :span="24">-->
        <!--            <a-form-model-item label="低通 单位kHz" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="lowfilter">-->
        <!--              <a-input-number v-model="model.lowfilter" placeholder="请输入低通 单位kHz" style="width: 100%" />-->
        <!--            </a-form-model-item>-->
        <!--          </a-col>-->
        <!--          <a-col :span="24">-->
        <!--            <a-form-model-item label="发射脉宽 单位us" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="pulsewidth">-->
        <!--              <a-input-number v-model="model.pulsewidth" placeholder="请输入发射脉宽 单位us" style="width: 100%" />-->
        <!--            </a-form-model-item>-->
        <!--          </a-col>-->
        <!--          <a-col :span="24">-->
        <!--            <a-form-model-item label="发射电压 0：低压 1：高压" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="voltage">-->
        <!--              <a-input-number v-model="model.voltage" placeholder="请输入发射电压 0：低压 1：高压" style="width: 100%" />-->
        <!--            </a-form-model-item>-->
        <!--          </a-col>-->
        <!--          <a-col :span="24">-->
        <!--            <a-form-model-item label=" 滑轮直径 单位mm" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="pulleydiameter">-->
        <!--              <a-input-number v-model="model.pulleydiameter" placeholder="请输入 滑轮直径 单位mm" style="width: 100%" />-->
        <!--            </a-form-model-item>-->
        <!--          </a-col>-->
        <!--          <a-col :span="24">-->
        <!--            <a-form-model-item label="电缆直径 单位mm" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="cablediameter">-->
        <!--              <a-input-number v-model="model.cablediameter" placeholder="请输入电缆直径 单位mm" style="width: 100%" />-->
        <!--            </a-form-model-item>-->
        <!--          </a-col>-->
        <!--          <a-col :span="24">-->
        <!--            <a-form-model-item label="绞车速度 1~8（八个等级）" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="winchspeed">-->
        <!--              <a-input-number v-model="model.winchspeed" placeholder="请输入绞车速度 1~8（八个等级）" style="width: 100%" />-->
        <!--            </a-form-model-item>-->
        <!--          </a-col>-->
        <!--          <a-col :span="24">-->
        <!--            <a-form-model-item label="是否校系 0:不启用；1:启用" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="iscorrection">-->
        <!--              <a-input-number v-model="model.iscorrection" placeholder="请输入是否校系 0:不启用；1:启用" style="width: 100%" />-->
        <!--            </a-form-model-item>-->
        <!--          </a-col>-->
        <!--          <a-col :span="24">-->
        <!--            <a-form-model-item label="波幅校系" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="amplitudecorrection">-->
        <!--              <a-input-number v-model="model.amplitudecorrection" placeholder="请输入波幅校系" style="width: 100%" />-->
        <!--            </a-form-model-item>-->
        <!--          </a-col>-->
        <!--          <a-col :span="24">-->
        <!--            <a-form-model-item label="基本信息id" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="basicinfoid">-->
        <!--              <a-input v-model="model.basicinfoid" placeholder="请输入基本信息id"  ></a-input>-->
        <!--            </a-form-model-item>-->
        <!--          </a-col>-->
        <!--          <a-col :span="24">-->
        <!--            <a-form-model-item label="关联表kongjing_tongdao的guid" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="guid">-->
        <!--              <a-input v-model="model.guid" placeholder="请输入关联表kongjing_tongdao的guid"  ></a-input>-->
        <!--            </a-form-model-item>-->
        <!--          </a-col>-->
        <!--        </a-row>-->
      </a-form-model>
    </j-form-container>
    <a-tabs default-active-key="1">
      <a-tab-pane key="1" tab="通道参数">
        <a-table
          :columns="columns1"
          :dataSource="dataSource1"
          :loading="loading"
          :pagination="ipagination1"
          class="j-table-force-nowrap"
          @change="handleTableChange1"
          :customRow="handleClick"
          :rowClassName="setRowClassName"
        >
        </a-table>
      </a-tab-pane>
      <a-tab-pane key="2" tab="孔径通道采样数据">
        <a-table
          :columns="columns2"
          :dataSource="dataSource2"
          :loading="loading"
          :pagination="ipagination2"
          class="j-table-force-nowrap"
          @change="handleTableChange2"
          :customRow="handleClick"
          :rowClassName="setRowClassName"
        >
        </a-table>
      </a-tab-pane>
      <a-tab-pane key="3" tab="孔径分析数据">
        <a-table
          :columns="columns3"
          :dataSource="dataSource3"
          :loading="loading"
          :pagination="ipagination3"
          class="j-table-force-nowrap"
          @change="handleTableChange3"
          :customRow="handleClick"
          :rowClassName="setRowClassName"
        >
        </a-table>
      </a-tab-pane>
    </a-tabs>
  </a-spin>
</template>

<script>
import { httpAction, getAction } from "@/api/manage";
import { validateDuplicateValue } from "@/utils/util";
import { FormTypes } from "@/utils/JEditableTableUtil";
import { JeecgListMixin } from "@/mixins/JeecgListMixin";
import { mixinDevice } from "@/utils/mixin";

export default {
  name: "KongjingBasicinfoForm",
  // mixins:[JeecgListMixin, mixinDevice],
  components: {},
  props: {
    //表单禁用
    disabled: {
      type: Boolean,
      default: false,
      required: false,
    },
  },
  data() {
    return {
      loading: false,
      dataSource1: [],
      columns1: [
        //通道参数	通道号	状态	中心距(mm)	增益	延时(us)
        {
          title: "通道号",
          dataIndex: "channelnum_dictText",
        },
        {
          title: "状态",
          dataIndex: "status_dictText",
        },
        {
          title: "中心距(mm)",
          dataIndex: "centerdistance",
          type: FormTypes.normal,
        },
        {
          title: "增益",
          dataIndex: "gain",
          type: FormTypes.normal,
        },
        {
          title: "延时(us)",
          dataIndex: "delay",
          type: FormTypes.normal,
        },
        {
          title: "校零时间(us)",
          dataIndex: "zerotime",
          type: FormTypes.normal,
        },
      ],
      ipagination1: {
        current: 1,
        pageSize: 10,
        pageSizeOptions: ["5", "10", "20", "30"],
        showTotal: (total, range) => {
          return range[0] + "-" + range[1] + " 共" + total + "条";
        },
        showQuickJumper: true,
        showSizeChanger: true,
        total: 0,
      },
      dataSource2: [],
      columns2: [
        //孔径通道采样数据	通道号	深度(m)	声时(us)	泥浆声速(km/s)	波幅	方位角	半径(mm)	增益 延时(us) 增强声时(us) 修正距离(mm) 修正方向(us) 噪声档位 测点时间(s) 波形数值
        {
          title: "通道号",
          dataIndex: "passway_dictText",
        },
        {
          title: "深度(m)",
          dataIndex: "depth",
        },
        {
          title: "声时(us)",
          dataIndex: "soundtime",
          type: FormTypes.normal,
        },
        {
          title: "泥浆声速(km/s)",
          dataIndex: "soundspeed",
          type: FormTypes.normal,
        },
        {
          title: "波幅",
          dataIndex: "amplitude",
          type: FormTypes.normal,
        },
        {
          title: "方位角",
          dataIndex: "azimuth",
          type: FormTypes.normal,
        },
        {
          title: "半径(mm)",
          dataIndex: "radius",
          type: FormTypes.normal,
        },
        {
          title: "增益",
          dataIndex: "gain",
          type: FormTypes.normal,
        },
        {
          title: "延时(us)",
          dataIndex: "delay",
          type: FormTypes.normal,
        },
        {
          title: "增强声时(us)",
          dataIndex: "enhancetime",
          type: FormTypes.normal,
        },
        {
          title: "修正距离(mm)",
          dataIndex: "correctiondistance",
          type: FormTypes.normal,
        },
        {
          title: "修正方向(us)",
          dataIndex: "correctiondirection_dictText",
          type: FormTypes.normal,
        },
        {
          title: "噪声档位",
          dataIndex: "adjustinggear",
          type: FormTypes.normal,
        },
        {
          title: "测点时间(s)",
          dataIndex: "testtime",
          type: FormTypes.normal,
        },
        {
          title: "波形数值",
          dataIndex: "wavedata",
          type: FormTypes.normal,
        },
      ],
      ipagination2: {
        current: 1,
        pageSize: 10,
        pageSizeOptions: ["5", "10", "20", "30"],
        showTotal: (total, range) => {
          return range[0] + "-" + range[1] + " 共" + total + "条";
        },
        showQuickJumper: true,
        showSizeChanger: true,
        total: 0,
      },
      dataSource3: [],
      columns3: [
        //孔径分析数据	设计井深(m) 设计孔径(mm)	最大孔径(mm)	最小孔径(mm)	平均孔径(mm)	偏心距(mm)	沉渣厚度(m) 垂直度(%)
        {
          title: "设计井深(m)",
          dataIndex: "designdepth",
        },
        {
          title: "设计孔径(mm)",
          dataIndex: "designdiameter",
        },
        {
          title: "最大孔径(mm)",
          dataIndex: "maxdiameter",
          type: FormTypes.normal,
        },
        {
          title: "最小孔径(mm)",
          dataIndex: "mindiameter",
          type: FormTypes.normal,
        },
        {
          title: "平均孔径(mm)",
          dataIndex: "avgdiameter",
          type: FormTypes.normal,
        },
        {
          title: "偏心距(mm)",
          dataIndex: "eccentricity",
          type: FormTypes.normal,
        },
        {
          title: "沉渣厚度(m)",
          dataIndex: "thickness",
          type: FormTypes.normal,
        },
        {
          title: "垂直度(%)",
          dataIndex: "verticality",
          type: FormTypes.normal,
        },
      ],
      ipagination3: {
        current: 1,
        pageSize: 10,
        pageSizeOptions: ["5", "10", "20", "30"],
        showTotal: (total, range) => {
          return range[0] + "-" + range[1] + " 共" + total + "条";
        },
        showQuickJumper: true,
        showSizeChanger: true,
        total: 0,
      },
      model: {},
      labelCol: {
        xs: { span: 24 },
        sm: { span: 5 },
      },
      wrapperCol: {
        xs: { span: 24 },
        sm: { span: 16 },
      },
      confirmLoading: false,
      validatorRules: {},
      url: {
        add: "/kongjingbasicinfo/kongjingBasicinfo/add",
        edit: "/kongjingbasicinfo/kongjingBasicinfo/edit",
        queryById: "/kongjingbasicinfo/kongjingBasicinfo/queryById",
        list: "/kongjingtongdao/kongjingTongdao/list",
        list1: "/kongjingpointsdata/kongjingPointsdata/list",
        list2: "/kongjinganalysisdata/kongjingAnalysisdata/list",
      },
    };
  },
  computed: {
    formDisabled() {
      return this.disabled;
    },
  },
  created() {
    //备份model原始值
    // this.modelDefault = JSON.parse(JSON.stringify(this.model));
    // this.getdataSource1();
    // this.getdataSource2();
    // this.getdataSource3();
  },
  methods: {
    getdataSource1: function () {
      this.dataSource1 = [];
      let params = {
        guid: this.model.guid,
        column: "id",
        order: "desc",
        pageSize: this.ipagination1.pageSize,
        pageNo: this.ipagination1.current,
      };
      getAction(this.url.list, params).then((res) => {
        if (res.success) {
          this.dataSource1 = res.result.records;
          this.ipagination1.total=res.result.total;
          console.log(this.dataSource1, "通道参数");
        } else {
          this.$message.error("暂无通道参数！");
        }
      });
    },
    getdataSource2: function () {
      this.dataSource2 = [];
      let params = {
        basicinfoid: this.model.basicinfoid,
        column: "id",
        order: "desc",
        pageSize: this.ipagination2.pageSize,
        pageNo: this.ipagination2.current,
      };
      getAction(this.url.list1, params).then((res) => {
        if (res.success) {
          this.dataSource2 = res.result.records;
          this.ipagination2.total=res.result.total;
          console.log(this.dataSource2, "孔径通道采样数据");
        } else {
          this.$message.error("暂无孔径通道采样数据！");
        }
      });
    },
    getdataSource3: function () {
      this.dataSource3 = [];
      let params = {
        basicinfoid: this.model.basicinfoid,
        column: "id",
        order: "desc",
        pageSize: this.ipagination3.pageSize,
        pageNo: this.ipagination3.current,
      };
      getAction(this.url.list2, params).then((res) => {
        if (res.success) {
          this.dataSource3 = res.result.records;
          this.ipagination3.total=res.result.total;
          console.log(this.dataSource3, "孔径分析数据");
        } else {
          this.$message.error("暂无孔径分析数据！");
        }
      });
    },
    handleTableChange1(pagination) {
      this.ipagination1.current = pagination.current;
      this.ipagination1.pageSize = pagination.pageSize;
      this.getdataSource1();
    },
    handleTableChange2(pagination) {
      this.ipagination2.current = pagination.current;
      this.ipagination2.pageSize = pagination.pageSize;
      this.getdataSource2();
    },
    handleTableChange3(pagination) {
      this.ipagination3.current = pagination.current;
      this.ipagination3.pageSize = pagination.pageSize;
      this.getdataSource3();
    },
    add() {
      this.edit(this.modelDefault);
    },
    edit(record) {
      this.model = Object.assign({}, record);
      this.visible = true;
      this.modelDefault = JSON.parse(JSON.stringify(this.model));
      this.getdataSource1();
      this.getdataSource2();
      this.getdataSource3();
    },
    submitForm() {
      const that = this;
      // 触发表单验证
      this.$refs.form.validate((valid) => {
        if (valid) {
          that.confirmLoading = true;
          let httpurl = "";
          let method = "";
          if (!this.model.id) {
            httpurl += this.url.add;
            method = "post";
          } else {
            httpurl += this.url.edit;
            method = "put";
          }
          httpAction(httpurl, this.model, method)
            .then((res) => {
              if (res.success) {
                that.$message.success(res.message);
                that.$emit("ok");
              } else {
                that.$message.warning(res.message);
              }
            })
            .finally(() => {
              that.confirmLoading = false;
            });
        }
      });
    },
  },
};
</script>
