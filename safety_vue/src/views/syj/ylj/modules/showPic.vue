<template>
  <j-modal
    :title="title"
    :width="650"
    :visible="visible"
    :maskClosable="false"
    switchFullscreen
    @cancel="handleCancel"
  >
    <a-spin :spinning="confirmLoading">
      <a-tabs default-active-key="1">
        <a-tab-pane key="1" tab="人脸">
          <img
            v-if="facePic !== '暂无图片'"
            style="height: 450px; width: 550px; margin: 5px 10px 5px 10px; float: left"
            :src="facePic"
            alt=""
          />
          <div v-else>暂无图片</div>
        </a-tab-pane>
        <a-tab-pane key="2" tab="破碎前">
          <img
            v-if="psqPic !== '暂无图片'"
            style="height: 450px; width: 550px; margin: 5px 10px 5px 10px; float: left"
            :src="psqPic"
            alt=""
          />
          <div v-else>暂无图片</div>
        </a-tab-pane>
        <a-tab-pane key="3" tab="破碎后">
          <img
            v-if="pshPic !== '暂无图片'"
            style="height: 450px; width: 550px; margin: 5px 10px 5px 10px; float: left"
            :src="pshPic"
            alt=""
          />
          <div v-else>暂无图片</div>
        </a-tab-pane>
        <a-tab-pane key="4" tab="视频">
          <video
            v-if="VideoPic !== '暂无视频'"
            autoplay
            controls
            width="100%"
            height="500"
            id="myVideo"
            :src="VideoPic"
          ></video>
          <div v-else>暂无视频</div>
        </a-tab-pane>
      </a-tabs>
    </a-spin>
  </j-modal>
</template>

<script>
import { httpAction, getAction } from "@/api/manage";
import JFormContainer from "@/components/jeecg/JFormContainer";

export default {
  name: "showPic",
  components: {
    JFormContainer,
  },
  props: {},
  data() {
    return {
      title: "",
      visible: false,
      model: {},
      facePic: "",
      psqPic: "",
      pshPic: "",
      VideoPic: "",
      labelCol: {
        xs: { span: 24 },
        sm: { span: 5 },
      },
      wrapperCol: {
        xs: { span: 24 },
        sm: { span: 16 },
      },
      confirmLoading: false,
      validatorRules: {},
      url: {
        list: "/syj/tSyjzb/yljPsPic",
      },
    };
  },
  computed: {},
  created() {},
  methods: {
    handleCancel() {
      this.close();
    },
    close() {
      this.$emit("close");
      this.visible = false;
    },
    edit(record) {
      this.visible = true;
      this.$nextTick(() => {
        let params = { id: record.rowId };
        getAction(this.url.list, params).then((res) => {
          if (res.success) {
            let data = res.result;
            this.facePic = data.facePic;
            this.psqPic = data.psqPic;
            this.pshPic = data.pshPic;
            this.VideoPic = data.VideoPic;
          }
        });
      });
    },
  },
};
</script>
