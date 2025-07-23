<template>
  <view class="cu-form-group" style="z-index: 10">
    <view class="flex align-center">
      <view class="title" v-if="required">
        <text class="text-red" v-if="required">*</text>
        <text space="ensp">{{ label }}</text>
      </view>
      <u--input
        :placeholder="placeholder"
        name="input"
        v-model="selectedValue"
        style="width: 100%"
      ></u--input>
      <view>
        <picker
          @change="PickerChange"
          :value="index"
          :range-key="rangeKey"
          :range="range"
        >
          <u-icon class="arrow-down" name="arrow-down" size="18"></u-icon>
        </picker>
      </view>
    </view>
  </view>
</template>

<script>
export default {
  name: "MySelect",
  props: {
    dictCode: String,
    value: String,
    label: String,
    rangeKey: { type: String, default: "label" },
    valueKey: { type: String, default: "value" },
    range: Array,
    searchUrl: String,
    selectedInput: String,
    placeholder: String,
    required: {
      type: Boolean,
      default: false,
      required: false,
    },
  },
  watch: {
    dictCode: {
      immediate: true,
      handler() {
        this.initDictData();
      },
    },
    selectedValue: {
      // immediate: true,
      handler() {
        this.$emit(`change`, this.selectedValue);
      },
    },
  },
  computed: {},
  data() {
    return {
      dictOptions: [],
      index: -1,
      selectedValue: this.value,
    };
  },
  methods: {
    initDictData() {
      //根据字典Code, 初始化字典数组
      if (this.searchUrl) {
        this.$http.get(this.searchUrl, { code: this.dictCode }).then((res) => {
          if (res.data.success) {
            this.dictOptions = res;
            this.getIndex();
          }
        });
      } else {
        let code = this.dictCode;
        this.$http.get(`/sys/dict/getDictItems/${code}`).then((res) => {
          if (res.data.success) {
            this.dictOptions = res.data.result;
            this.getIndex();
          }
        });
      }
    },
    // PickerChange1() {
    // 	// this.index=e.detail.value
    // 	// this.selectedInput = this.rangeKey[this.index]
    //     // this.selectedValue = this.range[this.index]
    //     this.$emit(`change`,this.selectedValue)
    // 	// this.cpdata = this.Carnumbers
    // 	// if(this.index==-1){
    // 	// 	this.index=0
    // 	// 	this.$emit('input',this.dictOptions[0][this.valueKey])
    // 	// }else{
    // 	// 	this.$emit('input', this.dictOptions[this.index][this.valueKey]);
    // 	// }
    // },
    PickerChange(e) {
      this.index = e.detail.value;
      // this.selectedInput = this.rangeKey[this.index]
      this.selectedValue = this.range[this.index];
      // this.$emit(`change`,this.selectedValue)
      // this.cpdata = this.Carnumbers
      // if(this.index==-1){
      // 	this.index=0
      // 	this.$emit('input',this.dictOptions[0][this.valueKey])
      // }else{
      // 	this.$emit('input', this.dictOptions[this.index][this.valueKey]);
      // }
    },
    getIndex() {
      for (var i = 0; i < this.dictOptions.length; i++) {
        if (this.dictOptions[i].value == this.value) {
          this.index = i;
          return;
        }
      }
      this.index = -1;
    },
  },
};
</script>

<style scoped>
.cu-form-group {
  /* background-color: #b4d655; */
  padding: 0;
  position: relative;
}
.cu-form-group uni-picker {
  width: 5px;
  height: 100%;
  position: absolute;
  top: 0;
  right: 3px;
  display: flex;
  align-items: center;
  padding-left: 5px;
  /* justify-content: center; */
  /* background: #c57878; */
}
.uni-input-input {
  background-color: #62cfe2;
}
.cu-form-group uni-picker::after {
  /* font-family: cuIcon;
  display: block;
  content: "\e6a3"; */
  position: absolute;
  /* font-size: 14px;
  color: #ffffff;
  line-height: 42px; */
  width: 0px;
  /* text-align: center;
  top: 0;
  bottom: 0;
  right: -8px;
  margin: auto; */
  background-color: #e97878;
}
</style>
