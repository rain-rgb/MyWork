<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="设备名称">
              <j-search-select-tag placeholder="请选择设备名称" v-model="queryParam.shebeino" :dictOptions="dictOption">
              </j-search-select-tag>
              {{ selectValue }}
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="评级">
              <j-search-select-tag placeholder="请选择评级" v-model="queryParam.level" :dictOptions="dictOption1"/>
            </a-form-item>
          </a-col>
          <a-col :xl="8" :lg="7" :md="8" :sm="24">
            <a-form-item label="检测部位">
              <a-input placeholder="请输入检测部位" v-model="queryParam.detloc"></a-input>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="检测日期范围">
              <j-date placeholder="开始检测日期" v-model="queryParam.testpiledate_begin" :showTime="true"
                      dateFormat="YYYY-MM-DD"/>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="">
              <j-date placeholder="结束检测日期" v-model="queryParam.testpiledate_end" :showTime="true"
                      dateFormat="YYYY-MM-DD"/>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
              <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
              <a-button type="primary" @click="searchReset" icon="reload" style="margin-left: 8px">重置</a-button>
              <a @click="handleToggleSearch" style="margin-left: 8px">
                {{ toggleSearchStatus ? '收起' : '展开' }}
                <a-icon :type="toggleSearchStatus ? 'up' : 'down'"/>
              </a>
            </span>
          </a-col>
        </a-row>
      </a-form>
    </div>
    <!-- 查询区域-END -->

    <!-- 操作按钮区域 -->
    <div class="table-operator">
      <a-button @click="handleAdd" type="primary" v-has="'kjbasic:add'" icon="plus">新增</a-button>
      <a-button type="primary" icon="download" v-has="'kjbasic:dc'" @click="handleExportXls('孔径基本信息数据')">导出</a-button>
      <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl"
                @change="handleImportExcel">
        <a-button type="primary" v-has="'kjbasic:dr'" icon="import">导入</a-button>
      </a-upload>
      <!-- 高级查询区域 -->
      <!--      <j-super-query :fieldList="superFieldList" ref="superQueryModal" @handleSuperQuery="handleSuperQuery"></j-super-query>-->
      <!--      <a-dropdown v-if="selectedRowKeys.length > 0">-->
      <!--        <a-menu slot="overlay">-->
      <!--          <a-menu-item key="1" @click="batchDel"><a-icon type="delete"/>删除</a-menu-item>-->
      <!--        </a-menu>-->
      <!--        <a-button style="margin-left: 8px"> 批量操作 <a-icon type="down" /></a-button>-->
      <!--      </a-dropdown>-->
    </div>

    <!-- table区域-begin -->
    <div>
      <div class="ant-alert ant-alert-info" style="margin-bottom: 16px;">
        <i class="anticon anticon-info-circle ant-alert-icon"></i> 已选择 <a
        style="font-weight: 600">{{ selectedRowKeys.length }}</a>项
        <a style="margin-left: 24px" @click="onClearSelected">清空</a>
      </div>

      <a-table
        ref="table"
        size="middle"
        :scroll="{x:true}"
        bordered
        rowKey="id"
        :columns="columns"
        :dataSource="dataSource"
        :pagination="ipagination"
        :loading="loading"
        :rowSelection="{selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"
        class="j-table-force-nowrap"
        @change="handleTableChange"
        :customRow="handleClick"
        :rowClassName="setRowClassName"
      >

        <span slot="density" slot-scope="density, record">
          <a-tag color="orange" v-if="record.density === 0">未启动</a-tag>
          <a-tag color="green" v-if="record.density >0">启动</a-tag>
        </span>

        <span slot="level" slot-scope="level, record">
           <a-tag v-if="record.level === 0">未评级</a-tag>
          <a-tag v-if="record.level === 1">Ⅰ类</a-tag>
          <a-tag v-if="record.level === 2">Ⅱ类</a-tag>
          <a-tag v-if="record.level === 3">Ⅲ类</a-tag>
        </span>

        <template slot="htmlSlot" slot-scope="text">
          <div v-html="text"></div>
        </template>
        <template slot="imgSlot" slot-scope="text">
          <span v-if="!text" style="font-size: 12px;font-style: italic;">无图片</span>
          <img v-else :src="getImgView(text)" height="25px" alt=""
               style="max-width:80px;font-size: 12px;font-style: italic;"/>
        </template>
        <template slot="fileSlot" slot-scope="text">
          <span v-if="!text" style="font-size: 12px;font-style: italic;">无文件</span>
          <a-button
            v-else
            :ghost="true"
            type="primary"
            icon="download"
            size="small"
            @click="downloadFile(text)">
            下载
          </a-button>
        </template>

        <span slot="action" slot-scope="text, record">
          <a-dropdown>
            <a @click="handlePreview(record)">报告预览</a>
          </a-dropdown>
          <a-divider type="vertical"/>
          <a v-has="'kjbasic:edit'" @click="handleEdit(record)">编辑</a>

          <!--          <a-divider type="vertical" />-->
          <a-dropdown>
            <a class="ant-dropdown-link">更多 <a-icon type="down"/></a>
            <a-menu slot="overlay">
              <a-menu-item>
                <a @click="handleDetail(record)">详情</a>
              </a-menu-item>
              <a-menu-item>
                <a @click="handleFile(record.id)">报告附件上传</a>
              </a-menu-item>
              <a-menu-item v-has="'kjbasic:del'">
                <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">
                  <a>删除</a>
                </a-popconfirm>
              </a-menu-item>
            </a-menu>
          </a-dropdown>
        </span>

      </a-table>
    </div>

    <kongjing-basicinfo-modal ref="modalForm" @ok="modalFormOk"></kongjing-basicinfo-modal>
    <kong-jing-file ref="file" :id="id" @change="change"></kong-jing-file>
  </a-card>
</template>

<script>

import '@/assets/less/TableExpand.less'
import { mixinDevice } from '@/utils/mixin'
import { JeecgListMixin } from '@/mixins/JeecgListMixin'
import KongjingBasicinfoModal from './modules/KongjingBasicinfoModal'
import { handertoken } from '@/mixins/getHanderToken'
import { usershebeiList } from '@api/api'
import Vue from 'vue'
import KongJingFile from '@views/syj/kongjing/modules/KongJingFile'
import { base64Encode } from '@api/kkfileView'

export default {
  name: 'KongjingBasicinfoList',
  mixins: [JeecgListMixin, mixinDevice, handertoken],
  components: {
    KongJingFile,
    KongjingBasicinfoModal
  },
  data() {
    return {
      id: 0,
      selectValue: '',
      asyncSelectValue: '',
      dictOption: [],
      dictOption1: [
        {
          text:'未评级',
          value:'0'
        },{
        text:'Ⅰ级',
        value:'1'
      },{
        text:'Ⅱ级',
        value:'2'
      },{
        text:'Ⅲ级',
        value:'3'
      }],
      description: '孔径基本信息数据管理页面',
      // 表头
      columns: [
        {
          title: '序号',
          dataIndex: '',
          key: 'rowIndex',
          width: 60,
          align: 'center',
          customRender: function (t, r, index) {
            return parseInt(index) + 1
          }
        },
        // {
        //   title:'设备商标识',
        //   align:"center",
        //   dataIndex: 'vendorid'
        // },
        {
          title: '设备名称',
          align: 'center',
          dataIndex: 'shebeino_dictText'
        },
        {
          title: '检测部位',
          align: 'center',
          dataIndex: 'detloc'
        },
        {
          title: '测试仪编号',
          align: 'center',
          dataIndex: 'machineid'
        },
        {
          title: '试桩编号',
          align: 'center',
          dataIndex: 'pileno'
        },
        {
          title: '检测日期',
          align: 'center',
          dataIndex: 'testpiledate'
        },
        {
          title: '检测时间',
          align: 'center',
          dataIndex: 'testpiletime'
        },
        {
          title: '上传时间',
          align: 'center',
          dataIndex: 'creattime'
        },
        {
          title: '检测人员',
          align: 'center',
          dataIndex: 'worker'
        },
        {
          title: '检测单位',
          align: 'center',
          dataIndex: 'testunit'
        },
        {
          title: '施工单位',
          align: 'center',
          dataIndex: 'constructionunit'
        },
        {
          title: '工程名称',
          align: 'center',
          dataIndex: 'projectname'
        },
        {
          title: '孔深(m)',
          align: 'center',
          dataIndex: 'depth'
        },
        {
          title: '孔径(mm)',
          align: 'center',
          dataIndex: 'diameter'
        },
        {
          title: '始测深度(m)',
          align: 'center',
          dataIndex: 'begindepth'
        },
        {
          title: '标高(m)',
          align: 'center',
          dataIndex: 'height'
        },
        {
          title: '管数',
          align: 'center',
          dataIndex: 'pipenums'
        },
        {
          title: '移距(mm)',
          align: 'center',
          dataIndex: 'shift'
        },
        {
          title: '偏移角',
          align: 'center',
          dataIndex: 'angle'
        },
        {
          title: '采集方式',
          align: 'center',
          dataIndex: 'method_dictText'
        },
        {
          title: '数字滤波',
          align: 'center',
          dataIndex: 'filter_dictText'
        },
        {
          title: '泥浆补偿',
          align: 'center',
          dataIndex: 'density',
          scopedSlots: { customRender: 'density' },
        },
        {
          title: '波形增强',
          align: 'center',
          dataIndex: 'waveenhance_dictText'
        },
        {
          title: '判读算法',
          align: 'center',
          dataIndex: 'algorithm_dictText'
        },
        {
          title: '幅值参数 ',
          align: 'center',
          dataIndex: 'amplitude'
        },
        {
          title: '方差',
          align: 'center',
          dataIndex: 'variance'
        },
        {
          title: '主频',
          align: 'center',
          dataIndex: 'frequency'
        },
        {
          title: '起点',
          align: 'center',
          dataIndex: 'origin_dictText'
        },
        {
          title: '首波方式',
          align: 'center',
          dataIndex: 'mode_dictText'
        },
        {
          title: '检测标准',
          align: 'center',
          dataIndex: 'standard'
        },
        {
          title: '采集方向',
          align: 'center',
          dataIndex: 'direction_dictText'
        },
        {
          title: '罗盘修正',
          align: 'center',
          dataIndex: 'compass_dictText'
        },
        {
          title: '罗盘基准角',
          align: 'center',
          dataIndex: 'compassangle'
        },
        {
          title: '采样间隔(us)',
          align: 'center',
          dataIndex: 'sampleinterval'
        },
        {
          title: '采样长度',
          align: 'center',
          dataIndex: 'samplelength'
        },
        {
          title: '高通(kHz)',
          align: 'center',
          dataIndex: 'highfilter'
        },
        {
          title: '低通(kHz)',
          align: 'center',
          dataIndex: 'lowfilter'
        },
        {
          title: '发射脉宽(us)',
          align: 'center',
          dataIndex: 'pulsewidth'
        },
        {
          title: '发射电压',
          align: 'center',
          dataIndex: 'voltage_dictText'
        },
        {
          title: ' 滑轮直径(mm)',
          align: 'center',
          dataIndex: 'pulleydiameter'
        },
        {
          title: '电缆直径(mm)',
          align: 'center',
          dataIndex: 'cablediameter'
        },
        {
          title: '绞车速度',
          align: 'center',
          dataIndex: 'winchspeed'
        },
        {
          title: '是否校系',
          align: 'center',
          dataIndex: 'iscorrection_dictText'
        },
        {
          title: '波幅校系',
          align: 'center',
          dataIndex: 'amplitudecorrection'
        },
        {
          title: '评级',
          align: 'center',
          fixed: 'right',
          dataIndex: 'level',
          scopedSlots: { customRender: 'level' },
        },
        // {
        //   title:'基本信息id',
        //   align:"center",
        //   dataIndex: 'basicinfoid'
        // },
        // {
        //   title:'关联表kongjing_tongdao的guid',
        //   align:"center",
        //   dataIndex: 'guid'
        // },
        {
          title: '操作',
          dataIndex: 'action',
          align: 'center',
          fixed: 'right',
          width: 147,
          scopedSlots: { customRender: 'action' }
        }
      ],
      url: {
        list: '/kongjingbasicinfo/kongjingBasicinfo/list',
        delete: '/kongjingbasicinfo/kongjingBasicinfo/delete',
        deleteBatch: '/kongjingbasicinfo/kongjingBasicinfo/deleteBatch',
        exportXlsUrl: '/kongjingbasicinfo/kongjingBasicinfo/exportXls',
        importExcelUrl: 'kongjingbasicinfo/kongjingBasicinfo/importExcel',

      },
      dictOptions: {},
      superFieldList: [],
    }
  },
  created() {
    this.getToken()
    this.getSuperFieldList()
    this.shebeiList()
  },
  computed: {
    importExcelUrl: function () {
      return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`
    },
  },
  methods: {
    shebeiList: function () {
      var sys_depart_orgcode = Vue.ls.get('SYS_DEPART_ORGCODE')
      usershebeiList({
        sys_depart_orgcode: sys_depart_orgcode,
        sbtypes: '56'
      }).then(res => {
        this.dictOption = []
        let result = res.result
        result.forEach(re => {
          this.dictOption.push({ text: re.sbname, value: re.sbjno })
        })
        //console.log(res)
      })
    },
    handlePreview(record) {
      if (record && record.file) {
        let url = window._CONFIG['onlinePreviewDomainURL'] + '?url=' + encodeURIComponent(base64Encode(record.file))
        window.open(url, '_blank')
      } else {
        this.$message.info('未上传相关附件！')
      }
    },
    handleFile: function (id) {
      this.$refs.file.showModal()
      this.id = id
    },
    change() {
      this.modalFormOk()
    },
    initDictConfig() {
    },
    getSuperFieldList() {
      let fieldList = []
      fieldList.push({ type: 'string', value: 'vendorid', text: '设备商标识', dictCode: '' })
      fieldList.push({ type: 'string', value: 'serialno', text: '流水号', dictCode: '' })
      fieldList.push({ type: 'string', value: 'machineid', text: '测试仪编号', dictCode: '' })
      fieldList.push({ type: 'string', value: 'pileno', text: '试桩编号', dictCode: '' })
      fieldList.push({ type: 'string', value: 'testpiledate', text: '检测日期(格式：yyyy-MM-dd)', dictCode: '' })
      fieldList.push({ type: 'string', value: 'testpiletime', text: '检测时间(格式：HH:mm:ss)', dictCode: '' })
      fieldList.push({ type: 'string', value: 'worker', text: '检测人员', dictCode: '' })
      fieldList.push({ type: 'string', value: 'testunit', text: '检测单位', dictCode: '' })
      fieldList.push({ type: 'string', value: 'constructionunit', text: '施工单位', dictCode: '' })
      fieldList.push({ type: 'string', value: 'projectname', text: '工程名称', dictCode: '' })
      fieldList.push({ type: 'double', value: 'depth', text: '孔深(单位：m)', dictCode: '' })
      fieldList.push({ type: 'int', value: 'diameter', text: '孔径(单位：mm)', dictCode: '' })
      fieldList.push({ type: 'double', value: 'begindepth', text: '始测深度(单位：m)', dictCode: '' })
      fieldList.push({ type: 'double', value: 'height', text: '标高(单位：m)', dictCode: '' })
      fieldList.push({ type: 'int', value: 'pipenums', text: '管数', dictCode: '' })
      fieldList.push({ type: 'int', value: 'shift', text: '移距(单位：mm)', dictCode: '' })
      fieldList.push({ type: 'int', value: 'angle', text: '偏移角', dictCode: '' })
      fieldList.push({
        type: 'int',
        value: 'method',
        text: '采集方式  0：自动提升 1：手动提升 2：手动(4s/m) 3：手动(6s/m) 4：手动(8s/m)',
        dictCode: ''
      })
      fieldList.push({ type: 'int', value: 'filter', text: '数字滤波 0：未启动 1：启动', dictCode: '' })
      fieldList.push({ type: 'int', value: 'density', text: '泥浆补偿 泥浆密度补偿 0:未启动 大于0：启动', dictCode: '' })
      fieldList.push({ type: 'int', value: 'waveenhance', text: '波形增强 0：未启动 1：启动', dictCode: '' })
      fieldList.push({ type: 'int', value: 'algorithm', text: '判读算法 0：自动判读 1：幅值判读 2：方差判读', dictCode: '' })
      fieldList.push({ type: 'double', value: 'amplitude', text: '幅值参数 ', dictCode: '' })
      fieldList.push({ type: 'double', value: 'variance', text: '方差', dictCode: '' })
      fieldList.push({ type: 'double', value: 'frequency', text: '主频', dictCode: '' })
      fieldList.push({ type: 'int', value: 'origin', text: '起点 0：负 1：正', dictCode: '' })
      fieldList.push({ type: 'int', value: 'mode', text: '首波方式 0：负 1：正', dictCode: '' })
      fieldList.push({ type: 'string', value: 'standard', text: '检测标准 公路、铁路、 建筑、广东规范等', dictCode: '' })
      fieldList.push({ type: 'int', value: 'direction', text: '采集方向 0：下放 1：上拉', dictCode: '' })
      fieldList.push({ type: 'int', value: 'compass', text: '罗盘修正 0：未修正 1：已修 正', dictCode: '' })
      fieldList.push({ type: 'double', value: 'compassangle', text: '罗盘基准角 范围[0-360]', dictCode: '' })
      fieldList.push({ type: 'double', value: 'sampleinterval', text: '采样间隔 单位us', dictCode: '' })
      fieldList.push({ type: 'int', value: 'samplelength', text: '采样长度 1024、2048', dictCode: '' })
      fieldList.push({ type: 'double', value: 'highfilter', text: '高通 单位kHz', dictCode: '' })
      fieldList.push({ type: 'double', value: 'lowfilter', text: '低通 单位kHz', dictCode: '' })
      fieldList.push({ type: 'int', value: 'pulsewidth', text: '发射脉宽 单位us', dictCode: '' })
      fieldList.push({ type: 'int', value: 'voltage', text: '发射电压 0：低压 1：高压', dictCode: '' })
      fieldList.push({ type: 'double', value: 'pulleydiameter', text: ' 滑轮直径 单位mm', dictCode: '' })
      fieldList.push({ type: 'double', value: 'cablediameter', text: '电缆直径 单位mm', dictCode: '' })
      fieldList.push({ type: 'int', value: 'winchspeed', text: '绞车速度 1~8（八个等级）', dictCode: '' })
      fieldList.push({ type: 'int', value: 'iscorrection', text: '是否校系 0:不启用；1:启用', dictCode: '' })
      fieldList.push({ type: 'double', value: 'amplitudecorrection', text: '波幅校系', dictCode: '' })
      fieldList.push({ type: 'string', value: 'basicinfoid', text: '基本信息id', dictCode: '' })
      fieldList.push({ type: 'string', value: 'guid', text: '关联表kongjing_tongdao的guid', dictCode: '' })
      this.superFieldList = fieldList
    }
  }
}
</script>
<style scoped>
@import '~@assets/less/common.less';
</style>