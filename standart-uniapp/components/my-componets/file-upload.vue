<template>
  <view class="margin-top">
    <view class="uni-file-box">
      <uni-file-picker
      ref="files"
      limit="9"
      v-model="imgIdList"
      :auto-upload="false"
      file-mediatype="all"
      @select="onFileChange"
      @change="handleChange"
      :files-list="imgIdList"
    >
      <u-button :customStyle="btnGetCode" type="primary" @click="selectFile" v-if="upload">{{ label }}</u-button>
    </uni-file-picker>
    </view>
    <!-- <view v-for="(record, index) in fileList" :key="index">
      <text v-if="record.name">{{ record.name }} </text>
      <text v-else-if="record.originalFilename">{{ record.originalFilename }} </text>
      <text v-else>{{ record.fjmc }} </text>
    </view> -->
    <view class="list">
      <view class="list-item">
        <view class="list-item-con">
          <view class="tablelist">
            <uni-table stripe emptyText="暂无更多数据">
              <!-- 表头行 -->
              <uni-tr>
                <uni-th width="60" align="center">序号</uni-th>
                <uni-th width="130" align="center">文件名称</uni-th>
                <uni-th width="120" align="center">操作</uni-th>
              </uni-tr>
              <!-- 表格数据行 -->
              <uni-tr v-for="(tablelist, index) in fileList" :key="index">
                <uni-td align="center">{{ index + 1 }}</uni-td>
                <uni-td align="center">
                  <text v-if="tablelist.name">{{ tablelist.name }} </text>
                  <text v-else-if="tablelist.originalFilename"
                    >{{ tablelist.originalFilename }}
                  </text>
                  <text v-else>{{ tablelist.fjmc }} </text>
                </uni-td>
                <uni-td align="center">
                  <text class="text-blue margin-right-xs" @click="downLoadF(tablelist)"
                    >下载</text
                  >
                  <text
                    class="text-blue margin-right-xs"
                    @click="downloadFilePreview(tablelist)"
                    >预览</text
                  >
                  <text class="text-blue" @click="ossDelete(tablelist)" v-if="upload"
                    >删除</text
                  >
                </uni-td>
              </uni-tr>
            </uni-table>
          </view>
        </view>
      </view>
    </view>
  </view>
</template>

<script>
export default {
  name: "FileUpload",
  props: {
    label: {
      type: String,
      default: "图片上传",
    },
    //最多可以选择的图片张数
    maxImg: {
      type: Number,
      default: 4,
    },
    //默认图片地址
    uploadFile: {
      type: Array,
      default: () => [],
    },
    //默认图片id
    uploadFileId: {
      type: Array,
      default: () => [],
    },
    //album 从相册选图，camera 使用相机，默认二者都有。如需直接开相机或直接选相册，请只使用一个选项
    sourceType: {
      type: Array,
      default: () => ["album", "camera"],
    },
    upload: {
      type: Boolean,
      default: true,
    },
    fileData: {
      type: Array,
      default: () => [],
    },
  },
  data() {
    return {
      uploadUrl: "/sy/syFileDirectory/uploadFileWjml",
      imgList: [],
      imgIdList: [],
      fileList: [],
      btnGetCode: {
        borderRadius: '7px',
        height: '30px', //驼峰命名注意----这里如果不规范，会整体失效,px，rpx作为单位都可以,一定要有引号
      }
    };
  },
  watch: {
    fileList(val) {
      if (Array.isArray(val) && this.fileList.length > 0) {
        this.$emit(`changeFileList`, this.fileList);
      }
    },
    fileData(val) {
      if (Array.isArray(val) && this.fileData.length > 0) {
        this.fileList = this.fileData;
      }
    },
  },
  methods: {
    handleChange(files) {
      //   this.fileList = files;
    },
    onFileChange(e) {
      //   let e = e;
      this.$refs.files.clearFiles(); //调用组件自身清空数据的方法解决组件自带的回显和自定义回显同时显示的问题
      //   this.fileList.push(e.tempFiles[0]);
      console.log(e, "文件上传------------------");
      //   let fullPath = e.tempFilePaths; //图片集合
      //   let uploadFiles = fullPath.map((item) => {
      //     return item.substring(item.lastIndexOf(".") + 1).toLowerCase(); //文件后缀小写
      //   });
      //   let typeInfo = ["pdf"];
      //   // 过滤出 uploadFiles 中在 typeInfo 中没有出现过的值
      //   const notFound = uploadFiles.filter((item) => !typeInfo.includes(item));
      // const files = e.detail.files; // 获取选中的文件列表
      // if (files.length > 0) {
      //   // 假设我们只上传第一个文件
      //   const file = files[0];
      //   this.uploadFile(file);
      // }
      // let filePath = this.replaceFileNameInPath(e.tempFilePaths[0], e.tempFiles[0].name);
      // e.tempFiles[0].file.path = filePath;
      // e.tempFiles[0].path = filePath;
      // e.tempFiles[0].url = filePath;
      // e.tempFilePaths[0] = filePath;
      // console.log(filePath, "filePath---------=======------------");
      // console.log(e.tempFiles[0].file.path, "e.tempFiles[0].file.path---------=======------------");

      uni.uploadFile({
        url: this.$config.apiUrl + this.uploadUrl,
        // filePath,
        filePath: e.tempFilePaths[0],
        name: "file",
        Headers: {
          "X-Access-Token": uni.getStorageSync("Access-Token"),
        },
        formData: {
          token: uni.getStorageSync("Access-Token"),
          file: e.tempFiles[0].file,
          apptype:"app",
          filename: e.tempFiles[0].name,
          form: "wjml",
          fileType: "2",
        },
        success: (res) => {
          let jsonObject = JSON.parse(res.data);
          let result = jsonObject.result;
          result.name = e.tempFiles[0].name;
          // result.fjyllj = e.tempFilePaths[0];
          result.extname = e.tempFiles[0].extname;

          this.fileList.push(result);
          console.log("res.data:", jsonObject, this.fileList);
        },
      });

      //   let paramsAll = {
      //     // #ifdef APP-PLUS || H5
      //     // files:fullPath,
      //     // #endif
      //     // #ifdef MP-ALIPAY
      //     // fileType:"2",
      //     // #endif
      //     filePath: e.tempFilePaths[0],
      //     // filePath,
      //     name: e.tempFiles[0].name,
      //     // #ifdef H5
      //     // file:e.tempFilePaths[0],
      //     // #endif
      //     // header,
      //     formData: {
      //       file: e.tempFiles[0].file,
      //       form: "wjml",
      //       fileType: "2",
      //     },
      //     // custom = {},
      //     params: {
      //     //   formData: {
      //     //     file: e.tempFiles[0].file,
      //     //     form: "wjml",
      //     //     fileType: "2",
      //     //   },
      //     },
      //   };
      //   this.$http
      //     .upload(this.$config.apiUrl + "/sys/common/upload", paramsAll)
      //     // .upload(this.$config.apiUrl + "/sy/syFileDirectory/uploadFileWjml", paramsAll)
      //     .then((res) => {
      //       console.log(res, "upload 文件上传-------------");
      //       // console.log(res.data.result.rows,'res.result.rows');
      //       // if (res.data.code == 200) {
      //       // 	if (res.data.result.rows.length > 0) {
      //       // 		this.listData = [...this.listData, ...res.data.result.rows]
      //       // 		console.log(this.listData,'this.listData');
      //       // 	}
      //       // }
      //     })
      //     .finally(() => {
      //       // this.status = 'nomore'
      //     });
    },
    getFileYlById() {
      let params = {};
      this.$http
        .get("/sy/syFile/getFileYlById", params)
        // .upload(this.$config.apiUrl + "/sy/syFileDirectory/uploadFileWjml", paramsAll)
        .then((res) => {
          console.log(res, "upload 文件上传-------------");
          // console.log(res.data.result.rows,'res.result.rows');
          // if (res.data.code == 200) {
          // 	if (res.data.result.rows.length > 0) {
          // 		this.listData = [...this.listData, ...res.data.result.rows]
          // 		console.log(this.listData,'this.listData');
          // 	}
          // }
        })
        .finally(() => {
          // this.status = 'nomore'
        });
    },
    replaceFileNameInPath(originalPath, newFullFileName) {
      // 找到原始路径中最后一个点号和最后一个斜杠的索引
      const lastDotIndex = originalPath.lastIndexOf(".");
      const lastSlashIndex = originalPath.lastIndexOf("/");

      // 确保找到了点号和斜杠，并且点号在斜杠之后（即文件名内）
      if (lastDotIndex !== -1 && lastSlashIndex !== -1 && lastDotIndex > lastSlashIndex) {
        // 提取路径部分和原始文件的扩展名
        const path = originalPath.substring(0, lastSlashIndex + 1);
        const originalExtension = originalPath.substring(lastDotIndex);

        // 从新文件名中提取不含扩展名的部分
        const newFileNameWithoutExtension = newFullFileName.split(".")[0];

        // 构造新路径，使用新文件名替换原始文件名，并保持原始扩展名不变
        const newPath = `${path}${newFileNameWithoutExtension}${originalExtension}`;

        return newPath;
      } else {
        // 如果没有找到点号、斜杠，或者点号不在斜杠之后，则返回一个错误消息或原始路径（根据需求调整）
        // 这里我们选择返回原始路径，并打印一个警告消息
        console.warn("Invalid path or file name format.");
        return originalPath;
      }
    },
    selectFile() {},
    // downLoadF(){
    //   var that = this;
    //   let id = record.id || record.fjid;
    //   uni.showModal({
    //     title: "确认删除",
    //     content: `是否删除选中文件?`,
    //     success: function (res) {
    //       if (res.confirm) {
    // 		console.log("用户点击确定");
    //         // 用户点击了确定按钮的相关逻辑可以放在这里
    //       } else if (res.cancel) {
    //         console.log("用户点击取消");
    //         // 用户点击了取消按钮的相关逻辑可以放在这里
    //       }
    //     },
    //   });

    // },
    // 下载
    downLoadF(row) {
      //   let url = row.filePath;
      let url = this.$config.unloadUrl + row.fjyllj;
      // url = url.replace(/\/jeecg-boot/g, "");

      //   let extension = url.split(".").pop(); // 获取最后一个"."后面的部分
      //   let fileType = this.typeChange(extension);
      console.log("url", url);
      console.log("点击下载", row.extname);
      //   console.log("点击下载extension", extension);
      wx.showLoading({
        title: "下载中",
      });
      // console.log("下载文件：", fileId);
      wx.downloadFile({
        url: url, //源文件地址
        // filePath: url, //指定的本地文件路径   一定要带文件后缀名  不然无法打开   wx.env.USER_DATA_PATH类似本地文件根目录
        // filePath: wx.env.USER_DATA_PATH + `/${row.filePath}`, //指定的本地文件路径   一定要带文件后缀名  不然无法打开   wx.env.USER_DATA_PATH类似本地文件根目录
        header: {
          "content-type": "application/json",
          "X-Access-Token": uni.getStorageSync("Access-Token"),
          Authorization: uni.getStorageSync("Access-Token"), // 根据需要自行修改
        },
        success(res) {
          console.log(res);
          //   let data = res.filePath;
          let data = res.tempFilePath;
          wx.hideLoading();
          wx.openDocument({
            filePath: data,
            showMenu: true, //表示右上角是否有转发按钮
          });
        },
      });
      //已验证Android,iOS 都可更改
      //使用FileSystemManager.rename()  模拟器 有权限进行写入等操作    Android,iOS 都无权限（已验证）   已放弃
    },
    fail: (res) => {
      wx.hideLoading();
      console.log("下载失败：", res);
      wx.showToast({
        title: res.errMsg,
        icon: "error",
      });
    },
    downloadFilePreview(row) {
      let urlFile = this.$config.unloadUrl + row.fjyllj;

      // urlFile = urlFile.replace(/\/jeecg-boot/g, "");
      console.log(urlFile, "urlFile--------------");

      let urlFileType = urlFile.split(".").pop(); // 获取最后一个"."后面的部分
      wx.showToast({
        title: "打开中…",
        icon: "loading",
        duration: 100000,
      });
      wx.downloadFile({
        url: urlFile, //

        header: {
          "content-type": "application/json",
          "X-Access-Token": uni.getStorageSync("Access-Token"),
          Authorization: uni.getStorageSync("Access-Token"), // 根据需要自行修改
        },
        success: function (res) {
          var Path = res.tempFilePath; // 返回的文件临时地址，用于后面打开本地预览所用
          wx.openDocument({
            filePath: Path,
            fileType: urlFileType, // 指定文件类型
            success: function (res) {
              wx.hideToast();
            },
            fail: function (res) {
              wx.showToast({
                title: "预览失败",
                icon: "loading",
                duration: 1000,
              });
              console.log("打开失败", res);
            },
          });
        },
        fail: function (res) {
          wx.showToast({
            title: "预览失败",
            icon: "loading",
            duration: 1000,
          });
        },
      });
    },
    ossDelete(record) {
      var that = this;
      let id = record.id || record.fjid;
      uni.showModal({
        title: "确认删除",
        content: `是否删除选中文件?`,
        success: function (res) {
          if (res.confirm) {
            that.handleDelete1(id);
            console.log("用户点击确定");
            // 用户点击了确定按钮的相关逻辑可以放在这里
          } else if (res.cancel) {
            console.log("用户点击取消");
            // 用户点击了取消按钮的相关逻辑可以放在这里
          }
        },
      });
    },
    handleDelete1(id) {
      // console.log(id,this.dataSourceSY,'idlllllllllll');
      this.fileList = this.fileList.filter((e) => {
        return e.id !== id && e.fjid !== id;
      });
    },
    typeChange(suffix) {
      let fileTypeMime = "";
      switch (
        suffix // switch获取后缀对应的mime 可参考上文链接
      ) {
        case "png":
          fileTypeMime = "image/png";
          break;
        case "doc":
          fileTypeMime = "application/msword";
          break;
        case "docx":
          fileTypeMime =
            "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
          break;
        case "jpg":
        case "jpeg":
          fileTypeMime = "image/jpeg";
          break;
        case "gif":
          fileTypeMime = "image/gif";
          break;
        case "svg":
          fileTypeMime = "image/svg+xml";
          break;
        case "tif":
        case "tiff":
          fileTypeMime = "image/tiff";
          break;
        case "txt":
          fileTypeMime = "text/plain";
          break;
        case "ppt":
          fileTypeMime = "application/vnd.ms-powerpoint";
          break;
        case "pptx":
          fileTypeMime =
            "application/vnd.openxmlformats-officedocument.presentationml.presentation";
          break;
        case "xls":
          fileTypeMime = "application/vnd.ms-excel";
          break;
        case "xlsx":
          fileTypeMime =
            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
          break;
        case "zip":
          fileTypeMime = "application/zip";
          break;
        case "7z":
          fileTypeMime = "application/x-7z-compressed";
          break;
      }
    },
  },
};
</script>

<style lang="scss" scoped>
::v-deep .uni-file-box{
  position: absolute;
  top: 10upx;
  right: 10upx;
  // width: 50upx !important;
  width: 30%;
}
</style>
