<template>
  <div>
    <form id="form" action="" method="post" onsubmit="return false;">
      <div class="paperA4">
        <div class="tableHeader"></div>
        <div class="tableContent">
          <table class="layui-table">
            <tr>
              <td>样品名称</td>
              <td><input type="text" id="sampleName" name="sampleName"></td>
              <td>试验日期</td>
              <td><input type="text" id="shiyanriqi" name="shiyanriqi"></td>
            </tr>
            <tr>
              <td>样品来源</td>
              <td><textarea id="sampleQuYangDiDian" name="sampleQuYangDiDian"></textarea></td>
              <td style="height: 70px">试验用途</td>
              <td><textarea id="sampleGcbw" name="sampleGcbw"></textarea></td>
            </tr>
            <tr>
              <td>搅拌方式</td>
              <td><input type="text" name="banhfs" id="banhfs"/></td>
              <td>强度等级</td>
              <td><input type="text" name="hunntsjqddj" id="hunntsjqddj"/></td>
            </tr>
          </table>
          <table class="layui-table next">
            <tr>
              <td width="20%">材料名称</td>
              <td width="20%">规格</td>
              <td>生产厂家/产地</td>
              <td>每立方米用量(kg)</td>
              <td>单位比</td>
            </tr>
            <tr>
              <td><input type="text" name="clmc01" id="clmc01"/></td>
              <td><input type="text" name="guige01" id="guige01"/></td>
              <td><input type="text" name="sccj01" id="sccj01"/></td>
              <td><input type="text" name="mlfmyl01" id="mlfmyl01"/></td>
              <td><input type="text" name="dwb01" id="dwb01"/></td>
            </tr>
            <tr>
              <td><input type="text" name="clmc02" id="clmc02"/></td>
              <td><input type="text" name="guige02" id="guige02"/></td>
              <td><input type="text" name="sccj02" id="sccj02"/></td>
              <td><input type="text" name="mlfmyl02" id="mlfmyl02"/></td>
              <td><input type="text" name="dwb02" id="dwb02"/></td>
            </tr>
            <tr>
              <td><input type="text" name="clmc03" id="clmc03"/></td>
              <td><input type="text" name="guige03" id="guige03"/></td>
              <td><input type="text" name="sccj03" id="sccj03"/></td>
              <td><input type="text" name="mlfmyl03" id="mlfmyl03"/></td>
              <td><input type="text" name="dwb03" id="dwb03"/></td>
            </tr>
            <tr>
              <td><input type="text" name="clmc04" id="clmc04"/></td>
              <td><input type="text" name="guige04" id="guige04"/></td>
              <td><input type="text" name="sccj04" id="sccj04"/></td>
              <td><input type="text" name="mlfmyl04" id="mlfmyl04"/></td>
              <td><input type="text" name="dwb04" id="dwb04"/></td>
            </tr>
            <tr>
              <td><input type="text" name="clmc05" id="clmc05"/></td>
              <td><input type="text" name="guige05" id="guige05"/></td>
              <td><input type="text" name="sccj05" id="sccj05"/></td>
              <td><input type="text" name="mlfmyl05" id="mlfmyl05"/></td>
              <td><input type="text" name="dwb05" id="dwb05"/></td>
            </tr>
            <tr>
              <td><input type="text" name="clmc06" id="clmc06"/></td>
              <td><input type="text" name="guige06" id="guige06"/></td>
              <td><input type="text" name="sccj06" id="sccj06"/></td>
              <td><input type="text" name="mlfmyl06" id="mlfmyl06"/></td>
              <td><input type="text" name="dwb06" id="dwb06"/></td>
            </tr>
            <tr>
              <td><input type="text" name="clmc07" id="clmc07"/></td>
              <td><input type="text" name="guige07" id="guige07"/></td>
              <td><input type="text" name="sccj07" id="sccj07"/></td>
              <td><input type="text" name="mlfmyl07" id="mlfmyl07"/></td>
              <td><input type="text" name="dwb07" id="dwb07"/></td>
            </tr>
            <tr>
              <td><input type="text" name="clmc08" id="clmc08"/></td>
              <td><input type="text" name="guige08" id="guige08"/></td>
              <td><input type="text" name="sccj08" id="sccj08"/></td>
              <td><input type="text" name="mlfmyl08" id="mlfmyl08"/></td>
              <td><input type="text" name="dwb08" id="dwb08"/></td>
            </tr>
            <tr>
              <td><input type="text" name="clmc09" id="clmc09"/></td>
              <td><input type="text" name="guige09" id="guige09"/></td>
              <td><input type="text" name="sccj09" id="sccj09"/></td>
              <td><input type="text" name="mlfmyl09" id="mlfmyl09"/></td>
              <td><input type="text" name="dwb09" id="dwb09"/></td>
            </tr>
            <tr>
              <td><input type="text" name="clmc10" id="clmc10"/></td>
              <td><input type="text" name="guige10" id="guige10"/></td>
              <td><input type="text" name="sccj10" id="sccj10"/></td>
              <td><input type="text" name="mlfmyl10" id="mlfmyl10"/></td>
              <td><input type="text" name="dwb10" id="dwb10"/></td>
            </tr>

          </table>
          <table class="layui-table next">
            <tr>
              <td width="5%">序号</td>
              <td colspan="3" width="35%">检测项目</td>
              <td>技术指标</td>
              <td>检测结果</td>
              <td>结果判定</td>
            </tr>
            <tr>
              <td rowspan="6">1</td>
              <td rowspan="6">稠度试验</td>
              <td rowspan="5">坍落度仪法</td>
              <td>坍落测定值(mm)</td>
              <td><input type="text" name="tldzb" id="tldzb"/></td>
              <td><input type="text" name="pingjunzhi1" id="pingjunzhi1" readonly="readonly"/></td>
              <td><input type="text" name="tldjg" id="tldjg"/></td>
            </tr>
            <tr>
              <td>黏聚性</td>
              <td><input type="text" name="njxzb" id="njxzb"/></td>
              <td><input type="text" name="nianjuxing1" id="nianjuxing1" readonly="readonly"/></td>
              <td><input type="text" name="njxjg" id="njxjg"/></td>
            </tr>
            <tr>
              <td>保水性</td>
              <td><input type="text" name="bsxzb" id="bsxzb"/></td>
              <td><input type="text" name="baoshuixing1" id="baoshuixing1" readonly="readonly"/></td>
              <td><input type="text" name="bsxjg" id="bsxjg"/></td>
            </tr>
            <tr>
              <td>棍度</td>
              <td><input type="text" name="gdzb" id="gdzb"/></td>
              <td><input type="text" name="gundu1" id="gundu1" readonly="readonly"/></td>
              <td><input type="text" name="gdjg" id="gdjg"/></td>
            </tr>
            <tr>
              <td>含砂情况</td>
              <td><input type="text" name="hsqkzb" id="hsqkzb"/></td>
              <td><input type="text" name="hanshaqingk1" id="hanshaqingk1" readonly="readonly"/></td>
              <td><input type="text" name="hsqkjg" id="hsqkjg"/></td>
            </tr>
            <tr>
              <td>维勃仪法</td>
              <td>维勃时间(s)</td>
              <td><input type="text" name="wbsjzb" id="wbsjzb"/></td>
              <td><input type="text" name="weibsjpingjz" id="weibsjpingjz" readonly="readonly"/></td>
              <td><input type="text" name="wbsjjg" id="wbsjjg"/></td>
            </tr>
            <tr>
              <td>2</td>
              <td colspan="3">表观密度试验(kg/m³)</td>
              <td><input type="text" name="bgmdzb" id="bgmdzb"/></td>
              <td><input type="text" name="biaogmdcedz" id="biaogmdcedz" readonly="readonly"/></td>
              <td><input type="text" name="bgmdjg" id="bgmdjg"/></td>
            </tr>
            <tr>
              <td>3</td>
              <td colspan="3">混凝土拌合物含气量(%)</td>
              <td><input type="text" name="hqlzb" id="hqlzb"/></td>
              <td><input type="text" name="hanql" id="hanql"/></td>
              <td><input type="text" name="hqljg" id="hqljg"/></td>
            </tr>
            <tr>
              <td rowspan="2">4</td>
              <td rowspan="2" colspan="2">凝结时间</td>
              <td>初凝(h:min)</td>
              <td><input type="text" name="cnzb" id="cnzb"/></td>
              <td><input type="text" name="pingjchunsj" id="pingjchunsj"/></td>
              <td><input type="text" name="cnjg" id="cnjg"/></td>
            </tr>
            <tr>
              <td>终凝(h:min)</td>
              <td><input type="text" name="znzb" id="znzb"/></td>
              <td><input type="text" name="pingjzhongnsj" id="pingjzhongnsj"/></td>
              <td><input type="text" name="znjg" id="znjg"/></td>
            </tr>
            <tr>
              <td>5</td>
              <td colspan="3">泌水率(%)</td>
              <td><input type="text" name="mslzb" id="mslzb"/></td>
              <td><input type="text" name="misl24xs1" id="misl24xs1"/></td>
              <td><input type="text" name="msljg" id="msljg"/></td>
            </tr>
            <tr>
              <td>6</td>
              <td colspan="3">钢纤维体积率(%)</td>
              <td><input type="text" name="gxwtjzb" id="gxwtjzb"/></td>
              <td><input type="text" name="gxwtjl" id="gxwtjl"/></td>
              <td><input type="text" name="gxwtjjg" id="gxwtjjg"/></td>
            </tr>
          </table>
        </div>
        <div class="tableRemark"></div>
      </div>
    </form>
  </div>

</template>

<script>
import {getAction, postAction} from "@api/manage";

export default {
  name: 'JB010501',
  data() {
    return {
    }
  },
  methods: {
  }
};
</script>
<style scoped>
</style>
