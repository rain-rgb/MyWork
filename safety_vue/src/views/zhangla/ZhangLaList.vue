<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="设备名称">
              <j-search-select-tag placeholder="请选择设备名称" v-model="queryParam.deviceno" :dictOptions="dictOption" >
              </j-search-select-tag>
              {{ selectValue }}
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="判定结果">
              <j-dict-select-tag placeholder="请选择判定结果" v-model="queryParam.result"
                                 dictCode="result"></j-dict-select-tag>
            </a-form-item>
          </a-col>
<!--          <a-col :xl="6" :lg="7" :md="8" :sm="24">-->
<!--            <a-form-item label="实验日期范围">-->
<!--              <j-date placeholder="开始实验日期" v-model="queryParam.syrq_begin" :showTime="true"-->
<!--                      dateFormat="YYYY-MM-DD HH:mm:ss"/>-->
<!--            </a-form-item>-->
<!--          </a-col>-->
<!--          <a-col :xl="6" :lg="7" :md="8" :sm="24">-->
<!--            <a-form-item label="">-->
<!--              <j-date placeholder="结束实验日期" v-model="queryParam.syrq_end" :showTime="true"-->
<!--                      dateFormat="YYYY-MM-DD HH:mm:ss"/>-->
<!--            </a-form-item>-->
<!--          </a-col>-->
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
      <a-button @click="handleAdd" v-has="'syj:add'" type="primary" icon="plus">新增</a-button>
      <a-button type="primary" icon="download" v-has="'syj:dc'" @click="handleExportXls('水稳主表')">导出</a-button>
      <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl"
                @change="handleImportExcel">
        <a-button type="primary" v-has="'syj:dr'" icon="import">导入</a-button>
      </a-upload>
      <!-- 高级查询区域 -->
      <!--      <j-super-query :fieldList="superFieldList" ref="superQueryModal"-->
      <!--                     @handleSuperQuery="handleSuperQuery"></j-super-query>-->
      <!--      <a-dropdown v-if="selectedRowKeys.length > 0">-->
      <!--        <a-menu slot="overlay">-->
      <!--          <a-menu-item key="1" @click="batchDel">-->
      <!--            <a-icon type="delete"/>-->
      <!--            删除-->
      <!--          </a-menu-item>-->
      <!--        </a-menu>-->
      <!--        <a-button style="margin-left: 8px"> 批量操作-->
      <!--          <a-icon type="down"/>-->
      <!--        </a-button>-->
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
        bordered
        rowKey="id"
        class="j-table-force-nowrap"
        :scroll="{x:true}"
        :columns="columns"
        :dataSource="dataSource"
        :pagination="ipagination"
        :loading="loading"
        :rowSelection="{selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"
        @change="handleTableChange"
        :customRow="handleClick"
        :rowClassName="setRowClassName"
      >
         <span slot="tags" slot-scope="tags, record">
        <a-tag color="green" v-if="record.result == '0'">合格</a-tag>
         <a-tag color="red" v-if="record.result == '1'">不合格</a-tag>
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
          <a  v-has="'syj:edit'" @click="handleEdit(record)">编辑</a>

          <a-divider type="vertical"/>
          <a-dropdown>
            <a class="ant-dropdown-link">更多 <a-icon type="down"/></a>
            <a-menu slot="overlay">
              <a-menu-item>
                <a @click="handleDetail(record)">详情</a>
              </a-menu-item>
              <a-menu-item v-has="'syj:del'">
                <a-popconfirm  title="确定删除吗?" @confirm="() => handleDelete(record.id)">
                  <a>删除</a>
                </a-popconfirm>
              </a-menu-item>
            </a-menu>
          </a-dropdown>
        </span>

      </a-table>
    </div>

    <ZhangLaModal ref="modalForm" @ok="modalFormOk"/>
  </a-card>
</template>

<script>

  import {JeecgListMixin} from '@/mixins/JeecgListMixin'
  import JDictSelectTag from '@/components/dict/JDictSelectTag.vue'
  import '@/assets/less/TableExpand.less'
  import JSuperQuery from '@/components/jeecg/JSuperQuery.vue'
  import ZhangLaModal from "./moduls/ZhangLaModal";
  import { handertoken } from '@/mixins/getHanderToken'
  import Vue from 'vue'
  import { SYS_DEPART_ORGCODE } from '@/store/mutation-types'
  import { usershebeiList } from '@api/api'
  export default {
    name: "ZhangLaList",
    mixins: [JeecgListMixin,handertoken,SYS_DEPART_ORGCODE],
    components: {
      JDictSelectTag,
      ZhangLaModal,
      JSuperQuery
    },
    data() {
      return {
        selectValue: '',
        asyncSelectValue: '',
        dictOption: [],
        description: '张拉主表管理页面',
        // 表头
        columns: [
          {
            title: '序号',
            dataIndex: '',
            key: 'rowIndex',
            width: 60,
            align: "center",
            customRender: function (t, r, index) {
              return parseInt(index) + 1;
            }
          },
          // {
          //   title: '所属组织机构',
          //   align: "center",
          //   dataIndex: 'sysOrgCode_dictText'
          // },
          {
            title: '设备名称',
            align: "center",
            dataIndex: 'deviceno_dictText',
          },
          {
            title: '梁型',
            align: "center",
            dataIndex: 'beamtype'
          },
          {
            title: '梁(构件)名称',
            align: "center",
            dataIndex: 'componentparts'
          },
          {
            title: '张拉时间',
            align: "center",
            dataIndex: 'tensioningdate'
          },
          {
            title: '操作员',
            align: "center",
            dataIndex: 'userid'
          },
          {
            title: '砼设计强度',
            align: "center",
            dataIndex: 'concretestrength',
          },
          {
            title: '弹性模量',
            dataIndex: 'modulusofelasticity',
            align: "center",
          },
          {
            title: '工程（施工）部位',
            align: "center",
            dataIndex: 'engineeringsite',
          },
          {
            title: '工程名称',
            align: "center",
            dataIndex: 'engineeringname',
          },
          {
            title: '是否合格',
            align: "center",
            dataIndex: 'result_dictText',
            key:'result_dictText',
            scopedSlots: { customRender: 'tags' },
          },
          {
            title: '操作',
            dataIndex: 'action',
            align: "center",
            fixed: "right",
            width: 147,
            scopedSlots: {customRender: 'action'},
          }
          // {
          //   title: '梁型',
          //   align: "center",
          //   dataIndex: 'beamtype',
          // },
          // {
          //   title: '砼设计强度',
          //   align: "center",
          //   dataIndex: 'concretestrength',
          // },
          // {
          //   title: '弹性模量',
          //   align: "center",
          //   dataIndex: 'modulusofelasticity',
          // },
          // {
          //   title: '钢束编号',
          //   align: "center",
          //   dataIndex: 'steelbeamno',
          // },
          // {
          //   title: '钢绞线根数',
          //   align: "center",
          //   dataIndex: 'steelstrand',
          // },
          // {
          //   title: '张拉断面',
          //   align: "center",
          //   dataIndex: 'tensioningsection',
          // },
          // {
          //   title: '张拉时间',
          //   align: "center",
          //   dataIndex: 'tensioningdate',
          // },
          // {
          //   title: '张拉力0',
          //   align: "center",
          //   dataIndex: 'tensioningforce0',
          // },
          // {
          //   title: '张拉力1',
          //   align: "center",
          //   dataIndex: 'tensioningsection1',
          // },
          // {
          //   title: '张拉力2',
          //   align: "center",
          //   dataIndex: 'tensioningsection2',
          // },
          // {
          //   title: '张拉力3',
          //   align: "center",
          //   dataIndex: 'tensioningsection3',
          // },
          // {
          //   title: '张拉力4',
          //   align: "center",
          //   dataIndex: 'tensioningsection4',
          // },
          // {
          //   title: '伸长量0',
          //   align: "center",
          //   dataIndex: 'elongation0',
          // },
          // {
          //   title: '伸长量1',
          //   align: "center",
          //   dataIndex: 'elongation1',
          // },
          // {
          //   title: '伸长量2',
          //   align: "center",
          //   dataIndex: 'elongation2',
          // },
          // {
          //   title: '伸长量3',
          //   align: "center",
          //   dataIndex: 'elongation3',
          // },
          // {
          //   title: '伸长量4',
          //   align: "center",
          //   dataIndex: 'elongation4',
          // },
          // {
          //   title: '油压0',
          //   align: "center",
          //   dataIndex: 'oilpressure0',
          // },
          // {
          //   title: '油压1',
          //   align: "center",
          //   dataIndex: 'oilpressure1',
          // },
          // {
          //   title: '油压2',
          //   align: "center",
          //   dataIndex: 'oilpressure2',
          // },
          // {
          //   title: '油压3',
          //   align: "center",
          //   dataIndex: 'oilpressure3',
          // },
          // {
          //   title: '油压4',
          //   align: "center",
          //   dataIndex: 'oilpressure4',
          // },
          // {
          //   title: '设计张拉控制力',
          //   align: "center",
          //   dataIndex: 'tensioncontrol',
          // },
          // {
          //   title: '总伸长量',
          //   align: "center",
          //   dataIndex: 'totalelongation',
          // },
          // {
          //   title: '理论伸长量',
          //   align: "center",
          //   dataIndex: 'theoreticalelongation',
          // },
          // {
          //   title: '延伸误差量',
          //   align: "center",
          //   dataIndex: 'extenderror',
          // },
          // {
          //   title: '是否合格缺省为0；0代表合格，1代表不合格',
          //   align: "center",
          //   dataIndex: 'result',
          // },
          // {
          //   title: '操作员',
          //   align: "center",
          //   dataIndex: 'userid',
          // },
          // {
          //   title: '其他信息 ',
          //   align: "center",
          //   dataIndex: 'otherinformation',
          // },
          // {
          //   title: '张拉力曲线 ',
          //   align: "center",
          //   dataIndex: 'forcecurve',
          // },
          // {
          //   title: '伸长量曲线 ',
          //   align: "center",
          //   dataIndex: 'elongationcurve',
          // },
          // {
          //   title: '油压曲线 ',
          //   align: "center",
          //   dataIndex: 'oilpressurecurve',
          // },
          // {
          //   title: '工程名称',
          //   align: "center",
          //   dataIndex: 'engineeringname',
          // },
          // {
          //   title: '工程（施工）部位',
          //   align: "center",
          //   dataIndex: 'engineeringsite',
          // }, {
          //   title: '状态位',
          //   align: "center",
          //   dataIndex: '',
          // },
        ],
        url: {
          list: "/zhangla/trZhangla/list",
          delete: "/zhangla/trZhangla/delete",
          deleteBatch: "/zhangla/trZhangla/deleteBatch",
          exportXlsUrl: "/zhangla/trZhangla/exportXls",
          importExcelUrl: "/zhangla/trZhangla/importExcel",
        },
        dictOptions: {},
        superFieldList: [],
      }
    },
    created() {
      this.getToken();
      this.getSuperFieldList();
      this.shebeiList();
    },
    computed: {
      importExcelUrl: function () {
        return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`;
      }
    },
    methods: {
      shebeiList:function (){
        var sys_depart_orgcode=Vue.ls.get('SYS_DEPART_ORGCODE');
        usershebeiList({
          sys_depart_orgcode:sys_depart_orgcode,
          sbtypes:'9'
        }).then(res=>{
          this.dictOption=[];
          let result=res.result;
          console.log(res,"设备列表")
          result.forEach(re=>{
            this.dictOption.push({text:re.sbname,value:re.sbjno})
          })
          //console.log(res)
        })
      },
      initDictConfig() {
      },
      getSuperFieldList() {
        let fieldList = [];
        fieldList.push({type: 'int', value: 'id', text: 'id', dictCode: ''})
        fieldList.push({type: 'string', value: 'uuid', text: '唯一码', dictCode: ''})
        fieldList.push({type: 'string', value: 'token', text: '鉴权ID,通过鉴权接口获取', dictCode: ''})
        fieldList.push({type: 'string', value: 'projectid', text: '预留字段，项目唯一标识，用于判断设备所属项目', dictCode: ''})
        fieldList.push({type: 'string', value: 'platformdeviceid', text: '平台设备识别码根据厂家编码和厂家设备编号从平台获取', dictCode: ''})
        fieldList.push({type: 'string', value: 'vendorno', text: '厂家编码根据厂家编码和厂家设备编号从平台获取', dictCode: ''})
        fieldList.push({type: 'string', value: 'deviceno', text: '厂家的设备唯一编号', dictCode: ''})
        fieldList.push({type: 'string', value: 'componentid', text: '梁识别码每片梁只能对应一个UUID，由厂家生成', dictCode: ''})
        fieldList.push({type: 'string', value: 'componentparts', text: '梁（构件）名称或编号'})
        fieldList.push({type: 'string', value: 'beamtype', text: '梁型', dictCode: ''})
        fieldList.push({type: 'string', value: 'concretestrength', text: '砼设计强度', dictCode: ''})
        fieldList.push({type: 'string', value: 'modulusofelasticity', text: '弹性模量', dictCode: ''})
        fieldList.push({type: 'string', value: 'steelbeamno', text: '钢束编号', dictCode: ''})
        fieldList.push({type: 'string', value: 'steelstrand', text: '钢绞线根数', dictCode: ''})
        fieldList.push({type: 'string', value: 'tensioningsection', text: '张拉断面', dictCode: ''})
        fieldList.push({type: 'date', value: 'tensioningdate', text: '张拉时间', dictCode: ''})
        fieldList.push({type: 'string', value: 'tensioningforce0', text: '张拉力0', dictCode: ''})
        fieldList.push({type: 'string', value: 'tensioningforce1', text: '张拉力1', dictCode: ''})
        fieldList.push({type: 'string', value: 'tensioningforce2', text: '张拉力2', dictCode: ''})
        fieldList.push({type: 'string', value: 'tensioningforce3', text: '张拉力3', dictCode: ''})
        fieldList.push({type: 'string', value: 'tensioningforce4', text: '张拉力4', dictCode: ''})
        fieldList.push({type: 'string', value: 'elongation0', text: '伸长量0', dictCode: ''})
        fieldList.push({type: 'string', value: 'elongation1', text: '伸长量1', dictCode: ''})
        fieldList.push({type: 'string', value: 'elongation2', text: '伸长量2', dictCode: ''})
        fieldList.push({type: 'string', value: 'elongation3', text: '伸长量3', dictCode: ''})
        fieldList.push({type: 'string', value: 'elongation4', text: '伸长量4', dictCode: ''})
        fieldList.push({type: 'string', value: 'oilpressure0', text: '油压0', dictCode: ''})
        fieldList.push({type: 'string', value: 'oilpressure1', text: '油压1', dictCode: ''})
        fieldList.push({type: 'string', value: 'oilpressure2', text: '油压2 ', dictCode: ''})
        fieldList.push({type: 'string', value: 'oilpressure3', text: '油压3', dictCode: ''})
        fieldList.push({type: 'string', value: 'oilpressure4', text: '油压4', dictCode: ''})
        fieldList.push({type: 'string', value: 'tensioncontrol', text: '设计张拉控制力', dictCode: ''})
        fieldList.push({type: 'string', value: 'totalelongation', text: '总伸长量', dictCode: ''})
        fieldList.push({type: 'string', value: 'theoreticalelongation', text: '理论伸长量', dictCode: ''})
        fieldList.push({type: 'string', value: 'extenderror', text: '延伸误差量', dictCode: ''})
        fieldList.push({type: 'int', value: 'result_dictText', text: '是否合格缺省为0；0代表合格，1代表不合格', dictCode: ''})
        fieldList.push({type: 'string', value: 'userid', text: '操作员', dictCode: ''})
        fieldList.push({type: 'string', value: 'otherinformation', text: '其他信息', dictCode: ''})
        fieldList.push({type: 'string', value: 'forcecurve', text: '张拉力曲线', dictCode: ''})
        fieldList.push({type: 'string', value: 'elongationcurve', text: '伸长量曲线', dictCode: ''})
        fieldList.push({type: 'string', value: 'oilpressurecurve', text: '油压曲线', dictCode: ''})
        fieldList.push({type: 'string', value: 'engineeringname', text: '工程名称', dictCode: ''})
        fieldList.push({type: 'string', value: 'engineeringsite', text: '工程（施工）部位', dictCode: ''})
        fieldList.push({type: 'int', value: 'status', text: '是否判断超标并报警0为没有判断过1为已判断并且进行过了生产统计  20为已经处理的数据   30为已经进行了超标处理统计   40为数据异常', dictCode: ''})
        this.superFieldList = fieldList
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>