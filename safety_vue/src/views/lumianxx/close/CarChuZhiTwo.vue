<template>
  <div>
    <a-modal
      title="超标处理"
      :width="width"
      :visible="visible"
      :confirm-loading="confirmLoading"
      @ok="handleOk"
      @cancel="handleCancel"
    >

      <div class="reason">超标原因：{{ modalObj.reason }}</div>
      <a-card v-if="level>1" title="处理信息" :bordered="false" :headStyle="{ color: '#0785fd' }">
        <a-form :form="form">
          <a-row>
            <a-col :span="12">
              <a-form-item label="问题原因" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input placeholder="无" v-model="dealObj.problemReasons" disabled></a-input>
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="处理方式" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input placeholder="无" v-model="dealObj.handleWay" disabled></a-input>
              </a-form-item>
            </a-col>
          </a-row>
          <a-row>
            <a-col :span="12">
              <a-form-item label="处理结果" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input placeholder="无" v-model="dealObj.handleResult" disabled></a-input>
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="处理时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input placeholder="无" v-model="dealObj.handleTime" disabled></a-input>
              </a-form-item>
            </a-col>
          </a-row>
          <a-row>
            <a-col :span="12">
              <a-form-item label="处置人" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input placeholder="无" v-model="dealObj.handlePerson" disabled></a-input>
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="附件" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <viewer :images="detailList">
                  <img
                    v-for="(item, index) in detailList"
                    :key="index"
                    style="height: 100px; width: 100px; margin: 5px 10px 5px 10px; float: left"
                    :src="[item.icon] "
                    alt=""
                  />
                </viewer>
              </a-form-item>
            </a-col>
          </a-row>
          
              <a-table
                ref="table"
                size="middle"
                bordered
                rowKey="id"
                class="j-table-force-nowrap"
                :scroll="{ x: true }"
                :columns="columns"
                :dataSource="dataSource"
                :pagination="false"
                :loading="loading"
                @change="tableChange"
              >
                <span slot="StationTags" slot-scope="StationTags, record">
                  <span>{{ record.startstation | getStation }}</span>
                  <span> - </span>
                  <span>{{ record.endstation | getStation }}</span>
                </span>
                <span slot="tags1" slot-scope="tags1, record">
                  <span v-if="record.workstat == '0'">施工中</span>
                  <span v-if="record.workstat == '1'">已完工</span>
                </span>
                <span slot="tags3" slot-scope="tags3, record">
                  <a-tag color="green" v-if="record.standard == 0">合格</a-tag>
                  <a-tag color="orange" v-if="record.standard == 1">初级超标</a-tag>
                  <a-tag color="yellow" v-if="record.standard == 2">中级超标</a-tag>
                  <a-tag color="red" v-if="record.standard == 3">高级超标</a-tag>
                </span>
                <span slot="tags4" slot-scope="tags4, record">
                  <a-tag color="red" v-if="record.id == dataSource1[0].id">整改前</a-tag>
                  <a-tag color="green" v-else>整改后</a-tag>
                </span>
              </a-table>
        </a-form>
      </a-card>

      <a-card v-if="level==3" title="监理信息" :bordered="false" :headStyle="{ color: '#0785fd' }">
        <a-form :form="form">
          <a-row>
            <a-col  :span="12">
              <a-form-item label="监理审批" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input placeholder="无" v-model="dealObj.supervisorApproval" disabled></a-input>
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="监理结果" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input placeholder="无" v-model="dealObj.supervisorResult" disabled></a-input>
              </a-form-item>
            </a-col>
          </a-row>
          <a-row>
            <a-col :span="12">
              <a-form-item label="监理处理时间" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input placeholder="无" v-model="dealObj.supervisorHandleTime" disabled></a-input>
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="审核人" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-input placeholder="无" v-model="dealObj.approvalPerson" disabled></a-input>
              </a-form-item>
            </a-col>
          </a-row>
          <a-row>
            <a-col :span="12">
              <a-form-item label="附件" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <viewer :images="detailList1" v-if="detailList1[0].icon">
                  <img
                    v-for="(item, index) in detailList1"
                    :key="index"
                    style="height: 100px; width: 100px; margin: 5px 10px 5px 10px; float: left"
                    :src="[item.icon]"
                    alt=""
                  />
                </viewer>
              </a-form-item>
            </a-col>
          </a-row>
        </a-form>
      </a-card>

      <a-card v-if="level == 1" title="施工方处置" :bordered="false" :headStyle="{ color: '#0785fd' }">
        <a-form-model ref="formModel" :form="form" :model="formModel">
          <a-row>
            <a-col :span="12">
              <a-form-model-item
                label="问题原因"
                :rules="[{ required: true, message: '问题原因不能为空' }]"
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
                prop="wtyy"
              >
                <a-textarea
                  placeholder="请输入问题原因"
                  v-model="formModel.wtyy"
                  :auto-size="{ minRows: 5, maxRows: 20 }"
                ></a-textarea>
              </a-form-model-item>
            </a-col>
            <a-col :span="12">
              <a-form-model-item
                label="处理方式"
                :rules="[{ required: true, message: '处理方式不能为空' }]"
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
                prop="clfs"
              >

                <a-auto-complete v-model="formModel.clfs" placeholder="请输入处理方式">
                </a-auto-complete>
              </a-form-model-item>
            </a-col>
          </a-row>
          <a-row>
            <a-col :span="12">
              <a-form-model-item
                label="处理结果"
                :rules="[{ required: true, message: '处理结果不能为空' }]"
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
                prop="cljg"
              >

                  <a-auto-complete v-model="formModel.cljg" placeholder="请输入处理结果">

                  </a-auto-complete>
              </a-form-model-item>
            </a-col>
            <a-col :span="12">
              <a-form-model-item label="上传图片" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <j-image-upload v-model="fileList" :isMultiple="isMultiple"></j-image-upload>
              </a-form-model-item>
            </a-col>
          </a-row>
          <a-row>
            <a-col :span="12">
              <a-button @click="reformData" style="margin-bottom:20px">选择整改数据</a-button>
            </a-col>
          </a-row>
              <a-table
                ref="table"
                size="middle"
                bordered
                rowKey="id"
                class="j-table-force-nowrap"
                :scroll="{ x: true }"
                :columns="columns"
                :dataSource="dataSource1"
                :pagination="false"
                :loading="loading"
                @change="tableChange"
              >
                <span slot="StationTags" slot-scope="StationTags, record">
                  <span>{{ record.startstation | getStation }}</span>
                  <span> - </span>
                  <span>{{ record.endstation | getStation }}</span>
                </span>
                <span slot="tags1" slot-scope="tags1, record">
                  <span v-if="record.workstat == '0'">施工中</span>
                  <span v-if="record.workstat == '1'">已完工</span>
                </span>
                <span slot="tags3" slot-scope="tags3, record">
                  <a-tag color="green" v-if="record.standard == 0">合格</a-tag>
                  <a-tag color="orange" v-if="record.standard == 1">初级超标</a-tag>
                  <a-tag color="yellow" v-if="record.standard == 2">中级超标</a-tag>
                  <a-tag color="red" v-if="record.standard == 3">高级超标</a-tag>
                </span>
                <span slot="tags4" slot-scope="tags4, record">
                  <a-tag color="red" v-if="record.id == dataSource1[0].id">整改前</a-tag>
                  <a-tag color="green" v-else>整改后</a-tag>
                </span>
              </a-table>
        </a-form-model>
      </a-card>

      <a-card v-if="level == 2" title="监理审核" :bordered="false" :headStyle="{ color: '#0785fd' }">
        <a-form-model ref="formModel" :form="form" :rules='rules' :model="formModel">
          <a-row v-show="isAgree == 2">
            <a-col :span="12">
              <a-form-model-item
                label="监理驳回原因"
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
                prop="jlbh"
              >
                <a-input placeholder="请输入监理驳回原因" v-model="formModel.jlbh"></a-input>
              </a-form-model-item>
            </a-col>
          </a-row>
          <a-row v-show="isAgree == 1">
            <a-col :span="12">
              <a-form-model-item
                label="监理审批"
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
                prop="spyj"
              >

                <a-auto-complete v-model="formModel.spyj" placeholder="请选择监理审批">

                </a-auto-complete>
              </a-form-model-item>
            </a-col>
            <a-col :span="12">
              <a-form-model-item
                label="监理结果"
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
                prop="spjg"
              >

                <a-auto-complete v-model="formModel.spjg" placeholder="请选择监理结果">

                </a-auto-complete>
              </a-form-model-item>
            </a-col>
          </a-row>
          <a-row>
            <a-col :span="12">
              <a-form-model-item label="是否同意" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-radio-group v-model="isAgree">
                  <a-radio :value="1">是</a-radio>
                  <a-radio :value="2">否</a-radio>
                </a-radio-group>
              </a-form-model-item>
            </a-col>
            <a-col :span="12">
              <a-form-model-item label="上传图片" :labelCol="labelCol" :wrapperCol="wrapperCol" v-show="isAgree == 1">
                <j-image-upload v-model="fileList1" :isMultiple="isMultiple"></j-image-upload>
              </a-form-model-item>
            </a-col>
          </a-row>
        </a-form-model>
      </a-card>

      <a-card v-if="level == 3" title="指挥部审核" :bordered="false" :headStyle="{ color: '#0785fd' }">
        <a-form-model ref="formModel" :form="form" :rules="rules" :model="formModel">
          <a-row>
            <a-col :span="12">
              <a-form-model-item label="是否同意" :labelCol="labelCol" :wrapperCol="wrapperCol">
                <a-radio-group v-model="isAgree">
                  <a-radio :value="1">是</a-radio>
                  <a-radio :value="2">否</a-radio>
                </a-radio-group>
              </a-form-model-item>
            </a-col>
          </a-row>
          <a-row v-show="isAgree == 2">
            <a-col :span="12">
              <a-form-model-item label="指挥部驳回原因" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="zhbbh">
                <a-input placeholder="请输入指挥部驳回原因" v-model="formModel.zhbbh"></a-input>
              </a-form-model-item>
            </a-col>
          </a-row>
          <a-row v-show="isAgree == 1">
            <a-col :span="12">
              <a-form-model-item label="指挥部审核" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="zhbyj">

                <a-auto-complete v-model="formModel.zhbyj" placeholder="请选择指挥部审核">
                </a-auto-complete>
              </a-form-model-item>
            </a-col>
            <a-col :span="12">
              <a-form-model-item label="指挥部结果" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="zhbjg">
                <a-auto-complete v-model="formModel.zhbjg" placeholder="请选择指挥部结果">
                </a-auto-complete>
              </a-form-model-item>
            </a-col>
          </a-row>
        </a-form-model>
      </a-card>

      
    <a-modal
      title="整改数据"
      :width="widthRe"
      :visible="visibleRe"
      :confirm-loading="confirmLoading"
      @ok="handleOkRe"
      @cancel="handleCancelRe"
    >

      <a-table
        ref="table"
        size="middle"
        bordered
        rowKey="id"
        :scroll="{ x: true }"
        :columns="columns"
        :dataSource="dataSource"
        :pagination="ipagination"
        :loading="loading"
        :rowSelection="{ selectedRowKeys: selectedRowKeys, onChange: onSelectChange, type: 'radio' }"
        @selection-change="onSelectChange"
        :customRow="rowClick"
        @change="tableChange"
      >
        <span slot="StationTags" slot-scope="StationTags, record">
          <span>{{ record.startstation | getStation }}</span>
          <span> - </span>
          <span>{{ record.endstation | getStation }}</span>
        </span>
        <span slot="tags1" slot-scope="tags1, record">
          <span v-if="record.workstat == '0'">施工中</span>
          <span v-if="record.workstat == '1'">已完工</span>
        </span>
        <span slot="tags3" slot-scope="tags3, record">
          <a-tag color="green" v-if="record.standard == 0">合格</a-tag>
          <a-tag color="orange" v-if="record.standard == 1">初级超标</a-tag>
          <a-tag color="yellow" v-if="record.standard == 2">中级超标</a-tag>
          <a-tag color="red" v-if="record.standard == 3">高级超标</a-tag>
        </span>
      </a-table>
    </a-modal>
    </a-modal>
  </div>
</template>
<script>
import { getAction,postAction } from '@/api/manage'
import JImageUpload from '@/components/jeecg/JImageUpload'
import { FormTypes, getRefPromise } from '@/utils/JEditableTableUtil'

export default {
  name: 'CarChuZhiTwo',
  components: { JImageUpload },
  // props: ['batchNo', 'bhz'],
  data() {
    return {
      level:1,
      sonloading:false,
      dealObj: {},
      form: this.$form.createForm(this),
      fileList:[],
      fileList1:"",
      detailList: [],
      detailList1:[],
      isAgree: 1,
      isMultiple: true,
      width: 1200,
      widthRe: 1100,
      visible: false,
      confirmLoading: false,
      visibleRe: false,
      formModel: {
        cljg: '',
        wtyy: '',
        clfs: '',
      },
      labelCol: {
        xs: { span: 24 },
        sm: { span: 6 },
      },
      wrapperCol: {
        xs: { span: 24 },
        sm: { span: 16 },
      },
      modalObj: {},
      url: {
        // carHandle: '/czsh/bhzCementOverHandler/carHandle',
        deallist1: '/hctfysworkareaoverhandler/hcTfysworkareaOverHandler/deallist1',
        // list1: '/czsh/bhzCementOverHandler/list',
        list1: '/hctfysworkareaoverhandler/hcTfysworkareaOverHandler/list',
        tableList1: '/lqbhz/bhzLqBases/queryBhzLqCailiaoByMainId', // baseGuid
        tableList2: '/swbhz/bhzSwBases/queryBhzSwCailiaoByMainId', // baseGuid
        kuanglist: '/bhzTerminology/bhzTerminology/list',
        hcTfysworkarea: '/hc_tfysworkarea/hcTfysworkarea/list1',//选择整改数据 土方压实选择框
      },
      columns: [
        {
          title: "施工桩号",
          align: "center",
          dataIndex: "startstation",
          scopedSlots: { customRender: "StationTags" },
        },
        {
          title: "施工时间",
          align: "center",
          dataIndex: "starttime",
          scopedSlots: { customRender: "sgTime" },
        },
        {
          title: "施工长度(m)",
          align: "center",
          dataIndex: "workmile",
        },
        {
          title: "平均厚度(cm)",
          align: "center",
          dataIndex: "thickavg",
        },
        {
          title: '施工状态',
          align: 'center',
          dataIndex: 'workstat',
          scopedSlots: { customRender: 'tags1' },
        },
        {
          title: "预警状态",
          align: "center",
          dataIndex: "standard",
          scopedSlots: { customRender: "tags3" },
        },
        {
          title: "整改状态",
          align: "center",
          dataIndex: "id",
          scopedSlots: { customRender: "tags4" },
        },
      ],
      ipagination: {
        current: 1,
        pageSize: 10,
        pageSizeOptions: ["10", "20", "30"],
        showTotal: (total, range) => {
          return range[0] + "-" + range[1] + " 共" + total + "条";
        },
        showQuickJumper: true,
        showSizeChanger: true,
        total: 0,
      },
      dataSource:[],
      dataSource1:[],
      loading: false,
      selectedRowKeys: [],
      chooseDataReList:null,
    }
  },
  filters: {
    getStation(startstation) {
      let startsta = null;
      if (startstation != null) {
        if (startstation.length == 2) {
          startsta = "K0+0" + startstation;
        } else if (startstation.length == 3) {
          startsta = "K0+" + startstation;
        } else if (startstation.length > 3) {
          let substring = startstation.substring(0, startstation.length - 3);
          let substring1 = startstation.substring(startstation.length - 3);
          startsta = "K" + substring + "+" + substring1;
        }
      }
      return startsta;
    },
  },
  mounted() {},
  methods: {
    reformData(){
      this.visibleRe = true;

      this.reformGetData();
    },
    reformGetData: function () {
      this.dataSource  = [];
      this.loading = true;
      let params = {
        standard: `0`,
        pageSize: this.ipagination.pageSize,
        pageNo: this.ipagination.current,
        blockid:this.modalObj.czblockid,
        projectid:this.modalObj.projectid,
        sectionid:this.modalObj.sectionid,
        startstation:this.modalObj.startstation,
        endstation:this.modalObj.endstation,
      }
      getAction(this.url.hcTfysworkarea, params).then((res) => {
        if (res.success) {
          this.dataSource = res.result.records;
          if (this.modalObj.czblockid) {
            this.dataSource.unshift(this.modalObj)
          }
          if (res.result.total) {
            this.ipagination.total = res.result.total;
          } else {
            this.ipagination.total = 0;
          }
          this.loading = false;
          if (this.dataSource1.length>1) {
            // console.log(this.dataSource1,'保存上次选中的数据');
            this.selectedRowKeys = [this.dataSource1[1].id];//保存上次选中的数据
          }
        } else {
          this.dataSource = null;
          this.loading = false;
          this.$message.warning(res.message);
        }
      })
    },
    tableChange(pagination) {
      this.ipagination.current = pagination.current;
      this.ipagination.pageSize = pagination.pageSize;
      // this.loadData();
      this.reformGetData();
    },
    handledata: function () {
      this.dealObj  = [];
      this.detailList  = [];
      this.detailList1  = [];
      let params = { baseid: this.modalObj.blockid }
      getAction(this.url.list1, params).then((res) => {
        if (res.success) {
          if (res.result.records.length > 0) {
            this.dealObj = res.result.records[0]
            if (this.dealObj.filePath !== null) {
              var filePath1 = this.dealObj.filePath.split(',')
              filePath1.forEach(re => {
                this.detailList.push({ icon: re })
              })
              //console.log('filePath1', filePath1)
            }
            if (this.dealObj.filePath2 !== null){
              var filePath3 = this.dealObj.filePath2.split(',')
              filePath3.forEach(re => {
                this.detailList1.push({ icon: re })
              })
            }
            //console.log("this.bhzCementOverHandler", res.result)
          } else {
            this.$message.warn('暂无超标处理信息！')
          }
        }
      })
    },

    showModal(record,level) {
      this.level = level
      this.fileList = []
      this.modalObj = Object.assign({}, record)
     // this.getclfslist()
      this.visible = true;

      this.reformGetData();//处理信息 table

      this.dataSource1 = [this.modalObj];//整改数据 table数据初始化
      this.chooseDataReList = null,//整改数据 选中初始化
      this.selectedRowKeys = [];//整改数据 选中初始化

      this.$nextTick(()=>{
        this.handledata()
        this.$refs.formModel.resetFields()
      })
    },
    subcommit() {
      this.$refs.formModel.validate((valid) => {//施工方处置
        if (valid) {
          console.log(this.dataSource1,this.dataSource1[1].blockid,'111111111111111111111');
          let params ={
            handleResult: this.formModel.cljg,
            problemReasons: this.formModel.wtyy,
            handleWay: this.formModel.clfs,
            baseid: this.modalObj.blockid,
            filePath: this.fileList != []?this.fileList:"",
            status: 10,
            startstation:this.modalObj.startstation,
            standard:this.modalObj.standard,
            czblockid:this.dataSource1[1].blockid,
          }
          postAction(this.url.deallist1, params).then((res) => {
            if (res.success) {
              this.$message.success('处置成功')
            } else {
              this.$message.error('处置失败')
            }
            this.confirmLoading = false
            this.visible = false
            this.$emit('change', true)
          })
        } else {
          this.confirmLoading = false
          this.$message.error('请填写必填项')
          return false
        }
      })
      // this.routeReload();
    },

    subcommit2() {
      this.$refs.formModel.validate((valid) => {
        if (valid) {
          let params = {}
          if (this.isAgree == 1) {
            params = {
              supervisorApproval: this.formModel.spyj,
              supervisorResult: this.formModel.spjg,
              filePath2: this.fileList1 != []?this.fileList1:"",
              baseid: this.modalObj.blockid,
              status: 40,
              startstation:this.modalObj.startstation,
              standard:this.modalObj.standard,
            }
          } else {
            params = {
              remark: this.formModel.jlbh,
              baseid: this.modalObj.blockid,
              status: 30,
              startstation:this.modalObj.startstation,
              standard:this.modalObj.standard,
            }
          }
          postAction(this.url.deallist1, params).then((res) => {
            if (res.success) {
              this.$message.success('审核成功')
            } else {
              this.$message.error('审核失败')
            }
            this.confirmLoading = false
            this.visible = false
            this.$emit('change', true)
          })
          // this.routeReload();
        } else {
          this.confirmLoading = false
          this.$message.error('请填写必填项');
          return false
        }
      })
    },

    subcommit3() {
      this.$refs.formModel.validate((valid) => {
        if (valid) {
          let params = {}
          if (this.isAgree == 1) {
            params = {
              headquartersApproval: this.formModel.zhbyj,
              headquartersResult: this.formModel.zhbjg,
              baseid: this.modalObj.blockid,
              // filePath3:"",
              status: 50,
              startstation:this.modalObj.startstation,
              standard:this.modalObj.standard,
            }
          } else {
            params = {
              headquartersRemark: this.formModel.zhbbh,
              baseid: this.modalObj.blockid,
              status: 60,
              startstation:this.modalObj.startstation,
              standard:this.modalObj.standard,
            }
          }
          postAction(this.url.deallist1, params).then((res) => {
            if (res.success) {
              this.$message.success('审批成功')
            } else {
              this.$message.error('审批失败')
            }
            this.confirmLoading = false
            this.visible = false
            this.$emit('change', true)
          })
          // this.routeReload();
        } else {
          this.confirmLoading = false
          console.log('请填写必填项')
          return false
        }
      })
    },
    routeReload() {
      //刷新页面
      this.reloadFlag = false
      let ToggleMultipage = 'ToggleMultipage'
      this.$store.dispatch(ToggleMultipage, false)
      this.$nextTick(() => {
        this.$store.dispatch(ToggleMultipage, true)
        this.reloadFlag = true
      })
      console.log('刷新页面')
    },
    handleOk() {
      if(this.level == 1){
        if (this.dataSource1.length < 2) {
          this.$message.error('未选择整改数据');
          return false;
        }
        if (this.fileList.length == 0) {
          this.$message.error('请上传附件图片！')
          return false
        }
        this.subcommit()
      }else if(this.level == 2){
        this.subcommit2()
      }else{
        this.subcommit3()
      }
      this.confirmLoading = true
      this.routeReload()
    },
    handleCancel() {
      this.visible = false
      this.$emit('change', false)
    },
    handleOkRe() {
      this.visibleRe = false
      let dataTar = this.dataSource1[0];
      this.dataSource1 = [];
      this.dataSource1 = [dataTar,this.chooseDataReList];
    },
    handleCancelRe() {
      this.visibleRe = false
    },
    rowClick: function (record) {
      return {
        on: {
          click: () => {
            // this.targetList = JSON.parse(JSON.stringify(record));
            this.selectedRowKeys = [record.id];
            // this.isCheck = false;
            this.chooseDataRe(record);
            console.log("点击了我",this.selectedRowKeys );
          },
          dblclick:()=>{
            // this.selectedRowKeys = [index];
            // this.enter(record);
            // console.log("双击了我");
          }
        },
      };
    },
    onSelectChange(selectedRowKeys, selectionRows) {
      console.log(selectedRowKeys,'selectedRowKeys',selectionRows);
      // [this.targetList] = JSON.parse(JSON.stringify(selectionRows));
      // this.isCheck = false;
      // this.isCheck = false;
      this.selectedRowKeys = selectedRowKeys;
      let record = selectionRows[0];
      this.chooseDataRe(record);
      // this.currentRow = true;
    },
    chooseDataRe(record){
      this.chooseDataReList = record;
      console.log(record,'record-----------',record.id);
    },
  },
}
</script>
<style lang="less" scoped>
.reason {

  color: rgb(239, 11, 11);
  line-height: 30px;
  text-align: center;
  font-size: 18px;
}

::v-deep .ant-upload-picture-card-wrapper {
  padding: 0;
}
.jtable {
  text-align: center;
}
</style>