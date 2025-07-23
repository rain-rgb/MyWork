<template>
  <j-modal
    title="料位记录"
    :width="800"
    :visible="visible"
    @ok="handleOk1"
    @cancel="handleCancel1"
    cancelText="关闭">
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
      @change="handleTableChange">

    </a-table>
  </j-modal>
</template>

<script>

  import { httpAction, getAction } from '@/api/manage'
  import { validateDuplicateValue } from '@/utils/util'
  import {JeecgListMixin} from "@/mixins/JeecgListMixin";
  import {mixinDevice} from "@/utils/mixin";

  export default {
    name: 'WzliaocangChangeFormList',
     mixins:[JeecgListMixin, mixinDevice],
    components: {
    },
    props: {
      //表单禁用
      disabled: {
        type: Boolean,
        default: false,
        required: false
      }
    },
    data () {
      return {
          description: 'wzliaocang_change管理页面',
          queryParam:{
            liaoweino:"",
            column: "id",
            order: "desc",
            pageSize:10,
            pageNo:1
          },
          visible:false,
          // 表头
          columns: [
            {
              title: '#',
              dataIndex: '',
              key:'rowIndex',
              width:60,
              align:"center",
              customRender:function (t,r,index) {
                return parseInt(index)+1;
              }
            },
            {
              title:'料位编号',
              align:"center",
              dataIndex: 'liaoweino'
            },
            {
              title:'料位重量（kg）',
              align:"center",
              dataIndex: 'lcWeight'
            },
            {
              title:'说明',
              align:"center",
              dataIndex: 'remark'
            },
            {
              title:'操作人',
              align:"center",
              dataIndex: 'usepeople'
            },
            {
              title:'时间',
              align:"center",
              dataIndex: 'createTime'
            },
          ],
          url: {
            list: "/wzliaocang/wzliaocangChange/list",
            delete: "/wzliaocang/wzliaocangChange/delete",
            deleteBatch: "/wzliaocang/wzliaocangChange/deleteBatch",
            exportXlsUrl: "/wzliaocang/wzliaocangChange/exportXls",
            importExcelUrl: "wzliaocang/wzliaocangChange/importExcel",

          },
          dictOptions:{},
          superFieldList:[],

      }
    },
    computed: {
      formDisabled(){
        return this.disabled
      },
    },
    created () {
       //备份model原始值
      this.modelDefault = JSON.parse(JSON.stringify(this.model));
    },
    methods: {
      handleCancel1 () {
      //  this.$emit('close');
        this.visible = false;
      },
      handleOk1 () {
        this.visible = false;
      },
      add () {
        this.edit(this.modelDefault);
      },
      edit (record) {
        this.model = Object.assign({}, record);
        this.queryParam.liaoweino =  record.liaoweino
        getAction(this.url.list, this.queryParam).then((res) => {
          console.log(res)
          if (res.success) {
            //update-begin---author:zhangyafei    Date:20201118  for：适配不分页的数据列表------------
            this.dataSource = res.result.records || res.result;
            if (res.result.total) {
              console.log(res.result)
              this.ipagination.total = res.result.total;
            } else {
              this.ipagination.total = 0;
            }
            //update-end---author:zhangyafei    Date:20201118  for：适配不分页的数据列表------------
          } else {
            this.$message.warning(res.message)
          }
        }).finally(() => {
          this.loading = false
        })
        this.visible = true;
      },
      submitForm () {
        const that = this;
        // 触发表单验证
        this.$refs.form.validate(valid => {
          if (valid) {
            that.confirmLoading = true;
            let httpurl = '';
            let method = '';
            if(!this.model.id){
              httpurl+=this.url.add;
              method = 'post';
            }else{
              httpurl+=this.url.edit;
               method = 'put';
            }
            httpAction(httpurl,this.model,method).then((res)=>{
              if(res.success){
                that.$message.success(res.message);
                that.$emit('ok');
              }else{
                that.$message.warning(res.message);
              }
            }).finally(() => {
              that.confirmLoading = false;
            })
          }
         
        })
      },
    }
  }
</script>