<template>
  <div>
  <j-modal
    centered
    :title="title"
    :width="1200"
    :visible="visible"
    switchFullscreen
    @ok="handleOk"
    :okButtonProps="{ class: { 'jee-hidden': disableSubmit } }"
    @cancel="handleCancel"
    cancelText="关闭"
  >
    <a-table
      :rowKey="(record, index) => index"
      size="middle"
      bordered
      :columns="columns"
      :loading="loading"
      :data-source="carAndMixingStationList"
      :pagination="false"
    >
      <!-- 使用 Vue 2 的插槽语法 -->
      <template slot="sampleNoClick" slot-scope="text, record">
        <a
          href="javascript:void(0)"
          @click="handleSampleNoClick(record)"
          class="text-blue-500 hover:underline cursor-pointer"
        >
          {{ record.sampleNo }}
        </a>
      </template>

      <span slot="action" slot-scope="text, record">
        <a @click="handleDetail(record)">详情</a>
      </span>
    </a-table>
  </j-modal>
    <SyyLjBasesModal ref="modalForm_TYH" @ok="modalFormOk"/>
    <TrGangjinbhcMModaldetail ref="modalForms_BHC"></TrGangjinbhcMModaldetail>
    <TrHnthtMModals ref="modalForms_HTJ"></TrHnthtMModals>
    <KyKzJBasesModal ref="modalForm_SNJ" @ok="modalFormOk"/>
    <WnjModal ref="modalForm_PYH" @ok="modalFormOk"/>
  </div>

</template>

<script>
import { getAction } from '@/api/manage'
import axios from 'axios'
import { base64Encode } from '@api/kkfileView'
import SyyLjBasesModal from '@views/syj/ylj/modules/SyyLjBasesModal.vue'
import TrGangjinbhcMModaldetail from '@views/syj/gangbao/modules/TrGangjinbhcMModaldetail.vue'
import TrHnthtMModals from '@views/syj/hntht/modules/TrHnthtMModals.vue'
import KyKzJBasesModal from '@views/kykzj/kykz/modules/KyKzJBasesModal.vue'
import WnjModal from '@views/syj/wnj/moduls/WnjModal.vue'

export default {
  name: 'qualityDetail',
  components: {
    KyKzJBasesModal,
    WnjModal,
    TrHnthtMModals,
    TrGangjinbhcMModaldetail,
    SyyLjBasesModal
  },
  data() {
    return {
      visible: false,
      loading: false,
      disableSubmit: true,
      title: '质量报告',
      currentProjectId: '', // 新增：存储当前项目ID
      carAndMixingStationList: [],
      columns: [
        {
          title: '试验名称',
          align: 'center',
          dataIndex: 'tiNames',
        },
        {
          title: '样品编号',
          align: 'center',
          dataIndex: 'sampleNo',
          scopedSlots: { customRender: 'sampleNoClick' },
        },
        {
          title: '查看报告',
          dataIndex: 'action',
          align: 'center',
          fixed: 'right',
          width: 147,
          scopedSlots: { customRender: 'action' },
        },
      ],
    }
  },
  methods: {
    handleCancel() {
      this.visible = false
    },
    handleOk() {
      this.visible = false
    },
    getQuality(code) {
      console.log('[DEBUG] 开始请求质量数据，参数:', code.treeid);
      this.loading = true;
      // 存储当前项目ID到组件数据中
      this.currentProjectId = code.treeid;

      let params = {
        getSampleNo: '',
        projectid: code.treeid,
      };

      console.log('[DEBUG] 请求参数:', params);
      this.carAndMixingStationList = [];

      const pureAxios = axios.create({
        paramsSerializer: (params) => {
          return Object
            .entries(params)
            .map(([key, val]) =>
              val === '' ? key : `${key}=${encodeURIComponent(val)}`
            )
            .join('&');
        }
      });

      pureAxios({
        url: 'http://121.40.163.88:8084/CATDPS/TestingMachineController.do',
        method: 'get',
        params
      })
        .then((res) => {
          console.log('[DEBUG] 响应数据:', res);
          console.log('[DEBUG] 响应数据类型:', typeof res.data);

          if (res.data && res.data.msg === "未找到对应的 sampleNo") {
            console.log('[DEBUG] 未找到对应 sampleNo');
            this.$message.warning('未进行相关试验');
          } else if (res.status === 200) {
            if (res.data) {
              const dataList = res.data.obj;
              if (Array.isArray(dataList) && dataList.length > 0) {
                console.log('[DEBUG] 数据列表长度:', dataList.length);
                console.log('[DEBUG] 第一条数据内容:', dataList[0]);
                console.log('[DEBUG] 第一条数据字段:', Object.keys(dataList[0]));

                // 将 projectid 添加到每条记录中
                this.carAndMixingStationList = dataList.map(item => ({
                  ...item,
                  projectid: this.currentProjectId
                }));
                console.log('[DEBUG] 已将数据赋值给 carAndMixingStationList');
              } else {
                console.log('[DEBUG] res.data.data.obj 为空数组或非数组类型');
                this.$message.info('暂无数据');
              }
            } else {
              console.log('[DEBUG] res.data.data 不存在');
              this.$message.info('暂无数据');
            }
          } else {
            console.log('[DEBUG] 请求失败，错误信息:', res.message);
            this.$message.warning(res.message);
          }
        })
        .catch((error) => {
          console.error('[ERROR] 请求异常:', error);
          console.error('[ERROR] 请求异常详情:', error.response || error.message);
          this.$message.error('网络请求失败');
        })
        .finally(() => {
          this.loading = false;
          console.log('[DEBUG] 请求处理完成，loading 状态已重置');
        });
    },
    handleDetail(record) {
      if (record && record.file) {
        let url = window._CONFIG['onlinePreviewDomainURL'] + '?url=' + encodeURIComponent(base64Encode(record.file))
        window.open(url, '_blank')
      } else {
        // 确保使用 encodeURIComponent 对参数值进行编码
        const sampleNo = encodeURIComponent(record.sampleNo || '');
        // 如果记录中有 projectid，则使用记录中的；否则使用组件存储的
        const projectid = record.projectid || this.currentProjectId;

        if (!projectid) {
          this.$message.warning('缺少项目ID');
          return;
        }

        // 构建包含 projectid 的 URL
        const baseUrl = 'http://121.40.163.88:8084/CATDPS/TestingMachineController.do';
        const params = new URLSearchParams({
          getReportPdfUrl: '',
          sampleNo: sampleNo,
          projectid: projectid // 添加 projectid 参数
        });

        const fullUrl = `${baseUrl}?${params.toString()}`;
        console.log('[DEBUG] 打开报告URL:', fullUrl);
        window.open(fullUrl, '_blank');
      }
    },
    handleDetail_TYH: function (record) {
      this.$refs.modalForm_TYH.edit(record);
      this.$refs.modalForm_TYH.title = "详情";
      this.$refs.modalForm_TYH.disableSubmit = true;
    },
    handleDetailes_BHC(e) {
      this.$refs.modalForms_BHC.title = '详情'
      this.$refs.modalForms_BHC.call(e)
    },
    handleDetails_HTJ(e) {
      this.$refs.modalForms_HTJ.call(e)
      this.$refs.modalForms_HTJ.title = '混凝土回弹数据详情'
    },
    handleDetail_PYH: function (record) {
      this.$refs.modalForm_PYH.edit(record);
      this.$refs.modalForm_PYH.title = "详情";
      this.$refs.modalForm_PYH.disableSubmit = true;
    },
    handleDetail_SNJ: function (record) {
      this.$refs.modalForm_SNJ.edit(record);
      this.$refs.modalForm_SNJ.title = "详情";
      this.$refs.modalForm_SNJ.disableSubmit = true;
    },
    handleSampleNoClick(record) {
      const sampleNo = record.sampleNo || '';
      const sys_depart_orgcode = record.sys_depart_orgcode || '';
      const that = this; // 保存 Vue 实例的引用
      const t1 = Date.now();
      // this.carAndMixingStationList = []
      if (!sampleNo) {
        this.$message.warning('样品编号为空');
        return;
      }

      // TYH 分支处理
      if (sampleNo.includes('TYH')) {
        getAction('syj/tSyjzb/list', {
          _t: t1,
          sys_depart_orgcode: sys_depart_orgcode,
          wtbh: sampleNo
        }).then((res) => {
          console.log('[DEBUG] TYH接口响应:', res);
          if (res.code === 200) {
            const branchData = res.result.records; // 获取分支数据
            if (branchData && branchData.length > 0) {
              // 直接传递数据到子组件，不修改主数据
              this.handleDetail_TYH(branchData[0]);
            } else {
              that.$message.warning('未找到相关试验记录');
            }
          } else {
            that.$message.warning(res.message);
          }
        }).catch((err) => {
          console.error('[ERROR] TYH请求失败:', err);
          that.$message.error('获取试验数据失败');
        });

        // BHC 分支处理
      } else if (sampleNo.includes('BHC')) {
        getAction('trgangjinbhcm/trGangjinbhcM/list', {
          _t: t1,
          sys_depart_orgcode: sys_depart_orgcode,
          code: sampleNo
        }).then((res) => {
          console.log('[DEBUG] BHC接口响应:', res);
          if (res.code === 200) {
            const branchData = res.result.records; // 获取分支数据
            if (branchData && branchData.length > 0) {
              // 直接传递数据到子组件，不修改主数据
              this.handleDetailes_BHC(branchData[0]);
            } else {
              that.$message.warning('未找到相关试验记录');
            }
          } else {
            that.$message.warning(res.message);
          }
        }).catch((err) => {
          console.error('[ERROR] BHC请求失败:', err);
          that.$message.error('获取钢筋试验数据失败');
        });

        // HTJ 分支处理
      } else if (sampleNo.includes('HTJ')) {
        getAction('trhnthtm/trHnthtM/list', {
          _t: t1,
          sys_depart_orgcode: sys_depart_orgcode,
          code: sampleNo
        }).then((res) => {
          console.log('[DEBUG] HTJ接口响应:', res);
          if (res.code === 200) {
            const branchData = res.result.records; // 获取分支数据
            if (branchData && branchData.length > 0) {
              // 直接传递数据到子组件，不修改主数据
              this.handleDetails_HTJ(branchData[0]);
            }else {
              that.$message.warning('未找到相关试验记录');
            }
          } else {
            that.$message.warning(res.message);
          }
        }).catch((err) => {
          console.error('[ERROR] HTJ请求失败:', err);
          that.$message.error('获取混凝土试验数据失败');
        });

        // PYH 分支处理
      } else if (sampleNo.includes('PYH')) {
        getAction('syj/tSyjzb/list7', {
          _t: t1,
          sys_depart_orgcode: sys_depart_orgcode,
          wtbh: sampleNo
        }).then((res) => {
          console.log('[DEBUG] PYH接口响应:', res);
          if (res.code === 200) {
            const branchData = res.result.records; // 获取分支数据
            if (branchData && branchData.length > 0) {
              // 直接传递数据到子组件，不修改主数据
              this.handleDetail_PYH(branchData[0]);
            }else {
              that.$message.warning('未找到相关试验记录');
            }
          } else {
            that.$message.warning(res.message);
          }
        }).catch((err) => {
          console.error('[ERROR] PYH请求失败:', err);
          that.$message.error('获取万能试验数据失败');
        });

        // SNJ/YJL分支处理
      } else if (sampleNo.includes('SNJ')||sampleNo.includes('YJL')) {
        getAction('syj/tSyjzb/list3', {
          _t: t1,
          sys_depart_orgcode: sys_depart_orgcode,
          wtbh: sampleNo
        }).then((res) => {
          console.log('[DEBUG] SNJ/YJL接口响应:', res);
          if (res.code === 200) {
            const branchData = res.result.records; // 获取分支数据
            if (branchData && branchData.length > 0) {
              // 直接传递数据到子组件，不修改主数据
              this.handleDetail_SNJ(branchData[0]);
            }else {
              that.$message.warning('未找到相关试验记录');
            }
          } else {
            that.$message.warning(res.message);
          }
        }).catch((err) => {
          console.error('[ERROR] HTJ请求失败:', err);
          that.$message.error('获取水泥试验数据失败');
        });

        // 未知类型处理
      } else {
        this.$message.warning('未知样品类型');
      }
    }
  }
}
</script>