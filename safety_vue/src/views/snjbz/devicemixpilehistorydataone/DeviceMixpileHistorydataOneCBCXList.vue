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
            <a-form-item label="处置状态">
              <j-dict-select-tag placeholder="请选择处置状态" v-model="queryParam.handlestate"
                                 dictCode="handlestate"></j-dict-select-tag>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="时间范围">
              <j-date placeholder="开始时间" v-model="queryParam.pileTime_begin" :showTime="true"
                      dateFormat="YYYY-MM-DD HH:mm:ss"/>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="">
              <j-date placeholder="结束时间" v-model="queryParam.pileTime_end" :showTime="true"
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
      <a-button @click="handleAdd" v-has="'mixpilehistoryonecbcx:add'" type="primary" icon="plus">新增</a-button>
      <a-button type="primary" icon="download" v-has="'mixpilehistoryonecbcx:dc'"
                @click="handleExportXls2h('搅拌桩成桩记录信息表')">导出
      </a-button>
      <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl"
                @change="handleImportExcel">
        <a-button type="primary" v-has="'mixpilehistoryonecbcx:dr'" icon="import">导入</a-button>
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
        <span slot="pileNo" slot-scope="shebeino,pileNo,record">
          <a-tag>{{ record.shebeino, record.pileNo, record.pileDesigndep }}</a-tag>
        </span>
        <template slot="shebeino_dictText" slot-scope="shebeino_dictText,record">
          <div style="width: 100px;white-space: break-spaces;">{{ shebeino_dictText }}</div>
        </template>
        <template slot="pileMileage" slot-scope="pileMileage,record">
          <div style="width: 200px;white-space: break-spaces;">{{ pileMileage }}</div>
        </template>
        <span slot="cement" slot-scope="text,record">
          <div v-for="(item,index) in record.deviceMixpileOneHandlerPages" :key="index">
            <a-divider v-if="index>0" style="margin:0;height:1px"/>
            <div v-if="item.pileDesigndep!==null">{{ item.pileDesigndep }}</div>
          </div>
        </span>
        <span slot="cement1" slot-scope="text,record ">
          <div v-for="(item,index) in record.deviceMixpileOneHandlerPages" :key="index">
            <a-divider v-if="index>0" style="margin:0;height:1px"/>
            <div v-if="item.pileRealdep!==null">{{
                item.pileRealdep.substring(0, record.pileRealdep.indexOf('.') + 3)
              }}</div>
          </div>
        </span>
        <span slot="cement2" slot-scope="text,record">
          <div v-for="(item,index) in record.deviceMixpileOneHandlerPages" :key="index">
            <a-divider v-if="index>0" style="margin:0;height:1px"/>
            <div v-if="item.gzcount!==null">{{ item.gzcount === 1 ? '初搅' : '复搅' }}</div>
          </div>
        </span>
        <span slot="cement3" slot-scope="text,record">
          <div v-for="(item,index) in record.deviceMixpileOneHandlerPages" :key="index">
            <a-divider v-if="index>0" style="margin:0;height:1px"/>
            <div v-if="item.pileWorktime!==null">{{ item.pileWorktime }}</div>
          </div>
        </span>
        <span slot="cement4" slot-scope="text,record">
          <div v-for="(item,index) in record.deviceMixpileOneHandlerPages" :key="index">
            <a-divider v-if="index>0" style="margin:0;height:1px"/>
            <div v-if="item.pileDspeed!==null">{{ Number(item.pileDspeed).toFixed(3) }}</div>
          </div>
        </span>
        <span slot="cement5" slot-scope="text,record">
          <div v-for="(item,index) in record.deviceMixpileOneHandlerPages" :key="index">
            <a-divider v-if="index>0" style="margin:0;height:1px"/>
            <div v-if="item.pileDelectr!==null">{{ Number(item.pileDelectr).toFixed(3) }}</div>
          </div>
        </span>
        <span slot="cement6" slot-scope="text,record">
          <div v-for="(item,index) in record.deviceMixpileOneHandlerPages" :key="index">
            <a-divider v-if="index>0" style="margin:0;height:1px"/>
            <div v-if="item.pileUspeed!==null">{{ Number(item.pileUspeed).toFixed(3) }}</div>
          </div>
        </span>
        <span slot="cement7" slot-scope="text,record">
          <div v-for="(item,index) in record.deviceMixpileOneHandlerPages" :key="index">
            <a-divider v-if="index>0" style="margin:0;height:1px"/>
            <div v-if="item.pileUelectr!==null">{{ Number(item.pileUelectr).toFixed(3) }}</div>
          </div>
        </span>
        <span slot="cement8" slot-scope="text,record">
          <div v-for="(item,index) in record.deviceMixpileOneHandlerPages" :key="index">
            <a-divider v-if="index>0" style="margin:0;height:1px"/>
            <div>{{
                (Number(item.pileDownbeton) + Number(item.pileUobeton)).toFixed(3)
              }}</div>
          </div>
        </span>
        <span slot="cement9" slot-scope="text,record">
          <div v-for="(item,index) in record.deviceMixpileOneHandlerPages" :key="index">
            <a-divider v-if="index>0" style="margin:0;height:1px"/>
            <div v-if="item.pileDowntime!==null">{{ Number(item.pileDowntime).toFixed(3) }}</div>
          </div>
        </span>
        <span slot="cement10" slot-scope="text,record">
          <div v-for="(item,index) in record.deviceMixpileOneHandlerPages" :key="index">
            <a-divider v-if="index>0" style="margin:0;height:1px"/>
            <div v-if="item.pileDownbeton!==null">{{ Number(item.pileDownbeton).toFixed(3) }}</div>
          </div>
        </span>
        <span slot="cement11" slot-scope="text,record">
          <div v-for="(item,index) in record.deviceMixpileOneHandlerPages" :key="index">
            <a-divider v-if="index>0" style="margin:0;height:1px"/>
            <div v-if="item.pileUptime!==null">{{ Number(item.pileUptime).toFixed(3) }}</div>
          </div>
        </span>
        <span slot="cement12" slot-scope="text,record">
          <div v-for="(item,index) in record.deviceMixpileOneHandlerPages" :key="index">
            <a-divider v-if="index>0" style="margin:0;height:1px"/>
            <div v-if="item.pileUobeton!==null">{{ Number(item.pileUobeton).toFixed(3) }}</div>
          </div>
        </span>
        <span slot="cement13" slot-scope="text,record">
          <div v-for="(item,index) in record.deviceMixpileOneHandlerPages" :key="index">
            <a-divider v-if="index>0" style="margin:0;height:1px"/>
            <div v-if="item.pileDensity!==null">{{ Number(item.pileDensity).toFixed(3) }}</div>
          </div>
        </span>
        <span slot="cement14" slot-scope="text,record">
          <div v-for="(item,index) in record.deviceMixpileOneHandlerPages" :key="index">
            <a-divider v-if="index>0" style="margin:0;height:1px"/>
            <div v-if="item.pileCement!==null">{{ Number(item.pileCement).toFixed(3) }}</div>
          </div>
        </span>
        <span slot="cement15" slot-scope="text,record">
          <div v-for="(item,index) in record.deviceMixpileOneHandlerPages" :key="index">
            <a-divider v-if="index>0" style="margin:0;height:1px"/>
            <div v-if="item.pileRatio!==null">{{ Number(item.pileRatio).toFixed(3) }}</div>
          </div>
        </span>
        <span slot="cement16" slot-scope="text,record">
          <div v-for="(item,index) in record.deviceMixpileOneHandlerPages" :key="index">
            <a-divider v-if="index>0" style="margin:0;height:1px"/>
            <div v-if="item.pileX!==null">{{ Number(item.pileX).toFixed(3) }}</div>
          </div>
        </span>
        <span slot="cement17" slot-scope="text,record">
          <div v-for="(item,index) in record.deviceMixpileOneHandlerPages" :key="index">
            <a-divider v-if="index>0" style="margin:0;height:1px"/>
            <div v-if="item.pileTime!==null">{{ item.pileTime }}</div>
          </div>
        </span>
        <span slot="cement18" slot-scope="text,record">
          <div v-for="(item,index) in record.deviceMixpileOneHandlerPages" :key="index">
            <a-divider v-if="index>0" style="margin:0;height:1px"/>
            <div v-if="item.chaobiaodengji!==null">{{ item.chaobiaodengji === 0 ? '合格' : '不合格' }}</div>
          </div>
        </span>
        <span slot="cement19" slot-scope="text, record">
          <a-tag color="green" v-if="record.deviceMixpileOneHandlerPages[0].handler.overproofStatus === 0">未处理</a-tag>
          <a-tag color="orange" v-if="record.deviceMixpileOneHandlerPages[0].handler.overproofStatus === 10">已处置</a-tag>
          <a-tag color="yellow"  v-if="record.deviceMixpileOneHandlerPages[0].handler.overproofStatus === 20">已闭合</a-tag>
          <a-tag color="red" v-if="record.deviceMixpileOneHandlerPages[0].handler.overproofStatus === 30">监理已驳回</a-tag>
       </span>

        <span slot="action" slot-scope="text, record">
         <a v-has="'mixpilehistoryonecbcx:edit'" @click="handleEdit(record)">编辑</a>
          <a @click="handleDetail(record)">详情</a>

          <a-divider type="vertical"/>
          <a-dropdown>
            <a class="ant-dropdown-link">更多 <a-icon type="down"/></a>
            <a-menu slot="overlay">
<!--              <a-menu-item v-has="'snjbz:czshInfo'">-->
              <!--                <a @click="showmadel1(record)">处理详情</a>-->
              <!--              </a-menu-item>-->
              <!--              <a-menu-item v-has="'snjbz:bh'">-->
              <!--                <a @click="showmadel2(record)">驳回</a>-->
              <!--              </a-menu-item>-->
              <!--              <a-menu-item v-has="'snjbz:sh'">-->
              <!--                <a @click="showmadel(record)">审核</a>-->
              <!--              </a-menu-item>-->

                <a-menu-item>
                  <a-popconfirm v-has="'mixpilehistoryonecbcx:del'" title="确定删除吗?"
                                @confirm="() => handleDelete(record.id)">
                   <a>删除</a>
                  </a-popconfirm>
                 </a-menu-item>
            </a-menu>
          </a-dropdown>


          <!--          <a-dropdown>-->
          <!--            <a class="ant-dropdown-link">更多 <a-icon type="down" /></a>-->
          <!--            <a-menu slot="overlay">-->
          <!--              <a-menu-item>-->
          <!--                <a @click="handleDetail(record)">详情</a>-->
          <!--              </a-menu-item>-->
          <!--              <a-menu-item>-->
          <!--                <a-popconfirm v-has="'mixpilehistoryone:del'" title="确定删除吗?" @confirm="() => handleDelete(record.id)">-->
          <!--                  <a>删除</a>-->
          <!--                </a-popconfirm>-->
          <!--              </a-menu-item>-->
          <!--            </a-menu>-->
          <!--          </a-dropdown>-->
        </span>

      </a-table>
    </div>

    <device-mixpile-historydata-one-c-b-modal ref="modalForm"
                                              @ok="modalFormOk"></device-mixpile-historydata-one-c-b-modal>
    <!--    <mixpile-one-over-handler-modal ref="modalForm1" @ok="modalFormOk"></mixpile-one-over-handler-modal>-->
  </a-card>
</template>

<script>

import '@/assets/less/TableExpand.less'
import { mixinDevice } from '@/utils/mixin'
import { JeecgListMixin } from '@/mixins/JeecgListMixin'
import DeviceMixpileHistorydataOneModal from './modules/DeviceMixpileHistorydataOneModal'
import JSuperQuery from '@/components/jeecg/JSuperQuery.vue'
import { usershebeiList } from '@api/api'
import Vue from 'vue'
// import MixpileOneOverHandlerModal from '../devicemixpileoneoverhandler/modules/MixpileOneOverHandlerModal'
import DeviceMixpileHistorydataOneCBModal
  from '@views/snjbz/devicemixpilehistorydataone/modules/DeviceMixpileHistorydataOneCBModal'

export default {
  name: 'deviceMixpileOneHandlerPages',
  mixins: [JeecgListMixin, mixinDevice],
  components: {
    // MixpileOneOverHandlerModal,
    DeviceMixpileHistorydataOneCBModal,
    JSuperQuery,
  },
  data() {
    return {
      flag: 0,
      bhz: 0,
      batchNo: '',
      dictOption: [],
      selectValue: '',
      description: 'device_mixpile_historydata_one管理页面',
      // 表头
      columns: [
        {
          title: '序号',
          dataIndex: '',
          key: 'rowIndex',
          width: 60,
          align: 'center',
          fixed: 'left',
          customRender: function (t, r, index) {
            return parseInt(index) + 1
          }
        },
        {
          title: '设备名称',
          align: 'center',
          fixed: 'left',
          dataIndex: 'shebeino_dictText',
          key: 'shebeino',
          scopedSlots: { customRender: 'shebeino_dictText' }
        },
        {
          title: '单桩编号',
          align: 'center',
          fixed: 'left',
          dataIndex: 'pileNo',
          key: 'pileNo',
        },
        {
          title: '里程桩号',
          align: 'center',
          dataIndex: 'pileMileage',
          key: 'pileMileage',
          fixed: 'left',
          scopedSlots: { customRender: 'pileMileage' }
        },
        {
          title: '设计桩长(m)',
          align: 'center',
          dataIndex: 'pileDesigndep',
          key: 'pileDesigndep',
        },
        {
          title: '实际桩长(m)',
          align: 'center',
          scopedSlots: { customRender: 'cement1' }
        },
        {
          title: '搅拌次数',
          align: 'center',
          scopedSlots: { customRender: 'cement2' }
        },
        {
          title: '喷浆时长(S)',
          align: 'center',
          scopedSlots: { customRender: 'cement3' }
        },
        {
          title: '下钻时长(S)',
          align: 'center',
          scopedSlots: { customRender: 'cement9' }
        },
        {
          title: '下钻平均速度(m/min)',
          align: 'center',
          scopedSlots: { customRender: 'cement4' }
        },
        {
          title: '下钻平均电流(A)',
          align: 'center',
          scopedSlots: { customRender: 'cement5' }
        },
        {
          title: '下钻浆量(L)',
          align: 'center',
          scopedSlots: { customRender: 'cement10' }
        },
        {
          title: '提钻时长(S)',
          align: 'center',
          scopedSlots: { customRender: 'cement11' }
        },
        {
          title: '提钻平均速度(m/min)',
          align: 'center',
          scopedSlots: { customRender: 'cement6' }
        },
        {
          title: '提钻平均电流(A)',
          align: 'center',
          scopedSlots: { customRender: 'cement7' }
        },
        {
          title: '提钻浆量(L)',
          align: 'center',
          scopedSlots: { customRender: 'cement12' }
        },
        {
          title: '桩号注浆量(L)',
          align: 'center',
          scopedSlots: { customRender: 'cement8' }
        },
        {
          title: '水泥浆比重',
          align: 'center',
          scopedSlots: { customRender: 'cement13' }
        },
        {
          title: '水泥用量(KG)',
          align: 'center',
          scopedSlots: { customRender: 'cement14' }
        },
        {
          title: '水灰比(%)',
          align: 'center',
          scopedSlots: { customRender: 'cement15' }
        },
        {
          title: '倾角',
          align: 'center',
          scopedSlots: { customRender: 'cement16' }
        },
        {
          title: '成桩时间',
          align: 'center',
          scopedSlots: { customRender: 'cement17' }
        },
        {
          title: '判定结果',
          align: 'center',
          scopedSlots: { customRender: 'cement18' }
        },
        {
          title: '处理状态',
          align: 'center',
          scopedSlots: { customRender: 'cement19' }
        },
        {
          title: '处置人',
          align: 'center',
          dataIndex: 'deviceMixpileOneHandlerPages[0].handler.handlePerson',
          key: 'deviceMixpileOneHandlerPages[0].handler.handlePerson',
        },
        {
          title: '审核人',
          align: 'center',
          dataIndex: 'deviceMixpileOneHandlerPages[0].handler.approvalPerson',
          key: 'deviceMixpileOneHandlerPages[0].handler.approvalPerson'
        },
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
        list: '/deviceMixpileHistorydataOne/deviceMixpileHistorydataOne/cbcxlist',
        delete: '/deviceMixpileHistorydataOne/deviceMixpileHistorydataOne/delete',
        deleteBatch: '/deviceMixpileHistorydataOne/deviceMixpileHistorydataOne/deleteBatch',
        exportXlsUrl: 'jmreport/view/635734078082105344',
        importExcelUrl: 'deviceMixpileHistorydataOne/deviceMixpileHistorydataOne/importExcel',
      },
      dictOptions: {},
      superFieldList: [],
    }
  },
  created() {
    this.getSuperFieldList()
    this.shebeiList()
  },
  computed: {
    importExcelUrl: function () {
      return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`
    },
  },
  methods: {

    // showmadel1: function (record) {
    //   this.$refs.modalForm1.edit(record);
    //   this.$refs.modalForm1.process=3;
    //   this.$refs.modalForm1.title = "处理详情";
    //   this.$refs.modalForm1.disableSubmit = false;
    // },

    shebeiList: function () {
      var sys_depart_orgcode = Vue.ls.get('SYS_DEPART_ORGCODE')
      usershebeiList({
        sys_depart_orgcode: sys_depart_orgcode,
        sbtypes: '16,53'
      }).then(res => {
        this.dictOption = []
        let result = res.result
        result.forEach(re => {
          this.dictOption.push({ text: re.sbname, value: re.sbjno })
        })
        //console.log(res)
      })
    },
    initDictConfig() {
    },

    getSuperFieldList() {
      let fieldList = []
      fieldList.push({ type: 'string', value: 'shebeino', text: '设备编号' })
      fieldList.push({ type: 'string', value: 'designdep', text: '设计深度' })
      fieldList.push({ type: 'string', value: 'curdep', text: '深度' })
      fieldList.push({ type: 'string', value: 'x', text: 'x轴角度' })
      fieldList.push({ type: 'string', value: 'y', text: 'y轴角度' })
      fieldList.push({ type: 'string', value: 'elec', text: '电流' })
      fieldList.push({ type: 'string', value: 'grouting', text: '灌浆量' })
      fieldList.push({ type: 'string', value: 'pileno', text: '桩号' })
      fieldList.push({ type: 'string', value: 'designratio', text: '设计水灰比' })
      fieldList.push({ type: 'string', value: 'designgrout', text: '设计灌浆量' })
      fieldList.push({ type: 'string', value: 'cement', text: '水泥量' })
      fieldList.push({ type: 'string', value: 'water', text: '用水量' })
      fieldList.push({ type: 'string', value: 'ratio', text: '水灰比' })
      fieldList.push({ type: 'string', value: 'serial', text: '盘次' })
      fieldList.push({ type: 'string', value: 'username', text: '操作员' })
      fieldList.push({ type: 'date', value: 'datatime', text: '数据时间' })
      fieldList.push({ type: 'string', value: 'ltdfloat', text: '经度' })
      fieldList.push({ type: 'string', value: 'lgdfloat', text: '纬度' })
      fieldList.push({ type: 'string', value: 'flowlst', text: '瞬时流量' })
      fieldList.push({ type: 'string', value: 'speed', text: '速度' })
      fieldList.push({ type: 'date', value: 'starttime', text: '开始时间' })
      fieldList.push({ type: 'date', value: 'endtime', text: '结束时间' })
      fieldList.push({ type: 'string', value: 'leflowlst', text: '累计流量' })
      fieldList.push({ type: 'int', value: 'pjtime', text: '喷浆时长' })
      fieldList.push({ type: 'string', value: 'xzjl', text: '下钻浆量' })
      fieldList.push({ type: 'string', value: 'tzjl', text: '抬钻浆量' })
      fieldList.push({ type: 'string', value: 'xztime', text: '下钻时长' })
      fieldList.push({ type: 'string', value: 'tztime', text: '抬钻时长' })
      this.superFieldList = fieldList
    }
  }
}
</script>
<style scoped>
@import '~@assets/less/common.less';
</style>