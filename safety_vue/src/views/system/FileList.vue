<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :md="6" :sm="8">
            <a-form-item label="文件名称">
              <a-input placeholder="请输入文件名称" v-model="queryParam.name"></a-input>
            </a-form-item>
          </a-col>
          <!--          <a-col :md="6" :sm="8">-->
          <!--            <a-form-item label="文件地址">-->
          <!--              <a-input placeholder="请输入文件地址" v-model="queryParam.url"></a-input>-->
          <!--            </a-form-item>-->
          <!--          </a-col>-->
          <a-col :md="6" :sm="8">
            <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
              <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
              <a-button type="primary" @click="searchReset" icon="reload" style="margin-left: 8px">重置</a-button>
            </span>
          </a-col>
        </a-row>
      </a-form>
    </div>

    <!-- 操作按钮区域 -->
    <div class="table-operator">
      <!--      <a-button type="primary" icon="download" @click="handleExportXls('文件列表')">导出</a-button>-->
      <a-button @click="handleAdd" type="primary" icon="plus">新增</a-button>
      <!--      <a-upload-->
      <!--        name="file"-->
      <!--        :multiple="false"-->
      <!--        :action="uploadAction"-->
      <!--        :headers="tokenHeader"-->
      <!--        :showUploadList="false"-->
      <!--        :beforeUpload="beforeUpload"-->
      <!--        @change="handleChange">-->

      <!--          <a-button type="primary">-->
      <!--          <a-icon type="upload"/>-->
      <!--          文件上传-->
      <!--          </a-button>-->
      <!--      </a-upload>-->

      <!--      <a-upload-->
      <!--        name="file"-->
      <!--        :multiple="false"-->
      <!--        :action="minioUploadAction"-->
      <!--        :headers="tokenHeader"-->
      <!--        :showUploadList="false"-->
      <!--        :beforeUpload="beforeUpload"-->
      <!--        @change="handleChange">-->
      <!--        <a-button>-->
      <!--          <a-icon type="upload"/>-->
      <!--          MINIO文件上传-->
      <!--        </a-button>-->
      <!--      </a-upload>-->
    </div>

    <!-- table区域-begin -->
    <div>
      <div class="ant-alert ant-alert-info" style="margin-bottom: 16px;">
        <i class="anticon anticon-info-circle ant-alert-icon"></i> 已选择 <a
        style="font-weight: 600">{{
          selectedRowKeys.length
        }}</a>项
        <a style="margin-left: 24px" @click="onClearSelected">清空</a>
      </div>

      <a-table
        ref="table"
        size="middle"
        bordered
        rowKey="id"
        :columns="columns"
        :dataSource="dataSource"
        :pagination="ipagination"
        :loading="loading"
        :rowSelection="{selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"
        @change="handleTableChange"
        :customRow="handleClick"
        :rowClassName="setRowClassName"
      >
        <template slot="fileSlot" slot-scope="text,record">
          <span v-if="!record.url" style="font-size: 12px;font-style: italic;">无文件</span>
          <a-button
            v-else
            :ghost="true"
            type="primary"
            icon="download"
            size="small"
            @click="downloadFile(record.url)">
            下载
          </a-button>
        </template>
        <span slot="action" slot-scope="text, record">
          <a @click="handlePreview(record)">预览</a>
            <a-divider type="vertical" v-has="'file:del'"/>
          <a @click="ossDelete(record.id)">删除</a>
        </span>

      </a-table>
    </div>
    <!-- table区域-end -->
    <FileModeal ref="modalForm" @ok="modalFormOk"></FileModeal>
  </a-card>
</template>

<script>
import { JeecgListMixin } from '@/mixins/JeecgListMixin'
import FileModeal from '@views/system/modules/FileModeal'
import  {base64Encode} from '@api/kkfileView'
export default {
  name: 'FileList',
  components: { FileModeal },
  mixins: [JeecgListMixin],
  data() {
    return {
      description: '文件列表',
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
          title: '所属组织机构',
          align: 'center',
          dataIndex: 'sysOrgCode_dictText'
        },
        {
          title: '文件名称',
          align: 'center',
          dataIndex: 'name'
        },
        {
          title: '创建人',
          align: 'center',
          dataIndex: 'createBy_dictText'
        },
        {
          title: '创建时间',
          align: 'center',
          dataIndex: 'createTime'
        },
        {
          title: '下载',
          align: 'center',
          dataIndex: 'url',
          key: 'url',
          scopedSlots: { customRender: 'fileSlot' },
        },
        {
          title: '操作',
          dataIndex: 'action',
          align: 'center',
          scopedSlots: { customRender: 'action' },
        }
      ],
      url: {
        upload: '/sys/oss/file/upload',
        list: '/sys/oss/file/list',
        delete: '/sys/oss/file/delete',
        minioUpload: '/sys/upload/uploadMinio'
      }
    }
  },
  computed: {
    uploadAction() {
      return window._CONFIG['domianURL'] + this.url.upload
    },
    minioUploadAction() {
      return window._CONFIG['domianURL'] + this.url.minioUpload
    },
  },
  methods: {
    beforeUpload(file) {
      var fileType = file.type
      if (fileType === 'image') {
        if (fileType.indexOf('image') < 0) {
          this.$message.warning('请上传图片')
          return false
        }
      } else if (fileType === 'file') {
        if (fileType.indexOf('image') >= 0) {
          this.$message.warning('请上传文件')
          return false
        }
      }
      return true
    },
    handleChange(info) {
      if (info.file.status === 'done') {
        if (info.file.response.success) {
          this.loadData()
          this.$message.success(`${info.file.name} 上传成功!`)
        } else {
          this.$message.error(`${info.file.response.message}`)
        }
      } else if (info.file.status === 'error') {
        this.$message.error(`${info.file.response.message}`)
      }
    },
    ossDelete(id) {
      var that = this
      that.$confirm({
        title: '确认删除',
        content: '是否删除选中文件?',
        onOk: function () {
          that.handleDelete(id)
        }
      })
    },
    handlePreview(record) {
      if (record && record.url) {
        let url = window._CONFIG['onlinePreviewDomainURL'] + '?url=' + encodeURIComponent(base64Encode(record.url))
        window.open(url, '_blank')
      }
    }
  }
}
</script>

<style scoped>
@import '~@assets/less/common.less';
</style>
