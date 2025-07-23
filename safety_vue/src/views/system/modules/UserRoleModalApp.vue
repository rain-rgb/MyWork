<template>
  <a-card :bordered="false">
    <!-- 抽屉 -->
    <a-drawer
      title="app权限列表"
      :width="screenWidth"
      @close="onClose"
      :visible="visible"
    >
      <!-- 抽屉内容的border -->
      <div
        :style="{
          padding:'10px',
          border: '1px solid #e9e9e9',
          background: '#fff',
        }">

        <div class="table-page-search-wrapper">
          <a-form layout="inline" :form="form" @keyup.enter.native="searchQuery">
            <a-row>
              <a-col :md="2" :sm="24">
                <a-button style="margin-bottom: 10px" type="primary" @click="handleAdd">新增</a-button>
              </a-col>
            </a-row>
          </a-form>
        </div>
        <div>
          <a-table
            ref="table"
            rowKey="id"
            size="middle"
            :columns="columns"
            :dataSource="dataSource"
            :loading="loading"
            :pagination="ipagination"
            :rowClassName="getRowClassname"
            @change="handleTableChange"
          >
          <span slot="action" slot-scope="text, record">
<!--            <a @click="handleEdit(record)">编辑</a>-->
<!--            <a-divider type="vertical"/>-->
            <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">
              <a>删除</a>
            </a-popconfirm>
          </span>

          </a-table>
        </div>
      </div>
    </a-drawer>
    <app-modal ref="modalForm" ></app-modal>   <!-- app权限表-->
  </a-card>
</template>

<script>
import {filterObj} from '@/utils/util';
import AppModal from '../modules/APPModal'
import { getAction , deleteAction } from '@api/manage'
export default {
  name: "UserRoleModalApp",
  //mixins: [pushdepartidShebei],
  components: {AppModal},
  data() {
    return {
      dataSource:[],
      ipagination: {
        current: 1,
        pageSize: 10,
        pageSizeOptions: ['10', '20', '30'],
        showTotal: (total, range) => {
          return range[0] + '-' + range[1] + ' 共' + total + '条'
        },
        showQuickJumper: true,
        showSizeChanger: true,
        total: 0
      },
      loading:true,
      columns: [
        {
          title: '名称',
          align: "center",
          dataIndex: 'appfunction_dictText',
        },
        {
          title: '操作',
          dataIndex: 'action',
          align: "center",
          scopedSlots: {customRender: 'action'},
        }
      ],
      queryParam: {
        appfunction: '',
      },
      title: "操作",
      visible: false,
      screenWidth: 800,
      model: {},
      dictId: "",
      roleId:'',
      status: 1,
      labelCol: {
        xs: {span: 5},
        sm: {span: 5},
      },
      wrapperCol: {
        xs: {span: 12},
        sm: {span: 12},
      },
      form: this.$form.createForm(this),
      validatorRules: {
        itemText: {rules: [{required: true, message: '请输入名称!'}]},
        itemValue: {rules: [{required: true, message: '请输入数据值!'}]},
      },
      url: {
        list: "/appfunction/appfunction/list",
        delete: "/appfunction/appfunction/delete",
        deleteBatch: "/sys/dictItem/deleteBatch",
      },
    }
  },
  created() {
    // 当页面初始化时,根据屏幕大小来给抽屉设置宽度
    this.resetScreenSize();
  },
  methods: {
    handleDelete(id){
      const that = this;
      let parameter={id:id};
      deleteAction(this.url.delete,parameter).then((res)=>{
        console.log(res)
        if (res.success) {
          that.$message.success(res.message)
        } else {
          that.$message.warning(res.message)
        }
      })
      this.routeReload();
    },
    routeReload() {//用于刷新页面
      this.reloadFlag = false
      let ToggleMultipage = 'ToggleMultipage'
      this.$store.dispatch(ToggleMultipage, false)
      this.$nextTick(() => {
        this.$store.dispatch(ToggleMultipage, true)
        this.reloadFlag = true
      })
      console.log("刷新页面")
    },
    add(roleId) {
      this.roleId = roleId;
      //var appfunction=this.roleId;

      console.log(this.roleId)
      this.edit({});
    },
    show(roleId) {
      this.roleId = roleId;
      this.queryParam.appfunction=roleId;
      console.log(this.roleId)
      // if (record.id) {
      //   this.dictId = record.id;
      // }
      this.queryParam = {}
      // this.form.resetFields();
      // this.model = Object.assign({}, record);
      // this.model.dictId = this.dictId;
      // this.model.status = this.status;
      this.visible = true;
      // this.$nextTick(() => {
      //   this.form.setFieldsValue(pick(this.model, 'itemText', 'itemValue'))
      // });
      // // 当其它模块调用该模块时,调用此方法加载字典数据
      this.loadData();
    },
    loadData(){
      let param={roleids:this.roleId,pageNo:this.ipagination.current,pageSize:this.ipagination.pageSize}
       getAction(this.url.list,param).then((res)=>{
         if(res.success){
           this.dataSource=res.result.records;
           this.ipagination.total = res.result.total
           this.loading=false;
           console.log(res)
         }

       })
    },
    handleTableChange(pagination, filters, sorter) {
      //分页、排序、筛选变化时触发
      //TODO 筛选
      // if (Object.keys(sorter).length > 0) {
      //   this.isorter2.column = sorter.field
      //   this.isorter2.order = 'ascend' == sorter.order ? 'asc' : 'desc'
      // }
      this.ipagination = pagination
      this.loadData()
    },
    getQueryParams() {
      var param = Object.assign({}, this.queryParam);
      param.appfunction = this.roleId;
      param.field = this.getQueryField();
      param.pageNo = this.ipagination.current;
      param.pageSize = this.ipagination.pageSize;
      if (this.superQueryParams) {
        param['superQueryParams'] = encodeURI(this.superQueryParams)
        param['superQueryMatchType'] = this.superQueryMatchType
      }
      return filterObj(param);
    },

    // 添加字典数据
    handleAdd() {
      this.$refs.modalForm.add(this.roleId);
      this.$refs.modalForm.title = "新增";
    },
    showDrawer() {
      this.visible = true
    },
    onClose() {
      this.visible = false
      this.form.resetFields();
      this.dataSource = [];
    },
    // 抽屉的宽度随着屏幕大小来改变
    resetScreenSize() {
      let screenWidth = document.body.clientWidth;
      if (screenWidth < 600) {
        this.screenWidth = screenWidth;
      } else {
        this.screenWidth = 600;
      }
    },
    //update--begin--autor:wangshuai-----date:20191204------for：系统管理 数据字典禁用和正常区别开，添加背景颜色 teambition JT-22------
    //增加样式方法返回值
    getRowClassname(record){
      if(record.status==0){
        return "data-rule-invalid"
      }
    }
    //update--end--autor:wangshuai-----date:20191204------for：系统管理 数据字典禁用和正常区别开，添加背景颜色 teambition JT-22------
  }
}
</script>
<style lang="less" scoped>
//update--begin--autor:wangshuai-----date:20191204------for：系统管理 数据字典禁用和正常区别开，添加背景颜色 teambition JT-22------
/deep/ .data-rule-invalid{
  background: #f4f4f4;
  color: #bababa;
}
//update--begin--autor:wangshuai-----date:20191204------for：系统管理 数据字典禁用和正常区别开，添加背景颜色 teambition JT-22------
</style>