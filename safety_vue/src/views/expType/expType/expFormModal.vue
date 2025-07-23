<template>
  <div>
    <a-modal v-model:visible="visible" title="粗集料试验检测报告（水泥混凝土用）" width="1000">
      <JB010507></JB010507>
    </a-modal>
  </div>

</template>

<script>
import {getAction, postAction} from "@api/manage";
import expForm from "./expForm";
import JB010501 from "@/views/expType/expForms/JB010501";
import JJ0505 from "@/views/expType/expForms/JJ0505";
import JJ0507 from "@/views/expType/expForms/JJ0507";
import JJ0509 from "@/views/expType/expForms/JJ0509";
import JB010507 from "@/views/expType/expForms/JB010507";

export default {
  name: 'expFormModal',
  components:{
    expForm,
    JB010501,
    JJ0505,
    JJ0507,
    JJ0509,
    JB010507
  },
  mounted() {
    this.getVisible()
  },
  data() {
    return {
      visible: false
    }
  },
  methods: {
    getVisible(){
      this.$bus.$on('open',(visible,data)=>{
        console.log('expFormModal接收到的数据',data)
        this.visible = visible
      })
    }
  }
};
</script>
<style scoped>
</style>
