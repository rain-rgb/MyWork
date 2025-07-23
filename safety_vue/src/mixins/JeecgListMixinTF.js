/**
 * 新增修改完成调用 modalFormOk方法 编辑弹框组件ref定义为modalForm
 * 高级查询按钮调用 superQuery方法  高级查询组件ref定义为superQueryModal
 * data中url定义 list为查询列表  delete为删除单条记录  deleteBatch为批量删除
 */
import { filterObj } from '@/utils/util';
import { deleteAction, getAction,downFile,getFileAccessHttpUrl } from '@/api/manage'
import Vue from 'vue'
import { ACCESS_TOKEN, TENANT_ID, SAFETY_PARTIAL_BASIC } from '@/store/mutation-types'
import store from '@/store'
import {Modal} from 'ant-design-vue'
import { SYS_DEPART_ORGCODE,DEPART_ID } from '@/store/mutation-types'


export const JeecgListMixin = {
  data(){
    return {
      //token header
      //tokenHeader: {'X-Access-Token': Vue.ls.get(ACCESS_TOKEN)},
      /* 查询条件-请不要在queryParam中声明非字符串值的属性 */
      queryParam: {  },
      /* 数据源 */
      dataSource:[],
      /* 分页参数 */
      ipagination:{
        current: 1,
        pageSize: 10,
        pageSizeOptions: ['10', '20', '30'],
        showTotal: (total, range) => {
          return range[0] + "-" + range[1] + " 共" + total + "条"
        },
        showQuickJumper: true,
        showSizeChanger: true,
        total: 0
      },
      /* 排序参数 */
      isorter:{
        column: 'id',
        order: 'desc',
      },
      /* 筛选参数 */
      filters: {},
      /* table加载状态 */
      loading:false,
      /* table选中keys*/
      selectedRowKeys: [],
      /* table选中records*/
      selectionRows: [],
      /* 查询折叠 */
      toggleSearchStatus:false,
      /* 高级查询条件生效状态 */
      superQueryFlag:false,
      /* 高级查询条件 */
      superQueryParams: '',
      /** 高级查询拼接方式 */
      superQueryMatchType: 'and',
      sys_depart_orgcode:'',
      sys_depart_project:'',
      depart_id:'',
      token:'',
    }
  },
  created() {
    this.token=Vue.ls.get(ACCESS_TOKEN);
    this.sys_depart_orgcode=Vue.ls.get('SYS_DEPART_ORGCODE');
    this.sys_depart_project=Vue.ls.get('SAFETY_PARTIAL_BASIC');
    this.depart_id=Vue.ls.get(DEPART_ID);
    console.log(this.token)
    console.log(this.depart_id)
    console.log(this.sys_depart_orgcode)
    console.log(this.sys_depart_project)
      if(!this.disableMixinCreated){
        console.log(' -- mixin created -- ')
        // this.loadData();
        //初始化字典配置 在自己页面定义
        //this.initDictConfig();
      }
  },
  computed: {
    //token header
    tokenHeader(){
      let head = {'X-Access-Token': Vue.ls.get(ACCESS_TOKEN)}
      let tenantid = Vue.ls.get(TENANT_ID)
      if(tenantid){
        head['tenant-id'] = tenantid
      }
      return head;
    }
  },
  methods:{
    loadData(arg) {
      if(!this.url.list){
        // this.$message.error("请设置url.list属性!")
        return
      }
      //加载数据 若传入参数1则加载第一页的内容
      if (arg === 1) {
        this.ipagination.current = 1;
      }
      var params = this.getQueryParams();//查询条件
      this.loading = true;
      getAction(this.url.list, params).then((res) => {
        //console.log(res)
        this.dataSource = [];
        if (res.success) {
          //update-begin---author:zhangyafei    Date:20201118  for：适配不分页的数据列表------------
          this.dataSource = res.result.records||res.result;
          if(res.result.total)
          {
            this.ipagination.total = res.result.total;
          } else {
            this.ipagination.total = 0;
          }
          //update-end---author:zhangyafei    Date:20201118  for：适配不分页的数据列表------------
        }else{
        this.dataSource = [];
        this.$message.warning(res.message)
        }
      }).finally(() => {
        this.loading = false
      })
    },
    initDictConfig(){
      console.log("--这是一个假的方法!")
    },
    handleSuperQuery(params, matchType) {
      //高级查询方法
      if(!params){
        this.superQueryParams=''
        this.superQueryFlag = false
      }else{
        this.superQueryFlag = true
        this.superQueryParams=JSON.stringify(params)
        this.superQueryMatchType = matchType
      }
      this.loadData(1)
    },
    getQueryParams() {
      //获取查询条件
      let sqp = {}
      if(this.superQueryParams){
        sqp['superQueryParams']=encodeURI(this.superQueryParams)
        sqp['superQueryMatchType'] = this.superQueryMatchType
      }
      if(this.sys_depart_orgcode){
        sqp['sys_depart_orgcode'] = this.sys_depart_orgcode
      }
      var param = Object.assign(sqp, this.queryParam, this.isorter ,this.filters);
      param.field = this.getQueryField();
      param.pageNo = this.ipagination.current;
      param.pageSize = this.ipagination.pageSize;
      console.log(param)
      console.log(filterObj(param))
      return filterObj(param);
    },
    getQueryField() {
      //TODO 字段权限控制++++++
      var str = "id,";
      this.columns.forEach(function (value) {
        str += "," + value.dataIndex;
      });
      return str;
    },

    onSelectChange(selectedRowKeys, selectionRows) {
      this.selectedRowKeys = selectedRowKeys;
      this.selectionRows = selectionRows;
    },
    onClearSelected() {
      this.selectedRowKeys = [];
      this.selectionRows = [];
    },
    searchQuery() {
      this.loadData(1);
    },
    superQuery() {
      this.$refs.superQueryModal.show();
    },
    searchReset() {
      this.queryParam = {}
      this.loadData(1);
    },
    batchDel: function () {
      if(!this.url.deleteBatch){
        this.$message.error("请设置url.deleteBatch属性!")
        return
      }
      if (this.selectedRowKeys.length <= 0) {
        this.$message.warning('请选择一条记录！');
        return;
      } else {
        var ids = "";
        for (var a = 0; a < this.selectedRowKeys.length; a++) {
          ids += this.selectedRowKeys[a] + ",";
        }
        var that = this;
        this.$confirm({
          title: "确认删除",
          content: "是否删除选中数据?",
          onOk: function () {
            that.loading = true;
            deleteAction(that.url.deleteBatch, {ids: ids}).then((res) => {
              if (res.success) {
               //重新计算分页问题
                that.reCalculatePage(that.selectedRowKeys.length)
                that.$message.success(res.message);
                that.loadData();
                that.onClearSelected();
              } else {
                that.$message.warning(res.message);
              }
            }).finally(() => {
              that.loading = false;
            });
          }
        });
      }
    },
    handleDelete: function (id) {
      if(!this.url.delete){
        this.$message.error("请设置url.delete属性!")
        return
      }
      var that = this;
      deleteAction(that.url.delete, {id: id}).then((res) => {
        if (res.success) {
		   //重新计算分页问题
          that.reCalculatePage(1)
          that.$message.success(res.message);
          that.loadData();
        } else {
          that.$message.warning(res.message);
        }
      });
    },
	reCalculatePage(count){
      //总数量-count
      let total=this.ipagination.total-count;
      //获取删除后的分页数
      let currentIndex=Math.ceil(total/this.ipagination.pageSize);
      //删除后的分页数<所在当前页
      if(currentIndex<this.ipagination.current){
        this.ipagination.current=currentIndex;
      }
      console.log('currentIndex',currentIndex)
    },
    handleEditAdd: function (record) {
      console.log(record)
      this.$refs.modalForm.editAdd(record);
      this.$refs.modalForm.title = "复制录入";
      this.$refs.modalForm.disableSubmit = false;
    },
    handleEdit: function (record) {
      console.log(record)
      this.$refs.modalForm.edit(record);
      this.$refs.modalForm.title = "编辑";
      this.$refs.modalForm.disableSubmit = false;
    },
    handleAdd: function () {
      this.$refs.modalForm.add();
      this.$refs.modalForm.title = "新增";
      this.$refs.modalForm.disableSubmit = false;
    },
    handlePrint:function (){
      this.$refs.modalForm.Print();
      this.$refs.modalForm.title = "打印";
      this.$refs.modalForm.disableSubmit = false;
    },
    handleTableChange(pagination, filters, sorter) {
      //分页、排序、筛选变化时触发
      //TODO 筛选
      if (Object.keys(sorter).length > 0) {
        this.isorter.column = sorter.field;
        this.isorter.order = "ascend" == sorter.order ? "asc" : "desc"
      }
      this.ipagination = pagination;
      this.loadData();
    },
    handleToggleSearch(){
      this.toggleSearchStatus = !this.toggleSearchStatus;
    },
    // 给popup查询使用(查询区域不支持回填多个字段，限制只返回一个字段)
    getPopupField(fields){
      return fields.split(',')[0]
    },
    modalFormOk() {
      // 新增/修改 成功时，重载列表
      this.loadData();
	  //清空列表选中
	  this.onClearSelected()
    },
    handleDetail:function(record){
      this.$refs.modalForm.edit(record);
      this.$refs.modalForm.title="详情";
      this.$refs.modalForm.disableSubmit = true;
    },
    // /* 单个搅拌桩导出 */
    // handleExportXls3(){
    //   let params = this.getQueryParams();
    //   console.log(params.shebeino,"设备名称")
    //   if (params.shebeino === undefined  ){
    //     this.$message.error("请选择设备名称")
    //   }else {
    //   //console.log("paramsStr",params)
    //   let url = `${window._CONFIG['domianURL']}/${this.url.exportXlsUrl1}?start=${params.datatime_begin}&end=${params.datatime_end}&shebeino=${params.shebeino}&token=${this.token}`;
    //   window.open(url);
    //   }
    // },


     URLencode(sStr) {
       return escape(sStr).replace(/\+/g, '%2B').replace(/\ /g,'%20').replace(/\'/g, '%27').replace(/\//g,'%2F')
     },

    /* 导出 */
    handleExportXls2(){
      let params = this.getQueryParams();
      if(params.pileTime_begin == null && params.pileTime_end == null ){
        this.$message.loading("今日成桩数据导出")
      }
      //console.log("paramsStr",params)
      var s=this.URLencode(params.pileMileage)
      // var s = encodeURIComponent(params.pileMileage);
      let url = `${window._CONFIG['domianURL']}/${this.url.exportXlsUrl}?start=${params.pileTime_begin}&end=${params.pileTime_end}&shebeino=${params.shebeino}&piletype=0&mileage=`+s+`&token=${this.token}`;

      setTimeout(()=>{
        window.open(url);
      },800)
    },
    handleExportXlscar(){
      let params = this.getQueryParams();
      if(params.rundate_begin == null || params.rundate_end == null || params.shebeiNo == null){
        this.$message.loading("请选择设备和时间范围！")
        return
      }
      console.log("paramsStr",params)
      let url = `${window._CONFIG['domianURL']}/${this.url.exportXlsUrl}?start=${params.rundate_begin}&end=${params.rundate_end}&shebeino=${params.shebeiNo}&token=${this.token}`;

      setTimeout(()=>{
        window.open(url);
      },800)
    },
    handleExportXls2h(){
      let params = this.getQueryParams();
      //console.log("paramsStr",params)
      if(params.pileTime_begin == null && params.pileTime_end == null ){
        this.$message.loading("今日成桩数据导出")
      }
      var s=this.URLencode(params.pileMileage)
      let url = `${window._CONFIG['domianURL']}/${this.url.exportXlsUrl}?start=${params.pileTime_begin}&end=${params.pileTime_end}&shebeino=${params.shebeino}&piletype=1&mileage=`+s+`&token=${this.token}`;
      setTimeout(()=>{
        window.open(url);
      },800)
    },
    printXls(){//打印功能需要先去报表设计页面设计打印格式
      if(this.selectedRowKeys.length!==1){
        let param = this.getQueryParams();
       console.log(param,"打印信息")
        this.$message.error("请选择一条任务单进行打印")
      }else if(this.selectedRowKeys.length==1){//?paramsStr=${paramsStr}
        let param = this.getQueryParams();
        param['selections'] = this.selectedRowKeys.join(",")
        console.log(param,"打印信息")
        let url = `${window._CONFIG['domianURL']}/${this.url.printUrl}?id=${param.selections}&token=${this.token}`;
        window.open(url);
      }
    },
    handleExportXls(fileName){
      if(!fileName || typeof fileName != "string"){
        fileName = "导出文件"
      }
      let param = this.getQueryParams();
      if(this.selectedRowKeys && this.selectedRowKeys.length>0){
        param['selections'] = this.selectedRowKeys.join(",")
      }
      console.log("导出参数",param)
      downFile(this.url.exportXlsUrl,param).then((data)=>{
        if (!data) {
          this.$message.warning("文件下载失败")
          return
        }
        if (typeof window.navigator.msSaveBlob !== 'undefined') {
          window.navigator.msSaveBlob(new Blob([data],{type: 'application/vnd.ms-excel'}), fileName+'.xls')
        }else{
          let url = window.URL.createObjectURL(new Blob([data],{type: 'application/vnd.ms-excel'}))
          let link = document.createElement('a')
          link.style.display = 'none'
          link.href = url
          link.setAttribute('download', fileName+'.xls')
          document.body.appendChild(link)
          link.click()
          document.body.removeChild(link); //下载完成移除元素
          window.URL.revokeObjectURL(url); //释放掉blob对象
        }
      })
    },
    /* 导入 */
    handleImportExcel(info){
	  this.loading = true;
      if (info.file.status !== 'uploading') {
        console.log(info.file, info.fileList);
      }
      if (info.file.status === 'done') {
		 this.loading = false;
        if (info.file.response.success) {
          // this.$message.success(`${info.file.name} 文件上传成功`);
          if (info.file.response.code === 201) {
            let { message, result: { msg, fileUrl, fileName } } = info.file.response
            let href = window._CONFIG['domianURL'] + fileUrl
            this.$warning({
              title: message,
              content: (
                <div>
                  <span>{msg}</span><br/>
                  <span>具体详情请 <a href={href} target="_blank" download={fileName}>点击下载</a> </span>
                </div>
              )
            })
          } else {
            this.$message.success(info.file.response.message || `${info.file.name} 文件上传成功`)
          }
          this.loadData()
        } else {
          this.$message.error(`${info.file.name} ${info.file.response.message}.`);
        }
      } else if (info.file.status === 'error') {
		 this.loading = false;
        if (info.file.response.status === 500) {
          let data = info.file.response
          const token = Vue.ls.get(ACCESS_TOKEN)
          if (token && data.message.includes("Token失效")) {
            this.$error({
              title: '登录已过期',
              content: '很抱歉，登录已过期，请重新登录',
              okText: '重新登录',
              mask: false,
              onOk: () => {
                store.dispatch('Logout').then(() => {
                  Vue.ls.remove(ACCESS_TOKEN)
                  window.location.reload();
                })
              }
            })
          }
        } else {
          this.$message.error(`文件上传失败: ${info.file.msg} `);
        }
      }
    },
    /* 图片预览 */
    getImgView(text){
      if(text && text.indexOf(",")>0){
        text = text.substring(0,text.indexOf(","))
      }
      return getFileAccessHttpUrl(text)
    },
    /* 文件下载 */
    // update--autor:lvdandan-----date:20200630------for：修改下载文件方法名uploadFile改为downloadFile------
    downloadFile(text){
      if(!text){
        this.$message.warning("未知的文件")
        return;
      }
      if(text.indexOf(",")>0){
        text = text.substring(0,text.indexOf(","))
      }
      let url = getFileAccessHttpUrl(text)
      window.open(url);
    },
  }

}