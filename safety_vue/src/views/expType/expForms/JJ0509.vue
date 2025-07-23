<template>
  <div>
    <form id="form" action="" method="post" onsubmit="return false;">
      <div class="tableHeader"></div>
      <div class="tableContent">
        <table class="layui-table">
          <tr>
            <td>样品名称</td>
            <td><input type="text" name="sampleName" id="sampleName"/></td>
            <td>试验日期</td>
            <td><input type="text" name="shiyanriqi" id="shiyanriqi"/></td>
          </tr>
          <tr>
            <td>样品来源</td>
            <td><textarea type="text" name="sampleQuYangDiDian" id="sampleQuYangDiDian"></textarea></td>
            <td>用途</td>
            <td><textarea type="text" name="sampleGcbw" id="sampleGcbw"></textarea></td>
          </tr>
          <tr>
            <td width="18%">强度等级</td>
            <td colspan="3"><input type="text" name="hunntsjqddj" enterIndex='2' id="hunntsjqddj" BG=""/></td>
            <!--					<td colspan="3">取样地点、时间</td>-->
            <!--					<td colspan="2"><input type="text"enterIndex='2'  name="quydidsj" id="quydidsj"/></td>-->
          </tr>
        </table>
        <table class="layui-table next">

          <tr>
            <td colspan="2">混凝土配合比报告编号</td>
            <td><input type="text" name="hunntpeihbbgbh" enterIndex='3' id="hunntpeihbbgbh"/></td>
            <td style="width: 80px">搅拌方式</td>
            <td colspan="2"><input type="text" name="banhfs" enterIndex='4' id="banhfs" BG=""/></td>
            <td style="width: 80px">塌落度(mm)</td>
            <td><input type="text" name="tanld" enterIndex='5' id="tanld"/></td>
          </tr>
          <tr>
            <td colspan="2">试验次数</td>
            <td colspan="3">1</td>
            <td colspan="3">2</td>
          </tr>
          <tr>
            <td colspan="2">试样筒质量(kg)</td>
            <td colspan="3"><input type="text" enterIndex='6' name="shiytzl1" id="shiytzl1"
                                   onchange="xiuyue(this.id,this.value,3);number()"/></td>
            <td colspan="3"><input type="text" enterIndex='7' name="shiytzl2" id="shiytzl2"
                                   onchange="xiuyue(this.id,this.value,3);number2()"/></td>
          </tr>
          <tr>
            <td colspan="2">混凝土+筒质量(kg)</td>
            <td colspan="3"><input type="text" enterIndex='8' name="daoshzshhunnthsytzzl1" id="daoshzshhunnthsytzzl1"
                                   onchange="xiuyue(this.id,this.value,2);number()"/></td>
            <td colspan="3"><input type="text" enterIndex='9' name="daoshzshhunnthsytzzl2" id="daoshzshhunnthsytzzl2"
                                   onchange="xiuyue(this.id,this.value,2);number2()"/></td>
          </tr>
          <tr>
            <td colspan="2">混凝土质量(kg)</td>
            <td colspan="3"><input type="text" name="hunntzl1" BG="" id="hunntzl1" readonly="readonly"/></td>
            <td colspan="3"><input type="text" name="hunntzl2" BG="" id="hunntzl2" readonly="readonly"/></td>
          </tr>
          <tr>
            <td colspan="2">试样筒容积(L)</td>
            <td colspan="6"><input type="text" enterIndex='10' name="shiytrj" id="shiytrj"
                                   onchange="xiuyue(this.id,this.value,3);number();number2()"/></td>
          </tr>
          <tr>
            <td rowspan="2">拌合物表观密度(kg/m³)</td>
            <td>单值</td>
            <td colspan="3"><input type="text" name="biaogmdcz1" readonly="readonly" BG=""
                                   id="biaogmdcz1"/></td>
            <td colspan="3"><input type="text" name="biaogmdcz2" readonly="readonly" BG=""
                                   id="biaogmdcz2"/></td>
          </tr>
          <tr>
            <td style="width:50px">平均值</td>
            <td colspan="6"><input type="text" name="biaogmdcedz" readonly="readonly" BG=""
                                   id="biaogmdcedz"/></td>
          </tr>
        </table>
      </div>
      <div class="tableRemark"></div>
    </form>
  </div>

</template>

<script>
import {getAction, postAction} from "@api/manage";

export default {
  name: 'JJ0509',
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
