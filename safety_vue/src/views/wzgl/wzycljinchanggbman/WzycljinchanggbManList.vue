<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="设备名称">
              <j-search-select-tag placeholder="请选择设备名称" v-model="queryParam.shebeibianhao" :dictOptions="dictOption">
              </j-search-select-tag>
              {{ selectValue }}
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="料仓名称">
              <j-search-select-tag placeholder="请选择料仓名称" v-model="queryParam.lcbianhao" :dictOptions="dictOption1">
              </j-search-select-tag>
              {{ selectValue1 }}
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="材料类型">
              <j-dict-select-tag placeholder="请选择材料类型" v-model="queryParam.nodetypeCP"
                                 dictCode="nodetypeCP"></j-dict-select-tag>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="供应商">
              <j-search-select-tag placeholder="请选择供应商名称" v-model="queryParam.gongyingshangdanweibianma"
                                   :dictOptions="dictOption3">
              </j-search-select-tag>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row :gutter="24">
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="时间范围">
              <j-date placeholder="进场时间" v-model="queryParam.jinchangshijian_begin" :showTime="true"
                      dateFormat="YYYY-MM-DD HH:mm:ss"/>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="结束时间">
              <j-date placeholder="出场时间" v-model="queryParam.chuchangshijian_begin" :showTime="true"
                      dateFormat="YYYY-MM-DD HH:mm:ss"/>
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
      <a-button @click="handleAdd" type="primary" v-has="'wzycljinchanggbman:add'" icon="plus">新增</a-button>
      <a-button type="primary" v-has="'wzycljinchanggbman:dc'" icon="download" @click="handleExportXls('人工登记过磅信息表')">
        导出
      </a-button>
      <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl"
                @change="handleImportExcel">
        <a-button type="primary" v-has="'wzycljinchanggbman:drs'" icon="import">导入</a-button>
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

        <span slot="tags" slot-scope="status, record">
          <a-tag color="green" v-if="record.shifouhege == '1'">合格</a-tag>
          <a-tag color="red" v-if="record.shifouhege == '0'">不合格</a-tag>
          <a-tag color="green" v-if="record.shifouhege == '合格'">合格</a-tag>
          <a-tag color="red" v-if="record.shifouhege == '不合格'">不合格</a-tag>
       </span>
        <span slot="isdel" slot-scope="status, record">
          <a-tag color="green" v-if="record.isdel == '0'">新增</a-tag>
          <a-tag color="green" v-if="record.isdel == '1'">已编辑</a-tag>
          <a-tag color="red" v-if="record.isdel == '2'">已删除</a-tag>
       </span>
        <span slot="tags1" slot-scope="tags1,record">
         <a-tag color="blue" v-if="record.taizhangtj == 0 || record.taizhangid_dictText == 0 ">{{record.taizhangtj_dictText}}</a-tag>
         <a-tag color="green" v-else-if="record.taizhangtj == 1 && record.taizhangid_dictText == 1">{{record.taizhangtj_dictText}}</a-tag>
         <a-tag color="red" v-else>{{record.taizhangtj_dictText}}</a-tag>
        </span>

        <span slot="action" slot-scope="text, record">

            <a @click="handlePreview(record)">质保单预览</a>
                    <a-divider type="vertical" />
                    <a-dropdown>
                      <a class="ant-dropdown-link">更多 <a-icon type="down" /></a>
                      <a-menu slot="overlay">
                         <a-menu-item>
                        <a @click="handleEdit(record)">编辑</a>
                        </a-menu-item>
<!--                        <a-menu-item>-->
<!--                          <a @click="handleDetail(record)">详情</a>-->
<!--                        </a-menu-item>-->
                        <a-menu-item>
                          <a-popconfirm title="确定推送试验平台吗?" @confirm="() => handlePushsy(record)">
                            <a>推送</a>
                          </a-popconfirm>
                        </a-menu-item>
                      </a-menu>
                    </a-dropdown>
        </span>

      </a-table>
    </div>

    <wzycljinchanggb-man-modal ref="modalForm" @ok="modalFormOk"></wzycljinchanggb-man-modal>
  </a-card>
</template>

<script>

import '@/assets/less/TableExpand.less'
import { mixinDevice } from '@/utils/mixin'
import { JeecgListMixin } from '@/mixins/JeecgListMixin'
import WzycljinchanggbManModal from './modules/WzycljinchanggbManModal'
import JSuperQuery from '@/components/jeecg/JSuperQuery.vue'
import { handertoken } from '@/mixins/getHanderToken'
import Vue from 'vue'
import { SYS_DEPART_ORGCODE } from '@/store/mutation-types'
import { usershebeiList, userliaocangList, gongyingshangmanList } from '@api/api'
import { base64Encode } from '@api/kkfileView'
import {putAction} from "@api/manage";

export default {
  name: 'WzycljinchanggbManList',
  mixins: [JeecgListMixin, mixinDevice, handertoken],
  components: {
    WzycljinchanggbManModal,
    JSuperQuery,
  },
  data() {
    return {
      selectValue: '',
      selectValue1: '',
      selectValue2: '',
      asyncSelectValue: '',
      dictOption: [],
      dictOption1: [],
      dictOption2: [],
      dictOption3: [],
      description: 'wzycljinchanggb_man管理页面',
      // 表头
      columns: [
        {
          title: '#',
          dataIndex: '',
          key: 'rowIndex',
          width: 60,
          align: 'center',
          customRender: function (t, r, index) {
            return parseInt(index) + 1
          }
        },
        {
          title: '设备名称',
          align: 'center',
          dataIndex: 'shebeibianhao_dictText'
        },
        {
          title: '料仓',
          align: 'center',
          dataIndex: 'lcbianhao_dictText'
        },
        // {
        //   title:'进出材料单',
        //   align:"center",
        //   dataIndex: 'jinchuliaodanno'
        // },

        {
          title: '材料名称',
          align: 'center',
          dataIndex: 'cailiaono_dictText'
        },
        {
          title: '规格型号',
          align: 'center',
          dataIndex: 'cheliangbianhao'
        },
        {
          title: '供应商单位',
          align: 'center',
          dataIndex: 'gongyingshangdanweibianma_dictText'
        },
        {
          title: '检验批号',
          align: 'center',
          dataIndex: 'pici'
        },
        {
          title: '质保单号',
          align: 'center',
          dataIndex: 'yuancaimiaoshu'
        },
        {
          title: '进场时间',
          align: 'center',
          dataIndex: 'jinchangshijian'
        },
        {
          title: '出厂日期',
          align: 'center',
          dataIndex: 'chuchangshijian'
        },

        {
          title: '数量',
          align: 'center',
          dataIndex: 'jingzhong'
        },
        {
          title: '材料单位',
          align: 'center',
          dataIndex: 'guobangleibie'
        },

        {
          title: '存放地点',
          align: 'center',
          dataIndex: 'liaocangid'
        },
        {
          title: '使用部位',
          align: 'center',
          dataIndex: 'houchepai'
        },
        {
          title: '推送状态',
          align: 'center',
          dataIndex: 'remark',
          scopedSlots: { customRender: 'tags1' }
        },
        // {
        //   title:'毛重',
        //   align:"center",
        //   dataIndex: 'maozhong'
        // },
        // {
        //   title:'皮重',
        //   align:"center",
        //   dataIndex: 'pizhong'
        // },
        // {
        //   title:'材料数量',
        //   align:"center",
        //   dataIndex: 'jingzhong'
        // },

        // {
        //   title:'扣率',
        //   align:"center",
        //   dataIndex: 'koulv'
        // },
        // {
        //   title:'材料厂家',
        //   align:"center",
        //   dataIndex: 'chengzhongpiancha'
        // },
        // {
        //   title:'材料名称',
        //   align:"center",
        //   dataIndex: 'liaocang_dictText'
        // },
        // {
        //   title:'登记员',
        //   align:"center",
        //   dataIndex: 'sibangyuan'
        // },
        // {
        //   title:'remark',
        //   align:"center",
        //   dataIndex: 'remark'
        // },

        // {
        //   title:'过磅类别(材料单位）',
        //   align:"center",
        //   dataIndex: 'guobangleibie'
        // },
        // {
        //   title:'车辆类型',
        //   align:"center",
        //   dataIndex: 'cheliangleixing'
        // },
        // {
        //   title:'唯一标识',
        //   align:"center",
        //   dataIndex: 'guid'
        // },
        // {
        //   title:'时间戳',
        //   align:"center",
        //   dataIndex: 'ts'
        // },
        // {
        //   title:'状态',
        //   align:"center",
        //   dataIndex: 'isdel',
        //   scopedSlots: { customRender: 'isdel' },
        // },
        // {
        //   title:'status',
        //   align:"center",
        //   dataIndex: 'status'
        // },
        // {
        //   title:'毛重(吨)',
        //   align:"center",
        //   dataIndex: 'maozhongt'
        // },
        // {
        //   title:'皮重(吨)',
        //   align:"center",
        //   dataIndex: 'pizhongt'
        // },
        // {
        //   title:'candi',
        //   align:"center",
        //   dataIndex: 'candi'
        // },
        // {
        //   title:'运输单位',
        //   align:"center",
        //   dataIndex: 'yunshudanwei'
        // },

        // {
        //   title:'isshouliao',
        //   align:"center",
        //   dataIndex: 'isshouliao'
        // },

        // {
        //   title:'是否统计 默认为0:未统计，1:已统计,15:统计出错',
        //   align:"center",
        //   dataIndex: 'istongji'
        // },
        // {
        //   title:'存放地点',
        //   align:"center",
        //   dataIndex: 'liaocangid'
        // },
        // {
        //   title:'serialno',
        //   align:"center",
        //   dataIndex: 'serialno'
        // },
        // {
        //   title:'reason',
        //   align:"center",
        //   dataIndex: 'reason'
        // },
        // {
        //   title:'fileUpload',
        //   align:"center",
        //   dataIndex: 'fileUpload'
        // },
        // {
        //   title:'istaizhangtj',
        //   align:"center",
        //   dataIndex: 'istaizhangtj'
        // },
        // {
        //   title:'送货单',
        //   align:"center",
        //   dataIndex: 'songhuodanpic'
        // },
        // {
        //   title:'是否合格',
        //   align:"center",
        //   dataIndex: 'shifouhege_dictText',
        //   scopedSlots: { customRender: 'tags' },
        // },
        // {
        //   title:'原材描述',
        //   align:"center",
        //   dataIndex: 'yuancaimiaoshu'
        // },
        // {
        //   title:'beizhu',
        //   align:"center",
        //   dataIndex: 'beizhu'
        // },
        // {
        //   title:'jianlipic',
        //   align:"center",
        //   dataIndex: 'jianlipic'
        // },
        // {
        //   title:'sibanpic',
        //   align:"center",
        //   dataIndex: 'sibanpic'
        // },
        // {
        //   title:'jcgkpic',
        //   align:"center",
        //   dataIndex: 'jcgkpic'
        // },
        // {
        //   title:'jccppic',
        //   align:"center",
        //   dataIndex: 'jccppic'
        // },
        // {
        //   title:'jchcppic',
        //   align:"center",
        //   dataIndex: 'jchcppic'
        // },
        // {
        //   title:'jcbfpic',
        //   align:"center",
        //   dataIndex: 'jcbfpic'
        // },
        // {
        //   title:'ccgkpic',
        //   align:"center",
        //   dataIndex: 'ccgkpic'
        // },
        // {
        //   title:'cccppic',
        //   align:"center",
        //   dataIndex: 'cccppic'
        // },
        // {
        //   title:'cchcppic',
        //   align:"center",
        //   dataIndex: 'cchcppic'
        // },
        // {
        //   title:'ccbfpic',
        //   align:"center",
        //   dataIndex: 'ccbfpic'
        // },
        // {
        //   title:'台账统计状态 0;未统计，1:已统计，10:没有料仓配置，15:没有供应商单位，20;没有材料字典，40:异常',
        //   align:"center",
        //   dataIndex: 'taizhangtj'
        // },
        // {
        //   title:'台账id',
        //   align:"center",
        //   dataIndex: 'taizhangid'
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
        list: '/wzycljinchanggbman/wzycljinchanggbMan/list',
        delete: '/wzycljinchanggbman/wzycljinchanggbMan/delete',
        deleteBatch: '/wzycljinchanggbman/wzycljinchanggbMan/deleteBatch',
        exportXlsUrl: '/wzycljinchanggbman/wzycljinchanggbMan/exportXls',
        importExcelUrl: 'wzycljinchanggbman/wzycljinchanggbMan/importExcel',
        pushSyData: "/sys/systems/sysMessages/pushSyDataMan"

      },
      dictOptions: {},
      superFieldList: [],
    }
  },
  created() {
    this.getToken()
    this.getSuperFieldList()
    this.shebeiList()
    this.liaocangList()
    this.gongyingshangData()
  },
  computed: {
    importExcelUrl: function () {
      return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`
    },
  },
  methods: {

    handlePreview(record) {
      if (record.songhuodanpic) {
        if (record.songhuodanpic.indexOf('http') > -1) {
          let url = window._CONFIG['onlinePreviewDomainURL'] + '?url=' + encodeURIComponent(base64Encode(record.songhuodanpic))
          window.open(url, '_blank')
        } else {
          let url = 'http://web.traiot.cn/docs/wz/' + record.songhuodanpic
          window.open(url, '_blank')
        }
        // let url = window._CONFIG['onlinePreviewDomainURL'] + '?url=' + encodeURIComponent(base64Encode("http://web.traiot.cn/docs/wz/"+record.songhuodanpic))

      } else {
        this.$message.info('未上传质保单！')
      }
    },

    shebeiList: function () {
      var sys_depart_orgcode = Vue.ls.get('SYS_DEPART_ORGCODE')
      usershebeiList({
        sys_depart_orgcode: sys_depart_orgcode,
        sbtypes: '63'
      }).then(res => {
        this.dictOption = []
        let result = res.result
        result.forEach(re => {
          this.dictOption.push({ text: re.sbname, value: re.sbjno })
        })
        //console.log(res)
      })
    },
    handlePushsy(record){
      putAction(this.url.pushSyData,record).then(res=>{
        if (res.success) {
          this.loadData();
          this.$message.success(res.message)
          this.$emit('ok');
        } else {
          this.$message.error(res.message)
        }
      })
    },

    liaocangList: function () {
      var sys_depart_orgcode = Vue.ls.get('SYS_DEPART_ORGCODE')
      userliaocangList({
        sys_depart_orgcode: sys_depart_orgcode,
      }).then(res => {
        this.dictOption1 = []
        let result = res.result
        result.forEach(re => {
          this.dictOption1.push({ text: re.name, value: re.guid })
        })
        //console.log(res)
      })
    },
    gongyingshangData: function () {
      var sys_depart_orgcode = Vue.ls.get('SYS_DEPART_ORGCODE')
      gongyingshangmanList({
        sys_depart_orgcode: sys_depart_orgcode,
        pageNo: 1,
        pageSize: 1000
      }).then(res => {
        this.dictOption3 = []
        let result = res.result.records
        result.forEach(re => {
          this.dictOption3.push({ text: re.gongyingshangname, value: re.guid })
        })
        //console.log(res)
      })
    },
    initDictConfig() {
    },
    getSuperFieldList() {
      let fieldList = []
      fieldList.push({ type: 'string', value: 'jinchuliaodanno', text: '进出材料单' })
      fieldList.push({ type: 'string', value: 'cailiaono', text: '材料编号' })
      fieldList.push({ type: 'string', value: 'jinchangshijian', text: '进场时间' })
      fieldList.push({ type: 'string', value: 'chuchangshijian', text: '出场时间' })
      fieldList.push({ type: 'string', value: 'pici', text: '生产批号' })
      fieldList.push({ type: 'string', value: 'cheliangbianhao', text: '规格型号' })
      fieldList.push({ type: 'string', value: 'qianchepai', text: '前车牌' })
      fieldList.push({ type: 'string', value: 'houchepai', text: '后车牌' })
      fieldList.push({ type: 'string', value: 'maozhong', text: '毛重' })
      fieldList.push({ type: 'string', value: 'pizhong', text: '皮重' })
      fieldList.push({ type: 'string', value: 'jingzhong', text: '材料数量' })
      fieldList.push({ type: 'string', value: 'kouzhong', text: '材料单位' })
      fieldList.push({ type: 'string', value: 'koulv', text: 'koulv' })
      fieldList.push({ type: 'string', value: 'chengzhongpiancha', text: '材料厂家' })
      fieldList.push({ type: 'string', value: 'liaocang', text: '材料名称' })
      fieldList.push({ type: 'string', value: 'sibangyuan', text: '司磅员' })
      fieldList.push({ type: 'string', value: 'remark', text: 'remark' })
      fieldList.push({ type: 'string', value: 'shebeibianhao', text: '设备编号' })
      fieldList.push({ type: 'string', value: 'gongyingshangdanweibianma', text: '供应商单位编码' })
      fieldList.push({ type: 'string', value: 'guobangleibie', text: '过磅类别(材料单位）' })
      fieldList.push({ type: 'string', value: 'cheliangleixing', text: '车辆类型' })
      fieldList.push({ type: 'string', value: 'guid', text: '唯一标识' })
      fieldList.push({ type: 'int', value: 'ts', text: '时间戳' })
      fieldList.push({ type: 'string', value: 'isdel', text: '状态0新增1已编辑过2已删除' })
      fieldList.push({ type: 'string', value: 'status', text: 'status' })
      fieldList.push({ type: 'string', value: 'maozhongt', text: '毛重(吨)' })
      fieldList.push({ type: 'string', value: 'pizhongt', text: '皮重(吨)' })
      fieldList.push({ type: 'string', value: 'candi', text: 'candi' })
      fieldList.push({ type: 'string', value: 'yunshudanwei', text: '运输单位' })
      fieldList.push({ type: 'string', value: 'lcbianhao', text: '料仓编号' })
      fieldList.push({ type: 'string', value: 'isshouliao', text: 'isshouliao' })
      fieldList.push({ type: 'string', value: 'jingzhongt', text: '净重(吨)' })
      fieldList.push({ type: 'int', value: 'istongji', text: '是否统计 默认为0:未统计，1:已统计,15:统计出错' })
      fieldList.push({ type: 'string', value: 'liaocangid', text: '存放地点' })
      fieldList.push({ type: 'string', value: 'serialno', text: 'serialno' })
      fieldList.push({ type: 'string', value: 'reason', text: 'reason' })
      fieldList.push({ type: 'string', value: 'fileUpload', text: 'fileUpload' })
      fieldList.push({ type: 'string', value: 'istaizhangtj', text: 'istaizhangtj' })
      fieldList.push({ type: 'string', value: 'songhuodanpic', text: '送货单' })
      fieldList.push({ type: 'string', value: 'shifouhege', text: '是否合格' })
      fieldList.push({ type: 'string', value: 'yuancaimiaoshu', text: '原材描述' })
      fieldList.push({ type: 'string', value: 'beizhu', text: 'beizhu' })
      fieldList.push({ type: 'string', value: 'jianlipic', text: 'jianlipic' })
      fieldList.push({ type: 'string', value: 'sibanpic', text: 'sibanpic' })
      fieldList.push({ type: 'string', value: 'jcgkpic', text: 'jcgkpic' })
      fieldList.push({ type: 'string', value: 'jccppic', text: 'jccppic' })
      fieldList.push({ type: 'string', value: 'jchcppic', text: 'jchcppic' })
      fieldList.push({ type: 'string', value: 'jcbfpic', text: 'jcbfpic' })
      fieldList.push({ type: 'string', value: 'ccgkpic', text: 'ccgkpic' })
      fieldList.push({ type: 'string', value: 'cccppic', text: 'cccppic' })
      fieldList.push({ type: 'string', value: 'cchcppic', text: 'cchcppic' })
      fieldList.push({ type: 'string', value: 'ccbfpic', text: 'ccbfpic' })
      fieldList.push({
        type: 'int',
        value: 'taizhangtj',
        text: '台账统计状态 0;未统计，1:已统计，10:没有料仓配置，15:没有供应商单位，20;没有材料字典，40:异常'
      })
      fieldList.push({ type: 'int', value: 'taizhangid', text: '台账id' })
      this.superFieldList = fieldList
    }
  }
}
</script>
<style scoped>
@import '~@assets/less/common.less';
</style>