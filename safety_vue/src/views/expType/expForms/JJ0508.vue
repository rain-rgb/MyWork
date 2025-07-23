<template>
  <div>
    <form id="form" action="" method="post" onsubmit="return false;" class="">
      <div class="tableHeader"></div>
      <div class="tableContent">
        <table class="layui-table">
          <tr>
            <td>桩号及部位</td>
            <td><input type="text" name="zhuanghaobw" id="zhuanghaobw" /></td>
            <td>试验用途</td>
            <td><input type="text" name="sampleGcbw" id="sampleGcbw" /></td>
          </tr>
          <tr>
            <td>制件日期</td>
            <td><input type="text" name="zhijianrq" id="zhijianrq" /></td>
            <td>试验日期</td>
            <td><input type="text" name="shiyanriqi" id="shiyanriqi" /></td>
          </tr>
          <tr>
            <td>设计强度</td>
            <td><input type="text" name="shejiqd" id="shejiqd" /></td>
            <td>抗渗标号</td>
            <td><input type="text" name="kangshenbh" id="kangshenbh" /></td>
          </tr>
        </table>
        <table class="layui-table next">
          <tr>
            <td colspan="5" width="40%">拌和方式
              <select id="banhfs" name="banhfs" BG="" style="width: 100px">
                <option value=""></option>
                <option value="机械拌和">机械拌和</option>
                <option value="人工拌和">人工拌和</option>
              </select></td>

            <td colspan="4" width="30%">振实方式
              <select id="zhensfs" name="zhensfs" BG="" style="width: 100px">
                <option value=""></option>
                <option value="机械振实">机械振实</option>
                <option value="插捣">插捣</option>
              </select>
            </td>

            <td colspan="4" width="30%">养护方式
              <select id="yanghfs" name="yanghfs" BG="" style="width: 100px">
                <option value=""></option>
                <option value="标养">标养</option>
                <option value="自然养护">自然养护</option>
              </select>
            </td>
          </tr>

          <tr>
            <td colspan="2">抗渗等级</td>
            <td colspan="1"><input type="text" id="shicksdj" name="shicksdj" readonly="readonly" BG=""></td>
            <td colspan="4">设计抗渗等级
              <select id="shejksbh" name="shejksbh" BG="" style="width: 100px; height: 20px;">
                <option value=""></option>
                <option value="P2">P2</option>
                <option value="P3">P3</option>
                <option value="P4">P4</option>
                <option value="P6">P6</option>
                <option value="P8">P8</option>
                <option value="P10">P10</option>
                <option value="P12">P12</option>
              </select>
            </td>
            <!--<td colspan="6" style="text-align: right">成型日期<input type="text" id="chengxrq" name="chengxrq" BG=""></td>-->
            <td colspan="3">
              成型日期</td>
            <td colspan="3"><input type="text" id="chengxrq" name="chengxrq" BG=""></td>
          </tr>

          <tr>
            <td width="7%">试验</br>日期</td>
            <td><input type="text" id="syrq1" name="syrq1" BG=""></td>
            <td><input type="text" id="syrq2" name="syrq2" BG=""></td>
            <td><input type="text" id="syrq3" name="syrq3" BG=""></td>
            <td><input type="text" id="syrq4" name="syrq4" BG=""></td>
            <td><input type="text" id="syrq5" name="syrq5" BG=""></td>
            <td><input type="text" id="syrq6" name="syrq6" BG=""></td>
            <td><input type="text" id="syrq7" name="syrq7" BG=""></td>
            <td><input type="text" id="syrq8" name="syrq8" BG=""></td>
            <td><input type="text" id="syrq9" name="syrq9" BG=""></td>
            <td><input type="text" id="syrq10" name="syrq10" BG=""></td>
            <td><input type="text" id="syrq11" name="syrq11" BG=""></td>
            <td><input type="text" id="syrq12" name="syrq12" BG=""></td>
          </tr>

          <tr>
            <td width="7%">试验</br>时间</td>
            <td>8:00</td>
            <td>16:00</td>
            <td>00:00</td>
            <td>8:00</td>
            <td>16:00</td>
            <td>00:00</td>
            <td>8:00</td>
            <td>16:00</td>
            <td>00:00</td>
            <td>8:00</td>
            <td>16:00</td>
            <td>00:00</td>
          </tr>

          <tr>
            <td>水压(MPa)</td>
            <td>0.1</td>
            <td>0.2</td>
            <td>0.3</td>
            <td>0.4</td>
            <td>0.5</td>
            <td>0.6</td>
            <td>0.7</td>
            <td>0.8</td>
            <td>0.9</td>
            <td>1.0</td>
            <td>1.1</td>
            <td>1.2</td>
          </tr>

          <tr>
            <td>1</td>
            <td><input type="text" id="shuiya11" name="shuiya11" BG="" onblur="js_ksdj()"></td>
            <td><input type="text" id="shuiya12" name="shuiya12" BG="" onblur="js_ksdj()"></td>
            <td><input type="text" id="shuiya13" name="shuiya13" BG="" onblur="js_ksdj()"></td>
            <td><input type="text" id="shuiya14" name="shuiya14" BG="" onblur="js_ksdj()"></td>
            <td><input type="text" id="shuiya15" name="shuiya15" BG="" onblur="js_ksdj()"></td>
            <td><input type="text" id="shuiya16" name="shuiya16" BG="" onblur="js_ksdj()"></td>
            <td><input type="text" id="shuiya17" name="shuiya17" BG="" onblur="js_ksdj()"></td>
            <td><input type="text" id="shuiya18" name="shuiya18" BG="" onblur="js_ksdj()"></td>
            <td><input type="text" id="shuiya19" name="shuiya19" BG="" onblur="js_ksdj()"></td>
            <td><input type="text" id="shuiya110" name="shuiya110" BG="" onblur="js_ksdj()"></td>
            <td><input type="text" id="shuiya111" name="shuiya111" BG="" onblur="js_ksdj()"></td>
            <td><input type="text" id="shuiya112" name="shuiya112" BG="" onblur="js_ksdj()"></td>
          </tr>

          <tr>
            <td>2</td>
            <td><input type="text" id="shuiya21" name="shuiya21" BG="" onblur="js_ksdj()"></td>
            <td><input type="text" id="shuiya22" name="shuiya22" BG="" onblur="js_ksdj()"></td>
            <td><input type="text" id="shuiya23" name="shuiya23" BG="" onblur="js_ksdj()"></td>
            <td><input type="text" id="shuiya24" name="shuiya24" BG="" onblur="js_ksdj()"></td>
            <td><input type="text" id="shuiya25" name="shuiya25" BG="" onblur="js_ksdj()"></td>
            <td><input type="text" id="shuiya26" name="shuiya26" BG="" onblur="js_ksdj()"></td>
            <td><input type="text" id="shuiya27" name="shuiya27" BG="" onblur="js_ksdj()"></td>
            <td><input type="text" id="shuiya28" name="shuiya28" BG="" onblur="js_ksdj()"></td>
            <td><input type="text" id="shuiya29" name="shuiya29" BG="" onblur="js_ksdj()"></td>
            <td><input type="text" id="shuiya210" name="shuiya210" BG="" onblur="js_ksdj()"></td>
            <td><input type="text" id="shuiya211" name="shuiya211" BG="" onblur="js_ksdj()"></td>
            <td><input type="text" id="shuiya212" name="shuiya212" BG="" onblur="js_ksdj()"></td>
          </tr>

          <tr>
            <td>3</td>
            <td><input type="text" id="shuiya31" name="shuiya31" BG="" onblur="js_ksdj()"></td>
            <td><input type="text" id="shuiya32" name="shuiya32" BG="" onblur="js_ksdj()"></td>
            <td><input type="text" id="shuiya33" name="shuiya33" BG="" onblur="js_ksdj()"></td>
            <td><input type="text" id="shuiya34" name="shuiya34" BG="" onblur="js_ksdj()"></td>
            <td><input type="text" id="shuiya35" name="shuiya35" BG="" onblur="js_ksdj()"></td>
            <td><input type="text" id="shuiya36" name="shuiya36" BG="" onblur="js_ksdj()"></td>
            <td><input type="text" id="shuiya37" name="shuiya37" BG="" onblur="js_ksdj()"></td>
            <td><input type="text" id="shuiya38" name="shuiya38" BG="" onblur="js_ksdj()"></td>
            <td><input type="text" id="shuiya39" name="shuiya39" BG="" onblur="js_ksdj()"></td>
            <td><input type="text" id="shuiya310" name="shuiya310" BG="" onblur="js_ksdj()"></td>
            <td><input type="text" id="shuiya311" name="shuiya311" BG="" onblur="js_ksdj()"></td>
            <td><input type="text" id="shuiya312" name="shuiya312" BG="" onblur="js_ksdj()"></td>
          </tr>

          <tr>
            <td>4</td>
            <td><input type="text" id="shuiya41" name="shuiya41" BG="" onblur="js_ksdj()"></td>
            <td><input type="text" id="shuiya42" name="shuiya42" BG="" onblur="js_ksdj()"></td>
            <td><input type="text" id="shuiya43" name="shuiya43" BG="" onblur="js_ksdj()"></td>
            <td><input type="text" id="shuiya44" name="shuiya44" BG="" onblur="js_ksdj()"></td>
            <td><input type="text" id="shuiya45" name="shuiya45" BG="" onblur="js_ksdj()"></td>
            <td><input type="text" id="shuiya46" name="shuiya46" BG="" onblur="js_ksdj()"></td>
            <td><input type="text" id="shuiya47" name="shuiya47" BG="" onblur="js_ksdj()"></td>
            <td><input type="text" id="shuiya48" name="shuiya48" BG="" onblur="js_ksdj()"></td>
            <td><input type="text" id="shuiya49" name="shuiya49" BG="" onblur="js_ksdj()"></td>
            <td><input type="text" id="shuiya410" name="shuiya410" BG="" onblur="js_ksdj()"></td>
            <td><input type="text" id="shuiya411" name="shuiya411" BG="" onblur="js_ksdj()"></td>
            <td><input type="text" id="shuiya412" name="shuiya412" BG="" onblur="js_ksdj()"></td>
          </tr>

          <tr>
            <td>5</td>
            <td><input type="text" id="shuiya51" name="shuiya51" BG="" onblur="js_ksdj()"></td>
            <td><input type="text" id="shuiya52" name="shuiya52" BG="" onblur="js_ksdj()"></td>
            <td><input type="text" id="shuiya53" name="shuiya53" BG="" onblur="js_ksdj()"></td>
            <td><input type="text" id="shuiya54" name="shuiya54" BG="" onblur="js_ksdj()"></td>
            <td><input type="text" id="shuiya55" name="shuiya55" BG="" onblur="js_ksdj()"></td>
            <td><input type="text" id="shuiya56" name="shuiya56" BG="" onblur="js_ksdj()"></td>
            <td><input type="text" id="shuiya57" name="shuiya57" BG="" onblur="js_ksdj()"></td>
            <td><input type="text" id="shuiya58" name="shuiya58" BG="" onblur="js_ksdj()"></td>
            <td><input type="text" id="shuiya59" name="shuiya59" BG="" onblur="js_ksdj()"></td>
            <td><input type="text" id="shuiya510" name="shuiya510" BG="" onblur="js_ksdj()"></td>
            <td><input type="text" id="shuiya511" name="shuiya511" BG="" onblur="js_ksdj()"></td>
            <td><input type="text" id="shuiya512" name="shuiya512" BG="" onblur="js_ksdj()"></td>
          </tr>

          <tr>
            <td>6</td>
            <td><input type="text" id="shuiya61" name="shuiya61" BG="" onblur="js_ksdj()"></td>
            <td><input type="text" id="shuiya62" name="shuiya62" BG="" onblur="js_ksdj()"></td>
            <td><input type="text" id="shuiya63" name="shuiya63" BG="" onblur="js_ksdj()"></td>
            <td><input type="text" id="shuiya64" name="shuiya64" BG="" onblur="js_ksdj()"></td>
            <td><input type="text" id="shuiya65" name="shuiya65" BG="" onblur="js_ksdj()"></td>
            <td><input type="text" id="shuiya66" name="shuiya66" BG="" onblur="js_ksdj()"></td>
            <td><input type="text" id="shuiya67" name="shuiya67" BG="" onblur="js_ksdj()"></td>
            <td><input type="text" id="shuiya68" name="shuiya68" BG="" onblur="js_ksdj()"></td>
            <td><input type="text" id="shuiya69" name="shuiya69" BG="" onblur="js_ksdj()"></td>
            <td><input type="text" id="shuiya610" name="shuiya610" BG="" onblur="js_ksdj()"></td>
            <td><input type="text" id="shuiya611" name="shuiya611" BG="" onblur="js_ksdj()"></td>
            <td><input type="text" id="shuiya612" name="shuiya612" BG="" onblur="js_ksdj()"></td>
          </tr>

          <tr>
            <td colspan="6">六个试件中三个渗水时的水压力(MPa)</td>
            <td colspan="7"><input type="text" id="liugsjzsgsssdsyl" name="liugsjzsgsssdsyl" readonly="readonly" BG="">
            </td>
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
  name: 'JJ0508',
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
